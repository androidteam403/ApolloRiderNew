package com.apollo.epos.activity.orderdelivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderStatusHitoryListResponse {

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

    public class AaFl {
        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("other")
        @Expose
        private Other__1 other;
        @SerializedName("icon")
        @Expose
        private Object icon;

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
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

    public class CreatedId {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("first_name")
        @Expose
        private String firstName;

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

    }

    public class Order {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("aa_fl")
        @Expose
        private AaFl aaFl;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public AaFl getAaFl() {
            return aaFl;
        }

        public void setAaFl(AaFl aaFl) {
            this.aaFl = aaFl;
        }

    }

    public class OrderStatus {

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
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
            this.color = color;
        }

    }

    public class Row {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("order_status")
        @Expose
        private OrderStatus orderStatus;
//        @SerializedName("is_deleted")
//        @Expose
//        private Boolean isDeleted;
        @SerializedName("created_id")
        @Expose
        private CreatedId createdId;
        @SerializedName("order")
        @Expose
        private Order order;
        @SerializedName("created_time")
        @Expose
        private String createdTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

//        public Boolean getIsDeleted() {
//            return isDeleted;
//        }
//
//        public void setIsDeleted(Boolean isDeleted) {
//            this.isDeleted = isDeleted;
//        }

        public CreatedId getCreatedId() {
            return createdId;
        }

        public void setCreatedId(CreatedId createdId) {
            this.createdId = createdId;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }
}