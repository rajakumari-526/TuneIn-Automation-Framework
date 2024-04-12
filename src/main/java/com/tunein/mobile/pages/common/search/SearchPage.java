package com.tunein.mobile.pages.common.search;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.GestureActionUtil.ScrollDistance;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.search.SearchPage.SearchPageState.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.USER_TRACKING;
import static com.tunein.mobile.utils.ApplicationUtil.closePermissionPopupFor;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SearchPage extends BasePage {

    public static final String PODCASTS = "Podcasts";

    public static final String STATIONS = "Stations";

    public static final String EPISODES = "Episodes";

    public static final String SHOWS = "Shows";

    public static final String RESULTS = "Results";

    public static final String SEARCH_PLACEHOLDER = "Search";

    public static final String NO_RESULTS = "No Results found for \"%s\"";

    public static final String EXPECTED_SEARCH_SUGGESTION = isAndroid() ? "Slipknot" : "Slipknot.";

    protected SelenideElement searchInputTextField = $(android(id("search_src_text"))
            .ios(id("searchTextFieldId"))).as("Search input text field");

    protected SelenideElement searchClearTextButton = $(android(id("search_close_btn"))
            .ios(iOSNsPredicateString("name == 'Clear text'"))).as("Clear text button");

    /* --- Trending --- */

    protected SelenideElement searchTrendingSearchesLabel = $(android(androidUIAutomator("text(\"Trending Searches\")"))
            .ios(iOSNsPredicateString("label == 'Trending Searches' AND visible == true"))).as("Trending searches label");

    protected SelenideElement searchTrendingSearchesContainer = $(android(id("tag_group"))
            .ios(xpath("//*[contains(@name, 'Trending')]/following-sibling::XCUIElementTypeCell[1]"))).as("Trending searches container");

    public ElementsCollection searchTrendingSearchesTags = $$(android(xpath("//*[contains(@resource-id, 'tag_group')]/android.widget.Button"))
            .ios(xpath("//*[contains(@name, 'Trending')]/following-sibling::XCUIElementTypeCell[1]//XCUIElementTypeCell"))).as("Trending searches tags");

    /* --- What Do You Want To Listen To --- */

    protected SelenideElement searchWhatDoYouWantToListenContainer = $(android(id("gallery_recycler_view"))
            .ios(xpath("//*[contains(@name, 'What do')]/following-sibling::XCUIElementTypeCell[1]"))).as("'What Do You Want to listen?' container");

    protected SelenideElement searchWhatDoYouWantToListenLabel = $(android(androidUIAutomator("text(\"What do you want to listen to?\")"))
            .ios(iOSNsPredicateString("label == 'What do you want to listen to?' AND visible == true"))).as("'What do you want to listen?' label");

    protected ElementsCollection searchWhatDoYouWantToListenList = $$(android(xpath("//*[contains(@resource-id, 'gallery_recycler_view')]/android.view.ViewGroup"))
            .ios(xpath("//*[contains(@name, 'What do')]/following-sibling::XCUIElementTypeCell[1]//XCUIElementTypeCell"))).as("'What do you want to listen?' list");

    protected ElementsCollection whatDoYouWantContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("'What do you want?' content");

    /* --- Recents --- */

    protected SelenideElement searchWhatDoYouWantToHearLabel = $(android(androidUIAutomator("text(\"What do you want to hear?\")"))
            .ios(xpath("//*[@label='What do you want to hear?']"))).as("'What do you want to hear?' label");

    protected SelenideElement searchRecentSearchesLabel = $(android(androidUIAutomator("text(\"Recent Searches\")"))
            .ios(iOSNsPredicateString("label == 'Recent Searches' AND visible == true"))).as("Recent Searches label");

    protected SelenideElement searchClearRecentSearchesButton = $(android(id("recent_search_clear_button"))
            .ios(iOSNsPredicateString("label == 'CLEAR RECENT SEARCHES'"))).as("Clear recent searches button");

    public ElementsCollection searchRecentList = $$(android(androidUIAutomator("resourceIdMatches(\"^.*search_label*$\").className(android.widget.TextView)"))
            .ios(xpath("//XCUIElementTypeOther[(@name='Recent Searches') and (@visible='true')]/following-sibling::XCUIElementTypeCell[.//*[(@name= 'DismissKey') and (@visible='true')]]"))).as("Recents list");

    /* --- Search Results --- */

    protected SelenideElement searchDidYouMeanLabel = $(android(androidUIAutomator("text(\"Did you mean?\")"))
            .ios(iOSNsPredicateString("name == \"Did you mean?\" AND type == \"XCUIElementTypeOther\""))).as("'Did you mean' label");

    protected SelenideElement searchDidYouMeanSuggestion = $(android(id("tunein.player:id/row_view_model_url_cell_text"))
            .ios(xpath("(//XCUIElementTypeOther[@name=\"Did you mean?\"]/following-sibling::*/XCUIElementTypeOther)[1]"))).as("Did you mean suggestion");

    public ElementsCollection searchResultsList = $$(android(xpath("//*[(contains(@resource-id,'row_view_model_url_cell_text')) or (contains(@resource-id,'row_square_cell_container')) or (contains(@resource-id,'gallery')) or (contains(@resource-id,'row_square_cell_image_border'))]"))
            .ios(xpath("//XCUIElementTypeCell[./XCUIElementTypeOther]"))).as("Results list");

    public ElementsCollection searchResultsCategoryList = $$(android(id("view_model_container_title"))
            .ios(xpath("//XCUIElementTypeTable//XCUIElementTypeStaticText[not(@name='')]/.."))).as("Results category list");

    protected SelenideElement searchNoResultsLabel = $(android(id("prompt_text_id"))
            .ios(iOSNsPredicateString("label CONTAINS 'No Results found for'"))).as("No results label");

    protected SelenideElement closebutton = $(android(cssSelector("[data-testid = close]"))
            .ios(iOSNsPredicateString("label == 'Close Button' AND type == 'XCUIElementTypeImage'"))).as("Close button");

    /* --- Loadable Component Methods --- */

    @Step
    @Override
    public SearchPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(searchInputTextField, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(searchWhatDoYouWantToListenList, Duration.ofMillis(config().waitExtraLongTimeoutMilliseconds()));
        return this;
    }

    @Step
    public SearchPage waitUntilSuccessfulResultsIsLoaded() {
        waitTillElementDisappear(searchWhatDoYouWantToHearLabel);
        waitUntilNumberOfElementsMoreThanZero(searchResultsList, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    @Step
    public SearchPage waitUntilNoResultsIsLoaded() {
        waitTillElementDisappear(searchWhatDoYouWantToHearLabel);
        waitTillVisibilityOfElement(searchNoResultsLabel, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract SearchPage tapCancelButton();

    @Step
    public SearchPage tapOnRandomSearchTag() {
        Random random = new Random();
        SelenideElement randomTag = searchTrendingSearchesTags.get(random.nextInt(searchTrendingSearchesTags.size()));
        clickOnElement(randomTag);
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    @Step("Tap on search trending tag with index {index}")
    public SearchPage tapOnSearchTagByIndex(int index) {
        clickOnElement(searchTrendingSearchesTags.get(index - 1));
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    public abstract SearchPage tapOnSearchTagByName(String tagName);

    @Step
    public SearchPage generateRecentsItems(int numberOfRecents) {
            if (numberOfRecents < 1 || numberOfRecents > 5) {
                throw new UnsupportedOperationException("Invalid number value, should be in range 1-5");
            }
            List<Contents> contentsList = new ArrayList<>() {{
                add(STREAM_STATION_UNICC);
                add(STREAM_PODCAST_MARKETPLACE);
                add(STREAM_FREE_SHORT_PODCAST);
                add(STREAM_STATION_WITHOUT_ADS);
                add(STREAM_STATION_WITH_MUSIC_AAC);
            }};
            for (Contents content : contentsList.subList(0, numberOfRecents)) {
                searchStreamAndPlayFirstResult(content);
                nowPlayingPage
                        .tapPauseButtonIfEnabled()
                        .minimizeNowPlayingScreen();
                if (contentProfilePage.isOnContentProfilePage()) {
                    navigationAction.tapBackButtonIfDisplayed();
                }
                clearSearchResults();
            }
            return this;
    }

    @Step("Type search text {searchQuery}")
    public abstract SearchPage typeSearchText(String searchQuery);

    @Step
    public SearchPage typeSearchTextWithNoResults(String searchQuery) {
        clearSearchResults();
        deviceNativeActions.typeText(searchInputTextField, searchQuery);
        return this.waitUntilNoResultsIsLoaded();
    }

    @Step("Clear search results")
    public SearchPage clearSearchResults() {
        if (isSearchFieldPrePopulated()) {
            clickOnElement(searchClearTextButton);
        }
        return this;
    }

    @Step
    public SearchPage tapOnSearchField() {
        clickOnElement(searchInputTextField);
        return this;
    }

    @Step
    public ContentProfilePage tapOnPodcastSearchResultWithName(String podcastName) {
        clickOnElement(getSearchResultWithStreamNameInCategory(PODCASTS, podcastName));
        return contentProfilePage.waitUntilPageReady();
    }

    @Step
    public NowPlayingPage tapOnStationSearchResultWithName(String stationName) {
        clickOnElement(getSearchResultWithStreamNameInCategory(STATIONS, stationName));
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step
    public NowPlayingPage tapOnEpisodeSearchResultWithName(String episodeName) {
        clickOnElement(getSearchResultWithStreamNameInCategory(EPISODES, episodeName));
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Click on search result with index {index}")
    public void tapOnSearchResultWithIndex(int index) {
        clickOnElement(waitVisibilityOfElement(searchResultsList.get(index)));
    }

    @Step("Search a {stream} and play first result")
    public NowPlayingPage searchStreamAndPlayFirstResult(Contents stream, boolean... expectLiveRewindPage) {
        typeSearchText(stream.getSearchQuery());
        clickOnElement(searchResultsList.get(0));
        if (expectLiveRewindPage.length > 0 && expectLiveRewindPage[0]) {
            listenLiveRewindModalDialog.waitUntilPageReady();
            return nowPlayingPage;
        } else {
            return waitForNowplayingPageWithContentType(ContentType.getContentTypeValue(stream.getStreamType()));
        }
    }

    @Step
    public NowPlayingPage searchStreamByNameAndPlay(Contents stream, CategoryType categoryType) {
        typeSearchText(stream.getStreamName());
        searchPage.openContentWithLabelUnderCategoryWithHeader(categoryType, LIST, SHORT, stream.getStreamName());
        return waitForNowplayingPageWithContentType(ContentType.getContentTypeValue(stream.getStreamType()));
    }

    @Step
    public ContentProfilePage searchProfileByNameAndOpenFirstInCategory(String query, CategoryType categoryType, ScrollDistance... scrollDistance) {
        typeSearchText(query);
        ScrollDistance distance = (scrollDistance.length > 0) ? scrollDistance[0] : SHORT;
        searchPage.openContentUnderCategoryWithHeader(categoryType, LIST, distance, 1, false);
        return contentProfilePage;
    }

    @Step
    public NowPlayingPage searchByGuideIdAndOpenFirstInCategory(String query, String category) {
        typeSearchText(query);
        clickOnElement(getFirstSearchResultInCategory(category));
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

    // Used in stream test
    public NowPlayingPage searchByGuideIdAndOpenFirstInCategoryForStreamTest(String query, String category) {
        typeSearchText(query);
        clickOnElement(getFirstSearchResultInCategory(category));
        nowPlayingPage.waitUntilStreamStopOpeningAndBuffering();
        closePermissionPopupFor(USER_TRACKING);
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

    public abstract SearchPage clearRecentSearchesByIndex(int index);

    @Step
    public SearchPage clearAllRecentSearches() {
        clickOnElement(searchClearRecentSearchesButton);
        return this;
    }

    @Step
    public SearchPage clearAllRecentSearchesIfDisplayed() {
        clickOnElementIfDisplayed(searchClearRecentSearchesButton);
        return this;
    }

    @Step("Click on card {cardType} in What do you want to listen section")
    public abstract ContentsListPage openCardInWhatDoYouWantToListen(WhatToListenCategory cardType);

    /* --- Validation Methods --- */

    @Step
    public SearchPage validateThatOnSearchPage() {
        assertThat(isOnSearchPage()).as("Search page was not opened").isTrue();
        return this;
    }

    @Step("Validate search item {content} under category {categoryType}")
    public SearchPage validateThatSearchItemUnderCategory(CategoryType categoryType, String content) {
        SelenideElement requiredContent = getContentWithLabelUnderCategoryWithHeader(categoryType, LIST, SHORT, content, 30);
        assertThat(isElementDisplayed(requiredContent)).as("Search page was not opened").isTrue();
        return this;
    }

    @Step("Verify What do you want to Listen is present from search")
    public SearchPage validateWhatDoYouWantToListenToIsDisplayed() {
        assertThat(isElementDisplayed(searchWhatDoYouWantToListenLabel)).as("What Do You Want toListen To was not there on search page").isTrue();
        return this;
    }

    @Step("Validate Trending search tags")
    public SearchPage validateTrendingSearchesIsDisplayed() {
        scrollTo(searchTrendingSearchesContainer, DOWN);
        assertThat(searchTrendingSearchesTags).as("Trending Searches tags are absent").isNotEmpty();
        return this;
    }

    @Step
    public SearchPage validateThatSearchPlaceholderIsDisplayed() {
        assertThat(getTextFromSearchField()).as("Search placeholder isn't displayed").isEqualTo(SEARCH_PLACEHOLDER);
        return this;
    }

    @Step
    public SearchPage validateThatRecentSearchesHasItem(String searchValue) {
        assertThat(searchRecentList)
                .as("Search station isn't displayed in Recent Searches")
                .anyMatch(recentElement -> getRecentItemName(recentElement).contains(searchValue));
        return this;
    }

    @Step("Validating recents list {searchValues} on search page")
    public SearchPage validateThatRecentListSearchesHasItems(List<String> searchValues) {
        List<String> listOfRecentsValues = searchRecentList.asFixedIterable().stream().map(recent -> getRecentItemName(recent)).collect(Collectors.toList());
        assertThat(listOfRecentsValues).as("Recent search stations are not displayed in correct order").containsExactlyElementsOf(searchValues);
        return this;
    }

    @Step
    public SearchPage validateThatSearchResultsHasItem(String searchValue) {
        assertThat(getSearchResultWithStreamName(searchValue).isDisplayed())
                .as("Search station isn't displayed in Search results")
                .isTrue();
        return this;
    }

    @Step
    public SearchPage validateSearchFieldHasValue(String textValue) {
        assertThat(getTextFromSearchField())
                .as("Search Field has wrong input text")
                .isEqualTo(textValue);
        return this;
    }

    @Step
    public SearchPage validateNoResultsText(String searchValue) {
        assertThat(getTextFromNoResultsLabel())
                .as("No results text doesn't match expected")
                .isEqualTo(String.format(NO_RESULTS, searchValue));
        return this;
    }

    @Step
    public SearchPage validateNumberOfRecentsIsCorrect(int expectedNumber) {
        tapOnSearchField();
        assertThat(getNumberOfRecentSearchResults())
                .as("Number of recents items is incorrect")
                .isEqualTo(expectedNumber);
        return this;
    }

    @Step
    public SearchPage validateDidYouMean() {
        assertThat(searchDidYouMeanLabel.isDisplayed()).as("Did you mean? label is not displayed").isTrue();
        return this;
    }

    @Step
    public SearchPage validateDidYouMeanSuggestion(String expectedSuggestion) {
        assertThat(getElementText(searchDidYouMeanSuggestion))
                .as("Search suggestion doesn't match expected")
                .isEqualTo(expectedSuggestion);
        return this;
    }

    @Step("Validate first search result is equal to {searchResultName}")
    public SearchPage validateFirstSearchResultNameEqualsTo(String category, String searchResultName) {
        String firstResultName = getElementNameOrLabel(getFirstSearchResultInCategory(category, SHORT));
        assertThat(firstResultName).as("Search results are different").isEqualTo(searchResultName);
        return this;
    }

    @Step("Validate required category {category} is opened from search page")
    public SearchPage validateThatRequiredSearchCategoryIsOpened(WhatToListenCategory category) {
        switch (category) {
            case MUSIC -> contentsListPage.validateThatCategoryIsDisplayed(CATEGORY_TYPE_TOP_MUSIC_GENRES);
            case NEWS_AND_TALK -> contentsListPage.validateThatCategoryIsDisplayed(CATEGORY_TYPE_LOCAL_NEWS_RADIO);
            case SPORTS -> contentsListPage.validateThatCategoryIsDisplayed(CATEGORY_TYPE_EXPLORE_BY_SPORT);
            case PODCASTS -> contentsListPage.validateThatCategoryIsDisplayed(CATEGORY_TYPE_TOP_PODCASTS_IN_YOUR_COUNTRY);
            case LOCAL -> contentsListPage.validateThatCategoryIsDisplayed(LOCAL_RADIO);
            case INTERNATIONAL -> contentsListPage.validateThatCategoryIsDisplayed(BY_LOCATION_REGIONS_CATEGORY);
            case AUDIOBOOKS -> contentsListPage.validateThatCategoryIsDisplayed(CATEGORY_TYPE_TOP_BOOK_SERIES);
            default -> throw new Error("Invalid category type " + category);
        }
        return this;
    }

    /* --- Helper Methods --- */

    private boolean isSearchFieldPrePopulated() {
        try {
            return !getElementText(waitVisibilityOfElement(searchInputTextField)).isEmpty()
                    && !getElementText(waitVisibilityOfElement(searchInputTextField)).equals(SEARCH_PLACEHOLDER);
        } catch (Throwable e) {
            return false;
        }
    }

    @Step("Wait for nowplaying page content type {categoryType}")
    private NowPlayingPage waitForNowplayingPageWithContentType(ContentType contentType) {
        if (contentProfilePage.isOnContentProfilePage()) contentProfilePage.tapProfilePlayButton();
        return nowPlayingPage.waitUntilPageReadyWithKnownContentType(contentType);
    }

    public boolean isOnSearchPage() {
        return isElementDisplayed(searchInputTextField);
    }

    public abstract boolean isCancelButtonDisplayed();

    public abstract String getRecentItemName(SelenideElement recentItem);

    public String getTextFromSearchField() {
        return getElementText(searchInputTextField);
    }

    public String getTextFromSearchTag(int index) {
        return getElementText(searchTrendingSearchesTags.get(index - 1));
    }

    public int getNumberOfRecentSearchResults() {
        return searchRecentList.size();
    }

    public abstract SelenideElement getFirstSearchResultInCategory(String category, ScrollDistance... scrollDistance);

    public abstract SelenideElement getSearchResultWithStreamName(String streamName);

    public abstract SelenideElement getSearchResultWithStreamNameInCategory(String categoryName, String streamName);

    protected SelenideElement getFirstSearchResult() {
        return waitVisibilityOfElement(searchResultsList.get(0));
    }

    protected SearchPageState getSearchPageState() {
        if (isElementDisplayed(searchTrendingSearchesLabel)) {
            return BEFORE_SEARCH_INACTIVE_FIELD;
        } else if (isElementDisplayed(searchClearRecentSearchesButton)) {
            return BEFORE_SEARCH_ACTIVE_FIELD_RECENTS_PRESENT;
        } else if (isElementDisplayed(searchWhatDoYouWantToHearLabel)) {
            return BEFORE_SEARCH_ACTIVE_FIELD_NO_RECENTS;
        } else if (searchResultsList.size() > 1) {
            return SEARCH_WITH_RESULTS;
        }
        if (isElementDisplayed(searchNoResultsLabel)) {
            return SEARCH_NO_RESULTS;
        } else {
            return UNKNOWN_STATE;
        }
    }

    public String getTextFromNoResultsLabel() {
        return getElementText(searchNoResultsLabel);
    }

    public String getNameOfFirstSearchResultInCategory(String category) {
        return getElementNameOrLabel(getFirstSearchResultInCategory(category, SHORT));
    }

    public abstract HashMap<String, SelenideElement> searchPageElements(SearchPageState expectedPageState);

    public enum SearchPageState {
        BEFORE_SEARCH_INACTIVE_FIELD,
        BEFORE_SEARCH_ACTIVE_FIELD_RECENTS_PRESENT,
        BEFORE_SEARCH_ACTIVE_FIELD_NO_RECENTS,
        SEARCH_WITH_RESULTS,
        SEARCH_NO_RESULTS,
        UNKNOWN_STATE
    }

    public enum WhatToListenCategory {
        NEWS_AND_TALK("News & Talk"),
        LOCAL("Local"),
        INTERNATIONAL("International"),
        SPORTS("Sports"),
        PODCASTS("Podcasts"),
        AUDIOBOOKS("Audiobooks"),
        MUSIC("Music");
        private String whatToListenCard;

        WhatToListenCategory(String whatToListenCard) {
            this.whatToListenCard = whatToListenCard;
        }

        public String getWhatToListenCardName() {
            return whatToListenCard;
        }
    }

}
