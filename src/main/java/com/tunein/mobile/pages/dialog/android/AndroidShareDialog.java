package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.ShareDialog;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class AndroidShareDialog extends ShareDialog {

    protected SelenideElement shareDialogNearbyButton = $(AppiumBy.androidUIAutomator("text(\"Nearby\")")).as("Nearby button");

    protected SelenideElement shareDialogRecommendedPeopleTitle = $(AppiumBy.id("android:id/chooser_row_text_option")).as("Recommended people title");

    @Step
    @Override
    public ShareDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(shareDialogRecommendedPeopleTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Check functionality of copy button")
    @Override
    public void validateShareDialogCopyButton() {
        String expected = getShareDialogTitleText();
        tapShareDialogCopyButton();
        String actual = deviceNativeActions.getClipboardText();
        getSoftAssertion().assertThat(actual).as("Copied share text doesn't match expected").contains(expected);
        getSoftAssertion().assertThat(actual).as("Copied share text doesn't include a URL").contains("http:");
        getSoftAssertion().assertAll();
    }
}
