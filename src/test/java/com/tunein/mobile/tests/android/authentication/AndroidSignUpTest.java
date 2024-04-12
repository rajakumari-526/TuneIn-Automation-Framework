package com.tunein.mobile.tests.android.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.authentication.SignUpTest;
import com.tunein.mobile.utils.DataUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PROFILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNUP_FORM;
import static com.tunein.mobile.testdata.TestGroupName.SIGNUP_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.MALE;
import static com.tunein.mobile.utils.DataUtil.getBirthYear;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;

public class AndroidSignUpTest extends SignUpTest {

    @Override
    public void testSignUpUIForm() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .validateUIElements(signUpPage.signUpPageElements())
                .validateTextOfUIElements(signUpPage.signUpPageElements());
    }

    @Override
    public void testSignUpWithEmptyEmail() {
        Users randomUser = generateRandomUser();
        randomUser.setEmail("");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @Override
    public void testSignUpWithInvalidEmail() {
        Users randomUser = generateRandomUser();
        randomUser.setEmail("abc123.com");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateInvalidEmailErrorText()
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @TestCaseId("30672")
    @Test(description = "SignUp with empty Name field", groups = {SIGNUP_TEST})
    public void signUpWithEmptyNameField() {
        Users randomUser = generateRandomUser();
        randomUser.setProfileName("");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @TestCaseId("30684")
    @Test(description = "SignUp with required fields only", groups = {SIGNUP_TEST})
    public void signUpWithRequiredFieldsOnly() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser, true)
                .tapOnCreateYourAccountButton();
        userProfilePage
                .validateUserProfileDetails(randomUser)
                .tapOnSignOutButton();
    }

    @Override
    public void testSignUpWithAllEmptyFields() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.validateThatCreateYourAccountButtonIsDisabled();
    }

    @Override
    public void testSignUpWithInvalidBirthYear() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.fullFillSignUpFieldsWithData(randomUser);
        int[] birthYears = {12, 116, -1};
        for (int birthYear : birthYears) {
            signUpPage
                    .typeBirthYear(getBirthYear(birthYear))
                    .tapOnCreateYourAccountButton();
            signUpPage.validateThatRegistrationErrorIsDisplayed();
        }
    }

    @Override
    public void testSignUpWithExistingEmail() {
        Users randomUser = generateRandomUser();
        randomUser.setEmail(USER_FOR_LOGIN_TEST.getEmail());
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .tapOnCreateYourAccountButton();
        signUpPage
                .validateInvalidEmailErrorText()
                .validateErrorSignUpExistingEmailIsDisplayed();
    }

    @Override
    public void testSignUpWithEmptyPassword() {
        Users randomUser = generateRandomUser();
        randomUser.setPassword("");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @Override
    public void testSignUpWithEmptyBirthYear() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .typeName(randomUser.getProfileName())
                .typeEmail(randomUser.getEmail())
                .typePassword(randomUser.getPassword())
                .chooseGenderType(MALE)
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @Override
    public void testSignUpWithSpecialCharsEmail() {
        Users randomUser = generateRandomUser();
        randomUser.setEmail("Tune!№;%:.auto@gmail.com");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateInvalidEmailErrorText()
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @TestCaseId("30686")
    @Test(description = "Verify Sign up with no internet connection", groups = {SIGNUP_TEST})
    public void testSignUpWithOutInternetConnection() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.fullFillSignUpFieldsWithData(randomUser);
        deviceNativeActions.disableWiFi();
        signUpPage.tapOnCreateYourAccountButton();
        signUpPage.validateThatYouAreOfflineNowErrorDisplayed();
        deviceNativeActions.enableWifi();
    }

    @TestCaseId("259676")
    @Test(description = "Verify sign up with long invalid name", groups = {SIGNUP_TEST})
    public void testSignUpWithLongInvalidName() {
        Users randomUser = generateRandomUser();
        randomUser.setProfileName(randomUser.getProfileName() + DataUtil.getRandomString(15));
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .tapOnCreateYourAccountButton();
        signUpPage.validateErrorNameTooLongIsDisplayed();
    }

    @Override
    public void testSignUpWithInvalidPassword() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.fullFillSignUpFieldsWithData(randomUser);
        for (String password : INVALID_PASSWORDS) {
            signUpPage
                    .typePassword(password)
                    .tapOnCreateYourAccountButton();
            signUpPage
                    .validateInvalidPasswordErrorText()
                    .validateThatSignUpPageIsDisplayed();
        }
    }

    @TestCaseId("30685")
    @Test(description = "Verify Hint is hidden when user enters text", groups = {SIGNUP_TEST})
    public void testVerifyDisplayOfHintForUsers() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser, true)
                .validatePlaceholderTextIsNotDisplayedForFields()
                .clearFieldsAndValidatePlaceholderTextIsDisplayed();
    }

    @TestCaseId("730079")
    @Test(description = "Test that regwall is not displayed after signup", groups = {SIGNUP_TEST})
    public void testRegwallAfterSignUp() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.signUpUserByEmail(randomUser);
        userProfilePage.validateThatOnProfilePage();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("30663")
    @Test(description = "Check UI elements of SignUp page in landscape mode", groups = {SIGNUP_TEST})
    public void testSignUpPageInLandScapeMode() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        signUpPage.fullFillSignUpFieldsWithData(randomUser);
        signUpPage.validateUIElements(signUpPage.signUpPageElements());
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("224404")
    @Test(description = "Verify SignUp in landscape mode", groups = {SIGNUP_TEST})
    public void testSignUpInLandScapeMode() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        signUpPage
                .signUpUserByEmail(randomUser)
                .validateUserProfileDetails(randomUser);
    }
}

