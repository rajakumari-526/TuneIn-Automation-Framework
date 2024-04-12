package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public class ContinueListeningDialog extends BasePage {

    protected SelenideElement continueListeningTitle = $(android(androidUIAutomator("text(\"Continue listening\")"))
            .ios(iOSNsPredicateString("label == \"Continue Listening\""))).as("Continue listening title");

    protected SelenideElement continueListeningSettingsButton = $(android(accessibilityId("Settings"))
            .ios(iOSNsPredicateString("label == \"SettingsGrayGear\""))).as("Settings button");

    protected SelenideElement continueListeningStartingButton = $(android(xpath("//android.widget.ProgressBar/.."))
            .ios(iOSNsPredicateString("label == \"Starting in %@...\""))).as("Starting button");

    protected SelenideElement continueListeningContentLogo = $(android(xpath("(//*[@text='Continue Listening']/following-sibling::android.view.View/android.view.View[@text=''])[1]"))
            .ios(xpath("//XCUIElementTypeImage"))).as("Content logo");

    protected SelenideElement continueListeningContentTitle = $(android(xpath("(//*[@text='Continue Listening']/following-sibling::android.view.View/android.view.View[not(@text='')])[1]"))
            .ios(xpath("(//XCUIElementTypeImage/following-sibling::*//XCUIElementTypeStaticText)[1]"))).as("Content title");

    protected SelenideElement continueListeningSubtitle = $(android(xpath("(//*[@text='Continue Listening']/following-sibling::android.view.View/android.view.View[not(@text='')])[2]"))
            .ios(xpath("(//XCUIElementTypeImage/following-sibling::*//XCUIElementTypeStaticText)[2]"))).as("Content subtitle");

    protected SelenideElement continueListeningCancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("label == \"Cancel\" AND type == \"XCUIElementTypeButton\""))).as("Cancel button");


    /* --- Loadable Component Method --- */

    @Step
    @Override
    public ContinueListeningDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(continueListeningTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public NowPlayingPage tapOnContinuePlayButton() {
        clickOnElement(continueListeningStartingButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Tap on continue listening dialog if displayed")
    public NowPlayingPage tapOnContinuePlayButtonIfDisplayed() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(continueListeningStartingButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Close continue listening dialog if displayed")
    public void closeContinueListeningDialogIfDisplayed() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(continueListeningTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            clickOnElementIfDisplayed(continueListeningCancelButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        }
    }

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    public boolean isContinueListeningDialogDisplayed() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(continueListeningTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

}
