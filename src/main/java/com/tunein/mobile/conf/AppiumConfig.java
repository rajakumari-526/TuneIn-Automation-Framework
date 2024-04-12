package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface AppiumConfig extends Config {

    @Key("appium.host")
    @DefaultValue("localhost")
    String appiumHost();

    @Key("appium.port")
    @DefaultValue("4723")
    int appiumPort();

    @Key("appium.log.level")
    @DefaultValue("debug")
    String appiumLogLevel();

    @Key("appium.use.prebuilt.wda")
    @DefaultValue("false")
    boolean appiumUsePrebuiltWDA();

    @Key("appium.use.prelaunched.server")
    @DefaultValue("false")
    boolean appiumUsePrelaunchedServer();

    @Key("appium.show.xcode.log")
    @DefaultValue("false")
    boolean appiumShowXcodeLog();

    @Key("appium.headless.mode")
    @DefaultValue("false")
    boolean appiumHeadlessMode();

    @Key("appium.start.retry.number")
    @DefaultValue("2")
    int appiumStartRetryNumber();

    @Key("appium.test.retry.number")
    @DefaultValue("0")
    int appiumTestRetryNumber();

    @Key("appium.auto.launch.app")
    @DefaultValue("false")
    boolean appAutoLaunch();

    @Key("screenshot.for.passed.tests")
    @DefaultValue("false")
    boolean screenshotForPassedTests();

    @Key("appium.real.device.uninstall.tunein.app")
    @DefaultValue("false")
    boolean appiumUninstallAppAfterTest();

    @Key("appium.real.device.install.tunein.app")
    @DefaultValue("false")
    boolean appiumInstallAppBeforeTest();

    @Key("post.screenshot.in.before.method")
    @DefaultValue("false")
    boolean postScreenshotInBeforeMethod();

}
