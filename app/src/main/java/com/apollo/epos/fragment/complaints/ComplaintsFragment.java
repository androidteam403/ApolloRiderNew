package com.apollo.epos.fragment.complaints;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.BottomSheetBinding;
import com.apollo.epos.databinding.FragmentComplaintsBinding;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.complaints.adapter.ComplaintsListAdapter;
import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsFragment extends BaseFragment implements ComplaintsFragmentCallback {
    private FragmentComplaintsBinding complaintsBinding;
    private int page = 1;
    List<ComplaintsResponse.Row> complaintsListLoad;
    private ComplaintsListAdapter complaintsListAdapter;
    private ComplaintsResponse complaintsResponse;
    private boolean isLoading = false;
    private boolean isLastRecord = false;
    String[] complaintReasons = {"Wrong phone number", "Vehicle registration number is wrong", "Update RC", "Wrong bank account number"};
    List<ComplaintReasonsListResponse.Row> complaintReasonsList = new ArrayList<>();
    private String complaintReason;

    public static ComplaintsFragment newInstance() {
        return new ComplaintsFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        complaintsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_complaints, container, false);
        return complaintsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationActivity.getInstance().setTitle(R.string.menu_complaints);
        setUp();
    }

    private void setUp() {
        complaintsBinding.setCallback(this);
        if (getSessionManager().getComplaintReasonsListResponse() != null) {
            onSuccessComplaintReasonsListApiCall(getSessionManager().getComplaintReasonsListResponse());
        } else {
            getController().getComplaintReasonsListApiCall();
        }
        this.page = 1;
        getController().getComplaintsListApiCall(page);
    }

    public ComplaintsFragmentController getController() {
        return new ComplaintsFragmentController(getContext(), this);
    }

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }

    @Override
    public void onSuccessGetComplaintsListApiCall(ComplaintsResponse complaintsResponse) {
        this.complaintsResponse = complaintsResponse;

        if (page == 1) {
            if (complaintsResponse != null) {
                if (complaintsResponse.getData() != null
                        && complaintsResponse.getData().getListData() != null
                        && complaintsResponse.getData().getListData().getRows() != null) {
                    this.complaintsListLoad = complaintsResponse.getData().getListData().getRows();
                    page++;
                    if (complaintsResponse.getData().getListData().getRows() != null && complaintsResponse.getData().getListData().getRows().size() > 0) {
//                        summaryAdapter = new SummaryAdapter(getContext(), myOrdersListResponse.getData().getListData().getRows(), null);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        complaintsBinding.complaintsRecyclerView.setLayoutManager(mLayoutManager);
                        complaintsBinding.complaintsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                        summaryBinding.summaryListRecycler.setAdapter(summaryAdapter);
                        complaintsBinding.noOrderFoundLayout.setVisibility(View.GONE);
                        complaintsBinding.complaintsRecyclerView.setVisibility(View.VISIBLE);
                        initAdapter();
                        initScrollListener();
                    } else {
                        complaintsBinding.complaintsRecyclerView.setVisibility(View.GONE);
                        complaintsBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            if (complaintsResponse.getData() != null
                    && complaintsResponse.getData().getListData() != null
                    && complaintsResponse.getData().getListData().getRows() != null
                    && complaintsResponse.getData().getListData().getRows().size() > 0) {
                page++;
                if (complaintsResponse.getData().getListData().getRows().size() < 10) {
                    this.isLastRecord = true;
                }
            } else if (complaintsResponse.getData() != null
                    && complaintsResponse.getData().getListData() != null
                    && complaintsResponse.getData().getListData().getRows() != null
                    && complaintsResponse.getData().getListData().getRows().size() == 0) {
                this.isLastRecord = true;
            }
            complaintsListLoad.remove(complaintsListLoad.size() - 1);
            int scrollPosition = complaintsListLoad.size();
            complaintsListAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;

            complaintsListLoad.addAll(complaintsResponse.getData().getListData().getRows());
            complaintsListAdapter.notifyDataSetChanged();
            isLoading = false;
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        complaintsBinding.complaintsRecyclerView.setLayoutManager(mLayoutManager);
        complaintsBinding.complaintsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initAdapter() {
        complaintsListAdapter = new ComplaintsListAdapter(getContext(), complaintsResponse.getData().getListData().getRows(), this);
        complaintsBinding.complaintsRecyclerView.setAdapter(complaintsListAdapter);
    }

    private void initScrollListener() {
        complaintsBinding.complaintsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == complaintsListLoad.size() - 1) {
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
        complaintsListLoad.add(null);
        complaintsListAdapter.notifyItemInserted(complaintsListLoad.size() - 1);
        getController().getComplaintsListApiCall(page);
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
    public void onClickComplaint() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.bottom_sheet, null, false);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();

        bottomSheetBinding.sheetHeader.setText(getResources().getString(R.string.label_order_complaint));
        bottomSheetBinding.cancelOrderHeader.setText(getResources().getString(R.string.label_complaint_reason_header));
        bottomSheetBinding.closeIcon.setOnClickListener(v -> dialog.dismiss());
        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
            if (complaintReasonsList != null && complaintReasonsList.size() > 0) {
                for (ComplaintReasonsListResponse.Row row : complaintReasonsList) {
                    if (row.getName().equals(complaintReason)) {
                        if (!bottomSheetBinding.comment.getText().toString().isEmpty()) {
                            getController().riderComplaintSaveUpdateApiCall(row.getUid(), bottomSheetBinding.comment.getText().toString().trim());
                            dialog.dismiss();
                            break;
                        } else {
                            Toast.makeText(getActivity(), "Comment should not be empty ", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(getActivity(), complaintReasons, null);
        bottomSheetBinding.rejectReasonSpinner.setAdapter(customAdapter);
        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complaintReason = complaintReasons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSuccessComplaintSaveUpdate(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessComplaintReasonsListApiCall(ComplaintReasonsListResponse complaintReasonsListResponse) {
        try {
            complaintReasons = new String[complaintReasonsListResponse.getData().getListData().getRows().size()];
            this.complaintReasonsList = complaintReasonsListResponse.getData().getListData().getRows();
            for (ComplaintReasonsListResponse.Row row : complaintReasonsListResponse.getData().getListData().getRows())
                complaintReasons[complaintReasonsListResponse.getData().getListData().getRows().indexOf(row)] = row.getName();

        } catch (Exception e) {
            System.out.println("onSuccessDeliveryReasonApiCall:::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }
}
