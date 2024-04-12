package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.userprofile.PrivacyPolicyPage;
import com.tunein.mobile.pages.common.userprofile.TermsOfServicePage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class RegWallPage extends BasePage {

    protected SelenideElement regWallMainImage = Selenide.$(android(id("reg_wall_logo"))
            .ios(iOSNsPredicateString("name == 'TuneInInkLogo'"))).as("Main image");

    protected SelenideElement regWallBannerTitle = Selenide.$(android(id("title"))
            .ios(id("regwallTitleId"))).as("Banner title");

    protected SelenideElement regWallBannerSubtitle = Selenide.$(android(id("description"))
            .ios(iOSNsPredicateString("name == 'regwallTextId'"))).as("Banner subtitle");

    protected SelenideElement regWallCloseButton = Selenide.$(android(id("close_button"))
            .ios(iOSNsPredicateString("name == 'regwallCloseButtonId'"))).as("Close button");

    protected SelenideElement regWallSignUpWithEmailButton = Selenide.$(android(id("email_signup_button"))
            .ios(iOSNsPredicateString("name == 'regwallEmailSignUpButtonId'"))).as("Sign-up with emeail button");

    protected SelenideElement regWallSignUpWithGoogleButton = Selenide.$(android(id("google_signin_button"))
            .ios(iOSNsPredicateString("name == 'regwallGoogleSignInButtonId'"))).as("Sign-up with google button");

    protected SelenideElement regWallSignUpWithFacebookButton = Selenide.$(android(id("facebook_signin_button"))
            .ios(iOSNsPredicateString("name == 'regwallFacebookSignInButtonId'"))).as("Sign-up with facebook button");

    protected SelenideElement regWallSignInButton = Selenide.$(android(id("fragment_reg_wall_sign_in"))
            .ios(id("regwallEmailSignInButtonId"))).as("Sign-in button");

    protected SelenideElement regWallTermsAndPrivacyButton = Selenide.$(android(id("fragment_reg_wall_terms_and_privacy"))
            .ios(id("regwallTermsOfServiceId"))).as("Terms and Privacy button");

    protected SelenideElement regWallPrivacyPolicyButton = Selenide.$(android(id("fragment_reg_wall_tos_privacy"))
            .ios(iOSNsPredicateString("name == 'regwallPrivacyPolicyId'"))).as("Privacy Policy button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public RegWallPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(regWallSignInButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract RegWallPage tapOnSignUpWithAppleButton();
    
    public abstract FacebookAuthenticationPage tapSignUpWithFacebookButton();

    public abstract GoogleAuthenticationPage tapSignUpWithGoogleButton();

    public abstract RegWallPage cancelSignUpFlowWithAppleButton();

    public abstract RegWallPage cancelSignUpFlowWithFacebookButton();

    public abstract RegWallPage cancelSignUpFlowWithGoogleButton();

    @Step("Tap on Signin button")
    public SignInPage tapOnSignInButton() {
        clickOnElement(regWallSignInButton);
        return signInPage.waitUntilPageReady();
    }

    @Step("Close regwall page if displayed")
    public RegWallPage closeRegWallPageIfDisplayed(Duration duration) {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(regWallCloseButton, duration);
        return this;
    }

    @Step("Close regwall page")
    public UserProfilePage closeRegWallPage() {
        tapOnRegWallCloseButton();
        return userProfilePage.waitUntilPageReady();
    }

    @Step
    public void tapOnRegWallCloseButton() {
        clickOnElement(regWallCloseButton);
    }

    @Step
    public SignUpPage tapSignUpWithEmailButton() {
        clickOnElement(regWallSignUpWithEmailButton);
        return signUpPage.waitUntilPageReady();
    }

    @Step
    public TermsOfServicePage tapTermsOfServiceLink() {
        clickOnElement(regWallTermsAndPrivacyButton);
        return termsOfServicePage.waitUntilPageReady();
    }

    @Step
    public PrivacyPolicyPage tapPrivacyPolicyLink() {
        clickOnElement(regWallPrivacyPolicyButton);
        return privacyPolicyPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public RegWallPage validateCustomHeaderDisplayedOnRegwallPage(String header) {
        assertThat(getElementText(regWallBannerTitle)).as("Label " + header + " is not displayed").isEqualTo(header);
        return this;
    }

    @Step
    public RegWallPage validateRegwallPageIsDisplayed() {
        assertThat(isOnRegWallPage()).as("RegWall page was not opened").isTrue();
        return regWallPage.waitUntilPageReady();
    }

    @Step
    public void validateRegwallPageIsNotDisplayed() {
        assertThat(isOnRegWallPage()).as("RegWall page was opened").isFalse();
    }

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> regWallPageElements();

    public boolean isOnRegWallPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(regWallSignUpWithEmailButton);
    }

}
