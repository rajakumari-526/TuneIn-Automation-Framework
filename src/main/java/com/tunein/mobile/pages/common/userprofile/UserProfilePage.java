package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.testdata.models.Users;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class UserProfilePage extends BasePage {

    protected SelenideElement profileSignedInSignedOutButton = $(android(id("sign_in_button"))
            .ios(iOSNsPredicateString("name CONTAINS 'contentProfilePrompt' AND type == 'XCUIElementTypeButton' AND visible == true"))).as("Signed-In/Signed-Out button");

    protected SelenideElement profileEditButton = $(android(id("edit_profile_button"))
            .ios(iOSNsPredicateString("label == 'EDIT PROFILE' AND visible == true"))).as("Edit button");

    protected SelenideElement profileLoginSignUpButton = $(android(androidUIAutomator("text(\"LOGIN / SIGN UP\")"))
            .ios(iOSNsPredicateString("name == 'contentProfilePromptTopButton' AND visible == true"))).as("Sign-Up button");

    protected SelenideElement profileSignOutButton = $(android(androidUIAutomator("text(\"SIGN OUT\")"))
            .ios(iOSNsPredicateString("name == 'contentProfilePromptBottomButton' AND visible == true"))).as("Sign-Out button");

    protected SelenideElement signOutConfirmationModal = $(android(id("android:id/message"))
            .ios(iOSNsPredicateString("name CONTAINS 'contentProfilePrompt' AND type == 'XCUIElementTypeButton' AND visible == true"))).as("Settings button");

    protected SelenideElement profileSettingsButton = $(android(androidUIAutomator("text(\"Settings\")"))
            .ios(iOSNsPredicateString("name == 'Settings'"))).as("Confirmation modal");

    protected SelenideElement profileTuneInPremiumButton = $(android(androidUIAutomator("text(\"TuneIn Premium\")"))
            .ios(iOSNsPredicateString("name == 'TuneIn Premium'"))).as("TuneIn Premium button");

    protected SelenideElement profileAboutTuneInButton = $(android(androidUIAutomator("text(\"About TuneIn\")"))
            .ios(iOSNsPredicateString("name == 'About TuneIn'"))).as("About TuneIn button");

    protected SelenideElement profileHelpCenterButton = $(android(androidUIAutomator("text(\"Help Center\")"))
            .ios(iOSNsPredicateString("name == 'Help Center'"))).as("Help Center button");

    protected SelenideElement profileImage = $(android(id("profile_image"))
            .ios(iOSNsPredicateString("name == 'favoritesProfileImage'"))).as("Profile image");

    protected SelenideElement profileFullNameTitle = $(android(id("profile_title"))
            .ios(iOSNsPredicateString("name == 'favoritesFullname'"))).as("Fullname title");

    protected SelenideElement profileUsernameTitle = $(android(id("username"))
            .ios(iOSNsPredicateString("name == 'favoritesUsername'"))).as("Username title");

    protected SelenideElement profileSignedOutUserTitle = $(android(id("profile_title"))
            .ios(id("Account"))).as("Signed-out user title");

    protected SelenideElement profileCloseButton = $(android(id("close_button"))
            .ios(iOSNsPredicateString("name == 'closeModeIdNavBarButton' AND visible == true"))).as("Close button");


    /* --- Loadable Component Method --- */

    @Step("Wait until User profile page is loaded")
    @Override
    public UserProfilePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(profileSignedInSignedOutButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Wait until User profile page is loaded for signed in user")
    public UserProfilePage waitUntilPageReadySignInUser() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(profileEditButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Wait until User profile page is loaded for signed out user")
    public UserProfilePage waitUntilPageReadySignOutUser() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(profileLoginSignUpButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract UserProfilePage tapOnSignOutButton();

    @Step
    public abstract UserProfilePage tapOnSignOutAndStaySignedIn();

    public abstract void tapOnSignOutButtonLightVersion();

    public abstract void signOutUserFlow();

    @Step("Tap on Log-in/ Sign-up button")
    public RegWallPage tapOnLoginSignupButton() {
        if (isElementDisplayed(profileSignOutButton)) {
            tapOnSignOutButton();
        }
        clickOnElement(profileLoginSignUpButton);
        return regWallPage.waitUntilPageReady();
    }

    @Step("Close Profile page")
    public void closeProfilePage() {
        clickOnElement(profileCloseButton);
    }

    @Step("Close Profile page if displayed")
    public void closeProfilePageIfDisplayed() {
        clickOnElementIfDisplayed(profileCloseButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }

    @Step("Tap on edit profile button")
    public EditUserProfilePage tapEditProfileButton() {
        clickOnElement(profileEditButton);
        return editUserProfilePage.waitUntilPageReady();
    }

    @Step("Tap on Settings button")
    public SettingsPage tapOnSettingsButton() {
        clickOnElement(profileSettingsButton);
        return settingsPage.waitUntilPageReady();
    }

    @Step("Tap on TuneIN Premium Button")
    public TuneInPremiumPage tapOnTuneInPremiumButton() {
        clickOnElement(profileTuneInPremiumButton);
        return tuneInPremiumPage.waitUntilPageReady();
    }

    @Step("Tap on About TuneIn button")
    public AboutPage tapOnAboutTuneInButton() {
        clickOnElement(profileAboutTuneInButton);
        return aboutPage.waitUntilPageReady();
    }

    @Step("Tap on Help Center button")
    public HelpCenterWebPage tapOnHelpCenterButton() {
        clickOnElement(profileHelpCenterButton);
        return helpCenterWebPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    public abstract UserProfilePage validateUserProfileDetails(Users user);

    @Step
    public UserProfilePage validateThatSignOutButtonIsDisplayed() {
        assertThat(isElementDisplayed(profileSignOutButton)).as("SignOut button is not displayed").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateThatUserSignedOut(String userFullname) {
        getSoftAssertion().assertThat(getFullname()).as("User is not signed out").isNotEqualTo(userFullname);
        getSoftAssertion().assertThat(isElementDisplayed(profileLoginSignUpButton)).as("Sign In button is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    public UserProfilePage validateThatOnProfilePage() {
        assertThat(isOnProfilePage()).as("Profile page was not opened").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateSignInButtonIsDisplayed() {
        assertThat(isSignInButtonDisplayed()).as("Sign-In button is not displayed").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateEditProfileButtonIsDisplayed() {
        assertThat(isEditProfileButtonDisplayed()).as("Edit profile button is not displayed").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateEditProfileButtonIsNotDisplayed() {
        assertThat(isEditProfileButtonNotDisplayed()).as("Edit profile button is displayed on Profile page").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateSignOutConfirmationModalIsDisplayed() {
        assertThat(isElementDisplayed(signOutConfirmationModal)).as("Sign Out confirmation prompt is not displayed on Profile page").isTrue();
        return this;
    }

    @Step
    public UserProfilePage validateMainProfileButtonsAreDisplayed() {
        getSoftAssertion().assertThat(isElementDisplayed(profileSettingsButton)).as("Settings button is not displayed on Profile page").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(profileAboutTuneInButton)).as("About button is not displayed on Profile page").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(profileTuneInPremiumButton)).as("Tune In premium button is not displayed on Profile page").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(profileHelpCenterButton)).as("Help Center button is not displayed on Profile page").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    public abstract UserProfilePage validateThatUserNameIsEqualTo(String userName);

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> profilePageElements();

    public boolean isOnProfilePage() {
        return isElementDisplayed(profileSettingsButton);
    }

    public boolean isEditProfileButtonDisplayed() {
        return isElementDisplayed(profileEditButton);
    }

    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(profileLoginSignUpButton);
    }

    public boolean isEditProfileButtonNotDisplayed() {
        return isElementNotDisplayed(profileEditButton);
    }

    public boolean isUserSignedIn() {
        return isElementDisplayed(profileSignOutButton) && isElementDisplayed(profileEditButton);
    }

    public boolean isUserSignedOut() {
        return getUsername().equals("") && isElementDisplayed(profileLoginSignUpButton);
    }

    public String getSignedOutUserTitle() {
        return getElementText(profileSignedOutUserTitle);
    }

    public String getUsername() {
        return getElementText(profileUsernameTitle);
    }

    public String getFullname() {
        return getElementText(profileFullNameTitle);
    }

    public abstract UserProfilePage closeSignOutPopUpIfDisplayed();

}
