package com.apollo.epos.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetRiderProfileResponse {

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
    public class CaCountry {

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
    public class CaState {

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
    public class Cluster {

        @SerializedName("uid")
        @Expose
        private Object uid;
        @SerializedName("name")
        @Expose
        private Object name;

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

    }
    public class Data {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("status")
        @Expose
        private Status status;
        @SerializedName("login_unique")
        @Expose
        private String loginUnique;
        @SerializedName("role")
        @Expose
        private Role role;
        @SerializedName("user_add_info")
        @Expose
        private UserAddInfo userAddInfo;
        @SerializedName("user_address")
        @Expose
        private UserAddress userAddress;
        @SerializedName("dob")
        @Expose
        private String dob;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
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

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getLoginUnique() {
            return loginUnique;
        }

        public void setLoginUnique(String loginUnique) {
            this.loginUnique = loginUnique;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public UserAddInfo getUserAddInfo() {
            return userAddInfo;
        }

        public void setUserAddInfo(UserAddInfo userAddInfo) {
            this.userAddInfo = userAddInfo;
        }

        public UserAddress getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(UserAddress userAddress) {
            this.userAddress = userAddress;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

    }
    public class DocType {

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
    public class IdentificationProof {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("doc_no")
        @Expose
        private String docNo;
        @SerializedName("doc_type")
        @Expose
        private DocType docType;
        @SerializedName("doc")
        @Expose
        private List<Object> doc = null;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getDocNo() {
            return docNo;
        }

        public void setDocNo(String docNo) {
            this.docNo = docNo;
        }

        public DocType getDocType() {
            return docType;
        }

        public void setDocType(DocType docType) {
            this.docType = docType;
        }

        public List<Object> getDoc() {
            return doc;
        }

        public void setDoc(List<Object> doc) {
            this.doc = doc;
        }

    }
    public class Language {

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
        private Object color;

        public Object getColor() {
            return color;
        }

        public void setColor(Object color) {
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
    public class PaCountry {

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
    public class PaState {

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
    public class RiderPostalCode {

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
    public class Role {

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
    public class SkillSet {

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
    public class Status {

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
    public class Store {

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
    public class UserAddInfo {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("capacity_units")
        @Expose
        private Integer capacityUnits;
        @SerializedName("capacity_weight")
        @Expose
        private Integer capacityWeight;
        @SerializedName("manufacturer")
        @Expose
        private String manufacturer;
        @SerializedName("max_cod_amount")
        @Expose
        private Integer maxCodAmount;
        @SerializedName("max_orders_per_day")
        @Expose
        private Integer maxOrdersPerDay;
        @SerializedName("model")
        @Expose
        private String model;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("vehicle_type")
        @Expose
        private VehicleType vehicleType;
        @SerializedName("cluster")
        @Expose
        private Cluster cluster;
        @SerializedName("skill_set")
        @Expose
        private SkillSet skillSet;
        @SerializedName("store")
        @Expose
        private Store store;
        @SerializedName("working_end_hrs")
        @Expose
        private String workingEndHrs;
        @SerializedName("working_start_hrs")
        @Expose
        private String workingStartHrs;
        @SerializedName("shift_to_time")
        @Expose
        private Object shiftToTime;
        @SerializedName("shift_from_time")
        @Expose
        private Object shiftFromTime;
        @SerializedName("language")
        @Expose
        private List<Language> language = null;
        @SerializedName("identification_proofs")
        @Expose
        private List<IdentificationProof> identificationProofs = null;
        @SerializedName("rider_postal_code")
        @Expose
        private List<RiderPostalCode> riderPostalCode = null;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Integer getCapacityUnits() {
            return capacityUnits;
        }

        public void setCapacityUnits(Integer capacityUnits) {
            this.capacityUnits = capacityUnits;
        }

        public Integer getCapacityWeight() {
            return capacityWeight;
        }

        public void setCapacityWeight(Integer capacityWeight) {
            this.capacityWeight = capacityWeight;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public Integer getMaxCodAmount() {
            return maxCodAmount;
        }

        public void setMaxCodAmount(Integer maxCodAmount) {
            this.maxCodAmount = maxCodAmount;
        }

        public Integer getMaxOrdersPerDay() {
            return maxOrdersPerDay;
        }

        public void setMaxOrdersPerDay(Integer maxOrdersPerDay) {
            this.maxOrdersPerDay = maxOrdersPerDay;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public VehicleType getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
        }

        public Cluster getCluster() {
            return cluster;
        }

        public void setCluster(Cluster cluster) {
            this.cluster = cluster;
        }

        public SkillSet getSkillSet() {
            return skillSet;
        }

        public void setSkillSet(SkillSet skillSet) {
            this.skillSet = skillSet;
        }

        public Store getStore() {
            return store;
        }

        public void setStore(Store store) {
            this.store = store;
        }

        public String getWorkingEndHrs() {
            return workingEndHrs;
        }

        public void setWorkingEndHrs(String workingEndHrs) {
            this.workingEndHrs = workingEndHrs;
        }

        public String getWorkingStartHrs() {
            return workingStartHrs;
        }

        public void setWorkingStartHrs(String workingStartHrs) {
            this.workingStartHrs = workingStartHrs;
        }

        public Object getShiftToTime() {
            return shiftToTime;
        }

        public void setShiftToTime(Object shiftToTime) {
            this.shiftToTime = shiftToTime;
        }

        public Object getShiftFromTime() {
            return shiftFromTime;
        }

        public void setShiftFromTime(Object shiftFromTime) {
            this.shiftFromTime = shiftFromTime;
        }

        public List<Language> getLanguage() {
            return language;
        }

        public void setLanguage(List<Language> language) {
            this.language = language;
        }

        public List<IdentificationProof> getIdentificationProofs() {
            return identificationProofs;
        }

        public void setIdentificationProofs(List<IdentificationProof> identificationProofs) {
            this.identificationProofs = identificationProofs;
        }

        public List<RiderPostalCode> getRiderPostalCode() {
            return riderPostalCode;
        }

        public void setRiderPostalCode(List<RiderPostalCode> riderPostalCode) {
            this.riderPostalCode = riderPostalCode;
        }

    }
    public class UserAddress {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("current_address")
        @Expose
        private String currentAddress;
        @SerializedName("ca_city")
        @Expose
        private String caCity;
        @SerializedName("ca_pincode")
        @Expose
        private String caPincode;
        @SerializedName("is_ca_is_same_as_pa")
        @Expose
        private Boolean isCaIsSameAsPa;
        @SerializedName("permanent_address")
        @Expose
        private String permanentAddress;
        @SerializedName("pa_city")
        @Expose
        private String paCity;
        @SerializedName("pa_pincode")
        @Expose
        private String paPincode;
        @SerializedName("ca_country")
        @Expose
        private CaCountry caCountry;
        @SerializedName("ca_state")
        @Expose
        private CaState caState;
        @SerializedName("pa_state")
        @Expose
        private PaState paState;
        @SerializedName("pa_country")
        @Expose
        private PaCountry paCountry;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(String currentAddress) {
            this.currentAddress = currentAddress;
        }

        public String getCaCity() {
            return caCity;
        }

        public void setCaCity(String caCity) {
            this.caCity = caCity;
        }

        public String getCaPincode() {
            return caPincode;
        }

        public void setCaPincode(String caPincode) {
            this.caPincode = caPincode;
        }

        public Boolean getIsCaIsSameAsPa() {
            return isCaIsSameAsPa;
        }

        public void setIsCaIsSameAsPa(Boolean isCaIsSameAsPa) {
            this.isCaIsSameAsPa = isCaIsSameAsPa;
        }

        public String getPermanentAddress() {
            return permanentAddress;
        }

        public void setPermanentAddress(String permanentAddress) {
            this.permanentAddress = permanentAddress;
        }

        public String getPaCity() {
            return paCity;
        }

        public void setPaCity(String paCity) {
            this.paCity = paCity;
        }

        public String getPaPincode() {
            return paPincode;
        }

        public void setPaPincode(String paPincode) {
            this.paPincode = paPincode;
        }

        public CaCountry getCaCountry() {
            return caCountry;
        }

        public void setCaCountry(CaCountry caCountry) {
            this.caCountry = caCountry;
        }

        public CaState getCaState() {
            return caState;
        }

        public void setCaState(CaState caState) {
            this.caState = caState;
        }

        public PaState getPaState() {
            return paState;
        }

        public void setPaState(PaState paState) {
            this.paState = paState;
        }

        public PaCountry getPaCountry() {
            return paCountry;
        }

        public void setPaCountry(PaCountry paCountry) {
            this.paCountry = paCountry;
        }

    }
    public class VehicleType {

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

