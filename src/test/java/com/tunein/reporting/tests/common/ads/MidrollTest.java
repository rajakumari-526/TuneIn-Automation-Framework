package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_MIDROLL_REQUEST;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;
import static com.tunein.mobile.utils.ReportingUtil.getUrlFromRequestWithParameterAndValue;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class MidrollTest extends BaseTest {

    @TestCaseId("752151")
    @Test(description = "[Midroll] ADS_PLAYBACK_FINISHED", groups = {UNIFIED_EVENTS, MIDROLL_TEST})
    public abstract void testMidrollAdsPlayBackFinished();

    @TestCaseId("752152")
    @Test(description = "[Midroll] ADS_PLAYBACK_STARTED", groups = {UNIFIED_EVENTS, MIDROLL_TEST})
    public void testMidrollAdsPlayBackStarted() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_SWITCH_AND_MIDROLL) {
            nowPlayingPage.generatePrerollForStream(content);
            customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_STARTED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_STARTED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752154")
    @Test(description = "Test Mid roll ads video audio roll requested", groups = {UNIFIED_EVENTS, MIDROLL_TEST})
    public void testMidRollAdsVideoAudioRollRequestedEvent() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_SWITCH_AND_MIDROLL) {
            nowPlayingPage.generatePrerollForStream(content);
            customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
    }

    @TestCaseId("752153")
    @Test(description = "Test Mid roll ads video audio roll response received", groups = {UNIFIED_EVENTS, MIDROLL_TEST})
    public void testAdsVideoAudioRollResponseReceivedEvent() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_SWITCH_AND_MIDROLL) {
            nowPlayingPage.generatePrerollForStream(content);
            customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL, Duration.ofSeconds(config().twoMinuteInSeconds()));
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752142")
    @Test(description = "Check [Midroll] ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED event", groups = {UNIFIED_EVENTS, MIDROLL_TEST})
    public void testMidRollAdsVideoAudioRollEligibilityDecided() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_SWITCH_AND_MIDROLL) {
            nowPlayingPage.generatePrerollForStream(content);
            customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL, Duration.ofSeconds(config().twoMinuteInSeconds()));
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_ELIGIBLE, true);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752251")
    @Test(description = "[Midroll] Success", groups = {UNIFIED_EVENTS, MIDROLL_TEST, SMOKE_TEST})
    public void testMidrollFlowSuccess() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        navigationAction.navigateTo(HOME);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));

        JsonObject appSessionData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_APP_SESSION_STARTED);
        unifiedEventsAsserts.validateThatEventIsPresent(appSessionData, UNIFIED_EVENT_APP_SESSION_STARTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(appSessionData, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateUnifiedEventCommonParams(appSessionData);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfoIsEmpty(appSessionData);
        unifiedEventsAsserts.verifyUnifiedEventContextUserInfoIsEmpty(appSessionData);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(appSessionData, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(appSessionData, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(appSessionData, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(appSessionData, UNIFIED_EVENT_JSON_PARAM_DEVICEID);

        JsonObject userPlayClickedData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_USER_PLAY_CLICKED);
        unifiedEventsAsserts.validateThatEventIsPresent(userPlayClickedData, UNIFIED_EVENT_USER_PLAY_CLICKED);
        unifiedEventsAsserts.validateUnifiedEventCommonParams(userPlayClickedData);
        unifiedEventsAsserts.verifyUnifiedEventContextUserInfoIsEmpty(userPlayClickedData);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_GUIDE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_LISTEN_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_DEVICEID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(userPlayClickedData, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);

        String[] events = {UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED,
                           UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED,
                           UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED,
                           UNIFIED_EVENT_ADS_PLAYBACK_STARTED,
                           UNIFIED_EVENT_ADS_PLAYBACK_FINISHED };

        for (String event : events) {
            JsonObject data = getLastUnifiedEventRequestByType(event, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL, Duration.ofSeconds(config().twoMinuteInSeconds()));
            unifiedEventsAsserts.validateThatEventIsPresent(data, event);
            unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_MIDROLL);
            unifiedEventsAsserts.validateUnifiedEventCommonParams(data);
            unifiedEventsAsserts.verifyUnifiedEventContextUserInfoIsEmpty(data);
            unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_WITH_MIDROLL);
            unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
            unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

            switch (event) {
                case UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED -> {
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_ELIGIBLE, true);
                }
                case UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED -> {
                     unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
                     unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                     unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                     unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                     unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
                }
                case UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                }
                case UNIFIED_EVENT_ADS_PLAYBACK_STARTED, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
                }
                default -> throw new IllegalStateException("Unexpected event: " + event);
            }
        }
    }

    @TestCaseId("753470")
    @Test(description = "Switch during midroll", groups = {NOW_PLAYING_TEST, SWITCH_TEST, PREROLL_TEST, MIDROLL_TEST})
    public abstract void testSwitchDuringMidRoll();

    @TestCaseId("750451")
    @Test(description = "[Ads] Controls during midroll", groups = {ADS_TEST, MIDROLL_TEST})
    public abstract void testAdsControlsDuringMidroll();

    @TestCaseId("753609")
    @Test(description = "Test switch to boost stream during midroll", groups = {ADS_TEST, SWITCH_TEST, ADS_PARAMETERS})
    public void testSwitchToBoostStreamDuringMidroll() {
        String requiredURL;
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_ENABLED, "true");
        arguments.put(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        updateLaunchArguments(arguments);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_WITH_SWITCH_AND_MIDROLL.getStreamTuneDeepLink());
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingTitleIsEqualTo(STREAM_STATION_WITH_SWITCH_AND_MIDROLL.getStreamName());
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        requiredURL = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_MIDROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL));
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        nowPlayingPage.swipeStationInSwitchContainer(LEFT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage.validateThatPreRollIsAbsent();
        nowPlayingPage.swipeStationInSwitchContainer(RIGHT);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        requiredURL = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_MIDROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_SWITCH_AND_MIDROLL));
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }

    @TestCaseId("753608")
    @Test(description = "Switch to boost stream after midroll", groups = {ADS_TEST, MIDROLL_TEST, SWITCH_TEST})
    public abstract void testSwitchToBoostStreamAfterMidroll();

    @TestCaseId("753471")
    @Test(description = "Test Midroll available after switch", groups = {ADS_TEST, SWITCH_TEST, ADS_PARAMETERS})
    public abstract void testMidrollAvailableAfterSwitch();

}
