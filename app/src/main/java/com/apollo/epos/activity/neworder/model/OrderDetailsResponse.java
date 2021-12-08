package com.apollo.epos.activity.neworder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsResponse implements Serializable {

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

    public class AaFl implements Serializable {

        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("other")
        @Expose
        private Other other;
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

    public class CancelAllowed implements Serializable {

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

    public class CreatedId implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private Object lastName;

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

        public Object getLastName() {
            return lastName;
        }

        public void setLastName(Object lastName) {
            this.lastName = lastName;
        }

    }

    public class CreatedInfo implements Serializable {

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

    public class Data implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("aa_fl")
        @Expose
        private AaFl aaFl;
        @SerializedName("awb_number")
        @Expose
        private String awbNumber;
        @SerializedName("branpickup_ver_code")
        @Expose
        private String branpickupVerCode;
        @SerializedName("branreturn_ver_code")
        @Expose
        private String branreturnVerCode;
        @SerializedName("cancel_allowed")
        @Expose
        private CancelAllowed cancelAllowed;
        @SerializedName("client_code")
        @Expose
        private String clientCode;
        @SerializedName("crate_amount")
        @Expose
        private Double crateAmount;
        @SerializedName("crate_cd")
        @Expose
        private String crateCd;
        @SerializedName("crate_type")
        @Expose
        private String crateType;
        @SerializedName("cus_pickup_ver_code")
        @Expose
        private String cusPickupVerCode;
        @SerializedName("cus_return_ver_code")
        @Expose
        private String cusReturnVerCode;
        @SerializedName("del_acc_code")
        @Expose
        private String delAccCode;
        @SerializedName("del_acc_name")
        @Expose
        private String delAccName;
        @SerializedName("del_add_id")
        @Expose
        private String delAddId;
        @SerializedName("del_add_tz")
        @Expose
        private String delAddTz;
        @SerializedName("deliver_apartment")
        @Expose
        private String deliverApartment;
        @SerializedName("deliver_branch")
        @Expose
        private String deliverBranch;
        @SerializedName("deliver_city")
        @Expose
        private String deliverCity;
        @SerializedName("deliver_country")
        @Expose
        private String deliverCountry;
        @SerializedName("deliver_email")
        @Expose
        private String deliverEmail;
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
        @SerializedName("deliver_notes")
        @Expose
        private String deliverNotes;
        @SerializedName("del_pn")
        @Expose
        private String delPn;
        @SerializedName("del_pincode")
        @Expose
        private Integer delPincode;
        @SerializedName("del_servs_tm")
        @Expose
        private String delServsTm;
        @SerializedName("deliver_state")
        @Expose
        private String deliverState;
        @SerializedName("deliver_street_name")
        @Expose
        private String deliverStreetName;
        @SerializedName("deliver_type")
        @Expose
        private String deliverType;
        @SerializedName("del_medim_usrnam")
        @Expose
        private String delMedimUsrnam;
        @SerializedName("distribution_center")
        @Expose
        private String distributionCenter;
        @SerializedName("no_of_units")
        @Expose
        private Object noOfUnits;
        @SerializedName("num_of_items")
        @Expose
        private Integer numOfItems;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("order_state")
        @Expose
        private OrderState orderState;
        @SerializedName("order_status")
        @Expose
        private OrderStatus orderStatus;
        @SerializedName("pakg_value")
        @Expose
        private Double pakgValue;
        @SerializedName("pakg_vol")
        @Expose
        private Double pakgVol;
        @SerializedName("pakg_wt")
        @Expose
        private Integer pakgWt;
        @SerializedName("partial_del_alow_fl")
        @Expose
        private PartialDelAlowFl partialDelAlowFl;
        @SerializedName("payment_type")
        @Expose
        private PaymentType paymentType;
        @SerializedName("pickup_acc_code")
        @Expose
        private Integer pickupAccCode;
        @SerializedName("pickup_acc_name")
        @Expose
        private String pickupAccName;
        @SerializedName("pickup_add_id")
        @Expose
        private String pickupAddId;
        @SerializedName("pickup_add_tizn")
        @Expose
        private String pickupAddTizn;
        @SerializedName("pickup_apt")
        @Expose
        private String pickupApt;
        @SerializedName("pickup_branch")
        @Expose
        private String pickupBranch;
        @SerializedName("pickup_city")
        @Expose
        private String pickupCity;
        @SerializedName("pickup_country")
        @Expose
        private String pickupCountry;
        @SerializedName("pickup_email")
        @Expose
        private String pickupEmail;
        @SerializedName("pickup_lndmrk")
        @Expose
        private String pickupLndmrk;
        @SerializedName("pickup_latitude")
        @Expose
        private double pickupLatitude;
        @SerializedName("pickup_locality")
        @Expose
        private String pickupLocality;
        @SerializedName("pickup_longitude")
        @Expose
        private double pickupLongitude;
        @SerializedName("pickup_notes")
        @Expose
        private String pickupNotes;
        @SerializedName("pickup_pn")
        @Expose
        private String pickupPn;
        @SerializedName("pickup_pincode")
        @Expose
        private Integer pickupPincode;
        @SerializedName("pickup_sertm")
        @Expose
        private Integer pickupSertm;
        @SerializedName("pickup_state")
        @Expose
        private String pickupState;
        @SerializedName("pickup_street_name")
        @Expose
        private String pickupStreetName;
        @SerializedName("priority")
        @Expose
        private Priority priority;
        @SerializedName("return_acc_cod")
        @Expose
        private Integer returnAccCod;
        @SerializedName("return_acc_name")
        @Expose
        private String returnAccName;
        @SerializedName("return_add_id")
        @Expose
        private String returnAddId;
        @SerializedName("return_allowed_fl")
        @Expose
        private ReturnAllowedFl returnAllowedFl;
        @SerializedName("return_apartment")
        @Expose
        private String returnApartment;
        @SerializedName("return_branch")
        @Expose
        private String returnBranch;
        @SerializedName("return_city")
        @Expose
        private String returnCity;
        @SerializedName("return_country")
        @Expose
        private String returnCountry;
        @SerializedName("return_email_id")
        @Expose
        private String returnEmailId;
        @SerializedName("return_landmark")
        @Expose
        private String returnLandmark;
        @SerializedName("return_locality")
        @Expose
        private String returnLocality;
        @SerializedName("return_pn")
        @Expose
        private String returnPn;
        @SerializedName("return_pincode")
        @Expose
        private Integer returnPincode;
        @SerializedName("return_state")
        @Expose
        private String returnState;
        @SerializedName("return_street_name")
        @Expose
        private String returnStreetName;
        @SerializedName("service_type")
        @Expose
        private ServiceType serviceType;
        @SerializedName("shipmt_odr_typecd")
        @Expose
        private Object shipmtOdrTypecd;
        @SerializedName("order_rider")
        @Expose
        private OrderRider orderRider;
        @SerializedName("order_handover")
        @Expose
        private OrderHandover orderHandover;
        @SerializedName("pickup_st_windo")
        @Expose
        private String pickupStWindo;
        @SerializedName("created_time")
        @Expose
        private String createdTime;
        @SerializedName("del_st_windo")
        @Expose
        private String delStWindo;
        @SerializedName("return_et_windo")
        @Expose
        private String returnEtWindo;
        @SerializedName("del_et_windo")
        @Expose
        private String delEtWindo;
        @SerializedName("return_st_windo")
        @Expose
        private String returnStWindo;
        @SerializedName("shipmt_odr_dt")
        @Expose
        private String shipmtOdrDt;
        @SerializedName("pickup_et_windo")
        @Expose
        private String pickupEtWindo;
        @SerializedName("items")
        @Expose
        private List<Item> items = null;
        @SerializedName("order_sh")
        @Expose
        private List<OrderSh> orderSh = null;

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

        public String getAwbNumber() {
            return awbNumber;
        }

        public void setAwbNumber(String awbNumber) {
            this.awbNumber = awbNumber;
        }

        public String getBranpickupVerCode() {
            return branpickupVerCode;
        }

        public void setBranpickupVerCode(String branpickupVerCode) {
            this.branpickupVerCode = branpickupVerCode;
        }

        public String getBranreturnVerCode() {
            return branreturnVerCode;
        }

        public void setBranreturnVerCode(String branreturnVerCode) {
            this.branreturnVerCode = branreturnVerCode;
        }

        public CancelAllowed getCancelAllowed() {
            return cancelAllowed;
        }

        public void setCancelAllowed(CancelAllowed cancelAllowed) {
            this.cancelAllowed = cancelAllowed;
        }

        public String getClientCode() {
            return clientCode;
        }

        public void setClientCode(String clientCode) {
            this.clientCode = clientCode;
        }

        public Double getCrateAmount() {
            return crateAmount;
        }

        public void setCrateAmount(Double crateAmount) {
            this.crateAmount = crateAmount;
        }

        public String getCrateCd() {
            return crateCd;
        }

        public void setCrateCd(String crateCd) {
            this.crateCd = crateCd;
        }

        public String getCrateType() {
            return crateType;
        }

        public void setCrateType(String crateType) {
            this.crateType = crateType;
        }

        public String getCusPickupVerCode() {
            return cusPickupVerCode;
        }

        public void setCusPickupVerCode(String cusPickupVerCode) {
            this.cusPickupVerCode = cusPickupVerCode;
        }

        public String getCusReturnVerCode() {
            return cusReturnVerCode;
        }

        public void setCusReturnVerCode(String cusReturnVerCode) {
            this.cusReturnVerCode = cusReturnVerCode;
        }

        public String getDelAccCode() {
            return delAccCode;
        }

        public void setDelAccCode(String delAccCode) {
            this.delAccCode = delAccCode;
        }

        public String getDelAccName() {
            return delAccName;
        }

        public void setDelAccName(String delAccName) {
            this.delAccName = delAccName;
        }

        public String getDelAddId() {
            return delAddId;
        }

        public void setDelAddId(String delAddId) {
            this.delAddId = delAddId;
        }

        public String getDelAddTz() {
            return delAddTz;
        }

        public void setDelAddTz(String delAddTz) {
            this.delAddTz = delAddTz;
        }

        public String getDeliverApartment() {
            return deliverApartment;
        }

        public void setDeliverApartment(String deliverApartment) {
            this.deliverApartment = deliverApartment;
        }

        public String getDeliverBranch() {
            return deliverBranch;
        }

        public void setDeliverBranch(String deliverBranch) {
            this.deliverBranch = deliverBranch;
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

        public String getDeliverEmail() {
            return deliverEmail;
        }

        public void setDeliverEmail(String deliverEmail) {
            this.deliverEmail = deliverEmail;
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

        public String getDeliverNotes() {
            return deliverNotes;
        }

        public void setDeliverNotes(String deliverNotes) {
            this.deliverNotes = deliverNotes;
        }

        public String getDelPn() {
            return delPn;
        }

        public void setDelPn(String delPn) {
            this.delPn = delPn;
        }

        public Integer getDelPincode() {
            return delPincode;
        }

        public void setDelPincode(Integer delPincode) {
            this.delPincode = delPincode;
        }

        public String getDelServsTm() {
            return delServsTm;
        }

        public void setDelServsTm(String delServsTm) {
            this.delServsTm = delServsTm;
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

        public String getDelMedimUsrnam() {
            return delMedimUsrnam;
        }

        public void setDelMedimUsrnam(String delMedimUsrnam) {
            this.delMedimUsrnam = delMedimUsrnam;
        }

        public String getDistributionCenter() {
            return distributionCenter;
        }

        public void setDistributionCenter(String distributionCenter) {
            this.distributionCenter = distributionCenter;
        }

        public Object getNoOfUnits() {
            return noOfUnits;
        }

        public void setNoOfUnits(Object noOfUnits) {
            this.noOfUnits = noOfUnits;
        }

        public Integer getNumOfItems() {
            return numOfItems;
        }

        public void setNumOfItems(Integer numOfItems) {
            this.numOfItems = numOfItems;
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

        public Double getPakgValue() {
            return pakgValue;
        }

        public void setPakgValue(Double pakgValue) {
            this.pakgValue = pakgValue;
        }

        public Double getPakgVol() {
            return pakgVol;
        }

        public void setPakgVol(Double pakgVol) {
            this.pakgVol = pakgVol;
        }

        public Integer getPakgWt() {
            return pakgWt;
        }

        public void setPakgWt(Integer pakgWt) {
            this.pakgWt = pakgWt;
        }

        public PartialDelAlowFl getPartialDelAlowFl() {
            return partialDelAlowFl;
        }

        public void setPartialDelAlowFl(PartialDelAlowFl partialDelAlowFl) {
            this.partialDelAlowFl = partialDelAlowFl;
        }

        public PaymentType getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
        }

        public Integer getPickupAccCode() {
            return pickupAccCode;
        }

        public void setPickupAccCode(Integer pickupAccCode) {
            this.pickupAccCode = pickupAccCode;
        }

        public String getPickupAccName() {
            return pickupAccName;
        }

        public void setPickupAccName(String pickupAccName) {
            this.pickupAccName = pickupAccName;
        }

        public String getPickupAddId() {
            return pickupAddId;
        }

        public void setPickupAddId(String pickupAddId) {
            this.pickupAddId = pickupAddId;
        }

        public String getPickupAddTizn() {
            return pickupAddTizn;
        }

        public void setPickupAddTizn(String pickupAddTizn) {
            this.pickupAddTizn = pickupAddTizn;
        }

        public String getPickupApt() {
            return pickupApt;
        }

        public void setPickupApt(String pickupApt) {
            this.pickupApt = pickupApt;
        }

        public String getPickupBranch() {
            return pickupBranch;
        }

        public void setPickupBranch(String pickupBranch) {
            this.pickupBranch = pickupBranch;
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

        public String getPickupEmail() {
            return pickupEmail;
        }

        public void setPickupEmail(String pickupEmail) {
            this.pickupEmail = pickupEmail;
        }

        public String getPickupLndmrk() {
            return pickupLndmrk;
        }

        public void setPickupLndmrk(String pickupLndmrk) {
            this.pickupLndmrk = pickupLndmrk;
        }

        public double getPickupLatitude() {
            return pickupLatitude;
        }

        public void setPickupLatitude(double pickupLatitude) {
            this.pickupLatitude = pickupLatitude;
        }

        public String getPickupLocality() {
            return pickupLocality;
        }

        public void setPickupLocality(String pickupLocality) {
            this.pickupLocality = pickupLocality;
        }

        public double getPickupLongitude() {
            return pickupLongitude;
        }

        public void setPickupLongitude(double pickupLongitude) {
            this.pickupLongitude = pickupLongitude;
        }

        public String getPickupNotes() {
            return pickupNotes;
        }

        public void setPickupNotes(String pickupNotes) {
            this.pickupNotes = pickupNotes;
        }

        public String getPickupPn() {
            return pickupPn;
        }

        public void setPickupPn(String pickupPn) {
            this.pickupPn = pickupPn;
        }

        public Integer getPickupPincode() {
            return pickupPincode;
        }

        public void setPickupPincode(Integer pickupPincode) {
            this.pickupPincode = pickupPincode;
        }

        public Integer getPickupSertm() {
            return pickupSertm;
        }

        public void setPickupSertm(Integer pickupSertm) {
            this.pickupSertm = pickupSertm;
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

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Integer getReturnAccCod() {
            return returnAccCod;
        }

        public void setReturnAccCod(Integer returnAccCod) {
            this.returnAccCod = returnAccCod;
        }

        public String getReturnAccName() {
            return returnAccName;
        }

        public void setReturnAccName(String returnAccName) {
            this.returnAccName = returnAccName;
        }

        public String getReturnAddId() {
            return returnAddId;
        }

        public void setReturnAddId(String returnAddId) {
            this.returnAddId = returnAddId;
        }

        public ReturnAllowedFl getReturnAllowedFl() {
            return returnAllowedFl;
        }

        public void setReturnAllowedFl(ReturnAllowedFl returnAllowedFl) {
            this.returnAllowedFl = returnAllowedFl;
        }

        public String getReturnApartment() {
            return returnApartment;
        }

        public void setReturnApartment(String returnApartment) {
            this.returnApartment = returnApartment;
        }

        public String getReturnBranch() {
            return returnBranch;
        }

        public void setReturnBranch(String returnBranch) {
            this.returnBranch = returnBranch;
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

        public String getReturnEmailId() {
            return returnEmailId;
        }

        public void setReturnEmailId(String returnEmailId) {
            this.returnEmailId = returnEmailId;
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

        public String getReturnPn() {
            return returnPn;
        }

        public void setReturnPn(String returnPn) {
            this.returnPn = returnPn;
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

        public ServiceType getServiceType() {
            return serviceType;
        }

        public void setServiceType(ServiceType serviceType) {
            this.serviceType = serviceType;
        }

        public Object getShipmtOdrTypecd() {
            return shipmtOdrTypecd;
        }

        public void setShipmtOdrTypecd(Object shipmtOdrTypecd) {
            this.shipmtOdrTypecd = shipmtOdrTypecd;
        }

        public OrderRider getOrderRider() {
            return orderRider;
        }

        public void setOrderRider(OrderRider orderRider) {
            this.orderRider = orderRider;
        }

        public OrderHandover getOrderHandover() {
            return orderHandover;
        }

        public void setOrderHandover(OrderHandover orderHandover) {
            this.orderHandover = orderHandover;
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

        public String getReturnEtWindo() {
            return returnEtWindo;
        }

        public void setReturnEtWindo(String returnEtWindo) {
            this.returnEtWindo = returnEtWindo;
        }

        public String getDelEtWindo() {
            return delEtWindo;
        }

        public void setDelEtWindo(String delEtWindo) {
            this.delEtWindo = delEtWindo;
        }

        public String getReturnStWindo() {
            return returnStWindo;
        }

        public void setReturnStWindo(String returnStWindo) {
            this.returnStWindo = returnStWindo;
        }

        public String getShipmtOdrDt() {
            return shipmtOdrDt;
        }

        public void setShipmtOdrDt(String shipmtOdrDt) {
            this.shipmtOdrDt = shipmtOdrDt;
        }

        public String getPickupEtWindo() {
            return pickupEtWindo;
        }

        public void setPickupEtWindo(String pickupEtWindo) {
            this.pickupEtWindo = pickupEtWindo;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<OrderSh> getOrderSh() {
            return orderSh;
        }

        public void setOrderSh(List<OrderSh> orderSh) {
            this.orderSh = orderSh;
        }

    }

    public class DelLocType implements Serializable {

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

    public class Dimenesions implements Serializable {


    }

    public class Item implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("itemname")
        @Expose
        private String itemname;
        @SerializedName("itemcd")
        @Expose
        private String itemcd;
        @SerializedName("itemprice")
        @Expose
        private Integer itemprice;
        @SerializedName("itemquantity")
        @Expose
        private String itemquantity;
        @SerializedName("itemtype")
        @Expose
        private Itemtype itemtype;
        @SerializedName("itemweight")
        @Expose
        private Integer itemweight;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getItemcd() {
            return itemcd;
        }

        public void setItemcd(String itemcd) {
            this.itemcd = itemcd;
        }

        public Integer getItemprice() {
            return itemprice;
        }

        public void setItemprice(Integer itemprice) {
            this.itemprice = itemprice;
        }

        public String getItemquantity() {
            return itemquantity;
        }

        public void setItemquantity(String itemquantity) {
            this.itemquantity = itemquantity;
        }

        public Itemtype getItemtype() {
            return itemtype;
        }

        public void setItemtype(Itemtype itemtype) {
            this.itemtype = itemtype;
        }

        public Integer getItemweight() {
            return itemweight;
        }

        public void setItemweight(Integer itemweight) {
            this.itemweight = itemweight;
        }

    }

    public class Itemtype implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__10 other;
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

        public Other__10 getOther() {
            return other;
        }

        public void setOther(Other__10 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class OrderHandover implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("handover_to")
        @Expose
        private String handoverTo;
        @SerializedName("signature")
        @Expose
        private List<Signature> signature = null;
        @SerializedName("created_time")
        @Expose
        private String createdTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getHandoverTo() {
            return handoverTo;
        }

        public void setHandoverTo(String handoverTo) {
            this.handoverTo = handoverTo;
        }

        public List<Signature> getSignature() {
            return signature;
        }

        public void setSignature(List<Signature> signature) {
            this.signature = signature;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }

    public class OrderRider implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
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

    public class OrderSh implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("order_status")
        @Expose
        private OrderStatus__1 orderStatus;
        @SerializedName("created_id")
        @Expose
        private CreatedId createdId;
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

        public CreatedId getCreatedId() {
            return createdId;
        }

        public void setCreatedId(CreatedId createdId) {
            this.createdId = createdId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }

    public class OrderState implements Serializable {

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

    public class OrderStatus implements Serializable {

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

    public class OrderStatus__1 implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__11 other;
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

        public Other__11 getOther() {
            return other;
        }

        public void setOther(Other__11 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Other implements Serializable {

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

    public class Other__1 implements Serializable {

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

    public class Other__10 implements Serializable {

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

    public class Other__11 implements Serializable {

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

    public class Other__2 implements Serializable {

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

    public class Other__3 implements Serializable {

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

    public class Other__4 implements Serializable {

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

    public class Other__5 implements Serializable {

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

    public class Other__6 implements Serializable {

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

    public class Other__7 implements Serializable {

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

    public class Other__8 implements Serializable {

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

    public class Other__9 implements Serializable {

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

    public class PartialDelAlowFl implements Serializable {

        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("other")
        @Expose
        private Other__5 other;
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

        public Other__5 getOther() {
            return other;
        }

        public void setOther(Other__5 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class PaymentType implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__6 other;
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

        public Other__6 getOther() {
            return other;
        }

        public void setOther(Other__6 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Priority implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("other")
        @Expose
        private Other__7 other;
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

        public Other__7 getOther() {
            return other;
        }

        public void setOther(Other__7 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class ReturnAllowedFl implements Serializable {

        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("other")
        @Expose
        private Other__8 other;
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

        public Other__8 getOther() {
            return other;
        }

        public void setOther(Other__8 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Rider implements Serializable {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;

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

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }

    }

    public class ServiceType implements Serializable {

        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;
        @SerializedName("other")
        @Expose
        private Other__9 other;
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

        public Other__9 getOther() {
            return other;
        }

        public void setOther(Other__9 other) {
            this.other = other;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

    }

    public class Signature implements Serializable {

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
        @SerializedName("fullPath")
        @Expose
        private String fullPath;
        @SerializedName("created_info")
        @Expose
        private CreatedInfo createdInfo;

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

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public CreatedInfo getCreatedInfo() {
            return createdInfo;
        }

        public void setCreatedInfo(CreatedInfo createdInfo) {
            this.createdInfo = createdInfo;
        }

    }
}
