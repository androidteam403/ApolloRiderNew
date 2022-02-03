package com.apollo.epos.fragment.reports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.reports.adapter.OrdersCodStatusAdapter;
import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentReportsBinding;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.utils.ActivityUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends BaseFragment implements ReportsFragmentCallback {
    private Activity mActivity;
    private FragmentReportsBinding reportsBinding;
    private OrdersCodStatusAdapter ordersCodStatusAdapter;
    private boolean isLoading = false;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusList;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusListLoad;

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reportsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false);
        return reportsBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getController().getOrdersCodStatusApiCall();
        Double codReceived = Double.parseDouble(getSessionManager().getCodReceived());
        Double codPendingDeposited = Double.parseDouble(getSessionManager().getCodPendingDeposited());
        DecimalFormat decim = new DecimalFormat("#,###.##");
        reportsBinding.codReceivedVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(codReceived));
        reportsBinding.codPendingVal.setText(getActivity().getResources().getString(R.string.label_rupee_symbol) + " " + decim.format(codPendingDeposited));
    }

    @Override
    public void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse) {
        ActivityUtils.hideDialog();
        if (ordersCodStatusResponse != null && ordersCodStatusResponse.getData() != null
                && ordersCodStatusResponse.getData().getListData() != null
                && ordersCodStatusResponse.getData().getListData().getRows() != null
                && ordersCodStatusResponse.getData().getListData().getRows().size() > 0) {
            this.ordersCodStatusListLoad = ordersCodStatusResponse.getData().getListData().getRows();
//            for (int i = 0; i < 30; i++)
//                ordersCodStatusListLoad.add(ordersCodStatusListLoad.get(0));
            if (ordersCodStatusListLoad != null) {
                ordersCodStatusList = new ArrayList<>();
                if (ordersCodStatusListLoad.size() > 10) {
                    for (int i = 0; i <= 10; i++) {
                        ordersCodStatusList.add(ordersCodStatusListLoad.get(i));
                    }
                } else {
                    this.ordersCodStatusList = ordersCodStatusListLoad;
                }
            }
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            reportsBinding.reportsRecycler.setLayoutManager(mLayoutManager);
            reportsBinding.reportsRecycler.setItemAnimator(new DefaultItemAnimator());
            reportsBinding.reportsRecycler.setVisibility(View.VISIBLE);
            reportsBinding.codreceivedCodpending.setVisibility(View.VISIBLE);
            reportsBinding.devider.setVisibility(View.VISIBLE);
            reportsBinding.noReportsFoundLayout.setVisibility(View.GONE);
            initAdapter();
            initScrollListener();
        } else {
            reportsBinding.reportsRecycler.setVisibility(View.GONE);
            reportsBinding.codreceivedCodpending.setVisibility(View.GONE);
            reportsBinding.devider.setVisibility(View.GONE);
            reportsBinding.noReportsFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initAdapter() {
        ordersCodStatusAdapter = new OrdersCodStatusAdapter(getContext(), ordersCodStatusList, this);
        reportsBinding.reportsRecycler.setAdapter(ordersCodStatusAdapter);
    }

    private void initScrollListener() {
        reportsBinding.reportsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == ordersCodStatusList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        ordersCodStatusList.add(null);
        ordersCodStatusAdapter.notifyItemInserted(ordersCodStatusList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ordersCodStatusList.remove(ordersCodStatusList.size() - 1);
            int scrollPosition = ordersCodStatusList.size();
            ordersCodStatusAdapter.notifyItemRemoved(scrollPosition);
            int currentSize = scrollPosition;
            int nextLimit = 10;
            for (int i = ordersCodStatusList.size(); i < 10; i++) {
                if (i < ordersCodStatusListLoad.size())
                    ordersCodStatusList.add(ordersCodStatusListLoad.get(i));
            }
            ordersCodStatusAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 1000);
    }

    @Override
    public void onFailureOrdersCodStatusApiCall(String message) {

    }

    public SessionManager getSessionManager() {
        return new SessionManager(getContext());
    }

    private ReportsFragmentController getController() {
        return new ReportsFragmentController(getContext(), this);
    }
}
