package com.apollo.epos.activity.paytmwirelessdevice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmRequestRequest implements Serializable {

    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("storeTerminalId")
    @Expose
    private String storeTerminalId;
    @SerializedName("requestType")
    @Expose
    private String requestType;
    private final static long serialVersionUID = 1605881885717541628L;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStoreTerminalId() {
        return storeTerminalId;
    }

    public void setStoreTerminalId(String storeTerminalId) {
        this.storeTerminalId = storeTerminalId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

}