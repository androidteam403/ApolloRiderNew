package com.apollo.epos.activity.trackmap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderEndJourneyUpdateResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public class Data {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("isUpdate")
        @Expose
        private Boolean isUpdate;

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

    }

}
