package com.tunein.mobile.pages.common.subscription;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.cssSelector;

public abstract class AlexaUpsellPage extends BasePage {

    protected static final String UPSELL_URL_PART = "upsell";

    // AndroidFindBy() is buggy with WebViews

    protected SelenideElement alexaStartFreeTrial = $(android(cssSelector("[data-testid = submitButton]"))
            .ios(iOSNsPredicateString("name == \"GO PREMIUM\" OR name == \"Start Free Trial\" AND type == \"XCUIElementTypeLink\""))).as("Start free-trial button");

    protected SelenideElement alexaNoThanks = $(android(cssSelector("[data-testid = closeLink]"))
            .ios(iOSNsPredicateString("name == \"No Thanks\" AND type == \"XCUIElementTypeLink\""))).as("No thanks button");

    protected SelenideElement alexaTosLink = $(android(cssSelector("[data-testid = policyLink]"))
            .ios(iOSNsPredicateString("name == \"Terms of Service\" AND type == \"XCUIElementTypeLink\""))).as("Terms of Service button");

    protected SelenideElement alexaPrivacyLink = $(android(cssSelector("[data-testid = privacyLink]"))
            .ios(iOSNsPredicateString("name == \"Privacy Policy\" AND type == \"XCUIElementTypeLink\""))).as("Privacy Policy button");

    /* --- Loadable Component Method --- */

    public abstract AlexaUpsellPage waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract void tapNoThanksButton();

    /* --- Validation Methods --- */

    @Step
    public AlexaUpsellPage validateIsOnAlexaUpsellPage() {
        assertThat(isOnAlexaUpsellPage()).as("Alexa upsell page is not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public abstract boolean isOnAlexaUpsellPage();
}
