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
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SwitchToolTipDialog extends BasePage {

    protected SelenideElement switchLogoTitle = $(android(id("tunein.player:id/logo"))
            .ios(id("Logo/ManualBoost"))).as("Logo");

    protected SelenideElement notNowButton = $(android(id("tunein.player:id/optOutButton"))
            .ios(id("NOT NOW"))).as("Not now button");

    protected SelenideElement switchDialogCloseButton = $(android(id("tunein.player:id/close_button"))
            .ios(id("Misc/Close"))).as("Close button");

    protected SelenideElement letsSwitchButton = $(android(id("tunein.player:id/optInButton"))
            .ios(id("LET'S SWITCH"))).as("Lets Switch button");

    /* --- Loadable Component Method --- */

    @Step("Wait until switch tool tip dialog is opened")
    public SwitchToolTipDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(switchLogoTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click on not now button")
    public void tapOnSwitchToolTipNotNowButton() {
        clickOnElement(notNowButton);
    }

    @Step("Click on not now button if displayed")
    public NowPlayingPage clickOnNotNowButtonIfDisplayed() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(notNowButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            clickOnElement(notNowButton);
            nowPlayingPage.closeGotItPrompt();
        }
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Click on lets switch button")
    public NowPlayingPage tapOnLetsSwitchButton() {
        clickOnElement(letsSwitchButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step("Validate switch tool tip dialog box is displayed")
    public void validateSwitchToolTipDialogBoxIsDisplayed() {
        assertThat(isElementDisplayed(switchLogoTitle)).as("Switch tool tip dialog is not displayed").isTrue();
    }

    @Step("Validate switch tool tip dialog box is not displayed")
    public void validateSwitchToolTipDialogBoxIsNotDisplayed() {
        assertThat(isElementNotDisplayed(switchLogoTitle)).as("Switch tool tip dialog is displayed").isTrue();
    }

    /* --- Helper Methods --- */

    public HashMap<String, SelenideElement> switchTollTipPageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();

        int increment = 0;
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, switchLogoTitle);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, switchDialogCloseButton);
        elementsMap.put("LET'S SWITCH", letsSwitchButton);
        elementsMap.put("NOT NOW", notNowButton);
        return elementsMap;
    }
}
