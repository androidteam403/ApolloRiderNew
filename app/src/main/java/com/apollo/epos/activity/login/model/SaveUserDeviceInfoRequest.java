package com.apollo.epos.activity.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveUserDeviceInfoRequest {

    @SerializedName("device_info")
    @Expose
    private DeviceInfo deviceInfo;
    @SerializedName("firebase_id")
    @Expose
    private String firebaseId;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public static class DeviceInfo {
        @SerializedName("mac_id")
        @Expose
        private String mac_id;

        @SerializedName("device_name")
        @Expose
        private String device_name;

        @SerializedName("manufacture")
        @Expose
        private String manufacture;

        @SerializedName("brand")
        @Expose
        private String brand;

        @SerializedName("user")
        @Expose
        private String user;

        @SerializedName("version_sdk")
        @Expose
        private String version_sdk;

        @SerializedName("fingerprint")
        @Expose
        private String fingerprint;

        @SerializedName("app_version_code")
        @Expose
        private String app_version_code;

        @SerializedName("app_version_name")
        @Expose
        private String app_version_name;

        public String getMac_id() {
            return mac_id;
        }

        public void setMac_id(String mac_id) {
            this.mac_id = mac_id;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getManufacture() {
            return manufacture;
        }

        public void setManufacture(String manufacture) {
            this.manufacture = manufacture;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getVersion_sdk() {
            return version_sdk;
        }

        public void setVersion_sdk(String version_sdk) {
            this.version_sdk = version_sdk;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public String getApp_version_code() {
            return app_version_code;
        }

        public void setApp_version_code(String app_version_code) {
            this.app_version_code = app_version_code;
        }

        public String getApp_version_name() {
            return app_version_name;
        }

        public void setApp_version_name(String app_version_name) {
            this.app_version_name = app_version_name;
        }
    }
}