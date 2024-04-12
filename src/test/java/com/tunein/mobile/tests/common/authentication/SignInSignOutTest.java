package com.tunein.mobile.tests.common.authentication;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.SHOW_REGWALL_ONLAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public abstract class SignInSignOutTest extends BaseTest {

    @TestCaseIds({@TestCaseId("24600"), @TestCaseId("24606"), @TestCaseId("37868"), @TestCaseId("37866"), @TestCaseId("749121"), @TestCaseId("749126")})
    @Test(description = "Verify signin/signout flow", groups = {SMOKE_TEST, SIGNIN_TEST, SIGNOUT_TEST, ACCEPTANCE_TEST})
    public abstract void testSignInSignOut();

    @TestCaseIds({@TestCaseId("729683"), @TestCaseId("738464")})
    @Test(description = "Verify sign in flow during audio playback", groups = {SMOKE_TEST, SIGNIN_TEST})
    public void testSignInDuringAudioPlayback() {
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(SEARCH);
        searchPage
                .searchStreamAndPlayFirstResult(STREAM_STATION_WITHOUT_ADS)
                .minimizeNowPlayingScreen();
        miniPlayerPage.validateMiniPlayerStopButtonIsDisplayed();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage.signInWithCredentials(USER_FOR_LOGIN_TEST.getEmail(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .validateUserProfileDetails(USER_FOR_LOGIN_TEST)
                .closeProfilePage();
        miniPlayerPage.validateMiniPlayerStopButtonIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("729682"), @TestCaseId("30699")})
    @Test(description = "Verify sign in UI", groups = {SMOKE_TEST, SIGNIN_TEST})
    public void testSignInUIForm() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .validateUIElements(signInPage.signInPageElements())
                .validateTextOfUIElements(signInPage.signInPageElements());
    }

    @TestCaseIds({@TestCaseId("729688"), @TestCaseId("30706")})
    @Test(description = "Verify sign in with empty fields flow", groups = {SIGNIN_TEST})
    public void testSignInWithEmptyFields() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .validateThatSignInAccountButtonIsDisabled()
                .typePassword(USER_FOR_LOGIN_TEST.getPassword())
                .validateThatSignInAccountButtonIsDisabled()
                .typeEmail(USER_FOR_LOGIN_TEST.getEmail())
                .typePassword("")
                .validateThatSignInAccountButtonIsDisabled();
    }

    @TestCaseIds({@TestCaseId("729685"), @TestCaseId("30702")})
    @Test(description = "Verify sign in with non existing account", groups = {SIGNIN_TEST})
    public void testSignInWithNonExistingAccount() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .signInWithCredentialsLightVersion("blablabla@gmail.com", USER_FOR_LOGIN_TEST.getPassword())
                .validateThatSignInFailedMessageDisplayed()
                .closeLoginErrorPopUp()
                .signInWithCredentialsLightVersion(USER_FOR_LOGIN_TEST.getEmail(), "!#$%%")
                .validateThatSignInFailedMessageDisplayed();
    }

    @TestCaseId("729686")
    @Test(description = "Sign in with valid email and invalid password", groups = {SIGNIN_TEST})
    public void testSignInWithInvalidPassword() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .signInWithCredentialsLightVersion(USER_FOR_LOGIN_TEST.getEmail(), "!#$%%")
                .validateThatSignInFailedMessageDisplayed();
    }

    @TestCaseId("736245")
    @Test(description = "Validate regwall is opened after adding favorite flow", groups = {SIGNIN_TEST, FAVORITES_TEST})
    public abstract void testRegWallIsOpenedAfterAddingFavorite();

    @TestCaseIds({@TestCaseId("729732"), @TestCaseId("30693")})
    @Test(description = "Verify Recover password with invalid email", groups = {SIGNIN_TEST, FORGOT_PASSWORD_TEST})
    public abstract void testRecoverPasswordWithInvalidEmail();

    @TestCaseIds({@TestCaseId("729733"), @TestCaseId("30694")})
    @Test(description = "Verify Recover password with empty email", groups = {SIGNIN_TEST, FORGOT_PASSWORD_TEST})
    public void testRecoverPasswordWithEmptyEmail() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        forgotPasswordPage.validateResetPasswordButtonIsDisabled();
    }

    @TestCaseIds({@TestCaseId("729730"), @TestCaseId("30689")})
    @Test(description = "Check UI for forgot password page", groups = {SIGNIN_TEST, FORGOT_PASSWORD_TEST})
    public void testForgotPasswordUIElements() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        signInPage.validateUIElements(forgotPasswordPage.forgotPasswordPageElements());
    }

    @TestCaseId("729686")
    @Test(description = "Sign in with invalid email and valid password", groups = {SIGNIN_TEST})
    public void testSignInWithInvalidEmail() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .signInWithCredentialsLightVersion("blablabla@gmail.com", USER_FOR_LOGIN_TEST.getPassword())
                .validateThatSignInFailedMessageDisplayed();
    }

    @TestCaseIds({@TestCaseId("729340")})
    @Test(description = "signing out after connecting via premium user", groups = {SIGNIN_TEST})
    public void testSignOutAfterConnectingViaPremiumUser() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .validateThatSignOutButtonIsDisplayed()
                .tapOnSignOutButton()
                .tapOnTuneInPremiumButton();
        tuneInPremiumPage.validateThatTuneInPremiumPageIsOpened();
    }

    @TestCaseId("30696")
    @Test(description = "verify there are no previous favorites after reinstall", groups = {SIGNIN_TEST})
    public void testNoPreviousFavoritesAfterReinstall() {
        signInPage.signInFlowForUser(USER_WITHOUT_FAVORITES);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(0);
    }

    @TestCaseIds({@TestCaseId("30695")})
    @Test(description = "Check placeholder text is displayed for email in forgot password page", groups = {SIGNIN_TEST})
    public void testCheckPlaceholderForEmailInForgotPasswordPage() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        forgotPasswordPage.validatePlaceholderForEmailInForgotPasswordPageIsDisplayed();
    }

    @Ignore("Functionality is outdated")
    @TestCaseId("729762")
    @Test(description = "Verify the Registration wall on launch", groups = {SIGNIN_TEST})
    public void testRegWallOnLaunch() {
        updateLaunchArgumentFor(SHOW_REGWALL_ONLAUNCH, "true");
        navigationAction.navigateTo(HOME);
        regWallPage.validateRegwallPageIsDisplayed();
    }

    @Issue("DROID-16201")
    @TestCaseIds({@TestCaseId("23907"), @TestCaseId("749625")})
    @Test(description = "SignOut during different stream states", groups = {SIGNUP_TEST, NOW_PLAYING_TEST})
    public abstract void testSignOutDuringAudioPremiumPlayback();

    @TestCaseIds({@TestCaseId("576448"), @TestCaseId("33742"), @TestCaseId("749149"), @TestCaseId("749618")})
    @Test(description = "Verify premium account does not have ads and can access locked content", groups = {PLATFORM_TEST, SUBSCRIPTION_TEST, ADS_TEST, ACCEPTANCE_TEST})
    public abstract void testPremiumNoAds();

    @TestCaseId("37912")
    @Test(description = "Test sign in after reinstall", groups = {SIGNIN_TEST})
    public abstract void testSignInAfterReinstall();

}
