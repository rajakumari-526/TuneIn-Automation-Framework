package com.tunein.mobile.testdata.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Streams {

    private String streamType;

    private String stationName;

    private String stationGuideId;

}
