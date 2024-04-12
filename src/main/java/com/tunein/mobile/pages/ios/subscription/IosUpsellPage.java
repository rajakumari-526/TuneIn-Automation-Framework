package com.tunein.mobile.pages.ios.subscription;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.isLaunchArgumentKeysSet;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class IosUpsellPage extends UpsellPage {

    protected SelenideElement appleSignInPrompt = $(iOSNsPredicateString("name == \"Sign in with Apple ID\" AND type == \"XCUIElementTypeAlert\"")).as("Apple sign-in prompt");

    protected SelenideElement appleSignInCancelButton = $(id("Cancel")).as("ACancel button");

    @Step
    @Override
    public UpsellPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(upsellStartListeningButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    @Override
    public void tapCloseButton() {
        clickOnElement(upsellCloseButton);
    }

    @Step
    @Override
    public UpsellPage tapOnStartListeningFreeButton() {
        clickOnElement(upsellStartListeningButton);
        return this;
    }

    @Override
    public void closeUpsellInWebViewIfDisplayed() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
    }

    @Step
    @Override
    public void closeUpsellIfDisplayed() {
        if (isLaunchArgumentKeysSet(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true")) {
            closePermissionPopupsIfDisplayed();
            clickOnElementIfDisplayed(upsellCloseButton);
        }
    }

    @Step("Closing Upsell")
    @Override
    public void closeUpsell() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(upsellCloseButton);
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public UpsellPage validateAppleSignInPrompt() {
        assertThat(isElementDisplayed(appleSignInPrompt)).as("Apple sign in prompt is not displayed").isTrue();
        clickOnElement(appleSignInCancelButton);
        return this;
    }

    /* --- Helper Methods --- */

    @Override
    public HashMap<String, SelenideElement> upsellPageElements() {
        return new HashMap<>() {{
            put(SKIP_TEXT_VALIDATION_PREFIX + "Close", upsellCloseButton);
            put("Start listening", upsellStartListeningButton);
            put("Terms of Service", upsellTosLink);
            put("Privacy Policy", upsellPrivacyLink);
        }};
    }

    @Override
    public boolean isOnUpsellPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(upsellStartListeningButton, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
    }

}
