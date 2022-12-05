package com.apollo.epos.activity.login.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetDetailsRequest implements Serializable {

    @SerializedName("HEADERTOKENKEY")
    @Expose
    private String headertokenkey;
    @SerializedName("HEADERTOKENVALUE")
    @Expose
    private String headertokenvalue;
    @SerializedName("REQUESTJSON")
    @Expose
    private String requestjson;
    @SerializedName("REQUESTTYPE")
    @Expose
    private String requesttype;
    @SerializedName("REQUESTURL")
    @Expose
    private String requesturl;


    public String getHeadertokenkey() {
        return headertokenkey;
    }

    public void setHeadertokenkey(String headertokenkey) {
        this.headertokenkey = headertokenkey;
    }

    public String getHeadertokenvalue() {
        return headertokenvalue;
    }

    public void setHeadertokenvalue(String headertokenvalue) {
        this.headertokenvalue = headertokenvalue;
    }

    public String getRequestjson() {
        return requestjson;
    }

    public void setRequestjson(String requestjson) {
        this.requestjson = requestjson;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getRequesturl() {
        return requesturl;
    }

    public void setRequesturl(String requesturl) {
        this.requesturl = requesturl;
    }

}

