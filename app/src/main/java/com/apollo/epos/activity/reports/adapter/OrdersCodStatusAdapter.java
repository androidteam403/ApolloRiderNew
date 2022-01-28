package com.apollo.epos.activity.reports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.reports.ReportsActivityCallback;
import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.databinding.AdapterOrdersCodStatusBinding;
import com.apollo.epos.databinding.LoadingProgressbarBinding;
import com.apollo.epos.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersCodStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<OrdersCodStatusResponse.Row> ordersCodStatusList;
    private ReportsActivityCallback mListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public OrdersCodStatusAdapter(Context context, List<OrdersCodStatusResponse.Row> ordersCodStatusList, ReportsActivityCallback mListener) {
        this.context = context;
        this.ordersCodStatusList = ordersCodStatusList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterOrdersCodStatusBinding ordersCodStatusBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_orders_cod_status, parent, false);
            return new OrdersCodStatusAdapter.ItemViewHolder(ordersCodStatusBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.loading_progressbar, parent, false);
            return new OrdersCodStatusAdapter.LoadingViewHolder(loadingProgressbarBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            itemOnBindViewHolder((ItemViewHolder) holder, position);
        }else if (holder instanceof LoadingViewHolder){

        }
    }

    private void itemOnBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        try {
            OrdersCodStatusResponse.Row orderCodStatus = ordersCodStatusList.get(position);
            holder.ordersCodStatusBinding.orderNumber.setText(orderCodStatus.getOrderNumber());
            holder.ordersCodStatusBinding.awbNumber.setText(orderCodStatus.getAwbNumber());
            holder.ordersCodStatusBinding.collectedAmount.setText(String.valueOf(orderCodStatus.getOrderPayment().getAmount()));
            if (orderCodStatus.getOrderPayment().getSettled().getName().equals("No"))
                holder.ordersCodStatusBinding.depositedAmount.setText("0");
            else
                holder.ordersCodStatusBinding.depositedAmount.setText(String.valueOf(orderCodStatus.getOrderPayment().getAmount()));
            if (orderCodStatus.getOrderRider().getDeliveredOn() != null && !orderCodStatus.getOrderRider().getDeliveredOn().isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = orderCodStatus.getOrderRider().getDeliveredOn();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                holder.ordersCodStatusBinding.orderdDate.setText(CommonUtils.getTimeFormatter(orderDateMills));
            } else {
                holder.ordersCodStatusBinding.orderdDate.setText("---");
            }
        } catch (Exception e) {
            System.out.println("OrdersCodStatusAdapter::::::::::::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return ordersCodStatusList.size();
    }

    public int getItemViewType(int position) {
        return ordersCodStatusList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        AdapterOrdersCodStatusBinding ordersCodStatusBinding;

        public ItemViewHolder(@NonNull AdapterOrdersCodStatusBinding ordersCodStatusBinding) {
            super(ordersCodStatusBinding.getRoot());
            this.ordersCodStatusBinding = ordersCodStatusBinding;
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
