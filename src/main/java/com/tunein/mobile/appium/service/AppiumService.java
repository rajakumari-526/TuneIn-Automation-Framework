package com.tunein.mobile.appium.service;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.time.Duration;

import static com.tunein.mobile.appium.driverprovider.AppiumSession.*;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.utils.CommandLineProgramUtil.stopServiceOnPort;

public class AppiumService {

    @Step("Start Appium server")
    public void startServer() {
        if (!config().appiumUsePrelaunchedServer()) {
            stopServer();
            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            builder
                    .usingPort(getAppiumPort())
                    .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, config().appiumLogLevel())
                    .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                    .withArgument(GeneralServerFlag.DEBUG_LOG_SPACING)
                    .withArgument(GeneralServerFlag.ALLOW_INSECURE, "chromedriver_autodownload,adb_shell")
                    .withArgument(GeneralServerFlag.USE_DRIVERS, isAndroid() ? "uiautomator2" : "xcuitest")
                    .withTimeout(Duration.ofSeconds(config().fiveMinutesInSeconds()));
            AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
            ReporterUtil.log("Service url:" + service.getUrl());
            ReporterUtil.log("Start Appium Server on port " + getAppiumPort());
            service.start();
        }
    }

    @Step("Stop server")
    public void stopServer() {
        stopServiceOnPort(getAppiumPort());
    }

}
