package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface HeadspinConfig extends Config {

    /** Token should be passed to gradle */
    @Key("appium.stream.test.enabled")
    @DefaultValue("false")
    boolean isAppiumStreamTestEnabled();

    /** Token should be passed to gradle */
    @Key("appium.stream.headspin.token")
    String appiumHeadspinToken();

    /** Headspin app id of the previously uploaded TuneIn App */
    @Key("appium.headspin.app.id")
    String appiumHeadspinAppId();

    //TODO generate token from headspin API response
    // example https://dev-us-pao-1.headspin.io:7030/v0/{TOKEN}}/wd/hub
    @Key("appium.stream.headspin.driver.url")
    String appiumHeadspinDriverUrl();

    /** headspin:capture.video value */
    @Key("appium.stream.headspin.capture.video")
    @DefaultValue("true")
    String appiumHeadspinCaptureVideo();

    /** headspin:capture.network value */
    @Key("appium.stream.headspin.capture.network")
    @DefaultValue("true")
    String appiumHeadspinCaptureNetwork();

    /** headspin:capture.disableHttp2 */
    @Key("appium.stream.headspin.disable.http2")
    @DefaultValue("true")
    String appiumHeadspinDisableHttp2();

    @Key("headspin.app.path")
    String headspinAppPath();

    @Key("headspin.ios.host")
    @DefaultValue("dev-us-sny-3.headspin.io:7007")
    String headspinIosHost();

    @Key("headspin.ios.device.host")
    @DefaultValue("dev-us-sny-3-proxy-11-mac.headspin.io")
    String headspinIosDeviceHost();

    @Key("headspin.android.host")
    @DefaultValue("dev-us-sny-3.headspin.io:7008")
    String headspinAndroidHost();

    @Key("headspin.android.device.host")
    @DefaultValue("dev-us-sny-3-proxy-12-lin.headspin.io")
    String headspinAndroidDeviceHost();

    @Key("headspin.android.host")
    @DefaultValue("appium-dev.headspin.io:443")
    String headspinLoadBalancerUrl();

    @Key("headspin.api.url")
    @DefaultValue("api-dev.headspin.io")
    String headspinApiUrl();
}
