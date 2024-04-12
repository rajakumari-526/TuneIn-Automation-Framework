package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public abstract class NowPlayingSleepTimerDialog extends BasePage {

    protected SelenideElement sleepTimerTitle = $(android(androidUIAutomator("text(\"Sleep timer\")"))
            .ios(iOSNsPredicateString("name == 'Sleep timer' AND type == 'XCUIElementTypeStaticText'"))).as("Title");

    protected SelenideElement sleepTimerCancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("label == 'Cancel'"))).as("Cancel button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NowPlayingSleepTimerDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(sleepTimerTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract NowPlayingPage turnOffSleepTimer();

    @Step
    public void tapCancelButton() {
        clickOnElement(sleepTimerCancelButton);
    }

    public abstract void tapStopButton();

    @Step("Set required sleep timer {sleepTimerOption}")
    public NowPlayingPage setRequiredSleepTimerOption(SleepTimerOptions sleepTimerOption) {
        clickOnElement(getSleepTimerOptionWithMinutes(Duration.ofMinutes(sleepTimerOption.getSleepTimerOptionValue())));
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

    @Step("Set sleep timer for {sleepTimerOption} minute")
    public void setRequiredSleepTimerOptionLite(SleepTimerOptions sleepTimerOption) {
        clickOnElement(getSleepTimerOptionWithMinutes(Duration.ofMinutes(sleepTimerOption.getSleepTimerOptionValue())));
    }

    /* --- Validation Methods --- */

    @Step("Verify if sleep timer screen is displayed")
    public NowPlayingSleepTimerDialog validateSleepTimerScreenIsDisplayed() {
        assertThat(isElementDisplayed(sleepTimerTitle, Duration.ofMinutes(config().waitVeryShortTimeoutSeconds()))).as("Sleep timer screen is not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public abstract SelenideElement getSleepTimerOptionWithMinutes(Duration minutes);

    public enum SleepTimerOptions {
        ONE(1),
        FIFTEEN(15),
        TWENTY(20),
        THIRTY(30),
        FORTY_FIVE(40),
        SIXTY(60),
        NINETY(90),
        ONE_HUNDRED_AND_TWENTY(120);

        private int sleepTimerOption;

        SleepTimerOptions(int sleepTimerOption) {
            this.sleepTimerOption = sleepTimerOption;
        }

        public int getSleepTimerOptionValue() {
            return sleepTimerOption;
        }

    }

}
