package com.tunein.mobile.deviceactions;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.CommandLineProgramUtil.getOutputOfExecution;
import static com.tunein.mobile.utils.CommandLineProgramUtil.runCommandLine;
import static com.tunein.mobile.utils.HeadspinUtil.*;

public class AndroidDeviceNativeActions extends DeviceNativeActions {

    @Step("Get device memroy info {memoryInfoType}")
    @Override
    public int getDeviceMemoryInfo(MemoryInfoType memoryInfoType) {
        List<List<Object>> listOfMemoryInfo = ((AndroidDriver) getAppiumDriver()).getPerformanceData(config().appPackageAndroid(), "memoryinfo", 10);
        return Integer.parseInt((String) listOfMemoryInfo.get(1).get(listOfMemoryInfo.get(0).indexOf(memoryInfoType.getMemoryInfoTypeName())));
    }

    @Step("Click on device back button")
    @Override
    public DeviceNativeActions clickBackButton() {
        try {
            ((AndroidDriver) getAppiumDriver()).pressKey(new KeyEvent(AndroidKey.BACK));
        } catch (WebDriverException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    @Step
    @Override
    public DeviceNativeActions clickVolumeDownButton(int times) {
        AndroidDriver driver = ((AndroidDriver) getAppiumDriver());
        for (int i = 0; i < times; i++) {
            driver.pressKey(new KeyEvent(AndroidKey.VOLUME_DOWN));
        }
        return this;
    }

    @Step
    @Override
    public void hideKeyboard() {
        ((AndroidDriver) getAppiumDriver()).hideKeyboard();
    }

    @Step
    @Override
    public boolean isKeyboardShown() {
        return ((AndroidDriver) getAppiumDriver()).isKeyboardShown();
    }

    @Override
    public String getClipboardText() {
        return ((AndroidDriver) getAppiumDriver()).getClipboardText();
    }

    @Step("Disable Wi-Fi")
    @Override
    public void disableWiFi() {
        ((AndroidDriver) getAppiumDriver()).setConnection(new ConnectionStateBuilder().withWiFiDisabled().build());
    }

    @Step("Enable Wi-Fi")
    @Override
    public void enableWifi() {
        if (!isDeviceConnectedToNetwork()) {
            ((AndroidDriver) getAppiumDriver()).setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
            long start = System.currentTimeMillis();
            while (!isDeviceConnectedToNetwork()) {
                if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                    ReporterUtil.log("Device is not connected to network");
                }
            }
        }
    }

    private boolean isDeviceConnectedToNetwork() {
        try {
            Process process = Runtime.getRuntime()
                    .exec("adb -s " + getUDID() + " shell dumpsys connectivity | grep 'Active default network: none'");
            String output = (new BufferedReader(new InputStreamReader(process.getInputStream()))).readLine();
            if (output == null) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Step
    @Override
    public void setOrientationMode(ScreenOrientation screenOrientation) {
        ((AndroidDriver) getAppiumDriver()).rotate(screenOrientation);
    }

    @Step
    @Override
    public ScreenOrientation getOrientationMode() {
        return ((AndroidDriver) getAppiumDriver()).getOrientation();
    }

    @Step
    @Override
    public void disableWelcomeChrome() {
        runCommandLine("adb -s " + getUDID() + " shell am set-debug-app --persistent com.android.chrome");
        runCommandLine("adb -s " + getUDID() + " shell 'echo \"chrome --disable-fre --no-default-browser-check --no-first-run\" > /data/local/tmp/chrome-command-line'");
        runCommandLine("adb -s " + getUDID() + " shell pm grant com.android.chrome android.permission.POST_NOTIFICATIONS");
    }

    @Step("Сlear cache for TuneIn app")
    @Override
    public void clearCache() {
        runCommandLine("adb -s " + getUDID() + " shell su -c 'rm -rf /data/data/tunein.player/cache/*");
    }

    @Step("Clear data for TuneIn app")
    @Override
    public void clearData() {
        runCommandLine("adb -s " + getUDID() + " shell pm clear tunein.player");
    }

    @Step("Lock device")
    @Override
    public void lockDevice() {
        if (config().testOnRealDevices()) {
            runAdbShellCommandInHeadspin("input keyevent 26");
        } else {
            ((AndroidDriver) getAppiumDriver()).lockDevice();
        }
    }

    @Step("Unlock device")
    @Override
    public void unlockDevice() {
        if (config().testOnRealDevices()) {
            runAdbShellCommandInHeadspin("input keyevent 82");
        } else {
            ((AndroidDriver) getAppiumDriver()).unlockDevice();
        }
    }

    @Override
    public void changeScreenDensity(String deviceUDID, int dimension) {
        if (config().testOnRealDevices()) {
            setDensityValueForHeadspinDevice(deviceUDID, dimension);
        } else {
            runCommandLine("adb -s " + deviceUDID + " shell wm density " + dimension);
        }
    }

    @Override
    public String getCurrentDensityValue(String deviceUDID) {
        if (config().testOnRealDevices()) {
            return getDensityInfoForHeadspinDevice(deviceUDID);
        } else {
            return getOutputOfExecution("adb -s " + deviceUDID + " shell wm density");
        }
    }

    @Override
    public String getProcStatsDuringHours(String deviceUDID, int hours) {
         String procstats = "";
        if (config().testOnRealDevices()) {
            procstats = getProcStatsForDeviceDuringHours(deviceUDID, hours);
        } else {
            procstats = getOutputOfExecution("adb -s " + deviceUDID + " shell dumpsys procstats --hours " + hours);
        }
        String[] arrayOfProcstats = StringUtils.substringAfter(StringUtils.substringBefore(StringUtils.substringAfter(procstats, "tunein.player /"), " over"), "TOTAL:").split("/");
        String[] rawValues = arrayOfProcstats[0].split("-");
       return rawValues[rawValues.length - 1];
    }

}
