package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public class PrivacyPolicyPage extends BasePage {

    protected SelenideElement privacyPolicyPageTitle = $(android(androidUIAutomator("text(\"Privacy Policy\")"))
            .ios(iOSNsPredicateString("label IN {'PRIVACY POLICY', 'Privacy Policy'} AND type == 'XCUIElementTypeStaticText'"))).as("Privacy Policy page title");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public PrivacyPolicyPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(privacyPolicyPageTitle, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public PrivacyPolicyPage validatePrivacyPolicyPageIsOpened() {
        assertThat(isOnPrivacyPolicyPage()).as("Privacy Policy page was not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnPrivacyPolicyPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(privacyPolicyPageTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }
}
