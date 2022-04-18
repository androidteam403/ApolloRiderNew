package com.apollo.epos.fragment.complaints.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplaintsResponse {

    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("zcServerDateTime")
    @Expose
    private String zcServerDateTime;
    @SerializedName("zcServerIp")
    @Expose
    private String zcServerIp;
    @SerializedName("zcServerHost")
    @Expose
    private String zcServerHost;

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

    public String getZcServerDateTime() {
        return zcServerDateTime;
    }

    public void setZcServerDateTime(String zcServerDateTime) {
        this.zcServerDateTime = zcServerDateTime;
    }

    public String getZcServerIp() {
        return zcServerIp;
    }

    public void setZcServerIp(String zcServerIp) {
        this.zcServerIp = zcServerIp;
    }

    public String getZcServerHost() {
        return zcServerHost;
    }

    public void setZcServerHost(String zcServerHost) {
        this.zcServerHost = zcServerHost;
    }

    public class CreatedId {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("last_name")
        @Expose
        private String lastName;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

    public class Data {

        @SerializedName("listData")
        @Expose
        private ListData listData;

        public ListData getListData() {
            return listData;
        }

        public void setListData(ListData listData) {
            this.listData = listData;
        }

    }

    public class ListData {

        @SerializedName("records")
        @Expose
        private String records;
        @SerializedName("select")
        @Expose
        private Boolean select;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("page")
        @Expose
        private Integer page;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;
        @SerializedName("zc_extra")
        @Expose
        private Object zcExtra;
        @SerializedName("pivotData")
        @Expose
        private Object pivotData;
        @SerializedName("aggregation")
        @Expose
        private Object aggregation;
        @SerializedName("size")
        @Expose
        private Integer size;

        public String getRecords() {
            return records;
        }

        public void setRecords(String records) {
            this.records = records;
        }

        public Boolean getSelect() {
            return select;
        }

        public void setSelect(Boolean select) {
            this.select = select;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

        public Object getZcExtra() {
            return zcExtra;
        }

        public void setZcExtra(Object zcExtra) {
            this.zcExtra = zcExtra;
        }

        public Object getPivotData() {
            return pivotData;
        }

        public void setPivotData(Object pivotData) {
            this.pivotData = pivotData;
        }

        public Object getAggregation() {
            return aggregation;
        }

        public void setAggregation(Object aggregation) {
            this.aggregation = aggregation;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

    }

    public class Other {

        @SerializedName("color")
        @Expose
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

    }

    public class Other__1 {

        @SerializedName("color")
        @Expose
        private String color;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

    }

    public class Reason {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Other getOther() {
            return other;
        }

        public void setOther(Other other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Row {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("comments")
        @Expose
        private String comments;
        @SerializedName("reason")
        @Expose
        private Reason reason;
        @SerializedName("status")
        @Expose
        private Status status;
        @SerializedName("created_id")
        @Expose
        private CreatedId createdId;

        @SerializedName("complaint_no")
        @Expose
        private String complaintNo;
        @SerializedName("created_time")
        @Expose
        private String createdTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public Reason getReason() {
            return reason;
        }

        public void setReason(Reason reason) {
            this.reason = reason;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public CreatedId getCreatedId() {
            return createdId;
        }

        public void setCreatedId(CreatedId createdId) {
            this.createdId = createdId;
        }

        public String getComplaintNo() {
            return complaintNo;
        }

        public void setComplaintNo(String complaintNo) {
            this.complaintNo = complaintNo;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }

    public class Status {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__1 other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Other__1 getOther() {
            return other;
        }

        public void setOther(Other__1 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }
}
