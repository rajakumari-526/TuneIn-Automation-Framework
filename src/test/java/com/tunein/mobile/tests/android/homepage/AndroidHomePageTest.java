package com.tunein.mobile.tests.android.homepage;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.tests.common.homepage.HomePageTest;
import com.tunein.mobile.utils.GestureActionUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.CategoryType;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.GENERATE_RECENT_LIST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_MUSIC_CATEGORY_60S;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_PREMIER_LEAGUE_CATEGORY;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollToRefresh;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;

public class AndroidHomePageTest extends HomePageTest {

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("730029")
    @Test(description = "open last brick cell in landscape mode", groups = {HOMEPAGE_TEST})
    public void testOpenLastBrickCellInLandscapeMode() {
        navigationAction.navigateTo(HOME);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        navigationAction.navigateToBrowsies(PODCASTS);
        homePage.tapOnLastBrickCellUnderCategory(THIS_WEEKS_FEATURED_PODCASTS);
        contentProfilePage.validateContentProfilePageIsOpened();
    }

    @TestCaseIds({@TestCaseId("730038"),
            @TestCaseId("730039"), //Home browses
            @TestCaseId("730040"), @TestCaseId("730239"), //podcasts browses
            @TestCaseId("730036"), //Tile View of badges
            @TestCaseId("730242") //For you browses
    })
    @Test(description = "Verify that badges appear in tile view correctly", groups = {HOMEPAGE_TEST})
    public void testBadgesAppearInTileViewForPodcasts() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        List<CategoryType> list = Arrays.asList(CATEGORY_TYPE_TOP_PODCASTS_IN_YOUR_COUNTRY, CATEGORY_TYPE_TOP_PODCASTS_GLOBALLY, CATEGORY_TYPE_TOP_10_PODCASTS);
        for (CategoryType categoryType:list) {
            navigationAction.navigateTo(HOME);
            navigationAction.navigateToBrowsies(PODCASTS);
            int index = homePage.getIndexOfFirstContentGreenBadgeStatusInCategory(categoryType, true, true, true);
            if (index >= 0) {
                takeScreenshot();
                homePage.openContentUnderCategoryWithHeader(categoryType, TILE, SHORT, index + 1, false);
                break;
            }
        }
        contentProfilePage
                .validateVisibilityOfGreenCircleForNewEpisode(true)
                .validateEpisodePublishedWithInPastFiveDays();
    }

    @Override
    public void testPlayStreamsFromRecents() {
        signUpPage.signUpFlowForUser(generateRandomUser());
        navigationAction.navigateTo(HOME);
        int numberOfStreamsToOpen = 3;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        for (int i = 1; i <= numberOfStreamsToOpen; i++) {
            homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, i, false);
            if (contentProfilePage.isOnContentProfilePage()) {
                contentProfilePage.tapProfilePlayButton();
            }
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .minimizeNowPlayingScreen();
            if (contentProfilePage.isOnContentProfilePage()) {
                deviceNativeActions.clickBackButton();
            }
        }
    }

    @Override
    public void testHomeWhatDoYouWantToListenTo() {
        HomePage.BrowsiesBarTabsLabels[] tabs = {MUSIC, NEWS_AND_TALK, SPORTS, PODCASTS};
        for (HomePage.BrowsiesBarTabsLabels tab : tabs) {
            navigationAction.navigateTo(HOME);
            homePage.openContentWithLabelUnderCategoryWithHeader(WHAT_DO_YOU_WANT_TO_LISTEN_TO, TILE, SHORT, tab.getbrowsiesBarTabName(), 40);
            switch (tab) {
                case MUSIC -> musicPage.validateMusicPageIsOpen();
                case NEWS_AND_TALK -> newsAndTalkPage.validateNewsAndTalkPageIsOpen();
                case SPORTS -> sportsPage.validateSportsPageIsOpen();
                case PODCASTS -> podcastsPage.validatePodcastsPageIsOpen();
                default -> throw new Error("Invalid platform type " + tab);
            }
        }
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseIds({@TestCaseId("729921"), @TestCaseId("729926")})
    @Test(description = "check browsies in landscape mode", groups = {BROWSIES_TEST, HOMEPAGE_TEST})
    public void testCheckBrowsiesInLandscapeMode() {
        navigationAction.navigateTo(HOME);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        homePage.validateThatAllBrowsiesBarTabsCanBeSelected();
    }
    
    @TestCaseId("729981")
    @Test(description = "test offline mode on home page", groups = {BROWSIES_TEST, HOMEPAGE_TEST})
    public void testOfflineModeOnHomepage() {
        navigationAction.navigateTo(HOME);
        deviceNativeActions.disableWiFi();
        offlinePage
                .waitUntilPageReady()
                .validateOfflineErrorMessageIsDisplayed();
    }

    @TestCaseId("729927")
    @Test(description = "Home Browsies test '>' caret button", groups = {BROWSIES_TEST, HOMEPAGE_TEST})
    public void testBrowsiesCaretButton() {
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_MUSIC_CATEGORY_60S);
        contentsListPage.validateCaretButtonDisplayed();
    }

    @Override
    public void testPickYourFavoriteTeam() {
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_PREMIER_LEAGUE_CATEGORY);
        contentsListPage.tapOnPickYourTeams();
        teamsPage
                .selectNumberOfTeams(2)
                .validateFollowTeamsButtonIsEnabled()
                .tapOnCancelButton();
        contentsListPage
                .waitUntilPageReady()
                .tapOnPickYourTeams();
        teamsPage
                .selectNumberOfTeams(2)
                .tapOnFollowTeamsButton();
        upsellPage
                .validateIsOnUpsellPage(true)
                .closeUpsell();
        teamsPage.tapOnCancelButton();
        contentsListPage
                .waitUntilPageReady()
                .tapOnPickYourTeams();
        teamsPage
                .unselectItemsFromTeams(2)
                .validateUnfollowTeamsButtonIsDisplayed();
    }

    @Override
    public void testFromNewToNoBadge() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        List<CategoryType> list = Arrays.asList(CATEGORY_TYPE_TOP_PODCASTS, CATEGORY_TYPE_POPULAR_PODCASTS_IN_YOUR_AREA);
        for (CategoryType categoryType : list) {
            navigationAction.navigateTo(HOME);
            int index = homePage.getIndexOfFirstContentGreenBadgeStatusInCategory(categoryType, true, false, true);
            if (index >= 0) {
                takeScreenshot();
                homePage.openContentUnderCategoryWithHeader(categoryType, TILE, MEDIUM, index + 1, false);
                contentProfilePage.validateVisibilityOfGreenCircleForNewEpisode(true);
                contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
                nowPlayingPage.minimizeNowPlayingScreen();
                navigationAction.navigateTo(HOME);
                miniPlayerPage.tapStopButton();
                GestureActionUtil.scrollToRefresh();
                homePage.validateGreenIconStateForEpisodeCell(categoryType, false, index + 1);
                break;
            }
        }
    }

    @TestCaseId("749135")
    @Test(description = "Remove Recents By Long Press", groups = {HOMEPAGE_TEST, ACCEPTANCE_TEST})
    public void testRemoveRecentsByLongPress() {
        int numberOfStreamsToOpen = 4;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .removeRecentsContentWithIndexUnderCategory(RECENTS, 1);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 3);
    }
}
