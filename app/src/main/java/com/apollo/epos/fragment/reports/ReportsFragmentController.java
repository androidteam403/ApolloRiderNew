package com.apollo.epos.fragment.reports;

import android.content.Context;

import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderDashboardCountResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

    public void getRiderDashboardCountsApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Call<RiderDashboardCountResponse> call = apiInterface.GET_RIDER_DASHBOARD_COUNTS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), mListener.fromDate(), mListener.toDate());
            call.enqueue(new Callback<RiderDashboardCountResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Response<RiderDashboardCountResponse> response) {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetRiderDashboardCountApiCall(response.body());
//                        getSessionManager().setCodReceived(String.valueOf(response.body().getData().getCount().getCodReceived()));
//                        getSessionManager().setCodPendingDeposited(String.valueOf(response.body().getData().getCount().getCodPending()));
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
                                    getRiderDashboardCountsApiCall();
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
                        ActivityUtils.hideDialog();
                        mListener.onFialureMessage("No Data Found");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage("No Data Found");
                    System.out.println("GET RIDER DASHBOARD COUNTS ==============" + t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void getOrdersCodStatusApiCall(int page) {
        if (NetworkUtils.isNetworkConnected(context)) {
//            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrdersCodStatusResponse> call = apiInterface.GET_ORDERS_COD_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), String.valueOf(page), "10", mListener.fromDate(), mListener.toDate());
            call.enqueue(new Callback<OrdersCodStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrdersCodStatusResponse> call, @NotNull Response<OrdersCodStatusResponse> response) {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrdersCodStatusApiCall(response.body());
                    } else if (response.code() == 401) {
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                    getOrdersCodStatusApiCall(page);
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
