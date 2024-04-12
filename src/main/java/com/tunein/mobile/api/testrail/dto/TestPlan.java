package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class TestPlan {

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public Object description;

    @SerializedName("milestone_id")
    public Object milestoneId;

    @SerializedName("assignedto_id")
    public Object assignedtoId;

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

    @SerializedName("created_on")
    public Integer createdOn;

    @SerializedName("created_by")
    public Integer createdBy;

    @SerializedName("url")
    public String url;

    @SerializedName("entries")
    public List<Entry> entries = null;

}
