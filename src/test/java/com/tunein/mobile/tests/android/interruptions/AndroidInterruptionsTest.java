package com.tunein.mobile.tests.android.interruptions;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.tests.common.interruptions.InterruptionsTest;
import com.tunein.mobile.utils.ApplicationUtil;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.ACCEPTANCE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.INTERRUPTIONS_TEST;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToRefresh;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class AndroidInterruptionsTest extends InterruptionsTest {

    @TestCaseId("749230")
    @Test(description = "Verify Internet connection. Retry button", groups = {ACCEPTANCE_TEST, INTERRUPTIONS_TEST})
    public void testInternetConnectionAndRetryButton() {
        navigationAction.navigateTo(SEARCH);
        deviceNativeActions.disableWiFi();
        searchPage.validateThatYouAreOfflineNowErrorDisplayed();
        navigationAction.navigateTo(LIBRARY);
        deviceNativeActions.enableWifi();
        scrollToRefresh();
        libraryPage.validateCustomURLButtonIsDisplayed();
    }

    @Override
    public void testAppBehaviourAfterCrash() {
        navigationAction.navigateTo(SETTINGS);
        aboutPage.tapOnCrashAppSection(DOWN, 12);
        customWait(Duration.ofSeconds(10));
        homePage.verifyAppIsNotRunning();
        ApplicationUtil.launchApp();
        upsellPage.closeUpsellIfDisplayed();
        homePage.validateHomePageIsOpened();
    }

}
