package com.apollo.epos.fragment.dashboard;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.BackSlash;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderDashboardCountResponse;
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

public class DashboardFragmentController {
    private Context context;
    private DashboardFragmentCallback mListener;

    public DashboardFragmentController(Context context, DashboardFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public SessionManager getSessionManager() {
        return new SessionManager(context);
    }

//    public void getRiderProfileDetailsApi(String token) {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ActivityUtils.showDialog(context, "Please wait.");
//            ApiInterface apiInterface = ApiClient.getApiService();
//            Call<GetRiderProfileResponse> call = apiInterface.GET_RIDER_PROFILE_API_CALL("Bearer " + token);
//            call.enqueue(new Callback<GetRiderProfileResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<GetRiderProfileResponse> call, @NotNull Response<GetRiderProfileResponse> response) {
//                    ActivityUtils.hideDialog();
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        mListener.onSuccessGetProfileDetailsApi(response.body());
//                    } else if (response.code() == 401) {
//                        ActivityUtils.showDialog(context, "Please wait.");
//                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
//                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
//                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
//                        call1.enqueue(new Callback<LoginResponse>() {
//                            @Override
//                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
//                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
//                                    getRiderProfileDetailsApi(response.body().getData().getToken());
//                                } else if (response.code() == 401) {
//                                    logout();
//                                } else {
//                                    mListener.onFialureMessage("Please try again");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
//                                ActivityUtils.hideDialog();
//                                mListener.onFialureMessage("Please try again");
//                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
//                            }
//                        });
//                    } else {
//                        mListener.onFialureMessage("No data found.");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<GetRiderProfileResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFialureMessage(t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }

//    public void riderUpdateStauts(String token, String activieStatus) {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            if (!ActivityUtils.isLoadingShowing())
//                ActivityUtils.showDialog(context, "Please wait.");
//            ApiInterface apiInterface = ApiClient.getApiService();
//
//            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
//            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
//            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
//            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
//            availableStatus.setUid(activieStatus);
//            userAddInfo.setAvailableStatus(availableStatus);
//            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
//
//            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + token, riderActiveStatusRequest);
//            call.enqueue(new Callback<RiderActiveStatusResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
//                    ActivityUtils.hideDialog();
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        new SessionManager(context).setRiderActiveStatus(activieStatus);
//                    } else if (response.code() == 401) {
//                        ActivityUtils.showDialog(context, "Please wait.");
//                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
//                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
//                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
//                        call1.enqueue(new Callback<LoginResponse>() {
//                            @Override
//                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
//                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
//                                    riderUpdateStauts(response.body().getData().getToken(), activieStatus);
//                                } else if (response.code() == 401) {
//                                    logout();
//                                } else {
//                                    mListener.onFialureMessage("Please try again");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
//                                ActivityUtils.hideDialog();
//                                mListener.onFialureMessage("Please try again");
//                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
//                            }
//                        });
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
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }

//    public void getRiderDashboardCountsApiCall() {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ActivityUtils.showDialog(context, "Please wait.");
//            ApiInterface apiInterface = ApiClient.getApiService();
//
//            Call<RiderDashboardCountResponse> call = apiInterface.GET_RIDER_DASHBOARD_COUNTS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), CommonUtils.getBeforeSevenDaysDate(), CommonUtils.getCurrentDate());//CommonUtils.getfromDate() + "-01"
//            call.enqueue(new Callback<RiderDashboardCountResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Response<RiderDashboardCountResponse> response) {
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        mListener.onSuccessGetRiderDashboardCountApiCall(response.body());
//                        getSessionManager().setCodReceived(String.valueOf(response.body().getData().getCount().getCodReceived()));
//                        getSessionManager().setCodPendingDeposited(String.valueOf(response.body().getData().getCount().getCodPending()));
//                    } else if (response.code() == 401) {
//                        ActivityUtils.showDialog(context, "Please wait.");
//                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
//                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
//                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
//                        call1.enqueue(new Callback<LoginResponse>() {
//                            @Override
//                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
//                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
//                                    getRiderDashboardCountsApiCall();
//                                } else if (response.code() == 401) {
//                                    logout();
//                                } else {
//                                    mListener.onFialureMessage("Please try again");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
//                                ActivityUtils.hideDialog();
//                                mListener.onFialureMessage("Please try again");
//                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
//                            }
//                        });
//                    } else {
//                        ActivityUtils.hideDialog();
//                        mListener.onFialureMessage("No Data Found");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFialureMessage("No Data Found");
//                    System.out.println("GET RIDER DASHBOARD COUNTS ==============" + t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }

//    public void logout() {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ApiInterface apiInterface = ApiClient.getApiService();
//
//            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
//            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
//            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
//            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
//            availableStatus.setUid("Offline");
//            userAddInfo.setAvailableStatus(availableStatus);
//            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
//
//            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), riderActiveStatusRequest);
//            call.enqueue(new Callback<RiderActiveStatusResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
//                    ActivityUtils.hideDialog();
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        new SessionManager(context).setRiderActiveStatus("Offline");
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
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }


    public void riderUpdateStauts(String token, String activieStatus) {
        if (NetworkUtils.isNetworkConnected(context)) {
            if (!ActivityUtils.isLoadingShowing())
                ActivityUtils.showDialog(context, "Please wait.");

            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            if (new SessionManager(context).getRiderProfileResponse().getData().getUid()!=null) {
                riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
            }
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid(activieStatus);
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);
            Gson gson = new Gson();
            String jsonLoginRequest = gson.toJson(riderActiveStatusRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL+"api/user/save-update/update-rider-available-status");
            getDetailsRequest.setRequestjson(jsonLoginRequest);
            getDetailsRequest.setHeadertokenkey("");
            getDetailsRequest.setHeadertokenvalue("Bearer " + token);
            getDetailsRequest.setRequesttype("POST");
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
                            RiderActiveStatusResponse riderActiveStatusResponse = gson.fromJson(BackSlash.removeSubString(res), RiderActiveStatusResponse.class);
                            if (riderActiveStatusResponse != null && riderActiveStatusResponse.getData() != null && riderActiveStatusResponse.getSuccess()) {
                                new SessionManager(context).setRiderActiveStatus(activieStatus);
                            }
                            else if (response.code() == 401) {
                                ActivityUtils.showDialog(context, "Please wait.");
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                            new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                            riderUpdateStauts(response.body().getData().getToken(), activieStatus);
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
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    System.out.println("RIDER ACTIVE STATUS ==============" + t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }

    }

    public void getRiderProfileDetailsApi(String loginToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/select/rider-profile-select");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ loginToken);
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
                            GetRiderProfileResponse riderProfileResponse = gson.fromJson(BackSlash.removeSubString(res), GetRiderProfileResponse.class);
                            if (riderProfileResponse != null && riderProfileResponse.getData() != null && riderProfileResponse.getSuccess()) {
                                mListener.onSuccessGetProfileDetailsApi(riderProfileResponse);

                            } else if (response.code() == 401) {
                                Gson tokenGson = new Gson();
                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();

                                String jsonTokenRequest = tokenGson.toJson(refreshTokenRequest);
                                GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
                                getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "refresh-token");
                                getDetailsRequest.setRequestjson(jsonTokenRequest);
                                getDetailsRequest.setHeadertokenkey("");
                                getDetailsRequest.setHeadertokenvalue("");
                                getDetailsRequest.setRequesttype("POST");
                                ActivityUtils.showDialog(context, "Please wait.");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(context).setLoginToken(response.body().toString());
                                            getRiderProfileDetailsApi(getSessionManager().getLoginToken());
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

    public void logout() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(context).getRiderProfileResponse().getData().getUid());
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
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
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
                                new SessionManager(context).setRiderActiveStatus("offline");
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
            mListener.onFialureMessage("Something went wrong.");
        }
    }


//    public void getRiderDashboardCountsApiCall() {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ActivityUtils.showDialog(context, "Please wait.");
//            ApiInterface apiInterface = ApiClient.getApiService();
//
//            Call<RiderDashboardCountResponse> call = apiInterface.GET_RIDER_DASHBOARD_COUNTS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), CommonUtils.getBeforeSevenDaysDate(), CommonUtils.getCurrentDate());//CommonUtils.getfromDate() + "-01"
//            call.enqueue(new Callback<RiderDashboardCountResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Response<RiderDashboardCountResponse> response) {
//                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                        mListener.onSuccessGetRiderDashboardCountApiCall(response.body());
//                        getSessionManager().setCodReceived(String.valueOf(response.body().getData().getCount().getCodReceived()));
//                        getSessionManager().setCodPendingDeposited(String.valueOf(response.body().getData().getCount().getCodPending()));
//                    } else if (response.code() == 401) {
//                        ActivityUtils.showDialog(context, "Please wait.");
//                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
//                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
//                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
//                        call1.enqueue(new Callback<LoginResponse>() {
//                            @Override
//                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
//                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
//                                    getRiderDashboardCountsApiCall();
//                                } else if (response.code() == 401) {
//                                    logout();
//                                } else {
//                                    mListener.onFialureMessage("Please try again");
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NotNull Call<LoginResponse> call1, @NotNull Throwable t) {
//                                ActivityUtils.hideDialog();
//                                mListener.onFialureMessage("Please try again");
//                                System.out.println("REFRESH_TOKEN_DASHBOARD ==============" + t.getMessage());
//                            }
//                        });
//                    } else {
//                        ActivityUtils.hideDialog();
//                        mListener.onFialureMessage("No Data Found");
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<RiderDashboardCountResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFialureMessage("No Data Found");
//                    System.out.println("GET RIDER DASHBOARD COUNTS ==============" + t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }

//    https://apis.v35.dev.zeroco.de/zc-v3.1-user-svc/2.0/apollo_rider/api/user/select/rider-dashboard-counts?from_date=2022-12-09&to_date=2022-12-15
    public void getRiderDashboardCountsApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/select/rider-dashboard-counts"+"?"+"from_date="+CommonUtils.getBeforeSevenDaysDate()+"&"+"to_date="+CommonUtils.getCurrentDate());
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
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
                            RiderDashboardCountResponse riderDashboardCountResponse = gson.fromJson(BackSlash.removeSubString(res), RiderDashboardCountResponse.class);
                            if (riderDashboardCountResponse != null && riderDashboardCountResponse.getData() != null && riderDashboardCountResponse.getSuccess()) {
                                mListener.onSuccessGetRiderDashboardCountApiCall(riderDashboardCountResponse);
                                getSessionManager().setCodReceived(String.valueOf(riderDashboardCountResponse.getData().getCount().getCodReceived()));
                                getSessionManager().setCodPendingDeposited(String.valueOf(riderDashboardCountResponse.getData().getCount().getCodPending()));
                            }
                            else if (response.code() == 401) {
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
                            }
                            else {
                                ActivityUtils.hideDialog();
                                mListener.onFialureMessage("No Data Found");
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage("No Data Found");
                    System.out.println("GET RIDER DASHBOARD COUNTS ==============" + t.getMessage());
                }
            });
        }else {
            mListener.onFialureMessage("Something went wrong.");
        }


    }


}
