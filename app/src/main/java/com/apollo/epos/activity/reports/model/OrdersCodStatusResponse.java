package com.apollo.epos.activity.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class OrdersCodStatusResponse {

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

    public class OrderPayment {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("amount")
        @Expose
        private Double amount;
        @SerializedName("settled")
        @Expose
        private Settled settled;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Settled getSettled() {
            return settled;
        }

        public void setSettled(Settled settled) {
            this.settled = settled;
        }

    }

    public class OrderRider {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("rider")
        @Expose
        private Rider rider;
        @SerializedName("delivered_on")
        @Expose
        private String deliveredOn;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Rider getRider() {
            return rider;
        }

        public void setRider(Rider rider) {
            this.rider = rider;
        }

        public String getDeliveredOn() {
            return deliveredOn;
        }

        public void setDeliveredOn(String deliveredOn) {
            this.deliveredOn = deliveredOn;
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

    public class Rider {

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

    public class Row {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("awb_number")
        @Expose
        private String awbNumber;
        @SerializedName("order_rider")
        @Expose
        private OrderRider orderRider;
        @SerializedName("order_payment")
        @Expose
        private OrderPayment orderPayment;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;

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

        public OrderRider getOrderRider() {
            return orderRider;
        }

        public void setOrderRider(OrderRider orderRider) {
            this.orderRider = orderRider;
        }

        public OrderPayment getOrderPayment() {
            return orderPayment;
        }

        public void setOrderPayment(OrderPayment orderPayment) {
            this.orderPayment = orderPayment;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

    }

    public class Settled {

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
}
