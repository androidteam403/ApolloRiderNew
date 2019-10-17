package com.apollo.epos.fragment.deliveryorder;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.apollo.epos.R;
import com.apollo.epos.activity.CancelOrderActivity;
import com.apollo.epos.activity.CaptureSignatureActivity;
import com.apollo.epos.activity.NavigationActivity;
import com.apollo.epos.activity.ScannerActivity;
import com.apollo.epos.adapter.CustomReasonAdapter;
import com.apollo.epos.fragment.dashboard.DashboardFragment;
import com.apollo.epos.utils.ActivityUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.apollo.epos.utils.ActivityUtils.getCurrentTime;
import static com.apollo.epos.utils.ActivityUtils.showLayoutDownAnimation;
import static com.apollo.epos.utils.ActivityUtils.showTextDownAnimation;

public class DeliveryOrderFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        View.OnFocusChangeListener, View.OnKeyListener {
    private Activity mActivity;
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
    @BindView(R.id.otp_verification_img)
    protected ImageView otpVerificationImg;
    @BindView(R.id.otp_verification_child_layout)
    protected LinearLayout otpVerificationChildLayout;
    @BindView(R.id.otp_editText_layout)
    protected LinearLayout otpEditTextLayout;
    @BindView(R.id.pin_hidden_edittext)
    protected EditText pinHiddenEditText;
    @BindView(R.id.opt_num1)
    protected EditText optNum1;
    @BindView(R.id.opt_num2)
    protected EditText optNum2;
    @BindView(R.id.opt_num3)
    protected EditText optNum3;
    @BindView(R.id.opt_num4)
    protected EditText optNum4;
    @BindView(R.id.opt_num5)
    protected EditText optNum5;
    @BindView(R.id.opt_num6)
    protected EditText optNum6;
    @BindView(R.id.verify_otp_btn)
    protected TextView verifyOtpBtn;
    @BindView(R.id.verified_otp_text)
    protected TextView verifiedOtpText;
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
    @BindView(R.id.parentScrollView)
    protected ScrollView parentScrollView;
    @BindView(R.id.user_contact_number)
    protected ImageView userContactNumber;

    String[] cancelReasons = {"Select from predefined statements", "Taken leave", "Not feeling well", "Soo far from my current location"};
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

    public static DeliveryOrderFragment newInstance() {
        return new DeliveryOrderFragment();
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
        return inflater.inflate(R.layout.fragment_delivery_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        CustomReasonAdapter customUserListAdapter = new CustomReasonAdapter(mActivity, customerTypesList);
        customerTypeSpinner.setAdapter(customUserListAdapter);
        customerTypeSpinner.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.reached_store_layout)
    void onReachedStoreLayoutClick() {
        if (selectionTag == 0) {
            selectionTag = 1;
            reachedStoreLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            reachedStoreImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));

            showTextDownAnimation(R.id.anim_reach_store_layout, animReachStoreLayout, storeReachedTime);
//            ActivityUtils.footerAnimation(mActivity, animReachStoreLayout, storeReachedTime);
            storeReachedTime.setText(getCurrentTime());
        }
    }

    @OnClick(R.id.scan_barcode_layout)
    void scanBarCodeClick() {
        if (selectionTag == 1) {
            new IntentIntegrator(mActivity).setCaptureActivity(ScannerActivity.class).initiateScan();
            mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }

    @OnClick(R.id.taken_parcel_layout)
    void onTakenParcelLayoutClick() {
        if (selectionTag == 2) {
            selectionTag = 3;
            takenParcelLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            takenParcelImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));

            showTextDownAnimation(R.id.anim_taken_parcel_layout, animTakenParcelLayout, parcelTakenTime);
            parcelTakenTime.setText(getCurrentTime());
            continueProcessLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.continue_driving_btn)
    void onContinueDrivingClick() {
        if (selectionTag == 3) {
            selectionTag = 4;
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
            userContactNumber.setImageDrawable(mActivity.getDrawable(R.drawable.icon_phone_select));
            deliveryItemsView.setVisibility(View.VISIBLE);
            userMobileNumberHeader.setVisibility(View.VISIBLE);
            userMobileNumber.setVisibility(View.VISIBLE);
            deliveryItemsListLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.reached_address_layout)
    void onReachedAddressLayoutClick() {
        if (selectionTag == 4) {
            selectionTag = 5;
            reachedDeliveryAddressLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            reachedAddressImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
            addressReachedTime.setText(getCurrentTime());
            addressReachedTime.setVisibility(View.VISIBLE);
            cancelItemBtn.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.collected_amount_parent_layout)
    void onCollectedAmtLayoutClick() {
        if (selectionTag == 5) {
            selectionTag = 6;
            collectedAmountParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            collectedAmountImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
            collectedAmountChildLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.handover_parcel_parent_layout)
    void onHandOverParcelParentLayoutClick() {
        if (selectionTag == 6) {
            selectionTag = 7;
            handoverParcelParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            handoverParcelImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
            handoverParcelChildLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.signature_pad_parent_layout)
    void onSignaturePadParentLayoutClick() {
        if (selectionTag == 7) {
            selectionTag = 8;
            signaturePadParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_pending_color));
            signaturePadImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
            signaturePadChildLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.signature_layout)
    void onSignatureLayoutClick() {
        Intent intent = new Intent(mActivity, CaptureSignatureActivity.class);
        startActivityForResult(intent, SIGNATURE_REQUEST_CODE);
        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
        if (selectionTag == 8) {
            selectionTag = 9;
            otpVerificationParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_pending_color));
            otpVerificationImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
            otpVerificationChildLayout.setVisibility(View.VISIBLE);
            setPINListeners();
        }
    }

    @OnClick(R.id.verify_otp_btn)
    void onVerifyOtpBtnClick() {
        if (pinHiddenEditText.getText().toString().equalsIgnoreCase("000000")) {
            otpEditTextLayout.setVisibility(View.GONE);
            verifyOtpBtn.setVisibility(View.GONE);
            otpEditTextLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.otp_bg));
            verifiedOtpText.setVisibility(View.VISIBLE);
            otpVerificationParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
            orderDeliveredParentLayout.setBackground(getResources().getDrawable(R.drawable.order_delivered_layout_bg));
            orderDeliveredChildOneLayout.setVisibility(View.GONE);
            orderDeliveredChildTwoLayout.setVisibility(View.VISIBLE);

            verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.continue_driving_btn_bg));
            cancelItemBtn.setVisibility(View.GONE);
            cancelOrderBtn.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            int rightDp = (int) getResources().getDimension(R.dimen.five_dp);
            int bottomDp = (int) getResources().getDimension(R.dimen.twenty_five_dp);
            int marginEnd = (int) ActivityUtils.convertDpToPixel(rightDp, mActivity);
            int marginBottom = (int) ActivityUtils.convertDpToPixel(bottomDp, mActivity);
            params.setMargins(0, 0, marginEnd, marginBottom);
            deliveryItemsView.setLayoutParams(params);

            new Handler().postDelayed(() -> {
                ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new DashboardFragment(), R.string.menu_dashboard);
                ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(1);
            }, 1000);
        } else {
            otpEditTextLayout.setVisibility(View.VISIBLE);
            verifyOtpBtn.setVisibility(View.VISIBLE);
            otpEditTextLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.otp_error_bg));
            verifiedOtpText.setVisibility(View.GONE);
            verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
        }
    }

    private void cameraPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//        builder.setItems(new CharSequence[]
//                        {"Camera", "Select from Gallery"},
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // The 'which' argument contains the index position
//                        // of the selected item
//                        switch (which) {
//                            case 0:
//                                camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
//                                //  uri = Uri.fromFile(file);
//                                //camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
//                                //camera_intent.putExtra("return-data",true);
//                                startActivityForResult(camera_intent, CAM_REQUEST);
//                                break;
//                            case 1:
//                                gal_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(Intent.createChooser(gal_intent, "Select Image from Gallery"), GAL_REQUEST);
//                        }
//                    }
//                });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //  user_image.setImageDrawable(null);
//                imageStatus = false;
//                dialogInterface.dismiss();
//            }
//        });
//        builder.create().show();
    }

    @OnClick(R.id.cancel_order_btn)
    public void toggleBottomSheet() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.show();

        TextView headerText = dialogView.findViewById(R.id.sheet_header);
        headerText.setText(getResources().getString(R.string.label_order_cancel));
        TextView cancelHeaderText = dialogView.findViewById(R.id.cancel_order_header);
        cancelHeaderText.setText(getResources().getString(R.string.label_cancel_reason_header));
        ImageView closeDialog = dialogView.findViewById(R.id.close_icon);
        closeDialog.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Spinner reasonSpinner = dialogView.findViewById(R.id.rejectReasonSpinner);
        CustomReasonAdapter customAdapter = new CustomReasonAdapter(mActivity, cancelReasons);
        reasonSpinner.setAdapter(customAdapter);
        reasonSpinner.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.cancel_item_btn)
    void onCancelItemClick() {
        Intent i = new Intent(mActivity, CancelOrderActivity.class);
        startActivity(i);
        mActivity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                hintSignatureText.setVisibility(View.GONE);
                signatureViewLayout.setVisibility(View.VISIBLE);
                byte[] byteArray = data.getByteArrayExtra("capturedSignature");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                customerSignatureView.setImageBitmap(bmp);
                signaturePadParentLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
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
                    scanBarcodeLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.order_status_processed_color));
                    scanBarcodeImg.setImageDrawable(mActivity.getDrawable(R.drawable.icon_status_completed));
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
            BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(selectedImage), null, o);
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
            return BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setPINListeners() {
        pinHiddenEditText.addTextChangedListener(vendorWatcher);

        optNum1.setOnFocusChangeListener(this);
        optNum2.setOnFocusChangeListener(this);
        optNum3.setOnFocusChangeListener(this);
        optNum4.setOnFocusChangeListener(this);
        optNum5.setOnFocusChangeListener(this);
        optNum6.setOnFocusChangeListener(this);

        optNum1.setOnKeyListener(this);
        optNum2.setOnKeyListener(this);
        optNum3.setOnKeyListener(this);
        optNum4.setOnKeyListener(this);
        optNum5.setOnKeyListener(this);
        optNum6.setOnKeyListener(this);
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
            setDefaultPinBackground(optNum5);
            setDefaultPinBackground(optNum6);
            if (s.length() == 0) {
                optNum1.setText("");
                optNum2.setText("");
                optNum3.setText("");
                optNum4.setText("");
                optNum5.setText("");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 1) {
                optNum1.setText(s.charAt(0) + "");
                optNum2.setText("");
                optNum3.setText("");
                optNum4.setText("");
                optNum5.setText("");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 2) {
                optNum2.setText(s.charAt(1) + "");
                optNum3.setText("");
                optNum4.setText("");
                optNum5.setText("");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 3) {
                optNum3.setText(s.charAt(2) + "");
                optNum4.setText("");
                optNum5.setText("");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 4) {
                optNum4.setText(s.charAt(3) + "");
                optNum5.setText("");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 5) {
                optNum5.setText(s.charAt(4) + "");
                optNum6.setText("");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.verify_otp_error_bg));
            } else if (s.length() == 6) {
                optNum6.setText(s.charAt(5) + "");
                verifyOtpBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.continue_driving_btn_bg));
            }
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
                        if (pinHiddenEditText.getText().length() == 6)
                            optNum6.setText("");
                        else if (pinHiddenEditText.getText().length() == 5)
                            optNum5.setText("");
                        else if (pinHiddenEditText.getText().length() == 4)
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
                case R.id.opt_num5:
                case R.id.opt_num6:
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
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Service.INPUT_METHOD_SERVICE);
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
        checkCallPermissionSetting();
    }

    @OnClick(R.id.user_contact_number)
    void onUserContactClick() {
        if (userPhoneClick)
            checkCallPermissionSetting();
    }

    private void checkCallPermissionSetting() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions_Status", "Permissions not granted");
               /* if (ContextCompat.checkSelfPermission(CameraFunctionality.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)*/
                // ask for permission
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Log.d("Permissions_Status", "We have a permission");
                // we have a permission
                requestACall();
            }
        } else {
            requestACall();
        }
    }

    private void requestACall() {
        String phonenumber = "+919440012212";
        Intent intentcall = new Intent();
        intentcall.setAction(Intent.ACTION_CALL);
        intentcall.setData(Uri.parse("tel:" + phonenumber)); // set the Uri
        mActivity.startActivity(intentcall);
    }
}