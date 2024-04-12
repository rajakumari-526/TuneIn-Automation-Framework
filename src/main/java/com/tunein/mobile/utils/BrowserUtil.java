package com.tunein.mobile.utils;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.google.common.collect.ImmutableMap;
import com.tunein.mobile.pages.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.DEEPLINK;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.NOTIFICATIONS;
import static com.tunein.mobile.utils.ApplicationUtil.adbExecuteScript;
import static com.tunein.mobile.utils.ApplicationUtil.closePermissionPopupFor;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.NAME;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.TEXT;
import static com.tunein.mobile.utils.ElementHelper.getElementAttributeValue;
import static com.tunein.mobile.utils.HeadspinUtil.runAdbShellCommandInHeadspin;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static io.appium.java_client.AppiumBy.*;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;
import static org.openqa.selenium.By.xpath;

public class BrowserUtil {

    /* --- Constants --- */

    public static final String INTERNET_SPEED_TEST_ERROR_MESSAGE = "Sorry, looks like the internet speed test is experiencing high demand. Please try again later.";

    public static final String INTERNET_SPEED_TEST = "internet speed test";

    public static final String RUN_SPEED_TEST = "RUN SPEED TEST";

    public static final String TEST_AGAIN = "TEST AGAIN";

    public static final String STAY_SIGNED_OUT = "Stay signed out";

    public static final String NO_THANKS = "No, thanks";

    public static final String GIVE_FEEDBACK = "Give feedback on these results?";

    public static final String WEB_DIALOG = "web dialog";

    public static final String GO = "Go";

    public static final String CLEAR_SEARCH = "Clear Search";

    public static final String NO_THANKS_LABEL = "No thanks";

    /* --- Constants (Bundle Ids) --- */

    public static final String SAFARI = "com.apple.mobilesafari";

    public static final String CHROME = "com.android.chrome";

    public static final String CHROME_ACTIVITY = "com.google.android.apps.chrome.Main";

    /* --- Constants (Locators) --- */

    public static final String LINK_TYPE = "XCUIElementTypeLink";

    public static final String BUTTON_TYPE = "XCUIElementTypeButton";

    public static final String TEXT_FIELD_TYPE = "XCUIElementTypeTextField";

    public static final String DOWNLOAD_UPLOAD_DATA_LOCATOR = (isIos())
            ? "//XCUIElementTypeOther[@name='%s']/XCUIElementTypeOther"
            : "//*[@resource-id='knowledge-verticals-internetspeedtest";

    public static final String DOWNLOAD_DATA_ANDROID = DOWNLOAD_UPLOAD_DATA_LOCATOR + "__download']/android.widget.TextView[1]";

    public static final String UPLOAD_DATA_ANDROID = DOWNLOAD_UPLOAD_DATA_LOCATOR + "__upload']/android.widget.TextView[1]";

    public static final String DOWNLOAD_DATA_IOS = String.format(DOWNLOAD_UPLOAD_DATA_LOCATOR + "[4]/XCUIElementTypeStaticText", WEB_DIALOG);

    public static final String UPLOAD_DATA_IOS = String.format(DOWNLOAD_UPLOAD_DATA_LOCATOR + "[6]/XCUIElementTypeStaticText", WEB_DIALOG);

    public static final String WEB_DIALOG_ELEMENT = String.format("**/XCUIElementTypeOther[`label == \"%s\"`][3]/XCUIElementTypeOther/XCUIElementTypeButton[2]", WEB_DIALOG);

    public static final String GET_THE_APP = "**/XCUIElementTypeButton[`label == \"Get the app\"`]";

    public static final String ADDRESS_BAR_IOS = String.format("type == '%s' AND name IN {'URL','TabBarItemTitle'}", TEXT_FIELD_TYPE);

    public static final String ADDRESS_BAR_ANDROID = "com.android.chrome:id/search_box_text";

    public static final String GO_BUTTON = String.format("type == '%s' AND name == '%s'", BUTTON_TYPE, GO);

    public static final String RUN_SPEED_TEST_ANDROID = String.format("text(\"%s\")", RUN_SPEED_TEST);

    public static final String TEST_AGAIN_ANDROID = String.format("text(\"%s\")", TEST_AGAIN);

    public static final String TEST_AGAIN_IOS = String.format("type == '%s' AND name CONTAINS '%s'", BUTTON_TYPE, TEST_AGAIN);

    public static final String RUN_SPEED_TEST_IOS = "//" + String.format("%s[@name=\"%s\"]", LINK_TYPE, RUN_SPEED_TEST);

    public static final String STAY_SIGNED_OUT_IOS = "//" + String.format("%s[@name=\"%s\"]", BUTTON_TYPE, STAY_SIGNED_OUT);

    public static final String INTERNET_SPEED = String.format("text(\"%s\")", INTERNET_SPEED_TEST);

    public static final String INTERNET_SPEED_ERROR_MESSAGE = String.format("**/XCUIElementTypeStaticText[`label == \"%s\"`]", INTERNET_SPEED_TEST_ERROR_MESSAGE);

    public static final String FEEDBACK = String.format("text(\"%s\")", NO_THANKS);

    public static final String NO_THANKS_ANDROID = "com.android.chrome:id/negative_button";

    public static final String NO_THANKS_IOS = "//" + String.format("%s[@name=\"%s\"]", BUTTON_TYPE, NO_THANKS_ANDROID);

    /* --- Selenide Elements --- */

    public static SelenideElement goButton = $(iOSNsPredicateString(GO_BUTTON)).as("Go button");

    public static SelenideElement getAppDialog = $(iOSClassChain(GET_THE_APP)).as("Get the app dialog");

    public static SelenideElement getAppCloseButton = $(iOSClassChain(WEB_DIALOG_ELEMENT)).as("Get the app dialog close button");

    public static SelenideElement staySignedOut = $(xpath(STAY_SIGNED_OUT_IOS)).as(STAY_SIGNED_OUT);

    public static SelenideElement feedbackResults = $(xpath(FEEDBACK)).as(GIVE_FEEDBACK);

    public static SelenideElement addressBar = $(android(AppiumBy.id(ADDRESS_BAR_ANDROID))
            .ios(iOSNsPredicateString(ADDRESS_BAR_IOS)))
            .as("Address bar");

    public static SelenideElement urlBar = $(AppiumBy.id("com.android.chrome:id/url_bar")).as("Address bar");

    public static SelenideElement youtubePlayButton = $(AppiumBy.id("com.google.android.youtube:id/player_control_play_pause_replay_button")).as("Play button");

    public static SelenideElement runSpeedTest = $(android(androidUIAutomator(RUN_SPEED_TEST_ANDROID))
            .ios(xpath(RUN_SPEED_TEST_IOS)))
            .as(RUN_SPEED_TEST);

    public static SelenideElement testAgain = $(android(androidUIAutomator(TEST_AGAIN_ANDROID))
            .ios(iOSNsPredicateString(TEST_AGAIN_IOS)))
            .as(TEST_AGAIN);

    public static SelenideElement internetSpeedTest = $(androidUIAutomator(INTERNET_SPEED)).as(INTERNET_SPEED_TEST);

    public static SelenideElement clearTextButton = $(androidUIAutomator(String.format("text(\"%s\")", CLEAR_SEARCH))).as(CLEAR_SEARCH);

    public static SelenideElement internetSpeedTestErrorMessage = $(iOSClassChain(INTERNET_SPEED_ERROR_MESSAGE)).as(INTERNET_SPEED_TEST_ERROR_MESSAGE);

    public static SelenideElement noThanksButton = $(android(AppiumBy.id(NO_THANKS_ANDROID))
            .ios(iOSNsPredicateString(NO_THANKS_IOS)))
            .as(NO_THANKS_LABEL);

    /* --- Action Methods --- */

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    @Step("Launching Safari mobile web browser")
    public static void launchSafariBrowser() {
        Map<String, Object> params = new HashMap<>();
        params.put("bundleId", SAFARI);
        getAppiumDriver().executeScript("mobile:launchApp", params);
    }

    @Step("Launching Chrome mobile web browser")
    public static void launchChromeBrowser() {
        if (isAndroid()) {
            if (config().testOnRealDevices()) {
                runAdbShellCommandInHeadspin("am start -n " + CHROME + "/" + CHROME_ACTIVITY);
                long start = System.currentTimeMillis();
                while (((AndroidDriver) getAppiumDriver()).queryAppState(CHROME) != RUNNING_IN_FOREGROUND) {
                    if (System.currentTimeMillis() - start > config().waitExtraLongTimeoutMilliseconds()) {
                        throw new Error("Chrome is not launched through adb");
                    }
                }
            } else {
                List<String> launchAppArgs = Arrays.asList("-n", CHROME + "/" + CHROME_ACTIVITY);
                ReporterUtil.log("Launch Chrome on device: " + getUDID() + ", " + adbExecuteScript("am start", launchAppArgs));
            }
        }
    }

    @Step("Terminating Safari mobile web browser")
    public static void terminateSafariBrowser() {
        getAppiumDriver().executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", SAFARI));
    }

    public enum Browser {
        SAFARI, CHROME
    }

    @Step("Launching {browser} mobile web browser")
    public static void launchBrowser(Browser browser) {
        switch (browser) {
            case SAFARI -> launchSafariBrowser();
            case CHROME -> launchChromeBrowser();
            default -> throw new IllegalStateException("Unexpected value: " + browser);
        }
    }

    /* --- Deeplink Helper Methods --- */

    @Step("Open web URL {url} in Safari")
    public static void openDeeplinkUrlInSafari(String url) {
        ReporterUtil.log("Opening deeplink \"" + url + "\"");
        clickOnElement(addressBar);
        addressBar.setValue(url);
        clickOnElement(goButton);
        closePermissionPopupFor(DEEPLINK);
    }

    @Step("Open web URL {url} in Chrome")
    public static void openUrlInChrome(String url) {
        ReporterUtil.log("Opening url \"" + url + "\"");
        SelenideElement element = (waitVisibilityOfElement(addressBar) != null) ? addressBar : urlBar;

        clickOnElement(element);
        element.setValue(url);
        clickOnElement($(androidUIAutomator(url)));
        closePermissionPopupFor(DEEPLINK);
    }

    /* --- Internet Speed Test Helper Methods --- */

    @Step("Launching internet stream test in {browser} web browser")
    public static void startInternetSpeedTest(Browser browser) {
        ReporterUtil.log("Staring internet speed test in " + browser);
        switch (browser) {
            case SAFARI -> startInternetSpeedTestInSafari();
            case CHROME -> startInternetSpeedTestInChrome();
            default -> throw new IllegalStateException("Unexpected value: " + browser);
        }
    }

    @Step("Handle \"Get the app\" dialog if displayed")
    public static void handleGetTheAppDialogIfDisplayed() {
        try {
            ReporterUtil.log("Checking for the \"Get the app\" dialog");
            if (getAppDialog.isDisplayed()) {
                ReporterUtil.log("Closing \"Get the app\" dialog");
                BasePage.clickOnElement(getAppCloseButton);
            }
        } catch (Exception ignored) {
            ReporterUtil.log("Dialog not detected");
        }
    }

    @Step("Tap on \"Internet Speed Test\" button")
    public static void tapRunSpeedTestButton() {
        BasePage.clickOnElement(runSpeedTest);
        closePermissionPopupFor(NOTIFICATIONS);
    }

    @Step("Tap on \"Youtube play\" button if displayed")
    public static void clickOnPlayButtonIfDisplayed() {
        clickOnElementIfDisplayed(youtubePlayButton);
    }

    @Step("Wait for the \"Internet Speed Test\" to finish")
    private static void waitForInternetSpeedTestToFinish() {
        // Testing internet speed (Download & Upload)
        try {
            long startTime = System.currentTimeMillis();
            while (waitVisibilityOfElement(testAgain, Duration.ofSeconds(config().oneMinuteInSeconds())) == null) {
                if (System.currentTimeMillis() - startTime > config().oneMinuteInMilliseconds()) break;
                ReporterUtil.log("Waiting for the " + INTERNET_SPEED_TEST + " results...");
            }
        } catch (Throwable e) {
            ReporterUtil.log("Failed to set speed values: " + e.getMessage());
        }
        customWait(Duration.ofMillis(config().waitCustomTimeoutMilliseconds()));
    }

    @Step("Start Internet Speed Test flow in Safari")
    public static void startInternetSpeedTestInSafari() {
        BrowserUtil.launchBrowser(Browser.SAFARI);
        handleGetTheAppDialogIfDisplayed();
        checkForStaySignedOutDialog();
        checkForInternetSpeedTestErrorMessage();

        if (waitVisibilityOfElement(runSpeedTest, Duration.ofSeconds(config().waitMediumTimeoutSeconds())) == null) {
            clickOnElement(addressBar);
            addressBar.setValue(INTERNET_SPEED_TEST);
            BasePage.clickOnElement(goButton);
            checkForStaySignedOutDialog();
            checkForGiveFeedbackOnTheseResultsDialog();
            checkForInternetSpeedTestErrorMessage();
        }

        // Internet speed testing (Download & Upload)
        takeScreenshot();
        tapRunSpeedTestButton();
        waitForInternetSpeedTestToFinish();
        getInternetSpeedTestValues();
    }

    @Step("Clear previously entered text from the search bar, if displayed")
    private static void clearTextFromAddressBarIfDisplayed() {
        if (waitVisibilityOfElement(clearTextButton) != null) {
            BasePage.clickOnElement(clearTextButton);
        }
    }

    @Step("Check if \"Internet speed test\" label exists")
    private static void checkForInternetSpeedTestLabel() {
        if (waitVisibilityOfElement(internetSpeedTest) != null) {
            BasePage.clickOnElement(urlBar);
        }
    }

    @Step("Check for the \"Stay signed Out\" label")
    private static void checkForStaySignedOutDialog() {
        if (waitVisibilityOfElement(staySignedOut, Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds())) != null) {
            BasePage.clickOnElement(staySignedOut);
        }
    }

    @Step("Check for the \"Give feedback on these results?\" label")
    private static void checkForGiveFeedbackOnTheseResultsDialog() {
        if (waitVisibilityOfElement(feedbackResults, Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds())) != null) {
            BasePage.clickOnElement(feedbackResults);
        }
    }

    @Step("Check for the internet speed test error message")
    private static void checkForInternetSpeedTestErrorMessage() {
        if (waitVisibilityOfElement(internetSpeedTestErrorMessage, Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds())) != null) {
            GestureActionUtil.scrollToRefresh();
        }
    }

    @Step("Start Internet Speed Test in Chrome")
    public static void startInternetSpeedTestInChrome() {
        BrowserUtil.launchBrowser(Browser.CHROME);
        checkForNotificationsWindowInChrome();
        checkForInternetSpeedTestLabel();
        clearTextFromAddressBarIfDisplayed();

        SelenideElement element = (waitVisibilityOfElement(addressBar) != null) ? addressBar : urlBar;
        element.click();
        element.setValue(INTERNET_SPEED_TEST);
        takeScreenshot();
        BasePage.clickOnElement(internetSpeedTest);

        waitTillVisibilityOfElement(runSpeedTest);
        BasePage.clickOnElement(runSpeedTest);
        closePermissionPopupFor(NOTIFICATIONS);

        // Testing internet speed (Download & Upload)
        try {
            long startTime = System.currentTimeMillis();
            while (waitVisibilityOfElement(testAgain, Duration.ofSeconds(config().oneMinuteInSeconds())) == null) {
                if (System.currentTimeMillis() - startTime > config().waitCustomTimeoutMilliseconds()) break;
                ReporterUtil.log("Waiting for the " + INTERNET_SPEED_TEST + " results...");
            }
        } catch (Throwable e) {
            ReporterUtil.log("Failed to set speed values: " + e.getMessage());
        }

        // Waiting for the web locators to load
        customWait(Duration.ofMillis(config().waitCustomTimeoutMilliseconds()));
        getInternetSpeedTestValues();
    }

    @Step("Get internet speed test values")
    public static void getInternetSpeedTestValues() {
        try {
            String downloadData = (isIos()) ? DOWNLOAD_DATA_IOS : DOWNLOAD_DATA_ANDROID;
            String downloadSpeedValue = getElementAttributeValue($(xpath(downloadData)), isIos() ? NAME : TEXT);

            String uploadData = (isIos()) ? UPLOAD_DATA_IOS : UPLOAD_DATA_ANDROID;
            String uploadSpeedValue = getElementAttributeValue($(xpath(uploadData)), NAME);

            ReporterUtil.log("downloadData: " + downloadSpeedValue);
            ReporterUtil.log("uploadData: " + uploadSpeedValue);

            GeneralTestUtil.setDownloadSpeedValue(downloadSpeedValue);
            GeneralTestUtil.setUploadSpeedValue(uploadSpeedValue);
        } catch (Throwable e) {
            ReporterUtil.log("Failed to set speed values: " + e.getMessage());
        }
    }

    @Step("Check for the \"Chrome notifications make things easier\" pop-up window")
    private static void checkForNotificationsWindowInChrome() {
        if (waitVisibilityOfElement(noThanksButton, Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds())) != null) {
            BasePage.clickOnElement(noThanksButton);
        }
    }
}
