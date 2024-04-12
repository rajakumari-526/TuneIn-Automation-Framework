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

public class IheartRadioPage extends BasePage {

    protected SelenideElement iHeartRadioSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"iHeart\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'iHeart') and contains(@name,'_selected')]"))).as("iHeart selected tab");

    protected ElementsCollection iHeartRadioContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("iHeart page content");

    /* --- Loadable Component Method --- */

    @Step
    public IheartRadioPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(iHeartRadioSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(iHeartRadioContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

}
