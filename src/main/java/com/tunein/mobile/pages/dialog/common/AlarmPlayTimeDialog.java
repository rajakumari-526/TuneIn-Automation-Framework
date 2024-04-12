package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class AlarmPlayTimeDialog extends BasePage {

    protected SelenideElement alarmPlayTimeTitle = $(androidUIAutomator("text(\"Alarm play time\")")).as("Time title");

    protected SelenideElement alarmPlayTimeAlarmHours = $x("(//*[@resource-id='android:id/numberpicker_input'])[1]").as("Time alarm hours");

    protected SelenideElement alarmPlayTimeAlarmMinutes = $x("(//*[@resource-id='android:id/numberpicker_input'])[2]").as("Time Alarm minutes");

    protected SelenideElement alarmTimeSaveButton = $(androidUIAutomator("text(\"SAVE\")")).as("Save button");

    protected SelenideElement alarmTimeCancelButton = $(androidUIAutomator("text(\"CANCEL\")")).as("Save button");

    @Step("Wait until Alarm PlayTime Dialog is ready")
    @Override
    public AlarmPlayTimeDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(alarmPlayTimeTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    public int getCurrentMinutes() {
        return Integer.parseInt(getElementText(alarmPlayTimeAlarmMinutes));
    }

    public int getCurrentHours() {
        return Integer.parseInt(getElementText(alarmPlayTimeAlarmHours));
    }

    public SelenideElement getAlarmTimeAlarmHours() {
        return alarmPlayTimeAlarmHours;
    }

    public SelenideElement getAlarmTimeAlarmMinutes() {
        return alarmPlayTimeAlarmMinutes;
    }

    @Step("Click on save button")
    public void tapOnSave() {
        clickOnElement(alarmTimeSaveButton);
    }
}
