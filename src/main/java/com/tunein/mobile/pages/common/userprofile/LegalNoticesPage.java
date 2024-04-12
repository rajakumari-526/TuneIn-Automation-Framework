package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class LegalNoticesPage extends BasePage {

    protected SelenideElement legalNoticePageTitle = $(android(xpath("//*[contains(@text,'licenses')]"))
            .ios(iOSNsPredicateString("label CONTAINS 'Copyright (c) 2010 – 2011'"))).as("Legal notice page title");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public LegalNoticesPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(legalNoticePageTitle, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public void validateThatLegalNoticesPageIsOpened() {
        assertThat(isElementDisplayed(legalNoticePageTitle)).as("Legal Notice page failed to download").isTrue();
    }
}
