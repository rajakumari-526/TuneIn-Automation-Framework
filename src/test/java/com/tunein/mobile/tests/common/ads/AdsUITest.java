package com.tunein.mobile.tests.common.ads;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BANNER_ADS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.PLAYER_AUTOPLAY_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateDefaultLaunchArgumentList;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class AdsUITest extends BaseTest {

    @TestCaseId("753861")
    @Test(description = "Check banner and preroll for premium logged out user", groups = {ADS_TEST, BANNER_TEST})
    public void testBannerAndPrerollForPremiumLoggedOutUser() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(PROFILE);
        userProfilePage.tapOnSignOutButton();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        deepLinksUtil.openURL(STREAM_STATION_IMA_VIDEO.getStreamTuneDeepLink());
        nowPlayingPage
                .validateThatPreRollIsDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateOneOfTheMaxBannersIsDisplayed();
    }

    @TestCaseId("753862")
    @Test(description = "Check banner, preroll, nowplaying ad banner after fresh install", groups = {ADS_TEST, BANNER_TEST})
    public void testBannerPrerollNowplayingAdBannerAfterFreshInstall() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        navigationAction.navigateTo(HOME);
        homePage.validateAdBannerDisplayed(true);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        nowPlayingPage.validateThatPreRollIsAbsent();
        deepLinksUtil.openURL(STREAM_STATION_IMA_VIDEO_SECOND.getStreamTuneDeepLink());
        customWait(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        nowPlayingPage
                .validateThatPreRollIsDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateOneOfTheMaxBannersIsDisplayed();
    }

    @TestCaseIds({@TestCaseId("753810"), @TestCaseId("753869")})
    @Test(description = "Check ads banner for non premium users", groups = {ADS_TEST, BANNER_TEST})
    public void testAdsBannerForNonPremiumUsers() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        navigationAction.navigateTo(HOME);
        homePage.validateAdBannerDisplayed(true);
        signInPage.signInFlowForUser(USER_GENERAL);
        homePage.validateAdBannerDisplayed(true);
        userProfilePage.signOutUserFlow();
        homePage.validateAdBannerDisplayed(true);
    }

    @TestCaseId("753812")
    @Test(description = "check Max 320x50 Appearance", groups = {ADS_TEST, BANNER_TEST})
    public void testMax320x50Appearance() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        navigationAction.navigateTo(HOME);
        homePage.validateAdBannerDisplayed(true);
        List<HomePage.BrowsiesBarTabsLabels> browsieList = Arrays.asList(SPORTS, PODCASTS, MUSIC);
        browsieList.forEach(browsieName -> {
            navigationAction.navigateToBrowsies(browsieName);
            homePage.validateAdBannerDisplayed(true);
        });

        navigationAction.navigateTo(LIBRARY);
        homePage.validateAdBannerDisplayed(true);
        navigationAction.navigateTo(SEARCH);
        homePage.validateAdBannerDisplayed(true);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        nowPlayingPage.validateThatNowPlayingIsOpened()
                .validateOneOfTheMaxBannersIsDisplayed();
        nowPlayingPage.minimizeNowPlayingScreen()
                .validateAdBannerDisplayed(true);
        miniPlayerPage.tapOnMiniPlayer();
        nowPlayingPage.validateThatNowPlayingIsOpened()
                .validateOneOfTheMaxBannersIsDisplayed();
    }

    @TestCaseId("753803")
    @Test(description = "Click on Max 300x250 banner in NP", groups = {ADS_TEST, BANNER_TEST})
    public void testClickOnMax300X250BannerInNowPlaying() {
        updateLaunchArgumentFor(BANNER_ADS, "true");

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_ADSWIZZ_AD);
        nowPlayingPage
                .validateNowPlayingAdBannerDisplayed(true)
                .tapOnNowPlayingAdBanner();
        nowPlayingPage.validateWebPageIsDisplayed();
    }

    @TestCaseId("750428")
    @Test(description = "Check IMA Preroll Auto-Play", groups = {ADS_TEST, PREROLL_TEST})
    public void testIMAPrerollAutoPlay() {
        updateLaunchArgumentFor(PLAYER_AUTOPLAY_ENABLED, "true");
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_ADSWIZZ_AD);
        nowPlayingPage.validateNowPlayingAdBannerDisplayed(true);
        restartApp(updateDefaultLaunchArgumentList(PLAYER_AUTOPLAY_ENABLED, "true"));
        upsellPage.closeUpsell();
        nowPlayingPage.validateThatPreRollIsDisplayed();
    }

    @TestCaseId("753811")
    @Test(description = "Click on Max 320x50 banner at Home Page", groups = {ADS_TEST, BANNER_TEST})
    public void testClickOnMax320x50BannerAtHomePage() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        homePage.validateAdBannerDisplayed(true)
                .clickAdBanner()
                .validateWebPageIsDisplayed();
    }

    @TestCaseId("753853")
    @Test(description = "Check banner for ads-free station", groups = {ADS_TEST, BANNER_TEST})
    public void testBannerForAdsFreeStation() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5);
        nowPlayingPage
                .validateThatPreRollIsAbsent()
                .validateNowPlayingAdBannerDisplayed(false)
                .minimizeNowPlayingScreen()
                .validateAdBannerDisplayed(false);
    }

    @TestCaseId("753854")
    @Test(description = "Verify Stop playing ads-free station", groups = {ADS_TEST, BANNER_TEST})
    public abstract void testStopPlayingAdsFreeStation();

    @TestCaseId("753855")
    @Test(description = "Check ad banner on different pages for different stations", groups = {ADS_TEST, BANNER_TEST})
    public void testAdBannerOnDifferentPagesForDifferentStations() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5);
        nowPlayingPage
                .validateAdBannerDisplayed(false);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KFJC);
        nowPlayingPage
                .validateOneOfTheMaxBannersIsDisplayed()
                .minimizeNowPlayingScreen();
        List<NavigationAction.NavigationActionItems> listOfPages = Arrays.asList(LIBRARY, SEARCH, HOME);
        customWait(Duration.ofSeconds(config().oneMinuteInSeconds()));
        for (NavigationAction.NavigationActionItems page : listOfPages) {
            navigationAction.navigateTo(page);
            nowPlayingPage.validateAdBannerDisplayed(true);
        }
    }

}
