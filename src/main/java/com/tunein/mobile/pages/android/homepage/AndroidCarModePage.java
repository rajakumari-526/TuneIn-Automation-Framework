package com.tunein.mobile.pages.android.homepage;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.homepage.CarModePage;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidCarModePage extends CarModePage {

    protected SelenideElement carModeHeaderSubTitleText = $(id("carModeHeaderSubTitleText")).as("Header subtitle text");

    protected SelenideElement carModeArtwork = $(id("carmode_artwork")).as("Logo");

    protected SelenideElement carModeVoiceSearchButton = $(id("carModeVoiceSearchText")).as("Voice search button");

    /* --- Validation Methods --- */

    @Override
    public CarModePage validateCarModeTitleIsEqualTo(String expectedTitle) {
        String carModeTitle = getElementText(carModeHeaderSubTitleText);
        String message = "Car Mode title \"" + carModeTitle + "\" doesn't match expected title \"" + expectedTitle + "\"";
        assertThat(carModeTitle).as(message).isEqualTo(expectedTitle);
        return this;
    }

    /* --- Helper Methods --- */

    public HashMap<String, SelenideElement> carModeElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;

        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, carModeHeaderSubTitleText);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, carModeArtwork);
        elementsMap.put("Favorites", carModeFavoritesButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, carModePlayStopButton);
        elementsMap.put("Voice Search", carModeVoiceSearchButton);
        elementsMap.put("Recents", carModeRecentsButton);
        elementsMap.put("Recommended", carModeRecommendedButton);
        elementsMap.put("Exit", carModeExitButton);

        return elementsMap;
    }
}
