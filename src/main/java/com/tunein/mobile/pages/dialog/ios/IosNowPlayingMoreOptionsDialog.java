package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.HelpCenterWebPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.*;
import static com.tunein.mobile.utils.ApplicationUtil.getAlertButtons;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class IosNowPlayingMoreOptionsDialog extends NowPlayingMoreOptionsDialog {

    protected SelenideElement moreOptionsCancelButton = $(accessibilityId("Cancel")).as("Cancel button");

    protected SelenideElement moreOptionsNeedHelpButton = $(accessibilityId("Need help?")).as("Need help? button");

    protected SelenideElement moreOptionsEditAlarmButton = $(iOSNsPredicateString("name == 'Edit Alarm' AND type == 'XCUIElementTypeStaticText' AND visible == true")).as("Edit alarm button");

    protected SelenideElement moreOptionsMenu = $(xpath("//XCUIElementTypeOther[./*/XCUIElementTypeOther/XCUIElementTypeStaticText[@name='More']]")).as("More options menu");

    @Step
    @Override
    public void closeMoreOptionsDialog() {
        clickAboveTheElement(moreOptionsMenu);
    }

    @Step
    @Override
    public void tapOnNowPlayingOptionsButton(MoreOptionsButtons moreOptionsButton) {
        switch (moreOptionsButton) {
            case SHARE, CANCEL, CLOCK_DISPLAY -> { }
            case SLEEP_TIMER -> tapSleepTimerButton();
            case SET_ALARM -> tapSetAlarmButton();
            case CHOOSE_STREAM -> tapChooseStreamButton();
            case NEED_HELP -> tapNeedHelpButton();
            case GO_TO_PROFILE -> tapGoToProfileButton();
            case CAR_MODE -> throw new UnsupportedOperationException("Functionality is absent for Android Platform");
            default -> throw new Error("Invalid more options button");
        }
    }

    @Step
    public HelpCenterWebPage tapNeedHelpButton() {
        clickOnElement(moreOptionsNeedHelpButton);
        return helpCenterWebPage.waitUntilPageReady();
    }

    @Step
    @Override
    public NowPlayingMoreOptionsDialog validateEditAlarmButtonsIsDisplayed() {
        assertThat(isElementDisplayed(moreOptionsEditAlarmButton))
                .as("Edit Alert button is not displayed")
                .isTrue();
        return this;
    }

    @Step
    @Override
    public NowPlayingMoreOptionsDialog validateGoToProfileButtonIsNotDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for iOS");
    }

    @Override
    public List<MoreOptionsButtons> getMoreOptionsButtons() {
        List<String> optionButtons = getAlertButtons();
        return optionButtons.stream().map(optionButton -> getMoreOptionsType(optionButton)).collect(Collectors.toList());
    }

    @Override
    public List<MoreOptionsButtons> getExpectedMoreOptionItems(ContentType contentType) {
        List<MoreOptionsButtons> expectedList = new ArrayList<>();
        expectedList.add(CLOCK_DISPLAY);
        expectedList.add(SET_ALARM);
        switch (contentType) {
            case LIVE_STATION, PREMIUM_LIVE_STATION, OWNED_AND_OPERATED -> {
                expectedList.add(SHARE);
                expectedList.add(CHOOSE_STREAM);
                expectedList.add(NEED_HELP);
            }
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                expectedList.add(SHARE);
                expectedList.add(NEED_HELP);
            }
            default -> throw new Error("Invalid ContentType type - " + contentType);
        }
        return expectedList;
    }
}
