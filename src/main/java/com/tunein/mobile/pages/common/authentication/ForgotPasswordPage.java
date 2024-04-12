package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class ForgotPasswordPage extends BasePage {

    protected SelenideElement forgotSignInEmailField = $(android(id("emailAddress"))
            .ios(iOSNsPredicateString("name == 'forgotPasswordEmailFieldId'"))).as("Email field");

    protected SelenideElement resetPasswordEmailError = $(android(id("dialog_message"))
            .ios(iOSNsPredicateString("label == \"Please enter a valid email address.\""))).as("Email error");

    protected SelenideElement resetPasswordButton = $(android(id("next"))
            .ios(iOSNsPredicateString("name == 'forgotPasswordResetButtonId'"))).as("Reset password button");

    protected SelenideElement resetPasswordBackButton = $(android(id("header_back"))
            .ios(iOSNsPredicateString("label == \"Back\""))).as("Back button");

    protected SelenideElement forgotPasswordDescriptionLabel = $(android(xpath("//*[contains(@text,'Enter your email address and we')]"))
            .ios(accessibilityId("forgotPasswordTitleId"))).as("Description label");

    protected SelenideElement forgotPasswordPageTitle = $(android(androidUIAutomator("text(\"Forgot password?\")"))
            .ios(iOSNsPredicateString("label == 'Forgot password?'"))).as("Password page title");

    private static final String EMAIL_PLACEHOLDER = isAndroid() ? "Email" : "Email address";

    /* --- Action Methods --- */

    @Step
    public void tapOnResetPasswordButton() {
        clickOnElement(resetPasswordButton);
    }

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public ForgotPasswordPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(forgotPasswordPageTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    public ForgotPasswordPage typeEmail(String email) {
        deviceNativeActions.typeText(forgotSignInEmailField, email);
        clickOnElementIfDisplayed(forgotPasswordDescriptionLabel);
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public ForgotPasswordPage validateResetEmailErrorIsDisplayed() {
        assertThat(isElementDisplayed(resetPasswordEmailError)).as("Please enter a valid email address.").isTrue();
        return this;
    }

    @Step
    public ForgotPasswordPage validateResetPasswordButtonIsDisabled() {
        assertThat(isElementEnabled(resetPasswordButton)).as("ResetPassword button is Enabled").isFalse();
        return this;
    }

    @Step
    public ForgotPasswordPage validatePlaceholderForEmailInForgotPasswordPageIsDisplayed() {
        assertThat(getElementText(forgotSignInEmailField))
                .as("'Email' text is not displayed in forgot password page")
                .isEqualTo(EMAIL_PLACEHOLDER);
        return this;
    }
    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> forgotPasswordPageElements();
}
