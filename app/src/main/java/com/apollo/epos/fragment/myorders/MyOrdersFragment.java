package com.apollo.epos.fragment.myorders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.apollo.epos.R;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.adapter.MyOrdersListAdapter;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentMyOrdersBinding;
import com.apollo.epos.fragment.myorders.adapter.ViewPagerAdapter;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, MyOrdersFragmentCallback, ViewPager.OnPageChangeListener {
    private Activity mActivity;
    private FragmentMyOrdersBinding myOrdersBinding;
    private MyOrdersListAdapter myOrdersListAdapter;
    private List<MyOrdersListResponse.Row> myOrdersList = new ArrayList<>();
    private List<MyOrdersListResponse.Row> tempOrdersList = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private static final String[] CHANNELS = new String[]{"KITKAT", "NOUGAT", "DONUT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    @BindView(R.id.date_filter_text)
    TextView dateFilterText;
    @BindView(R.id.orderTypeSpinner)
    protected Spinner orderTypeSpinner;
    private final Calendar myCalendar = null;
    private int mYear, mMonth, mDay;
    String[] orderTypes = {"All Orders", "Delivered", "New Order", "Cancelled"};   //"All Orders", "Order Created", "Order Packed", "Order Assigned", "Order Picked", "Order Transit", "Order Delivered", "Order Not Delivered"
    public static final int NEW_ORDER_ACTIVITY = 101;
    private int selectedStatus = 1;

    public static MyOrdersFragment newInstance() {
        return new MyOrdersFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myOrdersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);
        return myOrdersBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        NavigationActivity.getInstance().setTitle(R.string.menu_my_orders);
        CommonUtils.isMyOrdersListApiCall = false;
        NavigationActivity.getInstance().setMyOrdersFragmentCallback(this);
        final Calendar c = Calendar.getInstance();
        setUp();
        if (mYear == 0 && mMonth == 0 && mDay == 0) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            dateFilterText.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        }

        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, orderTypes, null);
        orderTypeSpinner.setAdapter(customAdapter);
        orderTypeSpinner.setOnItemSelectedListener(this);
    }

    private void setUp() {
        getController().globalSettingSelectApi();

        myOrdersBinding.viewPager.setVisibility(View.GONE);
        myOrdersBinding.whiteView.setVisibility(View.VISIBLE);
        orderStatusTabviews();
        getController().myOrdersListApiCall();
    }

    private void orderStatusTabviews() {
        myOrdersBinding.newOrder.setOnClickListener(v -> {
            selectedStatus = 1;
            myOrdersBinding.select.animate().x(0).setDuration(100);
            myOrdersBinding.newOrder.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            myOrdersBinding.viewPager.setCurrentItem(0, true);
            viewPagerAdapter.notifyDataSetChanged();
        });
        myOrdersBinding.intTransit.setOnClickListener(v -> {
            selectedStatus = 2;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(Color.WHITE);
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth();
            myOrdersBinding.select.animate().x(size).setDuration(100);

            myOrdersBinding.viewPager.setCurrentItem(1, true);
            viewPagerAdapter.notifyDataSetChanged();
        });
        myOrdersBinding.orderDelivered.setOnClickListener(v -> {
            selectedStatus = 3;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth() * 2;
            myOrdersBinding.select.animate().x(size).setDuration(100);

            myOrdersBinding.viewPager.setCurrentItem(2, true);
            viewPagerAdapter.notifyDataSetChanged();
        });
        myOrdersBinding.cancelOrder.setOnClickListener(v -> {
            selectedStatus = 4;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(Color.WHITE);

            int size = myOrdersBinding.intTransit.getWidth() * 3;
            myOrdersBinding.select.animate().x(size).setDuration(100);

            myOrdersBinding.viewPager.setCurrentItem(3, true);
            viewPagerAdapter.notifyDataSetChanged();
        });
    }


    @OnClick(R.id.date_filter_text)
    void onDateFilterClick() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                (view, year, monthOfYear, dayOfMonth) -> {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    dateFilterText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("MyOrdersFrag", "Selected Pos : " + orderTypes[position]);
        myOrdersList.clear();
        for (MyOrdersListResponse.Row item : tempOrdersList) {
            if (orderTypes[position].equalsIgnoreCase(item.getOrderStatus().getName())) {
                myOrdersList.add(item);
            }
            if (myOrdersListAdapter != null)
                myOrdersListAdapter.notifyDataSetChanged();
        }
        if (myOrdersList.size() == 0) {
            myOrdersList.addAll(tempOrdersList);
            if (myOrdersListAdapter != null)
                myOrdersListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessMyOrdersListApi(MyOrdersListResponse myOrdersListResponse) {
        if (myOrdersListResponse != null) {
            if (myOrdersListResponse.getData() != null
                    && myOrdersListResponse.getData().getListData() != null
                    && myOrdersListResponse.getData().getListData().getRows() != null) {
//                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                this.myOrdersList = myOrdersListResponse.getData().getListData().getRows();
                this.tempOrdersList = myOrdersListResponse.getData().getListData().getRows();

                List<List<MyOrdersListResponse.Row>> listofMyOrdersList = new ArrayList<>();

                List<MyOrdersListResponse.Row> myNewOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> myInTransitOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> deliveredOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> orderNotDeliveredList = new ArrayList<>();

                List<MyOrdersListResponse.Row> myOrdersLists = new ArrayList<>();
                for (MyOrdersListResponse.Row order : myOrdersList)
                    if (order.getOrderStatus().getUid() == null)
                        System.out.println("naveen");
                    else if (order.getOrderStatus().getUid().equals("ORDERUPDATE") || order.getOrderStatus().getUid().equals("ORDERACCEPTED") || (order.getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && order.getFailureAttempts() <= 2))
                        myNewOrdersList.add(order);
                    else if (order.getOrderStatus().getUid().equals("PICKUP") || order.getOrderStatus().getUid().equals("OUTFORDELIVERY") || order.getOrderStatus().getUid().equals("RETURNPICKED"))
                        myInTransitOrdersList.add(order);
                    else if (order.getOrderStatus().getUid().equals("DELIVERED") || order.getOrderStatus().getUid().equals("RETURNORDERRTO"))
                        deliveredOrdersList.add(order);
                    else if ((order.getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && order.getFailureAttempts() > 2) || order.getOrderStatus().getUid().equals("DELIVERYFAILED") || order.getOrderStatus().getUid().equals("CANCELRETURNINITIATED")
                            || (order.getOrderStatus().getUid().equals("CANCELLED") && !order.getOrderSh().get(order.getOrderSh().size() - 1).getOrderStatus().getUid().equals("ORDERUPDATE")) && !order.getOrderSh().get(order.getOrderSh().size() - 1).getOrderStatus().getUid().equals("ORDERACCEPTED")
                            || order.getOrderStatus().getUid().equals("CANCELORDERRTO"))
                        orderNotDeliveredList.add(order);

                listofMyOrdersList.add(myNewOrdersList);
                listofMyOrdersList.add(myInTransitOrdersList);
                listofMyOrdersList.add(deliveredOrdersList);
                listofMyOrdersList.add(orderNotDeliveredList);

                myOrdersBinding.newOrder.setText("New (" + myNewOrdersList.size() + ")");
                myOrdersBinding.intTransit.setText("In Transit (" + myInTransitOrdersList.size() + ")");
                myOrdersBinding.orderDelivered.setText("Delivered (" + deliveredOrdersList.size() + ")");
                myOrdersBinding.cancelOrder.setText("Cancelled (" + orderNotDeliveredList.size() + ")");

                viewPagerAdapter = new ViewPagerAdapter(getContext(), listofMyOrdersList, this);
                myOrdersBinding.viewPager.setAdapter(viewPagerAdapter);
                myOrdersBinding.viewPager.addOnPageChangeListener(this);
                myOrdersBinding.viewPager.setVisibility(View.VISIBLE);
                myOrdersBinding.whiteView.setVisibility(View.GONE);
                if (CommonUtils.selectedTab.equals("NEW")) {
                    CommonUtils.selectedTab = "";
                    myOrdersBinding.viewPager.setCurrentItem(0, true);
                } else if (CommonUtils.selectedTab.equals("INTRANSIT")) {
                    CommonUtils.selectedTab = "";
                    myOrdersBinding.viewPager.setCurrentItem(1, true);
                } else if (CommonUtils.selectedTab.equals("DELIVERED")) {
                    CommonUtils.selectedTab = "";
                    myOrdersBinding.viewPager.setCurrentItem(2, true);
                } else if (CommonUtils.selectedTab.equals("CANCELLED")) {
                    CommonUtils.selectedTab = "";
                    myOrdersBinding.viewPager.setCurrentItem(3, true);
                }
            } else {
                myOrdersBinding.viewPager.setVisibility(View.GONE);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                myOrdersBinding.whiteView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onFailureMyOrdersListApi() {

    }

    @Override
    public void onStatusClick(MyOrdersListResponse.Row item) {
        if (getSessionManager().getAsignedOrderUid() != null && getSessionManager().getAsignedOrderUid().size() > 0) {
            for (String orderUid : getSessionManager().getAsignedOrderUid()) {
                if (orderUid.equals(item.getUid())) {
                    NavigationActivity.notificationDotVisibility(false);
                    getSessionManager().setNotificationStatus(false);
                    getSessionManager().setAsignedOrderUid(null);
                    break;
                }
            }
        }
        switch (selectedStatus) {
            case 1:
                CommonUtils.selectedTab = "NEW";
                break;
            case 2:
                CommonUtils.selectedTab = "INTRANSIT";
                break;
            case 3:
                CommonUtils.selectedTab = "DELIVERED";
                break;
            case 4:
                CommonUtils.selectedTab = "CANCELLED";
                break;
            default:
        }
        startActivity(OrderDeliveryActivity.getStartIntent(getContext(), item.getOrderNumber()));
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickNotificationIcon() {
        getController().myOrdersListApiCall();
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
    public void onResume() {
        super.onResume();
        if (CommonUtils.isMyOrdersListApiCall) {
            CommonUtils.isMyOrdersListApiCall = false;
            myOrdersBinding.viewPager.setVisibility(View.GONE);
            myOrdersBinding.whiteView.setVisibility(View.VISIBLE);
//        selectedStatus = 1;
//        myOrdersBinding.select.animate().x(0).setDuration(100);
//        myOrdersBinding.newOrder.setTextColor(Color.WHITE);
//        myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
//        myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
//        myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            orderStatusTabviews();
            getController().myOrdersListApiCall();
        }
    }

    private MyOrdersFragmentController getController() {
        return new MyOrdersFragmentController(getContext(), this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            switch (position) {
                case 0:
                    selectedStatus = 1;
                    myOrdersBinding.select.animate().x(0).setDuration(100);
                    myOrdersBinding.newOrder.setTextColor(Color.WHITE);
                    myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 1:
                    selectedStatus = 2;
                    myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.intTransit.setTextColor(Color.WHITE);
                    myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

                    int size1 = myOrdersBinding.intTransit.getWidth();
                    myOrdersBinding.select.animate().x(size1).setDuration(100);
                    break;
                case 2:
                    selectedStatus = 3;
                    myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.orderDelivered.setTextColor(Color.WHITE);
                    myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

                    int size2 = myOrdersBinding.intTransit.getWidth() * 2;
                    myOrdersBinding.select.animate().x(size2).setDuration(100);
                    break;
                case 3:
                    selectedStatus = 4;
                    myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
                    myOrdersBinding.cancelOrder.setTextColor(Color.WHITE);

                    int size3 = myOrdersBinding.intTransit.getWidth() * 3;
                    myOrdersBinding.select.animate().x(size3).setDuration(100);
                    break;
                default:
            }
        } catch (Exception e) {
            System.out.println("onPageSelected ::::::::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}