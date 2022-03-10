package com.apollo.epos.fragment.myorders;

import android.content.Context;

import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.myorders.model.GlobalSettingSelectResponse;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<MyOrdersListResponse> call = apiInterface.GET_MY_ORDERS_LIST_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), "1", "10000",  CommonUtils.getBeforeSevenDaysDate(), CommonUtils.getCurrentDate());
            call.enqueue(new Callback<MyOrdersListResponse>() {
                @Override
                public void onResponse(@NotNull Call<MyOrdersListResponse> call, @NotNull Response<MyOrdersListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessMyOrdersListApi(response.body());
                    } else if (response.code() == 401) {
                        ActivityUtils.showDialog(context, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                    myOrdersListApiCall();
                                } else if (response.code() == 401) {
                                    logout();
                                } else {
                                    mListener.onFialureMessage("Please try again");
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
                                ActivityUtils.hideDialog();
                                mListener.onFialureMessage("Please try again");
                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                            }
                        });
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


    public void globalSettingSelectApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<GlobalSettingSelectResponse> call = apiInterface.GLOBAL_SETTING_SELECT_API_CALL("Bearer " + new SessionManager(context).getLoginToken());
            call.enqueue(new Callback<GlobalSettingSelectResponse>() {
                @Override
                public void onResponse(@NotNull Call<GlobalSettingSelectResponse> call, @NotNull Response<GlobalSettingSelectResponse> response) {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        new SessionManager(context).setGlobalSettingSelectResponse(response.body());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GlobalSettingSelectResponse> call, @NotNull Throwable t) {
                    System.out.println("globalSettingSelectApi:::::::::::::::::::::::::::::::" + t.getMessage());
                }
            });
        } else {
            System.out.println("globalSettingSelectApi::::::::::::::::::::::::::::::: Something went wrong.");
        }
    }


    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid("Offline");
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);

            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), riderActiveStatusRequest);
            call.enqueue(new Callback<RiderActiveStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        new SessionManager(context).setRiderActiveStatus("Offline");
                        mListener.onLogout();
                    } else if (response.code() == 401) {
                        mListener.onLogout();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

}
