package com.tunein.mobile.pages.android.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.DeleteYourAccountPage;
import com.tunein.mobile.pages.common.userprofile.SettingsPage;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.android.AndroidDriver;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.utils.ApplicationUtil.launchApp;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.CHECKED;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.TEXT;
import static com.tunein.mobile.utils.ElementHelper.getElementAttributeValue;
import static com.tunein.mobile.utils.ElementHelper.isElementChecked;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.LONG;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToElementWithoutError;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_BACKGROUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class AndroidSettingsPage extends SettingsPage {

    protected SelenideElement enableScrollableNowPlayingTitle = $(androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"Enable Scrollable Now Playing\").instance(0))")).as("Enable Scrollable Now Playing");

    protected SelenideElement enableInstantUnifiedEventsReporting = $(androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"Enable Instant Unified Events Reporting\").instance(0))")).as("Enable Instant Unified Events Reporting");

    protected SelenideElement enableVideoAdsEventsReporting = $(xpath("//*[@text='Enable Video Ads']")).as("Enable Video Ads");

    protected SelenideElement enableVideoAdsToggleButton = $(xpath("//*[@text='Enable Video Ads']/following-sibling::*/*[@resource-id='tunein.player:id/switchWidget']")).as("Video ads toggle");

    protected SelenideElement adDataOptOutToggleButton = $(xpath("//*[@text='Ad Data Opt Out']/following-sibling::*/*[@resource-id='tunein.player:id/switchWidget']")).as("Ad data opt out toggle");

    protected SelenideElement allowExternalDevicesToStartPlayback = $(androidUIAutomator("text(\"Allow external devices to start playback\")")).as("Allow external devices to start playback");

    protected SelenideElement bufferBeforePlaying = $(androidUIAutomator("text(\"Buffer before playing\")")).as("Buffer before playing");

    protected SelenideElement bufferSize = $(androidUIAutomator("text(\"Buffer size\")")).as("Buffer size");

    protected SelenideElement forceToRequestAutoDownload = $(xpath("//*[@text='Autodownload']/following-sibling::*/android.widget.Switch")).as("Force to request AutoDownload");

    protected SelenideElement preferredStream = $(androidUIAutomator("text(\"Preferred Stream\")")).as("Preferred Stream");

    protected SelenideElement openTuneInInCarMode = $(androidUIAutomator("text(\"Open TuneIn in Car Mode\")")).as("Open TuneIn in Car Mode");

    protected SelenideElement pauseInsteadOfDucking = $(androidUIAutomator("text(\"Pause Instead of Ducking\")")).as("Pause Instead of Ducking");

    protected SelenideElement manageNotifications = $(androidUIAutomator("text(\"Manage Notifications\")")).as("Manage Notifications");

    protected SelenideElement version = $(androidUIAutomator("text(\"Version\")")).as("Version");

    protected SelenideElement exitButton = $(androidUIAutomator("text(\"Exit\")")).as("Exit");

    protected SelenideElement deviceSerial = $(xpath("//android.widget.TextView[contains(@text, 'Device Serial')]/following-sibling::android.widget.TextView")).as("Device Serial");

    protected SelenideElement launchTestStartupActivity = $(xpath("//android.widget.TextView[contains(@text, 'Launch Test Startup Activity')]/following-sibling::android.widget.TextView")).as("Launch Test Startup Activity");

    @Step("Tap on \"Enable Scrollable Now Playing\" Title")
    @Override
    public SettingsPage tapOnEnableScrollableNowPlayingTitle() {
        clickOnElementIfDisplayed(enableScrollableNowPlayingTitle);
        return this;
    }

    @Step("Enable \"Instant Unified Events\" option from Settings page")
    @Override
    public SettingsPage enableInstantUnifiedEventsReportingFlow() {
        navigationAction.navigateTo(SETTINGS);
        clickOnElement(enableInstantUnifiedEventsReporting);
        long start = System.currentTimeMillis();
        while (((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid()) != RUNNING_IN_BACKGROUND) {
            if (System.currentTimeMillis() - start > config().waitExtraLongTimeoutMilliseconds()) {
               ReporterUtil.log("App doesn't have required state: " + RUNNING_IN_BACKGROUND);
               break;
            }
        }
        restartApp();
        return this;
    }

    @Step("Set value {toEnable} for \"Enable Video Ads\" option in Settings page")
    @Override
    public SettingsPage enableVideoAds(boolean toEnable) {
        navigationAction.navigateTo(SETTINGS);
        scrollTo(enableVideoAdsToggleButton, DOWN, config().scrollLotsOfTimes());
        boolean isEnabled = Boolean.parseBoolean(getElementAttributeValue(enableVideoAdsToggleButton, CHECKED));
        if (isEnabled != toEnable) {
            clickOnElement(enableVideoAdsEventsReporting);
        }
        launchApp();
        return this;
    }

    @Step("Set value {toEnable} for \"Ad Data Opt Out\" option in Settings page")
    @Override
    public SettingsPage enableOptOut(boolean toEnable) {
        navigationAction.navigateTo(SETTINGS);
        adDataOptOutToggleButton.scrollTo();
        boolean isEnabled = Boolean.parseBoolean(getElementAttributeValue(adDataOptOutToggleButton, CHECKED));
        if (isEnabled != toEnable) {
            clickOnElement(adDataOptOutToggleButton);
        }
        launchApp();
        return this;
    }

    @Step("Enable \"Scrollable Now Playing\" setting")
    @Override
    public void enableScrollableNowPlayingFlow() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSettingsButton();
        settingsPage
                .tapOnEnableScrollableNowPlayingTitle()
                .tapBackButton();
        userProfilePage.closeProfilePage();
        navigationAction.navigateTo(HOME);
    }

    @Step("Tap on force to request auto download")
    @Override
    public void updateAutoDownloadConfig(boolean enableAutoDownload) {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSettingsButton();
        SelenideElement requiredContent = scrollTo(forceToRequestAutoDownload, DOWN, 20);
        if (isElementChecked(requiredContent) && !enableAutoDownload) {
            clickOnElement(requiredContent);
        } else if (!isElementChecked(requiredContent) && enableAutoDownload) {
            clickOnElement(requiredContent);
        }
        settingsPage.tapBackButton();
        userProfilePage.closeProfilePage();
        navigationAction.navigateTo(HOME);
    }

    @Step("Tap on \"Delete Your Account\" setting")
    @Override
    public DeleteYourAccountPage tapOnDeleteYourAccount() {
        throw new UnsupportedOperationException("This functionality is absent for android Platform");
    }

    @Step("Tap on \"Exit\" button")
    @Override
    public void tapOnExitButton() {
        clickOnElement(scrollTo(exitButton, DOWN));
    }

    @Override
    public SettingsPage validateDeleteAccountButtonNotDisplayed() {
        throw new UnsupportedOperationException("This functionality is absent for android Platform");
    }

    @Override
    public HashMap<String, SelenideElement> settingsPageElements(Users user) {
        return new LinkedHashMap<>() {{
            put("Display", displaySettings);
            put("Autoplay", autoPlaySettings);
            put("Allow external devices to start playback", allowExternalDevicesToStartPlayback);
            put("Buffer before playing", bufferBeforePlaying);
            put("Buffer size", bufferSize);
            put("Preferred Stream", preferredStream);
            put("Open TuneIn in Car Mode", openTuneInInCarMode);
            put("Pause Instead of Ducking", pauseInsteadOfDucking);
            put("Waze Navigation", wazeNavigationSettings);
            put("Manage Notifications", manageNotifications);
            put("Autodownload", autoDownloadSettings);
            put("Recently Listened to Podcasts", recentlyListenedPodcasts);
            put("Use Cellular Data", useCellularData);
            put("Exit", exitButton);
            put("Version", version);
        }};
    }

    @Step("Scroll to the bottom of the \"Settings\" screen")
    private void scrollToTheBottomOfSettingsScreen() {
        try {
            scrollToElementWithoutWaitTimeout(launchTestStartupActivity, DOWN, LONG, config().scrollTenTimes());
        } catch (Exception ignored) {
            takeScreenshot();
            ReporterUtil.log("Unable to find element \"Launch Test Startup Activity\" after scrolling " + DOWN);
        }
    }

    @Step("Grab \"Device Serial\" from the \"Settings\" screen")
    private String grabDeviceSerialFromSettingsScreen() {
        ReporterUtil.log("Grabbing \"Device Serial\" from the \"Settings\" screen");
        try {
            return getElementAttributeValue(scrollToElementWithoutError(
                    deviceSerial,
                    DOWN,
                    SHORT,
                    config().scrollLotsOfTimes(),
                    config().timeoutOneSecond()), TEXT);
        } catch (Exception ignored) {
            ReporterUtil.log("Unable to find \"Device Serial\" from the \"Settings\" screen");
            takeScreenshot();
            return "";
        }
    }

    @Override
    @Step("Get \"Device Serial\" from the \"Settings\" screen")
    public String getDeviceServiceIdentifier() {
        return grabDeviceSerialFromSettingsScreen();
    }

    @Step("Validate that serial number is not equal to {beforeSerialId}")
    public void validateSerialNumberIsNotEqualTo(String beforeSerialId) {
        assertThat(settingsPage.getDeviceServiceIdentifier()).as("Serial id's are equal").isNotEqualTo(beforeSerialId);
    }

    @Step("Validate that serial number is equal to {beforeSerialId}")
    public void validateSerialNumberIsEqualTo(String beforeSerialId) {
        assertThat(settingsPage.getDeviceServiceIdentifier()).as("Serial id's are not equal").isEqualTo(beforeSerialId);
    }

}
