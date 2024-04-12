package com.tunein.mobile.reporting.authentication;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class SignInReporting {

    public static final String REPORT_SIGN_IN_COMPLETE = isAndroid() ? "signup|login|complete" : "regwall|screen|success.vmSignupAction";

    public static final String REPORT_SETTINGS_TAP_SIGN_IN = isAndroid() ? "settings|tap|signIn" : "regwall|screen|showAttempt...";

    public static final String REPORT_LOGIN_START = isAndroid() ? "signup|login|start" : "regwall|email.signIn|step1.vmSignupAction";

    public static final String REPORT_REGWALL_SCREEN_STEP0 = isAndroid() ? "signup|screen|step0" : "regwall|screen|step0.vmSignupAction";

    public static final String REPORT_REGWALL_SCREEN_TERMS = "regwall|screen|terms.vmSignupAction";

    public static final String REPORT_REGWALL_SCREEN_PRIVACY = "regwall|screen|privacy.vmSignupAction";

    public static final String REPORT_REGWALL_SKIP_COMPLETE = isAndroid() ? "signup|skip|complete" : "regwall|screen|dismiss.vmSignupAction";

}
