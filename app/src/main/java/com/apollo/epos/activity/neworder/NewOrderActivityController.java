package com.apollo.epos.activity.neworder;

import android.content.Context;

import com.apollo.epos.activity.neworder.model.OrderDetailsRequest;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderActivityController {
    private Context context;
    private NewOrderActivityCallback mListener;

    public NewOrderActivityController(Context context, NewOrderActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void orderDetailsApiCall(String token, String orderNumber) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");

            OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
            orderDetailsRequest.setOrderNumber(orderNumber);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderDetailsResponse> call = apiInterface.ORDER_DETAILS_API_CALL("Bearer " + token, orderDetailsRequest);
            call.enqueue(new Callback<OrderDetailsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderDetailsResponse> call, @NotNull Response<OrderDetailsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderDetailsApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderDetailsResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
