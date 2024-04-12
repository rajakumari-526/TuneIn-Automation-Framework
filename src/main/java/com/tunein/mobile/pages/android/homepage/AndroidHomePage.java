package com.tunein.mobile.pages.android.homepage;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.google.common.collect.Iterables;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidHomePage extends HomePage {

    private String homePageCardForCategoryLocator = "//*[./*[contains(@text,'%s')]]/following-sibling::*/android.widget.GridView";

    private String homePageContentByNameLocator = "//android.view.ViewGroup[./android.widget.TextView[@text='%s']]";

    private static final String ANDROID_CATEGORY_LOCATOR = "//*[@resource-id='tunein.player:id/view_model_container_title' and @text=\"%s\"]";
  
    private static final String HOME_PAGE_LOCATOR_VIEW_GROUP = "//android.view.ViewGroup";

    private static final String HOME_PAGE_GREEN_ICON_TRIANGLE = "tunein.player:id/row_status_badge";

    private static final String HOME_VIEW_GROUP = HOME_PAGE_LOCATOR_VIEW_GROUP + "/android.view.ViewGroup";

    private String recentsLocatorByName = "//androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup/following-sibling::*/android.widget.TextView[@text='%s']";

    public ElementsCollection brickCells = $$x("//*[@resource-id=\"tunein.player:id/row_banner_cell_image_container\"]").as("Brick cells");

    /* --- Validation Methods --- */

    //TODO RADIO tabs when they will be ready and implemented
    @Step
    @Override
    public HomePage validateThatAllBrowsiesBarTabsCanBeSelected() {
        List<BrowsiesBarTabsLabels> browsiesBarTabsLabels = Arrays.asList(FOR_YOU, SPORTS, MUSIC, AUDIOBOOKS, I_HEART_RADIO, NEWS_AND_TALK, PODCASTS, BY_LANGUAGE, BY_LOCATION);
        browsiesBarTabsLabels
                .stream()
                .forEach(browsiesBarTab -> {
                    String browsiesBarTabLabelValue = browsiesBarTab.getbrowsiesBarTabName();
                    tapOnRequiredBrowsiesBarTab(browsiesBarTab);
                    getSoftAssertion().assertThat(isBrowsiesBarTabSelected(browsiesBarTab))
                            .as("Browsie Bar tab " + browsiesBarTabLabelValue + " is not selected")
                            .isTrue();
                });
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Long press & remove {contentName} from {categoryType}")
    public void removeRecentsByContentNameUnderCategory(CategoryType categoryType, String contentName) {
        SelenideElement element = contentsListPage.getContentWithLabelUnderCategoryWithHeader(categoryType, TILE, MEDIUM, contentName);
        longPressOnElement(element);
        contentListItemDialog
                .waitUntilPageReady()
                .clickOnRemoveRecentsDialog();
    }

    @Step("Verify that {contentName} is not displayed under Recents under Homepage")
    public void validateThatContentNotDisplayedUnderRecents(String contentName) {
        String format = String.format(recentsLocatorByName, contentName);
        assertThat(isElementNotDisplayed(By.xpath(format), Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))).as(contentName + " is displayed in recents when it shouldn't be").isTrue();
    }

    @Step("Validate visibility (visibility={expectedGreenBadgeStatus}) for green badge for episode with index {index} under category {categoryType}")
    @Override
    public HomePage validateGreenIconStateForEpisodeCell(CategoryType categoryType, boolean expectedGreenBadgeStatus, int index) {
        if (index < 0) throw new Error("Index should be equal to or greater than 0");
        SelenideElement episode = homePage.getContentUnderCategoryWithHeader(categoryType, TILE, SHORT, index);
        SelenideElement greenBadge = getElementFromParent(episode, By.id(String.format(HOME_PAGE_GREEN_ICON_TRIANGLE)));
        if (expectedGreenBadgeStatus) {
            assertThat(isElementDisplayed(greenBadge)).as("Green Badge is not displayed").isTrue();
        } else {
            assertThat(isElementNotDisplayed(greenBadge, Duration.ofMillis(config().waitCustomTimeoutMilliseconds()))).as("Green badge is displayed").isTrue();
        }
        return this;
    }

    /* --- Action Methods --- */

    @Step
    @Override
    public HomePage tapOnAllBrowsiesBarTabs() {
        List<BrowsiesBarTabsLabels> browsiesBarTabsLabels = Arrays.asList(BrowsiesBarTabsLabels.values());
        browsiesBarTabsLabels
                .stream()
                .forEach(browsiesBarTab -> {
                    ReporterUtil.log("Should be opened " + browsiesBarTab + " browsies");
                    tapOnRequiredBrowsiesBarTab(browsiesBarTab);
                });
        return this;
    }

    @Override
    public boolean isBrowsiesBarTabSelected(BrowsiesBarTabsLabels browsiesBarTabLabel) {
       SelenideElement browsiesTab = getRequiredBrowsiesTab(browsiesBarTabLabel);
        return browsiesTab.isSelected();
    }

    @Override
    public int getBrowsiesBarTabOrderNumber(BrowsiesBarTabsLabels browsiesBarTabLabel) {
        switch (browsiesBarTabLabel) {
            case FOR_YOU -> {
                return 0;
            }
            case RADIO -> {
                return 1;
            }
            case SPORTS -> {
                return 2;
            }
            case AUDIOBOOKS -> {
                return 3;
            }
            case I_HEART_RADIO -> {
                return 4;
            }
            case NEWS_AND_TALK -> {
                return 5;
            }
            case PODCASTS -> {
                return 6;
            }
            case MUSIC -> {
                return 7;
            }
            case BY_LANGUAGE -> {
                return 8;
            }
            default -> throw new Error("Invalid browsies tab type - " + browsiesBarTabLabel);
        }
    }

    @Override
    public SelenideElement getSelectedBrowsiesBarTab() {
        return homePageBrowsiesBarElements
                .asDynamicIterable()
                .stream()
                .filter(browsiesBarTab -> browsiesBarTab.isSelected())
                .findFirst()
                .get();
    }

    @Override
    public By getBrowsiesBarTabBy(BrowsiesBarTabsLabels browsiesBarTabsLabel) {
        return androidUIAutomator(String.format("textContains(\"%s\").resourceId(\"tunein.player:id/browsies_tab_tag\")", browsiesBarTabsLabel.getbrowsiesBarTabName()));
    }

    @Step("Validate category {categoryType} is absent")
    @Override
    public void validateCategoryIsAbsent(CategoryType categoryType) {
    }

    @Step("Get index of first content with green badge status {greenBadgeStatus} under category {categoryType}")
    @Override
    public int getIndexOfFirstContentGreenBadgeStatusInCategory(CategoryType categoryType, boolean greenBadgeStatus, boolean swipeCard, boolean... skipError) {
        SelenideElement requiredCard;
        try {
            requiredCard = scrollTo(By.xpath(String.format(homePageCardForCategoryLocator, categoryType.getCategoryTypeValue())), DOWN, SHORT, config().scrollReallyLongTime());
        } catch (Error error) {
            if (skipError.length > 0 && skipError[0]) {
                return -1;
            } else {
                throw new Error("Category type " + categoryType.getCategoryTypeValue() + " was not found");
            }
        }
        for (int i = 0; i < ((swipeCard) ? 3 : 1); i++) {
            int index;
            ElementsCollection cellsInCard = requiredCard.$$(By.xpath(".//android.view.ViewGroup[./android.widget.ImageView]"));
            index = Iterables.indexOf(cellsInCard, cell ->
                    greenBadgeStatus
                            ? isElementFullyVisible(cell) && getElementFromParent(cell, By.id(String.format(HOME_PAGE_GREEN_ICON_TRIANGLE))).isDisplayed()
                            : isElementFullyVisible(cell) && !getElementFromParent(cell, By.id(String.format(HOME_PAGE_GREEN_ICON_TRIANGLE))).isDisplayed()
            );
            if (index != -1) return index;
            swipeElement(LEFT, config().defaultSwipeDistance(), requiredCard);
        }
        if (skipError.length > 0 && skipError[0]) {
            return -1;
        } else {
            String errorMessage = greenBadgeStatus
                    ? "Episodes with new badge is absent under category " + categoryType
                    : "Podcast without green badge is absent";
            throw new Error(errorMessage);
        }
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
        int count = requiredCard.$$x(".//android.view.ViewGroup[./*[contains(@resource-id,'row_tile_image_wrapper')]]").size();
        assertThat(count).as("Number of elements in " + categoryType.getCategoryTypeValue() + " card is not equal to " + expectedNumber).isEqualTo(expectedNumber);
        return this;
    }

    @Step
    @Override
    public ContentProfilePage tapOnLastBrickCellUnderCategory(CategoryType categoryType) {
        scrollTo(By.xpath(String.format(ANDROID_CATEGORY_LOCATOR, categoryType.getCategoryTypeValue())), DOWN);
        int totalBrickCells = brickCells.size();
        clickOnElement(brickCells.get(totalBrickCells - 1));
        return contentProfilePage.waitUntilPageReady();
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
        assertThat(isElementDisplayed(scrollTo(By.xpath(String.format(homePageContentByNameLocator, categoryType.getCategoryTypeValue())), DOWN, 20))).as(categoryType + " is not displayed").isTrue();
    }

    @Step("Remove recents by longpress")
    @Override
    public void removeRecentsContentWithIndexUnderCategory(CategoryType categoryType, int index) {
        SelenideElement element = contentsListPage.getContentUnderCategoryWithHeader(categoryType, TILE, MEDIUM, index);
        longPressOnElement(element);
        contentListItemDialog
                .waitUntilPageReady()
                .clickOnRemoveRecentsDialog();
    }


    /* --- Helper Methods --- */

    @Step
    @Override
    public String getRecentsContentName(String contentName) {
        String format = String.format(recentsLocatorByName, contentName);
        return getElementText(scrollTo(By.xpath(format), DOWN, 3));
    }
}
