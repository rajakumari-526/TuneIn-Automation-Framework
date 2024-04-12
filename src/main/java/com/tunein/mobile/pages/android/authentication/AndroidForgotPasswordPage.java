package com.tunein.mobile.pages.android.authentication;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.authentication.ForgotPasswordPage;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$x;

public class AndroidForgotPasswordPage extends ForgotPasswordPage {

    protected SelenideElement forgotPasswordTitle = $x("//*[@text='Forgot password?']").as("Forgot Pasword title");

    @Override
    public HashMap<String, SelenideElement> forgotPasswordPageElements() {
        return new HashMap<>() {{
            put("Email Address", forgotSignInEmailField);
            put("Submit", resetPasswordButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + "1", resetPasswordBackButton);
            put("Forgot Password?", forgotPasswordTitle);
            put("Enter the email address or username you used to sign up with and we'll send you a link to reset your password.", forgotPasswordDescriptionLabel);
        }};
    }
}
