package com.apollo.epos.activity.paytmwirelessdevice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmRequestResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("paytmTransactionId")
    @Expose
    private Object paytmTransactionId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("paymentMethod")
    @Expose
    private Object paymentMethod;
    private final static long serialVersionUID = -2567827234982478819L;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getPaytmTransactionId() {
        return paytmTransactionId;
    }

    public void setPaytmTransactionId(Object paytmTransactionId) {
        this.paytmTransactionId = paytmTransactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Object paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}