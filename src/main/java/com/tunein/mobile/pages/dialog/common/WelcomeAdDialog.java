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
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.id;

public class WelcomeAdDialog extends BasePage {

    protected SelenideElement closeAdButton = $(android(accessibilityId("Close Ad"))
            .ios(id("Close Ad"))).as("Close ad button");

    protected SelenideElement reportAdButton = $(android(accessibilityId("Report Ad"))
            .ios(id("Report Ad"))).as("Report ad button");

    /* --- Loadable Component Method --- */

    @Step("Wait until Welcome Ad dialog is loaded")
    @Override
    public WelcomeAdDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(reportAdButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click on 'Close Ad' button if displayed")
    public void clickOnCloseAdButtonIfDisplayed() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(closeAdButton, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()))) {
            clickOnElement(closeAdButton);
        }
    }

}
