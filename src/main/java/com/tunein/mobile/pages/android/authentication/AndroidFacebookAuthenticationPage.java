package com.tunein.mobile.pages.android.authentication;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.FacebookAuthenticationPage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class AndroidFacebookAuthenticationPage extends FacebookAuthenticationPage {

    @Step
    @Override
    public FacebookAuthenticationPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
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
        return userProfilePage.waitUntilPageReady();
    }

}
