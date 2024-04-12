package com.tunein.reporting.tests.android.upsell;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.utils.ApplicationUtil;
import com.tunein.reporting.tests.common.upsell.UpsellTest;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PREMIUM;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.UPSELL_TEST;

public class AndroidUpsellTest extends UpsellTest {

    @TestCaseId("739441")
    @Test(description = "Check Back button on Upsell event", groups = {UPSELL_TEST})
    public void testBackButtonOnUpsellEvent() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        deviceNativeActions.clickBackButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SUBSCRIBE_CANCEL_FIRST_LAUNCH_UPSELL);
    }

    @Override
    public void testFirstLaunchUpsellEvent() {
        ApplicationUtil.reinstallApp();
        upsellPage
                .waitUntilPageReady()
                .tapOnStartListeningFreeButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(REPORT_SUBSCRIBE_SHOW_FIRST_LAUNCH_UPSELL);
        expectedValues.add(REPORT_SUBSCRIBE_TAP_FIRST_LAUNCH_UPSELL);
        reportingAsserts.assertListOfRequestWithDataIsSent(messages, expectedValues);
    }

}
