package com.tunein.reporting.tests.ios.ads;

import com.google.gson.JsonObject;
import com.tunein.mobile.reporting.ReportingConstants;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.reporting.tests.common.ads.BannersTest;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.BannerEventConstants.*;
import static com.tunein.mobile.reporting.BannerEventConstants.IMA;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.*;
import static com.tunein.mobile.reporting.UnifiedEventConstants.MAX_BANNER;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.ReportingUtil.getUrlFromRequest;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class IosBannersTest extends BannersTest {

    @Override
    public void testBannerDeviceInfo() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_DEVICE_INFO, Duration.ofSeconds(config().oneMinuteInSeconds()));

        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_JSON_IDFA);
    }

    @Override
    public void testIMAPrerollBannerFlow() {
        String requiredURL = null;
        nowPlayingPage
                .generatePrerollForStream(STREAM_STATION_IMA_VIDEO)
                .validateStreamStartPlaying();

        ReportingConstants.RequestType[] events = {REPORTS_AD_REQUEST, REPORTS_AD_IMPRESSION, REPORTS_AD_START,
                REPORTS_AD_END};

        for (ReportingConstants.RequestType event : events) {
            switch (event) {
                case REPORTS_AD_REQUEST, REPORTS_AD_IMPRESSION -> {
                    String requiredURLForIMA = getUrlFromRequestWithParameterAndValue(event, IMA_EVENT_REQUEST_PARAM_N, IMA);
                    reportingAsserts.validateThatUrlIsPresent(requiredURLForIMA);
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_R);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_U, MOBILE_PREROLL_VIDEO);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_N, IMA);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_L, PREROLL);
                    reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_I, getStreamId(STREAM_STATION_IMA_VIDEO));
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURLForIMA, IMA_EVENT_REQUEST_PARAM_LISTEN_ID);

                    String requiredURLForMaxBanner = getUrlFromRequestWithParameterAndValue(event, IMA_EVENT_REQUEST_PARAM_N, MAX_BANNER);
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURLForMaxBanner, IMA_EVENT_REQUEST_PARAM_R);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURLForMaxBanner, IMA_EVENT_REQUEST_PARAM_N, MAX_BANNER);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURLForMaxBanner, IMA_EVENT_REQUEST_PARAM_L, BANNER);
                    reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURLForMaxBanner, IMA_EVENT_REQUEST_PARAM_I, getStreamId(STREAM_STATION_IMA_VIDEO));
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURLForMaxBanner, IMA_EVENT_REQUEST_PARAM_LISTEN_ID);

                }
                case REPORTS_AD_START, REPORTS_AD_END -> {
                    requiredURL = getUrlFromRequestWithParameterAndValue(event, IMA_EVENT_REQUEST_PARAM_N, IMA);
                    reportingAsserts.validateThatUrlIsPresent(requiredURL);
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_R);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_N, IMA);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_U, MOBILE_PREROLL_VIDEO);
                    reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, IMA_EVENT_REQUEST_PARAM_L, PREROLL);
                    reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_EVENT_REQUEST_PARAM_I, getStreamId(STREAM_STATION_IMA_VIDEO));
                    reportingAsserts.validateThatAdsParamIsPresent(requiredURL, IMA_EVENT_REQUEST_PARAM_LISTEN_ID);
                }

                default -> throw new Error("Invalid Request Type");
            }
        }
    }

    @Override
    public void testPrerollDisabledStationBannerFlow() {
        Contents content = PREROLL_DISABLED_STATION;
        deepLinksUtil.openTuneThroughDeeplink(content);
        String verifyURL = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(verifyURL);
        verifyURL = getUrlFromRequest(IMA_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(verifyURL);
        nowPlayingPage.validateThatPreRollIsAbsent();

        ReportingConstants.RequestType[] events = {REPORTS_AD_REQUEST, REPORTS_AD_IMPRESSION};

        for (ReportingConstants.RequestType event : events) {
            String requiredURL = getUrlFromRequestWithParameterAndValue(event, MAX_BANNER_REQUEST_PARAM_N, MAX_BANNER);
            if (requiredURL == null) {
                customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
                requiredURL = getUrlFromRequestWithParameterAndValue(event, MAX_BANNER_REQUEST_PARAM_N, MAX_BANNER);
            }
            reportingAsserts.validateThatUrlIsPresent(requiredURL);
            reportingAsserts.validateThatAdsParamIsPresent(requiredURL, MAX_BANNER_REQUEST_PARAM_R);
            reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, MAX_BANNER_REQUEST_PARAM_N, MAX_BANNER);
            reportingAsserts.validateThatAdsParameterIsEqualTo(requiredURL, MAX_BANNER_REQUEST_PARAM_L, BANNER);
            reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, MAX_BANNER_REQUEST_PARAM_I, getStreamId(content));
            reportingAsserts.validateThatAdsParamIsPresent(requiredURL, MAX_BANNER_REQUEST_PARAM_LISTEN_ID);
        }

    }
}
