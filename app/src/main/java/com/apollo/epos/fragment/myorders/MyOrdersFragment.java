package com.apollo.epos.fragment.myorders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.adapter.MyOrdersListAdapter;
import com.apollo.epos.fragment.neworder.NewOrderFragment;
import com.apollo.epos.model.MyOrdersItemModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnItemClickListener {
    private Activity mActivity;
    private MyOrdersListAdapter myOrdersListAdapter;
    @BindView(R.id.ordersRecyclerView)
    RecyclerView ordersRecyclerView;
    private ArrayList<MyOrdersItemModel> myOrdersList = new ArrayList<>();
    private ArrayList<MyOrdersItemModel> tempOrdersList = new ArrayList<>();
    @BindView(R.id.date_filter_text)
    TextView dateFilterText;
    @BindView(R.id.orderTypeSpinner)
    protected Spinner orderTypeSpinner;
    private final Calendar myCalendar = null;
    private int mYear, mMonth, mDay;
    String[] orderTypes = {"All Orders", "Delivered", "New Order", "Cancelled"};

    private MyOrdersViewModel myOrdersViewModel;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_orders, container, false);
//        myOrdersViewModel =
//                ViewModelProviders.of(this).get(MyOrdersViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        myOrdersViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getMyOrders();

        myOrdersListAdapter = new MyOrdersListAdapter(mActivity, myOrdersList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        ordersRecyclerView.setLayoutManager(mLayoutManager);
        ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ordersRecyclerView.setAdapter(myOrdersListAdapter);

        final Calendar c = Calendar.getInstance();
        if (mYear == 0 && mMonth == 0 && mDay == 0) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            dateFilterText.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        }

        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, orderTypes);
        orderTypeSpinner.setAdapter(customAdapter);
        orderTypeSpinner.setOnItemSelectedListener(this);

//        ArrayAdapter adapter = ArrayAdapter.createFromResource(mActivity, R.array.my_orders_item_array, android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        orderTypeSpinner.setAdapter(adapter);
//        orderTypeSpinner.setOnItemSelectedListener(this);
    }

    private void getMyOrders() {
        MyOrdersItemModel orderItem = new MyOrdersItemModel();
        orderItem.setOrderID("38478");
        orderItem.setOrderAmount("1250.00");
        orderItem.setPaymentType("COD");
        orderItem.setPharmacyAddress("7-1-488, beside Sonabai temple, Near Satyam Theater, Balkampet road, Ameerpet, Hyderabad");
        orderItem.setCustomerName("Vijay Bhaskar");
        orderItem.setCustomerAddress("8-2-43, beside Ratna Super Market, SR Nagar, Hyderabad");
        orderItem.setDeliveryStatus("Delivered");
        orderItem.setDateOfDelivery("12-08-19, 10:35");
        orderItem.setTravelledDistance("4.5 Km");
        orderItem.setCancelledReason("");
        myOrdersList.add(orderItem);
        tempOrdersList.add(orderItem);

        orderItem = new MyOrdersItemModel();
        orderItem.setOrderID("38479");
        orderItem.setOrderAmount("1500.00");
        orderItem.setPaymentType("Paid");
        orderItem.setPharmacyAddress("");
        orderItem.setCustomerName("Vijay Bhaskar");
        orderItem.setCustomerAddress("8-2-43, beside Ratna Super Market, SR Nagar, Hyderabad");
        orderItem.setDeliveryStatus("New Order");
        orderItem.setDateOfDelivery("Today, 12:45PM");
        orderItem.setTravelledDistance("0 Km");
        orderItem.setCancelledReason("");
        myOrdersList.add(orderItem);
        tempOrdersList.add(orderItem);

        orderItem = new MyOrdersItemModel();
        orderItem.setOrderID("38480");
        orderItem.setOrderAmount("2000.00");
        orderItem.setPaymentType("No Due");
        orderItem.setPharmacyAddress("7-1-488, beside Sonabai temple, Near Satyam Theater, Balkampet road, Ameerpet, Hyderabad");
        orderItem.setCustomerName("Vijay Bhaskar");
        orderItem.setCustomerAddress("8-2-43, beside Ratna Super Market, SR Nagar, Hyderabad");
        orderItem.setDeliveryStatus("Cancelled");
        orderItem.setDateOfDelivery("15-08-19, 15:35");
        orderItem.setTravelledDistance("4.5 Km");
        orderItem.setCancelledReason("Customer House door locked");
        myOrdersList.add(orderItem);
        tempOrdersList.add(orderItem);
    }

    @OnClick(R.id.date_filter_text)
    void onDateFilterClick() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        dateFilterText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("MyOrdersFrag", "Selected Pos : " + orderTypes[position]);
        myOrdersList.clear();
        for (MyOrdersItemModel item : tempOrdersList) {
            if (orderTypes[position].equalsIgnoreCase(item.getDeliveryStatus())) {
                myOrdersList.add(item);
            }
            myOrdersListAdapter.notifyDataSetChanged();
        }
        if (myOrdersList.size() == 0) {
            myOrdersList.addAll(tempOrdersList);
            myOrdersListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void OnItemClick(MyOrdersItemModel model) {
        if (model.getDeliveryStatus().equalsIgnoreCase("New Order")) {
            ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new NewOrderFragment(), R.string.menu_take_order);
            ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);
        }
    }
}