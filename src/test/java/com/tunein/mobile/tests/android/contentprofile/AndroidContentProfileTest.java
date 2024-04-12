package com.tunein.mobile.tests.android.contentprofile;

import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.contentprofile.ContentProfileTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.STATION_TITLE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons.FAVORITE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons.UNFAVORITE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.PODCAST_TYPE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.SCHEDULED_SPORT_EVENT_TYPE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.FAVORITES;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.TestGroupName.CONTENT_PROFILE_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.DataUtil.getDurationInSeconds;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToRefresh;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;
import static org.openqa.selenium.ScreenOrientation.PORTRAIT;

public class AndroidContentProfileTest extends ContentProfileTest {

    @Override
    public void testEpisodeListening() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()));
    }

    @Override
    public void testFavoriteUnfavoriteAsAnonymousUser(Contents content) {
        ContentProfileButtons[] favoriteActions = {FAVORITE, UNFAVORITE};
        for (ContentProfileButtons favoriteAction : favoriteActions) {
            deepLinksUtil.openContentProfileThroughDeeplink(content);
            contentProfilePage
                    .tapOnFavoriteButton()
                    .validateFavoriteButton(favoriteAction);
            navigationAction.tapBackButtonIfDisplayed();
            navigationAction.navigateTo(HOME);
            navigationAction.navigateTo(FAVORITES);

            switch (favoriteAction) {
                case FAVORITE -> {
                    favoritesPage.validateThatContentAppearedInFavorites(
                            content.getStreamName(),
                            favoritesPage.getCategoryTypeFromContents(content)
                    );
                }
                case UNFAVORITE -> {
                    favoritesPage.validateThatContentDisappearedInFavorites(content.getStreamName());
                }
                default -> throw new IllegalStateException("Unexpected value: " + favoriteAction);
            }
        }
    }

    @Override
    public void testFavoriteUnfavoriteTeamAsAnonymousUser() {
        deepLinksUtil.openTuneThroughDeeplink(TEAM_CATEGORY_BALTIMORE_ORIOLES, true);
        ContentProfileButtons[] favoriteActions = {UNFAVORITE, FAVORITE};
        for (ContentProfileButtons favoriteAction : favoriteActions) {
            deepLinksUtil.openContentProfileThroughDeeplink(TEAM_CATEGORY_BALTIMORE_ORIOLES);
            contentProfilePage
                    .tapOnFavoriteButton()
                    .validateFavoriteButton(favoriteAction);
            navigationAction.tapBackButtonIfDisplayed();
            navigationAction.navigateTo(HOME);
            navigationAction.navigateTo(FAVORITES);

            switch (favoriteAction) {
                case FAVORITE -> {
                    favoritesPage.validateThatContentAppearedInFavorites(
                            TEAM_CATEGORY_BALTIMORE_ORIOLES.getStreamName(),
                            favoritesPage.getCategoryTypeFromContents(TEAM_CATEGORY_BALTIMORE_ORIOLES)
                    );
                }
                case UNFAVORITE -> {
                    favoritesPage.validateThatContentDisappearedInFavorites(TEAM_CATEGORY_BALTIMORE_ORIOLES.getStreamName());
                }
                default -> throw new IllegalStateException("Unexpected value: " + favoriteAction);
            }
        }
    }

    @Override
    public void testFavoriteUnfavoriteEventContentProfileAsAnonymousUser() {
        for (ContentProfileButtons favoriteAction : new ContentProfileButtons[]{FAVORITE, UNFAVORITE}) {
            navigationAction.navigateToBrowsies(SPORTS);
            sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EVENTS, LIST, SHORT, 1, false, true);

            // Get profile name
            String profileTitle = contentProfilePage.getContentProfileTitleText();
            contentProfilePage
                    .tapOnFavoriteButton()
                    .validateFavoriteButton(favoriteAction);
            navigationAction.navigateTo(FAVORITES);

            switch (favoriteAction) {
                case FAVORITE -> favoritesPage.validateThatContentAppearedInFavorites(profileTitle, CATEGORY_TYPE_SHOWS);
                case UNFAVORITE -> favoritesPage.validateThatContentDisappearedInFavorites(profileTitle);
                default -> throw new IllegalStateException("Unexpected value: " + favoriteAction);
            }
        }
    }

    //TO DO- yet to add data providers for genre
    @TestCaseIds({
            @TestCaseId("575465"), @TestCaseId("575464"),
            @TestCaseId("575467"), @TestCaseId("712129")
    })
    @Test(description = "Verify Content Profile Pages When Offline",
            dataProviderClass = ContentProvider.class,
            dataProvider = "contentProfilesForOfflineMode",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testContentProfilePagesWhenOffline(Contents content) {
        deviceNativeActions.disableWiFi();
        deepLinksUtil.openContentProfileThroughDeeplinkLightVersion(content);
        scrollToRefresh();
        offlinePage
                .waitUntilPageReady()
                .validateOfflineErrorMessageIsDisplayed();
    }

    public void testOpenAllAlbumsFromArtistProfile() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_ARTIST_DRAKE);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_ALBUMS);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();

        String contentCellTitle = contentsListPage.getContentCellTitleTextByIndex(0);
        contentsListPage.clickOnContentCellByIndex(0);
        contentProfilePage.validateContentProfileTitleIsEqualTo(contentCellTitle);
    }

    @Override
    public void testCaretButtonInContentProfilePage(Contents content) {
        deepLinksUtil.openContentProfileThroughDeeplink(content);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_STATIONS_THAT_PLAY);
        contentsListPage
                .skipTestIfNotOnContentsListPage()
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();

        String contentCellTitle = contentsListPage.getContentCellTitleTextByIndex(0);
        contentsListPage.clickOnContentCellByIndex(0, LIVE_STATION);
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(contentCellTitle);
    }

    @Override
    public void testCaretButtonInContentProfilePageForPodcasts() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_EPISODES);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
        String contentCellTitle = contentsListPage.getContentCellTitleTextByIndex(0);
        contentsListPage.clickOnContentCellByIndex(0, PODCAST);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()))
                .validateNowPlayingSubtitleIsEqualTo(contentCellTitle);
    }

    @TestCaseId("712119")
    @Test(description = "[Three dots] Episode playback", groups = {CONTENT_PROFILE_TEST})
    public void testEpisodePlayButton() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog.tapEpisodePlayButton();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("130693")
    @Test(description = "Landscape mode and expanded dropdown menu", groups = {CONTENT_PROFILE_TEST})
    public void testLandscapeModeAndExpandedDropdownMenu() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.expandMoreSection();
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        moreDropDownPage.validateMoreDropdownIsExpanded();
        deviceNativeActions.setOrientationMode(PORTRAIT);
        moreDropDownPage.validateMoreDropdownIsExpanded();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("30132")
    @Test(description = "check UI for landscape mode", groups = {CONTENT_PROFILE_TEST})
    public void testCheckUIForLandscapeMode() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(PODCAST_TYPE));
    }

    @Issue("DROID-14171")
    @Override
    public void testPodcastProfileWithLiveStream() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STAR_TALK_RADIO_PODCAST);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, LIST, SHORT, 1, false);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STAR_TALK_RADIO_LIVE_STATION.getStreamType()))
                .minimizeNowPlayingScreen();
        //TODO: remove set scroll count from getContentItemDataByIndex to 3 when switched to production api
        String contentItemMetadata = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, 1, STATION_TITLE, false, 10);
        miniPlayerPage.validateCategoryItemWithMiniPlayerMetadata(STATION_TITLE, contentItemMetadata);
    }

    @Override
    public void testContentProfileFavoriteMore() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage
                .tapOnFavoriteButton()
                .validateFavoriteButton(FAVORITE)
                .expandMoreSection();
        moreDropDownPage.validateMoreDropdownIsExpanded();
        contentProfilePage
                .tapOnFavoriteButton()
                .validateFavoriteButton(UNFAVORITE);
        moreDropDownPage.validateMoreDropdownIsExpanded();
    }

    @Override
    public void testCompareEpisodeLength() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        String contentProfileDuration = episodeModalDialog.getEpisodeDuration();
        episodeModalDialog.tapEpisodePlayButton();
        nowPlayingPage
                .stopStreamPlaying()
                .validateStreamDurationIsCloseTo(getDurationInSeconds(contentProfileDuration.replace("Duration: ", "")));
    }

    @Override
    public void testScheduledSportEventContentProfileAsAnonymousUser() {
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EVENTS, LIST, SHORT, 1, false, true);
        contentProfilePage
                .validateUIElements(contentProfilePage.contentProfileElements(SCHEDULED_SPORT_EVENT_TYPE))
                .validateTextOfUIElements(contentProfilePage.contentProfileElements(SCHEDULED_SPORT_EVENT_TYPE));
    }

}
