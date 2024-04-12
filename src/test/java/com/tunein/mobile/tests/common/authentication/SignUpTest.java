package com.tunein.mobile.tests.common.authentication;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ApplicationUtil;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.ADD_YOUR_FAVORITES;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_STATION;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;

public abstract class SignUpTest extends BaseTest {

    @TestCaseId("566865")
    @Test(description = "Verify Registration Wall UI", groups = {SMOKE_TEST, SIGNIN_TEST, SIGNUP_TEST})
    public void testRegWallUIForm() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage
                .validateUIElements(regWallPage.regWallPageElements())
                .validateTextOfUIElements(regWallPage.regWallPageElements());
    }

    @TestCaseIds({@TestCaseId("729719"), @TestCaseId("737197")})
    @Test(description = "Verify sign up with email flow ", groups = {SMOKE_TEST, SIGNUP_TEST})
    public void testSignUpWithEmail() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .signUpUserByEmail(randomUser)
                .validateUserProfileDetails(randomUser);
    }

    @TestCaseIds({@TestCaseId("729702"), @TestCaseId("30661")})
    @Test(description = "Verify sign up UI form", groups = {SMOKE_TEST, SIGNUP_TEST})
    public abstract void testSignUpUIForm();

    @TestCaseIds({@TestCaseId("729706"), @TestCaseId("30666")})
    @Test(description = "Verify sign up with empty email flow ", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithEmptyEmail();

    @TestCaseIds({@TestCaseId("729705"), @TestCaseId("30665")})
    @Test(description = "Verify sign up UI form", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithInvalidEmail();

    @TestCaseIds({@TestCaseId("729703"), @TestCaseId("30660")})
    @Test(description = "Verify redirection to Privacy Policy page from signup screen", groups = {SIGNUP_TEST})
    public void testPrivacyPolicyPageFromSignUpPage() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.tapOnPrivacyPolicyLink();
        privacyPolicyPage.validatePrivacyPolicyPageIsOpened();
    }

    @TestCaseIds({@TestCaseId("729699"), @TestCaseId("30662")})
    @Test(description = "Check redirection to Terms Of Service Page from SignUp page", groups = {SIGNUP_TEST})
    public void checkRedirectionToTermsOfServicePage() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage.tapOnTermsOfServiceLink();
        termsOfServicePage.validateTermsOfServicePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("729741"), @TestCaseId("729711")})
    @Test(description = "Verify sign up with Invalid BirthYear", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithInvalidBirthYear();

    @TestCaseIds({@TestCaseId("30688")})
    @Test(description = "Verify sign up with empty all fields flow", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithAllEmptyFields();

    @TestCaseIds({@TestCaseId("729709"), @TestCaseId("30678")})
    @Test(description = "Verify sign up with empty password flow", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithEmptyPassword();

    @TestCaseIds({@TestCaseId("729714"), @TestCaseId("30667")})
    @Test(description = "Verify sign up with existing email", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithExistingEmail();

    @TestCaseIds({@TestCaseId("729715"), @TestCaseId("30681")})
    @Test(description = "Verify sign up with empty birth year", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithEmptyBirthYear();

    @TestCaseId("30668")
    @Test(description = "Verify sign up with special characters in email", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithSpecialCharsEmail();

    @TestCaseIds({@TestCaseId("729708"), @TestCaseId("30674"), @TestCaseId("30675"), @TestCaseId("30676"), @TestCaseId("30677")})
    @Test(description = "Verify sign up with invalid password flow", groups = {SIGNUP_TEST})
    public abstract void testSignUpWithInvalidPassword();

    @TestCaseIds({@TestCaseId("23902"), @TestCaseId("37863"), @TestCaseId("24601"), @TestCaseId("749122")})
    @Test(description = "Verify logout after sign up", groups = {SIGNUP_TEST, SIGNOUT_TEST, ACCEPTANCE_TEST})
    public void testLogoutAfterSignUp() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapSignUpWithEmailButton();
        signUpPage
                .signUpUserByEmail(randomUser)
                .validateUserProfileDetails(randomUser)
                .closeProfilePage();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoriteTextIsDisplayed(ADD_YOUR_FAVORITES);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
               .tapOnSignOutButton()
               .validateThatUserSignedOut(randomUser.getUsername());
    }

    @TestCaseId("730288")
    @Test(description = "Drop Request on first launch", groups = {SIGNUP_TEST, FAVORITES_TEST})
    public void userDidNotSignOutBeforeUninstall() {
        Users randomUser = generateRandomUser();
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.signUpUserByEmail(randomUser);
        userProfilePage.closeProfilePage();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .tapOnFavoriteIcon(true, FAVORITE_STATION)
                .minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(),
                favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS));
        ApplicationUtil.reinstallApp();
        signInPage.signInFlowForUser(randomUser);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(),
                favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS));
    }

    @Issue("IOS-17816")
    @TestCaseId("749130")
    @Test(description = "User profile information by changing password", groups = {ACCEPTANCE_TEST})
    public void testUserProfileInformation() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        navigationAction.navigateTo(PROFILE);
        String newPassword = "Tune@1234";
        String newProfileName = "TuneAutoTest";
        userProfilePage
                .tapEditProfileButton()
                .validateUIElements(editUserProfilePage.editProfilePageElements());
        editUserProfilePage
                .changePassword(randomUser.getPassword(), newPassword)
                .changeName(newProfileName);
        userProfilePage
                .tapOnSignOutButton()
                .tapOnLoginSignupButton()
                .tapOnSignInButton();
        signInPage.signInWithCredentials(randomUser.getEmail(), newPassword);
        userProfilePage
                .validateThatUserNameIsEqualTo(newProfileName)
                .closeProfilePage();
    }
}
