package com.tunein.mobile.tests.ios.nowplaying;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingButtonFunctionalityTest;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_0_5;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.SPEED_VALUE_1_0;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.CUSTOM_URL;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.AUDIOBOOK;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;

public class IosNowPlayingButtonFunctionalityTest extends NowPlayingButtonFunctionalityTest {

    //TODO When page object of Airplay view will be implemented in framework add verification
    // that airplay view is opened.
    @TestCaseId("711707")
    @Test(description = "Check AirPlay button", groups = {NOW_PLAYING_TEST})
    public void testAirPlayButton() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateThatExternalDevicesButtonDisplayed();
    }

    @Override
    public void testPodcastForwardAndRewindButtons() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .waitUntilContentPartiallyBuffered()
                .stopStreamPlaying()
                .validateStreamTimeAfterFastForwardAndRewind()
                .tapOnPlayButton()
                .waitUntilContentPartiallyBuffered()
                .validateStreamTimeAfterFastForwardAndRewind()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_PODCAST_MARKETPLACE.getStreamType()));
    }

    @Override
    public void testVariableSpeedPlayback() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validateStreamStartPlaying()
                .validateNowPlayingSpeedButtonIsDisplayed()
                .validateSpeedAdjustmentValue(SPEED_VALUE_1_0)
                .tapOnSpeedButton();
        nowPlayingSpeedPlaybackDialog.validateApplyToAllPodcastsIsOff();
    }

    @Override
    public void testForwardAndRewindButtonsAvailability() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WILD_94);
        nowPlayingPage
                .waitForTheStreamPlayback(STREAM_STATION_WILD_94)
                .validateRewindAndFastForwardButtonsAreNotDisplayed();
        Contents[] streams = {STREAM_PODCAST_MARKETPLACE, STREAM_AUDIOBOOK_LIAR};
        for (Contents stream : streams) {
            deepLinksUtil.openTuneThroughDeeplink(stream);
            nowPlayingPage
                    .validateStreamStartPlaying()
                    .validateRewindAndFastForwardButtonsAreDisplayed();
        }
    }

    @Override
    public void testPlayMoreThan30Seconds() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.playStreamMoreThanRequiredTime(30);
        nowPlayingPage
                .validateIsRewound(30)
                .validateIsFastForwarded()
                .stopStreamPlaying()
                .validateIsRewound(30)
                .validateIsFastForwarded();
    }

    @Override
    public void testPodcastPlaybackSpeedControls() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        contentProfilePage.tapProfilePlayButton(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        nowPlayingPage.validateStreamStartPlaying();

        String currentValue = nowPlayingPage.getSpeedAdjustmentValue().replace("x", "").replace("X", "");
        nowPlayingPage.tapOnSpeedButton();
        nowPlayingSpeedPlaybackDialog.validateVariableSpeedPlaybackPopUpisDisplayed(true);
        nowPlayingSpeedPlaybackDialog.setSpeedPickerPosition(currentValue, SPEED_VALUE_0_5);
        nowPlayingPage
                .validateSpeedAdjustmentValue(SPEED_VALUE_0_5)
                .minimizeNowPlayingScreen();

        contentProfilePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_EPISODES, LIST, SHORT, 3, false);
        nowPlayingPage
                .validateSpeedAdjustmentValue(SPEED_VALUE_1_0);
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
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 3, EPISODE_NAME, false);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 3);
        episodeModalDialog.tapEpisodeDownloadButton();
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
