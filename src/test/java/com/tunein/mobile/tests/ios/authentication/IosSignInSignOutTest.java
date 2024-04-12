package com.tunein.mobile.tests.ios.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.tests.common.authentication.SignInSignOutTest;
import com.tunein.mobile.utils.ApplicationUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_COMMERCIAL_FREE;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_PREMIUM_EPISODES;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_STATION;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.reinstallApp;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateDefaultLaunchArgumentList;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class IosSignInSignOutTest extends SignInSignOutTest {

    @TestCaseIds({@TestCaseId("729740")})
    @Test(description = "Verify sign in with remember me turned off", groups = {SIGNIN_TEST})
    public void testSignInWithRememberMeTurnOff() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .setRememberMeValue(false)
                .signInWithCredentials(USER_FOR_LOGIN_TEST.getEmail(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .tapOnSignOutButton()
                .tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage.validateEmailFieldIsEmpty();
    }

    @TestCaseIds({@TestCaseId("729738")})
    @Test(description = "Validate sign in with remember me turned on flow", groups = {SIGNIN_TEST})
    public void testSignInWithRememberMeTurnOn() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage
                .setRememberMeValue(true)
                .signInWithCredentials(USER_FOR_LOGIN_TEST.getEmail(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .tapOnSignOutButton()
                .tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage.validateEmailFieldIsFilled(USER_FOR_LOGIN_TEST.getEmail());
    }

    @Override
    public void testRegWallIsOpenedAfterAddingFavorite() {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        regWallPage
                .waitUntilPageReady()
                .validateCustomHeaderDisplayedOnRegwallPage("Save Your Favorites!");
    }

    @Override
    public void testRecoverPasswordWithInvalidEmail() {
        navigationAction.navigateTo(FORGOT_PASSWORD_FORM);
        forgotPasswordPage
                .typeEmail("wrong-email")
                .validateResetEmailErrorIsDisplayed()
                .validateResetPasswordButtonIsDisabled();
    }

    @Override
    public void testSignOutDuringAudioPremiumPlayback() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSignOutButtonLightVersion();
        regWallPage.closeRegWallPageIfDisplayed(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
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
        regWallPage.closeRegWallPageIfDisplayed(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        userProfilePage.closeProfilePage();
        miniPlayerPage.validateMiniPlayerIsNotDisplayed();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @TestCaseId("730150")
    @Test(description = "Finish screen without upsell", groups = {SIGNIN_TEST})
    public void testFinishScreenWithoutUpsell() {
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage.signInWithCredentialsLightVersion(USER_PREMIUM.getEmail(), USER_PREMIUM.getPassword());
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .validatePremiumTextDoesNotExist();
    }

    @Ignore("Functionality is outdated")
    @TestCaseIds({@TestCaseId("23839")})
    @Test(description = "Verify Registration wall for second and further launches", groups = {SIGNUP_TEST})
    public void testRegWallForSecondAndFurtherLaunches() {
        updateLaunchArgumentFor(SHOW_REGWALL_ONLAUNCH, "true");
        navigationAction.navigateTo(HOME);
        regWallPage
                .waitUntilPageReady()
                .tapOnSignInButton();
        signInPage.signInWithCredentialsLightVersion(USER_FOR_LOGIN_TEST.getEmail(), USER_FOR_LOGIN_TEST.getPassword());
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnSignOutButton()
                .validateThatUserSignedOut(USER_FOR_LOGIN_TEST.getUsername());
        ApplicationUtil.restartApp();
        upsellPage.closeUpsell();
        navigationAction.closePremiumTabTooltipIfPresent();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.validateThatUserSignedOut(USER_FOR_LOGIN_TEST.getUsername());
    }

    @TestCaseId("23905")
    @Test(description = "Reg wall appearance after Log out", groups = {SIGNOUT_TEST})
    public void testRegWallAppearanceAfterLogOut() {
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage.signInWithCredentials(USER_FOR_LOGIN_TEST.getEmail(), USER_FOR_LOGIN_TEST.getPassword());
        userProfilePage.tapOnSignOutButtonLightVersion();
        regWallPage
                .validateRegwallPageIsDisplayed()
                .closeRegWallPage();
        userProfilePage.validateThatUserSignedOut(USER_FOR_LOGIN_TEST.getProfileName());
    }

    @Override
    public void testPremiumNoAds() {
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
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.validateStreamStartPlaying(ContentType.LIVE_STATION);
        nowPlayingPage.minimizeNowPlayingScreen();

        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS));

        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS));
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));
    }

    @Override
    public void testSignInSignOut() {
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnLoginSignupButton();
        regWallPage.tapOnSignInButton();
        signInPage.signInWithCredentials(USER_WITH_FAVORITES_FOR_LOGIN_TEST.getEmail(), USER_WITH_FAVORITES_FOR_LOGIN_TEST.getPassword());
        userProfilePage
                .validateUserProfileDetails(USER_WITH_FAVORITES_FOR_LOGIN_TEST)
                .closeProfilePage();
        int count = USER_WITH_FAVORITES_FOR_LOGIN_TEST.getNumberOfFavorites();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(count);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .validateThatSignOutButtonIsDisplayed()
                .tapOnSignOutButton()
                .validateThatUserSignedOut(USER_WITH_FAVORITES_FOR_LOGIN_TEST.getProfileName());
    }

}
