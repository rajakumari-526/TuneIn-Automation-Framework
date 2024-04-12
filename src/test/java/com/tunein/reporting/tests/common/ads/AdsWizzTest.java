package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.*;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.ApplicationUtil.terminateApp;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AdsWizzTest extends BaseTest {

    @BeforeClass(groups = {ADS_WIZZ_TEST}, alwaysRun = true)
    public synchronized void beforePrerollTests() {
        blockImaAds();
    }

    @AfterClass(groups = {ADS_WIZZ_TEST}, alwaysRun = true)
    public synchronized void afterPrerollTests() {
        unblockImaAds();
    }

    @TestCaseId("752085")
    @Test(description = "[Adswizz] ADS_PLAYBACK_FINISHED", groups = {UNIFIED_EVENTS, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdsPlayBackFinishedForStation() {
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_FINISHED, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_FINISHED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752084")
    @Test(description = "[Adswizz] ADS_PLAYBACK_STARTED", groups = {UNIFIED_EVENTS, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdsPlayBackStarted() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().fourMinutesInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_PLAYBACK_STARTED, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
            if (data != null) break;
        }

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_PLAYBACK_STARTED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsPresent(data, UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @TestCaseId("752082")
    @Test(description = "Check [Adswizz] ADS_VIDEO_AUDIO_ROLL_REQUESTED event", groups = {UNIFIED_EVENTS, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdswizzAdsVideoAudioRollRequestedEvent() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().fourMinutesInSeconds()));
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
            if (data != null) break;
        }

        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_TYPE, AD_TYPE_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID);
        unifiedEventsAsserts.validateThatParameterValueIsInteger(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterValueIsGreaterThanZero(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
    }

    @TestCaseId("752083")
    @Test(description = "Check [Adswizz] ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED event", groups = {UNIFIED_EVENTS, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdswizzAdsVideoAudioRollResponseReceivedEvent() {
        JsonObject data = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
            if (data != null) break;
        }
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME, ADSWIZZ_AUDIO);
        unifiedEventsAsserts.validateThatParameterIsEqualTo(data, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID);
    }

    @Issue("DROID-16455")
    @TestCaseIds({
            @TestCaseId("752448"), @TestCaseId("752449"), @TestCaseId("752450"), @TestCaseId("752482"), @TestCaseId("752455"),
            @TestCaseId("752471"), @TestCaseId("752472"), @TestCaseId("752473"), @TestCaseId("752474"), @TestCaseId("752475"),
            @TestCaseId("752476"), @TestCaseId("752477"), @TestCaseId("752478"), @TestCaseId("753065"), @TestCaseId("752479"),
            @TestCaseId("752480"), @TestCaseId("752481")
    })
    @Test(description = "Check Adswizz Preroll- Direct Sold Targeting Params", groups = {ADS_PARAMETERS, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdswizzPreRollDirectSoldTargetingParams() {
        String requiredURL = null;
        Contents requiredContent = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
            requiredContent = content;
            if (requiredURL != null) break;
        }

        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ADS_PARTNER_ALIAS, AW_0_1ST_ADS_PARTNER_ALIAS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_SCREEN, NOWPLAYING);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_LANGUAGE, EN);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_FAMILY, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_ON_DEMAND, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(requiredContent));
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_LISTING_ID, getStreamId(requiredContent));

        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_MATURE);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_STATION_LANGUAGE);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_PERSONA);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_EVENT);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_VERSION);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PLAYER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_GENRE_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_PARTNER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_NEW_USER);
    }

    @Issue("DROID-16455")
    @TestCaseIds({
            @TestCaseId("753064"), @TestCaseId("753068"), @TestCaseId("753069"), @TestCaseId("753070"),
            @TestCaseId("753071"), @TestCaseId("753072"), @TestCaseId("753073"), @TestCaseId("753074"),
            @TestCaseId("753075"), @TestCaseId("753076"), @TestCaseId("753077"), @TestCaseId("753078"),
            @TestCaseId("753079"), @TestCaseId("753080"), @TestCaseId("753081"), @TestCaseId("753082"),
            @TestCaseId("753083"), @TestCaseId("753084"), @TestCaseId("753085")
    })
    @Test(description = "Check Adswizz midroll- Direct Sold Targeting Params", groups = {ADS_WIZZ_TEST, MIDROLL_TEST, ADS_PARAMETERS})
    public abstract void testAdswizzMidRollDirectSoldTargetingParams();

    @TestCaseIds({@TestCaseId("752419"), @TestCaseId("752421"), @TestCaseId("752422"), @TestCaseId("752425"), @TestCaseId("752430"),
            @TestCaseId("752434"), @TestCaseId("752440"), @TestCaseId("753067")})
    @Test(description = "Ads Wizz preroll- High Value Params", groups = {ADS_PARAMETERS, PREROLL_TEST, ADS_WIZZ_TEST})
    public abstract void testAdsWizzpPrerollHighValueParams();

    @TestCaseIds({
            @TestCaseId("753289"), @TestCaseId("753290"), @TestCaseId("753291"), @TestCaseId("753292"), @TestCaseId("753293"),
            @TestCaseId("753294"), @TestCaseId("753295"), @TestCaseId("753296"), @TestCaseId("753297")
    })
    @Test(description = "Adswizz Instream - Direct sold targeting params", groups = {INSTREAM_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdswizzInstreamDirectSoldTargetingParams() {
        String requiredUrl = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            JsonObject parameter = getParameterWithValueFromResponseOfRequest(TUNE_REQUEST, URL, INSTREAM_URL);
            if (parameter != null) requiredUrl = getValueOfParameter(parameter, URL);
            if (requiredUrl != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredUrl);

        String[] params = {ADS_WIZZ_REQUEST_PARAM_PARTNER_ID, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_CLASS,
                ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_GENRE_ID, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PLAYER_ID};
        for (String param : params) {
            reportingAsserts.validateThatRequestURLContainsParameter(requiredUrl, param);
        }

        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_LOTAMESEGMENTS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PREMIUM, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ADS_PARTNER_ALIAS, AW_0_1ST_ADS_PARTNER_ALIAS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_ADSWIZZ_AD));
    }

    @Issues({@Issue("DROID-16350"), @Issue("DROID-16349"), @Issue("IOS-17354")})
    @TestCaseIds({@TestCaseId("753235"), @TestCaseId("753236"), @TestCaseId("753237")})
    @Test(description = "Adswizz Midroll Opt-Out", groups = {MIDROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public abstract void testAdswizzMidrollOptOut();

    @TestCaseIds({@TestCaseId("753052"), @TestCaseId("753063"), @TestCaseId("753062"), @TestCaseId("753061"), @TestCaseId("753060"),
            @TestCaseId("753059"), @TestCaseId("753058"), @TestCaseId("753057"), @TestCaseId("753056")})
    @Test(description = "AdsWizz Midroll - High Value Params", groups = {PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public abstract void testAdsWizzpMidrollHighValueParams();

    @Issues({@Issue("DROID-16350"), @Issue("DROID-16349"), @Issue("IOS-17354")})
    @TestCaseIds({@TestCaseId("753222"), @TestCaseId("753223"), @TestCaseId("753224")})
    @Test(description = "Adswizz Preroll Opt-out", groups = {PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdswizzPreRollOptOut() {
        settingsPage.enableOptOut(true);
        String requiredURL = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
            if (requiredURL != null) break;
        }

        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENTV2);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_GDPR);
    }

    @Issues({@Issue("DROID-16350"), @Issue("DROID-16349"), @Issue("IOS-17354")})
    @TestCaseIds({@TestCaseId("753228"), @TestCaseId("753229"), @TestCaseId("753230")})
    @Test(description = "Adswizz Midroll Opt-In", groups = {MIDROLL_TEST, ADS_WIZZ_TEST, ADS_TEST, ADS_PARAMETERS})
    public abstract void testAdswizzMidrollOptIn();

    @Issues({@Issue("DROID-16581"), @Issue("IOS-17569")})
    @TestCaseIds({
            @TestCaseId("753283"), @TestCaseId("753284"), @TestCaseId("753285"), @TestCaseId("753286"),
            @TestCaseId("753287"), @TestCaseId("753288")
    })
    @Test(description = "Check [Instream] AdsWizz high value params", groups = {ADS_TEST, INSTREAM_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdWizzInstreamHighValueParams() {
        String requiredUrl = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            JsonObject parameter = getParameterWithValueFromResponseOfRequest(TUNE_REQUEST, URL, INSTREAM_URL);
            if (parameter != null) requiredUrl = getValueOfParameter(parameter, URL);
            if (requiredUrl != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredUrl);

        reportingAsserts.validateThatRequestURLContainsParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_SKEY);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_LISTENERID);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_BUNDLEID, BUNDLEID);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_PLATFORM, TUNEIN);
    }

    @Issue("DROID-16349")
    @TestCaseIds({@TestCaseId("753218"), @TestCaseId("753217"), @TestCaseId("753243")})
    @Test(description = "AdsWizz Preroll Opt-IN", groups = {PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdsWizzPrerollOptIn() {
        settingsPage.enableOptOut(false);
        String requiredURL = null;
        for (Contents content: STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
            if (requiredURL != null) break;
        }

        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YNY);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENT_V2);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_GDPR);
    }

    @TestCaseIds({@TestCaseId("753299"), @TestCaseId("753300"), @TestCaseId("753298") })
    @Test(description = "Instream-Adswizz - Opt-In", groups = {ADS_TEST, INSTREAM_TEST, ADS_PARAMETERS})
    public void testAdswizzInstreamOptIn() {
        settingsPage.enableOptOut(false);
        String requiredUrl = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            JsonObject parameter = getParameterWithValueFromResponseOfRequest(TUNE_REQUEST, URL, INSTREAM_URL);
            if (parameter != null) requiredUrl = getValueOfParameter(parameter, URL);
            if (requiredUrl != null) break;
        }
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_GDPR);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENTV2);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YNY);
    }

    @Issue("DROID-15952")
    @TestCaseIds({@TestCaseId("753303"), @TestCaseId("753302"), @TestCaseId("753301") })
    @Test(description = "Instream-Adswizz - Opt-Out", groups = {ADS_TEST, INSTREAM_TEST, ADS_PARAMETERS})
    public void testAdswizzInstreamOptOut() {
        settingsPage.enableOptOut(true);
        String requiredUrl = null;
        for (int i = 0; i < 3; i++) {
            nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_INSTREAM_AD);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            JsonObject parameter = getParameterWithValueFromResponseOfRequest(TUNE_REQUEST, URL, INSTREAM_URL);
            if (parameter != null) requiredUrl = getValueOfParameter(parameter, URL);
            if (requiredUrl != null) break;
        }
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_GDPR);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredUrl, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENTV2);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredUrl, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
    }

    @TestCaseId("750398")
    @Test(description = "AdsWizz Preroll w/ Companion Banner", groups = {PREROLL_TEST, ADS_WIZZ_TEST, ADS_TEST})
    public void testAdsWizzPrerollWithCompanionBanner() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        nowPlayingPage
                .validateNowPlayingAdBannerDisplayed(true)
                .tapOnNowPlayingAdBanner();
        customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds()));

        String[] displayPercentages = {ADS_WIZZ_COMPANION_BANNER_START, ADS_WIZZ_COMPANION_BANNER_FIRST, ADS_WIZZ_COMPANION_BANNER_MIDPOINT,
                                        ADS_WIZZ_COMPANION_BANNER_THIRD, ADS_WIZZ_COMPANION_BANNER_COMPLETE};
        for (String displayPercentage : displayPercentages) {
            String requiredURL = getUrlFromRequestWithValue(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, displayPercentage);
            reportingAsserts.validateThatUrlIsPresent(requiredURL);
        }
    }

    @TestCaseId("750408")
    @Test(description = "Adswizz Preroll - Content", groups = {ADS_TEST, PREROLL_TEST, ADS_WIZZ_TEST})
    public void testAdswizzPrerollContent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.validateStreamStartPlaying();
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        nowPlayingPage
                .validateThatPreRollIsDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed();
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseId("750402")
    @Test(description = "Adswizz Preroll Audio Display Percentage request", groups = {PREROLL_TEST, ADS_WIZZ_TEST, ADS_TEST, ADS_PARAMETERS})
    public void testAdsWizzPrerollAudioDisplayPercentageRequest() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_AD);
        customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        nowPlayingPage.validateNowPlayingAdBannerDisplayed(true);
        String[] displayPercentages = {ADS_WIZZ_COMPANION_BANNER_START, ADS_WIZZ_COMPANION_BANNER_FIRST, ADS_WIZZ_COMPANION_BANNER_MIDPOINT,
                ADS_WIZZ_COMPANION_BANNER_THIRD, ADS_WIZZ_COMPANION_BANNER_COMPLETE};
        for (String displayPercentage : displayPercentages) {
            String requiredURL = getUrlFromRequestWithValue(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, displayPercentage);
            reportingAsserts.validateThatUrlIsPresent(requiredURL);
        }

        deepLinksUtil.openURL(STREAM_STATION_WITHOUT_ADS.getStreamTuneDeepLink());
        customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        deepLinksUtil.openURL(STREAM_STATION_ADSWIZZ_AD.getStreamTuneDeepLink());
        List<String> requiredURLListBeforePause = getUrlsFromRequest(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        int sizeBeforePause = requiredURLListBeforePause.size();

        if (nowPlayingPage.isPrerollDisplayed()) {
            nowPlayingPage.stopStreamPlaying();
            customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
            List<String> requiredURLListAfterPause = getUrlsFromRequest(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            int sizeAfterPause = requiredURLListAfterPause.size();
            assertThat(sizeBeforePause)
                    .as("Requests size before pause: " + sizeBeforePause + "is not equal to after pause: " + sizeAfterPause)
                    .isEqualTo(sizeAfterPause);

            nowPlayingPage.tapOnPlayButton();
            customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
            List<String> requiredURLListBeforeTerminateApp = getUrlsFromRequest(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            int sizeBeforeTerminateApp = requiredURLListBeforeTerminateApp.size();

            terminateApp();
            customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
            List<String> requiredURLListAfterTerminateApp = getUrlsFromRequest(ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            int sizeAfterTerminateApp = requiredURLListAfterTerminateApp.size();
            assertThat(sizeBeforeTerminateApp)
                    .as("Requests size before pause: " + sizeBeforeTerminateApp + "is not equal to after killing app: " + sizeAfterPause)
                    .isEqualTo(sizeAfterTerminateApp);
        } else {
            ReporterUtil.log("Preroll is not displayed");
        }
    }

    @Issue("DROID-16673")
    @TestCaseId("750407")
    @Test(description = "Adswizz Preroll - No preroll on First Tune", groups = {ADS_TEST, PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testNoPrerollOnFirstTuneOfAdswizz() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        String requiredURLForAdswizzPreroll = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(requiredURLForAdswizzPreroll);
        String requiredURLForAdsImpression = getUrlFromRequestWithParameterAndValue(REPORTS_AD_IMPRESSION, "L", "preroll");
        reportingAsserts.validateThatUrlIsAbsent(requiredURLForAdsImpression);
        deepLinksUtil.openURL(STREAM_STATION_ADSWIZZ_AD.getStreamTuneDeepLink());
        customWait(Duration.ofSeconds(5));
        nowPlayingPage.validateThatPreRollIsDisplayed();
    }

    @TestCaseId("750404")
    @Test(description = "Adswizz Preroll - Auto restart on relaunch [Post Preroll]", groups = {ADS_TEST, PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdswizzPrerollAutoRestartOnRelaunch() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_AD);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        String requiredURLForBanner = getUrlFromRequest(MAX_BANNER_REQUEST, Duration.ofSeconds(config().twoMinuteInSeconds()));
        reportingAsserts.validateThatUrlIsPresent(requiredURLForBanner);
    }

    @TestCaseId("750405")
    @Test(description = "Adswizz Pre-roll Deeplinking", groups = {ADS_TEST, PREROLL_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public void testAdswizzPrerollDeeplinking() {
        nowPlayingPage
                .generatePrerollForStream(STREAM_STATION_ADSWIZZ_AD)
                .validateNowPlayingAdBannerDisplayed(true);
        nowPlayingPage.tapOnNowPlayingAdBanner();
        deepLinksUtil.openTuneThroughDeeplink(true, STREAM_STATION_IMA_VIDEO_SECOND);
        String requiredURL = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_PREROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_ADSWIZZ_AD));
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }

    @TestCaseId("750456")
    @Test(description = "Test Adswizz preroll after midroll", groups = {ADS_TEST, PREROLL_TEST, ADS_PARAMETERS})
    public abstract void testAdswizzPrerollAfterMidroll();

    @TestCaseId("753611")
    @Test(description = "Check Skip IMA preroll, get Adwizz preroll, banner and verify request, impression URL params", groups = {ADS_TEST, BANNER_TEST, IMA_TEST, ADS_WIZZ_TEST, ADS_PARAMETERS})
    public abstract void testSkipIMAPrerollGetAdwizzPrerollBannerAndVerifyURLParams();
}
