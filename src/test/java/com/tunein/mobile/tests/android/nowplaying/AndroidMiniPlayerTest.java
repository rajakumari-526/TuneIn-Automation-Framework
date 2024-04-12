package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.tests.common.nowplaying.MiniPlayerTest;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.STATION_SUBTITLE;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.END;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_PODCAST_MARKETPLACE;

public class AndroidMiniPlayerTest extends MiniPlayerTest {

    @Override
    public void testCheckMiniPlayerWhilePodcastIsPlaying() {
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_EPISODES);
        contentsListPage.clickOnContentCellByIndex(3);
        int podcastDuration = nowPlayingPage.getFullStreamDurationInSeconds();
        String podcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .waitEpisodeChanged(podcastEpisodeName)
                .validateNowPlayingOpenNewEpisode(podcastEpisodeName)
                .minimizeNowPlayingScreen();
        deviceNativeActions.clickBackButton();
        String contentItemMetadata = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 3, EPISODE_NAME, false, 10);
        miniPlayerPage.validateCategoryItemWithMiniPlayerMetadata(STATION_SUBTITLE, contentItemMetadata);
        contentProfilePage.tapOnRequiredCategory(CATEGORY_TYPE_EPISODES);
        contentsListPage.clickOnContentCellByIndex(0);
        String latestPodcastEpisodeName = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
        nowPlayingPage
                .moveScrubberSliderToSpecificPosition(END, podcastDuration)
                .validateNowPlayingSubtitleIsEqualTo(latestPodcastEpisodeName)
                .validatePlayButtonDisplayed()
                .minimizeNowPlayingScreen();
        miniPlayerPage.validateMiniPlayerIsDisplayed();
    }
}
