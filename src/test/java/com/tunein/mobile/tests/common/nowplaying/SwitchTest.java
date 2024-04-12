package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.GestureActionUtil;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.RECENTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_TOOLTIP_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class SwitchTest extends BaseTest {

    @TestCaseId("752163")
    @Test(description = "Check switch tool tip UI", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchToolTipUI() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog
                .waitUntilPageReady()
                .validateSwitchToolTipDialogBoxIsDisplayed();
        switchToolTipDialog.tapOnSwitchToolTipNotNowButton();
        nowPlayingPage.validateGotItPromptIsDisplayed();
    }

    @TestCaseId("752171")
    @Test(description = "Check Dismiss Switch tooltip", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDismissSwitchToolTip() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog
                .waitUntilPageReady()
                .validateUIElements(switchToolTipDialog.switchTollTipPageElements());
        switchToolTipDialog.tapOnSwitchToolTipNotNowButton();
        nowPlayingPage
                .validateGotItPromptIsDisplayed()
                .closeGotItPrompt();
        nowPlayingPage
                .waitUntilPageReady()
                .validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("752172"), @TestCaseId("752182")})
    @Test(description = "Check manual switch UI", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testManualSwitchUI() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateSwitchStationsContainerIsDisplayed();
    }

    @TestCaseId("752164")
    @Test(description = "Test tooltip should appear only once per install", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchTooltipShouldAppearOncePerInstall() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog
                .waitUntilPageReady()
                .validateUIElements(switchToolTipDialog.switchTollTipPageElements());
        switchToolTipDialog.tapOnSwitchToolTipNotNowButton();
        nowPlayingPage.closeGotItPrompt();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH_ADDITIONAL);
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsNotDisplayed();
        ArrayList<String> launchArgumentsWithUpdatedSwitchTooltip = updateDefaultLaunchArgumentList(BOOST_TOOLTIP_ENABLED, "true");
        ArrayList<String> launchArgumentsWithSwitchEnabled = updateDefaultLaunchArgumentList(BOOST_ENABLED, "true", launchArgumentsWithUpdatedSwitchTooltip);
        restartApp(launchArgumentsWithSwitchEnabled);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH_ADDITIONAL);
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsNotDisplayed();
    }

    @TestCaseId("752168")
    @Test(description = "Test tooltip warm cold state", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchTooltipWarmColdState() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog
                .waitUntilPageReady()
                .validateUIElements(switchToolTipDialog.switchTollTipPageElements());
        runAppInBackground(Duration.ofSeconds(3));
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsDisplayed();
        restartApp();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsNotDisplayed();
    }

    //TODO When switch element objects will be implemented, add verification for "Directory station should appear as focused tile in Boost swipe feature"
    @TestCaseId("752173")
    @Test(description = "Check functionality of Switch for station", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testFunctionalityOfSwitchStation() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(LEFT)
                .validateThatPreRollIsAbsent()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying()
                .swipeStationInSwitchContainer(RIGHT)
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying();
    }

    //TODO When switch element objects will be implemented, add verification for "Directory station should appear as focused tile in Boost swipe feature"
    @TestCaseId("752245")
    @Test(description = "Check functionality of changing screens during stream With switch station", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testChangingScreensDuringStreamWithSwitchStation() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .minimizeNowPlayingScreen();

        List<NavigationAction.NavigationActionItems> pageList = Arrays.asList(HOME, LIBRARY, SEARCH, PREMIUM, FAVORITES);
        pageList.forEach(barItem -> {
            navigationAction.navigateTo(barItem);
        });
        miniPlayerPage.extendMiniPlayer();
        nowPlayingPage
                .validateSwitchStationsContainerIsDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("752165")
    @Test(description = "Check Switch tooltip for different stations", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchToolTipForDifferentStations() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsNotDisplayed();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        switchToolTipDialog
                .waitUntilPageReady()
                .validateUIElements(switchToolTipDialog.switchTollTipPageElements());
    }

    @TestCaseId("752170")
    @Test(description = "Check let's switch button", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testLetsSwitchButton() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        switchToolTipDialog
                .waitUntilPageReady()
                .validateUIElements(switchToolTipDialog.switchTollTipPageElements());
        switchToolTipDialog.tapOnLetsSwitchButton();
        switchToolTipDialog.validateSwitchToolTipDialogBoxIsNotDisplayed();
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .validateThatPreRollIsAbsent()
                .swipeStationInSwitchContainer(LEFT)
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()));
    }

    @TestCaseId("752180")
    @Test(description = "Manual switch cold/warm", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testManualSwitchWarmColdState() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateSwitchStationsContainerIsDisplayed();
        nowPlayingPage
                .swipeStationInSwitchContainer(LEFT)
                .validateStreamStartPlaying();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateSwitchStationsContainerIsDisplayed()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName());
        restartApp(updateDefaultLaunchArgumentList(BOOST_ENABLED, "true"));
        upsellPage.closeUpsell();
        continueListeningDialog.tapOnContinuePlayButtonIfDisplayed();
        nowPlayingPage
                .validateSwitchStationsContainerIsDisplayed()
                .validateStreamStartPlaying();
    }

    @TestCaseId("752189")
    @Test(description = "Check Swipe or Tap to switch", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwipeAndTapNavigationOfSwitch() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateStreamStartPlaying()
                .swipeStationInSwitchContainer(LEFT)
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(RIGHT)
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .tapOnSwitchContainerStation(RIGHT)
                .validateStreamStartPlaying()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .tapOnSwitchContainerStation(LEFT)
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("752179")
    @Test(description = "Manual Switch and Premium user", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testManualSwitchAndPremiumUser() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateThatPreRollIsAbsent()
                .validateSwitchStationsContainerIsDisplayed()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(LEFT)

                .validateSwitchStationsContainerIsDisplayed()
                .validateThatPreRollIsAbsent()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(RIGHT)

                .validateSwitchStationsContainerIsDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()));
    }

    @Ignore
    @TestCaseId("752216")
    @Test(description = "Auto Switch and Autoplay", groups = {NOW_PLAYING_TEST})
    public void testAutoSwitchAndAutoplay() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        //arguments.put(BOOST_AUTOPLAY_ENABLED, "true");
        updateLaunchArguments(arguments);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()));
        //Todo Switch station should appear as focused tile in Boost swipe feature

        terminateApp();
        restartApp();
        upsellPage.closeUpsell();
        continueListeningDialog.tapOnContinuePlayButtonIfDisplayed();
        nowPlayingPage
                .validateThatPreRollIsAbsent()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @Ignore
    @TestCaseId("752217")
    @Test(description = "check Auto Switch and Alarm", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testPlayingViaAlarm() {
        int minutesAhead = 1;
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        // arguments.put(BOOST_AUTOPLAY_ENABLED, "true");
        updateLaunchArguments(arguments);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .setAlarmTimeFlow(minutesAhead);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.waitUntilAlarmGoesOff(Duration.ofMinutes(minutesAhead));
        nowPlayingPage
                .validateThatPreRollIsAbsent()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITHOUT_ADS.getStreamName())
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateSwitchStationsContainerIsDisplayed();
    }

    @TestCaseId("752182")
    @Test(description = "Open Station with Switch through deeplinks", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testOpenStationWithSwitchThroughDeeplinks() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateSwitchStationsContainerIsDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("752204")
    @Test(description = "Test manual switch and alarm", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public abstract void testManualSwitchAndAlarm();

    @TestCaseId("752186")
    @Test(description = "check Manual Switch and Sleep timer functionality", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public abstract void testManualSwitchAndSleeptimer();

    @TestCaseId("752188")
    @Test(description = "Check Manual Switch and Autoplay", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testManualSwitchAndAutoplay() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPageReady()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()));
        restartApp();
        upsellPage.closeUpsell();
        continueListeningDialog.tapOnContinuePlayButtonIfDisplayed();
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()));
        //Todo Directory station should appear as a focused tile in Boost swipe feature
    }

    @TestCaseId("753797")
    @Test(description = "Check directory and switch stations in recents after switch back", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDirectoryAndSwitchStationsInRecentsAfterSwitchBack() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .swipeStationInSwitchContainer(RIGHT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .tapOnPauseButton()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
    }

    @TestCaseId("753794")
    @Test(description = "Check Switch station is absent in recents", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testSwitchstationIsAbsentInRecents() {
        Contents content = STREAM_STATION_WITH_SWITCH;
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                .tapOnPauseButton()
                .validateStreamIsPaused()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        homePage
                .validateNumberOfElementsInHomePageCategory(RECENTS, UP, 1)
                .tapOnCategoryHeader(RECENTS, UP);
        homePage.validateThatContentAppearedInRecents(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("753607")
    @Test(description = "Switch to boost stream while pre-roll on primary is playing", groups = {NOW_PLAYING_TEST, SWITCH_TEST, ADS_TEST})
    public void testSwitchToBoostStreamWhilePrerollOnPrimaryStation() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .validateThatPreRollIsDisplayed(Duration.ofSeconds(config().waitLongTimeoutSeconds()))
                .swipeStationInSwitchContainer(LEFT)
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateThatPreRollIsAbsent()
                .validateNowPlayingAdBannerDisplayed(true);
        nowPlayingPage
                .swipeStationInSwitchContainer(RIGHT)
                .validateThatPreRollIsDisplayed(Duration.ofSeconds(config().waitLongTimeoutSeconds()))
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingAdBannerDisplayed(true)
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("753798")
    @Test(description = "Check directory and switch stations in recents after switch back. Pause button", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDirectoryAndSwitchStationsInRecentsAfterSwitchBackPauseButton() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        Contents content = STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON;
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateStreamStartPlaying()
                .tapOnStopButton()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
    }

    @TestCaseId("753799")
    @Test(description = "Check directory and switch stations in recents after we kill app", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDirectoryAndSwitchStationsInRecentsAfterKillingApp() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_SWITCH_STATION_WITH_STOP_BUTTON);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateNowPlayingTitleIsEqualTo(STREAM_SWITCH_STATION_WITH_STOP_BUTTON.getStreamName())
                .swipeStationInSwitchContainer(LEFT)
                .validateStreamStartPlaying();
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateNowplayingStationTitlesNotEqual(STREAM_SWITCH_STATION_WITH_STOP_BUTTON.getStreamName())
                .tapOnStopButton();

        restartApp(updateDefaultLaunchArgumentList(BOOST_ENABLED, "true"));
        upsellPage.closeUpsell();
        continueListeningDialog.closeContinueListeningDialogIfDisplayed();
        if (homePage.isOnHomePage()) {
            homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
        } else {
            nowPlayingPage
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .minimizeIfNowPlayingDisplayed();
            homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
        }
    }

    @TestCaseId("753800")
    @Test(description = "Check directory and switch stations in recents after we kill app Pause button", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testCheckDirectoryAndSwitchStationWithPauseButtonInRecentsAfterKillingApp() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON);
        nowPlayingPage
                .validateStreamStartPlaying()
                .validateNowPlayingTitleIsEqualTo(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON.getStreamName())
                .swipeStationInSwitchContainer(LEFT)
                .validateStreamStartPlaying();
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateNowplayingStationTitlesNotEqual(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON.getStreamName())
                .tapOnStopButton();

        restartApp(updateDefaultLaunchArgumentList(BOOST_ENABLED, "true"));
        upsellPage.closeUpsell();
        continueListeningDialog.closeContinueListeningDialogIfDisplayed();
        if (homePage.isOnHomePage()) {
            homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
        } else {
            nowPlayingPage
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .minimizeIfNowPlayingDisplayed();
            homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
        }
    }

    @TestCaseId("753606")
    @Test(description = "Switch to boost stream while primary is playing", groups = {NOW_PLAYING_TEST, SWITCH_TEST, ADS_TEST})
    public void testSwitchToBoostStreamWhilePrimaryStationPlaying() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH.getStreamTuneDeepLink());
        nowPlayingPage
                .validateThatPreRollIsDisplayed(Duration.ofSeconds(config().waitLongTimeoutSeconds()))
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingAdBannerDisplayed(true, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .validateStreamStartPlaying()
                .tapOnSwitchContainerStation(RIGHT)
                .validateThatPreRollIsAbsent()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .tapOnSwitchContainerStation(LEFT)
                .validateThatPreRollIsDisplayed(Duration.ofSeconds(config().waitLongTimeoutSeconds()))
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingAdBannerDisplayed(true, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        nowPlayingPage
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName())
                .tapOnSwitchContainerStation(RIGHT)
                .validateThatPreRollIsAbsent()
                .validateNowPlayingAdBannerDisplayed(true, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        nowPlayingPage
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH.getStreamName())
                .tapOnSwitchContainerStation(LEFT)
                .validateThatPreRollIsDisplayed(Duration.ofSeconds(config().waitLongTimeoutSeconds()))
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingAdBannerDisplayed(true, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        nowPlayingPage.validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH.getStreamName());
    }

    @TestCaseId("753795")
    @Test(description = "Directory and switch stations in recents.", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDirectoryAndSwitchStationInRecents() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITH_SWITCH.getStreamType()))
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateStreamStartPlaying()
                .tapOnStopButton()
                .validateStreamIsStopped()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
    }

    @TestCaseId("753796")
    @Test(description = "Check directory and switch stations in recents. Station with pause button.", groups = {NOW_PLAYING_TEST, SWITCH_TEST})
    public void testDirectoryAndSwitchStationWithPauseInRecents() {
        updateLaunchArgumentFor(BOOST_ENABLED, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON.getStreamType()))
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage
                .validateStreamStartPlaying()
                .tapOnStopButton()
                .validateStreamIsStopped()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 2);
    }
}
