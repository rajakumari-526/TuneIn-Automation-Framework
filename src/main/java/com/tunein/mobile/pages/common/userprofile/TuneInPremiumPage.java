package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class TuneInPremiumPage extends BasePage {

    protected SelenideElement upgradeToPremiumButton = $(android(id("tunein.player:id/premiumBtn"))
            .ios(iOSNsPredicateString("name == \"UPGRADE TO PREMIUM\" AND type == \"XCUIElementTypeButton\""))).as("Upgrade to Premium button");

    protected SelenideElement linkWithAlexaButton = $(android(id("tunein.player:id/linkAlexaBtn"))
            .ios(iOSNsPredicateString("name == \"Link with Alexa\" AND type == \"XCUIElementTypeButton\""))).as("'Link with Alexa' button");


    /* --- Loadable Component Method --- */

    @Step
    @Override
    public TuneInPremiumPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(upgradeToPremiumButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract RegWallPage tapSignIntoTuneInAccount();

    @Step
    public UpsellPage tapUpgradeToPremiumButton() {
        clickOnElement(upgradeToPremiumButton);
        return upsellPage.waitUntilPageReady();
    }

    @Step
    public AlexaUpsellPage tapLinkWithAlexaButton() {
        clickOnElement(linkWithAlexaButton);
        return alexaUpsellPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public TuneInPremiumPage validateThatTuneInPremiumPageIsOpened() {
        assertThat(isOnTuneInPremiumPage()).as("TuneIn Premium Page is not opened").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnTuneInPremiumPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(upgradeToPremiumButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }
}
