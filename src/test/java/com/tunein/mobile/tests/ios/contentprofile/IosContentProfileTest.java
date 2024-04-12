package com.tunein.mobile.tests.ios.contentprofile;

import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.contentprofile.ContentProfileTest;
import com.tunein.mobile.utils.GestureActionUtil;
import org.testng.annotations.Test;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_DURATION;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.STATION_TITLE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons.FAVORITE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons.UNFAVORITE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileLabels.LESS;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileLabels.MORE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.PODCAST_TYPE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.SCHEDULED_SPORT_EVENT_TYPE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.CONTENT_PROFILE_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.DataUtil.getDurationInSeconds;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class IosContentProfileTest extends ContentProfileTest {
    @Override
    public void testEpisodeListening() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()))
                .minimizeNowPlayingScreen();
        contentProfilePage.validateLiveStreamSpeakerIconForContentUnder(CATEGORY_TYPE_EPISODES, 1, true);
    }

    @Override
    public void testFavoriteUnfavoriteAsAnonymousUser(Contents content) {
        ContentProfileButtons[] favoriteActions = {FAVORITE, UNFAVORITE};
        for (ContentProfileButtons favoriteAction : favoriteActions) {
            deepLinksUtil.openContentProfileThroughDeeplink(content);
            contentProfilePage
                    .tapOnFavoriteButton()
                    .validateFavoriteButton(favoriteAction);
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
        ContentProfileButtons[] favoriteActions = {FAVORITE, UNFAVORITE};
        for (ContentProfileButtons favoriteAction : favoriteActions) {
            deepLinksUtil.openContentProfileThroughDeeplink(TEAM_CATEGORY_BALTIMORE_ORIOLES);
            contentProfilePage
                    .tapOnFavoriteButton()
                    .validateFavoriteButton(favoriteAction);
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

    public void testFavoriteUnfavoriteEventContentProfileAsAnonymousUser() {
        for (ContentProfileButtons favoriteAction : new ContentProfileButtons[] {FAVORITE, UNFAVORITE}) {
            navigationAction.navigateTo(HOME);
            homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
            GestureActionUtil.scrollToRefresh();
            sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_PREMIUM_EXCLUSIVE_SPORTS, TILE, SHORT, 1, false);
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

    @Issue("PLATFORM-15043")
    @TestCaseIds({@TestCaseId("576298")})
    @Test(description = "Test speaker icon for live show", groups = {CONTENT_PROFILE_TEST})
    public void testSpeakerIconForLiveShow() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PROGRAM_WITH_ACTIVE_SHOW);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, LIST, SHORT, 1, false, true);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();
        contentProfilePage.validateLiveStreamSpeakerIconForContentUnder(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, 1, true);
    }

    @TestCaseId("221757")
    @Test(description = "Test Open all albums from artist profile", groups = {CONTENT_PROFILE_TEST})
    public void testOpenAllAlbumsFromArtistProfile() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_ARTIST_DRAKE);
        contentsListPage.tapOnSeeAllButtonUnderCategoryTitle(CATEGORY_TYPE_ALBUMS);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();

        String contentCellTitle = contentsListPage.getContentCellTitleTextByIndex(0);
        contentsListPage.clickOnContentCellByIndex(0);
        contentProfilePage
                .validateContentProfilePageIsOpened()
                .validateContentProfileTitleIsEqualTo(contentCellTitle);
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
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(contentCellTitle)
                .minimizeNowPlayingScreen();
        contentsListPage.validateEpisodeSpeakerOrGreenIconDisplayed(0);
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
                .validateNowPlayingSubtitleIsEqualTo(contentCellTitle)
                .minimizeNowPlayingScreen();
        contentsListPage.validateEpisodeSpeakerOrGreenIconDisplayed(0);
    }

    @TestCaseId("221893")
    @Test(
            description = "Test Speaker icon near episode disappear after signout",
            dataProviderClass = ContentProvider.class,
            dataProvider = "usersProviders",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testSpeakerIconDisappearsAfterSignOut(Users user) {
        signInPage.signInFlowForUser(user);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 3);
        nowPlayingPage.minimizeNowPlayingScreen();
        contentProfilePage.validateLiveStreamSpeakerIconForContentUnder(CATEGORY_TYPE_EPISODES, 3, true);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnSignOutButton()
                .closeProfilePage();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.validateLiveStreamSpeakerIconForContentUnder(CATEGORY_TYPE_EPISODES, 3, false);
    }

    @Override
    public void testPodcastProfileWithLiveStream() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STAR_TALK_RADIO_PODCAST);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, LIST, SHORT, 1, false);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STAR_TALK_RADIO_LIVE_STATION.getStreamType()))
                .minimizeNowPlayingScreen();
        String contentItemMetadata = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, 1, STATION_TITLE, false);
        miniPlayerPage.validateCategoryItemWithMiniPlayerMetadata(STATION_TITLE, contentItemMetadata);
        contentProfilePage.validateLiveStreamSpeakerIconForContentUnder(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, 1, true);
    }

    @TestCaseId("729744")
    @Test(description = "Open podcast profile from Premium tab", groups = {CONTENT_PROFILE_TEST})
    public void testOpenPodcastProfileFromPremiumScreen() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_JOIN_THE_CONVERSATION, TILE, SHORT, 1, false, true);
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(PODCAST_TYPE));
    }

    @TestCaseId("719335")
    @Test(description = "Check new icon in See all podcasts page", groups = {CONTENT_PROFILE_TEST})
    public void testNewIconInSeeAllPodcastsPage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentsListPage.tapOnSeeAllButtonUnderCategoryTitle(CATEGORY_TYPE_EPISODES);
        contentsListPage.validateEpisodeSpeakerOrGreenIconDisplayed(1);
        int numberOfScrolls = scrollUntilElementDisappear(contentsListPage.getElementFromContentList(1), DOWN, config().scrollThreeTimes());
        scroll(UP, numberOfScrolls);
        contentsListPage.validateEpisodeSpeakerOrGreenIconDisplayed(1);
     }

    @TestCaseIds({@TestCaseId("221898"), @TestCaseId("221899")})
    @Test(description = "Press More/Less button for podcast's episode", groups = {CONTENT_PROFILE_TEST})
    public void testEpisodesLessOrMoreButtonContent() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage
                .tapContentItemMoreLessButtonByIndex(CATEGORY_TYPE_EPISODES, MORE, 1)
                .validateEpisodeLessMoreButtonIsDisplayed(CATEGORY_TYPE_EPISODES, ContentProfileButtons.LESS, 1)
                .validateEpisodeCellAdditionalInfoIsPresent(CATEGORY_TYPE_EPISODES, 1)
                .tapContentItemMoreLessButtonByIndex(CATEGORY_TYPE_EPISODES, LESS, 1)
                .validateEpisodeLessMoreButtonIsDisplayed(CATEGORY_TYPE_EPISODES, ContentProfileButtons.MORE, 1);
    }

    @Override
    public void testContentProfileFavoriteMore() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage
                .tapOnFavoriteButton()
                .validateFavoriteButton(FAVORITE)
                .expandMoreSection()
                .validateLessButtonIsDisplayed();
        contentProfilePage.tapOnFavoriteButton()
                .validateFavoriteButton(UNFAVORITE)
                .validateLessButtonIsDisplayed();
    }

    @Override
    public void testCompareEpisodeLength() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapContentItemMoreLessButtonByIndex(CATEGORY_TYPE_EPISODES, MORE, 1);
        String contentProfileDuration = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_DURATION, false);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage
                .stopStreamPlaying()
                .validateStreamDurationIsCloseTo(getDurationInSeconds(contentProfileDuration.replace("Duration: ", "")));
    }

    // TODO:
    //  1. Remove @Ignore when iOS mobile client app tests are moved to production API
    //  2. Support two categories for both stage and prod
    //   - CATEGORY_TYPE_UPCOMING_EVENTS used in stage
    //   - CATEGORY_TYPE_EVENTS used in prod
    //  3. Add new ViewModelType type `GameCell` for the openContentByCategoryName() method

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
