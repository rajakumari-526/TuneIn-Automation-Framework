package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementEnabled;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SignInPage extends BasePage {

    protected SelenideElement signInTitle = Selenide.$(android(id("signin_title"))
            .ios(iOSNsPredicateString("name == 'signUpTitleId'"))).as("Sign-in title");

    protected SelenideElement signInEmailField = Selenide.$(android(id("emailAddress"))
            .ios(id("regwallEmailFieldId"))).as("Email field");

    protected SelenideElement signInPasswordField = Selenide.$(android(id("password"))
            .ios(id("regwallPasswordFieldId"))).as("Password field");

    protected SelenideElement signInForgotPasswordButton = Selenide.$(android(id("forgotPassword"))
            .ios(iOSNsPredicateString("name == 'signInForgotPasswordButtonId'"))).as("Forget password button");

    protected SelenideElement signInSubmitButton = Selenide.$(android(id("next"))
            .ios(iOSNsPredicateString("name CONTAINS 'regwallSignInButtonId'"))).as("Submit button");

    protected SelenideElement signInTermsAndPrivacyButton = Selenide.$(android(id("fragment_reg_wall_terms_and_privacy"))
            .ios(iOSNsPredicateString("name == 'regwallTermsOfServiceId'"))).as("Terms and Privacy button");

    protected SelenideElement signInPrivacyPolicyButton = Selenide.$(android(id("fragment_reg_wall_tos_privacy"))
            .ios(iOSNsPredicateString("name == 'regwallPrivacyPolicyId'"))).as("Privacy Policy button");

    protected SelenideElement signInFailedMessage = Selenide.$(android(androidUIAutomator("text(\"Your username or password was not recognized\")"))
            .ios(iOSNsPredicateString("label == 'Login failed' AND type == 'XCUIElementTypeStaticText'"))).as("Sign-in failed message");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public SignInPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(signInSubmitButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract SignInPage tapSignInSubmitButton();

    public abstract SignInPage setRememberMeValue(boolean checkBoxValue);

    @Step
    public UserProfilePage signInWithCredentials(String email, String password) {
        typeEmail(email);
        typePassword(password);
        tapSignInSubmitButton();
        return userProfilePage.waitUntilPageReadySignInUser();
    }

    @Step
    public SignInPage signInWithCredentialsLightVersion(String email, String password) {
        typeEmail(email);
        typePassword(password);
        clickOnElement(signInSubmitButton);
        return this;
    }

    public abstract void signInFlowForUser(Users user);

    public abstract SignInPage typeEmail(String email);

    public abstract SignInPage typePassword(String password);

    @Step
    public SignInPage tapOnSignInButtonLightVersion() {
        clickOnElement(signInSubmitButton);
        return this;
    }

    @Step
    public ForgotPasswordPage tapForgetPassword() {
        clickOnElement(signInForgotPasswordButton);
        return forgotPasswordPage.waitUntilPageReady();
    }

    public abstract SignInPage validatePlaceholderTextIsNotDisplayedForSignInFields();

    public abstract SignInPage validatePlaceholderTextIsDisplayedForSignInFields();

    public abstract SignInPage closeLoginErrorPopUp();

    /* --- Validation Methods --- */

    public abstract SignInPage validateEmailFieldIsEmpty();

    @Step
    public SignInPage validateEmailFieldIsFilled(String email) {
        assertThat(getElementText(signInEmailField)).as("email field is empty").isEqualTo(email);
        return this;
    }

    @Step
    public SignInPage validateThatSignInAccountButtonIsDisabled() {
        assertThat(isElementEnabled(signInSubmitButton)).as("SignIn to Your Account button is enabled").isFalse();
        return this;
    }

    @Step
    public SignInPage validateThatSignInFailedMessageDisplayed() {
        assertThat(isElementEnabled(signInFailedMessage)).as("Invalid Sign In message is not displayed").isTrue();
        return this;
    }

    @Step
    public SignInPage clearAllFieldsInSignInPage() {
        deviceNativeActions.clearText(signInEmailField);
        deviceNativeActions.clearText(signInPasswordField);
        return this;
    }

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> signInPageElements();
}
