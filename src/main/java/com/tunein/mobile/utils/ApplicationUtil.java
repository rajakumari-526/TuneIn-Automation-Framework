package com.tunein.mobile.utils;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.SelenideAppium;
import com.epam.reportportal.annotations.Step;
import com.google.common.collect.ImmutableMap;
import com.tunein.mobile.pages.ScreenFacade;
import com.tunein.mobile.testdata.dataprovider.LocationProvider.DeviceLocation;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.Location;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Selenide.$;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.pages.alert.IosAlert.handleSimpleAlertIfPresent;
import static com.tunein.mobile.testdata.dataprovider.LocationProvider.getLocation;
import static com.tunein.mobile.utils.CommandLineProgramUtil.*;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static com.tunein.mobile.utils.HeadspinUtil.runAdbShellCommandInHeadspin;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.APP_ENVIRONMENT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.appmanagement.ApplicationState.NOT_RUNNING;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;

public class ApplicationUtil extends ScreenFacade {

    private static final String ANDROID = "android";

    private static final String IOS = "ios";

    private static Map<Long, Boolean> notificationPermissionMap = new HashMap<>();

    private static Map<Long, Boolean> deepLinkPermissionMap = new HashMap<>();

    private static Map<Long, Boolean> locationPermissionMap = new HashMap<>();

    private static Map<Long, Boolean> trackingPermissionMap = new HashMap<>();

    private static Map<Long, Boolean> chooseGoogleAccountMap = new HashMap<>();

    public static String getBundleId() {
        return isAndroid() ? config().appPackageAndroid() : config().bundleIdIos();
    }

    public static synchronized void setNotificationPermissionValue(Long threadId, Boolean isNotificationPermissionSet) {
        notificationPermissionMap.put(threadId, isNotificationPermissionSet);
    }

    public static synchronized Boolean isNotificationPermissionValueSet() {
        return notificationPermissionMap.get(Thread.currentThread().getId()) != null
                && notificationPermissionMap.get(Thread.currentThread().getId());
    }

    public static synchronized void setDeeplinkPermissionValue(Long threadId, Boolean isDeepLinkPermissionSet) {
        deepLinkPermissionMap.put(threadId, isDeepLinkPermissionSet);
    }

    public static synchronized void setLocationPermissionValue(Long threadId, Boolean isLocationPermissionSet) {
        locationPermissionMap.put(threadId, isLocationPermissionSet);
    }

    public static synchronized void setTrackingPermissionValue(Long threadId, Boolean isTrackingPermissionSet) {
        trackingPermissionMap.put(threadId, isTrackingPermissionSet);
    }

    public static synchronized void setChooseGoogleAccountValue(Long threadId, Boolean isChooseGoogleAccountSet) {
        chooseGoogleAccountMap.put(threadId, isChooseGoogleAccountSet);
    }

    public static synchronized Boolean isDeeplinkPermissionValueSet() {
        if (!isDeeplinkPermissionValueIsNull()) {
            return deepLinkPermissionMap.get(Thread.currentThread().getId());
        }
        return false;
    }

    public static synchronized Boolean isChooseGoogleAccountValueSet() {
        if (!isChooseGoogleAccountValueIsNull()) {
            return chooseGoogleAccountMap.get(Thread.currentThread().getId());
        }
        return false;
    }

    public static synchronized Boolean isLocationPermissionValueSet() {
        if (!isLocationPermissionValueIsNull()) {
            return locationPermissionMap.get(Thread.currentThread().getId());
        }
        return false;
    }

    public static synchronized Boolean isTrackingPermissionValueSet() {
        if (!isTrackingPermissionValueIsNull()) {
            return trackingPermissionMap.get(Thread.currentThread().getId());
        }
        return false;
    }

    public static synchronized Boolean isDeeplinkPermissionValueIsNull() {
        return deepLinkPermissionMap.get(Thread.currentThread().getId()) == null;
    }

    public static synchronized Boolean isChooseGoogleAccountValueIsNull() {
        return chooseGoogleAccountMap.get(Thread.currentThread().getId()) == null;
    }

    public static synchronized Boolean isLocationPermissionValueIsNull() {
        return locationPermissionMap.get(Thread.currentThread().getId()) == null;
    }

    public static synchronized Boolean isTrackingPermissionValueIsNull() {
        return trackingPermissionMap.get(Thread.currentThread().getId()) == null;
    }

    @Step("Uninstall TuneIn Radio app")
    public static void uninstallApp() {
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                try {
                    ((AndroidDriver) getAppiumDriver()).removeApp(getBundleId());
                } catch (Exception exception) {
                    ReporterUtil.log("Cannot uninstall app");
                }
                setChooseGoogleAccountValue(getThreadId(), false);
            }
            case IOS -> {
                HashMap<String, Object> arguments = new HashMap<>();
                arguments.put("bundleId", config().bundleIdIos());
                getAppiumDriver().executeScript("mobile: removeApp", arguments);
                setNotificationPermissionValue(getThreadId(), false);
                setTrackingPermissionValue(getThreadId(), false);
                setLocationPermissionValue(getThreadId(), false);
            }
            default -> throw new Error("Invalid platform type");
        }
        ReporterUtil.log("Uninstall app");
    }

    @Step
    public static void installApp() {
        if (config().testOnRealDevices()) {
            if (config().appiumInstallAppBeforeTest()) HeadspinUtil.installPreUploadedBuild(HeadspinUtil.getHeadspinAppId());
        } else {
            switch (config().mobileOS().toLowerCase()) {
                case ANDROID -> ((AndroidDriver) getAppiumDriver()).installApp(config().appPath());
                case IOS -> ((IOSDriver) getAppiumDriver()).installApp(config().appPath());
                default -> throw new Error("Invalid platform type");
            }
        }
        ReporterUtil.log("Install app");
    }

    @Step("Launching TuneIn Radio app")
    public static void launchApp(ArrayList<String>... launchArgumentsList) {
        if (!isAppInstalled()) {
            installApp();
        } else {
            terminateApp();
        }
        setRequiredAppPermissions();
        ArrayList<String> listOfLaunchArguments = launchArgumentsList.length > 0 ? launchArgumentsList[0] : getDefaultLaunchArgumentsList();
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                String launchArgumentString = String.join(" ", listOfLaunchArguments);
                adbStartDeviceWithLaunchArguments(launchArgumentString);
                setAndroidLaunchArguments(String.join(" ", launchArgumentString));
            }
            case IOS -> {
                Map<String, String> environmentMap = new HashMap<>();
                environmentMap.put("braze.inAppMessages.discard", "true");
                HashMap<String, Object> arguments = new HashMap<>();
                arguments.put("bundleId", config().bundleIdIos());
                arguments.put("arguments", listOfLaunchArguments);
                arguments.put("environment", environmentMap);
                ReporterUtil.log("Execute script mobile: launchApp");
                getAppiumDriver().executeScript("mobile: launchApp", arguments);
            }
            default -> throw new Error("Invalid platform type");
        }
    }

    @Step("Activating TuneIn Radio app")
    public static void activateApp() {
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                if (config().testOnRealDevices()) {
                    runAdbShellCommandInHeadspin("input keyevent KEYCODE_APP_SWITCH");
                    customWait(Duration.ofSeconds(2));
                    runAdbShellCommandInHeadspin("input keyevent KEYCODE_APP_SWITCH");
                    customWait(Duration.ofSeconds(1));

                } else {
                    runCommandLine("adb -s " + getUDID() + " shell input keyevent KEYCODE_APP_SWITCH");
                    customWait(Duration.ofSeconds(2));
                    runCommandLine("adb -s " + getUDID() + " shell input keyevent KEYCODE_APP_SWITCH");
                    customWait(Duration.ofSeconds(1));
                }
                if (((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid()) != RUNNING_IN_FOREGROUND) {
                    long start = System.currentTimeMillis();
                    while (System.currentTimeMillis() - start < config().twoMinutesInMilliseconds()) {
                        if (config().testOnRealDevices()) {
                            runAdbShellCommandInHeadspin("monkey -p " + config().appPackageAndroid() + " 1");
                        } else {
                            runCommandLine("adb -s " + getUDID() + " shell monkey -p " + config().appPackageAndroid() + " 1");
                        }
                        customWait(Duration.ofSeconds(3));
                        if (isElementNotDisplayed(By.id("leak_canary_bottom_navigation_bar"), Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()))) {
                            return;
                        }
                    }
                    throw new Error("App is not opened through adb");
                }
            }
            case IOS -> ((IOSDriver) getAppiumDriver()).activateApp(getBundleId());
            default -> throw new Error("Invalid platform type");
        }
        ReporterUtil.log("Open app");
    }

    /**
     * Returns application state.
     * @return application state
     */
    @Step("Returns application state")
    public static ApplicationState getAppState() {
        if (isIos()) {
            return ((IOSDriver) getAppiumDriver()).queryAppState(config().bundleIdIos());
        } else {
            return ((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid());
        }
    }

    @Step("Terminating TuneIn Radio app")
    public static void terminateApp() {
        long start = System.currentTimeMillis();
        while (getAppState() != NOT_RUNNING) {
            try {
              SelenideAppium.terminateApp(getBundleId());
            } catch (WebDriverException ex) {
                if (System.currentTimeMillis() - start > Duration.ofSeconds(config().waitLongTimeoutSeconds()).toMillis()) {
                  throw new Error("App cannot be terminated.");
                }
          }
      }
        ReporterUtil.log("App terminated");
    }

    @Step("Run app in background for {duration} seconds")
    public static void runAppInBackground(Duration duration) {
        ReporterUtil.log("Run app in Background");
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                if (config().testOnRealDevices()) {
                    runAdbShellCommandInHeadspin("input keyevent 3");
                } else {
                    runCommandLine("adb -s " + getUDID() + " shell input keyevent 3");
                }
                customWait(Duration.ofSeconds(duration.getSeconds()));
                activateApp();
            }
            case IOS -> ((IOSDriver) getAppiumDriver()).runAppInBackground(Duration.ofSeconds(duration.getSeconds()));
            default -> throw new Error("Invalid platform type");
        }
        ReporterUtil.log("Background app and foreground");
    }

    @Step("Close app")
    public static void closeApp() {
        ReporterUtil.log("Close app");
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                if (config().testOnRealDevices()) {
                    runAdbShellCommandInHeadspin("input keyevent 3");
                } else {
                    runCommandLine("adb -s " + getUDID() + " shell input keyevent 3");
                }
            }
            case IOS -> getAppiumDriver().executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));
            default -> throw new Error("Invalid platform type");
        }
        customWait(Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
    }

    public static boolean isAppInstalled() {
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                return ((AndroidDriver) getAppiumDriver()).isAppInstalled(getBundleId());
            }
            case IOS -> {
                return ((IOSDriver) getAppiumDriver()).isAppInstalled(getBundleId());
            }
            default -> throw new Error("Invalid platform type - " + config().mobileOS());
        }
    }

    @Step("Restart app")
    public static void restartApp(ArrayList<String>... launchArguments) {
        if (launchArguments.length > 0) {
            launchApp(launchArguments[0]);
        } else {
            launchApp();
        }
    }

    @SafeVarargs
    @Step("Reinstall app")
    public static void reinstallApp(ArrayList<String>... launchArguments) {
        uninstallApp();
        launchApp(launchArguments);
    }

    @Step("Install old version of app from resources for upgrade testing")
    public void downgradeApp(boolean... skipLaunchApp) {
        uninstallApp();
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> ((AndroidDriver) getAppiumDriver()).installApp(config().oldAppPath());
            case IOS -> ((IOSDriver) getAppiumDriver()).installApp(config().oldAppPath());
            default -> throw new Error("Invalid platform type");
        }
        ReporterUtil.log("Install old app for upgrade test");
        setRequiredAppPermissions();
        boolean skipLaunchAppAfterDowngrade = skipLaunchApp.length > 0 && skipLaunchApp[0];
        if (!skipLaunchAppAfterDowngrade) launchApp();
    }

    @Step("Upgrade app from older version without uninstalling older version")
    public void upgradeApp() {
        installApp();
        setRequiredAppPermissions();
        launchApp();
        upsellPage.closeUpsell();
        continueListeningDialog.closeContinueListeningDialogIfDisplayed();
        if (nowPlayingPage.isOnNowPlayingPage()) {
            nowPlayingPage
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .minimizeNowPlayingScreen();
        } else {
            homePage.waitUntilPageReady();
        }
    }

    // TODO: Add possibility to choose which permission should be given
    @Step("Set \"User Tracking\" and \"Location\" permissions")
    public static void setRequiredAppPermissions() {
        if (isIos() & !config().testOnRealDevices()) {
                HashMap<String, Object> permissions = new HashMap<>();
                permissions.put("userTracking", "NO");
                permissions.put("location", "yes");

                HashMap<String, Object> arguments = new HashMap<>();
                arguments.put("bundleId", config().bundleIdIos());
                arguments.put("access", permissions);

                getAppiumDriver().executeScript("mobile: setPermission", arguments);
                ReporterUtil.log("\"userTracking\" and \"location\" permissions are set");
            } else {
            adbGrantAllPermissions();
        }
    }

    //Set location doesn't work with headless mode in bitrise
    @Step("Set device location {deviceLocation}")
    public static void setDeviceLocation(DeviceLocation deviceLocation) {
        if (isIos()) {
            Location location = getLocation(deviceLocation);
            ((IOSDriver) getAppiumDriver()).setLocation(location);
            restartApp();
        }
    }

    public static void closePermissionPopupFor(ApplicationPermission permissionType) {
            switch (permissionType) {
                case NOTIFICATIONS -> {
                    if (!isNotificationPermissionValueSet() && isIos()) {
                        if (isPermissionAlertIsDisplayed(permissionType, config().waitShortTimeoutMilliseconds())) {
                            acceptDisplayedPermissionPopup();
                            ReporterUtil.log("Close permission popup for " + permissionType);
                            setNotificationPermissionValue(getThreadId(), true);
                        }
                    }
                }
                case LOCATION -> {
                    if (!isLocationPermissionValueSet() && isIos()) {
                        if (isPermissionAlertIsDisplayed(permissionType, config().waitShortTimeoutMilliseconds())) {
                            handleSimpleAlertIfPresent(permissionType);
                            ReporterUtil.log("Close permission popup for " + permissionType);
                            setLocationPermissionValue(getThreadId(), true);
                        }
                    }
                }
                case USER_TRACKING -> {
                    if (!isTrackingPermissionValueSet() && isIos()) {
                        if (isPermissionAlertIsDisplayed(permissionType, config().waitShortTimeoutMilliseconds())) {
                            handleSimpleAlertIfPresent(permissionType);
                            ReporterUtil.log("Close permission popup for " + permissionType);
                            setTrackingPermissionValue(getThreadId(), true);
                        }
                    }
                }
                case DEEPLINK -> {
                    if (!isDeeplinkPermissionValueSet() && isIos()) {
                        if (isPermissionAlertIsDisplayed(permissionType, config().timeoutForDeeplinkInMilliseconds())) {
                            acceptDisplayedPermissionPopup();
                            ReporterUtil.log("Close 'Open in' popup for the first deeplink in Safari if present " + permissionType);
                            setDeeplinkPermissionValue(getThreadId(), true);
                        }
                    }
                }
                case CHOOSE_GOOGLE_ACCOUNT -> {
                    if (!isChooseGoogleAccountValueSet() && isAndroid()) {
                        if (isPermissionAlertIsDisplayed(permissionType, config().waitShortTimeoutMilliseconds())) {
                            clickOnElementIfDisplayed($(AppiumBy.androidUIAutomator("text(\"" + permissionType.getPermissionButtonName() + "\")")), Duration.ofSeconds(config().waitShortTimeoutSeconds()));
                            ReporterUtil.log("Close 'Choose account' popup");
                            setChooseGoogleAccountValue(getThreadId(), true);
                        }
                    }
                }
                default -> ReporterUtil.log("Invalid permission type \"" + permissionType + "\" for iOS");
            }
    }

    public static List<String> getAlertButtons() {
        List<String> alertButtonsList = new ArrayList<String>();
        HashMap<String, String> alertButtons = new HashMap<>() {{
            put("action", "getButtons");
        }};
        try {
            alertButtonsList = (List<String>) getAppiumDriver().executeScript("mobile: alert", alertButtons);
        } catch (Exception e) {
            ReporterUtil.log("No permission alert detected");
        }
        return alertButtonsList;
    }

    private static boolean isPermissionAlertIsDisplayed(ApplicationPermission permissionType, int timeoutInMilliseconds) {
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> {
                long start = System.currentTimeMillis();
               SelenideElement button = $(AppiumBy.androidUIAutomator("text(\"" + permissionType.getPermissionButtonName() + "\")"));
                while (!isElementDisplayed(button)) {
                    if (System.currentTimeMillis() - start > timeoutInMilliseconds) {
                        return false;
                    }
                }
                return true;
            }
            case IOS -> {
                long start = System.currentTimeMillis();
                while (!getAlertButtons().contains(permissionType.getPermissionButtonName())) {
                    if (System.currentTimeMillis() - start > timeoutInMilliseconds) {
                        return false;
                    }
                }
                return true;
            }
            default -> throw new Error("Invalid platform type");
        }
    }

    @Step("Dismiss displayed permission popup")
    public static void dismissDisplayedPermissionPopup() {
        if (isIos()) {
            try {
                getAppiumDriver().switchTo().alert().dismiss();
            } catch (NoAlertPresentException ex) {
                ex.printStackTrace();
                ReporterUtil.log("Permission popup is not displayed");
            }
        }
    }

    @Step("Accept displayed permission popup")
    public static void acceptDisplayedPermissionPopup() {
        if (isIos()) {
            try {
                getAppiumDriver().switchTo().alert().accept();
            } catch (NoAlertPresentException ex) {
                ex.printStackTrace();
                ReporterUtil.log("Permission popup is not displayed");
            }
        }
    }

    @Step
    public static void waitUntilDeviceBecameActive() {
        if (isAndroid()) {
            long start = System.currentTimeMillis();
            while (!getOutputOfExecution("adb -s " + getUDID() + " get-state").contains("device")) {
                if (System.currentTimeMillis() - start > config().waitExtraLongTimeoutMilliseconds()) {
                    throw new RuntimeException(new Error("Device is not active after " + Duration.ofMillis(config().waitExtraLongTimeoutMilliseconds()).getSeconds() + " seconds"));
                }
            }
            ReporterUtil.log("Device is active");
        }
    }

    @Step
    public static void waitUntilDeviceBecameOffline() {
        if (isAndroid()) {
            long start = System.currentTimeMillis();
            while (!getOutputOfExecution("adb -s " + getUDID() + " get-state").equals("")) {
                if (System.currentTimeMillis() - start > config().waitExtraLongTimeoutMilliseconds()) {
                    ReporterUtil.log("Device is not offline after " + Duration.ofMillis(config().waitExtraLongTimeoutMilliseconds()).getSeconds() + " seconds");
                    return;
                }
            }
            ReporterUtil.log("Device is offline");
        }
    }

    @Step
    public static void adbRebootDevice() {
        if (isAndroid()) {
            ReporterUtil.log("Start rebooting device with udid " + getUDID());
            runCommandLine("adb -s " + getUDID() + " reboot");
            waitUntilDeviceBecameOffline();
            waitUntilDeviceBecameActive();
            ReporterUtil.log("Device with udid " + getUDID() + " is rebooted");

        }
    }

    @Step
    public static void adbRootDevice() {
        if (isAndroid()) {
            ReporterUtil.log("Adb root for device with udid " + getUDID());
            String result = executeCommandUntilSuccessful("adb -s " + getUDID() + " root");
            if (!result.equals("adb is already running as root")) {
                waitUntilDeviceBecameOffline();
                waitUntilDeviceBecameActive();
            }
            ReporterUtil.log("Output: " + result);
        }
    }

    @Step
    public static void adbGrantAllPermissions() {
        if (isAndroid()) {
            if (config().testOnRealDevices()) {
                runAdbShellCommandInHeadspin("pm grant " + config().appPackageAndroid() + " android.permission.ACCESS_COARSE_LOCATION");
                runAdbShellCommandInHeadspin("pm grant " + config().appPackageAndroid() + " android.permission.POST_NOTIFICATIONS", false);
                runAdbShellCommandInHeadspin("settings put global adaptive_battery_management_enabled 0", false);
                runAdbShellCommandInHeadspin("dumpsys deviceidle whitelist +" + config().appPackageAndroid(), false);

            } else {
                for (AndroidPermissions permission : AndroidPermissions.values()) {
                    try {
                        adbExecuteScript("pm grant", Arrays.asList(config().appPackageAndroid(), permission.getPermissionType()));
                    } catch (Throwable e) {
                        ReporterUtil.log("Permission " + permission.getPermissionType() + " is absent and not required");
                    }
                }
                adbExecuteScript("settings put global", Arrays.asList("adaptive_battery_management_enabled 0"));
                adbExecuteScript("dumpsys deviceidle", Arrays.asList("whitelist +" + config().appPackageAndroid()));
            }
        }
    }

    @Step
    public static Object adbExecuteScript(String command, List<String> argumentsList) {
            Map<String, Object> argumentsMap = ImmutableMap.of("command", command, "args", argumentsList);
            return getAppiumDriver().executeScript("mobile: shell", argumentsMap);
    }

    @Step("ADB - Start device with launch arguments: {launchArguments}")
    public static void adbStartDeviceWithLaunchArguments(String launchArguments) {
        if (isAndroid()) {
            if (config().testOnRealDevices()) {
                runAdbShellCommandInHeadspin("am start -n " + config().appPackageAndroid() + "/" + config().appActivityAndroid() + " " + launchArguments);
                long start = System.currentTimeMillis();
                while (((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid()) != RUNNING_IN_FOREGROUND) {
                    if (System.currentTimeMillis() - start > config().waitExtraLongTimeoutMilliseconds()) {
                        throw new Error("App is not launched through adb");
                    }
                }
            } else {
                List<String> launchAppArgs = Arrays.asList("-W", "-n", config().appPackageAndroid() + "/" + config().appActivityAndroid(), launchArguments);
                ReporterUtil.log("Launch app on device: " + getUDID() + ", " + adbExecuteScript("am start", launchAppArgs));
            }
        }
    }

    @Step
    public static void adbRemountDevice() {
        if (isAndroid()) {
            ReporterUtil.log("Adb remount for device with udid " + getUDID());
            ReporterUtil.log("Output: " + executeCommandUntilSuccessful("adb -s " + getUDID() + " remount"));
        }
    }

    //TODO Update functionality for ios when launch argument will be ready.
    // Update functionality for android when default build env will be prod.
    @Step
    public static void setRequiredEnvironment(ApplicationEnvironment environment) {
        if (isAndroid()) {
            waitTillVisibilityOfElement($(By.id("bottom_navigation")));
            runCommandLine("adb -s " + getUDID() + " shell am start -n tunein.player/" + config().appActivityAndroid() + " --es launchEnvironment " + environment.getEnvironment());
            long start = System.currentTimeMillis();
            while (!((AndroidDriver) getAppiumDriver()).currentActivity().contains("HomeActivity")) {
                if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                    throw new Error("Environment:" + environment.getEnvironment() + " is not set within " + config().waitCustomTimeoutMilliseconds() + " ms ");
                }
            }
            restartApp();
        } else {
            updateLaunchArgumentFor(APP_ENVIRONMENT, environment.getEnvironment());
        }
    }

    public static boolean isPortAvailable(int portNr) {
        boolean portFree;
        try (var ignored = new ServerSocket(portNr)) {
            portFree = true;
        } catch (IOException e) {
            portFree = false;
        }
        return portFree;
    }

    public enum ApplicationPermission {
        NOTIFICATIONS("Allow"),
        USER_TRACKING("Ask App Not to Track"),
        LOCATION("Allow While Using App"),
        OK("OK"),
        CLOSE("Close"),
        DEEPLINK("Open"),
        CHOOSE_GOOGLE_ACCOUNT("NONE OF THE ABOVE");

        private String permissionButtonName;

        private ApplicationPermission(String permissionButtonName) {
            this.permissionButtonName = permissionButtonName;
        }

        public String getPermissionButtonName() {
            return permissionButtonName;
        }
    }

    public enum ApplicationEnvironment {
        STAGE("Staging"),
        PROD("Production");

        private String environment;

        private ApplicationEnvironment(String environment) {
            this.environment = environment;
        }

        public String getEnvironment() {
            return environment;
        }
    }

    @Step("Open settings app for Android and iOS")
    public static void activateSettingsApp() {
        switch (config().mobileOS().toLowerCase()) {
            case ANDROID -> ((AndroidDriver) getAppiumDriver()).activateApp("com.android.settings");
            case IOS -> ((IOSDriver) getAppiumDriver()).activateApp("com.apple.Preferences");
            default -> throw new Error("Invalid platform type");
        }
        ReporterUtil.log("Open Setting app");
    }

    //TODO Uncomment permission if they will be enabled on dev side
    public enum AndroidPermissions {
        POST_NOTIFICATIONS("android.permission.POST_NOTIFICATIONS"),
//        ACTION_MANAGE_OVERLAY_PERMISSION("android.permission.ACTION_MANAGE_OVERLAY_PERMISSION"),
        SYSTEM_ALERT_WINDOW("android.permission.SYSTEM_ALERT_WINDOW"),
//        ACCESS_NOTIFICATIONS("android.permission.ACCESS_NOTIFICATIONS"),
        ACCESS_COARSE_LOCATION("android.permission.ACCESS_COARSE_LOCATION");
 //       SCHEDULE_EXACT_ALARM("android.permission.SCHEDULE_EXACT_ALARM");

        private String permissionType;

        private AndroidPermissions(String permissionType) {
            this.permissionType = permissionType;
        }

        public String getPermissionType() {
            return permissionType;
        }
    }

}
