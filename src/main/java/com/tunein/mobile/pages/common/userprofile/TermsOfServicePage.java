package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.authentication.RegWallPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class TermsOfServicePage extends BasePage {

    protected SelenideElement termsOfServicePageTitle = $(android(androidUIAutomator("resourceId(\"policy-end-user-license-agreement-and-terms-of-service\")"))
            .ios(iOSNsPredicateString("label CONTAINS 'Terms of Service'"))).as("Terms of Service page title");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public TermsOfServicePage waitUntilPageReady() {
        waitTillVisibilityOfElement(termsOfServicePageTitle, Duration.ofSeconds(config().oneMinuteInSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract RegWallPage tapBackButton();

        /* --- Validation Methods --- */

    @Step
    public TermsOfServicePage validateTermsOfServicePageIsOpened() {
        assertThat(isOnTermsOfServicePage()).as("'Terms of Service' page title failed to display").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnTermsOfServicePage() {
        return isElementDisplayed(termsOfServicePageTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }
}
