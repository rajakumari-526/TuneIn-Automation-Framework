package com.tunein.mobile.reporting;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class GAMEventConstants {

    public static final String INSTREAM_GAM_DOMAIN = "ad-hls.tunein.com";

    //GAM Params

    public static final String GAM_REQUEST_JSON_ADS_PARAMS = "adsParams";

    public static final String GAM_REQUEST_JSON_CUST_PARAMS = "cust_params";

    public static final String GAM_REQUEST_LISTENER_ID = "listenerId";

    public static final String GAM_REQUEST_GENDER = "gender";

    public static final String GAM_REQUEST_AGE = "age";

    public static final String GAM_REQUEST_VERSION = "version";

    public static final String GAM_REQUEST_PARTNER_ID = "partnerId";

    public static final String GAM_REQUEST_ADS_PARTNER_ALIAS = "ads_partner_alias";

    public static final String GAM_REQUEST_PREMIUM = "premium";

    public static final String GAM_REQUEST_LANGUAGE = "language";

    public static final String GAM_REQUEST_STATION_LANGUAGE = "station_language";

    public static final String GAM_REQUEST_STATION_ID = "stationId";

    public static final String GAM_REQUEST_PROGRAM_ID = "programId";

    public static final String GAM_REQUEST_GENRE_ID = "genre_id";

    public static final String GAM_REQUEST_CLASS = "class";

    public static final String GAM_REQUEST_IS_MATURE = "is_mature";

    public static final String GAM_REQUEST_IS_FAMILY = "is_family";

    public static final String GAM_REQUEST_IS_EVENT = "is_event";

    public static final String GAM_REQUEST_IS_ON_DEMAND = "is_ondemand";

    public static final String GAM_REQUEST_IS_NEW_USER = "is_new_user";

    public static final String GAM_REQUEST_ABTEST = "abtest";

    public static final String GAM_REQUEST_ENV = "env";

    public static final String GAM_REQUEST_LOTAMESEGMENTS = "lotamesegments";

    public static final String GAM_REQUEST_ENABLE_DOUBLE_PREROLL = "enableDoublePreroll";

    public static final String GAM_REQUEST_PPID = "ppid";

    public static final String GAM_REQUEST_CIU_SZS = "ciu_szs";

    public static final String GAM_REQUEST_RDID = "rdid";

    public static final String GAM_REQUEST_ID_TYPE = "idtype";

    public static final String GAM_REQUEST_IS_LAT = "is_lat";

    public static final String GAM_REQUEST_PALN = "paln";

    public static final String GAM_REQUEST_BUNDLE_ID = "bundleId";

    public static final String GAM_REQUEST_DESCRIPTION_URL = "description_url";

    public static final String GAM_REQUEST_HL = "hl";

    public static final String GAM_REQUEST_CUST_PARAMS = "cust_params";

    public static final String GAM_REQUEST_URL = "url";

    public static final String GAM_REQUEST_PARAM_NPA = "npa";

    public static final String GAM_REQUEST_PARAM_RDP = "rdp";

    public static final String GAM_REQUEST_PARAM_GDPR = "gdpr";

    public static final String GAM_REQUEST_PARAM_GDPR_CONSENT = "gdpr_consent";

    public static final String GAM_REQUEST_PARAM_US_PRIVACY = "us_privacy";

    //GAM Values

    public static final String ADS_PARTNER_ALIAS_VALUE = isAndroid() ? "mob.AndroidFree" : "mob.iOSFree";

    public static final String EN = "en";

    public static final String GAM_CUI_SZS_300_250 = "300x250";

    public static final String GAM_ID_TYPE = isAndroid() ? "adid" : "idfa";

    public static final String GAM_BUNDLE_ID = isAndroid() ? "tunein.player" : "418987775";

    public static final String ONE_YNY = "1YNY";

    public static final String ONE_YYY = "1YYY";

}
