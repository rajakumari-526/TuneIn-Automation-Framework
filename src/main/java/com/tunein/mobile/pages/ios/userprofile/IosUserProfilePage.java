package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;

import java.time.Duration;
import java.util.HashMap;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

public class IosUserProfilePage extends UserProfilePage {

    @Step
    @Override
    public UserProfilePage tapOnSignOutButton() {
        clickOnElement(profileSignOutButton);
        regWallPage
                .waitUntilPageReady()
                .closeRegWallPageIfDisplayed(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        return waitUntilPageReadySignOutUser();
    }

    @Override
    public UserProfilePage tapOnSignOutAndStaySignedIn() {
        throw new RuntimeException("throw new UnsupportedOperationException(\"Functionality is absent for iOS Platform\"");
    }

    @Step("Tap on signout button")
    @Override
    public void tapOnSignOutButtonLightVersion() {
        clickOnElement(profileSignOutButton);
    }

    @Step("Sign out of TuneIn Radio user account")
    @Override
    public void signOutUserFlow() {
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnSignOutButton()
                .closeProfilePage();
    }

    @Step("Validate User Profile details for user {user}")
    @Override
    public UserProfilePage validateUserProfileDetails(Users user) {
        getSoftAssertion().assertThat(getFullname()).as("Profile fullname should match").isEqualTo(user.getProfileName());
        getSoftAssertion().assertThat(getUsername()).as("Profile username should match").isEqualTo(user.getUsername());
        getSoftAssertion().assertAll();
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> profilePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "0", profileCloseButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "1", profileImage);
        elementsMap.put("Settings", profileSettingsButton);
        elementsMap.put("TuneIn Premium", profileTuneInPremiumButton);
        elementsMap.put("About TuneIn", profileAboutTuneInButton);
        elementsMap.put("Help Center", profileHelpCenterButton);

        if (isUserSignedIn()) {
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "2", profileFullNameTitle);
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "3", profileUsernameTitle);
            elementsMap.put("Edit Profile", profileEditButton);
            elementsMap.put("Sign Out", profileSignOutButton);
        } else {
            elementsMap.put("Account", profileSignedOutUserTitle);
            elementsMap.put("Login/Sign Up", profileLoginSignUpButton);
        }
        return elementsMap;
    }

    @Override
    public UserProfilePage closeSignOutPopUpIfDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Step("Validate that user name is equal to {userName}")
    @Override
    public UserProfilePage validateThatUserNameIsEqualTo(String userName) {
        assertThat(profileFullNameTitle.getText()).as("User name is not matching with" + userName).isEqualTo(userName);
        return this;
    }

}
