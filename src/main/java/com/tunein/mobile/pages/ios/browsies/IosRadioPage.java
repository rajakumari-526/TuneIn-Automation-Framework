package com.tunein.mobile.pages.ios.browsies;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.browsies.RadioPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementEnabled;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosRadioPage extends RadioPage {

    protected SelenideElement radioPageLogo = $(iOSNsPredicateString("name == 'explorer-logo'")).as("Page logo");

    protected SelenideElement radioPageSearchField = $(iOSNsPredicateString("type == 'XCUIElementTypeSearchField' AND visible == true")).as("Search field");

    protected SelenideElement radioPageCloseButton = $(iOSNsPredicateString("label == 'chevron down'")).as("Close button");

    @Step
    @Override
    public RadioPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(radioPageSearchField, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    @Step
    public void closeRadioPage() {
        clickOnElement(radioPageCloseButton);
    }

    public boolean isRadioPageOpened() {
        closePermissionPopupsIfDisplayed();
        return isElementEnabled(radioPageLogo);
    }

}
