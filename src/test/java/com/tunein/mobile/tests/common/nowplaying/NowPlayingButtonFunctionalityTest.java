package com.tunein.mobile.tests.common.nowplaying;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Users;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_STATIONS;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.LIBRARY;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.REWIND;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType.FAVORITE_STATION;
import static com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog.SleepTimerOptions.FIFTEEN;
import static com.tunein.mobile.testdata.TestGroupName.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.OWNED_AND_OPERATED;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.generateRandomUser;
import static com.tunein.mobile.utils.ApplicationUtil.restartApp;
import static com.tunein.mobile.utils.ApplicationUtil.runAppInBackground;

public abstract class NowPlayingButtonFunctionalityTest extends BaseTest {

    @TestCaseIds ({@TestCaseId("24620"), @TestCaseId("749485")})
    @Test(description = "Verify favorite and share button in NowPlaying", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testFavoriteAndShareButton() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        nowPlayingPage
                .tapOnFavoriteIcon(true, FAVORITE_STATION)
                .validateContentFollowState(true)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.tapOnFavoritesButton();
        favoritesPage
                .validateThatContentAppearedInFavorites(STREAM_STATION_KQED.getStreamName(), CATEGORY_TYPE_STATIONS);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
        nowPlayingPage
                .tapOnShareButton()
                .validateShareDialogDisplayed();
    }

    @TestCaseIds({@TestCaseId("576559"), @TestCaseId("576059"), @TestCaseId("749458")})
    @Test(description = "Verify Close button", groups = {NOW_PLAYING_TEST})
    public void testCloseButton() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.minimizeNowPlayingScreen();
        miniPlayerPage
                .validateMiniPlayerIsDisplayed()
                .extendMiniPlayer(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateThatCloseButtonDisplayed();
    }

    @TestCaseIds({@TestCaseId("576557"), @TestCaseId("37878"), @TestCaseId("24620"), @TestCaseId("749197"), @TestCaseId("749457")})
    @Test(description = "Check Listening options for station and podcast",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProvider",
            groups = {NOW_PLAYING_TEST, SMOKE_TEST, ACCEPTANCE_TEST})
    public void testOptionsButtonForDifferentContentTypes(Contents content) {
        ContentProvider.ContentType contentType = getContentTypeValue(content.getStreamType());
        navigationAction.navigateTo(HOME);
        deepLinksUtil
                .openTuneThroughDeeplink(content)
                .tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog
                .validateMoreOptionButtonsAreCorrect(nowPlayingMoreOptionsDialog.getExpectedMoreOptionItems(contentType))
                .closeMoreOptionsDialog();
        nowPlayingPage
                .stopStreamPlaying()
                .tapOnMoreOptionsButton()
                .validateMoreOptionButtonsAreCorrect(nowPlayingMoreOptionsDialog.getExpectedMoreOptionItems(contentType))
                .closeMoreOptionsDialog();
    }

    @TestCaseIds({@TestCaseId("576555"), @TestCaseId("281791")})
    @Test(description = "Check dont like ads button?", groups = {NOW_PLAYING_TEST})
    public void testDontLikeAdsButton() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.tapOnNoAdsButton();
        upsellPage.closeUpsell();
        nowPlayingPage
                .minimizeNowPlayingScreen()
                .extendMiniPlayer(STREAM_STATION_UNICC)
                .validateNoAdsButtonDisplayed()
                .stopStreamPlaying()
                .minimizeNowPlayingScreen();
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_UNICC);
        nowPlayingPage.validateNoAdsButtonNotDisplayed();
    }

    @TestCaseIds({@TestCaseId("23538"), @TestCaseId("30009"), @TestCaseId("576197"), @TestCaseId("24620"),
            @TestCaseId("37872"), @TestCaseId("749116"), @TestCaseId("749195"), @TestCaseId("749592"), @TestCaseId("749596"), @TestCaseId("749438")})
    @Test(description = "Check Forward/Rewind buttons for podcast ", groups = {NOW_PLAYING_TEST, SMOKE_TEST, ACCEPTANCE_TEST})
    public abstract void testPodcastForwardAndRewindButtons();

    @TestCaseIds({@TestCaseId("23536"), @TestCaseId("37872"), @TestCaseId("24620"), @TestCaseId("749116"),
            @TestCaseId("749195"), @TestCaseId("749654"), @TestCaseId("749436")})
    @Test(description = "Check Play/Pause buttons", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testPlayPauseButton() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validatePauseButtonDisplayed()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()))
                .stopStreamPlaying()
                .validatePlayButtonDisplayed()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_WITHOUT_ADS.getStreamType()));
    }

    @TestCaseIds({@TestCaseId("23537"), @TestCaseId("37872"),
            @TestCaseId("24620"), @TestCaseId("24621"), @TestCaseId("749116"), @TestCaseId("749195"),
            @TestCaseId("749138"), @TestCaseId("749587"), @TestCaseId("749437")})
    @Test(description = "Check Play/Stop buttons", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testPlayStopButton() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_TODAYS_HITS);
        nowPlayingPage
                .validateStopButtonDisplayed()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_TODAYS_HITS.getStreamType()))
                .stopStreamPlaying()
                .validatePlayButtonDisplayed()
                .validateNowPlayingMetadataIsNotEmpty(getContentTypeValue(STREAM_STATION_TODAYS_HITS.getStreamType()))
                .tapOnPlayButton()
                .validateStreamStartPlaying(OWNED_AND_OPERATED)
                .validateStopButtonDisplayed();
    }

    @Issues({@Issue("DROID-14653"), @Issue("DROID-14103")})
    @TestCaseIds({@TestCaseId("736115"), @TestCaseId("724823"), @TestCaseId("749196"), @TestCaseId("749727"),
            @TestCaseId("749605"), @TestCaseId("749608"), @TestCaseId("749475")})
    @Test(description = "Check Stream speed adjustment", groups = {NOW_PLAYING_TEST, ACCEPTANCE_TEST})
    public void testStreamSpeedAdjustment() {
        Users randomUser = generateRandomUser();
        signUpPage.signUpFlowForUser(randomUser);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .validateSpeedAdjustmentValue(SPEED_VALUE_1_0)
                .minimizeNowPlayingScreen();
        navigationAction.tapBackButtonIfDisplayed();
        userProfilePage.signOutUserFlow();

        deepLinksUtil.openContentProfileThroughDeeplink(STREAM_FREE_PODCAST_WITH_LONG_EPISODE);
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 1);
        nowPlayingPage.tapOnSpeedButton();
        nowPlayingSpeedPlaybackDialog.validateUIElements(nowPlayingSpeedPlaybackDialog.speedPlaybackDialogElements());
        nowPlayingSpeedPlaybackDialog.clickOnDoneButton();
        nowPlayingPage.stopStreamPlaying();
        for (String speedValue: SPEED_VALUES) {
            nowPlayingPage
                    .adjustPlaybackSpeed(speedValue, true)
                    .validateSpeedAdjustmentValue(speedValue);
        }
        nowPlayingPage.minimizeNowPlayingScreen();
        contentProfilePage.openContentProfileCategoryTypeByIndex(CATEGORY_TYPE_EPISODES, 2);
        nowPlayingPage
                .stopStreamPlaying()
                .validateSpeedAdjustmentValue(SPEED_VALUE_3_0);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateLiveLableIsDisplayed();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .stopStreamPlaying()
                .validateSpeedAdjustmentValue(SPEED_VALUE_3_0)
                .minimizeNowPlayingScreen();
        navigationAction.navigateTo(LIBRARY);

        signInPage.signInFlowForUser(USER_PREMIUM);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_AUDIOBOOK_GATSBY);
        nowPlayingPage
                .stopStreamPlaying()
                .validateSpeedAdjustmentValue(SPEED_VALUE_3_0);
    }

    @TestCaseId("749592")
    @Test(description = "Test Rewind or Fast Forward buttons for different content", groups = {NOW_PLAYING_TEST})
    public abstract void testForwardAndRewindButtonsAvailability();

    @TestCaseId("749604")
    @Test(description = "Test variable speed play back", groups = {NOW_PLAYING_TEST})
    public abstract void testVariableSpeedPlayback();

    @TestCaseId("749656")
    @Test(description = "Verify play pause buttons if app in background", groups = {NOW_PLAYING_TEST})
    public void testPlayPauseButtonIfAppRunningInBackGround() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        restartApp();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validatePauseButtonDisplayed();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validatePauseButtonDisplayed()
                .stopStreamPlaying()
                .validatePlayButtonDisplayed();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validatePlayButtonDisplayed()
                .validateMediaControlsButtonsLooksAsExpected(STREAM_STATION_WITHOUT_ADS);
    }

    @TestCaseIds({@TestCaseId("749671"), @TestCaseId("749673"), @TestCaseId("749614")})
    @Test(description = "Test play stream more than 30 seconds", groups = {NOW_PLAYING_TEST})
    public abstract void testPlayMoreThan30Seconds();

    @TestCaseId("749672")
    @Test(description = "Test play stream more than 10 seconds", groups = {NOW_PLAYING_TEST})
    public void testPlayMoreThan10Seconds() {
        int requiredTimeInSeconds = 10;
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage
                .tapNowPlayingRewindOrFastForwardButton(REWIND, 3)
                .playStreamMoreThanRequiredTime(requiredTimeInSeconds)
                .validateIsRewound(requiredTimeInSeconds)
                .validateStreamCloseToStart()
                .validateIsFastForwarded();
    }

    @TestCaseId("749590")
    @Test(description = "Verify play stop buttons if app in background", groups = {NOW_PLAYING_TEST})
    public void testPlayStopButtonIfAppRunningInBackGround() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .stopStreamPlaying()
                .validatePlayButtonDisplayed();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validatePlayButtonDisplayed()
                .tapOnPlayButton()
                .validatePauseButtonDisplayed();
        runAppInBackground(Duration.ofSeconds(3));
        nowPlayingPage
                .validateThatNowPlayingIsOpened()
                .validatePauseButtonDisplayed();
    }
  
    @TestCaseId("749475")
    @Test(description = "Test Podcast play back speed controls", groups = {NOW_PLAYING_TEST})
    public abstract void testPodcastPlaybackSpeedControls();

    @TestCaseId("749492")
    @Test(description = "More Options menu disappearance", groups = {NOW_PLAYING_TEST})
    public void testMoreOptionsMenuDisappearance() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog
                .validateMoreOptionsMenuDisplayed()
                .closeMoreOptionsDialog();
        nowPlayingMoreOptionsDialog.validateMoreOptionsMenuIsNotDisplayed();
    }

    @TestCaseId("749606")
    @Test(description = "Test variable speed presence in different stream types", groups = {NOW_PLAYING_TEST})
    public abstract void testVariableSpeedPlaybackDifferentContent();

    @TestCaseIds({@TestCaseId("749472"), @TestCaseId("749473")})
    @Test(description = "Check SleepTimer Icon Availability", groups = {NOW_PLAYING_TEST})
    public void testSleepTimerIconAvailability() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.validateSleepTimerIconDisplayed();
        nowPlayingPage.validateStateOfSleepTimer(false);

        nowPlayingPage.openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog
                .validateSleepTimerScreenIsDisplayed()
                .setRequiredSleepTimerOption(FIFTEEN);
        nowPlayingPage.validateStateOfSleepTimer(true);

        nowPlayingPage.openSleepTimerDialogThroughIcon();
        nowPlayingSleepTimerDialog.turnOffSleepTimer();
        nowPlayingPage.validateStateOfSleepTimer(false);
    }
}
