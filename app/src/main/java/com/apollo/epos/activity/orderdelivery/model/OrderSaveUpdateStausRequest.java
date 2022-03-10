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
}
