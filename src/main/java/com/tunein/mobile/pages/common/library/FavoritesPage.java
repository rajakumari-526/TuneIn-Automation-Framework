package com.tunein.mobile.pages.common.library;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.GestureActionUtil;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class FavoritesPage extends BasePage {

    /* --- Favorites page UI elements --- */

    public ElementsCollection favoritesCellContainers = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_square_cell_container.*$|^.*content_frame.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeOther"))).as("Favorites cell containers");

    protected SelenideElement findSomethingButton = $(android(id("primary_button"))
            .ios(iOSNsPredicateString("label == 'Find Something' AND type == 'XCUIElementTypeStaticText'"))).as("Find something button");

    protected SelenideElement addYourFavoriteText = $(android(id("title"))
            .ios(id("compactPromptTitleId"))).as("'Add Your Favorite' text");


    /* --- Loadable Component Method --- */

    @Step
    @Override
    public FavoritesPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero(favoritesCellContainers, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract FavoritesPage unfollowContentWithIndexUnderCategory(CategoryType categoryType, int index);

    /* --- Helper Methods --- */

    public CategoryType getCategoryTypeFromContents(Contents content) {
        String contentType = content.getContentProfileType();
        switch (contentType) {
            case "station", "premiumStation", "restricted", "news", "premiumNews", "teamCategory" -> {
                return CATEGORY_TYPE_STATIONS;
            }
            case "show" -> {
                return CATEGORY_TYPE_SHOWS;
            }
            case "sport", "podcast", "premiumPodcast" -> {
                return CATEGORY_TYPE_PODCASTS;
            }
            case "artist" -> {
                return CATEGORY_TYPE_ARTISTS;
            }
            case "Suggestions (Artist)" -> {
                return CATEGORY_TYPE_SUGGESTED_ARTIST;
            }
            case "song" -> {
                return CATEGORY_TYPE_SONGS;
            }
            case "team" -> {
                return CATEGORY_TYPE_TEAMS;
            }
            //TODO Add support for `Albums`
            default -> throw new IllegalStateException("Unexpected value: " + contentType);
        }
    }

    /**
     * This function validates if favorited content is listed in Favorites page
     *
     * @param contentName expects profile name from Contents.json ("FOX News Talk")
     * @param category    expects category type from Contents.json file ("premiumNews")
     */
    public abstract String getFavoritedContentName(String contentName, CategoryType category);

    /* --- Validation Methods --- */

    /**
     * This function validates if favorited content is listed in Favorites page
     *
     * @param contentName expects profile name from Contents.json ("FOX News Talk")
     * @param category    expects category type from Contents.json file ("premiumNews")
     */
    @Step("Validate that content {contentName} appeared in Favorites under category {category}")
    public FavoritesPage validateThatContentAppearedInFavorites(String contentName, CategoryType category) {
        GestureActionUtil.scrollToRefresh();
        closePermissionPopupsIfDisplayed();
        String errorMessage = "\"" + contentName + "\" content was not added to \"Favorites\"";
        assertThat(getFavoritedContentName(contentName, category)).as(errorMessage).isEqualToIgnoringCase(contentName);
        return this;
    }

    /**
     * This function validates if favorited content is listed in Favorites page
     *
     * @param contentName expects profile name from Contents.json ("FOX News Talk")
     */
    public abstract FavoritesPage validateThatContentAppearedInFavorites(String contentName);

    /**
     * This function validates if favorited content is NOT listed in Favorites page
     *
     * @param contentName expects profile name from Contents.json ("FOX News Talk")
     */

    public abstract void validateThatContentDisappearedInFavorites(String contentName);

    public abstract FavoritesPage validateAddYourFavoriteTextIsDisplayed(CategoryType categoryType);

    @Step
    public FavoritesPage tapOnFindSomething() {
        clickOnElement(findSomethingButton);
        return this;
    }

    @Step
    public FavoritesPage validateAddYourFavoritesPromptIsDisplayed() {
          getSoftAssertion().assertThat(isElementDisplayed(addYourFavoriteText)).as("Episode card time is not displayed").isTrue();
          getSoftAssertion().assertThat(isElementDisplayed(findSomethingButton)).as("Episode card time is not displayed").isTrue();
          getSoftAssertion().assertAll();
        return this;
    }

    @Step("Verify favourites page is opened")
    public FavoritesPage validateFavouritesPageIsOpened() {
        assertThat(isOnFavouritesPage()).as("Favourites page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnFavouritesPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(favoritesCellContainers.get(0), Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

}
