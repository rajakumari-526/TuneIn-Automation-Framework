package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Entry {

    @SerializedName("id")
    public String id;

    @SerializedName("suite_id")
    public Integer suiteId;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("include_all")
    public Boolean includeAll;

    @SerializedName("config_ids")
    public List<String> configIds = null;

    @SerializedName("case_ids")
    public List<String> caseIds = null;

    @SerializedName("runs")
    public List<com.tunein.mobile.api.testrail.dto.Run> runs = null;

}
