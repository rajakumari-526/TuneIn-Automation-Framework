package com.tunein.mobile.pages.ios.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.FacebookAuthenticationPage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosFacebookAuthenticationPage extends FacebookAuthenticationPage {

    protected SelenideElement facebookAuthLoginWithEmailButton = $(iOSNsPredicateString("name == 'Log in with phone or email' AND type == 'XCUIElementTypeLink'")).as("Button 'Login with email'");

    @Step("Wait until Facebook Authentication page is load")
    @Override
    public FacebookAuthenticationPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(facebookAuthLoginWithEmailButton);
        waitTillVisibilityOfElement(facebookAuthTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    @Override
    public UserProfilePage logInToFacebook(Users user) {
        if (isElementDisplayed(facebookAuthEmailTextField)) {
            typeEmail(user.getEmail());
            typePassword(user.getPassword());
            tapLogInButton();
        }
        tapContinueButton();
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        return userProfilePage.waitUntilPageReady();
    }

}
