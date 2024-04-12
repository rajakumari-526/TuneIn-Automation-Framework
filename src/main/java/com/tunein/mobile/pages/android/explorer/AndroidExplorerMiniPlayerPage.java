package com.tunein.mobile.pages.android.explorer;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.explorer.ExplorerMiniPlayerPage;

import java.util.HashMap;

public class AndroidExplorerMiniPlayerPage extends ExplorerMiniPlayerPage {

    @Override
    public ExplorerMiniPlayerPage clickExplorerMiniPlayerUnFavouriteButton() {
        clickOnElement(explorerMiniPlayerFavoriteButton);
        return this;
    }
  
    @Override
    public HashMap<String, SelenideElement> explorerMiniPlayerPageElements() {
        return new HashMap<>() {{
            put(SKIP_TEXT_VALIDATION_PREFIX + "1", explorerMiniPlayerStationLogo);
            put(SKIP_TEXT_VALIDATION_PREFIX + "2", explorerMiniPlayerFavoriteButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + "3", explorerMiniPlayerStationPodcastName);
            put(SKIP_TEXT_VALIDATION_PREFIX + "4", explorerMiniPlayerStationPodcastInfo);
            put(SKIP_TEXT_VALIDATION_PREFIX + "5", explorerMiniPlayerPlayButton);
        }};
    }

}
