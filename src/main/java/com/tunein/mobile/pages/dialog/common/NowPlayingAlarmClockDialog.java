package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class NowPlayingAlarmClockDialog extends BasePage {

    protected SelenideElement alarmClockClockTime = $(id("digitalClock")).as("Clock time");

    protected SelenideElement alarmClockStationTitle = $(id("stationTitle")).as("Station title");

    protected SelenideElement alarmClockStationSlogan = $(id("stationSlogan")).as("Station slogan");

    protected SelenideElement alarmClockSnoozeButton = $(id("snooze")).as("Snooze button");

    protected SelenideElement alarmClockStopButton = $(id("stop")).as("Stop button");

    protected SelenideElement alarmClockCloseButton = $(id("close")).as("Close button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NowPlayingAlarmClockDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(alarmClockSnoozeButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public NowPlayingPage tapOnStopButton() {
        clickOnElement(alarmClockStopButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step
    public NowPlayingPage tapOnCloseButton() {
        clickOnElement(alarmClockCloseButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public NowPlayingAlarmClockDialog validateAlarmClockDialogIsOpened() {
        assertThat(isOnAlarmClockPage()).as("Alarm clock dialog isn't opened").isTrue();
        return this;
    }

    @Step("Validate alarm dialog station title")
    public NowPlayingAlarmClockDialog validateAlarmClockDialogStationTitle(String stationTitle) {
        assertThat(getElementText(alarmClockStationTitle)).as("Alarm clock station title is not equal to" + stationTitle).isEqualTo(stationTitle);
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnAlarmClockPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(alarmClockClockTime);
    }

}
