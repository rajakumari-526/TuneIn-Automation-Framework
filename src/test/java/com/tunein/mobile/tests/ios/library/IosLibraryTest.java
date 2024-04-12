package com.tunein.mobile.tests.ios.library;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.library.LibraryTest;

import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingPageButtons.FAVORITE;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingPageButtons.UNFAVORITE;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.*;
import static com.tunein.mobile.testdata.TestGroupName.FAVORITES_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ElementHelper.getElementName;
import static com.tunein.mobile.utils.ElementHelper.getElementNameOrLabel;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class IosLibraryTest extends LibraryTest {

    @Override
    public void testCustomUrlFavorite() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateNowPlayingSubtitleIsEqualTo(CUSTOM_URL)
                .tapOnFavoriteIcon(true, FAVORITE_CUSTOM_URL);
        customUrlFavoriteDialog.tapSubmitButton();
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage.validateThatContentAppearedInFavorites(CUSTOM_URL, CATEGORY_TYPE_STATIONS);
    }

    @Override
    public void testCustomUrlFromFavoriteList() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_CUSTOM_URL);
        customUrlFavoriteDialog.tapSubmitButton();
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateNowPlayingSubtitleIsEqualTo(CUSTOM_URL);
    }

    @Test(description = "reorder favorite items by long press", groups = {FAVORITES_TEST})
    public void testReorderFavoriteItemsByLongPressInFavoritePage() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        SelenideElement startElement = contentProfilePage.getContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1);
        SelenideElement endElement = contentProfilePage.getContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 3);
        String firstElementBeforeReorder = getElementName(startElement);
        contentsListPage.reorderingTheElements(startElement, endElement);
        customWait(Duration.ofSeconds(10));
        String firstElementAfterReorder = getElementName(contentProfilePage.getContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1));
        contentsListPage.validateOrderOfList(firstElementBeforeReorder, firstElementAfterReorder, true);
    }

    @TestCaseId("266258")
    @Test(description = "reorder favorite items by long press in see all page", groups = {FAVORITES_TEST})
    public void testReorderFavoriteItemsByLongPressInSeeAllPage() {
        signInPage.signInFlowForUser(USER_WITH_MANY_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        contentsListPage.tapOnSeeAllButtonUnderCategoryTitle(CATEGORY_TYPE_EPISODES);
        SelenideElement startElement = contentsListPage.getCellFromContainer(1);
        SelenideElement endElement = contentsListPage.getCellFromContainer(3);
        String firstElementBeforeReorder = getElementNameOrLabel(startElement);
        contentsListPage.reorderingTheElements(startElement, endElement);
        customWait(Duration.ofSeconds(10));
        String firstElementAfterReorder = getElementNameOrLabel(contentsListPage.getCellFromContainer(1));
        contentsListPage.validateOrderOfList(firstElementBeforeReorder, firstElementAfterReorder, false);
    }

    @Override
    public void testOpenStationFromFavorites() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @Override
    public void testUnFavoriteContentFromNPAndFavoritePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(true, FAVORITE_STATION);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnFavoriteIcon(false, FAVORITE_STATION);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        Contents[] favoriteContents = {STREAM_STATION_WITHOUT_ADS, STREAM_PODCAST_MARKETPLACE};
        for (Contents content : favoriteContents) {
            favoritesPage.validateThatContentDisappearedInFavorites(content.getStreamName());
        }
    }

    public void testFavoritesCountAfterUserSignedOut() {
        HashMap<FavoriteContentType, Contents> contentProfiles = new HashMap<>();
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
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(5);

        signUpPage.signUpFlowForUser(generateRandomUser());
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateFavoritesCount(5)
                .tapOnFavoritesButton();
        for (Map.Entry<FavoriteContentType, Contents> content : contentProfiles.entrySet()) {
            contentsListPage.openContentUnderCategoryWithHeader(getCategoryType(content.getKey().toString()), LIST, SHORT, 1, false);
            if (content.getKey().equals(FAVORITE_PODCAST) || content.getKey().equals(FAVORITE_ARTIST) || content.getKey().equals(FAVORITE_SHOW)) {
                contentProfilePage.tapOnFavoriteButton();
                navigationAction.tapBackButtonIfDisplayed();
            } else {
                nowPlayingPage
                        .tapOnFavoriteIcon(false, content.getKey())
                        .minimizeNowPlayingScreen();
            }
        }
        userProfilePage.signOutUserFlow();

        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(0);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(2);
    }

    public void testRedirectUsingPrompt() {
        navigationAction.navigateTo(LIBRARY);
        libraryPage
                .validateRecentsPromptIsDisplayed()
                .tapOnStartListening();
        homePage.validateCategoryHeaderIsPresent(CATEGORY_TYPE_TOP_LOCAL_STATIONS);
        navigationAction.navigateTo(FAVORITES);
        favoritesPage
                .validateAddYourFavoritesPromptIsDisplayed()
                .tapOnFindSomething();
        homePage.validateCategoryHeaderIsPresent(CATEGORY_TYPE_TOP_LOCAL_STATIONS);
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage
                .validateDownloadsPromptIsDisplayed()
                .tapOnFindSomething();
        homePage.validateCategoryHeaderIsPresent(CATEGORY_TYPE_TOP_10_PODCASTS);
    }

    @TestCaseId("23142")
    @Test(description = "Favorite, unfavorite Station From NowPlaying screen", groups = {FAVORITES_TEST})
    public void testFavoriteUnFavoriteStationFromNowPlayingScreen() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        for (NowPlayingPage.NowPlayingPageButtons favoriteAction : new NowPlayingPage.NowPlayingPageButtons[]{FAVORITE, UNFAVORITE}) {
            deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
            boolean addToFavorite = true;
            if (favoriteAction.equals(UNFAVORITE)) {
                addToFavorite = false;
            }
            nowPlayingPage
                    .tapOnFavoriteIcon(addToFavorite, FAVORITE_STATION)
                    .minimizeIfNowPlayingDisplayed();
            navigationAction.navigateTo(FAVORITES);
            switch (favoriteAction) {
                case FAVORITE -> {
                    favoritesPage.validateThatContentAppearedInFavorites(
                            STREAM_STATION_WITHOUT_ADS.getStreamName(),
                            favoritesPage.getCategoryTypeFromContents(STREAM_STATION_WITHOUT_ADS)
                    );
                }
                case UNFAVORITE -> {
                    favoritesPage.validateThatContentDisappearedInFavorites(STREAM_STATION_WITHOUT_ADS.getStreamName());
                }
                default -> throw new IllegalStateException("Unexpected value: " + favoriteAction);
            }
        }
    }

    @TestCaseId("730472")
    @Test(description = "Favorite, unfavorite Show From NowPlaying screen", groups = {FAVORITES_TEST})
    public void testFavoriteUnFavoriteShowFromNowPlayingScreen() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        NowPlayingPage.NowPlayingPageButtons[] favoriteActions = {FAVORITE, UNFAVORITE};
        for (NowPlayingPage.NowPlayingPageButtons favoriteAction : favoriteActions) {
            boolean addToFavorite = true;
            if (favoriteAction.equals(UNFAVORITE)) {
                addToFavorite = false;
            }
            deepLinksUtil.openTuneThroughDeeplink(STREAM_PROGRAM_WITH_ACTIVE_SHOW);
            nowPlayingPage
                    .tapOnFavoriteIcon(addToFavorite, FAVORITE_SHOW)
                    .minimizeIfNowPlayingDisplayed();
            navigationAction.navigateTo(FAVORITES);
            switch (favoriteAction) {
                case FAVORITE -> {
                    favoritesPage.validateThatContentAppearedInFavorites(
                            STREAM_PROGRAM_WITH_ACTIVE_SHOW.getStreamName(),
                            favoritesPage.getCategoryTypeFromContents(STREAM_PROGRAM_WITH_ACTIVE_SHOW)
                    );
                }
                case UNFAVORITE -> {
                    favoritesPage.validateThatContentDisappearedInFavorites(STREAM_PROGRAM_WITH_ACTIVE_SHOW.getStreamName());
                }
                default -> throw new IllegalStateException("Unexpected value: " + favoriteAction);
            }
        }
    }

    @TestCaseId("23165")
    @Test(description = "Favorite and Unfavorite Podcast, episode From NowPlaying screen", groups = {FAVORITES_TEST})
    public void testFavUnfavPodcastEpisodeFromNowPlayingScreen() {
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
        favoritesPage
                .validateThatContentAppearedInFavorites(
                        STREAM_PODCAST_MARKETPLACE.getStreamName(),
                        favoritesPage.getCategoryTypeFromContents(STREAM_PODCAST_MARKETPLACE))
                .validateThatContentAppearedInFavorites(nowPlayingEpisode, CATEGORY_TYPE_EPISODES);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage
                .tapOnFavoriteIcon(false, FAVORITE_PODCAST)
                .tapOnFavoriteIcon(false, FAVORITE_EPISODE)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentDisappearedInFavorites(STREAM_PODCAST_MARKETPLACE.getStreamName());
        favoritesPage.validateThatContentDisappearedInFavorites(nowPlayingEpisode);
    }

    @Override
    public void testPlayDownloadedItem() {
        signInPage.signInFlowForUser(USER_WITH_PREMIUM_RECENTS);
        Contents[] podcasts = {STREAM_PODCAST_MARKETPLACE, STREAM_AUDIOBOOK_GATSBY};
        for (Contents podcast : podcasts) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            customWait(Duration.ofSeconds(30));
            contentProfilePage.tapEpisodeThreeDotsByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1);
            episodeModalDialog.tapEpisodeDownloadButton();
        }
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(0);

        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        nowPlayingPage
                .generateRecentsForNowPlayingPage(3, GENERATE_RECENT_LIST)
                .minimizeNowPlayingScreen();

        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(0);
        nowPlayingPage
                .validateEpisodePlaysFromPreviousSpot(currentStreamTime)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @Override
    public void tesAutoDownloads() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_THE_DAILY);
        contentProfilePage.tapOnFavoriteButton();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(1);
        libraryPage.validateDownloadsCount(0);
        restartApp();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateDownloadsCount(1);
        libraryPage.tapOnDownloadsButton();
        downloadsPage.validateStationAppearedInDownloads(STREAM_PODCAST_THE_DAILY.getStreamName());
    }
}
