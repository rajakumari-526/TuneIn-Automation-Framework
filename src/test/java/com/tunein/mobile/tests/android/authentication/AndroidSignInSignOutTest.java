package com.tunein.mobile.tests.android.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.authentication.SignInSignOutTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_COMMERCIAL_FREE;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_PREMIUM_EPISODES;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_STATION;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.reinstallApp;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BANNER_ADS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.REGWALL_FAVORITES;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateDefaultLaunchArgumentList;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;
import static org.openqa.selenium.ScreenOrientation.PORTRAIT;

public class AndroidSignInSignOutTest extends SignInSignOutTest {

    @TestCaseIds({@TestCaseId("263489")})
    @Test(description = "Verify sign in with User name", groups = {SIGNIN_TEST})
    public void testSignInWithUserName() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage.signInWithCredentials(USER_FOR_LOGIN_TEST.getUsername(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .validateUserProfileDetails(USER_FOR_LOGIN_TEST)
                .tapOnSignOutButton();
    }

    @Override
    public void testRegWallIsOpenedAfterAddingFavorite() {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        regWallPage.validateRegwallPageIsDisplayed();
    }

    @Override
    public void testRecoverPasswordWithInvalidEmail() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        forgotPasswordPage
                .typeEmail("wrong-email")
                .tapOnResetPasswordButton();
        forgotPasswordPage.validateResetEmailErrorIsDisplayed();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("30700")
    @Test(description = "Verify SignIn page in landscape mode", groups = {SIGNIN_TEST})
    public void testSignInPageInLandScapeMode() {
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage
                .typeEmail(USER_FOR_LOGIN_TEST.getUsername())
                .typePassword(USER_FOR_LOGIN_TEST.getPassword());
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        signInPage.validateUIElements(signInPage.signInPageElements());
        deviceNativeActions.setOrientationMode(PORTRAIT);
        signInPage.validateUIElements(signInPage.signInPageElements());
    }

    @TestCaseIds({@TestCaseId("30704")})
    @Test(description = "Verify Sign in with no internet connection", groups = {SIGNIN_TEST})
    public void testSignInWithOutInternetConnection() {
        navigationAction.navigateTo(SIGNIN_FORM);
        deviceNativeActions.disableWiFi();
        signInPage.signInWithCredentialsLightVersion(USER_FOR_LOGIN_TEST.getUsername(), USER_FOR_LOGIN_TEST.getPassword());
        signInPage.validateThatYouAreOfflineNowErrorDisplayed();
    }

    @TestCaseId("30708")
    @Test(description = "Hint is hidden when user taps to enter text in SignIn screen", groups = {SIGNIN_TEST})
    public void testVerifyDisplayOfHintForUsersInSignInScreen() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage
                .typeEmail(randomUser.getEmail())
                .typePassword(randomUser.getPassword())
                .validatePlaceholderTextIsNotDisplayedForSignInFields()
                .clearAllFieldsInSignInPage()
                .validatePlaceholderTextIsDisplayedForSignInFields();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseIds(@TestCaseId("3122"))
    @Test(description = "Check UI of forgot password page in landscape mode", groups = {SIGNIN_TEST, FORGOT_PASSWORD_TEST})
    public void testForgotPasswordUIElementsInLandScapeMode() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        signInPage.validateUIElements(forgotPasswordPage.forgotPasswordPageElements());
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("259652")
    @Test(description = "Signing out in landscape mode", groups = {SIGNIN_TEST, SIGNOUT_TEST})
    public void testSigningOutInLandscapeMode() {
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage.signInWithCredentials(USER_FOR_LOGIN_TEST.getUsername(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .tapOnSignOutButton()
                .validateThatUserSignedOut(USER_FOR_LOGIN_TEST.getProfileName());
    }

    @Override
    public void testPremiumNoAds() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        navigationAction.navigateTo(HOME);
        signInPage.signInFlowForUser(USER_PREMIUM);
        List<NavigationAction.NavigationActionItems> pageList = Arrays.asList(HOME, LIBRARY, SEARCH, PREMIUM, PROFILE);
        pageList.forEach(barItem -> {
            navigationAction.navigateTo(barItem);
            miniPlayerPage.validateAdBannerDisplayed(false);
        });

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        contentProfilePage.validateFreeTrialButtonNotDisplayed();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_COMMERCIAL_FREE, 1);
        nowPlayingPage.validateThatPreRollIsAbsent();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 1);
        nowPlayingPage.validateThatPreRollIsAbsent();
    }

    @Override
    public void testSignInAfterReinstall() {
        reinstallApp(updateDefaultLaunchArgumentList(BANNER_ADS, "true"));
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC, true);
        nowPlayingPage.validateStreamStartPlaying(LIVE_STATION);
        nowPlayingPage.minimizeNowPlayingScreen();

        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_UNICC.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_UNICC));

        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_UNICC.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_UNICC));
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));

        miniPlayerPage.validateAdBannerDisplayed(true);
    }

    @Override
    public void testSignInSignOut() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES_FOR_LOGIN_TEST);
        int count = USER_WITH_FAVORITES_FOR_LOGIN_TEST.getNumberOfFavorites();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(count);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .validateThatSignOutButtonIsDisplayed()
                .tapOnSignOutAndStaySignedIn()
                .tapOnSignOutButton()
                .validateThatUserSignedOut(USER_WITH_FAVORITES_FOR_LOGIN_TEST.getProfileName())
                .closeProfilePage();
    }

    @Override
    public void testSignOutDuringAudioPremiumPlayback() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSignOutButtonLightVersion();
        userProfilePage.closeProfilePage();
        miniPlayerPage.validateMiniPlayerIsNotDisplayed();
        deepLinksUtil
                .openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK)
                .tapProfilePlayButton();
        upsellPage.validateIsOnUpsellPage(true);
        upsellPage.closeUpsell();
        navigationAction.tapBackButtonIfDisplayed();
        signInPage.signInFlowForUser(USER_GENERAL);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSignOutButtonLightVersion();
        userProfilePage.closeProfilePage();
        miniPlayerPage.validateMiniPlayerIsNotDisplayed();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

}
