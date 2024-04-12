package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class NowPlayingSpeedPlaybackDialog extends BasePage {

    protected SelenideElement scrubber = $(android(id("selectedSpeedTxt"))
            .ios(xpath("//*[@name=\"AUDIO SPEED\"]/following-sibling::XCUIElementTypeOther[1]"))).as("Scrubber");

    protected SelenideElement scaleSpeedValuesContainter = $(android(id("speedSeekerRecyclerView"))
            .ios(iOSNsPredicateString("type == \"XCUIElementTypeScrollView\""))).as("Scale speed values container");

    protected SelenideElement doneButton = $(android(id("doneTxt"))
            .ios(iOSNsPredicateString("label == \"Done\""))).as("Done button");

    /* --- Loadable Component Method --- */

    @Step("Wait until speed playback dialog is loaded")
    public NowPlayingSpeedPlaybackDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(doneButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract NowPlayingPage setSpeedPickerPosition(String currentValue, String requiredValue, boolean... applyToAll);

    @Step("Click on done button")
    public NowPlayingPage clickOnDoneButton() {
        clickOnElement(doneButton);
        return nowPlayingPage;
    }
  
    public abstract NowPlayingSpeedPlaybackDialog tapAboveVariableSpeedPlaybackPopUp();

    public abstract NowPlayingSpeedPlaybackDialog swipeDownVariableSpeedPlaybackPopUp();

    /* --- Validation Methods --- */

    public abstract NowPlayingSpeedPlaybackDialog validateApplyToAllPodcastsIsOff();

    @Step("Validate display of Variable Speed Playback PopUp is '{playbackPopupisDisplayed}'")
    public NowPlayingSpeedPlaybackDialog validateVariableSpeedPlaybackPopUpisDisplayed(boolean playbackPopupisDisplayed) {
        if (playbackPopupisDisplayed) {
            assertThat(isElementDisplayed(doneButton)).as("Variable Speed Playback PopUp is not displayed").isTrue();
        } else {
            assertThat(isElementDisplayed(doneButton)).as("Variable Speed Playback PopUp is displayed").isFalse();
        }
        return this;
    }

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> speedPlaybackDialogElements();

}
