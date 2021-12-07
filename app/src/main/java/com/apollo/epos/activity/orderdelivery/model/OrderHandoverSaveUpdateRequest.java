package com.apollo.epos.activity.orderdelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHandoverSaveUpdateRequest {

    @SerializedName("handover_to")
    @Expose
    private String handoverTo;
    @SerializedName("signature")
    @Expose
    private Object signature;
    @SerializedName("order")
    @Expose
    private Order order;

    public String getHandoverTo() {
        return handoverTo;
    }

    public void setHandoverTo(String handoverTo) {
        this.handoverTo = handoverTo;
    }

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class Order {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }
    }

}