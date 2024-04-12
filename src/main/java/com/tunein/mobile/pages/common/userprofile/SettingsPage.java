package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SettingsPage extends BasePage {

    protected SelenideElement settingsBackButton = $(android(accessibilityId("Back"))
            .ios(iOSNsPredicateString("label == 'Back'"))).as("'Back' button");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement enableAlexaLiveSkill = $(android(androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"TuneIn Live Skill\").instance(0))"))
            .ios(xpath("//XCUIElementTypeStaticText[@name='TuneIn Live Skill']/following-sibling::XCUIElementTypeSwitch"))).as("\"TuneIn Live Skill\" label");

    protected SelenideElement settingsNavigationBar = $(android(xpath("//*[contains(@resource-id,'design_toolbar') and .//*[contains(@resource-id,'action_bar_help')]]"))
            .ios(iOSNsPredicateString("name=='AppSettingsTableView' and visible==true"))).as("Settings Navigation Bar");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement autoPlaySettings = $(android(androidUIAutomator("text(\"Autoplay\")"))
            .ios(iOSNsPredicateString("label=\"Autoplay\""))).as("\"Autoplay\" label");

    protected SelenideElement autoPlayToggle = $(android(xpath("//*[@text=\"Autoplay\"]/following-sibling::android.widget.FrameLayout"))
            .ios(xpath("//XCUIElementTypeStaticText[@name=\"Autoplay\"]/following-sibling::XCUIElementTypeSwitch"))).as("\"Autoplay\" toggle button");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement displaySettings = $(android(androidUIAutomator("text(\"Display\")"))
            .ios(iOSNsPredicateString("label=\"Display\""))).as("\"Display\" label");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement wazeNavigationSettings = $(android(androidUIAutomator("text(\"Waze Navigation\")"))
            .ios(iOSNsPredicateString("label=\"Waze Navigation\""))).as("\"Waze Navigation\" label");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement autoDownloadSettings = $(android(androidUIAutomator("text(\"Autodownload\")"))
            .ios(iOSNsPredicateString("label=\"Autodownload\""))).as("\"Autodownload\" label");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement recentlyListenedPodcasts = $(android(androidUIAutomator("text(\"Recently Listened to Podcasts\")"))
            .ios(iOSNsPredicateString("label=\"Recently Listened to Podcasts\""))).as("\"Recently Listened to Podcasts\" label");

    //TODO update the locators in Settings page when the task is finished by the Android eng team
    protected SelenideElement useCellularData = $(android(androidUIAutomator("text(\"Use Cellular Data\")"))
            .ios(iOSNsPredicateString("label=\"Use Cellular Data\""))).as("\"Use Cellular Data\" label");

    public SelenideElement development = $(android(androidUIAutomator("text(\"Development\")"))
            .ios(iOSNsPredicateString("label=\"Development\""))).as("\"Development\" label");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public SettingsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(settingsNavigationBar, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract SettingsPage tapOnEnableScrollableNowPlayingTitle();

    public abstract SettingsPage enableInstantUnifiedEventsReportingFlow();

    public abstract SettingsPage enableVideoAds(boolean toEnable);

    public abstract SettingsPage enableOptOut(boolean toEnable);

    public abstract void updateAutoDownloadConfig(boolean enableAutoDownload);

    @Step("Tap on \"Back\" button")
    public UserProfilePage tapBackButton() {
        clickOnElement(settingsBackButton);
        return userProfilePage.waitUntilPageReady();
    }

    @Step("Tap on autoPlay settings")
    public SettingsPage tapOnAutoPlaySettings() {
        clickOnElement(autoPlayToggle);
        return this;
    }

    @Step("Tap on \"Enable Alexa Live Skill\"")
    public AlexaUpsellPage tapOnEnableAlexaLiveSkill() {
        clickOnElement(enableAlexaLiveSkill);
        return alexaUpsellPage.waitUntilPageReady();
    }

    public abstract void enableScrollableNowPlayingFlow();

    public abstract DeleteYourAccountPage tapOnDeleteYourAccount();

    public abstract void tapOnExitButton();

    /* --- Validation Methods --- */

    @Step
    public SettingsPage validateIsOnSettingsPage() {
        assertThat(isOnSettingsPage()).as("Settings page is not displayed").isTrue();
        return this;
    }

    @Step
    @Override
    public BasePage validateUIElements(HashMap<String, SelenideElement> mapOfElements) {
        for (SelenideElement element : mapOfElements.values()) {
            boolean value = isElementDisplayed(scrollTo(element, DOWN));
            String description = element.getText().equals("") ? element.toString() : element.getText();
            getSoftAssertion().assertThat(value).as(description + " is not displayed").isTrue();
        }
        getSoftAssertion().assertAll();
        return this;
    }

    public abstract SettingsPage validateDeleteAccountButtonNotDisplayed();

    public abstract void validateSerialNumberIsNotEqualTo(String beforeSerialId);

    public abstract void validateSerialNumberIsEqualTo(String beforeSerialId);

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> settingsPageElements(Users user);

    public boolean isOnSettingsPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(settingsNavigationBar, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    /* --- Get Methods --- */

    public abstract String getDeviceServiceIdentifier();

    /* --- Scroll Methods --- */

    @Step("Scroll to {direction} {numberOfScrolls} time(s) until {element} is fully visible after {timeout} seconds")
    public void scrollToElementWithoutWaitTimeout(SelenideElement element, ScrollDirection direction, ScrollDistance scrollDistance, int numberOfScrolls) {
        try {
            for (int i = 0; i <= numberOfScrolls; i++) {
                if (isElementFullyVisibleWithoutWaitTimeout(element)) return;
                if (i != numberOfScrolls) scroll(direction, scrollDistance);
            }
        } catch (Exception ignored) {
            ReporterUtil.log("\"Cannot find element after scrolling \"" + direction + "\" " + numberOfScrolls + " time(s)");
        }
    }
}
