package com.apollo.epos.db;
/*
 * Created on : oct 29, 2021.
 * Author : NAVEEN.M
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.apollo.epos.model.GetRiderProfileResponse;
import com.google.gson.Gson;

public class SessionManager {
    SharedPreferences preferences;

    //Preff keys
    private static final String PREF_KEY_LOGIN_TOKEN = "PREF_KEY_LOGIN_TOKEN";
    private static final String PREF_KEY_RIDER_PROFILE_DETAILS = "PREF_KEY_RIDER_PROFILE_DETAILS";
    private static final String PREF_KEY_USER_ICON = "PREF_KEY_USER_ICON";
    private static final String PREF_KEY_RIDER_ACTIVE_STATUS = "PREF_KEY_RIDER_ACTIVE_STATUS";
    private static final String PREF_KEY_NOTIFICATION_STATUS = "PREF_KEY_NOTIFICATION_STATUS";
    private static final String PREF_KEY_NOTIFICATION_ARRIVED_TIME = "PREF_KEY_NOTIFICATION_ARRIVED_TIME";

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

}
