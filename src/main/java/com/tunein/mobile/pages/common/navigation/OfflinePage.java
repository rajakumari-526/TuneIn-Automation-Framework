package com.tunein.mobile.pages.common.navigation;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class OfflinePage extends BasePage {

    protected SelenideElement profileUnavailableInOffline = $(android(xpath("//*[contains(@resource-id,'noConnectionTxt') or contains(@resource-id,'pageNotFoundDescription')]"))
            .ios(iOSNsPredicateString("name CONTAINS 'This page is not available while you\'re offline'"))).as("Unavailable page in offline");

    /* --- Validation Methods --- */

    @Step
    public OfflinePage validateOfflineErrorMessageIsDisplayed() {
        assertThat(isElementDisplayed(profileUnavailableInOffline))
                .as("Profile is not available when you are offline text is not displayed")
                .isTrue();
        return this;
    }

    @Step
    @Override
    public OfflinePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(profileUnavailableInOffline, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }
}
