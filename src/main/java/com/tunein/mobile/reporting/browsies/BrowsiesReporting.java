package com.tunein.mobile.reporting.browsies;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public abstract class BrowsiesReporting {

    public static final String REPORT_BROWSIES_FOR_YOU = "feature|browsies.tap|home";

    public static final String REPORT_BROWSIES_LOCAL_RADIO = "feature|browsies.tap|local";

    public static final String REPORT_BROWSIES_SPORTS = "feature|browsies.tap|c424726";

    public static final String REPORT_BROWSIES_I_HEART_RADIO = isAndroid() ? "feature|browsies.tap|c100005513" : "feature|browsies.tap|c100003326";

    public static final String REPORT_BROWSIES_NEWS_AND_TALK = "feature|browsies.tap|c57922";

    public static final String REPORT_BROWSIES_PODCASTS = "feature|browsies.tap|c100000088";

    public static final String REPORT_BROWSIES_MUSIC = "feature|browsies.tap|c424724";

    public static final String REPORT_BROWSIES_BY_LANGUAGE = "feature|browsies.tap|languages";

    public static final String REPORT_BROWSIES_BY_LOCATION = "feature|browsies.tap|regions";

}
