package com.apollo.epos.activity.onlinepayment.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhonePeQrCodeResponse {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("QrCode")
    @Expose
    private String qrCode;
    @SerializedName("providerReferenceId")
    @Expose
    private String providerReferenceId;
    @SerializedName("StatusCode")
    @Expose
    private Object statusCode;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getProviderReferenceId() {
        return providerReferenceId;
    }

    public void setProviderReferenceId(String providerReferenceId) {
        this.providerReferenceId = providerReferenceId;
    }

    public Object getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Object statusCode) {
        this.statusCode = statusCode;
    }

}
