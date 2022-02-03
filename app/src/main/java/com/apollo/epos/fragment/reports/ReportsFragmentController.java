package com.apollo.epos.fragment.reports;

import android.content.Context;

import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragmentController {
    private Context context;
    private ReportsFragmentCallback mListener;

    public ReportsFragmentController(Context context, ReportsFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void getOrdersCodStatusApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrdersCodStatusResponse> call = apiInterface.GET_ORDERS_COD_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken());
            call.enqueue(new Callback<OrdersCodStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrdersCodStatusResponse> call, @NotNull Response<OrdersCodStatusResponse> response) {

                    if (response.body() != null && response.body().getData() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrdersCodStatusApiCall(response.body());
                    } else if (response.body() != null && !response.body().getSuccess()) {
                        ActivityUtils.hideDialog();
                        mListener.onFailureOrdersCodStatusApiCall("Something went wrong.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrdersCodStatusResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureOrdersCodStatusApiCall(t.getMessage());
                }
            });
        } else {
            mListener.onFailureOrdersCodStatusApiCall("Something went wrong.");
        }
    }
}
