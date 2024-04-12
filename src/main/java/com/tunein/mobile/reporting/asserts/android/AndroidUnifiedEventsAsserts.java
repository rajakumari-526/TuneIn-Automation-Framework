package com.tunein.mobile.reporting.asserts.android;

import com.epam.reportportal.annotations.Step;
import com.google.gson.JsonObject;
import com.tunein.mobile.reporting.asserts.common.UnifiedEventsAsserts;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;

import static com.tunein.mobile.pages.BasePage.getSoftAssertion;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.OWNED_AND_OPERATED;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.DataUtil.isInteger;
import static com.tunein.mobile.utils.DataUtil.isLong;
import static com.tunein.mobile.utils.ReportingUtil.getValueOfParameter;
import static java.lang.Boolean.parseBoolean;

public class AndroidUnifiedEventsAsserts extends UnifiedEventsAsserts {

    @Step("Check state of required Parameter {expectedParameter} which is dependent on state of other Parameter {dependentParameter}.")
    @Override
    public void validateParameterStateAccordingToDependentParameter(JsonObject data, String expectedParameter, String dependentParameter) {
        if (!Boolean.parseBoolean(getValueOfParameter(data, dependentParameter))) {
            validateThatParameterIsPresentAndValueIsNotEmpty(data, expectedParameter);
        } else {
            validateThatParameterIsNotPresent(data, expectedParameter);
        }
    }

    @Step("Verify that listening info Parameters are empty")
    @Override
    public void verifyUnifiedEventContextListeningInfoIsEmpty(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject listeningInfo = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_LISTENING_INFO);
        getSoftAssertion().assertThat(getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_LISTEN_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_LISTEN_ID + "' param is NOT 0")
                .isEqualTo("0");
        getSoftAssertion().assertThat(getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_GUIDE_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_GUIDE_ID + "' param is NOT 'null'")
                .isEqualTo("");
        getSoftAssertion().assertThat(getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_PARENT_GUIDE_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_PARENT_GUIDE_ID + "' param is NOT 'null'")
                .isEqualTo("");
        getSoftAssertion().assertAll();
    }

    @Step("Verify that user info Parameters are empty")
    @Override
    public void verifyUnifiedEventContextUserInfoIsEmpty(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject userInfo = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_USER_INFO);
        getSoftAssertion().assertThat(parseBoolean(getValueOfParameter(userInfo, UNIFIED_EVENT_JSON_PARAM_IS_REGISTERED)))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_IS_REGISTERED + "' param is NOT false")
                .isFalse();
        getSoftAssertion().assertThat(parseBoolean(getValueOfParameter(userInfo, UNIFIED_EVENT_JSON_PARAM_IS_LOGGED_IN)))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_IS_LOGGED_IN + "' param is NOT false")
                .isFalse();
        getSoftAssertion().assertThat(getValueOfParameter(userInfo, UNIFIED_EVENT_JSON_PARAM_USER_ID).toString())
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_USER_ID + "' param is NOT 'null'")
                .isEqualTo("");
        getSoftAssertion().assertAll();
    }

    @Step("Verify apState parameters")
    @Override
    public void verifyUnifiedEventContextAppState(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject appState = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_APP_STATE);
        String abTestIds = getValueOfParameter(appState, UNIFIED_EVENT_JSON_PARAM_AB_TEST_IDS);
        getSoftAssertion().assertThat(abTestIds)
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_AB_TEST_IDS + "' param is NOT present")
                .isNotNull();
        getSoftAssertion().assertThat(parseBoolean(getValueOfParameter(appState, UNIFIED_EVENT_JSON_PARAM_IN_BACKGROUND)))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_IN_BACKGROUND + "' param is NOT false")
                .isFalse();
        getSoftAssertion().assertAll();
    }

    @Step("Verify screen parameters")
    @Override
    public void verifyUnifiedEventContextScreen(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject screen = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_SCREEN);
        String height = getValueOfParameter(screen, UNIFIED_EVENT_JSON_PARAM_HEIGHT);
        getSoftAssertion().assertThat(isInteger(height))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_HEIGHT + "' param is NOT correct")
                .isTrue();
        String width = getValueOfParameter(screen, UNIFIED_EVENT_JSON_PARAM_WIDTH);
        getSoftAssertion().assertThat(isInteger(width))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_WIDTH + "' param is NOT correct")
                .isTrue();
        String density = getValueOfParameter(screen, UNIFIED_EVENT_JSON_PARAM_DENSITY);
        getSoftAssertion().assertThat(isInteger(density))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_DENSITY + "' param is NOT correct")
                .isTrue();
        getSoftAssertion().assertAll();
    }

    @Step("Verify device parameters")
    @Override
    public void verifyUnifiedEventContextDevice(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject device = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_DEVICE);
        getSoftAssertion().assertThat(getValueOfParameter(device, UNIFIED_EVENT_JSON_PARAM_MANAFACTURER))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_MANAFACTURER + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(device, UNIFIED_EVENT_JSON_PARAM_MODEL))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_MODEL + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(device, UNIFIED_EVENT_JSON_PARAM_BATTERY_PCT))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_BATTERY_PCT + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(device, UNIFIED_EVENT_JSON_PARAM_NAME))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_NAME + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(device, UNIFIED_EVENT_JSON_PARAM_TYPE))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_TYPE + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertAll();
    }

    @Step("Verify that listening info Parameters are not empty")
    @Override
    public void verifyUnifiedEventContextListeningInfo(JsonObject data, Contents content) {
        ContentProvider.ContentType contentType = getContentTypeValue(content.getStreamType());
        String guideID = "";
        String parentGuideId = "";

        if (contentType == PODCAST || contentType == PREMIUM_PODCAST || contentType == AUDIOBOOK) {
            guideID = getStreamId(content);
            parentGuideId = content.getStreamTuneDeepLink().replaceAll("tunein://tune/", "");
        } else if (contentType == LIVE_STATION || contentType == PREMIUM_LIVE_STATION || contentType == OWNED_AND_OPERATED) {
            guideID = getStreamId(content);
        }
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject listeningInfo = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_LISTENING_INFO);
        String listenId = getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_LISTEN_ID);
        getSoftAssertion().assertThat(listenId)
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_LISTEN_ID + "' param is empty")
                .isNotBlank()
                .isNotEqualTo(0);
        getSoftAssertion().assertThat(isLong(listenId))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_LISTEN_ID + "' param is NOT long")
                .isTrue();
        getSoftAssertion().assertThat(getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_GUIDE_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_GUIDE_ID + "' param is NOT " + guideID)
                .isEqualTo(guideID);
        getSoftAssertion().assertThat(getValueOfParameter(listeningInfo, UNIFIED_EVENT_JSON_PARAM_PARENT_GUIDE_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_PARENT_GUIDE_ID + "' param is NOT " + parentGuideId)
                .isEqualTo(parentGuideId);
        getSoftAssertion().assertAll();
    }

}
