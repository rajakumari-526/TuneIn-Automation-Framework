package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;

import static com.tunein.mobile.reporting.BannerEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_PREROLL_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.MAX_BANNER_REQUEST;
import static com.tunein.mobile.reporting.BannerEventConstants.ONE_YNY;
import static com.tunein.mobile.reporting.BannerEventConstants.ONE_YYY;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ReportingUtil.*;

public abstract class BannersTest extends BaseTest {

    @Issue("IOS-17350")
    @TestCaseIds({
            @TestCaseId("753415"), @TestCaseId("753416"), @TestCaseId("753417"), @TestCaseId("753418"), @TestCaseId("753419"),
            @TestCaseId("753420"), @TestCaseId("753421"), @TestCaseId("753422"), @TestCaseId("753423"), @TestCaseId("753424"),
            @TestCaseId("753425"), @TestCaseId("753426"), @TestCaseId("753427"), @TestCaseId("753428"), @TestCaseId("753429"),
            @TestCaseId("753430"), @TestCaseId("753431"), @TestCaseId("753432"), @TestCaseId("753433"), @TestCaseId("753434"),
            @TestCaseId("753435"), @TestCaseId("753436"), @TestCaseId("753437")
    })
    @Test(description = "Banner targeting_data", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerTargetingData() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_TARGETING_DATA, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        String keywordsValue = getValueOfParameter(requiredRequest, MAX_BANNER_REQUEST_JSON_KEYWORDS);

        String[] adsParams = {
                MAX_BANNER_REQUEST_JSON_USER_AGENT, MAX_BANNER_REQUEST_JSON_PARTNER_ID, MAX_BANNER_REQUEST_JSON_GENRE_ID, MAX_BANNER_REQUEST_JSON_CLASS, MAX_BANNER_REQUEST_JSON_PROGRAM_ID,
                MAX_BANNER_REQUEST_JSON_VERSION, MAX_BANNER_REQUEST_JSON_SHOW_ID, MAX_BANNER_REQUEST_JSON_PERSONA, MAX_BANNER_REQUEST_JSON_DEVICE, MAX_BANNER_REQUEST_JSON_CATEGORY_ID,
                MAX_BANNER_REQUEST_JSON_PPID, MAX_BANNER_REQUEST_JSON_IS_MATURE, MAX_BANNER_REQUEST_JSON_IS_FAMILY, MAX_BANNER_REQUEST_JSON_IS_EVENT,
                MAX_BANNER_REQUEST_JSON_IS_ON_DEMAND, MAX_BANNER_REQUEST_JSON_IS_NEW_USER, MAX_BANNER_REQUEST_JSON_IS_FIRST_IN_SESSION
        };
        for (String adsParam : adsParams) {
            reportingAsserts.validateThatAdsParamIsPresent(keywordsValue, adsParam);
        }

        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_LISTING_ID, getStreamId(STREAM_STATION_WITHOUT_ADS));
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_STATION_ID, getStreamId(STREAM_STATION_WITHOUT_ADS));
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_LANGUAGE, EN_US_VALUE);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_STATION_LANGUAGE, ENGLISH_VALUE);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_SCREEN, MAX_BANNER_REQUEST_NOW_PLAYING);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_JSON_ADS_PARTNER_ALIAS, MAX_BANNER_REQUEST_ADS_PARTNER_ALIAS);
    }

    @TestCaseIds({@TestCaseId("753413"), @TestCaseId("753414")})
    @Test(description = "Test banners ad info", groups = {ADS_TEST, ADS_PARAMETERS, BANNER_TEST})
    public void testBannersAdInfo() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_AD_INFO, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_AD_UNIT_ID);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, MAX_BANNER_REQUEST_AD_FORMAT, AD_FORMAT_VALUE);
    }

    @TestCaseIds({@TestCaseId("753411"), @TestCaseId("753412")})
    @Test(description = "Banners location_info", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerLocationInfo() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_LOCATION_INFO, Duration.ofSeconds(config().oneMinuteInSeconds()));

        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_JSON_LOC_SERVICES_ENABLED);
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_JSON_LOC_AUTH);
    }

    @TestCaseId("753410")
    @Test(description = "Banners device_info", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public abstract void testBannerDeviceInfo();

    @TestCaseIds({
            @TestCaseId("753403"), @TestCaseId("753404"), @TestCaseId("753405"), @TestCaseId("753406"),
            @TestCaseId("753327"), @TestCaseId("753328"), @TestCaseId("753329")
    })
    @Test(description = "Check Engineering Control Params in App Info", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testEngineeringControlParamsInAppInfo() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_APP_INFO, Duration.ofSeconds(config().oneMinuteInSeconds()));

        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);

        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, MAX_BANNER_REQUEST_JSON_DEBUG, "true");
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, MAX_BANNER_REQUEST_JSON_PACKAGE_NAME, TUNEIN_RADIO_PACKAGE);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, MAX_BANNER_REQUEST_JSON_MUTED, "true");
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_JSON_TEST_ADS);
    }

    @Issues({@Issue("DROID-16324"), @Issue("DROID-16622"), @Issue("IOS-17607"), @Issue("IOS-17604")})
    @TestCaseIds({@TestCaseId("753442"), @TestCaseId("753441"), @TestCaseId("753440")})
    @Test(description = "Banner adapters_info", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerAdaptersInfo() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_ADAPTERS_INFO, Duration.ofSeconds(config().twoMinuteInSeconds()));
        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        List<String> initialisedClassName = getValuesOfParameter(requiredRequest, MAX_BANNER_REQUEST_INITIALIZED_CLASS_NAMES);
        List<String> loadedClassName = getValuesOfParameter(requiredRequest, MAX_BANNER_REQUEST_LOADED_CLASS_NAMES);
        List<String> failedClassName = getValuesOfParameter(requiredRequest, MAX_BANNER_REQUEST_FAILED_CLASS_NAMES);

        List<String> adsParams = List.of(new String[]{MAX_BANNER_AL_APP_LOVIN_MEDIATION_ADAPTER, MAX_BANNER_AL_GOOGLE_MEDIATION_ADAPTER,
                MAX_BANNER_AL_IN_MOBI_MEDIATION_ADAPTER, MAX_BANNER_AL_AMAZON_MP_MEDIATION_ADAPTER,
                MAX_BANNER_AL_GOOGLE_AD_MANAGER_MEDIATION_ADAPTER, MAX_BANNER_AL_VERVE_MEDIATION_ADAPTER, MAX_BANNER_AL_FB_MEDIATION_ADAPTER, MAX_BANNER_AL_INNER_ACTIVE_MEDIATION_ADAPTER});
        for (String adsParam : adsParams) {
            reportingAsserts.validateThatAdsParamListIsPresent(initialisedClassName, adsParam);
            reportingAsserts.validateThatAdsParamListIsPresent(loadedClassName, adsParam);
        }
        reportingAsserts.validateThatAdsParamListIsEmpty(failedClassName);
    }

    @Issues({@Issue("DROID-16613"), @Issue("IOS-17604")})
    @TestCaseIds({@TestCaseId("753451"), @TestCaseId("753450"), @TestCaseId("753447"), @TestCaseId("753444"), @TestCaseId("753443")})
    @Test(description = "Banners Reporting - Opt-In", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerReportingOptInParams() {
        settingsPage.enableOptOut(false);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_TARGETING_DATA, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        String keywordsValue = getValueOfParameter(requiredRequest, MAX_BANNER_REQUEST_JSON_KEYWORDS);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(keywordsValue, MAX_BANNER_REQUEST_PARAM_SET_HAS_USER_CONSENT);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(keywordsValue, MAX_BANNER_REQUEST_PARAM_NS_USER_DEFAULTS);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_PARAM_US_PRIVACY, ONE_YNY);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_PARAM_GDPR, "0");
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_PARAM_SET_DO_NOT_SELL, "No");
    }

    @Issues({@Issue("DROID-15952"), @Issue("DROID-16613"), @Issue("IOS-17604"), @Issue("IOS-17365")})
    @TestCaseIds({@TestCaseId("753445"), @TestCaseId("753446"), @TestCaseId("753449"),
            @TestCaseId("753452"), @TestCaseId("753448")})
    @Test(description = "Check Banner Reporting opt out params", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerReportingOptOutParams() {
        settingsPage.enableOptOut(true);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);

        JsonObject requiredRequest = getJsonForParameterFromRequest(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_TARGETING_DATA, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        String keywordsValue = getValueOfParameter(requiredRequest, MAX_BANNER_REQUEST_JSON_KEYWORDS);

        reportingAsserts.validateThatRequestURLDoesntContainParameter(keywordsValue, MAX_BANNER_REQUEST_PARAM_SET_HAS_USER_CONSENT);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(keywordsValue, MAX_BANNER_REQUEST_PARAM_NS_USER_DEFAULTS);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_PARAM_GDPR, "0");
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
        reportingAsserts.validateThatAdsParameterIsEqualTo(keywordsValue, MAX_BANNER_REUQEST_PARAM_SET_DO_NOT_SELL, YES);
    }

    @TestCaseId("753610")
    @Test(description = "Launch station -> get IMA preroll -> banner", groups = {ADS_TEST, PREROLL_TEST, BANNER_TEST, ADS_PARAMETERS})
    public abstract void testIMAPrerollBannerFlow();

    @TestCaseIds({@TestCaseId("753407"), @TestCaseId("753408"), @TestCaseId("753409")})
    @Test(description = "Check Banner Reporting top level params", groups = {ADS_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerReportingTopLevelParams() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        JsonObject requiredRequest = getJsonFromRequest(MAX_BANNER_REQUEST, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatDataIsPresent(requiredRequest, MAX_BANNER_REQUEST);
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_SDK_KEY);
        reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, MAX_BANNER_REQUEST_JSON_RID);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, MAX_BANNER_REQUEST_MEDIATION_PROVIDER, MAX);
    }

    @TestCaseId("753613")
    @Test(description = "Check banner flow for Preroll Disabled station", groups = {ADS_TEST, PREROLL_TEST, BANNER_TEST, ADS_PARAMETERS})
    public abstract void testPrerollDisabledStationBannerFlow();

    @TestCaseId("753860")
    @Test(description = "Check banner and preroll for premium user", groups = {ADS_TEST, PREROLL_TEST, BANNER_TEST, ADS_PARAMETERS})
    public void testBannerAndPrerollForPremiumUser() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_WITH_PREROLL);
        String requiredUrl = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        reportingAsserts.validateThatUrlIsAbsent(requiredUrl);

        String requiredURL = getUrlFromRequestWithParameterAndValue(MAX_BANNER_REQUEST, MAX_BANNER_REQUEST_JSON_STATION_ID, getStreamId(STREAM_STATION_WITH_PREROLL));
        reportingAsserts.validateThatUrlIsAbsent(requiredURL);
    }
}
