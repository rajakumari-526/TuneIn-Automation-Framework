package com.tunein.mobile.pages.alert;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.utils.GeneralTestUtil.setIsStreamErrorPromptHandled;
import static com.tunein.mobile.utils.GeneralTestUtil.setStreamTestStatus;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosAlert extends Alert {

    private static HashMap<String, String> alertButtons;

    @Override
    protected By getAlertTitleByWithText(String text) {
        return iOSNsPredicateString(String.format("name CONTAINS '%s' AND type IN {'XCUIElementTypeStaticText', 'XCUIElementTypeTextView'}", text));
    }

    @Override
    protected SelenideElement getAlertButtonByText(String text) {
        return $(iOSNsPredicateString(String.format("label == '%s' AND type == 'XCUIElementTypeButton'", text))).as("Button with text " + text);
    }

    @Override
    public String getToastMessage() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    /* --- Alerts --- */

    /***
     * Get system alert buttons
     * @return list of systems alert button labels as List<String>
     */
    public static List<String> getAlertButtons() {
        List<String> alertButtonsList = new ArrayList<String>();
        alertButtons = new HashMap<String, String>();
        alertButtons.put("action", "getButtons");

        try {
            alertButtonsList = (List<String>) getAppiumDriver().executeScript("mobile: alert", alertButtons);
            return alertButtonsList;
        } catch (Throwable throwable) {
            ReporterUtil.log("No system alert detected");
        }
        return alertButtonsList;
    }

    /**
     * Dismisses system dialog on iOS
     * Reference: https://appiumpro.com/editions/31-automating-custom-alert-buttons-on-ios
     */
    @Step("Closing system alert prompt")
    public static void handleSimpleAlertIfPresent(ApplicationPermission button) {
        List<String> alertButtons = getAlertButtons();
        String buttonLabel = button.getPermissionButtonName();
        String message = "System Alert Prompt \"" + buttonLabel + (alertButtons.isEmpty() ? "\" was not detected" : "\" was detected");

        if (alertButtons.isEmpty()) {
            ReporterUtil.log(message);
            return;
        } else {
            ReporterUtil.log(message);
            setStreamTestStatus(false);
            setIsStreamErrorPromptHandled(true);
        }
        tapOnSystemPromptButton(buttonLabel);
    }

    /**
     * This method sends an action to interact with a system button
     * @param label system prompt button label
     */
    private static void tapOnSystemPromptButton(String label) {
        try {
            /* Send an action to interact with a button */
            alertButtons.put("action", "accept");
            alertButtons.put("buttonLabel", label);
            getAppiumDriver().executeScript("mobile: alert", alertButtons);
        } catch (Exception e) {
            ReporterUtil.log("System Alert Prompt \"" + label + "\" was not detected");
        }
    }
}
