package com.tunein.mobile.appium.driverprovider;

import com.codeborne.selenide.WebDriverProvider;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;

import static com.tunein.mobile.appium.driverprovider.AppiumSession.*;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.DataUtil.genIntArrayWithValue;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.getDefaultLaunchArgumentsList;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.setAndroidLaunchArguments;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class AndroidDriverProvider implements WebDriverProvider {

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setDeviceName(getDeviceName());
        options.setUdid(getUDID());
        options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(config().twoMinuteInSeconds()));

        if (config().testOnRealDevices()) {
            options.setCapability("noReset", true);
            options.setCapability("headspin:controlLock", true);
            options.setCapability("headspin:appiumVersion", "2.0.0");
            options.setCapability("headspin:capture.video", config().appiumHeadspinCaptureVideo());
            options.setCapability("appium:autoLaunch", true);
            if (config().isAppiumStreamTestEnabled() || config().stabilityTests()) {
                // Individual Settings, if set, the individual settings take precedence
                options.setCapability("headspin:capture.network", System.getProperty("appium.stream.headspin.capture.network"));
                // Prevent the device from using HTTP/2 during network capture.
                options.setCapability("headspin:capture.disableHttp2", config().appiumHeadspinDisableHttp2());
            }
        } else {
            options.setApp(config().appPath());
            options.setCapability("appium:autoLaunch", config().appAutoLaunch());
        }

        options.setPlatformName("Android");
        options.setAutoGrantPermissions(true);
        options.setAppPackage(config().appPackageAndroid());
        options.setAppActivity(config().appActivityAndroid());
        options.setMjpegServerPort(AppiumSession.getMjpegServerPort());
        options.setSystemPort(AppiumSession.getSystemPort());
        options.setChromedriverPorts(Arrays.asList(ArrayUtils.toObject(genIntArrayWithValue(6060, 50))));
        options.setChromedriverChromeMappingFile(Paths.get(".").normalize().toAbsolutePath() + "/src/main/resources/chromedrivers/mapping.json");
        options.setChromedriverExecutableDir(Paths.get(".").normalize().toAbsolutePath() + "/src/main/resources/chromedrivers");
        options.setLanguage("en");
        options.setLocale("US");
        options.setIsHeadless(config().appiumHeadlessMode());
        options.setNewCommandTimeout(Duration.ofSeconds(config().newCommandTimeoutInSeconds()));
        options.setSkipUnlock(true);
        options.setAppWaitDuration(Duration.ofSeconds(config().oneMinuteInSeconds()));
        options.setAdbExecTimeout(Duration.ofSeconds(config().twoMinuteInSeconds()));
        options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(config().twoMinuteInSeconds()));

        setAndroidLaunchArguments(String.join(" ", getDefaultLaunchArgumentsList()));
        String headspinHost = String.format("https://%s/v0/%s/wd/hub", config().headspinLoadBalancerUrl(), config().appiumHeadspinToken());
        for (int i = 0; i < config().appiumStartRetryNumber(); i++) {
            try {
            String host = config().testOnRealDevices()
                        ? headspinHost
                        : String.format("http://%s:%s", config().appiumHost(), AppiumSession.getAppiumPort());
                ReporterUtil.log("Build AndroidDriver on port: " + options.getSystemPort());
                return new AndroidDriver(new URL(host), options);
            } catch (Exception e) {
                e.printStackTrace();
                options.setSystemPort(setNewSystemPort());
                ReporterUtil.log("Trying to rebuild AndroidDriver: " + options.getSystemPort());
                customWait(Duration.ofMillis(config().waitCustomTimeoutMilliseconds()));
            }
        }
        throw new Error("Cannot build AndroidDriver");
    }

}
