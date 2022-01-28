package com.apollo.epos.fragment.changepassword;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordRequest;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragmentController {

    private Context context;
    private ChangePasswordFragmentCallback mListener;

    public ChangePasswordFragmentController(Context context, ChangePasswordFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void changePasswordApiCall(String oldPassword, String newPassword, String newConfirmPassword) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
            changePasswordRequest.setOldPassword(oldPassword);
            changePasswordRequest.setPassword(newPassword);
            changePasswordRequest.setConfirmPassword(newConfirmPassword);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ChangePasswordResponse> call = apiInterface.CHANGE_PASSWORD_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), changePasswordRequest);
            call.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(@NotNull Call<ChangePasswordResponse> call, @NotNull Response<ChangePasswordResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessChangePasswordApiCall(response.body());
                    } else if (response.body() != null && !response.body().getSuccess()) {
                        mListener.onFailureChangePasswordApiCall(response.body().getData().getErrors().get(0).getMsg());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ChangePasswordResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

    public void riderUpdateStauts(String token, String activieStatus) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid(activieStatus);
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);

            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + token, riderActiveStatusRequest);
            call.enqueue(new Callback<RiderActiveStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        new SessionManager(context).setRiderActiveStatus(activieStatus);
                        if (mListener != null)
                            mListener.onSuccessRiderUpdateStatusApiCall();
                    } else {
                        mListener.onFailureRiderUpdateStatusApiCall("Please try again later");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureRiderUpdateStatusApiCall("Please try again later");
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

}
