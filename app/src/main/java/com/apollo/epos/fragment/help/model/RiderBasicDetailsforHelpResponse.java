package com.apollo.epos.fragment.help.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiderBasicDetailsforHelpResponse {

    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
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
        @SerializedName("user_add_info")
        @Expose
        private UserAddInfo userAddInfo;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public UserAddInfo getUserAddInfo() {
            return userAddInfo;
        }

        public void setUserAddInfo(UserAddInfo userAddInfo) {
            this.userAddInfo = userAddInfo;
        }

    }

    public class Store {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("store_manager")
        @Expose
        private StoreManager storeManager;
        @SerializedName("email")
        @Expose
        private String email;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public StoreManager getStoreManager() {
            return storeManager;
        }

        public void setStoreManager(StoreManager storeManager) {
            this.storeManager = storeManager;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    public class StoreManager {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("phone")
        @Expose
        private String phone;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

    }

    public class UserAddInfo {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("store")
        @Expose
        private Store store;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }

    }

}