package com.tunein.reporting.tests.android.ads;

import com.google.gson.JsonObject;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.reporting.tests.common.ads.GAMTest;

import static com.tunein.mobile.reporting.GAMEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.GAM_INSTREAM_REQUEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_INSTREAM_GAM_AD;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.MALE;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.ReportingUtil.getJsonForParameterFromRequest;
import static com.tunein.mobile.utils.ReportingUtil.getValueOfParameter;

public class AndroidGAMTest extends GAMTest {

    @Override
    public void testInstreamGAMHighValueParameters() {
        Users randomUser = generateRandomUser();
        randomUser.setUserGender(MALE.getValue());
        signUpPage.signUpFlowForUser(randomUser);
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_INSTREAM_GAM_AD, true);
        nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
        JsonObject requiredRequest = getJsonForParameterFromRequest(GAM_INSTREAM_REQUEST, GAM_REQUEST_JSON_ADS_PARAMS);
        String customParamsValues = getValueOfParameter(requiredRequest, GAM_REQUEST_JSON_CUST_PARAMS);

        String[] adsParams = {GAM_REQUEST_PPID, GAM_REQUEST_CIU_SZS, GAM_REQUEST_ID_TYPE,
                GAM_REQUEST_IS_LAT, GAM_REQUEST_PALN, GAM_REQUEST_BUNDLE_ID, GAM_REQUEST_DESCRIPTION_URL,
                GAM_REQUEST_CUST_PARAMS, GAM_REQUEST_URL, GAM_REQUEST_HL};

        for (String adsParam : adsParams) {
            reportingAsserts.validateThatAdsParamIsPresent(requiredRequest, adsParam);
        }

        reportingAsserts.validateThatAdsParamIsPresent(customParamsValues, GAM_REQUEST_STATION_ID);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, GAM_REQUEST_CIU_SZS, GAM_CUI_SZS_300_250);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, GAM_REQUEST_ID_TYPE, GAM_ID_TYPE);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, GAM_REQUEST_BUNDLE_ID, GAM_BUNDLE_ID);
        reportingAsserts.validateThatAdsParameterIsEqualTo(requiredRequest, GAM_REQUEST_IS_LAT, "0");
        reportingAsserts.validateThatAdsParamIsNotPresent(requiredRequest, GAM_REQUEST_RDID);
    }
}
