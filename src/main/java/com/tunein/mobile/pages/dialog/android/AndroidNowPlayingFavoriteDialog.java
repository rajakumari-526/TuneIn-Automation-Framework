package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;

import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_CUSTOM_URL;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class AndroidNowPlayingFavoriteDialog extends NowPlayingFavoriteDialog {

    protected SelenideElement nowPlayingFavoritePopUp = $(AppiumBy.id("tunein.player:id/dialog_title")).as("Favorite popup");

    @Step
    @Override
    public NowPlayingFavoriteDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(nowPlayingFavoritePopUp, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Set isFavorite={addToFavorite} for content with type {favoriteContent}")
    @Override
    public void tapOnFavoriteContent(boolean addToFavorite, FavoriteContentType favoriteContent) {
        /* [note] Favorites dialog may not show up if station does not have show */
        // TODO Add possibility to identify that episode and show is favored
        if (favoriteContent == FAVORITE_CUSTOM_URL) {
            customUrlFavoriteDialog.waitUntilPageReady();
        } else if (isFavoritePopUpDisplayed()) {
            switch (favoriteContent) {
                case FAVORITE_STATION -> {
                    if (getElementText(nowPlayingFavoriteUnfavoriteStationButton).contains("Remove") != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoriteStationButton);
                    }
                }
                case FAVORITE_SHOW -> clickOnElement(nowPlayingFavoriteUnfavoriteShowButton);
                case FAVORITE_PODCAST -> {
                    if (getElementText(nowPlayingFavoriteUnfavoritePodcastButton).contains("Remove") != addToFavorite) {
                        clickOnElement(nowPlayingFavoriteUnfavoritePodcastButton);
                    }
                }
                case FAVORITE_EPISODE -> clickOnElement(nowPlayingFavoriteUnfavoriteEpisodeButton);
                case FAVORITE_SONG -> clickOnElement(nowPlayingFavoriteUnfavoriteSongButton);
                default -> throw new Error("Wrong favorite content type");
            }
        }
    }

    // TODO episode and show buttons should have Remove state
    @Override
    public Boolean isFavoritePromptHasRemoveOption(FavoriteContentType favoriteContentType) {
        switch (favoriteContentType) {
            case FAVORITE_STATION -> {
                return getElementText(nowPlayingFavoriteUnfavoriteStationButton).contains("Remove");
            }
            case FAVORITE_PODCAST -> {
                return getElementText(nowPlayingFavoriteUnfavoritePodcastButton).contains("Remove");
            }
            case FAVORITE_EPISODE -> {
                return getElementText(nowPlayingFavoriteUnfavoriteEpisodeButton).contains("Remove");
            }
            case FAVORITE_SHOW -> {
                return getElementText(nowPlayingFavoriteUnfavoriteShowButton).contains("Remove");
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isFavoritePopUpDisplayed() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(nowPlayingFavoritePopUp, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    @Override
    public String getFavoriteContentText(FavoriteContentType favoriteContentType) {
        if (isFavoritePopUpDisplayed()) {
            switch (favoriteContentType) {
                case FAVORITE_STATION -> {
                    return getElementText(nowPlayingFavoriteUnfavoriteStationButton);
                }
                case FAVORITE_SHOW -> {
                    return getElementText(nowPlayingFavoriteUnfavoriteShowButton);
                }
                case FAVORITE_PODCAST -> {
                    return getElementText(nowPlayingFavoriteUnfavoritePodcastButton);
                }
                case FAVORITE_EPISODE -> {
                    return getElementText(nowPlayingFavoriteUnfavoriteEpisodeButton);
                }
                case FAVORITE_SONG -> {
                    return getElementText(nowPlayingFavoriteUnfavoriteSongButton);
                }
                default -> throw new Error("Wrong favorite content type");
            }
        }
        throw new Error("Favorite dialog is not displayed");
    }

}
