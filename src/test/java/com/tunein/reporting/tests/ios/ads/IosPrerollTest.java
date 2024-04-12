package com.tunein.reporting.tests.ios.ads;

import com.google.gson.JsonObject;
import com.tunein.reporting.tests.common.ads.PrerollTest;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.DOWNLOADS;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.ADS_WIZZ_PREROLL_REQUEST;
import static com.tunein.mobile.reporting.ReportingConstants.RequestType.MAX_BANNER_REQUEST;
import static com.tunein.mobile.reporting.UnifiedEventConstants.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.ReportingUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.*;

public class IosPrerollTest extends PrerollTest {

    @Override
    public void testImaAdsVideoAudioRollEligibilityDecidedEvent() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5);
        customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds()));

        JsonObject firstEventData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED);
        JsonObject secondEventData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatEventIsPresent(firstEventData, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED);
        unifiedEventsAsserts.validateThatEventIsNotPresent(secondEventData, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(firstEventData, UNIFIED_EVENT_JSON_PARAM_AD_SLOT, AD_SLOT_PREROLL);
        unifiedEventsAsserts.validateThatParameterIsNotPresent(firstEventData, UNIFIED_EVENT_JSON_PARAM_IS_ELIGIBLE);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds()));

        JsonObject thirdEventData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED);
        JsonObject fourthEventData = getLastUnifiedEventRequestByType(UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatEventIsPresent(thirdEventData, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_ELIGIBILITY_DECIDED);
        unifiedEventsAsserts.validateThatEventIsPresent(fourthEventData, UNIFIED_EVENT_ADS_VIDEO_AUDIO_ROLL_REQUESTED);

        unifiedEventsAsserts.validateThatParameterIsEqualTo(thirdEventData, UNIFIED_EVENT_JSON_PARAM_IS_ELIGIBLE, true);
        unifiedEventsAsserts.validateThatParameterIsPresentAndValueIsNotEmpty(thirdEventData, UNIFIED_EVENT_JSON_PARAM_AD_SLOT);
        unifiedEventsAsserts.validateThatParameterIsNotPresent(thirdEventData, UNIFIED_EVENT_JSON_PARAM_FREQUENCY_CAP);
    }

    @Override
    public void testPlayDownloadedTopic() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_ADS_EXPLICITLY_ENABLED);
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog.tapEpisodeDownloadButton();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.tapOnContentName(contentName);
        nowPlayingPage.validateStreamStartPlaying();
        String requiredURLForPreroll = getUrlFromRequest(ADS_WIZZ_PREROLL_REQUEST);
        String requiredURLForBanner = getUrlFromRequest(MAX_BANNER_REQUEST, Duration.ofSeconds(config().oneMinuteInSeconds()));
        reportingAsserts.validateThatUrlIsAbsent(requiredURLForPreroll);
        reportingAsserts.validateThatUrlIsPresent(requiredURLForBanner);
    }

    @Override
    public void testLaunchStationWithVideoPreroll() {
        nowPlayingPage.generatePrerollForStream(STREAM_STATION_IMA_VIDEO_AD);
        nowPlayingPage
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying();
        String requiredURL = getUrlFromRequest(MAX_BANNER_REQUEST);
        reportingAsserts.validateThatUrlIsPresent(requiredURL);
    }

    @Override
    public void testMediaControlsForIMAPreroll() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        deepLinksUtil.openURL(STREAM_STATION_IMA_VIDEO_AD.getStreamTuneDeepLink());
        nowPlayingPage
                .validateThatPreRollIsDisplayed()
                .validateRewindAndFastForwardButtonsAreNotDisplayed()
                .validatePauseButtonDisplayed();
        scroll(UP, 3);
        nowPlayingPage.validateSeekBarDisplayed();
    }

}
