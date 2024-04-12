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

public abstract class PodcastsPage extends BasePage {

    protected SelenideElement podcastsSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Podcasts\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Podcasts') and contains(@name,'_selected')]"))).as("Podcasts selected tab");

    protected ElementsCollection podcastsContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("Podcasts page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public PodcastsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(podcastsSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(podcastsContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate Podcasts Page is Opened")
    public PodcastsPage validatePodcastsPageIsOpen() {
        assertThat(isOnPodcastsPage()).as("Podcast Page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnPodcastsPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(podcastsSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

}
