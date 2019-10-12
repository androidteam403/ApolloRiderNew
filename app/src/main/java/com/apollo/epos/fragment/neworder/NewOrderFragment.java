package com.apollo.epos.fragment.neworder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.apollo.epos.R;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.ReachPharmacyActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.fragment.cancelorderitem.CancelOrderItemFragment;
import com.apollo.epos.fragment.deliveryorder.DeliveryOrderFragment;
import com.apollo.epos.model.OrderItemModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewOrderFragment extends Fragment {
    private Activity mActivity;
    @BindView(R.id.items_view_image)
    protected ImageView itemsViewImage;
    @BindView(R.id.items_view_layout)
    protected LinearLayout itemsViewLayout;
    @BindView(R.id.order_list_layout)
    protected LinearLayout orderListLayout;
    @BindView(R.id.btn_reject_order)
    Button rejectOrderBtn;
    String[] rejectReasons = {"Select from predefined statements", "Taken leave", "Not feeling well", "Soo far from my current location"};
    @BindView(R.id.order_delivery_time_layout)
    protected LinearLayout orderDeliveryTimeLayout;
    @BindView(R.id.anim_parent_layout)
    protected LinearLayout animParentLayout;

    public static NewOrderFragment newInstance() {
        return new NewOrderFragment();
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
        return inflater.inflate(R.layout.fragment_new_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Animation RightSwipe = AnimationUtils.loadAnimation(mActivity, R.anim.right_swipe);
        orderDeliveryTimeLayout.startAnimation(RightSwipe);
    }

    ArrayList<OrderItemModel> medicineList = new ArrayList<>();

    @OnClick(R.id.items_view_image)
    void onEyeImageClick() {
        if (itemsViewLayout.getVisibility() == View.VISIBLE) {
//            itemsViewLayout
//                    .animate()
//                    .setDuration(500)
//                    .alpha(0.0f)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            itemsViewLayout.setVisibility(View.GONE);
//                            itemsViewLayout.animate().setListener(null);
//                        }
//                    });
//            animParentLayout.setLayoutTransition(null);

            LayoutTransition lt = new LayoutTransition();
            lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            animParentLayout.setLayoutTransition(lt);


//            headerAnimation(itemsViewLayout);
            itemsViewLayout.setVisibility(View.GONE);
            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_open));
        } else {
            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(1000);
            transition.addTarget(R.id.anim_parent_layout);
            TransitionManager.beginDelayedTransition(animParentLayout, transition);

//            footerAnimation(itemsViewLayout);
            itemsViewLayout.setVisibility(View.VISIBLE);

//            itemsViewLayout.setVisibility(View.VISIBLE);
//            itemsViewLayout.setAlpha(0.0f);
//            itemsViewLayout
//                    .animate()
//                    .setDuration(1000)
//                    .alpha(1.0f)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            itemsViewLayout.animate().setListener(null);
//                        }
//                    });
            itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
            orderListLayout.removeAllViews();
            getMedicineList();
            for (int i = 0; i < medicineList.size(); i++) {
                LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
                LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);
                TextView orderName = layout.findViewById(R.id.order_name);
                TextView orderCount = layout.findViewById(R.id.order_count);
                TextView orderPrice = layout.findViewById(R.id.order_price);

                orderName.setText(medicineList.get(i).getMedicineName());
                orderCount.setText(medicineList.get(i).getQty());
                orderPrice.setText(medicineList.get(i).getItemPrice());
                orderListLayout.addView(layout);
            }
        }
    }

    private void getMedicineList() {
        medicineList.clear();
        OrderItemModel item = new OrderItemModel();
        item.setMedicineName("Tomoxifin 20mg");
        item.setQty("1");
        item.setItemPrice("78.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Atripla");
        item.setQty("1");
        item.setItemPrice("150.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Triaminic");
        item.setQty("1");
        item.setItemPrice("140.00");
        medicineList.add(item);

        item = new OrderItemModel();
        item.setMedicineName("Crocin");
        item.setQty("1");
        item.setItemPrice("150.00");
        medicineList.add(item);
    }

    @OnClick(R.id.btn_accept_order)
    public void onAcceptingOrder() {
        ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new DeliveryOrderFragment(), R.string.menu_take_order);
        ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);

//        Intent i = new Intent(mActivity, ReachPharmacyActivity.class);
//        startActivity(i);
//        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @OnClick(R.id.btn_reject_order)
    public void toggleBottomSheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.show();

        ImageView closeDialog = dialogView.findViewById(R.id.close_icon);
        closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, rejectReasons);
        reasonSpinner.setAdapter(customAdapter);
        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(mActivity," "+reasonSpinner[position])
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
////            btnBottomSheet.setText("Close sheet");
//        } else {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
////            btnBottomSheet.setText("Expand sheet");
//        }
    }

    private void toggle() {

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(600);
        transition.addTarget(R.id.items_view_layout);

        TransitionManager.beginDelayedTransition(animParentLayout, transition);
        itemsViewLayout.setVisibility(View.VISIBLE);
    }
}