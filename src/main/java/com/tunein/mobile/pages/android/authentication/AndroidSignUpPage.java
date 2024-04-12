package com.tunein.mobile.pages.android.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.authentication.SignUpPage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.testdata.dataprovider.UserProvider;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SIGNUP_FORM;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.UserGenderType.getUserGenderEnumType;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidSignUpPage extends SignUpPage {

    private static final String INVALID_EMAIL_ERROR = "Invalid email";

    private static final String REGISTRATION_NAME_TOO_LONG_MESSAGE = "Registration field name is too long";

    private static final String EMAIL_PLACEHOLDER = "Email";

    private static final String PASSWORD_PLACEHOLDER = "Password";

    private static final String NAME_PLACEHOLDER = "First and Last Name";

    private static final String BIRTHYEAR_PLACEHOLDER = "Birth Year (YYYY)";

    protected SelenideElement signUpFirstNameField = $(id("name")).as("Firstname field");

    @Step
    @Override
    public SignUpPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(signUpEmailField, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    @Override
    public UserProfilePage signUpUserByEmail(Users user) {
        typeName(user.getProfileName());
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        typeBirthYear(user.getUserBirthYear());
        chooseGenderType(getUserGenderEnumType(user.getUserGender()));
        tapOnCreateYourAccountButton();
        return userProfilePage.waitUntilPageReadySignInUser();
    }

    @Step("Populating user credentials {user} into sign-up form page")
    @Override
    public SignUpPage fullFillSignUpFieldsWithData(Users user, boolean... onlyRequiredFields) {
        boolean updateRequiredFieldsOnly = (onlyRequiredFields.length > 0 && onlyRequiredFields[0]);
        typeName(user.getProfileName());
        typeEmail(user.getEmail());
        typePassword(user.getPassword());
        typeBirthYear(user.getUserBirthYear());
        if (!updateRequiredFieldsOnly) {
            chooseGenderType(getUserGenderEnumType(user.getUserGender()));
        }
        return signUpPage.waitUntilPageReady();
    }

    @Step
    @Override
    public RegWallPage tapBackButton() {
        clickOnElement(signUpBackButton);
        return regWallPage.waitUntilPageReady();
    }

    @Step
    @Override
    public SignUpPage pressNextButton() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step("Tap on create your account button")
    @Override
    public void tapOnCreateYourAccountButton() {
        ReporterUtil.log("tap on \"Create Your Account\" button");
        clickOnElement(signUpCreateAccountButton);
        termsOfServiceDialog.closeTermsOfServiceDialogIfPresent();
    }

    @Step
    @Override
    public SignUpPage chooseGenderType(UserProvider.UserGenderType genderType) {
        switch (genderType) {
            case MALE -> clickOnElement(signUpMaleRadioButton);
            case FEMALE -> clickOnElement(signUpFemaleRadioButton);
            case NON_BINARY -> clickOnElement(signUpNonBinaryRadioButton);
            case PREFER_NOT_TO_SAY -> clickOnElement(signUpPreferNonToSayRadioButton);
            default -> throw new Error(genderType + " radio button is not displayed in SignUp page");
        }
        return this;
    }

    @Step
    @Override
    public SignUpPage typeName(String userName) {
        deviceNativeActions.typeText(signUpFirstNameField, userName);
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> signUpPageElements() {
        return new HashMap<>() {{
            put("", signUpBackButton);
            put("Sign Up.", signUpTitle);
            put("First and Last Name", signUpFirstNameField);
            put("Email", signUpEmailField);
            put("Password", signUpPasswordField);
            put("Birth Year (YYYY)", signUpBirthYearField);
            put("Male", signUpMaleRadioButton);
            put("Female", signUpFemaleRadioButton);
            put("Non-Binary", signUpNonBinaryRadioButton);
            put("Prefer Not to Say", signUpPreferNonToSayRadioButton);
            put("CREATE YOUR ACCOUNT", signUpCreateAccountButton);
            put("Privacy Policy", signUpPrivacyPolicyButton);
            put("Terms of Service", signUpTermsOfServiceButton);
            put("By creating an account you agree to our updated", signUpTermsDescriptionText);
        }};
    }

    @Override
    public SignUpPage validatePasswordIsNotCorrectAccordingToRules() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    /* --- Validation Methods --- */

    @Step
    @Override
    public SignUpPage validateNextButtonIsDisabled() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public SignUpPage validateInvalidEmailErrorText() {
        assertThat(getElementText(signUpEmailErrorLabel))
                .as("'Invalid Email' error message failed to display in signup page")
                .isEqualTo(INVALID_EMAIL_ERROR);
        return this;
    }

    @Override
    public SignUpPage validatePasswordRequirementsImageAreGrey() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step("Validate that 'Invalid email' error is displayed ")
    @Override
    public SignUpPage validateErrorSignUpExistingEmailIsDisplayed() {
        String displayedMessage = alert.getToastMessage();
        assertThat(displayedMessage)
                .as(INVALID_EMAIL_ERROR + " toast message is not displayed." + displayedMessage + " is displayed instead of " + INVALID_EMAIL_ERROR)
                .isEqualTo(INVALID_EMAIL_ERROR);
      return this;
    }

    @Override
    public SignUpPage validateThatInvalidBirthYearErrorIsDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public SignUpPage validateThatRegistrationErrorIsDisplayed() {
        String displayedMessage = alert.getToastMessage();
        assertThat(displayedMessage)
                .as(displayedMessage + " is displayed instead of " + REGISTRATION_ERROR)
                .isEqualTo(REGISTRATION_ERROR);
        return this;
    }

    @Step
    @Override
    public SignUpPage validateErrorNameTooLongIsDisplayed() {
        String displayedMessage = alert.getToastMessage();
        assertThat(displayedMessage)
                .as(displayedMessage + " is displayed instead of " + REGISTRATION_NAME_TOO_LONG_MESSAGE)
                .isEqualTo(REGISTRATION_NAME_TOO_LONG_MESSAGE);
        return this;
    }

    @Step
    @Override
    public SignUpPage validateInvalidPasswordErrorText() {
        String displayedMessage = alert.getToastMessage();
        assertThat(displayedMessage)
                .as(displayedMessage + " is displayed instead of " + INVALID_PASSWORD_ERROR)
                .isEqualTo(INVALID_PASSWORD_ERROR);
        return this;
    }

    @Step
    @Override
    public SignUpPage validatePlaceholderTextIsNotDisplayedForFields() {
        getSoftAssertion().assertThat(getElementText(signUpFirstNameField)).as("'First and Last name' text is displayed after entering Name in SignUp page").isNotEqualTo(NAME_PLACEHOLDER);
        getSoftAssertion().assertThat(getElementText(signUpEmailField)).as("'Email' text is displayed after entering Email in SignUp page").isNotEqualTo(EMAIL_PLACEHOLDER);
        getSoftAssertion().assertThat(getElementText(signUpPasswordField)).as("'Password' text is displayed after entering password in SignUp page").isNotEqualTo(PASSWORD_PLACEHOLDER);
        getSoftAssertion().assertThat(getElementText(signUpBirthYearField)).as("'Birth Year (YYYY)' text is displayed after entering birth year in SignUp page").isNotEqualTo(BIRTHYEAR_PLACEHOLDER);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public SignUpPage clearFieldsAndValidatePlaceholderTextIsDisplayed() {
        deviceNativeActions.clearText(signUpFirstNameField);
        getSoftAssertion().assertThat(getElementText(signUpFirstNameField)).as("'First and Last name' text is not displayed after clearing Name in SignUp page").isEqualTo(NAME_PLACEHOLDER);
        deviceNativeActions.clearText(signUpEmailField);
        getSoftAssertion().assertThat(getElementText(signUpEmailField)).as("'Email' text is not displayed after clearing Email in SignUp page").isEqualTo(EMAIL_PLACEHOLDER);
        deviceNativeActions.clearText(signUpPasswordField);
        getSoftAssertion().assertThat(getElementText(signUpPasswordField)).as("'Password' text is not displayed after clearing password in SignUp page").isEqualTo(PASSWORD_PLACEHOLDER);
        deviceNativeActions.clearText(signUpBirthYearField);
        getSoftAssertion().assertThat(getElementText(signUpBirthYearField)).as("'Birth Year (YYYY)' text is displayed after clearing birth year in SignUp page").isEqualTo(BIRTHYEAR_PLACEHOLDER);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public void signUpFlowForUser(Users user) {
        navigationAction.navigateTo(SIGNUP_FORM);
        fullFillSignUpFieldsWithData(user);
        tapOnCreateYourAccountButton();
        userProfilePage
                .waitUntilPageReadySignInUser()
                .closeProfilePage();
    }
}
