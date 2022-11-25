package com.apollo.epos.activity.trackmap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStartJourneyUpdateRequest {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("order_rider")
    @Expose
    private OrderRider orderRider;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public OrderRider getOrderRider() {
        return orderRider;
    }

    public void setOrderRider(OrderRider orderRider) {
        this.orderRider = orderRider;
    }

    public static class OrderRider {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("actual_distance")
        @Expose
        private String actualDistance;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getActualDistance() {
            return actualDistance;
        }

        public void setActualDistance(String actualDistance) {
            this.actualDistance = actualDistance;
        }

    }

}