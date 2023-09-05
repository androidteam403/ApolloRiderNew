package com.apollo.epos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForceUpdateResponse implements Serializable {
    @SerializedName("android")
    @Expose
    private Android android;

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }

    public class Android implements Serializable {
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("build_no")
        @Expose
        private Integer buildNo;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("force_update")
        @Expose
        private String forceUpdate;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("app_availability")
        @Expose
        private String appAvailability;
        @SerializedName("message_maintance")
        @Expose
        private String messageMaintance;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Integer getBuildNo() {
            return buildNo;
        }

        public void setBuildNo(Integer buildNo) {
            this.buildNo = buildNo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(String forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAppAvailability() {
            return appAvailability;
        }

        public void setAppAvailability(String appAvailability) {
            this.appAvailability = appAvailability;
        }

        public String getMessageMaintance() {
            return messageMaintance;
        }

        public void setMessageMaintance(String messageMaintance) {
            this.messageMaintance = messageMaintance;
        }

    }
}
