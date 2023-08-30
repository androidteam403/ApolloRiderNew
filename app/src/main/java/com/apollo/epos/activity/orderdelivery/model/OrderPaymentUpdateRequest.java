package com.apollo.epos.activity.orderdelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderPaymentUpdateRequest {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("order_payment")
    @Expose
    private OrderPayment orderPayment;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public static class DeviceType {

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

    public static class OrderPayment {

        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("device_type")
        @Expose
        private DeviceType deviceType;
        @SerializedName("settlement_date")
        @Expose
        private String settlementDate;
        @SerializedName("txn_date")
        @Expose
        private String txnDate;
        @SerializedName("payment_mode")
        @Expose
        private PaymentMode paymentMode;
        @SerializedName("txn_id")
        @Expose
        private String txnId;
        @SerializedName("type")
        @Expose
        private Type type;

        @SerializedName("live_tracking_url")
        @Expose
        private String liveTrackingUrl;
        @SerializedName("PAYMENTMODE")
        @Expose
        private String PAYMENTMODE;

        @SerializedName("vendorid")
        @Expose
        private String vendorid;

        @SerializedName("settlementsite")
        @Expose
        private String settlementsite;

        @SerializedName("wallet_req_txn_id")
        @Expose
        private String walletReqTxnId;

        @SerializedName("resp_id")
        @Expose
        private String respId;

        @SerializedName("wallet_resp")
        @Expose
        private String walletResp;


        @SerializedName("request_time")
        @Expose
        private String requestTime;


        @SerializedName("response_time")
        @Expose
        private String responseTime;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public DeviceType getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(DeviceType deviceType) {
            this.deviceType = deviceType;
        }

        public String getSettlementDate() {
            return settlementDate;
        }

        public void setSettlementDate(String settlementDate) {
            this.settlementDate = settlementDate;
        }

        public String getTxnDate() {
            return txnDate;
        }

        public void setTxnDate(String txnDate) {
            this.txnDate = txnDate;
        }

        public PaymentMode getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(PaymentMode paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getLiveTrackingUrl() {
            return liveTrackingUrl;
        }

        public void setLiveTrackingUrl(String liveTrackingUrl) {
            this.liveTrackingUrl = liveTrackingUrl;
        }

        public String getPAYMENTMODE() {
            return PAYMENTMODE;
        }

        public void setPAYMENTMODE(String PAYMENTMODE) {
            this.PAYMENTMODE = PAYMENTMODE;
        }

        public String getVendorid() {
            return vendorid;
        }

        public void setVendorid(String vendorid) {
            this.vendorid = vendorid;
        }

        public String getSettlementsite() {
            return settlementsite;
        }

        public void setSettlementsite(String settlementsite) {
            this.settlementsite = settlementsite;
        }

        public String getWalletReqTxnId() {
            return walletReqTxnId;
        }

        public void setWalletReqTxnId(String walletReqTxnId) {
            this.walletReqTxnId = walletReqTxnId;
        }

        public String getRespId() {
            return respId;
        }

        public void setRespId(String respId) {
            this.respId = respId;
        }

        public String getWalletResp() {
            return walletResp;
        }

        public void setWalletResp(String walletResp) {
            this.walletResp = walletResp;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(String responseTime) {
            this.responseTime = responseTime;
        }
    }

    public static class PaymentMode {
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

    public static class Type {

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
}
