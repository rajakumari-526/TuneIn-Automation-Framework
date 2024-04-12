package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class HelpCenterWebPage extends BasePage {

    protected SelenideElement helpCenterTitle = $(android(androidUIAutomator("text(\"Help Center\")"))
            .ios(iOSNsPredicateString("label == 'TuneIn User Support' AND type  == 'XCUIElementTypeStaticText'"))).as("Help center title");

    @Override
    public HelpCenterWebPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(helpCenterTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

}
