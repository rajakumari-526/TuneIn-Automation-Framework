package com.tunein.mobile.tests.common.explorer;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.RADIO;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITH_PREROLL;
import static com.tunein.mobile.utils.ApplicationUtil.closeApp;
import static com.tunein.mobile.utils.ApplicationUtil.terminateApp;
import static com.tunein.mobile.utils.DeepLinksUtil.DEEPLINK_EXPLORER;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class ExplorerPageTest extends BaseTest {

    // TODO: Android testing blocked by https://tunein.atlassian.net/browse/DROID-16040 and https://tunein.atlassian.net/browse/DROID-16073
    @TestCaseId("749576")
    @Test(description = "Verify favorite and share button in NowPlaying", groups = {NOW_PLAYING_TEST, EXPLORER_TEST, IGNORE_TEST})
    public void testExplorerScrollable() {
        settingsPage.enableScrollableNowPlayingFlow();
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerPage.clickExplorerCloseButton();
        miniPlayerPage.waitUntilPageReady();
        miniPlayerPage
                .extendMiniPlayer()
                .validateStreamStartPlaying()
                .validateNowPlayingCardsElements();

        // Uses openURL() to bypass wait for preroll to finish
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        if (nowPlayingPage.isPrerollDisplayed()) {
            nowPlayingPage.stopStreamPlaying();
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.navigateTo(EXPLORER);
            explorerPage.clickExplorerCloseButton();
            miniPlayerPage.tapOnMiniPlayer();
            nowPlayingPage
                    .waitUntilPageReadyLiteVersion()
                    .tapOnPlayButton();
            if (nowPlayingPage.isPrerollDisplayed()) {
                scroll(DOWN, 2);
                nowPlayingPage
                        .validateSeekBarDisplayed()
                        .waitUntilPreRollAdDisappearIfDisplayed()
                        .validateNowPlayingCardsElements(STREAM_STATION_WITH_PREROLL);
            }
        } else {
            throw new SkipException("Skipping this test as preroll is absent");
        }
    }

    @TestCaseId("752299")
    @Test(description = "Verify UI Elements for Explorer page", groups = {EXPLORER_TEST})
    public void testVerifyUIElementsForExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .validateStationListIsNotEmpty()
                .validateThatAllExplorerBarTabsCanBeSelected();
        explorerPage.validateUIElements(explorerPage.explorePageElements());
    }

    @TestCaseId("752301")
    @Test(description = "Test explorer page is opened from browsies", groups = {EXPLORER_TEST})
    public void testExplorerPageIsOpenedFromBrowsies() {
        homePage.tapOnRequiredBrowsiesBarTab(RADIO);
        explorerPage.validateThatOnExplorerPage();
    }

    @TestCaseId("752304")
    @Test(description = "Close Explorer page", groups = {EXPLORER_TEST})
    public void testCloseExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .validateThatOnExplorerPage()
                .clickExplorerCloseButton();
        homePage.isOnHomePage();
    }

    @TestCaseId("752302")
    @Test(description = "Check Nowplaying page is opened from explorer page", groups = {EXPLORER_TEST})
    public void testNowplayingPageIsOpenedAfterClickingPause() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .validateThatOnExplorerPage()
                .clickStationByIndex(1);
        explorerMiniPlayerPage.validateStreamStartsPlaying();
        customWait(Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        explorerMiniPlayerPage
                .tapStopButton()
                .validateMiniPlayerPlayButtonIsDisplayed()
                .tapPlayButton()
                .validateStreamStartsPlaying()
                .clickExplorerMiniPlayer();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseId("752321")
    @Test(description = "Check that station is favourited from miniplayer of explorer page", groups = {EXPLORER_TEST})
    public void testFavouriteButtonOnMiniPlayerOfExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerMiniPlayerPage.clickExplorerMiniPlayerFavoriteButton();
        String contentTitle = explorerMiniPlayerPage.getStationName();
        explorerPage.clickExplorerCloseButton();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(contentTitle);
    }

    @TestCaseId("752316")
    @Test(description = "Check Miniplayer is not displayed in Explorer", groups = {EXPLORER_TEST})
    public void testItemIsNotPlayedInExplorer() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.validateThatOnExplorerPage();
        explorerMiniPlayerPage.validateExplorerMiniPlayerIsNotDisplayed();
    }

    @TestCaseId("752322")
    @Test(description = "Check that station is un favourited from miniplayer of explorer page", groups = {EXPLORER_TEST})
    public void testUnFavouriteButtonOnMiniPlayerOfExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        String contentTitle = explorerMiniPlayerPage.getStationName();
        explorerMiniPlayerPage.clickExplorerMiniPlayerFavoriteButton();
        explorerPage.clickExplorerCloseButton();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(contentTitle);
        navigationAction.navigateTo(EXPLORER);
        explorerMiniPlayerPage.clickExplorerMiniPlayerUnFavouriteButton();
        explorerPage.clickExplorerCloseButton();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentDisappearedInFavorites(contentTitle);
    }

    @TestCaseId("752317")
    @Test(description = "Check miniplayer UI of explorer page", groups = {EXPLORER_TEST})
    public void testMiniplayerUIOfExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerMiniPlayerPage
                .validateExplorerMiniPlayerIsDisplayed()
                .validateUIElements(explorerMiniPlayerPage.explorerMiniPlayerPageElements());
    }

    @TestCaseId("752320")
    @Test(description = "Test Open Now Playing", groups = {EXPLORER_TEST, NOW_PLAYING_TEST})
    public void testOpenNowPlaying() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerMiniPlayerPage
                .validateStreamStartsPlaying()
                .clickExplorerMiniPlayer();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseId("752318")
    @Test(description = "Check meta data of mini player of explorer page", groups = {EXPLORER_TEST})
    public void testMetaDataOfMiniPlayer() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerMiniPlayerPage.validateThatMetaDataIsNotEmpty();
        String title = explorerMiniPlayerPage.getStationName();
        String subTitle = explorerMiniPlayerPage.getStationSubTitle();
        explorerPage.clickStationByIndex(2);
        explorerMiniPlayerPage
                .validateThatMetaDataIsNotEmpty()
                .validateThatMetaDataHasChanged(title, subTitle);
    }

    @TestCaseId("752315")
    @Test(description = "Check that station is played from mapview ", groups = {EXPLORER_TEST})
    public void testPlayStationFromMapView() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .validateThatOnExplorerPage()
                .clickStationByIndex(1);
        explorerMiniPlayerPage.validateExplorerMiniPlayerIsDisplayed();
        String contentTitle = explorerMiniPlayerPage.getStationName();
        explorerPage.clickStationByIndex(2);
        explorerMiniPlayerPage
                .validateExplorerMiniPlayerIsDisplayed()
                .validateMiniPlayerPauseButtonIsDisplayed()
                .validateExplorerMiniplayerStationTitlesNotEqual(contentTitle);
    }

    @TestCaseId("752313")
    @Test(description = "Check that mapview is opened through deeplink", groups = {EXPLORER_TEST})
    public void testMapViewIsOpenedViaDeeplink() {
        navigationAction.navigateTo(HOME);
        terminateApp();
        deepLinksUtil.openURL(DEEPLINK_EXPLORER);
        explorerPage
                .waitUntilPageReady()
                .validateThatOnExplorerPage();
        closeApp();
        deepLinksUtil.openURL(DEEPLINK_EXPLORER);
        explorerPage.validateThatOnExplorerPage();
    }

    @TestCaseId("752334")
    @Test(description = "Verify Search Non existing item in Explorer page", groups = {EXPLORER_TEST})
    public void testSearchNonExistingItemInExplorerPage() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .typeSearch("abcxyz")
                .validateNoMatchingStationsFoundAlertDisplayed();
    }

    @TestCaseId("749134")
    @Test(description = "Validate Filters list Navigation", groups = {EXPLORER_TEST})
    public void testFilterListNavigation() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.validateAllFiltersCanBeScrollable();
    }

    @TestCaseId("752333")
    @Test(description = "Clear search field", groups = {EXPLORER_TEST})
    public void testClearSearchField() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage
                .validateThatOnExplorerPage()
                .typeSearch(STREAM_STATION_WITHOUT_ADS.getStreamName());
        int numberOfStationS = explorerPage.getCountOfStations();
        explorerPage
                .clearSearchField()
                .waitUntilPageReady();
        explorerPage.validateStationsCountIsGreaterThan(numberOfStationS);
    }

    @Issue("IOS-17801")
    @TestCaseId("752325")
    @Test(description = "Related station are playing from explorer", groups = {EXPLORER_TEST})
    public void testRelatedStationsPlayingFromExplore() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationByIndex(1);
        explorerMiniPlayerPage
                .validateExplorerMiniPlayerIsDisplayed()
                .validateStreamStartsPlaying()
                .validateRecommendedStationsAreDisplayed()
                .clickRecommendedStationByIndex(1)
                .validateExplorerMiniPlayerIsDisplayed()
                .validateStreamStartsPlaying();
    }

}
