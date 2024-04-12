package com.tunein.mobile.tests.common.search;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.pages.BasePage.CategoryType;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.getContentProfileType;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SEARCH;
import static com.tunein.mobile.pages.common.search.SearchPage.*;
import static com.tunein.mobile.pages.common.search.SearchPage.SearchPageState.*;
import static com.tunein.mobile.pages.common.search.SearchPage.WhatToListenCategory.PODCASTS;
import static com.tunein.mobile.pages.common.search.SearchPage.WhatToListenCategory.*;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class SearchTest extends BaseTest {

    @TestCaseIds({@TestCaseId("710136"), @TestCaseId("710137"), @TestCaseId("749240")})
    @Test(description = "Verify search page ui elements in active state", groups = {SMOKE_TEST, SEARCH_TEST, ACCEPTANCE_TEST})
    public void testActiveSearchFieldUI() {
        navigationAction.navigateTo(SEARCH);
        searchPage
                .tapOnSearchField()
                .validateUIElements(searchPage.searchPageElements(BEFORE_SEARCH_ACTIVE_FIELD_NO_RECENTS));
    }

    @TestCaseIds({@TestCaseId("710136"), @TestCaseId("710137"), @TestCaseId("749240")})
    @Test(description = "Verify search page ui elements for inactive state", groups = {SMOKE_TEST, SEARCH_TEST})
    public void testInactiveSearchFieldUI() {
        navigationAction.navigateTo(SEARCH);
        searchPage.validateUIElements(searchPage.searchPageElements(BEFORE_SEARCH_INACTIVE_FIELD));
    }

    @TestCaseIds({@TestCaseId("710136"), @TestCaseId("710137")})
    @Test(description = "Verify search page ui elements when results not found", groups = {SMOKE_TEST, SEARCH_TEST})
    public void testSearchResultsNotFoundUI() {
        String searchBadSearchResultsText = "!@#$";
        navigationAction.navigateTo(SEARCH);
        searchPage
                .waitUntilPageReady()
                .typeSearchTextWithNoResults(searchBadSearchResultsText)
                .validateSearchFieldHasValue(searchBadSearchResultsText)
                .validateNoResultsText(searchBadSearchResultsText)
                .validateUIElements(searchPage.searchPageElements(SEARCH_NO_RESULTS));
    }

    @TestCaseIds({@TestCaseId("729485"), @TestCaseId("729404")})
    @Test(description = "Verify recent search page ui elements", groups = {SEARCH_TEST})
    public void testSearchRecentsUI() {
        int numberOfRecents = 2;
        navigationAction.navigateTo(SEARCH);
        searchPage
                .generateRecentsItems(numberOfRecents)
                .validateNumberOfRecentsIsCorrect(numberOfRecents)
                .validateUIElements(searchPage.searchPageElements(BEFORE_SEARCH_ACTIVE_FIELD_RECENTS_PRESENT));
    }

    @TestCaseIds({@TestCaseId("23799"), @TestCaseId("30552")})
    @Test(description = "[Item for searching] - Show", groups = {SEARCH_TEST})
    public void testSearchAndPlayShow() {
        navigationAction.navigateTo(SEARCH);
        searchPage.searchProfileByNameAndOpenFirstInCategory("Show", CATEGORY_TYPE_SHOWS, MEDIUM);
        contentProfilePage.tapProfilePlayButton();
        nowPlayingPage.validateStreamStartPlaying();
    }

    @TestCaseIds({@TestCaseId("23798"), @TestCaseId("30549"), @TestCaseId("749240")})
    @Test(description = "[Item for searching] - Station", groups = {SEARCH_TEST, ACCEPTANCE_TEST})
    public void testSearchAndPlayStation() {
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamByNameAndPlay(STREAM_STATION_WITHOUT_ADS, CATEGORY_TYPE_STATIONS);
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @TestCaseIds({
            @TestCaseId("221791"), @TestCaseId("582335"), // Artist profile
            @TestCaseId("576304"), @TestCaseId("582324"), @TestCaseId("221689"), // Show profile
            @TestCaseId("221686"), @TestCaseId("712123") // Podcast profile
    })
    @Test(description = "Open Station page using Search",
            dataProviderClass = ContentProvider.class,
            dataProvider = "nextContentProfiles",
            groups = {CONTENT_PROFILE_TEST, SEARCH_TEST})
    public void testOpenContentProfileFromSearch(Contents content) {
        ContentProfilePage.ContentProfileType contentProfileType = getContentProfileType(content.getContentProfileType());
        navigationAction.navigateTo(SEARCH);
        searchPage.searchProfileByNameAndOpenFirstInCategory(content.getSearchQuery(), RESULTS_CATEGORY);
        contentProfilePage.validateUIElements(contentProfilePage.contentProfileElements(contentProfileType));
    }

    @TestCaseIds({@TestCaseId("576429"), @TestCaseId("576452")})
    @Test(description = "More (caret) button in search results", groups = {SEARCH_TEST, PLATFORM_TEST})
    public void testCaretButtonInSearchResults() {
        HashMap<String, CategoryType> categoryTypeMap = new HashMap<>();
        categoryTypeMap.put("rock", CATEGORY_TYPE_STATIONS);
        categoryTypeMap.put("marketplace", CATEGORY_TYPE_PODCASTS);
        categoryTypeMap.put("MLB", CATEGORY_TYPE_EVENTS);
        navigationAction.navigateTo(SEARCH);
        for (Map.Entry<String, CategoryType> searchValue : categoryTypeMap.entrySet()) {
            searchPage
                    .typeSearchText(searchValue.getKey())
                    .tapOnCategoryHeader(searchValue.getValue(), DOWN);
            contentsListPage
                    .validateContentsListPageIsDisplayed()
                    .validateContentListIsNotEmpty();
            navigationAction.tapBackButtonIfDisplayed();
        }
    }

    @TestCaseIds({@TestCaseId("711797"), @TestCaseId("711798")})
    @Test(description = "Search same item multiple times", groups = {SEARCH_TEST, PLATFORM_TEST})
    public void testSearchSameItemMultipleTimes() {
        navigationAction.navigateTo(SEARCH);
        searchPage
                .typeSearchText("Classic Music")
                .validateUIElements(searchPage.searchPageElements(SEARCH_WITH_RESULTS));
        String nameOfFirstSearchElement = searchPage.getNameOfFirstSearchResultInCategory(STATIONS);
        for (int i = 0; i <= 6; i++) {
            searchPage
                    .typeSearchText("Classic Music")
                    .validateUIElements(searchPage.searchPageElements(SEARCH_WITH_RESULTS));
            searchPage.validateFirstSearchResultNameEqualsTo(STATIONS, nameOfFirstSearchElement);
        }
    }

    @TestCaseIds({@TestCaseId("33735"), @TestCaseId("749240"), @TestCaseId("576443")})
    @Test(description = "Open Station, Show, MarketPlace, AL jazeera, Baltimore contents using Search",
            groups = {SEARCH_TEST, PLATFORM_TEST, ACCEPTANCE_TEST})
    public void testCategoryContentFromSearch() {
        navigationAction.navigateTo(SEARCH);
        List<String> searchList = new ArrayList<>();
        for (Object[] object : searchContentProfiles()) {
            Contents content = (Contents) object[0];
            String streamName = content.getStreamName();
            searchList.add(streamName);
            searchPage
                    .typeSearchText(streamName)
                    .validateThatSearchItemUnderCategory(favoritesPage.getCategoryTypeFromContents(content), content.getStreamName())
                    .openContentWithLabelUnderCategoryWithHeader(favoritesPage.getCategoryTypeFromContents(content), LIST, SHORT, streamName);
            if (nowPlayingPage.isOnNowPlayingPage(Duration.ofSeconds(5))) {
                nowPlayingPage.minimizeIfNowPlayingDisplayed();
            }
            if (contentProfilePage.isOnContentProfilePage(Duration.ofSeconds(5))) {
                navigationAction.tapBackButtonIfDisplayed();
            }
        }
        Collections.reverse(searchList);
        searchPage
                .clearSearchResults()
                .validateThatRecentListSearchesHasItems(searchList);
    }

    @TestCaseId("33735")
    @Test(description = "Verify 'What Do You want to listen to?' categories on the search screen",
            groups = {SEARCH_TEST, PLATFORM_TEST})
    public void testWhatDoYouWantToListenFromSearch() {
        WhatToListenCategory[] tabs = {MUSIC, NEWS_AND_TALK, SPORTS, PODCASTS, LOCAL, INTERNATIONAL, AUDIOBOOKS};
        for (WhatToListenCategory tab : tabs) {
            navigationAction.navigateTo(SEARCH);
            searchPage
                    .validateWhatDoYouWantToListenToIsDisplayed()
                    .openCardInWhatDoYouWantToListen(tab);
            searchPage.validateThatRequiredSearchCategoryIsOpened(tab);
            navigationAction.tapBackButtonIfDisplayed();
        }
    }

    @TestCaseIds({@TestCaseId("33735"), @TestCaseId("749240")})
    @Test(description = "Verify 'Trending searches' categories on the search screen",
            groups = {SEARCH_TEST, PLATFORM_TEST, ACCEPTANCE_TEST})
    public void testTrendingSearches() {
        navigationAction.navigateTo(SEARCH);
        searchPage
                .validateTrendingSearchesIsDisplayed()
                .tapOnSearchTagByIndex(2);
        contentsListPage.validateContentsListPageIsDisplayed();
    }

}
