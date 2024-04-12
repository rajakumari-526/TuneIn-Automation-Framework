package com.tunein.mobile.pages.dialog.ios;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;

public class IosNowPlayingFavoriteDialog extends NowPlayingFavoriteDialog {

    private final static String UNFAVORITE = "Unfavorite";

    @Step("Tap on favourite button, to set favorite status {addToFavorite}")
    @Override
    public NowPlayingFavoriteDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero($$(AppiumBy.iOSNsPredicateString("label CONTAINS 'avorite '")), Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    @Step("Set isFavorite={addToFavorite} for content with type {favoriteContent}")
    @Override
    public void tapOnFavoriteContent(boolean addToFavorite, FavoriteContentType favoriteContent) {
        /* [note] Favorites dialog may not show up if station does not have show */
        if (isFavoritePopUpDisplayed()) {
            switch (favoriteContent) {
                case FAVORITE_STATION -> {
                    if (getElementNameOrLabel(nowPlayingFavoriteUnfavoriteStationButton).contains(UNFAVORITE) != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoriteStationButton);
                    }
                }
                case FAVORITE_SHOW -> {
                    if (getElementNameOrLabel(nowPlayingFavoriteUnfavoriteShowButton).contains(UNFAVORITE) != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoriteShowButton);
                    }
                }
                case FAVORITE_PODCAST -> {
                    if (getElementNameOrLabel(nowPlayingFavoriteUnfavoritePodcastButton).contains(UNFAVORITE) != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoritePodcastButton);
                    }
                }
                case FAVORITE_EPISODE -> {
                    if (getElementNameOrLabel(nowPlayingFavoriteUnfavoriteEpisodeButton).contains(UNFAVORITE) != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoriteEpisodeButton);
                    }
                }
                case FAVORITE_SONG -> throw new UnsupportedOperationException("Song cannot be favorited on iOS");
                case FAVORITE_CUSTOM_URL -> {
                    clickOnElement(nowPlayingFavoriteUnfavoriteStationButton);
                    customUrlFavoriteDialog.waitUntilPageReady();
                }
                default -> throw new Error("Invalid FavoriteContentType type - " + favoriteContent);
            }
        }

    }

    @Override
    public Boolean isFavoritePromptHasRemoveOption(FavoriteContentType favoriteContentType) {
        return switch (favoriteContentType) {
            case FAVORITE_STATION -> getElementText(nowPlayingFavoriteUnfavoriteStationButton).contains(UNFAVORITE);
            case FAVORITE_PODCAST -> getElementText(nowPlayingFavoriteUnfavoritePodcastButton).contains(UNFAVORITE);
            case FAVORITE_EPISODE -> getElementText(nowPlayingFavoriteUnfavoriteEpisodeButton).contains(UNFAVORITE);
            case FAVORITE_SHOW -> getElementText(nowPlayingFavoriteUnfavoriteShowButton).contains(UNFAVORITE);
            default -> false;
        };
    }

    @Override
    public boolean isFavoritePopUpDisplayed() {
        closePermissionPopupsIfDisplayed();
        return isElementsMoreThanZero($$(AppiumBy.iOSNsPredicateString("label CONTAINS 'avorite '")), config().waitLongTimeoutMilliseconds());
    }

    @Override
    public String getFavoriteContentText(FavoriteContentType favoriteContentType) {
        if (isFavoritePopUpDisplayed()) {
            switch (favoriteContentType) {
                case FAVORITE_STATION -> {
                    return getElementNameOrLabel(nowPlayingFavoriteUnfavoriteStationButton);
                }
                case FAVORITE_SHOW -> {
                    return getElementNameOrLabel(nowPlayingFavoriteUnfavoriteShowButton);
                }
                case FAVORITE_PODCAST -> {
                    return getElementNameOrLabel(nowPlayingFavoriteUnfavoritePodcastButton);
                }
                case FAVORITE_EPISODE -> {
                    return getElementNameOrLabel(nowPlayingFavoriteUnfavoriteEpisodeButton);
                }
                case FAVORITE_SONG -> throw new UnsupportedOperationException("Song cannot be favorited on iOS");
                default ->
                        throw new UnsupportedOperationException("Invalid FavoriteContentType type - " + favoriteContentType);
            }
        }
        throw new Error("Favorite dialog is not displayed");
    }

}
