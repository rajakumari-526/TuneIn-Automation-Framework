package com.tunein.mobile.pages.android.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.library.FavoritesPage;
import com.tunein.mobile.utils.GestureActionUtil;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AndroidFavoritesPage extends FavoritesPage {

    private static final String FAVORITES_LOCATOR_BY_NAME = "//android.widget.FrameLayout[.//android.widget.TextView[@text=\"%s\"]]/following-sibling::*/android.widget.TextView[contains(translate(@text,\"ABCDEFGHIJKLMNOPQRSTUVWXYZ\",\"abcdefghijklmnopqrstuvwxyz\"),\"%s\")]";

    private static final String FAVORITES_LOCATOR_BY_NAME_WITHOUT_CATEGORY = ".//android.view.ViewGroup[./android.widget.TextView[contains(translate(@text,\"ABCDEFGHIJKLMNOPQRSTUVWXYZ\",\"abcdefghijklmnopqrstuvwxyz\"),\"%s\")]]";

    private String addYourFavoritesTextLocator = "//android.view.ViewGroup/android.widget.TextView[contains(@text,\"%s\")]";

    /* --- Helper Methods --- */

    @Step("Unfollow Content with index {index} under category {categoryType}")
    @Override
    public FavoritesPage unfollowContentWithIndexUnderCategory(CategoryType categoryType, int index) {
        SelenideElement element = contentsListPage.getContentUnderCategoryWithHeader(categoryType, LIST, MEDIUM, index);
        longPressOnElement(element);
        contentListItemDialog
                .waitUntilPageReady()
                .clickOnUnfollowButton();
        return this.waitUntilPageReady();
    }

    @Override
    public String getFavoritedContentName(String contentName, CategoryType category) {
        String format = String.format(FAVORITES_LOCATOR_BY_NAME, category.getCategoryTypeValue(), contentName.toLowerCase());
        return getElementText(scrollTo(By.xpath(format), DOWN, config().scrollTenTimes()));
    }

    @Step("Validate that content {contentName} appeared in Favorites")
    @Override
    public FavoritesPage validateThatContentAppearedInFavorites(String contentName) {
        GestureActionUtil.scrollToRefresh();
        closePermissionPopupsIfDisplayed();
        String errorMessage = "\"" + contentName + "\" content was not added to \"Favorites\"";
        String format = String.format(FAVORITES_LOCATOR_BY_NAME_WITHOUT_CATEGORY, contentName.toLowerCase());
        SelenideElement content = scrollTo(By.xpath(format), DOWN, config().scrollFewTimes());
        assertThat(isElementDisplayed(content)).as(errorMessage).isTrue();
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate that content with name {contentName} dissappeared from favoutites")
    @Override
    public void validateThatContentDisappearedInFavorites(String contentName) {
        GestureActionUtil.scrollToRefresh();
        GestureActionUtil.scrollToRefresh();
        By favoriteItem = AppiumBy.xpath(String.format("//*[contains(@text,\"%s\")]", contentName));
        assertThatThrownBy(() -> {
            scrollTo(favoriteItem, DOWN, 3);
        }).isInstanceOf(Error.class)
                .hasMessageContaining("Cannot find element after scroll");
    }

    @Step
    @Override
    public FavoritesPage validateAddYourFavoriteTextIsDisplayed(CategoryType categoryType) {
       SelenideElement value = scrollTo(By.xpath(String.format(addYourFavoritesTextLocator, categoryType.getCategoryTypeValue())), DOWN);
        assertThat(isElementDisplayed(value)).as("Add you favorite text is not displayed").isTrue();
        return this;
    }

    /* --- Action Methods --- */
}
