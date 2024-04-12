package com.tunein.mobile.pages.common.premium;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class PremiumPage extends BasePage {

    protected SelenideElement premiumFreeTrialButton = $(android(id("primary_button"))
            .ios(iOSNsPredicateString("name == 'Start Free Trial' OR name == 'Go Premium' AND type == 'XCUIElementTypeButton'"))).as("Free-trial button");

    protected SelenideElement premiumPageTitle = $(android(id("title"))
            .ios(iOSNsPredicateString("name == 'compactPromptTitleId'"))).as("Premium page title");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public PremiumPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(premiumPageTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public UpsellPage tapFreeTrialButton() {
        clickOnElement(premiumFreeTrialButton);
        return upsellPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public PremiumPage validatePremiumPageIsOpened() {
        assertThat(isOnPremiumPage()).as("Premium page is not opened").isTrue();
        return this;
    }

    @Step("Validate start free trail button is displayed")
    public PremiumPage validateFreeTrailButtonIsDisplayed() {
        assertThat(isElementDisplayed(premiumFreeTrialButton)).as("Start Free Trail button is not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnPremiumPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(premiumPageTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }
}
