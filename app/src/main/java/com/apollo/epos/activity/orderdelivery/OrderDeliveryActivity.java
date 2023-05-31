package com.apollo.epos.activity.orderdelivery;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.apollo.epos.utils.ActivityUtils.getCurrentTime;
import static com.apollo.epos.utils.ActivityUtils.showLayoutDownAnimation;
import static com.apollo.epos.utils.ActivityUtils.showTextDownAnimation;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.PiontsCallback;
import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.CancelOrderActivity;
import com.apollo.epos.activity.CaptureSignatureActivity;
import com.apollo.epos.activity.ScannerActivity;
import com.apollo.epos.activity.login.LoginActivity;
import com.apollo.epos.activity.navigation.NavigationActivity;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.onlinepayment.OnlinePaymentActivity;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderPaymentSelectResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollo.epos.activity.trackmap.TrackMapActivity;
import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.databinding.ActivityOrderDeliveryBinding;
import com.apollo.epos.databinding.BottomSheetBinding;
import com.apollo.epos.databinding.DialogAlertCustomBinding;
import com.apollo.epos.databinding.DialogAlertMessageBinding;
import com.apollo.epos.dialog.DialogManager;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.listeners.DialogMangerCallback;
import com.apollo.epos.service.FloatingTouchService;
import com.apollo.epos.service.GPSLocationService;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.AppConstants;
import com.apollo.epos.utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.novoda.merlin.Merlin;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class OrderDeliveryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        View.OnFocusChangeListener, View.OnKeyListener, OrderDeliveryActivityCallback, DirectionApiCallback, TaskLoadedCallback, PiontsCallback {
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
    protected View orderDeliveryProcessImg;
    @BindView(R.id.delivery_items_view)
    protected View deliveryItemsView;
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
    protected TextView mapViewLayout;
    private final int REQ_LOC_PERMISSION = 5002;
    private boolean isPharmacyLoc = false;
    private boolean isDestinationLoc = false;
    private boolean isStoreLoc = false;
    private OrderDetailsResponse orderDetailsResponse;
    private ActivityOrderDeliveryBinding orderDeliveryBinding;
    private String pickupPhoneNumber, orderNotDeliveredPhoneNumber;
    private String customerPhoneNumber;

    private String branPickupVerificationCode = "00000000";
    private String branReturnVerificatonCode = "00000000";//
    private String cusPickupVerificationCode = "00000000";
    private String cusDeliveryVerificationCode = "00000000";//

    private boolean isBranPickupVerificationCode = false; // forward flow pickup verification
    private boolean isBranReturnVerificatonCode = false; //
    private boolean isCusPickupVerificationCode = false; // return flow pickup verification
    private boolean isCusDeliveryVerificationCode = false; // forward Delivery verification

    private String orderUid;
    private String orderNumber;
    private String customerNameTypeSinner;
    private String orderCancelReason;
    private String paymentType;
    private String transactionId = "";
    private static TextView notificationText;
    private int orderCurrentStatus = 0; // order assigned =1, order in transit =2, order delivered =3, order not delivered =4, order handover to pharmacy =5.

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

    public static Intent getStartIntent(Context context, String orderNumber, boolean isLaunchedByPushNotification) {
        Intent intent = new Intent(context, OrderDeliveryActivity.class);
        intent.putExtra("order_number", orderNumber);
        intent.putExtra("ORDER_ASSIGNED", isLaunchedByPushNotification);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    private boolean isLaunchedByPushNotification;

    private Handler liveDistanceCalculateHandler = new Handler();
    private Runnable liveDistanceCalculateRunnable = new Runnable() {
        @Override
        public void run() {
//            Toast.makeText(OrderDeliveryActivity.this, getSessionManager().getRiderTravelledDistanceinDay() + "m", Toast.LENGTH_SHORT).show();
            liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
            liveDistanceCalculateHandler.postDelayed(liveDistanceCalculateRunnable, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSessionManager().setRiderTravelledDistanceinDay("0.0");
        orderDeliveryBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_delivery);
        orderDeliveryBinding.setCallback(this);
        ButterKnife.bind(this);
        if (getSessionManager().getOrderPaymentTypeList() != null && getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 0) {
            orderDeliveryBinding.paymentCash.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(0).getName());
            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 1)
                orderDeliveryBinding.paymentCard.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(1).getName());
            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 2)
                orderDeliveryBinding.paymentWallet.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(2).getName());

        }
        orderDeliveryBinding.loadingWhiteScreen.setVisibility(View.VISIBLE);
        notificationText = (TextView) findViewById(R.id.notification_dot);
        isScreen = true;
        if (getSessionManager().getDeliveryFailureReasonseList() != null)
            onSuccessDeliveryReasonApiCall(getSessionManager().getDeliveryFailureReasonseList());
        if (getSessionManager().getNotificationStatus()) {
            orderDeliveryBinding.notificationDot.setVisibility(View.VISIBLE);
            anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(350); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            orderDeliveryBinding.notificationDot.startAnimation(anim);
        } else {
            orderDeliveryBinding.notificationDot.setVisibility(View.GONE);
        }

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            try {
//                //bundle must contain all info sent in "data" field of the notification
//                String orderNumberId = bundle.getString("uid");
//                if (getIntent().getStringExtra("order_number") == null) {
//                    isLaunchedByPushNotification = true;
//                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId, orderDeliveryBinding);
//                }
//            } catch (Exception e) {
//                System.out.println("push notification new order activity::::::::::::::::::::::::::::" + e.getMessage());
//            }
//
//        }
        if (getIntent() != null) {
            if (getIntent().getStringExtra("order_number") != null) {
                String orderNumberId = getIntent().getStringExtra("order_number");
                isLaunchedByPushNotification = getIntent().getBooleanExtra("ORDER_ASSIGNED", false);
                if (orderNumberId != null && !orderNumberId.isEmpty()) {
                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId, orderDeliveryBinding);
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
//                    Toast.makeText(this, "online selected", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.payment_wallet:
                    // do operations specific to this selection
//                    Toast.makeText(this, "wallet selected", Toast.LENGTH_SHORT).show();
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
            onClickDeliveredLabel();
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
            hideKeyboard();
            orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
            orderDeliveryBinding.handedoverParceltoCustomerName.setVisibility(View.VISIBLE);
            if (this.customerNameTypeSinner.equals("Other"))
                orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + orderDeliveryBinding.handoverUserName.getText().toString().trim() + " )");
            else
                orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + this.customerNameTypeSinner + " )");
            orderDeliveryBinding.handoverToParcel.setText("1. Handedover Parcel");
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

    private String codCardCash = null;

    @Override
    public void onClickCollectPaymentSave() {
        selectionTag = 6;
        if (orderDeliveryBinding.paymentCash.isChecked()) {
            this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(0).getUid();
            new OrderDeliveryActivityController(this, this).orderPaymentUpdateApiCall(this.orderDetailsResponse, "cash", "", "");
        } else if (orderDeliveryBinding.paymentCard.isChecked()) {
            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 1)
                this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(1).getUid();
        } else if (orderDeliveryBinding.paymentWallet.isChecked()) {
            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 2)
                this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(2).getUid();
            startActivityForResult(OnlinePaymentActivity.getStartIntent(this, orderDetailsResponse), CommonUtils.ONLINE_PAYMENT_ACTIVITY);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @Override
    public String getCodCardCash() {
        return codCardCash;
    }

    @Override
    public void onSuccessOrderPaymentUpdateApiCall() {
        ActivityUtils.showDialog(this, "Please Wait.");
        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
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
                    if (row.getOrderStatus().getUid() != null && (row.getOrderStatus().getUid().equals("PICKUP") || row.getOrderStatus().getUid().equals("OUTFORDELIVERY") || row.getOrderStatus().getUid().equals("RETURNPICKED"))) {
                        isTransitOrder = true;
                        orderCurrentStatus = 2;
                        isOrderPicked = true;
                        orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String orderDate = row.getCreatedTime();
                        Date orderDates = formatter.parse(orderDate);
                        long orderDateMills = orderDates.getTime();
                        orderDeliveryBinding.pickupOtpVerifyText.setText("1. Picked up " + CommonUtils.getTimeFormatter(orderDateMills));
//                        selectionTag = 1;
                        pickupOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
                        pickupOtpVerificationChildLayout.setVisibility(View.GONE);
                        pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                        orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));

                        //pickup header
                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));

                        orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                        orderDeliveryBinding.pickupDetailsProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                        orderDeliveryBinding.pickupDetailsInnerHead.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.pickupDetailsInnerHead.setText("Order Picked");
                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText("Order Picked");
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    }

                }
                if (!isTransitOrder) {
                    orderDeliveryBinding.pickupOtpVerificationParentLayoutParent.setVisibility(View.GONE);
                }

                if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
//                    continueProcessLayout.setVisibility(View.VISIBLE);
                    cancelOrderBtn.setVisibility(View.GONE);
                    orderDeliveryBinding.customerheadName.setText("Delivery Failed");
//                    orderDeliveryBinding.cancelledOtpVerificationParentLayoutParent.setVisibility(View.GONE);
//                    orderDeliveryBinding.handovertoPharmacy.setText("2. Handedover to pharmacy");

                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
                    orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    orderCurrentStatus = 4;
//                    onClickDelivered();
                    orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
                    if (!this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                        orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
                    }


                    if (!isTransitOrder) {
                        isOrderCancelledandNotPicked = true;
                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                        orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                        Dialog alertDialog = new Dialog(this);
                        DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
                        alertDialog.setContentView(alertMessageBinding.getRoot());
                        alertDialog.setCancelable(false);
                        alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                            alertDialog.dismiss();
                            finish();
                        });
                        alertDialog.show();
                    }
                }

                if (isOrderDelivered) {
                    selectionTag = 10;
                    orderCurrentStatus = 3;
                    orderDeliveryBinding.actionBarDeliverBy.setText("Delivered at: ");
                    if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
                        if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
                            orderDeliveryBinding.orderStatusHeader.setText("Cancel Order To Store");
                            orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                            orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                            orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
                        } else {
                            orderDeliveryBinding.orderStatusHeader.setText("Order Delivered");
                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
                        }
                        orderDeliveryBinding.delivered.setVisibility(View.GONE);
                        orderHandoverToPharmacy = 2;
                        orderDeliveryBinding.cancelledReachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.cancelledReachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        orderDeliveryBinding.cancelledOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.cancelledOtpVerificationChildLayout.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null ||
                                orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                            orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
                        }
                        ActivityUtils.hideDialog();
                        orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));

                        orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Order Returned");
                        orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Order Returned");
                        orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));


                        orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                        orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.GONE);
                        orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                        orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));

                        orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                        cancelItemBtn.setVisibility(View.GONE);
                        cancelOrderBtn.setVisibility(View.GONE);
                        orderDeliveryBinding.cancelledOrderSuccessStatus.setText("Order Returned to store");

                        for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
                            if (row.getOrderStatus().getUid().equals("DELIVERED")) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String orderDate = row.getCreatedTime();
                                Date orderDates = formatter.parse(orderDate);
                                long orderDateMills = orderDates.getTime();
                                orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                                orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                                break;
                            }
                        }


                        orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
                        orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
                        orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
                    } else {
                        if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null ||
                                orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty()) {
                            orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
                            orderDeliveryBinding.verifiedOtpText.setText("Verification completed successfully");
                        }
                        if (this.paymentType.equals("COD"))
                            new OrderDeliveryActivityController(this, this).getOrderPaymentTypeinCod(orderUid);
                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));

                        orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                        orderDeliveryBinding.deliverInnerHeader.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.deliverInnerHeader.setText("Order Delivered");
                        orderDeliveryBinding.deliveryItemsView.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                        //Continue process layout
                        orderDeliveryBinding.continueProcessLayout.setVisibility(View.GONE);

                        //handover the parcel to
                        orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
                        handoverParcelChildLayout.setVisibility(View.GONE);
                        handoverParcelParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        handoverParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));

                        //continue driving
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
                        otpVerificationChildLayout.setVisibility(View.VISIBLE);
                        verifiedOtpText.setVisibility(View.VISIBLE);
                        otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                        cancelItemBtn.setVisibility(View.GONE);
                        cancelOrderBtn.setVisibility(View.GONE);

                        //proof of handover
                        signaturePadImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        signaturePadChildLayout.setVisibility(View.VISIBLE);
                        hintSignatureText.setVisibility(View.GONE);
                        if (this.orderDetailsResponse.getData().getOrderHandover() != null && this.orderDetailsResponse.getData().getOrderHandover().getHandoverTo() != null) {
                            orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + this.orderDetailsResponse.getData().getOrderHandover().getHandoverTo() + " )");
                            orderDeliveryBinding.handedoverParceltoCustomerName.setVisibility(View.VISIBLE);
                        }
                        orderDeliveryBinding.touchHereForNewSign.setVisibility(View.GONE);
                        signatureViewLayout.setVisibility(View.VISIBLE);
                        if (this.orderDetailsResponse.getData().getOrderHandover().getSignature().size() > 0) {
                            Glide.with(this).load(this.orderDetailsResponse.getData().getOrderHandover().getSignature().get(0).getFullPath()).into(customerSignatureView);
                            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        } else {
                            orderDeliveryBinding.proofofHandoverAnimLayout.setLayoutTransition(null);
                            orderDeliveryBinding.proofOfHandover.setText("3. Proof of handover skipped");
                            orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
                            orderDeliveryBinding.signaturePadChildLayout.setVisibility(View.GONE);
                            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.alert_header_bg));
                        }
                        //                    customerSignatureView.setImageBitmap(bmp);

                        // collect payment
                        otpEditTextLayout.setVisibility(View.GONE);
                        verifyOtpBtn.setVisibility(View.GONE);
                        otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                        orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
                        orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                        verifiedOtpText.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.collectPaymentText.setText("4. Collected Payment");
                        otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                        orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
                        for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
                            if (row.getOrderStatus().getUid().equals("DELIVERED")) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String orderDate = row.getCreatedTime();
                                Date orderDates = formatter.parse(orderDate);
                                long orderDateMills = orderDates.getTime();
                                orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                                orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                                //deliver header
                                orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                                orderDeliveryBinding.customerheadName.setText("Order Delivered");
//                            orderDeliveryBinding.deliverNameHeadLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                                orderDeliveryBinding.deliverNameHeadCompletedIcon.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        orderCurrentStatus = 0;
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
//                    deliveryItemsView.setLayoutParams(params);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("onSuccessOrderStatusHistoryListApiCall=============================" + e.getMessage());
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
        if (getSessionManager().getNotificationStatus()) {
            if (isOrderDelivered) {
                notificationText.clearAnimation();
                NavigationActivity.notificationDotVisibility(false);
                DashboardFragment.newOrderViewVisibility(false);
//                getSessionManager().setNotificationStatus(false);

                startActivity(NavigationActivity.getStartIntent(this));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            } else {
                Dialog alertDialog = new Dialog(this);
                DialogAlertCustomBinding alertCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_custom, null, false);
                alertDialog.setContentView(alertCustomBinding.getRoot());
                alertCustomBinding.title.setText("Alert!");
                alertCustomBinding.subtitle.setText("Are sure want to leave this page?");
                alertCustomBinding.dialogButtonNO.setText("No");
                alertCustomBinding.dialogButtonOK.setText("Yes");
                alertCustomBinding.dialogButtonOK.setOnClickListener(v -> {
                    notificationText.clearAnimation();
                    NavigationActivity.notificationDotVisibility(false);
                    DashboardFragment.newOrderViewVisibility(false);
//                    getSessionManager().setNotificationStatus(false);

                    startActivity(NavigationActivity.getStartIntent(this));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    alertDialog.dismiss();
                    finish();
                });
                alertCustomBinding.dialogButtonNO.setOnClickListener(v -> alertDialog.dismiss());
                alertDialog.show();
            }
        } else {
            Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
        }
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

//        onBackPressed();
    }

    static Animation anim;
    private static boolean isScreen;

    public static void notificationDotVisibility(boolean show) {
        if (isScreen) {
            if (show) {
                notificationText.setVisibility(View.VISIBLE);
                anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(350); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                notificationText.startAnimation(anim);
            } else {
                notificationText.setVisibility(View.GONE);
            }
        }
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
        if (this.customerNameTypeSinner.equals("Other")) {
            new OrderDeliveryActivityController(this, this).orderHandoverSaveUpdate(null, null, orderUid, orderNumber, orderDeliveryBinding.handoverUserName.getText().toString().trim());
        } else {
            new OrderDeliveryActivityController(this, this).orderHandoverSaveUpdate(null, null, orderUid, orderNumber, this.customerNameTypeSinner);
        }
        orderDeliveryBinding.proofofHandoverAnimLayout.setLayoutTransition(null);
        orderDeliveryBinding.proofOfHandover.setText("3. Proof of handover skipped");
        orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
        orderDeliveryBinding.signaturePadChildLayout.setVisibility(View.GONE);
        signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.alert_header_bg));
        selectionTag = 5;
        if (!paymentType.equals("COD")) {
            ActivityUtils.showDialog(this, "Please Wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
        }
    }

    @Override
    public void onClickReturnLabel() {
        if (orderCurrentStatus != 1) {
            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
            orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickOrderNotDelivered() {
        if (orderCurrentStatus != 2 && orderCurrentStatus != 4) {
            orderDeliveryBinding.returnLabel.setVisibility(View.VISIBLE);
            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.GONE);
        }
    }

    private boolean isOrderCancelledandNotPicked = false;

    @Override
    public void onClickPickedLabel() {
        if (!isOrderCancelledandNotPicked) {
            expanded = true;
            orderDeliveryBinding.packedLabel.setVisibility(View.GONE);
            orderDeliveryBinding.packed.setVisibility(View.VISIBLE);
        }
    }

    private boolean expanded;

    @Override
    public void onClickPicked() {
        if (orderCurrentStatus != 1) {
            expanded = false;
            orderDeliveryBinding.packedLabel.setVisibility(View.VISIBLE);
            orderDeliveryBinding.packed.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickDeliveredLabel() {
        if (orderCurrentStatus != 1 && orderCurrentStatus != 3 && orderCurrentStatus != 4 && orderCurrentStatus != 5) {
            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
            orderDeliveryBinding.delivered.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickDelivered() {
        if (orderCurrentStatus != 2) {
            orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
            orderDeliveryBinding.delivered.setVisibility(View.GONE);
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

        if (isCusDeliveryVerificationCode || pinHiddenEditText.getText().toString().equalsIgnoreCase(cusDeliveryVerificationCode)) {
            selectionTag = 4;
            hideKeyboard();
            otpEditTextLayout.setVisibility(View.GONE);
            verifyOtpBtn.setVisibility(View.GONE);
            otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
            verifiedOtpText.setVisibility(View.VISIBLE);
            otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
            if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null ||
                    orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty()) {
                orderDeliveryBinding.verifiedOtpText.setText("Verification completed successfully");
            }
            verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
            cancelItemBtn.setVisibility(View.GONE);
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
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet, null, false);
        dialog.setContentView(bottomSheetBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();

        bottomSheetBinding.sheetHeader.setText(R.string.label_change_order_status);
        bottomSheetBinding.cancelOrderHeader.setText(R.string.label_choose_appropriate_reasons);
        bottomSheetBinding.cancelOrderSendBtn.setText(R.string.label_change_order_status);
        bottomSheetBinding.closeIcon.setOnClickListener(v -> {
            dialog.dismiss();
        });
        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
            ActivityUtils.showDialog(this, "Please wait.");
            if (cancelReasonsList != null && cancelReasonsList.size() > 0) {
                for (DeliveryFailreReasonsResponse.Row row : cancelReasonsList) {
                    if (row.getName().equals(orderCancelReason)) {
                        if (row.getUid().equals("PDIT") || row.getUid().equals("CNA")) {
                            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, row.getUid(), bottomSheetBinding.comment.getText().toString().trim(), transactionId);
                            dialog.dismiss();
                        } else if (row.getUid().equals("NCAPL") || row.getUid().equals("NRFCCN")) {
                            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERYATTEMPTED", orderUid, row.getUid(), bottomSheetBinding.comment.getText().toString().trim(), transactionId);
                            dialog.dismiss();
                        }

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
        } else if (requestCode == CommonUtils.ONLINE_PAYMENT_ACTIVITY && resultCode == RESULT_OK) {
            if (data != null) {
                boolean isPaymentSuccessfull = data.getBooleanExtra("PAYMENT_SUCCESSFULL", false);
                if (isPaymentSuccessfull) {
                    String transactionId = data.getStringExtra("TRANSACTION_ID");
                    this.transactionId = transactionId;
                    new OrderDeliveryActivityController(this, this).orderPaymentUpdateApiCall(this.orderDetailsResponse, "wallet", "", transactionId);
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
        } else if (requestCode == 105) {
            if (data != null) {
                boolean isOrderCancelled = data.getBooleanExtra("is_order_cancelled", false);
                boolean isOrderShifted = data.getBooleanExtra("IS_ORDER_SHIFTED", false);
                if (isOrderCancelled || isOrderShifted)
                    finish();
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
//                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, orderDeliveryBinding.cancelledOptNum4);
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
//                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, pickupOptNum4);
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
//                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, optNum4);
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
        isStoreLoc = false;
        gotoTrackMapActivity("Pharmacy", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
//        gotoTrackMapActivity("Pharmacy", 17.4410197, 78.3788463);
    }

    @OnClick(R.id.map_view_layout)
    void onMapClick() {
        isPharmacyLoc = false;
        isDestinationLoc = true;
        isStoreLoc = false;
        gotoTrackMapActivity("Destination", this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude());
//        gotoTrackMapActivity("Destination", 17.4411128, 78.3827845);
    }

    @Override
    public void onClickReturntoStoreShowMap() {
        isPharmacyLoc = false;
        isDestinationLoc = false;
        isStoreLoc = true;
        gotoTrackMapActivity("Store", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
//        gotoTrackMapActivity("Store", 17.4411128, 78.3827845);
    }

    @Override
    public void onSuccessOrderPaymentTypeInCod(OrderPaymentSelectResponse orderPaymentSelectResponse) {
        orderDeliveryBinding.paymentTypeIncod.setText("( " + orderPaymentSelectResponse.getData().getOrderPayment().getType().getUid() + " )");
        orderDeliveryBinding.paymentTypeIncod.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailureOrderPaymentTypeInCod(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessOrderEndJourneyUpdateApiCall(OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse) {
        isOrderDelivered = true;
        System.out.println("onSuccessOrderEndJourneyUpdateApiCall:::::::::::::::::::::::::" + orderEndJourneyUpdateResponse.getMessage());
    }

    @Override
    public void onLogout() {
        getSessionManager().clearAllSharedPreferences();
        NavigationActivity.getInstance().stopBatteryLevelLocationService();
        Intent intent = new Intent(OrderDeliveryActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean isOrderCancelled() {
        return isOrderCancelled;
    }

    @Override
    public void orderCancelled() {
        finish();
    }

    private void gotoTrackMapActivity(String locType, double latitude, double longitude) {
        if (checkForLocPermission()) {
            if (checkGPSOn(this)) {
                Intent intent = new Intent(this, TrackMapActivity.class);
                intent.putExtra("locType", locType);
                intent.putExtra("Lat", latitude);
                intent.putExtra("Lon", longitude);
                intent.putExtra("order_number", orderNumber);
                intent.putExtra("order_uid", this.orderDetailsResponse.getData().getUid());
                intent.putExtra("order_state", this.orderDetailsResponse.getData().getOrderState().getName());
                startActivityForResult(intent, 105);
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
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setMessage("GPS is disabled in your device. Please enable GPS to use services")
                .setCancelable(false)
                .setPositiveButton("Open Settings",
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
            }, getString(R.string.app_name), "Allow access to get the location.", getString(R.string.ok));
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, reqCode);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    private boolean isOrderCancelled = false;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
            if (intent.getBooleanExtra("order_cancelled", false) && intent.getStringExtra("order_uid").equals(orderUid)) {
                Dialog alertDialog = new Dialog(this);
                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
                alertDialog.setContentView(alertMessageBinding.getRoot());
                alertDialog.setCancelable(false);
                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    if (orderDeliveryBinding.orderStatusHeader.getText().toString().equals("Order Assigned") || orderDeliveryBinding.orderStatusHeader.getText().toString().equals("Order Rider Updated")) {
                        finish();
                    } else {
                        this.isOrderCancelled = true;
                        ActivityUtils.showDialog(this, "Please Wait");
                        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "", "", transactionId);
                    }
                });
                alertDialog.show();
            } else if (intent.getBooleanExtra("order_shifted", false)) {
                Dialog alertDialog = new Dialog(this);
                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
                alertDialog.setContentView(alertMessageBinding.getRoot());
                alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
                alertDialog.setCancelable(false);
                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    finish();
                });
                alertDialog.show();
            } else if (intent.getBooleanExtra("COMPLAINT_RESOLVED", false)) {
                Dialog alertDialog = new Dialog(this);
                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
                alertDialog.setContentView(alertMessageBinding.getRoot());
                alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
                alertDialog.setCancelable(false);
                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                    alertDialog.dismiss();
                });
                alertDialog.show();
            } else {
                orderDeliveryBinding.notificationDot.setVisibility(View.VISIBLE);
            }
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
                        gotoTrackMapActivity("Pharmacy", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
                    } else if (isDestinationLoc) {
                        gotoTrackMapActivity("Destination", this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude());
                    } else if (isStoreLoc) {
                        gotoTrackMapActivity("Store", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
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
        liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
        liveDistanceCalculateHandler.postDelayed(liveDistanceCalculateRunnable, 3000);
        CommonUtils.CURRENT_SCREEN = getClass().getSimpleName();
        Hawk.put(AppConstants.LAST_ACTIVITY, getClass().getSimpleName());
        super.onResume();
        startService(new Intent(OrderDeliveryActivity.this, FloatingTouchService.class));
        if (GPSLocationService.isFromSetting == true) {
            GPSLocationService.isFromSetting = false;
        }
        if (CommonUtils.isIs_order_delivery_or_track_map_screen) {
            CommonUtils.isIs_order_delivery_or_track_map_screen = false;
            new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderUid, orderDeliveryBinding);
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
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickBackIcon() {
        onBackPressed();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse) {
        try {
            this.orderDetailsResponse = orderDetailsResponse;
            if (this.orderDetailsResponse != null) {
                if (this.orderDetailsResponse.getData() != null) {
                    this.paymentType = this.orderDetailsResponse.getData().getPaymentType().getName();
                    if (!this.paymentType.equals("COD")) {
                        orderDeliveryBinding.collectPayment.setVisibility(View.GONE);
                        orderDeliveryBinding.completedDeliveryText.setText("4. Complete delivery");
                    } else {
                        orderDeliveryBinding.collectPayment.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.completedDeliveryText.setText("5. Complete delivery");
                        orderDeliveryBinding.totalAmount.setText(getResources().getString(R.string.label_rupee_symbol) + " " + String.valueOf(this.orderDetailsResponse.getData().getPakgValue()));
                    }
                    if (this.orderDetailsResponse.getData().getCancelAllowed() != null && this.orderDetailsResponse.getData().getCancelAllowed().getName().equals("Yes")) {
                        if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNPICKED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNORDERRTO")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERACCEPTED")
                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERUPDATE")) {
                            if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() > 2) {
                                cancelOrderBtn.setVisibility(View.VISIBLE);
                            } else {
                                cancelOrderBtn.setVisibility(View.GONE);
                            }
                        } else {
                            cancelOrderBtn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        cancelOrderBtn.setVisibility(View.GONE);
                    }
                    this.orderUid = this.orderDetailsResponse.getData().getUid();
                    this.orderNumber = this.orderDetailsResponse.getData().getOrderNumber();
                    orderDeliveryBinding.orderStatusHeader.setText(this.orderDetailsResponse.getData().getOrderStatus().getName());

                    orderDeliveryBinding.orderNumber.setText("#" + String.valueOf(this.orderDetailsResponse.getData().getOrderNumber()));
//                    orderDeliveryBinding.crateAmount.setText(String.valueOf(this.orderDetailsResponse.getData().getCrateAmount()));
//                    orderDeliveryBinding.paymentType.setText(this.orderDetailsResponse.getData().getPaymentType().getName());


//                    String customerAddresss = orderDetailsResponse.getData().getDeliverApartment() + ", " + orderDetailsResponse.getData().getDeliverStreetName() + ", " + orderDetailsResponse.getData().getDeliverCity() + ", " + orderDetailsResponse.getData().getDeliverState() + ", " + orderDetailsResponse.getData().getDelPincode() + ", " + orderDetailsResponse.getData().getDeliverCountry();
//                    String pickupAddress = orderDetailsResponse.getData().getPickupApt() + ", " + orderDetailsResponse.getData().getPickupStreetName() + ", " + orderDetailsResponse.getData().getPickupCity() + ", " + orderDetailsResponse.getData().getPickupState() + ", " + orderDetailsResponse.getData().getPickupPincode() + ", " + orderDetailsResponse.getData().getPickupCountry();
//                    String returnAddress = orderDetailsResponse.getData().getReturnApartment() + ", " + orderDetailsResponse.getData().getReturnStreetName() + ", " + orderDetailsResponse.getData().getReturnCity() + ", " + orderDetailsResponse.getData().getReturnState() + ", " + orderDetailsResponse.getData().getReturnPincode() + ", " + orderDetailsResponse.getData().getReturnCountry();


                    String customerAddresss = "";
                    if (orderDetailsResponse.getData().getDeliverApartment() != null) {
                        customerAddresss = orderDetailsResponse.getData().getDeliverApartment() + ", ";
                    }
                    if (orderDetailsResponse.getData().getDeliverStreetName() != null) {
                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverStreetName() + ", ";
                    }
                    if (orderDetailsResponse.getData().getDeliverCity() != null) {
                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverCity() + ", ";
                    }
                    if (orderDetailsResponse.getData().getDeliverState() != null) {
                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverState() + ", ";
                    }
                    if (orderDetailsResponse.getData().getDelPincode() != null) {
                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDelPincode() + ", ";
                    }
                    if (orderDetailsResponse.getData().getDeliverCountry() != null) {
                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverCountry();
                    }

                    String pickupAddress = "";
                    if (orderDetailsResponse.getData().getPickupApt() != null) {
                        pickupAddress = orderDetailsResponse.getData().getPickupApt() + ", ";
                    }
                    if (orderDetailsResponse.getData().getPickupStreetName() != null) {
                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupStreetName() + ", ";
                    }
                    if (orderDetailsResponse.getData().getPickupCity() != null) {
                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupCity() + ", ";
                    }
                    if (orderDetailsResponse.getData().getPickupState() != null) {
                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupState() + ", ";
                    }
                    if (orderDetailsResponse.getData().getPickupPincode() != null) {
                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupPincode() + ", ";
                    }
                    if (orderDetailsResponse.getData().getPickupCountry() != null) {
                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupCountry();
                    }

                    String returnAddress = "";
                    if (orderDetailsResponse.getData().getReturnApartment() != null) {
                        returnAddress = orderDetailsResponse.getData().getReturnApartment() + ", ";
                    }
                    if (orderDetailsResponse.getData().getReturnStreetName() != null) {
                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnStreetName() + ", ";
                    }
                    if (orderDetailsResponse.getData().getReturnCity() != null) {
                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnCity() + ", ";
                    }
                    if (orderDetailsResponse.getData().getReturnState() != null) {
                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnState() + ", ";
                    }
                    if (orderDetailsResponse.getData().getReturnPincode() != null) {
                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnPincode() + ", ";
                    }
                    if (orderDetailsResponse.getData().getReturnCountry() != null) {
                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnCountry();
                    }


                    if (orderDetailsResponse.getData().getBranpickupVerCode() != null)
                        this.branPickupVerificationCode = orderDetailsResponse.getData().getBranpickupVerCode();
                    if (orderDetailsResponse.getData().getBranreturnVerCode() != null)
                        this.branReturnVerificatonCode = orderDetailsResponse.getData().getBranreturnVerCode();
                    if (orderDetailsResponse.getData().getCusPickupVerCode() != null)
                        this.cusPickupVerificationCode = orderDetailsResponse.getData().getCusPickupVerCode();
                    if (orderDetailsResponse.getData().getCusDeliveryVerCode() != null)
                        this.cusDeliveryVerificationCode = orderDetailsResponse.getData().getCusDeliveryVerCode();

                    if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERUPDATE") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DSPASSIGN") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERACCEPTED") || (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() <= 2)) {
                        orderCurrentStatus = 1;
                        onClickPickedLabel();
                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                        orderDeliveryBinding.customerTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                        orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                        if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
                            orderDeliveryBinding.returnLabel.setVisibility(View.VISIBLE);

                            orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                            orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                            orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                            orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));


                            if (orderDetailsResponse.getData().getPickupSite().getOtpCustPickup() == null) {
                                if (orderDetailsResponse.getData().getCusPickupVerCode() == null || orderDetailsResponse.getData().getCusPickupVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpCustPickup())) {
                                    orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
                                    orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
                                    isCusPickupVerificationCode = true;
                                    orderDeliveryBinding.pickupOptNum1.setText("0");
                                    orderDeliveryBinding.pickupOptNum2.setText("0");
                                    orderDeliveryBinding.pickupOptNum3.setText("0");
                                    orderDeliveryBinding.pickupOptNum4.setText("0");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
                                }
                            } else {
                                if (orderDetailsResponse.getData().getCusPickupVerCode() == null || orderDetailsResponse.getData().getCusPickupVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpCustPickup() != null && !orderDetailsResponse.getData().getPickupSite().getOtpCustPickup())) {
                                    orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
                                    orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
                                    isCusPickupVerificationCode = true;
                                    orderDeliveryBinding.pickupOptNum1.setText("0");
                                    orderDeliveryBinding.pickupOptNum2.setText("0");
                                    orderDeliveryBinding.pickupOptNum3.setText("0");
                                    orderDeliveryBinding.pickupOptNum4.setText("0");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
                                }
                            }
                        } else {
                            orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                            orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
                            orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                            orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));

                            if (orderDetailsResponse.getData().getPickupSite().getOtpPickup() == null) {
                                if (orderDetailsResponse.getData().getBranpickupVerCode() == null || orderDetailsResponse.getData().getBranpickupVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpPickup())) {
                                    orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
                                    orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
                                    isBranPickupVerificationCode = true;
                                    orderDeliveryBinding.pickupOptNum1.setText("0");
                                    orderDeliveryBinding.pickupOptNum2.setText("0");
                                    orderDeliveryBinding.pickupOptNum3.setText("0");
                                    orderDeliveryBinding.pickupOptNum4.setText("0");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
                                }
                            } else {
                                if (orderDetailsResponse.getData().getBranpickupVerCode() == null || orderDetailsResponse.getData().getBranpickupVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpPickup() != null && !orderDetailsResponse.getData().getPickupSite().getOtpPickup())) {
                                    orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
                                    orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
                                    isBranPickupVerificationCode = true;
                                    orderDeliveryBinding.pickupOptNum1.setText("0");
                                    orderDeliveryBinding.pickupOptNum2.setText("0");
                                    orderDeliveryBinding.pickupOptNum3.setText("0");
                                    orderDeliveryBinding.pickupOptNum4.setText("0");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
                                }
                            }
                        }


                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("PICKUP") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("OUTFORDELIVERY") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNPICKED")) {
                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                        continueProcessLayout.setVisibility(View.GONE);
                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_header_bg));
                        orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                        orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorBlack));
                        orderCurrentStatus = 2;
                        if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                            selectionTag = 10;
                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
                            orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                            orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                            orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                            orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                            orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                            orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                            orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
                            orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
                            orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
                            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);


                            if (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() == null) {
                                if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
                                    orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                                    orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                                isBranReturnVerificatonCode = true;
                                    isCusDeliveryVerificationCode = true;
                                    orderDeliveryBinding.cancelledOptNum1.setText("0");
                                    orderDeliveryBinding.cancelledOptNum2.setText("0");
                                    orderDeliveryBinding.cancelledOptNum3.setText("0");
                                    orderDeliveryBinding.cancelledOptNum4.setText("0");
                                    orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                                }
                            } else {
                                if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() != null && !orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr())) {
                                    orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                                    orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                                isBranReturnVerificatonCode = true;
                                    isCusDeliveryVerificationCode = true;
                                    orderDeliveryBinding.cancelledOptNum1.setText("0");
                                    orderDeliveryBinding.cancelledOptNum2.setText("0");
                                    orderDeliveryBinding.cancelledOptNum3.setText("0");
                                    orderDeliveryBinding.cancelledOptNum4.setText("0");
                                    orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                                }

                            }
                            new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                        } else {
                            onClickDeliveredLabel();
                            selectionTag = 1;
                            onContinueDrivingClick();
                            if (orderDetailsResponse.getData().getPickupSite().getOtpDelivery() == null) {
                                if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpDelivery())) {
                                    orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
                                    orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
                                    isCusDeliveryVerificationCode = true;
                                    orderDeliveryBinding.optNum1.setText("0");
                                    orderDeliveryBinding.optNum2.setText("0");
                                    orderDeliveryBinding.optNum3.setText("0");
                                    orderDeliveryBinding.optNum4.setText("0");
                                    orderDeliveryBinding.verifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
                                }
                            } else {
                                if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpDelivery() != null && !orderDetailsResponse.getData().getPickupSite().getOtpDelivery())) {
                                    orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
                                    orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
                                    isCusDeliveryVerificationCode = true;
                                    orderDeliveryBinding.optNum1.setText("0");
                                    orderDeliveryBinding.optNum2.setText("0");
                                    orderDeliveryBinding.optNum3.setText("0");
                                    orderDeliveryBinding.optNum4.setText("0");
                                    orderDeliveryBinding.verifyOtpBtn.setText("Verify");
                                    orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                    orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
                                }

                            }
                            new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                        }
                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNORDERRTO") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                        orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
                        orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
                        onClickPickedLabel();
                        onClickDeliveredLabel();
                        selectionTag = 1;
                        onContinueDrivingClick();
                        isOrderDelivered = true;
                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                    } else if ((this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() > 2) || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                        selectionTag = 10;
                        orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
                        orderDeliveryBinding.cancelOrderBtn.setVisibility(View.GONE);
                        orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);


                        if (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() == null) {
                            if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
                                orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                                orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                                isBranReturnVerificatonCode = true;
                                orderDeliveryBinding.cancelledOptNum1.setText("0");
                                orderDeliveryBinding.cancelledOptNum2.setText("0");
                                orderDeliveryBinding.cancelledOptNum3.setText("0");
                                orderDeliveryBinding.cancelledOptNum4.setText("0");
                                orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                            }
                        } else {
                            if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() != null && !orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr())) {
                                orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                                orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                                isBranReturnVerificatonCode = true;
                                orderDeliveryBinding.cancelledOptNum1.setText("0");
                                orderDeliveryBinding.cancelledOptNum2.setText("0");
                                orderDeliveryBinding.cancelledOptNum3.setText("0");
                                orderDeliveryBinding.cancelledOptNum4.setText("0");
                                orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                                orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                            }
                        }
                    }
                    if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                        cancelOrderBtn.setVisibility(View.GONE);
                        orderDeliveryBinding.actionBarDeliverBy.setText("Pickup by: ");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String orderDate = orderDetailsResponse.getData().getPickupEtWindo();
                        Date orderDates = formatter.parse(orderDate);
                        long orderDateMills = orderDates.getTime();
                        orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));


                        //pickup address and details*******************************************
                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getPickupAccName());
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText(this.orderDetailsResponse.getData().getPickupAddId());
                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
                        orderDeliveryBinding.pharmacyAddress.setText(pickupAddress);
                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
                        if (orderDetailsResponse.getData().getPickupNotes() != null && !orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        } else {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
                        }
                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());


                        //delivery address and details*********************************************

                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getReturnAccName());
//                        orderDeliveryBinding.customerheadName.setText(this.orderDetailsResponse.getData().getReturnAddId());
                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
                        orderDeliveryBinding.customerAddress.setText(returnAddress);
                        String returnMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
                        orderDeliveryBinding.userMobileNumber.setText("*******" + returnMobileNumber.substring(returnMobileNumber.length() - 3));
                        orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());


                        // order not delivered address and details
                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getReturnAccName());
//                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText(this.orderDetailsResponse.getData().getReturnAddId());
                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
                        orderDeliveryBinding.orderNotDeliveryAddress.setText(returnAddress);
                        String orderNotDeliveredPhoneNubmber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
                        orderDeliveryBinding.orderNotDeliveredPhoneNumber.setText("*******" + orderNotDeliveredPhoneNubmber.substring(orderNotDeliveredPhoneNubmber.length() - 3));
                        this.orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());

                    } else {
                        orderDeliveryBinding.actionBarDeliverBy.setText("Deliver by: ");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String orderDate = orderDetailsResponse.getData().getDelEtWindo();
                        Date orderDates = formatter.parse(orderDate);
                        long orderDateMills = orderDates.getTime();
                        orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));

                        //pickup address and details
                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getPickupAccName());
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText(this.orderDetailsResponse.getData().getPickupAddId());
                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
                        orderDeliveryBinding.pharmacyAddress.setText(pickupAddress);
                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
                        if (orderDetailsResponse.getData().getPickupNotes() != null && !orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        } else {
                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
                        }
                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());

                        //delivery address and details
                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getDelAccName());
//                        orderDeliveryBinding.customerheadName.setText(this.orderDetailsResponse.getData().getDelAddId());
                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getDeliverLandmark());
                        orderDeliveryBinding.customerAddress.setText(customerAddresss);
                        String userMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
                        orderDeliveryBinding.userMobileNumber.setText("*******" + userMobileNumber.substring(userMobileNumber.length() - 3));
                        if (orderDetailsResponse.getData().getDeliverNotes() != null && !orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.VISIBLE);
                            orderDeliveryBinding.deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
                        } else {
                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
                        }
                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());

                        //order not delivery address and details
                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getReturnAccName());
//                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText(this.orderDetailsResponse.getData().getReturnAddId());
                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
                        orderDeliveryBinding.orderNotDeliveryAddress.setText(returnAddress);
                        String orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
                        orderDeliveryBinding.orderNotDeliveredPhoneNumber.setText("*******" + orderNotDeliveredPhoneNumber.substring(orderNotDeliveredPhoneNumber.length() - 3));
                        this.orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
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
            orderDeliveryBinding.loadingWhiteScreen.setVisibility(View.GONE);
            ActivityUtils.hideDialog();
        } catch (Exception e) {
            System.out.println("onSuccessOrderSelectApi call:::::::::::::::::::::::::::" + e);
        }
    }

    @Override
    public void onFialureOrderDetailsApiCall() {

    }

    @Override
    public void onClickPickupVerifyOtp() {
        if ((this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") ? isCusPickupVerificationCode : isBranPickupVerificationCode) || (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") ? pickupPinHiddenEditText.getText().toString().trim().equals(cusPickupVerificationCode) : pickupPinHiddenEditText.getText().toString().trim().equals(branPickupVerificationCode))) {
            hideKeyboard();
            ActivityUtils.showDialog(this, "Please wait.");
            if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN"))
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("RETURNPICKED", orderUid, "", "", transactionId);
            else
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("PICKUP", orderUid, "", "", transactionId);
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

    private boolean isOrderPicked = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onSuccessOrderSaveUpdateStatusApi(String status) {
        try {
            int deliveryFailureAttempts = Integer.parseInt(getSessionManager().getGlobalSettingSelectResponse().getData().getDeliverAttempts().getUid());
            if (status.equals("PICKU")) {
                orderDeliveryBinding.orderStatusHeader.setText("Order Pickup");
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("OUTFORDELIVERY", orderUid, "", "", transactionId);
            } else if (status.equals("PICKUP") || status.equals("RETURNPICKED")) {//|| status.equals("OUTFORDELIVERY")
                DrawRouteMaps.getInstance(this, this, this, this).draw(
                        new LatLng(this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude()),
                        new LatLng(this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude()), null, 0);

                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);

                isOrderPicked = true;
                orderCurrentStatus = 2;
                selectionTag = 1;

                orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
                if (status.equals("PICKUP")) {
                    if (this.orderDetailsResponse.getData().getCancelAllowed() != null && this.orderDetailsResponse.getData().getCancelAllowed().getName().equals("Yes")) {
                        cancelOrderBtn.setVisibility(View.VISIBLE);
                    }
                    orderDeliveryBinding.orderStatusHeader.setText("Out For Delivery");
                } else {
                    orderDeliveryBinding.orderStatusHeader.setText("Return Picked");
                }
                orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                orderDeliveryBinding.pickupDetailsProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                orderDeliveryBinding.pickupDetailsInnerHead.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                orderDeliveryBinding.pickupDetailsInnerHead.setText("Order Picked");
                orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText("Order Picked");
                orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String orderDate = CommonUtils.getCurrentTimeDate();
                Date orderDates = formatter.parse(orderDate);
                long orderDateMills = orderDates.getTime();
                orderDeliveryBinding.pickupOtpVerifyText.setText("1. Picked up " + CommonUtils.getTimeFormatter(orderDateMills));
                pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
                pickupOtpVerificationChildLayout.setVisibility(View.GONE);
                pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                // continueProcessLayout.setVisibility(View.VISIBLE);
                onClickPicked();


                if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                    orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
                    orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
                    orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);

                    if (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() == null) {
                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                            isCusDeliveryVerificationCode = true;
//                        isBranReturnVerificatonCode = true;
                            orderDeliveryBinding.cancelledOptNum1.setText("0");
                            orderDeliveryBinding.cancelledOptNum2.setText("0");
                            orderDeliveryBinding.cancelledOptNum3.setText("0");
                            orderDeliveryBinding.cancelledOptNum4.setText("0");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                        }
                    } else {
                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() != null && !orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr())) {
                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                            isCusDeliveryVerificationCode = true;
//                        isBranReturnVerificatonCode = true;
                            orderDeliveryBinding.cancelledOptNum1.setText("0");
                            orderDeliveryBinding.cancelledOptNum2.setText("0");
                            orderDeliveryBinding.cancelledOptNum3.setText("0");
                            orderDeliveryBinding.cancelledOptNum4.setText("0");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                        }
                    }
                } else {
                    onContinueDrivingClick();
                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorBlack));
                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
                    if (orderDetailsResponse.getData().getPickupSite().getOtpDelivery() == null) {
                        if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpDelivery())) {
                            orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
                            orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
                            isCusDeliveryVerificationCode = true;
                            orderDeliveryBinding.optNum1.setText("0");
                            orderDeliveryBinding.optNum2.setText("0");
                            orderDeliveryBinding.optNum3.setText("0");
                            orderDeliveryBinding.optNum4.setText("0");
                            orderDeliveryBinding.verifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
                        }
                    } else {
                        if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpDelivery() != null && !orderDetailsResponse.getData().getPickupSite().getOtpDelivery())) {
                            orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
                            orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
                            isCusDeliveryVerificationCode = true;
                            orderDeliveryBinding.optNum1.setText("0");
                            orderDeliveryBinding.optNum2.setText("0");
                            orderDeliveryBinding.optNum3.setText("0");
                            orderDeliveryBinding.optNum4.setText("0");
                            orderDeliveryBinding.verifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
                        }
                    }
                }
                ActivityUtils.hideDialog();
            } else if (status.equals("DELIVERED") || status.equals("RETURNORDERRTO")) {
//                ActivityUtils.hideDialog();
                if (status.equals("DELIVERED"))
                    orderDeliveryBinding.orderStatusHeader.setText("Delivered");
                else if (status.equals("RETURNORDERRTO"))
                    orderDeliveryBinding.orderStatusHeader.setText("Return Order To Store");
                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
                orderCurrentStatus = 3;
                orderDeliveryBinding.actionBarDeliverBy.setText("Delivered at: ");
                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
                orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
                if (orderDetailsResponse.getData().getBranreturnVerCode() == null ||
                        orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
                    orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
                }
                if (this.paymentType.equals("COD")) {
                    orderDeliveryBinding.paymentTypeIncod.setVisibility(View.VISIBLE);
                    if (orderDeliveryBinding.paymentCash.isChecked())
                        orderDeliveryBinding.paymentTypeIncod.setText("( Cash )");
                    else if (orderDeliveryBinding.paymentCard.isChecked())
                        orderDeliveryBinding.paymentTypeIncod.setText("( Card )");
                    else if (orderDeliveryBinding.paymentWallet.isChecked())
                        orderDeliveryBinding.paymentTypeIncod.setText("( Wallet )");
                }

                if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                    ActivityUtils.hideDialog();
                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));


                    orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Order Returned");
                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Order Returned");
                    orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                    orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));


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
                    orderDeliveryBinding.cancelledOrderSuccessStatus.setText("Order Returned to store");
                    orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                    orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                    orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
                    orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
                    orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
                } else {
                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                    orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));

                    orderDeliveryBinding.deliveryItemsView.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
                    orderDeliveryBinding.deliverInnerHeader.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.deliverInnerHeader.setText("Order Delivered");

                    orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                    orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));

                    otpEditTextLayout.setVisibility(View.GONE);
                    verifyOtpBtn.setVisibility(View.GONE);
                    otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
                    orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
                    orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
                    verifiedOtpText.setVisibility(View.VISIBLE);
                    orderDeliveryBinding.collectPaymentText.setText("4. Collected Payment");
                    orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String orderDate = CommonUtils.getCurrentTimeDate();
                    Date orderDates = formatter.parse(orderDate);
                    long orderDateMills = orderDates.getTime();
                    orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
                    orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));

                    //deliver header
                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.customerheadName.setText("Order Delivered");
//                orderDeliveryBinding.deliverNameHeadLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
                    orderDeliveryBinding.deliverNameHeadCompletedIcon.setVisibility(View.VISIBLE);
                    orderCurrentStatus = 0;

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
//                deliveryItemsView.setLayoutParams(params);

//                new Handler().postDelayed(() -> {
////                NavigationActivity.getInstance().showFragment(new DashboardFragment(), R.string.menu_dashboard);
////                NavigationActivity.getInstance().updateSelection(1);
//
//                    Intent intent = getIntent();
//                    intent.putExtra("OrderCompleted", "true");
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                }, 1000);
                }
            } else if (status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() < deliveryFailureAttempts - 1) {
                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
                finish();
            }
//            else if ((status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() >= deliveryFailureAttempts) || status.equals("DELIVERYFAILED")) {
//
//                if (status.equals("DELIVERYATTEMPTED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Attempted");
//                else if (status.equals("DELIVERYFAILED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Failed");
////                onSuccessOrderSaveUpdateStatusApi("CANCELRETURNINITIATED");
////                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "CANCELRETURNINITIATED", null);
//            }
            else if ((status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() >= deliveryFailureAttempts - 1)
                    || status.equals("DELIVERYFAILED")
                    || status.equals("CANCELRETURNINITIATED")) {
                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                ActivityUtils.hideDialog();
                orderCurrentStatus = 4;
                orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
                orderDeliveryBinding.delivered.setVisibility(View.GONE);
                orderDeliveryBinding.customerheadName.setText("Delivery Attempted");
//                orderDeliveryBinding.cancelledOtpVerificationParentLayoutParent.setVisibility(View.GONE);
//                orderDeliveryBinding.handovertoPharmacy.setText("2. Handedover to pharmacy");

                if (!this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
                    orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
                    orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
                }

                if (!isOrderPicked) {
                    orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.colorGrey));

                    orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
                    orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                    orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                    isOrderCancelledandNotPicked = true;
                    onClickPicked();
                    Dialog alertDialog = new Dialog(this);
                    DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
                    alertDialog.setContentView(alertMessageBinding.getRoot());
                    alertDialog.setCancelable(false);
                    alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
                        alertDialog.dismiss();
                        finish();
                    });
                    alertDialog.show();
                } else {
                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));

                    if (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() == null) {
                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                            isBranReturnVerificatonCode = true;
                            orderDeliveryBinding.cancelledOptNum1.setText("0");
                            orderDeliveryBinding.cancelledOptNum2.setText("0");
                            orderDeliveryBinding.cancelledOptNum3.setText("0");
                            orderDeliveryBinding.cancelledOptNum4.setText("0");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                        }
                    } else {
                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr() != null && !orderDetailsResponse.getData().getPickupSite().getOtpBranchHndovr())) {
                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
                            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
                            isBranReturnVerificatonCode = true;
                            orderDeliveryBinding.cancelledOptNum1.setText("0");
                            orderDeliveryBinding.cancelledOptNum2.setText("0");
                            orderDeliveryBinding.cancelledOptNum3.setText("0");
                            orderDeliveryBinding.cancelledOptNum4.setText("0");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
                            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
                        }
                    }
                }
                if (status.equals("DELIVERYATTEMPTED"))
                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Attempted");
                else if (status.equals("DELIVERYFAILED"))
                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Failed");
                else if (status.equals("CANCELRETURNINITIATED"))
                    orderDeliveryBinding.orderStatusHeader.setText("Cancel Return Initiated");

                orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
                orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
                orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
                cancelItemBtn.setVisibility(View.GONE);
                cancelOrderBtn.setVisibility(View.GONE);
                orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
                orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
                orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
                orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
                orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                onClickDelivered();
            } else if (status.equals("CANCELORDERRTO")) {
                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
                isOrderDelivered = true;
                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
                orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
                orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
                if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
                    orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
                }
                orderCurrentStatus = 5;
                orderDeliveryBinding.orderStatusHeader.setText("Cancel Order To Store");
                ActivityUtils.hideDialog();
                orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
                orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));

                orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Handedover to store");
                orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Handedover to store");
                orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
                orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
                orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
                orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));

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
            System.out.println("onSuccessOrderSaveUpdateStatusApi:::::::::::::::::::::::::::::::::::::" + e.getMessage());
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
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
        }
    }

    @Override
    public void onFailureOrderHandoverSaveUpdateApi(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // cancelled order return to store flow
    private int orderHandoverToPharmacy = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
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
//                if (orderCurrentStatus == 4) {
//                    ActivityUtils.showDialog(this, "Please Wait.");
//                    new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_handover_to_pharmacy", orderUid, "", "");
//                }
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

    private boolean isStatusCancelled = false;

    @Override
    public void onClickCancelledVerifyOtp() {

        if ((isCusDeliveryVerificationCode && this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) || orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase(cusDeliveryVerificationCode) &&
                this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
            hideKeyboard();
            ActivityUtils.showDialog(this, "Please Wait.");
            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("RETURNORDERRTO", orderUid, "", "", transactionId);
        } else if (isBranReturnVerificatonCode || orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase(branReturnVerificatonCode)) {
            hideKeyboard();
            ActivityUtils.showDialog(this, "Please Wait.");
            if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
                this.isStatusCancelled = true;
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "", "", transactionId);
            } else {
                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELORDERRTO", orderUid, "", "", transactionId);
            }
        } else {
            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.VISIBLE);
            orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.VISIBLE);
            orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
            orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.GONE);
            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
        }
    }

    @Override
    public boolean isStatusCancelled() {
        return this.isStatusCancelled;
    }

    @Override
    public void statusCanelled() {
        this.isStatusCancelled = false;
        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELORDERRTO", orderUid, "", "", transactionId);
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
            startActivity(NavigationActivity.getStartIntent(this));
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
        super.onPause();
    }

    @Override
    public void onDirectionApi(int colorFlag, JSONArray jsonArray) {

        String distance, time;
        float removing = 0;
        float removingTime = 0;
        try {
            if (jsonArray != null) {
                distance = ((JSONObject) jsonArray.get(0)).getJSONObject("distance").getString("text");
                time = ((JSONObject) jsonArray.get(0)).getJSONObject("duration").getString("value");
                removing = Float.parseFloat(distance.replace("\"", "").replace("km", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
//                removingTime = Float.parseFloat(time.replace("\"", "").replace("mins", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
                removingTime = Float.parseFloat(time) / 60;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final boolean[] isStartJourneyUpdate = {false};
        float finalRemoving = removing;
        float finalTime = removingTime;
        if (finalRemoving != 0) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    float i;
                    try {
                        for (i = 0; i <= 100; i++) {
                            runOnUiThread(() -> {
                                if (!isStartJourneyUpdate[0]) {
                                    isStartJourneyUpdate[0] = true;
                                    new OrderDeliveryActivityController(OrderDeliveryActivity.this, OrderDeliveryActivity.this).orderStartJourneyUpdateApiCall(OrderDeliveryActivity.this.orderDetailsResponse.getData().getUid(), String.valueOf(finalRemoving));
                                }
                            });
                            sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    @Override
    public Polyline onTaskDone(boolean flag, Object... values) {
        return null;
    }

    @Override
    public Polyline onSecondTaskDone(boolean flag, Object... values) {
        return null;
    }

    @Override
    public void pointsFirst(List<LatLng> pionts) {

    }

    @Override
    public void pointsSecond(List<LatLng> pionts) {

    }
}


//package com.apollo.epos.activity.orderdelivery;
//
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//import static com.apollo.epos.utils.ActivityUtils.getCurrentTime;
//import static com.apollo.epos.utils.ActivityUtils.showLayoutDownAnimation;
//import static com.apollo.epos.utils.ActivityUtils.showTextDownAnimation;
//import static com.apollo.epos.utils.AppConstants.LAST_ACTIVITY;
//
//import android.Manifest;
//import android.animation.LayoutTransition;
//import android.annotation.SuppressLint;
//import android.app.ActivityManager;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.PorterDuff;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.StrictMode;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.databinding.DataBindingUtil;
//import androidx.transition.Slide;
//import androidx.transition.Transition;
//import androidx.transition.TransitionManager;
//
//import com.ahmadrosid.lib.drawroutemap.DirectionApiCallback;
//import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
//import com.ahmadrosid.lib.drawroutemap.PiontsCallback;
//import com.ahmadrosid.lib.drawroutemap.TaskLoadedCallback;
//import com.apollo.epos.R;
//import com.apollo.epos.activity.BaseActivity;
//import com.apollo.epos.activity.CancelOrderActivity;
//import com.apollo.epos.activity.CaptureSignatureActivity;
//import com.apollo.epos.activity.ScannerActivity;
//import com.apollo.epos.activity.login.LoginActivity;
//import com.apollo.epos.activity.navigation.NavigationActivity;
//import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
//import com.apollo.epos.activity.onlinepayment.OnlinePaymentActivity;
//import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
//import com.apollo.epos.activity.orderdelivery.model.OrderPaymentSelectResponse;
//import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
//import com.apollo.epos.activity.trackmap.TrackMapActivity;
//import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateResponse;
//import com.apollo.epos.adapter.CustomReasonAdapter;
//import com.apollo.epos.databinding.ActivityOrderDeliveryBinding;
//import com.apollo.epos.databinding.BottomSheetBinding;
//import com.apollo.epos.databinding.DialogAlertCustomBinding;
//import com.apollo.epos.databinding.DialogAlertMessageBinding;
//import com.apollo.epos.dialog.DialogManager;
//import com.apollo.epos.fragment.dashboard.DashboardFragment;
//import com.apollo.epos.listeners.DialogMangerCallback;
//import com.apollo.epos.service.FloatingTouchService;
//import com.apollo.epos.service.GPSLocationService;
//import com.apollo.epos.utils.ActivityUtils;
//import com.apollo.epos.utils.CommonUtils;
//import com.bumptech.glide.Glide;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.material.appbar.CollapsingToolbarLayout;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//import com.novoda.merlin.Merlin;
//import com.orhanobut.hawk.Hawk;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class OrderDeliveryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,
//        View.OnFocusChangeListener, View.OnKeyListener, OrderDeliveryActivityCallback, DirectionApiCallback, TaskLoadedCallback, PiontsCallback {
//    @BindView(R.id.reached_store_layout)
//    protected LinearLayout reachedStoreLayout;
//    @BindView(R.id.reached_store_img)
//    protected ImageView reachedStoreImg;
//    @BindView(R.id.store_reached_time)
//    protected TextView storeReachedTime;
//    @BindView(R.id.scan_barcode_layout)
//    protected LinearLayout scanBarcodeLayout;
//    @BindView(R.id.scan_barcode_img)
//    protected ImageView scanBarcodeImg;
//    @BindView(R.id.scanned_barcode_img)
//    protected ImageView scannedBarcodeImg;
//    @BindView(R.id.scanned_barcode_layout)
//    protected LinearLayout scannedBarLayout;
//    @BindView(R.id.scanned_bar_code)
//    protected TextView scannedBarCode;
//    @BindView(R.id.taken_parcel_layout)
//    protected LinearLayout takenParcelLayout;
//    @BindView(R.id.taken_parcel_img)
//    protected ImageView takenParcelImg;
//    @BindView(R.id.parcel_taken_time)
//    protected TextView parcelTakenTime;
//    @BindView(R.id.continue_process_layout)
//    protected RelativeLayout continueProcessLayout;
//    @BindView(R.id.continue_driving_btn)
//    protected TextView continueDrivingBtn;
//    @BindView(R.id.order_delivery_process_img)
//    protected View orderDeliveryProcessImg;
//    @BindView(R.id.delivery_items_view)
//    protected View deliveryItemsView;
//    @BindView(R.id.user_mobile_number_header)
//    protected TextView userMobileNumberHeader;
//    @BindView(R.id.user_mobile_number)
//    protected TextView userMobileNumber;
//    @BindView(R.id.delivery_items_list_layout)
//    protected LinearLayout deliveryItemsListLayout;
//    @BindView(R.id.reached_address_layout)
//    protected LinearLayout reachedDeliveryAddressLayout;
//    @BindView(R.id.reached_address_img)
//    protected ImageView reachedAddressImg;
//    @BindView(R.id.address_reached_time)
//    protected TextView addressReachedTime;
//    @BindView(R.id.collected_amount_parent_layout)
//    protected LinearLayout collectedAmountParentLayout;
//    @BindView(R.id.collected_amount_img)
//    protected ImageView collectedAmountImg;
//    @BindView(R.id.collected_amount_child_layout)
//    protected LinearLayout collectedAmountChildLayout;
//    @BindView(R.id.handover_parcel_parent_layout)
//    protected LinearLayout handoverParcelParentLayout;
//    @BindView(R.id.handover_parcel_img)
//    protected ImageView handoverParcelImg;
//    @BindView(R.id.handover_parcel_child_layout)
//    protected LinearLayout handoverParcelChildLayout;
//    @BindView(R.id.customerTypeSpinner)
//    protected Spinner customerTypeSpinner;
//    @BindView(R.id.signature_pad_parent_layout)
//    protected LinearLayout signaturePadParentLayout;
//    @BindView(R.id.signature_pad_img)
//    protected ImageView signaturePadImg;
//    @BindView(R.id.signature_pad_child_layout)
//    protected LinearLayout signaturePadChildLayout;
//    @BindView(R.id.signature_layout)
//    protected LinearLayout signatureLayout;
//    @BindView(R.id.hint_signature_text)
//    protected TextView hintSignatureText;
//    @BindView(R.id.signature_view_layout)
//    protected RelativeLayout signatureViewLayout;
//    @BindView(R.id.customer_signature_view)
//    protected ImageView customerSignatureView;
//    @BindView(R.id.capture_image_layout)
//    protected LinearLayout captureImageLayout;
//    @BindView(R.id.capture_image)
//    protected ImageView captureImage;
//    @BindView(R.id.skip_photo_btn)
//    protected TextView skipPhotoBtn;
//    @BindView(R.id.captured_image_layout)
//    protected LinearLayout capturedImageLayout;
//    @BindView(R.id.captured_image)
//    protected ImageView capturedImage;
//    @BindView(R.id.clear_captured_image)
//    protected ImageView clearCapturedImage;
//    @BindView(R.id.otp_verification_parent_layout)
//    protected LinearLayout otpVerificationParentLayout;
//
//
//    @BindView(R.id.pickup_otp_verification_parent_layout)
//    protected LinearLayout pickupOtpVerificationParentLayout;
//
//    @BindView(R.id.otp_verification_img)
//    protected ImageView otpVerificationImg;
//
//    @BindView(R.id.pickup_otp_verification_img)
//    protected ImageView pickupOtpVerificationImg;
//
//    @BindView(R.id.otp_verification_child_layout)
//    protected LinearLayout otpVerificationChildLayout;
//
//
//    @BindView(R.id.pickup_otp_verification_child_layout)
//    protected LinearLayout pickupOtpVerificationChildLayout;
//    @BindView(R.id.pickup_otp_verification_child_layout_edge)
//    protected LinearLayout pickupOtpVerificationChildLayoutEdge;
//    @BindView(R.id.otp_editText_layout)
//    protected LinearLayout otpEditTextLayout;
//
//    @BindView(R.id.pickup_otp_editText_layout)
//    protected LinearLayout pickupOtpEditTextLayout;
//
//    @BindView(R.id.pin_hidden_edittext)
//    protected EditText pinHiddenEditText;
//    @BindView(R.id.pickup_pin_hidden_edittext)
//    protected EditText pickupPinHiddenEditText;
//    @BindView(R.id.opt_num1)
//    protected EditText optNum1;
//    @BindView(R.id.opt_num2)
//    protected EditText optNum2;
//    @BindView(R.id.opt_num3)
//    protected EditText optNum3;
//    @BindView(R.id.opt_num4)
//    protected EditText optNum4;
////    @BindView(R.id.opt_num5)
////    protected EditText optNum5;
////    @BindView(R.id.opt_num6)
////    protected EditText optNum6;
//
//
//    @BindView(R.id.pickup_opt_num1)
//    protected EditText pickupOptNum1;
//    @BindView(R.id.pickup_opt_num2)
//    protected EditText pickupOptNum2;
//    @BindView(R.id.pickup_opt_num3)
//    protected EditText pickupOptNum3;
//    @BindView(R.id.pickup_opt_num4)
//    protected EditText pickupOptNum4;
//
//
//    @BindView(R.id.verify_otp_btn)
//    protected TextView verifyOtpBtn;
//    @BindView(R.id.pickup_verify_otp_btn)
//    protected TextView pickupVerifyOtpBtn;
//
//
//    @BindView(R.id.verified_otp_text)
//    protected TextView verifiedOtpText;
//
//    @BindView(R.id.pickup_verified_otp_text)
//    protected TextView pickupVerifiedOtpText;
//
//    @BindView(R.id.order_delivered_parent_layout)
//    protected LinearLayout orderDeliveredParentLayout;
//    @BindView(R.id.order_delivered_child_one_layout)
//    protected RelativeLayout orderDeliveredChildOneLayout;
//    @BindView(R.id.order_delivered_child_two_layout)
//    protected LinearLayout orderDeliveredChildTwoLayout;
//    @BindView(R.id.cancel_item_btn)
//    protected TextView cancelItemBtn;
//    @BindView(R.id.cancel_order_btn)
//    protected TextView cancelOrderBtn;
//
//    @BindView(R.id.anim_reach_store_layout)
//    protected LinearLayout animReachStoreLayout;
//    @BindView(R.id.anim_taken_parcel_layout)
//    protected LinearLayout animTakenParcelLayout;
//    @BindView(R.id.anim_scan_barcode_layout)
//    protected LinearLayout animScanBarCodeLayout;
//    @BindView(R.id.anim_address_reached_layout)
//    protected LinearLayout animAddressReachedLayout;
//    @BindView(R.id.user_contact_number)
//    protected ImageView userContactNumber;
//    //    @BindView(R.id.finish_activity_img)
////    protected ImageView finishActivityImg;
//    private boolean isOrderDelivered = false;
//    String[] cancelReasons = {"Customer not availale", "Door locked"};
//    List<DeliveryFailreReasonsResponse.Row> cancelReasonsList = new ArrayList<>();
//    String[] customerTypesList = {"Customer", "Other"};
//    private int selectionTag = 0;
//    public static final int CAM_REQUEST = 1;
//    public static final int GAL_REQUEST = 2;
//    private int SIGNATURE_REQUEST_CODE = 3;
//    Intent camera_intent, gal_intent;
//    private File file;
//    private boolean imageStatus = false;
//    private Bitmap bp;
//    private boolean userPhoneClick = false;
//    @BindView(R.id.map_view_layout)
//    protected TextView mapViewLayout;
//    private final int REQ_LOC_PERMISSION = 5002;
//    private boolean isPharmacyLoc = false;
//    private boolean isDestinationLoc = false;
//    private boolean isStoreLoc = false;
//    private OrderDetailsResponse orderDetailsResponse;
//    private ActivityOrderDeliveryBinding orderDeliveryBinding;
//    private String pickupPhoneNumber, orderNotDeliveredPhoneNumber;
//    private String customerPhoneNumber;
//
//    private String branPickupVerificationCode = "00000000";
//    private String branReturnVerificatonCode = "00000000";//
//    private String cusPickupVerificationCode = "00000000";
//    private String cusDeliveryVerificationCode = "00000000";//
//
//    private boolean isBranPickupVerificationCode = false;
//    private boolean isBranReturnVerificatonCode = false;
//    private boolean isCusPickupVerificationCode = false;
//    private boolean isCusDeliveryVerificationCode = false;
//
//    private String orderUid;
//    private String orderNumber;
//    private String customerNameTypeSinner;
//    private String orderCancelReason;
//    private String paymentType;
//    private String transactionId = "";
//    private static TextView notificationText;
//    private int orderCurrentStatus = 0; // order assigned =1, order in transit =2, order delivered =3, order not delivered =4, order handover to pharmacy =5.
//
//    public static Intent getStartIntent(Context context, OrderDetailsResponse orderDetailsResponse) {
//        Intent intent = new Intent(context, OrderDeliveryActivity.class);
//        intent.putExtra(CommonUtils.ORDER_DETAILS_RESPONSE, orderDetailsResponse);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        return intent;
//    }
//
//    public static Intent getStartIntent(Context context, String orderNumber) {
//        Intent intent = new Intent(context, OrderDeliveryActivity.class);
//        intent.putExtra("order_number", orderNumber);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        return intent;
//    }
//
//    public static Intent getStartIntent(Context context, String orderNumber, boolean isLaunchedByPushNotification) {
//        Intent intent = new Intent(context, OrderDeliveryActivity.class);
//        intent.putExtra("order_number", orderNumber);
//        intent.putExtra("ORDER_ASSIGNED", isLaunchedByPushNotification);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        return intent;
//    }
//
//    private boolean isLaunchedByPushNotification;
//
//    private Handler liveDistanceCalculateHandler = new Handler();
//    private Runnable liveDistanceCalculateRunnable = new Runnable() {
//        @Override
//        public void run() {
////            Toast.makeText(OrderDeliveryActivity.this, getSessionManager().getRiderTravelledDistanceinDay() + "m", Toast.LENGTH_SHORT).show();
//            liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
//            liveDistanceCalculateHandler.postDelayed(liveDistanceCalculateRunnable, 3000);
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        orderDeliveryBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_delivery);
//        orderDeliveryBinding.setCallback(this);
//        ButterKnife.bind(this);
//        if (getSessionManager().getOrderPaymentTypeList() != null && getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 0) {
//            orderDeliveryBinding.paymentCash.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(0).getName());
//            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 1)
//                orderDeliveryBinding.paymentCard.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(1).getName());
//            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 2)
//                orderDeliveryBinding.paymentWallet.setText(getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(2).getName());
//
//        }
//        orderDeliveryBinding.loadingWhiteScreen.setVisibility(View.VISIBLE);
//        notificationText = (TextView) findViewById(R.id.notification_dot);
//        isScreen = true;
//        if (getSessionManager().getDeliveryFailureReasonseList() != null)
//            onSuccessDeliveryReasonApiCall(getSessionManager().getDeliveryFailureReasonseList());
//        if (getSessionManager().getNotificationStatus()) {
//            orderDeliveryBinding.notificationDot.setVisibility(View.VISIBLE);
//            anim = new AlphaAnimation(0.0f, 1.0f);
//            anim.setDuration(350); //You can manage the blinking time with this parameter
//            anim.setStartOffset(20);
//            anim.setRepeatMode(Animation.REVERSE);
//            anim.setRepeatCount(Animation.INFINITE);
//            orderDeliveryBinding.notificationDot.startAnimation(anim);
//        } else {
//            orderDeliveryBinding.notificationDot.setVisibility(View.GONE);
//        }
//
////        Bundle bundle = getIntent().getExtras();
////        if (bundle != null) {
////            try {
////                //bundle must contain all info sent in "data" field of the notification
////                String orderNumberId = bundle.getString("uid");
////                if (getIntent().getStringExtra("order_number") == null) {
////                    isLaunchedByPushNotification = true;
////                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId, orderDeliveryBinding);
////                }
////            } catch (Exception e) {
////                System.out.println("push notification new order activity::::::::::::::::::::::::::::" + e.getMessage());
////            }
////
////        }
//        if (getIntent() != null) {
//            if (getIntent().getStringExtra("order_number") != null) {
//                String orderNumberId = getIntent().getStringExtra("order_number");
//                isLaunchedByPushNotification = getIntent().getBooleanExtra("ORDER_ASSIGNED", false);
//                if (orderNumberId != null && !orderNumberId.isEmpty()) {
//                    new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderNumberId, orderDeliveryBinding);
//                } else {
//                    finish();
//                    return;
//                }
//            }
//        }
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }
////        if (getSupportActionBar() != null) {
////            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
////            getSupportActionBar().setDisplayShowCustomEnabled(true);
////            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
////            View view = getSupportActionBar().getCustomView();
////            ImageView backArrow = view.findViewById(R.id.back_arrow);
////            TextView activityName = view.findViewById(R.id.activity_title);
////            TextView activityName = view.findViewById(R.id.activity_title);
////            LinearLayout notificationLayout = view.findViewById(R.id.notification_layout);
////            backArrow.setOnClickListener(v -> {
////                finish();
////                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
////            });
////            activityName.setText(getResources().getString(R.string.menu_take_order));
////        }
//
////        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationIcon(R.drawable.icon_back);// Toolbar icon in Drawable folder
////        toolbar.setTitle("App");
////        toolbar.setTitleTextColor(Color.WHITE);// Title Color
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();// Do what do you want on toolbar button
//            }
//        });
//
//        CollapsingToolbarLayout collapsingToolbarLayout =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//
////        collapsingToolbarLayout.setTitle("My Toolbar Title");
//        collapsingToolbarLayout.setTitleEnabled(false);
//        toolbar.setTitle("My Title");
//        toolbar.setTitleTextAppearance(this, R.style.ToolbarTextAppearance);
//
//
//        RadioGroup rg = (RadioGroup) findViewById(R.id.cash_card_radiogroup);
//
//        rg.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId) {
//                case R.id.payment_cash:
//                    // do operations specific to this selection
////                    Toast.makeText(this, "cash selected", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.payment_card:
//                    // do operations specific to this selection
////                    Toast.makeText(this, "online selected", Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.payment_wallet:
//                    // do operations specific to this selection
////                    Toast.makeText(this, "wallet selected", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        });
//        CustomReasonAdapter customUserListAdapter = new CustomReasonAdapter(this, customerTypesList, this);
//        customerTypeSpinner.setAdapter(customUserListAdapter);
////        customerTypeSpinner.setOnItemSelectedListener(this);
//        customerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                OrderDeliveryActivity.this.customerNameTypeSinner = customerTypesList[position];
//                if (customerTypesList[position].equals("Other")) {
//                    orderDeliveryBinding.handoverUserName.setVisibility(View.VISIBLE);
//                } else {
//                    orderDeliveryBinding.handoverUserName.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    @OnClick(R.id.reached_store_layout)
//    void onReachedStoreLayoutClick() {
//        if (selectionTag == 0) {
//            selectionTag = 1;
//            reachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            reachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//
//            showTextDownAnimation(R.id.anim_reach_store_layout, animReachStoreLayout, storeReachedTime);
////            ActivityUtils.footerAnimation(mActivity, animReachStoreLayout, storeReachedTime);
//            storeReachedTime.setText(getCurrentTime());
//        }
//    }
//
//    @OnClick(R.id.pickup_otp_verification_parent_layout)
//    void onPickupOtpVerificationParentLayoutClick() {
//        if (selectionTag == 0) {
//            pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
//            pickupOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            pickupOtpVerificationChildLayout.setVisibility(View.VISIBLE);
//            setpickupPINListeners();
//        }
//    }
//
//    @OnClick(R.id.scan_barcode_layout)
//    void scanBarCodeClick() {
//        if (selectionTag == 1) {
//            new IntentIntegrator(this).setCaptureActivity(ScannerActivity.class).initiateScan();
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//        }
//    }
//
//    @OnClick(R.id.taken_parcel_layout)
//    void onTakenParcelLayoutClick() {
//        if (selectionTag == 2) {
//            selectionTag = 3;
//            takenParcelLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            takenParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//
//            showTextDownAnimation(R.id.anim_taken_parcel_layout, animTakenParcelLayout, parcelTakenTime);
//            parcelTakenTime.setText(getCurrentTime());
//            continueProcessLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnClick(R.id.continue_driving_btn)
//    void onContinueDrivingClick() {
//        if (selectionTag == 1) {
//            selectionTag = 2;
//            onClickDeliveredLabel();
////            Animation RightSwipe = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_swipe);
////            parentScrollView.startAnimation(RightSwipe);
//
////            int bottomDp = (int) getResources().getDimension(R.dimen.one_hundred_dp);
////            int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, mActivity);
////            parentScrollView.post(new Runnable() {
////                public void run() {
////                    parentScrollView.scrollTo(0, marginBottom);
////                }
////            });
//            userPhoneClick = true;
//            userContactNumber.setImageDrawable(getResources().getDrawable(R.drawable.icon_phone_select));
//            deliveryItemsView.setVisibility(View.VISIBLE);
//            orderDeliveryProcessImg.setVisibility(View.GONE);
//            userMobileNumberHeader.setVisibility(View.VISIBLE);
//            userMobileNumber.setVisibility(View.VISIBLE);
//            deliveryItemsListLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnClick(R.id.reached_address_layout)
//    void onReachedAddressLayoutClick() {
//        if (selectionTag == 4) {
//            selectionTag = 5;
//            reachedDeliveryAddressLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            reachedAddressImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            addressReachedTime.setText(getCurrentTime());
//            addressReachedTime.setVisibility(View.VISIBLE);
//            cancelItemBtn.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnClick(R.id.collected_amount_parent_layout)
//    void onCollectedAmtLayoutClick() {
//        if (selectionTag == 5) {
//            selectionTag = 6;
//            collectedAmountParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            collectedAmountImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            collectedAmountChildLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @OnClick(R.id.handover_parcel_parent_layout)
//    void onHandOverParcelParentLayoutClick() {
//        if (selectionTag == 2) {
//            handoverParcelChildLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClickHandoverTheParcelBtn() {
//        selectionTag = 3;
//        if (isCutomerNameSlected()) {
//            hideKeyboard();
//            orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
//            orderDeliveryBinding.handedoverParceltoCustomerName.setVisibility(View.VISIBLE);
//            if (this.customerNameTypeSinner.equals("Other"))
//                orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + orderDeliveryBinding.handoverUserName.getText().toString().trim() + " )");
//            else
//                orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + this.customerNameTypeSinner + " )");
//            orderDeliveryBinding.handoverToParcel.setText("1. Handedover Parcel");
//            handoverParcelChildLayout.setVisibility(View.GONE);
//            handoverParcelParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            handoverParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//        }
//    }
//
//    private boolean isCutomerNameSlected() {
//        if (this.customerNameTypeSinner.equals("Other") && orderDeliveryBinding.handoverUserName.getText().toString().trim().isEmpty()) {
//            orderDeliveryBinding.handoverUserName.setError("Enter customer name.");
//            orderDeliveryBinding.handoverUserName.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onClickCollectPayment() {
//        if (selectionTag == 5) {
//            orderDeliveryBinding.collectPaymentRadio.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private String codCardCash = null;
//
//    @Override
//    public void onClickCollectPaymentSave() {
//        selectionTag = 6;
//        if (orderDeliveryBinding.paymentCash.isChecked()) {
//            this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(0).getUid();
//            new OrderDeliveryActivityController(this, this).orderPaymentUpdateApiCall(this.orderDetailsResponse, "cash", "", "");
//        } else if (orderDeliveryBinding.paymentCard.isChecked()) {
//            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 1)
//                this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(1).getUid();
//        } else if (orderDeliveryBinding.paymentWallet.isChecked()) {
//            if (getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().size() > 2)
//                this.codCardCash = getSessionManager().getOrderPaymentTypeList().getData().getListData().getRows().get(2).getUid();
//            startActivityForResult(OnlinePaymentActivity.getStartIntent(this, orderDetailsResponse), CommonUtils.ONLINE_PAYMENT_ACTIVITY);
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//        }
//    }
//
//    @Override
//    public String getCodCardCash() {
//        return codCardCash;
//    }
//
//    @Override
//    public void onSuccessOrderPaymentUpdateApiCall() {
//        ActivityUtils.showDialog(this, "Please Wait.");
//        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
//    }
//
//    @Override
//    public void onFailureOrderPaymentApiCall() {
//
//    }
//
//    @Override
//    public void onClickEyeImage() {
//        if (orderDeliveryBinding.itemsViewLayout.getVisibility() == View.VISIBLE) {
//            LayoutTransition lt = new LayoutTransition();
//            lt.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
//            orderDeliveryBinding.animParentLayout.setLayoutTransition(lt);
//
//            orderDeliveryBinding.itemsViewLayout.setVisibility(View.GONE);
//            orderDeliveryBinding.itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_open));
//        } else {
//            Transition transition = new Slide(Gravity.TOP);
//            transition.setDuration(1000);
//            transition.addTarget(R.id.anim_parent_layout);
//            TransitionManager.beginDelayedTransition(orderDeliveryBinding.animParentLayout, transition);
//
//            orderDeliveryBinding.itemsViewLayout.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.itemsViewImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_eye_close));
//        }
//    }
//
//    @Override
//    public void onSuccessOrderStatusHistoryListApiCall(OrderStatusHitoryListResponse orderStatusHitoryListResponse) {
//        try {
//            if (orderStatusHitoryListResponse != null) {
//                boolean isTransitOrder = false;
//                for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
//                    if (row.getOrderStatus().getUid() != null && (row.getOrderStatus().getUid().equals("PICKUP") || row.getOrderStatus().getUid().equals("OUTFORDELIVERY") || row.getOrderStatus().getUid().equals("RETURNPICKED"))) {
//                        isTransitOrder = true;
//                        orderCurrentStatus = 2;
//                        isOrderPicked = true;
//                        orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                        String orderDate = row.getCreatedTime();
//                        Date orderDates = formatter.parse(orderDate);
//                        long orderDateMills = orderDates.getTime();
//                        orderDeliveryBinding.pickupOtpVerifyText.setText("1. Picked up " + CommonUtils.getTimeFormatter(orderDateMills));
////                        selectionTag = 1;
//                        pickupOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
//                        pickupOtpVerificationChildLayout.setVisibility(View.GONE);
//                        pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                        orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//
//                        //pickup header
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//
//                        orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                        orderDeliveryBinding.pickupDetailsProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                        orderDeliveryBinding.pickupDetailsInnerHead.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.pickupDetailsInnerHead.setText("Order Picked");
//                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText("Order Picked");
////                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                        break;
//                    }
//
//                }
//                if (!isTransitOrder) {
//                    orderDeliveryBinding.pickupOtpVerificationParentLayoutParent.setVisibility(View.GONE);
//                }
//
//                if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
////                    continueProcessLayout.setVisibility(View.VISIBLE);
//                    cancelOrderBtn.setVisibility(View.GONE);
//                    orderDeliveryBinding.customerheadName.setText("Delivery Failed");
////                    orderDeliveryBinding.cancelledOtpVerificationParentLayoutParent.setVisibility(View.GONE);
////                    orderDeliveryBinding.handovertoPharmacy.setText("2. Handedover to pharmacy");
//
//                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
//                    orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    orderCurrentStatus = 4;
////                    onClickDelivered();
//                    orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
////                    orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
//                    if (!this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                        orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
//                    }
//
//
//                    if (!isTransitOrder) {
//                        isOrderCancelledandNotPicked = true;
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
//                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                        orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                        Dialog alertDialog = new Dialog(this);
//                        DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
//                        alertDialog.setContentView(alertMessageBinding.getRoot());
//                        alertDialog.setCancelable(false);
//                        alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
//                            alertDialog.dismiss();
//                            finish();
//                        });
//                        alertDialog.show();
//                    }
//                }
//
//                if (isOrderDelivered) {
//                    selectionTag = 10;
//                    orderCurrentStatus = 3;
//                    orderDeliveryBinding.actionBarDeliverBy.setText("Delivered at: ");
//                    if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
//                        if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
//                            orderDeliveryBinding.orderStatusHeader.setText("Cancel Order To Store");
//                            orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                            orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                            orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
//                            orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
//                        } else {
//                            orderDeliveryBinding.orderStatusHeader.setText("Order Delivered");
//                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//                        }
//                        orderDeliveryBinding.delivered.setVisibility(View.GONE);
//                        orderHandoverToPharmacy = 2;
//                        orderDeliveryBinding.cancelledReachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.cancelledReachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        orderDeliveryBinding.cancelledOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.cancelledOtpVerificationChildLayout.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null ||
//                                orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
//                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
//                            orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
//                        }
//                        ActivityUtils.hideDialog();
//                        orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//
//                        orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Order Returned");
//                        orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Order Returned");
//                        orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//
//
//                        orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.GONE);
//                        orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                        orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        cancelItemBtn.setVisibility(View.GONE);
//                        cancelOrderBtn.setVisibility(View.GONE);
//                        orderDeliveryBinding.cancelledOrderSuccessStatus.setText("Order Returned to store");
//
//                        for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
//                            if (row.getOrderStatus().getUid().equals("DELIVERED")) {
//                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                                String orderDate = row.getCreatedTime();
//                                Date orderDates = formatter.parse(orderDate);
//                                long orderDateMills = orderDates.getTime();
//                                orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                                orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                                break;
//                            }
//                        }
//
//
//                        orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//                        orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
//                        orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
//                    } else {
//                        if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null ||
//                                orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty()) {
//                            orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
//                            orderDeliveryBinding.verifiedOtpText.setText("Verification completed successfully");
//                        }
//                        if (this.paymentType.equals("COD"))
//                            new OrderDeliveryActivityController(this, this).getOrderPaymentTypeinCod(orderUid);
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                        orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//
//                        orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                        orderDeliveryBinding.deliverInnerHeader.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.deliverInnerHeader.setText("Order Delivered");
//                        orderDeliveryBinding.deliveryItemsView.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                        //Continue process layout
//                        orderDeliveryBinding.continueProcessLayout.setVisibility(View.GONE);
//
//                        //handover the parcel to
//                        orderDeliveryBinding.handoverParcelChildLayoutAnim.setLayoutTransition(null);
//                        handoverParcelChildLayout.setVisibility(View.GONE);
//                        handoverParcelParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        handoverParcelImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//
//                        //continue driving
//                        userPhoneClick = true;
//                        userContactNumber.setImageDrawable(getResources().getDrawable(R.drawable.icon_phone_select));
//                        deliveryItemsView.setVisibility(View.VISIBLE);
//                        orderDeliveryProcessImg.setVisibility(View.GONE);
//                        userMobileNumberHeader.setVisibility(View.VISIBLE);
//                        userMobileNumber.setVisibility(View.VISIBLE);
//                        deliveryItemsListLayout.setVisibility(View.VISIBLE);
//
//                        //otp verified
//                        otpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        otpEditTextLayout.setVisibility(View.GONE);
//                        verifyOtpBtn.setVisibility(View.GONE);
//                        otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                        otpVerificationChildLayout.setVisibility(View.VISIBLE);
//                        verifiedOtpText.setVisibility(View.VISIBLE);
//                        otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        cancelItemBtn.setVisibility(View.GONE);
//                        cancelOrderBtn.setVisibility(View.GONE);
//
//                        //proof of handover
//                        signaturePadImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        signaturePadChildLayout.setVisibility(View.VISIBLE);
//                        hintSignatureText.setVisibility(View.GONE);
//                        if (this.orderDetailsResponse.getData().getOrderHandover() != null && this.orderDetailsResponse.getData().getOrderHandover().getHandoverTo() != null) {
//                            orderDeliveryBinding.handedoverParceltoCustomerName.setText("( " + this.orderDetailsResponse.getData().getOrderHandover().getHandoverTo() + " )");
//                            orderDeliveryBinding.handedoverParceltoCustomerName.setVisibility(View.VISIBLE);
//                        }
//                        orderDeliveryBinding.touchHereForNewSign.setVisibility(View.GONE);
//                        signatureViewLayout.setVisibility(View.VISIBLE);
//                        if (this.orderDetailsResponse.getData().getOrderHandover().getSignature().size() > 0) {
//                            Glide.with(this).load(this.orderDetailsResponse.getData().getOrderHandover().getSignature().get(0).getFullPath()).into(customerSignatureView);
//                            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        } else {
//                            orderDeliveryBinding.proofofHandoverAnimLayout.setLayoutTransition(null);
//                            orderDeliveryBinding.proofOfHandover.setText("3. Proof of handover skipped");
//                            orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
//                            orderDeliveryBinding.signaturePadChildLayout.setVisibility(View.GONE);
//                            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.alert_header_bg));
//                        }
//                        //                    customerSignatureView.setImageBitmap(bmp);
//
//                        // collect payment
//                        otpEditTextLayout.setVisibility(View.GONE);
//                        verifyOtpBtn.setVisibility(View.GONE);
//                        otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                        orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
//                        orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                        verifiedOtpText.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.collectPaymentText.setText("4. Collected Payment");
//                        otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                        orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//                        for (OrderStatusHitoryListResponse.Row row : orderStatusHitoryListResponse.getData().getListData().getRows()) {
//                            if (row.getOrderStatus().getUid().equals("DELIVERED")) {
//                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                                String orderDate = row.getCreatedTime();
//                                Date orderDates = formatter.parse(orderDate);
//                                long orderDateMills = orderDates.getTime();
//                                orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                                orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                                //deliver header
//                                orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                                orderDeliveryBinding.customerheadName.setText("Order Delivered");
////                            orderDeliveryBinding.deliverNameHeadLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                                orderDeliveryBinding.deliverNameHeadCompletedIcon.setVisibility(View.VISIBLE);
//                                break;
//                            }
//                        }
//                        orderCurrentStatus = 0;
//                        orderDeliveredChildOneLayout.setVisibility(View.GONE);
//                        orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);
//
//                        verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        cancelItemBtn.setVisibility(View.GONE);
//                        cancelOrderBtn.setVisibility(View.GONE);
//
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.MATCH_PARENT);
//                        int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
//                        int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
//                        int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, this);
//                        int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, this);
//                        params.setMargins(0, 0, marginEnd, marginBottom);
////                    deliveryItemsView.setLayoutParams(params);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("onSuccessOrderStatusHistoryListApiCall=============================" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void onFailureOrderStatusHistoryListApiCall(String message) {
//
//    }
//
//    @Override
//    public void onClickOrderNotDeliveredCallIcon() {
//        checkCallPermissionSetting(orderNotDeliveredPhoneNumber);
//    }
//
//    @Override
//    public void onClickNotificationIcon() {
//        if (getSessionManager().getNotificationStatus()) {
//            if (isOrderDelivered) {
//                notificationText.clearAnimation();
//                NavigationActivity.notificationDotVisibility(false);
//                DashboardFragment.newOrderViewVisibility(false);
////                getSessionManager().setNotificationStatus(false);
//
//                startActivity(NavigationActivity.getStartIntent(this));
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                finish();
//            } else {
//                Dialog alertDialog = new Dialog(this);
//                DialogAlertCustomBinding alertCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_custom, null, false);
//                alertDialog.setContentView(alertCustomBinding.getRoot());
//                alertCustomBinding.title.setText("Alert!");
//                alertCustomBinding.subtitle.setText("Are sure want to leave this page?");
//                alertCustomBinding.dialogButtonNO.setText("No");
//                alertCustomBinding.dialogButtonOK.setText("Yes");
//                alertCustomBinding.dialogButtonOK.setOnClickListener(v -> {
//                    notificationText.clearAnimation();
//                    NavigationActivity.notificationDotVisibility(false);
//                    DashboardFragment.newOrderViewVisibility(false);
////                    getSessionManager().setNotificationStatus(false);
//
//                    startActivity(NavigationActivity.getStartIntent(this));
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                    alertDialog.dismiss();
//                    finish();
//                });
//                alertCustomBinding.dialogButtonNO.setOnClickListener(v -> alertDialog.dismiss());
//                alertDialog.show();
//            }
//        } else {
//            Toast.makeText(this, "No Notification.", Toast.LENGTH_SHORT).show();
//        }
////        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//
////        onBackPressed();
//    }
//
//    static Animation anim;
//    private static boolean isScreen;
//
//    public static void notificationDotVisibility(boolean show) {
//        if (isScreen) {
//            if (show) {
//                notificationText.setVisibility(View.VISIBLE);
//                anim = new AlphaAnimation(0.0f, 1.0f);
//                anim.setDuration(350); //You can manage the blinking time with this parameter
//                anim.setStartOffset(20);
//                anim.setRepeatMode(Animation.REVERSE);
//                anim.setRepeatCount(Animation.INFINITE);
//                notificationText.startAnimation(anim);
//            } else {
//                notificationText.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    @Override
//    public void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse) {
//        try {
//            cancelReasons = new String[deliveryFailreReasonsResponse.getData().getListData().getRows().size()];
//            this.cancelReasonsList = deliveryFailreReasonsResponse.getData().getListData().getRows();
//            for (DeliveryFailreReasonsResponse.Row row : deliveryFailreReasonsResponse.getData().getListData().getRows())
//                cancelReasons[deliveryFailreReasonsResponse.getData().getListData().getRows().indexOf(row)] = row.getName();
//
//        } catch (Exception e) {
//            System.out.println("onSuccessDeliveryReasonApiCall:::::::::::::::::::::::::::::::" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void onFailureDeliveryFailureApiCall() {
//
//    }
//
//    @OnClick(R.id.signature_pad_parent_layout)
//    void onSignaturePadParentLayoutClick() {
//        if (selectionTag == 4) {
//            signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
//            signaturePadImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            signaturePadChildLayout.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClickProofofHandoverSkip() {
//        if (this.customerNameTypeSinner.equals("Other")) {
//            new OrderDeliveryActivityController(this, this).orderHandoverSaveUpdate(null, null, orderUid, orderNumber, orderDeliveryBinding.handoverUserName.getText().toString().trim());
//        } else {
//            new OrderDeliveryActivityController(this, this).orderHandoverSaveUpdate(null, null, orderUid, orderNumber, this.customerNameTypeSinner);
//        }
//        orderDeliveryBinding.proofofHandoverAnimLayout.setLayoutTransition(null);
//        orderDeliveryBinding.proofOfHandover.setText("3. Proof of handover skipped");
//        orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
//        orderDeliveryBinding.signaturePadChildLayout.setVisibility(View.GONE);
//        signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.alert_header_bg));
//        selectionTag = 5;
//        if (!paymentType.equals("COD")) {
//            ActivityUtils.showDialog(this, "Please Wait.");
//            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
//        }
//    }
//
//    @Override
//    public void onClickReturnLabel() {
//        if (orderCurrentStatus != 1) {
//            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onClickOrderNotDelivered() {
//        if (orderCurrentStatus != 2 && orderCurrentStatus != 4) {
//            orderDeliveryBinding.returnLabel.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.GONE);
//        }
//    }
//
//    private boolean isOrderCancelledandNotPicked = false;
//
//    @Override
//    public void onClickPickedLabel() {
//        if (!isOrderCancelledandNotPicked) {
//            expanded = true;
//            orderDeliveryBinding.packedLabel.setVisibility(View.GONE);
//            orderDeliveryBinding.packed.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private boolean expanded;
//
//    @Override
//    public void onClickPicked() {
//        if (orderCurrentStatus != 1) {
//            expanded = false;
//            orderDeliveryBinding.packedLabel.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.packed.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onClickDeliveredLabel() {
//        if (orderCurrentStatus != 1 && orderCurrentStatus != 3 && orderCurrentStatus != 4 && orderCurrentStatus != 5) {
//            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//            orderDeliveryBinding.delivered.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClickDelivered() {
//        if (orderCurrentStatus != 2) {
//            orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.delivered.setVisibility(View.GONE);
//        }
//    }
//
//    @OnClick(R.id.signature_layout)
//    void onSignatureLayoutClick() {
//        if (selectionTag != 10) {
//            Intent intent = new Intent(this, CaptureSignatureActivity.class);
//            intent.putExtra("order_number", orderNumber);
//            intent.putExtra("customer_name", this.customerNameTypeSinner.equals("Other") ? orderDeliveryBinding.handoverUserName.getText().toString().trim() : this.customerNameTypeSinner);
//            startActivityForResult(intent, SIGNATURE_REQUEST_CODE);
//            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//        }
//    }
//
//    @OnClick(R.id.capture_image)
//    void onCaptureImageClick() {
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        cameraPermissionSetting();
//    }
//
//    @OnClick(R.id.clear_captured_image)
//    void onClearCapturedImageClick() {
//        captureImageLayout.setVisibility(View.VISIBLE);
//        capturedImageLayout.setVisibility(View.GONE);
//    }
//
//    @OnClick(R.id.otp_verification_parent_layout)
//    void onOtpVerificationParentLayoutClick() {
//        if (selectionTag == 3) {
//            otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
//            otpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            otpVerificationChildLayout.setVisibility(View.VISIBLE);
//            setPINListeners();
//        }
//    }
//
//    @OnClick(R.id.verify_otp_btn)
//    void onVerifyOtpBtnClick() {
//
//        if (isCusDeliveryVerificationCode || pinHiddenEditText.getText().toString().equalsIgnoreCase(cusDeliveryVerificationCode)) {
//            selectionTag = 4;
//            hideKeyboard();
//            otpEditTextLayout.setVisibility(View.GONE);
//            verifyOtpBtn.setVisibility(View.GONE);
//            otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//            verifiedOtpText.setVisibility(View.VISIBLE);
//            otpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//            if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null ||
//                    orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty()) {
//                orderDeliveryBinding.verifiedOtpText.setText("Verification completed successfully");
//            }
//            verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//            cancelItemBtn.setVisibility(View.GONE);
//        } else {
//            otpEditTextLayout.setVisibility(View.VISIBLE);
//            verifyOtpBtn.setVisibility(View.VISIBLE);
//            otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
//            verifiedOtpText.setVisibility(View.GONE);
//            verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//        }
//    }
//
//    private void cameraPermissionSetting() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("Permissions_Status", "Permissions not granted");
//               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED)*/
//                // ask for permission
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//            } else {
//                Log.d("Permissions_Status", "We have a permission");
//                // we have a permission
//                imagePickerAction();
//            }
//        } else {
//            imagePickerAction();
//        }
//    }
//
//    private void imagePickerAction() {
//        camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//        startActivityForResult(camera_intent, CAM_REQUEST);
//    }
//
//    @OnClick(R.id.cancel_order_btn)
//    public void toggleBottomSheet() {
//        BottomSheetDialog dialog = new BottomSheetDialog(this);
//        BottomSheetBinding bottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet, null, false);
//        dialog.setContentView(bottomSheetBinding.getRoot());
//        dialog.setCancelable(false);
//        dialog.show();
//
//        bottomSheetBinding.sheetHeader.setText(R.string.label_change_order_status);
//        bottomSheetBinding.cancelOrderHeader.setText(R.string.label_choose_appropriate_reasons);
//        bottomSheetBinding.cancelOrderSendBtn.setText(R.string.label_change_order_status);
//        bottomSheetBinding.closeIcon.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//        bottomSheetBinding.cancelOrderSendBtn.setOnClickListener(v -> {
//            ActivityUtils.showDialog(this, "Please wait.");
//            if (cancelReasonsList != null && cancelReasonsList.size() > 0) {
//                for (DeliveryFailreReasonsResponse.Row row : cancelReasonsList) {
//                    if (row.getName().equals(orderCancelReason)) {
//                        if (row.getUid().equals("PDIT") || row.getUid().equals("CNA")) {
//                            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, row.getUid(), bottomSheetBinding.comment.getText().toString().trim(), transactionId);
//                            dialog.dismiss();
//                        } else if (row.getUid().equals("NCAPL") || row.getUid().equals("NRFCCN")) {
//                            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERYATTEMPTED", orderUid, row.getUid(), bottomSheetBinding.comment.getText().toString().trim(), transactionId);
//                            dialog.dismiss();
//                        }
//
//                        break;
//                    }
//                }
//            }
//        });
////        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
//        CustomReasonAdapter customAdapter = new CustomReasonAdapter(this, cancelReasons, null);
//        bottomSheetBinding.rejectReasonSpinner.setAdapter(customAdapter);
//        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                OrderDeliveryActivity.this.orderCancelReason = cancelReasons[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
////        bottomSheetBinding.rejectReasonSpinner.setOnItemSelectedListener(this);
//    }
//
//    @OnClick(R.id.cancel_item_btn)
//    void onCancelItemClick() {
//        Intent i = new Intent(this, CancelOrderActivity.class);
//        startActivity(i);
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
////        ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new CancelOrderItemFragment(), R.string.menu_take_order);
////        ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////        Toast.makeText(getApplicationContext(), rejectReasons[position], Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SIGNATURE_REQUEST_CODE) {
//            if (data != null) {
//                try {
//                    byte[] byteArray = data.getByteArrayExtra("capturedSignature");
//                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//                    if (this.customerNameTypeSinner.equals("Other")) {
//                        new OrderDeliveryActivityController(this, this).uploadFile(bmp, orderUid, orderNumber, orderDeliveryBinding.handoverUserName.getText().toString().trim());
//                    } else {
//                        new OrderDeliveryActivityController(this, this).uploadFile(bmp, orderUid, orderNumber, this.customerNameTypeSinner);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else if (requestCode == CommonUtils.ONLINE_PAYMENT_ACTIVITY && resultCode == RESULT_OK) {
//            if (data != null) {
//                boolean isPaymentSuccessfull = data.getBooleanExtra("PAYMENT_SUCCESSFULL", false);
//                if (isPaymentSuccessfull) {
//                    String transactionId = data.getStringExtra("TRANSACTION_ID");
//                    this.transactionId = transactionId;
//                    new OrderDeliveryActivityController(this, this).orderPaymentUpdateApiCall(this.orderDetailsResponse, "wallet", "", transactionId);
//                }
//            }
//        } else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
//            imageStatus = true;
//            Bundle bundle = data.getExtras();
//            Bitmap bitmap = bundle.getParcelable("data");
//            captureImageLayout.setVisibility(View.GONE);
//            capturedImageLayout.setVisibility(View.VISIBLE);
//            capturedImage.setImageBitmap(bitmap);
//            bp = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//        } else if (requestCode == GAL_REQUEST) {
//            if (data != null) {
//                imageStatus = true;
//                Uri choosenImage = data.getData();
//                if (choosenImage != null) {
//                    captureImageLayout.setVisibility(View.GONE);
//                    capturedImageLayout.setVisibility(View.VISIBLE);
//                    bp = decodeUri(choosenImage, 50);
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    bp.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//                    capturedImage.setImageBitmap(bp);
//                }
//            } else if (requestCode == 3) {
//                if (data == null) {
//                    imageStatus = true;
//                }
//            }
//        } else if (requestCode == 105) {
//            if (data != null) {
//                boolean isOrderCancelled = data.getBooleanExtra("is_order_cancelled", false);
//                boolean isOrderShifted = data.getBooleanExtra("IS_ORDER_SHIFTED", false);
//                if (isOrderCancelled || isOrderShifted)
//                    finish();
//            }
//        } else {
//            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
////check for null
//            if (result != null) {
//                if (result.getContents() == null) {
////                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
//                } else {
//                    selectionTag = 2;
//                    scanBarcodeLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                    scanBarcodeImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                    scannedBarCode.setText("Scanned Bar Code: " + result.getContents());
//
//                    showLayoutDownAnimation(R.id.anim_scan_barcode_layout, animScanBarCodeLayout, scannedBarLayout);
////                    scannedBarLayout.setVisibility(View.VISIBLE);
//                }
//            } else {
//// This is important, otherwise the result will not be passed to the fragment
//                super.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//    }
//
//    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
//        try {
//            // Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//            // The new size we want to scale to
//            // final int REQUIRED_SIZE =  size;
//            // Find the correct scale value. It should be the power of 2.
//            int width_tmp = o.outWidth, height_tmp = o.outHeight;
//            int scale = 1;
//            while (true) {
//                if (width_tmp / 2 < REQUIRED_SIZE
//                        || height_tmp / 2 < REQUIRED_SIZE) {
//                    break;
//                }
//                width_tmp /= 2;
//                height_tmp /= 2;
//                scale *= 2;
//            }
//            // Decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void setCancelledPINListeners() {
//        orderDeliveryBinding.cancelledPinHiddenEdittext.addTextChangedListener(cancelledVendorWatcher);
//
//        orderDeliveryBinding.cancelledOptNum1.setOnFocusChangeListener(this);
//        orderDeliveryBinding.cancelledOptNum2.setOnFocusChangeListener(this);
//        orderDeliveryBinding.cancelledOptNum3.setOnFocusChangeListener(this);
//        orderDeliveryBinding.cancelledOptNum4.setOnFocusChangeListener(this);
//
//        orderDeliveryBinding.cancelledOptNum1.setOnKeyListener(this);
//        orderDeliveryBinding.cancelledOptNum2.setOnKeyListener(this);
//        orderDeliveryBinding.cancelledOptNum3.setOnKeyListener(this);
//        orderDeliveryBinding.cancelledOptNum4.setOnKeyListener(this);
//        orderDeliveryBinding.cancelledPinHiddenEdittext.setOnKeyListener(this);
//    }
//
//    TextWatcher cancelledVendorWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum1);
//            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum2);
//            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum3);
//            setDefaultPinBackground(orderDeliveryBinding.cancelledOptNum4);
//            if (s.length() == 0) {
//                orderDeliveryBinding.cancelledOptNum1.setText("");
//                orderDeliveryBinding.cancelledOptNum2.setText("");
//                orderDeliveryBinding.cancelledOptNum3.setText("");
//                orderDeliveryBinding.cancelledOptNum4.setText("");
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 1) {
//                orderDeliveryBinding.cancelledOptNum1.setText(s.charAt(0) + "");
//                orderDeliveryBinding.cancelledOptNum2.setText("");
//                orderDeliveryBinding.cancelledOptNum3.setText("");
//                orderDeliveryBinding.cancelledOptNum4.setText("");
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 2) {
//                orderDeliveryBinding.cancelledOptNum2.setText(s.charAt(1) + "");
//                orderDeliveryBinding.cancelledOptNum3.setText("");
//                orderDeliveryBinding.cancelledOptNum4.setText("");
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 3) {
//                orderDeliveryBinding.cancelledOptNum3.setText(s.charAt(2) + "");
//                orderDeliveryBinding.cancelledOptNum4.setText("");
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 4) {
//                orderDeliveryBinding.cancelledOptNum4.setText(s.charAt(3) + "");
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
////                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, orderDeliveryBinding.cancelledOptNum4);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//
//    private void setpickupPINListeners() {
//        pickupPinHiddenEditText.addTextChangedListener(pickupVendorWatcher);
//
//        pickupOptNum1.setOnFocusChangeListener(this);
//        pickupOptNum2.setOnFocusChangeListener(this);
//        pickupOptNum3.setOnFocusChangeListener(this);
//        pickupOptNum4.setOnFocusChangeListener(this);
//
//        pickupOptNum1.setOnKeyListener(this);
//        pickupOptNum2.setOnKeyListener(this);
//        pickupOptNum3.setOnKeyListener(this);
//        pickupOptNum4.setOnKeyListener(this);
//        pickupPinHiddenEditText.setOnKeyListener(this);
//    }
//
//    TextWatcher pickupVendorWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            setDefaultPinBackground(pickupOptNum1);
//            setDefaultPinBackground(pickupOptNum2);
//            setDefaultPinBackground(pickupOptNum3);
//            setDefaultPinBackground(pickupOptNum4);
//            if (s.length() == 0) {
//                pickupOptNum1.setText("");
//                pickupOptNum2.setText("");
//                pickupOptNum3.setText("");
//                pickupOptNum4.setText("");
//                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 1) {
//                pickupOptNum1.setText(s.charAt(0) + "");
//                pickupOptNum2.setText("");
//                pickupOptNum3.setText("");
//                pickupOptNum4.setText("");
//                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 2) {
//                pickupOptNum2.setText(s.charAt(1) + "");
//                pickupOptNum3.setText("");
//                pickupOptNum4.setText("");
//                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 3) {
//                pickupOptNum3.setText(s.charAt(2) + "");
//                pickupOptNum4.setText("");
//                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 4) {
//                pickupOptNum4.setText(s.charAt(3) + "");
//                pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
////                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, pickupOptNum4);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//
//
//    private void setPINListeners() {
//        pinHiddenEditText.addTextChangedListener(vendorWatcher);
//
//        optNum1.setOnFocusChangeListener(this);
//        optNum2.setOnFocusChangeListener(this);
//        optNum3.setOnFocusChangeListener(this);
//        optNum4.setOnFocusChangeListener(this);
////        optNum5.setOnFocusChangeListener(this);
////        optNum6.setOnFocusChangeListener(this);
//
//        optNum1.setOnKeyListener(this);
//        optNum2.setOnKeyListener(this);
//        optNum3.setOnKeyListener(this);
//        optNum4.setOnKeyListener(this);
////        optNum5.setOnKeyListener(this);
////        optNum6.setOnKeyListener(this);
//        pinHiddenEditText.setOnKeyListener(this);
//    }
//
//    TextWatcher vendorWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            setDefaultPinBackground(optNum1);
//            setDefaultPinBackground(optNum2);
//            setDefaultPinBackground(optNum3);
//            setDefaultPinBackground(optNum4);
////            setDefaultPinBackground(optNum5);
////            setDefaultPinBackground(optNum6);
//            if (s.length() == 0) {
//                optNum1.setText("");
//                optNum2.setText("");
//                optNum3.setText("");
//                optNum4.setText("");
////                optNum5.setText("");
////                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 1) {
//                optNum1.setText(s.charAt(0) + "");
//                optNum2.setText("");
//                optNum3.setText("");
//                optNum4.setText("");
////                optNum5.setText("");
////                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 2) {
//                optNum2.setText(s.charAt(1) + "");
//                optNum3.setText("");
//                optNum4.setText("");
////                optNum5.setText("");
////                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 3) {
//                optNum3.setText(s.charAt(2) + "");
//                optNum4.setText("");
////                optNum5.setText("");
////                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            } else if (s.length() == 4) {
//                optNum4.setText(s.charAt(3) + "");
////                optNum5.setText("");
////                optNum6.setText("");
//                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
////                ActivityUtils.hideKeyboardFrom(OrderDeliveryActivity.this, optNum4);
////                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//            }
////            else if (s.length() == 5) {
////                optNum5.setText(s.charAt(4) + "");
////                optNum6.setText("");
////                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
////            } else if (s.length() == 6) {
////                optNum6.setText(s.charAt(5) + "");
////                verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
////            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        final int id = v.getId();
//        switch (id) {
//            case R.id.opt_num1:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.opt_num2:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.opt_num3:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.opt_num4:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.opt_num5:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.opt_num6:
//                if (hasFocus) {
//                    setFocus(pinHiddenEditText);
//                    showSoftKeyboard(pinHiddenEditText);
//                }
//                break;
//            case R.id.pickup_opt_num1:
//                if (hasFocus) {
//                    setFocus(pickupPinHiddenEditText);
//                    showSoftKeyboard(pickupPinHiddenEditText);
//                }
//                break;
//            case R.id.pickup_opt_num2:
//                if (hasFocus) {
//                    setFocus(pickupPinHiddenEditText);
//                    showSoftKeyboard(pickupPinHiddenEditText);
//                }
//                break;
//            case R.id.pickup_opt_num3:
//                if (hasFocus) {
//                    setFocus(pickupPinHiddenEditText);
//                    showSoftKeyboard(pickupPinHiddenEditText);
//                }
//                break;
//            case R.id.pickup_opt_num4:
//                if (hasFocus) {
//                    setFocus(pickupPinHiddenEditText);
//                    showSoftKeyboard(pickupPinHiddenEditText);
//                }
//                break;
//            case R.id.cancelled_opt_num1:
//                if (hasFocus) {
//                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                }
//                break;
//            case R.id.cancelled_opt_num2:
//                if (hasFocus) {
//                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                }
//                break;
//            case R.id.cancelled_opt_num3:
//                if (hasFocus) {
//                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                }
//                break;
//            case R.id.cancelled_opt_num4:
//                if (hasFocus) {
//                    setFocus(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                    showSoftKeyboard(orderDeliveryBinding.cancelledPinHiddenEdittext);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            final int id = v.getId();
//            switch (id) {
//                case R.id.pin_hidden_edittext:
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
////                        if (pinHiddenEditText.getText().length() == 6)
////                            optNum6.setText("");
////                        else if (pinHiddenEditText.getText().length() == 5)
////                            optNum5.setText("");
//                        if (pinHiddenEditText.getText().length() == 4)
//                            optNum4.setText("");
//                        else if (pinHiddenEditText.getText().length() == 3)
//                            optNum3.setText("");
//                        else if (pinHiddenEditText.getText().length() == 2)
//                            optNum2.setText("");
//                        else if (pinHiddenEditText.getText().length() == 1)
//                            optNum1.setText("");
//                        if (pinHiddenEditText.length() > 0) {
//                            pinHiddenEditText.setText(pinHiddenEditText.getText().subSequence(0, pinHiddenEditText.length() - 1));
//                            pinHiddenEditText.post(() -> pinHiddenEditText.setSelection(pinHiddenEditText.getText().toString().length()));
//                        }
//                        return true;
//                    }
//                case R.id.opt_num1:
//                case R.id.opt_num2:
//                case R.id.opt_num3:
//                case R.id.opt_num4:
////                case R.id.opt_num5:
////                case R.id.opt_num6:
//                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
//                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
//                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
//                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
//                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
//                    }
//                    break;
//                case R.id.pickup_pin_hidden_edittext:
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        if (pickupPinHiddenEditText.getText().length() == 4)
//                            pickupOptNum4.setText("");
//                        else if (pickupPinHiddenEditText.getText().length() == 3)
//                            pickupOptNum3.setText("");
//                        else if (pickupPinHiddenEditText.getText().length() == 2)
//                            pickupOptNum2.setText("");
//                        else if (pickupPinHiddenEditText.getText().length() == 1)
//                            pickupOptNum1.setText("");
//                        if (pickupPinHiddenEditText.length() > 0) {
//                            pickupPinHiddenEditText.setText(pickupPinHiddenEditText.getText().subSequence(0, pickupPinHiddenEditText.length() - 1));
//                            pickupPinHiddenEditText.post(() -> pickupPinHiddenEditText.setSelection(pickupPinHiddenEditText.getText().toString().length()));
//                        }
//                        return true;
//                    }
//                case R.id.pickup_opt_num1:
//                case R.id.pickup_opt_num2:
//                case R.id.pickup_opt_num3:
//                case R.id.pickup_opt_num4:
//                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
//                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
//                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
//                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
//                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
//                    }
//                    break;
//
//
//                case R.id.cancelled_pin_hidden_edittext:
//                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 4)
//                            orderDeliveryBinding.cancelledOptNum4.setText("");
//                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 3)
//                            orderDeliveryBinding.cancelledOptNum3.setText("");
//                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 2)
//                            orderDeliveryBinding.cancelledOptNum2.setText("");
//                        else if (orderDeliveryBinding.cancelledPinHiddenEdittext.getText().length() == 1)
//                            orderDeliveryBinding.cancelledOptNum1.setText("");
//                        if (orderDeliveryBinding.cancelledPinHiddenEdittext.length() > 0) {
//                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText(orderDeliveryBinding.cancelledPinHiddenEdittext.getText().subSequence(0, orderDeliveryBinding.cancelledPinHiddenEdittext.length() - 1));
//                            orderDeliveryBinding.cancelledPinHiddenEdittext.post(() -> orderDeliveryBinding.cancelledPinHiddenEdittext.setSelection(orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().length()));
//                        }
//                        return true;
//                    }
//                case R.id.cancelled_opt_num1:
//                case R.id.cancelled_opt_num2:
//                case R.id.cancelled_opt_num3:
//                case R.id.cancelled_opt_num4:
//                    if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 ||
//                            keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 ||
//                            keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 ||
//                            keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 ||
//                            keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9) {
//                    }
//                    break;
//                default:
//                    return false;
//            }
//        }
//        return false;
//    }
//
//    private void setDefaultPinBackground(EditText editText) {
//        editText.getBackground().setColorFilter(getResources().getColor(R.color.colorGrey), PorterDuff.Mode.SRC_IN);
//    }
//
//    public void showSoftKeyboard(EditText editText) {
//        if (editText == null)
//            return;
//        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
//        editText.setRawInputType(Configuration.KEYBOARD_12KEY);
//        imm.showSoftInput(editText, 0);
//    }
//
//    public static void setFocus(EditText editText) {
//        if (editText == null)
//            return;
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//    }
//
//    @OnClick(R.id.pharma_contact_number)
//    void onPharmaContactClick() {
//        checkCallPermissionSetting(pickupPhoneNumber);
//    }
//
//    @OnClick(R.id.user_contact_number)
//    void onUserContactClick() {
//        if (userPhoneClick)
//            checkCallPermissionSetting(customerPhoneNumber);
//    }
//
//    private void checkCallPermissionSetting(String phoneNumber) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                Log.d("Permissions_Status", "Permissions not granted");
//               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED)*/
//                // ask for permission
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//            } else {
//                Log.d("Permissions_Status", "We have a permission");
//                // we have a permission
//                requestACall(phoneNumber);
//            }
//        } else {
//            requestACall(phoneNumber);
//        }
//    }
//
//    private void requestACall(String phoneNumber) {
//        Intent intentcall = new Intent();
//        intentcall.setAction(Intent.ACTION_CALL);
//        intentcall.setData(Uri.parse("tel:" + phoneNumber)); // set the Uri
//        startActivity(intentcall);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Intent intent = getIntent();
//            setResult(RESULT_OK, intent);
//            finish();
//            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    //    @OnClick(R.id.finish_activity_img)
//    void onFinishActivityClick() {
//        finish();
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//    }
//
//    @OnClick(R.id.pharmacy_map_view_img)
//    void onPharmaMapImgClick() {
//        isPharmacyLoc = true;
//        isDestinationLoc = false;
//        isStoreLoc = false;
//        gotoTrackMapActivity("Pharmacy", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
////        gotoTrackMapActivity("Pharmacy", 17.4410197, 78.3788463);
//    }
//
//    @OnClick(R.id.map_view_layout)
//    void onMapClick() {
//        isPharmacyLoc = false;
//        isDestinationLoc = true;
//        isStoreLoc = false;
//        gotoTrackMapActivity("Destination", this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude());
////        gotoTrackMapActivity("Destination", 17.4411128, 78.3827845);
//    }
//
//    @Override
//    public void onClickReturntoStoreShowMap() {
//        isPharmacyLoc = false;
//        isDestinationLoc = false;
//        isStoreLoc = true;
//        gotoTrackMapActivity("Store", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
////        gotoTrackMapActivity("Store", 17.4411128, 78.3827845);
//    }
//
//    @Override
//    public void onSuccessOrderPaymentTypeInCod(OrderPaymentSelectResponse orderPaymentSelectResponse) {
//        orderDeliveryBinding.paymentTypeIncod.setText("( " + orderPaymentSelectResponse.getData().getOrderPayment().getType().getUid() + " )");
//        orderDeliveryBinding.paymentTypeIncod.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onFailureOrderPaymentTypeInCod(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onSuccessOrderEndJourneyUpdateApiCall(OrderEndJourneyUpdateResponse orderEndJourneyUpdateResponse) {
//        isOrderDelivered = true;
//        System.out.println("onSuccessOrderEndJourneyUpdateApiCall:::::::::::::::::::::::::" + orderEndJourneyUpdateResponse.getMessage());
//    }
//
//    @Override
//    public void onLogout() {
//        getSessionManager().clearAllSharedPreferences();
//        NavigationActivity.getInstance().stopBatteryLevelLocationService();
//        Intent intent = new Intent(OrderDeliveryActivity.this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//    }
//
//    @Override
//    public boolean isOrderCancelled() {
//        return isOrderCancelled;
//    }
//
//    @Override
//    public void orderCancelled() {
//        finish();
//    }
//
//    private void gotoTrackMapActivity(String locType, double latitude, double longitude) {
//        if (checkForLocPermission()) {
//            if (checkGPSOn(this)) {
//                Intent intent = new Intent(this, TrackMapActivity.class);
//                intent.putExtra("locType", locType);
//                intent.putExtra("Lat", latitude);
//                intent.putExtra("Lon", longitude);
//                intent.putExtra("order_number", orderNumber);
//                intent.putExtra("order_uid", this.orderDetailsResponse.getData().getUid());
//                intent.putExtra("order_state", this.orderDetailsResponse.getData().getOrderState().getName());
//                startActivityForResult(intent, 105);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//            } else {
//                showGPSDisabledAlertToUser(this);
//            }
//        } else {
//            requestForLocPermission(REQ_LOC_PERMISSION);
//            return;
//        }
//    }
//
//    private boolean checkForLocPermission() {
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean checkGPSOn(Context context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//
//    public void showGPSDisabledAlertToUser(Context context) {
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
//        alertDialogBuilder.setTitle(getString(R.string.alert));
//        alertDialogBuilder.setMessage(getString(R.string.permission_gps_bogy))
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.open_settings),
//                        (dialog, id) -> {
//                            Intent callGPSSettingIntent = new Intent(
//                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(callGPSSettingIntent);
//                        });
//        alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
//                (dialog, id) -> dialog.cancel());
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }
//
//    private void requestForLocPermission(final int reqCode) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
//            // Show an explanation to the user *asynchronously* -- don't block
//            // this thread waiting for the user's response! After the user
//            // sees the explanation, try again to request the permission.
//            DialogManager.showSingleBtnPopup(this, new DialogMangerCallback() {
//                @Override
//                public void onOkClick(View v) {
//                    ActivityCompat.requestPermissions(OrderDeliveryActivity.this, new String[]{ACCESS_FINE_LOCATION}, reqCode);
//                }
//
//                @Override
//                public void onCancelClick(View view) {
//
//                }
//            }, getString(R.string.app_name), getString(R.string.locationPermissionMsg), getString(R.string.ok));
//        } else {
//            // No explanation needed, we can request the permission.
//            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, reqCode);
//            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//            // app-defined int constant. The callback method gets the
//            // result of the request.
//        }
//    }
//
//    private boolean isOrderCancelled = false;
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null)
//            if (intent.getBooleanExtra("order_cancelled", false) && intent.getStringExtra("order_uid").equals(orderUid)) {
//                Dialog alertDialog = new Dialog(this);
//                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
//                alertDialog.setContentView(alertMessageBinding.getRoot());
//                alertDialog.setCancelable(false);
//                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
//                    alertDialog.dismiss();
//                    if (orderDeliveryBinding.orderStatusHeader.getText().toString().equals("Order Assigned") || orderDeliveryBinding.orderStatusHeader.getText().toString().equals("Order Rider Updated")) {
//                        finish();
//                    } else {
//                        this.isOrderCancelled = true;
//                        ActivityUtils.showDialog(this, "Please Wait");
//                        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "", "", transactionId);
//                    }
//                });
//                alertDialog.show();
//            } else if (intent.getBooleanExtra("order_shifted", false)) {
//                Dialog alertDialog = new Dialog(this);
//                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
//                alertDialog.setContentView(alertMessageBinding.getRoot());
//                alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
//                alertDialog.setCancelable(false);
//                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
//                    alertDialog.dismiss();
//                    finish();
//                });
//                alertDialog.show();
//            } else if (intent.getBooleanExtra("COMPLAINT_RESOLVED", false)) {
//                Dialog alertDialog = new Dialog(this);
//                DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
//                alertDialog.setContentView(alertMessageBinding.getRoot());
//                alertMessageBinding.message.setText(intent.getStringExtra("NOTIFICATION"));
//                alertDialog.setCancelable(false);
//                alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
//                    alertDialog.dismiss();
//                });
//                alertDialog.show();
//            } else {
//                orderDeliveryBinding.notificationDot.setVisibility(View.VISIBLE);
//            }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQ_LOC_PERMISSION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    if (isPharmacyLoc) {
//                        gotoTrackMapActivity("Pharmacy", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
//                    } else if (isDestinationLoc) {
//                        gotoTrackMapActivity("Destination", this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude());
//                    } else if (isStoreLoc) {
//                        gotoTrackMapActivity("Store", this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude());
//                    }
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    DialogManager.showToast(this, getString(R.string.noAccessTo));
//                }
//            }
//            break;
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    @Override
//    protected Merlin createMerlin() {
//        return new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks().build(this);
//    }
//
//    @Override
//    protected void onResume() {
//        liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
//        liveDistanceCalculateHandler.postDelayed(liveDistanceCalculateRunnable, 3000);
//        CommonUtils.CURRENT_SCREEN = getClass().getSimpleName();
//        Hawk.put(LAST_ACTIVITY, getClass().getSimpleName());
//        super.onResume();
//        startService(new Intent(OrderDeliveryActivity.this, FloatingTouchService.class));
//        if (GPSLocationService.isFromSetting == true) {
//            GPSLocationService.isFromSetting = false;
//        }
//        if (CommonUtils.isIs_order_delivery_or_track_map_screen) {
//            CommonUtils.isIs_order_delivery_or_track_map_screen = false;
//            new OrderDeliveryActivityController(this, this).orderDetailsApiCall(getSessionManager().getLoginToken(), orderUid, orderDeliveryBinding);
//        }
//    }
//
//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onFailureMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onClickBackIcon() {
//        onBackPressed();
//    }
//
//    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
//    @Override
//    public void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse) {
//        try {
//            this.orderDetailsResponse = orderDetailsResponse;
//            if (this.orderDetailsResponse != null) {
//                if (this.orderDetailsResponse.getData() != null) {
//                    this.paymentType = this.orderDetailsResponse.getData().getPaymentType().getName();
//                    if (!this.paymentType.equals("COD")) {
//                        orderDeliveryBinding.collectPayment.setVisibility(View.GONE);
//                        orderDeliveryBinding.completedDeliveryText.setText("4. Complete delivery");
//                    } else {
//                        orderDeliveryBinding.collectPayment.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.completedDeliveryText.setText("5. Complete delivery");
//                        orderDeliveryBinding.totalAmount.setText(getResources().getString(R.string.label_rupee_symbol) + " " + String.valueOf(this.orderDetailsResponse.getData().getPakgValue()));
//                    }
//                    if (this.orderDetailsResponse.getData().getCancelAllowed() != null && this.orderDetailsResponse.getData().getCancelAllowed().getName().equals("Yes")) {
//                        if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNPICKED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNORDERRTO")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERACCEPTED")
//                                || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERUPDATE")) {
//                            if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() > 2) {
//                                cancelOrderBtn.setVisibility(View.VISIBLE);
//                            } else {
//                                cancelOrderBtn.setVisibility(View.GONE);
//                            }
//                        } else {
//                            cancelOrderBtn.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        cancelOrderBtn.setVisibility(View.GONE);
//                    }
//                    this.orderUid = this.orderDetailsResponse.getData().getUid();
//                    this.orderNumber = this.orderDetailsResponse.getData().getOrderNumber();
//                    orderDeliveryBinding.orderStatusHeader.setText(this.orderDetailsResponse.getData().getOrderStatus().getName());
//
//                    orderDeliveryBinding.orderNumber.setText("#" + String.valueOf(this.orderDetailsResponse.getData().getOrderNumber()));
////                    orderDeliveryBinding.crateAmount.setText(String.valueOf(this.orderDetailsResponse.getData().getCrateAmount()));
////                    orderDeliveryBinding.paymentType.setText(this.orderDetailsResponse.getData().getPaymentType().getName());
//
//
////                    String customerAddresss = orderDetailsResponse.getData().getDeliverApartment() + ", " + orderDetailsResponse.getData().getDeliverStreetName() + ", " + orderDetailsResponse.getData().getDeliverCity() + ", " + orderDetailsResponse.getData().getDeliverState() + ", " + orderDetailsResponse.getData().getDelPincode() + ", " + orderDetailsResponse.getData().getDeliverCountry();
////                    String pickupAddress = orderDetailsResponse.getData().getPickupApt() + ", " + orderDetailsResponse.getData().getPickupStreetName() + ", " + orderDetailsResponse.getData().getPickupCity() + ", " + orderDetailsResponse.getData().getPickupState() + ", " + orderDetailsResponse.getData().getPickupPincode() + ", " + orderDetailsResponse.getData().getPickupCountry();
////                    String returnAddress = orderDetailsResponse.getData().getReturnApartment() + ", " + orderDetailsResponse.getData().getReturnStreetName() + ", " + orderDetailsResponse.getData().getReturnCity() + ", " + orderDetailsResponse.getData().getReturnState() + ", " + orderDetailsResponse.getData().getReturnPincode() + ", " + orderDetailsResponse.getData().getReturnCountry();
//
//
//                    String customerAddresss = "";
//                    if (orderDetailsResponse.getData().getDeliverApartment() != null) {
//                        customerAddresss = orderDetailsResponse.getData().getDeliverApartment() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getDeliverStreetName() != null) {
//                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverStreetName() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getDeliverCity() != null) {
//                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverCity() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getDeliverState() != null) {
//                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverState() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getDelPincode() != null) {
//                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDelPincode() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getDeliverCountry() != null) {
//                        customerAddresss = customerAddresss + orderDetailsResponse.getData().getDeliverCountry();
//                    }
//
//                    String pickupAddress = "";
//                    if (orderDetailsResponse.getData().getPickupApt() != null) {
//                        pickupAddress = orderDetailsResponse.getData().getPickupApt() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getPickupStreetName() != null) {
//                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupStreetName() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getPickupCity() != null) {
//                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupCity() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getPickupState() != null) {
//                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupState() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getPickupPincode() != null) {
//                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupPincode() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getPickupCountry() != null) {
//                        pickupAddress = pickupAddress + orderDetailsResponse.getData().getPickupCountry();
//                    }
//
//                    String returnAddress = "";
//                    if (orderDetailsResponse.getData().getReturnApartment() != null) {
//                        returnAddress = orderDetailsResponse.getData().getReturnApartment() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getReturnStreetName() != null) {
//                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnStreetName() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getReturnCity() != null) {
//                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnCity() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getReturnState() != null) {
//                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnState() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getReturnPincode() != null) {
//                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnPincode() + ", ";
//                    }
//                    if (orderDetailsResponse.getData().getReturnCountry() != null) {
//                        returnAddress = returnAddress + orderDetailsResponse.getData().getReturnCountry();
//                    }
//
//
//                    if (orderDetailsResponse.getData().getBranpickupVerCode() != null)
//                        this.branPickupVerificationCode = orderDetailsResponse.getData().getBranpickupVerCode();
//                    if (orderDetailsResponse.getData().getBranreturnVerCode() != null)
//                        this.branReturnVerificatonCode = orderDetailsResponse.getData().getBranreturnVerCode();
//                    if (orderDetailsResponse.getData().getCusPickupVerCode() != null)
//                        this.cusPickupVerificationCode = orderDetailsResponse.getData().getCusPickupVerCode();
//                    if (orderDetailsResponse.getData().getCusDeliveryVerCode() != null)
//                        this.cusDeliveryVerificationCode = orderDetailsResponse.getData().getCusDeliveryVerCode();
//
//                    if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERUPDATE") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DSPASSIGN") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("ORDERACCEPTED") || (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() <= 2)) {
//                        orderCurrentStatus = 1;
//                        onClickPickedLabel();
//                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                        orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                        orderDeliveryBinding.customerTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                        orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                        orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                        if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//                            orderDeliveryBinding.returnLabel.setVisibility(View.VISIBLE);
//
//                            orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                            orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                            orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                            orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                            if (orderDetailsResponse.getData().getCusPickupVerCode() == null || orderDetailsResponse.getData().getCusPickupVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpCustPickup())) {
//                                orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
//                                orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
//                                isCusPickupVerificationCode = true;
//                                orderDeliveryBinding.pickupOptNum1.setText("0");
//                                orderDeliveryBinding.pickupOptNum2.setText("0");
//                                orderDeliveryBinding.pickupOptNum3.setText("0");
//                                orderDeliveryBinding.pickupOptNum4.setText("0");
//                                orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
//                                orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                                orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
//                            }
//                        } else {
//                            orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                            orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
//                            orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                            orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                            if (orderDetailsResponse.getData().getBranpickupVerCode() == null || orderDetailsResponse.getData().getBranpickupVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpPickup())) {
//                                orderDeliveryBinding.pickupOtpVerifyText.setText("1. Pickup verification");
//                                orderDeliveryBinding.pickupOtpEditTextLayout.setVisibility(View.GONE);
//                                isBranPickupVerificationCode = true;
//                                orderDeliveryBinding.pickupOptNum1.setText("0");
//                                orderDeliveryBinding.pickupOptNum2.setText("0");
//                                orderDeliveryBinding.pickupOptNum3.setText("0");
//                                orderDeliveryBinding.pickupOptNum4.setText("0");
//                                orderDeliveryBinding.pickupVerifyOtpBtn.setText("Verify");
//                                orderDeliveryBinding.pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                                orderDeliveryBinding.pickupPinHiddenEdittext.setText("00000000");
//                            }
//                        }
//
//
//                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("PICKUP") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("OUTFORDELIVERY") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNPICKED")) {
//                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                        continueProcessLayout.setVisibility(View.GONE);
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_header_bg));
//                        orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
//                        orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                        orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorBlack));
//                        orderCurrentStatus = 2;
//                        if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                            selectionTag = 10;
//                            orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//                            orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                            orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                            orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                            orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                            orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                            orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                            orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
//                            orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
//                            orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
//                            orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                            if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
//                                orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
//                                orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
////                                isBranReturnVerificatonCode = true;
//                                isCusDeliveryVerificationCode = true;
//                                orderDeliveryBinding.cancelledOptNum1.setText("0");
//                                orderDeliveryBinding.cancelledOptNum2.setText("0");
//                                orderDeliveryBinding.cancelledOptNum3.setText("0");
//                                orderDeliveryBinding.cancelledOptNum4.setText("0");
//                                orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
//                                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                                orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
//                            }
//                            new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
//                        } else {
//                            onClickDeliveredLabel();
//                            selectionTag = 1;
//                            onContinueDrivingClick();
//                            if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpDelivery())) {
//                                orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
//                                orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
//                                isCusDeliveryVerificationCode = true;
//                                orderDeliveryBinding.optNum1.setText("0");
//                                orderDeliveryBinding.optNum2.setText("0");
//                                orderDeliveryBinding.optNum3.setText("0");
//                                orderDeliveryBinding.optNum4.setText("0");
//                                orderDeliveryBinding.verifyOtpBtn.setText("Verify");
//                                orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                                orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
//                            }
//                            new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
//                        }
//                    } else if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("RETURNORDERRTO") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELORDERRTO")) {
//                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                        orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
//                        orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
//                        onClickPickedLabel();
//                        onClickDeliveredLabel();
//                        selectionTag = 1;
//                        onContinueDrivingClick();
//                        isOrderDelivered = true;
//                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
//                    } else if ((this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() > 2) || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("DELIVERYFAILED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELRETURNINITIATED") || this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
//                        orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                        selectionTag = 10;
//                        orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//                        new OrderDeliveryActivityController(this, this).orderStatusHistoryListApiCall(this.orderDetailsResponse.getData().getUid());
//                        orderDeliveryBinding.cancelOrderBtn.setVisibility(View.GONE);
//                        orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
//                        if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
//                            orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
//                            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                            isBranReturnVerificatonCode = true;
//                            orderDeliveryBinding.cancelledOptNum1.setText("0");
//                            orderDeliveryBinding.cancelledOptNum2.setText("0");
//                            orderDeliveryBinding.cancelledOptNum3.setText("0");
//                            orderDeliveryBinding.cancelledOptNum4.setText("0");
//                            orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
//                            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                            orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
//                        }
//                    }
//                    if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                        cancelOrderBtn.setVisibility(View.GONE);
//                        orderDeliveryBinding.actionBarDeliverBy.setText("Pickup by: ");
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                        String orderDate = orderDetailsResponse.getData().getPickupEtWindo();
//                        Date orderDates = formatter.parse(orderDate);
//                        long orderDateMills = orderDates.getTime();
//                        orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//
//
//                        //pickup address and details*******************************************
//                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getPickupAccName());
////                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText(this.orderDetailsResponse.getData().getPickupAddId());
//                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
//                        orderDeliveryBinding.pharmacyAddress.setText(pickupAddress);
//                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
//                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
//                        if (orderDetailsResponse.getData().getPickupNotes() != null && !orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
//                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
//                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
//                        } else {
//                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
//                        }
//                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
//
//
//                        //delivery address and details*********************************************
//
//                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getReturnAccName());
////                        orderDeliveryBinding.customerheadName.setText(this.orderDetailsResponse.getData().getReturnAddId());
//                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
//                        orderDeliveryBinding.customerAddress.setText(returnAddress);
//                        String returnMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//                        orderDeliveryBinding.userMobileNumber.setText("*******" + returnMobileNumber.substring(returnMobileNumber.length() - 3));
//                        orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
//                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//
//
//                        // order not delivered address and details
//                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getReturnAccName());
////                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText(this.orderDetailsResponse.getData().getReturnAddId());
//                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
//                        orderDeliveryBinding.orderNotDeliveryAddress.setText(returnAddress);
//                        String orderNotDeliveredPhoneNubmber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//                        orderDeliveryBinding.orderNotDeliveredPhoneNumber.setText("*******" + orderNotDeliveredPhoneNubmber.substring(orderNotDeliveredPhoneNubmber.length() - 3));
//                        this.orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//
//                    } else {
//                        orderDeliveryBinding.actionBarDeliverBy.setText("Deliver by: ");
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                        String orderDate = orderDetailsResponse.getData().getDelEtWindo();
//                        Date orderDates = formatter.parse(orderDate);
//                        long orderDateMills = orderDates.getTime();
//                        orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//
//                        //pickup address and details
//                        orderDeliveryBinding.apolloPhamrmacyAddId.setText(this.orderDetailsResponse.getData().getPickupAccName());
////                        orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText(this.orderDetailsResponse.getData().getPickupAddId());
//                        orderDeliveryBinding.pharmacyLandmark.setText(this.orderDetailsResponse.getData().getPickupLndmrk());
//                        orderDeliveryBinding.pharmacyAddress.setText(pickupAddress);
//                        String pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
//                        orderDeliveryBinding.pickupPhoneNumber.setText("*******" + pickupPhoneNumber.substring(pickupPhoneNumber.length() - 3));
//                        if (orderDetailsResponse.getData().getPickupNotes() != null && !orderDetailsResponse.getData().getPickupNotes().isEmpty()) {
//                            orderDeliveryBinding.pickupInstruction.setVisibility(View.VISIBLE);
//                            orderDeliveryBinding.pickupInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
//                        } else {
//                            orderDeliveryBinding.pickupInstruction.setVisibility(View.GONE);
//                        }
//                        this.pickupPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getPickupPn());
//
//                        //delivery address and details
//                        orderDeliveryBinding.customerName.setText(this.orderDetailsResponse.getData().getDelAccName());
////                        orderDeliveryBinding.customerheadName.setText(this.orderDetailsResponse.getData().getDelAddId());
//                        orderDeliveryBinding.customerLandmark.setText(this.orderDetailsResponse.getData().getDeliverLandmark());
//                        orderDeliveryBinding.customerAddress.setText(customerAddresss);
//                        String userMobileNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
//                        orderDeliveryBinding.userMobileNumber.setText("*******" + userMobileNumber.substring(userMobileNumber.length() - 3));
//                        if (orderDetailsResponse.getData().getDeliverNotes() != null && !orderDetailsResponse.getData().getDeliverNotes().isEmpty()) {
//                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.VISIBLE);
//                            orderDeliveryBinding.deliveryInstructionText.setText(orderDetailsResponse.getData().getPickupNotes());
//                        } else {
//                            orderDeliveryBinding.deliveryInstruction.setVisibility(View.GONE);
//                        }
//                        this.customerPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getDelPn());
//
//                        //order not delivery address and details
//                        orderDeliveryBinding.orderNotDeliveredAddId.setText(this.orderDetailsResponse.getData().getReturnAccName());
////                        orderDeliveryBinding.orderNotDeliveredAddHeadId.setText(this.orderDetailsResponse.getData().getReturnAddId());
//                        orderDeliveryBinding.orderNotDeliveryLandmark.setText(this.orderDetailsResponse.getData().getReturnLandmark());
//                        orderDeliveryBinding.orderNotDeliveryAddress.setText(returnAddress);
//                        String orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//                        orderDeliveryBinding.orderNotDeliveredPhoneNumber.setText("*******" + orderNotDeliveredPhoneNumber.substring(orderNotDeliveredPhoneNumber.length() - 3));
//                        this.orderNotDeliveredPhoneNumber = String.valueOf(this.orderDetailsResponse.getData().getReturnPn());
//                    }
//                    orderDeliveryBinding.orderListLayout.removeAllViews();
//                    if (orderDetailsResponse.getData().getItems() != null && orderDetailsResponse.getData().getItems().size() > 0) {
//                        TextView itemsCount = (TextView) findViewById(R.id.items_count);
//                        itemsCount.setText(orderDetailsResponse.getData().getItems().size() + " " + "Items");
//                        for (int i = 0; i < orderDetailsResponse.getData().getItems().size(); i++) {
//                            LayoutInflater layoutInflater = LayoutInflater.from(this);
//                            LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.view_order_list, null, false);
//
//                            TextView orderName = layout.findViewById(R.id.order_name);
//                            TextView orderCount = layout.findViewById(R.id.order_count);
//                            TextView orderPrice = layout.findViewById(R.id.order_price);
//
//                            orderName.setText(orderDetailsResponse.getData().getItems().get(i).getItemname());
//                            orderCount.setText(orderDetailsResponse.getData().getItems().get(i).getItemquantity());
//                            orderPrice.setText(String.valueOf(orderDetailsResponse.getData().getItems().get(i).getItemprice()));
//
//                            orderDeliveryBinding.orderListLayout.addView(layout);
//                        }
//                    }
//                }
//            }
//            orderDeliveryBinding.loadingWhiteScreen.setVisibility(View.GONE);
//            ActivityUtils.hideDialog();
//        } catch (Exception e) {
//            System.out.println("onSuccessOrderSelectApi call:::::::::::::::::::::::::::" + e);
//        }
//    }
//
//    @Override
//    public void onFialureOrderDetailsApiCall() {
//
//    }
//
//    @Override
//    public void onClickPickupVerifyOtp() {
//        if ((this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") ? isCusPickupVerificationCode : isBranPickupVerificationCode) || (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN") ? pickupPinHiddenEditText.getText().toString().trim().equals(cusPickupVerificationCode) : pickupPinHiddenEditText.getText().toString().trim().equals(branPickupVerificationCode))) {
//            hideKeyboard();
//            ActivityUtils.showDialog(this, "Please wait.");
//            if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN"))
//                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("RETURNPICKED", orderUid, "", "", transactionId);
//            else
//                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("PICKUP", orderUid, "", "", transactionId);
//        } else {
//            pickupOtpEditTextLayout.setVisibility(View.VISIBLE);
//            pickupVerifyOtpBtn.setVisibility(View.VISIBLE);
//            pickupOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
//            pickupVerifiedOtpText.setVisibility(View.GONE);
//            pickupVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//        }
//    }
//
//    @Override
//    public void onFialureMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private boolean isOrderPicked = false;
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    @Override
//    public void onSuccessOrderSaveUpdateStatusApi(String status) {
//        try {
//            int deliveryFailureAttempts = Integer.parseInt(getSessionManager().getGlobalSettingSelectResponse().getData().getDeliverAttempts().getUid());
//            if (status.equals("PICKU")) {
//                orderDeliveryBinding.orderStatusHeader.setText("Order Pickup");
//                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("OUTFORDELIVERY", orderUid, "", "", transactionId);
//            } else if (status.equals("PICKUP") || status.equals("RETURNPICKED")) {//|| status.equals("OUTFORDELIVERY")
//                DrawRouteMaps.getInstance(this, this, this, this).draw(
//                        new LatLng(this.orderDetailsResponse.getData().getPickupLatitude(), this.orderDetailsResponse.getData().getPickupLongitude()),
//                        new LatLng(this.orderDetailsResponse.getData().getDeliverLatitude(), this.orderDetailsResponse.getData().getDeliverLongitude()), null, 0);
//
//                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//
//                isOrderPicked = true;
//                orderCurrentStatus = 2;
//                selectionTag = 1;
//
//                orderDeliveryBinding.apolloPhamrmacyAddHeadCompletedIcon.setVisibility(View.VISIBLE);
//                if (status.equals("PICKUP")) {
//                    if (this.orderDetailsResponse.getData().getCancelAllowed() != null && this.orderDetailsResponse.getData().getCancelAllowed().getName().equals("Yes")) {
//                        cancelOrderBtn.setVisibility(View.VISIBLE);
//                    }
//                    orderDeliveryBinding.orderStatusHeader.setText("Out For Delivery");
//                } else {
//                    orderDeliveryBinding.orderStatusHeader.setText("Return Picked");
//                }
//                orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                orderDeliveryBinding.pickupDetailsProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                orderDeliveryBinding.pickupDetailsInnerHead.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                orderDeliveryBinding.customerTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                orderDeliveryBinding.pickupDetailsInnerHead.setText("Order Picked");
//                orderDeliveryBinding.apolloPhamrmacyAddHeadId.setText("Order Picked");
//                orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                String orderDate = CommonUtils.getCurrentTimeDate();
//                Date orderDates = formatter.parse(orderDate);
//                long orderDateMills = orderDates.getTime();
//                orderDeliveryBinding.pickupOtpVerifyText.setText("1. Picked up " + CommonUtils.getTimeFormatter(orderDateMills));
//                pickupOtpVerificationChildLayoutEdge.setLayoutTransition(null);
//                pickupOtpVerificationChildLayout.setVisibility(View.GONE);
//                pickupOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                // continueProcessLayout.setVisibility(View.VISIBLE);
//                onClickPicked();
//
//
//                if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    orderDeliveryBinding.orderCancelHeadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                    orderDeliveryBinding.orderCancelTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
//                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.pharmacy_circle_color));
//                    orderDeliveryBinding.returnLabel.setVisibility(View.GONE);
//                    orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                    if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
//                        orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
//                        orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                        isCusDeliveryVerificationCode = true;
////                        isBranReturnVerificatonCode = true;
//                        orderDeliveryBinding.cancelledOptNum1.setText("0");
//                        orderDeliveryBinding.cancelledOptNum2.setText("0");
//                        orderDeliveryBinding.cancelledOptNum3.setText("0");
//                        orderDeliveryBinding.cancelledOptNum4.setText("0");
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
//                    }
//                } else {
//                    onContinueDrivingClick();
//                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorBlack));
//                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_processing_curves_bg));
//                    if (orderDetailsResponse.getData().getCusDeliveryVerCode() == null || orderDetailsResponse.getData().getCusDeliveryVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpDelivery())) {
//                        orderDeliveryBinding.deliveryOtpVerificationText.setText("2. Delivery verification");
//                        orderDeliveryBinding.otpEditTextLayout.setVisibility(View.GONE);
//                        isCusDeliveryVerificationCode = true;
//                        orderDeliveryBinding.optNum1.setText("0");
//                        orderDeliveryBinding.optNum2.setText("0");
//                        orderDeliveryBinding.optNum3.setText("0");
//                        orderDeliveryBinding.optNum4.setText("0");
//                        orderDeliveryBinding.verifyOtpBtn.setText("Verify");
//                        orderDeliveryBinding.verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        orderDeliveryBinding.pinHiddenEdittext.setText("00000000");
//                    }
//                }
//                ActivityUtils.hideDialog();
//            } else if (status.equals("DELIVERED") || status.equals("RETURNORDERRTO")) {
////                ActivityUtils.hideDialog();
//                if (status.equals("DELIVERED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Delivered");
//                else if (status.equals("RETURNORDERRTO"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Return Order To Store");
//                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
//                orderCurrentStatus = 3;
//                orderDeliveryBinding.actionBarDeliverBy.setText("Delivered at: ");
//                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
//                orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
//                if (orderDetailsResponse.getData().getBranreturnVerCode() == null ||
//                        orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
//                    orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
//                }
//                if (this.paymentType.equals("COD")) {
//                    orderDeliveryBinding.paymentTypeIncod.setVisibility(View.VISIBLE);
//                    if (orderDeliveryBinding.paymentCash.isChecked())
//                        orderDeliveryBinding.paymentTypeIncod.setText("( Cash )");
//                    else if (orderDeliveryBinding.paymentCard.isChecked())
//                        orderDeliveryBinding.paymentTypeIncod.setText("( Card )");
//                    else if (orderDeliveryBinding.paymentWallet.isChecked())
//                        orderDeliveryBinding.paymentTypeIncod.setText("( Wallet )");
//                }
//
//                if (this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                    ActivityUtils.hideDialog();
//                    orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                    orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//
//
//                    orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Order Returned");
//                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Order Returned");
//                    orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                    orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                    orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//
//
//                    orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                    orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.GONE);
//                    orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                    orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//
//                    orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                    cancelItemBtn.setVisibility(View.GONE);
//                    cancelOrderBtn.setVisibility(View.GONE);
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                    String orderDate = CommonUtils.getCurrentTimeDate();
//                    Date orderDates = formatter.parse(orderDate);
//                    long orderDateMills = orderDates.getTime();
//                    orderDeliveryBinding.cancelledOrderSuccessStatus.setText("Order Returned to store");
//                    orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                    orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                    orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//                    orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
//                    orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
//                } else {
//                    orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                    orderDeliveryBinding.deliveryTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                    orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorBlack));
//
//                    orderDeliveryBinding.deliveryItemsView.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//                    orderDeliveryBinding.deliverInnerHeader.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.deliverInnerHeader.setText("Order Delivered");
//
//                    orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                    orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//
//                    otpEditTextLayout.setVisibility(View.GONE);
//                    verifyOtpBtn.setVisibility(View.GONE);
//                    otpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                    orderDeliveryBinding.collectPaymentRadio.setVisibility(View.GONE);
//                    orderDeliveryBinding.collectPaymentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.collectPaymentImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                    verifiedOtpText.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.collectPaymentText.setText("4. Collected Payment");
//                    orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                    String orderDate = CommonUtils.getCurrentTimeDate();
//                    Date orderDates = formatter.parse(orderDate);
//                    long orderDateMills = orderDates.getTime();
//                    orderDeliveryBinding.orderCompletedSuccessfullyDateAndTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                    orderDeliveryBinding.deliveryonDatetime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//
//                    //deliver header
//                    orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.customerheadName.setText("Order Delivered");
////                orderDeliveryBinding.deliverNameHeadLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                    orderDeliveryBinding.deliverNameHeadCompletedIcon.setVisibility(View.VISIBLE);
//                    orderCurrentStatus = 0;
//
//                    orderDeliveredChildOneLayout.setVisibility(View.GONE);
//                    orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);
//
//                    verifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                    cancelItemBtn.setVisibility(View.GONE);
//                    cancelOrderBtn.setVisibility(View.GONE);
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.MATCH_PARENT);
//                    int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
//                    int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
//                    int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, this);
//                    int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, this);
//                    params.setMargins(0, 0, marginEnd, marginBottom);
////                deliveryItemsView.setLayoutParams(params);
//
////                new Handler().postDelayed(() -> {
//////                NavigationActivity.getInstance().showFragment(new DashboardFragment(), R.string.menu_dashboard);
//////                NavigationActivity.getInstance().updateSelection(1);
////
////                    Intent intent = getIntent();
////                    intent.putExtra("OrderCompleted", "true");
////                    setResult(RESULT_OK, intent);
////                    finish();
////                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
////                }, 1000);
//                }
//            } else if (status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() < deliveryFailureAttempts - 1) {
//                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
//                finish();
//            }
////            else if ((status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() >= deliveryFailureAttempts) || status.equals("DELIVERYFAILED")) {
////
////                if (status.equals("DELIVERYATTEMPTED"))
////                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Attempted");
////                else if (status.equals("DELIVERYFAILED"))
////                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Failed");
//////                onSuccessOrderSaveUpdateStatusApi("CANCELRETURNINITIATED");
//////                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "CANCELRETURNINITIATED", null);
////            }
//            else if ((status.equals("DELIVERYATTEMPTED") && this.orderDetailsResponse.getData().getFailureAttempts() >= deliveryFailureAttempts - 1)
//                    || status.equals("DELIVERYFAILED")
//                    || status.equals("CANCELRETURNINITIATED")) {
//                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                ActivityUtils.hideDialog();
//                orderCurrentStatus = 4;
//                orderDeliveryBinding.deliveredLabel.setVisibility(View.GONE);
//                orderDeliveryBinding.delivered.setVisibility(View.GONE);
//                orderDeliveryBinding.customerheadName.setText("Delivery Attempted");
////                orderDeliveryBinding.cancelledOtpVerificationParentLayoutParent.setVisibility(View.GONE);
////                orderDeliveryBinding.handovertoPharmacy.setText("2. Handedover to pharmacy");
//
//                if (!this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//                    orderDeliveryBinding.deliveredLabelProcessingLineHead.setVisibility(View.VISIBLE);
//                    orderDeliveryBinding.deliveredLabel.setVisibility(View.VISIBLE);
//                }
//
//                if (!isOrderPicked) {
//                    orderDeliveryBinding.apolloPhamrmacyAddHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.pickupDetailsInnerHeadIdLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.pickupDetailsProcessingLineHead.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//
//                    orderDeliveryBinding.apolloPhamrmacyAddHeadId.setTextColor(getResources().getColor(R.color.colorGrey));
//                    orderDeliveryBinding.customerHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                    orderDeliveryBinding.customerHeadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                    isOrderCancelledandNotPicked = true;
//                    onClickPicked();
//                    Dialog alertDialog = new Dialog(this);
//                    DialogAlertMessageBinding alertMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_message, null, false);
//                    alertDialog.setContentView(alertMessageBinding.getRoot());
//                    alertDialog.setCancelable(false);
//                    alertMessageBinding.dialogButtonOk.setOnClickListener(v -> {
//                        alertDialog.dismiss();
//                        finish();
//                    });
//                    alertDialog.show();
//                } else {
//                    orderDeliveryBinding.deliverNameHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    orderDeliveryBinding.deliverNameInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                    if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty() || (getSessionManager().getGlobalSettingSelectResponse() != null && !getSessionManager().getGlobalSettingSelectResponse().getData().getOtpBranchHndovr())) {
//                        orderDeliveryBinding.cancelledOtpVerifyText.setText("2. Return verification");
//                        orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                        isBranReturnVerificatonCode = true;
//                        orderDeliveryBinding.cancelledOptNum1.setText("0");
//                        orderDeliveryBinding.cancelledOptNum2.setText("0");
//                        orderDeliveryBinding.cancelledOptNum3.setText("0");
//                        orderDeliveryBinding.cancelledOptNum4.setText("0");
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setText("Verify");
//                        orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                        orderDeliveryBinding.cancelledPinHiddenEdittext.setText("00000000");
//                    }
//                }
//                if (status.equals("DELIVERYATTEMPTED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Attempted");
//                else if (status.equals("DELIVERYFAILED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Delivery Failed");
//                else if (status.equals("CANCELRETURNINITIATED"))
//                    orderDeliveryBinding.orderStatusHeader.setText("Cancel Return Initiated");
//
//                orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_disable_curves_bg));
//                orderDeliveryBinding.orderDeliveryProcessImg.setVisibility(View.VISIBLE);
//                orderDeliveryBinding.orderNotDeliveredParentLayout.setVisibility(View.VISIBLE);
//                cancelItemBtn.setVisibility(View.GONE);
//                cancelOrderBtn.setVisibility(View.GONE);
//                orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
//                orderDeliveryBinding.deliveryheadTxt.setBackground(getResources().getDrawable(R.drawable.order_disable_circle_bg));
//                orderDeliveryBinding.customerheadName.setTextColor(getResources().getColor(R.color.colorGrey));
//                orderDeliveryBinding.deliveryheadTxt.setTextColor(getResources().getColor(R.color.colorGrey));
//                orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.order_active_circle_bg));
////                onClickDelivered();
//            } else if (status.equals("CANCELORDERRTO")) {
//                new OrderDeliveryActivityController(this, this).orderEndJourneyUpdateApiCall(orderUid);
//                isOrderDelivered = true;
//                orderDeliveryBinding.pharmacyMapViewImg.setVisibility(View.GONE);
//                orderDeliveryBinding.mapViewLayout.setVisibility(View.GONE);
//                orderDeliveryBinding.orderNotDeliveredMapViewImg.setVisibility(View.GONE);
//                if (orderDetailsResponse.getData().getBranreturnVerCode() == null || orderDetailsResponse.getData().getBranreturnVerCode().isEmpty()) {
//                    orderDeliveryBinding.cancelledVerifiedOtpText.setText("Verification completed successfully");
//                }
//                orderCurrentStatus = 5;
//                orderDeliveryBinding.orderStatusHeader.setText("Cancel Order To Store");
//                ActivityUtils.hideDialog();
//                orderDeliveryBinding.orderNotDeliveredHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//                orderDeliveryBinding.orderNotDeliveredInnerHeadLayout.setBackground(getResources().getDrawable(R.drawable.status_completed_curves_bg));
//
//                orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                orderDeliveryBinding.orderNotDeliveredAddHeadId.setText("Handedover to store");
//                orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setText("Handedover to store");
//                orderDeliveryBinding.orderNotDeliveredHeadCompletedIcon.setVisibility(View.VISIBLE);
//                orderDeliveryBinding.orderNotDeliveredAddInnerHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                orderDeliveryBinding.orderNotDeliveredAddHeadId.setTextColor(getResources().getColor(R.color.order_status_processed_color));
//                orderDeliveryBinding.orderCancelHeadTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                orderDeliveryBinding.orderCancelTxt.setBackground(getResources().getDrawable(R.drawable.delivery_item_bg));
//                orderDeliveryBinding.orderNotDeliveredProcessingLine.setBackgroundColor(getResources().getColor(R.color.order_accepted_color));
//
//                orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.GONE);
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.GONE);
//                orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_bg));
//                orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.VISIBLE);
//                orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//
//                orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.continue_driving_btn_bg));
//                cancelItemBtn.setVisibility(View.GONE);
//                cancelOrderBtn.setVisibility(View.GONE);
//
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                String orderDate = CommonUtils.getCurrentTimeDate();
//                Date orderDates = formatter.parse(orderDate);
//                long orderDateMills = orderDates.getTime();
//                orderDeliveryBinding.cancelledOrderHandedoverToPharmacyTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                orderDeliveryBinding.cancelledOrderHandoverParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
//                orderDeliveryBinding.cancelledOrderHandoverChildOneLayout.setVisibility(View.GONE);
//                orderDeliveryBinding.cancelledOrderHandoverChildTwoLayout.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//            System.out.println("onSuccessOrderSaveUpdateStatusApi:::::::::::::::::::::::::::::::::::::" + e.getMessage());
//        }
//    }
//
//    @Override
//    public void onFialureOrderSaveUpdateStatusApi(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onClickHandoverTheParcelItem(String item) {
//        if (item.equals("Other")) {
//            orderDeliveryBinding.handoverUserName.setVisibility(View.VISIBLE);
//        } else {
//            orderDeliveryBinding.handoverUserName.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onSuccessOrderHandoverSaveUpdateApi(Bitmap bmp) {
//        orderDeliveryBinding.proofofHandoverSkip.setVisibility(View.GONE);
//        hintSignatureText.setVisibility(View.GONE);
//        signatureViewLayout.setVisibility(View.VISIBLE);
//        customerSignatureView.setImageBitmap(bmp);
//        signaturePadParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//        selectionTag = 5;
//        if (!paymentType.equals("COD")) {
//            ActivityUtils.showDialog(this, "Please Wait.");
//            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("DELIVERED", orderUid, "", "", transactionId);
//        }
//    }
//
//    @Override
//    public void onFailureOrderHandoverSaveUpdateApi(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//
//    // cancelled order return to store flow
//    private int orderHandoverToPharmacy = 0;
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    @Override
//    public void onClickCancelledReachedtheStore() {
//        if (orderHandoverToPharmacy == 0) {
//            try {
//                orderDeliveryBinding.cancelledReachedStoreLayout.setBackgroundColor(getResources().getColor(R.color.order_status_processed_color));
//                orderDeliveryBinding.cancelledReachedStoreImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                String orderDate = CommonUtils.getCurrentTimeDate();
//                Date orderDates = formatter.parse(orderDate);
//                long orderDateMills = orderDates.getTime();
//                orderDeliveryBinding.cancelledStoreReachedTime.setText(CommonUtils.getTimeFormatter(orderDateMills));
//                orderDeliveryBinding.cancelledStoreReachedTime.setVisibility(View.VISIBLE);
//                orderHandoverToPharmacy = 1;
////                if (orderCurrentStatus == 4) {
////                    ActivityUtils.showDialog(this, "Please Wait.");
////                    new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("order_handover_to_pharmacy", orderUid, "", "");
////                }
//            } catch (Exception e) {
//                System.out.println("onClickCancelledReachedtheStore :::::::::::::::::::::::::::::::::::::" + e.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public void onClickCancelledOtpVerificationParentLayout() {
//        if (orderHandoverToPharmacy == 1) {
//            orderHandoverToPharmacy = 2;
//            orderDeliveryBinding.cancelledOtpVerificationParentLayout.setBackgroundColor(getResources().getColor(R.color.order_status_pending_color));
//            orderDeliveryBinding.cancelledOtpVerificationImg.setImageDrawable(getDrawable(R.drawable.icon_status_completed));
//            orderDeliveryBinding.cancelledOtpVerificationChildLayout.setVisibility(View.VISIBLE);
//            setCancelledPINListeners();
//        }
//    }
//
//    private boolean isStatusCancelled = false;
//
//    @Override
//    public void onClickCancelledVerifyOtp() {
//
//        if ((isCusDeliveryVerificationCode && this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) || orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase(cusDeliveryVerificationCode) &&
//                this.orderDetailsResponse.getData().getOrderState().getName().equals("RETURN")) {
//            hideKeyboard();
//            ActivityUtils.showDialog(this, "Please Wait.");
//            new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("RETURNORDERRTO", orderUid, "", "", transactionId);
//        } else if (isBranReturnVerificatonCode || orderDeliveryBinding.cancelledPinHiddenEdittext.getText().toString().equalsIgnoreCase(branReturnVerificatonCode)) {
//            hideKeyboard();
//            ActivityUtils.showDialog(this, "Please Wait.");
//            if (this.orderDetailsResponse.getData().getOrderStatus().getUid().equals("CANCELLED")) {
//                this.isStatusCancelled = true;
//                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELRETURNINITIATED", orderUid, "", "", transactionId);
//            } else {
//                new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELORDERRTO", orderUid, "", "", transactionId);
//            }
//        } else {
//            orderDeliveryBinding.cancelledOtpEditTextLayout.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.cancelledVerifyOtpBtn.setVisibility(View.VISIBLE);
//            orderDeliveryBinding.cancelledOtpEditTextLayout.setBackground(getResources().getDrawable(R.drawable.otp_error_bg));
//            orderDeliveryBinding.cancelledVerifiedOtpText.setVisibility(View.GONE);
//            orderDeliveryBinding.cancelledVerifyOtpBtn.setBackground(getResources().getDrawable(R.drawable.verify_otp_error_bg));
//        }
//    }
//
//    @Override
//    public boolean isStatusCancelled() {
//        return this.isStatusCancelled;
//    }
//
//    @Override
//    public void statusCanelled() {
//        this.isStatusCancelled = false;
//        new OrderDeliveryActivityController(this, this).ordersSaveUpdateStatusApiCall("CANCELORDERRTO", orderUid, "", "", transactionId);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!isOrderDelivered) {
//            Dialog alertDialog = new Dialog(this);
//            DialogAlertCustomBinding alertCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_alert_custom, null, false);
//            alertDialog.setContentView(alertCustomBinding.getRoot());
//            alertCustomBinding.title.setText("Alert!");
//            alertCustomBinding.subtitle.setText("Are sure want to leave this page?");
//            alertCustomBinding.dialogButtonNO.setText("No");
//            alertCustomBinding.dialogButtonOK.setText("Yes");
//            alertCustomBinding.dialogButtonOK.setOnClickListener(v -> onBackPress());
//            alertCustomBinding.dialogButtonNO.setOnClickListener(v -> alertDialog.dismiss());
//            alertDialog.show();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    private void onBackPress() {
//        if (isLaunchedByPushNotification) {
//            startActivity(NavigationActivity.getStartIntent(this));
//            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause() {
//        liveDistanceCalculateHandler.removeCallbacks(liveDistanceCalculateRunnable);
//        super.onPause();
//    }
//
//    @Override
//    public void onDirectionApi(int colorFlag, JSONArray jsonArray) {
//
//        String distance, time;
//        float removing = 0;
//        float removingTime = 0;
//        try {
//            if (jsonArray != null) {
//                distance = ((JSONObject) jsonArray.get(0)).getJSONObject("distance").getString("text");
//                time = ((JSONObject) jsonArray.get(0)).getJSONObject("duration").getString("value");
//                removing = Float.parseFloat(distance.replace("\"", "").replace("km", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
////                removingTime = Float.parseFloat(time.replace("\"", "").replace("mins", ""));//Pattern.compile("\"").matcher(distance).replaceAll("");
//                removingTime = Float.parseFloat(time) / 60;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        final boolean[] isStartJourneyUpdate = {false};
//        float finalRemoving = removing;
//        float finalTime = removingTime;
//        if (finalRemoving != 0) {
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    float i;
//                    try {
//                        for (i = 0; i <= 100; i++) {
//                            runOnUiThread(() -> {
//                                if (!isStartJourneyUpdate[0]) {
//                                    isStartJourneyUpdate[0] = true;
//                                    new OrderDeliveryActivityController(OrderDeliveryActivity.this, OrderDeliveryActivity.this).orderStartJourneyUpdateApiCall(OrderDeliveryActivity.this.orderDetailsResponse.getData().getUid(), String.valueOf(finalRemoving));
//                                }
//                            });
//                            sleep(500);
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            thread.start();
//        }
//    }
//
//    @Override
//    public Polyline onTaskDone(boolean flag, Object... values) {
//        return null;
//    }
//
//    @Override
//    public Polyline onSecondTaskDone(boolean flag, Object... values) {
//        return null;
//    }
//
//    @Override
//    public void pointsFirst(List<LatLng> pionts) {
//
//    }
//
//    @Override
//    public void pointsSecond(List<LatLng> pionts) {
//
//    }
//}