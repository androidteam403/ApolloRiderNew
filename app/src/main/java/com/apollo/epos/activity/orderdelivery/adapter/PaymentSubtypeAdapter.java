package com.apollo.epos.activity.orderdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.login.model.OrderPaymentTypeResponse;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivityCallback;
import com.apollo.epos.databinding.AdapterPaymentSubtypeBinding;

import java.util.List;

public class PaymentSubtypeAdapter extends RecyclerView.Adapter<PaymentSubtypeAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderPaymentTypeResponse.Row> paymentSubtypeList;
    private OrderDeliveryActivityCallback mCallback;

    public PaymentSubtypeAdapter(Context mContext, List<OrderPaymentTypeResponse.Row> paymentSubtypeList, OrderDeliveryActivityCallback mCallback) {
        this.mContext = mContext;
        this.paymentSubtypeList = paymentSubtypeList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public PaymentSubtypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPaymentSubtypeBinding paymentSubtypeBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_payment_subtype, parent, false);
        return new PaymentSubtypeAdapter.ViewHolder(paymentSubtypeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentSubtypeAdapter.ViewHolder holder, int position) {
        OrderPaymentTypeResponse.Row row = paymentSubtypeList.get(position);
        holder.adapterPaymentSubtypeBinding.setRow(row);
        holder.itemView.setOnClickListener(v -> {
            mCallback.onSelectPaymentSubtype(row, position);
        });
    }

    @Override
    public int getItemCount() {
        return paymentSubtypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterPaymentSubtypeBinding adapterPaymentSubtypeBinding;

        public ViewHolder(@NonNull AdapterPaymentSubtypeBinding adapterPaymentSubtypeBinding) {
            super(adapterPaymentSubtypeBinding.getRoot());
            this.adapterPaymentSubtypeBinding = adapterPaymentSubtypeBinding;
        }
    }
}
