package com.apollo.epos.fragment.reports;

import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderDashboardCountResponse;

public interface ReportsFragmentCallback {

    void onFialureMessage(String message);

    void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse);

    void onFailureOrdersCodStatusApiCall(String message);

    void onLogout();

    void onSuccessGetRiderDashboardCountApiCall(RiderDashboardCountResponse riderDashboardCountResponse);

    void onClickFromDate();

    void onClickToDate();

    void onClickOk();

    String fromDate();

    String toDate();
}
