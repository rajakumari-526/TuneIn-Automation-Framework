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

public class MusicPage extends BasePage {

    protected SelenideElement musicSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Music\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Music') and contains(@name,'_selected')]"))).as("Music selected tab");

    protected ElementsCollection musicContent = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.view.ViewGroup)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeStaticText"))).as("Music page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public MusicPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(musicSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(musicContent, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate Music Page is Opened")
    public MusicPage validateMusicPageIsOpen() {
        assertThat(isOnMusicPage()).as("Music Page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnMusicPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(musicSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

}
