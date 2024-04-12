package com.tunein.mobile.pages.common.browsies;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.navigation.TeamsPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class SportsPage extends BasePage {

    protected SelenideElement sportsSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Sports\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Sports') and contains(@name,'_selected')]"))).as("Sports selected tab");

    protected ElementsCollection sportEvents = $$(android(xpath("//*[@resource-id='tunein.player:id/view_model_list']//android.view.ViewGroup"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("Sports page content");

    protected SelenideElement pickTeamsButton = $(android(androidUIAutomator("text(\"PICK YOUR TEAMS\")"))
            .ios(iOSNsPredicateString("label == 'Pick Your Teams' AND type == 'XCUIElementTypeButton'"))).as("Pick team button");

    protected SelenideElement pickYourTeamsPrompt = $(android(xpath("//*[@resource-id=\"tunein.player:id/content_frame\" and @class=\"android.view.ViewGroup\"]"))
            .ios(iOSNsPredicateString("name == 'compactPromptTitleId' AND label CONTAINS 'Listen Live'"))).as("Pick your team prompt");

    protected SelenideElement pickYourTeamsPromptTitle = $(android(id("title"))
            .ios(id("compactPromptTitleId"))).as("Pick your teams prompt title");

    protected SelenideElement pikYourTeamsPromptDescription = $(android(id("subtitle"))
            .ios(id("compactPromptSubtitleId"))).as("Pick your teams prompt description");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public SportsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(sportsSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(sportEvents, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate Sports Page is Opened")
    public SportsPage validateSportsPageIsOpen() {
        assertThat(isOnSportsPage()).as("Sports Page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnSportsPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(sportsSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    @Step
    public TeamsPage tapOnPickYourTeams() {
        clickOnElement(scrollTo(pickTeamsButton, DOWN));
        return teamsPage.waitUntilPageReady();
    }

    @Step
    public SportsPage validatePickYourTeamPromptElementsArePresent() {
        closePermissionPopupsIfDisplayed();
        scrollTo(pickTeamsButton, DOWN, 5);
        assertThat(isElementDisplayed(pickYourTeamsPrompt)).as("Pick Your Teams Prompt is not displayed under Sports browsies").isTrue();
        assertThat(isElementDisplayed(pickTeamsButton)).as("'Pick Teams' button is not displayed in Prompt").isTrue();
        assertThat(isElementDisplayed(pickYourTeamsPromptTitle)).as("Prompt title is not displayed").isTrue();
        assertThat(isElementDisplayed(pikYourTeamsPromptDescription)).as("Prompt Description is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

}
