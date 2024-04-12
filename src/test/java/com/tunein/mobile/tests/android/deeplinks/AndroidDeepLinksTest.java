package com.tunein.mobile.tests.android.deeplinks;

import com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.deeplinks.DeepLinksTest;

import java.time.Duration;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_BBC_RADIO_5;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.REGWALL_FAVORITES;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.switchToAppContext;

public class AndroidDeepLinksTest extends DeepLinksTest {

    @Override
    public void testAutoFollowDeeplinkSignInColdAndWarmState() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        runAppInBackground(Duration.ofSeconds(2));
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateContentFollowState(true);
        restartApp();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateContentFollowState(true);
    }

    @Override
    public void testAutoFollowDeeplinkSignOut() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateContentFollowState(true);
    }

    @Override
    public void testDeeplinkWithAutofollowWhileDifferentPagesAreOpened(NavigationActionItems navigationActionItem) {
        updateLaunchArgumentFor(REGWALL_FAVORITES, "false");
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(navigationActionItem);
        if (navigationActionItem == UPSELL) {
            switchToAppContext(NATIVE_APP);
        }
        if (navigationActionItem == CARMODE) {
            carModePage.clickOnExitCardModeMode();
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }
}
