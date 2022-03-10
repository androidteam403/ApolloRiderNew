package com.apollo.epos.activity.onlinepayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.onlinepayment.model.PhonePeQrCodeResponse;
import com.apollo.epos.databinding.ActivityOnlinePaymentBinding;
import com.apollo.epos.databinding.DialogGeneratePhonepeQrcodeBinding;
import com.apollo.epos.databinding.DialogPhonepeLinkBinding;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;
import com.google.zxing.WriterException;
import com.novoda.merlin.Merlin;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class OnlinePaymentActivity extends BaseActivity implements OnlinePaymentCallback {
    private ActivityOnlinePaymentBinding onlinePaymentBinding;
    private OrderDetailsResponse orderDetailsResponse;
    private String amount;

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this);
    }

    public static Intent getStartIntent(Context context, OrderDetailsResponse orderDetailsResponse) {
        Intent intent = new Intent(context, OnlinePaymentActivity.class);
        intent.putExtra(CommonUtils.ORDER_DETAILS_RESPONSE, orderDetailsResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    private OnlinePaymentController getController() {
        return new OnlinePaymentController(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onlinePaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_online_payment);
        setUp();
    }

    @SuppressLint("SetTextI18n")
    private void setUp() {
        onlinePaymentBinding.setCallback(this);
        if (getIntent() != null) {
            this.orderDetailsResponse = (OrderDetailsResponse) getIntent().getSerializableExtra(CommonUtils.ORDER_DETAILS_RESPONSE);
            if (this.orderDetailsResponse != null) {
                try {
                    amount = String.valueOf(this.orderDetailsResponse.getData().getPakgValue() * 100).substring(0, String.valueOf(this.orderDetailsResponse.getData().getPakgValue() * 100).indexOf("."));
                    onlinePaymentBinding.fulfilmentId.setText("#" + this.orderDetailsResponse.getData().getOrderNumber());
                    onlinePaymentBinding.amount.setText(getResources().getString(R.string.label_rupee_symbol) + " " + this.orderDetailsResponse.getData().getPakgValue());
                    onlinePaymentBinding.phoneNumber.setText(this.orderDetailsResponse.getData().getDelPn());
                    onlinePaymentBinding.phoneNumber.setSelection(this.orderDetailsResponse.getData().getDelPn().length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickNotificationIcon() {
        Toast.makeText(this, "You clicked notification icon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickPhonePeQRCode() {
        getController().phonePeQRCodeApiCall(orderDetailsResponse);
    }


    @Override
    public void onClickPhonePeLink() {
        if (phoneNumberValidate())
            getController().generatePhonePePaymentLinkApiCall(amount, onlinePaymentBinding.phoneNumber.getText().toString().trim(), orderDetailsResponse.getData().getOrderNumber(), orderDetailsResponse.getData().getPickupBranch());
    }

    private boolean phoneNumberValidate() {
        if (onlinePaymentBinding.phoneNumber.getText().toString().isEmpty()) {
            onlinePaymentBinding.phoneNumber.setError("Phone number should not be empty");
            onlinePaymentBinding.phoneNumber.requestFocus();
            return false;
        } else if (onlinePaymentBinding.phoneNumber.getText().toString().length() < 10) {
            onlinePaymentBinding.phoneNumber.setError("Phone number must have 10 digits");
            onlinePaymentBinding.phoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClickHdfcLink() {

    }

    private String phonePeQrCodetransactionId;
    Dialog generatePhonePeQrCodeDialog;

    @Override
    public void onSuccessPhonepeQrCode(PhonePeQrCodeResponse phonePeQrCodeResponse, String phonePeQrCodetransactionId) {
        generatePhonePeQrCodeDialog = new Dialog(this, R.style.fadeinandoutcustomDialog);
        DialogGeneratePhonepeQrcodeBinding generatePhonepeQrcodeBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_generate_phonepe_qrcode, null, false);
        generatePhonePeQrCodeDialog.setContentView(generatePhonepeQrcodeBinding.getRoot());
        generatePhonePeQrCodeDialog.setCancelable(false);
        generatePhonepeQrcodeBinding.setCallback(OnlinePaymentActivity.this);
        generatePhonePeQrCodeDialog.show();
        QrCodeGeneration(phonePeQrCodeResponse.getQrCode(), generatePhonepeQrcodeBinding, this);
        ActivityUtils.hideDialog();
        this.phonePeQrCodetransactionId = phonePeQrCodetransactionId;
    }

    @Override
    public void onSuccessPhonepeCheckStatus(PhonePeQrCodeResponse phonePeQrCodeResponse) {
//        Toast.makeText(this, phonePeQrCodeResponse.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("PAYMENT_SUCCESSFULL", true);
        intent.putExtra("TRANSACTION_ID", phonePeQrCodetransactionId);
        setResult(Activity.RESULT_OK, intent);
        finish();
        ActivityUtils.hideDialog();
//        if (generatePhonePeQrCodeDialog != null)
//            generatePhonePeQrCodeDialog.dismiss();
    }

    @Override
    public void onSuccessPhonepePaymentCancelled(PhonePeQrCodeResponse phonePeQrCodeResponse) {
        ActivityUtils.hideDialog();
        finish();
    }

    @Override
    public void onClickPhonePeCheckPaymentStatus() {
        getController().phoneCheckPaymentStatusApiCall(phonePeQrCodetransactionId);
    }

    @Override
    public void onClickPhonePePaymentCancel() {
        getController().phonePePaymentCancelledApiCall(phonePeQrCodetransactionId);
    }

    private String phonePeLinktransactionId;
    private String phonePeLinkRefferenceId;

    @Override
    public void onSuccessGeneratePhonePeLink(String response) {
        if (response != null) {
            String[] data = response.substring(0, response.indexOf("<")).split("\\|");
            if (data[0].equals("success")) {
                this.phonePeLinktransactionId = data[2];
                this.phonePeLinkRefferenceId = data[3];
                phonePePaymentLinkDialogShow();
                Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
            } else if (data[0].equals("failure")) {
                Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSuccessPhonePeLinkPaymentCheckStatus(String response) {
        if (response != null) {
            String[] data = response.substring(0, response.indexOf("<")).split("\\|");
            switch (data[0]) {
                case "payment_pending":
                    Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
                    break;
                case "success":
                    Intent intent = new Intent();
                    intent.putExtra("PAYMENT_SUCCESSFULL", true);
                    intent.putExtra("TRANSACTION_ID", phonePeLinktransactionId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    ActivityUtils.hideDialog();
//                    if (phonepeLinkPaymentDialog != null)
//                        phonepeLinkPaymentDialog.dismiss();
                    Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
                    break;
                case "False":
                    Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onSuccessPhonePeLinkPaymentCancelled(String response) {
        if (response != null) {
            String[] data = response.substring(0, response.indexOf("<")).split("\\|");
            if (data[0].equals("success")) {
                ActivityUtils.hideDialog();
                finish();
                Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
            } else if (data[0].equals("failure")) {
                Toast.makeText(this, data[1], Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void QrCodeGeneration(Object qrCodeData, DialogGeneratePhonepeQrcodeBinding generatePhonepeQrcodeBinding, Context context) {
        generatePhonepeQrcodeBinding.qrCodeDisplay.setVisibility(View.VISIBLE);

// below line is for getting
        // the windowmanager service.
        WindowManager manager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        QRGEncoder qrgEncoder = new QRGEncoder((String) qrCodeData, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            if (qrCodeData != null) {
//                generatePhonepeQrcodeBinding.qrlogo.setVisibility(View.VISIBLE);
                generatePhonepeQrcodeBinding.qrCodeDisplay.setImageBitmap(bitmap);
//                generatePhonepeQrcodeBinding.loadingPanel.setVisibility(View.GONE);
//                ActivityUtils.hideDialog();
            }
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    Dialog phonepeLinkPaymentDialog;

    private void phonePePaymentLinkDialogShow() {
        phonepeLinkPaymentDialog = new Dialog(this);
        DialogPhonepeLinkBinding phonepeLinkBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_phonepe_link, null, false);
        phonepeLinkPaymentDialog.setContentView(phonepeLinkBinding.getRoot());
        phonepeLinkPaymentDialog.setCancelable(false);
        phonepeLinkBinding.phonepeLinkPaymentCheckStatus.setOnClickListener(v -> getController().phonePeLinkCheckStatusApiCall(phonePeLinktransactionId));
        phonepeLinkBinding.phonepeLinkPaymentCancelledRequest.setOnClickListener(v -> getController().phonePeLinkCancelledRequestApiCall(phonePeLinkRefferenceId, phonePeLinktransactionId, onlinePaymentBinding.phoneNumber.getText().toString().trim()));
        phonepeLinkPaymentDialog.show();
    }
}
