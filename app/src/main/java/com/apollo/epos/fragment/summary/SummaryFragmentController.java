package com.apollo.epos.fragment.summary;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.BackSlash;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.AppConstants;
import com.apollo.epos.utils.CommonUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryFragmentController {

    private Context mContext;
    private SummaryFragmentCallback mListener;

    public SummaryFragmentController(Context mContext, SummaryFragmentCallback mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public void myOrdersListApiCall(int page) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/orders/list/my-order-list"+"?"+"page="+page+"&"+"rows="+"10"+"&"+"from_date="+mListener.fromDate()+"&"+"to_date="+mListener.toDate());
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(mContext).getLoginToken());
            getDetailsRequest.setRequesttype("GET");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            MyOrdersListResponse myOrdersListResponse = gson.fromJson(BackSlash.removeSubString(res), MyOrdersListResponse.class);
                            if (myOrdersListResponse != null && myOrdersListResponse.getData() != null && myOrdersListResponse.getSuccess()) {
                                mListener.onSuccessMyOrdersListApi(myOrdersListResponse);
                            }
                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(mContext, "Please wait.");
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                            new SessionManager(mContext).setLoginToken(response.body().getData().getToken());
                                            myOrdersListApiCall(page);
                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });
                            }
                            else {
//                        mListener.onFailureMessage();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }

//    public void myOrdersListApiCall(int page) {
//        if (NetworkUtils.isNetworkConnected(mContext)) {
//            ApiInterface apiInterface = ApiClient.getApiService();
//            Call<MyOrdersListResponse> call = apiInterface.GET_MY_ORDERS_LIST_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), String.valueOf(page), "10", mListener.fromDate(), mListener.toDate());
//            call.enqueue(new Callback<MyOrdersListResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<MyOrdersListResponse> call, @NotNull Response<MyOrdersListResponse> response) {
//                    ActivityUtils.hideDialog();
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        mListener.onSuccessMyOrdersListApi(response.body());
//                    }
//                    else if (response.code() == 401) {
//                        ActivityUtils.showDialog(mContext, "Please wait.");
//                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
//                        refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
//                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
//                        call1.enqueue(new Callback<LoginResponse>() {
//                            @Override
//                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
//                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                                    new SessionManager(mContext).setLoginToken(response.body().getData().getToken());
//                                    myOrdersListApiCall(page);
//                                } else if (response.code() == 401) {
//                                    logout();
//                                } else {
//                                    mListener.onFailureMessage("Please try again");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
//                                ActivityUtils.hideDialog();
//                                mListener.onFailureMessage("Please try again");
//                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
//                            }
//                        });
//                    }
//                    else {
////                        mListener.onFailureMessage();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<MyOrdersListResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFailureMessage(t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFailureMessage("Something went wrong.");
//        }
//    }
//    public void logout() {
//        if (NetworkUtils.isNetworkConnected(mContext)) {
//            ApiInterface apiInterface = ApiClient.getApiService();
//            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
//            riderActiveStatusRequest.setUid(new SessionManager(mContext).getRiderProfileResponse().getData().getUid());
//            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
//            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
//            availableStatus.setUid("Offline");
//            userAddInfo.setAvailableStatus(availableStatus);
//            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
//
//            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), riderActiveStatusRequest);
//            call.enqueue(new Callback<RiderActiveStatusResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
//                    ActivityUtils.hideDialog();
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        new SessionManager(mContext).setRiderActiveStatus("Offline");
//                        mListener.onLogout();
//                    } else if (response.code() == 401) {
//                        mListener.onLogout();
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFailureMessage("Something went wrong.");
//        }
//    }

    public void logout() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(mContext).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            availableStatus.setUid("offline");
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);

            Gson gson = new Gson();
            String jsonriderRequest = gson.toJson(riderActiveStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/save-update/update-rider-available-status");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(mContext).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            getDetailsRequest.setRequestjson(jsonriderRequest);

            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            RiderActiveStatusResponse riderActiveStatusResponse = gson.fromJson(BackSlash.removeSubString(res), RiderActiveStatusResponse.class);
                            if (riderActiveStatusResponse != null && riderActiveStatusResponse.getData() != null && riderActiveStatusResponse.getSuccess()) {
                                new SessionManager(mContext).setRiderActiveStatus("offline");
                                mListener.onLogout();
                            } else if (response.code()==401) {
                                ActivityUtils.hideDialog();
                                mListener.onLogout();

                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    System.out.println("RIDER ACTIVE STATUS ==========="+t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
}
