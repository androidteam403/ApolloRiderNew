package com.apollo.epos.activity.trackmap;

import android.content.Context;

import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausResponse;
import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateRequest;
import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollo.epos.activity.trackmap.model.OrderStartJourneyUpdateRequest;
import com.apollo.epos.activity.trackmap.model.OrderStartJourneyUpdateResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackMapActivityController {
    private Context context;
    private TrackMapActivityCallback mListener;

    public TrackMapActivityController(Context context, TrackMapActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }
    public void ordersSaveUpdateStatusApiCall(String orderStatus, String orderUid, String orderCancelReason, String comment) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderSaveUpdateStausRequest orderSaveUpdateStausRequest = new OrderSaveUpdateStausRequest();
            orderSaveUpdateStausRequest.setUid(orderUid);
            OrderSaveUpdateStausRequest.OrderStatus orderStatus1 = new OrderSaveUpdateStausRequest.OrderStatus();
            orderStatus1.setUid(orderStatus);
            orderSaveUpdateStausRequest.setOrderStatus(orderStatus1);
            OrderSaveUpdateStausRequest.DeliveryFailureReason deliveryFailureReason = new OrderSaveUpdateStausRequest.DeliveryFailureReason();
            deliveryFailureReason.setUid(orderCancelReason);
            orderSaveUpdateStausRequest.setDeliveryFailureReason(deliveryFailureReason);
            orderSaveUpdateStausRequest.setComment(comment);
            ApiInterface apiInterface = ApiClient.getApiService();

            Call<OrderSaveUpdateStausResponse> call = apiInterface.ORDER_SAVE_UPDATE_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderSaveUpdateStausRequest);
            call.enqueue(new Callback<OrderSaveUpdateStausResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderSaveUpdateStausResponse> call, @NotNull Response<OrderSaveUpdateStausResponse> response) {

                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderSaveUpdateStatusApi(orderStatus);

                    } else {
                        ActivityUtils.hideDialog();
//                        mListener.onFialureOrderSaveUpdateStatusApi(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderSaveUpdateStausResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderStartJourneyUpdateApiCall(String uid, String distance) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");

            OrderStartJourneyUpdateRequest orderStartJourneyUpdateRequest = new OrderStartJourneyUpdateRequest();
            orderStartJourneyUpdateRequest.setUid(uid);
            OrderStartJourneyUpdateRequest.OrderRider orderRider = new OrderStartJourneyUpdateRequest.OrderRider();
            orderRider.setActualDistance(distance);
            orderRider.setStartTime(CommonUtils.getCurrentTimeDate());
            orderStartJourneyUpdateRequest.setOrderRider(orderRider);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderStartJourneyUpdateResponse> call = apiInterface.ORDER_START_JOURNEY_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderStartJourneyUpdateRequest);
            call.enqueue(new Callback<OrderStartJourneyUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderStartJourneyUpdateResponse> call, @NotNull Response<OrderStartJourneyUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderStartJourneyUpdateApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data saved.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderStartJourneyUpdateResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderEndJourneyUpdateApiCall(String uid) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");

            OrderEndJourneyUpdateRequest orderEndJourneyUpdateRequest = new OrderEndJourneyUpdateRequest();
            orderEndJourneyUpdateRequest.setUid(uid);
            OrderEndJourneyUpdateRequest.OrderRider orderRider = new OrderEndJourneyUpdateRequest.OrderRider();
            orderRider.setEndTime(CommonUtils.getCurrentTimeDate());
            orderEndJourneyUpdateRequest.setOrderRider(orderRider);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderEndJourneyUpdateResponse> call = apiInterface.ORDER_END_JOURNEY_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderEndJourneyUpdateRequest);
            call.enqueue(new Callback<OrderEndJourneyUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderEndJourneyUpdateResponse> call, @NotNull Response<OrderEndJourneyUpdateResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderEndJourneyUpdateApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data saved.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderEndJourneyUpdateResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
