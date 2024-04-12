package com.tunein.mobile.testdata.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HeadspinStreams {

    @Setter(AccessLevel.NONE)
    private String deviceName;

    @Setter(AccessLevel.NONE)
    private String stationName;

    @Setter(AccessLevel.NONE)
    private String stationId;

    @Setter(AccessLevel.NONE)
    private String streamType;

    @Setter(AccessLevel.NONE)
    private String networkType;

}
