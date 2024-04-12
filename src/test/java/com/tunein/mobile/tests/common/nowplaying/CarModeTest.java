package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.CARMODE;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;

public abstract class CarModeTest extends BaseTest {
    @TestCaseIds({@TestCaseId("22971"), @TestCaseId("29697")})
    @Test(description = "Check UI Elements of Car Mode", groups = {NOW_PLAYING_TEST, SMOKE_TEST})
    public void testCarModeUIElements() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        String stationTitle = nowPlayingPage.getTextValueOfNowPlayingTitle();
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);
        carModePage.validateUIElements(carModePage.carModeElements());
        carModePage.validateCarModeTitleIsEqualTo(stationTitle);
    }

    @TestCaseIds({@TestCaseId("37891"), @TestCaseId("24641"), @TestCaseId("749202")})
    @Test(description = "Check Car Mode controls", groups = {CARMODE_TEST, ACCEPTANCE_TEST})
    public abstract void testCarModeControls();

}
