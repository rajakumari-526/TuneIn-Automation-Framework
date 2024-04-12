package com.tunein.mobile.tests.ios.nowplaying;

import com.tunein.mobile.tests.common.nowplaying.CarModeTest;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.CARMODE;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_WITH_FAVORITES;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class IosCarModeTest extends CarModeTest {

    @Override
    public void testCarModeControls() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);

        // TODO: https://tunein.atlassian.net/browse/IOS-16835 Car mode play/pause button listed as not visible, so can't be interacted with

        carModePage.clickOnCarModeRecentsButton();
        contentsListPage.openContentUnderCategoryWithHeader(RECENTS, LIST, SHORT, 1, false);
        carModePage.clickOnCarModeRecommendedButton();
        contentsListPage.openContentUnderCategoryWithHeader(RECOMMENDED, LIST, SHORT, 1, false);
        carModePage.clickOnCarModeFavoritesButton();
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);

        carModePage.clickOnExitCardModeMode();
        miniPlayerPage
                .waitUntilPageReady()
                .validateMiniPlayerIsDisplayed();
    }
}
