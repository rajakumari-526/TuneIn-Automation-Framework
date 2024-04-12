package com.tunein.mobile.tests.ios.browsies;

import com.tunein.mobile.tests.common.browsies.BrowsiesTest;
import org.testng.SkipException;
import org.testng.annotations.Ignore;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.MUSIC;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class IosBrowsiesTest extends BrowsiesTest {

    @Ignore("Test is absent for iOS Platform because badge locator is not available")
    @Override
    public void testGreenIconBadgeIsAbsentForPodcastOnBrowsies() {
        throw new SkipException("Test is absent for iOS Platform because badge locator is not available");
    }

    @Override
    public void testDeepBrowse() {
        navigationAction.navigateTo(HOME);
        navigationAction.navigateToBrowsies(MUSIC);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TOP_MUSIC_GENRES, LIST, MEDIUM, 3, false);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapGoToProfileButton();
        contentProfilePage.validateContentProfilePageIsOpened();
        deviceNativeActions.clickBackButton();
        contentsListPage.validateContentsListPageIsDisplayed();
        deviceNativeActions.clickBackButton();
        musicPage.validateMusicPageIsOpen();
    }

}
