package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface AppConfig extends Config {

    @Key("mobile.os")
    String mobileOS();

    @Key("app.path")
    String appPath();

    @Key("old.app.path")
    String oldAppPath();

    @Key("device.name")
    @DefaultValue("DEFAULT_NAME")
    String deviceName();

    @Key("udid")
    String udid();

    @Key("platform.version")
    String platformVersion();

    @Key("bundle.id.ios")
    @DefaultValue("com.tunein.TuneInRadio")
    String bundleIdIos();

    @Key("apple.app.id")
    @DefaultValue("418987775")
    String appleAppId();

    @Key("app.package.android")
    @DefaultValue("tunein.player")
    String appPackageAndroid();

    @Key("app.activity.android")
    @DefaultValue("tunein.ui.activities.splash.SplashScreenActivity")
    String appActivityAndroid();

    @Key("test.groups")
    @DefaultValue("")
    String testGroups();

    @Key("pr.tests")
    @DefaultValue("")
    String prTests();

    @Key("prod.environment")
    @DefaultValue("true")
    Boolean isProdEnvironment();

    @Key("build.name")
    @DefaultValue("")
    String buildName();

    @Key("skip.tests.without.testcase")
    @DefaultValue("false")
    Boolean skipTestsWithoutTestCase();

    @Key("test.on.real.devices")
    @DefaultValue("false")
    Boolean testOnRealDevices();

}
