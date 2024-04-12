package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.xpath;

public class AlarmTimeDialog extends BasePage {

    protected SelenideElement alarmTimeTitle = $(android(androidUIAutomator("text(\"Alarm time\")"))).as("Time title");

    protected SelenideElement alarmTimeAlarmHours = $(android(xpath("(//*[@resource-id='android:id/numberpicker_input'])[1]"))).as("Alarm hours");

    protected SelenideElement alarmTimeAlarmMinutes = $(android(xpath("(//*[@resource-id='android:id/numberpicker_input'])[2]"))).as("Alarm minutes");

    protected SelenideElement alarmTimeTimeConvention = $(android(xpath("(//*[@resource-id='android:id/numberpicker_input'])[3]"))).as("Time convention");

    protected SelenideElement alarmTimeSaveButton = $(android(androidUIAutomator("text(\"SAVE\")"))).as("Save button");

    protected SelenideElement alarmTimeCancelButton = $(android(xpath("text(\"CANCEL\")"))).as("Canvel button");

    @Step("Wait until Alarm Time Dialog is ready")
    @Override
    public AlarmTimeDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(alarmTimeTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    public int getCurrentMinutes() {
        return Integer.parseInt(getElementText(alarmTimeAlarmMinutes));
    }

    public int getCurrentHours() {
        return Integer.parseInt(getElementText(alarmTimeAlarmHours));
    }

    public String getCurrentTimeConvention() {
        return getElementText(alarmTimeTimeConvention);
    }

    public SelenideElement getAlarmTimeAlarmHours() {
        return alarmTimeAlarmHours;
    }

    public SelenideElement getAlarmTimeAlarmMinutes() {
        return alarmTimeAlarmMinutes;
    }

    public SelenideElement getAlarmTimeTimeConvention() {
        return alarmTimeTimeConvention;
    }

    @Step
    public void tapOnSave() {
        clickOnElement(alarmTimeSaveButton);
    }

}
