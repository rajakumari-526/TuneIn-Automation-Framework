package com.tunein.mobile.pages.ios.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.library.FavoritesPage;
import com.tunein.mobile.utils.GestureActionUtil;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IosFavoritesPage extends FavoritesPage {

    private static final String FAVORITES_LOCATOR_BY_NAME = "//*[@name=\"%s\"]/following-sibling::*/XCUIElementTypeOther[contains(@name,\"%s\")]";

    private String addYourFavoritsTextLocator = "//XCUIElementTypeStaticText[contains(@label,\"%s\")]";

    /* --- Helper Methods --- */

    @Override
    public FavoritesPage unfollowContentWithIndexUnderCategory(CategoryType categoryType, int index) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public String getFavoritedContentName(String contentName, CategoryType category) {
        // before split: "MSNBC. MSNBC Live with Alex Witt"
        String format = String.format(FAVORITES_LOCATOR_BY_NAME, category.getCategoryTypeValue(), contentName);
        String subtitle = getElementNameOrLabel(scrollTo(By.xpath(format), DOWN, 3));
        String[] dataArray = subtitle.split("\\. ", 0);
        // after split: "MSNBC"
        return dataArray[0];
    }

    @Step("Validate that content {contentName} appeared in Favorites")
    @Override
    public FavoritesPage validateThatContentAppearedInFavorites(String contentName) {
        GestureActionUtil.scrollToRefresh();
        String errorMessage = "\"" + contentName + "\" content was not added to \"Favorites\"";
        boolean isContainsName = favoritesCellContainers.asDynamicIterable().stream().anyMatch(SelenideElement -> getElementText(SelenideElement).contains(contentName));
        assertThat(isContainsName).as(errorMessage).isTrue();
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate that content with name {contentName} dissappeared from favoutites")
    @Override
    public void validateThatContentDisappearedInFavorites(String contentName) {
        By favoriteItem = AppiumBy.iOSNsPredicateString(String.format("name CONTAINS '%s' AND type == 'XCUIElementTypeOther'", contentName));
        assertThatThrownBy(() -> {
            scrollTo(favoriteItem, DOWN, 3);
        }).isInstanceOf(Error.class)
                .hasMessageContaining("Cannot find element after scroll");
    }

    /* --- Action Methods --- */

    @Step
    @Override
    public FavoritesPage validateAddYourFavoriteTextIsDisplayed(CategoryType categoryType) {
        SelenideElement value = scrollTo(By.xpath(String.format(addYourFavoritsTextLocator, categoryType.getCategoryTypeValue())), DOWN);
        assertThat(isElementDisplayed(value)).as("Add you favorite text is not displayed").isTrue();
        return this;
    }
}
