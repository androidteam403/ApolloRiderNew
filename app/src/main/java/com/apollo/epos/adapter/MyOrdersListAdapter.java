package com.apollo.epos.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.myorders.OnItemClickListener;
import com.apollo.epos.model.MyOrdersItemModel;
import com.apollo.epos.model.NotificationItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<MyOrdersItemModel> myOrdersList;
    private boolean isCancelledClicked = false;
    private OnItemClickListener listener;

    public MyOrdersListAdapter(Activity activity, ArrayList<MyOrdersItemModel> myOrdersList, OnItemClickListener listener) {
        this.activity = activity;
        this.myOrdersList = myOrdersList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_id)
        TextView orderId;
        @BindView(R.id.order_amount)
        TextView orderAmount;
        @BindView(R.id.order_payment_type)
        TextView orderPaymentType;
        @BindView(R.id.pharmacy_address)
        TextView pharmacyAddress;
        @BindView(R.id.customer_name)
        TextView customerName;
        @BindView(R.id.customer_address)
        TextView customerAddress;
        @BindView(R.id.order_status)
        TextView orderStatus;
        @BindView(R.id.order_date_text)
        TextView orderDateText;
        @BindView(R.id.order_date)
        TextView orderDate;
        @BindView(R.id.travelled_distance)
        TextView travelledDistance;
        @BindView(R.id.order_status_layout)
        RelativeLayout orderStatusLayout;
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
        @BindView(R.id.divider_view_one)
        View dividerViewOne;
        @BindView(R.id.divider_view_two)
        View dividerViewTwo;
        @BindView(R.id.divider_view_three)
        View dividerViewThree;
        @BindView(R.id.divider_view_four)
        View dividerViewFour;
        @BindView(R.id.divider_view_five)
        View dividerViewFive;
        @BindView(R.id.divider_view_six)
        View dividerViewSix;


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
        MyOrdersItemModel item = myOrdersList.get(position);
        holder.orderId.setText(item.getOrderID());
        holder.orderAmount.setText(item.getOrderAmount());
        holder.orderPaymentType.setText(item.getPaymentType());
        holder.pharmacyAddress.setText(item.getPharmacyAddress());
        holder.customerName.setText(item.getCustomerName());
        holder.customerAddress.setText(item.getCustomerAddress());
        holder.newOrderCustomerName.setText(item.getCustomerName());
        holder.newOrderCustomerAddress.setText(item.getCustomerAddress());
        holder.orderStatus.setText(item.getDeliveryStatus());
        if (item.getDeliveryStatus().equalsIgnoreCase("Delivered")) {
            holder.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
            holder.userAddressLayout.setVisibility(View.GONE);
            holder.orderStatusLayout.setClickable(false);
            holder.orderDateText.setText(activity.getResources().getString(R.string.label_delivered_on));
            holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.order_delivered_bg));
            holder.cancelledReasonLayout.setVisibility(View.GONE);
            holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
        } else if (item.getDeliveryStatus().equalsIgnoreCase("New Order")) {
            holder.pharmaUserAddressLayout.setVisibility(View.GONE);
            holder.userAddressLayout.setVisibility(View.VISIBLE);
            holder.orderStatusLayout.setClickable(false);
            holder.orderDateText.setText(activity.getResources().getString(R.string.label_booked_on));
            holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.new_order_bg));
            holder.cancelledReasonLayout.setVisibility(View.GONE);
            holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.new_order_bg));
            holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.new_order_bg));
            holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.new_order_bg));
            holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.new_order_bg));
            holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.colorWhite));
            holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.new_order_bg));
        } else if (item.getDeliveryStatus().equalsIgnoreCase("Cancelled")) {
            holder.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
            holder.userAddressLayout.setVisibility(View.GONE);
            holder.orderStatusLayout.setClickable(true);
            holder.orderDateText.setText(activity.getResources().getString(R.string.label_cancelled_on));
            holder.orderStatusLayout.setBackgroundColor(activity.getColor(R.color.dashboard_pending_text_color));
            holder.cancelledReasonLayout.setVisibility(View.GONE);
            holder.dividerViewOne.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewTwo.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewThree.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewFour.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewFive.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.dividerViewSix.setBackgroundColor(activity.getColor(R.color.order_item_divider_color));
            holder.orderStatusLayout.setOnClickListener(v -> {
                if (!isCancelledClicked) {
                    isCancelledClicked = true;
                    holder.cancelledReasonLayout.setVisibility(View.VISIBLE);
                } else {
                    isCancelledClicked = false;
                    holder.cancelledReasonLayout.setVisibility(View.GONE);
                }
            });
        }
        holder.orderDate.setText(item.getDateOfDelivery());
        holder.travelledDistance.setText(item.getTravelledDistance());

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.OnItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }
}
