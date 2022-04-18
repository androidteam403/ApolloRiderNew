package com.apollo.epos.activity.orderdelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSaveUpdateStausRequest {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("order_status")
    @Expose
    private OrderStatus orderStatus;

    @SerializedName("order_payment")
    @Expose
    private OrderPayment orderPayment;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("delivery_failure_reason")
    @Expose
    private DeliveryFailureReason deliveryFailureReason;

    @SerializedName("paymentSubType")
    @Expose
    private String paymentSubType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DeliveryFailureReason getDeliveryFailureReason() {
        return deliveryFailureReason;
    }

    public void setDeliveryFailureReason(DeliveryFailureReason deliveryFailureReason) {
        this.deliveryFailureReason = deliveryFailureReason;
    }

    public String getPaymentSubType() {
        return paymentSubType;
    }

    public void setPaymentSubType(String paymentSubType) {
        this.paymentSubType = paymentSubType;
    }

    public static class OrderStatus {

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

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public static class DeliveryFailureReason {
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


        @SerializedName("settlement_date")
        @Expose
        private String settlementDate;
        @SerializedName("txn_date")
        @Expose
        private String txnDate;
        @SerializedName("txn_id")
        @Expose
        private String txnId;
        @SerializedName("type")
        @Expose
        private OrderPaymentUpdateRequest.Type type;


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

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public OrderPaymentUpdateRequest.Type getType() {
            return type;
        }

        public void setType(OrderPaymentUpdateRequest.Type type) {
            this.type = type;
        }

    }

}
