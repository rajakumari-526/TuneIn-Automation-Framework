package com.tunein.reporting.tests.ios.ads;

import com.tunein.mobile.testdata.models.Contents;
import com.tunein.reporting.tests.common.ads.IMATest;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.IMA_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.PAGE_AD_LIVE_INTERACTION;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STATIONS_WITH_IMA_VIDEO;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_KQED;
import static com.tunein.mobile.utils.ReportingUtil.PrerollType.IMA_VIDEO;
import static com.tunein.mobile.utils.ReportingUtil.*;

public class IosIMATest extends IMATest {

    @Override
    public void testIMAPrerollOptOutParams() {
        settingsPage.enableOptOut(true);
        String requiredURL = null;
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            nowPlayingPage.generatePrerollForStream(content);
            nowPlayingPage.waitUntilPreRollAdDisappearIfDisplayed();
            requiredURL = getUrlFromRequestWithParameterAndValue(IMA_REQUEST, IMA_REQUEST_PARAM_AD_TYPE, AUDIO_VIDEO);
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL);

        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_RDP, "1");
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_US_PRIVACY, ONE_YYY);
        reportingAsserts.validateThatRequestURLContainsParameterWithValue(requiredURL, IMA_REQUEST_PARAM_GDPR, "false");

        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, IMA_REQUEST_PARAM_NPA);
        reportingAsserts.validateThatRequestURLDoesntContainParameter(requiredURL, IMA_REQUEST_PARAM_GDPR_CONSENT);
    }

    @Override
    public void testVideoPrerollQuartilePercentageRequestWhenNavigatingFromNowPlayingPage() {
        String requiredURL = null;
        setPrerollType(IMA_VIDEO);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        for (Contents content : STATIONS_WITH_IMA_VIDEO) {
            deepLinksUtil.openURL(content.getStreamTuneDeepLink());
            requiredURL = getUrlFromRequest(PAGE_AD_LIVE_INTERACTION, Duration.ofSeconds(config().oneMinuteInSeconds()));
            if (requiredURL != null) break;
        }
        reportingAsserts.validateThatUrlIsPresent(requiredURL, PAGE_AD_LIVE_INTERACTION.getRequestTypeValue());
        nowPlayingPage.minimizeNowPlayingScreen();
        String[] displayPercentages = {PREROLL_EVENT_REQUEST_QUARTILE_START, PREROLL_EVENT_REQUEST_QUARTILE_FIRST,
                PREROLL_EVENT_REQUEST_QUARTILE_MIDPOINT, PREROLL_EVENT_REQUEST_QUARTILE_THIRD, PREROLL_EVENT_REQUEST_QUARTILE_COMPLETE};
        for (String displayPercentage : displayPercentages) {
            requiredURL = getUrlFromRequestWithParameterAndValue(PAGE_AD_LIVE_INTERACTION, IMA_ADS_LABEL, displayPercentage, Duration.ofSeconds(config().oneMinuteInSeconds()));
            reportingAsserts.validateThatUrlIsPresent(requiredURL, displayPercentage);
        }
    }

}
