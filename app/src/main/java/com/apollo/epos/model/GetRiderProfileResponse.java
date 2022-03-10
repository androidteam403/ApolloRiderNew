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
    public class CaDistrict {

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
    public class CaPinCode {

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
    public class CadCity {

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
    public class CreatedInfo__1 {

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
    public class CuaRegion {

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
    public class Data {

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
        @SerializedName("user_add_info")
        @Expose
        private UserAddInfo userAddInfo;
        @SerializedName("user_address")
        @Expose
        private UserAddress userAddress;
        @SerializedName("dob")
        @Expose
        private String dob;
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

        public List<Pic> getPic() {
            return pic;
        }

        public void setPic(List<Pic> pic) {
            this.pic = pic;
        }

    }
    public class Dimenesions {

        @SerializedName("300_300")
        @Expose
        private String _300300;
        @SerializedName("300_300_fullPath")
        @Expose
        private String _300300FullPath;

        public String get300300() {
            return _300300;
        }

        public void set300300(String _300300) {
            this._300300 = _300300;
        }

        public String get300300FullPath() {
            return _300300FullPath;
        }

        public void set300300FullPath(String _300300FullPath) {
            this._300300FullPath = _300300FullPath;
        }

    }
    public class Dimenesions__1 {

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
    public class Doc {

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
    public class DocType {

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
        private List<Doc> doc = null;

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

        public List<Doc> getDoc() {
            return doc;
        }

        public void setDoc(List<Doc> doc) {
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
    public class PaDistrict {

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
    public class PaPinCode {

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
    public class PadCity {

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
    public class PeaRegion {

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
        private Dimenesions__1 dimenesions;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("fullPath")
        @Expose
        private String fullPath;
        @SerializedName("created_info")
        @Expose
        private CreatedInfo__1 createdInfo;

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

        public Dimenesions__1 getDimenesions() {
            return dimenesions;
        }

        public void setDimenesions(Dimenesions__1 dimenesions) {
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

        public CreatedInfo__1 getCreatedInfo() {
            return createdInfo;
        }

        public void setCreatedInfo(CreatedInfo__1 createdInfo) {
            this.createdInfo = createdInfo;
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
        @SerializedName("area_of_op")
        @Expose
        private Object areaOfOp;
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
        private double maxCodAmount;
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
        @SerializedName("rider_postal_code")
        @Expose
        private List<RiderPostalCode> riderPostalCode = null;
        @SerializedName("identification_proofs")
        @Expose
        private List<IdentificationProof> identificationProofs = null;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Object getAreaOfOp() {
            return areaOfOp;
        }

        public void setAreaOfOp(Object areaOfOp) {
            this.areaOfOp = areaOfOp;
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

        public double getMaxCodAmount() {
            return maxCodAmount;
        }

        public void setMaxCodAmount(double maxCodAmount) {
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

        public List<RiderPostalCode> getRiderPostalCode() {
            return riderPostalCode;
        }

        public void setRiderPostalCode(List<RiderPostalCode> riderPostalCode) {
            this.riderPostalCode = riderPostalCode;
        }

        public List<IdentificationProof> getIdentificationProofs() {
            return identificationProofs;
        }

        public void setIdentificationProofs(List<IdentificationProof> identificationProofs) {
            this.identificationProofs = identificationProofs;
        }

    }
    public class UserAddress {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("current_address")
        @Expose
        private String currentAddress;
        @SerializedName("is_ca_is_same_as_pa")
        @Expose
        private Boolean isCaIsSameAsPa;
        @SerializedName("permanent_address")
        @Expose
        private String permanentAddress;
        @SerializedName("ca_country")
        @Expose
        private CaCountry caCountry;
        @SerializedName("cua_region")
        @Expose
        private CuaRegion cuaRegion;
        @SerializedName("ca_state")
        @Expose
        private CaState caState;
        @SerializedName("pa_pin_code")
        @Expose
        private PaPinCode paPinCode;
        @SerializedName("pad_city")
        @Expose
        private PadCity padCity;
        @SerializedName("cad_city")
        @Expose
        private CadCity cadCity;
        @SerializedName("pea_region")
        @Expose
        private PeaRegion peaRegion;
        @SerializedName("pa_country")
        @Expose
        private PaCountry paCountry;
        @SerializedName("pa_state")
        @Expose
        private PaState paState;
        @SerializedName("ca_pin_code")
        @Expose
        private CaPinCode caPinCode;
        @SerializedName("ca_district")
        @Expose
        private CaDistrict caDistrict;
        @SerializedName("pa_district")
        @Expose
        private PaDistrict paDistrict;

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

        public CaCountry getCaCountry() {
            return caCountry;
        }

        public void setCaCountry(CaCountry caCountry) {
            this.caCountry = caCountry;
        }

        public CuaRegion getCuaRegion() {
            return cuaRegion;
        }

        public void setCuaRegion(CuaRegion cuaRegion) {
            this.cuaRegion = cuaRegion;
        }

        public CaState getCaState() {
            return caState;
        }

        public void setCaState(CaState caState) {
            this.caState = caState;
        }

        public PaPinCode getPaPinCode() {
            return paPinCode;
        }

        public void setPaPinCode(PaPinCode paPinCode) {
            this.paPinCode = paPinCode;
        }

        public PadCity getPadCity() {
            return padCity;
        }

        public void setPadCity(PadCity padCity) {
            this.padCity = padCity;
        }

        public CadCity getCadCity() {
            return cadCity;
        }

        public void setCadCity(CadCity cadCity) {
            this.cadCity = cadCity;
        }

        public PeaRegion getPeaRegion() {
            return peaRegion;
        }

        public void setPeaRegion(PeaRegion peaRegion) {
            this.peaRegion = peaRegion;
        }

        public PaCountry getPaCountry() {
            return paCountry;
        }

        public void setPaCountry(PaCountry paCountry) {
            this.paCountry = paCountry;
        }

        public PaState getPaState() {
            return paState;
        }

        public void setPaState(PaState paState) {
            this.paState = paState;
        }

        public CaPinCode getCaPinCode() {
            return caPinCode;
        }

        public void setCaPinCode(CaPinCode caPinCode) {
            this.caPinCode = caPinCode;
        }

        public CaDistrict getCaDistrict() {
            return caDistrict;
        }

        public void setCaDistrict(CaDistrict caDistrict) {
            this.caDistrict = caDistrict;
        }

        public PaDistrict getPaDistrict() {
            return paDistrict;
        }

        public void setPaDistrict(PaDistrict paDistrict) {
            this.paDistrict = paDistrict;
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





