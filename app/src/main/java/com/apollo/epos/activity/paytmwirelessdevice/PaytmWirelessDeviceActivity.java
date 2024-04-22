package com.apollo.epos.activity.paytmwirelessdevice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.apollo.epos.R;
import com.apollo.epos.activity.BaseActivity;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusResponse;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestResponse;
import com.apollo.epos.databinding.ActivityPaytmWirelessDeviceBinding;
import com.apollo.epos.databinding.DialogPaytmCheckstatusBinding;
import com.apollo.epos.utils.CommonUtils;
import com.novoda.merlin.Merlin;

import java.util.Objects;

public class PaytmWirelessDeviceActivity extends BaseActivity implements PaytmWirelessDeviceActivityCallback {

    private ActivityPaytmWirelessDeviceBinding activityPaytmWirelessDeviceBinding;
    private OrderDetailsResponse orderDetailsResponse;
    private String amount;
    private Dialog paytmPaymentDialog;
    private String responseTime = "";
    private String requestTime = "";

    @Override
    protected Merlin createMerlin() {
        return new Merlin.Builder().withConnectableCallbacks().withDisconnectableCallbacks().withBindableCallbacks().build(this);
    }

    public static Intent getStartIntent(Context context, OrderDetailsResponse orderDetailsResponse) {
        Intent intent = new Intent(context, PaytmWirelessDeviceActivity.class);
        intent.putExtra(CommonUtils.ORDER_DETAILS_RESPONSE, orderDetailsResponse);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPaytmWirelessDeviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_paytm_wireless_device);
        setUp();
    }

    private void setUp() {
        activityPaytmWirelessDeviceBinding.setCallback(this);
        if (getIntent() != null) {
            this.orderDetailsResponse = (OrderDetailsResponse) getIntent().getSerializableExtra(CommonUtils.ORDER_DETAILS_RESPONSE);
            if (this.orderDetailsResponse != null) {
                try {
                    amount = String.valueOf(this.orderDetailsResponse.getData().getPakgValue());
                    activityPaytmWirelessDeviceBinding.fulfilmentId.setText("#" + this.orderDetailsResponse.getData().getOrderNumber());
                    activityPaytmWirelessDeviceBinding.amount.setText(getResources().getString(R.string.label_rupee_symbol) + " " + this.orderDetailsResponse.getData().getPakgValue());
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
    public void onClickGeneratePaytmPayment() {
        String terminalId = activityPaytmWirelessDeviceBinding.terminalId.getText().toString();
        if (!terminalId.isEmpty()) {
            PaytmRequestRequest paytmRequestRequest = new PaytmRequestRequest();
            if (orderDetailsResponse != null &&
                    orderDetailsResponse.getData() != null &&
                    orderDetailsResponse.getData().getDeliverBranch() != null &&
                    !orderDetailsResponse.getData().getDeliverBranch().isEmpty()) {
                paytmRequestRequest.setStoreId("16001");//orderDetailsResponse.getData().getDeliverBranch()
                paytmRequestRequest.setTransactionId(orderDetailsResponse.getData().getDeliverBranch() + CommonUtils.getCurrentTimeStamp());
            }
            paytmRequestRequest.setAmount("1");//amount
            paytmRequestRequest.setTransactionDate(CommonUtils.getCurrentTimeDate());
            paytmRequestRequest.setStoreTerminalId(terminalId);
            paytmRequestRequest.setRequestType("VIVEKAGAM");
            hideKeyboard();
            getController().generatePaytmRequest(paytmRequestRequest);
        } else {
            Toast.makeText(this, "Terminal ID should not be empty.", Toast.LENGTH_SHORT).show();
            activityPaytmWirelessDeviceBinding.terminalId.requestFocus();
        }
    }

    @Override
    public void onFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if (message.equalsIgnoreCase("Merchant transaction id does not exist..!")) {
            if (paytmPaymentDialog != null && paytmPaymentDialog.isShowing()) {
                paytmPaymentDialog.dismiss();
            }
        }
    }

    @Override
    public void onSuccessPaytmRequestApi(PaytmRequestResponse paytmRequestResponse, PaytmRequestRequest paytmRequestRequest) {
        requestTime = CommonUtils.getCurrentTimeDate();
        paytmPaymentDialog = new Dialog(this, R.style.fadeinandoutcustomDialog);
        DialogPaytmCheckstatusBinding paytmCheckstatusBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_paytm_checkstatus, null, false);
        Objects.requireNonNull(paytmPaymentDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        paytmPaymentDialog.setContentView(paytmCheckstatusBinding.getRoot());
        paytmPaymentDialog.setCancelable(false);
        paytmCheckstatusBinding.paytmPaymentCheckStatus.setOnClickListener(v -> {
            PaytmCheckStatusRequest paytmCheckStatusRequest = new PaytmCheckStatusRequest();
            paytmCheckStatusRequest.setStoreId(paytmRequestRequest.getStoreId());
            paytmCheckStatusRequest.setTransactionId(paytmRequestRequest.getTransactionId());
            paytmCheckStatusRequest.setTransactionDate(paytmRequestRequest.getTransactionDate());
            paytmCheckStatusRequest.setStoreTerminalId(paytmRequestRequest.getStoreTerminalId());
            paytmCheckStatusRequest.setRequestType(paytmRequestRequest.getRequestType());
            getController().paytmPaymentCheckStatus(paytmCheckStatusRequest);
        });
        paytmPaymentDialog.show();
    }

    @Override
    public void onSuccessPaytmCheckStatus(PaytmCheckStatusResponse paytmCheckStatusResponse, PaytmCheckStatusRequest paytmCheckStatusRequest) {
        Toast.makeText(this, paytmCheckStatusResponse.getMessage(), Toast.LENGTH_SHORT).show();
        if (paytmPaymentDialog != null && paytmPaymentDialog.isShowing()) {
            paytmPaymentDialog.dismiss();
        }

        responseTime = CommonUtils.getCurrentTimeDate();
        Intent intent = new Intent();
        intent.putExtra("PAYMENT_SUCCESSFULL", true);
        intent.putExtra("TRANSACTION_ID", paytmCheckStatusResponse.getPaytmTransactionId());
        intent.putExtra("PAYMENTMODE", "PAYTM WIRELESS DEVICE");
        intent.putExtra("request_time", requestTime);
        intent.putExtra("response_time", responseTime);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private PaytmWirelessDeviceActivityController getController() {
        return new PaytmWirelessDeviceActivityController(this, this);
    }
}
