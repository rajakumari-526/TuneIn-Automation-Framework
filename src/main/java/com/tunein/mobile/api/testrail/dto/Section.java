package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Section {

    @SerializedName("id")
    public Integer id;

    @SerializedName("suite_id")
    public Integer suiteId;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public Object description;

    @SerializedName("parent_id")
    public Integer parentId;

    @SerializedName("display_order")
    public Integer displayOrder;

    @SerializedName("depth")
    public Integer depth;

}
