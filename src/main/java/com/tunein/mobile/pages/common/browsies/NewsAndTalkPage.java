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
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class NewsAndTalkPage extends BasePage {

    protected SelenideElement newsAndTalkSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"News & Talk\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'News & Talk') and contains(@name,'_selected')]"))).as("News & Talk selected tab");

    protected ElementsCollection newsAndTalkContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("News & Talk page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NewsAndTalkPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(newsAndTalkSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(newsAndTalkContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate News & Talk Page is Opened")
    public NewsAndTalkPage validateNewsAndTalkPageIsOpen() {
        assertThat(isOnNewsAndTalkPage()).as("News & Talk Page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnNewsAndTalkPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(newsAndTalkSelectedTab, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }
}
