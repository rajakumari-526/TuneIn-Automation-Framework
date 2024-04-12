package com.tunein.mobile.pages.android.search;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.search.SearchPage;
import com.tunein.mobile.utils.GestureActionUtil.ScrollDistance;
import org.openqa.selenium.By;

import java.util.HashMap;

import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;

public class AndroidSearchPage extends SearchPage {

    private String searchTagNameLocator = "//*[contains(@resource-id, 'tag_group')]/android.widget.Button[contains(@text,'%s')]";

    private String searchResultForRegularCategoryLocatorByIndex = "(//android.widget.FrameLayout[.//android.widget.TextView[contains(@text,'%s')]]/following-sibling::android.view.ViewGroup)[%s]";

    private String searchResultLocatorByName = "(//android.view.ViewGroup[./android.widget.TextView[contains(@text,'%s')]])[1]";

    private String searchResultWithNameInCategoryLocator = "(//android.widget.FrameLayout[.//android.widget.TextView[contains(@text,'%s')]]/following-sibling::android.view.ViewGroup[./android.widget.TextView[contains(@text, '%s')]])[1]";

    @Override
    public SearchPage tapCancelButton() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public SearchPage tapOnSearchTagByName(String tagName) {
        clickOnElement(By.xpath(String.format(searchTagNameLocator, tagName)));
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    @Step("Type text {searchQuery} to search")
    @Override
    public SearchPage typeSearchText(String searchQuery) {
        clearSearchResults();
        deviceNativeActions.typeText(searchInputTextField, searchQuery);
        return this.waitUntilSuccessfulResultsIsLoaded();
    }

    @Step
    @Override
    public SearchPage clearRecentSearchesByIndex(int index) {
        clickOnElement(searchRecentList.get(index - 1).$(By.xpath(".//*[contains(@resource-id,'delete_button')]")));
        return this;
    }

    @Override
    public boolean isCancelButtonDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Override
    public ContentsListPage openCardInWhatDoYouWantToListen(WhatToListenCategory contentLabel) {
        String android = "//*[./*[contains(@text, 'What do you want to listen to?')]]/following-sibling::*//android.widget.TextView[@text='%s']";
        By requiredCard = By.xpath(String.format(android, contentLabel.getWhatToListenCardName()));
        clickOnElement(requiredCard);
        return contentsListPage.waitUntilPageReady();
    }

    @Override
    public String getRecentItemName(SelenideElement recentItem) {
        return getElementText(recentItem.$(By.xpath(".//*[contains(@resource-id,'recent_search_label')]")));
    }

    @Override
    public SelenideElement getFirstSearchResultInCategory(String category, ScrollDistance... scrollDistance) {
        ScrollDistance distance = (scrollDistance.length > 0) ? scrollDistance[0] : ScrollDistance.SHORT;
        SelenideElement requiredContent = scrollTo(By.xpath(String.format(searchResultForRegularCategoryLocatorByIndex, category, 1)), DOWN, distance);
        return requiredContent;
    }

    @Override
    public SelenideElement getSearchResultWithStreamName(String streamName) {
        return scrollTo(By.xpath(String.format(searchResultLocatorByName, streamName)), DOWN);
    }

    @Override
    public SelenideElement getSearchResultWithStreamNameInCategory(String categoryName, String streamName) {
        return scrollTo(By.xpath(String.format(searchResultWithNameInCategoryLocator, categoryName, streamName)), DOWN);
    }

    @Override
    public HashMap<String, SelenideElement> searchPageElements(SearchPageState expectedPageState) {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "1", searchInputTextField);

        switch (expectedPageState) {
            case BEFORE_SEARCH_INACTIVE_FIELD -> {
               /// elementsMap.put("Trending Searches", searchTrendingSearchesLabel); dynamic data
                elementsMap.put("What do you want to listen to?", searchWhatDoYouWantToListenLabel);
               // elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "2", searchTrendingSearchesContainer);  dynamic data
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "3", searchWhatDoYouWantToListenContainer);
            }
            case BEFORE_SEARCH_ACTIVE_FIELD_RECENTS_PRESENT -> {
                elementsMap.put("Recent Searches", searchRecentSearchesLabel);
                elementsMap.put("Clear Recent Searches", searchClearRecentSearchesButton);
            }
            case BEFORE_SEARCH_ACTIVE_FIELD_NO_RECENTS -> elementsMap.put("What do you want to hear?", searchWhatDoYouWantToHearLabel);
            case SEARCH_WITH_RESULTS -> elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", searchResultsList.get(0));
            case SEARCH_NO_RESULTS -> elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "5", searchNoResultsLabel);
            default -> throw new Error("Wrong expected search state");
        }
        return elementsMap;
    }

}
