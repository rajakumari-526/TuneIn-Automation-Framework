package com.tunein.mobile.pages.ios.explorer;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.explorer.ExplorerMiniPlayerPage;
import io.appium.java_client.AppiumBy;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;

public class IosExplorerMiniPlayerPage extends ExplorerMiniPlayerPage {

    protected SelenideElement explorerMiniPlayerPauseButton = $(AppiumBy.id("miniPlayerPauseButton")).as("Explorer mini-player pause button");

    protected SelenideElement explorerMiniPlayerUnFavoriteButton = $(AppiumBy.xpath("//XCUIElementTypeButton[@name='map unfavorited' and @value]")).as("UnFavorite button");

    @Override
    public ExplorerMiniPlayerPage clickExplorerMiniPlayerUnFavouriteButton() {
        clickOnElement(explorerMiniPlayerUnFavoriteButton);
        return this;
    }
  
    @Override
    public HashMap<String, SelenideElement> explorerMiniPlayerPageElements() {
        return new HashMap<>() {{
            put(SKIP_TEXT_VALIDATION_PREFIX + "1", explorerMiniPlayerStationLogo);
            put(SKIP_TEXT_VALIDATION_PREFIX + "2", explorerMiniPlayerFavoriteButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + "3", explorerMiniPlayerStationPodcastName);
            put(SKIP_TEXT_VALIDATION_PREFIX + "4", explorerMiniPlayerStationPodcastInfo);
            put(SKIP_TEXT_VALIDATION_PREFIX + "5", explorerMiniPlayerPauseButton);
        }};
    }
}
