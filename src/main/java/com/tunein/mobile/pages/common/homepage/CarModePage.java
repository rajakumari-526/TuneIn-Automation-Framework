package com.tunein.mobile.pages.common.homepage;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class CarModePage extends BasePage {

    // TODO: Listed as not visible on iOS on Appium Inspector, despite the user being able to see it
    protected SelenideElement carModePlayStopButton = $(android(id("carModePlayIcon"))
            .ios(iOSNsPredicateString("name CONTAINS 'png' AND type == 'XCUIElementTypeImage'"))).as("Play/Stop button");

    protected SelenideElement carModePlayButton = $(android(id("carModePlayIcon"))
            .ios(iOSNsPredicateString("name == 'carmode-play.png' AND type == 'XCUIElementTypeImage'"))).as("Play button");

    protected SelenideElement carModePauseButton = $(android(id("absent"))
            .ios(iOSNsPredicateString("name == 'carmode-pause.png' AND type == 'XCUIElementTypeImage'"))).as("Pause button");
    
    protected SelenideElement carModeFavoritesButton = $(android(id("carModePresetText"))
            .ios(iOSNsPredicateString("label == 'FAVORITES' AND type == 'XCUIElementTypeButton'"))).as("Favorites button");
    
    protected SelenideElement carModeRecentsButton = $(android(id("carModeRecentsText"))
            .ios(iOSNsPredicateString("label == 'RECENTS' AND type == 'XCUIElementTypeButton'"))).as("Recents button");
    
    protected SelenideElement carModeRecommendedButton = $(android(id("carModeRecommendedText"))
            .ios(iOSNsPredicateString("label == 'RECOMMENDED' AND type == 'XCUIElementTypeButton'"))).as("Recommended button");
    
    protected SelenideElement carModeExitButton = $(android(id("carModeExitText"))
            .ios(iOSNsPredicateString("label == 'EXIT CAR MODE' AND type == 'XCUIElementTypeButton'"))).as("Exit button");

    public SelenideElement carModeSmallTitle = $(id("mini_player_status")).as("Small title");

    @Step
    public CarModePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(carModeExitButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public void clickOnExitCardModeMode() {
        clickOnElement(carModeExitButton);
    }

    @Step
    public ContentsListPage clickOnCarModeFavoritesButton() {
        clickOnElement(carModeFavoritesButton);
        return contentsListPage.waitUntilPageReady();
    }

    @Step
    public ContentsListPage clickOnCarModeRecentsButton() {
        clickOnElement(carModeRecentsButton);
        return contentsListPage.waitUntilPageReady();
    }

    @Step
    public ContentsListPage clickOnCarModeRecommendedButton() {
        clickOnElement(carModeRecommendedButton);
        return contentsListPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public CarModePage validateThatCarModePageIsDisplayed() {
        assertThat(isElementDisplayed(carModeExitButton)).as("CarMode page is not displayed").isTrue();
        return this;
    }

    @Step
    public abstract CarModePage validateCarModeTitleIsEqualTo(String expectedTitle);

    @Step("Validate Play button is displayed")
    public CarModePage validatePlayButtonDisplayed() {
        assertThat(isElementDisplayed(carModePlayButton)).as("Play button is not displayed").isTrue();
        return this;
    }

    @Step("Validate Play button is not displayed")
    public CarModePage validatePlayButtonNotDisplayed() {
        assertThat(isElementNotDisplayed(carModePlayButton)).as("Play button is displayed").isTrue();
        return this;
    }

    @Step("Validate stop button is displayed")
    public CarModePage validateStopButtonDisplayed() {
        assertThat(isElementDisplayed(carModePlayStopButton)).as("Stop button is not displayed").isTrue();
        return this;
    }

    @Step("Validate pause button is displayed")
    public CarModePage validatePauseButtonDisplayed() {
        assertThat(isElementDisplayed(carModePauseButton)).as("Pause button is not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnCarModePage() {
        return isElementDisplayed(carModeExitButton, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
    }

    public abstract HashMap<String, SelenideElement> carModeElements();
}
