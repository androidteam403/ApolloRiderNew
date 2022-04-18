package com.apollo.epos.db;
/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.apollo.epos.activity.login.model.OrderPaymentTypeResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.fragment.myorders.model.GlobalSettingSelectResponse;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SessionManager {
    SharedPreferences preferences;

    //Preff keys
    private static final String PREF_KEY_LOGIN_TOKEN = "PREF_KEY_LOGIN_TOKEN";
    private static final String PREF_KEY_RIDER_PROFILE_DETAILS = "PREF_KEY_RIDER_PROFILE_DETAILS";
    private static final String PREF_KEY_USER_ICON = "PREF_KEY_USER_ICON";
    private static final String PREF_KEY_RIDER_ACTIVE_STATUS = "PREF_KEY_RIDER_ACTIVE_STATUS";
    private static final String PREF_KEY_NOTIFICATION_STATUS = "PREF_KEY_NOTIFICATION_STATUS";
    private static final String PREF_KEY_NOTIFICATION_ARRIVED_TIME = "PREF_KEY_NOTIFICATION_ARRIVED_TIME";
    private static final String PREF_KEY_DELIVERY_FAILURE_REASONS = "PREF_KEY_DELIVERY_FAILURE_REASONS";
    private static final String PREF_KEY_RIDER_TRAVELLED_DISTANCE_IN_DAY = "PREF_KEY_RIDER_TRAVELLED_DISTANCE_IN_DAY";
    private static final String PREF_KEY_ASIGNED_ORDER_UID = "PREF_KEY_ASIGNED_ORDER_UID";
    private static final String PREF_KEY_COD_RECEIVED = "PREF_KEY_COD_RECEIVED";
    private static final String PREF_KEY_COD_PENDING_DEPOSITED = "PREF_KEY_COD_PENDING_DEPOSITED";
    private static final String PREF_KEY_ORDER_PAYMENT_TYPE_LIST = "PREF_KEY_ORDER_PAYMENT_TYPE_LIST";
    private static final String PREF_KEY_GLOBAL_SETTING_SELECT = "PREF_KEY_GLOBAL_SETTING_SELECT";
    private static final String PREF_KEY_COMPLAINT_REASONS = "PREF_KEY_COMPLAINT_REASONS";

    public SessionManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearAllSharedPreferences() {
        preferences.edit().clear().apply();
    }

    public void setLoginToken(String loginLoken) {
        preferences.edit().putString(PREF_KEY_LOGIN_TOKEN, loginLoken).apply();
    }


    public String getLoginToken() {
        return preferences.getString(PREF_KEY_LOGIN_TOKEN, "");
    }

    public void setRiderIconUrl(String riderIconUrl) {
        preferences.edit().putString(PREF_KEY_USER_ICON, riderIconUrl).apply();
    }

    public String getrRiderIconUrl() {
        return preferences.getString(PREF_KEY_USER_ICON, "");
    }

    public void setRiderProfileDetails(GetRiderProfileResponse getRiderProfileResponse) {
        String riderProfileDetails = new Gson().toJson(getRiderProfileResponse);
        preferences.edit().putString(PREF_KEY_RIDER_PROFILE_DETAILS, riderProfileDetails).apply();
    }

    public GetRiderProfileResponse getRiderProfileResponse() {
        String riderProfileDetails = preferences.getString(PREF_KEY_RIDER_PROFILE_DETAILS, null);
        return new Gson().fromJson(riderProfileDetails, GetRiderProfileResponse.class);
    }

    public void setDeliveryFailureReasonsList(DeliveryFailreReasonsResponse deliveryFailreReasonsResponse) {
        String deliveryFailreReasonsResponseList = new Gson().toJson(deliveryFailreReasonsResponse);
        preferences.edit().putString(PREF_KEY_DELIVERY_FAILURE_REASONS, deliveryFailreReasonsResponseList).apply();
    }

    public DeliveryFailreReasonsResponse getDeliveryFailureReasonseList() {
        String deliveryFailreReasonsResponseList = preferences.getString(PREF_KEY_DELIVERY_FAILURE_REASONS, null);
        return new Gson().fromJson(deliveryFailreReasonsResponseList, DeliveryFailreReasonsResponse.class);
    }

    public void setRiderActiveStatus(String riderActiveStatus) {
        preferences.edit().putString(PREF_KEY_RIDER_ACTIVE_STATUS, riderActiveStatus).apply();
    }

    public String getRiderActiveStatus() {
        return preferences.getString(PREF_KEY_RIDER_ACTIVE_STATUS, "Online");
    }

    public void setNotificationStatus(boolean notificationStatus) {
        preferences.edit().putBoolean(PREF_KEY_NOTIFICATION_STATUS, notificationStatus).apply();
    }

    public boolean getNotificationStatus() {
        return preferences.getBoolean(PREF_KEY_NOTIFICATION_STATUS, false);
    }

    public void setNotificationArrivedTime(String arrivedTime) {
        preferences.edit().putString(PREF_KEY_NOTIFICATION_ARRIVED_TIME, arrivedTime).apply();
    }

    public String getNotificationArrivedTime() {
        return preferences.getString(PREF_KEY_NOTIFICATION_ARRIVED_TIME, "Offline");
    }

    public void setRiderTravelledDistanceinDay(String riderTravelledDistanceinDay) {
        preferences.edit().putString(PREF_KEY_RIDER_TRAVELLED_DISTANCE_IN_DAY, riderTravelledDistanceinDay).apply();
    }

    public String getRiderTravelledDistanceinDay() {
        return preferences.getString(PREF_KEY_RIDER_TRAVELLED_DISTANCE_IN_DAY, "0.0");
    }

    public void setAsignedOrderUid(List<String> orderUids) {
        String orderUidListString = new Gson().toJson(orderUids);
        preferences.edit().putString(PREF_KEY_ASIGNED_ORDER_UID, orderUidListString).apply();
    }

    public List<String> getAsignedOrderUid() {
        String orderUidList = preferences.getString(PREF_KEY_ASIGNED_ORDER_UID, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(orderUidList, type);
    }

    public void setCodReceived(String codReceived) {
        preferences.edit().putString(PREF_KEY_COD_RECEIVED, codReceived).apply();
    }

    public String getCodReceived() {
        return preferences.getString(PREF_KEY_COD_RECEIVED, "0.0");
    }

    public void setCodPendingDeposited(String codPendingDeposited) {
        preferences.edit().putString(PREF_KEY_COD_PENDING_DEPOSITED, codPendingDeposited).apply();
    }

    public String getCodPendingDeposited() {
        return preferences.getString(PREF_KEY_COD_PENDING_DEPOSITED, "0.0");
    }

    public void setOrderPaymentTypeList(OrderPaymentTypeResponse orderPaymentTypeResponse) {
        String orderPaymentTypeResponseList = new Gson().toJson(orderPaymentTypeResponse);
        preferences.edit().putString(PREF_KEY_ORDER_PAYMENT_TYPE_LIST, orderPaymentTypeResponseList).apply();
    }

    public OrderPaymentTypeResponse getOrderPaymentTypeList() {
        String orderPaymentTypeResponseList = preferences.getString(PREF_KEY_ORDER_PAYMENT_TYPE_LIST, null);
        return new Gson().fromJson(orderPaymentTypeResponseList, OrderPaymentTypeResponse.class);
    }

    public void setGlobalSettingSelectResponse(GlobalSettingSelectResponse globalSettingSelectResponse) {
        String globalSettingSelectResponseString = new Gson().toJson(globalSettingSelectResponse);
        preferences.edit().putString(PREF_KEY_GLOBAL_SETTING_SELECT, globalSettingSelectResponseString).apply();
    }

    public GlobalSettingSelectResponse getGlobalSettingSelectResponse() {
        String globalSettingSelectResponseString = preferences.getString(PREF_KEY_GLOBAL_SETTING_SELECT, null);
        return new Gson().fromJson(globalSettingSelectResponseString, GlobalSettingSelectResponse.class);
    }

    public void setComplaintReasonsListResponse(ComplaintReasonsListResponse complaintReasonsListResponse) {
        String complaintReasonsListResponseString = new Gson().toJson(complaintReasonsListResponse);
        preferences.edit().putString(PREF_KEY_COMPLAINT_REASONS, complaintReasonsListResponseString).apply();
    }

    public ComplaintReasonsListResponse getComplaintReasonsListResponse() {
        String complaintReasonsListResponseString = preferences.getString(PREF_KEY_COMPLAINT_REASONS, null);
        return new Gson().fromJson(complaintReasonsListResponseString, ComplaintReasonsListResponse.class);
    }
}
