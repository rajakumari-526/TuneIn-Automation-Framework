package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.models.Users;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.By.xpath;

public abstract class FacebookAuthenticationPage extends BasePage {

    protected SelenideElement facebookAuthEnglishLocalisationLabel = $(android(androidUIAutomator("text(\"English (US)\""))
            .ios(iOSNsPredicateString("name == 'English (US)' AND type == 'XCUIElementTypeStaticText'"))).as("English localisation label");

    protected SelenideElement facebookAuthTitle = $(android(xpath("//*[@content-desc='facebook'] | //*[@resource-id='m-future-page-header-title']"))
            .ios(iOSNsPredicateString("label IN {'Log in to your Facebook account to connect to TuneIn', 'Log in With Facebook', 'Log into your Facebook account to connect to TuneIn'}"))).as("Title on facebook auth page");

    protected SelenideElement facebookAuthEmailTextField = $(android(xpath("//android.widget.EditText[@resource-id='m_login_email']"))
            .ios(iOSNsPredicateString("value == 'Mobile number or email' and type == 'XCUIElementTypeTextField'"))).as("Email text filed");

    protected SelenideElement facebookAuthPasswordTextField = $(android(xpath("//android.widget.EditText[@resource-id='m_login_password']"))
            .ios(iOSNsPredicateString("value == 'Facebook Password' and type == 'XCUIElementTypeSecureTextField'"))).as("Password text field");

    protected SelenideElement facebookAuthLogInButton = $(android(androidUIAutomator("text(\"Log in\")"))
            .ios(iOSNsPredicateString("name IN {'Log In', 'Log in'} and type == 'XCUIElementTypeButton'"))).as("Login button");

    protected SelenideElement facebookAuthContinueButton = $(android(androidUIAutomator("text(\"Continue\")"))
            .ios(iOSNsPredicateString("name == 'Continue'"))).as("Continue button");

    protected SelenideElement facebookAuthCancelWebViewButton = $(android(androidUIAutomator("text(\"Cancel\")"))
            .ios(xpath("//*[@name='Continue']/../following-sibling::*//*[@name='Cancel']"))).as("Cancel button");

    /* --- Loadable Component Method --- */

    public abstract FacebookAuthenticationPage waitUntilPageReady();

    /* --- Action Methods --- */

    @Step
    protected FacebookAuthenticationPage typeEmail(String email) {
        deviceNativeActions.typeText(facebookAuthEmailTextField, email);
        return this;
    }

    @Step
    protected FacebookAuthenticationPage typePassword(String password) {
        deviceNativeActions.typeText(facebookAuthPasswordTextField, password);
        return this;
    }

    @Step
    protected FacebookAuthenticationPage tapLogInButton() {
        clickOnElement(facebookAuthLogInButton);
        return this;
    }

    @Step
    protected FacebookAuthenticationPage tapContinueButton() {
        clickOnElement(facebookAuthContinueButton);
        return this;
    }

    public abstract UserProfilePage logInToFacebook(Users user);

    /* --- Helper Methods --- */

}
