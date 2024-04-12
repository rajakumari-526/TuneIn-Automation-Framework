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

public abstract class ByLocationPage extends BasePage {

    protected SelenideElement byLocationSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"By Location\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'By Location') and contains(@name,'_selected')]"))).as("By Location selected tab");

    protected ElementsCollection byLocationContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.widget.TextView)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("By Location page content");


    /* --- Loadable Component Method --- */

    @Step
    public ByLocationPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(byLocationSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(byLocationContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

}
