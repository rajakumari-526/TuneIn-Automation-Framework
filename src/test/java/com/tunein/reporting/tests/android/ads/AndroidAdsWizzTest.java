package com.tunein.reporting.tests.android.ads;

import com.tunein.mobile.testdata.models.Contents;
import com.tunein.reporting.tests.common.ads.AdsWizzTest;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_MIDROLL_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_PREROLL_REQUEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;

import static com.tunein.mobile.reporting.AdsWizzEventConstants.EN;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ENGLISH;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.NOWPLAYING;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ONE_YNY;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.ONE_YYY;
import static com.tunein.mobile.reporting.BannerEventConstants.*;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.IMAEventConstants.IMA;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.ReportingUtil.getUrlFromRequest;
import static com.tunein.mobile.utils.ReportingUtil.getUrlFromRequestWithParameterAndValue;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class AndroidAdsWizzTest extends AdsWizzTest {

    @Override
    public void testAdsWizzpPrerollHighValueParams() {
        String requiredURL = null;
        for (Contents content : STATIONS_WITH_ADSWIZZ) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
            if (requiredURL != null) break;
        }

        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_SKEY);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ZONEID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_LISTENERID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_SESSION_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, COMPANIONADS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAMETER_AW_0_REQ_BUNDLEID, config().appPackageAndroid());
    }

    @Override
    public void testAdsWizzpMidrollHighValueParams() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(30));
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURL = getUrlFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_SKEY);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ZONEID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_LISTENERID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_SESSION_ID);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_PARAMETER_IS_EVENT_COMPANIONADS, COMPANIONADS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAMETER_AW_0_REQ_BUNDLEID, config().appPackageAndroid());
    }

    @Override
    public void testAdswizzMidRollDirectSoldTargetingParams() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURL = getUrlFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ADS_PARTNER_ALIAS, AW_0_1ST_ADS_PARTNER_ALIAS);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_SCREEN, NOWPLAYING);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_LANGUAGE, EN);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_STATION_LANGUAGE, ENGLISH);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_FAMILY, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_MATURE, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_ON_DEMAND, "false");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_MIDROLL));
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_LISTING_ID, getStreamId(STREAM_STATION_WITH_MIDROLL));

        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_CLASS);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_PERSONA);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_SESSION_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_EVENT);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_VERSION);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PLAYER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_GENRE_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_PARTNER_ID);
        reportingAsserts.validateThatRequestURLContainsParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_IS_NEW_USER);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_LOTAMESEGMENTS);
    }

    @Override
    public void testAdswizzMidrollOptIn() {
        settingsPage.enableOptOut(false);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(50));
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURL = getUrlFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENT_V2);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_GDPR);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YNY);
    }

    @Override
    public void testAdswizzMidrollOptOut() {
        settingsPage.enableOptOut(true);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(50));
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        String requiredURL = getUrlFromRequest(ADS_WIZZ_MIDROLL_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, ADS_WIZZ_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENT_V2);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, ADS_WIZZ_REQUEST_PARAM_GDPR);
    }

    @Override
    public void testAdswizzPrerollAfterMidroll() {
        updateLaunchArgumentFor(ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS, String.valueOf(config().timeBetweenRollsInSeconds()));
        navigationAction.navigateTo(HOME);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_MIDROLL);
        customWait(Duration.ofSeconds(config().timeBetweenRollsInSeconds()));
        reportingUtil.waitUntilRequestWithParameterAndValueIsSent(ADS_WIZZ_MIDROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_WITH_MIDROLL));

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        String requireUrl = getUrlFromRequestWithParameterAndValue(ADS_WIZZ_PREROLL_REQUEST, ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID, getStreamId(STREAM_STATION_ADSWIZZ_AD));
        reportingAsserts.validateThatUrlIsPresent(requireUrl);
    }

    @Override
    public void testSkipIMAPrerollGetAdwizzPrerollBannerAndVerifyURLParams() {
        String requiredURL;
        Contents content = STREAM_STATION_IMA_VIDEO_AD;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openTuneThroughDeeplink(content);
        reportingUtil.waitUntilRequestWithParameterAndValueIsSent(REPORTS_AD_REQUEST, IMA_EVENT_REQUEST_PARAM_N, IMA, Duration.ofSeconds(config().oneMinuteInSeconds()));
        String[] requests = {"MaxBannerRequest", "MaxBannerImpression", "AdwizzPrerollRequest", "AdwizzPrerollImpression"};

        for (String request : requests) {
            switch (request) {
                case "AdwizzPrerollRequest", "AdwizzPrerollImpression" -> {
                    if (request.contains("Request")) {
                        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_REQUEST, IMA_EVENT_REQUEST_PARAM_N, ADWIZZ_AUDIO_ADWIZZ_DISPLAY);
                    } else {
                        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_IMPRESSION, IMA_EVENT_REQUEST_PARAM_N, ADWIZZ_AUDIO_ADWIZZ_DISPLAY);
                    }
                    reportingAsserts.validateThatUrlIsPresent(requiredURL);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_N, ADWIZZ_AUDIO_ADWIZZ_DISPLAY);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_I, getStreamId(content));
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_F);
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_U);
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_L);
                }
                case "MaxBannerRequest", "MaxBannerImpression" -> {
                    if (request.contains("Request")) {
                        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_REQUEST, IMA_EVENT_REQUEST_PARAM_N, MAX_BANNER);
                    } else {
                        requiredURL = getUrlFromRequestWithParameterAndValue(REPORTS_AD_IMPRESSION, IMA_EVENT_REQUEST_PARAM_N, MAX_BANNER);
                    }
                    reportingAsserts.validateThatUrlIsPresent(requiredURL);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_N, MAX_BANNER);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_L, BANNER);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_I, getStreamId(content));
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_R);
                }
                default -> throw new IllegalStateException("Unexpected or undefined request: " + request);
            }
        }
    }
}
