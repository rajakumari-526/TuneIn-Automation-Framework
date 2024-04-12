package com.tunein.mobile.tests.common.browsies;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.TestGroupName.HOMEPAGE_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;

import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;

public abstract class BrowsiesTest extends BaseTest {

    @TestCaseIds(
            {@TestCaseId("729869"), @TestCaseId("729904"),
            @TestCaseId("729925"), @TestCaseId("576445"),
            @TestCaseId("33739"), @TestCaseId("749133")
            })
    @Test(
            description = "Validate that all browsies tabs can be selected",
            groups = {SMOKE_TEST, BROWSIES_TEST, HOMEPAGE_TEST, PLATFORM_TEST, ACCEPTANCE_TEST}
    )
    public void testBrowsiesCanBeSelected() {
        navigationAction.navigateTo(HOME);
        homePage.validateThatAllBrowsiesBarTabsCanBeSelected();
    }

    @TestCaseId("730037")
    @Test(description = "Check that badge is absent for podcast", groups = {BROWSIES_TEST})
    public abstract void testGreenIconBadgeIsAbsentForPodcastOnBrowsies();

    @TestCaseIds({@TestCaseId("729913"), @TestCaseId("22835")})
    @Test(description = "Check Music Browsies", groups = {BROWSIES_TEST})
    public void testMusicBrowsies() {
        navigationAction.navigateToBrowsies(MUSIC);
        contentProfilePage.openContentWithLabelUnderCategoryWithHeader(CATEGORY_TYPE_TOP_MUSIC_GENRES, TILE, SHORT, "Rock");
        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("729919"), @TestCaseId("729995"), @TestCaseId("22843")})
    @Test(description = "test pick your favorite team", groups = {BROWSIES_TEST})
    public void testCheckPodcastsBrowsies() {
        navigationAction.navigateToBrowsies(PODCASTS);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_PODCASTS, 3);
        String expectedTitle = contentProfilePage.getContentProfileTitleText();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateNowPlayingTitleIsEqualTo(expectedTitle);
    }

    @TestCaseIds({@TestCaseId("729918"), @TestCaseId("22836")})
    @Test(description = "Validate the By Language browsies tab", groups = {BROWSIES_TEST})
    public void testByLanguageBrowsiesTab() {
        navigationAction.navigateToBrowsies(BY_LANGUAGE);
        contentsListPage.openContentWithLabelUnderCategoryWithHeader(CATEGORY_LANGUAGES, LIST, SHORT, "Arabic");
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_CATEGORIES, LIST, SHORT, 1, false);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_EXPLORE, LIST, SHORT, 1, false, true);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateStreamStartPlaying(LIVE_STATION);
    }

    @TestCaseId("729997")
    @Test(description = "Open genres from Music browsies", groups = {BROWSIES_TEST})
    public void testGenresFromMusicBrowsies() {
        navigationAction.navigateToBrowsies(MUSIC);
        musicPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TOP_MUSIC_GENRES, TILE, SHORT, 1, false);
        contentsListPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 1, false);
        nowPlayingPage.validateStreamStartPlaying(LIVE_STATION);
    }

    @TestCaseId("729869")
    @Test(description = "Navigation from News and Talk to Music tab", groups = {BROWSIES_TEST})
    public void testNavigationFromNewsAndTalkToMusic() {
        navigationAction.navigateTo(HOME);
        homePage.openContentWithLabelUnderCategoryWithHeader(WHAT_DO_YOU_WANT_TO_LISTEN_TO, TILE, LONG, NEWS_AND_TALK.getbrowsiesBarTabName());
        navigationAction.navigateTo(HOME);
        homePage.openContentWithLabelUnderCategoryWithHeader(WHAT_DO_YOU_WANT_TO_LISTEN_TO, TILE, LONG, MUSIC.getbrowsiesBarTabName());
    }

    @TestCaseIds({@TestCaseId("729915"), @TestCaseId("729916"), @TestCaseId("22838")})
    @Test(description = "Check News and Talk browsies", groups = {BROWSIES_TEST})
    public void testCheckNewsAndTalkBrowsies() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(NEWS_AND_TALK);
        newsAndTalkPage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_LOCAL_NEWS_RADIO, TILE, SHORT, 2, false, false);
        nowPlayingPage.validateThatNowPlayingIsOpened();
    }

    @TestCaseId("749134")
    @Test(description = "Check that tapping back (' < ') button redirects to the previous screen.", groups = {BROWSIES_TEST, ACCEPTANCE_TEST})
    public abstract void testDeepBrowse();

    @TestCaseIds({@TestCaseId("576437"), @TestCaseId("576454")})
    @Test(description = "Verify More buttons under Browsie", groups = {BROWSIES_TEST, PLATFORM_TEST})
    public void testMoreButtonUnderBrowsie() {
        navigationAction.navigateToBrowsies(MUSIC);
        musicPage.tapOnCategoryHeader(CATEGORY_TYPE_LISTEN_COMMERCIAL_FREE, DOWN);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
        navigationAction.tapBackButtonIfDisplayed();
        navigationAction.navigateToBrowsies(PODCASTS);
        podcastsPage.tapOnCategoryHeader(TRUECRIME_PODCASTS, DOWN);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
        navigationAction.tapBackButtonIfDisplayed();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        contentProfilePage.tapOnCategoryHeader(CATEGORY_TYPE_SHOWS, DOWN);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
    }

}
