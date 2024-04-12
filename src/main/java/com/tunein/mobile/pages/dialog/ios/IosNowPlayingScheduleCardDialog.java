package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.dialog.common.NowPlayingScheduledDialog;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.utils.ElementHelper.getElementText;

public class IosNowPlayingScheduleCardDialog extends NowPlayingScheduledDialog {

    protected SelenideElement scheduleCardStreamTitle = $x("//XCUIElementTypeTable[.//*[@value='More']]/preceding-sibling::*//XCUIElementTypeStaticText[1]").as("Stream title");

    protected SelenideElement scheduleCardStreamSubtitle = $x("//XCUIElementTypeTable[.//*[@value='More']]/preceding-sibling::*//XCUIElementTypeStaticText[2]").as("Stream subtitle");

    protected SelenideElement scheduleCardStreamIcon = $x("//XCUIElementTypeTable[.//*[@value='More']]/preceding-sibling::*//XCUIElementTypeImage").as("Stream icon");

    @Override
    public String getStreamName() {
        return getElementText(scheduleCardStreamTitle);
    }

    @Override
    public HashMap<String, SelenideElement> schedulePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put("More", scheduleCardMoreButton);
        elementsMap.put("Share", scheduleCardShareButton);
        elementsMap.put("Favorite", scheduleCardFavoriteButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "1", scheduleCardStreamIcon);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "2", scheduleCardStreamSubtitle);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "3", scheduleCardStreamTitle);
        return elementsMap;
    }

}
