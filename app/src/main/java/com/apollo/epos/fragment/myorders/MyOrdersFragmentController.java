package com.apollo.epos.fragment.myorders;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.BackSlash;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordRequest;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.myorders.model.GlobalSettingSelectResponse;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
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
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/orders/list/my-order-list"+"?"+"page="+String.valueOf(1)+"&"+"rows="+String.valueOf(1000)+"&from_date="+CommonUtils.getBeforeSevenDaysDate()+"&to_date="+CommonUtils.getCurrentDate());
//            getDetailsRequest.setRequesturl(String.format("%sapi/orders/list/rider-orders-cod-status?%s10&%s&%s", BuildConfig.BASE_URL, String.valueOf(page), mListener.fromDate(), mListener.toDate()));

            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("GET");

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
                            MyOrdersListResponse myOrdersListResponse = gson.fromJson(BackSlash.removeSubString(res), MyOrdersListResponse.class);
                            if (myOrdersListResponse != null && myOrdersListResponse.getData() != null && myOrdersListResponse.getSuccess()) {
                                mListener.onSuccessMyOrdersListApi(myOrdersListResponse);

                            }

                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(context, "Please wait.");

                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            myOrdersListApiCall();
                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFialureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFialureMessage("Please try again");
                                        System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
                                    }
                                });

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/global_setting/select/?uid=11FFD5814054DD13E06634029136E461");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken() );
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setRequesttype("GET");
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
                            GlobalSettingSelectResponse globalSettingSelectResponse = gson.fromJson(BackSlash.removeSubString(res), GlobalSettingSelectResponse.class);
                            if (globalSettingSelectResponse != null && globalSettingSelectResponse.getData() != null && globalSettingSelectResponse.getSuccess()) {
                                new SessionManager(context).setGlobalSettingSelectResponse(globalSettingSelectResponse);

                            } else {
                                ActivityUtils.hideDialog();
                                System.out.println("globalSettingSelectApi::::::::::::::::::::::::::::::: Something went wrong.");

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid("Offline");

            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
            Gson gson = new Gson();
            String jsonriderActiveStatusRequest = gson.toJson(riderActiveStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/save-update/update-rider-available-status");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(context).getLoginToken() );
            getDetailsRequest.setRequestjson(jsonriderActiveStatusRequest);
            getDetailsRequest.setRequesttype("POST");
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
                                new SessionManager(context).setRiderActiveStatus("Offline");
                                mListener.onLogout();
                            } else if (response.code() == 401) {
                        mListener.onLogout();
                    }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage("Something went wrong.");
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }


}
