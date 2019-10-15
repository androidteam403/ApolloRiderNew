package com.apollo.epos.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.adapter.CancelOrderItemsAdapter;
import com.apollo.epos.fragment.cancelorderitem.OnItemClickListener;
import com.apollo.epos.model.OrderItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelOrderActivity extends AppCompatActivity implements OnItemClickListener {
    @BindView(R.id.items_view_layout)
    protected LinearLayout itemsViewLayout;
    @BindView(R.id.cancelItemRecyclerView)
    RecyclerView cancelItemRecyclerView;
    private CancelOrderItemsAdapter cancelOrderItemsAdapter;
    @BindView(R.id.cancelled_list_layout)
    protected LinearLayout cancelledListLayout;
    @BindView(R.id.cancel_items_layout)
    protected LinearLayout cancelItemsLayout;
    @BindView(R.id.refund_total_amount)
    protected TextView refundTotalAmount;
    @BindView(R.id.total_payable_amount)
    protected TextView totalPayableAmount;
    @BindView(R.id.anim_parent_layout)
    protected LinearLayout animParentLayout;
    ArrayList<OrderItemModel> medicineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        setOrderItemsList();
    }

    private void getMedicineList() {
        medicineList.clear();
        OrderItemModel item = new OrderItemModel();
        item.setMedicineName("Tomoxifin 20mg");
        item.setQty("2");
        item.setItemPrice("75.00");
        item.setItemTotalPrice("150.00");
        item.setSelected(false);
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Atripla");
        item.setQty("1");
        item.setItemPrice("28.50");
        item.setItemTotalPrice("28.50");
        item.setSelected(false);
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Triaminic");
        item.setQty("1");
        item.setItemPrice("96.50");
        item.setItemTotalPrice("96.50");
        item.setSelected(false);
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Crocin");
        item.setQty("1");
        item.setItemPrice("102.00");
        item.setItemTotalPrice("102.00");
        item.setSelected(false);
        medicineList.add(item);
    }

    private void setOrderItemsList() {
        getMedicineList();
        cancelOrderItemsAdapter = new CancelOrderItemsAdapter(this, medicineList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        cancelItemRecyclerView.setLayoutManager(mLayoutManager);
        cancelItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cancelItemRecyclerView.setAdapter(cancelOrderItemsAdapter);
    }

    boolean isItemCancelled = false;

    @Override
    public void onItemClick(int position, boolean isSelected) {
        medicineList.get(position).setSelected(isSelected);
        cancelOrderItemsAdapter.notifyDataSetChanged();
        ArrayList<OrderItemModel> cancelledMedicineList = new ArrayList<>();
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                cancelledMedicineList.add(item);
            }
        }
        for (OrderItemModel item : medicineList) {
            if (item.isSelected()) {
                isItemCancelled = true;
                break;
            } else {
                isItemCancelled = false;
            }
        }
        if (isItemCancelled) {
            cancelItemsLayout.setVisibility(View.VISIBLE);
            showCancelledItems(cancelledMedicineList);
        } else {
            cancelItemsLayout.setVisibility(View.GONE);
        }
    }

    private void showCancelledItems(ArrayList<OrderItemModel> cancelledMedicineList) {
        cancelledListLayout.removeAllViews();
        double refundTotalAmt = 0.0;
        for (int i = 0; i < cancelledMedicineList.size(); i++) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_cancelled_item, null, false);
            TextView orderName = layout.findViewById(R.id.order_name);
            TextView orderQuantity = layout.findViewById(R.id.order_quantity);
            TextView itemPrice = layout.findViewById(R.id.item_price);
            TextView itemTotalPrice = layout.findViewById(R.id.item_total_price);
            ImageView cancelItemImg = layout.findViewById(R.id.cancel_item_img);

            orderName.setText(cancelledMedicineList.get(i).getMedicineName());
            orderQuantity.setText(cancelledMedicineList.get(i).getQty());
            itemPrice.setText(cancelledMedicineList.get(i).getItemPrice());
            itemTotalPrice.setText(cancelledMedicineList.get(i).getItemTotalPrice());
            cancelledListLayout.addView(layout);
            refundTotalAmt = refundTotalAmt + Double.parseDouble(cancelledMedicineList.get(i).getItemTotalPrice());
        }
//        cancelItemImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelledMedicineList.remove(cancelledMedicineList.get(i));
//            }
//        });
        refundTotalAmount.setText("-" + String.format("%.2f", refundTotalAmt));
        double totalPayableAmt = 377.00 - refundTotalAmt;
        totalPayableAmount.setText(String.format("%.2f", totalPayableAmt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final MenuItem menuCloseItem = menu.findItem(R.id.action_close);
        View actionNotificationView = MenuItemCompat.getActionView(menuCloseItem);
        actionNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuCloseItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close) {
            this.finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.update_order_btn)
    void onUpdateBtnClick() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
