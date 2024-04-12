package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Run {

    @SerializedName("id")
    public Integer id;

    @SerializedName("suite_id")
    public Integer suiteId;

    @SerializedName("name")
    public String name;

    @SerializedName("case_ids")
    public List<String> caseIds = null;

    @SerializedName("description")
    public Object description;

    @SerializedName("milestone_id")
    public Object milestoneId;

    @SerializedName("assignedto_id")
    public Object assignedtoId;

    @SerializedName("include_all")
    public Boolean includeAll;

    @SerializedName("is_completed")
    public Boolean isCompleted;

    @SerializedName("completed_on")
    public Object completedOn;

    @SerializedName("passed_count")
    public Integer passedCount;

    @SerializedName("blocked_count")
    public Integer blockedCount;

    @SerializedName("untested_count")
    public Integer untestedCount;

    @SerializedName("retest_count")
    public Integer retestCount;

    @SerializedName("failed_count")
    public Integer failedCount;

    @SerializedName("custom_status1_count")
    public Integer customStatus1Count;

    @SerializedName("custom_status2_count")
    public Integer customStatus2Count;

    @SerializedName("custom_status3_count")
    public Integer customStatus3Count;

    @SerializedName("custom_status4_count")
    public Integer customStatus4Count;

    @SerializedName("custom_status5_count")
    public Integer customStatus5Count;

    @SerializedName("custom_status6_count")
    public Integer customStatus6Count;

    @SerializedName("custom_status7_count")
    public Integer customStatus7Count;

    @SerializedName("project_id")
    public Integer projectId;

    @SerializedName("plan_id")
    public Integer planId;

    @SerializedName("entry_index")
    public Integer entryIndex;

    @SerializedName("entry_id")
    public String entryId;

    @SerializedName("config")
    public Object config;

    @SerializedName("config_ids")
    public List<Object> configIds = null;

    @SerializedName("created_on")
    public Integer createdOn;

    @SerializedName("created_by")
    public Integer createdBy;

    @SerializedName("url")
    public String url;

}
