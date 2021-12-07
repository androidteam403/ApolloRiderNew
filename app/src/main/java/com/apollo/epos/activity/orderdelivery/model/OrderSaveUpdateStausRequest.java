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
    private String deliveryFailureReason;

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

    public String getDeliveryFailureReason() {
        return deliveryFailureReason;
    }

    public void setDeliveryFailureReason(String deliveryFailureReason) {
        this.deliveryFailureReason = deliveryFailureReason;
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

}
