package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Project {

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("announcement")
    public Object announcement;

    @SerializedName("show_announcement")
    public Boolean showAnnouncement;

    @SerializedName("is_completed")
    public Boolean isCompleted;

    @SerializedName("completed_on")
    public Object completedOn;

    @SerializedName("suite_mode")
    public Integer suiteMode;

    @SerializedName("url")
    public String url;

}
