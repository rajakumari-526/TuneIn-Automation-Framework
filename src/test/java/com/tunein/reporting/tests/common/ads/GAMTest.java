package com.tunein.reporting.tests.common.ads;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.models.Users;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.reporting.GAMEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.GAM_INSTREAM_REQUEST;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_INSTREAM_GAM_AD;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.MALE;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ReportingUtil.*;

public abstract class GAMTest extends BaseTest {

    @Issues({@Issue("DROID-16334"), @Issue("IOS-17344")})
    @TestCaseIds({
            @TestCaseId("753316"), @TestCaseId("753317"), @TestCaseId("753318"), @TestCaseId("753319"),
            @TestCaseId("753320"), @TestCaseId("753321"), @TestCaseId("753322"), @TestCaseId("753323"),
            @TestCaseId("753324"), @TestCaseId("753325"), @TestCaseId("753326"), @TestCaseId("753327"),
            @TestCaseId("753328"), @TestCaseId("753329"), @TestCaseId("753330"), @TestCaseId("753331"),
            @TestCaseId("753332"), @TestCaseId("753333"), @TestCaseId("753334"), @TestCaseId("753335"),
            @TestCaseId("753336"), @TestCaseId("753337"), @TestCaseId("753339"), @TestCaseId("753340")
    })
    @Test(description = "Instream-GAM Direct Sold Targeting Parameters", groups = {ADS_TEST, GAM_TEST, INSTREAM_TEST, ADS_PARAMETERS})
    public void testGAMInstreamDirectSoldTargetingParameters() {
        Users randomUser = generateRandomUser();
        randomUser.setUserGender(MALE.getValue());
        signUpPage.signUpFlowForUser(randomUser);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_INSTREAM_GAM_AD, true);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        JsonObject requiredRequest = getJsonForParameterFromRequest(GAM_INSTREAM_REQUEST, GAM_REQUEST_JSON_ADS_PARAMS);
        String customParamsValues = getValueOfParameter(requiredRequest, GAM_REQUEST_JSON_CUST_PARAMS);

        reportingAsserts.validateThatDataIsPresent(requiredRequest, GAM_INSTREAM_REQUEST);

        String[] adsParams = {GAM_REQUEST_GENDER, GAM_REQUEST_AGE,
                GAM_REQUEST_VERSION, GAM_REQUEST_PARTNER_ID, GAM_REQUEST_ADS_PARTNER_ALIAS, GAM_REQUEST_PREMIUM, GAM_REQUEST_PROGRAM_ID,
                GAM_REQUEST_GENRE_ID, GAM_REQUEST_CLASS, GAM_REQUEST_IS_MATURE, GAM_REQUEST_IS_FAMILY, GAM_REQUEST_IS_EVENT,
                GAM_REQUEST_IS_ON_DEMAND, GAM_REQUEST_IS_NEW_USER, GAM_REQUEST_ABTEST, GAM_REQUEST_ENV, GAM_REQUEST_ENABLE_DOUBLE_PREROLL};

        for (String adsParam : adsParams) {
            reportingAsserts.validateThatAdsParamIsPresent(customParamsValues, adsParam);
        }

        if (isAndroid()) {
            reportingAsserts.validateThatAdsParamIsNotPresent(customParamsValues, GAM_REQUEST_LOTAMESEGMENTS);
            reportingAsserts.validateThatAdsParamIsNotPresent(customParamsValues, GAM_REQUEST_LISTENER_ID);

        } else {
            reportingAsserts.validateThatAdsParamIsPresent(customParamsValues, GAM_REQUEST_LOTAMESEGMENTS);
            reportingAsserts.validateThatAdsParamIsPresent(customParamsValues, GAM_REQUEST_LISTENER_ID);

        }

        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_ADS_PARTNER_ALIAS, ADS_PARTNER_ALIAS_VALUE);
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_LANGUAGE, EN);
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_STATION_LANGUAGE, EN);
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_STATION_ID, getStreamId(STREAM_STATION_INSTREAM_GAM_AD));
    }

    @Issues({@Issue("IOS-17340"), @Issue("PLATFORM-16547")})
    @TestCaseIds({
            @TestCaseId("753304"), @TestCaseId("753305"), @TestCaseId("753314"), @TestCaseId("753313"),
            @TestCaseId("753312"), @TestCaseId("753311"), @TestCaseId("753310"), @TestCaseId("753309"),
            @TestCaseId("753308"), @TestCaseId("753307"), @TestCaseId("753306"), @TestCaseId("753315")
    })
    @Test(description = "Instream GAM high value parameters", groups = {ADS_TEST, INSTREAM_TEST, GAM_TEST, ADS_PARAMETERS})
    public abstract void testInstreamGAMHighValueParameters();

    @Issues({@Issue("DROID-16355"), @Issue("IOS-17357")})
    @TestCaseIds({@TestCaseId("753351"), @TestCaseId("753352"), @TestCaseId("753353"), @TestCaseId("753354"), @TestCaseId("753355")})
    @Test(description = "Instream GAM: Opt-out", groups = {ADS_TEST, GAM_TEST, INSTREAM_TEST, ADS_PARAMETERS})
    public void testGAMInstreamOptOut() {
        settingsPage.enableOptOut(true);
        Users randomUser = generateRandomUser();
        randomUser.setUserGender(MALE.getValue());
        signUpPage.signUpFlowForUser(randomUser);

        nowPlayingPage.generatePrerollForStream(STREAM_STATION_INSTREAM_GAM_AD, true);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        JsonObject requiredRequest = getJsonForParameterFromRequest(GAM_INSTREAM_REQUEST, GAM_REQUEST_JSON_ADS_PARAMS);
        String customParamsValues = getValueOfParameter(requiredRequest, GAM_REQUEST_JSON_CUST_PARAMS);

        reportingAsserts.validateThatDataIsPresent(requiredRequest, GAM_INSTREAM_REQUEST);
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_RDP, "1");
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_GDPR, "0");
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(customParamsValues, GAM_REQUEST_PARAM_NPA);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(customParamsValues, GAM_REQUEST_PARAM_GDPR_CONSENT);
    }

    @Issues({@Issue("DROID-16355"), @Issue("IOS-17357"), @Issue("DROID-16583"), @Issue("IOS-17571")})
    @TestCaseIds({
            @TestCaseId("753341"), @TestCaseId("753342"), @TestCaseId("753343"),
            @TestCaseId("753344"), @TestCaseId("753345")
    })
    @Test(description = "Check GAM Instream OptIn Parameters", groups = {ADS_TEST, GAM_TEST, INSTREAM_TEST, ADS_PARAMETERS})
    public void testGAMInstreamOptInParams() {
        settingsPage.enableOptOut(false);
        Users randomUser = generateRandomUser();
        randomUser.setUserGender(MALE.getValue());
        signUpPage.signUpFlowForUser(randomUser);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_INSTREAM_GAM_AD, true);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        JsonObject requiredRequest = getJsonForParameterFromRequest(GAM_INSTREAM_REQUEST, GAM_REQUEST_JSON_ADS_PARAMS);
        String customParamsValues = getValueOfParameter(requiredRequest, GAM_REQUEST_JSON_CUST_PARAMS);

        reportingAsserts.validateThatDataIsPresent(requiredRequest, GAM_INSTREAM_REQUEST);

        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_RDP, "0");
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_GDPR, "0");
        reportingAsserts.validateThatAdsParameterIsEqualTo(customParamsValues, GAM_REQUEST_PARAM_US_PRIVACY, ONE_YNY);

        reportingAsserts.validateThatRequestURLDoesntContainParameter(customParamsValues, GAM_REQUEST_PARAM_NPA);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(customParamsValues, GAM_REQUEST_PARAM_GDPR_CONSENT);
    }

}
