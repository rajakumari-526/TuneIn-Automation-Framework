package com.tunein.mobile.pages.ios.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.FacebookAuthenticationPage;
import com.tunein.mobile.pages.common.authentication.GoogleAuthenticationPage;
import com.tunein.mobile.pages.common.authentication.RegWallPage;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.alert.Alert.*;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosRegWallPage extends RegWallPage {

    private static final String USE_GOOGLE_TO_SIGN_IN_TITLE = "Wants to Use “google.com” to Sign In";

    private static final String USE_FACEBOOK_TO_SIGN_IN_TITLE = "Wants to Use “facebook.com” to Sign In";

    private static final String USE_APPLEID_TO_SIGN_IN_TITLE = "Sign in with your Apple ID";

    private static final String APPLEID_LOGIN_FAILED_TITLE = "Login failed";

    protected SelenideElement regWallSignUpWithAppleButton = $(iOSNsPredicateString("name == 'regwallAppleSignInButtonId'")).as("Sign-up with Apple button");

    @Step
    @Override
    public RegWallPage tapOnSignUpWithAppleButton() {
        clickOnElement(regWallSignUpWithAppleButton);
        alert.handleAlertIfDisplayed(USE_APPLEID_TO_SIGN_IN_TITLE, SETTINGS_BUTTON_TEXT);
        return this;
    }

    @Step
    @Override
    public FacebookAuthenticationPage tapSignUpWithFacebookButton() {
        clickOnElement(regWallSignUpWithFacebookButton);
        alert.handleAlertIfDisplayed(USE_FACEBOOK_TO_SIGN_IN_TITLE, CONTINUE_BUTTON_TEXT);
        return facebookAuthenticationPage.waitUntilPageReady();
    }

    @Step
    @Override
    public GoogleAuthenticationPage tapSignUpWithGoogleButton() {
        clickOnElement(regWallSignUpWithGoogleButton);
        alert.handleAlertIfDisplayed(USE_GOOGLE_TO_SIGN_IN_TITLE, CONTINUE_BUTTON_TEXT);
        return googleAuthenticationPage.waitUntilPageReady();
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithAppleButton() {
        clickOnElement(regWallSignUpWithAppleButton);
        alert.handleAlertIfDisplayed(USE_APPLEID_TO_SIGN_IN_TITLE, CLOSE_BUTTON_TEXT);
        alert.handleAlertIfDisplayed(APPLEID_LOGIN_FAILED_TITLE, OK_BUTTON_TEXT);
        return this;
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithFacebookButton() {
        clickOnElement(regWallSignUpWithFacebookButton);
        alert.handleAlertIfDisplayed(USE_FACEBOOK_TO_SIGN_IN_TITLE, CANCEL_BUTTON_TEXT);
        return regWallPage.waitUntilPageReady();
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithGoogleButton() {
        clickOnElement(regWallSignUpWithGoogleButton);
        alert.handleAlertIfDisplayed(USE_GOOGLE_TO_SIGN_IN_TITLE, CANCEL_BUTTON_TEXT);
        return regWallPage.waitUntilPageReady();
    }

    @Override
    public HashMap<String, SelenideElement> regWallPageElements() {
        return new HashMap<>() {{
            put("Close", regWallCloseButton);
            put("Welcome.", regWallBannerTitle);
            put("TuneIn brings together live sports, music, news, and podcasts — hear what matters most to you! Sign in or create an account today.", regWallBannerSubtitle);
            put("CONTINUE WITH GOOGLE", regWallSignUpWithGoogleButton);
            put("CONTINUE WITH FACEBOOK", regWallSignUpWithFacebookButton);
            put("CONTINUE WITH APPLE", regWallSignUpWithAppleButton);
            put("SIGN UP WITH EMAIL", regWallSignUpWithEmailButton);
            put("Sign In", regWallSignInButton);
            put("Terms of Service", regWallTermsAndPrivacyButton);
            put("Privacy Policy", regWallPrivacyPolicyButton);
        }};
    }

}
