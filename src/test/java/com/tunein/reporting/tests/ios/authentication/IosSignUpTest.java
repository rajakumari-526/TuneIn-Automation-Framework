package com.tunein.reporting.tests.ios.authentication;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.reporting.tests.common.authentication.SignUpTest;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.REGWALL;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNUP_FORM;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.authentication.SignUpReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.SIGNUP_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;

public class IosSignUpTest extends SignUpTest {

    public void testCancelGoogleAppleFacebookFlowEvent() {
        navigationAction.navigateTo(REGWALL);
        regWallPage.cancelSignUpFlowWithGoogleButton();
        regWallPage.cancelSignUpFlowWithFacebookButton();
        regWallPage.cancelSignUpFlowWithAppleButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(REPORT_REGWALL_CANCEL_WITH_GOOGLE);
        expectedValues.add(REPORT_REGWALL_CANCEL_WITH_FACEBOOK);
        expectedValues.add(REPORT_REGWALL_CANCEL_WITH_APPLE);
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }

    @TestCaseId("739468")
    @Test(description = "Check Create Step 2 event", groups = {SIGNUP_TEST})
    public void testCreateStepTwoEvent() {
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.typeEmail(randomUser.getEmail());
        signUpPage.typePassword(randomUser.getPassword());
        signUpPage.pressNextButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_REGWALL_EMAIL_SIGN_UP_STEP2);
    }

}
