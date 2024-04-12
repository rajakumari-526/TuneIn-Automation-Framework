package com.tunein.reporting.tests.common.explorer;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.EXPLORER;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.REPORTS_ASHX;
import static com.tunein.mobile.reporting.explorer.ExplorerPageReporting.REPORT_EXPLORER_MAP_FILTER_SELECT;
import static com.tunein.mobile.reporting.explorer.ExplorerPageReporting.REPORT_EXPLORER_MAP_LAUNCH_MAP_VIEW_SESSION_ID;
import static com.tunein.mobile.testdata.TestGroupName.EXPLORER_TEST;

public class ExplorerPageTest extends BaseTest {

    @TestCaseId("752340")
    @Test(description = "Check apply filter event on Map view", groups = {EXPLORER_TEST})
    public void testApplyFilterOnMapView() {
        navigationAction.navigateTo(EXPLORER);
        explorerPage.clickStationFilterByIndex(1);
        String requestUrl = reportingUtil.getRequestWithValueInBody(REPORTS_ASHX, REPORT_EXPLORER_MAP_FILTER_SELECT);
        reportingAsserts.validateThatUrlIsPresent(requestUrl);
    }

    @TestCaseId("752338")
    @Test(description = "Check apply filter sessionId on Map view", groups = {EXPLORER_TEST})
    public void testApplyFilterSessionIdOnMapView() {
        navigationAction.navigateTo(EXPLORER);
        String requestUrl = reportingUtil.getRequestWithValueInBody(REPORTS_ASHX, REPORT_EXPLORER_MAP_LAUNCH_MAP_VIEW_SESSION_ID);
        reportingAsserts.validateThatUrlIsPresent(requestUrl);
    }

}
