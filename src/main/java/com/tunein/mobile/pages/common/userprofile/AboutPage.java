package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.utils.GestureActionUtil.*;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AboutPage extends BasePage {

    protected SelenideElement buildVersionSection = $(android(id("tunein.player:id/app_version_summary"))
            .ios(iOSNsPredicateString("name IN {'VersionSection', 'Version'} OR label == 'Version'"))).as("Version label");

    protected SelenideElement aboutPageHelpCenterButton = $(android(androidUIAutomator("text(\"Help Center\")"))
            .ios(iOSNsPredicateString("label == \"Help Center\""))).as("Help Center button");

    protected SelenideElement aboutPagePrivacyPolicyButton = $(android(androidUIAutomator("text(\"Privacy Policy\")"))
            .ios(iOSNsPredicateString("label == \"Privacy Policy\""))).as("Privacy Policy button");

    protected SelenideElement aboutPageTermsButton = $(android(androidUIAutomator("text(\"Terms\")"))
            .ios(iOSNsPredicateString("label == \"Terms\""))).as("Terms button");

    protected SelenideElement aboutPageLegalNoticesButton = $(android(androidUIAutomator("text(\"Legal Notices\")"))
            .ios(iOSNsPredicateString("label == \"Legal Notices\""))).as("Legal Notices button");

    protected SelenideElement helpCenterPage = $(android(xpath("//*[contains(@text,'help.tunein.com')]"))
            .ios(iOSNsPredicateString("name='TabBarItemTitle' AND value CONTAINS 'help.tunein.com'"))).as("Help Center page");

    protected SelenideElement appCrashButton = $(android(androidUIAutomator("text(\"Crash Reporting Test\")"))
            .ios(iOSNsPredicateString("label == \"Simulate Crash\""))).as("Simulate Crash button");

    /* --- Loadable Component Method --- */

    @Step("Wait until About page is ready")
    @Override
    public AboutPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(buildVersionSection, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Navigate to {aboutPageItems}")
    public void navigateToAboutPageItem(AboutPageItems aboutPageItems) {
        closePermissionPopupsIfDisplayed();
        switch (aboutPageItems) {
            case HELP_CENTER -> tapOnHelpCenterButton();
            case PRIVACY_POLICY -> tapOnPrivacyPolicyButton();
            case TERMS -> tapOnTermsButton();
            case LEGAL_NOTICES -> tapOnLegalNoticesButton();
            default -> throw new IllegalStateException("About page navigation item isn't available " + aboutPageItems);
        }
    }

    @Step("Click on Help Center button")
    public abstract HelpCenterWebPage tapOnHelpCenterButton();

    @Step("Click on Privacy policy button")
    public PrivacyPolicyPage tapOnPrivacyPolicyButton() {
        clickOnElement(scrollTo(aboutPagePrivacyPolicyButton, DOWN).as("Privacy policy button"));
        return privacyPolicyPage.waitUntilPageReady();
    }

    @Step("Click on Terms of service page")
    public TermsOfServicePage tapOnTermsButton() {
        clickOnElement(scrollTo(aboutPageTermsButton, DOWN).as("Terms button"));
        return termsOfServicePage.waitUntilPageReady();
    }

    public abstract LegalNoticesPage tapOnLegalNoticesButton();

    public AboutPage tapOnCrashAppSection(ScrollDirection direction, int... numberOfScrolls) {
        closePermissionPopupsIfDisplayed();
        clickOnElement(scrollTo(appCrashButton, direction, numberOfScrolls));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate that About page is opened")
    public AboutPage validateAboutPageIsOpened() {
        assertThat(isOnAboutPage()).as("About page was not opened").isTrue();
        return this;
    }

    @Step("Validate that Help Center page is opened")
    public void validateThatHelpCenterPageIsOpened() {
        assertThat(isElementDisplayed(helpCenterPage, Duration.ofSeconds(config().pageReadyTimeoutSeconds()))).as("Help center page failed to download").isTrue();
    }

    @Step("Validating about page items {aboutPageItems}")
    public void validateCorrespondingAboutPageIsOpened(AboutPageItems aboutPageItems) {
        switch (aboutPageItems) {
            case HELP_CENTER -> aboutPage.validateThatHelpCenterPageIsOpened();
            case PRIVACY_POLICY -> privacyPolicyPage.validatePrivacyPolicyPageIsOpened();
            case TERMS -> termsOfServicePage.validateTermsOfServicePageIsOpened();
            case LEGAL_NOTICES -> legalNoticesPage.validateThatLegalNoticesPageIsOpened();
            default -> throw new IllegalStateException("About page item isn't available " + aboutPageItems);
        }
    }

    /* --- Helper Methods --- */

    public SelenideElement getVersion() {
        return buildVersionSection;
    }

    public boolean isOnAboutPage() {
        return isElementDisplayed(scrollTo(buildVersionSection, UP));
    }

    public enum AboutPageItems {
        HELP_CENTER,
        PRIVACY_POLICY,
        TERMS,
        LEGAL_NOTICES
    }

    public abstract String getDeviceServiceIdentifier();

}
