package com.tunein.mobile.reporting.homepage;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class HomePageReporting {

    public static final String REPORT_HOMEPAGE_SUPER_PROMPT = isAndroid() ? "" : "feature|prompts.subscription|view";

    public static final String REPORT_SPORTS_INTEREST_SELECTION_SHOW = "feature|interestSelection|show";

    public static final String REPORT_SPORTS_INTEREST_SELECTION_DISMISS = "feature|interestSelection|dismiss";

    public static final String REPORT_SPORTS_INTEREST_SELECTION_SAVE = "feature|interestSelection|save";

}
