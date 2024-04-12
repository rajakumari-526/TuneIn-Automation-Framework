package com.tunein.reporting.tests.common.authentication;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.REGWALL;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNIN_FORM;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.authentication.SignInReporting.REPORT_LOGIN_START;
import static com.tunein.mobile.reporting.authentication.SignInReporting.REPORT_SIGN_IN_COMPLETE;
import static com.tunein.mobile.reporting.authentication.SignInReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.SIGNIN_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_FOR_LOGIN_TEST;

public class SignInSignOutTest extends BaseTest {

    @Ignore
    @TestCaseIds({@TestCaseId("729873"), @TestCaseId("717904")})
    @Test(description = "Check report for Sign In", groups = {SIGNIN_TEST})
    public void testSignInStartAndComplete() {
        signInPage.signInFlowForUser(USER_FOR_LOGIN_TEST);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SIGN_IN_COMPLETE);
    }

    @Ignore
    @TestCaseId("739475")
    @Test(description = "Check Regwall Show event", groups = {SIGNIN_TEST})
    public void testTapSignInEvent() {
        navigationAction.navigateTo(REGWALL);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SETTINGS_TAP_SIGN_IN);
    }

    @Ignore
    @TestCaseId("739460")
    @Test(description = "Check Login start event", groups = {SIGNIN_TEST})
    public void testLoginStartEvent() {
        navigationAction.navigateTo(SIGNIN_FORM);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_LOGIN_START);
    }

    @Ignore
    @TestCaseId("739458")
    @Test(description = "Check Open RegWall event", groups = {SIGNIN_TEST})
    public void testOpenRegwallEvent() {
        navigationAction.navigateTo(REGWALL);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_SCREEN_STEP0);
    }

    @Ignore
    @TestCaseId("739459")
    @Test(description = "Check Close RegWall event", groups = {SIGNIN_TEST})
    public void testCloseRegwallEvent() {
        navigationAction.navigateTo(REGWALL);
        regWallPage.closeRegWallPage();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_SKIP_COMPLETE);
    }

}
