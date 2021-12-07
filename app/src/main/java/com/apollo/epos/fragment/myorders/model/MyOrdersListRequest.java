package com.apollo.epos.fragment.myorders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersListRequest {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("records")
    @Expose
    private String records;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("total")
    @Expose
    private String total;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    public static class DelMedimUsrnam {

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

    public class OrderState {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

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

    }
    public class OrderStatus {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;

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

    }
    public static class Row {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("awb_number")
        @Expose
        private String awbNumber;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("order_state")
        @Expose
        private OrderState orderState;
        @SerializedName("order_status")
        @Expose
        private OrderStatus orderStatus;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("del_medim_usrnam")
        @Expose
        private DelMedimUsrnam delMedimUsrnam;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAwbNumber() {
            return awbNumber;
        }

        public void setAwbNumber(String awbNumber) {
            this.awbNumber = awbNumber;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public OrderState getOrderState() {
            return orderState;
        }

        public void setOrderState(OrderState orderState) {
            this.orderState = orderState;
        }

        public OrderStatus getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public DelMedimUsrnam getDelMedimUsrnam() {
            return delMedimUsrnam;
        }

        public void setDelMedimUsrnam(DelMedimUsrnam delMedimUsrnam) {
            this.delMedimUsrnam = delMedimUsrnam;
        }

    }
}