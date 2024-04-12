package com.tunein.mobile.tests.ios.deeplinks;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.deeplinks.DeepLinksTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileButtons.FAVORITE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.CARMODE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.TestGroupName.DEEPLINK_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_BBC_RADIO_5;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.REGWALL_FAVORITES;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class IosDeepLinksTest extends DeepLinksTest {

    @Override
    public void testAutoFollowDeeplinkSignInColdAndWarmState() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        runAppInBackground(Duration.ofSeconds(2));
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .goToStreamProfile();
        contentProfilePage.validateFavoriteButton(FAVORITE);
        restartApp();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .goToStreamProfile();
        contentProfilePage.validateFavoriteButton(FAVORITE);
    }

    @Override
    public void testAutoFollowDeeplinkSignOut() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .goToStreamProfile();
        contentProfilePage.validateFavoriteButton(FAVORITE);
    }

    @Override
    public void testDeeplinkWithAutofollowWhileDifferentPagesAreOpened(NavigationAction.NavigationActionItems navigationActionItem) {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "false");
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(navigationActionItem);
        if (navigationActionItem == CARMODE) {
            carModePage.clickOnExitCardModeMode();
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseIds({@TestCaseId("717970"), @TestCaseId("717971")})
    @Test(description = "Test deeplink while settings is open",
            dataProviderClass = NavigationAction.class,
            dataProvider = "deepLinkNavigations",
            groups = {DEEPLINK_TEST})
    public void testDeeplinkWithGuideIdWhileDifferentPagesAreOpened(NavigationAction.NavigationActionItems navigationActionItem) {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "false");
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(navigationActionItem);
        if (navigationActionItem == CARMODE) {
            carModePage.clickOnExitCardModeMode();
        }
        deepLinksUtil.openDeeplinkWithGuideId(STREAM_STATION_BBC_RADIO_5);
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }
}
