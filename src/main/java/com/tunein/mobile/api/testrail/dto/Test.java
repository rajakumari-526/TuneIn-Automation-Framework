package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Test {

    @SerializedName("offset")
    public Integer offset;

    @SerializedName("limit")
    public String limit;

    @SerializedName("size")
    public Integer size;

    @SerializedName("_links")
    public Link links;

    @SerializedName("tests")
    public Tests[] tests;

    public class Link {

        @SerializedName("next")
        public String next;

        @SerializedName("prev")
        public String prev;
    }

    public class Tests {
        @SerializedName("id")
        public Integer id;

        @SerializedName("case_id")
        public Integer caseId;

        @SerializedName("status_id")
        public Integer statusId;

        @SerializedName("assignedto_id")
        public Object assignedtoId;

        @SerializedName("run_id")
        public Integer runId;

        @SerializedName("title")
        public String title;

        @SerializedName("template_id")
        public Integer templateId;

        @SerializedName("type_id")
        public Integer typeId;

        @SerializedName("priority_id")
        public Integer priorityId;

        @SerializedName("estimate")
        public Object estimate;

        @SerializedName("estimate_forecast")
        public Object estimateForecast;

        @SerializedName("refs")
        public Object refs;

        @SerializedName("milestone_id")
        public Object milestoneId;

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
}
