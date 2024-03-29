package com.apollo.epos.activity.reports;

import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;

public interface ReportsActivityCallback {
    void onFailureMessage(String message);

    void onClickNotificationIcon();

    void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse);

    void onFailureOrdersCodStatusApiCall(String message);

    void onClickBack();

    void onLogout();
}
