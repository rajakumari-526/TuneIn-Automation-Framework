package com.tunein.mobile.tests.ios.upgrade;

import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.upgrade.UpgradeTest;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_PREMIUM_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.DOWNLOADS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.uninstallApp;

public class IosUpgradeTest extends UpgradeTest {

    @Override
    public void testDownloadedContent() {
        List<String> listOfDownloadedEpisodes = new ArrayList<>();
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(USER_PREMIUM);
        Contents[] podcasts = {STREAM_FREE_SHORT_PODCAST, STREAM_PREMIUM_PODCAST_JSA, STREAM_AUDIOBOOK_GATSBY};
        for (Contents podcast : podcasts) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            String contentName = contentProfilePage.getContentItemDataByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
            listOfDownloadedEpisodes.add(contentName);
            contentProfilePage.tapEpisodeThreeDotsByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1);

            episodeModalDialog.tapEpisodeDownloadButton();
            navigationAction.navigateTo(HOME);
            navigationAction.navigateTo(DOWNLOADS);
            downloadsPage.validateThatContentAppearedInDownloads(contentName);
        }

        applicationUtil.upgradeApp();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_PROGRESSIVE_VOICES);
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
        contentProfilePage
                .tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1)
                .tapEpisodeDownloadButton();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.validateThatContentAppearedInDownloads(contentName);
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_PROGRESSIVE_VOICES);
        contentProfilePage
                .tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1)
                .tapEpisodeDeleteButton();
        for (String episode : listOfDownloadedEpisodes) {
            navigationAction.navigateTo(DOWNLOADS);
            downloadsPage.validateThatContentAppearedInDownloads(episode);
        }
        for (Contents podcast : podcasts) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            if (podcast.isPremium()) {
                contentProfilePage
                        .tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_PREMIUM_EPISODES, 1)
                        .tapEpisodeDeleteButton();
            } else {
                contentProfilePage
                        .tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1)
                        .tapEpisodeDeleteButton();
            }
        }
    }

    @Override
    public void testUpgradeDeleteApp() {
        applicationUtil.downgradeApp();
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS, true);
        nowPlayingPage.minimizeNowPlayingScreen();
        libraryPage.openCustomUrlFlow(CUSTOM_URL);
        nowPlayingPage.minimizeNowPlayingScreen();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        contentProfilePage.tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1);
        episodeModalDialog.tapEpisodeDownloadButton();
        uninstallApp();

        applicationUtil.upgradeApp();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.validateDownloadsPromptIsDisplayed();
    }
}
