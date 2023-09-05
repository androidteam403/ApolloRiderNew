package com.apollo.epos.activity;

import com.apollo.epos.model.ForceUpdateResponse;

public interface SplashScreenCallback {
    void onSuccessForceUpdateApiCall(ForceUpdateResponse forceUpdateResponse);

    void onFailureForceUpdateApiCall(String message);
}
