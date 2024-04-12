package com.tunein.mobile.pages.ios.homepage;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.homepage.CarModePage;
import com.tunein.mobile.utils.ReporterUtil;

import java.util.HashMap;

public class IosCarModePage extends CarModePage {

    /* --- Validation Methods --- */

    @Override
    public CarModePage validateCarModeTitleIsEqualTo(String expectedTitle) {
        // TODO: https://tunein.atlassian.net/browse/IOS-16835 Title element is listed as not visible by Appium
        ReporterUtil.log("Not supported on iOS due to title element being listed as not visible by Appium despite user being able to see it");
        return this;
    }

    /* --- Helper Methods --- */

    @Override
    public HashMap<String, SelenideElement> carModeElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();

        elementsMap.put("FAVORITES", carModeFavoritesButton);
        elementsMap.put("RECENTS", carModeRecentsButton);
        elementsMap.put("RECOMMENDED", carModeRecommendedButton);
        elementsMap.put("EXIT CAR MODE", carModeExitButton);

        return elementsMap;
    }
}
