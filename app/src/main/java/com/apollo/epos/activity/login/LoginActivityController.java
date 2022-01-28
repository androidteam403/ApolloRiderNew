package com.apollo.epos.activity.login;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.model.FirebaseTokenRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoRequest;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.databinding.ActivityOrderDeliveryBinding;
import com.apollo.epos.db.SessionManager;
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

public class LoginActivityController {
    private Context context;
    private LoginActivityCallback mListener;

    public LoginActivityController(Context context, LoginActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void loginApiCall(String userName, String password, String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setAppUserName(userName);
            loginRequest.setAppPassword(password);
            Call<LoginResponse> call = apiInterface.DO_LOGIN_API_CALL(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {

                    if (response.body() != null && response.body().getData() != null && response.body().getSuccess()) {
                        mListener.onSuccessLoginApi(response.body());
                        saveUserDeviceInfoApiCall(firebaseToken);
                        firebaseToken(firebaseToken);
                    } else if (response.body() != null && !response.body().getSuccess()) {
                        ActivityUtils.hideDialog();
                        mListener.onFailureLoginApi(response.body().getMessage());

                    }
                }

                @Override
                public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureLoginApi(t.getMessage());
                }
            });
        } else {
            mListener.onFailureLoginApi("Something went wrong.");
        }
    }

    public void getRiderProfileDetailsApi(String token) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<GetRiderProfileResponse> call = apiInterface.GET_RIDER_PROFILE_API_CALL("Bearer " + token);
            call.enqueue(new Callback<GetRiderProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetRiderProfileResponse> call, @NotNull Response<GetRiderProfileResponse> response) {

                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetProfileDetailsApi(response.body());
                    } else {
                        ActivityUtils.hideDialog();
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

    public void saveUserDeviceInfoApiCall(String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            SaveUserDeviceInfoRequest saveUserDeviceInfoRequest = new SaveUserDeviceInfoRequest();
            SaveUserDeviceInfoRequest.DeviceInfo deviceInfo = new SaveUserDeviceInfoRequest.DeviceInfo();
            deviceInfo.setMac_id(CommonUtils.getDeviceId(context));
            deviceInfo.setDevice_name(CommonUtils.getDeviceName());
            deviceInfo.setManufacture(Build.MANUFACTURER);
            deviceInfo.setBrand(Build.BRAND);
            deviceInfo.setUser(Build.USER);
            deviceInfo.setVersion_sdk(Build.VERSION.SDK);
            deviceInfo.setFingerprint(Build.FINGERPRINT);
            deviceInfo.setApp_version_code(String.valueOf(BuildConfig.VERSION_CODE));
            deviceInfo.setApp_version_name(BuildConfig.VERSION_NAME);
            saveUserDeviceInfoRequest.setDeviceInfo(deviceInfo);
            saveUserDeviceInfoRequest.setFirebaseId(firebaseToken);

            Call<SaveUserDeviceInfoResponse> call = apiInterface.SAVE_USER_DEVICE_INFO_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), saveUserDeviceInfoRequest);
            call.enqueue(new Callback<SaveUserDeviceInfoResponse>() {
                @Override
                public void onResponse(@NotNull Call<SaveUserDeviceInfoResponse> call, @NotNull Response<SaveUserDeviceInfoResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {

                    }
                }

                @Override
                public void onFailure(@NotNull Call<SaveUserDeviceInfoResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void firebaseToken(String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            FirebaseTokenRequest o = new FirebaseTokenRequest();
            o.setFirebaseToken(firebaseToken);

            Call<Object> call = apiInterface.UPDATE_FIREBASE_TOKEN_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), o);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
//                    if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
//
//                    }
                }

                @Override
                public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
    public void deliveryFailureReasonApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<DeliveryFailreReasonsResponse> call = apiInterface.DELIVERY_FAILURE_REASONS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), "application/json");
            call.enqueue(new Callback<DeliveryFailreReasonsResponse>() {
                @Override
                public void onResponse(@NotNull Call<DeliveryFailreReasonsResponse> call, @NotNull Response<DeliveryFailreReasonsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().isSuccess()) {
                        mListener.onSuccessDeliveryReasonApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DeliveryFailreReasonsResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
