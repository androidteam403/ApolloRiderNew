package com.apollo.epos.activity.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {
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

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("addInfo")
        @Expose
        private AddInfo addInfo;

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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public AddInfo getAddInfo() {
            return addInfo;
        }

        public void setAddInfo(AddInfo addInfo) {
            this.addInfo = addInfo;
        }
    }

    public class Pic {

        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("saved")
        @Expose
        private Boolean saved;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contentType")
        @Expose
        private String contentType;
        @SerializedName("dimenesions")
        @Expose
        private Dimenesions dimenesions;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("created_info")
        @Expose
        private CreatedInfo createdInfo;
        @SerializedName("fullPath")
        @Expose
        private String fullPath;

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Boolean getSaved() {
            return saved;
        }

        public void setSaved(Boolean saved) {
            this.saved = saved;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public Dimenesions getDimenesions() {
            return dimenesions;
        }

        public void setDimenesions(Dimenesions dimenesions) {
            this.dimenesions = dimenesions;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public CreatedInfo getCreatedInfo() {
            return createdInfo;
        }

        public void setCreatedInfo(CreatedInfo createdInfo) {
            this.createdInfo = createdInfo;
        }

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

    }

    public class AddInfo {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("middle_name")
        @Expose
        private Object middleName;

        @SerializedName("user_code")
        @Expose
        private String userCode;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;

        @SerializedName("pic")
        @Expose
        private List<Pic> pic = null;


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

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Object getMiddleName() {
            return middleName;
        }

        public void setMiddleName(Object middleName) {
            this.middleName = middleName;
        }


        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }


        public List<Pic> getPic() {
            return pic;
        }

        public void setPic(List<Pic> pic) {
            this.pic = pic;
        }

    }

    public class Dimenesions {

        @SerializedName("200_200")
        @Expose
        private String _200200;
        @SerializedName("200_200_fullPath")
        @Expose
        private String _200200FullPath;

        public String get200200() {
            return _200200;
        }

        public void set200200(String _200200) {
            this._200200 = _200200;
        }

        public String get200200FullPath() {
            return _200200FullPath;
        }

        public void set200200FullPath(String _200200FullPath) {
            this._200200FullPath = _200200FullPath;
        }

    }

    public class CreatedInfo {

        @SerializedName("created_on")
        @Expose
        private Long createdOn;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("app_user_id")
        @Expose
        private String appUserId;
        @SerializedName("user_code")
        @Expose
        private String userCode;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("role_code")
        @Expose
        private String roleCode;
        @SerializedName("role_name")
        @Expose
        private String roleName;

        public Long getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Long createdOn) {
            this.createdOn = createdOn;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAppUserId() {
            return appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

    }
}



































