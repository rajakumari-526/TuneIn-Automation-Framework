package com.tunein.mobile.reporting;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class BannerEventConstants {

    public static final String MAX_BANNER_DOMAIN = "ms4.applovin.com";

    // Banner params

    public static final String MAX_BANNER_REQUEST_JSON_APP_INFO = "app_info";

    public static final String MAX_BANNER_REQUEST_JSON_LOCATION_INFO = "location_info";

    public static final String MAX_BANNER_REQUEST_JSON_DEVICE_INFO = "device_info";

    public static final String MAX_BANNER_REQUEST_JSON_AD_INFO = "ad_info";

    public static final String MAX_BANNER_REQUEST_JSON_TARGETING_DATA = "targeting_data";

    public static final String MAX_BANNER_REQUEST_JSON_ADAPTERS_INFO = "adapters_info";

    public static final String MAX_BANNER_REQUEST_JSON_KEYWORDS = "keywords";

    public static final String MAX_BANNER_REQUEST_JSON_USER_AGENT = "useragent";

    public static final String MAX_BANNER_REQUEST_JSON_PARTNER_ID = "partnerId";

    public static final String MAX_BANNER_REQUEST_JSON_ADS_PARTNER_ALIAS = "ads_partner_alias";

    public static final String MAX_BANNER_REQUEST_JSON_LISTING_ID = "ListingId";

    public static final String MAX_BANNER_REQUEST_JSON_GENRE_ID = "genre_id";

    public static final String MAX_BANNER_REQUEST_JSON_CLASS = "class";

    public static final String MAX_BANNER_REQUEST_JSON_STATION_ID = "stationId";

    public static final String MAX_BANNER_REQUEST_JSON_PROGRAM_ID = "programId";

    public static final String MAX_BANNER_REQUEST_JSON_IS_MATURE = "is_mature";

    public static final String MAX_BANNER_REQUEST_JSON_IS_FAMILY = "is_family";

    public static final String MAX_BANNER_REQUEST_JSON_IS_EVENT = "is_event";

    public static final String MAX_BANNER_REQUEST_JSON_IS_ON_DEMAND = "is_ondemand";

    public static final String MAX_BANNER_REQUEST_JSON_IS_NEW_USER = "is_new_user";

    public static final String MAX_BANNER_REQUEST_JSON_IS_FIRST_IN_SESSION = "isFirstInSession";

    public static final String MAX_BANNER_REQUEST_JSON_LANGUAGE = "language";

    public static final String MAX_BANNER_REQUEST_JSON_STATION_LANGUAGE = "station_language";

    public static final String MAX_BANNER_REQUEST_JSON_VERSION = "version";

    public static final String MAX_BANNER_REQUEST_JSON_SHOW_ID = "show_id";

    public static final String MAX_BANNER_REQUEST_JSON_PERSONA = "persona";

    public static final String MAX_BANNER_REQUEST_JSON_DEVICE = "device";

    public static final String MAX_BANNER_REQUEST_JSON_CATEGORY_ID = "categoryId";

    public static final String MAX_BANNER_REQUEST_JSON_PPID = "ppid";

    public static final String MAX_BANNER_REQUEST_JSON_SCREEN = "screen";

    public static final String MAX_BANNER_REQUEST_PARAM_SET_HAS_USER_CONSENT = "setHasUserConsent";

    public static final String MAX_BANNER_REQUEST_PARAM_NS_USER_DEFAULTS = "NSUserDefaults";

    public static final String MAX_BANNER_REQUEST_PARAM_GDPR = "gdpr";

    public static final String MAX_BANNER_REQUEST_PARAM_SET_DO_NOT_SELL = "setDoNotSell";

    public static final String MAX_BANNER_REQUEST_PARAM_US_PRIVACY = "us_privacy";

    public static final String MAX_BANNER_REUQEST_PARAM_SET_DO_NOT_SELL = "setDoNotSell";

    public static final String MAX_BANNER_REQUEST_JSON_LOC_SERVICES_ENABLED = "loc_services_enabled";

    public static final String MAX_BANNER_REQUEST_JSON_LOC_AUTH = "loc_auth";

    public static final String MAX_BANNER_REQUEST_JSON_IDFA = "idfa";

    public static final String MAX_BANNER_REQUEST_JSON_DEBUG = "debug";

    public static final String MAX_BANNER_REQUEST_JSON_TEST_ADS = "test_ads";

    public static final String MAX_BANNER_REQUEST_JSON_PACKAGE_NAME = "package_name";

    public static final String MAX_BANNER_REQUEST_JSON_MUTED = "muted";

    public static final String MAX_BANNER_REQUEST_JSON_RID = "rid";

    public static final String MAX_BANNER_REQUEST_SDK_KEY = "sdk_key";

    public static final String MAX_BANNER_REQUEST_MEDIATION_PROVIDER = "mediation_provider";

    public static final String MAX_BANNER_REQUEST_JSON_ADS = "Ads";

    public static final String MAX_BANNER_REQUEST_PARAM_N = "N";

    public static final String MAX_BANNER_REQUEST_PARAM_R = "R";

    public static final String MAX_BANNER_REQUEST_PARAM_U = "U";

    public static final String MAX_BANNER_REQUEST_PARAM_L = "L";

    public static final String MAX_BANNER_REQUEST_PARAM_I = "I";

    public static final String MAX_BANNER_REQUEST_PARAM_LISTEN_ID = "listenId";

    public static final String MAX_BANNER_REQUEST_LISTEN_ID = "listenId";

    //Banner Params Values

    public static final String MAX_BANNER_REQUEST_ADS_PARTNER_ALIAS = isAndroid() ? "mob.AndroidFree" : "mob.iOSFree";

    public static final String ENGLISH_VALUE = "English";

    public static final String EN_US_VALUE = isAndroid() ? "en_US" : "en";

    public static final String MAX_BANNER_REQUEST_NOW_PLAYING = isAndroid() ? "nowplaying" : "NowPlaying";

    public static final String MAX_BANNER_REQUEST_AD_UNIT_ID = "ad_unit_id";

    public static final String MAX_BANNER_REQUEST_AD_FORMAT = "ad_format";

    public static final String AD_FORMAT_VALUE = "MREC";

    public static final String TUNEIN_RADIO_PACKAGE = isAndroid() ? "tunein.player" : "com.tunein.TuneInRadio";

    public static final String ONE_YNY = "1YNY";

    public static final String ONE_YYY = "1YYY";

    public static final String MAX_BANNER_REQUEST_INITIALIZED_CLASS_NAMES = "initialized_classnames";

    public static final String MAX_BANNER_REQUEST_LOADED_CLASS_NAMES = "loaded_classnames";

    public static final String MAX_BANNER_REQUEST_FAILED_CLASS_NAMES = "failed_classnames";

    public static final String MAX_BANNER_AL_APP_LOVIN_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.AppLovinMediationAdapter" : "ALAppLovinMediationAdapter";

    public static final String MAX_BANNER_AL_GOOGLE_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.GoogleMediationAdapter" : "ALGoogleMediationAdapter";

    public static final String MAX_BANNER_AL_IN_MOBI_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.InMobiMediationAdapter" : "ALInMobiMediationAdapter";

    public static final String MAX_BANNER_AL_MOBILE_FUSE_MEDIATION_ADAPTER = "com.applovin.mediation.adapters.MobileFuseMediationAdapter";

    public static final String MAX_BANNER_AL_AMAZON_MP_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.AmazonAdMarketplaceMediationAdapter" : "ALAmazonAdMarketplaceMediationAdapter";

    public static final String MAX_BANNER_AL_GOOGLE_AD_MANAGER_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.GoogleAdManagerMediationAdapter" : "ALGoogleAdManagerMediationAdapter";

    public static final String MAX_BANNER_AL_VERVE_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.VerveMediationAdapter" : "ALVerveMediationAdapter";

    public static final String MAX_BANNER_AL_FB_MEDIATION_ADAPTER = "com.applovin.mediation.adapters.FacebookMediationAdapter";

    public static final String MAX_BANNER_VUNGLE_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.VungleMediationAdapter" : "ALVungleMediationAdapter";

    public static final String MAX_BANNER_AL_INNER_ACTIVE_MEDIATION_ADAPTER = isAndroid() ? "com.applovin.mediation.adapters.InneractiveMediationAdapter" : "ALInneractiveMediationAdapter";

    public static final String YES = "Yes";

    public static final String BANNER = "banner";

    public static final String MAX = "max";

    public static final String MAX_BANNER = "max_banner";

    public static final String IMA = "IMA";

}
