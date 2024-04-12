package com.tunein.mobile.appium.driverprovider;

import com.codeborne.selenide.WebDriverProvider;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.ios.options.simulator.Permissions;
import io.appium.java_client.remote.AutomationName;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import java.net.URL;
import java.time.Duration;

import static com.tunein.mobile.appium.driverprovider.AppiumSession.*;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.mitmproxy.MitmproxyDriverProvider.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.getDefaultLaunchArguments;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class IosDriverProvider implements WebDriverProvider {
    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        XCUITestOptions options = new XCUITestOptions();
        options.merge(capabilities);
        options.setAutomationName(AutomationName.IOS_XCUI_TEST);
        options.setPlatformName("iOS");
        options.setWdaLaunchTimeout(Duration.ofMinutes(10));
        // Set headspin device or simulator name
        options.setDeviceName(getDeviceName());

        // Setting up UDID for the headspin device or iOS simulator
        options.setUdid(getUDID());

        if (config().testOnRealDevices()) {
            options.setCapability("headspin:controlLock", true);
            if (config().isAppiumStreamTestEnabled()) {
                // Individual Settings, if set, the individual settings take precedence
                options.setCapability("headspin:capture.network", config().appiumHeadspinCaptureNetwork());
                // Prevent the device from using HTTP/2 during network capture.
                options.setCapability("headspin:capture.disableHttp2", config().appiumHeadspinDisableHttp2());
                // Enable video capture
                options.setCapability("headspin:capture.video", config().appiumHeadspinCaptureVideo());
            }
        } else {
            // setPermissions capability does not work with headspin devices
            options.setPermissions(new Permissions("{\"" + config().bundleIdIos() + "\": {\"userTracking\": \"YES\", \"location\": \"yes\"}}"));
            options.setApp(config().appPath());
        }

        options.setBundleId(config().bundleIdIos());
        options.setCapability("locationServicesAuthorized", true);
        options.setPlatformVersion(config().platformVersion());
        options.setWdaLocalPort(AppiumSession.getSystemPort());
        options.setDerivedDataPath(AppiumSession.getDerivedDataPath());
        options.setLanguage("en");
        options.setLocale("en_US");
        options.setProcessArguments(getDefaultLaunchArguments());
        options.setNewCommandTimeout(Duration.ofSeconds(config().newCommandTimeoutInSeconds()));
        options.setShowXcodeLog(config().appiumShowXcodeLog());
        options.setUsePrebuiltWda(config().appiumUsePrebuiltWDA());
        options.setIsHeadless(config().appiumHeadlessMode());
        options.setMaxTypingFrequency(20);
        options.setScreenshotQuality(2);
        options.setWdaConnectionTimeout(Duration.ofSeconds(config().fiveMinutesInSeconds()));
        options.setWdaLaunchTimeout(Duration.ofSeconds(config().fiveMinutesInSeconds()));
        options.setSimulatorStartupTimeout(Duration.ofSeconds(config().newCommandTimeoutInSeconds()));
        if (config().isReportingTesting()) {
            enableProxyOnMacOSMachine(getProxyPort());
            setNewBypassingDomains();
        }

        for (int i = 0; i < config().appiumStartRetryNumber(); i++) {
            try {
                String host = config().testOnRealDevices()
                        ? String.format("https://%s/v0/%s/wd/hub", config().headspinIosHost(), config().appiumHeadspinToken())
                        : String.format("http://%s:%s", config().appiumHost(), AppiumSession.getAppiumPort());
                ReporterUtil.log("Build IOSDriver on port: " + options.getWdaLocalPort());
                IOSDriver driver = new IOSDriver(new URL(host), options);
                return driver;
            } catch (Exception e) {
                e.printStackTrace();
                ReporterUtil.log("Trying to rebuild IOSDriver on port: " + getSystemPort());
                customWait(Duration.ofSeconds(90));
            }
        }
        throw new Error("Cannot build IOSDriver");
    }
}
