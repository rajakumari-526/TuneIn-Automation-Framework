package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.DEFAULT;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_FREE_PODCAST_WITH_LONG_EPISODE;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;

public class NowPlayingMetaDataTest extends BaseTest {

    @TestCaseIds({@TestCaseId("710131"), @TestCaseId("738986"), @TestCaseId("24621"), @TestCaseId("749138"),
    @TestCaseId("749424"), @TestCaseId("749508"), @TestCaseId("749427"), @TestCaseId("749510"), @TestCaseId("749432"),
    @TestCaseId("749433"), @TestCaseId("749517"), @TestCaseId("749423"), @TestCaseId("749520"), @TestCaseId("749431")})
    @Test(description = "Check metadata for different content types",
            dataProviderClass = ContentProvider.class,
            dataProvider = "fullListOfStreamTypesDataProviders",
            groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testMetaDataForVariousContent(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(content);
        nowPlayingPage.validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(content.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("23540"), @TestCaseId("30010")})
    @Test(description = "Change scrubber slider position and check metadata", groups = {NOW_PLAYING_TEST})
    public void testMetadataAfterChangingScrubberPositionInPodcast() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeNameBeforeScroll = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        String podcastNameBeforeScroll = nowPlayingPage.getTextValueOfNowPlayingTitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(DEFAULT, podcastDuration)
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_FREE_PODCAST_WITH_LONG_EPISODE.getStreamType()))
                .validateNowPlayingSubtitleIsEqualTo(podcastEpisodeNameBeforeScroll)
                .validateNowPlayingTitleIsEqualTo(podcastNameBeforeScroll);
    }

    @TestCaseIds({
            @TestCaseId("576564"), @TestCaseId("713409"),
            @TestCaseId("576566"), @TestCaseId("736183"),
            @TestCaseId("576567"), @TestCaseId("736184"),
            @TestCaseId("130686"), @TestCaseId("221608"),
            @TestCaseId("24620"), @TestCaseId("749462"),
            @TestCaseId("749463"), @TestCaseId("749466"),
            @TestCaseId("749467"), @TestCaseId("749461")
    })
    @Test(description = "Check NowPlaying title with title in Content Profile Page",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamTypesShortDataProviders",
            groups = {NOW_PLAYING_TEST, CONTENT_PROFILE_TEST, ACCEPTANCE_TEST}
    )
    public void testNowPlayingTitle(Contents content) {
        if (content.isPremium()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }
        deepLinksUtil.openTuneThroughDeeplink(content);
        String streamTitle = nowPlayingPage.getTextValueOfNowPlayingTitle();
        nowPlayingPage.goToStreamProfile();
        contentProfilePage.validateContentProfileTitleIsEqualTo(streamTitle);
    }

}
