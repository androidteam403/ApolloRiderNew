package com.apollo.epos.activity.orderdelivery;

import android.graphics.Bitmap;

import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;

public interface OrderDeliveryActivityCallback {

    void onClickBackIcon();

    void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse);

    void onFialureOrderDetailsApiCall();

    void onClickPickupVerifyOtp();

    void onFialureMessage(String message);

    void onSuccessOrderSaveUpdateStatusApi(String status);

    void onFialureOrderSaveUpdateStatusApi(String message);

    void onClickHandoverTheParcelItem(String item);

    void onSuccessOrderHandoverSaveUpdateApi(Bitmap bmp);

    void onFailureOrderHandoverSaveUpdateApi(String message);

    void onClickHandoverTheParcelBtn();

    void onClickCollectPayment();

    void onClickCollectPaymentSave();

    void onSuccessOrderPaymentUpdateApiCall();

    void onFailureOrderPaymentApiCall();

    void onClickEyeImage();

    void onSuccessOrderStatusHistoryListApiCall(OrderStatusHitoryListResponse orderStatusHitoryListResponse);

    void onFailureOrderStatusHistoryListApiCall(String message);

    void onClickOrderNotDeliveredCallIcon();
}
