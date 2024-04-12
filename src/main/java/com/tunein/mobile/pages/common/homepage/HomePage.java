package com.tunein.mobile.pages.common.homepage;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.getBrowsiesBarTabType;
import static com.tunein.mobile.utils.ElementHelper.getElementTextOrLabel;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.LONG;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.RIGHT;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class HomePage extends BasePage {

    protected SelenideElement homepageBrowsiesBar = $(android(id("tabLayout"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeScrollView'"))).as("Browsies bar");

    protected ElementsCollection homePageBrowsiesBarElements = $$(android(xpath("//*[contains(@resource-id, 'tabLayout')]/*//android.widget.TextView"))
            .ios(iOSNsPredicateString("name CONTAINS 'browsiesHomeButtonId'"))).as("Browsies bar elements");

    protected ElementsCollection homepageRecentsList = $$(android(xpath("//*[./*[contains(@text, 'RECENTS')]]/following-sibling::android.widget.ScrollView/*/android.view.ViewGroup"))
            .ios(xpath("//*[@name='RECENTS']/following-sibling::XCUIElementTypeCell[1]/*/XCUIElementTypeCell"))).as("Recents list");

    protected ElementsCollection homepageWhatDoYouWantToListenList = $$(android(xpath("//*[contains(@text, 'What Do')]/../following-sibling::android.widget.ScrollView/*/android.view.ViewGroup"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeCell' AND name  IN { 'Music', 'Sports', 'News & Talk', 'Podcasts'}"))).as("What Do You Want To Listen? list");

    protected ElementsCollection homepageGeneralListOfStations = $$(android(xpath("//*[contains(@resource-id, 'row_tile_title')]/.."))
            .ios(iOSNsPredicateString("name == 'tileCellViewIdentifier'"))).as("Genaral  list of stations");

    //TODO we need to update this locators later
    protected SelenideElement homepageContentTable = $(android(xpath("//*[(contains(@resource-id,'viewPager')) and ((.//android.widget.LinearLayout) | (.//android.view.ViewGroup) | (.//android.widget.RelativeLayout) )]"))
            .ios(xpath("//XCUIElementTypeTable[(./XCUIElementTypeCell) and (not(@label='Empty list'))]"))).as("Homepage content table");

    protected SelenideElement homepageForYouBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"For You\")"))
            .ios(iOSNsPredicateString("label == 'For You' AND type == 'XCUIElementTypeButton'"))).as("'For You' Browsies bar tab");

    protected SelenideElement homepageRadioBrowsiesBarTab = $(android(androidUIAutomator("textContains(\"Radio\").resourceId(\"tunein.player:id/browsies_tab_tag\")"))
            .ios(iOSNsPredicateString("label == 'Radio' AND type == 'XCUIElementTypeButton'"))).as("'Radio' Browsies bar tab");

    protected SelenideElement homepageSportsBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Sports\")"))
            .ios(iOSNsPredicateString("label == 'Sports' AND type == 'XCUIElementTypeButton'"))).as("'Sports' Browsies bar tab");

    protected SelenideElement homepageIHeartRadioBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"iHeartRadio\")"))
            .ios(iOSNsPredicateString("label == 'iHeartRadio' AND type == 'XCUIElementTypeButton'"))).as("'iHeartRadio' Browsies bar tab");

    protected SelenideElement homepageNewsTalkBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"News & Talk\")"))
            .ios(iOSNsPredicateString("label == 'News & Talk' AND type == 'XCUIElementTypeButton'"))).as("'News & Talk' Browsies bar tab");

    protected SelenideElement homepagePodcastsBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Podcasts\")"))
            .ios(iOSNsPredicateString("label == 'Podcasts' AND type == 'XCUIElementTypeButton'"))).as("'Podcasts' Browsies bar tab");

    protected SelenideElement homepageAudiobooksBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Audiobooks\")"))
            .ios(iOSNsPredicateString("label == 'Audiobooks' AND type == 'XCUIElementTypeButton'"))).as("'Audiobooks' Browsies bar tab");

    protected SelenideElement homepageMusicBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Music\")"))
            .ios(iOSNsPredicateString("label == 'Music' AND type == 'XCUIElementTypeButton'"))).as("'Music' Browsies bar tab");

    protected SelenideElement homepageTrendingBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Trending\")"))
            .ios(iOSNsPredicateString("label == 'Trending' AND type == 'XCUIElementTypeButton'"))).as("'Trending' Browsies bar tab");

    protected SelenideElement homepageByLocationBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"By Location\")"))
            .ios(iOSNsPredicateString("label == 'By Location' AND type == 'XCUIElementTypeButton'"))).as("'By Location' Browsies bar tab");

    protected SelenideElement homepageByLanguageBrowsiesBarTab = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"By Language\")"))
            .ios(iOSNsPredicateString("label == 'By Language' AND type == 'XCUIElementTypeButton'"))).as("'By Language' Browsies bar tab");

    public SelenideElement homepagePremiumPrompt = $(android(xpath("//android.view.ViewGroup[contains(@resource-id,'content_frame') and .//*[@text='Get the Premium Experience']]"))
            .ios(xpath("//XCUIElementTypeCell[.//XCUIElementTypeButton[@name='Start Free Trial']]"))).as("Premium prompt");

    protected SelenideElement homepageFreeTrialButton = $(android(id("tunein.player:id/primary_button"))
            .ios(iOSNsPredicateString("label == \"Start Free Trial\" AND type == \"XCUIElementTypeButton\""))).as("Free-trial button");

    protected SelenideElement homepageGoPremiumTitle = $(android(androidUIAutomator("text(\"Go Premium!\")"))
            .ios(accessibilityId("compactPromptTitleId"))).as("Go premium title");

    protected SelenideElement homepageGoPremiumSubTitle = $(android(id("tunein.player:id/subtitle"))
            .ios(accessibilityId("compactPromptSubtitleId"))).as("Go premium subtitle");

    public ElementsCollection contentCellContainer = $$(android(xpath("//*[@resource-id='tunein.player:id/gallery_recycler_view']"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeCell"))).as("Cell container");


    /* --- Loadable Component Method --- */

    @Step("Wait until Home Profile page is ready")
    @Override
    public HomePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(homepageContentTable, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract void swipeRequiredCategory(CategoryType categoryType, ScrollDirection direction, int numberOfSwipe);

    public abstract HomePage tapOnAllBrowsiesBarTabs();

    @Step("Tap on required browsies bar tab {browsiesBarTabsLabel}")
    public void tapOnRequiredBrowsiesBarTab(BrowsiesBarTabsLabels browsiesBarTabsLabel) {
        By browsiesBarTab = getBrowsiesBarTabBy(browsiesBarTabsLabel);
        if (isBrowsiesBarTabDisplayed(browsiesBarTabsLabel)) {
            clickOnElement(browsiesBarTab);
        } else {
            SelenideElement firstDisplayedBrowsiesBarTab = getFirstDisplayedBrowsiesBarTab();
            String firstDisplayedBrowsiesBarTabLabel = getElementTextOrLabel(firstDisplayedBrowsiesBarTab);
            int firstDisplayedBrowsiesBarTabIndex = getBrowsiesBarTabOrderNumber(getBrowsiesBarTabType(firstDisplayedBrowsiesBarTabLabel));
            int requiredBrowsiesBarTabIndex = getBrowsiesBarTabOrderNumber(browsiesBarTabsLabel);
            if (firstDisplayedBrowsiesBarTabIndex > requiredBrowsiesBarTabIndex) {
                clickOnElement(swipeToElement(browsiesBarTab, 12, RIGHT, homepageBrowsiesBar, SHORT));
            } else {
                clickOnElement(swipeToElement(browsiesBarTab, 12, LEFT, homepageBrowsiesBar, SHORT));
            }
        }
        switch (browsiesBarTabsLabel) {
            case FOR_YOU -> homePage.waitUntilPageReady();
            case RADIO -> radioPage.waitUntilPageReady();
            case SPORTS -> sportsPage.waitUntilPageReady();
            case I_HEART_RADIO -> iheartRadioPage.waitUntilPageReady();
            case NEWS_AND_TALK -> newsAndTalkPage.waitUntilPageReady();
            case PODCASTS -> podcastsPage.waitUntilPageReady();
            case MUSIC -> musicPage.waitUntilPageReady();
            case BY_LANGUAGE -> byLanguagePage.waitUntilPageReady();
            case BY_LOCATION -> byLocationPage.waitUntilPageReady();
            default -> waitTillVisibilityOfElement(homepageContentTable, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        }
    }

    @Step
    public UpsellPage scrollToAndTapFreeTrialButton(int... numberOfScrolls) {
        scrollTo(homepagePremiumPrompt, DOWN, LONG, numberOfScrolls);
        clickOnElement(homepageFreeTrialButton);
        return upsellPage.waitUntilPageReady();
    }

    public abstract void openContentItemWithNameInCategory(CategoryType categoryType, String contentName);

    public abstract ContentProfilePage tapOnLastBrickCellUnderCategory(CategoryType categoryType);

    public abstract void removeRecentsContentWithIndexUnderCategory(CategoryType categoryType, int index);

    public abstract void removeRecentsByContentNameUnderCategory(CategoryType categoryType, String contentName);

    /* --- Validation Methods --- */

    @Step("Validate home page is opened")
    public HomePage validateHomePageIsOpened() {
        assertThat(isOnHomePage()).as("Home Page is not opened").isTrue();
        return this;
    }

    public abstract HomePage validateThatAllBrowsiesBarTabsCanBeSelected();

    public abstract HomePage validateNumberOfElementsInHomePageCategory(CategoryType categoryType, ScrollDirection direction, int expectedNumber);

    @Step
    public void validateGoPremiumPromptState(boolean expected, ScrollDistance... scrollDistance) {
        boolean actual = false;
        for (int i = 0; i <= 10; i++) {
            if (isElementDisplayed(homepagePremiumPrompt)) {
                actual = true;
                break;
            } else scroll(DOWN, scrollDistance);
        }
        assertThat(actual).as("Go Premium prompt on homepage is not as expected").isEqualTo(expected);
    }

    public abstract void validateCategoryIsAbsent(CategoryType categoryType);

    @Step
    public HomePage validateThatNoRecentsAvailable() {
        assertThat(homepageRecentsList.size()).as("Recents list is available").isEqualTo(0);
        return this;
    }

    @Step("Validate that Recents section is not empty")
    public HomePage validateThatRecentsNotEmpty() {
        assertThat(homepageRecentsList.size()).as("Recents list is empty").isGreaterThan(0);
        return this;
    }

    @Step
    public HomePage validateThatBrowsiesBarTabIsSelected(BrowsiesBarTabsLabels browsiesBarTabLabel) {
        assertThat(isBrowsiesBarTabSelected(browsiesBarTabLabel)).as("Tab " + browsiesBarTabLabel + " is not selected").isTrue();
        return this;
    }

    @Step("Validate content {contentName} appeared in recents")
    public HomePage validateThatContentAppearedInRecents(String contentName) {
        assertThat(getRecentsContentName(contentName)).as(contentName + " is not displayed").isEqualTo(contentName);
        return this;
    }

    public abstract void validateCategoryHeaderIsPresent(CategoryType categoryType);

    public abstract void validateThatContentNotDisplayedUnderRecents(String contentName);

    public abstract HomePage validateGreenIconStateForEpisodeCell(CategoryType categoryType, boolean expectedGreenBadgeStatus, int index);

    /* --- Helper Methods --- */

    public boolean isOnHomePage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(homepageBrowsiesBar, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    public boolean isBrowsiesBarTabDisplayed(BrowsiesBarTabsLabels browsiesBarTabLabel) {
       SelenideElement browsiesTab = getRequiredBrowsiesTab(browsiesBarTabLabel);
        return isElementDisplayed(browsiesTab);
    }

    public abstract boolean isBrowsiesBarTabSelected(BrowsiesBarTabsLabels browsiesBarTabLabel);

    public abstract int getBrowsiesBarTabOrderNumber(BrowsiesBarTabsLabels browsiesBarTabLabel);

    public abstract String getRecentsContentName(String contentName);

    public abstract SelenideElement getSelectedBrowsiesBarTab();

    protected SelenideElement getFirstDisplayedBrowsiesBarTab() {
        return homePageBrowsiesBarElements
                .asDynamicIterable()
                .stream()
                .filter(browsiesBarTab -> isElementDisplayed(browsiesBarTab))
                .findFirst()
                .get();
    }

    public abstract By getBrowsiesBarTabBy(BrowsiesBarTabsLabels browsiesBarTabsLabel);

    protected SelenideElement getRequiredBrowsiesTab(BrowsiesBarTabsLabels tabName) {
        switch (tabName) {
            case FOR_YOU -> {
                return homepageForYouBrowsiesBarTab;
            }
            case RADIO -> {
                return homepageRadioBrowsiesBarTab;
            }
            case SPORTS -> {
                return homepageSportsBrowsiesBarTab;
            }
            case I_HEART_RADIO -> {
                return homepageIHeartRadioBrowsiesBarTab;
            }
            case NEWS_AND_TALK -> {
                return homepageNewsTalkBrowsiesBarTab;
            }
            case PODCASTS -> {
                return homepagePodcastsBrowsiesBarTab;
            }
            case AUDIOBOOKS -> {
                return homepageAudiobooksBrowsiesBarTab;
            }
            case MUSIC -> {
                return homepageMusicBrowsiesBarTab;
            }
            case BY_LANGUAGE -> {
                return homepageByLanguageBrowsiesBarTab;
            }
            case BY_LOCATION -> {
                return homepageByLocationBrowsiesBarTab;
            }
            default -> throw new Error("Invalid browsies tab type - " + tabName.toString());
        }
    }

    public int getHomePageRecentsNumber() {
        closePermissionPopupsIfDisplayed();
        return homepageRecentsList.asDynamicIterable().stream().toList().size();
    }

    public abstract int getIndexOfFirstContentGreenBadgeStatusInCategory(CategoryType categoryType, boolean greenBadgeStatus, boolean swipeCard, boolean... skipError);

    public enum BrowsiesBarTabsLabels {
        FOR_YOU("For You"),
        RADIO("Radio"),
        SPORTS("Sports"),
        AUDIOBOOKS("Audiobooks"),
        I_HEART_RADIO("iHeartRadio"),
        NEWS_AND_TALK("News & Talk"),
        PODCASTS("Podcasts"),
        MUSIC("Music"),
        BY_LANGUAGE("By Language"),
        BY_LOCATION("By Location");

        private String browsiesBarTabName;

        private BrowsiesBarTabsLabels(String browsiesBarTabName) {
            this.browsiesBarTabName = browsiesBarTabName;
        }

        public String getbrowsiesBarTabName() {
            return browsiesBarTabName;
        }

        public static BrowsiesBarTabsLabels getBrowsiesBarTabType(final String operationName) {
            List<BrowsiesBarTabsLabels> browsiesBarTabsLabelsTypeList = Arrays.asList(BrowsiesBarTabsLabels.values());
            return browsiesBarTabsLabelsTypeList.stream().filter(eachContent -> eachContent.toString().equalsIgnoreCase(operationName))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return browsiesBarTabName;
        }

    }
}
