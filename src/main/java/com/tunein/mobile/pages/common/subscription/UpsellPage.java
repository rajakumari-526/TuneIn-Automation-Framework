package com.tunein.mobile.pages.common.subscription;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.ScreenOrientation;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.openqa.selenium.By.cssSelector;

public abstract class UpsellPage extends BasePage {

    protected static final String UPSELL_URL_PART = "upsell";

    protected static final String EXPECTED_PRICE_LISTING = "7 days free, then $9.99/month";

    protected static final String EXPECTED_PRICE1_LISTING = "Unlock everything with TuneIn Premium for $9.99/month or $99.99/annually. Cancel anytime!";

    protected static final String EXPECTED_PRICE2_LISTING = "Try risk-free for 7 days then $9.99/month or 30 days free then $99.99/annually.";

    protected static final String EXPECTED_PRICE3_LISTING = "7 days free, then $9.99/month. Cancel anytime!";

    // AndroidFindBy() is buggy with WebViews

    protected SelenideElement upsellCloseButton = $(android(cssSelector("[data-testid = close]"))
            .ios(iOSNsPredicateString("label == 'Close Button' AND type == 'XCUIElementTypeImage'"))).as("Close button");

    protected SelenideElement upsellFullPriceContainer = $(android(cssSelector("[data-testid = priceContainer]"))
            .ios(iOSNsPredicateString("label CONTAINS 'Cancel anytime' OR label CONTAINS '/month'"))).as("Full price container");

    protected SelenideElement upsellStartListeningButton = $(android(cssSelector("[data-testid = submitButton]"))
            .ios(iOSNsPredicateString("name == 'START YOUR FREE TRIAL' OR name == 'GO PREMIUM' OR name == 'START LISTENING NOW' OR name == 'CLAIM YOUR DEAL' AND type == 'XCUIElementTypeLink'"))).as("Start listening button");

    protected SelenideElement upsellTosLink = $(android(cssSelector("[data-testid = policyLink]"))
            .ios(iOSNsPredicateString("name == 'Terms of Service' AND type == 'XCUIElementTypeLink'"))).as("Terms of Service button");

    protected SelenideElement upsellPrivacyLink = $(android(cssSelector("[data-testid = privacyLink]"))
            .ios(iOSNsPredicateString("name == 'Privacy Policy' AND type == 'XCUIElementTypeLink'"))).as("Privacy Policy button");

    /* --- Loadable Component Method --- */

    public abstract UpsellPage waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract void tapCloseButton();

    public abstract UpsellPage tapOnStartListeningFreeButton();

    //TODO Investigate: closeUpsellInWebViewIfDisplayed should work for real devices
    public abstract void closeUpsellInWebViewIfDisplayed();

    public abstract void closeUpsellIfDisplayed();

    /** This method ignore launch arguments */
    public abstract void closeUpsell();

    /* --- Validation Methods --- */

    public abstract UpsellPage validateAppleSignInPrompt();

    @Step
    public UpsellPage validateIsOnUpsellPage(boolean isUpsellExpected) {
        String errorMessage = (isUpsellExpected) ? "Upsell page is not open" : "Upsell page is open";
        assertThat(isOnUpsellPage()).as(errorMessage).isEqualTo(isUpsellExpected);
        return this;
    }

    @Step("Validate Upsell price")
    public UpsellPage validateUpsellPrice() {
        Assertions.assertThat(getElementText(upsellFullPriceContainer)).as("Validate upsell price")
                .containsAnyOf(EXPECTED_PRICE_LISTING, EXPECTED_PRICE1_LISTING, EXPECTED_PRICE2_LISTING, EXPECTED_PRICE3_LISTING);
        return this;
    }

    @Step
    public UpsellPage validateUpsellPriceIsNotEqualTo(String priceValue) {
        String secondPrice = getFullPriceString();
        assertThat(priceValue).as("Upsell price was not changed with region change").isNotEqualTo(secondPrice);
        return this;
    }

    @Step
    public UpsellPage validatePortraitOrientation() {
        assertThat(deviceNativeActions.getOrientationMode())
                .as("Upsell screen is not in portrait orientation")
                .isEqualTo(ScreenOrientation.PORTRAIT);
        return this;
    }

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> upsellPageElements();

    public abstract boolean isOnUpsellPage();

    public String getFullPriceString() {
        return getElementText(upsellFullPriceContainer);
    }

}
