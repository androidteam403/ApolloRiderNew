package com.apollo.epos.fragment.dashboard;

import com.apollo.epos.fragment.dashboard.model.RiderDashboardCountResponse;
import com.apollo.epos.model.GetRiderProfileResponse;

public interface DashboardFragmentCallback {
    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFialureMessage(String message);

    void onSuccessGetRiderDashboardCountApiCall(RiderDashboardCountResponse riderDashboardCountResponse);

    void onClickCodReciebedorPendingDeposits();

    void onClickTotalDeliveredCancelledOrders();
}
