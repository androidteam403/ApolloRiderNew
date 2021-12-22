package com.apollo.epos.activity.trackmap.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderEndJourneyUpdateRequest {

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
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("distance_travelled")
        @Expose
        private String distanceTravelled;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDistanceTravelled() {
            return distanceTravelled;
        }

        public void setDistanceTravelled(String distanceTravelled) {
            this.distanceTravelled = distanceTravelled;
        }

    }
}
