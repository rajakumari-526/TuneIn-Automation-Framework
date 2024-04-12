package com.tunein.mobile.tests.common.interruptions;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_SHOWS;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.STATION_TYPE_SHOW;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.FOR_YOU;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.SPORTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public abstract class InterruptionsTest extends BaseTest {

    @TestCaseIds({@TestCaseId("23402"), @TestCaseId("576054")})
    @Test(description = "Check NowPlaying Cold state ", groups = {NOW_PLAYING_TEST, INTERRUPTIONS_TEST})
    public void testNowPlayingColdState() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        restartApp();
        continueListeningDialog.tapOnContinuePlayButtonIfDisplayed();
        nowPlayingPage.validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("23401"), @TestCaseId("576055")})
    @Test(description = "Check NowPlaying Warm state ", groups = {NOW_PLAYING_TEST, INTERRUPTIONS_TEST})
    public void testNowPlayingWarmState() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()))
                .minimizeNowPlayingScreen();
        navigationAction
                .mainNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    runAppInBackground(Duration.ofSeconds(3));
                    miniPlayerPage.validateUIElements(miniPlayerPage.miniPlayerPageElements(true));
                });
    }

    @TestCaseIds({@TestCaseId("221824"), @TestCaseId("130612")})
    @Test(description = "content profile Cold state", groups = {CONTENT_PROFILE_TEST, INTERRUPTIONS_TEST})
    public void testContentProfileColdState() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        restartApp();
        upsellPage.closeUpsell();
        continueListeningDialog.closeContinueListeningDialogIfDisplayed();
        homePage.validateHomePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("30133"), @TestCaseId("221823")})
    @Test(description = "content profile Warm state", groups = {CONTENT_PROFILE_TEST, INTERRUPTIONS_TEST})
    public void testContentProfileWarmState() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_SHOWS, LIST, SHORT, 3, false, true);
        runAppInBackground(Duration.ofSeconds(1));
        contentProfilePage.validateContentProfilePageIsOpened();
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(STATION_TYPE_SHOW));
    }

    @TestCaseIds({
            @TestCaseId("729991"),
            @TestCaseId("729906"),
            @TestCaseId("730045"),
            @TestCaseId("729920"),
            @TestCaseId("22832"),
            @TestCaseId("730244"),
            @TestCaseId("730210"),
            @TestCaseId("730271"),
            @TestCaseId("730209"),
            @TestCaseId("22833")
    })
    @Test(
            description = "Cold/Warm state",
            groups = {BROWSIES_TEST, INTERRUPTIONS_TEST}
    )
    public void testBrowsiesColdWarmState() {
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        runAppInBackground(Duration.ofSeconds(3));
        homePage
                .validateThatBrowsiesBarTabIsSelected(SPORTS)
                .tapOnRequiredBrowsiesBarTab(FOR_YOU);
        homePage.validateThatAllBrowsiesBarTabsCanBeSelected();
        restartApp();
        upsellPage.closeUpsell();
        homePage.validateThatBrowsiesBarTabIsSelected(FOR_YOU);
        homePage.validateThatAllBrowsiesBarTabsCanBeSelected();
    }

    @TestCaseId("749178")
    @Test(description = "App behaviour after crash", groups = {ACCEPTANCE_TEST, INTERRUPTIONS_TEST})
    public abstract void testAppBehaviourAfterCrash();

}
