package com.tunein.mobile.tests.common.homepage;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.GestureActionUtil;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.PODCAST_TYPE_PREMIUM;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.*;
import static com.tunein.mobile.utils.DeepLinksUtil.GenresAndCategoriesList.DEEPLINK_PREMIER_LEAGUE_CATEGORY;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.*;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;

public abstract class HomePageTest extends BaseTest {

    @Issue("PLATFORM-17133")
    @TestCaseIds({@TestCaseId("730237"), @TestCaseId("730025")})
    @Test(description = "Press on carrot button in carousel with several brick cells", groups = {HOMEPAGE_TEST})
    public void testCarrotButtonInCarouselForPodcast() {
        navigationAction.navigateToBrowsies(PODCASTS);
        homePage.tapOnCategoryHeader(SPORTS_TALK_PODCASTS, DOWN);
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateContentListIsNotEmpty();
    }

    @TestCaseId("730261")
    @Test(description = "Check recent streams on Homepage", groups = {HOMEPAGE_TEST})
    public void testRecentStreamsOnHomePage() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        Users randomUser = generateRandomUser();
        navigationAction.navigateTo(SIGNUP_FORM);
        signUpPage.signUpUserByEmail(randomUser);
        userProfilePage.closeProfilePage();
        navigationAction.navigateTo(HOME);
        int numberOfStreamsToOpen = 3;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
    }

    @TestCaseIds({@TestCaseId("730057")})
    @Test(description = "Check Your Games category is absent if user doesn't have favorite teams", groups = {HOMEPAGE_TEST})
    public void testValidateThatGamesCategoryIsAbsent() {
        signInPage.signInFlowForUser(USER_WITHOUT_FAVORITES);
        navigationAction.navigateTo(HOME);
        homePage.validateCategoryIsAbsent(YOUR_GAMES);
    }

    @TestCaseIds({
            @TestCaseId("729911"), @TestCaseId("730269"),
            @TestCaseId("730256"), @TestCaseId("730257"),
            @TestCaseId("730260"), @TestCaseId("749135")
    })
    @Test(description = "Play streams from recents", groups = {HOMEPAGE_TEST, ACCEPTANCE_TEST})
    public abstract void testPlayStreamsFromRecents();

    @TestCaseId("730078")
    @Test(description = "Validate that all browsies tabs can be selected", groups = {BROWSIES_TEST, HOMEPAGE_TEST})
    public abstract void testHomeWhatDoYouWantToListenTo();

    @TestCaseIds({
            @TestCaseId("729914"), @TestCaseId("729996"), // Android
            @TestCaseId("22837") // iOS
    })
    @Test(description = "Home Browsies test Sports page", groups = {BROWSIES_TEST, HOMEPAGE_TEST})
    public void testHomeSportsPage() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
        sportsPage.openContentWithLabelUnderCategoryWithHeader(CATEGORY_TYPE_EXPLORE_BY_SPORT, TILE, SHORT, "Soccer");
        contentsListPage
                .validateContentsListPageIsDisplayed()
                .validateThatCategoryIsDisplayed(CATEGORY_TYPE_UPCOMING_EVENTS)
                .openContentUnderCategoryWithHeader(CATEGORY_TYPE_STATIONS, LIST, SHORT, 5, false);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseId("730058")
    @Test(description = "Test Your Games from Home to check has several items", groups = {HOMEPAGE_TEST})
    public void testYourGamesPageHasSeveralItems() {
        signInPage.signInFlowForUser(USER_WITH_TEAMS_FAVORITED);
        navigationAction.navigateTo(HOME);
        homePage.tapOnCategoryHeader(YOUR_GAMES, DOWN);
        contentsListPage.validateContentListSizeIsGreaterThanOrEqualTo(3);
    }

    @TestCaseId("739081")
    @Test(description = "Test pick your favorite team", groups = {HOMEPAGE_TEST})
    public abstract void testPickYourFavoriteTeam();

    @TestCaseId("729986")
    @Test(description = "Go Premium prompt is absent for premium user", groups = {HOMEPAGE_TEST})
    public void testGoPremiumPromptIsAbsentForPremiumUser() {
        navigationAction.navigateTo(HOME);
        homePage.scrollToAndTapFreeTrialButton(14);
        upsellPage
                .validateIsOnUpsellPage(true)
                .closeUpsell();
        navigationAction.navigateTo(LIBRARY);
        signInPage.signInFlowForUser(USER_PREMIUM);
        navigationAction.navigateTo(HOME);
        homePage.validateGoPremiumPromptState(false, LONG);
    }

    @TestCaseId("730264")
    @Test(description = "Recents after sign out", groups = {HOMEPAGE_TEST})
    public void testRecentsAfterSignOut() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        signUpPage.signUpFlowForUser(generateRandomUser());
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnSignOutButton()
                .closeProfilePage();
        navigationAction.navigateTo(HOME);
        homePage.tapOnRequiredBrowsiesBarTab(FOR_YOU);
        homePage.validateCategoryIsAbsent(RECENTS);
        int numberOfStreamsToOpen = 3;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
    }

    @TestCaseId("730265")
    @Test(description = "Recents are merged after sign up", groups = {HOMEPAGE_TEST})
    public void testRecentsAreMergedAfterSignUp() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        navigationAction.navigateTo(HOME);
        int numberOfStreamsToOpen = 3;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        //TODO to remove after https://tunein.atlassian.net/browse/IOS-13559 is fixed
        GestureActionUtil.scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        int moreStreamsToOpen = 1;
        nowPlayingPage.generateRecentsForNowPlayingPage(moreStreamsToOpen, GENERATE_RECENT_LIST, 3);
        switchToolTipDialog.clickOnNotNowButtonIfDisplayed();
        nowPlayingPage
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        //TODO to remove after https://tunein.atlassian.net/browse/IOS-13559 is fixed
        GestureActionUtil.scrollToRefresh();
        homePage
                .waitUntilPageReady()
                .validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen + moreStreamsToOpen);
    }

    @Issue("IOS-13559")
    @TestCaseIds({@TestCaseId("730258"), @TestCaseId("730259")})
    @Test(
            description = "Check commercial-free station and premium podcast playback from recents",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamPremiumDataProviders",
            groups = {HOMEPAGE_TEST}
    )
    public void testPlayCommercialFreeStationAndPremiumPodcastFromRecents(Contents content) {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh();
        //TODO - Recents Roll-up is not updated if user plays an item from it bug id - https://tunein.atlassian.net/browse/IOS-13559
        homePage.openContentUnderCategoryWithHeader(RECENTS, TILE, SHORT, 1, false);
        if (content.getContentProfileType().equals(PODCAST_TYPE_PREMIUM.getContentProfileType())) {
            contentProfilePage.validateContentProfilePageIsOpened();
        } else {
            nowPlayingPage.validateStreamStartPlaying(LIVE_STATION);
        }
    }

    @TestCaseId("736181")
    @Test(description = "Pick your Team UI", groups = {HOMEPAGE_TEST})
    public void testPickYourTeamUI() {
        deepLinksUtil.openContentsListThroughDeeplink(DEEPLINK_PREMIER_LEAGUE_CATEGORY);
        sportsPage.validatePickYourTeamPromptElementsArePresent();
        sportsPage.tapOnPickYourTeams();
        teamsPage.validateUIElements(teamsPage.teamsPageElements());
    }

    @TestCaseIds({@TestCaseId("730290"), @TestCaseId("729894")})
    @Test(description = "Compare resents in Home and Library pages", groups = {HOMEPAGE_TEST})
    public void testRecentsFromHomeAndLibrary() {
        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(HOME);
        int expected = homePage.getItemsNumberUnderCategory(RECENTS);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateNumberOfRecents(expected);
    }

    @Issue("IOS-13559")
    @TestCaseIds({@TestCaseId("730266"), @TestCaseId("717981"), @TestCaseId("717978")})
    @Test(description = "Check recents are merged after sign in", groups = {BROWSIES_TEST, PLATFORM_TEST})
    public void testRecentsAreMergedAfterSignIn() {
        int numberOfStreamsToOpen = 1;
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_AAC_FORMAT);
        nowPlayingPage
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        //TODO to remove after https://tunein.atlassian.net/browse/IOS-13559 is fixed
        GestureActionUtil.scrollToRefresh();
        int noOfRecentsFromSignedInUser = homePage.getHomePageRecentsNumber();
        userProfilePage.signOutUserFlow();
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        GestureActionUtil.scrollToRefresh();
        homePage.waitUntilPageReady();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        signInPage.signInFlowForUser(randomUser);
        navigationAction.navigateTo(HOME);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen + noOfRecentsFromSignedInUser);
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST, 1)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        GestureActionUtil.scrollToRefresh();
        homePage.waitUntilPageReady();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen + noOfRecentsFromSignedInUser + 1);
    }

    @TestCaseId("730276")
    @Test(description = "Quantity of items range from 0 to more than that fits the screen", groups = {HOMEPAGE_TEST})
    public void testRecentsRangeFromZeroToMoreThanThatFitsTheScreen() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        signInPage.signInFlowForUser(USER_WITHOUT_RECENTS);
        homePage.validateThatNoRecentsAvailable();
        userProfilePage.signOutUserFlow();
        signInPage.signInFlowForUser(USER_FOR_RECENTS_TEST);
        int noOfRecentsFromSignedInUser = USER_FOR_RECENTS_TEST.getNumberOfRecents();
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, noOfRecentsFromSignedInUser);
        userProfilePage.signOutUserFlow();
        signInPage.signInFlowForUser(USER_WITHOUT_FAVORITES);
        homePage.swipeRequiredCategory(RECENTS, DOWN, 10);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, 5);
    }

    @TestCaseIds({@TestCaseId("22845"), @TestCaseId("730331"), @TestCaseId("730147")})
    @Test(description = "Verify navigation bar hidden when scrolled down & displayed when scrolled up", groups = {HOMEPAGE_TEST})
    public void testHidingNavigationBar() {
        navigationAction.navigateTo(HOME);
        navigationAction
                .validateNavigationBarAfterScrolling(DOWN)
                .validateNavigationBarAfterScrolling(UP)
                .navigateToBrowsies(SPORTS);
        navigationAction.validateNavigationBarAfterScrolling(DOWN);
        scroll(DOWN, 5, LONG);
        navigationAction.validateNavigationBarAfterScrolling(DOWN);
    }

    @TestCaseIds({@TestCaseId("730042"), @TestCaseId("730246")})
    @Test(description = "Test from new to no badge", groups = {HOMEPAGE_TEST})
    public abstract void testFromNewToNoBadge();

    @Issue("IOS-17857")
    @TestCaseIds({@TestCaseId("738554"), @TestCaseId("738553")})
    @Test(description = "Settings UI", groups = {PLATFORM_TEST, PLATFORM_TEST})
    public void testValidateSettingsUIElements() {
        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(SETTINGS);
        settingsPage.validateUIElements(settingsPage.settingsPageElements(USER_GENERAL));
    }

    @TestCaseIds({@TestCaseId("717980"), @TestCaseId("717981")})
    @Test(description = "Check merge of recents content for Random user", groups = {HOMEPAGE_TEST, PLATFORM_TEST})
    public void testMergeOfRecentsContentForRandomUser() {
        int numberOfStreamsToOpen = 2;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(HOME);
        GestureActionUtil.scrollToRefresh(MEDIUM);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen);
        signUpPage.signUpFlowForUser(generateRandomUser());
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.playStreamMoreThanRequiredTime(30);
        nowPlayingPage
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        GestureActionUtil.scrollToRefresh(MEDIUM);
        homePage.validateNumberOfElementsInHomePageCategory(RECENTS, UP, numberOfStreamsToOpen + 1);
    }

    @TestCaseIds({@TestCaseId("717980"), @TestCaseId("717981")})
    @Test(description = "Check merge of recents content for Premium user", groups = {HOMEPAGE_TEST, PLATFORM_TEST})
    public void testMergeRecentsForPremiumUser() {
        signInPage.signInFlowForUser(USER_WITH_PREMIUM_RECENTS);
        int numberOfStreamsToOpen = 2;
        nowPlayingPage
                .generateRecentsForNowPlayingPage(numberOfStreamsToOpen, GENERATE_RECENT_LIST)
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnCategoryHeader(RECENTS, UP);
        homePage
                .validateThatContentAppearedInRecents(STREAM_STATION_UNICC.getStreamName())
                .validateThatContentAppearedInRecents(STREAM_STATION_WITHOUT_ADS.getStreamName());
    }

}
