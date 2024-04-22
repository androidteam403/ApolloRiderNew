package com.apollo.epos.activity.paytmwirelessdevice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaytmCheckStatusResponse implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("paytmTransactionId")
    @Expose
    private String paytmTransactionId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("paymentMethod")
    @Expose
    private Object paymentMethod;
    private final static long serialVersionUID = -5656645011309510497L;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getPaytmTransactionId() {
        return paytmTransactionId;
    }

    public void setPaytmTransactionId(String paytmTransactionId) {
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