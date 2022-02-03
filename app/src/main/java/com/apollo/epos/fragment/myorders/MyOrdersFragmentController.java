package com.apollo.epos.fragment.myorders;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

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

    public static String url = "api/orders/list/my-order-list?page=" + 1 + "&rows=" + 20 + "&created_date=" + ActivityUtils.getCurrentTimeDate().substring(0, ActivityUtils.getCurrentTimeDate().indexOf(" "));

    public void myOrdersListApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
//            MyOrdersListRequest myOrdersListRequest = new MyOrdersListRequest();
//            myOrdersListRequest.setPage(1);
//            List<MyOrdersListRequest.Row> rowList = new ArrayList<>();
//            MyOrdersListRequest.Row row = new MyOrdersListRequest.Row();
//            String createdTime = ActivityUtils.getCurrentTimeDate();
//            row.setCreatedTime(createdTime.substring(0, createdTime.indexOf(" ")));
//            MyOrdersListRequest.DelMedimUsrnam delMedimUsrnam = new MyOrdersListRequest.DelMedimUsrnam();
//            delMedimUsrnam.setFirstName(new SessionManager(context).getRiderProfileResponse().getData().getFirstName());
//            rowList.add(row);
//            myOrdersListRequest.setSize("20");
//            myOrdersListRequest.setRows(rowList);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<MyOrdersListResponse> call = apiInterface.GET_MY_ORDERS_LIST_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), "1", "10000");
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
