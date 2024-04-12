package com.tunein.mobile.pages.alert;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.clickOnElement;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GeneralTestUtil.setIsStreamErrorPromptHandled;

public abstract class Alert {

    public static final String CONTINUE_BUTTON_TEXT = "Continue";

    public static final String SETTINGS_BUTTON_TEXT = "Settings";

    public static final String CANCEL_BUTTON_TEXT = "Cancel";

    public static final String OPEN_BUTTON_TEXT = "Open";

    public static final String OK_BUTTON_TEXT = "OK";

    public static final String CLOSE_BUTTON_TEXT = "Close";

    protected boolean isAlertWithTitleDisplayed(String title) {
        return isElementDisplayed(getAlertTitleByWithText(title), Duration.ofSeconds(config().waitShortTimeoutSeconds()));
    }

    protected abstract By getAlertTitleByWithText(String text);

    protected abstract SelenideElement getAlertButtonByText(String text);

    @Step("Handle alert {alertTitleText} if displayed")
    public void handleAlertIfDisplayed(String alertTitleText, String alertRequiredButtonText) {
        long start = System.currentTimeMillis();
        while (!isAlertWithTitleDisplayed(alertTitleText)) {
            if (System.currentTimeMillis() - start > config().waitShortTimeoutMilliseconds()) {
                ReporterUtil.log("Condition not met within" + config().waitShortTimeoutMilliseconds() + " ms ");
                return;
            }
        }
        clickOnElement(getAlertButtonByText(alertRequiredButtonText));
        setIsStreamErrorPromptHandled(true);
    }

    @Step("Handle alert {alertTitleText} if displayed")
    public void handleAlertIfDisplayed(String alertTitleText, String alertRequiredButtonText, Duration duration) {
        long start = System.currentTimeMillis();
        while (!isAlertWithTitleDisplayed(alertTitleText)) {
            if (System.currentTimeMillis() - start > duration.toMillis()) {
                ReporterUtil.log("Condition not met within" + config().waitShortTimeoutMilliseconds() + " ms ");
                return;
            }
        }
        clickOnElement(getAlertButtonByText(alertRequiredButtonText));
    }

    @Step
    public boolean isAlertWithTitleDisplayed(String alertTitleText, int timeoutInMilliseconds) {
        long start = System.currentTimeMillis();
        while (!isAlertWithTitleDisplayed(alertTitleText)) {
            if (System.currentTimeMillis() - start > timeoutInMilliseconds) {
                ReporterUtil.log("Condition not met within" + timeoutInMilliseconds + " ms ");
                return false;
            }
        }
        return true;
    }

    public abstract String getToastMessage();
}
