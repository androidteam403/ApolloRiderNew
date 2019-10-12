package com.apollo.epos.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.cancelorderitem.OnItemClickListener;
import com.apollo.epos.model.MyOrdersItemModel;
import com.apollo.epos.model.OrderItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CancelOrderItemsAdapter extends RecyclerView.Adapter<CancelOrderItemsAdapter.MyViewHolder> {
    private Activity activity;
    ArrayList<OrderItemModel> medicineList;
    private OnItemClickListener listener;

    public CancelOrderItemsAdapter(Activity activity, ArrayList<OrderItemModel> medicineList, OnItemClickListener listener) {
        this.activity = activity;
        this.medicineList = medicineList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.medicineCheckBox)
        CheckBox medicineCheckBox;
        @BindView(R.id.order_name)
        TextView orderName;
        @BindView(R.id.order_quantity)
        TextView orderQuantity;
        @BindView(R.id.item_price)
        TextView itemPrice;
        @BindView(R.id.item_total_price)
        TextView itemTotalPrice;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cancel_order_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItemModel item = medicineList.get(position);
        holder.medicineCheckBox.setChecked(item.isSelected());
        holder.orderName.setText(item.getMedicineName());
        holder.orderQuantity.setText(item.getQty());
        holder.itemPrice.setText(item.getItemPrice());
        holder.itemTotalPrice.setText(item.getItemTotalPrice());

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onItemClick(position, !item.isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}
