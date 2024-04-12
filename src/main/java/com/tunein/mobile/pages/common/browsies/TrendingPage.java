package com.tunein.mobile.pages.common.browsies;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.openqa.selenium.By.xpath;

public class TrendingPage extends BasePage {

    protected SelenideElement trendingSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Trending\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Trending') and contains(@name,'_selected')]"))).as("Trending selected tab");

    protected ElementsCollection trendingPageContent = $$(android(xpath("//*[@resource-id='tunein.player:id/view_model_list']//android.view.ViewGroup"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("Trending page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public TrendingPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(trendingSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(trendingPageContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }
}
