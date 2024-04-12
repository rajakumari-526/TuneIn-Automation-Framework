package com.tunein.mobile.tests.common.deeplinks;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.DEEPLINK_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_UNICC;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_BASEBALL_CATEGORY;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.REGWALL_FAVORITES;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.switchToAppContext;

public abstract class DeepLinksTest extends BaseTest {

    @TestCaseId("718908")
    @Test(description = "Test deeplink while Premium page Upsell is open", groups = {DEEPLINK_TEST})
    public void testDeeplinkWhilePremiumPageUpsellIsDisplayed() {
        navigationAction.navigateTo(UPSELL);
        switchToAppContext(NATIVE_APP);
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_BASEBALL_CATEGORY);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
    }

    @TestCaseId("718908")
    @Test(description = "Test deeplink while Don't like ads Upsell is open", groups = {DEEPLINK_TEST})
    public void testDeeplinkWhileDontLikeAdsUpsellIsDisplayed() {
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS)
                .stopStreamPlaying();
        nowPlayingPage.tapOnNoAdsButton();
        switchToAppContext(NATIVE_APP);
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_BASEBALL_CATEGORY);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
    }

    @TestCaseId("718908")
    @Test(description = "Check Deeplink While Different Pages Are Displayed",
            dataProviderClass = ContentProvider.class, dataProvider = "deepLinkPagesDataProviders", groups = {DEEPLINK_TEST})
    public void testDeeplinkWhileDifferentPagesAreDisplayed(NavigationActionItems navigationScreen) {
        navigationAction.navigateTo(navigationScreen);
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_BASEBALL_CATEGORY);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
    }

    @TestCaseIds({@TestCaseId("24067"), @TestCaseId("24068"), @TestCaseId("717972"), @TestCaseId("717973")})
    @Test(description = "Auto follow Deep-link (Signin) (Cold/Warm State)", groups = {DEEPLINK_TEST})
    public abstract void testAutoFollowDeeplinkSignInColdAndWarmState();

    @TestCaseId("24070")
    @Test(description = "Auto follow Deep-link-sign-out state", groups = {DEEPLINK_TEST})
    public abstract void testAutoFollowDeeplinkSignOut();

    @TestCaseId("566872")
    @Test(description = "Open some pages after regwall deeplink", groups = {DEEPLINK_TEST})
    public void testOpenRegWallDeeplinkAndTestFewStations() {
        deepLinksUtil.openRegWallThroughDeeplink();
        regWallPage
                .validateRegwallPageIsDisplayed()
                .tapOnRegWallCloseButton();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.tapProfilePlayButton(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()))
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);
        carModePage
                .validateThatCarModePageIsDisplayed()
                .clickOnExitCardModeMode();
        navigationAction.navigateTo(REGWALL);
        regWallPage.validateRegwallPageIsDisplayed();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.tapProfilePlayButton(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("717970"), @TestCaseId("717971")})
    @Test(description = "Test deeplink while different pages are opened and auto followed",
            dataProviderClass = NavigationAction.class,
            dataProvider = "deepLinkNavigations",
            groups = {DEEPLINK_TEST})
    public abstract void testDeeplinkWithAutofollowWhileDifferentPagesAreOpened(NavigationAction.NavigationActionItems navigationActionItem);

    @TestCaseIds({@TestCaseId("717970"), @TestCaseId("717971")})
    @Test(description = "Test deeplink while Nowplaying page is open", groups = {DEEPLINK_TEST})
    public void testDeeplinkWithAutofollowWhileNowplayingPageisOpened() {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "false");
        navigationAction.navigateTo(HOME);
        deepLinksUtil
                .openTuneThroughDeeplink(STREAM_STATION_UNICC)
                .validateThatNowPlayingIsOpened();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.validateThatNowPlayingIsOpened();
        }

}
