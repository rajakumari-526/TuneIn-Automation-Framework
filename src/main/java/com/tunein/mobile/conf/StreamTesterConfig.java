package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface StreamTesterConfig extends Config {

    @Key("appium.stream.write.to.file")
    @DefaultValue("true")
    boolean appiumStreamWriteToFile();

    @Key("appium.stream.file.name")
    //@DefaultValue("player_test_source_streams")
    String appiumStreamFileName();

    @Key("appium.stream.tune.from.deeplink")
    @DefaultValue("false")
    boolean appiumStreamTuneFromDeepLink();

    @Key("appium.stream.monitor.duration")
    @DefaultValue("20")
    int appiumStreamMonitorDuration();

    @Key("appium.master.file.name")
    @DefaultValue("player_test_master.csv")
    String appiumMasterFileName();

    @Key("appium.master.file.path")
    @DefaultValue("/Users/serviceaccount/Downloads/Headspin_Files/master_csv/")
    String appiumMasterFilePath();

    @Key("appium.report.file.path")
    @DefaultValue("/Users/serviceaccount/Downloads/Headspin_Files/")
    String appiumReportFilePath();

    @Key("appium.report.file.name")
    @DefaultValue("player_test_source_streams_results")
    String appiumReportFileName();

    @Key("appium.premium.user.test")
    @DefaultValue("0")
    int appiumPremiumUserTest();

    /** This config is used to set custom  attribute `station_id` when stream test triggered via workflow_dispatch */
    @Key("appium.stream.station.id")
    @DefaultValue("")
    String appiumStreamTestStationId();

    /** This config is used to set custom  attribute `station_name` when stream test triggered via workflow_dispatch */
    @Key("appium.stream.station.name")
    @DefaultValue("")
    String appiumStreamTestStationName();

    /** This config is used to set custom attribute `owner` when stream test triggered via workflow_dispatch */
    @Key("appium.stream.owner.name")
    @DefaultValue("")
    String appiumStreamTestOwnerName();

    /** This config enables internet speed test */
    @Key("appium.stream.internet.speed.test")
    @DefaultValue("true")
    Boolean appiumStreamInternetSpeedTest();

    /** This config key grabs Serial id from the Settings screen */
    @Key("appium.stream.get.serial.id")
    @DefaultValue("false")
    Boolean appiumStreamGetSerialIdFromDevice();

    /** Local Streams.json file path */
    @Key("appium.stream.local.json.file.path")
    @DefaultValue("streams/Streams.json")
    String appiumStreamLocalStreamsJsonPath();
}
