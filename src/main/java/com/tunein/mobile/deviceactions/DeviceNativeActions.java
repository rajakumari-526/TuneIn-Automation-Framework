package com.tunein.mobile.deviceactions;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import org.openqa.selenium.ScreenOrientation;

import java.util.Arrays;

import static com.tunein.mobile.pages.BasePage.clickOnElement;
import static com.tunein.mobile.utils.ElementHelper.ElementSide.CENTER;

public abstract class DeviceNativeActions {

    @Step("Type {text}")
    public void typeText(SelenideElement element, String text, boolean... dontHideKeyboard) {
            clickOnElement(element, CENTER);
            element.clear();
            element.setValue(text);
            if (dontHideKeyboard.length > 0 && dontHideKeyboard[0]) {
                return;
            }
            hideKeyboard();
    }

    @Step("Clear text for {element}")
    public void clearText(SelenideElement element) {
        clickOnElement(element);
        element.clear();
        hideKeyboard();
    }

    public abstract int getDeviceMemoryInfo(MemoryInfoType memoryInfoType);

    public abstract DeviceNativeActions clickBackButton();

    public abstract DeviceNativeActions clickVolumeDownButton(int times);

    public abstract void hideKeyboard();

    public abstract boolean isKeyboardShown();

    public abstract String getClipboardText();

    public abstract void disableWiFi();

    public abstract void enableWifi();

    public abstract void setOrientationMode(ScreenOrientation screenOrientation);

    public abstract ScreenOrientation getOrientationMode();

    public abstract void disableWelcomeChrome();

    public abstract void clearCache();

    public abstract void clearData();

    public abstract void lockDevice();

    public abstract void unlockDevice();

    public abstract void changeScreenDensity(String deviceUDID, int dimension);

    public abstract String getCurrentDensityValue(String deviceUDID);

    @Step("Get procstats during {hours} hours")
    public abstract String getProcStatsDuringHours(String deviceUDID, int hours);

    public enum MemoryInfoType {

        TOTAL_PRIVATE_DIRTY(0, "totalPrivateDirty"),
        NATIVE_PRIVATE_DIRTY(1, "nativePrivateDirty"),
        DALVIK_PRIVATE_DIRTY(2, "dalvikPrivateDirty"),
        EGL_PRIVATE_DIRTY(3, "eglPrivateDirty"),
        GL_PRIVATE_DIRTY(4, "glPrivateDirty"),
        TOTAL_PSS(5, "totalPss"),
        NATIVE_PSS(6, "nativePss"),
        DALVIK_PSS(7, "dalvikPss"),
        EGL_PSS(8, "eglPss"),
        GL_PSS(9, "glPss"),
        NATIVE_HEAP_ALLOCATED_SIZE(10, "nativeHeapAllocatedSize"),
        NATIVE_HEAP_SIZE(11, "nativeHeapSize"),
        NATIVE_RSS(12, "nativeRss"),
        DALVIK_RSS(13, "dalvikRss"),
        TOTAL_RSS(14, "totalRss");

        private int memoryInfoIndex;

        private String memoryInfoTypeName;

        MemoryInfoType(int memoryInfoIndex, String memoryInfoTypeName) {
            this.memoryInfoIndex = memoryInfoIndex;
            this.memoryInfoTypeName = memoryInfoTypeName;
        }

        public int getMemoryInfoIndex() {
            return memoryInfoIndex;
        }

        public String getMemoryInfoTypeName() {
            return memoryInfoTypeName;
        }

        public static MemoryInfoType findMemoryInfoType(int memoryInfoIndex) {
            return Arrays.stream(MemoryInfoType.values()).
                    filter(memoryInfo -> memoryInfo.getMemoryInfoIndex() == memoryInfoIndex).
                    findFirst()
                    .orElse(null);
        }
    }

}
