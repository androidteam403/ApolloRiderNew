package com.apollo.epos.fragment.reports;

import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;

public interface ReportsFragmentCallback {
    void onSuccessOrdersCodStatusApiCall(OrdersCodStatusResponse ordersCodStatusResponse);

    void onFailureOrdersCodStatusApiCall(String message);

}
