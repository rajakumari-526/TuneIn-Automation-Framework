package com.tunein.mobile.tests.ios.nowplaying;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingGeneralTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.Prompts.STREAM_ERROR_POPUP;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_0_5;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_1_0;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.SET_ALARM;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.ONE;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_EVENT;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_FOR_LOGIN_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.BrowserUtil.launchSafariBrowser;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static io.appium.java_client.appmanagement.ApplicationState.NOT_RUNNING;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;

public class IosNowPlayingGeneralTest extends NowPlayingGeneralTest {

    @Override
    public void testCustomUrl() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateNowPlayingSubtitleIsEqualTo(CUSTOM_URL)
                .validateNowPlayingMetadataIsNotEmpty(ContentProvider.ContentType.CUSTOM_URL)
                .stopStreamPlaying()
                .validatePlayButtonDisplayed()
                .tapOnPlayButton()
                .validatePauseButtonDisplayed();
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

    @TestCaseIds({@TestCaseId("576593")})
    @Test(description = "Verify Alarm timer button Availability - is set", groups = {NOW_PLAYING_TEST})
    public void testAlarmIconIsSet() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog
                .turnOnAlarm()
                .tapOnSaveButton();
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog
                .validateEditAlarmButtonsIsDisplayed()
                .tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog.validateAlarmDialogIsOpened();
    }

    @Override
    public void testAlarmStartsPlaying() {
        Integer minutesAhead = 2;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage
                .setAlarmTimeFlow(minutesAhead)
                .minimizeNowPlayingScreen()
                .tapStopButton();
        miniPlayerPage.waitUntilMiniPlayerDisappear(Duration.ofSeconds(minutesAhead * config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_UNICC.getStreamType()));
    }

    @Override
    public void testSleepTimerStopsPlayback() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateSleepTimerIconDisplayed()
                .openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.setRequiredSleepTimerOption(ONE);
        nowPlayingPage.validateEditSleepTimerIconDisplayed();
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage.validateStreamIsStopped();
    }

    @Override
    public void testSwitchFromAudiobookToDifferentContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (Object[] object : differentStreamTypesDataProviders()) {
            Contents contentFromDataProvider = (Contents) object[0];
            deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_LIAR);
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.navigateTo(SEARCH);
            searchPage.searchStreamAndPlayFirstResult(contentFromDataProvider);
            nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(contentFromDataProvider.getStreamType()));
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.tapBackButtonIfDisplayed();
            searchPage.clearSearchResults();
            navigationAction.navigateTo(HOME);
        }
    }

    @Override
    public void testGoToProfileCustomUrl() {
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateNowPlayingSubtitleIsEqualTo(CUSTOM_URL)
                .tapOnNowPlayingTitle();
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @Override
    public void testWarmAndColdStateForNowPlayingPage() {
        signInPage.signInFlowForUser(USER_FOR_LOGIN_TEST);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        List<NavigationAction.NavigationActionItems> pageList = Arrays.asList(HOME, FAVORITES, LIBRARY, PREMIUM, PROFILE);
        pageList.forEach(barItem -> {
            navigationAction.navigateTo(barItem);
            launchSafariBrowser();
            activateApp();
            navigationAction.validateCorrespondingPageIsOpened(barItem);
            if (userProfilePage.isOnProfilePage()) {
                userProfilePage.closeProfilePage();
            }
            miniPlayerPage.validateMiniPlayerStopButtonIsDisplayed();
        });
        miniPlayerPage.extendMiniPlayer();
        launchSafariBrowser();
        activateApp();
        nowPlayingPage.validateThatNowPlayingIsOpened();
        restartApp();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        homePage.validateHomePageIsOpened();
        navigationAction.navigateTo(PROFILE);
        userProfilePage.validateUserProfileDetails(USER_FOR_LOGIN_TEST);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        terminateApp();
        nowPlayingPage.verifyAppState(NOT_RUNNING);
    }

    @Override
    public void testPremiumContent() {
        Contents[] streams = {STREAM_PREMIUM_PODCAST_JSA, STREAM_AL_JAZEERA};
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (Contents stream : streams) {
            deepLinksUtil.openTuneThroughDeeplink(stream);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .stopStreamPlaying()
                    .validateStreamIsStopped()
                    .tapOnPlayButton()
                    .validateSeekBarLooksAsExpected(stream);
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("749610"), @TestCaseId("749608")})
    @Test(description = "Check stream speed in cold state", groups = {NOW_PLAYING_TEST})
    public void testCheckStreamSpeedInColdState() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.validateSpeedAdjustmentValue(SPEED_VALUE_1_0);
        nowPlayingPage
                .adjustPlaybackSpeed(SPEED_VALUE_0_5, true)
                .validateSpeedAdjustmentValue(SPEED_VALUE_0_5);
        terminateApp();
        launchApp();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.validateSpeedAdjustmentValue(SPEED_VALUE_1_0);
    }

    @TestCaseId("749632")
    @Test(description = "Open and play Deleted Stream", groups = {NOW_PLAYING_TEST})
    public void testOpenDeletedStream() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_DELETED);
        nowPlayingPage
                .closePopUpIfDisplayed(STREAM_ERROR_POPUP)
                .validateThatNowPlayingIsOpened()
                .verifyAppState(RUNNING_IN_FOREGROUND);
    }

    @Override
    public void testSwitchingFromDownloadsToDifferentContents() {
        int downloadEpisodeIndex = 1;
        Contents[] contents = {STREAM_FREE_SHORT_PODCAST, STREAM_FREE_PODCAST_WITH_LONG_EPISODE};
        for (Contents content : contents) {
            deepLinksUtil.openContentProfileThroughDeeplink(content);
            String contentName = contentProfilePage.getContentItemDataByIndex(content.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
            contentProfilePage.tapEpisodeThreeDotsByIndex(content.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, downloadEpisodeIndex);
            episodeModalDialog.tapEpisodeDownloadButton();
            navigationAction.navigateTo(SEARCH);
            navigationAction.navigateTo(DOWNLOADS);
            downloadsPage.validateThatContentAppearedInDownloads(contentName);
        }
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        contentsListPage.clickOnContentCellByIndex(0);
        nowPlayingPage
                .validateStreamStartPlaying()
                .minimizeNowPlayingScreen();

        Contents[] streams = {STREAM_PODCAST_MARKETPLACE, STREAM_FREE_STREAM_FROM_PREMIUM_OWNED_AND_OPERATED, STREAM_STATION_WITHOUT_ADS};
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
        nowPlayingPage.validateNowPlayingSubtitleIsEqualTo(CUSTOM_URL);

        for (Contents content : contents) {
            deepLinksUtil.openContentProfileThroughDeeplink(content);
            contentProfilePage
                    .tapEpisodeThreeDotsByIndex(content.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, downloadEpisodeIndex)
                    .tapEpisodeDeleteButton();
            navigationAction.navigateTo(HOME);
        }
    }
}
