package com.apollo.epos.fragment.myorders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.adapter.MyOrdersListAdapter;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.databinding.FragmentMyOrdersBinding;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, MyOrdersFragmentCallback {
    private Activity mActivity;
    private FragmentMyOrdersBinding myOrdersBinding;
    private MyOrdersListAdapter myOrdersListAdapter;
    @BindView(R.id.ordersRecyclerView)
    RecyclerView ordersRecyclerView;
    private List<MyOrdersListResponse.Row> myOrdersList = new ArrayList<>();
    private List<MyOrdersListResponse.Row> tempOrdersList = new ArrayList<>();


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
        NavigationActivity.getInstance().setMyOrdersFragmentCallback(this);
        final Calendar c = Calendar.getInstance();
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

//    private float x1,x2;
//    static final int MIN_DISTANCE = 150;

    private int min_distance = 100;
    private float downX, downY, upX, upY;

    private int swiping = 0;

    private void orderStatusTabviews() {
        myOrdersBinding.newOrder.setOnClickListener(v -> {
            selectedStatus = 1;
            swiping = 0;
            myOrdersBinding.select.animate().x(0).setDuration(100);
            myOrdersBinding.newOrder.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            List<MyOrdersListResponse.Row> myNewOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row newOrder : myOrdersList)
                if (newOrder.getOrderStatus().getUid().equals("ORDERUPDATE") || newOrder.getOrderStatus().getUid().equals("ORDERACCEPTED"))
                    myNewOrdersList.add(newOrder);
            if (myNewOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myNewOrdersList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_assigned);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        });
        myOrdersBinding.intTransit.setOnClickListener(v -> {
            selectedStatus = 2;
            swiping = 1;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(Color.WHITE);
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth();
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> myInTransitOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row inTransitOrder : myOrdersList)
                if (inTransitOrder.getOrderStatus().getUid().equals("PICKUP") || inTransitOrder.getOrderStatus().getUid().equals("OUTFORDELIVERY") || inTransitOrder.getOrderStatus().getUid().equals("RETURNPICKED"))
                    myInTransitOrdersList.add(inTransitOrder);
            if (myInTransitOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myInTransitOrdersList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_intransit);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        });
        myOrdersBinding.orderDelivered.setOnClickListener(v -> {
            selectedStatus = 3;
            swiping = 2;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth() * 2;
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> deliveredOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row deliveredOrder : myOrdersList)
                if (deliveredOrder.getOrderStatus().getUid().equals("DELIVERED") || deliveredOrder.getOrderStatus().getUid().equals("RETURNORDERRTO"))
                    deliveredOrdersList.add(deliveredOrder);
            if (deliveredOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, deliveredOrdersList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_delivered);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        });
        myOrdersBinding.cancelOrder.setOnClickListener(v -> {
            selectedStatus = 4;
            swiping = 3;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(Color.WHITE);

            int size = myOrdersBinding.intTransit.getWidth() * 3;
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> orderNotDeliveredList = new ArrayList<>();
            for (MyOrdersListResponse.Row deliveredOrder : myOrdersList)
                if (deliveredOrder.getOrderStatus().getUid().equals("DELIVERYATTEMPTED") || deliveredOrder.getOrderStatus().getUid().equals("DELIVERYFAILED") || deliveredOrder.getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || deliveredOrder.getOrderStatus().getUid().equals("CANCELLED"))
                    orderNotDeliveredList.add(deliveredOrder);
            if (orderNotDeliveredList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, orderNotDeliveredList, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_cancelled);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        });

        myOrdersBinding.ordersRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            View v;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                this.v = v;
                switch (event.getAction()) { // Check vertical and horizontal touches
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        //HORIZONTAL SCROLL
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > min_distance) {
                                // left or right
                                if (deltaX < 0) {
                                    this.onLeftToRightSwipe();
                                    return true;
                                }
                                if (deltaX > 0) {
                                    this.onRightToLeftSwipe();
                                    return true;
                                }
                            } else {
                                //not long enough swipe...
                                return false;
                            }
                        }
                        //VERTICAL SCROLL
                        else {
                            if (Math.abs(deltaY) > min_distance) {
                                // top or down
                                if (deltaY < 0) {
                                    this.onTopToBottomSwipe();
                                    return true;
                                }
                                if (deltaY > 0) {
                                    this.onBottomToTopSwipe();
                                    return true;
                                }
                            } else {
                                //not long enough swipe...
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }

            public void onLeftToRightSwipe() {
                leftToRight();
            }

            public void onRightToLeftSwipe() {
                rightToLeft();
            }

            public void onTopToBottomSwipe() {
//                Toast.makeText(v.getContext(),"top to bottom",
//                        Toast.LENGTH_SHORT).show();
            }

            public void onBottomToTopSwipe() {
//                Toast.makeText(v.getContext(),"bottom to top",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        myOrdersBinding.noOrderFoundLayout.setOnTouchListener(new View.OnTouchListener() {
            View v;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                this.v = v;
                switch (event.getAction()) { // Check vertical and horizontal touches
                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        //HORIZONTAL SCROLL
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > min_distance) {
                                // left or right
                                if (deltaX < 0) {
                                    this.onLeftToRightSwipe();
                                    return true;
                                }
                                if (deltaX > 0) {
                                    this.onRightToLeftSwipe();
                                    return true;
                                }
                            } else {
                                //not long enough swipe...
                                return false;
                            }
                        }
                        //VERTICAL SCROLL
                        else {
                            if (Math.abs(deltaY) > min_distance) {
                                // top or down
                                if (deltaY < 0) {
                                    this.onTopToBottomSwipe();
                                    return true;
                                }
                                if (deltaY > 0) {
                                    this.onBottomToTopSwipe();
                                    return true;
                                }
                            } else {
                                //not long enough swipe...
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }

            public void onLeftToRightSwipe() {
                leftToRight();
            }

            public void onRightToLeftSwipe() {
                rightToLeft();
            }

            public void onTopToBottomSwipe() {
//                Toast.makeText(v.getContext(),"top to bottom",
//                        Toast.LENGTH_SHORT).show();
            }

            public void onBottomToTopSwipe() {
//                Toast.makeText(v.getContext(),"bottom to top",
//                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void leftToRight() {
//        Toast.makeText(getContext(),"left to right",
//                Toast.LENGTH_SHORT).show();

        if (swiping > 0) {
            swiping = swiping - 1;
        }

        if (swiping == 2) {
            selectedStatus = 3;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth() * 2;
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> deliveredOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row deliveredOrder : myOrdersList)
                if (deliveredOrder.getOrderStatus().getUid().equals("DELIVERED") || deliveredOrder.getOrderStatus().getUid().equals("RETURNORDERRTO"))
                    deliveredOrdersList.add(deliveredOrder);
            if (deliveredOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, deliveredOrdersList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_delivered);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
        if (swiping == 1) {
            selectedStatus = 2;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(Color.WHITE);
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth();
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> myInTransitOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row inTransitOrder : myOrdersList)
                if (inTransitOrder.getOrderStatus().getUid().equals("PICKUP") || inTransitOrder.getOrderStatus().getUid().equals("OUTFORDELIVERY") || inTransitOrder.getOrderStatus().getUid().equals("RETURNPICKED"))
                    myInTransitOrdersList.add(inTransitOrder);
            if (myInTransitOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myInTransitOrdersList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_intransit);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
        if (swiping == 0) {
            selectedStatus = 1;
            myOrdersBinding.select.animate().x(0).setDuration(100);
            myOrdersBinding.newOrder.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            List<MyOrdersListResponse.Row> myNewOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row newOrder : myOrdersList)
                if (newOrder.getOrderStatus().getUid().equals("ORDERUPDATE") || newOrder.getOrderStatus().getUid().equals("ORDERACCEPTED"))
                    myNewOrdersList.add(newOrder);
            if (myNewOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myNewOrdersList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_assigned);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void rightToLeft() {
//        Toast.makeText(getContext(),"right to left",
//                Toast.LENGTH_SHORT).show();


        if (swiping < 3) {
            swiping = swiping + 1;
        }
        if (swiping == 1) {
            selectedStatus = 2;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(Color.WHITE);
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth();
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> myInTransitOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row inTransitOrder : myOrdersList)
                if (inTransitOrder.getOrderStatus().getUid().equals("PICKUP") || inTransitOrder.getOrderStatus().getUid().equals("OUTFORDELIVERY") || inTransitOrder.getOrderStatus().getUid().equals("RETURNPICKED"))
                    myInTransitOrdersList.add(inTransitOrder);
            if (myInTransitOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myInTransitOrdersList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_intransit);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
        if (swiping == 2) {
            selectedStatus = 3;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(Color.WHITE);
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(getResources().getColor(R.color.colorPrimary));

            int size = myOrdersBinding.intTransit.getWidth() * 2;
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> deliveredOrdersList = new ArrayList<>();
            for (MyOrdersListResponse.Row deliveredOrder : myOrdersList)
                if (deliveredOrder.getOrderStatus().getUid().equals("DELIVERED") || deliveredOrder.getOrderStatus().getUid().equals("RETURNORDERRTO"))
                    deliveredOrdersList.add(deliveredOrder);
            if (deliveredOrdersList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, deliveredOrdersList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_delivered);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
        if (swiping == 3) {
            selectedStatus = 4;
            myOrdersBinding.newOrder.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.orderDelivered.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.intTransit.setTextColor(getResources().getColor(R.color.colorPrimary));
            myOrdersBinding.cancelOrder.setTextColor(Color.WHITE);

            int size = myOrdersBinding.intTransit.getWidth() * 3;
            myOrdersBinding.select.animate().x(size).setDuration(100);
            List<MyOrdersListResponse.Row> orderNotDeliveredList = new ArrayList<>();
            for (MyOrdersListResponse.Row deliveredOrder : myOrdersList)
                if (deliveredOrder.getOrderStatus().getUid().equals("DELIVERYATTEMPTED") || deliveredOrder.getOrderStatus().getUid().equals("DELIVERYFAILED") || deliveredOrder.getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || deliveredOrder.getOrderStatus().getUid().equals("CANCELLED"))
                    orderNotDeliveredList.add(deliveredOrder);
            if (orderNotDeliveredList.size() > 0) {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                myOrdersListAdapter = new MyOrdersListAdapter(mActivity, orderNotDeliveredList, MyOrdersFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                ordersRecyclerView.setLayoutManager(mLayoutManager);
                ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                ordersRecyclerView.setAdapter(myOrdersListAdapter);
            } else {
                myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_cancelled);
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        }
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
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.GONE);
                ordersRecyclerView.setVisibility(View.VISIBLE);
                this.myOrdersList = myOrdersListResponse.getData().getListData().getRows();
                this.tempOrdersList = myOrdersListResponse.getData().getListData().getRows();
                List<MyOrdersListResponse.Row> myNewOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> myInTransitOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> deliveredOrdersList = new ArrayList<>();
                List<MyOrdersListResponse.Row> orderNotDeliveredList = new ArrayList<>();

                List<MyOrdersListResponse.Row> myOrdersLists = new ArrayList<>();
                for (MyOrdersListResponse.Row order : myOrdersList)
                    if (order.getOrderStatus().getUid() == null)
                        System.out.println("naveen");
                    else if (order.getOrderStatus().getUid().equals("ORDERUPDATE") || order.getOrderStatus().getUid().equals("ORDERACCEPTED"))
                        myNewOrdersList.add(order);
                    else if (order.getOrderStatus().getUid().equals("PICKUP") || order.getOrderStatus().getUid().equals("OUTFORDELIVERY") || order.getOrderStatus().getUid().equals("RETURNPICKED"))
                        myInTransitOrdersList.add(order);
                    else if (order.getOrderStatus().getUid().equals("DELIVERED") || order.getOrderStatus().getUid().equals("RETURNORDERRTO"))
                        deliveredOrdersList.add(order);
                    else if (order.getOrderStatus().getUid().equals("DELIVERYATTEMPTED") || order.getOrderStatus().getUid().equals("DELIVERYFAILED") || order.getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || order.getOrderStatus().getUid().equals("CANCELLED"))
                        orderNotDeliveredList.add(order);
                if (selectedStatus == 1) {
                    myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_assigned);
                    myOrdersLists = myNewOrdersList;
                } else if (selectedStatus == 2) {
                    myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_intransit);
                    myOrdersLists = myInTransitOrdersList;
                } else if (selectedStatus == 3) {
                    myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_delivered);
                    myOrdersLists = deliveredOrdersList;
                } else if (selectedStatus == 4) {
                    myOrdersBinding.noOrdersInstatusText.setText(R.string.label_no_orders_cancelled);
                    myOrdersLists = orderNotDeliveredList;
                }
                myOrdersBinding.newOrder.setText("New (" + myNewOrdersList.size() + ")");
                myOrdersBinding.intTransit.setText("In Transit (" + myInTransitOrdersList.size() + ")");
                myOrdersBinding.orderDelivered.setText("Delivered (" + deliveredOrdersList.size() + ")");
                myOrdersBinding.cancelOrder.setText("Cancelled (" + orderNotDeliveredList.size() + ")");
                if (myOrdersLists.size() > 0) {
                    myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myOrdersLists, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
                    ordersRecyclerView.setLayoutManager(mLayoutManager);
                    ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    ordersRecyclerView.setAdapter(myOrdersListAdapter);
                } else {
                    myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                    ordersRecyclerView.setVisibility(View.GONE);
                }
            } else {
                myOrdersBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
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
        startActivity(OrderDeliveryActivity.getStartIntent(getContext(), item.getOrderNumber()));
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClickNotificationIcon() {
        getController().myOrdersListApiCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        orderStatusTabviews();
        getController().myOrdersListApiCall();
    }

    private MyOrdersFragmentController getController() {
        return new MyOrdersFragmentController(getContext(), this);
    }
}