package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Suite {

    @SerializedName("completed_on")
    public Object completedOn;

    @SerializedName("description")
    public String description;

    @SerializedName("id")
    public Integer id;

    @SerializedName("is_baseline")
    public Boolean isBaseline;

    @SerializedName("is_completed")
    public Boolean isCompleted;

    @SerializedName("is_master")
    public Boolean isMaster;

    @SerializedName("name")
    public String name;

    @SerializedName("project_id")
    public Integer projectId;

    @SerializedName("url")
    public String url;

}
