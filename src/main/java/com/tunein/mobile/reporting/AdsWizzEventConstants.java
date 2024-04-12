package com.tunein.mobile.reporting;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class AdsWizzEventConstants {

    public static final String ADS_WIZZ_EVENT_REPORT_DOMAIN_ANDROID = "tunein.deliveryengine.adswizz.com";

    public static final String ADS_WIZZ_EVENT_REPORT_DOMAIN = "deliveryengine.tunein.adswizz.com";

    public static final String ADS_WIZZ_EVENT_REPORT_DOMAIN_IOS = "tunein.adswizz.com";

    // ADS_WIZZ params
    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ADS_PARTNER_ALIAS = "aw_0_1st.ads_partner_alias";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_SESSION_ID = "aw_0_1st.sessionid";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_CLASS = "aw_0_1st.class";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_GENRE_ID = "aw_0_1st.genre_id";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_LOTAMESEGMENTS = "aw_0_1st.lotamesegments";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PLAYER_ID = "aw_0_1st.playerId";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_STATION_ID = "aw_0_1st.stationId";

    public static final String ADS_WIZZ_REQUEST_PARAM_PARTNER_ID = "partnerId";

    public static final String ADS_WIZZ_REQUEST_PARAM_IS_EVENT = "is_event";

    public static final String ADS_WIZZ_REQUEST_PARAM_IS_FAMILY = "is_family";

    public static final String ADS_WIZZ_REQUEST_PARAM_IS_MATURE = "is_mature";

    public static final String ADS_WIZZ_REQUEST_PARAM_IS_NEW_USER = "is_new_user";

    public static final String ADS_WIZZ_REQUEST_PARAM_IS_ON_DEMAND = "is_ondemand";

    public static final String ADS_WIZZ_REQUEST_PARAM_LANGUAGE = "language";

    public static final String ADS_WIZZ_REQUEST_PARAM_STATION_LANGUAGE = "station_language";

    public static final String ADS_WIZZ_REQUEST_PARAM_LISTING_ID = "ListingId";

    public static final String ADS_WIZZ_REQUEST_PARAM_PERSONA = "persona";

    public static final String ADS_WIZZ_REQUEST_PARAM_SCREEN = "screen";

    public static final String ADS_WIZZ_REQUEST_PARAM_VERSION = "version";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENTV2 = "aw_0_req.userConsentV2";

    public static final String ADS_WIZZ_REQUEST_PARAM_GDPR = "aw_0_req.gdpr";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_BUNDLEID = "aw_0_1st.bundleId";

    public static final String ADS_WIZZ_REQUEST_PARAMETER_AW_0_REQ_BUNDLEID = "aw_0_req.bundleId";

    public static final String ADS_WIZZ_REQUEST_PARAM_US_PRIVACY = "us_privacy";

    public static final String ADS_WIZZ_REQUEST_PARAM_SKEY = "aw_0_1st.skey";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_ZONEID = "aw_0_1st.zoneid";

    public static final String ADS_WIZZ_REQUEST_PARAM_LISTENERID = "aw_0_awz.listenerid";

    public static final String ADS_WIZZ_REQUEST_PARAMETER_IS_EVENT_AW_0_1ST_COMPANIONZONES = "aw_0_1st.companionzones";

    public static final String ADS_WIZZ_REQUEST_PARAMETER_IS_EVENT_AW_0_REQ_APPLEAPPID = "aw_0_req.appleAppId";

    public static final String ADS_WIZZ_REQUEST_PARAM_CATEGORY_ID = "categoryId";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_1ST_PREMIUM = "aw_0_1st.premium";

    public static final String ADS_PARAMETER_IS_EVENT_COMPANIONADS = "companionAds";

    public static final String ADS_WIZZ_REQUEST_PARAM_AW_0_REQ_USER_CONSENT_V2 = "aw_0_req.userConsentV2";

    public static final String ADS_WIZZ_REQUEST_PARAM_PLATFORM = "aw_0_1st.platform";

    public static final String ADS_WIZZ_COMPANION_BANNER_START = "start";

    public static final String ADS_WIZZ_COMPANION_BANNER_FIRST = "first";

    public static final String ADS_WIZZ_COMPANION_BANNER_MIDPOINT = "midpoint";

    public static final String ADS_WIZZ_COMPANION_BANNER_THIRD = "third";

    public static final String ADS_WIZZ_COMPANION_BANNER_COMPLETE = "complete";

    // ADS_WIZZ Values
    public static final String AW_0_1ST_ADS_PARTNER_ALIAS = isAndroid() ? "mob.AndroidFree" : "mob.iOSFree";

    public static final String BUNDLEID = isAndroid() ? "tunein.player" : "418987775";

    public static final String TUNEIN = "tunein";

    public static final String COMPANIONADS = "true";

    public static final String ONE_YNY = "1YNY";

    public static final String ONE_YYY = "1YYY";

    public static final String NOWPLAYING = isAndroid() ? "nowplaying" : "NowPlaying";

    public static final String EN = isAndroid() ? "en_US" : "en";

    public static final String ENGLISH = "English";

    public static final String SPANISH = "Spanish";

}
