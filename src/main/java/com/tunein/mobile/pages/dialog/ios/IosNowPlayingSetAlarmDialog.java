package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.NowPlayingSetAlarmDialog;
import io.appium.java_client.AppiumBy;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.*;

public class IosNowPlayingSetAlarmDialog extends NowPlayingSetAlarmDialog {

    protected SelenideElement nowPlayingSetAlarmHours = $(AppiumBy.iOSNsPredicateString("value CONTAINS 'o’clock'")).as("Alarm hours");

    protected SelenideElement nowPlayingSetAlarmMinutes = $(AppiumBy.iOSNsPredicateString("value CONTAINS 'minutes'")).as("Alarm minutes");

    protected SelenideElement nowPlayingSetAlarTimeConvention = $(AppiumBy.iOSNsPredicateString("value IN {'PM', 'AM'}")).as("Alarm time convention");

    @Step
    @Override
    public NowPlayingSetAlarmDialog setAlarmInCoupleMinutes(int... minutes) {
        int times = (minutes.length > 0) ? minutes[0] : 0;
        int currentMinutes = getCurrentMinutes();
        int currentHours = getCurrentHours();
        String currentTimeConvention = getCurrentTimeConvention();
        if (currentMinutes + times >= SECONDS_IN_MINUTE) {
            setValueForPickerWheel(nowPlayingSetAlarmMinutes, currentMinutes + times - SECONDS_IN_MINUTE + "");
            if (currentHours == HOURS_IN_TIME_CONVENTION) {
                setValueForPickerWheel(nowPlayingSetAlarmHours, "1");
                if (currentTimeConvention.equals("PM")) {
                    setValueForPickerWheel(nowPlayingSetAlarTimeConvention, "AM");
                } else {
                    setValueForPickerWheel(nowPlayingSetAlarTimeConvention, "PM");
                }
            } else {
                setValueForPickerWheel(nowPlayingSetAlarmHours, currentHours + 1 + "");
            }
        } else {
            setValueForPickerWheel(nowPlayingSetAlarmMinutes, currentMinutes + times + "");
        }
        return this;
    }

    @Step("Set duration for the alarm play time")
    @Override
    public NowPlayingSetAlarmDialog setAlarmPlayTimeDuration(int... minutes) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    public boolean isAlarmEnabled() {
        return getElementValue(nowPlayingSetAlarmSwitcher).equals("1");
    }

    private int getCurrentMinutes() {
        return Integer.valueOf(getElementValue(nowPlayingSetAlarmMinutes).replace(" minutes", ""));
    }

    private int getCurrentHours() {
        return Integer.valueOf(getElementValue(nowPlayingSetAlarmHours).replace(" o’clock", ""));
    }

    private String getCurrentTimeConvention() {
        return getElementValue(nowPlayingSetAlarTimeConvention);
    }
}
