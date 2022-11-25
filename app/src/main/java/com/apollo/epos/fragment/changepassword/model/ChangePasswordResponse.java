package com.apollo.epos.fragment.changepassword.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChangePasswordResponse {

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
        @SerializedName("isExecSuccess")
        @Expose
        private Boolean isExecSuccess;
        @SerializedName("errors")
        @Expose
        private List<Error> errors = null;


        public List<Error> getErrors() {
            return errors;
        }

        public void setErrors(List<Error> errors) {
            this.errors = errors;
        }

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

        public Boolean getIsExecSuccess() {
            return isExecSuccess;
        }

        public void setIsExecSuccess(Boolean isExecSuccess) {
            this.isExecSuccess = isExecSuccess;
        }
    }

    public class Error {

        @SerializedName("msg")
        @Expose
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

