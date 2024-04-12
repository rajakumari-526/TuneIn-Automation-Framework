package com.tunein.mobile.listeners.reportportal;

import com.epam.reportportal.testng.ReportPortalTestNGListener;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class ListenersTestNG extends ReportPortalTestNGListener {

    @Override
    public void onExecutionStart() {
        super.onExecutionStart();
    }

    @Override
    public void onExecutionFinish() {
        super.onExecutionFinish();
    }

    @Override
    public void onStart(ISuite suite) {
        super.onStart(suite);
    }

    @Override
    public void onFinish(ISuite suite) {
        super.onFinish(suite);
    }

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        System.out.println("Start test: " + testResult.getName());
        super.onTestStart(testResult);
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        super.onTestSuccess(testResult);
        if (config().screenshotForPassedTests()) takeScreenshot(testResult);
    }

    @Override
    public void onTestFailure(ITestResult testResult) {
        takeScreenshot(testResult);
        ReporterUtil.log("onTestFailure Method " + testResult.getName());
        if (isIos()) {
            List<String> buttons = getAlertButtons();
            buttons.stream().forEach(alert -> {
                if (buttons.contains("Ask App Not to Track")) {
                    dismissDisplayedPermissionPopup();
                } else {
                    acceptDisplayedPermissionPopup();
                }
            });
        } else if (isAndroid()) {
            clickOnElementIfDisplayed($(androidUIAutomator("text(\"Close app\")")), Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()), false);
        }
        super.onTestFailure(testResult);
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        takeScreenshot(testResult);
        ReporterUtil.log("onTestSkipped Method " + testResult.getName());
        ReporterUtil.log("Caused by " + testResult.getSkipCausedBy());
        ReporterUtil.log("Exception " + testResult.getThrowable());
        if (isAndroid()) {
            clickOnElementIfDisplayed($(androidUIAutomator("text(\"Close app\")")), Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()), false);
        }
        super.onTestSkipped(testResult);
    }

}
