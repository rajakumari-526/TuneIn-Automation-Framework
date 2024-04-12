package com.tunein.mobile.pages.ios.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.authentication.SignUpPage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.dataprovider.UserProvider;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNUP_FORM;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.getUserGenderEnumType;
import static com.tunein.mobile.utils.ElementHelper.ElementSide.LEFT;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public class IosSignUpPage extends SignUpPage {

    protected SelenideElement signUpFirstStepLabel = $(id("regwallSignUpStep1TextId")).as("First step label");

    protected SelenideElement signUpSecondStepLabel = $(id("regwallSignUpStep2TextId")).as("Second step label");

    protected SelenideElement signUpCharactersRequirementsText = $(accessibilityId("8 characters and 1 number or special character")).as("Characters requirements text");

    protected SelenideElement signUpCharactersRequirementsImage = $(id("Requirements_empty")).as("Characters requirements image");

    protected SelenideElement signUpSpecialCharactersRequirementsGreyImage = $(iOSNsPredicateString("label == 'Requirements_empty'")).as("Characters requirements grey image");

    protected SelenideElement signUpMaleLabel = $(iOSNsPredicateString("name == 'signUpMaleRadioButtonId' AND type == 'XCUIElementTypeStaticText'")).as("Male label");

    protected SelenideElement signUpFemaleLabel = $(iOSNsPredicateString("name == 'signUpFemaleRadioButtonId' AND type == 'XCUIElementTypeStaticText'")).as("Female label");

    protected SelenideElement signUpNonBinaryLabel = $(iOSNsPredicateString("name == 'signUpNonBinaryRadioButtonId' AND type == 'XCUIElementTypeStaticText'")).as("Non-binary label");

    protected SelenideElement signUpPreferNonToSayLabel = $(iOSNsPredicateString("name == 'signUpUnspecifiedRadioButtonId' AND type == 'XCUIElementTypeStaticText'")).as("Prefere not to say label");

    protected SelenideElement signUpErrorWithExistingEmail = $(iOSNsPredicateString("label == \"Login failed\"")).as("Error with existing email");

    protected SelenideElement signUpErrorWithInvalidBirthYear = $(iOSNsPredicateString("label == \"There was a problem with your registration. Please try back another time.\"")).as("Error with invalid birth year");

    protected SelenideElement signUpNextButton = $(id("regwallSignUpButtonId")).as("Next button");

    protected SelenideElement signUpYearFieldError = $(id("signUpYearErrorId")).as("Year field error");

    private String invalidEmailError = "Please enter a valid email address.";

    private String errorSignUpWithExistingEmail = "Username or email already exists";

    private static final String INVALID_EMAIL_ERROR = "Please enter a valid email address.";

    @Step
    @Override
    public SignUpPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        if (isElementDisplayed(signUpFirstStepLabel)) {
            waitTillVisibilityOfElement(signUpNextButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        } else {
            waitTillVisibilityOfElement(signUpCreateAccountButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        }
        return this;
    }

    @Step
    @Override
    public BasePage tapBackButton() {
        clickOnElement(signUpBackButton);
        if (isFirstStepPageIsDisplayed()) {
            return signUpPage.waitUntilPageReady();
        }
        return regWallPage.waitUntilPageReady();
    }

    @Step
    @Override
    public SignUpPage pressNextButton() {
        clickOnElement(signUpNextButton);
        return signUpPage.waitUntilPageReady();
    }

    @Step
    @Override
    public UserProfilePage signUpUserByEmail(Users user) {
        if (isSecondStepPageIsDisplayed()) {
            tapBackButton();
        }
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        pressNextButton();
        typeBirthYear(user.getUserBirthYear());
        chooseGenderType(getUserGenderEnumType(user.getUserGender()));
        tapOnCreateYourAccountButton();
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        return userProfilePage.waitUntilPageReadySignInUser();
    }

    @Step("Populating user credentials {user} into sign-up form page")
    @Override
    public SignUpPage fullFillSignUpFieldsWithData(Users user, boolean... onlyRequiredFields) {
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        if (!user.getEmail().isEmpty() && !user.getPassword().isEmpty() && isElementNotDisplayed(signUpEmailErrorLabel)) {
            pressNextButton();
            typeBirthYear(user.getUserBirthYear());
            chooseGenderType(getUserGenderEnumType(user.getUserGender()));
        }
        return this;
    }

    @Step("Tap on create your account button")
    @Override
    public void tapOnCreateYourAccountButton() {
        ReporterUtil.log("tap on \"Create Your Account\" button");
        clickOnElement(signUpCreateAccountButton);
    }

    @Step
    @Override
    public SignUpPage chooseGenderType(UserProvider.UserGenderType genderType) {
        switch (genderType) {
            case MALE -> clickOnElement(signUpMaleRadioButton, LEFT);
            case FEMALE -> clickOnElement(signUpFemaleRadioButton, LEFT);
            case NON_BINARY -> clickOnElement(signUpNonBinaryRadioButton, LEFT);
            case PREFER_NOT_TO_SAY -> clickOnElement(signUpPreferNonToSayRadioButton, LEFT);
            default -> throw new Error(genderType + " radio button is not displayed in SignUp page");
        }
        return this;
    }

    @Override
    public SignUpPage typeName(String userName) {
        throw new UnsupportedOperationException("Functionality not supported in Ios platform");
    }

    private boolean isFirstStepPageIsDisplayed() {
        return isElementDisplayed(signUpFirstStepLabel);
    }

    private boolean isSecondStepPageIsDisplayed() {
        return isElementDisplayed(signUpSecondStepLabel);
    }

    @Override
    public HashMap<String, SelenideElement> signUpPageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put("Back", signUpBackButton);
        elementsMap.put("By creating an account you agree to our updated", signUpTermsDescriptionText);
        elementsMap.put("Privacy Policy", signUpPrivacyPolicyButton);
        elementsMap.put("Terms of Service", signUpTermsOfServiceButton);

        if (isFirstStepPageIsDisplayed()) {
            elementsMap.put("Step 1 of 2", signUpFirstStepLabel);
            elementsMap.put("Sign up.", signUpTitle);
            elementsMap.put("Email address", signUpEmailField);
            elementsMap.put("Password", signUpPasswordField);
            elementsMap.put("8 characters and 1 number or special character", signUpCharactersRequirementsText);
            elementsMap.put("NEXT", signUpNextButton);
        } else if (isSecondStepPageIsDisplayed()) {
            elementsMap.put("Step 2 of 2", signUpSecondStepLabel);
            elementsMap.put("Birth Year (YYYY)", signUpBirthYearField);
            elementsMap.put("Male", signUpMaleRadioButton);
            elementsMap.put("Female", signUpFemaleRadioButton);
            elementsMap.put("Non-binary", signUpNonBinaryRadioButton);
            elementsMap.put("Prefer not to say", signUpPreferNonToSayRadioButton);
            elementsMap.put("CREATE YOUR ACCOUNT", signUpCreateAccountButton);
        }
        return elementsMap;
    }

    @Override
    public SignUpPage validatePlaceholderTextIsNotDisplayedForFields() {
        throw new UnsupportedOperationException("Functionality is absent for IOS platform");
    }

    @Override
    public SignUpPage validateInvalidPasswordErrorText() {
        throw new UnsupportedOperationException("Functionality is absent for IOS platform");
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public SignUpPage validateNextButtonIsDisabled() {
        assertThat(isElementEnabled(signUpNextButton)).as("SignUp Next button is Enabled").isFalse();
        return this;
    }

    @Step
    @Override
    public SignUpPage validateInvalidEmailErrorText() {
        assertThat(getElementText(signUpEmailErrorLabel))
                .as("'Invalid Email' error message failed to display in signup page")
                .isEqualTo(invalidEmailError);
        return this;
    }

    @Step
    @Override
    public SignUpPage validatePasswordRequirementsImageAreGrey() {
        assertThat(getElementText(signUpCharactersRequirementsImage)).as("Password requirements image is not grey").contains("empty");
        return this;
    }

    @Step("Validate that 'Login failed' error is displayed ")
    @Override
    public SignUpPage validateErrorSignUpExistingEmailIsDisplayed() {
        assertThat(isElementDisplayed(signUpErrorWithExistingEmail))
                .as("'Username or email already exists' error message failed to display in signup page")
                .isTrue();
        return this;
    }

    @Step
    @Override
    public SignUpPage validateThatRegistrationErrorIsDisplayed() {
        assertThat(isElementDisplayed(signUpErrorWithInvalidBirthYear))
                .as("'There was a problem with your registration. Please try back another time.' error message failed to display in signup page")
                .isTrue();
        alert.handleAlertIfDisplayed(REGISTRATION_ERROR, OK_BUTTON_TEXT);
        return this;
    }

    @Override
    public SignUpPage clearFieldsAndValidatePlaceholderTextIsDisplayed() {
        throw new UnsupportedOperationException("Functionality not supported in Ios platform");
    }

    @Step
    @Override
    public SignUpPage validateThatInvalidBirthYearErrorIsDisplayed() {
        assertThat(isElementDisplayed(signUpYearFieldError))
                .as("'Please enter a valid 4 digit birth year in a YYYY format.' error message failed to display in signup page")
                .isTrue();
        return this;
    }

    @Override
    public SignUpPage validateErrorNameTooLongIsDisplayed() {
        assertThat(alert.isAlertWithTitleDisplayed(INVALID_PASSWORD_ERROR, config().waitVeryShortTimeoutMilliseconds()))
                .as("Error \"Password too long\" is not displayed")
                .isTrue();
        alert.handleAlertIfDisplayed(INVALID_PASSWORD_ERROR, OK_BUTTON_TEXT);
        return this;
    }

    @Step
    @Override
    public SignUpPage validatePasswordIsNotCorrectAccordingToRules() {
        assertThat(getElementText(signUpSpecialCharactersRequirementsGreyImage)).as("Password characters requirements image is not green").contains("empty");
        return this;
    }

    @Step
    @Override
    public void signUpFlowForUser(Users user) {
        navigationAction.navigateTo(SIGNUP_FORM);
        fullFillSignUpFieldsWithData(user);
        tapOnCreateYourAccountButton();
        signInSignUpSuccessPage
                .waitUntilPageReady()
                .tapOnFinishButton();
        userProfilePage
                .waitUntilPageReadySignInUser()
                .closeProfilePage();
    }
}
