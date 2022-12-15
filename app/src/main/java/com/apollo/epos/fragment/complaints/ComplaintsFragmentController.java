package com.apollo.epos.fragment.complaints;

import android.content.Context;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.BackSlash;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateRequest;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateResponse;
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

public class ComplaintsFragmentController {

    private Context mContext;
    private ComplaintsFragmentCallback mListener;

    public ComplaintsFragmentController(Context mContext, ComplaintsFragmentCallback mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public void getComplaintsListApiCall(int page) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(String.format("%sapi/rider_complaint/list?page=%s", BuildConfig.BASE_URL, String.valueOf(page)));
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(mContext).getLoginToken());
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
                            ComplaintsResponse complaintsResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintsResponse.class);
                            if (complaintsResponse != null && complaintsResponse.getData() != null && complaintsResponse.getSuccess()) {
                                mListener.onSuccessGetComplaintsListApiCall(complaintsResponse);

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
                                ActivityUtils.showDialog(mContext, "Please wait.");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(mContext).setLoginToken(response.body().toString());
                                            getComplaintsListApiCall(page);                                        } else if (response.code() == 401) {
                                            logout();
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
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
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
    public void getComplaintReasonsListApiCall() {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/choose-data/complaint_reason");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(mContext).getLoginToken());
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
                            ComplaintReasonsListResponse complaintsResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintReasonsListResponse.class);
                            if (complaintsResponse != null && complaintsResponse.getData() != null && complaintsResponse.getSuccess()) {
                                mListener.onSuccessComplaintReasonsListApiCall(complaintsResponse);

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
                                ActivityUtils.showDialog(mContext, "Please wait.");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(mContext).setLoginToken(response.body().toString());
                                            getComplaintReasonsListApiCall();
                                        }
                                           else if (response.code() == 401) {
                                    logout();
//
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
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
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
    public void riderComplaintSaveUpdateApiCall(String reason, String comment) {
        if (NetworkUtils.isNetworkConnected(mContext)) {
            ActivityUtils.showDialog(mContext, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            ComplaintSaveUpdateRequest complaintSaveUpdateRequest=new ComplaintSaveUpdateRequest();

            complaintSaveUpdateRequest.setComments(comment);
            ComplaintSaveUpdateRequest.Reason reason1 = new ComplaintSaveUpdateRequest.Reason();
            reason1.setUid(reason);
            complaintSaveUpdateRequest.setReason(reason1);
            Gson gson = new Gson();
            String jsoncomplaintSaveUpdatRequest = gson.toJson(complaintSaveUpdateRequest);

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/rider_complaint/save-update");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setRequestjson(jsoncomplaintSaveUpdatRequest);
            getDetailsRequest.setHeadertokenvalue("Bearer "+ new SessionManager(mContext).getLoginToken());
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
                            ComplaintSaveUpdateResponse complaintSaveUpdateResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintSaveUpdateResponse.class);
                            if (complaintSaveUpdateResponse != null && complaintSaveUpdateResponse.getSuccess()) {
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
                                ActivityUtils.showDialog(mContext, "Please wait.");
//                                HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                                refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                                Call<ResponseBody> call1 = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);

//                                Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call1, @NotNull Response<ResponseBody> response) {
                                        if (response.code() == 200 && response.body() != null) {
                                            new SessionManager(mContext).setLoginToken(response.body().toString());
                                            riderComplaintSaveUpdateApiCall(reason, comment);
                                        }
                                        else if (response.code() == 401) {
                                            logout();
//
                                        } else {
                                            mListener.onFailureMessage("Please try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call1, @NotNull Throwable t) {
                                        ActivityUtils.hideDialog();
                                        mListener.onFailureMessage("Please try again");
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
                    mListener.onFailureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
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