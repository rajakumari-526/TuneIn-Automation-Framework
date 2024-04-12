package com.tunein.mobile.pages.ios.authentication;

import com.codeborne.selenide.SelenideElement;
import com.tunein.mobile.pages.common.authentication.ForgotPasswordPage;

import java.util.HashMap;

public class IosForgotPasswordPage extends ForgotPasswordPage {

    @Override
    public HashMap<String, SelenideElement> forgotPasswordPageElements() {
        return new HashMap<>() {{
            put("Email Address", forgotSignInEmailField);
            put("Reset Password", resetPasswordButton);
            put(SKIP_TEXT_VALIDATION_PREFIX + "1", resetPasswordBackButton);
            put("Enter your email and we will send a link to reset your password.", forgotPasswordDescriptionLabel);
        }};
    }
}
