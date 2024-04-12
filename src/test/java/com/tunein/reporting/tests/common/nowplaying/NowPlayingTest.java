package com.tunein.reporting.tests.common.nowplaying;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class NowPlayingTest extends BaseTest {

    @TestCaseId("752072")
    @Test(description = "Check USER_PLAY_CLICKED event", groups = {UNIFIED_EVENTS, NOW_PLAYING_TEST})
    public void testUserPlayClickedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_LA_JEFA);
        customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_USER_PLAY_CLICKED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_USER_PLAY_CLICKED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_LA_JEFA);
    }

    @TestCaseId("752100")
    @Test(description = "Test LISTEN_SESSION_STARTED event", groups = {UNIFIED_EVENTS, NOW_PLAYING_TEST})
    public void testListenSessionStartedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_LISTEN_SESSION_STARTED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_LISTEN_SESSION_STARTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfo(data, STREAM_STATION_WITHOUT_ADS);
    }

}
