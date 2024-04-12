package com.tunein.mobile.testdata.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Contents {

    @Setter(AccessLevel.NONE)
    String searchQuery;

    @Setter(AccessLevel.NONE)
    String streamName;

    @Setter(AccessLevel.NONE)
    String streamSlogan;

    @Setter(AccessLevel.NONE)
    String streamProfileDeepLink;

    @Setter(AccessLevel.NONE)
    String streamTuneDeepLink;

    @Setter(AccessLevel.NONE)
    String streamType;

    @Setter(AccessLevel.NONE)
    String contentProfileType;

    @Setter(AccessLevel.NONE)
    boolean streamHasShows;

    @Setter(AccessLevel.NONE)
    String[] streamFormat;

    @Setter(AccessLevel.NONE)
    boolean isPremium;

    public static String getStreamId(Contents content) {
        return content.getSearchQuery().replaceAll("aa6-", "");
    }

    public static String getStreamFormat(Contents content) {
            return content.getStreamFormat()[0];
    }

}
