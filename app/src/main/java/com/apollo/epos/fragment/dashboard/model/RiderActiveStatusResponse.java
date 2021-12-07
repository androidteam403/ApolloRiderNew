package com.apollo.epos.fragment.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RiderActiveStatusResponse {

    @SerializedName("message")
    @Expose
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
    public class Data {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("isUpdate")
        @Expose
        private Boolean isUpdate;
        @SerializedName("zcDebugLogs")
        @Expose
        private ZcDebugLogs zcDebugLogs;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Boolean getIsUpdate() {
            return isUpdate;
        }

        public void setIsUpdate(Boolean isUpdate) {
            this.isUpdate = isUpdate;
        }

        public ZcDebugLogs getZcDebugLogs() {
            return zcDebugLogs;
        }

        public void setZcDebugLogs(ZcDebugLogs zcDebugLogs) {
            this.zcDebugLogs = zcDebugLogs;
        }

    }
    public class _1 {

        @SerializedName("context")
        @Expose
        private String context;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("place")
        @Expose
        private String place;
        @SerializedName("log")
        @Expose
        private String log;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }

    }

    public class ZcDebugLogs {

        @SerializedName("1")
        @Expose
        private List<_1> _1 = null;

        public List<_1> get1() {
            return _1;
        }

        public void set1(List<_1> _1) {
            this._1 = _1;
        }

    }

}
