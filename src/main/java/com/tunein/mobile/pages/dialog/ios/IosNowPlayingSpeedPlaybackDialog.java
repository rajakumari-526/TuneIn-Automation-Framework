package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingSpeedPlaybackDialog;
import com.tunein.mobile.utils.GestureActionUtil;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.interactions.PointerInput;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.VALUE;
import static com.tunein.mobile.utils.ElementHelper.getElementAttributeValue;
import static com.tunein.mobile.utils.GestureActionUtil.dragAndDropAction;
import static com.tunein.mobile.utils.GestureActionUtil.swipeElement;
import static java.util.TimeZone.SHORT;
import static org.assertj.core.api.Assertions.assertThat;

public class IosNowPlayingSpeedPlaybackDialog extends NowPlayingSpeedPlaybackDialog {

    protected SelenideElement audioSpeedLabel = $(AppiumBy.iOSNsPredicateString("label == \"AUDIO SPEED\"")).as("Audio Speed label");

    protected SelenideElement applyToAllPodcastsToggle = $(AppiumBy.iOSNsPredicateString("type == \"XCUIElementTypeSwitch\"")).as("Apply to all podcast toggle");

    protected SelenideElement variableSpeedPlayBackContainer = $x("//XCUIElementTypeStaticText[@name='AUDIO SPEED']//parent::XCUIElementTypeOther").as("Spped playback container");

    private static final String IOS_STREAM_SPEED_LOCATOR = "//XCUIElementTypeStaticText[@name='%s']//parent::XCUIElementTypeOther";

    /* --- Action Methods --- */

    @Step("Change speed picker position from {currentValue} to {requiredValue} ")
    @Override
    public NowPlayingPage setSpeedPickerPosition(String currentValue, String requiredValue, boolean... applyToAll) {
        SelenideElement currentSpeed = $x(String.format(IOS_STREAM_SPEED_LOCATOR, currentValue));
        SelenideElement requiredSpeed = $x(String.format(IOS_STREAM_SPEED_LOCATOR, requiredValue));
        PointerInput.MouseButton button = (Double.parseDouble(currentValue) > Double.parseDouble(requiredValue)) ? PointerInput.MouseButton.RIGHT : PointerInput.MouseButton.LEFT;

        dragAndDropAction(currentSpeed, requiredSpeed, button);
        waitUntilPageReady();
        if (applyToAll.length > 0 && (getElementAttributeValue(applyToAllPodcastsToggle, VALUE).equals("0") && applyToAll[0]
        || getElementAttributeValue(applyToAllPodcastsToggle, VALUE).equals("1") && !applyToAll[0])) {
            clickOnElement(applyToAllPodcastsToggle);
        }
        clickOnElement(doneButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Override
    @Step("Swipe down variable speed playback popup & validate popup is not closed")
    public NowPlayingSpeedPlaybackDialog swipeDownVariableSpeedPlaybackPopUp() {
        swipeElement(GestureActionUtil.SwipeDirection.DOWN, SHORT, audioSpeedLabel);
        validateVariableSpeedPlaybackPopUpisDisplayed(true);
        clickOnElement(doneButton);
        return this;
    }

    /* --- Validation Methods --- */

    @Override
    @Step("Verify apply to all podcasts toggle is off")
    public NowPlayingSpeedPlaybackDialog validateApplyToAllPodcastsIsOff() {
        assertThat(Integer.parseInt(applyToAllPodcastsToggle.getAttribute("value"))).as("Apply to all podcasts is on").isEqualTo(0);
        return this;
    }

    @Override
    @Step("Click above variable speed playback popup")
    public NowPlayingSpeedPlaybackDialog tapAboveVariableSpeedPlaybackPopUp() {
        clickAboveTheElement(variableSpeedPlayBackContainer);
        return this;
    }

    /* --- Helper Methods --- */

    @Override
    public HashMap<String, SelenideElement> speedPlaybackDialogElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, scrubber);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, scaleSpeedValuesContainter);
        elementsMap.put("AUDIO SPEED", audioSpeedLabel);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, applyToAllPodcastsToggle);
        elementsMap.put("DONE", doneButton);
        return elementsMap;
    }
}
