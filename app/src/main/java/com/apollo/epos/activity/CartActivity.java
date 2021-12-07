package com.apollo.epos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.adapter.CartItemAdapter;
import com.apollo.epos.adapter.SearchedItemAdapter;
import com.apollo.epos.fragment.takeneworder.OnItemClickListener;
import com.apollo.epos.model.OrderItemModel;
import com.novoda.merlin.Merlin;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity implements OnItemClickListener {
    @BindView(R.id.searchedItemRecyclerView)
    RecyclerView searchedItemRecyclerView;
    ArrayList<OrderItemModel> medicineList = new ArrayList<>();
    private CartItemAdapter cartItemsAdapter;
    @BindView(R.id.continue_btn_text)
    protected TextView continueBtnText;
    private int itemQty = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        setSearchedItemsList();
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    private void setSearchedItemsList() {
        getMedicineList();
        cartItemsAdapter = new CartItemAdapter(this, medicineList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchedItemRecyclerView.setLayoutManager(mLayoutManager);
        searchedItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchedItemRecyclerView.setAdapter(cartItemsAdapter);
    }

    private void getMedicineList() {
        medicineList.clear();
        OrderItemModel item = new OrderItemModel();
        item.setMedicineName("Dolamide Tab");
        item.setMedicineDesc("by RANBAXY LABORATORIES LTD");
        item.setMedicineQty("10 Tablet(s) in a strip");
        item.setQty("2");
        item.setDiscountPrice("54.40*");
        item.setItemPrice("68.00");
        item.setItemTotalPrice("108.80");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Atripla");
        item.setMedicineDesc("by SYSTOPIC LTD");
        item.setMedicineQty("10 Tablet(s) in a strip");
        item.setQty("1");
        item.setDiscountPrice("36.00*");
        item.setItemPrice("45.00");
        item.setItemTotalPrice("72.80");
        medicineList.add(item);
    }


    boolean isItemSelected = false;
    private int selectedCnt = 0;

    @Override
    public void onItemClick(int position, boolean isSelected) {
//        medicineList.get(position).setSelected(isSelected);
//        cartItemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDecrementClick(int position) {
        itemQty = Integer.parseInt(medicineList.get(position).getQty());
        if (itemQty > 1) {
            itemQty = itemQty - 1;
            medicineList.get(position).setQty(String.valueOf(itemQty));
            cartItemsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onIncrementClick(int position) {
        itemQty = Integer.parseInt(medicineList.get(position).getQty());
        itemQty = itemQty + 1;
        medicineList.get(position).setQty(String.valueOf(itemQty));
        cartItemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
