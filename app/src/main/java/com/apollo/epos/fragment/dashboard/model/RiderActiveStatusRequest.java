package com.apollo.epos.fragment.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RiderActiveStatusRequest {

    @SerializedName("user_add_info")
    @Expose
    private UserAddInfo userAddInfo;
    @SerializedName("uid")
    @Expose
    private String uid;

    public UserAddInfo getUserAddInfo() {
        return userAddInfo;
    }

    public void setUserAddInfo(UserAddInfo userAddInfo) {
        this.userAddInfo = userAddInfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static class AvailableStatus {

        @SerializedName("uid")
        @Expose
        private String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

    }

    public static class UserAddInfo {

        @SerializedName("available_status")
        @Expose
        private AvailableStatus availableStatus;

        public AvailableStatus getAvailableStatus() {
            return availableStatus;
        }

        public void setAvailableStatus(AvailableStatus availableStatus) {
            this.availableStatus = availableStatus;
        }

    }
}
