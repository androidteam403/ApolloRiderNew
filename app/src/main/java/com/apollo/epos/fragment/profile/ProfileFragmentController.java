package com.apollo.epos.fragment.profile;

import android.content.Context;

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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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
            Call<GetRiderProfileResponse> call = apiInterface.GET_RIDER_PROFILE_API_CALL("Bearer " + new SessionManager(context).getLoginToken());
            call.enqueue(new Callback<GetRiderProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<GetRiderProfileResponse> call, @NotNull Response<GetRiderProfileResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetProfileDetailsApi(response.body());
                    } else if (response.code() == 401){
                        ActivityUtils.showDialog(context, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                    getRiderProfileDetailsApi();
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
                    }else {
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

    public void getComplaintReasonsListApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please Wait");
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ComplaintReasonsListResponse> call = apiInterface.GET_COMPLAINT_REASONS_LIST_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), "application/json");
            call.enqueue(new Callback<ComplaintReasonsListResponse>() {
                @Override
                public void onResponse(@NotNull Call<ComplaintReasonsListResponse> call, @NotNull Response<ComplaintReasonsListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintReasonsListApiCall(response.body());
                    }else if (response.code() == 401){
                        ActivityUtils.showDialog(context, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                    getRiderProfileDetailsApi();
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
                    }else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ComplaintReasonsListResponse> call, @NotNull Throwable t) {
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
            ComplaintSaveUpdateRequest complaintSaveUpdateRequest = new ComplaintSaveUpdateRequest();
            complaintSaveUpdateRequest.setComments(comment);
            ComplaintSaveUpdateRequest.Reason reason1 = new ComplaintSaveUpdateRequest.Reason();
            reason1.setUid(reason);
            complaintSaveUpdateRequest.setReason(reason1);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ComplaintSaveUpdateResponse> call = apiInterface.COMPLAINT_SAVE_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), complaintSaveUpdateRequest);
            call.enqueue(new Callback<ComplaintSaveUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<ComplaintSaveUpdateResponse> call, @NotNull Response<ComplaintSaveUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintSaveUpdate(response.body().getMessage());
                    } else if (response.code() == 401){

                        ActivityUtils.showDialog(context, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(context).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(context).setLoginToken(response.body().getData().getToken());
                                    getRiderProfileDetailsApi();
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

                    }else {
                        mListener.onFailureGetProfileDetailsApi("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ComplaintSaveUpdateResponse> call, @NotNull Throwable t) {
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
