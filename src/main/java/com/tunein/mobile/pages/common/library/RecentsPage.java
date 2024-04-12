package com.tunein.mobile.pages.common.library;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public abstract class RecentsPage extends BasePage {

    protected SelenideElement libraryRecentLabel = $(android(androidUIAutomator("text('RECENTS')"))
            .ios(iOSNsPredicateString("name == 'RECENTS' AND type == 'XCUIElementTypeOther'"))).as("Recents label");

    protected SelenideElement recentHeader = $(android(id("tunein.player:id/prompt_button"))
            .ios(iOSNsPredicateString("name == 'TuneIn_Radio.ListTableView'"))).as("Recents header");

    /* --- Loadable Component Method --- */

    @Step
    public RecentsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(recentHeader, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public static ElementsCollection getItemsUnderRecentsCategory() {
        String locator = isAndroid() ? "//*[@resource-id='tunein.player:id/row_square_cell_title']" : "//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeOther[@visible='true']";
        return $$(By.xpath(String.format(locator)));
    }

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

}
