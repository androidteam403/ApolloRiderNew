package com.apollo.epos.fragment.profile;

import android.content.Context;

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

public class ProfileFragmentController {
    private Context context;
    private ProfileFragmentCallback mListener;

    public ProfileFragmentController(Context context, ProfileFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void getRiderProfileDetailsApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<GetRiderProfileResponse> call = apiInterface.GET_RIDER_PROFILE_API_CALL("Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MzUzNDk3MjEsImQiOiJDb1NOb0thS2ExUT0ifQ.dcQKi3Jk0fyOUvP0xrRXtmOQfCU-3vtncFqX9b6lM6U");
            call.enqueue(new Callback<GetRiderProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetRiderProfileResponse> call, @NotNull Response<GetRiderProfileResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetProfileDetailsApi(response.body());
                    } else {
                        mListener.onFailureGetProfileDetailsApi("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GetRiderProfileResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureGetProfileDetailsApi(t.getMessage());
                }
            });
        } else {
            mListener.onFailureGetProfileDetailsApi("Something went wrong.");
        }
    }
}
