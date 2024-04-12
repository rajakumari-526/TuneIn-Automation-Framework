package com.tunein.mobile.pages.alert;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class AndroidAlert extends Alert {

    protected SelenideElement toastMessage = $x("//android.widget.Toast[1]").as("Toast message");

    @Override
    protected By getAlertTitleByWithText(String text) {
         return androidUIAutomator(String.format("text(\"%s\")", text));
    }

    @Override
    protected SelenideElement getAlertButtonByText(String text) {
       return $(androidUIAutomator(String.format("text(\"%s\")", text))).as("Button with text " + text);
    }

    @Override
    public String getToastMessage() {
        String displayedToastMessage = toastMessage.getAttribute("name");
        ReporterUtil.log("The displayed toast message is : " + displayedToastMessage);
        return displayedToastMessage;
    }

}
