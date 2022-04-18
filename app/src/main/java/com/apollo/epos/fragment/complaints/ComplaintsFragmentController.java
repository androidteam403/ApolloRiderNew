package com.apollo.epos.fragment.complaints;

import android.content.Context;

import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateRequest;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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
            Call<ComplaintsResponse> call = apiInterface.GET_COMPLAINTS_LIST_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), String.valueOf(page));
            call.enqueue(new Callback<ComplaintsResponse>() {
                @Override
                public void onResponse(@NotNull Call<ComplaintsResponse> call, @NotNull Response<ComplaintsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetComplaintsListApiCall(response.body());
                    } else if (response.code() == 401) {

                        ActivityUtils.showDialog(mContext, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(mContext).setLoginToken(response.body().getData().getToken());
                                    getComplaintsListApiCall(page);
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

                    } else {
                        mListener.onFailureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ComplaintsResponse> call, @NotNull Throwable t) {
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
            ActivityUtils.showDialog(mContext, "Please Wait");
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ComplaintReasonsListResponse> call = apiInterface.GET_COMPLAINT_REASONS_LIST_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), "application/json");
            call.enqueue(new Callback<ComplaintReasonsListResponse>() {
                @Override
                public void onResponse(@NotNull Call<ComplaintReasonsListResponse> call, @NotNull Response<ComplaintReasonsListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintReasonsListApiCall(response.body());
                    }else if (response.code() == 401){
                        ActivityUtils.showDialog(mContext, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(mContext).setLoginToken(response.body().getData().getToken());
                                    getComplaintReasonsListApiCall();
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
                    }else {
                        mListener.onFailureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ComplaintReasonsListResponse> call, @NotNull Throwable t) {
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
            ComplaintSaveUpdateRequest complaintSaveUpdateRequest = new ComplaintSaveUpdateRequest();
            complaintSaveUpdateRequest.setComments(comment);
            ComplaintSaveUpdateRequest.Reason reason1 = new ComplaintSaveUpdateRequest.Reason();
            reason1.setUid(reason);
            complaintSaveUpdateRequest.setReason(reason1);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<ComplaintSaveUpdateResponse> call = apiInterface.COMPLAINT_SAVE_UPDATE_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), complaintSaveUpdateRequest);
            call.enqueue(new Callback<ComplaintSaveUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<ComplaintSaveUpdateResponse> call, @NotNull Response<ComplaintSaveUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintSaveUpdate(response.body().getMessage());
                    } else if (response.code() == 401){

                        ActivityUtils.showDialog(mContext, "Please wait.");
                        HashMap<String, Object> refreshTokenRequest = new HashMap<>();
                        refreshTokenRequest.put("token", new SessionManager(mContext).getLoginToken());
                        Call<LoginResponse> call1 = apiInterface.REFRESH_TOKEN(refreshTokenRequest);
                        call1.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call1, @NotNull Response<LoginResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    new SessionManager(mContext).setLoginToken(response.body().getData().getToken());
                                    riderComplaintSaveUpdateApiCall(reason, comment);
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

                    }else {
                        mListener.onFailureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ComplaintSaveUpdateResponse> call, @NotNull Throwable t) {
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
            ApiInterface apiInterface = ApiClient.getApiService();
            RiderActiveStatusRequest riderActiveStatusRequest = new RiderActiveStatusRequest();
            riderActiveStatusRequest.setUid(new SessionManager(mContext).getRiderProfileResponse().getData().getUid());
            RiderActiveStatusRequest.UserAddInfo userAddInfo = new RiderActiveStatusRequest.UserAddInfo();
            RiderActiveStatusRequest.AvailableStatus availableStatus = new RiderActiveStatusRequest.AvailableStatus();
            availableStatus.setUid("Offline");
            userAddInfo.setAvailableStatus(availableStatus);
            riderActiveStatusRequest.setUserAddInfo(userAddInfo);

            Call<RiderActiveStatusResponse> call = apiInterface.RIDER_ACTIVE_STATUS_API_CALL("Bearer " + new SessionManager(mContext).getLoginToken(), riderActiveStatusRequest);
            call.enqueue(new Callback<RiderActiveStatusResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderActiveStatusResponse> call, @NotNull Response<RiderActiveStatusResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        new SessionManager(mContext).setRiderActiveStatus("Offline");
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
            mListener.onFailureMessage("Something went wrong.");
        }
    }
}
