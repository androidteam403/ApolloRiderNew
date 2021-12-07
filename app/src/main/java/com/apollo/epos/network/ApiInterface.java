package com.apollo.epos.network;

import com.apollo.epos.activity.login.model.FirebaseTokenRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoRequest;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoResponse;
import com.apollo.epos.activity.neworder.model.OrderDetailsRequest;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.orderdelivery.model.FileDataResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderPaymentUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHistoryListRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderLalangBatteryStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderLatlangBatteryStatusRequest;
import com.apollo.epos.fragment.myorders.model.MyOrdersListRequest;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @POST("login")
    Call<LoginResponse> DO_LOGIN_API_CALL(@Body LoginRequest loginRequest);

    @POST("api/user/save-update/update-user-device-info")
    Call<SaveUserDeviceInfoResponse> SAVE_USER_DEVICE_INFO_API_CALL(@Header("authorization") String token, @Body SaveUserDeviceInfoRequest saveUserDeviceInfoRequest);

    @POST("updateFirebaseToken")
    Call<Object> dummy(@Header("authorization") String token, @Body FirebaseTokenRequest o);

    @POST("api/user/select/rider-profile-select")
    Call<GetRiderProfileResponse> GET_RIDER_PROFILE_API_CALL(@Header("authorization") String token);

    @POST("api/user/save-update/update-rider-available-status")
    Call<RiderActiveStatusResponse> RIDER_ACTIVE_STATUS_API_CALL(@Header("authorization") String token, @Body RiderActiveStatusRequest riderActiveStatusRequest);

    @POST("api/user/save-update/update-rider-lat-long-battery-status")
    Call<RiderLalangBatteryStatusResponse> RIDER_LALANG_BATTERY_STATUS_API_CALL(@Header("authorization") String token, @Body RiderLatlangBatteryStatusRequest riderLatlangBatteryStatusRequest);

    @POST("api/orders/list/my-order-list")
    Call<MyOrdersListResponse> GET_MY_ORDERS_LIST_API_CALL(@Header("authorization") String token, @Body MyOrdersListRequest myOrdersListRequest);

    @POST("api/orders/select/my-order-select")
    Call<OrderDetailsResponse> ORDER_DETAILS_API_CALL(@Header("authorization") String token, @Body OrderDetailsRequest orderDetailsRequest);

    @POST("api/order_status_his/list/order-status-history-list")
    Call<OrderStatusHitoryListResponse> ORDER_STATUS_HISTORY_LIST_API_CALL(@Header("authorization") String token, @Body OrderStatusHistoryListRequest orderStatusHistoryListRequest);

    @PUT("api/orders/save-update")
    Call<OrderSaveUpdateStausResponse> ORDER_SAVE_UPDATE_STATUS_API_CALL(@Header("authorization") String token, @Body OrderSaveUpdateStausRequest orderSaveUpdateStausRequest);

    @POST("upload")
    Call<FileDataResponse> UPLOAD_IMAGE_API_CALL(@Body RequestBody file);

    @POST("api/order_handover/save-update/order-handover-save-update")
    Call<OrderHandoverSaveUpdateResponse> ORDER_HANDOVER_SAVE_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderHandoverSaveUpdateRequest orderHandoverSaveUpdateRequest);

    @POST("api/orders/save-update/order-payment-update")
    Call<Object> ORDER_PAYMENT_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderPaymentUpdateRequest orderPaymentUpdateRequest);
}