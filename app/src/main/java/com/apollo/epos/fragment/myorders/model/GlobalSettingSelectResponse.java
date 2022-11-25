package com.apollo.epos.fragment.myorders.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalSettingSelectResponse {

    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("zcServerDateTime")
    @Expose
    private String zcServerDateTime;
    @SerializedName("zcServerIp")
    @Expose
    private String zcServerIp;
    @SerializedName("zcServerHost")
    @Expose
    private String zcServerHost;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getZcServerDateTime() {
        return zcServerDateTime;
    }

    public void setZcServerDateTime(String zcServerDateTime) {
        this.zcServerDateTime = zcServerDateTime;
    }

    public String getZcServerIp() {
        return zcServerIp;
    }

    public void setZcServerIp(String zcServerIp) {
        this.zcServerIp = zcServerIp;
    }

    public String getZcServerHost() {
        return zcServerHost;
    }

    public void setZcServerHost(String zcServerHost) {
        this.zcServerHost = zcServerHost;
    }

    public class AutoAlctnAtmpt {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Other getOther() {
            return other;
        }

        public void setOther(Other other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class AutoAlctnFreq {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__1 other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Other__1 getOther() {
            return other;
        }

        public void setOther(Other__1 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Data {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("auto_alctn")
        @Expose
        private Boolean autoAlctn;
        @SerializedName("otp_branch_hndovr")
        @Expose
        private Boolean otpBranchHndovr;
        @SerializedName("otp_cust_pickup")
        @Expose
        private Boolean otpCustPickup;
        @SerializedName("otp_delivery")
        @Expose
        private Boolean otpDelivery;
        @SerializedName("otp_pickup")
        @Expose
        private Boolean otpPickup;
        @SerializedName("auto_alctn_atmpt")
        @Expose
        private AutoAlctnAtmpt autoAlctnAtmpt;
        @SerializedName("auto_alctn_freq")
        @Expose
        private AutoAlctnFreq autoAlctnFreq;
        @SerializedName("deliver_attempts")
        @Expose
        private DeliverAttempts deliverAttempts;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Boolean getAutoAlctn() {
            return autoAlctn;
        }

        public void setAutoAlctn(Boolean autoAlctn) {
            this.autoAlctn = autoAlctn;
        }

        public Boolean getOtpBranchHndovr() {
            return otpBranchHndovr;
        }

        public void setOtpBranchHndovr(Boolean otpBranchHndovr) {
            this.otpBranchHndovr = otpBranchHndovr;
        }

        public Boolean getOtpCustPickup() {
            return otpCustPickup;
        }

        public void setOtpCustPickup(Boolean otpCustPickup) {
            this.otpCustPickup = otpCustPickup;
        }

        public Boolean getOtpDelivery() {
            return otpDelivery;
        }

        public void setOtpDelivery(Boolean otpDelivery) {
            this.otpDelivery = otpDelivery;
        }

        public Boolean getOtpPickup() {
            return otpPickup;
        }

        public void setOtpPickup(Boolean otpPickup) {
            this.otpPickup = otpPickup;
        }

        public AutoAlctnAtmpt getAutoAlctnAtmpt() {
            return autoAlctnAtmpt;
        }

        public void setAutoAlctnAtmpt(AutoAlctnAtmpt autoAlctnAtmpt) {
            this.autoAlctnAtmpt = autoAlctnAtmpt;
        }

        public AutoAlctnFreq getAutoAlctnFreq() {
            return autoAlctnFreq;
        }

        public void setAutoAlctnFreq(AutoAlctnFreq autoAlctnFreq) {
            this.autoAlctnFreq = autoAlctnFreq;
        }

        public DeliverAttempts getDeliverAttempts() {
            return deliverAttempts;
        }

        public void setDeliverAttempts(DeliverAttempts deliverAttempts) {
            this.deliverAttempts = deliverAttempts;
        }

    }

    public class DeliverAttempts {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__2 other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Other__2 getOther() {
            return other;
        }

        public void setOther(Other__2 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Other {

        @SerializedName("color")
        @Expose
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

    }

    public class Other__1 {

        @SerializedName("color")
        @Expose
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

    }

    public class Other__2 {

        @SerializedName("color")
        @Expose
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

    }
}