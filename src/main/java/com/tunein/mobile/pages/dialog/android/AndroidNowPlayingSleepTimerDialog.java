package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;

public class AndroidNowPlayingSleepTimerDialog extends NowPlayingSleepTimerDialog {

    protected SelenideElement sleepTimerStopButton = $(AppiumBy.androidUIAutomator("text(\"STOP TIMER\")")).as("Stop button");

    protected String sleepTimerMinutesLocator = "//*[contains(@text,'%s minute')]";

    @Step
    @Override
    public NowPlayingPage turnOffSleepTimer() {
        tapStopButton();
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

    @Step
    @Override
    public void tapStopButton() {
        clickOnElement(sleepTimerStopButton);
    }

    @Override
    public SelenideElement getSleepTimerOptionWithMinutes(Duration minutes) {
        return $x((String.format(sleepTimerMinutesLocator, minutes.toMinutes()))).as("Sleep timer value: " + minutes.toMillis());
    }

}
