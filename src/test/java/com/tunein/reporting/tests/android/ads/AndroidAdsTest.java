package com.tunein.reporting.tests.android.ads;

import com.google.gson.JsonObject;
import com.tunein.reporting.tests.common.ads.AdsTest;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class AndroidAdsTest extends AdsTest {

    @Override
    public void testAdsRequestEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_REQUESTED, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMATS_ACCEPTED, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_REQUESTED);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_ADSWIZZ_AD);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);

        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
        unifiedEventsAsserts.validateThatParameterContainsValue(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMATS_ACCEPTED, AD_DISPLAY_FORMAT_300_250);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD, false);
    }

    @Override
    public void testAdsDisplayClicked() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        nowPlayingPage.tapOnNowPlayingAdBanner();
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_CLICKED);
        List<String> list = Arrays.asList(AD_SLOT_INSTREAM, AD_SLOT_DISPLAY);

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_CLICKED);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, list);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_DESTINATION_URL);
    }

    @Override
    public void testAdsDisplayImpressionEvent() {
        JsonObject data = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);

        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
    }

    @Override
    public void testInStreamFlowSuccessEvents() {
        String[] events = {
                UNIFIED_EVENT_APP_SESSION_STARTED, UNIFIED_EVENT_USER_PLAY_CLICKED,
                UNIFIED_EVENT_LISTEN_SESSION_STARTED, UNIFIED_EVENT_ADS_INSTREAM_RECEIVED,
                UNIFIED_EVENT_ADS_INSTREAM_STARTED, UNIFIED_EVENT_ADS_INSTREAM_QUARTILE_STATUS,
                UNIFIED_EVENT_ADS_INSTREAM_COMPLETED
        };
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_PREMIUM_NEWS_MSNBC);
            JsonObject instreamData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_INSTREAM_STARTED, Duration.ofSeconds(config().tenMinutesInSeconds()));
            if (instreamData != null) break;
        }
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        for (String event : events) {
            JsonObject data = getLastUnifiedEventRequestByType(event);
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
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_DEVICEID);
                }
                case UNIFIED_EVENT_ADS_INSTREAM_RECEIVED, UNIFIED_EVENT_ADS_INSTREAM_STARTED, UNIFIED_EVENT_ADS_INSTREAM_COMPLETED -> {
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_PREMIUM_NEWS_MSNBC);
                }
                case UNIFIED_EVENT_ADS_INSTREAM_QUARTILE_STATUS -> {
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_LIST);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_INSTREAM);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_QUARTILE, QUARTILE_STATUS_LIST);
                    unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_PREMIUM_NEWS_MSNBC);
                }
                default -> throw new IllegalStateException("Unexpected event: " + event);
            }
        }
    }

    @Override
    public void testDisplaySuccessFlow() {
        navigationAction.navigateTo(HOME);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_APP_SESSION_STARTED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_APP_SESSION_STARTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateUnifiedEventCommonParams(data);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfoIsEmpty(data);

        String[] events = {UNIFIED_EVENT_ADS_DISPLAY_REQUESTED, UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED, UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION, UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS, UNIFIED_EVENT_ADS_DISPLAY_CERTIFIED_IMPRESSION};

        for (String event : events) {
            navigationAction.navigateTo(HOME);
            data = getLastUnifiedEventRequestByType(event);
            unifiedEventsAsserts.validateThatEventIsPresent(data, event);

            unifiedEventsAsserts.validateUnifiedEventCommonParams(data);
            unifiedEventsAsserts.verifyUnifiedEventContextListeningInfoIsEmpty(data);
            unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_DISPLAY);
            unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_DISPLAY);
            unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, AD_NETWORK_NAME_LIST);
            unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_TYPE, UNIFIED_EVENT_TYPE);

            switch (event) {
                case UNIFIED_EVENT_ADS_DISPLAY_REQUESTED -> {
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD, false);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                    unifiedEventsAsserts.validateThatParameterContainsValue(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMATS_ACCEPTED, AD_DISPLAY_FORMAT_320_50);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);

                }
                case UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED -> {
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD, false);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_320_50);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);

                }
                case UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_320_50);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
                }
                case UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_320_50);
                }
                case UNIFIED_EVENT_ADS_DISPLAY_CERTIFIED_IMPRESSION -> {
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
                    unifiedEventsAsserts.validateThatParameterValueInTheList(data, UNIFIED_EVENT_JSON_PARAM_REVENUE_PRECISION, AD_REVENUE_PRECISION_PRECISE_LIST);
                    unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_REVENUE);
                    unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT, AD_DISPLAY_FORMAT_320_50);
                }
                default -> throw new IllegalStateException("Unexpected event: " + event);
            }
        }
    }

}
