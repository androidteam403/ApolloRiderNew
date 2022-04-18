package com.apollo.epos.fragment.profile.model;

import com.google.gson.annotations.SerializedName;

public class ADSPlayListRequest {
    @SerializedName("screen_id")
    private String screen_id;

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }
}
