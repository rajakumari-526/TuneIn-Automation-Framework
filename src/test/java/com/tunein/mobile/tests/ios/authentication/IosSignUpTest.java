package com.tunein.mobile.tests.ios.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.authentication.SignUpTest;
import com.tunein.mobile.utils.GestureActionUtil;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.SIGNOUT_TEST;
import static com.tunein.mobile.testdata.TestGroupName.SIGNUP_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_FOR_LOGIN_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.MALE;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.DataUtil.getBirthYear;
import static com.tunein.mobile.utils.DataUtil.getRandomString;

public class IosSignUpTest extends SignUpTest {

    @Override
    public void testSignUpUIForm() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .validateUIElements(signUpPage.signUpPageElements())
                .validateTextOfUIElements(signUpPage.signUpPageElements());
        signUpPage
                .typeEmail(randomUser.getEmail())
                .typePassword(randomUser.getPassword())
                .pressNextButton()
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
                .validateNextButtonIsDisabled()
                .validateCreateYourAccountButtonIsNotDisplayed();
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
                .validateNextButtonIsDisabled();
    }

    @TestCaseId("729720")
    @Test(description = "Sign up from Premium screen ", groups = {SIGNUP_TEST})
    public void testSignUpFromPremiumScreen() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnTuneInPremiumButton();
        tuneInPremiumPage
                .validateThatTuneInPremiumPageIsOpened()
                .tapSignIntoTuneInAccount();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .tapOnCreateYourAccountButton();
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        tuneInPremiumPage.validateThatTuneInPremiumPageIsOpened();
    }

    @Override
    public void testSignUpWithAllEmptyFields() {
        Users randomUser = generateRandomUser();
        randomUser.setEmail("");
        randomUser.setPassword("");
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .validateNextButtonIsDisabled()
                .validateThatCreateYourAccountButtonIsDisabled();
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
        signUpPage.validateErrorSignUpExistingEmailIsDisplayed();
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
                .validateNextButtonIsDisabled()
                .validatePasswordRequirementsImageAreGrey();
    }

    @Override
    public void testSignUpWithEmptyBirthYear() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .typeEmail(randomUser.getEmail())
                .typePassword(randomUser.getPassword())
                .pressNextButton()
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
                .typeEmail(randomUser.getEmail())
                .typePassword(randomUser.getPassword())
                .validateInvalidEmailErrorText()
                .validateNextButtonIsDisabled();
    }

    @Override
    public void testSignUpWithInvalidPassword() {
        Users randomUser = generateRandomUser();
        randomUser.setPassword(getRandomString(32) + 1);
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .typeEmail(randomUser.getEmail())
                .typePassword("111aaa")
                .validatePasswordIsNotCorrectAccordingToRules()
                .validateNextButtonIsDisabled();
        signUpPage
                .typePassword(getRandomString(8))
                .validatePasswordIsNotCorrectAccordingToRules()
                .validateNextButtonIsDisabled();
        signUpPage
                .typePassword(getRandomString(4))
                .validatePasswordIsNotCorrectAccordingToRules()
                .validateNextButtonIsDisabled();
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .tapOnCreateYourAccountButton();
        signUpPage.validateErrorNameTooLongIsDisplayed();
    }

    @Override
    public void testSignUpWithInvalidBirthYear() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.fullFillSignUpFieldsWithData(randomUser);
        int[] birthYears = {12, 116};
        for (int birthYear : birthYears) {
            signUpPage
                    .typeBirthYear(getBirthYear(birthYear))
                    .tapOnCreateYourAccountButton();
            signUpPage.validateThatRegistrationErrorIsDisplayed();
        }
        signUpPage.typeBirthYear(getBirthYear(-1));
        signUpPage
                .validateThatInvalidBirthYearErrorIsDisplayed()
                .validateThatCreateYourAccountButtonIsDisabled();
    }

    @TestCaseId("729742")
    @Test(description = "Verify Swipe down on RegWall", groups = {SIGNUP_TEST})
    public void testSwipeDownOnRegwall() {
        navigationAction.navigateTo(REGWALL);
        GestureActionUtil.swipeDownScreen();
        userProfilePage.validateThatOnProfilePage();
    }

    @TestCaseId("730149")
    @Test(description = "Finish screen with upsell", groups = {SIGNUP_TEST})
    public void testFinishScreenWithUpsell() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage
                .fullFillSignUpFieldsWithData(randomUser)
                .tapOnCreateYourAccountButton();
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .validateSignUpSuccessPremiumText();
    }

    @TestCaseId("739060")
    @Test(description = "Logout on account deletion", groups = {SIGNUP_TEST, SIGNOUT_TEST})
    public void testLogOutOnAccountDeletion() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.signUpUserByEmail(randomUser);
        userProfilePage.tapOnSettingsButton();
        settingsPage.tapOnDeleteYourAccount();
        deleteYourAccountPage.tapOnDeleteYourAccountButton();
        settingsPage.validateDeleteAccountButtonNotDisplayed();
    }
}
