package com.apollo.epos.fragment.myorders;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.myorders.model.MyOrdersListRequest;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersFragmentController {
    private Context context;
    private MyOrdersFragmentCallback mListener;

    public MyOrdersFragmentController(Context context, MyOrdersFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void myOrdersListApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            MyOrdersListRequest myOrdersListRequest = new MyOrdersListRequest();
            myOrdersListRequest.setPage(1);
            List<MyOrdersListRequest.Row> rowList = new ArrayList<>();
            MyOrdersListRequest.Row row = new MyOrdersListRequest.Row();
            String createdTime = ActivityUtils.getCurrentTimeDate();
            row.setCreatedTime(createdTime.substring(0, createdTime.indexOf(" ")));
            MyOrdersListRequest.DelMedimUsrnam delMedimUsrnam = new MyOrdersListRequest.DelMedimUsrnam();
            delMedimUsrnam.setFirstName(new SessionManager(context).getRiderProfileResponse().getData().getFirstName());
            rowList.add(row);
            myOrdersListRequest.setSize("20");
            myOrdersListRequest.setRows(rowList);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<MyOrdersListResponse> call = apiInterface.GET_MY_ORDERS_LIST_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), myOrdersListRequest);
            call.enqueue(new Callback<MyOrdersListResponse>() {
                @Override
                public void onResponse(@NotNull Call<MyOrdersListResponse> call, @NotNull Response<MyOrdersListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessMyOrdersListApi(response.body());
                    } else {
                        mListener.onFailureMyOrdersListApi();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MyOrdersListResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
