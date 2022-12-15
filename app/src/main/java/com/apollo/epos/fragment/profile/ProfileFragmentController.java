package com.apollo.epos.fragment.profile;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.BackSlash;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateRequest;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.AppConstants;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragmentController {
    private final Context context;
    private final ProfileFragmentCallback mListener;

    public ProfileFragmentController(Context context, ProfileFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void getRiderProfileDetailsApi() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/select/rider-profile-select");
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
                                            getRiderProfileDetailsApi();
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
                    mListener.onFailureGetProfileDetailsApi(t.getMessage());
                }
            });

        } else {
            mListener.onFailureGetProfileDetailsApi("Something went wrong.");
        }
    }

    public void getComplaintReasonsListApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            Gson gson = new Gson();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/choose-data/complaint_reason");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+new SessionManager(context).getLoginToken());
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
                            ComplaintReasonsListResponse complaintReasonsListResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintReasonsListResponse.class);
                            if (complaintReasonsListResponse != null && complaintReasonsListResponse.getData() != null && complaintReasonsListResponse.getSuccess()) {
                                mListener.onSuccessComplaintReasonsListApiCall(complaintReasonsListResponse);

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
                                            getRiderProfileDetailsApi();
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

    public void riderComplaintSaveUpdateApiCall(String reason, String comment) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            ComplaintSaveUpdateRequest complaintSaveUpdateRequest = new ComplaintSaveUpdateRequest();
            complaintSaveUpdateRequest.setComments(comment);
            ComplaintSaveUpdateRequest.Reason reason1 = new ComplaintSaveUpdateRequest.Reason();
            reason1.setUid(reason);
            complaintSaveUpdateRequest.setReason(reason1);
            Gson gson = new Gson();
            String jsoncomplaintRequest = gson.toJson(complaintSaveUpdateRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/rider_complaint/save-update");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer "+new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            getDetailsRequest.setRequestjson(jsoncomplaintRequest);

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
                            ComplaintSaveUpdateResponse complaintSaveUpdateResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintSaveUpdateResponse.class);
                            if (complaintSaveUpdateResponse != null && complaintSaveUpdateResponse.getData() != null && complaintSaveUpdateResponse.getSuccess()) {
                                mListener.onSuccessComplaintSaveUpdate(complaintSaveUpdateResponse.getMessage());

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
                                            getRiderProfileDetailsApi();
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
                    mListener.onFailureGetProfileDetailsApi(t.getMessage());
                }
            });

        } else {
            mListener.onFailureGetProfileDetailsApi("Something went wrong.");
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
}
