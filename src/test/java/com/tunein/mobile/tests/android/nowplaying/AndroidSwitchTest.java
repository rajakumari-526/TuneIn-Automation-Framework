package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog;
import com.tunein.mobile.tests.common.nowplaying.SwitchTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isReleaseTestRun;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.*;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.TestGroupName.SWITCH_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToRefresh;

public class AndroidSwitchTest extends SwitchTest {

    @Override
    public void testManualSwitchAndSleeptimer() {
        NowPlayingSleepTimerDialog.SleepTimerOptions sleepTimeout = isReleaseTestRun() ? FIFTEEN : ONE;
        int timeout = isReleaseTestRun() ? config().fifteenMinutesInSeconds() : config().oneMinuteInSeconds();
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .openSleepTimerDialogThroughIcon();

        nowPlayingSleepTimerDialog.setRequiredSleepTimerOptionLite(sleepTimeout);
        nowPlayingPage.validateEditSleepTimerIconDisplayed();
        customWait(Duration.ofSeconds(timeout));
        nowPlayingPage.validateStreamIsStopped();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        switchToolTipDialog.clickOnNotNowButtonIfDisplayed();
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateSwitchStationsContainerIsDisplayed();
    }

    @Override
    public void testManualSwitchAndAlarm() {
        int minutesAhead = 2;
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .setAlarmTimeFlow(minutesAhead);
        String stationTitle = nowPlayingPage.getTextValueOfNowPlayingTitle();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.waitUntilAlarmGoesOff(Duration.ofMinutes(minutesAhead));
        nowPlayingAlarmClockDialog
                    .validateAlarmClockDialogStationTitle(stationTitle)
                    .tapOnCloseButton();
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITHOUT_ADS.getStreamName())
                .validateSwitchStationsContainerIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("752162"), @TestCaseId("752160")})
    @Test(description = "Check switch indicator on different screens", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchIndicatorOnDifferentScreens() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(SEARCH);
        searchPage
                .typeSearchText(STREAM_STATION_WITH_SWITCH.getStreamName())
                .verifySwitchIndicatorForStation(STREAM_STATION_WITH_SWITCH.getStreamName());
        searchPage.tapOnStationSearchResultWithName(STREAM_STATION_WITH_SWITCH.getStreamName());
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .verifySwitchIndicatorForStation(STREAM_STATION_WITH_SWITCH.getStreamName());
        navigationAction.navigateTo(LIBRARY);
        libraryPage.verifySwitchIndicatorForStation(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

}
