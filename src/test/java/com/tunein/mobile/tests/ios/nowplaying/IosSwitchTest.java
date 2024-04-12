package com.tunein.mobile.tests.ios.nowplaying;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog;
import com.tunein.mobile.tests.common.nowplaying.SwitchTest;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isReleaseTestRun;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.FIFTEEN;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.ONE;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.TestGroupName.SWITCH_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITH_SWITCH;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class IosSwitchTest extends SwitchTest {

    @TestCaseId("752214")
    @Test(description = "Check Open station through deeplink when auto Switch is enabled", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testOpenStationThroughDeeplinkWhenAutoSwitchIsEnabled() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        //arguments.put(BOOST_AUTOPLAY_ENABLED, "true");
        updateLaunchArguments(arguments);
        restartApp();
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateStreamStartPlaying()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("752213")
    @Test(description = "Check functionality of auto switch for premium user", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testFunctionalityOfAutoSwitchWithPremiumUser() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        //arguments.put(BOOST_AUTOPLAY_ENABLED, "true");
        updateLaunchArguments(arguments);
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateThatPreRollIsAbsent()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(LEFT)
                .validateThatPreRollIsAbsent()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(RIGHT)
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    //Todo Boost station should appear as focused tile in Boost swipe feature
    @Ignore
    @TestCaseId("752211")
    @Test(description = "Open station when Auto Switch is enabled", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testOpenStationWhenAutoSwitchIsEnabled() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        //arguments.put(BOOST_AUTOPLAY_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .swipeStationInSwitchContainer(LEFT)
                .validateThatPreRollIsAbsent()
                .validateStreamStartPlaying()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(RIGHT)
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

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
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.waitUntilAlarmGoesOff(Duration.ofMinutes(minutesAhead));
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITHOUT_ADS.getStreamName())
                .validateSwitchStationsContainerIsDisplayed();
    }

}
