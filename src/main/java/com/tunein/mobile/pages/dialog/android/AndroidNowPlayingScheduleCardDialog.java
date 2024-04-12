package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.dialog.common.NowPlayingScheduledDialog;

import java.util.HashMap;

public class AndroidNowPlayingScheduleCardDialog extends NowPlayingScheduledDialog {

    @Override
    public String getStreamName() {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

    @Override
    public HashMap<String, SelenideElement> schedulePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put("More", scheduleCardMoreButton);
        elementsMap.put("Share", scheduleCardShareButton);
        elementsMap.put("Favorite", scheduleCardFavoriteButton);
        return elementsMap;
    }
}
