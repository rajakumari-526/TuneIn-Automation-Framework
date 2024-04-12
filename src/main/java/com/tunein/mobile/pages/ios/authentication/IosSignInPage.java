package com.tunein.mobile.pages.ios.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.SignInPage;
import com.tunein.mobile.testdata.models.Users;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNIN_FORM;
import static com.tunein.mobile.utils.ElementHelper.*;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public class IosSignInPage extends SignInPage {

    protected SelenideElement signInDeviceImage = $(iOSNsPredicateString("name == 'Devices'")).as("Device image");

    protected SelenideElement signInRememberMeCheckBox = $(iOSNsPredicateString("name IN {'regwallRememberMeCheckboxId_true','regwallRememberMeCheckboxId_false'}")).as("Remember me checkbox");

    protected SelenideElement signInOkButton = $(accessibilityId("OK")).as("Ok button");

    protected static final String EMAIL_ADDRESS_PLACEHOLDER = "Email address";

    private static final String SIGNIN_FAILED_MESSAGE = "sorry, an error has occurred. Please try again.";

    @Step("Tap on SignIn Submit button")
    @Override
    public SignInPage tapSignInSubmitButton() {
        clickOnElement(signInSubmitButton);
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        return this;
    }

    @Step
    @Override
    public SignInPage setRememberMeValue(boolean checkBoxValue) {
        if (getElementName(signInRememberMeCheckBox).contains("true") != checkBoxValue) {
            clickOnElement(signInRememberMeCheckBox);
        }
        return this;
    }

    @Step("Sign-in with a {user} user")
    @Override
    public void signInFlowForUser(Users user) {
        navigationAction.navigateTo(SIGNIN_FORM);
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        if (getElementValue(signInPasswordField).isEmpty()) {
            typePassword(user.getPassword());
        }
        tapSignInSubmitButton();
        userProfilePage
                .waitUntilPageReady()
                .closeProfilePage();
    }

    @Step
    @Override
    public SignInPage typeEmail(String email) {
        deviceNativeActions.typeText(signInEmailField, email);
        return this;
    }

    @Step
    @Override
    public SignInPage typePassword(String password) {
        deviceNativeActions.typeText(signInPasswordField, password);
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> signInPageElements() {
        return new HashMap<>() {{
            put("Sign in.", signInTitle);
            put("Email address", signInEmailField);
            put("Password", signInPasswordField);
            put("SIGN IN", signInSubmitButton);
            put("FORGOT PASSWORD?", signInForgotPasswordButton);
            put("Terms of Service", signInTermsAndPrivacyButton);
            put("Privacy Policy", signInPrivacyPolicyButton);
            put("Remember me", signInRememberMeCheckBox);
        }};
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public SignInPage validateEmailFieldIsEmpty() {
        assertThat(getElementText(signInEmailField)).as("email field is not empty").isEqualTo(EMAIL_ADDRESS_PLACEHOLDER);
        return this;
    }

    @Override
    public SignInPage validatePlaceholderTextIsNotDisplayedForSignInFields() {
        throw new UnsupportedOperationException("Functionality is absent for iOS");
    }

    @Override
    public SignInPage validatePlaceholderTextIsDisplayedForSignInFields() {
        throw new UnsupportedOperationException("Functionality is absent for iOS");
    }

    @Step
    @Override
    public SignInPage closeLoginErrorPopUp() {
        alert.handleAlertIfDisplayed(SIGNIN_FAILED_MESSAGE, OK_BUTTON_TEXT);
        return this;
    }

}
