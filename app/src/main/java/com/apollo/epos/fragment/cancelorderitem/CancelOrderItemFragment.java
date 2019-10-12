package com.apollo.epos.fragment.cancelorderitem;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.adapter.CancelOrderItemsAdapter;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.adapter.MyOrdersListAdapter;
import com.apollo.epos.fragment.deliveryorder.DeliveryOrderFragment;
import com.apollo.epos.model.OrderItemModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CancelOrderItemFragment extends Fragment implements OnItemClickListener {
    private Activity mActivity;
    @BindView(R.id.items_view_image)
    protected ImageView itemsViewImage;
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
    @BindView(R.id.order_delivery_time_layout)
    protected LinearLayout orderDeliveryTimeLayout;
    String[] rejectReasons = {"Select from predefined statements", "Taken leave", "Not feeling well", "Soo far from my current location"};

    public static CancelOrderItemFragment newInstance() {
        return new CancelOrderItemFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancel_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setOrderItemsList();
        Animation RightSwipe = AnimationUtils.loadAnimation(mActivity, R.anim.right_swipe);
        orderDeliveryTimeLayout.startAnimation(RightSwipe);
    }

    ArrayList<OrderItemModel> medicineList = new ArrayList<>();

    @OnClick(R.id.items_view_image)
    void onEyeImageClick() {
        if (itemsViewLayout.getVisibility() == View.VISIBLE) {
            LayoutTransition lt = new LayoutTransition();
            lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            animParentLayout.setLayoutTransition(lt);

            itemsViewLayout.setVisibility(View.GONE);
            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_open));
        } else {
            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(1000);
            transition.addTarget(R.id.anim_parent_layout);
            TransitionManager.beginDelayedTransition(animParentLayout, transition);

            itemsViewLayout.setVisibility(View.VISIBLE);
            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
            setOrderItemsList();
        }
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
        cancelOrderItemsAdapter = new CancelOrderItemsAdapter(mActivity, medicineList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
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
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_cancelled_item, null, false);
            TextView orderName = layout.findViewById(R.id.order_name);
            TextView orderQuantity = layout.findViewById(R.id.order_quantity);
            TextView itemPrice = layout.findViewById(R.id.item_price);
            TextView itemTotalPrice = layout.findViewById(R.id.item_total_price);

            orderName.setText(cancelledMedicineList.get(i).getMedicineName());
            orderQuantity.setText(cancelledMedicineList.get(i).getQty());
            itemPrice.setText(cancelledMedicineList.get(i).getItemPrice());
            itemTotalPrice.setText(cancelledMedicineList.get(i).getItemTotalPrice());
            cancelledListLayout.addView(layout);
            refundTotalAmt = refundTotalAmt + Double.parseDouble(cancelledMedicineList.get(i).getItemTotalPrice());
        }
        refundTotalAmount.setText("-" + String.format("%.2f", refundTotalAmt));
        double totalPayableAmt = 377.00 - refundTotalAmt;
        totalPayableAmount.setText(String.format("%.2f", totalPayableAmt));
    }
}