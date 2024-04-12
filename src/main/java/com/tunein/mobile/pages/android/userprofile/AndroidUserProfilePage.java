package com.tunein.mobile.pages.android.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;
import io.appium.java_client.AppiumBy;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidUserProfilePage extends UserProfilePage {

    private static final String SIGN_OUT_POP_UP_TITLE = "Are you sure? Staying signed in gives you seamless access to your recents and favorites across all your devices.";

    private static final String SIGN_OUT_BUTTON = "SIGN OUT";

    private static final String STAY_SIGNED_IN_BUTTON = "STAY SIGNED IN";

    protected SelenideElement profileStaySignedInButton = $(AppiumBy.androidUIAutomator("text(\"STAY SIGNED IN\")")).as("Stay signed-in button");

    @Step
    @Override
    public UserProfilePage tapOnSignOutButton() {
        clickOnElement(profileSignOutButton);
        closeSignOutPopUpIfDisplayed();
        return waitUntilPageReadySignOutUser();
    }

    @Step
    @Override
    public UserProfilePage tapOnSignOutAndStaySignedIn() {
        clickOnElement(profileSignOutButton);
        validateSignOutConfirmationModalIsDisplayed();
        clickOnElement(profileStaySignedInButton);
        return this;
    }

    @Step("Tap on signout button")
    @Override
    public void tapOnSignOutButtonLightVersion() {
        clickOnElement(profileSignOutButton);
        closeSignOutPopUpIfDisplayed();
    }

    @Step("Sign out of TuneIn Radio user account")
    @Override
    public void signOutUserFlow() {
        navigationAction
                .navigateTo(PROFILE);
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
            elementsMap.put("Login/Sign Up", profileLoginSignUpButton);
        }
        return elementsMap;
    }

    @Step
    @Override
    public UserProfilePage closeSignOutPopUpIfDisplayed() {
        alert.handleAlertIfDisplayed(SIGN_OUT_POP_UP_TITLE, SIGN_OUT_BUTTON);
        return this;
    }

    @Step("Validate that user name is equal to {userName}")
    @Override
    public UserProfilePage validateThatUserNameIsEqualTo(String userName) {
        assertThat(profileSignedOutUserTitle.getText()).as("User name is not matching with" + userName).isEqualTo(userName);
        return this;
    }

}
