package com.tunein.mobile.reporting.asserts.common;

import com.epam.reportportal.annotations.Step;
import com.google.gson.JsonObject;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.mitmproxy.InterceptedMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.getSoftAssertion;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.utils.DataUtil.convertSpecialCharactersToHexInString;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ReportingAsserts {

    @Step("Verify that request with body {expectedValue} is sent")
    public void assertRequestWithDataIsSent(List<InterceptedMessage> messages, String expectedValue) {
        String convertedExpectedValue = REPORT_BODY_NAME_EVENT + (isAndroid() ? convertSpecialCharactersToHexInString(expectedValue) : expectedValue);

        InterceptedMessage requestWithRequiredBody = messages
                .stream()
                .filter(message -> getRequestBody(message).contains(convertedExpectedValue))
                .findFirst()
                .orElse(null);
        assertThat(requestWithRequiredBody)
                .as(String.format("Request with body '%s' was not sent or body is incorrect", convertedExpectedValue))
                .isNotNull();

        List<Integer> invalidResponseCodes = Arrays.asList(
                RESPONSE_STATUS_401_CODE,
                RESPONSE_STATUS_403_CODE,
                RESPONSE_STATUS_500_CODE,
                RESPONSE_STATUS_NULL
        );
        assertThat(getResponseStatus(requestWithRequiredBody))
                .as("Response for request with body '%s' is not successful or response is absent", convertedExpectedValue)
                .isNotIn(invalidResponseCodes);

        ReporterUtil.log("Received request's url: " + getRequestUrl(requestWithRequiredBody));
        ReporterUtil.log("Received request's body: " + getRequestBody(requestWithRequiredBody));
        ReporterUtil.log("Received response status: " + getResponseStatus(requestWithRequiredBody));
    }

    @Step("Verify that list of requests with body  {expectedList} is sent")
    public void assertListOfRequestWithDataIsSent(List<InterceptedMessage> messages, List<String> expectedList) {
        for (String expectedValue : expectedList) {
            String convertedExpectedValue = REPORT_BODY_NAME_EVENT + (isAndroid() ? convertSpecialCharactersToHexInString(expectedValue) : expectedValue);

            InterceptedMessage requestWithRequiredBody = messages
                    .stream()
                    .filter(message -> getRequestBody(message).contains(convertedExpectedValue))
                    .findFirst()
                    .orElse(null);
            getSoftAssertion()
                    .assertThat(requestWithRequiredBody)
                    .as(String.format("Request with body '%s' was not sent or body is incorrect", convertedExpectedValue))
                    .isNotNull();

            List<Integer> invalidResponseCodes = Arrays.asList(
                    RESPONSE_STATUS_401_CODE,
                    RESPONSE_STATUS_403_CODE,
                    RESPONSE_STATUS_500_CODE,
                    RESPONSE_STATUS_NULL
            );
            getSoftAssertion()
                    .assertThat(getResponseStatus(requestWithRequiredBody))
                    .as("Response for Request with body '%s' is not successful or response is absent", convertedExpectedValue)
                    .isNotIn(invalidResponseCodes);

            ReporterUtil.log("Received request's url: " + getRequestUrl(requestWithRequiredBody));
            ReporterUtil.log("Received request's body: " + getRequestBody(requestWithRequiredBody));
            ReporterUtil.log("Received response status: " + getResponseStatus(requestWithRequiredBody));
        }
        getSoftAssertion().assertAll();

    }

    @Step("Verify that request url contains parameter {parameter} with value {value}")
    public void validateThatRequestURLContainsParameterWithValue(String url, String parameter, String value) {
        assertThat(url)
                .as(url + " doesn't contain parameter " + parameter + " which equals " + value)
                .contains(parameter + "=" + value);
    }

    @Step("Verify that request url contains parameter {parameter}")
    public void validateThatRequestURLContainsParameter(String url, String parameter) {
        assertThat(url)
                .as(url + " doesn't contain parameter " + parameter)
                .contains(parameter);
    }

    @Step("Verify that request url doesnt contain parameter {parameter}")
    public void validateThatRequestURLDoesntContainParameter(String url, String parameter) {
        assertThat(url)
                .as(url + " contains parameter " + parameter)
                .doesNotContain(parameter);
    }

    @Step("Verify that required url is present. {url}")
    public void validateThatUrlIsPresent(String url, String... valueFromUrl) {
        String error = valueFromUrl.length > 0 ? "Can't find required URL which contains " + valueFromUrl[0] : "Can't find required URL:";
        assertThat(url)
                .as(error)
                .isNotNull();
    }

    @Step("Verify that required url is absent. {url}")
    public void validateThatUrlIsAbsent(String url, String... valueFromUrl) {
        String error = valueFromUrl.length > 0 ? "URL " + url + " which contains " + valueFromUrl[0] + " is present" : "URL " + url + " is present";
        assertThat(url)
                .as("URL is present  " + url)
                .isNull();
    }

    @Step("Verify that required event {requestType} is present.")
    public void validateThatDataIsPresent(JsonObject data, RequestType requestType) {
        assertThat(data)
                .as("Can NOT find Json data: " + requestType.getRequestTypeValue())
                .isNotNull();
    }

    @Step("Verify that required parameter {expectedAdsParameter} is present and value is not null.")
    public void validateThatAdsParamIsPresent(JsonObject data, String expectedAdsParameter) {
        String valueOfAttribute = getValueOfParameter(data, expectedAdsParameter);
        assertThat(valueOfAttribute)
                .as("Can NOT find '" + valueOfAttribute)
                .isNotNull();
    }

    @Step("Verify that required parameter {expectedAdsParameter} is not present")
    public void validateThatAdsParamIsNotPresent(JsonObject data, String expectedAdsParameter) {
        assertThat(data.get(expectedAdsParameter))
                .as("Parameter" + expectedAdsParameter + " is present")
                .isNull();
    }

    @Step("Verify that required parameter {expectedAdsParameter} is present and equal to {expectedValue}")
    public void validateThatAdsParameterIsEqualTo(JsonObject data, String expectedAdsParameter, String expectedValue) {
        String valueOfAttribute = getValueOfParameter(data, expectedAdsParameter);
        assertThat(valueOfAttribute)
                .as(expectedAdsParameter + "' param is not equal to " + expectedValue)
                .isEqualTo(expectedValue);
    }

    @Step("Verify that required parameter {expectedAdsParameter} is present.")
    public void validateThatAdsParamIsPresent(String data, String expectedAdsParameter) {
        try {
            assertThat(URLDecoder.decode(data, "UTF-8"))
                    .as("Can NOT find '" + expectedAdsParameter + "' in data: " + data)
                    .containsAnyOf(expectedAdsParameter + "=", expectedAdsParameter + ":");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Verify that required parameter {expectedAdsParameter} is not present.")
    public void validateThatAdsParamIsNotPresent(String data, String expectedAdsParameter) {
        try {
            getSoftAssertion().assertThat(URLDecoder.decode(data, "UTF-8"))
                    .as(expectedAdsParameter + " is present in data: " + data)
                    .doesNotContain(expectedAdsParameter + "=");
            getSoftAssertion().assertThat(URLDecoder.decode(data, "UTF-8"))
                    .as(expectedAdsParameter + " is present in data: " + data)
                    .doesNotContain(expectedAdsParameter + ":");
            getSoftAssertion().assertAll();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Verify that required parameter {expectedAdsParameter} is present and equal to {expectedValue}")
    public void validateThatAdsParameterIsEqualTo(String data, String expectedAdsParameter, String expectedValue) {
        try {
            assertThat(URLDecoder.decode(data, "UTF-8"))
                    .as(expectedAdsParameter + "' param is not equal to " + expectedValue)
                    .containsAnyOf(expectedAdsParameter + "=" + expectedValue, expectedAdsParameter + ":" + expectedValue);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Verify that required parameter {expectedAdsParameter} is present.")
    public void validateThatAdsParamListIsPresent(List<String> expectedAdsParameter, String expectedValue) {
        assertThat(expectedAdsParameter).as(expectedValue + " param is not present").contains(expectedValue);
    }

    @Step("Verify that required parameter {expectedAdsParameter} is not present.")
    public void validateThatAdsParamListIsEmpty(List<String> expectedAdsParameter) {
        assertThat(expectedAdsParameter).as("Parameter is present").isEmpty();
    }

}
