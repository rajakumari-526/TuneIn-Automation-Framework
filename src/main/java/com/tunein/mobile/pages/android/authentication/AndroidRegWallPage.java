package com.tunein.mobile.pages.android.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.FacebookAuthenticationPage;
import com.tunein.mobile.pages.common.authentication.GoogleAuthenticationPage;
import com.tunein.mobile.pages.common.authentication.RegWallPage;

import java.util.HashMap;

public class AndroidRegWallPage extends RegWallPage {

    @Override
    public RegWallPage tapOnSignUpWithAppleButton() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public FacebookAuthenticationPage tapSignUpWithFacebookButton() {
        clickOnElement(regWallSignUpWithFacebookButton);
        return facebookAuthenticationPage.waitUntilPageReady();
    }

    @Step
    @Override
    public GoogleAuthenticationPage tapSignUpWithGoogleButton() {
        clickOnElement(regWallSignUpWithGoogleButton);
        return googleAuthenticationPage.waitUntilPageReady();
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithAppleButton() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithFacebookButton() {
        clickOnElement(regWallSignUpWithFacebookButton);
        deviceNativeActions.clickBackButton();
        return regWallPage.waitUntilPageReady();
    }

    @Step
    @Override
    public RegWallPage cancelSignUpFlowWithGoogleButton() {
        clickOnElement(regWallSignUpWithGoogleButton);
        deviceNativeActions.clickBackButton();
        return regWallPage.waitUntilPageReady();
    }

    @Override
    public HashMap<String, SelenideElement> regWallPageElements() {
        return new HashMap<>() {{
            put("", regWallCloseButton);
            put("Welcome.", regWallBannerTitle);
            put("TuneIn brings together live sports, music, news, and podcasts — hear what matters most to you! Sign in or create an account today.", regWallBannerSubtitle);
            put("CONTINUE WITH GOOGLE", regWallSignUpWithGoogleButton);
            put("CONTINUE WITH FACEBOOK", regWallSignUpWithFacebookButton);
            put("SIGN UP WITH EMAIL", regWallSignUpWithEmailButton);
            put("Sign In", regWallSignInButton);
            put("Terms of Service", regWallTermsAndPrivacyButton);
            put("Privacy Policy", regWallPrivacyPolicyButton);
        }};
    }

}
