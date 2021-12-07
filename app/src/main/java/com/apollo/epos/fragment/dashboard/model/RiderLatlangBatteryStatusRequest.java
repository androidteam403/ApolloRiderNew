package com.apollo.epos.fragment.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiderLatlangBatteryStatusRequest {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("user_add_info")
    @Expose
    private UserAddInfo userAddInfo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserAddInfo getUserAddInfo() {
        return userAddInfo;
    }

    public void setUserAddInfo(UserAddInfo userAddInfo) {
        this.userAddInfo = userAddInfo;
    }

    public static class UserAddInfo {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("battery_status")
        @Expose
        private String batteryStatus;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;

        @SerializedName("last_active")
        @Expose
        private String lastActive;


        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBatteryStatus() {
            return batteryStatus;
        }

        public void setBatteryStatus(String batteryStatus) {
            this.batteryStatus = batteryStatus;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getLastActive() {
            return lastActive;
        }

        public void setLastActive(String lastActive) {
            this.lastActive = lastActive;
        }
    }
}
