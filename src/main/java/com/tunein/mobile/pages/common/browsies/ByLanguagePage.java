package com.tunein.mobile.pages.common.browsies;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import io.appium.java_client.pagefactory.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.openqa.selenium.By.xpath;

public class ByLanguagePage extends BasePage {

    protected SelenideElement byLanguageSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"By Language\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'By Language') and contains(@name,'_selected')]"))).as("By language selected tab");

    protected ElementsCollection byLanguageContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.widget.TextView)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("By language page content");


    /* --- Loadable Component Method --- */

    @Step
    public ByLanguagePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(byLanguageSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(byLanguageContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }
}
