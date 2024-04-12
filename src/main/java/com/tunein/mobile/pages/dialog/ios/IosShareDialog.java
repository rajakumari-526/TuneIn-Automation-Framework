package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.ShareDialog;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static org.assertj.core.api.Assertions.assertThat;

public class IosShareDialog extends ShareDialog {

    protected SelenideElement shareDialogCloseButton = $(AppiumBy.iOSNsPredicateString("label == \"Close\"")).as("Close button");

    protected SelenideElement shareDialogAddToReadingListButton = $(AppiumBy.iOSNsPredicateString("label == \"Add to Reading List\"")).as("'Add to reading list' button");

    @Step
    @Override
    public ShareDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(shareDialogCopyButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Check functionality of copy button")
    @Override
    public void validateShareDialogCopyButton() {
        tapShareDialogCopyButton();
        String actual = deviceNativeActions.getClipboardText();
        assertThat(actual).as("Copied share text doesn't include a URL").contains("http:");
    }
}
