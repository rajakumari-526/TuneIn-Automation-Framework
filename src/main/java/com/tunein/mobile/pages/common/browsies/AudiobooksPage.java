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
import static io.appium.java_client.AppiumBy.*;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class AudiobooksPage extends BasePage {

    protected SelenideElement audiobooksSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Audiobooks\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Audiobooks') and contains(@name,'_selected')]"))).as("Audiobook selected tab");

    protected ElementsCollection audiobooksPageContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("Audiobook page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public AudiobooksPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(audiobooksSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(audiobooksPageContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

}
