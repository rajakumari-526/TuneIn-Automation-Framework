package com.tunein.mobile.tests.common.contentprofile;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.STATION_TITLE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.*;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.NEWS_AND_TALK;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public abstract class ContentProfileTest extends BaseTest {

    @TestCaseIds({
            @TestCaseId("221628"), @TestCaseId("30131"), // Station Content Profile
            @TestCaseId("221665"), @TestCaseId("711981"), // Podcast Content Profile
            @TestCaseId("130622"), @TestCaseId("221723"), @TestCaseId("130575"), // Album Content Profile
            @TestCaseId("576023"), @TestCaseId("576299"), // Shows Content Profile
            @TestCaseId("221804"), @TestCaseId("130655"), // Single Event Content Profile
            @TestCaseId("576453"), @TestCaseId("576430"), // Platform Acceptance test
            @TestCaseId("33741"), @TestCaseId("576447"), // Platform Acceptance test
            @TestCaseId("749132") // Audiobook test
    })
    @Test(description = "Verify UI elements of the different content profile pages",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentContentProfiles",
            groups = {CONTENT_PROFILE_TEST, PLATFORM_TEST, ACCEPTANCE_TEST}
    )
    public void testContentProfilePages(Contents content) {
        ContentProfileType contentProfileType = getContentProfileType(content.getContentProfileType());
        deepLinksUtil.openContentProfileThroughDeeplink(content);
        contentProfilePage
                .validateUIElements(contentProfilePage.contentProfileElements(contentProfileType))
                .validateTextOfUIElements(contentProfilePage.contentProfileElements(contentProfileType));
    }

    @TestCaseIds({@TestCaseId("221643"), @TestCaseId("130658"), @TestCaseId("221664"), @TestCaseId("749132"), @TestCaseId("749622")})
    @Test(
            description = "Verify stream plays from different content profile pages while stream is playing",
            dataProviderClass = ContentProvider.class,
            dataProvider = "playableThroughDeepLinkForContentProfiles",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testContentProfilePagesAndPlayStream(Contents station) {
        //  If stream is premium, sign-in with a premium account is required
        if (station.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        // Tune to a stream via deeplink and minimize now-playing
        deepLinksUtil
                .openTuneThroughDeeplink(station)
                .minimizeNowPlayingScreen()
                .validateMiniPlayerIsDisplayed();

        // Open content profile screen via deeplink and tap on the profile play button
        deepLinksUtil.openContentProfileThroughDeeplink(station);
        contentProfilePage.tapProfilePlayButton(station);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(station.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("221630"), @TestCaseId("130648"),
            @TestCaseId("221759"), @TestCaseId("130545"),
            @TestCaseId("221664"), @TestCaseId("221661")})
    @Test(
            description = "Verify stream plays from different content profile pages while stream is NOT playing",
            dataProviderClass = ContentProvider.class,
            dataProvider = "playableContentProfiles",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testStreamPlaybackFromStationProfilePageWhenStreamIsNotPlaying(Contents station) {
        //  If stream is premium, sign-in with a premium account is required
        if (station.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        // Open content profile screen via deeplink and tap on the profile play button
        deepLinksUtil.openContentProfileThroughDeeplink(station);
        contentProfilePage.tapProfilePlayButton(station);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(station.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("221646"), @TestCaseId("130651")})
    @Test(
            description = "Open station through podcast cell from station content profile",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testPodcastStreamPlaybackFromStationProfilePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_KQED);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_PODCASTS, LIST, MEDIUM, 1, false);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("221663"), @TestCaseId("130649")})
    @Test(
            description = "Open station through station cell from station content profile",
            dataProviderClass = ContentProvider.class,
            dataProvider = "playBackContentProfiles",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testStationStreamPlaybackFromStationProfilePage(Contents station) {
        deepLinksUtil.openContentProfileThroughDeeplink(station);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_STATIONS, 1);
        nowPlayingPage
                .validateStreamStartPlaying()
                .stopStreamPlaying();
    }

    @TestCaseIds({
            @TestCaseId("221809"), // More drop down
            @TestCaseId("221825"), // Less drop down
            @TestCaseId("130490"), // More drop down
            @TestCaseId("130491"), // Less drop down
    })
    @Test(
            description = "Verify More Button text after clicking",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testMoreButtonTextAfterClicking() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage
                .validateMoreButtonIsDisplayed()
                .expandMoreSection()
                .validateLessButtonIsDisplayed()
                .collapseLessSection()
                .validateMoreButtonIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("221645"), @TestCaseId("130650")})
    @Test(
            description = "Tune to a related station from restricted content profile page",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testRelatedStationPlaybackFromRestrictedProfilePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_RESTRICTED);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_RELATED_STATIONS, 3);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("221753"), @TestCaseId("130576")})
    @Test(
            description = "Navigate to an Artist profile with available Station that plays this artist",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testStationPlaybackFromArtistProfilePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_ARTIST_DRAKE);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("576488"), @TestCaseId("576297"), @TestCaseId("582325")})
    @Test(
            description = "Test Station profile with available Shows.",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testShowsContentProfilePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 1, false);
        contentProfilePage
                .validateContentProfilePageIsOpened()
                .validateUIElements(contentProfilePage.contentProfileElements(STATION_TYPE_SHOW));
    }

    @TestCaseIds({@TestCaseId("221660"), @TestCaseId("576492")})
    @Test(
            description = "Test non-premium stream from premium music station content profile page",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testPremiumStationContentProfilePage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        // Open free music station category
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, MEDIUM, 1, false);
        nowPlayingPage.goToStreamProfile();
        // Open commercial-free music station category with non-premium user
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_COMMERCIAL_FREE, LIST, MEDIUM, 1, false);
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("221682"), @TestCaseId("712116"), @TestCaseId("576197")})
    @Test(description = "[Listening Content] Episodes",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST, SMOKE_TEST}
    )
    public abstract void testEpisodeListening();

    @TestCaseIds({
            @TestCaseId("221648"), @TestCaseId("23135"), @TestCaseId("23179"), @TestCaseId("130384"), // Station profile
            @TestCaseId("729750"), // O&O station profile
            @TestCaseId("576301"), @TestCaseId("23136"), @TestCaseId("130385"), // Show profile
            @TestCaseId("221683"), @TestCaseId("729748"), @TestCaseId("711819"), // Podcast profile
            @TestCaseId("221754"), @TestCaseId("130383"), // Artist profile
            @TestCaseId("729749"), // Game replay profile
    })
    @Test(
            description = "[Favorite button] Favorite/Unfavorite",
            dataProviderClass = ContentProvider.class,
            dataProvider = "favoriteUnfavoriteContentProfiles",
            groups = {CONTENT_PROFILE_TEST, FAVORITES_TEST}
    )
    public abstract void testFavoriteUnfavoriteAsAnonymousUser(Contents content);

    @Issue("PLATFORM-16150")
    @TestCaseId("130388") // Team station profile
    @Test(description = "[Favorite button] Favorite/Unfavorite Team", groups = {CONTENT_PROFILE_TEST, FAVORITES_TEST})
    public abstract void testFavoriteUnfavoriteTeamAsAnonymousUser();

    @TestCaseIds({@TestCaseId("711932"), @TestCaseId("712115"), @TestCaseId("221667")})
    @Test(
            description = "[Play] First episode",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public void testFirstEpisodePlayback() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_UNRESOLVED);
        String beforePlayback = contentProfilePage.getContentItemDataByIndex(
                CATEGORY_TYPE_EPISODES, 1, ContentDescriptionArea.EPISODE_NAME, false
        );
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateNowPlayingSubtitleIsEqualTo(beforePlayback);
    }

    @TestCaseIds({@TestCaseId("709826"), @TestCaseId("576135")})
    @Test(
            description = "[Donate button] Redirection to donate page",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testDonateButton() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_KQED);
        contentProfilePage.tapOnDonateButton();
        contentProfilePage.validateThatDonatePageIsOpened();
    }

    @TestCaseIds({ @TestCaseId("221752"), @TestCaseId("582334") })
    @Test(
            description = "Open Albums from Artist",
            groups = { CONTENT_PROFILE_TEST }
    )
    public void testOpenAlbumsFromArtist() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_ARTIST_MADONNA);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_ALBUMS);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
        String contentCellTitle = contentsListPage.getContentCellTitleTextByIndex(0);
        contentsListPage.clickOnContentCellByIndex(0);
        contentProfilePage.validateContentProfileTitleIsEqualTo(contentCellTitle);
    }

    //TODO create test case for Android
    @TestCaseId("221684")
    @Test(
            description = "[Affiliate link] redirection to 'Affiliate' screen",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testRedirectionToAffiliateScreen() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnAffiliatesButton();
        contentsListPage.validateContentsListPageIsDisplayed();
    }

    // TODO improve appLaunch logic on app
    @TestCaseIds({@TestCaseId("221845"), @TestCaseId("130652")})
    @Test(
            description = "[Favorite button] Favorite/Unfavorite Event profile screen",
            groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST}
    )
    public abstract void testFavoriteUnfavoriteEventContentProfileAsAnonymousUser();

    @TestCaseIds({@TestCaseId("221669"), @TestCaseId("582326"), @TestCaseId("130547")})
    @Test(description = "test show profile without available stream",
            groups = {CONTENT_PROFILE_TEST})
    public void testShowProfileWithoutAvailableStream() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_SHOW);
        contentProfilePage.tapProfilePlayButtonLightVersion();
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("221607"), @TestCaseId("130685")})
    @Test(description = "Test open Station Content Profile page From Home page", groups = {CONTENT_PROFILE_TEST})
    public void testContentProfilePageFromHomePageStations() {
        navigationAction.navigateTo(HOME);
        homePage.openContentUnderCategoryWithHeader(NATIONAL_AND_WORLD_NEWS, TILE, SHORT, 1, false, true);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("221607"), @TestCaseId("130685"), @TestCaseId("712124")})
    @Test(description = "Test open Podcast Content Profile page From Home page", groups = {CONTENT_PROFILE_TEST})
    public void testContentProfilePageFromHomePagePodcasts() {
        navigationAction.navigateTo(HOME);
        homePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_POPULAR_PODCASTS_IN_YOUR_AREA, TILE, SHORT, 1, false);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("730075"), @TestCaseId("730211"), @TestCaseId("729994")})
    @Test(description = "Test Open content form For You", groups = {CONTENT_PROFILE_TEST})
    public void testNowPlayingPageFromForYou() {
        signInPage.signInFlowForUser(USER_STATION_WITH_RECENTS);
        navigationAction.navigateTo(HOME);
        homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 1, false);
        if (contentProfilePage.isOnContentProfilePage()) {
            contentProfilePage.tapProfilePlayButton();
        }
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateStreamStartPlaying(LIVE_STATION);
    }

    @TestCaseId("221757")
    @Test(description = "Test Open all albums from artist profile", groups = {CONTENT_PROFILE_TEST})
    public abstract void testOpenAllAlbumsFromArtistProfile();

    // TODO improve appLaunch logic on app
    @TestCaseIds({@TestCaseId("221835"), @TestCaseId("576489")})
    @Test(
            description = "[Live Sport Event] Team 1 vs Team 2 not live: Elements",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testLiveEventContentProfileAsAnonymousUser() {
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_LIVE_EVENTS, LIST, SHORT, 1, false, true);
        contentProfilePage
                .validateUIElements(contentProfilePage.contentProfileElements(LIVE_SPORT_EVENT_TYPE))
                .validateTextOfUIElements(contentProfilePage.contentProfileElements(LIVE_SPORT_EVENT_TYPE));
    }

    // TODO improve appLaunch logic on app
    @TestCaseId("221834")
    @Test(
            description = "[Scheduled Sport Event] Team 1 vs Team 2 not live",
            groups = {CONTENT_PROFILE_TEST}
    )
    public abstract void testScheduledSportEventContentProfileAsAnonymousUser();

    @TestCaseIds({
            @TestCaseId("712118"), // Android episode description
            @TestCaseId("712759"), // Android episode title
            @TestCaseId("711982"), // Android episode date
            @TestCaseId("712936"), // iOS episode description
    })
    @Test(
            description = "Check episode cell UI on Podcast Page",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testEpisodeCells() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.validateUIOfContentProfileCells(CATEGORY_TYPE_EPISODES, 2);
    }

    @TestCaseIds({@TestCaseId("221681"), @TestCaseId("130546")})
    @Test(
            description = "Test podcast page with live stream",
            groups = { CONTENT_PROFILE_TEST }
    )
    public abstract void testPodcastProfileWithLiveStream();

    @TestCaseIds({@TestCaseId("221668"), @TestCaseId("712128")})
    @Test(description = "Test live show on podcast page", groups = {CONTENT_PROFILE_TEST})
    public void testContentProfileLiveShowOnPodcastPage() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STAR_TALK_RADIO_PODCAST);
        String showName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, 1, STATION_TITLE, false);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(showName);
    }

    @TestCaseIds({@TestCaseId("221887"), @TestCaseId("712130")})
    @Test(description = "Test green icon is present near new episode", groups = {CONTENT_PROFILE_TEST})
    public void testGreenIconIsPresentNearNewEpisode() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage
                .validateVisibilityOfGreenCircleForNewEpisode(true)
                .validateEpisodePublishedWithInPastFiveDays();
    }

    @TestCaseIds({@TestCaseId("221728"), @TestCaseId("221764"), @TestCaseId("582333")})
    @Test(
            description = "Verify caret button in contest profile page for Artists and Albums",
            dataProviderClass = ContentProvider.class,
            dataProvider = "categoryProvider",
            groups = {CONTENT_PROFILE_TEST}
    )
    public abstract void testCaretButtonInContentProfilePage(Contents content);

    @TestCaseId("130774")
    @Test(description = "Verify caret button in contest profile page for podcasts page", groups = {CONTENT_PROFILE_TEST})
    public abstract void testCaretButtonInContentProfilePageForPodcasts();

    @TestCaseIds({
            @TestCaseId("709788"), @TestCaseId("221649"), // Favorited Show
            @TestCaseId("712126"), // Favorited Podcast
            @TestCaseId("582336"), @TestCaseId("221793") // Favorited Artist
    })
    @Test(
            description = "Open Content Profile from Favorites",
            dataProviderClass = ContentProvider.class,
            dataProvider = "userWithFavorites",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testOpenContentProfileFromFavorites(Contents content) {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        navigationAction.navigateTo(FAVORITES);
        sportsPage.openContentUnderCategoryWithHeader(favoritesPage.getCategoryTypeFromContents(content), LIST, SHORT, 1, false);

        ContentProfileType contentProfileType = getContentProfileType(content.getContentProfileType());
        contentProfilePage
                .validateUIElements(contentProfilePage.contentProfileElements(contentProfileType))
                .validateTextOfUIElements(contentProfilePage.contentProfileElements(contentProfileType));
    }

    @TestCaseIds({@TestCaseId("582322"), @TestCaseId("712125"), @TestCaseId("221687"), @TestCaseId("221691")})
    @Test(description = "Verify Content Profile Page from NowPlaying screen",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProvider",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testContentProfilePageFromNowPlayingScreen(Contents content) {
        ContentProfileType contentProfileType = getContentProfileType(content.getContentProfileType());
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.goToStreamProfile();
        contentProfilePage.validateContentProfilePageIsOpened();
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(contentProfileType));
    }

    @TestCaseIds({ @TestCaseId("221930"), @TestCaseId("130512") })
    @Test(description = "Check share button through more dropdown menu", groups = { CONTENT_PROFILE_TEST })
    public void testShareButtonThroughMoreDropdownMenu() {
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EVENTS, LIST, SHORT, 1, false);
        contentProfilePage.expandMoreSection();
        moreDropDownPage.clickShareButton();
        shareDialog.validateShareDialogDisplayed();
    }

    @TestCaseIds({@TestCaseId("576303"), @TestCaseId("582323")})
    @Test(
            description = "Open show content profile from Browsies",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testOpenProfileFromBrowsies() {
        navigationAction.navigateToBrowsies(NEWS_AND_TALK);
        newsAndTalkPage.openContentWithLabelUnderCategoryWithHeader(CATEGORY_TYPE_EXPLORE_BY_CATEGORY, TILE, SHORT, "Business & Finance");
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 1, false, true);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("729743")})
    @Test(description = "Open Station Profile from Premium screen", groups = {CONTENT_PROFILE_TEST})
    public void testOpenStationProfileFromPremiumPage() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_COMMERCIAL_FREE_CALMING_MUSIC, TILE, SHORT, 1, false);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("566770"), @TestCaseId("711984")})
    @Test(
            description = "Episodes - already played premium episode)",
            groups = { CONTENT_PROFILE_TEST, NOW_PLAYING_TEST }
    )
    public void testAlreadyPlayedEpisode() {
        // TODO improve first launch with a new serial
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_THE_DAILY);
        String latestEpisode = contentProfilePage.getContentItemDataByIndex(
                CATEGORY_TYPE_EPISODES, 2, ContentDescriptionArea.EPISODE_NAME, false);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EPISODES, LIST, SHORT, 2, false);
        nowPlayingPage
                 .stopStreamPlaying()
                 .minimizeNowPlayingScreen();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_THE_DAILY);
        contentProfilePage
                .tapProfilePlayButton();
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateNowPlayingSubtitleIsEqualTo(latestEpisode);
    }

    @TestCaseIds({@TestCaseId("221680"), @TestCaseId("130547")})
    @Test(
            description = "Check Show Profile without live stream",
            groups = {CONTENT_PROFILE_TEST}
    )
    public void testContentProfileWithScheduledShow() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_CNN);
        premiumPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 1, false);
        ContentProfileType contentProfileType = getContentProfileType(SHOW_TYPE_SCHEDULED.getContentProfileType());
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(contentProfileType));
    }

    @TestCaseIds({
            @TestCaseId("130770"), @TestCaseId("130694"), //Android
            @TestCaseId("221831"), @TestCaseId("221830")  //iOS
    })
    @Test(description = "Check short and long description in dropdown more menu", groups = {CONTENT_PROFILE_TEST})
    public void testCheckShortAndLongDescriptionInMoreDropdown() {
        Contents[] podcastsList = {STREAM_STAR_TALK_RADIO_PODCAST, STREAM_PODCAST_MARKETPLACE};
        for (Contents podcast : podcastsList) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            contentProfilePage.validateDescriptionAfterExpandMoreMenu();
            navigationAction.navigateTo(HOME);
        }
    }

    @TestCaseId("730297")
    @Test(
            description = "Test that favorite button doesn't close more",
            groups = { CONTENT_PROFILE_TEST, FAVORITES_TEST }
    )
    public abstract void testContentProfileFavoriteMore();

    @TestCaseId("712143")
    @Test(description = "[Three Dots] Check drawer menu UI", groups = {CONTENT_PROFILE_TEST})
    public void testEpisodeDrawerMenuUI() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog.validateUIElements(episodeModalDialog.episodeModalDialogElements());
    }

    @TestCaseId("712121")
    @Test(description = "Play episode after drawer was closed", groups = {CONTENT_PROFILE_TEST})
    public void testPlayEpisodeAfterDrawerWasClosed() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.minimizeNowPlayingScreen();
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog.validateUIElements(episodeModalDialog.episodeModalDialogElements());
        episodeModalDialog.closeEpisodeModalDialog();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()));
    }

    @Ignore("This test is deprecated by QA")
    @TestCaseIds({@TestCaseId("582337"), @TestCaseId("582338")}) //Android
    @Test(description = "Open Podcast/Station/Shows from Team Category page", groups = {CONTENT_PROFILE_TEST})
    public void testTeamCategoryContentProfile() {
        // TODO: Test fails on iOS due to being unable to handle streams with stream errors
        deepLinksUtil.openContentProfileThroughDeeplink(TEAM_CATEGORY_BALTIMORE_ORIOLES);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .minimizeNowPlayingScreen();
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TEAM_PODCASTS, TILE, SHORT, 1, false);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @Issue("PLATFORM-15083")
    @TestCaseId("712120")
    @Test(description = "Compare episode length between content profile and Now Playing", groups = {CONTENT_PROFILE_TEST, NOW_PLAYING_TEST})
    public abstract void testCompareEpisodeLength();

    @TestCaseIds({ @TestCaseId("130499"), @TestCaseId("582000"), @TestCaseId("582001") })
    @Test(description = "Sharing from Profile screen", groups = { CONTENT_PROFILE_TEST })
    public void testShareDialogCopyButton() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.expandMoreSection();
        moreDropDownPage.clickShareButton();
        shareDialog.validateShareDialogCopyButton();
    }

    @TestCaseIds({@TestCaseId("221810"), @TestCaseId("709830"), @TestCaseId("710231"), @TestCaseId("130511"), @TestCaseId("749132")})
    @Test(description = "Check content profile more drop down menu UI",
            dataProviderClass = ContentProvider.class,
            dataProvider = "stationPodcastAudiobookDataProvider",
            groups = {CONTENT_PROFILE_TEST})
    public void testContentProfileMoreDropDownMenuUI(Contents content) {
        ContentProvider.ContentType contentType = getContentTypeValue(content.getStreamType());
        deepLinksUtil.openContentProfileThroughDeeplink(content);
        contentProfilePage.expandMoreSection();
        moreDropDownPage.validateUIElements(moreDropDownPage.moreDropDownPageElements(contentType));
        contentProfilePage
                .collapseLessSection()
                .validateMoreButtonIsDisplayed();
    }

}
