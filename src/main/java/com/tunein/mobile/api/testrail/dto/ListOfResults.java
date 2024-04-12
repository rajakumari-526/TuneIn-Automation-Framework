package com.tunein.mobile.api.testrail.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ListOfResults {

    @SerializedName("offset")
    public Integer offset;

    @SerializedName("limit")
    public String limit;

    @SerializedName("size")
    public Integer size;

    @SerializedName("_links")
    public ListOfResults.Link links;

    @SerializedName("results")
    public Result[] results;

    public class Link {

        @SerializedName("next")
        public String next;

        @SerializedName("prev")
        public String prev;
    }

}

