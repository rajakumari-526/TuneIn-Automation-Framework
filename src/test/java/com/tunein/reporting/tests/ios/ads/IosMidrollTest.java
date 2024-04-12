package com.tunein.reporting.tests.ios.ads;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import com.tunein.mobile.utils.ReporterUtil;
import com.tunein.reporting.tests.common.ads.MidrollTest;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_MIDROLL_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_PREROLL_REQUEST;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static org.assertj.core.api.Assertions.assertThat;

public class IosMidrollTest extends MidrollTest {

    @Override
    public void testMidrollAdsPlayBackFinished() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        navigationAction.navigateTo(HOME);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_FINISHED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsNotPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @Override
    public void testSwitchDuringMidRoll() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH_AND_MIDROLL.getStreamTuneDeepLink());
        customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        String requiredURLDuringPreroll = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_PREROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL));
        reportingAsserts.validateThatUrlIsPresent(requiredURLDuringPreroll);
        nowPlayingPage.validateSwitchStationsContainerIsDisplayed();

        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        List<String> requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        long start = System.currentTimeMillis();
        while (requiredURLList.size() <= 1) {
            requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            if (System.currentTimeMillis() - start > config().twoMinutesInMilliseconds()) {
                ReporterUtil.log("Midroll is absent during " + config().waitShortTimeoutMilliseconds() + " ms ");
                break;
            }
        }
        String requiredURL = (requiredURLList.size() > 1) ? Iterables.getLast(requiredURLList) : null;
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }

    @Override
    public void testAdsControlsDuringMidroll() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().oneMinuteInSeconds()));
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        nowPlayingPage
                .validateStreamStartPlaying()
                .waitUntilPreRollAdDisappearIfDisplayed();
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        List<String> requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        String requiredURL = (requiredURLList.size() > 1) ? Iterables.getLast(requiredURLList) : null;
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        nowPlayingPage.validatePauseButtonDisplayed();
        scroll(DOWN, 2);
        nowPlayingPage
                .validatePauseButtonDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validatePauseButtonDisplayed();
        scroll(DOWN, 2);
        nowPlayingPage.validateNowPlayingProfileCardIsDisplayed();
    }

    @Override
    public void testSwitchToBoostStreamAfterMidroll() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);

        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_SWITCH_AND_MIDROLL);
        List<String> requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        long start = System.currentTimeMillis();
        while (requiredURLList.size() <= 1) {
            requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            if (System.currentTimeMillis() - start > config().twoMinutesInMilliseconds()) {
                ReporterUtil.log("Midroll is absent during " + config().waitShortTimeoutMilliseconds() + " ms ");
                break;
            }
        }
        String requiredURL = (requiredURLList.size() > 1) ? Iterables.getLast(requiredURLList) : null;
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        List<String> requiredMidrollURLsBoostStation = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        assertThat(requiredMidrollURLsBoostStation.size() > 2).as("Midroll URLs are generated for boost station").isFalse();

        nowPlayingPage.swipeStationInSwitchContainer(RIGHT);
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        List<String> requiredMidrollURLDirectoryStation = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        while (requiredMidrollURLDirectoryStation.size() <= 1) {
            requiredMidrollURLDirectoryStation = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            if (System.currentTimeMillis() - start > config().twoMinutesInMilliseconds()) {
                ReporterUtil.log("Midroll is absent during " + config().waitShortTimeoutMilliseconds() + " ms ");
                break;
            }
        }
        String latestRequiredURL = (requiredMidrollURLDirectoryStation.size() > 2) ? Iterables.getLast(requiredMidrollURLDirectoryStation) : null;
        reportingAsserts.validateThatUrlIsPresent(latestRequiredURL);
    }

    @Override
    public void testMidrollAvailableAfterSwitch() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        arguments.put(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH_AND_MIDROLL);
        customWait(Duration.ofSeconds(config().waitTwentySecondsTimeout()));

        nowPlayingPage
                .swipeStationInSwitchContainer(LEFT)
                .validateStreamStartPlaying()
                .validateNowplayingStationTitlesNotEqual(STREAM_STATION_WITH_SWITCH_AND_MIDROLL.getStreamName())
                .swipeStationInSwitchContainer(RIGHT)
                .waitUntilPreRollAdDisappearIfDisplayed();
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));

        List<String> requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        long start = System.currentTimeMillis();
        while (requiredURLList.size() <= 1) {
            requiredURLList = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            if (System.currentTimeMillis() - start > config().twoMinutesInMilliseconds()) {
                ReporterUtil.log("Midroll is absent during " + config().waitShortTimeoutMilliseconds() + " ms ");
                break;
            }
        }
        String requiredURL = (requiredURLList.size() > 1) ? Iterables.getLast(requiredURLList) : null;
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }
}
