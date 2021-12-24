package com.apollo.epos.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.myorders.MyOrdersFragmentCallback;
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
    private Activity activity;
    private List<MyOrdersListResponse.Row> myOrdersList;

    private MyOrdersFragmentCallback listener;

    public MyOrdersListAdapter(Activity activity, List<MyOrdersListResponse.Row> myOrdersList, MyOrdersFragmentCallback listener) {
        this.activity = activity;
        this.myOrdersList = myOrdersList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_id)
        TextView orderId;
        //        @BindView(R.id.order_amount)
//        TextView orderAmount;
//        @BindView(R.id.order_payment_type)
//        TextView orderPaymentType;
        @BindView(R.id.pharmacy_address)
        TextView pharmacyAddress;
        @BindView(R.id.customer_name)
        TextView customerName;
        @BindView(R.id.customer_address)
        TextView customerAddress;
        //        @BindView(R.id.order_status)
//        TextView orderStatus;
//        @BindView(R.id.order_date_text)
//        TextView orderDateText;
        @BindView(R.id.order_date)
        TextView orderDate;
        //        @BindView(R.id.travelled_distance)
//        TextView travelledDistance;
//        @BindView(R.id.status_type_icon)
//        ImageView statusTypeIcon;
//        @BindView(R.id.order_status_layout)
//        LinearLayout orderStatusLayout;
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
        //        @BindView(R.id.divider_view_one)
//        View dividerViewOne;
//        @BindView(R.id.divider_view_two)
//        View dividerViewTwo;
//        @BindView(R.id.divider_view_three)
//        View dividerViewThree;
        @BindView(R.id.divider_view_four)
        View dividerViewFour;
        //        @BindView(R.id.divider_view_five)
//        View dividerViewFive;
//        @BindView(R.id.divider_view_six)
//        View dividerViewSix;
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
        holder.orderId.setText("#" + item.getOrderNumber());
//        holder.orderAmount.setText(String.valueOf(item.getCrateAmount()));
//        holder.orderPaymentType.setText(item.getPaymentType().getName());
        String orderDate = null;
        String pickupAddress = item.getDeliverApartment() + ", " + item.getDeliverStreetName() + ", " + item.getDeliverCity() + ", " + item.getDeliverState() + ", " + item.getDelPincode() + ", " + item.getDeliverCountry();
        String customerAddress = item.getPickupApt() + ", " + item.getPickupStreetName() + ", " + item.getPickupCity() + ", " + item.getPickupState() + ", " + item.getPickupPincode() + ", " + item.getPickupCountry();
        String returnAddress = item.getReturnApartment() + ", " + item.getReturnStreetName() + ", " + item.getReturnCity() + ", " + item.getReturnState() + ", " + item.getReturnPincode() + ", " + item.getReturnCountry();


        if (item.getOrderState().getName().equals("RETURN")) {
            if (item.getOrderStatus().getUid().equals("order_assigned") || item.getOrderStatus().getUid().equals("order_transit")) {
                holder.deliverBy.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            }else if (item.getOrderStatus().getUid().equals("order_delivered")){
                holder.deliverBy.setText("Delivered at: ");
                orderDate = item.getPickupEtWindo();
            }else {
                holder.deliverBy.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            }

            // Pickup Address
            holder.apolloPharmacyName.setText(item.getPickupAddId());
            holder.pharmacyAddress.setText(pickupAddress);
            holder.pickupLandmark.setText(item.getPickupLndmrk());

            // Delivery Address
            holder.deliveryText.setText("R");
            holder.customerName.setText(item.getReturnAddId());
            holder.customerAddress.setText(returnAddress);
            holder.customerLandmark.setText(item.getReturnLandmark());
        } else {
             if (item.getOrderStatus().getUid().equals("order_assigned") || item.getOrderStatus().getUid().equals("order_transit")) {
                 holder.deliverBy.setText("Deliver by: ");
                 orderDate = item.getDelEtWindo();
            }else if (item.getOrderStatus().getUid().equals("order_delivered")) {
                 holder.deliverBy.setText("Delivered at: ");
                 orderDate = item.getDelEtWindo();
             }else{
                 holder.deliverBy.setText("Deliver by: ");
                 orderDate = item.getDelEtWindo();
             }

            // Pickup Address
            holder.apolloPharmacyName.setText(item.getPickupAddId());
            holder.pharmacyAddress.setText(pickupAddress);
            holder.pickupLandmark.setText(item.getPickupLndmrk());

            // Delivery Address
            holder.customerName.setText(item.getDelAddId());
            holder.customerAddress.setText(customerAddress);
            holder.customerLandmark.setText(item.getDeliverLandmark());
        }

//        holder.customerName.setText(item.getCustomerName());

//        holder.newOrderCustomerName.setText(item.getCustomerName());
//        holder.newOrderCustomerAddress.setText(item.getCustomerAddress());
//        holder.orderStatus.setText(item.getOrderStatus().getName());
//        if (item.getDeliveryStatus().equalsIgnoreCase("Delivered")) {
        holder.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
        holder.userAddressLayout.setVisibility(View.GONE);
//        holder.orderStatusLayout.setClickable(false);
//        holder.orderDateText.setText(activity.getResources().getString(R.string.label_delivered_on));
//        holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.order_delivered_bg));
        holder.cancelledReasonLayout.setVisibility(View.GONE);
//        holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//        holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//        holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
        holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//        holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//        holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//        holder.statusTypeIcon.setImageDrawable(activity.getDrawable(R.drawable.icon_delivered_order));
//        } else if (item.getDeliveryStatus().equalsIgnoreCase("New Order")) {
//            holder.pharmaUserAddressLayout.setVisibility(View.GONE);
//            holder.userAddressLayout.setVisibility(View.VISIBLE);
//            holder.orderStatusLayout.setClickable(false);
//            holder.orderDateText.setText(activity.getResources().getString(R.string.label_booked_on));
//            holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.cancelledReasonLayout.setVisibility(View.GONE);
//            holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.colorWhite));
//            holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.new_order_bg));
//            holder.statusTypeIcon.setImageDrawable(activity.getDrawable(R.drawable.icon_new_order));
//        } else if (item.getDeliveryStatus().equalsIgnoreCase("Cancelled")) {
//            holder.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
//            holder.userAddressLayout.setVisibility(View.GONE);
//            holder.orderStatusLayout.setClickable(true);
//            holder.orderDateText.setText(activity.getResources().getString(R.string.label_cancelled_on));
//            holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.dashboard_pending_text_color));
//            holder.cancelledReasonLayout.setVisibility(View.VISIBLE);
//            holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
//            holder.statusTypeIcon.setImageDrawable(activity.getDrawable(R.drawable.icon_cancelled_order));
//            holder.orderStatusLayout.setOnClickListener(v -> {
//                if (holder.cancelledReasonLayout.getVisibility() == View.GONE) {
//                    holder.cancelledReasonLayout.setVisibility(View.VISIBLE);
//                } else {
//                    holder.cancelledReasonLayout.setVisibility(View.GONE);
//                }
//            });
//        }


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


//        holder.orderDate.setText(item.getCreatedTime());
//        holder.travelledDistance.setText(item.getTravelledDistance());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStatusClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }


}
