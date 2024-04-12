package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.TermsOfServiceDialog;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class AndroidTermsOfServiceDialog extends TermsOfServiceDialog {

    protected SelenideElement termsOfServiceDialogTitleText = $(androidUIAutomator("text(\"Updated Terms of Service\")")).as("Terms Of Service dialog's title");

    protected SelenideElement acceptButton = $(androidUIAutomator("text(\"ACCEPT\")")).as("Accept button");

    @Step("Wait until Terms of Service dialog appears")
    @Override
    public TermsOfServiceDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(termsOfServiceDialogTitleText, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Close Terms of Service dialog if displayed")
    @Override
    public void closeTermsOfServiceDialogIfPresent() {
        closePermissionPopupsIfDisplayed();
        if (isTermsOfServiceDialog()) clickOnElement(acceptButton);
    }

    @Override
    public boolean isTermsOfServiceDialog() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(termsOfServiceDialogTitleText, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

}
