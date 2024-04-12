package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_ADSWIZZ_INSTREAM_AD;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class AdsTest extends BaseTest {

    @TestCaseId("752079")
    @Test(description = "Check ADS_DISPLAY_CLICKED event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsClickedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage.tapOnNowPlayingAdBanner();
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_CLICKED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_CLICKED);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
    }

    @TestCaseId("752073")
    @Test(description = "Check ADS_DISPLAY_REQUESTED event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public abstract void testAdsRequestEvent();

    @TestCaseId("752074")
    @Test(description = "Check ADS_DISPLAY_RESPONSE_RECEIVED event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsResponseEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_WITHOUT_ADS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);

        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_LATENCY_MSECS);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_TEST_NAME, CONTROL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_NAME, DEFAULT_WATERFALL);

        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
    }

    @TestCaseId("752074")
    @Test(description = "Check ADS_DISPLAY_RESPONSE_RECEIVED event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsDisplayResponseReceivedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_LATENCY_MSECS);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_TEST_NAME, CONTROL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_NAME, DEFAULT_WATERFALL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);

        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
    }

    @TestCaseId("752075")
    @Test(description = "Check ADS_DISPLAY_IMPRESSION event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsImpressionEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        customWait(Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_LATENCY_MSECS);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_TEST_NAME, WATERFALL_TEST_NAME_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_NAME, DEFAULT_WATERFALL);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_KQED);
        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
    }

    @TestCaseId("752076")
    @Test(description = "Check ADS_DISPLAY_VIEWABILITY_STATUS event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsDisplayViewabilityEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(10));
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_VIEWABLE, true);
        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
    }

    @TestCaseId("752103")
    @Test(description = "Test Ads in stream quartile status", groups = {UNIFIED_EVENTS, INSTREAM_TEST})
    public void testAdsInStreamQuartileStatus() {
        JsonObject data = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_INSTREAM_QUARTILE_STATUS);
            if (data != null) break;
        }
        List<String> quartileStatus = Arrays.asList(QUARTILE_FIRST, QUARTILE_MIDPOINT, QUARTILE_THIRD);

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_INSTREAM_QUARTILE_STATUS);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, DFP);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_QUARTILE, quartileStatus);
    }

    @TestCaseId("752102")
    @Test(description = "Test ADS_INSTREAM_COMPLETED", groups = {UNIFIED_EVENTS, INSTREAM_TEST})
    public void testAdsInStreamCompleted() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_IMA_VIDEO);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_INSTREAM_COMPLETED, Duration.ofSeconds(config().tenMinutesInSeconds()));

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_INSTREAM_COMPLETED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, DFP);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
    }

    @TestCaseId("752140")
    @Test(description = "Check ADS_DISPLAY_VIEWABILITY_STATUS for Instream event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsDisplayViewabilityStatusForInstreamEvent() {
        JsonObject data = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (data != null) break;
        }

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, DFP);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD, true);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_VIEWABLE, true);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
    }

    @TestCaseId("752141")
    @Test(description = "Test In stream Ads display clicked", groups = {UNIFIED_EVENTS, INSTREAM_TEST})
    public abstract void testAdsDisplayClicked();

    @TestCaseId("752104")
    @Test(description = "Check ADS_INSTREAM_STARTED event", groups = {UNIFIED_EVENTS})
    public void testAdsInstreamStartedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_IMA_VIDEO);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_INSTREAM_STARTED, Duration.ofSeconds(config().tenMinutesInSeconds()));
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_INSTREAM_STARTED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, DFP);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
    }

    @TestCaseId("752105")
    @Test(description = "Check ADS_INSTREAM_RECEIVED event", groups = {UNIFIED_EVENTS, INSTREAM_TEST})
    public void testAdsInstreamReceivedEvent() {
        JsonObject data = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(
                    UNIFIED_EVENT_ADS_INSTREAM_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_SLOT,
                    AD_SLOT_INSTREAM,
                    Duration.ofSeconds(config().oneMinuteInSeconds())
            );
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_INSTREAM_RECEIVED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, DFP);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752137")
    @Test(description = "Check ADS_DISPLAY_IMPRESSION event", groups = {UNIFIED_EVENTS, INSTREAM_TEST})
    public abstract void testAdsDisplayImpressionEvent();

    @TestCaseId("752107")
    @Test(description = "Check ADS_DISPLAY_CERTIFIED_IMPRESSION event", groups = {UNIFIED_EVENTS, ADS_TEST})
    public void testAdsDisplayCertifiedImpressionEvent() {
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_CERTIFIED_IMPRESSION, Duration.ofSeconds(config().oneMinuteInSeconds()));
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_CERTIFIED_IMPRESSION);

        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_LATENCY_MSECS);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_TEST_NAME, WATERFALL_TEST_NAME_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_WATERFALL_NAME, DEFAULT_WATERFALL);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_320_50);

        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_REVENUE);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_REVENUE_PRECISION, AD_REVENUE_PRECISION_PRECISE_LIST);
        unifiedEventsAsserts.validateParameterStateAccordingToDependentParameter(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
    }

    @TestCaseId("752252")
    @Test(description = "Test in stream flow success events ", groups = {UNIFIED_EVENTS, INSTREAM_TEST, SMOKE_TEST})
    public abstract void testInStreamFlowSuccessEvents();

    @Issue("DROID-16579")
    @TestCaseId("752250")
    @Test(description = "Check [Display] Success Flow", groups = {UNIFIED_EVENTS, ADS_TEST, SMOKE_TEST})
    public abstract void testDisplaySuccessFlow();

}
