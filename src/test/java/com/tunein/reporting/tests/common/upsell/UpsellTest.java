package com.tunein.reporting.tests.common.upsell;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import io.appium.mitmproxy.InterceptedMessage;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PREMIUM;
import static com.tunein.mobile.reporting.ReportingConstants.*;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.REPORT_SUBSCRIBE_SHOW_PREMIUM_UPSELL;
import static com.tunein.mobile.testdata.TestGroupName.UPSELL_TEST;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.REPORT_SUBSCRIBE_TAP_HOME_UPSELL;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PROFILE;
import static com.tunein.mobile.reporting.upsell.UpsellReporting.REPORT_SUBSCRIBE_SHOW_SETTINGS_UPSELL;

public abstract class UpsellTest extends BaseTest {

    @Ignore
    @TestCaseId("739637")
    @Test(description = "Check Open upsell from Premium page", groups = {UPSELL_TEST})
    public void testUpsellFromPremiumEvent() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SUBSCRIBE_SHOW_PREMIUM_UPSELL);
    }

    @Ignore
    @TestCaseId("739636")
    @Test(description = "Check event for Open upsell from Home", groups = {UPSELL_TEST})
    public void testOpenUpsellFromHomeEvent() {
        navigationAction.navigateTo(HOME);
        homePage.scrollToAndTapFreeTrialButton(config().scrollLotsOfTimes());
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SUBSCRIBE_TAP_HOME_UPSELL);
    }

    @Ignore
    @TestCaseId("739638")
    @Test(description = "Check First Launch", groups = {UPSELL_TEST})
    public abstract void testFirstLaunchUpsellEvent();

    @Ignore
    @TestCaseId("739639")
    @Test(description = "Check Open upsell from Settings", groups = {UPSELL_TEST})
    public void testUpsellFromSettingsEvent() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnTuneInPremiumButton();
        tuneInPremiumPage.tapUpgradeToPremiumButton();
        List<InterceptedMessage> messages = reportingUtil.getListOfRequestsByType(REPORT_REQUEST, REPORT_REQUEST_QUERY_NAME_C, REPORT_REQUEST_QUERY_VALUE_EVENTLIST);
        reportingAsserts.assertRequestWithDataIsSent(messages, REPORT_SUBSCRIBE_SHOW_SETTINGS_UPSELL);
    }

}
