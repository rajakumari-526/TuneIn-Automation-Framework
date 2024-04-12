package com.tunein.mobile.tests.android.browsies;

import com.tunein.mobile.pages.BasePage.CategoryType;
import com.tunein.mobile.tests.common.browsies.BrowsiesTest;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.MUSIC;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;

public class AndroidBrowsiesTest extends BrowsiesTest {

    @Override
    public void testGreenIconBadgeIsAbsentForPodcastOnBrowsies() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        List<CategoryType> list = Arrays.asList(CATEGORY_TYPE_TOP_PODCASTS, CATEGORY_TYPE_POPULAR_PODCASTS_IN_YOUR_AREA);
        for (CategoryType categoryType:list) {
            navigationAction.navigateTo(HOME);
            int index = homePage.getIndexOfFirstContentGreenBadgeStatusInCategory(categoryType, false, true, true);
            if (index > 0) {
                takeScreenshot();
                homePage.openContentUnderCategoryWithHeader(categoryType, TILE, MEDIUM, index, false);
                break;
            }
        }
        contentProfilePage.validateVisibilityOfGreenCircleForNewEpisode(false);
    }

    @Override
    public void testDeepBrowse() {
        navigationAction.navigateTo(HOME);
        navigationAction.navigateToBrowsies(MUSIC);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TOP_MUSIC_GENRES, LIST, SHORT, 2, false);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapGoToProfileButton();
        contentProfilePage.validateContentProfilePageIsOpened();

        deviceNativeActions.clickBackButton();
        contentsListPage.waitUntilPageReady();
        contentsListPage.validateContentsListPageIsDisplayed();

        deviceNativeActions.clickBackButton();
        if (!homePage.isOnHomePage()) {
            deviceNativeActions.clickBackButton();
        }
        homePage.waitUntilPageReady();
        homePage.validateHomePageIsOpened();
    }
}
