package com.tunein.mobile.reporting;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.utils.DataUtil.getIpAddressFromDomain;

public class ReportingConstants {

    public static final String HTTP = "http://";

    public static final String HTTPS = "https://";

    public static final String PROD_OMPL_REPORTS_TUNEIN_COM = "opml.radiotime.com";

    public static final String PROD_REPORTS_TUNEIN_COM = "reports.tunein.com";

    public static final String REPORTS_RADIOTIME_COM = "reports.radiotime.com";

    public static final String REPORT_DOMAIN_OR_IP = isAndroid() ? getIpAddressFromDomain(PROD_REPORTS_TUNEIN_COM) : PROD_OMPL_REPORTS_TUNEIN_COM;

    public static final int RESPONSE_STATUS_401_CODE = 401;

    public static final int RESPONSE_STATUS_403_CODE = 403;

    public static final String RESPONSE_STATUS_403_TEXT = "Forbidden";

    public static final int RESPONSE_STATUS_500_CODE = 500;

    public static final int RESPONSE_STATUS_NULL = 0;

    // Reporting URL
    public static final String REPORT_REQUEST = HTTPS + REPORT_DOMAIN_OR_IP + "/Report.ashx";

    public static final String ACCOUNT_REQUEST = HTTPS + REPORT_DOMAIN_OR_IP + "/Account.ashx";

    public static final String FAVORITES_REQUEST = HTTPS + REPORT_DOMAIN_OR_IP + "/favorites.ashx";

    // Reporting query parameters
    public static final String REPORT_REQUEST_QUERY_NAME_C = "c";

    public static final String REPORT_REQUEST_QUERY_VALUE_EVENTLIST = "eventlist";

    public static final String REPORT_REQUEST_QUERY_VALUE_ADDINTERESTS = "addInterests";

    // Reporting body parameters

    public static final String REPORT_BODY_NAME_EVENT = isAndroid() ? "event=" : "event1=";

    public static final String DYNAMIC_VALUE = "dynamic_value";

    public static final String ABSENT_VALUE = "absent_value";

    public static final String RESPONSE_JSON_PARAM_BODY = "body";

    public static final String URL = "url";

    public static final String INSTREAM_URL = "https://rfcm.streamguys1.com";

    public enum RequestType {
        CLIENT_REPORT_REQUEST("/events/client-reports"),
        ADS_WIZZ_PREROLL_REQUEST(isAndroid() ? "/vast/4.0/request/590" : "/vast/4.0/request?"),
        ADS_WIZZ_MIDROLL_REQUEST(isAndroid() ? "/vast/4.0/request/3011" : "/vast/4.0/request?"),
        ADS_WIZZ_PREROLL_DISPLAY_PERCENTAGE_REQUEST("/vast/4.0/v1.1/quartile"),
        TUNE_REQUEST("/Tune.ashx?"),
        IMA_REQUEST(isAndroid() ? "/gampad/ads?ad_type" : "/gampad/ads?iu"),
        PAGE_AD_LIVE_INTERACTION("/interaction/?ai"),
        GAM_INSTREAM_REQUEST("/master.m3u8"),
        MAX_BANNER_REQUEST("/1.0/mediate?"),

        NOWPLAYING_CALL("/profiles/s"),

        REPORTS_AD_REQUEST("/reports/a/r?"),
        REPORTS_AD_IMPRESSION("/reports/a/i?"),

        REPORTS_AD_START("/reports/a/start?"),

        REPORTS_AD_END("/reports/a/end?"),
        REPORTS_AD_FAILURE("/reports/a/f?"),
        ADSWIZZ_INSTREAM_REQUEST("www/delivery/"),
        REPORTS_ASHX("Report.ashx");

        private String requestTypeValue;

        private RequestType(String requestTypeValue) {
            this.requestTypeValue = requestTypeValue;
        }

        public String getRequestTypeValue() {
            return requestTypeValue;
        }

        public static RequestType getCategoryType(final String requestType) {
            List<RequestType> requestsTypeTypesList = Arrays.asList(RequestType.values());
            return requestsTypeTypesList.stream().filter(eachContent -> eachContent.toString().equals(requestType))
                    .findAny()
                    .orElse(null);
        }
    }

}
