package com.apollo.epos.activity;

import android.content.Context;

import com.apollo.epos.model.ForceUpdateResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenController {
    private Context context;
    private SplashScreenCallback callback;

    public SplashScreenController(Context context, SplashScreenCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void forceUpdateApiCall() {
        ApiInterface apiInterface = ApiClient.getApiService();
        Call<ForceUpdateResponse> call = apiInterface.FORCE_UPDATE_RESPONSE_API_CALL();
        call.enqueue(new Callback<ForceUpdateResponse>() {
            @Override
            public void onResponse(Call<ForceUpdateResponse> call, Response<ForceUpdateResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    callback.onSuccessForceUpdateApiCall(response.body());
                } else {
                    callback.onFailureForceUpdateApiCall("No data found.");
                }
            }

            @Override
            public void onFailure(Call<ForceUpdateResponse> call, Throwable t) {
                callback.onFailureForceUpdateApiCall(t.getMessage());
            }
        });
    }
}
