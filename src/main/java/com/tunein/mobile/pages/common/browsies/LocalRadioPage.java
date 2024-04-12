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
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.utils.ElementHelper.longPressOnElement;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.openqa.selenium.By.xpath;

public class LocalRadioPage extends BasePage {

    protected SelenideElement localRadioSelectedTab = $(android(androidUIAutomator("className(android.widget.TextView).textContains(\"Local Radio\").selected(true)"))
            .ios(xpath("//XCUIElementTypeButton[contains(@label,'Local Radio') and contains(@name,'_selected')]"))).as("Local Radio selected tab");

    protected ElementsCollection localRadioStations = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.widget.TextView)"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeCell"))).as("Local radio page content");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public LocalRadioPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(localRadioSelectedTab, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        waitUntilNumberOfElementsMoreThanZero(localRadioStations, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public void tapOnFirstStationInList(boolean... isLongPress) {
        if (isLongPress.length > 1 && isLongPress[0]) {
            longPressOnElement(localRadioStations.get(0));
            contentListItemDialog.waitUntilPageReady();
            return;
        }
        clickOnElement(localRadioStations.get(0));
        nowPlayingPage.waitUntilPageReadyWithKnownContentType(LIVE_STATION);
    }

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

}
