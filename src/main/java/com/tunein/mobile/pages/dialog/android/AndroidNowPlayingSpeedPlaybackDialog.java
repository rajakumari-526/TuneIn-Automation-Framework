package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingSpeedPlaybackDialog;
import com.tunein.mobile.utils.GestureActionUtil;

import org.openqa.selenium.interactions.PointerInput;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static java.util.TimeZone.SHORT;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.RIGHT;

public class AndroidNowPlayingSpeedPlaybackDialog extends NowPlayingSpeedPlaybackDialog {

    private static final String ANDROID_STREAM_SPEED_LOCATOR = "//android.widget.TextView[@text='%s' and @resource-id='tunein.player:id/speedTxt']";

    /* --- Action Methods --- */

    @Step("Change speed picker position from {currentValue} to {requiredValue} ")
    @Override
    public NowPlayingPage setSpeedPickerPosition(String currentValue, String requiredValue, boolean... applyToAll) {
        SelenideElement currentSpeed = $x(String.format(ANDROID_STREAM_SPEED_LOCATOR, currentValue));
        SelenideElement requiredSpeed = $x(String.format(ANDROID_STREAM_SPEED_LOCATOR, requiredValue));
        PointerInput.MouseButton button = (Double.parseDouble(currentValue) > Double.parseDouble(requiredValue)) ? RIGHT : LEFT;
        dragAndDropAction(currentSpeed, requiredSpeed, button);
        waitUntilPageReady();
        clickOnElement(doneButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    @Step("Click above variable speed playback popup")
    public NowPlayingSpeedPlaybackDialog tapAboveVariableSpeedPlaybackPopUp() {
        deviceNativeActions.clickBackButton();
        return this;
    }

    @Override
    @Step("Swipe down variable speed playback popup & validate popup is closed")
    public NowPlayingSpeedPlaybackDialog swipeDownVariableSpeedPlaybackPopUp() {
        swipeElement(GestureActionUtil.SwipeDirection.DOWN, SHORT, doneButton);
        validateVariableSpeedPlaybackPopUpisDisplayed(false);
        return this;
    }

    /* --- Validation Methods --- */

    @Override
    public NowPlayingSpeedPlaybackDialog validateApplyToAllPodcastsIsOff() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    /* --- Helper Methods --- */

    @Override
    public HashMap<String, SelenideElement> speedPlaybackDialogElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, scrubber);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, scaleSpeedValuesContainter);
        elementsMap.put("DONE", doneButton);
        return elementsMap;
    }

}
