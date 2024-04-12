package com.tunein.mobile.tests.common.upsell;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.utils.GestureActionUtil;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.*;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.activateApp;
import static com.tunein.mobile.utils.ApplicationUtil.terminateApp;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public abstract class UpsellTest extends BaseTest {

    @TestCaseIds({@TestCaseId("718233"), @TestCaseId("730587")})
    @Test(description = "[Upsell Screen] Elements", groups = {UPSELL_TEST})
    public abstract void testUpsellUI();

    @TestCaseIds({@TestCaseId("259870"), @TestCaseId("730584")})
    @Test(description = "Close Upsell screen", groups = {UPSELL_TEST})
    public abstract void testUpsellCloseButton();

    @TestCaseIds({@TestCaseId("728513")})
    @Test(description = "Upsell ribbon", groups = {UPSELL_TEST})
    public void testUpsellRibbon() {
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_UNICC)
                .tapOnNoAdsButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("259859"), @TestCaseId("730581")})
    @Test(description = "Profile tab upsell", groups = {UPSELL_TEST})
    public void testProfileUpsell() {
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnTuneInPremiumButton()
                .tapUpgradeToPremiumButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("259869"), @TestCaseId("730583"), @TestCaseId("749149")})
    @Test(description = "[Premium Content] Premium Podcast upsell", groups = {UPSELL_TEST, ACCEPTANCE_TEST})
    public void testPremiumPodcastUpsell() {
        deepLinksUtil
                .openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK)
                .tapProfilePlayButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @Ignore("Premium prompt is disabled for podcasts by dev team")
    @TestCaseIds({@TestCaseId("712964")})
    @Test(description = "[Profile] Premium prompt in the Commercial Free Podcast Profile", groups = {UPSELL_TEST})
    public void testPremiumPodcastFreeTrialButtonUpsell() {
        deepLinksUtil
                .openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK)
                .tapPremiumFreeTrialButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("730591"), @TestCaseId("729986")})
    @Test(description = "[Home] Start Your Free Trial", groups = {UPSELL_TEST})
    public void testHomeFreeTrialButtonUpsell() {
        navigationAction.navigateTo(HOME);
        homePage.scrollToAndTapFreeTrialButton(config().scrollLotsOfTimes());
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("259868"), @TestCaseId("730582")})
    @Test(description = "[Premium Content] premium Podcast (tap on locked content)", groups = {UPSELL_TEST})
    public void testPremiumPodcastLockedContentUpsell() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PREMIUM_PODCAST_NURSE_TALK);
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_PREMIUM_EPISODES, LIST, SHORT, 1, false);
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("724866")})
    @Test(description = "Swiping upsell", groups = {UPSELL_TEST})
    public void testSwipeUpsell() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        GestureActionUtil.swipeDownScreen();
        upsellPage.validateIsOnUpsellPage(true);
    }

    @TestCaseIds({@TestCaseId("729643"), @TestCaseId("729855")})
    @Test(description = "Alexa upsell", groups = {UPSELL_TEST})
    public abstract void testAlexaUpsell();

    @Issue("IOS-16593")
    @TestCaseIds({@TestCaseId("24113")})
    @Test(description = "Check price label in upsell", groups = {UPSELL_TEST})
    public void testUpsellPriceLabel() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        upsellPage.validateUpsellPrice();
    }

    @TestCaseIds({@TestCaseId("221651")})
    @Test(description = "[Subscription] Tuning premium stream from profile as not subscribed user", groups = {UPSELL_TEST, CONTENT_PROFILE_TEST})
    public void testPremiumStreamLockedContentUpsell() {
        deepLinksUtil
                .openContentProfileThroughDeeplink(STREAM_AL_JAZEERA)
                .tapProfilePlayButton();

        upsellPage.validateUIElements(upsellPage.upsellPageElements());
        upsellPage.closeUpsell();

        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_COMMERCIAL_FREE, LIST, MEDIUM, 1, false);
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("717978"), @TestCaseId("51341")})
    @Test(description = "Check app after closing upsell on first launch", groups = {UPSELL_TEST})
    public void testCheckAppAfterClosingUpsellOnFirstLaunch() {
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
        upsellPage.closeUpsell();
        terminateApp();
        customWait(Duration.ofSeconds(30));
        activateApp();
        upsellPage.validateIsOnUpsellPage(false);
        navigationAction.navigateTo(HOME);
        homePage.scrollToAndTapFreeTrialButton(config().scrollLotsOfTimes());
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }
}
