package com.tunein.mobile.appium.driverprovider;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.appium.AppiumDriverRunner;
import com.codeborne.selenide.appium.SelenideAppium;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.AppiumDriver;

import static com.tunein.mobile.pages.BasePage.isIos;

public class AppiumDriverProvider {

    public static AppiumDriver getAppiumDriver() {
        try {
            return isIos() ? AppiumDriverRunner.getIosDriver() : AppiumDriverRunner.getAndroidDriver();
        } catch (IllegalStateException ex) {
            return createAppiumDriver();
        }
    }

    @Step("Create Appium driver")
    public static AppiumDriver createAppiumDriver() {
            Configuration.webdriverLogsEnabled = true;
            Configuration.screenshots = false;
            SelenideAppium.launchApp();
            return isIos() ? AppiumDriverRunner.getIosDriver() : AppiumDriverRunner.getAndroidDriver();
    }

    @Step("Close Appium driver")
    public static void quiteAppiumDriver() {
        try {
            getAppiumDriver().quit();
            ReporterUtil.log("Close Appium driver");
        } catch (Throwable ex) {
        }
    }

}
