package com.apollo.epos.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.takeneworder.OnItemClickListener;
import com.apollo.epos.model.OrderItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchedItemAdapter extends RecyclerView.Adapter<SearchedItemAdapter.MyViewHolder> {
    private Activity activity;
    ArrayList<OrderItemModel> medicineList;
    private OnItemClickListener listener;

    public SearchedItemAdapter(Activity activity, ArrayList<OrderItemModel> medicineList, OnItemClickListener listener) {
        this.activity = activity;
        this.medicineList = medicineList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.medicine_info_layout)
        LinearLayout medicineInfoLayout;
        @BindView(R.id.medicineCheckBox)
        CheckBox medicineCheckBox;
        @BindView(R.id.medicine_name)
        TextView medicineName;
        @BindView(R.id.medicine_info)
        TextView medicineInfo;
        @BindView(R.id.qty_decrement)
        ImageView qtyDecrement;
        @BindView(R.id.item_qnty)
        TextView itemQnty;
        @BindView(R.id.qty_increment)
        ImageView qtyIncrement;
        @BindView(R.id.item_total_price)
        TextView itemTotalPrice;
        @BindView(R.id.parent_layout)
        LinearLayout parentLayout;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_searched_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItemModel item = medicineList.get(position);
        holder.medicineName.setText(item.getMedicineName());
        holder.medicineInfo.setText(item.getMedicineDesc());
        holder.itemQnty.setText(item.getQty());
        holder.itemTotalPrice.setText(item.getItemTotalPrice());
        holder.medicineCheckBox.setChecked(item.isSelected());

        if (item.isSelected()) {
            holder.parentLayout.setBackground(activity.getDrawable(R.drawable.selected_order_item_bg));
        } else {
            holder.parentLayout.setBackground(null);
        }

        holder.medicineInfoLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, !item.isSelected());
            }
        });
        holder.qtyDecrement.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDecrementClick(position);
            }
        });
        holder.qtyIncrement.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIncrementClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}
