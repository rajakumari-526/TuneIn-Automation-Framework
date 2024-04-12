package com.tunein.mobile.pages.android.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.SignInPage;
import com.tunein.mobile.testdata.models.Users;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNIN_FORM;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidSignInPage extends SignInPage {

    protected SelenideElement signInEmailText = $(By.id("signin_enter_email_hint")).as("Email text");

    protected SelenideElement signInPasswordText = $(By.id("sign_in_password_hint")).as("Password text");

    protected SelenideElement signInOkButton = $(androidUIAutomator("text(\"OK\")")).as("OK button");

    private static final String EMAIL_ADDRESS_PLACEHOLDER = "Email or Username";

    private static final String PASSWORD_PLACEHOLDER = "Password";

    private static final String SIGNIN_FAILED_MESSAGE = "Your username or password was not recognized";

    @Step("Tap on SignIn Submit button")
    @Override
    public SignInPage tapSignInSubmitButton() {
        clickOnElement(signInSubmitButton);
        if (isElementDisplayed(signInFailedMessage, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            clickOnElement(signInOkButton);
            clickOnElement(signInSubmitButton);
        }
        termsOfServiceDialog.closeTermsOfServiceDialogIfPresent();
        return this;
    }

    @Step
    @Override
    public SignInPage setRememberMeValue(boolean checkBoxValue) {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step("Sign-in with a {user} user")
    @Override
    public void signInFlowForUser(Users user) {
        navigationAction.navigateTo(SIGNIN_FORM);
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        tapSignInSubmitButton();
        userProfilePage
                .waitUntilPageReady()
                .closeProfilePage();
    }

    @Step("Type email: {email}")
    @Override
    public SignInPage typeEmail(String email) {
        waitTillVisibilityOfElement(signInEmailField).setValue(email);
        return this;
    }

    @Step("Type password")
    @Override
    public SignInPage typePassword(String password) {
        waitTillVisibilityOfElement(signInPasswordField).setValue(password);
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> signInPageElements() {
        return new HashMap<>() {{
            put("Sign In.", signInTitle);
            put("Email or Username", signInEmailText);
            put("Email or Username", signInEmailField);
            put("Password", signInPasswordText);
            put("Password", signInPasswordField);
            put("SIGN IN", signInSubmitButton);
            put("FORGOT PASSWORD?", signInForgotPasswordButton);
            put("Terms of Service", signInTermsAndPrivacyButton);
            put("Privacy Policy", signInPrivacyPolicyButton);
        }};
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public SignInPage validateEmailFieldIsEmpty() {
        assertThat(getElementText(signInEmailField)).as("email field is not empty").isEqualTo(EMAIL_ADDRESS_PLACEHOLDER);
        return this;
    }

    @Step
    @Override
    public SignInPage closeLoginErrorPopUp() {
        alert.handleAlertIfDisplayed(SIGNIN_FAILED_MESSAGE, OK_BUTTON_TEXT);
        return this;
    }

    @Override
    public SignInPage validatePlaceholderTextIsNotDisplayedForSignInFields() {
        getSoftAssertion().assertThat(getElementText(signInEmailField)).as("'Email Address' text is displayed after entering Email in SignIn page").isNotEqualTo(EMAIL_ADDRESS_PLACEHOLDER);
        getSoftAssertion().assertThat(getElementText(signInPasswordField)).as("'Password' text is displayed after entering Password in SignIn page").isNotEqualTo(PASSWORD_PLACEHOLDER);
        getSoftAssertion().assertAll();
        return this;
    }

    @Override
    public SignInPage validatePlaceholderTextIsDisplayedForSignInFields() {
        getSoftAssertion().assertThat(getElementText(signInEmailField)).as("'Email Address' text is not displayed after clearing Email in SignIn page").isEqualTo(EMAIL_ADDRESS_PLACEHOLDER);
        getSoftAssertion().assertThat(getElementText(signInPasswordField)).as("'Password' text is not displayed after clearing password in SignIn page").isEqualTo(PASSWORD_PLACEHOLDER);
        getSoftAssertion().assertAll();
        return this;
    }

}
