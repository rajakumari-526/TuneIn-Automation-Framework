package com.tunein.mobile.deviceactions;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.ScreenOrientation;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;

public class IosDeviceNativeActions extends DeviceNativeActions {

    @Override
    public int getDeviceMemoryInfo(MemoryInfoType memoryInfoType) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Step
    @Override
    public DeviceNativeActions clickBackButton() {
        getAppiumDriver().navigate().back();
        return this;
    }

    @Override
    public DeviceNativeActions clickVolumeDownButton(int times) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Override
    public void hideKeyboard() {
        ReporterUtil.log("Hide keyboard method fails on iOS");
    }

    @Step
    @Override
    public boolean isKeyboardShown() {
        return ((IOSDriver) getAppiumDriver()).isKeyboardShown();
    }

    @Override
    public String getClipboardText() {
        return ((IOSDriver) getAppiumDriver()).getClipboardText();
    }

    @Override
    public void disableWiFi() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");

    }

    @Override
    public void enableWifi() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Step
    @Override
    public void setOrientationMode(ScreenOrientation screenOrientation) {
        ((IOSDriver) getAppiumDriver()).rotate(screenOrientation);
    }

    @Step
    @Override
    public ScreenOrientation getOrientationMode() {
        return ((IOSDriver) getAppiumDriver()).getOrientation();
    }

    @Override
    public void disableWelcomeChrome() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
    }

    @Override
    public void clearCache() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
    }

    @Override
    public void clearData() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
    }

    @Step("Lock device")
    @Override
    public void lockDevice() {
        ((IOSDriver) getAppiumDriver()).lockDevice();
    }

    @Step("Unlock device")
    @Override
    public void unlockDevice() {
        ((IOSDriver) getAppiumDriver()).unlockDevice();
    }

    @Override
    public void changeScreenDensity(String deviceUDID, int dimension) {
        ReporterUtil.log("Functionality is absent for iOS Platform");
    }

    @Override
    public String getCurrentDensityValue(String deviceUDID) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Override
    public String getProcStatsDuringHours(String deviceUDID, int hours) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

}
