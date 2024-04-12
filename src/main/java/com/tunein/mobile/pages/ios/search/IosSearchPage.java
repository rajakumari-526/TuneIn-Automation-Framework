package com.tunein.mobile.pages.ios.search;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.search.SearchPage;
import com.tunein.mobile.utils.GestureActionUtil.ScrollDistance;
import com.tunein.mobile.utils.GestureActionUtil.ScrollDirection;
import com.tunein.mobile.utils.GestureActionUtil.SwipeDirection;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;

import org.openqa.selenium.By;

import java.time.Duration;
import java.util.HashMap;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosSearchPage extends SearchPage {

    /* --- Constants --- */

    private static final String SEARCH = "Search";

    private static final String TRENDING_SEARCHES = "Trending Searches";

    private static final String WHAT_DO_YOU_WANT_TO_LISTEN = "What do you want to listen to?";

    private static final String WHAT_DO_YOU_WANT_TO_HEAR = "What do you want to hear?";

    private static final String CANCEL = "Cancel";

    private static final String RECENT_SEARCHES = "Recent Searches";

    private static final String CLEAR_RECENT_SEARCHES = "Clear Recent Searches. ";

    private String searchTagNameLocator = "//*[contains(@name, 'Trending')]/following-sibling::XCUIElementTypeCell[1]//XCUIElementTypeCell[contains(@label,'%s')]";

    private String searchResultForExplorerCategoryLocatorByIndex = "(//XCUIElementTypeOther[@name ='%s']/following-sibling::*//XCUIElementTypeOther[@name = 'tileCellViewIdentifier'])[%s]";

    private String searchResultForRegularCategoryLocatorByIndex = "(//XCUIElementTypeOther[@name = '%s']/following-sibling::*/XCUIElementTypeOther)[%s]";

    private String searchResultWithNameInCategoryLocator = "(//XCUIElementTypeOther[@name = '%s']/following-sibling::*/XCUIElementTypeOther[contains(@label,'%s')])[1]";

    private String searchResultLocatorByName = "(//*[contains(@label,'%s')])[1]";

    private String searchExploreCategoryLocatorByName = "//*[contains(@name,'%s')]/following-sibling::*//XCUIElementTypeCollectionView";

    /* --- Locators --- */

    protected SelenideElement searchCancelButton = $(iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name == 'CANCEL'")).as("Cancel button");

    /* --- Helper Methods --- */

    @Step
    @Override
    public SearchPage tapCancelButton() {
        clickOnElement(searchCancelButton);
        return this;
    }

    @Step
    @Override
    public SearchPage tapOnSearchTagByName(String tagName) {
        clickOnElement(By.xpath(String.format(searchTagNameLocator, tagName)));
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    @Step("Type text to search")
    @Override
    public SearchPage typeSearchText(String searchQuery) {
        clearSearchResults();
        deviceNativeActions.typeText(searchInputTextField, searchQuery);
        clickOnElement(By.id("Search"));
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    @Step
    @Override
    public SearchPage clearRecentSearchesByIndex(int index) {
        clickOnElement(searchRecentList.get(index - 1).$(By.xpath(".//*[contains(@name,'DismissKey')]")));
        return this;
    }

    @Override
    public boolean isCancelButtonDisplayed() {
        return isElementDisplayed(searchCancelButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
    }

    public ContentsListPage openCardInWhatDoYouWantToListen(WhatToListenCategory contentLabel) {
        String ios = "//*[./*[contains(@name, 'What do you want to listen to?')]]/following-sibling::*//XCUIElementTypeCell[.//*[@label='%s']]";
        By requiredCard = By.xpath(String.format(ios, contentLabel.getWhatToListenCardName()));
        clickOnElement(requiredCard);
        return contentsListPage.waitUntilPageReady();
    }

    @Override
    public String getRecentItemName(SelenideElement recentItem) {
        return getElementName(recentItem.$(By.xpath(".//XCUIElementTypeOther"))).replace(". ", "");
    }

    @Override
    public SelenideElement getFirstSearchResultInCategory(String category, ScrollDistance... scrollDistance) {
        ScrollDistance distance = (scrollDistance.length > 0) ? scrollDistance[0] : ScrollDistance.SHORT;
        if (category.contains("Explore")) {
                scrollToRefresh();
                return scrollTo(By.xpath(String.format(searchResultForExplorerCategoryLocatorByIndex, category, 1)), DOWN, distance);
            } else {
                scrollToRefresh();
                return scrollTo(By.xpath(String.format(searchResultForRegularCategoryLocatorByIndex, category, 1)), DOWN, distance);
            }
     }

    @Override
    public SelenideElement getSearchResultWithStreamName(String streamName) {
        return scrollTo(By.xpath(String.format(searchResultLocatorByName, streamName)), ScrollDirection.DOWN);
    }

    @Override
    public SelenideElement getSearchResultWithStreamNameInCategory(String categoryName, String streamName) {
        if (categoryName.contains("Explore")) {
           SelenideElement collectionElement = scrollTo(By.xpath(String.format(searchExploreCategoryLocatorByName, categoryName)), ScrollDirection.DOWN);
            return swipeToElement(By.xpath(String.format(searchResultLocatorByName, streamName)), 5, SwipeDirection.LEFT, collectionElement);
        } else {
            return scrollTo(By.xpath(String.format(searchResultWithNameInCategoryLocator, categoryName, streamName)), ScrollDirection.DOWN);
        }
    }

    @Override
    public HashMap<String, SelenideElement> searchPageElements(SearchPageState expectedPageState) {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "1", searchInputTextField);
        switch (expectedPageState) {
            case BEFORE_SEARCH_INACTIVE_FIELD -> {
               // elementsMap.put(TRENDING_SEARCHES, searchTrendingSearchesLabel); dynamic data
                elementsMap.put(WHAT_DO_YOU_WANT_TO_LISTEN, searchWhatDoYouWantToListenLabel);
               // elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "3", searchTrendingSearchesContainer); dynamic data
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", searchWhatDoYouWantToListenContainer);
            }
            case BEFORE_SEARCH_ACTIVE_FIELD_RECENTS_PRESENT -> {
                elementsMap.put(CANCEL, searchCancelButton);
                elementsMap.put(RECENT_SEARCHES, searchRecentSearchesLabel);
                elementsMap.put(CLEAR_RECENT_SEARCHES, searchClearRecentSearchesButton);
            }
            case BEFORE_SEARCH_ACTIVE_FIELD_NO_RECENTS -> {
                elementsMap.put(CANCEL, searchCancelButton);
                elementsMap.put(WHAT_DO_YOU_WANT_TO_HEAR, searchWhatDoYouWantToHearLabel);
            }
            case SEARCH_WITH_RESULTS -> {
                elementsMap.put(CANCEL, searchCancelButton);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "5", searchResultsList.get(0));
            }
            case SEARCH_NO_RESULTS -> {
                elementsMap.put(CANCEL, searchCancelButton);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "6", searchNoResultsLabel);
            }
            default -> throw new Error("Wrong expected search state");
        }
        return elementsMap;
    }
}
