package com.apollo.epos.fragment.profile;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateRequest;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

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
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessGetProfileDetailsApi(response.body());
                    } else {
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
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintReasonsListApiCall(response.body());
                    } else {
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
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessComplaintSaveUpdate(response.body().getMessage());
                    } else {
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
}
