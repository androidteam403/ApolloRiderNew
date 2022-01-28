package com.apollo.epos.activity.reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.reports.adapter.OrdersCodStatusAdapter;
import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.databinding.ActivityReportsBinding;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;
import com.novoda.merlin.Merlin;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends BaseActivity implements ReportsActivityCallback {

    private ActivityReportsBinding reportsBinding;
    private static TextView notificationText;
    private OrdersCodStatusAdapter ordersCodStatusAdapter;
    private boolean isLoading = false;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusList;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusListLoad;

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ReportsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportsBinding = DataBindingUtil.setContentView(this, R.layout.activity_reports);
        reportsBinding.setCallback(this);
        notificationText = (TextView) findViewById(R.id.notification_dot);
        getController().getOrdersCodStatusApiCall();
    }

    @Override
    public void onClickNotificationIcon() {
        if (getSessionManager().getNotificationStatus()) {
            notificationText.clearAnimation();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        } else {
            Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.CURRENT_SCREEN = getClass().getSimpleName();
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
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            reportsBinding.reportsRecycler.setLayoutManager(mLayoutManager);
            reportsBinding.reportsRecycler.setItemAnimator(new DefaultItemAnimator());
            reportsBinding.reportsRecycler.setVisibility(View.VISIBLE);
            reportsBinding.noReportsFoundLayout.setVisibility(View.GONE);
            initAdapter();
            initScrollListener();
        } else {
            reportsBinding.reportsRecycler.setVisibility(View.GONE);
            reportsBinding.noReportsFoundLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initAdapter() {
        ordersCodStatusAdapter = new OrdersCodStatusAdapter(this, ordersCodStatusList, this);
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

    @Override
    public void onClickBack() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private ReportsActivityController getController() {
        return new ReportsActivityController(this, this);
    }

    static Animation anim;

    public static void notificationDotVisibility(boolean show) {
        if (show) {
            if (notificationText != null) {
                notificationText.setVisibility(View.VISIBLE);
                anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                notificationText.startAnimation(anim);
            }
        } else {
            if (notificationText != null)
                notificationText.setVisibility(View.GONE);
        }
    }
}
