package com.apollo.epos.fragment.summary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.reports.adapter.OrdersCodStatusAdapter;
import com.apollo.epos.databinding.AdapterSummaryBinding;
import com.apollo.epos.databinding.LoadingProgressbarBinding;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.fragment.summary.SummaryFragmentCallback;
import com.apollo.epos.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MyOrdersListResponse.Row> summaryList;
    private SummaryFragmentCallback mListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public SummaryAdapter(Context context, List<MyOrdersListResponse.Row> summaryList, SummaryFragmentCallback mListener) {
        this.context = context;
        this.summaryList = summaryList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterSummaryBinding summaryBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_summary, parent, false);
            return new SummaryAdapter.ItemViewHolder(summaryBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.loading_progressbar, parent, false);
            return new SummaryAdapter.LoadingViewHolder(loadingProgressbarBinding);
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SummaryAdapter.ItemViewHolder) {
            itemOnBindViewHolder((SummaryAdapter.ItemViewHolder) holder, position);
        } else if (holder instanceof OrdersCodStatusAdapter.LoadingViewHolder) {

        }
    }

    private void itemOnBindViewHolder(@NonNull SummaryAdapter.ItemViewHolder holder, int position) {
        MyOrdersListResponse.Row item = summaryList.get(position);
        holder.summaryBinding.orderStatus.setText(" " + item.getOrderStatus().getName());
        holder.summaryBinding.orderStatus.setTextColor(Color.parseColor(item.getOrderStatus().getOther().getColor()));
        holder.summaryBinding.orderId.setText("#" + item.getOrderNumber());

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date orderDates = formatter.parse(item.getCreatedTime());
            long orderDateMills = orderDates.getTime();
            holder.summaryBinding.createdDate.setText(CommonUtils.getTimeFormatter(orderDateMills));

        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        if (item.getOrderState().getName().equals("RETURN")) {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.summaryBinding.deliveredOnText.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            } else if (item.getOrderStatus().getUid().equals("RETURNORDERRTO")) {
                holder.summaryBinding.deliveredOnText.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.summaryBinding.deliveredOnText.setText("Pickup by: ");
                orderDate = item.getPickupEtWindo();
            }
            // Pickup Address
            holder.summaryBinding.pickupAccountName.setText(item.getPickupAccName());
            holder.summaryBinding.pickupAddress.setText(pickupAddress);
            holder.summaryBinding.pickupLandMark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.summaryBinding.orderTxt.setText("R");
            holder.summaryBinding.deliveryAccountName.setText(item.getReturnAccName());
            holder.summaryBinding.deliveryAddress.setText(returnAddress);
            holder.summaryBinding.deliveryLandMark.setText(item.getReturnLandmark());
        } else {
            if (item.getOrderStatus().getUid().equals("ORDERACCEPTED") || item.getOrderStatus().getUid().equals("ORDERUPDATE")) {
                holder.summaryBinding.deliveredOnText.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            } else if (item.getOrderStatus().getUid().equals("DELIVERED")) {
                holder.summaryBinding.deliveredOnText.setText("Delivered at: ");
                orderDate = item.getOrderSh().get(0).getCreatedTime();
            } else {
                holder.summaryBinding.deliveredOnText.setText("Deliver by: ");
                orderDate = item.getDelEtWindo();
            }
            // Pickup Address
            holder.summaryBinding.pickupAccountName.setText(item.getPickupAccName());
            holder.summaryBinding.pickupAddress.setText(pickupAddress);
            holder.summaryBinding.pickupLandMark.setText(item.getPickupLndmrk());
            // Delivery Address
            holder.summaryBinding.deliveryAccountName.setText(item.getDelAccName());
            holder.summaryBinding.deliveryAddress.setText(customerAddress);
            holder.summaryBinding.deliveryLandMark.setText(item.getDeliverLandmark());
        }
//        holder.summaryBinding.pharmaUserAddressLayout.setVisibility(View.VISIBLE);
//        holder.summaryBinding.userAddressLayout.setVisibility(View.GONE);
//        holder.summaryBinding.cancelledReasonLayout.setVisibility(View.GONE);
//        holder.summaryBinding.dividerViewFour.setBackgroundColor(context.getColor(R.color.order_item_divider_color));
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDate = CommonUtils.getCurrentTimeDate();
            Date currentDates = formatter.parse(currentDate);
            Date orderDates = formatter.parse(orderDate);
            long orderDateMills = orderDates.getTime();
            holder.summaryBinding.deliverBy.setText(CommonUtils.getTimeFormatter(orderDateMills));
            if (currentDates.compareTo(orderDates) < 0) {
                System.out.println("orderDates is Greater than currentDates");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    public int getItemViewType(int position) {
        return summaryList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AdapterSummaryBinding summaryBinding;

        public ItemViewHolder(@NonNull AdapterSummaryBinding summaryBinding) {
            super(summaryBinding.getRoot());
            this.summaryBinding = summaryBinding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingProgressbarBinding loadingProgressbarBinding;

        public LoadingViewHolder(@NonNull LoadingProgressbarBinding loadingProgressbarBinding) {
            super(loadingProgressbarBinding.getRoot());
            this.loadingProgressbarBinding = loadingProgressbarBinding;
        }
    }
}
