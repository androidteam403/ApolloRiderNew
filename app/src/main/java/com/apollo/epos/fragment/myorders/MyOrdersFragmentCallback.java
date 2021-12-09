package com.apollo.epos.fragment.myorders;

import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;

public interface MyOrdersFragmentCallback {

    void onFialureMessage(String message);

    void onSuccessMyOrdersListApi(MyOrdersListResponse myOrdersListResponse);

    void onFailureMyOrdersListApi();

    void onStatusClick(MyOrdersListResponse.Row item);


}
