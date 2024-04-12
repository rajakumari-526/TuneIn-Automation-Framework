package com.tunein.reporting.tests.ios.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.reporting.tests.common.authentication.SignInSignOutTest;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.REGWALL;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.authentication.SignInReporting.REPORT_REGWALL_SCREEN_PRIVACY;
import static com.tunein.mobile.reporting.authentication.SignInReporting.REPORT_REGWALL_SCREEN_TERMS;
import static com.tunein.mobile.testdata.TestGroupName.SIGNIN_TEST;

public class IosSignInSignOutTest extends SignInSignOutTest {

    @TestCaseId("739473")
    @Test(description = "Check Terms of Service and Privacy links events", groups = {SIGNIN_TEST})
    public void testTermsOfServiceAndPrivacyLinksEvent() {
        navigationAction.navigateTo(REGWALL);
        regWallPage.tapTermsOfServiceLink();
        termsOfServicePage.tapBackButton();
        regWallPage.tapPrivacyPolicyLink();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = Arrays.asList(new String[]{REPORT_REGWALL_SCREEN_TERMS, REPORT_REGWALL_SCREEN_PRIVACY});
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }

}
