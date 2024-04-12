package com.tunein.mobile.pages.common.contentprofile;

import com.codeborne.selenide.ElementsCollection;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AffiliatesPage extends BasePage {

    /* --- UI Elements (Affiliates) --- */

    protected ElementsCollection affiliatesContentsList = $$(android(id("row_square_cell_container"))
            .ios(iOSNsPredicateString("type == 'XCUIElementTypeCell' AND accessible == true"))).as("Affiliates content list");

    /* --- Loadable Component Method --- */

    @Step("Wait until Affiliates page is ready")
    @Override
    public AffiliatesPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero(affiliatesContentsList, Duration.ofMillis(config().waitExtraLongTimeoutMilliseconds()));
        return this;
    }

    @Step("Validating Affiliates content")
    public AffiliatesPage validateThatAffiliatesPageIsOpenedWithName(String affiliatesName) {
        assertThat(affiliatesPage.getNavigationTitleText())
                .as("Affiliates page title doesn't match expected \"" + affiliatesName + "\" title")
                .isEqualTo(affiliatesName.replace(" >", ""));
        return this;
    }
}
