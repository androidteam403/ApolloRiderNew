package com.apollo.epos.activity.navigation;

public interface NavigationActivityCallback {
    void onFialureMessage(String message);

    void onSuccessRiderUpdateStatusApiCall();

    void onFailureRiderUpdateStatusApiCall(String message);
}
