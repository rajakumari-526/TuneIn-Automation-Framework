package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingGeneralTest;
import com.tunein.mobile.utils.DataUtil;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.BasePage.isReleaseTestRun;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.FAST_FORWARD;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.REWIND;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStreamTimeType.PASSED;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_SONG;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.FIFTEEN;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.ONE;
import static com.tunein.mobile.testdata.TestGroupName.ACCEPTANCE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_EVENT;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_FOR_LOGIN_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToRefresh;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_TOOLTIP_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_BACKGROUND;

public class AndroidNowPlayingGeneralTest extends NowPlayingGeneralTest {

    @Override
    public void testCustomUrl() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(CUSTOM_URL)
                .validateNowPlayingMetadataIsNotEmpty(ContentType.CUSTOM_URL)
                .stopStreamPlaying()
                .validatePlayButtonDisplayed()
                .tapOnPlayButton()
                .validatePauseButtonDisplayed();
    }

    @Override
    public void testSleepTimerStopsPlayback() {
        SleepTimerOptions sleepTimeout = isReleaseTestRun() ? FIFTEEN : ONE;
        int timeout = isReleaseTestRun() ? config().fifteenMinutesInSeconds() : config().oneMinuteInSeconds();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateSleepTimerIconDisplayed()
                .openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.setRequiredSleepTimerOption(sleepTimeout);
        nowPlayingPage.validateEditSleepTimerIconDisplayed();
        customWait(Duration.ofSeconds(timeout));
        nowPlayingPage.validateStreamIsStopped();
    }

    @Override
    public void testSwitchFromAudiobookToDifferentContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (Object[] object : differentStreamTypesDataProviders()) {
            Contents contentFromDataProvider = (Contents) object[0];
            Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
            arguments.put(BOOST_TOOLTIP_ENABLED, "false");
            arguments.put(BOOST_ENABLED, "false");
            updateLaunchArguments(arguments);
            deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_LIAR);
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.navigateTo(SEARCH);
            searchPage.searchStreamAndPlayFirstResult(contentFromDataProvider);
            nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(contentFromDataProvider.getStreamType()));
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.tapBackButtonIfDisplayed();
            searchPage.clearSearchResults();
            deviceNativeActions.hideKeyboard();
            navigationAction.navigateTo(HOME);
        }
    }

    @Override
    public void testLiveEventContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        sportsPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EVENTS, LIST, SHORT, 1, false, true);
        contentProfilePage.skipTestIfNonPlayableContent();
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage
                .stopStreamPlaying()
                .validateSeekBarLooksAsExpected(LIVE_EVENT)
                .validateMediaControlsButtonsLooksAsExpected();
        nowPlayingPage
                .tapOnPlayButton()
                .validateSeekBarLooksAsExpected(LIVE_EVENT)
                .validateMediaControlsButtonsLooksAsExpected();
    }

    @TestCaseIds({@TestCaseId("37872"), @TestCaseId("749195"), @TestCaseId("749592")})
    @Test(description = "Check Forward/Rewind buttons for station", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testStationForwardAndRewindButtons() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STAR_TALK_RADIO_LIVE_STATION);
        nowPlayingPage
                .stopStreamPlaying()
                .waitUntilContentPartiallyBuffered()
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @Override
    public void testAlarmStartsPlaying() {
        int minutesAhead = 2;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_LA_JEFA);
        nowPlayingPage
                .setAlarmTimeFlow(minutesAhead)
                .minimizeNowPlayingScreen()
                .tapStopButton();
        miniPlayerPage.waitUntilMiniPlayerDisappear(Duration.ofSeconds(minutesAhead * config().oneMinuteInSeconds()));
        nowPlayingAlarmClockDialog
                .validateAlarmClockDialogIsOpened()
                .tapOnCloseButton();
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_LA_JEFA.getStreamType()));
    }

    @Override
    public void testGoToProfileCustomUrl() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateNowPlayingTitleIsEqualTo(CUSTOM_URL)
                .tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.validateGoToProfileButtonIsNotDisplayed();
    }

    @TestCaseIds({@TestCaseId("131037"), @TestCaseId("130375")})
    @Test(description = "Validate that song is favorited instead of station", groups = {NOW_PLAYING_TEST})
    public void testFavoriteSong() {
        List<Contents> stationList = Arrays.asList(STREAM_STATION_CLASSIC_ROCK_HITS, STREAM_STATION_CHRISTIAN_HITS, STREAM_STATION_TODAYS_HITS);
        for (Contents station : stationList) {
            deepLinksUtil.openTuneThroughDeeplink(station);
            nowPlayingPage
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .tapOnFavoriteIcon(true);
            if (nowPlayingFavoriteDialog.isFavoritePopUpDisplayed()) {
                String song = DataUtil.getSongNameFromString(nowPlayingFavoriteDialog.getFavoriteContentText(FAVORITE_SONG));
                nowPlayingFavoriteDialog.tapOnFavoriteContent(true, FAVORITE_SONG);
                nowPlayingPage.minimizeNowPlayingScreen();
                navigationAction.navigateTo(FAVORITES);
                favoritesPage
                        .validateFavouritesPageIsOpened()
                        .validateThatContentAppearedInFavorites(song, CATEGORY_TYPE_SONGS)
                        .validateThatContentDisappearedInFavorites(station.getStreamName());
                return;
            }
        }
        ReporterUtil.log("Unable to favorite song on tested station");
        throw new SkipException("Skip Test");
    }

    @TestCaseId("37879")
    @Test(description = "Verify alarm changes volume", groups = {ACCEPTANCE_TEST, NOW_PLAYING_TEST})
    public void testAlarmVolume() {
        int minutesAhead = 2;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        deviceNativeActions.clickVolumeDownButton(8);

        nowPlayingPage
                .validateAudioVolumeLessThanMaximum(nowPlayingPage.getCurrentVolumeFromAudioInfo())
                .setAlarmTimeFlow(minutesAhead)
                .minimizeNowPlayingScreen()
                .tapStopButton();
        miniPlayerPage.waitUntilMiniPlayerDisappear(Duration.ofSeconds(minutesAhead * config().oneMinuteInSeconds()));
        nowPlayingAlarmClockDialog
                .validateAlarmClockDialogIsOpened()
                .tapOnCloseButton();
        nowPlayingPage
                .validateAudioVolumeIsMaximum(nowPlayingPage.getCurrentVolumeFromAudioInfo());
    }

    @Override
    public void testWarmAndColdStateForNowPlayingPage() {
        signInPage.signInFlowForUser(USER_FOR_LOGIN_TEST);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        List<NavigationAction.NavigationActionItems> pageList = Arrays.asList(HOME, LIBRARY, FAVORITES, PREMIUM, DOWNLOADS, PROFILE);
        pageList.forEach(barItem -> {
            navigationAction.navigateTo(barItem);
            activateSettingsApp();
            activateApp();
            navigationAction.validateCorrespondingPageIsOpened(barItem);
            if (userProfilePage.isOnProfilePage()) {
                userProfilePage.closeProfilePage();
            }
            miniPlayerPage.validateMiniPlayerStopButtonIsDisplayed();
        });
        miniPlayerPage.extendMiniPlayer();
        activateSettingsApp();
        activateApp();
        nowPlayingPage.validateThatNowPlayingIsOpened();
        restartApp();
        continueListeningDialog
                .closeContinueListeningDialogIfDisplayed();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        homePage.validateHomePageIsOpened();
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .validateUserProfileDetails(USER_FOR_LOGIN_TEST)
                .closeProfilePage();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSettingsButton();
        settingsPage.tapOnExitButton();
        customWait(Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        nowPlayingPage.verifyAppState(RUNNING_IN_BACKGROUND);
    }

    @Override
    public void testPremiumContent() {
        Contents[] streams = {STREAM_AL_JAZEERA, STREAM_PREMIUM_PODCAST_JSA};
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (Contents stream : streams) {
            deepLinksUtil.openTuneThroughDeeplink(stream);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .validateStreamTimeAfterFastForwardAndRewind()
                    .stopStreamPlaying()
                    .validateStreamIsStopped()
                    .tapOnPlayButton()
                    .validateSeekBarLooksAsExpected(stream);
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @Issue("DROID-16225")
    @TestCaseId("749683")
    @Test(description = "Test Listen live or rewind window functionality", groups = {NOW_PLAYING_TEST})
    public void testListenLiveOrRewindButton() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(STREAM_WITH_LISTEN_LIVE_OR_REWIND_BUTTON, true);
        listenLiveRewindModalDialog.tapRewindButton();
        nowPlayingPage
                .validateStreamCloseToStart()
                .tapOnPlayLiveButton()
                .validateLiveLableIsDisplayed()
                .waitForTheStreamPlayback();
        int secondsBeforeAction = nowPlayingPage.getStreamTimeInSeconds(PASSED);
        nowPlayingPage
                .tapNowPlayingRewindOrFastForwardButton(REWIND)
                .waitForTheStreamPlayback()
                .tapNowPlayingRewindOrFastForwardButton(REWIND)
                .waitForTheStreamPlayback()
                .validateTimerIsChangedAfterRewindOrForward(secondsBeforeAction, PASSED, REWIND, 2);
        int secondsBeforeActionAfterFastForward = nowPlayingPage.getStreamTimeInSeconds(PASSED);
        nowPlayingPage
                .tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD)
                .waitForTheStreamPlayback()
                .validateTimerIsChangedAfterRewindOrForward(secondsBeforeActionAfterFastForward, PASSED, FAST_FORWARD, 1);
    }

    @TestCaseId("749550")
    @Test(description = "Test long press on stations item", groups = {NOW_PLAYING_TEST})
    public void testLongPressOnStationsItem() {
        String station = homePage.getContentTitleUnderCategoryWithHeader(LOCAL_PUBLIC_RADIO, TILE, SHORT, 1);
        homePage.openContentUnderCategoryWithHeader(LOCAL_PUBLIC_RADIO, TILE, SHORT, 1, false);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();
        homePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TOP_LOCAL_NEWS, TILE, SHORT, 1, true);
        contentListItemDialog
                .validateContentStreamDialogIsDisplayed()
                .tapOnContentItem(0);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();
        homePage.openContentUnderCategoryWithHeader(LOCAL_PUBLIC_RADIO, TILE, SHORT, 1, true);
        contentListItemDialog
                .validateContentStreamDialogIsDisplayed()
                .validateDialogTitleIsEqualToStationName(station);
    }
  
    @Issues({@Issue("DROID-14103"), @Issue("DROID-16413")})
    @TestCaseIds({@TestCaseId("749714"), @TestCaseId("749628"), @TestCaseId("749630"), @TestCaseId("749627"), @TestCaseId("749629")})
    @Test(description = "Check Fast Forward and Rewind buttons for different stream formats", groups = {NOW_PLAYING_TEST})
    public void testCheckFastForwardAndRewindButtonsForDifferentStreamFormats() {
        for (Object[] object : stationsWithOneStreamFormatDataProvider()) {
            Contents contentFromDataProvider = (Contents) object[0];
            deepLinksUtil.openTuneThroughDeeplink(contentFromDataProvider);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .validateRewindAndFastForwardButtonsAreDisplayed()
                    .validateStreamTimeAfterFastForwardAndRewind();
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateRewindAndFastForwardButtonsAreDisplayed()
                .validateStreamTimeAfterFastForwardAndRewind();
    }

    @TestCaseId("749547")
    @Test(description = "Delete Different content from recents", groups = {NOW_PLAYING_TEST})
    public void testDeleteDifferentContentFromRecents() {
        int numberOfStreamsToOpen = 3;
        signInPage.signInFlowForUser(USER_PREMIUM);
        List<Contents> generateRecents = Arrays.asList(STREAM_STATION_WITHOUT_ADS, STREAM_PODCAST_MARKETPLACE, STREAM_NEWS_PREMIUM_FOX_NEWS);
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, generateRecents)
                .minimizeNowPlayingScreen();
        for (Contents content : generateRecents) {
            navigationAction.navigateTo(HOME);
            scrollToRefresh();
            homePage
                    .waitUntilPageReady()
                    .removeRecentsByContentNameUnderCategory(RECENTS, content.getStreamName());
            homePage.validateThatContentNotDisplayedUnderRecents(content.getStreamName());
            navigationAction.navigateTo(LIBRARY);
            libraryPage.validateContentNotDisplayedInLibraryRecents(content.getStreamName());
        }
    }

    @Override
    public void testSwitchingFromDownloadsToDifferentContents() {
        Contents[] contents = {STREAM_FREE_SHORT_PODCAST, STREAM_FREE_PODCAST_WITH_LONG_EPISODE};
        for (Contents content : contents) {
            deepLinksUtil.openContentProfileThroughDeeplink(content);
            String contentName = contentProfilePage.getContentItemDataByIndex(content.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
            contentProfilePage.tapEpisodeThreeDotsByIndex(content.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1);
            episodeModalDialog
                    .tapEpisodeDownloadButton()
                    .closeEpisodeModalDialog();
            navigationAction.navigateTo(SEARCH);
            navigationAction.navigateTo(DOWNLOADS);
            downloadsPage.validateThatContentAppearedInDownloads(contentName);
        }
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(3);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();

        Contents[] streams = {STREAM_PODCAST_MARKETPLACE, STREAM_STATION_CLASSIC_ROCK_HITS, STREAM_STATION_WITHOUT_ADS};
        for (Contents content : streams) {
            navigationAction.navigateTo(HOME);
            navigationAction.navigateTo(DOWNLOADS);
            contentsListPage.clickOnContentCellByIndex(1);
            nowPlayingPage.validateStreamStartPlaying();
            deepLinksUtil.openTuneThroughDeeplink(content);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .minimizeNowPlayingScreen();
        }

        contentsListPage.clickOnContentCellByIndex(1);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(CUSTOM_URL);
    }

}
