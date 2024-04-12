package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingButtonFunctionalityTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.REWIND;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_0_5;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_1_0;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.CUSTOM_URL;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.AUDIOBOOK;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.PODCAST;

public class AndroidNowPlayingButtonFunctionalityTest extends NowPlayingButtonFunctionalityTest {

    @Override
    public void testPodcastForwardAndRewindButtons() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .waitUntilContentPartiallyBuffered()
                .stopStreamPlaying()
                .validateStreamTimeAfterFastForwardAndRewind()
                .waitUntilContentPartiallyBuffered()
                .validateStreamTimeAfterFastForwardAndRewind()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()));
    }

    @TestCaseId("749755")
    @Test(description = "Test Live button", groups = {NOW_PLAYING_TEST})
    public void testLiveButton() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.playStreamMoreThanRequiredTime(30);
        nowPlayingPage
                .validateLiveButtonIsNotClickable()
                .tapNowPlayingRewindOrFastForwardButton(REWIND)
                .validatePlayLiveLabelIsDisplayed()
                .tapOnPlayLiveButton()
                .validateLiveLableIsDisplayed()
                .validateLiveLabelChangeToPlayLive();
    }

    @Override
    public void testForwardAndRewindButtonsAvailability() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        Contents[] streams = {STREAM_STATION_WITHOUT_ADS, STREAM_PODCAST_MARKETPLACE, STREAM_AUDIOBOOK_LIAR};
        for (Contents stream : streams) {
            deepLinksUtil.openTuneThroughDeeplink(stream);
            nowPlayingPage
                    .waitForTheStreamPlayback(stream)
                    .validateRewindAndFastForwardButtonsAreDisplayed();
        }
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WILD_94);
        nowPlayingPage
                .waitForTheStreamPlayback(STREAM_STATION_WILD_94)
                .validateRewindAndFastForwardButtonsAreNotDisplayed();
    }

    @Override
    public void testVariableSpeedPlayback() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateStreamStartPlaying()
                .validateNowPlayingSpeedButtonIsDisplayed()
                .validateSpeedAdjustmentValue(SPEED_VALUE_1_0);
    }

    @TestCaseId("749658")
    @Test(description = "Play Podcast in offline mode", groups = {NOW_PLAYING_TEST})
    public void testPlayPodcastInOfflineMode() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.stopStreamPlaying();
        customWait(Duration.ofSeconds(30));
        deviceNativeActions.disableWiFi();

        nowPlayingPage
                .tapOnPlayButton()
                .validateStreamStartPlaying()
                .validatePauseButtonDisplayed()
                .stopStreamPlaying()
                .validatePlayButtonDisplayed();
    }

    @Override
    public void testPlayMoreThan30Seconds() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .tapNowPlayingRewindOrFastForwardButton(REWIND, 5)
                .playStreamMoreThanRequiredTime(30)
                .validateIsRewound(30)
                .validateIsFastForwarded()
                .stopStreamPlaying()
                .validateIsRewound(30)
                .validateStreamStartPlaying(PODCAST)
                .stopStreamPlaying()
                .validateIsFastForwarded()
                .validateStreamStartPlaying(PODCAST);
    }

    @Override
    public void testPodcastPlaybackSpeedControls() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        contentProfilePage.tapProfilePlayButton(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);

        String currentValue = nowPlayingPage.getSpeedAdjustmentValue().replace("x", "").replace("X", "");
        nowPlayingPage.tapOnSpeedButton();
        nowPlayingSpeedPlaybackDialog
                .validateVariableSpeedPlaybackPopUpisDisplayed(true)
                .setSpeedPickerPosition(currentValue, SPEED_VALUE_0_5)
                .minimizeNowPlayingScreen();

        for (int i = 1; i <= 3; i++) {
            contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EPISODES, LIST, SHORT, i, false);
            nowPlayingPage
                    .validateSpeedAdjustmentValue(SPEED_VALUE_0_5)
                    .minimizeNowPlayingScreen();
        }
    }

    @Override
    public void testVariableSpeedPlaybackDifferentContent() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        for (Object[] object : differentStreamTypesDataProviders()) {
            Contents contentFromDataProvider = (Contents) object[0];
            deepLinksUtil.openTuneThroughDeeplink(contentFromDataProvider);
            ContentProvider.ContentType contentType = getContentTypeValue(contentFromDataProvider.getStreamType());
            if (contentType == PODCAST || contentType == PREMIUM_PODCAST || contentType == AUDIOBOOK) {
                nowPlayingPage.validateSpeedAdjustmentValue(SPEED_VALUE_1_0);
            } else {
                nowPlayingPage.validateNowPlayingSpeedButtonIsNotDisplayed();
            }
        }

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog
                .tapEpisodeDownloadButton()
                .closeEpisodeModalDialog();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.tapOnContentName(contentName);
        nowPlayingPage
                .validateSpeedAdjustmentValue(SPEED_VALUE_1_0)
                .minimizeNowPlayingScreen();

        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(LIBRARY);
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.validateNowPlayingSpeedButtonIsNotDisplayed();
    }
}
