package com.apollo.epos.fragment.help;

import android.content.Context;

import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.help.model.RiderBasicDetailsforHelpResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpFragmentController {
    private Context context;
    private HelpFragmentCallback mListener;

    public HelpFragmentController(Context context, HelpFragmentCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void riderBasicDetailsforHelpApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            HashMap<String, String> riderBasicDetailsforHelpRequest = new HashMap<>();
            riderBasicDetailsforHelpRequest.put("uid", new SessionManager(context).getRiderProfileResponse().getData().getUid());
            ApiInterface apiInterface = ApiClient.getApiService();
            Call<RiderBasicDetailsforHelpResponse> call = apiInterface.RIDER_BASIC_DETAILS_FOR_HELP_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), riderBasicDetailsforHelpRequest);
            call.enqueue(new Callback<RiderBasicDetailsforHelpResponse>() {
                @Override
                public void onResponse(@NotNull Call<RiderBasicDetailsforHelpResponse> call, @NotNull Response<RiderBasicDetailsforHelpResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessRiderBasicDetailsforHelpApiCall(response.body());
                    } else {
                        mListener.onFailureRiderBasicDetailsforHelpApiCall("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RiderBasicDetailsforHelpResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFailureMessage("Something went wrong.");
        }
    }
}
