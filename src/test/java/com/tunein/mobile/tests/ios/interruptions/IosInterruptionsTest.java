package com.tunein.mobile.tests.ios.interruptions;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.tests.common.interruptions.InterruptionsTest;
import com.tunein.mobile.utils.ApplicationUtil;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.ABOUT;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.NEED_HELP;
import static com.tunein.mobile.testdata.TestGroupName.INTERRUPTIONS_TEST;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.utils.ApplicationUtil.activateApp;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;

public class IosInterruptionsTest extends InterruptionsTest {

    @TestCaseIds ({@TestCaseId("712787"), @TestCaseId("749474")})
    @Test(description = "Check NowPlaying page after returning to app from browser", groups = {NOW_PLAYING_TEST, INTERRUPTIONS_TEST})
    public void testNowPlayingAfterReturnFromBrowser() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(NEED_HELP);
        activateApp();
        nowPlayingPage.validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @Override
    public void testAppBehaviourAfterCrash() {
        navigationAction.navigateTo(ABOUT);
        aboutPage.tapOnCrashAppSection(DOWN, 10);
        homePage.verifyAppIsNotRunning();
        ApplicationUtil.launchApp();
        upsellPage.closeUpsell();
        homePage.validateHomePageIsOpened();
    }
}
