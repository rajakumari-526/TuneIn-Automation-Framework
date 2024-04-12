package com.tunein.mobile.pages.common.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public abstract class CustomUrlAddPage extends BasePage {

    protected SelenideElement customUrlTitle = $(android(androidUIAutomator("text(\"Custom URL\")"))
            .ios(iOSNsPredicateString("name == 'Add Custom URL'  AND type == 'XCUIElementTypeStaticText' AND visible == true"))).as("Custom url title");

    protected SelenideElement customUrlSearchField = $(android(id("add_custom_url_id"))
            .ios(id("customUrlSearchField"))).as("Search field");

    protected SelenideElement customUrlCancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("name == 'CANCEL' AND type == 'XCUIElementTypeButton'"))).as("Search field");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public CustomUrlAddPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(customUrlSearchField, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public CustomUrlAddPage typeCustomUrl(String customUrl) {
        deviceNativeActions.typeText(customUrlSearchField, customUrl, true);
        return this;
    }

    public abstract NowPlayingPage saveCustomURL();

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

}
