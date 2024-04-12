package com.tunein.mobile.pages.ios.homepage;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.RADIO;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.getBrowsiesBarTabType;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class IosHomePage extends HomePage {

    private static final String SELECTED_BROWSIES_BAR_TAB_SUFFIX = "_selected";

    private String homePageCardForCategoryLocator = "//XCUIElementTypeOther[contains(@name,'%s')]/following-sibling::XCUIElementTypeCell[1]";

    private String homePageContentByNameLocator = "//XCUIElementTypeCell/following-sibling::XCUIElementTypeOther[@name='%s' and @visible='true']";

    private String homePageWhatDoYouWantToListenToLocator = "//XCUIElementTypeCell[@name='%s']/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeImage";

    private String recentsLocatorByName = "//XCUIElementTypeOther/following-sibling::*/XCUIElementTypeOther[contains(@name,\"%s\")]";

    @Step
    @Override
    public HomePage validateThatAllBrowsiesBarTabsCanBeSelected() {
        homePageBrowsiesBarElements
                .asDynamicIterable()
                .stream()
                .forEach(browsiesBarTab -> {
                    String browsiesBarTabLabelValue = getElementContentDescOrLabel(browsiesBarTab);
                    BrowsiesBarTabsLabels browsiesBarTabLabel = getBrowsiesBarTabType(browsiesBarTabLabelValue);
                    if (browsiesBarTabLabel != RADIO) {
                        tapOnRequiredBrowsiesBarTab(browsiesBarTabLabel);
                        getSoftAssertion().assertThat(isBrowsiesBarTabSelected(browsiesBarTabLabel)).as("Browsie Bar tab " + browsiesBarTabLabelValue + " is not selected").isTrue();
                    }
                });
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public HomePage tapOnAllBrowsiesBarTabs() {
        homePageBrowsiesBarElements
                .asFixedIterable()
                .stream()
                .forEach(browsiesBarTab -> {
                    String browsiesBarTabLabelValue = getElementContentDescOrLabel(browsiesBarTab);
                    BrowsiesBarTabsLabels browsiesBarTabLabel = getBrowsiesBarTabType(browsiesBarTabLabelValue);
                    ReporterUtil.log("Should be opened " + browsiesBarTabLabel + " browsies");
                    tapOnRequiredBrowsiesBarTab(browsiesBarTabLabel);
                });
        return this;
    }

    @Override
    public boolean isBrowsiesBarTabSelected(BrowsiesBarTabsLabels browsiesBarTabLabel) {
       SelenideElement browsiesTab = getRequiredBrowsiesTab(browsiesBarTabLabel);
        return getElementName(browsiesTab).contains(SELECTED_BROWSIES_BAR_TAB_SUFFIX);
    }

    @Override
    public int getBrowsiesBarTabOrderNumber(BrowsiesBarTabsLabels browsiesBarTabLabel) {
        String browsesBarTabId = homePageBrowsiesBarElements
                .asFixedIterable()
                .stream()
                .filter(browsesBarTab -> browsesBarTab.getAttribute("label").contains(browsiesBarTabLabel.getbrowsiesBarTabName()))
                .map(requiredBrowsesBarTab -> requiredBrowsesBarTab.getAttribute("name"))
                .findFirst()
                .get();
        int indexOfBrowsiesTab = Integer.parseInt(browsesBarTabId.replace("browsiesHomeButtonId", "").replace(SELECTED_BROWSIES_BAR_TAB_SUFFIX, ""));
        return indexOfBrowsiesTab;
    }

    @Override
    public SelenideElement getSelectedBrowsiesBarTab() {
       return homePageBrowsiesBarElements
               .asDynamicIterable()
               .stream()
               .filter(browsiesBarTab -> getElementName(browsiesBarTab).contains(SELECTED_BROWSIES_BAR_TAB_SUFFIX))
               .findFirst()
               .get();
    }

    @Override
    public By getBrowsiesBarTabBy(BrowsiesBarTabsLabels browsiesBarTabsLabel) {
        return By.xpath(String.format("//XCUIElementTypeButton[@label = '%s']", browsiesBarTabsLabel.getbrowsiesBarTabName()));
    }

    @Step("Validate category {categoryType} is absent")
    @Override
    public void validateCategoryIsAbsent(CategoryType categoryType) {
        assertThat(isElementNotDisplayed(By.xpath(String.format(homePageContentByNameLocator, categoryType.getCategoryTypeValue())), Duration.ofSeconds(5))).as("Your Games is visible").isTrue();
    }

    @Step("Open content with name {contentName} in category {categoryType}")
    @Override
    public void openContentItemWithNameInCategory(CategoryType categoryType, String contentName) {
        SelenideElement requiredCard = scrollTo(By.xpath(String.format(homePageCardForCategoryLocator, categoryType.getCategoryTypeValue())), DOWN);
        By locator = By.xpath(String.format(homePageContentByNameLocator, contentName));
        SelenideElement requiredContent = swipeToElement(locator, 5, LEFT, requiredCard);
        clickOnElement(requiredContent);
    }

    @Step("Validate that number of elements on Homepage under category {categoryType} should be to equal {expectedNumber}")
    @Override
    public HomePage validateNumberOfElementsInHomePageCategory(CategoryType categoryType, ScrollDirection direction, int expectedNumber) {
        SelenideElement requiredCard = scrollTo(By.xpath(String.format(homePageCardForCategoryLocator, categoryType.getCategoryTypeValue())), direction);
        int count = requiredCard.$$x(".//XCUIElementTypeCollectionView/XCUIElementTypeCell").size();
        assertThat(count).as("Number of elements in " + categoryType.getCategoryTypeValue() + " card is not equal to " + expectedNumber).isEqualTo(expectedNumber);
        return this;
    }

    @Step("Get index of first content with green badge status {greenBadgeStatus} under category {categoryType}")
    @Override
    public int getIndexOfFirstContentGreenBadgeStatusInCategory(CategoryType categoryType, boolean greenBadgeStatus, boolean swipeCard, boolean... skipError) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform because badge locator is not available");
    }

    @Step
    @Override
    public ContentProfilePage tapOnLastBrickCellUnderCategory(CategoryType categoryType) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Step("Swipe required category {categoryType} {numberOfSwipe} times")
    @Override
    public void swipeRequiredCategory(CategoryType categoryType, ScrollDirection direction, int numberOfSwipe) {
        SelenideElement requiredCard = scrollTo(By.xpath(String.format(homePageCardForCategoryLocator, categoryType.getCategoryTypeValue())), direction);
        for (int i = 0; i < numberOfSwipe; i++) {
            swipeElement(LEFT, config().defaultSwipeDistance(), requiredCard);
        }
    }

    @Step("Validate category with header {categoryType} is present")
    @Override
    public void validateCategoryHeaderIsPresent(CategoryType categoryType) {
        SelenideElement requiredCard = scrollTo(By.xpath(String.format(homePageContentByNameLocator, categoryType.getCategoryTypeValue())), DOWN);
        assertThat(isElementDisplayed(requiredCard)).as(categoryType + " is not displayed").isTrue();
    }

    @Step
    @Override
    public void removeRecentsContentWithIndexUnderCategory(CategoryType categoryType, int index) {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public void removeRecentsByContentNameUnderCategory(CategoryType categoryType, String contentName) {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public void validateThatContentNotDisplayedUnderRecents(String contentName) {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Override
    public HomePage validateGreenIconStateForEpisodeCell(CategoryType categoryType, boolean expectedGreenBadgeStatus, int index) {
        throw new UnsupportedOperationException("iOS platform doesn't have locators for green badge");
    }

    @Step
    @Override
    public String getRecentsContentName(String contentName) {
        String format = String.format(recentsLocatorByName, contentName);
        String subtitle = getElementNameOrLabel(scrollTo(By.xpath(format), DOWN, 3));
        String[] dataArray = subtitle.split("\\. ", 0);
        return dataArray[0];
    }
}
