package com.tunein.mobile.tests.common.library;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_CUSTOM_URL;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_STATION;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class LibraryTest extends BaseTest {

    @TestCaseIds({@TestCaseId("33736"), @TestCaseId("576444")})
    @Test(description = "Check favorites count after user signed out", groups = {PLATFORM_TEST, FAVORITES_TEST})
    public abstract void testFavoritesCountAfterUserSignedOut();

    @TestCaseId("575352")
    @Test(description = "test see all button under favorites page", groups = {FAVORITES_TEST})
    public void testSeeAllButtonInFavoritesPage() {
        signInPage.signInFlowForUser(USER_WITH_MANY_FAVORITES);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_ARTISTS, 1);
        contentProfilePage.validateContentProfilePageIsOpened();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 3);
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseId("575377")
    @Test(description = "add custom URL button is present after adding favorites", groups = {FAVORITES_TEST})
    public void testAddCustomURLButtonIsPresentAfterAddingFavorites() {
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateCustomURLButtonIsDisplayed();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        regWallPage.closeRegWallPageIfDisplayed(Duration.ofSeconds(3));
        nowPlayingPage
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        libraryPage.validateCustomURLButtonIsDisplayed();
        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateCustomURLButtonIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("576296"), @TestCaseId("576387")})
    @Test(description = "Open podcast from favorites", groups = {FAVORITES_TEST})
    public void testOpenPodcastFromFavorites() {
        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(FAVORITES);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PODCASTS, 1);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("23183"), @TestCaseId("37903"), @TestCaseId("749203")})
    @Test(description = "Custom URL favorite", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public abstract void testCustomUrlFavorite();

    @TestCaseId("23169")
    @Test(description = "Merging Favorites - Sign Up", groups = {FAVORITES_TEST})
    public void testMergingFavoritesAfterSignUp() {
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateFavoritesCount(0)
                .openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_CUSTOM_URL);
        customUrlFavoriteDialog.tapSubmitButton();
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.navigateTo(HOME);
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage
                .validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS)
                .validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS))
                .validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));
    }

    @TestCaseIds({@TestCaseId("23167"), @TestCaseId("749114")})
    @Test(description = "Unfavorite content from NP and Favorites page for signout user", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public abstract void testUnFavoriteContentFromNPAndFavoritePage();

    @TestCaseIds({@TestCaseId("23153"), @TestCaseId("130368")})
    @Test(description = "Open custom url from favorites list", groups = {FAVORITES_TEST})
    public abstract void testCustomUrlFromFavoriteList();

    @TestCaseIds({@TestCaseId("23166"), @TestCaseId("749114")})
    @Test(description = "Test favorite from now playing screen and favorites page for signout user", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public void testFavoriteContentFromNPAndFavoritesPage() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITH_ACTIVE_SHOW);
        contentProfilePage.tapOnFavoriteButton();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        Contents[] favoriteContents = {STREAM_STATION_WITHOUT_ADS, STREAM_PODCAST_MARKETPLACE, STREAM_STATION_WITH_ACTIVE_SHOW};
        for (Contents content : favoriteContents) {
            favoritesPage.validateThatContentAppearedInFavorites(content.getStreamName(), favoritesPage.getCategoryTypeFromContents(content));
        }

    }

    @TestCaseIds({@TestCaseId("23168"), @TestCaseId("749114")})
    @Test(description = "Merging Favorites - Sign in", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public void testMergingFavoritesAfterSignIn() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(0);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage
                .validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS))
                .validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnSignOutButton()
                .closeProfilePage();
        restartApp();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_CUSTOM_URL);
        customUrlFavoriteDialog.tapSubmitButton();
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.minimizeNowPlayingScreen();
        signInPage.signInFlowForUser(randomUser);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage
                .validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS)
                .validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS))
                .validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));
    }

    @TestCaseId("729893")
    @Test(description = "Check recent order", groups = {FAVORITES_TEST})
    public void testRecentOrderInLibrary() {
        int numberOfRecentItems = 3;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfRecentItems, GENERATE_RECENT_LIST)
                .minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(LIBRARY);
        recentsPage
                .tapOnCategoryHeader(RECENTS, DOWN)
                .validateContentListsOrder(GENERATE_RECENT_LIST.subList(0, numberOfRecentItems));
    }
        
    @TestCaseIds({@TestCaseId("29930"), @TestCaseId("23149"), @TestCaseId("749236")})
    @Test(description = "Open station from favorites", groups = {FAVORITES_TEST})
    public abstract void testOpenStationFromFavorites();

    @TestCaseIds({@TestCaseId("29929"), @TestCaseId("23148")})
    @Test(description = "Open show from favorites", groups = {FAVORITES_TEST})
    public void testOpenShowFromFavorites() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 2, false);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("738559"), @TestCaseId("738558")})
    @Test(description = "redirect using prompt", groups = {FAVORITES_TEST})
    public abstract void testRedirectUsingPrompt();

    @TestCaseId("749236")
    @Test(description = "Verify Library page UI", groups = {ACCEPTANCE_TEST, LIBRARY_TEST})
    public void testCheckLibraryPageUI() {
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateUIElements(libraryPage.libraryPageElements());
        libraryPage.tapOnDownloadsButton();
        downloadsPage.validateDownloadsPageWithNoDownloads();
    }

    @TestCaseId("749236")
    @Test(description = "Play downloaded item", groups = {DOWNLOADS_TEST})
    public abstract void testPlayDownloadedItem();

    @Issue("DROID-11543")
    @TestCaseId("749459")
    @Test(description = "Check favourite Icon after sign out", groups = {FAVORITES_TEST})
    public void testCheckFavouriteIconAfterSignOut() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        customWait(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        nowPlayingPage
                .validateContentFollowState(true)
                .minimizeNowPlayingScreen();
        userProfilePage.signOutUserFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateContentFollowState(false);
    }

    @TestCaseId("749232")
    @Test(description = "Auto-Downloads", groups = {DOWNLOADS_TEST})
    public abstract void tesAutoDownloads();
}
