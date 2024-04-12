package com.tunein.mobile.pages.android.subscription;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.isLaunchArgumentKeysSet;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitVisibilityOfElement;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.WEBVIEW;
import static com.tunein.mobile.utils.WebViewUtil.*;

public class AndroidUpsellPage extends UpsellPage {

    // AndroidFindBy() is buggy with WebViews

    protected SelenideElement upsellWebView = $x("//android.webkit.WebView[(@text !='') and (not (//android.webkit.WebView[@text='Advertisement']))] | /html/body").as("Webview");

    // AndroidFindBy() is buggy with WebViews
    protected SelenideElement upsellMonthlyRadioButton = $(By.cssSelector("[data-testid = radioButtonMonthlyLabel]")).as("Radio button monthly");

    protected SelenideElement upsellYearlyRadioButton = $(By.cssSelector("[data-testid = radioButtonYearlyLabel]")).as("Radio button yearly");

    @Step
    @Override
    public UpsellPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitVisibilityOfElement(upsellWebView, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        switchToWebViewWithUrl(UPSELL_URL_PART);
        waitTillVisibilityOfElement(upsellStartListeningButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    public UpsellPage chooseMonthlySubscription() {
        switchToWebViewWithUrl(UPSELL_URL_PART);
        clickOnElement(upsellMonthlyRadioButton);
        return this;
    }

    @Step
    public UpsellPage chooseYearlySubscription() {
        switchToWebViewWithUrl(UPSELL_URL_PART);
        clickOnElement(upsellYearlyRadioButton);
        return this;
    }

    @Step
    @Override
    public void tapCloseButton() {
        switchToWebViewWithUrl(UPSELL_URL_PART);
        clickOnElement(upsellCloseButton);
        switchToAppContext(NATIVE_APP);
    }

    @Step
    @Override
    public UpsellPage tapOnStartListeningFreeButton() {
        switchToWebViewWithUrl(UPSELL_URL_PART);
        clickOnElement(upsellStartListeningButton);
        switchToAppContext(NATIVE_APP);
        return this;
    }

    @Step
    @Override
    public void closeUpsellInWebViewIfDisplayed() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(upsellWebView)) {
            switchToAppContext(WEBVIEW);
            try {
                long start = System.currentTimeMillis();
                while (getNumberOfWindowHandles() <= 0) {
                    if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                        throw new TimeoutException(String.format("Condition not met within %s ms ", config().waitCustomTimeoutMilliseconds()));
                    }
                }
                tapCloseButton();
            } catch (Exception e) {
            } finally {
                switchToAppContext(NATIVE_APP);
            }
        }
    }

    @Step
    @Override
    public void closeUpsellIfDisplayed() {
        if (config().testOnRealDevices() || isLaunchArgumentKeysSet(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true")) {
            closePermissionPopupsIfDisplayed();
            switchToAppContext(NATIVE_APP);
            if (isElementDisplayed(upsellWebView, Duration.ofSeconds(config().waitMediumTimeoutSeconds()))) {
                deviceNativeActions.clickBackButton();
            }
        }
    }

    @Step("Closing Upsell")
    @Override
    public void closeUpsell() {
        closePermissionPopupsIfDisplayed();
        switchToAppContext(NATIVE_APP);
        if (isElementDisplayed(upsellWebView, Duration.ofSeconds(config().waitMediumTimeoutSeconds()))) {
            deviceNativeActions.clickBackButton();
        }
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public UpsellPage validateAppleSignInPrompt() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    /* --- Helper Methods --- */

    @Override
    public HashMap<String, SelenideElement> upsellPageElements() {
        return new HashMap<>() {{
            put(SKIP_TEXT_VALIDATION_PREFIX + 1, upsellCloseButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + 2, upsellStartListeningButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + 3, upsellFullPriceContainer);
            put("Terms of Service", upsellTosLink);
            put("Privacy Policy", upsellPrivacyLink);
        }};
    }

    @Override
    public boolean isOnUpsellPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(upsellWebView, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }

}
