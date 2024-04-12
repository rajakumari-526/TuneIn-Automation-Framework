package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SETTINGS;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;

public abstract class MiniPlayerTest extends BaseTest {

    @TestCaseIds({@TestCaseId("576052"), @TestCaseId("24624"), @TestCaseId("749664"), @TestCaseId("749722")})
    @Test(description = "Check UI Elements of Mini Player", groups = {NOW_PLAYING_TEST, MINI_PLAYER_TEST, ACCEPTANCE_TEST})
    public void testMiniPlayingUIElements() {
      deepLinksUtil
              .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
              .minimizeNowPlayingScreen();
      miniPlayerPage
              .validateUIElements(miniPlayerPage.miniPlayerPageElements(true));
      miniPlayerPage
              .tapStopButton()
              .validateUIElements(miniPlayerPage.miniPlayerPageElements(true));
    }

    @TestCaseIds({@TestCaseId("23403"), @TestCaseId("576142"), @TestCaseId("37872"), @TestCaseId("749195")})
    @Test(description = "Mini Player - Switching tabs", groups = {NOW_PLAYING_TEST, MINI_PLAYER_TEST, ACCEPTANCE_TEST})
    public void testMiniPlayingWhenChangingScreens() {
        signInPage.signInFlowForUser(USER_GENERAL);
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
                .minimizeNowPlayingScreen();
        navigationAction
                .mainNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    miniPlayerPage.validateUIElements(miniPlayerPage.miniPlayerPageElements(true));
                });
    }

    @TestCaseIds({@TestCaseId("576197"), @TestCaseId("24624"), @TestCaseId("749659")})
    @Test(description = "Check Mini Player when playing podcast episode and station",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProvider",
            groups = {NOW_PLAYING_TEST, MINI_PLAYER_TEST, SMOKE_TEST, ACCEPTANCE_TEST})
    public void testMiniPlayerStationAndPodcast(Contents content) {
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                .minimizeNowPlayingScreen();
        miniPlayerPage
                .tapStopButton()
                .validateLiveIconIsNotDisplayed()
                .validateMiniPlayerPlayButtonIsDisplayed();
        miniPlayerPage
                .tapPlayButton();
        if (getContentTypeValue(content.getStreamType()) == LIVE_STATION) {
            miniPlayerPage.validateLiveIconIsDisplayed();
        } else {
            miniPlayerPage.validateLiveIconIsNotDisplayed();
        }
        miniPlayerPage.validateMiniPlayerStopButtonIsDisplayed();
    }

    @TestCaseId("749584")
    @Test(description = "Test check mini-player while podcast is playing", groups = {NOW_PLAYING_TEST, MINI_PLAYER_TEST})
    public abstract void testCheckMiniPlayerWhilePodcastIsPlaying();

    @TestCaseId("749583")
    @Test(description = "Check Mini Player after relaunch the app",
            groups = {NOW_PLAYING_TEST, MINI_PLAYER_TEST})
    public void testMiniPlayerAfterRelaunch() {
        navigationAction.navigateTo(SETTINGS);
        settingsPage
                .tapOnAutoPlaySettings()
                .tapBackButton();
        userProfilePage.closeProfilePage();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateThatNowPlayingIsOpened();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(SETTINGS);
        settingsPage
                .tapOnAutoPlaySettings()
                .tapBackButton();
        userProfilePage.closeProfilePage();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateThatNowPlayingIsOpened();
        runAppInBackground(Duration.ofSeconds(3));
        upsellPage.closeUpsell();
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .minimizeNowPlayingScreen()
                .validateMiniPlayerIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("37872"), @TestCaseId("749195")})
    @Test(
            description = "Mini Player - Extend MiniPlayer from different screens",
            groups = {MINI_PLAYER_TEST, ACCEPTANCE_TEST}
    )
    public void testExtendedMiniPlayerFromDifferentScreens() {
        signInPage.signInFlowForUser(USER_GENERAL);
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
                .minimizeNowPlayingScreen();
        navigationAction
                .differentNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    miniPlayerPage.validateUIElements(miniPlayerPage.miniPlayerPageElements(true));
                });
    }

}
