package com.apollo.epos.fragment.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RiderDashboardCountResponse {

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

    public class Count {

        @SerializedName("distance_travelled")
        @Expose
        private Object distanceTravelled;
        @SerializedName("cod_pending")
        @Expose
        private Double codPending;
        @SerializedName("cod_received")
        @Expose
        private Double codReceived;
        @SerializedName("cancelled_orders")
        @Expose
        private Integer cancelledOrders;
        @SerializedName("delivered_orders")
        @Expose
        private Integer deliveredOrders;
        @SerializedName("total_orders")
        @Expose
        private Integer totalOrders;

        public Object getDistanceTravelled() {
            return distanceTravelled;
        }

        public void setDistanceTravelled(Object distanceTravelled) {
            this.distanceTravelled = distanceTravelled;
        }

        public Double getCodPending() {
            return codPending;
        }

        public void setCodPending(Double codPending) {
            this.codPending = codPending;
        }

        public Double getCodReceived() {
            return codReceived;
        }

        public void setCodReceived(Double codReceived) {
            this.codReceived = codReceived;
        }

        public Integer getCancelledOrders() {
            return cancelledOrders;
        }

        public void setCancelledOrders(Integer cancelledOrders) {
            this.cancelledOrders = cancelledOrders;
        }

        public Integer getDeliveredOrders() {
            return deliveredOrders;
        }

        public void setDeliveredOrders(Integer deliveredOrders) {
            this.deliveredOrders = deliveredOrders;
        }

        public Integer getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Integer totalOrders) {
            this.totalOrders = totalOrders;
        }

    }

    public class Data {

        @SerializedName("count")
        @Expose
        private Count count;
        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("user_code")
        @Expose
        private String userCode;

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

    }
}
