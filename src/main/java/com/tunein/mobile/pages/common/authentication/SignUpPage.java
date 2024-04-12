package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.userprofile.PrivacyPolicyPage;
import com.tunein.mobile.pages.common.userprofile.TermsOfServicePage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.utils.ElementHelper.*;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class SignUpPage extends BasePage {

    protected static final String INVALID_PASSWORD_ERROR = "Your password must be between 8 and 32 characters long, and must contain letters and either numbers or special characters.";

    protected static final String REGISTRATION_ERROR = "There was a problem with your registration. Please try back another time.";
    
    protected SelenideElement signUpBackButton = $(android(id("header_back"))
            .ios(iOSNsPredicateString("label == 'Back'"))).as("Back button");
    
    protected SelenideElement signUpTitle = $(android(id("sign_up_title"))
            .ios(id("signUpTitleId"))).as("Title");
    
    protected SelenideElement signUpEmailField = $(android(id("emailAddress"))
            .ios(id("regwallEmailFieldId"))).as("Email field");
    
    protected SelenideElement signUpPasswordField = $(android(id("password"))
            .ios(id("regwallPasswordFieldId"))).as("Password field");

    protected SelenideElement signUpBirthYearField = $(android(id("birthYear"))
            .ios(id("signUpYearFieldId"))).as("Birth year field");

    protected SelenideElement signUpDevicesImage = $(android(id("reg_wall_logo"))
            .ios(iOSNsPredicateString("label contains 'Devices'"))).as("Device image");

    protected SelenideElement signUpMaleRadioButton = $(android(id("maleRadioButton"))
            .ios(iOSClassChain("**/XCUIElementTypeButton[`name == 'signUpMaleRadioButtonId'`]"))).as("Male radio button");

    protected SelenideElement signUpFemaleRadioButton = $(android(id("femaleRadioButton"))
            .ios(iOSClassChain("**/XCUIElementTypeButton[`name == 'signUpFemaleRadioButtonId'`]"))).as("Female radio button");

    protected SelenideElement signUpNonBinaryRadioButton = $(android(id("nonBinaryRadioButton"))
            .ios(iOSClassChain("**/XCUIElementTypeButton[`name == 'signUpNonBinaryRadioButtonId'`]"))).as("Non-binary radio button");

    protected SelenideElement signUpPreferNonToSayRadioButton = $(android(id("preferNotToSayRadioButton"))
            .ios(iOSClassChain("**/XCUIElementTypeButton[`name == 'signUpUnspecifiedRadioButtonId'`]"))).as("Prefer not to say radio button");

    protected SelenideElement signUpCreateAccountButton = $(android(id("next"))
            .ios(accessibilityId("regwallSignUpActionButtonId"))).as("Create account button");

    protected SelenideElement signUpTermsOfServiceButton = $(android(id("fragment_reg_wall_terms_and_privacy"))
            .ios(iOSNsPredicateString("name == 'regwallTermsOfServiceId'"))).as("Terms of Service button");

    protected SelenideElement signUpPrivacyPolicyButton = $(android(id("fragment_reg_wall_tos_privacy"))
            .ios(id("regwallPrivacyPolicyId"))).as("Privacy Policy button");

    protected SelenideElement signUpTermsDescriptionText = $(android(id("fragment_reg_wall_creating_account"))
            .ios(id("regwallTermsDescriptionId"))).as("Terms Description text");

    protected SelenideElement signUpEmailErrorLabel = $(android(id("reg_wall_email_error_label"))
            .ios(id("regwallEmailErrorId"))).as("Email error label");

    /* --- Loadable Component Method --- */

    public abstract SignUpPage waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract void signUpFlowForUser(Users user);

    public abstract UserProfilePage signUpUserByEmail(Users user);

    public abstract SignUpPage fullFillSignUpFieldsWithData(Users user, boolean... onlyRequiredFields);

    public abstract SignUpPage pressNextButton();

    @Step
    public SignUpPage typeEmail(String email) {
        deviceNativeActions.typeText(signUpEmailField, email);
        return this;
    }

    @Step
    public SignUpPage typePassword(String password) {
        deviceNativeActions.typeText(signUpPasswordField, password);
        return this;
    }

    @Step
    public SignUpPage typeBirthYear(int birthYear) {
        deviceNativeActions.typeText(signUpBirthYearField, String.valueOf(birthYear));
        return this;
    }

    public abstract void tapOnCreateYourAccountButton();

    public abstract SignUpPage chooseGenderType(UserGenderType genderType);

    public abstract SignUpPage typeName(String userName);

    public abstract BasePage tapBackButton();

    @Step
    public TermsOfServicePage tapOnTermsOfServiceLink() {
        clickOnElement(signUpTermsOfServiceButton);
        ReporterUtil.log("Opening \"Terms Of Service\" page");
        return termsOfServicePage.waitUntilPageReady();
    }

    @Step
    public PrivacyPolicyPage tapOnPrivacyPolicyLink() {
        clickOnElement(signUpPrivacyPolicyButton);
        ReporterUtil.log("Opening \"Privacy Policy\" page");
        return privacyPolicyPage.waitUntilPageReady();
    }

    public abstract SignUpPage clearFieldsAndValidatePlaceholderTextIsDisplayed();

    /* --- Validation Methods --- */

    public abstract SignUpPage validateNextButtonIsDisabled();

    public abstract SignUpPage validateInvalidEmailErrorText();

    public abstract SignUpPage validateErrorSignUpExistingEmailIsDisplayed();

    public abstract SignUpPage validateThatRegistrationErrorIsDisplayed();

    @Step
    public SignUpPage validateThatSignUpPageIsDisplayed() {
        assertThat(isElementDisplayed(signUpCreateAccountButton)).as("Create Your Account button is not displayed").isTrue();
        ReporterUtil.log("Displayed \"SignUp\" page");
        return this;
    }

    @Step
    public SignUpPage validateThatCreateYourAccountButtonIsDisabled() {
        assertThat(isElementEnabled(signUpCreateAccountButton)).as("Create Your Account button is enabled").isFalse();
        return this;
    }

    @Step
    public SignUpPage validateCreateYourAccountButtonIsNotDisplayed() {
        assertThat(isElementNotDisplayed(signUpCreateAccountButton)).as("Create Your Account button is displayed").isTrue();
        return this;
    }

    public abstract SignUpPage validatePasswordIsNotCorrectAccordingToRules();

    public abstract SignUpPage validatePasswordRequirementsImageAreGrey();

    public abstract SignUpPage validateErrorNameTooLongIsDisplayed();
    
    public abstract SignUpPage validateThatInvalidBirthYearErrorIsDisplayed();

    public abstract SignUpPage validateInvalidPasswordErrorText();

    public abstract SignUpPage validatePlaceholderTextIsNotDisplayedForFields();

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> signUpPageElements();

}
