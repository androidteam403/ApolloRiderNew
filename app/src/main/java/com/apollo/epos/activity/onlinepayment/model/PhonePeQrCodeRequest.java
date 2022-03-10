package com.apollo.epos.activity.onlinepayment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhonePeQrCodeRequest {

    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("expiresIn")
    @Expose
    private Integer expiresIn;
    @SerializedName("RequestType")
    @Expose
    private String requestType;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

}
