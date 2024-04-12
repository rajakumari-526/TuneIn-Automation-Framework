package com.tunein.mobile.reporting;

import static com.tunein.mobile.pages.BasePage.isAndroid;

public class IMAEventConstants {

    public static final String IMA_EVENT_REPORT_DOMAIN_FIRST = "pubads.g.doubleclick.net";

    public static final String IMA_EVENT_REPORT_DOMAIN_SECOND = "securepubads.g.doubleclick.net";

    public static final String IMA_EVENT_REPORT_DOMAIN_THIRD = "googleads.g.doubleclick.net";

    public static final String MEDIA_ADMOB_COM = "media.admob.com";

    public static final String GOOGLE_TAG_SERVICES = "googletagservices.com";

    public static final String GOOGLE_SYNDICATION = "googlesyndication.com";

    public static final String G_DOUBLECLICK = "g.doubleclick.net";

    public static final String GOOGLE_AD_SERVICES = "googleadservices.com";

    public static final String IMA_EVENT_NOW_PLAYING_CALL = "feed.radiotime.com";

    // IMA params

    public static final String IMA_REQUEST_PARAM_AD_RULE = "ad_rule";

    public static final String IMA_REQUEST_PARAM_AD_TYPE = "ad_type";

    public static final String IMA_REQUEST_PARAM_CIU_SZS = "ciu_szs";

    public static final String IMA_REQUEST_PARAM_CORRELATOR = "correlator";

    public static final String IMA_REQUEST_PARAM_DESCRIPTION_URL = "description_url";

    public static final String IMA_REQUEST_PARAM_GDFP_REQ = "gdfp_req";

    public static final String IMA_REQUEST_PARAM_IU = "iu";

    public static final String IMA_REQUEST_PARAM_OUTPUT = "output";

    public static final String IMA_REQUEST_PARAM_PPID = "ppid";

    public static final String IMA_REQUEST_PARAM_RDID = "rdid";

    public static final String IMA_REQUEST_PARAM_ID_TYPE = "idtype";

    public static final String IMA_REQUEST_PARAM_IS_LAT = "is_lat";

    public static final String IMA_REQUEST_PARAM_SCOR = "scor";

    public static final String IMA_REQUEST_PARAM_SID = "sid";

    public static final String IMA_REQUEST_PARAM_SZ = "sz";

    public static final String IMA_REQUEST_PARAM_UNVIEWED_POSITION_START = "unviewed_position_start";

    public static final String IMA_REQUEST_PARAM_HL = "hl";

    public static final String IMA_REQUEST_PARAM_URL = "url";

    public static final String IMA_REQUEST_PARAM_RDP = "rdp";

    public static final String IMA_REQUEST_PARAM_GDPR = "gdpr";

    public static final String IMA_REQUEST_PARAM_NPA = "npa";

    public static final String IMA_REQUEST_PARAM_GDPR_CONSENT = "gdpr_consent";

    public static final String IMA_REQUEST_PARAM_US_PRIVACY = "us_privacy";

    public static final String IMA_EVENT_REQUEST_PARAM_ADS_PARTNER_ALIAS = "ads_partner_alias";

    public static final String IMA_EVENT_REQUEST_PARAM_AFFILIATE_IDS = isAndroid() ? "AffiliateIds" : "affiliateIds";

    public static final String IMA_EVENT_REQUEST_PARAM_CATEGORY_ID = "categoryId";

    public static final String IMA_EVENT_REQUEST_PARAM_CLASS = "class";

    public static final String IMA_EVENT_REQUEST_PARAM_GENRE_ID = "genre_id";

    public static final String IMA_EVENT_REQUEST_PARAM_ENV = "env";

    public static final String IMA_EVENT_REQUEST_PARAM_IS_EVENT = "is_event";

    public static final String IMA_EVENT_REQUEST_PARAM_IS_FAMILY = "is_family";

    public static final String IMA_EVENT_REQUEST_PARAM_IS_MATURE = "is_mature";

    public static final String IMA_EVENT_REQUEST_PARAM_IS_NEW_USER = "is_new_user";

    public static final String IMA_EVENT_REQUEST_PARAM_IS_ON_DEMAND = "is_ondemand";

    public static final String IMA_EVENT_REQUEST_PARAM_LANGUAGE = "language";

    public static final String IMA_EVENT_REQUEST_PARAM_LISTENER_ID = "listenerId";

    public static final String IMA_EVENT_REQUEST_PARAM_PROGRAM_ID = "programId";

    public static final String IMA_EVENT_REQUEST_PARAM_LISTING_ID = "ListingId";

    public static final String IMA_EVENT_REQUEST_PARAM_PARTNER_ID = "partnerId";

    public static final String IMA_EVENT_REQUEST_PARAM_PERSONA = "persona";

    public static final String IMA_EVENT_REQUEST_PARAM_PREMIUM = "premium";

    public static final String IMA_EVENT_REQUEST_PARAM_SCREEN = "screen";

    public static final String IMA_EVENT_REQUEST_PARAM_STATION_ID = "stationId";

    public static final String IMA_EVENT_REQUEST_PARAM_ENABLE_DOUBLE_PREROLL = "enableDoublePreroll";

    public static final String IMA_EVENT_REQUEST_PARAM_VERSION = "version";

    public static final String IMA_EVENT_REQUEST_STATION_LANGUAGE = "station_language";

    public static final String IMA_ADS_LABEL = "label";

    public static final String IMA_EVENT_REQUEST_PARAM_N = "N";

    public static final String IMA_EVENT_REQUEST_PARAM_VPOS = "vpos";

    public static final String IMA_EVENT_REQUEST_PARAM_R = "R";

    public static final String IMA_EVENT_REQUEST_PARAM_U = "U";

    public static final String IMA_EVENT_REQUEST_PARAM_L = "L";

    public static final String IMA_EVENT_REQUEST_PARAM_I = "I";

    public static final String IMA_EVENT_REQUEST_PARAM_F = "F";

    public static final String IMA_EVENT_REQUEST_PARAM_M = "M";

    public static final String IMA_EVENT_REQUEST_PARAM_LISTEN_ID = "listenId";

    public static final String IMA_EVENT_REQUEST_JSON_ADS = "Ads";

    public static final String IMA_EVENT_REQUEST_CAN_SHOW_VIDEO_PREROLL_ADS = "CanShowVideoPrerollAds";

    public static final String IMA_EVENT_REQUEST_CAN_SHOW_PREROLL_ADS = "CanShowPrerollAds";

    // IMA Values

    public static final String ONE_YNY = "1YNY";

    public static final String ENGLISH = "English";

    public static final String NOWPLAYING = "NowPlaying";

    public static final String AUDIO_VIDEO = "audio_video";

    public static final String MOBILE_PREROLL_VIDEO = isAndroid() ? "/15480783/Mobile-Preroll-Video/Android" : "/15480783/Mobile-Preroll-Video/iOS";

    public static final String XML_VMAP1 = "xml_vmap1";

    public static final String ADID_IDFA = isAndroid() ? "adid" : "idfa";

    public static final String EN = "en";

    public static final String ONE_YYY = "1YYY";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_START = "part2viewed";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_FIRST = "videoplaytime25";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_MIDPOINT = "videoplaytime50";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_THIRD = "videoplaytime75";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE = "videoplaytime100";

    public static final String PREROLL_EVENT_REQUEST_VIEWABLE_IMPRESSION = "viewable_impression";

    public static final String PREROLL_EVENT_REQUEST_MEASURABLE_IMPRESSION = "active_view_video_measurable_impression";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_PAUSED = "adpause";

    public static final String PREROLL_EVENT_REQUEST_QUARTILE_RESUME = "adresume";

    public static final String IMA = "IMA";

    public static final String PREROLL = "preroll";

    public static final String ADWIZZ_AUDIO_ADWIZZ_DISPLAY = isAndroid() ? "adswizz_audio" : "adswizz_display";

}
