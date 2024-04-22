package com.apollo.epos.activity.paytmwirelessdevice;

import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusResponse;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestResponse;

public interface PaytmWirelessDeviceActivityCallback {

    void onClickBack();

    void onClickNotificationIcon();

    void onClickGeneratePaytmPayment();

    void onFailureMessage(String message);

    void onSuccessPaytmRequestApi(PaytmRequestResponse paytmRequestResponse, PaytmRequestRequest paytmRequestRequest);

    void onSuccessPaytmCheckStatus(PaytmCheckStatusResponse paytmCheckStatusResponse, PaytmCheckStatusRequest paytmCheckStatusRequest);
}
