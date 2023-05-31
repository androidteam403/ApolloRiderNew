package com.apollo.epos.network;

import com.apollo.epos.activity.login.model.FirebaseTokenRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.login.model.OrderPaymentTypeResponse;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoRequest;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoResponse;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.onlinepayment.model.PhonePeQrCodeRequest;
import com.apollo.epos.activity.onlinepayment.model.PhonePeQrCodeResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.activity.orderdelivery.model.FileDataResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderPaymentSelectResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderPaymentUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollo.epos.activity.reports.model.OrdersCodStatusResponse;
import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateRequest;
import com.apollo.epos.activity.trackmap.model.OrderEndJourneyUpdateResponse;
import com.apollo.epos.activity.trackmap.model.OrderStartJourneyUpdateRequest;
import com.apollo.epos.activity.trackmap.model.OrderStartJourneyUpdateResponse;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordRequest;
import com.apollo.epos.fragment.changepassword.model.ChangePasswordResponse;
import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusRequest;
import com.apollo.epos.fragment.dashboard.model.RiderActiveStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderDashboardCountResponse;
import com.apollo.epos.fragment.dashboard.model.RiderLalangBatteryStatusResponse;
import com.apollo.epos.fragment.dashboard.model.RiderLatlangBatteryStatusRequest;
import com.apollo.epos.fragment.myorders.model.GlobalSettingSelectResponse;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateRequest;
import com.apollo.epos.fragment.profile.model.ComplaintSaveUpdateResponse;
import com.apollo.epos.model.GetRiderProfileResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("login")
    Call<LoginResponse> DO_LOGIN_API_CALL(@Body LoginRequest loginRequest);

    @POST("api/user/save-update/update-user-device-info")
    Call<SaveUserDeviceInfoResponse> SAVE_USER_DEVICE_INFO_API_CALL(@Header("authorization") String token, @Body SaveUserDeviceInfoRequest saveUserDeviceInfoRequest);

    @GET("api/global_setting/select/?uid=11FFD5814054DD13E06634029136E461")
    Call<GlobalSettingSelectResponse> GLOBAL_SETTING_SELECT_API_CALL(@Header("authorization") String token);

    @POST("updateFirebaseToken")
    Call<Object> UPDATE_FIREBASE_TOKEN_API_CALL(@Header("authorization") String token, @Body FirebaseTokenRequest firebaseTokenRequest);

    @GET("api/user/select/rider-profile-select")
    Call<GetRiderProfileResponse> GET_RIDER_PROFILE_API_CALL(@Header("authorization") String token);

    @POST("api/choose-data/complaint_reason")
    Call<ComplaintReasonsListResponse> GET_COMPLAINT_REASONS_LIST_API_CALL(@Header("authorization") String token, @Header("Content-Type") String contentType);

    @POST("api/rider_complaint/save-update")
    Call<ComplaintSaveUpdateResponse> COMPLAINT_SAVE_UPDATE_API_CALL(@Header("authorization") String token, @Body ComplaintSaveUpdateRequest complaintSaveUpdateRequest);

    @POST("api/user/save-update/update-rider-available-status")
    Call<RiderActiveStatusResponse> RIDER_ACTIVE_STATUS_API_CALL(@Header("authorization") String token, @Body RiderActiveStatusRequest riderActiveStatusRequest);

    @POST("api/user/save-update/update-rider-lat-long-battery-status")
    Call<RiderLalangBatteryStatusResponse> RIDER_LALANG_BATTERY_STATUS_API_CALL(@Header("authorization") String token, @Body RiderLatlangBatteryStatusRequest riderLatlangBatteryStatusRequest);

    @GET("api/orders/list/my-order-list")
    Call<MyOrdersListResponse> GET_MY_ORDERS_LIST_API_CALL(@Header("authorization") String token, @Query("page") String page, @Query("rows") String rows, @Query("from_date") String fromDate, @Query("to_date") String toDate);

    @GET("api/orders/select/my-order-select")
    Call<OrderDetailsResponse> ORDER_DETAILS_API_CALL(@Header("authorization") String token, @Query("order_number") String orderNumber);

    @POST("api/choose-data/delivery_failure_reasons")
    Call<DeliveryFailreReasonsResponse> DELIVERY_FAILURE_REASONS_API_CALL(@Header("authorization") String token, @Header("Content-Type") String contentType);

    @GET("api/order_status_his/list/order-status-history-list")
    Call<OrderStatusHitoryListResponse> ORDER_STATUS_HISTORY_LIST_API_CALL(@Header("authorization") String token, @Query("uid") String uid);

    @POST("api/orders/save-update/order-status-update")
    Call<OrderSaveUpdateStausResponse> ORDER_SAVE_UPDATE_STATUS_API_CALL(@Header("authorization") String token, @Body OrderSaveUpdateStausRequest orderSaveUpdateStausRequest);

    @POST("upload")
    Call<FileDataResponse> UPLOAD_IMAGE_API_CALL(@Body RequestBody file);

    @POST("api/order_handover/save-update/order-handover-save-update")
    Call<OrderHandoverSaveUpdateResponse> ORDER_HANDOVER_SAVE_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderHandoverSaveUpdateRequest orderHandoverSaveUpdateRequest);

    @POST("api/orders/save-update/order-payment-update")
    Call<Object> ORDER_PAYMENT_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderPaymentUpdateRequest orderPaymentUpdateRequest);

    @GET("api/orders/select/orders-payment-select")
    Call<OrderPaymentSelectResponse> GET_ORDER_PAYMENT_TYPE_IN_COD(@Header("authorization") String token, @Query("uid") String uid);

    @POST("api/orders/save-update/ord-rdr-strt-jrny-update")
    Call<OrderStartJourneyUpdateResponse> ORDER_START_JOURNEY_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderStartJourneyUpdateRequest orderStartJourneyUpdateRequest);

    @POST("api/orders/save-update/ord-rdr-end-jrny-update")
    Call<OrderEndJourneyUpdateResponse> ORDER_END_JOURNEY_UPDATE_API_CALL(@Header("authorization") String token, @Body OrderEndJourneyUpdateRequest orderEndJourneyUpdateRequest);

    @GET("api/user/select/rider-dashboard-counts")
    Call<RiderDashboardCountResponse> GET_RIDER_DASHBOARD_COUNTS_API_CALL(@Header("authorization") String token, @Query("from_date") String fromDate, @Query("to_date") String toDate);

    @POST("api/user/save-update/change-password")
    Call<ChangePasswordResponse> CHANGE_PASSWORD_API_CALL(@Header("authorization") String token, @Body ChangePasswordRequest changePasswordRequest);

    @GET("api/orders/list/rider-orders-cod-status")
    Call<OrdersCodStatusResponse> GET_ORDERS_COD_STATUS_API_CALL(@Header("authorization") String token, @Query("page") String page, @Query("rows") String rows, @Query("from_date") String fromDate, @Query("to_date") String toDate);

    @POST("refresh-token")
    Call<LoginResponse> REFRESH_TOKEN(@Body Object refreshTokenRequest);

    @GET("api/choose-data/order_payment_type")
    Call<OrderPaymentTypeResponse> GET_ORDER_PAYMENT_TYPE_LIST_API_CALL();

    @GET("api/rider_complaint/list")
    Call<ComplaintsResponse> GET_COMPLAINTS_LIST_API_CALL(@Header("authorization") String token, @Query("page") String page);

    //Payment Api's
//    @POST("https://online.apollopharmacy.org/PHONEPEPROD/APOLLO/PhonePe/GenerateQrCode")// UAT http://lms.apollopharmacy.org:8033/PHONEPEUAT/APOLLO/PhonePe/GenerateQrCode
    @POST("GenerateQrCode")
//https://online.apollopharmacy.org/PHONEPEUAT/APOLLO/PhonePe/
    Call<PhonePeQrCodeResponse> PHONEPE_QRCODE_API_CALL(@Body PhonePeQrCodeRequest phonePeQrCodeRequest);

    //    @POST("https://online.apollopharmacy.org/PHONEPEPROD/APOLLO/PhonePe/CheckPaymentStatus")// UAT http://lms.apollopharmacy.org:8033/PHONEPEUAT/APOLLO/PhonePe/CheckPaymentStatus
    @POST("CheckPaymentStatus")
//https://online.apollopharmacy.org/PHONEPEUAT/APOLLO/PhonePe/
    Call<PhonePeQrCodeResponse> PHONEPE_CHECK_PAYMENT_STAUS_API_CALL(@Body PhonePeQrCodeRequest phonePeQrCodeRequest);

    //    @POST("https://online.apollopharmacy.org/PHONEPEPROD/APOLLO/PhonePe/PaymentCancel")// UAT http://lms.apollopharmacy.org:8033/PHONEPEUAT/APOLLO/PhonePe/PaymentCancel
    @POST("PaymentCancel")
//https://online.apollopharmacy.org/PHONEPEUAT/APOLLO/PhonePe/
    Call<PhonePeQrCodeResponse> PHONEPE_PAYMENT_CANCELLED_API_CALL(@Body PhonePeQrCodeRequest phonePeQrCodeRequest);

    @GET("https://online.apollopharmacy.org/PHONEPELINK/apollophonepe.aspx")
    Call<ResponseBody> GENERATE_PHONEPE_LINK_API_CALL(@Query("Siteid") String siteId, @Query("docnum") String docnum, @Query("terminal") String terminal, @Query("amount") String amount, @Query("mobnum") String mobnum, @Query("Action") String action);

    @GET("https://online.apollopharmacy.org/PHONEPELINK/apollophonepe.aspx")
    Call<ResponseBody> PHONEPE_LINK_CHECK_STATUS_API_CALL(@Query("Siteid") String siteId, @Query("docnum") String docnum, @Query("terminal") String terminal, @Query("transactionid") String transactionId, @Query("Action") String action);

    @GET("https://online.apollopharmacy.org/PHONEPELINK/apollophonepe.aspx")
    Call<ResponseBody> PHONEPE_LINK_CANCELLED_REQUEST_API_CALL(@Query("Siteid") String siteId, @Query("docnum") String docnum, @Query("referenceid") String referenceId, @Query("mobnum") String mobileNumber, @Query("transactionid") String transactionId, @Query("Action") String action);
}