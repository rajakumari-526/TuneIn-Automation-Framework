package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.By.xpath;

public class GoogleAuthenticationPage extends BasePage {

    protected SelenideElement googleAuthTitle = $(android(androidUIAutomator("text(\"Sign in\")"))
            .ios(iOSNsPredicateString("name == 'Sign in' AND type == 'XCUIElementTypeStaticText'"))).as("Google auth. title");

    protected SelenideElement googleAuthEmailField = $(android(xpath("//android.widget.EditText[@resource-id='identifierId']"))
            .ios(xpath("//android.widget.EditText[@resource-id='identifierId']"))).as("Email field");

    protected SelenideElement googleAuthCreateAccountButton = $(android(xpath("text(\"Create account\")"))
            .ios(iOSNsPredicateString("label == 'Create account"))).as("Create account button");

    protected SelenideElement googleAuthNextButton = $(android(xpath("//android.widget.Button[contains(@text,'Next')] |  //android.view.View[@resource-id='passwordNext']"))
            .ios(iOSNsPredicateString("label == 'Next'"))).as("Next button");

    protected SelenideElement googleAuthPasswordField = $(android(xpath("//android.view.View[@resource-id='password']//android.widget.EditText"))
            .ios(xpath("//XCUIElementTypeSecureTextField/following-sibling:: XCUIElementTypeOther[2]"))).as("Password field");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public GoogleAuthenticationPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(googleAuthTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    /* --- Helper Methods --- */

}
