package com.tunein.mobile.pages.android.explorer;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.explorer.ExplorerPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.common.explorer.ExplorerPage.ExplorerBarTabsLabels.*;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class AndroidExplorerPage extends ExplorerPage {

    /* --- Loadable Component Method --- */

    /* --- Action Methods --- */

    @Step("Enter Search station {station}")
    @Override
    public ExplorerPage typeSearch(String station) {
        clickOnElement(explorerSearchButton);
        explorerEditSearch.setValue(station);
        clickOnElement(explorerCloseSearch);
        return this;
    }

    @Step("Clear search text field")
    @Override
    public ExplorerPage clearSearchField() {
        waitTillVisibilityOfElement(clearSearchButton);
        clickOnElement(clearSearchButton);
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate all the Explorer Bar filters can be scrollable")
    public ExplorerPage validateAllFiltersCanBeScrollable() {
        List<ExplorerBarTabsLabels> explorerBarTabsLabels = Arrays.asList(
                ALL_MUSIC, COUNTRY, TOP_40, ROCK, HIP_HOP, NEWS, TALK, PUBLIC, SPORTS, EMERGENCY, RELIGION, CLASSICAL);
        explorerBarTabsLabels
                .stream()
                .forEach(explorerBarTab -> {
                    SelenideElement explorerBarElement = getRequiredExplorerTab(explorerBarTab);
                    scrollToRequiredExplorerBarTab(explorerBarTab);
                    getSoftAssertion().assertThat(isElementDisplayed(explorerBarElement))
                            .as("Explorer Bar tab " + explorerBarTab.getexplorerBarTabName() + " is not displayed")
                            .isTrue();
                });
        getSoftAssertion().assertAll();
        return this;
    }

        /* --- Helper Methods --- */

    @Override
    public By getExplorerBarTabBy(ExplorerBarTabsLabels explorerBarTabsLabel) {
        return AppiumBy.androidUIAutomator(String.format("className(android.widget.Button).textContains(\"%s\")", explorerBarTabsLabel.getexplorerBarTabName()));
    }

    @Step("Is explorer bar tab {explorerBarTabLabel} selected")
    @Override
    public boolean isExplorerBarTabSelected(ExplorerBarTabsLabels explorerBarTabLabel) {
        SelenideElement explorerTab = getRequiredExplorerTab(explorerBarTabLabel);
        return this.isOnExplorerPage();
        //Todo add verification to selected tabs DROID-16467
    }

    @Step("Get explorer bar tab order")
    @Override
    public int getExplorerBarTabOrderNumber(ExplorerBarTabsLabels explorerBarTabLabel) {
        switch (explorerBarTabLabel) {
            case ALL_MUSIC -> {
                return 0;
            }
            case TOP_40 -> {
                return 1;
            }
            case HIP_HOP -> {
                return 2;
            }
            case ROCK -> {
                return 3;
            }
            case COUNTRY -> {
                return 4;
            }
            case CLASSICAL -> {
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
            case RELIGION -> {
                return 9;
            }
            case SPORTS -> {
                return 10;
            }
            case EMERGENCY -> {
                return 11;
            }
            default -> throw new Error("Invalid explorer tab type - " + explorerBarTabLabel);
        }
    }

}
