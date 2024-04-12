package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_0_5;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.*;
import static com.tunein.mobile.testdata.TestGroupName.IGNORE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;

public abstract class NowPlayingScrollableTest extends BaseTest {

    @Issue("IOS-16774")
    @TestCaseIds({@TestCaseId("729376"), @TestCaseId("730342"), @TestCaseId("749400"), @TestCaseId("749404")})
    @Test(description = "Check Now Playing Scrollable elements",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProvider",
            groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableUIElements(Contents content) {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.validateNowPlayingCardsElements(content);
    }

    @Issue("IOS-16774")
    @TestCaseIds({@TestCaseId("730444"), @TestCaseId("749405")})
    @Test(description = " Opened content from Now Playing scrollable card", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableOpenFromNowPlaying() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .openContentByNumberInCard(2, SIMILAR_TO_CARD)
                .validateNowPlayingCardsElements();
    }

    @TestCaseIds({@TestCaseId("730201"), @TestCaseId("730427"), @TestCaseId("749410")})
    @Test(description = "Check Episode card", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableEpisodeCard() {
        Contents podcast = STREAM_PODCAST_MARKETPLACE;
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(podcast);
        nowPlayingPage
                .validateNowPlayingCardElementsFor(EPISODE_CARD, podcast)
                .tapOnSeeMoreButton()
                .validateNowPlayingCardElementsFor(EPISODE_CARD, podcast)
                .validateDateAndTimeDisplayedInEpisodeCard()
                .tapOnSeeLessButton()
                .validateNowPlayingCardElementsFor(EPISODE_CARD, podcast)
                .scrollToRequiredCard(PROFILE_CARD);
        nowPlayingPage.tapOnGoToProfileButtonInProfileCard();
        contentProfilePage.validateContentProfilePageIsOpened();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.scrollToRequiredCard(PROFILE_CARD);
        nowPlayingPage.tapOnGoToProfileButtonInProfileCard();
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("730201"), @TestCaseId("730427"), @TestCaseId("749413")})
    @Test(description = "Test Recents scrollable card", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableRecents() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        int numberOfStreamsToOpen = 4;
        settingsPage.enableScrollableNowPlayingFlow();
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .validateNumberOfContentInRecentCard(numberOfStreamsToOpen - 1);
    }

    @Issue("IOS-16774")
    @TestCaseIds({@TestCaseId("730443"), @TestCaseId("749406")})
    @Test(description = "Check Nowplaying Scrollable After opening from Browse/Profile/etc", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public abstract void testScrollableOpenedFrom();

    @TestCaseIds({@TestCaseId("730153"), @TestCaseId("749407")})
    @Test(description = "Check NowPlaying scrollable after re-opening", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableReopening() {
        Contents content = STREAM_PODCAST_MARKETPLACE;
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil
                .openTuneThroughDeeplink(content)
                .minimizeNowPlayingScreen();
        miniPlayerPage.extendMiniPlayer(content);
        nowPlayingPage
                .validateNowPlayingCardsElements(content)
                .minimizeNowPlayingScreen();
        miniPlayerPage.extendMiniPlayer(content);
        nowPlayingPage
                .validateMediaControlsButtonsLooksAsExpected()
                .validateNowPlayingCardsElements(content);
    }

    @TestCaseId("749418")
    @Test(description = "Log out during the stream is playing", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testLogOutDuringStreamIsPlaying() {
        Contents content = STREAM_PODCAST_MARKETPLACE;
        settingsPage.enableScrollableNowPlayingFlow();
        signInPage.signInFlowForUser(USER_GENERAL);
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateNowPlayingCardsElements(content)
                .minimizeNowPlayingScreen();
        userProfilePage.signOutUserFlow();
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateNowPlayingCardsElements(content);
    }

    @Issues({@Issue("DROID-16040"), @Issue("IOS-16774")})
    @TestCaseId("749421")
    @Test(description = "Using background mode on the Now Playing screen", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testUsingBackgroundModeOnTheNowPlayingScreen() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateStreamStartPlaying()
                .scrollToRequiredCard(PROFILE_CARD);

        nowPlayingPage.validateNowPlayingCardsElements(STREAM_STATION_WITHOUT_ADS);
        runAppInBackground(Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()));
        nowPlayingPage.validateNowPlayingCardsElements(STREAM_STATION_WITHOUT_ADS);
    }

    @Issues({@Issue("DROID-16040"), @Issue("IOS-17218")})
    @TestCaseIds({@TestCaseId("749403"), @TestCaseId("750403")})
    @Test(description = "Check scrolling during pre roll", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public abstract void testScrollingDuringPreRoll();

    @Issue("DROID-16040")
    @TestCaseId("749411")
    @Test(description = "NP Scrollable and seek bar", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testNowPlayingScrollableAndSeekBar() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        Contents podcast = STREAM_FREE_PODCAST_WITH_LONG_EPISODE;
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(podcast);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(DEFAULT, podcastDuration)
                .validateNowPlayingSeekBarChangedPosition(DEFAULT, podcastDuration)
                .moveScrubberSliderToSpecificPosition(BEGINNING, podcastDuration)
                .validateNowPlayingSeekBarChangedPosition(BEGINNING, podcastDuration)
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeChanged(podcastEpisodeName)
                .validateNowPlayingOpenNewEpisode(podcastEpisodeName)
                .validateNowPlayingScreenIsScrollable();
    }

    @Issues({@Issue("DROID-16040"), @Issue("IOS-16835"), @Issue("IOS-16774")})
    @TestCaseId("749416")
    @Test(description = "Check now playing scrollable and car mode", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testNPScrollableAndCarMode() {
        settingsPage.enableScrollableNowPlayingFlow();
        navigationAction.navigateTo(CARMODE);
        carModePage
                .validateThatCarModePageIsDisplayed()
                .clickOnCarModeRecentsButton();
        contentsListPage.openContentUnderCategoryWithHeader(RECENTS, LIST, SHORT, 2, false);
        carModePage.clickOnExitCardModeMode();

        miniPlayerPage
                .waitUntilPageReady()
                .extendMiniPlayer();
        nowPlayingPage
                .validatePauseButtonDisplayed()
                .validateNowPlayingCardsElements();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);
        carModePage.clickOnExitCardModeMode();
        miniPlayerPage.extendMiniPlayer();
        if (nowPlayingPage.isPrerollDisplayed()) {
            scroll(DOWN, 5);
            nowPlayingPage
                    .validateSeekBarDisplayed()
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .validateNowPlayingCardsElements(STREAM_STATION_UNICC);
        }
    }

    @Issue("DROID-16040")
    @TestCaseId("749549")
    @Test(description = "test scrollable local radio view", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public abstract void testScrollableLocalRadioView();

    @Issue("DROID-16040")
    @TestCaseId("749419")
    @Test(description = "Switching between screens while item is playing", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testSwitchingBetweenScreensWhileItemIsPlaying() {
        settingsPage.enableScrollableNowPlayingFlow();
        NavigationActionItems[] navigations = {HOME, LIBRARY, SEARCH, PREMIUM};
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (NavigationActionItems navigation : navigations) {
            navigationAction.navigateTo(navigation);
            for (int i = 0; i < 2; i++) {
                switch (navigation) {
                    case HOME -> homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, i + 1, false);
                    case LIBRARY -> libraryPage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, i + 1, false);
                    case SEARCH ->
                            searchPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_POPULAR_STATIONS_IN_YOUR_AREA, TILE, SHORT, i + 1, false, false);
                    case PREMIUM ->
                            premiumPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_COMMERCIAL_FREE_NEWS, TILE, SHORT, i + 1, false);
                    default -> throw new IllegalStateException("Unexpected value: " + navigation);
                }
                if (navigation != SEARCH && contentProfilePage.isOnContentProfilePage()) {
                    contentProfilePage.tapProfilePlayButton();
                }
                nowPlayingPage
                        .validateNowPlayingScreenIsScrollable()
                        .minimizeNowPlayingScreen();
                if (navigation != SEARCH) navigationAction.tapBackButtonIfDisplayed();
            }
        }
    }

    @Issue("DROID-16040")
    @TestCaseId("749417")
    @Test(description = "Scrolling Now playing screen after changing podcast audio speed", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollingNowPlayingScreenAfterChangingPodcastAudioSpeed() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .validateStreamStartPlaying()
                .adjustPlaybackSpeed(SPEED_VALUE_0_5, true);
        nowPlayingPage.validateNowPlayingCardsElements();
    }

    @Issue("DROID-16040")
    @TestCaseId("749422")
    @Test(description = "Test Now Playing screen scrolling after autoplay", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testScrollableAutoplay() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        restartApp();
        upsellPage.closeUpsell();
        continueListeningDialog.tapOnContinuePlayButtonIfDisplayed();
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateNowPlayingCardsElements();
    }

    @Issue("DROID-16040")
    @TestCaseId("749412")
    @Test(description = "NP Scrollable Schedule card", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public abstract void testNowPlayingScheduleCard();

    @Issue("DROID-16040")
    @TestCaseId("749414")
    @Test(description = "MetaData for Scrollable NowPlaying", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testMetaDataForScrollableNowPlaying() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage
                .validateRequiredCardsArePresent(Arrays.asList(PROFILE_CARD))
                .openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 1, false);
        nowPlayingPage.scrollToRequiredCard(PROFILE_CARD);
        nowPlayingPage.validateNowPlayingStationCardTitleIsEqualTo(STREAM_STATION_WITHOUT_ADS.getStreamName());
    }
}
