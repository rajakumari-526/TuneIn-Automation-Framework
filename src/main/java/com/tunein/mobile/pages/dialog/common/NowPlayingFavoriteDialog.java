package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.BasePage;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.By.xpath;

public abstract class NowPlayingFavoriteDialog extends BasePage {

    protected SelenideElement nowPlayingFavoriteUnfavoriteEpisodeButton = $(android(xpath("//android.widget.TextView[@text='Favorite']/following-sibling::*//android.widget.TextView[1]"))
            .ios(iOSNsPredicateString("label CONTAINS 'avorite episode'"))).as("Favorite/Unfavorite episode button");

    protected SelenideElement nowPlayingFavoriteUnfavoriteShowButton = $(android(xpath("//android.widget.TextView[@text='Favorite']/following-sibling::*//android.widget.TextView[1]"))
            .ios(iOSNsPredicateString("label CONTAINS 'avorite show'"))).as("Favorite/Unfavorite show button");

    protected SelenideElement nowPlayingFavoriteUnfavoriteSongButton = $(android(xpath("//android.widget.TextView[@text='Favorite']/following-sibling::*//android.widget.TextView[1]"))
            .ios(iOSNsPredicateString("not defined"))).as("Favorite/Unfavorite song button");

    protected SelenideElement nowPlayingFavoriteUnfavoritePodcastButton = $(android(xpath("//android.widget.TextView[@text='Favorite']/following-sibling::*//android.widget.TextView[2]"))
            .ios(iOSNsPredicateString("label CONTAINS 'avorite podcast'"))).as("Favorite/Unfavorite podcast button");

    protected SelenideElement nowPlayingFavoriteUnfavoriteStationButton = $(android(xpath("//android.widget.TextView[@text='Favorite']/following-sibling::*//android.widget.TextView[2]"))
            .ios(iOSNsPredicateString("label CONTAINS 'avorite station'"))).as("Favorite/Unfavorite station button");

    /* --- Loadable Component Method --- */

    public abstract NowPlayingFavoriteDialog waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract void tapOnFavoriteContent(boolean addToFavorite, FavoriteContentType favoriteContent);

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    public abstract Boolean isFavoritePromptHasRemoveOption(FavoriteContentType favoriteContentType);

    public abstract boolean isFavoritePopUpDisplayed();

    public abstract String getFavoriteContentText(FavoriteContentType favoriteContentType);

    public enum FavoriteContentType {
        FAVORITE_STATION("Stations"),
        FAVORITE_EPISODE("Episodes"),
        FAVORITE_PODCAST("Podcasts"),
        FAVORITE_SHOW("Shows"),
        FAVORITE_SONG("Songs"),
        FAVORITE_ARTIST("Artists"),
        FAVORITE_CUSTOM_URL("Custom URL");

        private String favoriteContentTypeValue;

        private FavoriteContentType(String favoriteContentTypeValue) {
            this.favoriteContentTypeValue = favoriteContentTypeValue;
        }

        public String getFavoriteContentTypeValue() {
            return favoriteContentTypeValue;
        }

        public static FavoriteContentType getFavoriteContentType(final String favoriteContentTypeTitle) {
            List<FavoriteContentType> favoriteContentTypesList = Arrays.asList(FavoriteContentType.values());
            return favoriteContentTypesList.stream().filter(eachFavorite -> eachFavorite.toString().equals(favoriteContentTypeTitle))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return favoriteContentTypeValue;
        }
    }

}
