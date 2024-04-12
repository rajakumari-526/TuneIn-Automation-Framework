package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.SET_ALARM;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.FIFTEEN;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class NowPlayingGeneralTest extends BaseTest {

    //TODO we need to create one test case which will contain all stream formats
    @TestCaseIds({
            @TestCaseId("576192"), @TestCaseId("23412"), @TestCaseId("749628"), // AAC
            @TestCaseId("29795"), @TestCaseId("23415"), @TestCaseId("749630"), @TestCaseId("749519"), // HLS
            @TestCaseId("29781"), @TestCaseId("23409"), @TestCaseId("749627"), // MP3
            @TestCaseId("749629")
    })
    @Test(description = "Test stations with different streams formats",
            dataProviderClass = ContentProvider.class,
            dataProvider = "stationsWithOneStreamFormatDataProvider",
            groups = {SMOKE_TEST, NOW_PLAYING_TEST})
    public void testStationsPlaybackWithDifferentFormats(Contents station) {
        if (station.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(station);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(station.getStreamType()));
    }

    @TestCaseIds({
            @TestCaseId("23400"), @TestCaseId("576052"),
            @TestCaseId("33731"), @TestCaseId("576428"),
            @TestCaseId("37872"), @TestCaseId("24620"),
            @TestCaseId("456468"), @TestCaseId("749116"),
            @TestCaseId("749195"), @TestCaseId("749115"),
            @TestCaseId("749577"), @TestCaseId("749664"),
            @TestCaseId("749613"), @TestCaseId("749615"),
            @TestCaseId("749621")
    })
    @Test(description = "Check UI Elements for different stream types",
            dataProviderClass = ContentProvider.class,
            dataProvider = "fullListOfStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST, PLATFORM_TEST})
    public void testNowPlayingUIElementsForDifferentStreamType(Contents content) {
        boolean isNoAdsButtonExpected = !content.isPremium();
        if (content.isPremium()) signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.validateNoAdsButton(isNoAdsButtonExpected);
        if (content.isPremium()) nowPlayingPage.validateStationLogoDisplayed();
        nowPlayingPage
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(content.getStreamType()))
                .validateSeekBarLooksAsExpected(content)
                .validateMediaControlsButtonsLooksAsExpected(content)
                .stopStreamPlaying()
                .validateNoAdsButton(isNoAdsButtonExpected);
        if (content.isPremium()) nowPlayingPage.validateStationLogoDisplayed();
        nowPlayingPage
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(content.getStreamType()))
                .validateSeekBarLooksAsExpected(content)
                .validateMediaControlsButtonsLooksAsExpected(content);
    }

    @TestCaseId("576545")
    @Test(description = "Now Playing - Switching tabs", groups = {NOW_PLAYING_TEST})
    public void testNowPlayingWhenChangingScreens() {
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
                .minimizeNowPlayingScreen();
        navigationAction
                .mainNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    miniPlayerPage.extendMiniPlayer(STREAM_STATION_WITHOUT_ADS);
                    nowPlayingPage
                            .validateMediaControlsButtonsLooksAsExpected(STREAM_STATION_WITHOUT_ADS)
                            .minimizeNowPlayingScreen();
                });
    }

    @TestCaseIds({@TestCaseId("576578"), @TestCaseId("130334"), @TestCaseId("576198"), @TestCaseId("749614"), @TestCaseId("749511")})
    @Test(description = "Custom URL NowPlaying page", groups = {NOW_PLAYING_TEST, SMOKE_TEST})
    public abstract void testCustomUrl();

    @TestCaseIds({@TestCaseId("130338"), @TestCaseId("749115")})
    @Test(description = "Check game replay NowPlaying page ", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testGameReplayContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_GAME_REPLAY);
        nowPlayingPage
                .validateSeekBarLooksAsExpected(STREAM_GAME_REPLAY)
                .validateMediaControlsButtonsLooksAsExpected(STREAM_GAME_REPLAY)
                .validateStreamTimeAfterFastForwardAndRewind();
        nowPlayingPage
                .stopStreamPlaying()
                .validateSeekBarLooksAsExpected(STREAM_GAME_REPLAY)
                .validateMediaControlsButtonsLooksAsExpected(STREAM_GAME_REPLAY);
        nowPlayingPage
                .tapOnPlayButton()
                .validateSeekBarLooksAsExpected(STREAM_GAME_REPLAY)
                .validateMediaControlsButtonsLooksAsExpected(STREAM_GAME_REPLAY)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749116")
    @Test(description = "Check Now Playing for live event", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public abstract void testLiveEventContent();

    @TestCaseId("749116")
    @Test(description = "Check Now Playing for show", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testShowContent() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 1, false);
        contentProfilePage.skipTestIfNonPlayableContent();
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage
                .stopStreamPlaying()
                .validateSeekBarLooksAsExpected()
                .validateMediaControlsButtonsLooksAsExpected();
        nowPlayingPage
                .tapOnPlayButton()
                .validateSeekBarLooksAsExpected()
                .validateMediaControlsButtonsLooksAsExpected()
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749116")
    @Test(description = "Check Now Playing for show", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testAudiobookChapterContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_GATSBY);
        nowPlayingPage
                .stopStreamPlaying()
                .validateSeekBarLooksAsExpected(STREAM_AUDIOBOOK_GATSBY)
                .validateMediaControlsButtonsLooksAsExpected(STREAM_AUDIOBOOK_GATSBY);
        nowPlayingPage
                .tapOnPlayButton()
                .validateSeekBarLooksAsExpected(STREAM_AUDIOBOOK_GATSBY)
                .validateMediaControlsButtonsLooksAsExpected(STREAM_AUDIOBOOK_GATSBY)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseIds({@TestCaseId("576597"), @TestCaseId("30077")})
    @Test(description = "Verify Alarm timer button Availability - not set", groups = {NOW_PLAYING_TEST})
    public void testAlarmIconNotSet() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog
                .turnOnAlarm()
                .tapOnSaveButton();
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog
                .turnOffAlarm()
                .tapOnSaveButton();
    }

    @TestCaseIds({@TestCaseId("23034"), @TestCaseId("30044"), @TestCaseId("24622"), @TestCaseId("749139")})
    @Test(description = "Verify that stream starts playing at the appropriate time", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public abstract void testAlarmStartsPlaying();

    @TestCaseIds({@TestCaseId("728487"), @TestCaseId("749435")})
    @Test(description = "Check that station is without pre-roll", groups = {NOW_PLAYING_TEST})
    public void testStationWithoutPreroll() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateThatPreRollIsAbsent();
    }

    @TestCaseIds({@TestCaseId("576598"), @TestCaseId("30082")})
    @Test(description = "Check that sleep timer icon is displayed", groups = {NOW_PLAYING_TEST})
    public void testSleepTimerIcon() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.setRequiredSleepTimerOption(FIFTEEN);
        nowPlayingPage.validateSleepTimerIconDisplayed();
    }

    @TestCaseIds({@TestCaseId("24623"), @TestCaseId("720388"), @TestCaseId("37880"), @TestCaseId("24623"), @TestCaseId("749140")})
    @Test(description = "Check that playback is stopped if timer sleep is set", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public abstract void testSleepTimerStopsPlayback();

    @TestCaseIds({@TestCaseId("576599"), @TestCaseId("739050")})
    @Test(description = "Check that sleep timer button is not displayed", groups = {NOW_PLAYING_TEST})
    public void testSleepTimerNotSet() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage
                .validateSleepTimerIconDisplayed()
                .openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.setRequiredSleepTimerOption(FIFTEEN);
        nowPlayingPage.openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.turnOffSleepTimer();
        nowPlayingPage.validateSleepTimerIconDisplayed();
    }

    @TestCaseIds({@TestCaseId("23423"), @TestCaseId("749636")})
    @Test(description = "Switching from station to different types of content",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST})
    public void testSwitchFromStationToDifferentContent(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(content);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(content.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("23424"), @TestCaseId("749637")})
    @Test(description = "Switching from podcast to different types of content",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST})
    public void testSwitchFromPodcastToDifferentContent(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(content);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(content.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("260626"), @TestCaseId("749639")})
    @Test(description = "Switching from premium station to different types of content",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST})
    public void testSwitchFromPremiumStationToDifferentContent(Contents content) {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_SECOND_PREMIUM_OWNED_AND_OPERATED);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(content);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(content.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("260626"), @TestCaseId("23424"), @TestCaseId("23423"), @TestCaseId("749641")})
    @Test(description = "Switching from different types of content to custom Url",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST})
    public void testSwitchFromDifferentContentToCustomUrl(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.minimizeNowPlayingScreen();
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.validateStreamStartPlaying(ContentType.CUSTOM_URL);
    }

    @TestCaseIds({@TestCaseId("728697"), @TestCaseId("749638"), @TestCaseId("749465")})
    @Test(description = "Switching from custom Url to different types of content",
            dataProviderClass = ContentProvider.class,
            dataProvider = "differentStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST})
    public void testSwitchFromCustomUrlToDifferentContent(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(content);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(content.getStreamType()));
        if (getContentTypeValue(content.getStreamType()) == LIVE_STATION && !content.isPremium()) {
            String streamTitle = nowPlayingPage.getTextValueOfNowPlayingTitle();
            nowPlayingPage.goToStreamProfile();
            contentProfilePage.validateContentProfileTitleIsEqualTo(streamTitle);
        }
    }

    @TestCaseId("749641")
    @Test(description = "Switching from audiobook to different types of content", groups = {NOW_PLAYING_TEST})
    public abstract void testSwitchFromAudiobookToDifferentContent();

    @TestCaseIds({@TestCaseId("730298"), @TestCaseId("24620"), @TestCaseId("749116"), @TestCaseId("749614"), @TestCaseId("749440")})
    @Test(description = "Change scrubber slider position for podcast in Now Playing Page", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testChangingScrubberSliderPositionInPodcast() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(DEFAULT, podcastDuration)
                .validateNowPlayingSeekBarChangedPosition(DEFAULT, podcastDuration)
                .moveScrubberSliderToSpecificPosition(BEGINNING, podcastDuration)
                .validateNowPlayingSeekBarChangedPosition(BEGINNING, podcastDuration)
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeChanged(podcastEpisodeName)
                .validateNowPlayingOpenNewEpisode(podcastEpisodeName);
    }

    @TestCaseIds({@TestCaseId("576569"), @TestCaseId("713410"), @TestCaseId("749464")})
    @Test(description = "Check go to profile of custom url", groups = {NOW_PLAYING_TEST})
    public abstract void testGoToProfileCustomUrl();

    @TestCaseId("749113")
    @Test(description = "Anonymous user can play stream", groups = {ACCEPTANCE_TEST, NOW_PLAYING_TEST})
    public void testAnonymousUserCanPlayStream() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_CNN.getStreamName())
                .validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("24596"), @TestCaseId("37869"), @TestCaseId("749192")})
    @Test(description = "Warm and cold state for now playing page", groups = {ACCEPTANCE_TEST, NOW_PLAYING_TEST})
    public abstract void testWarmAndColdStateForNowPlayingPage();

    @TestCaseIds({@TestCaseId("130768"), @TestCaseId("576449"), @TestCaseId("749454")})
    @Test(
            description = "Redirection from unavailable stations",
            groups = {PLATFORM_TEST, NOW_PLAYING_TEST}
    )
    public void testRedirectionFromUnavailableStations() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_UNAVAILABLE_STATION, true);
        String unAvailableStationTitle = STREAM_UNAVAILABLE_STATION.getStreamName();
        nowPlayingPage
                .waitTillVisibilityOfLive()
                .stopStreamPlaying()
                .validateNowplayingStationTitlesNotEqual(unAvailableStationTitle)
                .minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false, false);
        nowPlayingPage
                .waitTillVisibilityOfLive()
                .stopStreamPlaying()
                .validateNowplayingStationTitlesNotEqual(unAvailableStationTitle)
                .minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(HOME);
        homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 2, false, false);
        nowPlayingPage
                .waitTillVisibilityOfLive()
                .stopStreamPlaying()
                .validateNowplayingStationTitlesNotEqual(unAvailableStationTitle);
    }

    @TestCaseId("749149")
    @Test(description = "Test Premium Content", groups = {ACCEPTANCE_TEST, NOW_PLAYING_TEST})
    public abstract void testPremiumContent();

    @TestCaseIds({@TestCaseId("24592"), @TestCaseId("37871"), @TestCaseId("749115")})
    @Test(description = "Test different stream types",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamTypeDataProviders",
            groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testPlayDifferentStreamTypes(Contents contents) {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(contents);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(contents.getStreamType()))
                .tapOnMoreOptionsButton()
                .tapChooseStreamButton();
        for (StreamFormat streamFormat : nowPlayingChooseStreamDialog.getStreamFormatList()) {
            nowPlayingChooseStreamDialog.chooseRequiredStreamFormat(streamFormat);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .validateNowPlayingTitleIsEqualTo(contents.getStreamName());
            nowPlayingPage
                    .tapOnMoreOptionsButton()
                    .tapChooseStreamButton();
        }
    }

    @Issue("DROID-16434")
    @TestCaseIds({@TestCaseId("749650"), @TestCaseId("749642")})
    @Test(description = "Test switch from stream to different screens", groups = {NOW_PLAYING_TEST})
    public void testSwitchFromStreamToDifferentScreens() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validatePauseButtonDisplayed()
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();
        navigationAction
                .mainNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    miniPlayerPage.extendMiniPlayer();
                    nowPlayingPage
                            .validatePauseButtonDisplayed()
                            .validateStreamStartPlaying()
                            .minimizeNowPlayingScreen();
                });
    }

    @TestCaseId("749493")
    @Test(description = "Podcast episode rollover", groups = {NOW_PLAYING_TEST})
    public void testPodcastEpisodeRollover() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeChanged(podcastEpisodeName)
                .validateNowPlayingOpenNewEpisode(podcastEpisodeName);

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EPISODES, LIST, SHORT, 1, false);
        podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeStopped()
                .validatePlayButtonDisplayed();
    }

    @TestCaseId("749570")
    @Test(description = "Audiobook chapter rollover", groups = {NOW_PLAYING_TEST})
    public void testAudiobookChapterRollover() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_GATSBY);
        int chapterDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String chapterName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, chapterDuration)
                .waitEpisodeChanged(chapterName)
                .validateNowPlayingOpenNewEpisode(chapterName);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_GATSBY_LAST_CHAPTER);
        chapterDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, chapterDuration)
                .waitEpisodeStopped()
                .validatePlayButtonDisplayed();
    }

    @TestCaseId("749614")
    @Test(description = "Check podcast after user listen last episode", groups = {NOW_PLAYING_TEST})
    public void testCheckPodcastAfterUserListenLastEpisode() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeStopped()
                .validatePlayButtonDisplayed()
                .tapOnPlayButton()
                .validateNowPlayingSeekBarChangedPosition(BEGINNING, podcastDuration)
                .validateNowPlayingSubtitleIsEqualTo(podcastEpisodeName);
    }

    @TestCaseId("749730")
    @Test(description = "Close Speed playback dialog", groups = {NOW_PLAYING_TEST})
    public void testCloseSpeedPlayBackDialog() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_FREE_SHORT_PODCAST);
        nowPlayingPage.tapOnSpeedButton();

        nowPlayingSpeedPlaybackDialog
                .validateVariableSpeedPlaybackPopUpisDisplayed(true)
                .tapAboveVariableSpeedPlaybackPopUp()
                .validateVariableSpeedPlaybackPopUpisDisplayed(false);

        nowPlayingPage.tapOnSpeedButton();
        nowPlayingSpeedPlaybackDialog.swipeDownVariableSpeedPlaybackPopUp();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseId("749620")
    @Test(description = "Interrupt and restore Audiobook stream", groups = {NOW_PLAYING_TEST})
    public void testInterruptAndRestoreAudiobookStream() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_AUDIOBOOK_GATSBY);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 2);
        nowPlayingPage.validateThatPreRollIsAbsent();

        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        nowPlayingPage.minimizeNowPlayingScreen();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 3);

        nowPlayingPage
                .validateStreamStartPlaying()
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_AUDIOBOOK_GATSBY);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 2);
        nowPlayingPage
                .validateEpisodePlaysFromPreviousSpot(currentStreamTime)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749617")
    @Test(description = "Interrupt and restore GameReplay stream", groups = {NOW_PLAYING_TEST})
    public void testInterruptAndRestoreGameReplayStream() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_GAME_REPLAY);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 2);

        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        long timeBeforeInterruption = System.currentTimeMillis();
        nowPlayingPage.minimizeNowPlayingScreen();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 3, true);
        long timeAfterInterruption = System.currentTimeMillis();

        nowPlayingPage
                .waitUntilPageReadyWithKnownContent(STREAM_GAME_REPLAY)
                .minimizeNowPlayingScreen();

        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 2, true);
        nowPlayingPage
                .waitUntilPageReadyLiteVersion()
                .validateEpisodePlaysFromPreviousSpot((currentStreamTime + (int) (timeAfterInterruption - timeBeforeInterruption) / 1000))
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749619")
    @Test(description = "Interrupt and restore Premium Podcast stream", groups = {NOW_PLAYING_TEST})
    public void testInterruptAndRestorePremiumPodcastStream() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_JSA);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 1, true);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_JSA);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 1, true);
        nowPlayingPage
                .waitUntilPageReadyLiteVersion()
                .stopStreamPlaying()
                .validateStreamPassedTimeIsNotChanged();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 3);
        customWait(Duration.ofSeconds(10));
        int currentStreamTime = nowPlayingPage.getCurrentStreamTimeInSeconds();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 3, true);
        nowPlayingPage
                .waitUntilPageReadyLiteVersion()
                .validateEpisodePlaysFromPreviousSpot(currentStreamTime)
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749570")
    @Test(description = "Play Downloaded Content and switch to different Content", groups = {DOWNLOADS_TEST})
    public abstract void testSwitchingFromDownloadsToDifferentContents();

}
