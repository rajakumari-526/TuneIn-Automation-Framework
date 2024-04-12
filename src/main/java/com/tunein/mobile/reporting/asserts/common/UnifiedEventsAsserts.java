package com.tunein.mobile.reporting.asserts.common;

import com.epam.reportportal.annotations.Step;
import com.google.gson.JsonObject;
import com.tunein.mobile.testdata.models.Contents;

import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.getSoftAssertion;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.utils.DataUtil.isInteger;
import static com.tunein.mobile.utils.ReportingUtil.getValueOfParameter;
import static com.tunein.mobile.utils.ReportingUtil.getValuesOfParameter;
import static java.lang.Boolean.parseBoolean;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class UnifiedEventsAsserts {

    @Step("Verify that required event {eventType} is present.")
    public void validateThatEventIsPresent(JsonObject data, String eventType) {
        assertThat(data)
                .as("Can NOT find '" + UNIFIED_EVENT_REPORT + "' report with '" + eventType + "' event")
                .isNotNull();
    }

    @Step("Verify that required event {eventType} is absent.")
    public void validateThatEventIsNotPresent(JsonObject data, String eventType) {
        assertThat(data)
                .as(UNIFIED_EVENT_REPORT + "' report with '" + eventType + "' event present")
                .isNull();
    }

    @Step("Verify that required context Parameter {expectedParameter} is present in the request and value is not empty.")
    public void validateThatContextParameterIsPresentAndValueIsNotEmpty(JsonObject data, String expectedParameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        assertThat(getValueOfParameter(context, expectedParameter))
                .as(event + "' event type. '" + expectedParameter + "' param is empty")
                .isNotBlank();
    }

    @Step("Verify that required Parameter {expectedParameter} is present in the request and value is not empty.")
    public void validateThatParameterIsPresentAndValueIsNotEmpty(JsonObject data, String expectedParameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        String valueOfAttribute = getValueOfParameter(data, expectedParameter);
        assertThat(valueOfAttribute)
                .as(event + "' event type. '" + expectedParameter + "' param is empty")
                .isNotBlank();
    }

    @Step("Verify that required Parameter {expectedParameter} is present")
    public void validateThatParameterIsPresent(JsonObject data, String expectedParameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        assertThat(data.get(expectedParameter))
                .as(event + "' event type. '" + expectedParameter + "' param is not present")
                .isNotNull();
    }

    @Step("Verify that required Parameter {parameter} value is integer.")
    public void validateThatParameterValueIsInteger(JsonObject data, String parameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        String valueOfAttribute = getValueOfParameter(data, parameter);
        assertThat(isInteger(valueOfAttribute))
                .as(event + "' event type. '"
                        + parameter + "' param is NOT integer. Actual is '" + valueOfAttribute + "'")
                .isTrue();
    }

    @Step("Verify that required Parameter {parameter} value is greater than 0.")
    public void validateThatParameterValueIsGreaterThanZero(JsonObject data, String parameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        String valueOfAttribute = getValueOfParameter(data, parameter);
        assertThat(Integer.parseInt(valueOfAttribute))
                .as(event + "' event type. '" + parameter + "' param is 0")
                .isGreaterThan(0);
    }

    @Step("Verify that required Parameter {expectedParameter} is not present in the request.")
    public void validateThatParameterIsNotPresent(JsonObject data, String expectedParameter) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        assertThat(data.get(expectedParameter))
                .as(event + "' event type. '" + expectedParameter + "' param is present")
                .isNull();
    }

    @Step("Verify that required Parameter {expectedParameter} is present equal to {expectedValue}")
    public void validateThatParameterIsEqualTo(JsonObject data, String expectedParameter, String expectedValue) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        String valueOfAttribute = getValueOfParameter(data, expectedParameter);
        assertThat(valueOfAttribute)
                .as(event + "' event type. '" + expectedParameter + "' param is not equal to " + expectedValue)
                .isEqualTo(expectedValue);
    }

    @Step("Verify that required Parameter {expectedParameter} is present and one of it's values equal to {expectedValue}")
    public void validateThatParameterContainsValue(JsonObject data, String expectedParameter, String expectedValue) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        List<String> valueOfAttribute = getValuesOfParameter(data, expectedParameter);
        assertThat(valueOfAttribute)
                .as(event + "' event type. '" + expectedParameter + "' param does not contain value " + expectedValue)
                .contains(expectedValue);
    }

    @Step("Verify that required Parameter {expectedParameter} value is equal to {expectedValue}")
    public void validateThatParameterIsEqualTo(JsonObject data, String expectedParameter, boolean expectedValue) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        boolean valueOfAttribute = Boolean.parseBoolean(getValueOfParameter(data, expectedParameter));
        assertThat(valueOfAttribute)
                .as(event + "' event type. '" + expectedParameter + "' param is not equal to " + expectedValue)
                .isEqualTo(expectedValue);
    }

    @Step("Verify that required parameter {expectedParameter} value is in the list of {expectedValues}")
    public void validateThatParameterValueInTheList(JsonObject data, String expectedParameter, List<String> expectedValues) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        String valueOfAttribute = getValueOfParameter(data, expectedParameter);
        assertThat(valueOfAttribute)
                .as(event + "' event type. '" + expectedParameter + "' param is not in " + expectedValues)
                .isIn(expectedValues);
    }

    @Step("Verify that common Parameters are present in request")
    public void validateUnifiedEventCommonParams(JsonObject data) {
        String event = getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT);
        getSoftAssertion().assertThat(getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_EVENT_TS))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_EVENT_TS + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(data, UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID + "' param is empty")
                .isNotBlank();

        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_USER_AGENT))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_USER_AGENT + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_PARTNER_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_PARTNER_ID + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_DEVICEID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_DEVICEID + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_IP))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_IP + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_LOCALE))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_LOCALE + "' param is empty")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(context, UNIFIED_EVENT_JSON_PARAM_SESSION_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_SESSION_ID + "' param is empty")
                .isNotBlank();

        JsonObject os = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_OS);
        String name = getValueOfParameter(os, UNIFIED_EVENT_JSON_PARAM_NAME).toLowerCase();
        getSoftAssertion().assertThat(name)
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_NAME + "' param is NOT correct")
                .isEqualTo(config().mobileOS().toLowerCase());
        String version = getValueOfParameter(os, UNIFIED_EVENT_JSON_PARAM_VERSION);
        getSoftAssertion().assertThat(version)
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_VERSION + "' param is NOT correct")
                .isNotBlank();

        JsonObject app = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_APP);
        getSoftAssertion().assertThat(getValueOfParameter(app, UNIFIED_EVENT_JSON_PARAM_VERSION))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_VERSION + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertThat(getValueOfParameter(app, UNIFIED_EVENT_JSON_PARAM_BUILD))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_BUILD + "' param is NOT correct")
                .isNotBlank();

        JsonObject network = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_NETWORK);
        getSoftAssertion().assertThat(getValueOfParameter(network, UNIFIED_EVENT_JSON_PARAM_WIFI))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_WIFI + "' param is NOT correct")
                .isNotBlank();
        getSoftAssertion().assertAll();

        verifyUnifiedEventContextDevice(data);
        verifyUnifiedEventContextAppState(data);
        verifyUnifiedEventContextScreen(data);
    }

    @Step("Verify that user info Parameters are not empty after sign in")
    public void verifyUnifiedEventContextUserInfoAfterSignIn(JsonObject data) {
        String event = data.get(UNIFIED_EVENT_JSON_PARAM_EVENT).toString();
        JsonObject context = (JsonObject) data.get(UNIFIED_EVENT_JSON_PARAM_CONTEXT);
        JsonObject userInfo = (JsonObject) context.get(UNIFIED_EVENT_JSON_PARAM_USER_INFO);
        getSoftAssertion().assertThat(parseBoolean(getValueOfParameter(userInfo, UNIFIED_EVENT_JSON_PARAM_IS_LOGGED_IN)))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_IS_LOGGED_IN + "' param is NOT true")
                .isTrue();
        getSoftAssertion().assertThat(getValueOfParameter(userInfo, UNIFIED_EVENT_JSON_PARAM_USER_ID))
                .as(event + "' event type. '" + UNIFIED_EVENT_JSON_PARAM_USER_ID + "' param is empty'")
                .isNotBlank();
        getSoftAssertion().assertAll();
    }

    public abstract void verifyUnifiedEventContextListeningInfoIsEmpty(JsonObject data);

    public abstract void verifyUnifiedEventContextUserInfoIsEmpty(JsonObject data);

    public abstract void verifyUnifiedEventContextAppState(JsonObject data);

    public abstract void verifyUnifiedEventContextScreen(JsonObject data);

    public abstract void verifyUnifiedEventContextDevice(JsonObject data);

    public abstract void validateParameterStateAccordingToDependentParameter(JsonObject data, String expectedParameter, String dependentParameter);

    public abstract void verifyUnifiedEventContextListeningInfo(JsonObject data, Contents content);

}
