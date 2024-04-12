package com.tunein.reporting.tests.android.authentication;

import com.tunein.reporting.tests.common.authentication.SignUpTest;
import io.appium.mitmproxy.InterceptedMessage;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.REGWALL;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.authentication.SignUpReporting.REPORT_REGWALL_CANCEL_WITH_FACEBOOK;
import static com.tunein.mobile.reporting.authentication.SignUpReporting.REPORT_REGWALL_CANCEL_WITH_GOOGLE;

public class AndroidSignUpTest extends SignUpTest {

    public void testCancelGoogleAppleFacebookFlowEvent() {
        navigationAction.navigateTo(REGWALL);
        regWallPage.cancelSignUpFlowWithGoogleButton();
        regWallPage.cancelSignUpFlowWithFacebookButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(REPORT_REGWALL_CANCEL_WITH_GOOGLE);
        expectedValues.add(REPORT_REGWALL_CANCEL_WITH_FACEBOOK);
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }
}
