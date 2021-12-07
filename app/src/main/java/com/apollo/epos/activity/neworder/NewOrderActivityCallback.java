package com.apollo.epos.activity.neworder;

import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;

public interface NewOrderActivityCallback {
    void onSuccessOrderDetailsApiCall(OrderDetailsResponse orderDetailsResponse);

    void onFialureOrderDetailsApiCall();

    void onFialureMessage(String message);
}
