package com.tunein.reporting.tests.common.authentication;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Users;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.authentication.SignUpReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.SIGNUP_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;

public abstract class SignUpTest extends BaseTest {

    @Ignore
    @TestCaseId("739464")
    @Test(description = "Check Create Facebook step 1", groups = {SIGNUP_TEST})
    public void testCreateFacebookStepOne() {
        navigationAction.navigateTo(FACEBOOK_SIGNUP_FORM);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_CONTINUE_WITH_FACEBOOK);
    }

    @Ignore
    @TestCaseId("739463")
    @Test(description = "Check Create Complete event", groups = {SIGNUP_TEST})
    public void testCreateCompleteEvent() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.signUpUserByEmail(randomUser);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_SIGNUP_COMPLETE);
    }

    @Ignore
    @TestCaseId("739462")
    @Test(description = "Check Create Step 1 event", groups = {SIGNUP_TEST})
    public void testCreateStepOneEvent() {
        navigationAction.navigateTo(SIGNUP_FORM);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_EMAIL_SIGN_UP_STEP1);
    }

    @Ignore
    @TestCaseId("739465")
    @Test(description = "Check Create Google step 1", groups = {SIGNUP_TEST})
    public void testCreateGoogleStepOne() {
        navigationAction.navigateTo(GOOGLE_SIGNUP_FORM);
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_CONTINUE_WITH_GOOGLE);
    }

    @Ignore
    @TestCaseId("739471")
    @Test(description = "Check Cancel google apple Facebook flow event", groups = {SIGNUP_TEST})
    public abstract void testCancelGoogleAppleFacebookFlowEvent();
}
