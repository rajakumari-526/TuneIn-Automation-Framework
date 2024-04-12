package com.tunein.mobile.pages.common.browsies;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.BasePage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;

public abstract class RadioPage extends BasePage {

    protected SelenideElement explorerLogo = $(android(id("logo"))
            .ios(iOSNsPredicateString("name == \"explorer-logo\""))).as("Explorer logo");

    /* --- Loadable Component Method --- */

    public abstract RadioPage waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract void closeRadioPage();

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    public abstract boolean isRadioPageOpened();

}
