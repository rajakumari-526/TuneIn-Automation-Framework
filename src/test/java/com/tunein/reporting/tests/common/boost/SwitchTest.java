package com.tunein.reporting.tests.common.boost;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.utils.LaunchArgumentsUtil;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.tunein.mobile.reporting.ReportingConstants.RequestType.REPORTS_ASHX;
import static com.tunein.mobile.reporting.explorer.ExplorerPageReporting.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITH_SWITCH;
import static com.tunein.mobile.utils.ApplicationUtil.reinstallApp;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BOOST_TOOLTIP_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArguments;
import static com.tunein.mobile.utils.ReportingUtil.getRequestWithValueInBody;

public abstract class SwitchTest extends BaseTest {

    @TestCaseId("752210")
    @Test(description = "Check [Tooltip] Show/Optout/Optin reports", groups = {NOW_PLAYING_TEST, SWITCH_TEST, ADS_PARAMETERS})
    public void testShowOptInOptOutReports() {
        String requiredURL;
        Map<LaunchArgumentsUtil.LaunchArgumentsTypes, String> arguments = new HashMap<>();
        arguments.put(BOOST_TOOLTIP_ENABLED, "true");
        arguments.put(BOOST_ENABLED, "true");
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        switchToolTipDialog.tapOnLetsSwitchButton();
        reinstallApp();
        updateLaunchArguments(arguments);
        upsellPage.closeUpsell();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITH_SWITCH);
        switchToolTipDialog
                .waitUntilPageReady()
                .validateSwitchToolTipDialogBoxIsDisplayed();
        switchToolTipDialog.tapOnSwitchToolTipNotNowButton();
        nowPlayingPage.closeGotItPrompt();

        requiredURL = getRequestWithValueInBody(REPORTS_ASHX, BOOST_SHOW_TOOL_TIP);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        requiredURL = getRequestWithValueInBody(REPORTS_ASHX, BOOST_OPT_IN_TOOL_TIP);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
        requiredURL = getRequestWithValueInBody(REPORTS_ASHX, BOOST_OPT_OUT_TOOL_TIP);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }
}
