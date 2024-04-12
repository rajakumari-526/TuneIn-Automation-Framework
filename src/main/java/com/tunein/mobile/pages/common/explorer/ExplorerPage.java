package com.tunein.mobile.pages.common.explorer;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.explorer.ExplorerPage.ExplorerBarTabsLabels.getExplorerBarTabType;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.GestureActionUtil.swipeToElement;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

/**
 * Also known as MapView and Radio
 */
public abstract class ExplorerPage extends BasePage {

    protected SelenideElement explorerCloseButton = $(android(id("close"))
            .ios(id("chevron down"))).as("Explorer close button");

    protected SelenideElement explorerLogo = $(android(id("logo"))
            .ios(iOSNsPredicateString("name == \"explorer-logo\""))).as("Explorer logo");

    protected ElementsCollection explorerStationList = $$(android(id("annotationFrame"))
            .ios(xpath("//XCUIElementTypeOther/*/XCUIElementTypeOther[./XCUIElementTypeImage[following-sibling::XCUIElementTypeOther]]"))).as("Station list");

    protected SelenideElement explorerSearchButton = $(android(id("actionChip"))
            .ios(iOSNsPredicateString("type == \"XCUIElementTypeSearchField\""))).as("Search button");

    protected SelenideElement explorerEditSearch = $(android(id("searchQuery"))).as("Edit Search");

    protected SelenideElement explorerCloseSearch = $(android(id("closeSearch"))).as("Close Search");

    public ElementsCollection explorerStationFilterList = $$(android(id("filterChip"))
            .ios(xpath("//XCUIElementTypeOther[.//XCUIElementTypeSearchField]/following-sibling::*/XCUIElementTypeButton"))).as("Station filter list");

    public SelenideElement explorerAllMusicButton = $(android(androidUIAutomator("text(\"ALL MUSIC\")"))
            .ios(iOSNsPredicateString("name == \"ALL MUSIC\""))).as("Explorer All Music button");

    public SelenideElement explorerTop40Button = $(android(androidUIAutomator("text(\"TOP 40\")"))
            .ios(iOSNsPredicateString("name == \"TOP 40\""))).as("Explorer TOP 40 button");

    public SelenideElement explorerHipHopButton = $(android(androidUIAutomator("text(\"HIP-HOP\")"))
            .ios(iOSNsPredicateString("name == \"HIP-HOP\""))).as("Explorer HIP-HOP button");

    public SelenideElement explorerRockButton = $(android(androidUIAutomator("text(\"ROCK\")"))
            .ios(iOSNsPredicateString("name == \"ROCK\""))).as("Explorer ROCK button");

    public SelenideElement explorerCountyButton = $(android(androidUIAutomator("text(\"COUNTRY\")"))
            .ios(iOSNsPredicateString("name == \"COUNTRY\""))).as("Explorer COUNTRY button");

    public SelenideElement explorerClassicalButton = $(android(androidUIAutomator("text(\"CLASSICAL\")"))
            .ios(iOSNsPredicateString("name == \"CLASSICAL\""))).as("Explorer CLASSICAL button");

    public SelenideElement explorerNewsButton = $(android(androidUIAutomator("text(\"NEWS\")"))
            .ios(iOSNsPredicateString("name == \"NEWS\""))).as("Explorer NEWS button");

    public SelenideElement explorerTalkButton = $(android(androidUIAutomator("text(\"TALK\")"))
            .ios(iOSNsPredicateString("name == \"TALK\""))).as("Explorer TALK button");

    public SelenideElement explorerPublicButton = $(android(androidUIAutomator("text(\"PUBLIC\")"))
            .ios(iOSNsPredicateString("name == \"PUBLIC\""))).as("Explorer PUBLIC button");

    public SelenideElement explorerReligionButton = $(android(androidUIAutomator("text(\"RELIGION\")"))
            .ios(iOSNsPredicateString("name == \"RELIGION\""))).as("Explorer RELIGION button");

    public SelenideElement explorerSportsButton = $(android(androidUIAutomator("text(\"SPORTS\")"))
            .ios(iOSNsPredicateString("name == \"SPORTS\""))).as("Explorer SPORTS button");

    public SelenideElement explorerEmergencyButton = $(android(androidUIAutomator("text(\"EMERGENCY\")"))
            .ios(iOSNsPredicateString("name == \"EMERGENCY\""))).as("Explorer EMERGENCY button");

    public SelenideElement explorerFilterContainer = $(android(id("filters"))
            .ios(iOSNsPredicateString("type == \"XCUIElementTypeScrollView\""))).as("Filters container");

    protected SelenideElement explorerNoMatchingStationsFoundLabel = $(android(id("noResultsText"))
            .ios(iOSNsPredicateString("name == \"No matching stations found\""))).as("No matching stations found");

    protected SelenideElement clearSearchButton = $(android(xpath("//android.view.View[contains(@content-desc,'Remove')]"))
            .ios(accessibilityId("Clear text"))).as("Clear Search text field");

    /* --- Loadable Component Method --- */

    @Override
    public ExplorerPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero(explorerStationList, Duration.ofSeconds(config().oneMinuteInSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click Explorer close button")
    public void clickExplorerCloseButton() {
        clickOnElement(explorerCloseButton);
    }

    @Step("Click Explorer station by index {index}")
    public ExplorerMiniPlayerPage clickStationByIndex(int index) {
        clickOnElement(explorerStationList.asDynamicIterable().stream().collect(Collectors.toList()).get(index));
        return explorerMiniPlayerPage.waitUntilPageReady();
    }

    @Step("Click Explorer station filter by index {index}")
    public ExplorerPage clickStationFilterByIndex(int index) {
        clickOnElement(explorerStationFilterList.get(index));
        customWait(Duration.ofSeconds(5));
        return this;
    }

    @Step("Scroll to Explorer bar tab {explorerBarTabsLabel}")
    public ExplorerPage scrollToRequiredExplorerBarTab(ExplorerBarTabsLabels explorerBarTabsLabel) {
        By explorerBarTab = getExplorerBarTabBy(explorerBarTabsLabel);
        if (!isExplorerBarTabDisplayed(explorerBarTabsLabel)) {
            SelenideElement firstDisplayedExplorerBarTab = getFirstDisplayedExplorerBarTab();
            String firstDisplayedExplorerBarTabLabel = getElementTextOrLabel(firstDisplayedExplorerBarTab);
            int firstDisplayedExplorerBarTabIndex = getExplorerBarTabOrderNumber(getExplorerBarTabType(firstDisplayedExplorerBarTabLabel));
            int requiredExplorerBarTabIndex = getExplorerBarTabOrderNumber(explorerBarTabsLabel);
            if (firstDisplayedExplorerBarTabIndex > requiredExplorerBarTabIndex) {
                swipeToElement(explorerBarTab, 12, RIGHT, explorerFilterContainer, SHORT);
            } else {
                swipeToElement(explorerBarTab, 12, LEFT, explorerFilterContainer, SHORT);
            }
        }
        return this;
    }

    public abstract ExplorerPage clearSearchField();

    public abstract ExplorerPage typeSearch(String station);

    @Step("Get count of stations in Explorer page")
    public int getCountOfStations() {
        waitVisibilityOfElements(explorerStationList);
        return explorerStationList.asDynamicIterable().stream().toList().size();
    }

    /* --- Validation Methods --- */

    @Step("Validate is on Explorer/MapView/Radio page")
    public ExplorerPage validateThatOnExplorerPage() {
        assertThat(isOnExplorerPage()).as("Explorer page is not open").isTrue();
        return this;
    }

    @Step("Validate station list is not empty")
    public ExplorerPage validateStationListIsNotEmpty() {
        assertThat(explorerStationList).as("Contents list is empty").isNotEmpty();
        return this;
    }

    @Step("Validate station filter list is not empty")
    public ExplorerPage validateStationFilterListIsNotEmpty() {
        assertThat(explorerStationFilterList).as("Contents list is empty").isNotEmpty();
        return this;
    }

    @Step("Validate stations count is greater than {numberOfStations} in explorer page")
    public ExplorerPage validateStationsCountIsGreaterThan(int numberOfStations) {
        assertThat(getCountOfStations()).as("Current number of stations is not greater than  " + numberOfStations).isGreaterThan(numberOfStations);
        return this;
    }

    @Step("Validate all the Explorer Bar Tabs can be selected")
    public ExplorerPage validateThatAllExplorerBarTabsCanBeSelected() {
        explorerStationFilterList
                .asFixedIterable()
                .stream()
                .forEach(explorerBarTab -> {
                    swipeToElement(explorerBarTab, 2, LEFT, explorerFilterContainer, SHORT);
                    String explorerBarTabLabelValue = getElementName(explorerBarTab);
                    ExplorerBarTabsLabels explorerBarTabLabel = getExplorerBarTabType(explorerBarTabLabelValue);
                    clickOnElement(getExplorerBarTabBy(explorerBarTabLabel));
                    getSoftAssertion().assertThat(isExplorerBarTabSelected(explorerBarTabLabel)).as("Explorer Bar tab " + explorerBarTabLabelValue + " is not selected").isTrue();
                });
        getSoftAssertion().assertAll();
        return this;
    }

    public abstract ExplorerPage validateAllFiltersCanBeScrollable();

    @Step("Validate no matching stations found alert displayed")
    public ExplorerPage validateNoMatchingStationsFoundAlertDisplayed() {
        assertThat(isElementDisplayed(explorerNoMatchingStationsFoundLabel)).as("No matching stations found not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnExplorerPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(explorerLogo, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    public abstract boolean isExplorerBarTabSelected(ExplorerBarTabsLabels explorerBarTabLabel);

    public boolean isExplorerBarTabDisplayed(ExplorerBarTabsLabels explorerBarTabLabel) {
        SelenideElement explorerTab = getRequiredExplorerTab(explorerBarTabLabel);
        return isElementDisplayed(explorerTab);
    }

    protected SelenideElement getFirstDisplayedExplorerBarTab() {
        return explorerStationFilterList
                .asDynamicIterable()
                .stream()
                .filter(explorerBarTab -> isElementDisplayed(explorerBarTab))
                .findFirst()
                .get();
    }

    public SelenideElement getRequiredExplorerTab(ExplorerBarTabsLabels tabName) {
        switch (tabName) {
            case ALL_MUSIC -> {
                return explorerAllMusicButton;
            }
            case COUNTRY -> {
                return explorerCountyButton;
            }
            case CLASSICAL -> {
                return explorerClassicalButton;
            }
            case TOP_40 -> {
                return explorerTop40Button;
            }
            case ROCK -> {
                return explorerRockButton;
            }
            case HIP_HOP -> {
                return explorerHipHopButton;
            }
            case NEWS -> {
                return explorerNewsButton;
            }
            case TALK -> {
                return explorerTalkButton;
            }
            case PUBLIC -> {
                return explorerPublicButton;
            }
            case SPORTS -> {
                return explorerSportsButton;
            }
            case RELIGION -> {
                return explorerReligionButton;
            }
            case EMERGENCY -> {
                return explorerEmergencyButton;
            }
            default -> throw new Error("Invalid explorer tab type - " + tabName.toString());
        }
    }

    @Step("Explore page elements")
    public HashMap<String, SelenideElement> explorePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, explorerLogo);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, explorerFilterContainer);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, explorerCloseButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, explorerSearchButton);
        return elementsMap;
    }

    public abstract By getExplorerBarTabBy(ExplorerBarTabsLabels explorerBarTabsLabel);

    public enum ExplorerBarTabsLabels {
        ALL_MUSIC("ALL MUSIC"),
        CLASSICAL("CLASSICAL"),
        COUNTRY("COUNTRY"),
        TOP_40("TOP 40"),
        ROCK("ROCK"),
        HIP_HOP("HIP-HOP"),
        NEWS("NEWS"),
        TALK("TALK"),
        PUBLIC("PUBLIC"),
        SPORTS("SPORTS"),
        EMERGENCY("EMERGENCY"),
        RELIGION("RELIGION");
        private String explorerBarTabName;

        private ExplorerBarTabsLabels(String explorerBarTabName) {
            this.explorerBarTabName = explorerBarTabName;
        }

        public String getexplorerBarTabName() {
            return explorerBarTabName;
        }

        public static ExplorerBarTabsLabels getExplorerBarTabType(final String operationName) {
            List<ExplorerPage.ExplorerBarTabsLabels> explorerBarTabsLabelsTypeList = Arrays.asList(ExplorerBarTabsLabels.values());
            return explorerBarTabsLabelsTypeList.stream().filter(eachContent -> eachContent.toString().equalsIgnoreCase(operationName))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return explorerBarTabName;
        }
    }

    public abstract int getExplorerBarTabOrderNumber(ExplorerBarTabsLabels explorerBarTabLabel);
}
