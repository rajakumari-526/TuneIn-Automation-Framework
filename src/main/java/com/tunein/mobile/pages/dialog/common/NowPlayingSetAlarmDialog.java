package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class NowPlayingSetAlarmDialog extends BasePage {

    protected static final int SECONDS_IN_MINUTE = 60;

    protected static final int HOURS_IN_TIME_CONVENTION = 12;

    protected SelenideElement nowPlayingSetAlarmTitle = $(android(androidUIAutomator("text(\"Alarm\")"))
            .ios(iOSNsPredicateString("label == 'Alarm clock'"))).as("Set alarm title");

    protected SelenideElement nowPlayingSetAlarmSwitcher = $(android(id("settings_alarm_checkbox"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeSwitch' AND value IN {'0', '1'}"))).as("Set alarm switcher");

    protected SelenideElement nowPlayingSetAlarmSaveButton = $(android(androidUIAutomator("text(\"SAVE\")"))
            .ios(iOSNsPredicateString("label == 'SAVE'"))).as("Save button");

    protected SelenideElement nowPlayingSetAlarmCancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("label == 'CLOSE'"))).as("Cancel button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NowPlayingSetAlarmDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(nowPlayingSetAlarmTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public NowPlayingSetAlarmDialog turnOnAlarm() {
        if (!isAlarmEnabled()) {
            clickOnElement(nowPlayingSetAlarmSwitcher);
        }
        return this;
    }

    @Step
    public NowPlayingSetAlarmDialog turnOffAlarm() {
        if (isAlarmEnabled()) {
            clickOnElement(nowPlayingSetAlarmSwitcher);
        }
        return this;
    }

    public abstract NowPlayingSetAlarmDialog setAlarmInCoupleMinutes(int... minutes);

    public abstract NowPlayingSetAlarmDialog setAlarmPlayTimeDuration(int... minutes);

    @Step
    public void tapOnCancelButton() {
        clickOnElement(nowPlayingSetAlarmCancelButton);
    }

    @Step
    public void tapOnSaveButton() {
        clickOnElement(nowPlayingSetAlarmSaveButton);
    }

    /* --- Validation Methods --- */

    @Step
    public NowPlayingSetAlarmDialog validateAlarmDialogIsOpened() {
        assertThat(isAlarmDialogOpened()).as("Alarm dialog is not opened").isTrue();
        return this;
    }

    @Step("Validate that alarm is enabled")
    public NowPlayingSetAlarmDialog validateAlarmIsEnabled() {
        assertThat(isAlarmEnabled()).as("Alarm is not enabled").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public abstract boolean isAlarmEnabled();

    public boolean isAlarmDialogOpened() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(nowPlayingSetAlarmTitle);
    }

}
