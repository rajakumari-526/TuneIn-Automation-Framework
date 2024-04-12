package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.AlarmPlayTimeDialog;
import com.tunein.mobile.pages.dialog.common.AlarmTimeDialog;
import com.tunein.mobile.pages.dialog.common.NowPlayingSetAlarmDialog;
import io.appium.java_client.AppiumBy;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.isElementChecked;

public class AndroidNowPlayingSetAlarmDialog extends NowPlayingSetAlarmDialog {

    protected SelenideElement alarmTimeLabel = $(AppiumBy.androidUIAutomator("text(\"Alarm time\")")).as("Alarm time label");

    public SelenideElement alarmPlayTimeLabel = $(AppiumBy.androidUIAutomator("text(\"Alarm play time\")")).as("Play time label");

    @Step
    @Override
    public NowPlayingSetAlarmDialog setAlarmInCoupleMinutes(int... minutes) {
        tapOnAlarmTime();
        int times = (minutes.length > 0) ? minutes[0] : 0;
        int currentMinutes = alarmTimeDialog.getCurrentMinutes();
        int currentHours = alarmTimeDialog.getCurrentHours();
        String currentTimeConvention = alarmTimeDialog.getCurrentTimeConvention();
        if (currentMinutes + times >= SECONDS_IN_MINUTE) {
            setValueForPickerWheel(alarmTimeDialog.getAlarmTimeAlarmMinutes(), currentMinutes + times - SECONDS_IN_MINUTE + "");
            if (currentHours == HOURS_IN_TIME_CONVENTION) {
                setValueForPickerWheel(alarmTimeDialog.getAlarmTimeAlarmHours(), "1");
                if (currentTimeConvention.equals("PM")) {
                    setValueForPickerWheel(alarmTimeDialog.getAlarmTimeTimeConvention(), "AM");
                } else {
                    setValueForPickerWheel(alarmTimeDialog.getAlarmTimeTimeConvention(), "PM");
                }
            } else {
                setValueForPickerWheel(alarmTimeDialog.getAlarmTimeAlarmHours(), currentHours + 1 + "");
            }
        } else {
            setValueForPickerWheel(alarmTimeDialog.getAlarmTimeAlarmMinutes(), currentMinutes + times + "");
        }
        alarmTimeDialog.tapOnSave();
        return nowPlayingSetAlarmDialog.waitUntilPageReady();
    }

    @Step("Set duration for the alarm play time")
    @Override
    public NowPlayingSetAlarmDialog setAlarmPlayTimeDuration(int... minutes) {
        tapOnAlarmPlayTime();
        int times = (minutes.length > 0) ? minutes[0] : 0;
        int currentMinutes = alarmPlayTimeDialog.getCurrentMinutes();
        int currentHours = alarmPlayTimeDialog.getCurrentHours();
        if (currentMinutes + times >= SECONDS_IN_MINUTE) {
            setValueForPickerWheel(alarmTimeDialog.getAlarmTimeAlarmMinutes(), currentMinutes + times - SECONDS_IN_MINUTE + "");
            if (currentHours == HOURS_IN_TIME_CONVENTION) {
                setValueForPickerWheel(alarmPlayTimeDialog.getAlarmTimeAlarmHours(), "1");
            } else {
                setValueForPickerWheel(alarmPlayTimeDialog.getAlarmTimeAlarmHours(), currentHours + 1 + "");
            }
        } else {
            setValueForPickerWheel(alarmPlayTimeDialog.getAlarmTimeAlarmMinutes(), times + "");
        }
        alarmPlayTimeDialog.tapOnSave();
        return nowPlayingSetAlarmDialog.waitUntilPageReady();
    }

    @Override
    public boolean isAlarmEnabled() {
        return isElementChecked(nowPlayingSetAlarmSwitcher);
    }

    private AlarmTimeDialog tapOnAlarmTime() {
        clickOnElement(alarmTimeLabel);
        return alarmTimeDialog.waitUntilPageReady();
    }

    private AlarmPlayTimeDialog tapOnAlarmPlayTime() {
        clickOnElement(alarmPlayTimeLabel);
        return alarmPlayTimeDialog.waitUntilPageReady();
    }

}
