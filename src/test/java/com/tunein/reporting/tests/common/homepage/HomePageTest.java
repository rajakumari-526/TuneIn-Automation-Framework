package com.tunein.reporting.tests.common.homepage;

import com.google.gson.JsonObject;
import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.reporting.homepage.HomePageReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.HOMEPAGE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.UNIFIED_EVENTS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_PREMIER_LEAGUE_CATEGORY;
import static com.tunein.mobile.utils.ReportingUtil.getLastUnifiedEventRequestByType;

public abstract class HomePageTest extends BaseTest {

    @Ignore
    @TestCaseId("739330")
    @Test(description = "Check Interests selection", groups = {HOMEPAGE_TEST})
    public void testPickYourTeamEvent() {
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_PREMIER_LEAGUE_CATEGORY);
        contentsListPage.tapOnPickYourTeams();
        teamsPage.tapOnCancelButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(REPORT_SPORTS_INTEREST_SELECTION_SHOW);
        expectedValues.add(REPORT_SPORTS_INTEREST_SELECTION_DISMISS);
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }

    @Ignore
    @TestCaseId("739330")
    @Test(description = "Check favorites call in interests selection", groups = {HOMEPAGE_TEST})
    public void testFollowTeamsEvent() {
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_PREMIER_LEAGUE_CATEGORY);
        contentsListPage.tapOnPickYourTeams();
        teamsPage.selectNumberOfTeams(1)
                 .tapOnFollowTeamsButton();
        upsellPage.closeUpsell();
        List<InterceptedMessage> eventMessages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(eventMessages, REPORT_SPORTS_INTEREST_SELECTION_SAVE);
        List<InterceptedMessage> favMessages = reportingUtil.getListOfRequestsByType(FAVORITES_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_ADDINTERESTS);
        String id = reportingUtil.getQueryParamValue(favMessages.get(0), "id").replaceAll("[^a-zA-Z0-9]", " ").trim();
        String deeplink = "tunein://profile/" + id;
        deepLinksUtil.openURL(deeplink);
        contentProfilePage
                .waitUntilPageReady()
                .validateContentProfilePageIsOpened();
    }

    @TestCaseId("752071")
    @Test(description = "Check unified event APP_SESSION_STARTED", groups = {HOMEPAGE_TEST, UNIFIED_EVENTS})
    public void testAppSessionStartedEvent() {
        signInPage.signInFlowForUser(USER_GENERAL);
        restartApp();
        JsonObject data = getLastUnifiedEventRequestByType(UNIFIED_EVENT_APP_SESSION_STARTED);
        userProfilePage.signOutUserFlow();
        unifiedEventsAsserts.validateThatEventIsPresent(data, UNIFIED_EVENT_APP_SESSION_STARTED);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(data, UNIFIED_EVENT_JSON_PARAM_SESSION_ID);
        unifiedEventsAsserts.validateUnifiedEventCommonParams(data);
        unifiedEventsAsserts.verifyUnifiedEventContextListeningInfoIsEmpty(data);
        unifiedEventsAsserts.verifyUnifiedEventContextUserInfoAfterSignIn(data);
    }

}
