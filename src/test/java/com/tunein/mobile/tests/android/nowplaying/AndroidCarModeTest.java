package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.tests.common.nowplaying.CarModeTest;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.CARMODE;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_WITH_FAVORITES;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class AndroidCarModeTest extends CarModeTest {

    @Override
    public void testCarModeControls() {
        signInPage.signInFlowForUser(USER_WITH_FAVORITES);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);

        // TODO: https://tunein.atlassian.net/browse/DROID-15707 Need identifiers for Car Mode play button state to test play state

        carModePage.clickOnCarModeRecentsButton();
        contentsListPage.openContentUnderCategoryWithHeader(RECENTS, LIST, SHORT, 2, false);
        carModePage.clickOnCarModeRecommendedButton();
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        if (contentsListPage.isOnContentsListPage()) {
            contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        }
        carModePage.clickOnCarModeFavoritesButton();
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);

        deviceNativeActions.clickBackButton();
        carModePage.validateThatCarModePageIsDisplayed();

        carModePage.clickOnExitCardModeMode();
        miniPlayerPage
                .waitUntilPageReady()
                .validateMiniPlayerIsDisplayed();
    }
}
