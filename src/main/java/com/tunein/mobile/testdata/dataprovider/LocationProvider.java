package com.tunein.mobile.testdata.dataprovider;

import org.openqa.selenium.html5.Location;

public class LocationProvider {

    public static Location getLocation(DeviceLocation location) {
        switch (location) {
            case LOC_USA_TUNEIN_SF -> {
                return new Location(37.778610, -122.395290, 10);
            }
            case LOC_GERMANY -> {
                return new Location(50.734380, 7.095480, 100);
            }
            default -> throw new Error("Unsupported location");
        }
    }

    public enum DeviceLocation {
        LOC_USA_TUNEIN_SF, LOC_GERMANY
    }
}
