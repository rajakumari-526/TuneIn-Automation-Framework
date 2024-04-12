package com.tunein.reporting.tests.common.browsies;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.FOR_YOU;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.browsies.BrowsiesReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.SIGNIN_TEST;

public class BrowsiesTest extends BaseTest {

    @Ignore
    @TestCaseIds({@TestCaseId("739105"), @TestCaseId("739106")})
    @Test(description = "Check message for each browsies tab", groups = {SIGNIN_TEST})
    public void browsiesEventReportingTest() {
        navigationAction.navigateTo(HOME);
        homePage
                .tapOnAllBrowsiesBarTabs()
                .tapOnRequiredBrowsiesBarTab(FOR_YOU);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = Arrays.asList(new String[]{REPORT_BROWSIES_FOR_YOU, REPORT_BROWSIES_LOCAL_RADIO, REPORT_BROWSIES_SPORTS,
                REPORT_BROWSIES_I_HEART_RADIO, REPORT_BROWSIES_NEWS_AND_TALK, REPORT_BROWSIES_PODCASTS, REPORT_BROWSIES_MUSIC, REPORT_BROWSIES_BY_LANGUAGE, REPORT_BROWSIES_BY_LOCATION});
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }

}
