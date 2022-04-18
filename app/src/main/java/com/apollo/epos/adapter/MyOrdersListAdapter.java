package com.apollo.epos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder> {
    private Context activity;
    private List<MyOrdersListResponse.Row> myOrdersList;

    private MyOrdersListAdapterCallback listener;

    public MyOrdersListAdapter(Context activity, List<MyOrdersListResponse.Row> myOrdersList, MyOrdersListAdapterCallback listener) {
        this.activity = activity;
        this.myOrdersList = myOrdersList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_id)
        TextView orderId;
        @BindView(R.id.pharmacy_address)
        TextView pharmacyAddress;
        @BindView(R.id.customer_name)
        TextView customerName;
        @BindView(R.id.customer_address)
        TextView customerAddress;
        @BindView(R.id.order_date)
        TextView orderDate;
        @BindView(R.id.cancelled_reason_layout)
        LinearLayout cancelledReasonLayout;
        @BindView(R.id.pharma_user_address_layout)
        LinearLayout pharmaUserAddressLayout;
        @BindView(R.id.user_address_layout)
        LinearLayout userAddressLayout;
        @BindView(R.id.new_order_customer_name)
        TextView newOrderCustomerName;
        @BindView(R.id.new_order_customer_address)
        TextView newOrderCustomerAddress;
        @BindView(R.id.divider_view_four)
        View dividerViewFour;
        @BindView(R.id.apollo_phamrmacy)
        TextView apolloPharmacyName;
        @BindView(R.id.pickup_landmark)
        TextView pickupLandmark;
        @BindView(R.id.customer_landmark)
        TextView customerLandmark;
        @BindView(R.id.delivery_txt)
        TextView deliveryText;
        @BindView(R.id.deliver_by)
        TextView deliverBy;
        @BindView(R.id.order_status_layout)
        LinearLayout orderStatusLayout;
        @BindView(R.id.order_status)
        TextView orderStatus;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_orders_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyOrdersListResponse.Row item = myOrdersList.get(position);
        if (listener == null) {
            holder.orderStatusLayout.setVisibility(View.VISIBLE);
            holder.orderStatus.setText(item.getOrderStatus().getName());
            holder.orderStatus.setTextColor(Color.parseColor(item.getOrderStatus().getOther().getColor()));
        } else {
            holder.orderStatusLayout.setVisibility(View.GONE);
        }

        holder.orderId.setText("#" + item.getOrderNumber());
        String orderDate = null;

        String customerAddress = "";
        if (item.getDeliverApartment() != null) {
            customerAddress = item.getDeliverApartment() + ", ";
        }
        if (item.getDeliverStreetName() != null) {
            customerAddress = customerAddress + item.getDeliverStreetName() + ", ";
        }
        if (item.getDeliverCity() != null) {
            customerAddress = customerAddress + item.getDeliverCity() + ", ";
        }
        if (item.getDeliverState() != null) {
            customerAddress = customerAddress + item.getDeliverState() + ", ";
        }
        if (item.getDelPincode() != null) {
            customerAddress = customerAddress + item.getDelPincode() + ", ";
        }
        if (item.getDeliverCountry() != null) {
            customerAddress = customerAddress + item.getDeliverCountry();
        }
//        item.getDeliverApartment() + ", " + item.getDeliverStreetName() + ", " + item.getDeliverCity() + ", " + item.getDeliverState() + ", " + item.getDelPincode() + ", " + item.getDeliverCountry();

        String pickupAddress = "";
        if (item.getPickupApt() != null) {
            pickupAddress = item.getPickupApt() + ", ";
        }
        if (item.getPickupStreetName() != null) {
            pickupAddress = pickupAddress + item.getPickupStreetName() + ", ";
        }
        if (item.getPickupCity() != null) {
            pickupAddress = pickupAddress + item.getPickupCity() + ", ";
        }
        if (item.getPickupState() != null) {
            pickupAddress = pickupAddress + item.getPickupState() + ", ";
        }
        if (item.getPickupPincode() != null) {
            pickupAddress = pickupAddress + item.getPickupPincode() + ", ";
        }
        if (item.getPickupCountry() != null) {
            pickupAddress = pickupAddress + item.getPickupCountry();
        }
//        = item.getPickupApt() + ", " + item.getPickupStreetName() + ", " + item.getPickupCity() + ", " + item.getPickupState() + ", " + item.getPickupPincode() + ", " + item.getPickupCountry();

        String returnAddress = "";
        if (item.getReturnApartment() != null) {
            returnAddress = item.getReturnApartment() + ", ";
        }
        if (item.getReturnStreetName() != null) {
            returnAddress = returnAddress + item.getReturnStreetName() + ", ";
        }
        if (item.getReturnCity() != null) {
            returnAddress = returnAddress + item.getReturnCity() + ", ";
        }
        if (item.getReturnState() != null) {
            returnAddress = returnAddress + item.getReturnState() + ", ";
        }
        if (item.getReturnPincode() != null) {
            returnAddress = returnAddress + item.getReturnPincode() + ", ";
        }
        if (item.getReturnCountry() != null) {
            returnAddress = returnAddress + item.getReturnCountry();
        }
//                item.getReturnApartment() + ", " + item.getReturnStreetName() + ", " + item.getReturnCity() + ", " + item.getReturnState() + ", " + item.getReturnPincode() + ", " + item.getReturnCountry();

        if (item.getOrderState().getName().equals("RETURN")) {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.deliverBy.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            } else if (item.getOrderStatus().getUid().equals("RETURNORDERRTO")) {
                holder.deliverBy.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.deliverBy.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            }
            // Pickup Address
            holder.apolloPharmacyName.setText(item.getPickupAccName());
            holder.pharmacyAddress.setText(pickupAddress);
            holder.pickupLandmark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.deliveryText.setText("R");
            holder.customerName.setText(item.getReturnAccName());
            holder.customerAddress.setText(returnAddress);
            holder.customerLandmark.setText(item.getReturnLandmark());
        } else {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.deliverBy.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            } else if (item.getOrderStatus().getUid().equals("DELIVERED")) {
                holder.deliverBy.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.deliverBy.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            }
            // Pickup Address
            holder.apolloPharmacyName.setText(item.getPickupAccName());
            holder.pharmacyAddress.setText(pickupAddress);
            holder.pickupLandmark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.customerName.setText(item.getDelAccName());
            holder.customerAddress.setText(customerAddress);
            holder.customerLandmark.setText(item.getDeliverLandmark());
        }
        holder.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
        holder.userAddressLayout.setVisibility(View.GONE);
        holder.cancelledReasonLayout.setVisibility(View.GONE);
        holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDate = CommonUtils.getCurrentTimeDate();
            Date currentDates = formatter.parse(currentDate);
            Date orderDates = formatter.parse(orderDate);
            long orderDateMills = orderDates.getTime();
            holder.orderDate.setText(CommonUtils.getTimeFormatter(orderDateMills));
            if (currentDates.compareTo(orderDates) < 0) {
                System.out.println("orderDates is Greater than currentDates");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickOrder(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }


    public interface MyOrdersListAdapterCallback {
        void onClickOrder(MyOrdersListResponse.Row order);
    }
}
