package com.tunein.reporting.tests.android.ads;

import com.google.gson.JsonObject;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import com.tunein.reporting.tests.common.ads.MidrollTest;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_MIDROLL_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_PREROLL_REQUEST;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidMidrollTest extends MidrollTest {

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
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
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
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURLDuringPreroll = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_PREROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL));
        reportingAsserts.validateThatUrlIsPresent(requiredURLDuringPreroll);
        nowPlayingPage.validateSwitchStationsContainerIsDisplayed();

        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        String requiredURLDuringMidroll = getUrlFromRequestWithParameterAndValue(
                ADS_WIZZ_MIDROLL_REQUEST,
                ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID,
                getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL),
                Duration.ofSeconds(config().oneMinuteInSeconds())
        );
        reportingAsserts.validateThatUrlIsPresent(requiredURLDuringMidroll);
        nowPlayingPage.validateSwitchStationsContainerIsDisplayed();
    }

    @Override
    public void testAdsControlsDuringMidroll() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().oneMinuteInSeconds()));
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        String requiredURL = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_MIDROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_PREROLL));
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        nowPlayingPage
                .validateRewindAndFastForwardButtonsAreNotDisplayed()
                .validatePauseButtonDisplayed();
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validatePauseButtonDisplayed()
                .validateRewindAndFastForwardButtonsAreDisplayed();
    }

    @Override
    public void testSwitchToBoostStreamAfterMidroll() {
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);

        String requiredMidrollURLDirectoryStation = null;
        for (Contents content : STATIONS_WITH_SWITCH_AND_MIDROLL) {
            nowPlayingPage.generatePrerollForStream(content);
            customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
            requiredMidrollURLDirectoryStation = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_MIDROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(content));
            if (requiredMidrollURLDirectoryStation != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredMidrollURLDirectoryStation);

        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        List<String> requiredMidrollURLBoostStation = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        assertThat(requiredMidrollURLBoostStation.size() == 1).as("Midroll URLs are generated for boost station").isTrue();

        nowPlayingPage.swipeStationInSwitchContainer(RIGHT);
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        List<String> requiredMidrollURLDirectoryStationAfterSwitchBack = getUrlsFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        assertThat(requiredMidrollURLDirectoryStationAfterSwitchBack.size() > 1).as("Midroll URLs are not greater than two").isTrue();
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
        String requiredURL = getUrlFromRequestWithParameterAndValue(
                ADS_WIZZ_MIDROLL_REQUEST,
                ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID,
                getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL),
                Duration.ofSeconds(config().oneMinuteInSeconds())
        );
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }
}
