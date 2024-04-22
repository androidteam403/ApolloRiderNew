package com.apollo.epos.activity.paytmwirelessdevice;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmCheckStatusResponse;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestRequest;
import com.apollo.epos.activity.paytmwirelessdevice.model.PaytmRequestResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaytmWirelessDeviceActivityController {

    private Context mContext;
    private PaytmWirelessDeviceActivityCallback mCallback;

    public PaytmWirelessDeviceActivityController(Context mContext, PaytmWirelessDeviceActivityCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    public void generatePaytmRequest(PaytmRequestRequest paytmRequestRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<PaytmRequestResponse> call = apiInterface.PAYTM_REQUEST_API_CALL(BuildConfig.PAYTM_WIRELESS_DEVICE_PAYTM_REQUEST_URL, paytmRequestRequest);
            call.enqueue(new Callback<PaytmRequestResponse>() {
                @Override
                public void onResponse(@NotNull Call<PaytmRequestResponse> call, @NotNull Response<PaytmRequestResponse> response) {
                    if (response.body() != null && response.body().getSuccess()) {
                        if (mCallback != null) {
                            ActivityUtils.hideDialog();
                            mCallback.onSuccessPaytmRequestApi(response.body(), paytmRequestRequest);
                        }
                    } else if (response.body() != null && !response.body().getSuccess()) {
                        ActivityUtils.hideDialog();
                        mCallback.onFailureMessage(response.body().getMessage());
                    } else {
                        ActivityUtils.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PaytmRequestResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }

    public void paytmPaymentCheckStatus(PaytmCheckStatusRequest paytmCheckStatusRequest) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please Wait...");

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<PaytmCheckStatusResponse> call = apiInterface.PAYTM_CHECK_STATUS_API_CALL(BuildConfig.PAYTM_WIRELESS_DEVICE_PAYTM_CHECK_STATUS_URL, paytmCheckStatusRequest);
            call.enqueue(new Callback<PaytmCheckStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<PaytmCheckStatusResponse> call, @NotNull Response<PaytmCheckStatusResponse> response) {
                    if (response.body() != null && response.body().getSuccess()) {
                        if (mCallback != null) {
                            ActivityUtils.hideDialog();
                            mCallback.onSuccessPaytmCheckStatus(response.body(), paytmCheckStatusRequest);
                        }
                    } else if (response.body() != null && !response.body().getSuccess()) {
                        ActivityUtils.hideDialog();
                        mCallback.onFailureMessage(response.body().getMessage());
                    } else {
                        ActivityUtils.hideDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PaytmCheckStatusResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mCallback.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mCallback.onFailureMessage("Something went wrong.");
        }
    }
}
