package com.tunein.mobile.tests.common.upgrade;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Users;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.SET_ALARM;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public abstract class UpgradeTest extends BaseTest {

    @TestCaseIds({@TestCaseId("24536"), @TestCaseId("37904"), @TestCaseId("749220")})
    @Test(description = "Test upgrade carries over sign in state", groups = {UPGRADE_TEST, ACCEPTANCE_TEST})
    public void testUpgradeSignIn() {
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        applicationUtil.upgradeApp();
        homePage.validateThatRecentsNotEmpty();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_UNICC.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_UNICC));
        deviceNativeActions.clickBackButton();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS);
        userProfilePage.signOutUserFlow();
    }

    @TestCaseId("749337")
    @Test(
            description = "Test sign in after app is deleted",
            dataProviderClass = ContentProvider.class,
            dataProvider = "usersProviders",
            groups = {SIGNIN_TEST, UPGRADE_TEST})
    public void testSignInAfterAppIsDeleted(Users user) {
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(user);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_KQED.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_KQED));
        deviceNativeActions.clickBackButton();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS);
        uninstallApp();
        launchApp();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.validateThatUserSignedOut(user.getProfileName());
    }

    @TestCaseId("749347")
    @Test(description = "Test upgrade carries over sign in state", groups = {UPGRADE_TEST})
    public void testUpgradeAlarm() {
        applicationUtil.downgradeApp();
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog
                .turnOnAlarm()
                .tapOnSaveButton();

        applicationUtil.upgradeApp();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog.validateAlarmIsEnabled();
    }

    @TestCaseId("749334")
    @Test(description = "Test upgrade as anonymous user no favorites some recents", groups = {UPGRADE_TEST})
    public void testUpgradeAnonymousFavoritesRecents() {
        int numberOfStreamsToOpen = 3;

        applicationUtil.downgradeApp();
        navigationAction.navigateTo(HOME);
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoritesPromptIsDisplayed();

        applicationUtil.upgradeApp();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoritesPromptIsDisplayed();
    }

    @TestCaseId("749346")
    @Test(description = "Test upsell appearance after upgrade", groups = {UPSELL_TEST, UPGRADE_TEST})
    public void testUpsellAppearanceAfterUpgrade() {
        applicationUtil.downgradeApp(true);
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
        upsellPage
                .waitUntilPageReady()
                .validateIsOnUpsellPage(true);
        upsellPage.closeUpsell();
        signInPage.signInFlowForUser(USER_FOR_LOGIN_TEST);
        terminateApp();
        applicationUtil.upgradeApp();
        upsellPage.validateIsOnUpsellPage(false);
        navigationAction.navigateTo(HOME);
        homePage.validateThatRecentsNotEmpty();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateThatFavouritesNotEmpty();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.validateThatSignOutButtonIsDisplayed();
    }

    @TestCaseId("749348")
    @Test(description = "Test Downloaded content", groups = {UPGRADE_TEST})
    public abstract void testDownloadedContent();

    @TestCaseId("749351")
    @Test(description = "Test upgrade carries over sign in state while playing stream", groups = {UPGRADE_TEST})
    public void testUpgradeSignInPlayStream() {
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);

        applicationUtil.upgradeApp();
        homePage.validateThatRecentsNotEmpty();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage
                .validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS))
                .validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS);
    }

    @TestCaseId("749357")
    @Test(description = "Test upgrade user with no favorites", groups = {UPGRADE_TEST})
    public void testUpgradeNoFavorites() {
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(USER_WITHOUT_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoritesPromptIsDisplayed();
        terminateApp();

        applicationUtil.upgradeApp();
        homePage.validateThatRecentsNotEmpty();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoritesPromptIsDisplayed();
    }

    @TestCaseId("749352")
    @Test(description = "Test that upgrade after uninstall doesn't carry anything over", groups = {UPGRADE_TEST})
    public abstract void testUpgradeDeleteApp();

    @TestCaseId("749365")
    @Test(description = "Buffered podcast", groups = {NOW_PLAYING_TEST, UPGRADE_TEST})
    public void testVerifyBufferedPodcast() {
        applicationUtil.downgradeApp();
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_FREE_SHORT_PODCAST);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        int nowPlayingDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        int streamTimeBeforeEndOfEpisode = nowPlayingDuration / 2;

        nowPlayingPage
                .playStreamForRequiredTime(Duration.ofSeconds(streamTimeBeforeEndOfEpisode))
                .stopStreamPlaying();
        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        long startTime = System.currentTimeMillis();
        nowPlayingPage
                .tapOnPlayButton()
                .validateEpisodePlaysFromPreviousSpot(currentStreamTime);
        long timeBeforeUpgrade = System.currentTimeMillis();

        applicationUtil.upgradeApp();
        homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 1, false);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.validateEpisodePlaysFromPreviousSpot(currentStreamTime + (int) ((timeBeforeUpgrade - startTime) / 1000));
    }
}
