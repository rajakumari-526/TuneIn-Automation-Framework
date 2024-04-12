package com.tunein.mobile.pages.common.library;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.RECENTS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.LIBRARY;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class LibraryPage extends BasePage {

    protected SelenideElement libraryAddCustomUrlButton = $(android(androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"CUSTOM URL\").instance(0))"))
            .ios(iOSNsPredicateString("label == 'Custom URL' AND type == 'XCUIElementTypeButton'"))).as("Add custom url button");

    protected SelenideElement libraryFavoritesCountLabel = $(android(xpath("//android.widget.TextView[./preceding-sibling::android.widget.TextView[@text='Favorites']]"))
            .ios(xpath("//XCUIElementTypeOther[./preceding-sibling::XCUIElementTypeStaticText[@name='Favorites']]/XCUIElementTypeStaticText"))).as("Favorites count label");

    protected SelenideElement libraryDownloadsLabel = $(android(androidUIAutomator("text(\"Downloads\")"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Downloads'"))).as("Downloads label");

    protected SelenideElement libraryFavoritesLabel = $(android(xpath("//*[@resource-id='tunein.player:id/row_square_cell_title' and @text='Favorites']"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Favorites'"))).as("Favorites label");

    protected SelenideElement libraryRecentsPrompt = $(android(xpath("(//android.widget.FrameLayout/following-sibling::android.view.ViewGroup)[1]"))
            .ios(xpath("//XCUIElementTypeOther[contains(@name,'RECENTS')]/following-sibling::XCUIElementTypeCell[1]"))).as("Recents prompt");

    protected SelenideElement libraryRecentsDescription = $(android(id("subtitle"))
            .ios(id("compactPromptSubtitleId"))).as("Recents Description");

    protected SelenideElement libraryRecentsStartListening = $(android(id("primary_button"))
            .ios(iOSNsPredicateString("name == \"Start Listening\" AND type == \"XCUIElementTypeStaticText\""))).as("Start listening button");

    protected SelenideElement recentsLabel = $(android(androidUIAutomator("text(\"RECENTS\")"))
            .ios(iOSNsPredicateString("name == \"RECENTS\" AND type == \"XCUIElementTypeOther\""))).as("Recents label");

    protected SelenideElement downloadsCountLabel = $(android(xpath("//android.widget.TextView[./preceding-sibling::android.widget.TextView[@text='Downloads']]"))
            .ios(xpath("//XCUIElementTypeOther[./preceding-sibling::XCUIElementTypeStaticText[@name='Downloads']]/XCUIElementTypeStaticText"))).as("Downloads count label");

    protected SelenideElement libraryDownloadLabel = $(android(xpath("//*[@resource-id='tunein.player:id/row_square_cell_title' and @text='Downloads']"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label == 'Downloads'"))).as("Download label");

    private String libraryRecentsLocator = "//android.widget.ImageView[@content-desc='%s']";


    /* --- Loadable Component Method --- */

    @Step
    @Override
    public LibraryPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(libraryDownloadsLabel);
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public CustomUrlAddPage tapOnAddCustomUrlButton() {
        clickOnElement(libraryAddCustomUrlButton, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        return customUrlAddPage.waitUntilPageReady();
    }

    @Step("Flow: Open custom url {customUrl}")
    public NowPlayingPage openCustomUrlFlow(String customUrl) {
        if (customUrl.isEmpty()) {
            throw new IllegalStateException("customUrl cannot be empty");
        }
        navigationAction.navigateTo(LIBRARY);
        tapOnAddCustomUrlButton();
        return customUrlAddPage
                .typeCustomUrl(customUrl)
                .saveCustomURL();
    }

    @Step("Tap on favorites button")
    public FavoritesPage tapOnFavoritesButton() {
        clickOnElement(libraryFavoritesLabel);
        return favoritesPage.waitUntilPageReady();
    }

    @Step("Tap downloads in library page")
    public DownloadsPage tapOnDownloadsButton() {
        clickOnElement(libraryDownloadsLabel);
        return downloadsPage.waitUntilPageReady();
    }

    public static ElementsCollection getItemsUnderRecentsCategory() {
        String locator = isAndroid() ? "//*[@resource-id='tunein.player:id/row_square_cell_title']" : "//*[@type='XCUIElementTypeCell']/following-sibling::*/XCUIElementTypeOther";
        return $$(By.xpath(String.format(locator)));
    }

    @Step("Click on Start listening")
    public LibraryPage tapOnStartListening() {
        clickOnElement(libraryRecentsStartListening);
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate that favotite count is equal to {expectedValue}")
    public LibraryPage validateFavoritesCount(int expectedValue) {
        int actualValue = Integer.parseInt(getElementText(libraryFavoritesCountLabel).replaceAll("\\D", ""));
        assertThat(actualValue).as("Count " + expectedValue + " is not matching").isEqualTo(expectedValue);
        return this;
    }

    @Step("Validate downloads {expectedValue} count")
    public LibraryPage validateDownloadsCount(int expectedValue) {
        int actualValue = Integer.parseInt(getElementText(downloadsCountLabel).replaceAll("\\D", ""));
        assertThat(actualValue).as("Count " + expectedValue + " is not matching").isEqualTo(expectedValue);
        return this;
    }

    @Step
    public LibraryPage validateCustomURLButtonIsDisplayed() {
        assertThat(isElementDisplayed(libraryAddCustomUrlButton)).as("Add custom URL button is not displayed").isTrue();
        return this;
    }

    @Step
    public LibraryPage validateNumberOfRecents(int expectedCount) {
        int actualCount = libraryPage.getItemsNumberUnderCategory(RECENTS);
        assertThat(actualCount).as("Resents count is not matching from Home to Library").isEqualTo(expectedCount);
        return this;
    }

    @Step("Validate Downloads label is displayed")
    public LibraryPage validateDownloadsIsDisplayed() {
        assertThat(isElementDisplayed(libraryDownloadsLabel)).as("Downloads is not displayed").isTrue();
        return this;
    }

    public LibraryPage validateRecentsPromptIsDisplayed() {
        getSoftAssertion().assertThat(isElementDisplayed(libraryRecentsPrompt)).as("Episode card date is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(libraryRecentsDescription)).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(libraryRecentsStartListening)).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("verify that library page is opened")
    public LibraryPage validateLibraryPageIsOpened() {
        assertThat(isOnLibraryPage()).as("Library page was not opened").isTrue();
        return this;
    }

    @Step("Verify that favourites are not empty")
    public LibraryPage validateThatFavouritesNotEmpty() {
        int actualValue = Integer.parseInt(getElementText(libraryFavoritesCountLabel).replaceAll("\\D", ""));
        assertThat(actualValue).as("Favourites list is empty").isNotEqualTo(0);
        return this;
    }

    @Step("Verify content {contentName} is not displayed in library recents")
    public LibraryPage validateContentNotDisplayedInLibraryRecents(String contentName) {
        String format = String.format(libraryRecentsLocator, contentName);
        assertThat(isElementNotDisplayed(By.xpath(format), Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))).as(contentName + " is displayed in Library recents when it shouldn't be").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnLibraryPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(libraryAddCustomUrlButton);
    }

    public HashMap<String, SelenideElement> libraryPageElements() {
        return new HashMap<>() {{
            put(SKIP_TEXT_VALIDATION_PREFIX + 0, recentsLabel);
            put(SKIP_TEXT_VALIDATION_PREFIX + 1, libraryFavoritesCountLabel);
            put(SKIP_TEXT_VALIDATION_PREFIX + 2, downloadsCountLabel);
            put("Custom URL", libraryAddCustomUrlButton);
        }};
    }
}
