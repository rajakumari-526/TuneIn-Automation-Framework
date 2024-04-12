package com.tunein.mobile.pages.android.subscription;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;

import org.openqa.selenium.By;
import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitVisibilityOfElement;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.switchToAppContext;
import static com.tunein.mobile.utils.WebViewUtil.switchToWebViewWithUrl;

public class AndroidAlexaUpsellPage extends AlexaUpsellPage {

    protected SelenideElement alexaWebView = $x("//android.webkit.WebView[@text !='']").as("Webview");

    // AndroidFindBy() is buggy with WebViews
    protected SelenideElement alexaRadioButtonMonthly = $(By.cssSelector("[data-testid = radioButtonMonthly]")).as("Radio button monthly");

    protected SelenideElement alexaRadioButtonYearly = $(By.cssSelector("[data-testid = radioButtonYearly]")).as("Radio button yearly");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public AlexaUpsellPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitVisibilityOfElement(alexaWebView, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        switchToWebViewWithUrl(UPSELL_URL_PART);
        waitTillVisibilityOfElement(alexaStartFreeTrial, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    @Override
    public void tapNoThanksButton() {
        switchToWebViewWithUrl(UPSELL_URL_PART);
        clickOnElement(alexaNoThanks);
        switchToAppContext(NATIVE_APP);
    }

    /* --- Helper Methods --- */

    @Step
    @Override
    public boolean isOnAlexaUpsellPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(alexaStartFreeTrial, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
    }

}
