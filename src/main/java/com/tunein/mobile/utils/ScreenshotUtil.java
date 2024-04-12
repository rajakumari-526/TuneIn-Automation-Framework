package com.tunein.mobile.utils;

import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;

public class ScreenshotUtil {

    private static final String SCREENSHOTS_FOLDER_PATH = "build/test-screenshots";

    public static void takeScreenshot(ITestResult result) {
        try {
            File srcFile = getAppiumDriver().getScreenshotAs(OutputType.FILE);
            Date dateNow = new Date();
            SimpleDateFormat format = new SimpleDateFormat("_yyyy-MM-dd_hh-mm-SS");

            String nameOfScreenshot = (config().testOnRealDevices())
                    ? result.getName() + format.format(dateNow) + "_" + getAppiumDriver().getSessionId()
                    : result.getName() + format.format(dateNow);

            File theDir = new File(SCREENSHOTS_FOLDER_PATH);
            if (!theDir.exists()) theDir.mkdirs();
            File targetFile = new File(SCREENSHOTS_FOLDER_PATH + "/" + nameOfScreenshot + ".jpg");
            try {
                Files.move(srcFile, targetFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ReporterUtil.log(targetFile, "Screenshot");
        } catch (Exception e) {
            e.printStackTrace();
            ReporterUtil.log(" Unable to take screenshot");
        }
    }

    public static void takeScreenshot() {
        File srcFile = getAppiumDriver().getScreenshotAs(OutputType.FILE);
        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("_yyyy-MM-dd_hh-mm-ss-SS");

        String nameOfScreenshot = (config().testOnRealDevices())
                ? format.format(dateNow) + "_" + getAppiumDriver().getSessionId()
                : format.format(dateNow);

        File theDir = new File(SCREENSHOTS_FOLDER_PATH);
        if (!theDir.exists()) theDir.mkdirs();
        File targetFile = new File(SCREENSHOTS_FOLDER_PATH + "/" + nameOfScreenshot + ".jpg");
        try {
            Files.move(srcFile, targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ReporterUtil.log(targetFile, "Screenshot");
    }

}
