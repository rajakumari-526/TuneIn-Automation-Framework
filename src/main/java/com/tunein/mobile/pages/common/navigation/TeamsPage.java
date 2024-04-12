package com.tunein.mobile.pages.common.navigation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.ElementHelper.isElementEnabled;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class TeamsPage extends BasePage {

    protected SelenideElement teamPageTitle = $(android(id("tunein.player:id/headerTitle"))
            .ios(iOSNsPredicateString("label CONTAINS \"Pick your teams\""))).as("Team page title");

    public ElementsCollection teamList = $$(android(xpath("//android.view.ViewGroup[./android.view.ViewGroup[contains(@resource-id,'row_tile_image_wrapper')]]"))
            .ios(iOSNsPredicateString("name == 'tileCellViewIdentifier'"))).as("Teams list");

    protected SelenideElement followTeamsButton = $(android(androidUIAutomator("text(\"FOLLOW TEAMS\")"))
            .ios(iOSNsPredicateString("label == 'Follow Teams' AND type =='XCUIElementTypeButton'"))).as("Follow teams button");

    protected SelenideElement cancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("label == 'CANCEL' AND type =='XCUIElementTypeButton'"))).as("Cancel button");

    protected SelenideElement unfollowTeamsButton = $(android(androidUIAutomator("text(\"UNFOLLOW TEAMS\")"))
            .ios(id("UNFOLLOW TEAMS"))).as("Unfollow teams button");

    protected SelenideElement teamsListContainer = $(android(id("gallery_recycler_view"))
            .ios(xpath("//XCUIElementTypeCollectionView[.//XCUIElementTypeOther[@name='tileCellViewIdentifier' and @visible = 'true']]"))).as("Teams list container");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public TeamsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero(teamList, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public TeamsPage validateFollowTeamsButtonIsEnabled() {
        assertThat(isElementEnabled(followTeamsButton)).as("follow teams button is not enabled").isTrue();
        return this;
    }

    @Step
    public TeamsPage validateUnfollowTeamsButtonIsDisplayed() {
        assertThat(isElementDisplayed(unfollowTeamsButton)).as("unfollow teams button is not visible").isTrue();
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public TeamsPage selectNumberOfTeams(int noOfTeams) {
        if (noOfTeams == 0) {
            throw new UnsupportedOperationException("Invalid number value, should be greater than 1");
        }
        for (int i = 0; i < noOfTeams; i++) {
            clickOnElement(teamList.get(i), Duration.ofSeconds(config().waitShortTimeoutSeconds()));
            closePermissionPopupsIfDisplayed();
        }
        return this;
    }

    @Step
    public UpsellPage tapOnFollowTeamsButton() {
        clickOnElement(followTeamsButton);
        return upsellPage.waitUntilPageReady();
    }

    @Step
    public UpsellPage tapOnUnFollowTeamsButton() {
        clickOnElement(unfollowTeamsButton);
        return upsellPage.waitUntilPageReady();
    }

    @Step
    public void tapOnCancelButton() {
        clickOnElement(cancelButton);
    }

    public HashMap<String, SelenideElement> teamsPageElements() {
        return new HashMap<>() {{
            put("Pick your teams. Never miss a game.", teamPageTitle);
            put(SKIP_TEXT_VALIDATION_PREFIX, teamsListContainer);
            put("Follow Teams", followTeamsButton);
            put("Dismiss", cancelButton);
        }};
    }

    public abstract TeamsPage unselectItemsFromTeams(int noOfTeams);
}
