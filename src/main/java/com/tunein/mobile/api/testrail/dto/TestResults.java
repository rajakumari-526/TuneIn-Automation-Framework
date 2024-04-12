package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TestResults {

    @SerializedName("status_id")
    public Integer statusId;

    @SerializedName("comment")
    public String comment;

    @SerializedName("version")
    public String version;

    @SerializedName("defects")
    public String defects;

    @SerializedName("elapsed")
    public String elapsed;

}
