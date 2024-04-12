package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ShareDialog extends BasePage {

    protected SelenideElement shareDialogTitleText = $(android(androidUIAutomator("textContains(\"Listen to\")"))
            .ios(iOSNsPredicateString("label CONTAINS 'Listen to'"))).as("Share dialog title");

    protected SelenideElement shareDialogCopyButton = $(android(id("com.android.intentresolver:id/copy"))
            .ios(iOSNsPredicateString("label == \"Copy\" AND type == \"XCUIElementTypeButton\""))).as("Copy button");

    public abstract ShareDialog waitUntilPageReady();

    /* --- Action Methods --- */

    /**
     * Method will exit Share Dialog, either to Content Profile or Now Playing
     */
    @Step("Tap on Share dialog copy button")
    public void tapShareDialogCopyButton() {
        clickOnElement(shareDialogCopyButton);
    }

    /* --- Validation Methods --- */

    @Step("Validate Share Dialog is displayed")
    public ShareDialog validateShareDialogDisplayed() {
        assertThat(isElementDisplayed(shareDialogCopyButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()))).as("Share popup is not displayed").isTrue();
        return this;
    }

    /**
     * Method will exit Share Dialog, either to Content Profile or Now Playing
     */
    public abstract void validateShareDialogCopyButton();

    /* --- Helper Methods --- */

    @Step
    public String getShareDialogTitleText() {
        return getElementText(shareDialogTitleText);
    }
}
