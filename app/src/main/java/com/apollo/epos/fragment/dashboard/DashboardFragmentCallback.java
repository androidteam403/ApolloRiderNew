package com.apollo.epos.fragment.dashboard;

import com.apollo.epos.model.GetRiderProfileResponse;

public interface DashboardFragmentCallback {
    void onSuccessGetProfileDetailsApi(GetRiderProfileResponse getRiderProfileResponse);

    void onFialureMessage(String message);
}
