package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Result {

    @SerializedName("id")
    public Integer id;

    @SerializedName("test_id")
    public Integer testId;

    @SerializedName("status_id")
    public Integer statusId;

    @SerializedName("created_by")
    public Integer createdBy;

    @SerializedName("created_on")
    public Integer createdOn;

    @SerializedName("assignedto_id")
    public Object assignedtoId;

    @SerializedName("comment")
    public String comment;

    @SerializedName("version")
    public String version;

    @SerializedName("elapsed")
    public Object elapsed;

    @SerializedName("defects")
    public String defects;

    @SerializedName("custom_step_results")
    public Object customStepResults;

}
