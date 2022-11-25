package com.apollo.epos.fragment.summary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentSummaryBinding;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.fragment.summary.adapter.SummaryAdapter;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SummaryFragment extends BaseFragment implements SummaryFragmentCallback {
    private Activity mActivity;
    private FragmentSummaryBinding summaryBinding;
    private String fromDate = CommonUtils.getBeforeSevenDaysDate(); //CommonUtils.getfromDate() + "-01";
    private String toDate = CommonUtils.getCurrentDate();
    private int page = 1;
    List<MyOrdersListResponse.Row> summaryListLoad;
    private SummaryAdapter summaryAdapter;
    private boolean isLoading = false;
    private boolean isLastRecord = false;

    public static SummaryFragment newInstance() {
        return new SummaryFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        summaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false);
        return summaryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity.getInstance().setTitle(R.string.menu_summary);
        summaryBinding.setCallbak(this);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date fromDates = formatter.parse(fromDate);
            long fromDateMills = fromDates.getTime();
            summaryBinding.fromDate.setText(CommonUtils.getDateFormatForSummaryEditText(fromDateMills));

            Date toDates = formatter.parse(toDate);
            long toDateMills = toDates.getTime();
            summaryBinding.toDate.setText(CommonUtils.getDateFormatForSummaryEditText(toDateMills));
            ActivityUtils.showDialog(getContext(), "Please wait.");
            this.page = 1;
            getController().myOrdersListApiCall(page);

        } catch (Exception e) {
            System.out.println("onViewCreated:::::::::::::::::" + e.getMessage());
        }
    }

    private SummaryFragmentController getController() {
        return new SummaryFragmentController(getContext(), this);
    }

    @Override
    public void onLogout() {
        getSessionManager().clearAllSharedPreferences();
        NavigationActivity.getInstance().stopBatteryLevelLocationService();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onSuccessMyOrdersListApi(MyOrdersListResponse myOrdersListResponse) {
        if (page == 1) {
            if (myOrdersListResponse != null) {
                if (myOrdersListResponse.getData() != null
                        && myOrdersListResponse.getData().getListData() != null
                        && myOrdersListResponse.getData().getListData().getRows() != null) {
                    this.summaryListLoad = myOrdersListResponse.getData().getListData().getRows();
                    page++;
                    if (myOrdersListResponse.getData().getListData().getRows() != null && myOrdersListResponse.getData().getListData().getRows().size() > 0) {
//                        summaryAdapter = new SummaryAdapter(getContext(), myOrdersListResponse.getData().getListData().getRows(), null);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        summaryBinding.summaryListRecycler.setLayoutManager(mLayoutManager);
                        summaryBinding.summaryListRecycler.setItemAnimator(new DefaultItemAnimator());
//                        summaryBinding.summaryListRecycler.setAdapter(summaryAdapter);
                        summaryBinding.noOrderFoundLayout.setVisibility(View.GONE);
                        summaryBinding.summaryListRecycler.setVisibility(View.VISIBLE);
                        initAdapter();
                        initScrollListener();
                    } else {
                        summaryBinding.summaryListRecycler.setVisibility(View.GONE);
                        summaryBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            if (myOrdersListResponse.getData() != null
                    && myOrdersListResponse.getData().getListData() != null
                    && myOrdersListResponse.getData().getListData().getRows() != null
                    && myOrdersListResponse.getData().getListData().getRows().size() > 0) {
                page++;
                if (myOrdersListResponse.getData().getListData().getRows().size() < 10) {
                    this.isLastRecord = true;
                }
            } else if (myOrdersListResponse.getData() != null
                    && myOrdersListResponse.getData().getListData() != null
                    && myOrdersListResponse.getData().getListData().getRows() != null
                    && myOrdersListResponse.getData().getListData().getRows().size() == 0) {
                this.isLastRecord = true;
            }
            summaryListLoad.remove(summaryListLoad.size() - 1);
            int scrollPosition = summaryListLoad.size();
            summaryAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            summaryListLoad.addAll(myOrdersListResponse.getData().getListData().getRows());
            summaryAdapter.notifyDataSetChanged();
            isLoading = false;


        }
    }

    private void initAdapter() {
        summaryAdapter = new SummaryAdapter(getContext(), summaryListLoad, null);
        summaryBinding.summaryListRecycler.setAdapter(summaryAdapter);
    }

    private void initScrollListener() {
        summaryBinding.summaryListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == summaryListLoad.size() - 1) {
                        //bottom of list!
                        if (!isLastRecord) {
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        summaryListLoad.add(null);
        summaryAdapter.notifyItemInserted(summaryListLoad.size() - 1);
        getController().myOrdersListApiCall(page);
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFromDate() {
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        int mYear;
        int mMonth;
        int mDay;
        //Objects.requireNonNull(summaryBinding.fromDate).getText().toString().isEmpty()
        if (fromDate.isEmpty()) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String selectedBirthDate = fromDate;
            String[] expDate = selectedBirthDate.split("-");
            mYear = Integer.parseInt(expDate[0]);
            mMonth = Integer.parseInt(expDate[1]) - 1;
            mDay = Integer.parseInt(expDate[2]);
        }
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            String selectedDate;
            c.set(year, monthOfYear, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
//            requiredDOBFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.getTime());
            try {
                summaryBinding.fromDate.setText(CommonUtils.getDateFormatForSummaryEditText(c.getTimeInMillis()));
                this.fromDate = CommonUtils.getSqlDateFormat(c);
                if (CommonUtils.isFromDateBeforeToDate(selectedDate, toDate)) {
                    summaryBinding.dateComparisonErrorText.setVisibility(View.GONE);
                    onClickOk();
                } else {
                    summaryBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, mYear, mMonth, mDay);
        dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)
        dialog.show();
    }

    @Override
    public void onClickToDate() {
        Calendar c = Calendar.getInstance(Locale.ENGLISH);
        int mYear;
        int mMonth;
        int mDay;
        //Objects.requireNonNull(summaryBinding.toDate).getText().toString().isEmpty()
        if (toDate.isEmpty()) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String selectedBirthDate = toDate;
            String[] expDate = selectedBirthDate.split("-");
            mYear = Integer.parseInt(expDate[0]);
            mMonth = Integer.parseInt(expDate[1]) - 1;
            mDay = Integer.parseInt(expDate[2]);
        }
        final DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            String selectedDate;
            c.set(year, monthOfYear, dayOfMonth);
            selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
//            requiredDOBFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.getTime());
            try {
                summaryBinding.toDate.setText(CommonUtils.getDateFormatForSummaryEditText(c.getTimeInMillis()));
                this.toDate = CommonUtils.getSqlDateFormat(c);
                if (CommonUtils.isFromDateBeforeToDate(fromDate, selectedDate)) {
                    summaryBinding.dateComparisonErrorText.setVisibility(View.GONE);
                    onClickOk();
                } else {
                    summaryBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, mYear, mMonth, mDay);
        dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis()));// - (1000 * 60 * 60 * 24 * 365.25 * 18)
        dialog.show();
    }

    @Override
    public void onClickOk() {
        if (CommonUtils.isFromDateBeforeToDate(fromDate, toDate)) {
            summaryBinding.dateComparisonErrorText.setVisibility(View.GONE);
            ActivityUtils.showDialog(getContext(), "Please wait.");
            page = 1;
            this.isLastRecord = false;
            getController().myOrdersListApiCall(page);
        } else {
            summaryBinding.dateComparisonErrorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String fromDate() {
        return fromDate;
    }

    @Override
    public String toDate() {
        return toDate;
    }


}
