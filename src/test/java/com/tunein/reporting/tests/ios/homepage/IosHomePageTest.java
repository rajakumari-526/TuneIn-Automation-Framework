package com.tunein.reporting.tests.ios.homepage;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.reporting.tests.common.homepage.HomePageTest;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.homepage.HomePageReporting.REPORT_HOMEPAGE_SUPER_PROMPT;
import static com.tunein.mobile.testdata.TestGroupName.HOMEPAGE_TEST;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;

public class IosHomePageTest extends HomePageTest {

    @Ignore
    @TestCaseId("739329")
    @Test(description = "Check Event for Super Prompt", groups = {HOMEPAGE_TEST})
    public void superPromptEventReportingTest() {
        navigationAction.navigateTo(HOME);
        scrollTo(homePage.homepagePremiumPrompt, DOWN, 5);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_HOMEPAGE_SUPER_PROMPT);
    }
}
