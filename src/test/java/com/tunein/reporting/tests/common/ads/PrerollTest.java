package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.BannerEventConstants.MAX_BANNER_REQUEST_JSON_STATION_ID;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.*;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.IMAEventConstants.MOBILE_PREROLL_VIDEO;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.NOWPLAYING_CALL;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;
import static com.tunein.mobile.utils.ReportingUtil.getUrlFromRequest;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class PrerollTest extends BaseTest {

    @TestCaseId("752063")
    @Test(description = "Check [IMA] ADS_VIDEO_AUDIO_ROLL_REQUESTED event", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testAdsVideoAudioRollRequestedEvent() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_IMA_VIDEO);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, IMA_VIDEO);

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, IMA_VIDEO);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID, MOBILE_PREROLL_VIDEO);
    }

    @TestCaseId("752080")
    @Test(description = "Check [IMA] ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED event", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public abstract void testImaAdsVideoAudioRollEligibilityDecidedEvent();

    @TestCaseId("752069")
    @Test(description = "Check [IMA] ADS_PLAYBACK_STARTED event", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testAdsPlayBackStartedEventForIMAAudio() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_IMA_AUDIO_AD);

        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_STARTED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_STARTED);


        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
    }

    @TestCaseId("752064")
    @Test(description = "Check [IMA] ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED event", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testImaAdsVideoAudioRollResponseReceivedEvent() {
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, IMA_VIDEO);
            if (data != null) break;
        }

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, IMA_VIDEO);

        unifiedEventsAsserts.validateThatContextParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_DEVICEID);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
    }

    @TestCaseId("752065")
    @Test(description = "Check [IMA] ADS_PLAYBACK_STARTED event for IMA Video", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testAdsPlayBackStartedEventForIMAVideo() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_IMA_VIDEO);

        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_STARTED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_STARTED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

    }

    @TestCaseId("752066")
    @Test(description = "Check [IMA] ADS_PLAYBACK_FINISHED event for IMA Video", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testAdsPlayBackFinishedForIMAVideo() {
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_FINISHED, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_VIDEO);
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_VIDEO);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
    }

    @TestCaseId("752070")
    @Test(description = "Check [IMA] ADS_PLAYBACK_FINISHED event", groups = {UNIFIED_EVENTS, PREROLL_TEST})
    public void testAdsPlayBackFinishedEventForIMAAudio() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_IMA_AUDIO_AD);

        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_FINISHED);

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
    }

    @TestCaseId("752247")
    @Test(description = "Pre-roll flow", groups = {UNIFIED_EVENTS, PREROLL_TEST, SMOKE_TEST})
    public void testPrerollFlow() {
        String[] events = {UNIFIED_EVENT_APP_SESSION_STARTED,
                UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED,
                UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED, UNIFIED_EVENT_ADS_PLAYBACK_STARTED, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED, UNIFIED_EVENT_USER_PLAY_CLICKED, UNIFIED_EVENT_LISTEN_SESSION_STARTED};
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        for (String event : events) {
            JsonObject data;
            if (event.equals(UNIFIED_EVENT_APP_SESSION_STARTED)) {
                data = getLastUnifiedEventRequestByType(event);
            } else {
                data = getLastUnifiedEventRequestByType(event, UNIFIED_EVENT_JSON_PARAM_GUIDE_ID, getStreamId(STREAM_STATION_ADSWIZZ_AD));
            }
            unifiedEventsAsserts.validateThatEventIsPresent(data, event);
            unifiedEventsAsserts.validateUnifiedEventCommonParams(data);
            unifiedEventsAsserts.verifyUnifiedEventContextUserInfoIsEmpty(data);
            unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
            unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
            unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_TYPE, UNIFIED_EVENT_TYPE);
            switch (event) {
                case UNIFIED_EVENT_APP_SESSION_STARTED -> {
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfoIsEmpty(data);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_DEVICEID);
                }
                case UNIFIED_EVENT_USER_PLAY_CLICKED, UNIFIED_EVENT_LISTEN_SESSION_STARTED -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_DEVICEID);
                }
                case UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED -> {
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
                }
                case UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED -> {
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
                }
                case UNIFIED_EVENT_ADS_PLAYBACK_STARTED, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED -> {
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
                    unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
                }
                default -> throw new IllegalStateException("Unexpected event: " + event);
            }
        }
    }

    @TestCaseId("750446")
    @Test(description = "Test topic from podcast ads explicitly disabled", groups = {PREROLL_TEST, ADS_PARAMETERS})
    public void testEpisodeFromPodcastAdsExplicitlyDisabled() {
        String requiredURL = null;
        nowPlayingPage.generatePrerollForStream(STREAM_PODCAST_ADS_EXPLICITLY_DISABLED);
        nowPlayingPage.validateStreamStartPlaying();
        requiredURL = getUrlFromRequest(IMA_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
        requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
        requiredURL = getUrlFromRequestWithParameterAndValue(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_STATION_ID, getStreamId(STREAM_PODCAST_ADS_EXPLICITLY_DISABLED));
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
    }

    @TestCaseId("750444")
    @Test(description = "Play 'Podcast Ads Explicitly Enabled' topic", groups = {ADS_TEST, PREROLL_TEST, ADS_PARAMETERS})
    public void testPlayPodcastAdsExplicitlyEnabledTopic() {
        nowPlayingPage.generatePrerollForStream(STREAM_PODCAST_ADS_EXPLICITLY_ENABLED);
        String requiredURL = getUrlFromRequest(IMA_REQUEST, Duration.ofSeconds(config().oneMinuteInSeconds()));
        if (requiredURL == null) {
            requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST, Duration.ofSeconds(config().oneMinuteInSeconds()));
        }
        String requiredBannerURL = getUrlFromRequest(MAX_BANNER_REQUEST, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatUrlIsPresent(requiredBannerURL);
        nowPlayingPage.validateNowPlayingAdBannerDisplayed(true);
    }

    @TestCaseId("750445")
    @Test(description = "Play downloaded topic", groups = {ADS_TEST, PREROLL_TEST, ADS_PARAMETERS})
    public abstract void testPlayDownloadedTopic();

    @TestCaseId("750415")
    @Test(description = "Check Video Preroll Launch station with Video", groups = {ADS_TEST, PREROLL_TEST, ADS_PARAMETERS})
    public abstract void testLaunchStationWithVideoPreroll();

    @TestCaseId("750414")
    @Test(description = "Check IMA Video Preroll launch station", groups = {ADS_TEST, PREROLL_TEST, IMA_TEST, ADS_PARAMETERS})
    public void testIMAVideoPrerollLaunchStation() {
        JsonObject requiredResponse = null;
        Contents[] contents = {STREAM_STATION_BBC_RADIO_5, STREAM_STATION_CLASSIC_ROCK_HITS, STREAM_STATION_NON_PUBLIC};
        for (Contents content : contents) {
            for (int i = 0; i < 3; i++) {
                nowPlayingPage.generatePrerollForStream(content);
                requiredResponse = getResponseFromRequest(NOWPLAYING_CALL, Duration.ofSeconds(config().waitShortTimeoutSeconds()), getStreamId(content));
                if (requiredResponse != null) break;
            }
            reportingAsserts.validateThatDataIsPresent(requiredResponse, NOWPLAYING_CALL);
            JsonObject ads = requiredResponse.getAsJsonObject(IMA_EVENT_REQUEST_JSON_ADS);
            if (content.equals(STREAM_STATION_BBC_RADIO_5)) {
                reportingAsserts.validateThatAdsParameterIsEqualTo(ads, IMA_EVENT_REQUEST_CAN_SHOW_VIDEO_PREROLL_ADS, "false");
            } else {
                reportingAsserts.validateThatAdsParameterIsEqualTo(ads, IMA_EVENT_REQUEST_CAN_SHOW_VIDEO_PREROLL_ADS, "true");
            }
        }
    }
  
    @TestCaseId("750427")
    @Test(description = "Check [IMA] media controls during preroll", groups = {IMA_TEST, ADS_TEST, ADS_PARAMETERS, PREROLL_TEST})
    public abstract void testMediaControlsForIMAPreroll();

    @TestCaseId("750434")
    @Test(description = "IMA Preroll - Disable Audio Preroll", groups = {ADS_TEST, PREROLL_TEST, ADS_PARAMETERS})
    public void testImaPrerollDisableAudioPreroll() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_DISABLED_PREROLLS);
        nowPlayingPage.validateNowPlayingAdBannerDisplayed(true);

        JsonObject requiredResponse = getResponseFromRequest(NOWPLAYING_CALL, Duration.ofSeconds(config().waitShortTimeoutSeconds()), getStreamId(STREAM_STATION_WITH_DISABLED_PREROLLS));
        JsonObject ads = requiredResponse.getAsJsonObject(IMA_EVENT_REQUEST_JSON_ADS);
        reportingAsserts.validateThatAdsParameterIsEqualTo(ads, IMA_EVENT_REQUEST_CAN_SHOW_VIDEO_PREROLL_ADS, "false");
        reportingAsserts.validateThatAdsParameterIsEqualTo(ads, IMA_EVENT_REQUEST_CAN_SHOW_PREROLL_ADS, "false");

        String requiredURLForBanner = getUrlFromRequest(MAX_BANNER_REQUEST, Duration.ofSeconds(config().twoMinuteInSeconds()));
        reportingAsserts.validateThatUrlIsPresent(requiredURLForBanner);
    }

}

