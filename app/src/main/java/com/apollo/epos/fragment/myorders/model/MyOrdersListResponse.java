package com.apollo.epos.fragment.myorders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersListResponse {

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

    public class DelLocType {

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

    public class OrderRider {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("distance_travelled")
        @Expose
        private Object distanceTravelled;
        @SerializedName("rider")
        @Expose
        private Rider rider;
        @SerializedName("delivered_on")
        @Expose
        private Object deliveredOn;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Object getDistanceTravelled() {
            return distanceTravelled;
        }

        public void setDistanceTravelled(Object distanceTravelled) {
            this.distanceTravelled = distanceTravelled;
        }

        public Rider getRider() {
            return rider;
        }

        public void setRider(Rider rider) {
            this.rider = rider;
        }

        public Object getDeliveredOn() {
            return deliveredOn;
        }

        public void setDeliveredOn(Object deliveredOn) {
            this.deliveredOn = deliveredOn;
        }

    }

    public class OrderSh {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("order_status")
        @Expose
        private OrderStatus__1 orderStatus;
        @SerializedName("created_time")
        @Expose
        private String createdTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public OrderStatus__1 getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(OrderStatus__1 orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }

    public class OrderState {

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

    public class OrderStatus {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__2 other;
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

        public Other__2 getOther() {
            return other;
        }

        public void setOther(Other__2 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class OrderStatus__1 {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__4 other;
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

        public Other__4 getOther() {
            return other;
        }

        public void setOther(Other__4 other) {
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

    public class Other__2 {

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

    public class Other__3 {

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

    public class Other__4 {

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

    public class PaymentType {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__3 other;
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

        public Other__3 getOther() {
            return other;
        }

        public void setOther(Other__3 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Rider {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }

    }

    public class Row {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("crate_amount")
        @Expose
        private Double crateAmount;

        @SerializedName("del_acc_name")
        @Expose
        private String delAccName;
        @SerializedName("del_add_id")
        @Expose
        private String delAddId;
        @SerializedName("deliver_apartment")
        @Expose
        private String deliverApartment;
        @SerializedName("deliver_city")
        @Expose
        private String deliverCity;
        @SerializedName("deliver_country")
        @Expose
        private String deliverCountry;
        @SerializedName("deliver_landmark")
        @Expose
        private String deliverLandmark;
        @SerializedName("deliver_latitude")
        @Expose
        private Double deliverLatitude;
        @SerializedName("deliver_locality")
        @Expose
        private String deliverLocality;
        @SerializedName("del_loc_type")
        @Expose
        private DelLocType delLocType;
        @SerializedName("deliver_longitude")
        @Expose
        private Double deliverLongitude;
        @SerializedName("del_pincode")
        @Expose
        private Integer delPincode;
        @SerializedName("deliver_state")
        @Expose
        private String deliverState;
        @SerializedName("deliver_street_name")
        @Expose
        private String deliverStreetName;
        @SerializedName("deliver_type")
        @Expose
        private String deliverType;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("order_state")
        @Expose
        private OrderState orderState;
        @SerializedName("order_status")
        @Expose
        private OrderStatus orderStatus;
        @SerializedName("payment_type")
        @Expose
        private PaymentType paymentType;
        @SerializedName("pickup_acc_name")
        @Expose
        private String pickupAccName;
        @SerializedName("pickup_add_id")
        @Expose
        private String pickupAddId;
        @SerializedName("pickup_apt")
        @Expose
        private String pickupApt;
        @SerializedName("pickup_city")
        @Expose
        private String pickupCity;
        @SerializedName("pickup_country")
        @Expose
        private String pickupCountry;
        @SerializedName("pickup_lndmrk")
        @Expose
        private String pickupLndmrk;
        @SerializedName("pickup_latitude")
        @Expose
        private Double pickupLatitude;
        @SerializedName("pickup_locality")
        @Expose
        private String pickupLocality;
        @SerializedName("pickup_longitude")
        @Expose
        private Double pickupLongitude;
        @SerializedName("pickup_pincode")
        @Expose
        private Integer pickupPincode;
        @SerializedName("pickup_state")
        @Expose
        private String pickupState;
        @SerializedName("pickup_street_name")
        @Expose
        private String pickupStreetName;
        @SerializedName("return_acc_name")
        @Expose
        private String returnAccName;
        @SerializedName("return_add_id")
        @Expose
        private String returnAddId;
        @SerializedName("return_apartment")
        @Expose
        private String returnApartment;
        @SerializedName("return_city")
        @Expose
        private String returnCity;
        @SerializedName("return_country")
        @Expose
        private String returnCountry;
        @SerializedName("return_landmark")
        @Expose
        private String returnLandmark;
        @SerializedName("return_locality")
        @Expose
        private String returnLocality;
        @SerializedName("return_pincode")
        @Expose
        private Integer returnPincode;
        @SerializedName("return_state")
        @Expose
        private String returnState;
        @SerializedName("return_street_name")
        @Expose
        private String returnStreetName;
        @SerializedName("order_rider")
        @Expose
        private OrderRider orderRider;
        @SerializedName("failure_attempts")
        @Expose
        private int failureAttempts;
        @SerializedName("pickup_st_windo")
        @Expose
        private String pickupStWindo;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("del_st_windo")
        @Expose
        private String delStWindo;
        @SerializedName("del_et_windo")
        @Expose
        private String delEtWindo;

        @SerializedName("deliver_branch")
        @Expose
        private String deliverBranch;

        @SerializedName("pickup_branch")
        @Expose
        private String pickupBranch;

        @SerializedName("pickup_et_windo")
        @Expose
        private String pickupEtWindo;
        @SerializedName("order_sh")
        @Expose
        private List<OrderSh> orderSh = null;

        public int getFailureAttempts() {
            return failureAttempts;
        }

        public void setFailureAttempts(int failureAttempts) {
            this.failureAttempts = failureAttempts;
        }

        public String getDelAccName() {
            return delAccName;
        }

        public void setDelAccName(String delAccName) {
            this.delAccName = delAccName;
        }

        public String getPickupAccName() {
            return pickupAccName;
        }

        public void setPickupAccName(String pickupAccName) {
            this.pickupAccName = pickupAccName;
        }

        public String getReturnAccName() {
            return returnAccName;
        }

        public void setReturnAccName(String returnAccName) {
            this.returnAccName = returnAccName;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Double getCrateAmount() {
            return crateAmount;
        }

        public void setCrateAmount(Double crateAmount) {
            this.crateAmount = crateAmount;
        }

        public String getDelAddId() {
            return delAddId;
        }

        public void setDelAddId(String delAddId) {
            this.delAddId = delAddId;
        }

        public String getDeliverApartment() {
            return deliverApartment;
        }

        public void setDeliverApartment(String deliverApartment) {
            this.deliverApartment = deliverApartment;
        }

        public String getDeliverCity() {
            return deliverCity;
        }

        public void setDeliverCity(String deliverCity) {
            this.deliverCity = deliverCity;
        }

        public String getDeliverCountry() {
            return deliverCountry;
        }

        public void setDeliverCountry(String deliverCountry) {
            this.deliverCountry = deliverCountry;
        }

        public String getDeliverLandmark() {
            return deliverLandmark;
        }

        public void setDeliverLandmark(String deliverLandmark) {
            this.deliverLandmark = deliverLandmark;
        }

        public Double getDeliverLatitude() {
            return deliverLatitude;
        }

        public void setDeliverLatitude(Double deliverLatitude) {
            this.deliverLatitude = deliverLatitude;
        }

        public String getDeliverLocality() {
            return deliverLocality;
        }

        public void setDeliverLocality(String deliverLocality) {
            this.deliverLocality = deliverLocality;
        }

        public DelLocType getDelLocType() {
            return delLocType;
        }

        public void setDelLocType(DelLocType delLocType) {
            this.delLocType = delLocType;
        }

        public Double getDeliverLongitude() {
            return deliverLongitude;
        }

        public void setDeliverLongitude(Double deliverLongitude) {
            this.deliverLongitude = deliverLongitude;
        }

        public Integer getDelPincode() {
            return delPincode;
        }

        public void setDelPincode(Integer delPincode) {
            this.delPincode = delPincode;
        }

        public String getDeliverState() {
            return deliverState;
        }

        public void setDeliverState(String deliverState) {
            this.deliverState = deliverState;
        }

        public String getDeliverStreetName() {
            return deliverStreetName;
        }

        public void setDeliverStreetName(String deliverStreetName) {
            this.deliverStreetName = deliverStreetName;
        }

        public String getDeliverType() {
            return deliverType;
        }

        public void setDeliverType(String deliverType) {
            this.deliverType = deliverType;
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

        public PaymentType getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
        }

        public String getPickupAddId() {
            return pickupAddId;
        }

        public void setPickupAddId(String pickupAddId) {
            this.pickupAddId = pickupAddId;
        }

        public String getPickupApt() {
            return pickupApt;
        }

        public void setPickupApt(String pickupApt) {
            this.pickupApt = pickupApt;
        }

        public String getPickupCity() {
            return pickupCity;
        }

        public void setPickupCity(String pickupCity) {
            this.pickupCity = pickupCity;
        }

        public String getPickupCountry() {
            return pickupCountry;
        }

        public void setPickupCountry(String pickupCountry) {
            this.pickupCountry = pickupCountry;
        }

        public String getPickupLndmrk() {
            return pickupLndmrk;
        }

        public void setPickupLndmrk(String pickupLndmrk) {
            this.pickupLndmrk = pickupLndmrk;
        }

        public Double getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(Double pickupLatitude) {
            this.pickupLatitude = pickupLatitude;
        }

        public String getPickupLocality() {
            return pickupLocality;
        }

        public void setPickupLocality(String pickupLocality) {
            this.pickupLocality = pickupLocality;
        }

        public Double getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(Double pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public Integer getPickupPincode() {
            return pickupPincode;
        }

        public void setPickupPincode(Integer pickupPincode) {
            this.pickupPincode = pickupPincode;
        }

        public String getPickupState() {
            return pickupState;
        }

        public void setPickupState(String pickupState) {
            this.pickupState = pickupState;
        }

        public String getPickupStreetName() {
            return pickupStreetName;
        }

        public void setPickupStreetName(String pickupStreetName) {
            this.pickupStreetName = pickupStreetName;
        }

        public String getReturnAddId() {
            return returnAddId;
        }

        public void setReturnAddId(String returnAddId) {
            this.returnAddId = returnAddId;
        }

        public String getReturnApartment() {
            return returnApartment;
        }

        public void setReturnApartment(String returnApartment) {
            this.returnApartment = returnApartment;
        }

        public String getReturnCity() {
            return returnCity;
        }

        public void setReturnCity(String returnCity) {
            this.returnCity = returnCity;
        }

        public String getReturnCountry() {
            return returnCountry;
        }

        public void setReturnCountry(String returnCountry) {
            this.returnCountry = returnCountry;
        }

        public String getReturnLandmark() {
            return returnLandmark;
        }

        public void setReturnLandmark(String returnLandmark) {
            this.returnLandmark = returnLandmark;
        }

        public String getReturnLocality() {
            return returnLocality;
        }

        public void setReturnLocality(String returnLocality) {
            this.returnLocality = returnLocality;
        }

        public Integer getReturnPincode() {
            return returnPincode;
        }

        public void setReturnPincode(Integer returnPincode) {
            this.returnPincode = returnPincode;
        }

        public String getReturnState() {
            return returnState;
        }

        public void setReturnState(String returnState) {
            this.returnState = returnState;
        }

        public String getReturnStreetName() {
            return returnStreetName;
        }

        public void setReturnStreetName(String returnStreetName) {
            this.returnStreetName = returnStreetName;
        }

        public OrderRider getOrderRider() {
            return orderRider;
        }

        public void setOrderRider(OrderRider orderRider) {
            this.orderRider = orderRider;
        }

        public String getPickupStWindo() {
            return pickupStWindo;
        }

        public void setPickupStWindo(String pickupStWindo) {
            this.pickupStWindo = pickupStWindo;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getDelStWindo() {
            return delStWindo;
        }

        public void setDelStWindo(String delStWindo) {
            this.delStWindo = delStWindo;
        }

        public String getDelEtWindo() {
            return delEtWindo;
        }

        public void setDelEtWindo(String delEtWindo) {
            this.delEtWindo = delEtWindo;
        }

        public String getDeliverBranch() {
            return deliverBranch;
        }

        public void setDeliverBranch(String deliverBranch) {
            this.deliverBranch = deliverBranch;
        }

        public String getPickupBranch() {
            return pickupBranch;
        }

        public void setPickupBranch(String pickupBranch) {
            this.pickupBranch = pickupBranch;
        }

        public String getPickupEtWindo() {
            return pickupEtWindo;
        }

        public void setPickupEtWindo(String pickupEtWindo) {
            this.pickupEtWindo = pickupEtWindo;
        }

        public List<OrderSh> getOrderSh() {
            return orderSh;
        }

        public void setOrderSh(List<OrderSh> orderSh) {
            this.orderSh = orderSh;
        }

    }
}