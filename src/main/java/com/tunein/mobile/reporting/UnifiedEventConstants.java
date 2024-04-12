package com.tunein.mobile.reporting;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.reporting.ReportingConstants.HTTPS;

public class UnifiedEventConstants {

    public static final String UNIFIED_EVENT_REPORT_STAGE = HTTPS + "event.stage.platform.tunein.com";

    public static final String UNIFIED_EVENT_REPORT_PROD_DOMAIN = "event.platform.tunein.com";

    public static final String UNIFIED_EVENT_REPORT_PROD = HTTPS + UNIFIED_EVENT_REPORT_PROD_DOMAIN;

    public static final String UNIFIED_EVENT_REPORT = config().isProdEnvironment() ? UNIFIED_EVENT_REPORT_PROD : UNIFIED_EVENT_REPORT_STAGE;

    // Unified events

    public static final String UNIFIED_EVENT_APP_SESSION_STARTED = "APP_SESSION_STARTED";

    public static final String UNIFIED_EVENT_USER_PLAY_CLICKED = "USER_PLAY_CLICKED";

    public static final String UNIFIED_EVENT_LISTEN_SESSION_STARTED = "LISTEN_SESSION_STARTED";

    public static final String UNIFIED_EVENT_ADS_PLAYBACK_STARTED = "ADS_PLAYBACK_STARTED";

    public static final String UNIFIED_EVENT_ADS_PLAYBACK_FINISHED = "ADS_PLAYBACK_FINISHED";

    public static final String UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED = "ADS_VIDEO_AUDIO_ROLL_REQUESTED";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_CLICKED = "ADS_DISPLAY_CLICKED";

    public static final String UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED = "ADS_VIDEO_AUDIO_ROLL_RESPONSE_RECEIVED";

    public static final String UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED = "ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED";

    public static final String UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUEST_FAILED = "ADS_VIDEO_AUDIO_ROLL_REQUEST_FAILED";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_REQUESTED = "ADS_DISPLAY_REQUESTED";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_RESPONSE_RECEIVED = "ADS_DISPLAY_RESPONSE_RECEIVED";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_IMPRESSION = "ADS_DISPLAY_IMPRESSION";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_VIEWABILITY_STATUS = "ADS_DISPLAY_VIEWABILITY_STATUS";

    public static final String UNIFIED_EVENT_ADS_INSTREAM_RECEIVED = "ADS_INSTREAM_RECEIVED";

    public static final String UNIFIED_EVENT_ADS_INSTREAM_STARTED = "ADS_INSTREAM_STARTED";

    public static final String UNIFIED_EVENT_ADS_INSTREAM_QUARTILE_STATUS = "ADS_INSTREAM_QUARTILE_STATUS";

    public static final String UNIFIED_EVENT_ADS_INSTREAM_COMPLETED = "ADS_INSTREAM_COMPLETED";

    public static final String UNIFIED_EVENT_ADS_DISPLAY_CERTIFIED_IMPRESSION = "ADS_DISPLAY_CERTIFIED_IMPRESSION";

    // JSON params

    public static final String UNIFIED_EVENT_TYPE = "EVENT_TYPE_TRACK";

    public static final String UNIFIED_EVENT_JSON_PARAM_DEVICE_ID = "device_id";

    public static final String UNIFIED_EVENT_JSON_PARAM_SENT_AT = "sent_at";

    public static final String UNIFIED_EVENT_JSON_PARAM_DATA = "data";

    public static final String UNIFIED_EVENT_JSON_PARAM_TYPE = "type";

    public static final String UNIFIED_EVENT_JSON_PARAM_EVENT = "event";

    public static final String UNIFIED_EVENT_JSON_PARAM_CONTEXT = "context";

    public static final String UNIFIED_EVENT_JSON_PARAM_EVENT_TS = "eventTs";

    public static final String UNIFIED_EVENT_JSON_PARAM_MESSAGE_ID = "messageId";

    public static final String UNIFIED_EVENT_JSON_PARAM_PARTNER_ID = "partnerId";

    public static final String UNIFIED_EVENT_JSON_PARAM_SESSION_ID = "sessionId";

    public static final String UNIFIED_EVENT_JSON_PARAM_DEVICEID = "deviceId";

    public static final String UNIFIED_EVENT_JSON_PARAM_DEVICE = "device";

    public static final String UNIFIED_EVENT_JSON_PARAM_APP = "app";

    public static final String UNIFIED_EVENT_JSON_PARAM_APP_STATE = "appState";

    public static final String UNIFIED_EVENT_JSON_PARAM_AB_TEST_IDS = "abTestIds";

    public static final String UNIFIED_EVENT_JSON_PARAM_IN_BACKGROUND = "inBackground";

    public static final String UNIFIED_EVENT_JSON_PARAM_NETWORK = "network";

    public static final String UNIFIED_EVENT_JSON_PARAM_WIFI = "wifi";

    public static final String UNIFIED_EVENT_JSON_PARAM_MANAFACTURER = "manufacturer";

    public static final String UNIFIED_EVENT_JSON_PARAM_MODEL = "model";

    public static final String UNIFIED_EVENT_JSON_PARAM_BATTERY_PCT = "batteryPct";

    public static final String UNIFIED_EVENT_JSON_PARAM_NAME = "name";

    public static final String UNIFIED_EVENT_JSON_PARAM_VERSION = "version";

    public static final String UNIFIED_EVENT_JSON_PARAM_BUILD = "build";

    public static final String UNIFIED_EVENT_JSON_PARAM_PAGE = "page";

    public static final String UNIFIED_EVENT_JSON_PARAM_PATH = "path";

    public static final String UNIFIED_EVENT_JSON_PARAM_IP = "ip";

    public static final String UNIFIED_EVENT_JSON_PARAM_LOCALE = "locale";

    public static final String UNIFIED_EVENT_JSON_PARAM_SCREEN = "screen";

    public static final String UNIFIED_EVENT_JSON_PARAM_OS = "os";

    public static final String UNIFIED_EVENT_JSON_PARAM_HEIGHT = "height";

    public static final String UNIFIED_EVENT_JSON_PARAM_WIDTH = "width";

    public static final String UNIFIED_EVENT_JSON_PARAM_DENSITY = "density";

    public static final String UNIFIED_EVENT_JSON_PARAM_LISTENING_INFO = "listeningInfo";

    public static final String UNIFIED_EVENT_JSON_PARAM_LISTEN_ID = "listenId";

    public static final String UNIFIED_EVENT_JSON_PARAM_GUIDE_ID = "guideId";

    public static final String UNIFIED_EVENT_JSON_PARAM_PARENT_GUIDE_ID = "parentGuideId";

    public static final String UNIFIED_EVENT_JSON_PARAM_TIME_ZONE = "timezone";

    public static final String UNIFIED_EVENT_JSON_PARAM_USER_INFO = "userInfo";

    public static final String UNIFIED_EVENT_JSON_PARAM_USER_ID = "userId";

    public static final String UNIFIED_EVENT_JSON_PARAM_IS_REGISTERED = "isRegistered"; // boolean

    public static final String UNIFIED_EVENT_JSON_PARAM_IS_LOGGED_IN = "isLoggedIn"; // boolean

    public static final String UNIFIED_EVENT_JSON_PARAM_IS_ELIGIBLE = "isEligible"; // boolean

    public static final String UNIFIED_EVENT_JSON_PARAM_IS_COMPANION_AD = "isCompanionAd"; // boolean

    public static final String UNIFIED_EVENT_JSON_PARAM_IS_VIEWABLE = "isViewable"; // boolean

    public static final String UNIFIED_EVENT_JSON_PARAM_USER_AGENT = "userAgent";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_SLOT = "adSlot";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_NETWORK_NAME = "adNetworkName";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_REQUEST_ID = "adRequestId";

    public static final String UNIFIED_EVENT_JSON_PARAM_ERROR_CODE = "errorCode";

    public static final String UNIFIED_EVENT_JSON_PARAM_ERROR_MESSAGES = "errorMessage";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_TYPE = "adType";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_UNIT_ID = "adUnitId";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMAT = "adDisplayFormat";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_CREATIVE_ID = "adCreativeId";

    public static final String UNIFIED_EVENT_JSON_PARAM_REVENUE = "revenue";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_DISPLAY_FORMATS_ACCEPTED = "adDisplayFormatsAccepted";

    public static final String UNIFIED_EVENT_JSON_PARAM_CURRENT_VIDEOAUDIOROLL_IDX = "currentVideoaudiorollIdx";

    public static final String UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_REQUESTED = "noOfVideoaudiorollRequested";

    public static final String UNIFIED_EVENT_JSON_PARAM_NO_OF_VIDEOAUDIOROLLS_RECEIVED = "noOfVideoaudiorollsReceived";

    public static final String UNIFIED_EVENT_JSON_PARAM_QUARTILE = "quartile";

    public static final String UNIFIED_EVENT_JSON_PARAM_AD_UNIT_EVENT_ID = "adUnitEventId";

    public static final String UNIFIED_EVENT_JSON_PARAM_DURATION = "duration";

    public static final String UNIFIED_EVENT_JSON_PARAM_DESTINATION_URL = "destinationUrl";

    public static final String UNIFIED_EVENT_JSON_PARAM_FREQUENCY_CAP = "frequencyCap";

    public static final String UNIFIED_EVENT_JSON_PARAM_WATERFALL_LATENCY_MSECS = "waterfallLatencyMsecs";

    public static final String UNIFIED_EVENT_JSON_PARAM_WATERFALL_TEST_NAME = "waterfallTestName";

    public static final String UNIFIED_EVENT_JSON_PARAM_WATERFALL_NAME = "waterfallName";

    public static final String UNIFIED_EVENT_JSON_PARAM_REVENUE_PRECISION = "revenuePrecision";

    // Values
    public static final String AD_SLOT_PREROLL = "AD_SLOT_PREROLL";

    public static final String AD_SLOT_MIDROLL = "AD_SLOT_MIDROLL";

    public static final String AD_SLOT_DISPLAY = "AD_SLOT_DISPLAY";

    public static final String AD_SLOT_INSTREAM = "AD_SLOT_INSTREAM";

    public static final String DFP = "dfp";

    public static final String MOBILEFUSE = "MobileFuse";

    public static final String GOOGLE_ADMOB = "Google AdMob";

    public static final String APPLOVIN = "AppLovin";

    public static final String INMOBI = "InMobi";

    public static final String ADSWIZZ = "adswizz";

    public static final String ADSWIZZ_AUDIO = "adswizz_audio";

    public static final String MAX_MEDIUM_RECTANGLE = "max_medium_rectangle";

    public static final String MAX_BANNER = "max_banner";

    public static final String TUNEIN_ADWIZZ_COM_3011 = "tunein.adswizz.com,3011";

    public static final String IMA_VIDEO = "ima_video";

    public static final String IMA_AUDIO = "ima_audio";

    public static final String GOOGLE_AD_MANAGER_NATIVE = "Google Ad Manager Native";

    public static final String AD_TYPE_DISPLAY = "AD_TYPE_DISPLAY";

    public static final String REVENUE_PRECISION = "REVENUE_PRECISION";

    public static final String AD_REVENUE_PRECISION_UNSPECIFIED = "AD_REVENUE_PRECISION_UNSPECIFIED";

    public static final String AD_TYPE_UNSPECIFIED = "AD_TYPE_UNSPECIFIED";

    public static final String AD_TYPE_AUDIO = "AD_TYPE_AUDIO";

    public static final String AD_TYPE_VIDEO = "AD_TYPE_VIDEO";

    public static final String TUNEIN_ADSWIZZ_COM_590_591_1342 = "tunein.adswizz.com,590,591,1342";

    public static final String TUNEIN_ADSWIZZ_COM_625_626_631 = "tunein.adswizz.com,625,626,631";

    public static final String MOBILE_PREROLL_VIDEO = isAndroid() ? "/15480783/Mobile-Preroll-Video/Android" : "/15480783/Mobile-Preroll-Video/iOS";

    public static final String AD_DISPLAY_FORMAT_300_250 = "AD_DISPLAY_FORMAT_300_250";

    public static final String AD_DISPLAY_FORMAT_320_50 = "AD_DISPLAY_FORMAT_320_50";

    public static final String AD_DISPLAY_FORMAT_728_90 = "AD_DISPLAY_FORMAT_728_90";

    public static final String QUARTILE_FIRST = "QUARTILE_FIRST";

    public static final String QUARTILE_MIDPOINT = "QUARTILE_MIDPOINT";

    public static final String QUARTILE_THIRD = "QUARTILE_THIRD";

    public static final String APPLE = "Apple";

    public static final String FACEBOOK = "Facebook";

    public static final String FACEBOOK_NATIVE = "Facebook Native";

    public static final String CONTROL = "Control";

    public static final String DEFAULT_WATERFALL = "Default Waterfall";

    public static final String AD_REVENUE_PRECISION_PRECISE = "AD_REVENUE_PRECISION_PRECISE";

    public static final String ANDROID_MED_REC_20_VS_15_REFRESH = "AndroidMedRec_20vs15sRefresh";

    public static final List<String> AD_NETWORK_NAME_LIST = Arrays.asList(DFP, APPLOVIN, MOBILEFUSE, GOOGLE_ADMOB, IMA_VIDEO, IMA_AUDIO, ADSWIZZ_AUDIO, INMOBI, MAX_BANNER, MAX_MEDIUM_RECTANGLE, GOOGLE_AD_MANAGER_NATIVE, FACEBOOK, FACEBOOK_NATIVE);

    public static final List<String> AD_TYPE_LIST = Arrays.asList(AD_TYPE_UNSPECIFIED, AD_TYPE_DISPLAY, AD_TYPE_VIDEO, AD_TYPE_AUDIO);
  
    public static final List<String> QUARTILE_STATUS_LIST = Arrays.asList(QUARTILE_FIRST, QUARTILE_MIDPOINT, QUARTILE_THIRD);

    public static final List<String> WATERFALL_TEST_NAME_LIST = Arrays.asList(CONTROL, ANDROID_MED_REC_20_VS_15_REFRESH);

    public static final List<String> AD_REVENUE_PRECISION_PRECISE_LIST = Arrays.asList(AD_REVENUE_PRECISION_PRECISE, AD_REVENUE_PRECISION_UNSPECIFIED);

}
