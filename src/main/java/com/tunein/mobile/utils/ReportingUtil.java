package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.appium.mitmproxy.InterceptedMessage;
import org.openqa.selenium.remote.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.mitmproxy.MitmproxyDriverProvider.generateListOfAllowedHosts;
import static com.tunein.mobile.mitmproxy.MitmproxyDriverProvider.messagesByThread;
import static com.tunein.mobile.pages.BasePage.getSoftAssertion;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.CLIENT_REPORT_REQUEST;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.utils.CommandLineProgramUtil.getOutputOfExecution;
import static com.tunein.mobile.utils.DataUtil.parseJson;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.ADS_PREROLL_TYPE;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class ReportingUtil {

    public static String getRequestUrl(InterceptedMessage message) {
        try {
            return message.getRequest().getUrl();
        } catch (NullPointerException ex) {
            return "";
        }
    }

    public static String getRequestBody(InterceptedMessage message) {
        try {
            return new String(message.getRequest().getBody());
        } catch (NullPointerException ex) {
            return "";
        }
    }

    public static String getResponseBody(InterceptedMessage message) {
        try {
            return new String(message.getResponse().getBody());
        } catch (NullPointerException ex) {
            return "";
        }
    }

    public static int getResponseStatus(InterceptedMessage message) {
        try {
            return message.getResponse().getStatusCode();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public static String[] getRequestQueryParams(InterceptedMessage message) {
        String url = message.getRequest().getUrl();
        int index = url.indexOf('?');
        url = url.substring(index + 1);
        return url.split("&");
    }

    public List<InterceptedMessage> getListOfRequestsByType(String requestUrl, String queryName, String queryValue) {
        return messagesByThread.stream()
                .filter(message -> getRequestUrl(message).contains(requestUrl)
                        && isQueryParamWithValuePresent(message, queryName, queryValue))
                .collect(Collectors.toList());
    }

    public List<InterceptedMessage> getListOfRequestsByUrl(String requestUrl) {
        return messagesByThread.stream()
                .filter(message -> getRequestUrl(message).contains(requestUrl))
                .collect(Collectors.toList());
    }

    public boolean isQueryParamWithValuePresent(InterceptedMessage message, String queryName, String queryValue) {
        String[] queryParams = getRequestQueryParams(message);
        for (String queryParam : queryParams) {
            String paramName = queryParam.substring(0, queryParam.indexOf('='));
            if (paramName.equals(queryName)) {
                String param = DataUtil.decodeUrl(queryParam);
                String value = param.substring(param.indexOf('=') + 1);
                if (value.equals(queryValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getQueryParamValue(InterceptedMessage message, String queryName) {
        String[] queryParams = getRequestQueryParams(message);
        String value = "";
        for (String queryParam : queryParams) {
            String paramName = queryParam.substring(0, queryParam.indexOf('='));
            if (paramName.equals("id")) {
                String param = DataUtil.decodeUrl(queryParam);
                value = param.substring(param.indexOf('=') + 1);
                break;
            }
        }
        return value;
    }

    public InterceptedMessage getRequiredRequestWithParameter(List<InterceptedMessage> messages, String parameter) {
        return messages.stream()
                .filter(message -> getRequestBody(message).contains(parameter))
                .findFirst()
                .get();
    }

    private static boolean isRequestSent(InterceptedMessage message, String requestUrl) {
        int statusCode = message.getResponse().getStatusCode();
        return getRequestUrl(message).contains(requestUrl)
                && (message.getRequest().getMethod() != HttpMethod.OPTIONS.toString())
                && (statusCode != RESPONSE_STATUS_401_CODE)
                && (statusCode != RESPONSE_STATUS_403_CODE)
                && (statusCode != RESPONSE_STATUS_500_CODE);
    }

    @Step("Get required unified event with type {eventType}")
    public static JsonObject getLastUnifiedEventRequestByType(String eventType, Duration... timeout) {
        ArrayList<JsonObject> urlList = null;
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().oneMinuteInSeconds());
        takeScreenshot();
        JsonObject dataObject = null;
        long start = System.currentTimeMillis();
        while (dataObject == null) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                if (isRequestSent(message, CLIENT_REPORT_REQUEST.getRequestTypeValue())) {
                    String fullRequestBody = getRequestBody(message);
                    JsonObject jsonBody = parseJson(fullRequestBody);
                    JsonArray data = (JsonArray) jsonBody.get(UNIFIED_EVENT_JSON_PARAM_DATA);
                    urlList.add(jsonBody);

                    for (int j = 0; j < data.size(); j++) {
                        dataObject = data.get(j).getAsJsonObject();
                        if (getValueOfParameter(dataObject, UNIFIED_EVENT_JSON_PARAM_TYPE).equals(UNIFIED_EVENT_TYPE)
                                && getValueOfParameter(dataObject, UNIFIED_EVENT_JSON_PARAM_EVENT).equals(eventType)) {
                            getSoftAssertion().assertThat(getValueOfParameter(jsonBody, UNIFIED_EVENT_JSON_PARAM_DEVICE_ID)).as(eventType + "' event type. '"
                                    + UNIFIED_EVENT_JSON_PARAM_DEVICE_ID + "' param is empty").isNotBlank();
                            getSoftAssertion().assertThat(getValueOfParameter(jsonBody, UNIFIED_EVENT_JSON_PARAM_SENT_AT)).as(eventType + "' event type. '"
                                    + UNIFIED_EVENT_JSON_PARAM_SENT_AT + "' param is empty").isNotBlank();
                            getSoftAssertion().assertAll();
                            ReporterUtil.log("Required Unified Event data: ", dataObject); //LOGGER INFO
                            return dataObject;
                        } else {
                            dataObject = null;
                        }
                    }
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }

        ReporterUtil.log("Request '" + UNIFIED_EVENT_REPORT + "' url by '" + eventType + "' type is NOT present." + " All requests are:"); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get required unified event with type {eventType}, parameter {paramName} and value {paramValue}")
    public static JsonObject getLastUnifiedEventRequestByType(String eventType, String paramName, String paramValue, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().oneMinuteInSeconds());
        ArrayList<JsonObject> urlList = null;
        takeScreenshot();
        JsonObject dataObject = null;
        long start = System.currentTimeMillis();
        while (dataObject == null) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                if (isRequestSent(message, CLIENT_REPORT_REQUEST.getRequestTypeValue())) {
                    String fullRequestBody = getRequestBody(message);
                    JsonObject jsonBody = parseJson(fullRequestBody);
                    JsonArray data = (JsonArray) jsonBody.get(UNIFIED_EVENT_JSON_PARAM_DATA);
                    urlList.add(jsonBody);

                    urlList.add(jsonBody);
                    for (int j = 0; j < data.size(); j++) {
                        dataObject = data.get(j).getAsJsonObject();
                        if (getValueOfParameter(dataObject, UNIFIED_EVENT_JSON_PARAM_TYPE).equals(UNIFIED_EVENT_TYPE)
                                && getValueOfParameter(dataObject, UNIFIED_EVENT_JSON_PARAM_EVENT).equals(eventType)
                                && (dataObject.toString().replaceAll(" ", "").replaceAll("\"", "").contains(paramName + ":" + paramValue)
                                || dataObject.get(paramName).toString().contains(paramValue))) {
                            getSoftAssertion().assertThat(getValueOfParameter(jsonBody, UNIFIED_EVENT_JSON_PARAM_DEVICE_ID)).as(eventType + "' event type. '"
                                    + UNIFIED_EVENT_JSON_PARAM_DEVICE_ID + "' param is empty").isNotBlank();
                            getSoftAssertion().assertThat(getValueOfParameter(jsonBody, UNIFIED_EVENT_JSON_PARAM_SENT_AT)).as(eventType + "' event type. '"
                                    + UNIFIED_EVENT_JSON_PARAM_SENT_AT + "' param is empty").isNotBlank();
                            getSoftAssertion().assertAll();
                            ReporterUtil.log("Required Unified Event data: ", dataObject); //LOGGER INFO
                            return dataObject;
                        } else {
                            dataObject = null;
                        }
                    }

                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }
        ReporterUtil.log("Request '" + UNIFIED_EVENT_REPORT + "' url by '" + eventType + "' type is NOT present." + " All requests are:"); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get Json with parameter {parameter} for request {requestType}")
    public static JsonObject getJsonForParameterFromRequest(RequestType requestType, String parameter, Duration... timeout) {
        customWait(Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds()));
        ArrayList<String> urlList = new ArrayList<>();
        for (int i = messagesByThread.size() - 1; i >= 0; i--) {
            InterceptedMessage message = messagesByThread.get(i);
            urlList.add(getRequestUrl(message));
            if (isRequestSent(message, requestType.getRequestTypeValue())) {
                String fullRequestBody = getRequestBody(message);
                if (fullRequestBody != null && !fullRequestBody.isEmpty() && fullRequestBody.contains(parameter)) {
                    JsonObject jsonBody = parseJson(fullRequestBody);
                    JsonObject dataObject = jsonBody.get(parameter).getAsJsonObject();
                    ReporterUtil.log("Required " + parameter + " data: ", dataObject); //LOGGER INFO
                    return dataObject;
                }
            }
        }

        ReporterUtil.log("Request '" + requestType.getRequestTypeValue() + "' is NOT present." + " All requests are:"); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get Json for request {requestType}")
    public static JsonObject getJsonFromRequest(RequestType requestType, Duration... timeout) {
        JsonObject dataObject = null;
        long start = System.currentTimeMillis();
        ArrayList<String> urlList = new ArrayList<>();
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        while (dataObject == null) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    dataObject = parseJson(getRequestBody(message));
                    ReporterUtil.log("Required json data: ", dataObject); //LOGGER INFO
                    return dataObject;
                } else {
                    dataObject = null;
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }

        ReporterUtil.log("Request '" + requestType.getRequestTypeValue() + "' is NOT present." + " All requests are:"); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get url for request {requestType}")
    public static String getUrlFromRequest(RequestType requestType, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        ArrayList<String> urlList = new ArrayList<>();
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    try {
                        String fullRequestUrl = getRequestUrl(message);
                        requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                    return requestUrl;
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout);
                break;
            }
        }
        ReporterUtil.log("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present."); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get Json response for request {requestType}")
    public static JsonObject getResponseFromRequest(RequestType requestType, Duration timeout, String... valueInUrl) {
        Duration timeoutValue = Duration.ofSeconds(timeout.toSeconds());
        ArrayList<String> urlList = new ArrayList<>();
        long start = System.currentTimeMillis();
        JsonObject fullResponseBody = null;
        while (fullResponseBody == null) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    fullResponseBody = parseJson(getResponseBody(message));
                    if (valueInUrl.length > 0) {
                        if (getRequestUrl(message).contains(valueInUrl[0])) {
                            ReporterUtil.log("Required response : ", fullResponseBody);
                            return fullResponseBody;
                        } else {
                            fullResponseBody = null;
                        }
                    }
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout);
            }
        }
        ReporterUtil.log("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present."); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get url with parameter {parameter} and value {value}")
    public static String getUrlWithParameterAndValue(String parameter, String value, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        ArrayList<String> urlList = new ArrayList<>();
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (getRequestUrl(message).contains(parameter + "=" + value)) {
                    try {
                        String fullRequestUrl = getRequestUrl(message);
                        requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                    return requestUrl;
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }
        ReporterUtil.log("Request with parameter " + parameter + " and value " + " is absent"); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get url for request {requestType} with parameter {parameter} and value {value}")
    public static String getUrlFromRequestWithParameterAndValue(RequestType requestType, String parameter, String value, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        ArrayList<String> urlList = new ArrayList<>();
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    if (getRequestUrl(message).contains(parameter + "=" + value)) {
                        try {
                            String fullRequestUrl = getRequestUrl(message);
                            requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                        return requestUrl;
                    }
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }

        ReporterUtil.log("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present."); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Wait until request {requestType} with parameter {parameter} and value {value} is sent")
    public void waitUntilRequestWithParameterAndValueIsSent(RequestType requestType, String parameter, String value, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    if (getRequestUrl(message).contains(parameter + "=" + value)) {
                        try {
                            String fullRequestUrl = getRequestUrl(message);
                            requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                        break;
                    }
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                throw new Error("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present.");
            }
        }
    }

    @Step("Wait until request {requestType} is sent")
    public void waitUntilRequestIsSent(RequestType requestType, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    try {
                        String fullRequestUrl = getRequestUrl(message);
                        requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                    break;
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                throw new Error("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present.");
            }
        }
    }

    @Step("Get urls for requests with type {requestType}")
    public static List<String> getUrlsFromRequest(RequestType requestType, Duration... timeout) {
        customWait(Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().oneMinuteInSeconds()));
        //takeScreenshot();
        ArrayList<String> urlList = new ArrayList<>();
        ArrayList<String> allRequests = new ArrayList<>();

        for (int i = messagesByThread.size() - 1; i >= 0; i--) {
            InterceptedMessage message = messagesByThread.get(i);
            allRequests.add(getRequestUrl(message));
            if (isRequestSent(message, requestType.getRequestTypeValue())) {
                String requestUrl;
                try {
                    String fullRequestUrl = getRequestUrl(message);
                    requestUrl = URLDecoder.decode(fullRequestUrl, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                urlList.add(requestUrl);
            }
        }
        if (urlList.isEmpty()) {
            ReporterUtil.log("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present."); //LOGGER INFO
            allRequests.forEach(ReporterUtil::log);
        }
        return urlList;
    }

    @Step("Get url for request {requestType} with value {value}")
    public static String getUrlFromRequestWithValue(RequestType requestType, String value, Duration... timeout) {
        customWait(Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds()));
        takeScreenshot();
        String requestUrl = null;
        for (int i = messagesByThread.size() - 1; i >= 0; i--) {
            InterceptedMessage message = messagesByThread.get(i);
            if (isRequestSent(message, requestType.getRequestTypeValue())) {
                if (getRequestUrl(message).contains(value)) {
                    requestUrl = getRequestUrl(message);
                    ReporterUtil.log("Required URL: " + requestUrl); //LOGGER INFO
                    break;
                }
            }
        }
        return requestUrl;
    }

    @Step("Get parameter {paramName} with value {paramValue} from response of request {requestType}")
    public static JsonObject getParameterWithValueFromResponseOfRequest(RequestType requestType, String paramName, String paramValue, Duration... timeout) {
        customWait(Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds()));
        ArrayList<String> urlLists = new ArrayList<>();
        for (int i = messagesByThread.size() - 1; i >= 0; i--) {
            InterceptedMessage message = messagesByThread.get(i);
            urlLists.add(getRequestUrl(message));
            if (isRequestSent(message, requestType.getRequestTypeValue())) {
                JsonObject fullResponseBody = parseJson(getResponseBody(message));
                JsonArray jsonBody = (JsonArray) fullResponseBody.get(RESPONSE_JSON_PARAM_BODY);
                for (int j = 0; j < jsonBody.size(); j++) {
                    JsonObject dataObject = jsonBody.get(j).getAsJsonObject();
                    if (isParameterPresent(dataObject, paramName) && getValueOfParameter(dataObject, paramName).contains(paramValue)) {
                        ReporterUtil.log("Required response parameter: ", dataObject); //LOGGER INFO
                        return dataObject;
                    }
                }
            }
        }

        ReporterUtil.log("Request '" + requestType.getRequestTypeValue() + "' is NOT present." + " All requests are:"); //LOGGER INFO
        urlLists.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    @Step("Get request with type {requestType} and value {valueInBody} in body")
    public static String getRequestWithValueInBody(RequestType requestType, String valueInBody, Duration... timeout) {
        Duration timeoutValue = Duration.ofSeconds(timeout.length > 0 ? (int) timeout[0].toSeconds() : config().waitLongTimeoutSeconds());
        ArrayList<String> urlList = new ArrayList<>();
        takeScreenshot();
        String requestUrl = "";
        long start = System.currentTimeMillis();
        while (requestUrl.isEmpty()) {
            urlList = new ArrayList<>();
            for (int i = messagesByThread.size() - 1; i >= 0; i--) {
                InterceptedMessage message = messagesByThread.get(i);
                urlList.add(getRequestUrl(message));
                if (isRequestSent(message, requestType.getRequestTypeValue())) {
                    try {
                        if (URLDecoder.decode(getRequestBody(message), "UTF-8").contains(valueInBody)) {
                            requestUrl = getRequestUrl(message);
                            ReporterUtil.log("Required URL: ", requestUrl.split("&")); //LOGGER INFO
                            ReporterUtil.log("Request body: ", getRequestBody(message).split("&")); //LOGGER INFO
                            return requestUrl;
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (System.currentTimeMillis() - start > timeoutValue.toMillis()) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                break;
            }
        }

        ReporterUtil.log("Request with url's subdirectory'" + requestType.getRequestTypeValue() + "' is NOT present."); //LOGGER INFO
        urlList.forEach(ReporterUtil::log); //LOGGER INFO - print all URLs
        return null;
    }

    public static String getValueOfParameter(JsonObject object, String parameter) {
        return object.get(parameter).getAsString();
    }

    public static List<String> getValuesOfParameter(JsonObject object, String parameter) {
        JsonArray jsonArray = object.get(parameter).getAsJsonArray();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsString());
        }
        return list;
    }

    public static boolean isParameterPresent(JsonObject object, String parameter) {
        try {
            object.get(parameter);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Step("Block IMA ads")
    public static void blockImaAds() {
        ReporterUtil.log(getOutputOfExecution("env http_proxy=http://localhost:" + config().proxyPort() + " curl -I http://blockima.com/path"));
    }

    @Step("Unblock IMA ads")
    public static void unblockImaAds() {
        ReporterUtil.log(getOutputOfExecution("env http_proxy=http://localhost:" + config().proxyPort() + " curl -I http://unblockima.com/path"));
    }

    @Step("Refresh allow hosts list")
    public static void refreshAllowHosts() {
        String hosts = generateListOfAllowedHosts().replaceAll("\\|", "_");
        ReporterUtil.log(getOutputOfExecution("env http_proxy=http://localhost:" + config().proxyPort() + " curl -I http://updateallowhosts.com/" + hosts));
    }

    @Step("Allow all hosts list")
    public static void allowAllHosts() {
        ReporterUtil.log(getOutputOfExecution("env http_proxy=http://localhost:" + config().proxyPort() + " curl -I http://updateallowhosts.com/"));
    }

    public static void setPrerollType(PrerollType prerollType) {
        if (isIos()) {
            ReporterUtil.log("Set preroll type " + prerollType.getPrerollType());
            updateLaunchArgumentFor(ADS_PREROLL_TYPE, prerollType.getPrerollType());
        }
    }

    public enum PrerollType {
        IMA_AUDIO("imaAudio"),
        IMA_VIDEO("imaVideo"),
        ADSWIZZ("adswizzAudio"),
        DISABLED("disabled");
        private String prerollType;

        private PrerollType(String prerollType) {
            this.prerollType = prerollType;
        }

        public String getPrerollType() {
            return prerollType;
        }

    }

}
