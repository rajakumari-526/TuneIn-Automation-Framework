package com.tunein.mobile.pages.ios.explorer;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.explorer.ExplorerPage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.tunein.mobile.pages.common.explorer.ExplorerPage.ExplorerBarTabsLabels.getExplorerBarTabType;
import static com.tunein.mobile.utils.ElementHelper.getElementName;
import static com.tunein.mobile.utils.ElementHelper.getElementValue;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.isElementFullyVisible;
import static com.tunein.mobile.utils.GestureActionUtil.swipeToElement;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;

public class IosExplorerPage extends ExplorerPage {

    /* --- Loadable Component Method --- */

    /* --- Action Methods --- */

    @Step("Enter Search station {station}")
    @Override
    public ExplorerPage typeSearch(String station) {
        deviceNativeActions.typeText(explorerSearchButton, station);
        clickOnElementIfDisplayed($(id("Search")));
        return this;
    }

    @Step("Clear search text field")
    @Override
    public ExplorerPage clearSearchField() {
        waitTillVisibilityOfElement(clearSearchButton);
        clickOnElement(clearSearchButton);
        clickOnElementIfDisplayed($(id("Search")));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate all the Explorer Bar filters can be scrollable")
    public ExplorerPage validateAllFiltersCanBeScrollable() {
        explorerStationFilterList
                .asFixedIterable()
                .stream()
                .forEach(explorerBarTab -> {
                    swipeToElement(explorerBarTab, 2, LEFT, explorerFilterContainer, SHORT);
                    String explorerBarTabLabelValue = getElementName(explorerBarTab);
                    ExplorerBarTabsLabels explorerBarTabLabel = getExplorerBarTabType(explorerBarTabLabelValue);
                    SelenideElement explorerBarElement = getRequiredExplorerTab(explorerBarTabLabel);
                    getSoftAssertion().assertThat(isElementFullyVisible(explorerBarElement)).as("Explorer Bar tab" + explorerBarTabLabelValue + " is not scrollable").isTrue();
                });
        getSoftAssertion().assertAll();
        return this;
    }

    /* --- Helper Methods --- */

    @Override
    public By getExplorerBarTabBy(ExplorerPage.ExplorerBarTabsLabels explorerBarTabsLabel) {
        return By.xpath(String.format("//XCUIElementTypeButton[@label = '%s']", explorerBarTabsLabel.getexplorerBarTabName()));
    }

    @Step("Is explorer bar tab {explorerBarTabLabel} selected")
    @Override
    public boolean isExplorerBarTabSelected(ExplorerBarTabsLabels explorerBarTabLabel) {
        SelenideElement explorerTab = getRequiredExplorerTab(explorerBarTabLabel);
        String val = getElementValue(explorerTab);
        return val.equals("1");
    }

    @Step("Get explorer bar tab order")
    @Override
    public int getExplorerBarTabOrderNumber(ExplorerBarTabsLabels explorerBarTabLabel) {
        switch (explorerBarTabLabel) {
            case ALL_MUSIC -> {
                return 0;
            }
            case CLASSICAL -> {
                return 1;
            }
            case COUNTRY -> {
                return 2;
            }
            case TOP_40 -> {
                return 3;
            }
            case ROCK -> {
                return 4;
            }
            case HIP_HOP -> {
                return 5;
            }
            case NEWS -> {
                return 6;
            }
            case TALK -> {
                return 7;
            }
            case PUBLIC -> {
                return 8;
            }
            case SPORTS -> {
                return 9;
            }
            case EMERGENCY -> {
                return 10;
            }
            case RELIGION -> {
                return 11;
            }
            default -> throw new Error("Invalid explorer tab type - " + explorerBarTabLabel);
        }
    }

}
