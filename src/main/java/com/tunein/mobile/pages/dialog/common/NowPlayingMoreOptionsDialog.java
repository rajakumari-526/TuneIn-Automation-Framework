package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class NowPlayingMoreOptionsDialog extends BasePage {

    protected SelenideElement moreOptionsShareButton = $(android(androidUIAutomator("text(\"Share\")"))
            .ios(accessibilityId("Share"))).as("Share button");

    protected SelenideElement moreOptionsSetAlarmButton = $(android(androidUIAutomator("text(\"Set alarm\")"))
            .ios(iOSNsPredicateString("name IN {'Set Alarm' , 'Edit Alarm'} AND type == 'XCUIElementTypeStaticText'"))).as("Set alarm button");

    protected SelenideElement moreOptionsSetSleepTimerButton = $(android(androidUIAutomator("text(\"Set sleep timer\")"))
            .ios(iOSNsPredicateString("name IN {'Edit Sleep Timer' , 'Sleep timer'} AND type == 'XCUIElementTypeStaticText'"))).as("Set sleep timer button");

    protected SelenideElement moreOptionsCarModeButton = $(android(androidUIAutomator("text(\"Car mode\")"))
            .ios(accessibilityId("Car mode"))).as("Car mode button");

    protected SelenideElement moreOptionsChooseStreamButton = $(android(androidUIAutomator("text(\"Choose stream\")"))
            .ios(accessibilityId("Choose stream"))).as("Choose stream button");

    protected SelenideElement moreOptionsClockDisplayButton = $(android(androidUIAutomator("text(\"Clock display\")"))
            .ios(accessibilityId("Clock display"))).as("Clock display button");

    protected SelenideElement moreOptionsGoToProfileButton = $(android(androidUIAutomator("text(\"Go to profile\")"))
            .ios(iOSNsPredicateString("name IN {'View station', 'Go to podcast'}"))).as("Go to Profile button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NowPlayingMoreOptionsDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(moreOptionsSetAlarmButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public ContentProfilePage tapGoToProfileButton() {
        clickOnElement(moreOptionsGoToProfileButton);
        return contentProfilePage.waitUntilPageReady();
    }

    public abstract void closeMoreOptionsDialog();

    @Step
    public NowPlayingChooseStreamDialog tapChooseStreamButton() {
        clickOnElement(moreOptionsChooseStreamButton);
        return nowPlayingChooseStreamDialog.waitUntilPageReady();
    }

    @Step
    public NowPlayingSleepTimerDialog tapSleepTimerButton() {
        clickOnElement(moreOptionsSetSleepTimerButton);
        return nowPlayingSleepTimerDialog.waitUntilPageReady();
    }

    @Step
    public NowPlayingSetAlarmDialog tapSetAlarmButton() {
        clickOnElement(moreOptionsSetAlarmButton);
        return nowPlayingSetAlarmDialog.waitUntilPageReady();
    }

    public abstract void tapOnNowPlayingOptionsButton(MoreOptionsButtons moreOptionsButton);

    /* --- Validation Methods --- */

    @Step("Validate More options buttons {expectedButtons} are correct")
    public NowPlayingMoreOptionsDialog validateMoreOptionButtonsAreCorrect(List<MoreOptionsButtons> expectedButtons) {
        List<MoreOptionsButtons> moreOptionsButtonList = getMoreOptionsButtons();
        moreOptionsButtonList.stream().forEach(button ->
                getSoftAssertion()
                    .assertThat(button)
                    .as("Button " + button.getMoreOptionsButton() + " is not displayed in More Options Menu")
                    .isIn(expectedButtons)
        );
        getSoftAssertion().assertAll();
        return this;
    }

    public abstract NowPlayingMoreOptionsDialog validateEditAlarmButtonsIsDisplayed();

    public abstract NowPlayingMoreOptionsDialog validateGoToProfileButtonIsNotDisplayed();

    @Step("Validate more options menu dialog box is displayed")
    public NowPlayingMoreOptionsDialog validateMoreOptionsMenuDisplayed() {
        assertThat(isElementDisplayed(moreOptionsSetAlarmButton)).as("more options menu dialog box is not displayed").isTrue();
        return this;
    }

    @Step("Validate more options menu dialog box is not displayed")
    public void validateMoreOptionsMenuIsNotDisplayed() {
        assertThat(isElementNotDisplayed(moreOptionsSetAlarmButton)).as("more options menu dialog box is displayed").isTrue();
    }

    /* --- Helper Methods --- */

    public abstract List<MoreOptionsButtons> getMoreOptionsButtons();

    public abstract List<MoreOptionsButtons> getExpectedMoreOptionItems(ContentType contentType);

    public enum MoreOptionsButtons {
        SHARE("Share"),
        CHOOSE_STREAM("Choose stream"),
        NEED_HELP("Need help?"),
        CLOCK_DISPLAY("Clock display"),
        SET_ALARM((isAndroid()) ? "Set alarm" : "Set Alarm"),
        SLEEP_TIMER((isAndroid()) ? "Set sleep timer" : "Sleep timer"),
        CAR_MODE("Car mode"),
        GO_TO_PROFILE("Go to profile"),
        CANCEL("Cancel");

        private String moreOptionsButtonTitle;

        MoreOptionsButtons(String moreOptionsButtonTitle) {
            this.moreOptionsButtonTitle = moreOptionsButtonTitle;
        }

        public String getMoreOptionsButton() {
            return moreOptionsButtonTitle;
        }

        public static MoreOptionsButtons getMoreOptionsType(final String moreOptionsButtonTitle) {
            List<MoreOptionsButtons> moreOptionsButtonsList = Arrays.asList(MoreOptionsButtons.values());
            return moreOptionsButtonsList.stream().filter(eachMoreOptionItem -> eachMoreOptionItem.toString().equals(moreOptionsButtonTitle))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return moreOptionsButtonTitle;
        }
    }

}
