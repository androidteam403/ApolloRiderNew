package com.apollo.epos.fragment.summary;

import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;

public interface SummaryFragmentCallback {

    void onLogout();

    void onSuccessMyOrdersListApi(MyOrdersListResponse myOrdersListResponse);

    void onFailureMessage(String message);

    void onClickFromDate();

    void onClickToDate();

    void onClickOk();

    String fromDate();

    String toDate();
}
