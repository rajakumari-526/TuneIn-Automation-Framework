package com.tunein.reporting.tests.ios.upsell;

import com.tunein.reporting.tests.common.upsell.UpsellTest;
import io.appium.mitmproxy.InterceptedMessage;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.REPORT_SUBSCRIBE_SHOW_FIRST_LAUNCH_UPSELL;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.REPORT_SUBSCRIBE_TAP_FIRST_LAUNCH_UPSELL;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class IosUpsellTest extends UpsellTest {

    @Override
    public void testFirstLaunchUpsellEvent() {
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
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
