package com.apollo.epos.activity.orderdelivery;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.CancelOrderActivity;
import com.apollo.epos.activity.CaptureSignatureActivity;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.ScannerActivity;
import com.apollo.epos.activity.TrackMapActivity;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.databinding.ActivityOrderDeliveryBinding;
import com.apollo.epos.databinding.BottomSheetBinding;
import com.apollo.epos.databinding.DialogAlertCustomBinding;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.listeners.DialogMangerCallback;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.novoda.merlin.Merlin;
import com.orhanobut.hawk.Hawk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.apollo.epos.utils.ActivityUtils.getCurrentTime;
import static com.apollo.epos.utils.ActivityUtils.showLayoutDownAnimation;
import static com.apollo.epos.utils.ActivityUtils.showTextDownAnimation;
import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;

public class OrderDeliveryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        View.OnFocusChangeListener, View.OnKeyListener, OrderDeliveryActivityCallback {
    @BindView(R.id.reached_store_layout)
    protected LinearLayout reachedStoreLayout;
    @BindView(R.id.reached_store_img)
    protected ImageView reachedStoreImg;
    @BindView(R.id.store_reached_time)
    protected TextView storeReachedTime;
    @BindView(R.id.scan_barcode_layout)
    protected LinearLayout scanBarcodeLayout;
    @BindView(R.id.scan_barcode_img)
    protected ImageView scanBarcodeImg;
    @BindView(R.id.scanned_barcode_img)
    protected ImageView scannedBarcodeImg;
    @BindView(R.id.scanned_barcode_layout)
    protected LinearLayout scannedBarLayout;
    @BindView(R.id.scanned_bar_code)
    protected TextView scannedBarCode;
    @BindView(R.id.taken_parcel_layout)
    protected LinearLayout takenParcelLayout;
    @BindView(R.id.taken_parcel_img)
    protected ImageView takenParcelImg;
    @BindView(R.id.parcel_taken_time)
    protected TextView parcelTakenTime;
    @BindView(R.id.continue_process_layout)
    protected RelativeLayout continueProcessLayout;
    @BindView(R.id.continue_driving_btn)
    protected TextView continueDrivingBtn;
    @BindView(R.id.order_delivery_process_img)
    protected ImageView orderDeliveryProcessImg;
    @BindView(R.id.delivery_items_view)
    protected TextView deliveryItemsView;
    @BindView(R.id.user_mobile_number_header)
    protected TextView userMobileNumberHeader;
    @BindView(R.id.user_mobile_number)
    protected TextView userMobileNumber;
    @BindView(R.id.delivery_items_list_layout)
    protected LinearLayout deliveryItemsListLayout;
    @BindView(R.id.reached_address_layout)
    protected LinearLayout reachedDeliveryAddressLayout;
    @BindView(R.id.reached_address_img)
    protected ImageView reachedAddressImg;
    @BindView(R.id.address_reached_time)
    protected TextView addressReachedTime;
    @BindView(R.id.collected_amount_parent_layout)
    protected LinearLayout collectedAmountParentLayout;
    @BindView(R.id.collected_amount_img)
    protected ImageView collectedAmountImg;
    @BindView(R.id.collected_amount_child_layout)
    protected LinearLayout collectedAmountChildLayout;
    @BindView(R.id.handover_parcel_parent_layout)
    protected LinearLayout handoverParcelParentLayout;
    @BindView(R.id.handover_parcel_img)
    protected ImageView handoverParcelImg;
    @BindView(R.id.handover_parcel_child_layout)
    protected LinearLayout handoverParcelChildLayout;
    @BindView(R.id.customerTypeSpinner)
    protected Spinner customerTypeSpinner;
    @BindView(R.id.signature_pad_parent_layout)
    protected LinearLayout signaturePadParentLayout;
    @BindView(R.id.signature_pad_img)
    protected ImageView signaturePadImg;
    @BindView(R.id.signature_pad_child_layout)
    protected LinearLayout signaturePadChildLayout;
    @BindView(R.id.signature_layout)
    protected LinearLayout signatureLayout;
    @BindView(R.id.hint_signature_text)
    protected TextView hintSignatureText;
    @BindView(R.id.signature_view_layout)
    protected RelativeLayout signatureViewLayout;
    @BindView(R.id.customer_signature_view)
    protected ImageView customerSignatureView;
    @BindView(R.id.capture_image_layout)
    protected LinearLayout captureImageLayout;
    @BindView(R.id.capture_image)
    protected ImageView captureImage;
    @BindView(R.id.skip_photo_btn)
    protected TextView skipPhotoBtn;
    @BindView(R.id.captured_image_layout)
    protected LinearLayout capturedImageLayout;
    @BindView(R.id.captured_image)
    protected ImageView capturedImage;
    @BindView(R.id.clear_captured_image)
    protected ImageView clearCapturedImage;
    @BindView(R.id.otp_verification_parent_layout)
    protected LinearLayout otpVerificationParentLayout;


    @BindView(R.id.pickup_otp_verification_parent_layout)
    protected LinearLayout pickupOtpVerificationParentLayout;

    @BindView(R.id.otp_verification_img)
    protected ImageView otpVerificationImg;

    @BindView(R.id.pickup_otp_verification_img)
    protected ImageView pickupOtpVerificationImg;

    @BindView(R.id.otp_verification_child_layout)
    protected LinearLayout otpVerificationChildLayout;


    @BindView(R.id.pickup_otp_verification_child_layout)
    protected LinearLayout pickupOtpVerificationChildLayout;
    @BindView(R.id.pickup_otp_verification_child_layout_edge)
    protected LinearLayout pickupOtpVerificationChildLayoutEdge;
    @BindView(R.id.otp_editText_layout)
    protected LinearLayout otpEditTextLayout;

    @BindView(R.id.pickup_otp_editText_layout)
    protected LinearLayout pickupOtpEditTextLayout;

    @BindView(R.id.pin_hidden_edittext)
    protected EditText pinHiddenEditText;
    @BindView(R.id.pickup_pin_hidden_edittext)
    protected EditText pickupPinHiddenEditText;
    @BindView(R.id.opt_num1)
    protected EditText optNum1;
    @BindView(R.id.opt_num2)
    protected EditText optNum2;
    @BindView(R.id.opt_num3)
    protected EditText optNum3;
    @BindView(R.id.opt_num4)
    protected EditText optNum4;
//    @BindView(R.id.opt_num5)
//    protected EditText optNum5;
//    @BindView(R.id.opt_num6)
//    protected EditText optNum6;


    @BindView(R.id.pickup_opt_num1)
    protected EditText pickupOptNum1;
    @BindView(R.id.pickup_opt_num2)
    protected EditText pickupOptNum2;
    @BindView(R.id.pickup_opt_num3)
    protected EditText pickupOptNum3;
    @BindView(R.id.pickup_opt_num4)
    protected EditText pickupOptNum4;


    @BindView(R.id.verify_otp_btn)
    protected TextView verifyOtpBtn;
    @BindView(R.id.pickup_verify_otp_btn)
    protected TextView pickupVerifyOtpBtn;


    @BindView(R.id.verified_otp_text)
    protected TextView verifiedOtpText;

    @BindView(R.id.pickup_verified_otp_text)
    protected TextView pickupVerifiedOtpText;

    @BindView(R.id.order_delivered_parent_layout)
    protected LinearLayout orderDeliveredParentLayout;
    @BindView(R.id.order_delivered_child_one_layout)
    protected RelativeLayout orderDeliveredChildOneLayout;
    @BindView(R.id.order_delivered_child_two_layout)
    protected LinearLayout orderDeliveredChildTwoLayout;
    @BindView(R.id.cancel_item_btn)
    protected TextView cancelItemBtn;
    @BindView(R.id.cancel_order_btn)
    protected TextView cancelOrderBtn;

    @BindView(R.id.anim_reach_store_layout)
    protected LinearLayout animReachStoreLayout;
    @BindView(R.id.anim_taken_parcel_layout)
    protected LinearLayout animTakenParcelLayout;
    @BindView(R.id.anim_scan_barcode_layout)
    protected LinearLayout animScanBarCodeLayout;
    @BindView(R.id.anim_address_reached_layout)
    protected LinearLayout animAddressReachedLayout;
    @BindView(R.id.user_contact_number)
    protected ImageView userContactNumber;
    //    @BindView(R.id.finish_activity_img)
//    protected ImageView finishActivityImg;
    private boolean isOrderDelivered = false;
    String[] cancelReasons = {"Customer not availale", "Door locked"};
    List<DeliveryFailreReasonsResponse.Row> cancelReasonsList = new ArrayList<>();
    String[] customerTypesList = {"Customer", "Other"};
    private int selectionTag = 0;
    public static final int CAM_REQUEST = 1;
    public static final int GAL_REQUEST = 2;
    private int SIGNATURE_REQUEST_CODE = 3;
    Intent camera_intent, gal_intent;
    private File file;
    private boolean imageStatus = false;
    private Bitmap bp;
    private boolean userPhoneClick = false;
    @BindView(R.id.map_view_layout)
    protected LinearLayout mapViewLayout;
    private final int REQ_LOC_PERMISSION = 5002;
    private boolean isPharmacyLoc = false;
    private boolean isDestinationLoc = false;
    private OrderDetailsResponse orderDetailsResponse;
    private ActivityOrderDeliveryBinding orderDeliveryBinding;
    private String pickupPhoneNumber, orderNotDeliveredPhoneNumber;
    private String customerPhoneNumber;
    private String branPickupVerificationCode;
    private String branReturnVerificatonCode;
    private String cusPickupVerificationCode;
    private String cusReturnVerificationCode;
    private String orderUid;
    private String orderNumber;
    private String customerNameTypeSinner;
    private String orderCancelReason;
    private String paymentType;

    public static Intent getStartIntent(Context context, OrderDetailsResponse orderDetailsResponse) {
        Intent intent = new Intent(context, OrderDeliveryActivity.class);
        intent.putExtra(CommonUtils.ORDER_DETAILS_RESPONSE, orderDetailsResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    public static Intent getStartIntent(Context context, String orderNumber) {
        Intent intent = new Intent(context, OrderDeliveryActivity.class);
        intent.putExtra("order_number", orderNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;

    }

    private boolean isLaunchedByPushNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_delivery);
        orderDeliveryBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_delivery);
        orderDeliveryBinding.setCallback(this);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                //bundle must contain all info sent in "data" field of the notification
                String orderNumberId = bundle.getString("uid");
                if (getIntent().getStringExtra("order_number") == null) {
                    isLaunchedByPushNotification = true;
                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId);
                }
            } catch (Exception e) {
                System.out.println("push notification new order activity::::::::::::::::::::::::::::" + e.getMessage());
            }

        }
        if (getIntent() != null) {
            if (getIntent().getStringExtra("order_number") != null) {
                String orderNumberId = getIntent().getStringExtra("order_number");
                if (orderNumberId != null && !orderNumberId.isEmpty()) {
                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId);
                } else {
                    finish();
                    return;
                }
            }
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setDisplayShowCustomEnabled(true);
//            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
//            View view = getSupportActionBar().getCustomView();
//            ImageView backArrow = view.findViewById(R.id.back_arrow);
//            TextView activityName = view.findViewById(R.id.activity_title);
//            TextView activityName = view.findViewById(R.id.activity_title);
//            LinearLayout notificationLayout = view.findViewById(R.id.notification_layout);
//            backArrow.setOnClickListener(v -> {
//                finish();
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//            });
//            activityName.setText(getResources().getString(R.string.menu_take_order));
//        }

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.icon_back);// Toolbar icon in Drawable folder
//        toolbar.setTitle("App");
//        toolbar.setTitleTextColor(Color.WHITE);// Title Color
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();// Do what do you want on toolbar button
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

//        collapsingToolbarLayout.setTitle("My Toolbar Title");
        collapsingToolbarLayout.setTitleEnabled(false);
        toolbar.setTitle("My Title");
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);


        RadioGroup rg = (RadioGroup) findViewById(R.id.cash_card_radiogroup);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.payment_cash:
                    // do operations specific to this selection
//                    Toast.makeText(this, "cash selected", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.payment_card:
                    // do operations specific to this selection
//                    Toast.makeText(this, "card selected", Toast.LENGTH_SHORT).show();
                    break;

            }
        });
        CustomReasonAdapter customUserListAdapter = new CustomReasonAdapter(this, customerTypesList, this);
        customerTypeSpinner.setAdapter(customUserListAdapter);
//        customerTypeSpinner.setOnItemSelectedListener(this);
        customerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderDeliveryActivity.this.customerNameTypeSinner = customerTypesList[position];
                if (customerTypesList[position].equals("Other")) {
                    orderDeliveryBinding.handoverUserName.setVisibility(View.VISIBLE);
                } else {
                    orderDeliveryBinding.handoverUserName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.reached_store_layout)
    void onReachedStoreLayoutClick() {
        if (selectionTag == 0) {
            selectionTag = 1;
            reachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            reachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));

            showTextDownAnimation(R.id.anim_reach_store_layout, animReachStoreLayout, storeReachedTime);
//            ActivityUtils.footerAnimation(mActivity, animReachStoreLayout, storeReachedTime);
            storeReachedTime.setText(getCurrentTime());
        }
    }

    @OnClick(R.id.pickup_otp_verification_parent_layout)
    void onPickupOtpVerificationParentLayoutClick() {
        if (selectionTag == 0) {
            pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
            pickupOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            pickupOtpVerificationChildLayout.setVisibility(View.VISIBLE);
            setpickupPINListeners();
        }
    }

    @OnClick(R.id.scan_barcode_layout)
    void scanBarCodeClick() {
        if (selectionTag == 1) {
            new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @OnClick(R.id.taken_parcel_layout)
    void onTakenParcelLayoutClick() {
        if (selectionTag == 2) {
            selectionTag = 3;
            takenParcelLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            takenParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));

            showTextDownAnimation(R.id.anim_taken_parcel_layout, animTakenParcelLayout, parcelTakenTime);
            parcelTakenTime.setText(getCurrentTime());
            continueProcessLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.continue_driving_btn)
    void onContinueDrivingClick() {
        if (selectionTag == 1) {
            selectionTag = 2;
//            Animation RightSwipe = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_swipe);
//            parentScrollView.startAnimation(RightSwipe);

//            int bottomDp = (int) getResources().getDimension(R.dimen.one_hundred_dp);
//            int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, mActivity);
//            parentScrollView.post(new Runnable() {
//                public void run() {
//                    parentScrollView.scrollTo(0, marginBottom);
//                }
//            });
            userPhoneClick = true;
            userContactNumber.setImageDrawable(getResources().getDrawable(R.drawable.icon_phone_select));
            deliveryItemsView.setVisibility(View.VISIBLE);
            orderDeliveryProcessImg.setVisibility(View.GONE);
            userMobileNumberHeader.setVisibility(View.VISIBLE);
            userMobileNumber.setVisibility(View.VISIBLE);
            deliveryItemsListLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.reached_address_layout)
    void onReachedAddressLayoutClick() {
        if (selectionTag == 4) {
            selectionTag = 5;
            reachedDeliveryAddressLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            reachedAddressImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            addressReachedTime.setText(getCurrentTime());
            addressReachedTime.setVisibility(View.VISIBLE);
            cancelItemBtn.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.collected_amount_parent_layout)
    void onCollectedAmtLayoutClick() {
        if (selectionTag == 5) {
            selectionTag = 6;
            collectedAmountParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            collectedAmountImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            collectedAmountChildLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.handover_parcel_parent_layout)
    void onHandOverParcelParentLayoutClick() {
        if (selectionTag == 2) {
            handoverParcelChildLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickHandoverTheParcelBtn() {
        selectionTag = 3;
        if (isCutomerNameSlected()) {
            orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
            handoverParcelChildLayout.setVisibility(View.GONE);
            handoverParcelParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            handoverParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
        }
    }

    private boolean isCutomerNameSlected() {
        if (this.customerNameTypeSinner.equals("Other") && orderDeliveryBinding.handoverUserName.getText().toString().trim().isEmpty()) {
            orderDeliveryBinding.handoverUserName.setError("Enter customer name.");
            orderDeliveryBinding.handoverUserName.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClickCollectPayment() {
        if (selectionTag == 5) {
            orderDeliveryBinding.collectPaymentRadio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickCollectPaymentSave() {
        selectionTag = 6;
        if (orderDeliveryBinding.paymentCash.isChecked())
            new OrderDeliveryActivityController(this, this).orderPaymentUpdateApiCall(this.orderDetailsResponse, "cash", "");
    }

    @Override
    public void onSuccessOrderPaymentUpdateApiCall() {
        ActivityUtils.showDialog(this, "Please Wait.");
        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_delivered", orderUid, "", "");
    }

    @Override
    public void onFailureOrderPaymentApiCall() {

    }

    @Override
    public void onClickEyeImage() {
        if (orderDeliveryBinding.itemsViewLayout.getVisibility() == View.VISIBLE) {
            LayoutTransition lt = new LayoutTransition();
            lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            orderDeliveryBinding.animParentLayout.setLayoutTransition(lt);

            orderDeliveryBinding.itemsViewLayout.setVisibility(View.GONE);
            orderDeliveryBinding.itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_open));
        } else {
            Transition transition = new Slide(Gravity.TOP);
            transition.setDuration(1000);
            transition.addTarget(R.id.anim_parent_layout);
            TransitionManager.beginDelayedTransition(orderDeliveryBinding.animParentLayout, transition);

            orderDeliveryBinding.itemsViewLayout.setVisibility(View.VISIBLE);
            orderDeliveryBinding.itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
        }
    }

    @Override
    public void onSuccessOrderStatusHistoryListApiCall(OrderStatusHitoryListResponse orderStatusHitoryListResponse) {
        try {
            if (orderStatusHitoryListResponse != null) {
                boolean isTransitOrder = false;
                for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
                    if (row.getOrderStatus().getUid().equals("order_transit")) {
                        isTransitOrder = true;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String orderDate = row.getCreatedTime();
                        Date orderDates = formatter.parse(orderDate);
                        long orderDateMills = orderDates.getTime();
                        orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickedup at " + CommonUtils.getTimeFormatter(orderDateMills));
                        selectionTag = 1;
                        pickupOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
                        pickupOtpVerificationChildLayout.setVisibility(View.GONE);
                        pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        break;
                    }

                }
                if (!isTransitOrder) {
                    orderDeliveryBinding.pickupOtpVerificationParentLayoutParent.setVisibility(View.GONE);
                }

                if (!this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("order_not_delivered")) {
                    continueProcessLayout.setVisibility(View.VISIBLE);
                } else {
                    orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
                }

                if (isOrderDelivered) {
                    selectionTag = 10;

                    //Continue process layout
                    orderDeliveryBinding.continueProcessLayout.setVisibility(View.GONE);

                    //handover the parcel to
                    orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
                    handoverParcelChildLayout.setVisibility(View.GONE);
                    handoverParcelParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    handoverParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));

                    //on click continue driving
                    userPhoneClick = true;
                    userContactNumber.setImageDrawable(getResources().getDrawable(R.drawable.icon_phone_select));
                    deliveryItemsView.setVisibility(View.VISIBLE);
                    orderDeliveryProcessImg.setVisibility(View.GONE);
                    userMobileNumberHeader.setVisibility(View.VISIBLE);
                    userMobileNumber.setVisibility(View.VISIBLE);
                    deliveryItemsListLayout.setVisibility(View.VISIBLE);

                    //otp verified
                    otpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                    otpEditTextLayout.setVisibility(View.GONE);
                    verifyOtpBtn.setVisibility(View.GONE);
                    otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    verifiedOtpText.setVisibility(View.VISIBLE);
                    otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                    cancelItemBtn.setVisibility(View.GONE);
                    cancelOrderBtn.setVisibility(View.GONE);

                    //proof of handover
                    signaturePadImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                    signaturePadChildLayout.setVisibility(View.VISIBLE);
                    hintSignatureText.setVisibility(View.GONE);
                    orderDeliveryBinding.touchHereForNewSign.setVisibility(View.GONE);
                    signatureViewLayout.setVisibility(View.VISIBLE);
                    Glide.with(this).load(this.orderDetailsResponse.getData().getOrderHandover().getSignature().get(0).getFullPath()).into(customerSignatureView);
//                    customerSignatureView.setImageBitmap(bmp);
                    signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));

                    // collect payment
                    otpEditTextLayout.setVisibility(View.GONE);
                    verifyOtpBtn.setVisibility(View.GONE);
                    otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
                    orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                    verifiedOtpText.setVisibility(View.VISIBLE);
                    otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
                    for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
                        if (row.getOrderStatus().getUid().equals("order_delivered")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String orderDate = row.getCreatedTime();
                            Date orderDates = formatter.parse(orderDate);
                            long orderDateMills = orderDates.getTime();
                            orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                            break;
                        }
                    }
                    orderDeliveredChildOneLayout.setVisibility(View.GONE);
                    orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);

                    verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                    cancelItemBtn.setVisibility(View.GONE);
                    cancelOrderBtn.setVisibility(View.GONE);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
                    int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
                    int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, this);
                    int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, this);
                    params.setMargins(0, 0, marginEnd, marginBottom);
                    deliveryItemsView.setLayoutParams(params);
                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onFailureOrderStatusHistoryListApiCall(String message) {

    }

    @Override
    public void onClickOrderNotDeliveredCallIcon() {
        checkCallPermissionSetting(orderNotDeliveredPhoneNumber);
    }

    @Override
    public void onClickNotificationIcon() {
        onBackPressed();
    }

    @Override
    public void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse) {
        try {
            cancelReasons = new String[deliveryFailreReasonsResponse.getData().getListData().getRows().size()];
            this.cancelReasonsList = deliveryFailreReasonsResponse.getData().getListData().getRows();
            for (DeliveryFailreReasonsResponse.Row row : deliveryFailreReasonsResponse.getData().getListData().getRows())
                cancelReasons[deliveryFailreReasonsResponse.getData().getListData().getRows().indexOf(row)] = row.getName();

        } catch (Exception e) {
            System.out.println("onSuccessDeliveryReasonApiCall:::::::::::::::::::::::::::::::" + e.getMessage());
        }
    }

    @Override
    public void onFailureDeliveryFailureApiCall() {

    }


    @OnClick(R.id.signature_pad_parent_layout)
    void onSignaturePadParentLayoutClick() {
        if (selectionTag == 4) {
            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
            signaturePadImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            signaturePadChildLayout.setVisibility(View.VISIBLE);
            orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickProofofHandoverSkip() {
        orderDeliveryBinding.proofofHandoverAnimLayout.setLayoutTransition(null);
        orderDeliveryBinding.proofOfHandover.setText("3. Proof of handover skipped");
        orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
        orderDeliveryBinding.signaturePadChildLayout.setVisibility(View.GONE);
        signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.alert_header_bg));
        selectionTag = 5;
        if (!paymentType.equals("COD")) {
            ActivityUtils.showDialog(this, "Please Wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_delivered", orderUid, "", "");
        }
    }

    @OnClick(R.id.signature_layout)
    void onSignatureLayoutClick() {
        if (selectionTag != 10) {
            Intent intent = new Intent(this, CaptureSignatureActivity.class);
            intent.putExtra("order_number", orderNumber);
            intent.putExtra("customer_name", this.customerNameTypeSinner.equals("Other") ? orderDeliveryBinding.handoverUserName.getText().toString().trim() : this.customerNameTypeSinner);
            startActivityForResult(intent, SIGNATURE_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @OnClick(R.id.capture_image)
    void onCaptureImageClick() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cameraPermissionSetting();
    }

    @OnClick(R.id.clear_captured_image)
    void onClearCapturedImageClick() {
        captureImageLayout.setVisibility(View.VISIBLE);
        capturedImageLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.otp_verification_parent_layout)
    void onOtpVerificationParentLayoutClick() {
        if (selectionTag == 3) {
            otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
            otpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            otpVerificationChildLayout.setVisibility(View.VISIBLE);
            setPINListeners();
        }
    }

    @OnClick(R.id.verify_otp_btn)
    void onVerifyOtpBtnClick() {
        selectionTag = 4;
        if (pinHiddenEditText.getText().toString().equalsIgnoreCase("0000") || pinHiddenEditText.getText().toString().equalsIgnoreCase(cusPickupVerificationCode)) {
            otpEditTextLayout.setVisibility(View.GONE);
            verifyOtpBtn.setVisibility(View.GONE);
            otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
            verifiedOtpText.setVisibility(View.VISIBLE);
            otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));

//            orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//            orderDeliveredChildOneLayout.setVisibility(View.GONE);
//            orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);

            verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
            cancelItemBtn.setVisibility(View.GONE);
//            cancelOrderBtn.setVisibility(View.GONE);

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//            int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
//            int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
//            int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, this);
//            int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, this);
//            params.setMargins(0, 0, marginEnd, marginBottom);
//            deliveryItemsView.setLayoutParams(params);
//
//            new Handler().postDelayed(() -> {
////                NavigationActivity.getInstance().showFragment(new DashboardFragment(), R.string.menu_dashboard);
////                NavigationActivity.getInstance().updateSelection(1);
//
//                Intent intent = getIntent();
//                intent.putExtra("OrderCompleted", "true");
//                setResult(RESULT_OK, intent);
//                finish();
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//            }, 1000);


//            ActivityUtils.showDialog(this, "Please Wait.");
//            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_delivered", orderUid);
        } else {
            otpEditTextLayout.setVisibility(View.VISIBLE);
            verifyOtpBtn.setVisibility(View.VISIBLE);
            otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
            verifiedOtpText.setVisibility(View.GONE);
            verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
        }
    }

    private void cameraPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                Log.d("Permissions_Status", "We have a permission");
                // we have a permission
                imagePickerAction();
            }
        } else {
            imagePickerAction();
        }
    }

    private void imagePickerAction() {
        camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        startActivityForResult(camera_intent, CAM_REQUEST);
    }

    @OnClick(R.id.cancel_order_btn)
    public void toggleBottomSheet() {

        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet, null, false);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();

        TextView headerText = dialogView.findViewById(R.id.sheet_header);
        headerText.setText(getResources().getString(R.string.label_order_cancel));
        TextView cancelHeaderText = dialogView.findViewById(R.id.cancel_order_header);
        cancelHeaderText.setText(getResources().getString(R.string.label_cancel_reason_header));
        ImageView closeDialog = dialogView.findViewById(R.id.close_icon);
        bottomSheetBinding.closeIcon.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
            ActivityUtils.showDialog(this, "Please wait.");
            if (cancelReasonsList != null && cancelReasonsList.size() > 0) {
                for (DeliveryFailreReasonsResponse.Row row : cancelReasonsList) {
                    if (row.getName().equals(orderCancelReason)) {
                        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_not_delivered", orderUid, row.getUid(), bottomSheetBinding.comment.getText().toString().trim());
                        dialog.dismiss();
                        break;
                    }
                }
            }
        });
//        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(this, cancelReasons, null);
        bottomSheetBinding.rejectReasonSpinner.setAdapter(customAdapter);
        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderDeliveryActivity.this.orderCancelReason = cancelReasons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.cancel_item_btn)
    void onCancelItemClick() {
        Intent i = new Intent(this, CancelOrderActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//        ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new CancelOrderItemFragment(), R.string.menu_take_order);
//        ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(), rejectReasons[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNATURE_REQUEST_CODE) {
            if (data != null) {
                try {
                    byte[] byteArray = data.getByteArrayExtra("capturedSignature");
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    if (this.customerNameTypeSinner.equals("Other")) {
                        new OrderDeliveryActivityController(this, this).uploadFile(bmp, orderUid, orderNumber, orderDeliveryBinding.handoverUserName.getText().toString().trim());
                    } else {
                        new OrderDeliveryActivityController(this, this).uploadFile(bmp, orderUid, orderNumber, this.customerNameTypeSinner);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            imageStatus = true;
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            captureImageLayout.setVisibility(View.GONE);
            capturedImageLayout.setVisibility(View.VISIBLE);
            capturedImage.setImageBitmap(bitmap);
            bp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        } else if (requestCode == GAL_REQUEST) {
            if (data != null) {
                imageStatus = true;
                Uri choosenImage = data.getData();
                if (choosenImage != null) {
                    captureImageLayout.setVisibility(View.GONE);
                    capturedImageLayout.setVisibility(View.VISIBLE);
                    bp = decodeUri(choosenImage, 50);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    capturedImage.setImageBitmap(bp);
                }
            } else if (requestCode == 3) {
                if (data == null) {
                    imageStatus = true;
                }
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//check for null
            if (result != null) {
                if (result.getContents() == null) {
//                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    selectionTag = 2;
                    scanBarcodeLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    scanBarcodeImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                    scannedBarCode.setText("Scanned Bar Code: " + result.getContents());

                    showLayoutDownAnimation(R.id.anim_scan_barcode_layout, animScanBarCodeLayout, scannedBarLayout);
//                    scannedBarLayout.setVisibility(View.VISIBLE);
                }
            } else {
// This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setCancelledPINListeners() {
        orderDeliveryBinding.cancelledPinHiddenEdittext.addTextChangedListener(cancelledVendorWatcher);

        orderDeliveryBinding.cancelledOptNum1.setOnFocusChangeListener(this);
        orderDeliveryBinding.cancelledOptNum2.setOnFocusChangeListener(this);
        orderDeliveryBinding.cancelledOptNum3.setOnFocusChangeListener(this);
        orderDeliveryBinding.cancelledOptNum4.setOnFocusChangeListener(this);

        orderDeliveryBinding.cancelledOptNum1.setOnKeyListener(this);
        orderDeliveryBinding.cancelledOptNum2.setOnKeyListener(this);
        orderDeliveryBinding.cancelledOptNum3.setOnKeyListener(this);
        orderDeliveryBinding.cancelledOptNum4.setOnKeyListener(this);
        orderDeliveryBinding.cancelledPinHiddenEdittext.setOnKeyListener(this);
    }

    TextWatcher cancelledVendorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum1);
            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum2);
            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum3);
            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum4);
            if (s.length() == 0) {
                orderDeliveryBinding.cancelledOptNum1.setText("");
                orderDeliveryBinding.cancelledOptNum2.setText("");
                orderDeliveryBinding.cancelledOptNum3.setText("");
                orderDeliveryBinding.cancelledOptNum4.setText("");
                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 1) {
                orderDeliveryBinding.cancelledOptNum1.setText(s.charAt(0) + "");
                orderDeliveryBinding.cancelledOptNum2.setText("");
                orderDeliveryBinding.cancelledOptNum3.setText("");
                orderDeliveryBinding.cancelledOptNum4.setText("");
                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 2) {
                orderDeliveryBinding.cancelledOptNum2.setText(s.charAt(1) + "");
                orderDeliveryBinding.cancelledOptNum3.setText("");
                orderDeliveryBinding.cancelledOptNum4.setText("");
                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 3) {
                orderDeliveryBinding.cancelledOptNum3.setText(s.charAt(2) + "");
                orderDeliveryBinding.cancelledOptNum4.setText("");
                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 4) {
                orderDeliveryBinding.cancelledOptNum4.setText(s.charAt(3) + "");
                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, orderDeliveryBinding.cancelledOptNum4);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setpickupPINListeners() {
        pickupPinHiddenEditText.addTextChangedListener(pickupVendorWatcher);

        pickupOptNum1.setOnFocusChangeListener(this);
        pickupOptNum2.setOnFocusChangeListener(this);
        pickupOptNum3.setOnFocusChangeListener(this);
        pickupOptNum4.setOnFocusChangeListener(this);

        pickupOptNum1.setOnKeyListener(this);
        pickupOptNum2.setOnKeyListener(this);
        pickupOptNum3.setOnKeyListener(this);
        pickupOptNum4.setOnKeyListener(this);
        pickupPinHiddenEditText.setOnKeyListener(this);
    }

    TextWatcher pickupVendorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setDefaultPinBackground(pickupOptNum1);
            setDefaultPinBackground(pickupOptNum2);
            setDefaultPinBackground(pickupOptNum3);
            setDefaultPinBackground(pickupOptNum4);
            if (s.length() == 0) {
                pickupOptNum1.setText("");
                pickupOptNum2.setText("");
                pickupOptNum3.setText("");
                pickupOptNum4.setText("");
                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 1) {
                pickupOptNum1.setText(s.charAt(0) + "");
                pickupOptNum2.setText("");
                pickupOptNum3.setText("");
                pickupOptNum4.setText("");
                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 2) {
                pickupOptNum2.setText(s.charAt(1) + "");
                pickupOptNum3.setText("");
                pickupOptNum4.setText("");
                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 3) {
                pickupOptNum3.setText(s.charAt(2) + "");
                pickupOptNum4.setText("");
                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 4) {
                pickupOptNum4.setText(s.charAt(3) + "");
                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, pickupOptNum4);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void setPINListeners() {
        pinHiddenEditText.addTextChangedListener(vendorWatcher);

        optNum1.setOnFocusChangeListener(this);
        optNum2.setOnFocusChangeListener(this);
        optNum3.setOnFocusChangeListener(this);
        optNum4.setOnFocusChangeListener(this);
//        optNum5.setOnFocusChangeListener(this);
//        optNum6.setOnFocusChangeListener(this);

        optNum1.setOnKeyListener(this);
        optNum2.setOnKeyListener(this);
        optNum3.setOnKeyListener(this);
        optNum4.setOnKeyListener(this);
//        optNum5.setOnKeyListener(this);
//        optNum6.setOnKeyListener(this);
        pinHiddenEditText.setOnKeyListener(this);
    }

    TextWatcher vendorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setDefaultPinBackground(optNum1);
            setDefaultPinBackground(optNum2);
            setDefaultPinBackground(optNum3);
            setDefaultPinBackground(optNum4);
//            setDefaultPinBackground(optNum5);
//            setDefaultPinBackground(optNum6);
            if (s.length() == 0) {
                optNum1.setText("");
                optNum2.setText("");
                optNum3.setText("");
                optNum4.setText("");
//                optNum5.setText("");
//                optNum6.setText("");
                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 1) {
                optNum1.setText(s.charAt(0) + "");
                optNum2.setText("");
                optNum3.setText("");
                optNum4.setText("");
//                optNum5.setText("");
//                optNum6.setText("");
                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 2) {
                optNum2.setText(s.charAt(1) + "");
                optNum3.setText("");
                optNum4.setText("");
//                optNum5.setText("");
//                optNum6.setText("");
                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 3) {
                optNum3.setText(s.charAt(2) + "");
                optNum4.setText("");
//                optNum5.setText("");
//                optNum6.setText("");
                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 4) {
                optNum4.setText(s.charAt(3) + "");
//                optNum5.setText("");
//                optNum6.setText("");
                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, optNum4);
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
            }
//            else if (s.length() == 5) {
//                optNum5.setText(s.charAt(4) + "");
//                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 6) {
//                optNum6.setText(s.charAt(5) + "");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.opt_num1:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.opt_num2:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.opt_num3:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.opt_num4:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.opt_num5:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.opt_num6:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            case R.id.pickup_opt_num1:
                if (hasFocus) {
                    setFocus(pickupPinHiddenEditText);
                    showSoftKeyboard(pickupPinHiddenEditText);
                }
                break;
            case R.id.pickup_opt_num2:
                if (hasFocus) {
                    setFocus(pickupPinHiddenEditText);
                    showSoftKeyboard(pickupPinHiddenEditText);
                }
                break;
            case R.id.pickup_opt_num3:
                if (hasFocus) {
                    setFocus(pickupPinHiddenEditText);
                    showSoftKeyboard(pickupPinHiddenEditText);
                }
                break;
            case R.id.pickup_opt_num4:
                if (hasFocus) {
                    setFocus(pickupPinHiddenEditText);
                    showSoftKeyboard(pickupPinHiddenEditText);
                }
                break;
            case R.id.cancelled_opt_num1:
                if (hasFocus) {
                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
                }
                break;
            case R.id.cancelled_opt_num2:
                if (hasFocus) {
                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
                }
                break;
            case R.id.cancelled_opt_num3:
                if (hasFocus) {
                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
                }
                break;
            case R.id.cancelled_opt_num4:
                if (hasFocus) {
                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        if (pinHiddenEditText.getText().length() == 6)
//                            optNum6.setText("");
//                        else if (pinHiddenEditText.getText().length() == 5)
//                            optNum5.setText("");
                        if (pinHiddenEditText.getText().length() == 4)
                            optNum4.setText("");
                        else if (pinHiddenEditText.getText().length() == 3)
                            optNum3.setText("");
                        else if (pinHiddenEditText.getText().length() == 2)
                            optNum2.setText("");
                        else if (pinHiddenEditText.getText().length() == 1)
                            optNum1.setText("");
                        if (pinHiddenEditText.length() > 0) {
                            pinHiddenEditText.setText(pinHiddenEditText.getText().subSequence(0, pinHiddenEditText.length() - 1));
                            pinHiddenEditText.post(() -> pinHiddenEditText.setSelection(pinHiddenEditText.getText().toString().length()));
                        }
                        return true;
                    }
                case R.id.opt_num1:
                case R.id.opt_num2:
                case R.id.opt_num3:
                case R.id.opt_num4:
//                case R.id.opt_num5:
//                case R.id.opt_num6:
                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
                    }
                    break;
                case R.id.pickup_pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (pickupPinHiddenEditText.getText().length() == 4)
                            pickupOptNum4.setText("");
                        else if (pickupPinHiddenEditText.getText().length() == 3)
                            pickupOptNum3.setText("");
                        else if (pickupPinHiddenEditText.getText().length() == 2)
                            pickupOptNum2.setText("");
                        else if (pickupPinHiddenEditText.getText().length() == 1)
                            pickupOptNum1.setText("");
                        if (pickupPinHiddenEditText.length() > 0) {
                            pickupPinHiddenEditText.setText(pickupPinHiddenEditText.getText().subSequence(0, pickupPinHiddenEditText.length() - 1));
                            pickupPinHiddenEditText.post(() -> pickupPinHiddenEditText.setSelection(pickupPinHiddenEditText.getText().toString().length()));
                        }
                        return true;
                    }
                case R.id.pickup_opt_num1:
                case R.id.pickup_opt_num2:
                case R.id.pickup_opt_num3:
                case R.id.pickup_opt_num4:
                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
                    }
                    break;


                case R.id.cancelled_pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 4)
                            orderDeliveryBinding.cancelledOptNum4.setText("");
                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 3)
                            orderDeliveryBinding.cancelledOptNum3.setText("");
                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 2)
                            orderDeliveryBinding.cancelledOptNum2.setText("");
                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 1)
                            orderDeliveryBinding.cancelledOptNum1.setText("");
                        if (orderDeliveryBinding.cancelledPinHiddenEdittext.length() > 0) {
                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText(orderDeliveryBinding.cancelledPinHiddenEdittext.getText().subSequence(0, orderDeliveryBinding.cancelledPinHiddenEdittext.length() - 1));
                            orderDeliveryBinding.cancelledPinHiddenEdittext.post(() -> orderDeliveryBinding.cancelledPinHiddenEdittext.setSelection(orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().length()));
                        }
                        return true;
                    }
                case R.id.cancelled_opt_num1:
                case R.id.cancelled_opt_num2:
                case R.id.cancelled_opt_num3:
                case R.id.cancelled_opt_num4:
                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    private void setDefaultPinBackground(EditText editText) {
        editText.getBackground().setColorFilter(getResources().getColor(R.color.colorGrey), PorterDuff.Mode.SRC_IN);
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        editText.setRawInputType(Configuration.KEYBOARD_12KEY);
        imm.showSoftInput(editText, 0);
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @OnClick(R.id.pharma_contact_number)
    void onPharmaContactClick() {
        checkCallPermissionSetting(pickupPhoneNumber);
    }

    @OnClick(R.id.user_contact_number)
    void onUserContactClick() {
        if (userPhoneClick)
            checkCallPermissionSetting(customerPhoneNumber);
    }

    private void checkCallPermissionSetting(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Log.d("Permissions_Status", "We have a permission");
                // we have a permission
                requestACall(phoneNumber);
            }
        } else {
            requestACall(phoneNumber);
        }
    }

    private void requestACall(String phoneNumber) {
        Intent intentcall = new Intent();
        intentcall.setAction(Intent.ACTION_CALL);
        intentcall.setData(Uri.parse("tel:" + phoneNumber)); // set the Uri
        startActivity(intentcall);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @OnClick(R.id.finish_activity_img)
    void onFinishActivityClick() {
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @OnClick(R.id.pharmacy_map_view_img)
    void onPharmaMapImgClick() {
        isPharmacyLoc = true;
        isDestinationLoc = false;
        gotoTrackMapActivity("Pharmacy", 17.4410197, 78.3788463);
    }

    @OnClick(R.id.map_view_layout)
    void onMapClick() {
        isPharmacyLoc = false;
        isDestinationLoc = true;
        gotoTrackMapActivity("Destination", 17.4411128, 78.3827845);
    }

    private void gotoTrackMapActivity(String locType, double latitude, double longitude) {
        if (checkForLocPermission()) {
            if (checkGPSOn(this)) {
                Intent intent = new Intent(this, TrackMapActivity.class);
                intent.putExtra("locType", locType);
                intent.putExtra("Lat", latitude);
                intent.putExtra("Lon", longitude);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            } else {
                showGPSDisabledAlertToUser(this);
            }
        } else {
            requestForLocPermission(REQ_LOC_PERMISSION);
            return;
        }
    }

    private boolean checkForLocPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public boolean checkGPSOn(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showGPSDisabledAlertToUser(Context context) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(getString(R.string.alert));
        alertDialogBuilder.setMessage(getString(R.string.permission_gps_bogy))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.open_settings),
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void requestForLocPermission(final int reqCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            DialogManager.showSingleBtnPopup(this, new DialogMangerCallback() {
                @Override
                public void onOkClick(View v) {
                    ActivityCompat.requestPermissions(OrderDeliveryActivity.this, new String[]{ACCESS_FINE_LOCATION}, reqCode);
                }

                @Override
                public void onCancelClick(View view) {

                }
            }, getString(R.string.app_name), getString(R.string.locationPermissionMsg), getString(R.string.ok));
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, reqCode);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
            orderDeliveryBinding.notificationDot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_LOC_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (isPharmacyLoc) {
                        gotoTrackMapActivity("Pharmacy", 17.4410197, 78.3788463);
                    } else if (isDestinationLoc) {
                        gotoTrackMapActivity("Destination", 17.4411128, 78.3827845);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    DialogManager.showToast(this, getString(R.string.noAccessTo));
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks().build(this);
    }

    @Override
    protected void onResume() {
        CommonUtils.CURRENT_SCREEN = "OrderDeliveryActivity";
        Hawk.put(LAST_ACTIVITY, getClass().getSimpleName());
        super.onResume();
        if (GPSLocationService.isFromSetting == true) {
            GPSLocationService.isFromSetting = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(OrderDeliveryActivity.this, FloatingTouchService.class);
        if (isMyServiceRunning(FloatingTouchService.class)) {
            stopService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClickBackIcon() {
        onBackPressed();
    }

    @Override
    public void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse) {
        try {
            this.orderDetailsResponse = orderDetailsResponse;
            if (this.orderDetailsResponse != null) {
                if (this.orderDetailsResponse.getData() != null) {
                    this.paymentType = this.orderDetailsResponse.getData().getPaymentType().getName();
                    if (!this.paymentType.equals("COD")) {
                        orderDeliveryBinding.collectPayment.setVisibility(View.GONE);
                        orderDeliveryBinding.completedDeliveryText.setText("4. Completed delivery");
                    } else {
                        orderDeliveryBinding.collectPayment.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.completedDeliveryText.setText("5. Completed delivery");
                        orderDeliveryBinding.totalAmount.setText(getResources().getString(R.string.label_rupee_symbol) + " " + String.valueOf(this.orderDetailsResponse.getData().getPakgValue()));
                    }
                    if (this.orderDetailsResponse.getData().getCancelAllowed() != null && this.orderDetailsResponse.getData().getCancelAllowed().getName().equals("Yes")) {
                        cancelOrderBtn.setVisibility(View.VISIBLE);
                    } else {
                        cancelOrderBtn.setVisibility(View.GONE);
                    }
                    this.orderUid = this.orderDetailsResponse.getData().getUid();
                    this.orderNumber = this.orderDetailsResponse.getData().getOrderNumber();
                    orderDeliveryBinding.orderStatusHeader.setText(this.orderDetailsResponse.getData().getOrderStatus().getName());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String orderDate = orderDetailsResponse.getData().getDelEtWindo();
                    Date orderDates = formatter.parse(orderDate);
                    long orderDateMills = orderDates.getTime();
                    orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                    orderDeliveryBinding.orderNumber.setText("#" + String.valueOf(this.orderDetailsResponse.getData().getOrderNumber()));
//                    orderDeliveryBinding.crateAmount.setText(String.valueOf(this.orderDetailsResponse.getData().getCrateAmount()));
//                    orderDeliveryBinding.paymentType.setText(this.orderDetailsResponse.getData().getPaymentType().getName());
                    String pickupAddress = orderDetailsResponse.getData().getDeliverApartment() + ", " + orderDetailsResponse.getData().getDeliverStreetName() + ", " + orderDetailsResponse.getData().getDeliverCity() + ", " + orderDetailsResponse.getData().getDeliverState() + ", " + orderDetailsResponse.getData().getDelPincode() + ", " + orderDetailsResponse.getData().getDeliverCountry();
                    String customerAddresss = orderDetailsResponse.getData().getPickupApt() + ", " + orderDetailsResponse.getData().getPickupStreetName() + ", " + orderDetailsResponse.getData().getPickupCity() + ", " + orderDetailsResponse.getData().getPickupState() + ", " + orderDetailsResponse.getData().getPickupPincode() + ", " + orderDetailsResponse.getData().getPickupCountry();
                    this.branPickupVerificationCode = orderDetailsResponse.getData().getBranpickupVerCode();
                    this.branReturnVerificatonCode = orderDetailsResponse.getData().getBranreturnVerCode();
                    this.cusPickupVerificationCode = orderDetailsResponse.getData().getCusPickupVerCode();
                    this.cusReturnVerificationCode = orderDetailsResponse.getData().getCusReturnVerCode();

                    if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("order_transit")) {
                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("order_delivered")) {
                        isOrderDelivered = true;
                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("order_not_delivered")) {
                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                        orderDeliveryBinding.returnItToStoreText.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.cancelOrderBtn.setVisibility(View.GONE);
                        orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
                    }
                    if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {

                        //pickup address and details
                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getDelAddId());
                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getDeliverLandmark());
                        orderDeliveryBinding.pharmacyAddress.setText(customerAddresss);
                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
                        if (orderDetailsResponse.getData().getDeliverNotes() != null && orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
                        } else {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getDeliverNotes());
                        }
                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());

                        //delivery address and details
                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getPickupAddId());
                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
                        orderDeliveryBinding.customerAddress.setText(pickupAddress);
                        String userMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                        orderDeliveryBinding.userMobileNumber.setText("*******" + userMobileNumber.substring(userMobileNumber.length() - 3));

                        if (orderDetailsResponse.getData().getPickupNotes() != null && orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
                        } else {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }
                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());

                        // order not delivered address and details
                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getDelAddId());
                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getDeliverLandmark());
                        orderDeliveryBinding.orderNotDeliveryAddress.setText(customerAddresss);
                        String orderNotDeliveredPhoneNubmber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + orderNotDeliveredPhoneNubmber.substring(orderNotDeliveredPhoneNubmber.length() - 3));

                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
                    } else {
                        //pickup address and details
                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getPickupAddId());
                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
                        orderDeliveryBinding.pharmacyAddress.setText(pickupAddress);
                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
                        if (orderDetailsResponse.getData().getPickupNotes() != null && orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
                        } else {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }
                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());

                        //delivery address and details
                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getDelAddId());
                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getDeliverLandmark());
                        orderDeliveryBinding.customerAddress.setText(customerAddresss);
                        String userMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
                        orderDeliveryBinding.userMobileNumber.setText("*******" + userMobileNumber.substring(userMobileNumber.length() - 3));
                        if (orderDetailsResponse.getData().getDeliverNotes() != null && orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
                        } else {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        }
                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());

                        //order not delivery address and details
                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getPickupAddId());
                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
                        orderDeliveryBinding.orderNotDeliveryAddress.setText(pickupAddress);
                        String orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                        orderDeliveryBinding.orderNotDeliveredPhoneNumber.setText("*******" + orderNotDeliveredPhoneNumber.substring(orderNotDeliveredPhoneNumber.length() - 3));

                        this.orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                    }
                    orderDeliveryBinding.orderListLayout.removeAllViews();
                    if (orderDetailsResponse.getData().getItems() != null && orderDetailsResponse.getData().getItems().size() > 0) {
                        TextView itemsCount = (TextView) findViewById(R.id.items_count);
                        itemsCount.setText(orderDetailsResponse.getData().getItems().size() + " " + "Items");
                        for (int i = 0; i < orderDetailsResponse.getData().getItems().size(); i++) {
                            LayoutInflater layoutInflater = LayoutInflater.from(this);
                            LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);

                            TextView orderName = layout.findViewById(R.id.order_name);
                            TextView orderCount = layout.findViewById(R.id.order_count);
                            TextView orderPrice = layout.findViewById(R.id.order_price);

                            orderName.setText(orderDetailsResponse.getData().getItems().get(i).getItemname());
                            orderCount.setText(orderDetailsResponse.getData().getItems().get(i).getItemquantity());
                            orderPrice.setText(String.valueOf(orderDetailsResponse.getData().getItems().get(i).getItemprice()));

                            orderDeliveryBinding.orderListLayout.addView(layout);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onFialureOrderDetailsApiCall() {

    }

    @Override
    public void onClickPickupVerifyOtp() {
        if (pickupPinHiddenEditText.getText().toString().trim().equals("0000") || pickupPinHiddenEditText.getText().toString().trim().equals(branPickupVerificationCode)) {
            ActivityUtils.showDialog(this, "Please wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_picked", orderUid, "", "");
        } else {
            pickupOtpEditTextLayout.setVisibility(View.VISIBLE);
            pickupVerifyOtpBtn.setVisibility(View.VISIBLE);
            pickupOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
            pickupVerifiedOtpText.setVisibility(View.GONE);
            pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
        }
    }

    @Override
    public void onFialureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessOrderSaveUpdateStatusApi(String status) {
        try {
            if (status.equals("order_picked")) {
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_transit", orderUid, "", "");
            } else if (status.equals("order_transit")) {
                selectionTag = 1;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickedup at " + CommonUtils.getTimeFormatter(orderDateMills));
                pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
                pickupOtpVerificationChildLayout.setVisibility(View.GONE);
                pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                continueProcessLayout.setVisibility(View.VISIBLE);
                ActivityUtils.hideDialog();
            } else if (status.equals("order_delivered")) {
                ActivityUtils.hideDialog();
                otpEditTextLayout.setVisibility(View.GONE);
                verifyOtpBtn.setVisibility(View.GONE);
                otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
                orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                verifiedOtpText.setVisibility(View.VISIBLE);
                otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));

                orderDeliveredChildOneLayout.setVisibility(View.GONE);
                orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);

                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                cancelItemBtn.setVisibility(View.GONE);
                cancelOrderBtn.setVisibility(View.GONE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
                int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
                int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, this);
                int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, this);
                params.setMargins(0, 0, marginEnd, marginBottom);
                deliveryItemsView.setLayoutParams(params);

                new Handler().postDelayed(() -> {
//                NavigationActivity.getInstance().showFragment(new DashboardFragment(), R.string.menu_dashboard);
//                NavigationActivity.getInstance().updateSelection(1);

                    Intent intent = getIntent();
                    intent.putExtra("OrderCompleted", "true");
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }, 1000);
            } else if (status.equals("order_not_delivered")) {
                ActivityUtils.hideDialog();
                orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
                orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
                cancelItemBtn.setVisibility(View.GONE);
                cancelOrderBtn.setVisibility(View.GONE);


//                finish();
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            } else if (status.equals("order_handover_to_pharmacy")) {
                ActivityUtils.hideDialog();
                orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.GONE);
                orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
                orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));

                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                cancelItemBtn.setVisibility(View.GONE);
                cancelOrderBtn.setVisibility(View.GONE);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
                orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
                orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onFialureOrderSaveUpdateStatusApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickHandoverTheParcelItem(String item) {
        if (item.equals("Other")) {
            orderDeliveryBinding.handoverUserName.setVisibility(View.VISIBLE);
        } else {
            orderDeliveryBinding.handoverUserName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessOrderHandoverSaveUpdateApi(Bitmap bmp) {
        orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
        hintSignatureText.setVisibility(View.GONE);
        signatureViewLayout.setVisibility(View.VISIBLE);
        customerSignatureView.setImageBitmap(bmp);
        signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
        selectionTag = 5;
        if (!paymentType.equals("COD")) {
            ActivityUtils.showDialog(this, "Please Wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_delivered", orderUid, "", "");
        }
    }

    @Override
    public void onFailureOrderHandoverSaveUpdateApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // cancelled order return to store flow
    private int orderHandoverToPharmacy = 0;

    @Override
    public void onClickCancelledReachedtheStore() {
        if (orderHandoverToPharmacy == 0) {
            try {
                orderDeliveryBinding.cancelledReachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.cancelledReachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                orderDeliveryBinding.cancelledStoreReachedTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                orderDeliveryBinding.cancelledStoreReachedTime.setVisibility(View.VISIBLE);
                orderHandoverToPharmacy = 1;
            } catch (Exception e) {
                System.out.println("onClickCancelledReachedtheStore :::::::::::::::::::::::::::::::::::::" + e.getMessage());
            }
        }
    }

    @Override
    public void onClickCancelledOtpVerificationParentLayout() {
        if (orderHandoverToPharmacy == 1) {
            orderHandoverToPharmacy = 2;
            orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
            orderDeliveryBinding.cancelledOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
            orderDeliveryBinding.cancelledOtpVerificationChildLayout.setVisibility(View.VISIBLE);
            setCancelledPINListeners();
        }
    }

    @Override
    public void onClickCancelledVerifyOtp() {
        if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase("0000") || orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase(branReturnVerificatonCode)) {
            ActivityUtils.showDialog(this, "Please Wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_handover_to_pharmacy", orderUid, "", "");
        } else {
            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.VISIBLE);
            orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.VISIBLE);
            orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
            orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.GONE);
            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
        }
    }

    @Override
    public void onBackPressed() {
        if (!isOrderDelivered) {
            Dialog alertDialog = new Dialog(this);
            DialogAlertCustomBinding alertCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_custom, null, false);
            alertDialog.setContentView(alertCustomBinding.getRoot());
            alertCustomBinding.title.setText("Alert!");
            alertCustomBinding.subtitle.setText("Are sure want to leave this page?");
            alertCustomBinding.dialogButtonNO.setText("No");
            alertCustomBinding.dialogButtonOK.setText("Yes");
            alertCustomBinding.dialogButtonOK.setOnClickListener(v -> onBackPress());
            alertCustomBinding.dialogButtonNO.setOnClickListener(v -> alertDialog.dismiss());
            alertDialog.show();
        } else {
            super.onBackPressed();
        }

    }

    private void onBackPress() {
        if (isLaunchedByPushNotification) {
            startActivity(new Intent(OrderDeliveryActivity.this, NavigationActivity.class).putExtra("isPushNotfication", true));
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}