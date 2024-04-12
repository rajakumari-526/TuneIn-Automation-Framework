package com.tunein.mobile.pages.common.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.utils.GestureActionUtil;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class DownloadsPage extends BasePage {

    /* --- UI Elements --- */

    protected SelenideElement downloadsPrompt = $(android(id("emptyState"))
            .ios(xpath("//XCUIElementTypeOther/XCUIElementTypeStaticText[contains(@name,'Download for offline listening.')]"))).as("Downloads prompt");

    protected SelenideElement downloadsDescription = $(android(id("subtitle"))
            .ios(iOSNsPredicateString("name == \"Click on the download icon next to your favorite podcasts and podcast episodes to save them for offline listening.\" AND type == \"XCUIElementTypeStaticText\""))).as("Description");

    protected SelenideElement downloadsFindSomething = $(android(id("button"))
            .ios(iOSNsPredicateString("label == \"FIND SOMETHING\" AND type == \"XCUIElementTypeButton\""))).as("Find something button");

    protected SelenideElement downloadForOfflineListeningText = $(android(id("emptyState"))
            .ios(iOSNsPredicateString("label == \"Download for offline listening.\""))).as("Offline listening text");

    /* --- Loadable Component Method --- */

    @Step
    public abstract DownloadsPage waitUntilPageReady();

    /* --- Validation Methods --- */

    @Step("Validate downloads prompt")
    public DownloadsPage validateDownloadsPromptIsDisplayed() {
        getSoftAssertion().assertThat(isElementDisplayed(downloadsPrompt)).as("Episode card date is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(downloadsDescription)).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(downloadsFindSomething)).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate downloads page")
    public DownloadsPage validateDownloadsPageWithNoDownloads() {
        assertThat(isElementDisplayed(downloadForOfflineListeningText)).as("'Download for offline listening' text is not displayed in Downloads page").isTrue();
        return this;
    }

    @Step("Validate that content {contentName} appeared in Downloads")
    public DownloadsPage validateThatContentAppearedInDownloads(String contentName) {
        GestureActionUtil.scrollToRefresh();
        String errorMessage = "\"" + contentName + "\" content was not added to \"Downloads\"";
        assertThat(isElementDisplayed(getDownloadsContentWithName(contentName))).as(errorMessage).isTrue();
        return this;
    }

    @Step("Validate that content {stationName} appeared in Downloads")
    public DownloadsPage validateStationAppearedInDownloads(String stationName) {
        String errorMessage = "\"" + stationName + "\" content was not added to \"Downloads\"";
        assertThat(isElementDisplayed(getDownloadsStationName(stationName))).as(errorMessage).isTrue();
        return this;
    }

    /* --- Action Methods --- */

    @Step("Tap on Find Something")
    public DownloadsPage tapOnFindSomething() {
        clickOnElement(downloadsFindSomething);
        return this;
    }

    public abstract NowPlayingPage tapOnContentName(String name);

    public abstract SelenideElement getDownloadsContentWithName(String contentName);

    public abstract SelenideElement getDownloadsStationName(String stationName);

    /* --- Helper Methods --- */

}
