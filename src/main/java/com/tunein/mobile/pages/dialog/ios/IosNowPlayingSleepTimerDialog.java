package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosNowPlayingSleepTimerDialog extends NowPlayingSleepTimerDialog {

    protected SelenideElement sleepTimerDoneButton = $(iOSNsPredicateString("label == 'DONE'")).as("Done button");

    protected SelenideElement sleepTimerNoneButton = $(iOSNsPredicateString("label == \"None\"")).as("None button");

    protected String sleepTimerMinutesLocator = "label CONTAINS '%s minute'";

    @Step
    @Override
    public NowPlayingPage turnOffSleepTimer() {
        tapNoneButton();
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step
    @Override
    public void tapStopButton() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public SelenideElement getSleepTimerOptionWithMinutes(Duration minutes) {
        return $(iOSNsPredicateString(String.format(sleepTimerMinutesLocator, minutes.toMinutes()))).as("Sleep timer value: " + minutes.toMillis());
    }

    @Step
    public NowPlayingPage tapNoneButton() {
        clickOnElement(sleepTimerNoneButton);
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

}
