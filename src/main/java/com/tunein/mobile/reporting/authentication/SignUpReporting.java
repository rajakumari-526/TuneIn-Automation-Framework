package com.tunein.mobile.reporting.authentication;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class SignUpReporting {

    public static final String REPORT_REGWALL_CONTINUE_WITH_FACEBOOK = isAndroid() ? "signup|createFacebook|step1" : "regwall|facebook|step1.vmSignupAction";

    public static final String REPORT_REGWALL_CREATE_STEP1 = isAndroid() ? "signup|create|step1" : "regwall|email.signUp|step1.vmSignupAction";

    public static final String REPORT_REGWALL_SIGNUP_COMPLETE = isAndroid() ? "signup|create|complete" : "regwall|screen|success.vmSignupAction";

    public static final String REPORT_REGWALL_CONTINUE_WITH_GOOGLE = isAndroid() ? "signup|createGoogle|step1" : "regwall|google|step1.vmSignupAction";

    public static final String REPORT_REGWALL_CANCEL_WITH_FACEBOOK = isAndroid() ? "signup|createFacebook|canceled" : "regwall|facebook|canceled.vmSignupAction";

    public static final String REPORT_REGWALL_CANCEL_WITH_GOOGLE = isAndroid() ? "signup|createGoogle|canceled" : "regwall|google|canceled.vmSignupAction";

    public static final String REPORT_REGWALL_CANCEL_WITH_APPLE = "regwall|apple|canceled.vmSignupAction";

    public static final String REPORT_REGWALL_EMAIL_SIGN_UP_STEP1 = isAndroid() ? "signup|create|step1" : "regwall|email.signUp|step1.vmSignupAction";

    public static final String REPORT_REGWALL_EMAIL_SIGN_UP_STEP2 = "regwall|email.signUp|step2.vmSignupAction";

}
