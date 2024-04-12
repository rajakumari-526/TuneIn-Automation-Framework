package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class SingleCase {

    @SerializedName("id")
    public Integer id;

    @SerializedName("title")
    public String title;

    @SerializedName("section_id")
    public Integer sectionId;

    @SerializedName("template_id")
    public Integer templateId;

    @SerializedName("type_id")
    public Integer typeId;

    @SerializedName("priority_id")
    public Integer priorityId;

    @SerializedName("milestone_id")
    public Object milestoneId;

    @SerializedName("refs")
    public Object refs;

    @SerializedName("created_by")
    public Integer createdBy;

    @SerializedName("created_on")
    public Integer createdOn;

    @SerializedName("updated_by")
    public Integer updatedBy;

    @SerializedName("updated_on")
    public Integer updatedOn;

    @SerializedName("estimate")
    public Object estimate;

    @SerializedName("estimate_forecast")
    public Object estimateForecast;

    @SerializedName("suite_id")
    public Integer suiteId;

    @SerializedName("custom_preconds")
    public String customPreconds;

    @SerializedName("custom_steps")
    public String customSteps;

    @SerializedName("custom_expected")
    public String customExpected;

    @SerializedName("custom_mission")
    public Object customMission;

    @SerializedName("custom_goals")
    public Object customGoals;

    @SerializedName("custom_platform")
    public List<Integer> customPlatform = null;

}
