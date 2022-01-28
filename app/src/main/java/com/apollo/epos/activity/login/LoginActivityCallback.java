package com.apollo.epos.activity.login;

import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.model.GetRiderProfileResponse;

public interface LoginActivityCallback {

    void onFialureMessage(String message);

    void onSuccessLoginApi(LoginResponse loginResponse);

    void onFailureLoginApi(String message);

    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onSuccessDeliveryReasonApiCall(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse);

    void onFailureGetProfileDetailsApi(String message);

    void onClickSignin();

    void onClickForgotPasswordText();
}
