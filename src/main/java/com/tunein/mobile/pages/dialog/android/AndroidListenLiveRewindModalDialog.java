package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.ListenLiveRewindModalDialog;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidListenLiveRewindModalDialog extends ListenLiveRewindModalDialog {

    protected SelenideElement nowPlayingPopUpLiveButton = $(androidUIAutomator("text(\"Listen Live\")")).as("Live button");

    protected SelenideElement nowPlayingPopUpRewindButton = $(androidUIAutomator("text(\"Rewind\")")).as("Rewind button");

    protected SelenideElement nowPlayingPopUpLiveRewindTitle = $(androidUIAutomator("text(\"Listen live or rewind\")")).as("Title");

    protected SelenideElement nowPlayingInfoPopUp = $(id("layout_container")).as("Info popup");

    public boolean isListenLiveRewindModalDisplayed() {
        return isElementDisplayed(nowPlayingPopUpLiveRewindTitle);
    }

    @Step
    @Override
    public ListenLiveRewindModalDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(nowPlayingPopUpLiveRewindTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Override
    @Step("Click on listen live button")
    public NowPlayingPage tapListenLiveButton() {
        clickOnElement(nowPlayingPopUpLiveButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    @Step("Close listen live or rewind dialog")
    public NowPlayingPage closeInformationPopUpIfPresent() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(nowPlayingInfoPopUp)) {
            deviceNativeActions.clickBackButton();
        }
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    @Step("Click on rewind button")
    public NowPlayingPage tapRewindButton() {
        clickOnElement(nowPlayingPopUpRewindButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    @Step("Verify Listen live Rewind modal is displayed")
    public void validateListenLiveRewindModalDisplayed() {
        assertThat(isListenLiveRewindModalDisplayed()).as("Listen live Rewind modal is not displayed").isTrue();
    }
}
