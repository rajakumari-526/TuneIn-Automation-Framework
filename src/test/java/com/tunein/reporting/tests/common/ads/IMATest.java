package com.tunein.reporting.tests.common.ads;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ApplicationUtil.closeApp;
import static com.tunein.mobile.utils.ApplicationUtil.terminateApp;
import static com.tunein.mobile.utils.ReportingUtil.PrerollType.IMA_VIDEO;
import static com.tunein.mobile.utils.ReportingUtil.*;

public abstract class IMATest extends BaseTest {

    @TestCaseIds({
            @TestCaseId("752420"), @TestCaseId("752423"), @TestCaseId("752424"), @TestCaseId("752426"),
            @TestCaseId("752427"), @TestCaseId("752428"), @TestCaseId("752429"), @TestCaseId("752431"),
            @TestCaseId("752432"), @TestCaseId("752433"), @TestCaseId("752436"),
            @TestCaseId("752437"), @TestCaseId("752438"), @TestCaseId("752439"),
            @TestCaseId("752441"), @TestCaseId("752442"), @TestCaseId("752445")
    })
    @Test(description = "Check IMA Preroll High Value Parameters", groups = {ADS_PARAMETERS, ADS_TEST, PREROLL_TEST, IMA_TEST})
    public void testIMAPrerollHighValueParameters() {
        String requiredURL = null;
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequestWithParameterAndValue(IMA_REQUEST, IMA_REQUEST_PARAM_AD_TYPE, AUDIO_VIDEO);
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_CIU_SZS);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_CORRELATOR);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_DESCRIPTION_URL);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_PPID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_SCOR);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_SID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_SZ);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_URL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_AD_RULE, "1");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_AD_TYPE, AUDIO_VIDEO);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_GDFP_REQ, "1");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_OUTPUT, XML_VMAP1);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_IU, MOBILE_PREROLL_VIDEO);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_UNVIEWED_POSITION_START, "1");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_HL, EN);
        if (isAndroid()) {
            reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_IS_LAT, "0");
            reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, IMA_REQUEST_PARAM_RDID);
        } else {
            reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_REQUEST_PARAM_RDID);
        }
    }

    @Issues({@Issue("IOS-17357"), @Issue("DROID-14103")})
    @TestCaseIds({
            @TestCaseId("753262"), @TestCaseId("753263"), @TestCaseId("753264"),
            @TestCaseId("753265"), @TestCaseId("753266")
    })
    @Test(description = "Check IMA Preroll OptIn Params", groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS})
    public void testIMAPrerollOptInParams() {
        settingsPage.enableOptOut(false);
        String requiredURL = null;
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequestWithParameterAndValue(IMA_REQUEST, IMA_REQUEST_PARAM_AD_TYPE, AUDIO_VIDEO);
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_RDP, "0");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_GDPR, "0");
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, IMA_REQUEST_PARAM_NPA);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, IMA_REQUEST_PARAM_GDPR_CONSENT);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_US_PRIVACY, ONE_YNY);
    }

    @Issues({@Issue("DROID-15952"), @Issue("DROID-16355"), @Issue("IOS-17357")})
    @TestCaseIds({
            @TestCaseId("753272"), @TestCaseId("753273"), @TestCaseId("753274"),
            @TestCaseId("753275"), @TestCaseId("753276")
    })
    @Test(description = "Check IMA Preroll OptOut Params", groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS})
    public abstract void testIMAPrerollOptOutParams();

    @Issue("IOS-14655")
    @TestCaseIds({
            @TestCaseId("752446"), @TestCaseId("752447"), @TestCaseId("752451"), @TestCaseId("752452"), @TestCaseId("752453"),
            @TestCaseId("752456"), @TestCaseId("752458"), @TestCaseId("752457"), @TestCaseId("752459"), @TestCaseId("752463"),
            @TestCaseId("752461"), @TestCaseId("752462"), @TestCaseId("752465"), @TestCaseId("752466"), @TestCaseId("752467"),
            @TestCaseId("752468"), @TestCaseId("752469"), @TestCaseId("752470"), @TestCaseId("752483"), @TestCaseId("752486"),
            @TestCaseId("752484"), @TestCaseId("752487"), @TestCaseId("752488"), @TestCaseId("752489"), @TestCaseId("753282")
    })
    @Test(description = "IMA Preroll - Direct Sold Targeting Params", groups = {PREROLL_TEST, IMA_TEST, ADS_TEST, ADS_PARAMETERS})
    public void testImaPrerollDirectSoldTargetingParams() {
        String requiredURL = null;
        Contents selectedContent = null;
        for (Contents content: STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequest(IMA_REQUEST);
            selectedContent = content;
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_IS_EVENT, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_IS_FAMILY, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_IS_MATURE, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_IS_NEW_USER, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_IS_ON_DEMAND, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_PREMIUM, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_STATION_LANGUAGE, ENGLISH);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_SCREEN, NOWPLAYING);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_STATION_ID, getStreamId(selectedContent));
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_LISTING_ID, getStreamId(selectedContent));

        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_CATEGORY_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_ENABLE_DOUBLE_PREROLL);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_CLASS);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_GENRE_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_ENV);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_AFFILIATE_IDS);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_LANGUAGE);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_LISTENER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_PROGRAM_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_PERSONA);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_VERSION);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_PARTNER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, IMA_EVENT_REQUEST_PARAM_ADS_PARTNER_ALIAS);
    }

    @Issues({@Issue("DROID-15749"), @Issue("IOS-13109")})
    @TestCaseId("750417")
    @Test(description = "Video preroll quartile percentage reporting while preroll is playing",
            groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS}
    )
    public void testVideoPrerollQuartilePercentageRequestWhilePrerollPlaying() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content, true);
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION);
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String[] displayPercentages = {PREROLL_EVENT_REQUEST_QUARTILE_START, PREROLL_EVENT_REQUEST_QUARTILE_FIRST, PREROLL_EVENT_REQUEST_QUARTILE_MIDPOINT,
                PREROLL_EVENT_REQUEST_QUARTILE_THIRD, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE, PREROLL_EVENT_REQUEST_VIEWABLE_IMPRESSION, PREROLL_EVENT_REQUEST_MEASURABLE_IMPRESSION};
        for (String displayPercentage : displayPercentages) {
            requiredURL = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, displayPercentage, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
            reportingAsserts.validateThatUrlIsPresent(requiredURL, displayPercentage);
        }

    }

    @TestCaseId("750417")
    @Test(description = "Video preroll quartile percentage reporting while preroll is pause and resume",
            groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS}
    )
    public void testVideoPrerollQuartilePercentageRequestWhilePrerollPauseResume() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            deepLinksUtil.openURL(content.getStreamTuneDeepLink());
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        nowPlayingPage.stopStreamPlaying();
        String requiredURLAfterPauseWithPauseLabel = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_PAUSED);
        reportingAsserts.validateThatUrlIsPresent(requiredURLAfterPauseWithPauseLabel, PREROLL_EVENT_REQUEST_QUARTILE_PAUSED);
        String requiredURLAfterPauseWithCompleteLabel = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
        reportingAsserts.validateThatUrlIsAbsent(requiredURLAfterPauseWithCompleteLabel, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);

        nowPlayingPage
                .tapOnPlayButton()
                .waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURLAfterResumeWithResumeLabel = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_RESUME);
        reportingAsserts.validateThatUrlIsPresent(requiredURLAfterResumeWithResumeLabel, PREROLL_EVENT_REQUEST_QUARTILE_RESUME);
        String requiredURLAfterResumeWithCompleteLabel = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
        reportingAsserts.validateThatUrlIsPresent(requiredURLAfterResumeWithCompleteLabel, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
    }

    @Test(description = "Video preroll quartile percentage reporting while app in background",
            groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS}
    )
    public void testVideoPrerollQuartilePercentageRequestWhileAppInBackground() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            deepLinksUtil.openURL(content.getStreamTuneDeepLink());
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        closeApp();
        String requiredURLAfterAppClose = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE, Duration.ofSeconds(config().twoMinuteInSeconds()));
        reportingAsserts.validateThatUrlIsAbsent(requiredURLAfterAppClose, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
    }

    @Issue("DROID-14117")
    @TestCaseId("750417")
    @Test(description = "Video preroll quartile percentage reporting when navigating from now playing page",
            groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS}
    )
    public abstract void testVideoPrerollQuartilePercentageRequestWhenNavigatingFromNowPlayingPage();

    @TestCaseId("750417")
    @Test(description = "Video preroll quartile percentage reporting while navigating to another station", groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS})
    public void testVideoPrerollQuartilePercentageRequestWhileAnotherStationPlaying() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            deepLinksUtil.openURL(content.getStreamTuneDeepLink());
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        String requiredURLAfterLaunchAnotherStation = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
        reportingAsserts.validateThatUrlIsAbsent(requiredURLAfterLaunchAnotherStation, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
    }

    @TestCaseId("750417")
    @Test(description = "Video preroll quartile percentage reporting while app kill", groups = {PREROLL_TEST, ADS_TEST, IMA_TEST, ADS_PARAMETERS})
    public void testVideoPrerollQuartilePercentageRequestWhileAppKill() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            deepLinksUtil.openURL(content.getStreamTuneDeepLink());
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        terminateApp();
        String requiredURLAfterAppTerminate = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
        reportingAsserts.validateThatUrlIsAbsent(requiredURLAfterAppTerminate, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE);
    }

    @TestCaseId("750431")
    @Test(description = "IMA Preroll - No preroll on First Tune", groups = {ADS_TEST, PREROLL_TEST, IMA_TEST, ADS_PARAMETERS})
    public void testNoPrerollOnFirstTuneOfIMA() {
        setPrerollType(IMA_VIDEO);
        String requiredURL;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_IMA_VIDEO_AD);
        requiredURL = getUrlFromRequestWithParameterAndValue(IMA_REQUEST, IMA_REQUEST_PARAM_IU, MOBILE_PREROLL_VIDEO);
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_REQUEST, IMA_EVENT_REQUEST_PARAM_N, PREROLL);
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_IMA_AUDIO_AD);
        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_REQUEST, IMA_EVENT_REQUEST_PARAM_L, PREROLL);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }

}
