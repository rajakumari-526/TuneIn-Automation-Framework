package com.tunein.mobile.tests.android.upgrade;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.upgrade.UpgradeTest;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_PREMIUM_EPISODES;
import static com.tunein.mobile.pages.BasePage.isReleaseTestRun;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_NAME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.UPGRADE_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.*;

public class AndroidUpgradeTest extends UpgradeTest {

    @TestCaseId("749353")
    @Test(description = "Test check serial number after uninstall", groups = {UPGRADE_TEST})
    public void testCheckSerialNumberAfterUninstall() {
        if (isReleaseTestRun()) throw new SkipException("Skip test as serial number is absent in settings for release build");
        applicationUtil.downgradeApp();
        navigationAction.navigateTo(SETTINGS);

        String serialId = settingsPage.getDeviceServiceIdentifier();
        reinstallApp();

        navigationAction.navigateTo(SETTINGS);
        settingsPage.validateSerialNumberIsNotEqualTo(serialId);
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
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateAddYourFavoritesPromptIsDisplayed();
        navigationAction.navigateTo(HOME);
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.validateDownloadsPromptIsDisplayed();
    }

    @TestCaseId("749370")
    @Test(description = "Test check serial number after clearing cache", groups = {UPGRADE_TEST})
    public void testCheckSerialNumberAfterClearingCache() {
        navigationAction.navigateTo(SETTINGS);
        String serialId = settingsPage.getDeviceServiceIdentifier();
        deviceNativeActions.clearCache();
        terminateApp();

        launchApp();
        navigationAction.navigateTo(SETTINGS);
        settingsPage.validateSerialNumberIsEqualTo(serialId);
    }

    @TestCaseId("749369")
    @Test(description = "Test check serial number after clearing data", groups = {UPGRADE_TEST})
    public void testCheckSerialNumberAfterClearingData() {
        navigationAction.navigateTo(SETTINGS);
        String serialId = settingsPage.getDeviceServiceIdentifier();
        deviceNativeActions.clearData();
        terminateApp();

        launchApp();
        navigationAction.navigateTo(SETTINGS);
        settingsPage.validateSerialNumberIsNotEqualTo(serialId);
    }

    @Override
    public void testDownloadedContent() {
        List<String> listOfDownloadedEpisodes = new ArrayList<>();
        applicationUtil.downgradeApp();
        signInPage.signInFlowForUser(USER_PREMIUM);
        Contents[] podcasts = {STREAM_PODCAST_PROGRESSIVE_VOICES, STREAM_PREMIUM_PODCAST_NURSE_TALK, STREAM_AUDIOBOOK_GATSBY};
        for (Contents podcast : podcasts) {
            deepLinksUtil.openContentProfileThroughDeeplink(podcast);
            String contentName = contentProfilePage.getContentItemDataByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
            listOfDownloadedEpisodes.add(contentName);
            contentProfilePage.tapEpisodeThreeDotsByIndex(podcast.isPremium() ? CATEGORY_TYPE_PREMIUM_EPISODES : CATEGORY_TYPE_EPISODES, 1);

            episodeModalDialog.tapEpisodeDownloadButton();
            deviceNativeActions.clickBackButton();
            navigationAction.tapBackButtonIfDisplayed();
            navigationAction.navigateTo(HOME);
            navigationAction.navigateTo(DOWNLOADS);
            downloadsPage.validateThatContentAppearedInDownloads(contentName);
        }

        applicationUtil.upgradeApp();
        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        String contentName = contentProfilePage.getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_NAME, false);
        contentProfilePage
                .tapEpisodeThreeDotsByIndex(CATEGORY_TYPE_EPISODES, 1)
                .tapEpisodeDownloadButton();
        deviceNativeActions.clickBackButton();
        deviceNativeActions.disableWiFi();
        deviceNativeActions.clickBackButton();
        navigationAction.navigateTo(DOWNLOADS);
        downloadsPage.validateThatContentAppearedInDownloads(contentName);
        for (String episodes : listOfDownloadedEpisodes) {
            downloadsPage.validateThatContentAppearedInDownloads(episodes);
        }
    }

}
