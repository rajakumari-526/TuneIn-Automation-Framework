package com.tunein.mobile.tests.ios.homepage;

import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.tests.common.homepage.HomePageTest;
import com.tunein.mobile.utils.GestureActionUtil;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.GENERATE_RECENT_LIST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_PODCAST_MARKETPLACE;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_PREMIER_LEAGUE_CATEGORY;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class IosHomePageTest extends HomePageTest {

    @Override
    public void testPlayStreamsFromRecents() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        signUpPage.signUpFlowForUser(generateRandomUser());
        navigationAction.navigateTo(HOME);
        int numberOfStreamsToOpen = 4;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        GestureActionUtil.scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        for (int i = 1; i <= numberOfStreamsToOpen; i++) {
            navigationAction.navigateTo(HOME);
            homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, i, false);
            if (contentProfilePage.isOnContentProfilePage()) {
                contentProfilePage.tapProfilePlayButton();
            }
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .minimizeNowPlayingScreen();
        }
    }

    @Override
    public void testPickYourFavoriteTeam() {
        navigationAction.navigateTo(HOME);
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
        contentsListPage
                .waitUntilPageReady()
                .tapOnPickYourTeams();
        teamsPage
                .selectNumberOfTeams(2)
                .validateUnfollowTeamsButtonIsDisplayed();
    }

    @Override
    public void testFromNewToNoBadge() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.validateVisibilityOfGreenCircleForNewEpisode(true);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        miniPlayerPage.tapStopButton();
        homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 1, false);
        contentProfilePage.validateVisibilityOfGreenCircleForNewEpisode(false);
    }

    @Override
    public void testHomeWhatDoYouWantToListenTo() {
        HomePage.BrowsiesBarTabsLabels[] tabs = {MUSIC, NEWS_AND_TALK, SPORTS, PODCASTS};
        for (HomePage.BrowsiesBarTabsLabels tab : tabs) {
            navigationAction.navigateTo(HOME);
            homePage.openContentWithLabelUnderCategoryWithHeader(WHAT_DO_YOU_WANT_TO_LISTEN_TO, TILE, MEDIUM, tab.getbrowsiesBarTabName(), 16);
            switch (tab) {
                case MUSIC -> musicPage.validateThatCategoryHeaderIsDisplayed(CATEGORY_TYPE_TOP_MUSIC_GENRES, DOWN);
                case NEWS_AND_TALK -> newsAndTalkPage.validateThatCategoryHeaderIsDisplayed(CATEGORY_TYPE_NEWS_PODCASTS, DOWN);
                case SPORTS -> sportsPage.validateThatCategoryHeaderIsDisplayed(CATEGORY_TYPE_EXPLORE_BY_SPORT, DOWN);
                case PODCASTS -> podcastsPage.validateThatCategoryHeaderIsDisplayed(CATEGORY_TYPE_TOP_10_PODCASTS, DOWN);
                default -> throw new Error("Invalid platform type " + tab);
            }
        }
    }

}
