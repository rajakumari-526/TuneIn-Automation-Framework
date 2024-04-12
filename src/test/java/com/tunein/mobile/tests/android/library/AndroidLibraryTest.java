package com.tunein.mobile.tests.android.library;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.library.LibraryTest;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.isReleaseTestRun;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.STATION_TYPE_SHOW;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.*;
import static com.tunein.mobile.testdata.TestGroupName.ACCEPTANCE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.FAVORITES_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ElementHelper.longPressOnElement;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class AndroidLibraryTest extends LibraryTest {

    @Override
    public void testCustomUrlFavorite() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateStreamStartPlaying(ContentType.CUSTOM_URL)
                .validateNowPlayingTitleIsEqualTo(CUSTOM_URL)
                .minimizeNowPlayingScreen();
        libraryPage.tapOnFavoritesButton();
        favoritesPage
                .validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS)
                .openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(CUSTOM_URL);
    }

    @Override
    public void testOpenStationFromFavorites() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateStreamStartPlaying();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .tapOnFavoriteIcon(true, FAVORITE_STATION)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName(), favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS));
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITHOUT_ADS.getStreamName())
                .tapOnFavoriteIcon(false, FAVORITE_STATION)
                .validateStreamStartPlaying();
    }

    @Override
    public void testUnFavoriteContentFromNPAndFavoritePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PROGRAM_WITH_ACTIVE_SHOW);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PROGRAM_WITH_ACTIVE_SHOW);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .tapOnFavoriteIcon(false, FAVORITE_PODCAST)
                .minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.unfollowContentWithIndexUnderCategory(CATEGORY_TYPE_STATIONS, 1);
        Contents[] favoriteContents = {STREAM_STATION_WITHOUT_ADS, STREAM_PODCAST_MARKETPLACE, STREAM_PROGRAM_WITH_ACTIVE_SHOW};
        for (Contents content : favoriteContents) {
            favoritesPage.validateThatContentDisappearedInFavorites(content.getStreamName());
        }
    }

    @TestCaseIds({@TestCaseId("130410"), @TestCaseId("749236")})
    @Test(description = "Remove Item From Favorites By Long press", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public void testRemoveItemFromFavoritesByLongPress() {
        signUpPage.signUpFlowForUser(generateRandomUser());
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE, true)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(FAVORITES);
        SelenideElement element = contentsListPage.getContentUnderCategoryWithHeader(CATEGORY_TYPE_EPISODES, LIST, MEDIUM, 1);
        longPressOnElement(element);
        contentListItemDialog
                .waitUntilPageReady()
                .validateUnfavoriteDialogDisplayed()
                .clickOnUnfollowButton();
        favoritesPage
                .waitUntilPageReady()
                .validateThatContentDisappearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName());
    }

    @Override
    public void testCustomUrlFromFavoriteList() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(CUSTOM_URL);
    }

    @Override
    public void testFavoritesCountAfterUserSignedOut() {
        HashMap<NowPlayingFavoriteDialog.FavoriteContentType, Contents> contentProfiles = new HashMap<>();
        contentProfiles.put(FAVORITE_STATION, STREAM_STATION_WITHOUT_ADS);
        contentProfiles.put(FAVORITE_SHOW, STREAM_PROGRAM_WITH_ACTIVE_SHOW);
        contentProfiles.put(FAVORITE_EPISODE, STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        contentProfiles.put(FAVORITE_ARTIST, STREAM_ARTIST_DRAKE);
        contentProfiles.put(FAVORITE_PODCAST, STREAM_PODCAST_MARKETPLACE);
        for (Map.Entry<FavoriteContentType, Contents> stream : contentProfiles.entrySet()) {
            if (stream.getKey().equals(FAVORITE_PODCAST) || stream.getKey().equals(FAVORITE_ARTIST)) {
                deepLinksUtil.openContentProfileThroughDeeplink(stream.getValue());
                contentProfilePage.tapOnFavoriteButton();
            } else {
                deepLinksUtil.openTuneThroughDeeplink(stream.getValue());
                nowPlayingPage
                        .tapOnFavoriteIcon(true, stream.getKey())
                        .minimizeNowPlayingScreen();
            }
        }
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(5);
        signUpPage.signUpFlowForUser(generateRandomUser());
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateFavoritesCount(5)
                .tapOnFavoritesButton();
        for (Map.Entry<FavoriteContentType, Contents> content : contentProfiles.entrySet()) {
            favoritesPage.unfollowContentWithIndexUnderCategory(getCategoryType(content.getKey().toString()), 1);
        }
        navigationAction.tapBackButtonIfDisplayed();
        userProfilePage.signOutUserFlow();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(0);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PROGRAM_WITH_ACTIVE_SHOW);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_SHOW);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(2);
    }

    @TestCaseId("749154")
    @Test(description = "Verify Offline mode for auto-downloads", groups = {FAVORITES_TEST, ACCEPTANCE_TEST})
    public void testOfflineModeForAutoDownloads() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog
                .tapEpisodeDownloadButton()
                .closeEpisodeModalDialog();
        deviceNativeActions.disableWiFi();
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateDownloadsIsDisplayed()
                .tapOnDownloadsButton();
        downloadsPage.tapOnContentName(contentName);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @Override
    public void testRedirectUsingPrompt() {
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateRecentsPromptIsDisplayed()
                .tapOnStartListening();
        homePage.validateHomePageIsOpened();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage
                .validateAddYourFavoritesPromptIsDisplayed()
                .tapOnFindSomething();
        homePage.validateHomePageIsOpened();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage
                .validateDownloadsPromptIsDisplayed()
                .tapOnFindSomething();
        homePage.validateCategoryHeaderIsPresent(CATEGORY_TYPE_TOP_10_PODCASTS);
    }

    @TestCaseIds({@TestCaseId("29932"), @TestCaseId("736200"), @TestCaseId("130370")})
    @Test(description = "Favorite station, premium station and Show From NowPlaying screen", dataProviderClass = ContentProvider.class,
            dataProvider = "favStationShowFromNPScreen", groups = {FAVORITES_TEST})
    public void testFavoriteContentFromNowPlayingScreen(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
            deepLinksUtil.openTuneThroughDeeplink(content);
            nowPlayingPage.tapOnFavoriteIcon(false, FAVORITE_STATION);
        } else {
            signUpPage.signUpFlowForUser(generateRandomUser());
            deepLinksUtil.openTuneThroughDeeplink(content);
        }
        if (content.getContentProfileType().equals(STATION_TYPE_SHOW.getContentProfileType())) {
            nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_SHOW);
        } else {
            nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        }
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(content.getStreamName());
        if (content.isPremium()) {
            deepLinksUtil.openTuneThroughDeeplink(content);
            nowPlayingPage.tapOnFavoriteIcon(false, FAVORITE_STATION);
        }
    }

    @TestCaseIds({@TestCaseId("130371"), @TestCaseId("576388")})
    @Test(description = "Favorite Podcast & Episode From NowPlaying screen", groups = {FAVORITES_TEST})
    public void testFavoritePodcastAndEpisodeFromNowPlayingScreen() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        String nowPlayingEpisode = contentProfilePage.getContentItemDataByIndex(
                CATEGORY_TYPE_EPISODES, 1, ContentProfilePage.ContentDescriptionArea.EPISODE_NAME, false);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage
                .tapOnFavoriteIcon(true, FAVORITE_PODCAST)
                .tapOnFavoriteIcon(true, FAVORITE_EPISODE)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName(),
                favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE));
        favoritesPage.validateThatContentAppearedInFavorites(nowPlayingEpisode, CATEGORY_TYPE_EPISODES);
    }

    @Override
    public void testPlayDownloadedItem() {
        signInPage.signInFlowForUser(USER_WITH_PREMIUM_RECENTS);
        Contents[] podcasts = {STREAM_AUDIOBOOK_LIAR, STREAM_PODCAST_MARKETPLACE};
        for (Contents podcast : podcasts) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            contentProfilePage.tapEpisodeThreeDotsByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1);
            episodeModalDialog.tapEpisodeDownloadButton();
            deviceNativeActions.clickBackButton();
        }
        deviceNativeActions.clickBackButton();
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(3);

        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        nowPlayingPage
                .generateRecentsForNowPlayingPage(3, GENERATE_RECENT_LIST)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(3);
        nowPlayingPage
                .validateEpisodePlaysFromPreviousSpot(currentStreamTime)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @Override
    public void tesAutoDownloads() {
        if (isReleaseTestRun()) throw new SkipException("Skip test for release build, as autodownload request requires 1 hour for prod build");
        settingsPage.updateAutoDownloadConfig(false);
        restartApp();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateFavoritesCount(1)
                .validateDownloadsCount(0);
        settingsPage.updateAutoDownloadConfig(true);
        restartApp();
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateDownloadsCount(1)
                .tapOnDownloadsButton();
        downloadsPage.validateThatContentAppearedInDownloads(STREAM_PODCAST_THE_DAILY.getStreamName());
    }
}
