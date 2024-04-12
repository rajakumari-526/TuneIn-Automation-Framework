package com.tunein.stability.tests.android;

import com.codeborne.selenide.appium.SelenideAppium;
import com.opencsv.CSVWriter;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.HeadspinUtil;
import com.tunein.mobile.utils.ReporterUtil;
import com.tunein.stability.tests.common.ListeningTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.quiteAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.FAST_FORWARD;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.REWIND;
import static com.tunein.mobile.testdata.TestGroupName.STABILITY_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.getContentTypeValue;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.streamsForStabilityTesting;
import static com.tunein.mobile.testdata.models.Contents.getStreamFormat;
import static com.tunein.mobile.testdata.models.Contents.getStreamId;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.BrowserUtil.clickOnPlayButtonIfDisplayed;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GeneralTestUtil.DETAILED_DATE_PATTERN;
import static com.tunein.mobile.utils.HeadspinUtil.NetworkConfigurationProfileTypes.*;
import static com.tunein.mobile.utils.HeadspinUtil.downloadHarFileForSession;
import static com.tunein.mobile.utils.HeadspinUtil.getHeadSpinSessionUrlToReportPortal;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.TestResultsUtil.TestStatus.FAILED;
import static com.tunein.mobile.utils.TestResultsUtil.*;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class AndroidListeningTest extends ListeningTest {

    @Override
    @AfterMethod(alwaysRun = true)
    public void afterMethodInBaseTest(ITestResult testResult, Method method) {
        ReporterUtil.log("Start writing data to csv file");
        String stationName;
        String stationId;
        String mediaType;
        String reportPortalUrl;
        String headspinSessionId = String.valueOf(getAppiumDriver().getSessionId());
        String sessionIdUrl = "https://ui.headspin.io/sessions/" + headspinSessionId + "/waterfall";

        if (testResult.getParameters().length > 0) {
            Contents content = (Contents) testResult.getParameters()[0];
            mediaType = getStreamFormat(content);
            stationName = content.getStreamName();
            stationId = getStreamId(content);
        } else {
            stationName = Stream.of(streamsForStabilityTesting())
                    .map(content -> (Contents) content[0])
                    .map(Contents::getStreamName)
                    .collect(Collectors.joining(", "));
            stationId = Stream.of(streamsForStabilityTesting())
                    .map(content -> (Contents) content[0])
                    .map(content -> getStreamId(content))
                    .collect(Collectors.joining(", "));
            mediaType = "ignore";
        }

        String testName = method.getName();
        try {
            reportPortalUrl = getUrlForTestWithName(testName);
        } catch (Throwable throwable) {
            reportPortalUrl = getReportPortalUrl();
        }

        String testDescription = method.getAnnotation(Test.class).description();
        String status = testResult.isSuccess() ? "passed" : generalTestUtil.getStatusOfStream();
        if (testName.equals("testWeakNetworkCondition")) HeadspinUtil.setDeviceNetworkConfiguration(NETWORK_PROFILE_WIFI);
        int silenceThreshold = testName.equals("testInterruptPlayback") ? 50 : 20;

        if (config().appiumUninstallAppAfterTest()) uninstallApp();
        customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        quiteAppiumDriver();

        String silenceDetected = generalTestUtil.isSilenceDetectedForHeadspinSession(headspinSessionId, Duration.ofSeconds(silenceThreshold));
        if (testResult.isSuccess() && silenceDetected.contains("yes")) {
            if (!testName.equals("testUseOfMediaControls")) {
                status = "Silence is detected";
                setStatusForTestWithName(FAILED, testName);
            } else {
                status = "passed";
                silenceDetected = "ignore";
            }
        }

        String udid = getUDID();
        ReporterUtil.log("Device: " + udid + " with thread: " + Thread.currentThread().getId());

        if (!testName.equals("testWeakNetworkCondition")) downloadHarFileForSession(headspinSessionId);

        String testType = config().isDevPR() ? "PR" : "Daily";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DETAILED_DATE_PATTERN);
        Date startDate = new Date(testResult.getStartMillis());
        Date endDate = new Date(testResult.getEndMillis());
        String start = dateFormat.format(startDate.getTime());
        String end = dateFormat.format(endDate.getTime());
        String testDuration = TimeUnit.MILLISECONDS.toMinutes(testResult.getEndMillis() - testResult.getStartMillis()) + " minutes";
        if (testDuration.equals("0 minutes")) {
            testDuration = TimeUnit.MILLISECONDS.toSeconds(testResult.getEndMillis() - testResult.getStartMillis()) + " seconds";
        }

         String[] array = new String[] {
                 start,
                 end,
                 testDuration,
                 testName,
                 testDescription,
                 testType,
                 stationName,
                 stationId,
                 mediaType,
                 status,
                 silenceDetected,
                 udid,
                 reportPortalUrl,
                 sessionIdUrl
        };

        try {
            if (config().writeToFileStabilityResults()) {
                String masterCsvFilePath = config().stabilityTestsMasterFilePath() + config().stabilityTestsMasterFileName();
                CSVWriter writer = new CSVWriter(new FileWriter(masterCsvFilePath, true));
                writer.writeNext(array);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(
            description = "Listening in Car mode",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProviderStabilityTest",
            groups = {STABILITY_TEST}
    )
    public void testPlaybackDuringCarMode(Contents stream) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        deepLinksUtil.openTuneThroughDeeplink(stream);
        nowPlayingPage.minimizeNowPlayingScreen();
        navigationAction.navigateTo(CARMODE);
        customWait(Duration.ofSeconds(config().timeStabilityTestInSeconds()));
        carModePage.clickOnExitCardModeMode();
        miniPlayerPage.validateStreamStartsPlaying();
    }

    @Test(
            description = "Stream stability during screen switch",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProviderStabilityTest",
            groups = {STABILITY_TEST}
    )
    public void testStreamStabilityDuringScreenSwitch(Contents content) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        int timeoutValue = 270;
        if (config().isDevPR()) {
            timeoutValue = config().twoMinuteInSeconds();
        }
        NavigationActionItems[] pageList = {HOME, SEARCH, PREMIUM, PROFILE, LIBRARY};
        for (NavigationActionItems page : pageList) {
            switch (page) {
                case HOME, SEARCH, PREMIUM, LIBRARY -> {
                    nowPlayingPage.minimizeIfNowPlayingDisplayed();
                    navigationAction.navigateTo(page);
                    deepLinksUtil.openTuneThroughDeeplink(content);
                    customWait(Duration.ofSeconds(timeoutValue));
                    nowPlayingPage
                            .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                            .validateNowPlayingTitleIsEqualTo(content.getStreamName())
                            .minimizeNowPlayingScreen();
                    customWait(Duration.ofSeconds(timeoutValue));
                    miniPlayerPage.validateStreamStartsPlaying();
                }
                case PROFILE -> {
                    navigationAction.navigateTo(page);
                    deepLinksUtil.openTuneThroughDeeplink(content);
                    customWait(Duration.ofSeconds(timeoutValue));
                    nowPlayingPage
                            .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                            .validateNowPlayingTitleIsEqualTo(content.getStreamName())
                            .minimizeIfNowPlayingDisplayed();
                    userProfilePage.closeProfilePage();
                    customWait(Duration.ofSeconds(timeoutValue));
                    miniPlayerPage.validateStreamStartsPlaying();
                }
                default -> throw new IllegalStateException("Unexpected page: " + page);
            }
        }
    }

    @Test(
            description = "Switch between stations/podcasts",
            groups = {STABILITY_TEST}
    )
    public void testSwitchBetweenStationsAndPodcasts() {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        int timeoutValue = 11;
        if (config().isDevPR()) {
            timeoutValue = 5;
        }
        for (Object[] object : streamsForStabilityTesting()) {
            Contents stream = (Contents) object[0];
            deepLinksUtil.openTuneThroughDeeplink(stream);
            customWait(Duration.ofMinutes(timeoutValue));
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(stream.getStreamName());
        }
    }

    @Test(
            description = "Use media control buttons during listening",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamsForStabilityTesting",
            groups = {STABILITY_TEST}
    )
    public void testUseOfMediaControls(Contents content) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        deepLinksUtil.openTuneThroughDeeplink(content);
        boolean isRewindFastForwardButtonsDisplayed = nowPlayingPage.isRewindButtonIsDisplayed();
        int minutesTimeout = isRewindFastForwardButtonsDisplayed ? 10 : 20;
        if (config().isDevPR()) {
            minutesTimeout = isRewindFastForwardButtonsDisplayed ? 4 : 8;
        }
        customWait(Duration.ofMinutes(minutesTimeout));
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                .validateNowPlayingTitleIsEqualTo(content.getStreamName())
                .stopStreamPlaying();
        customWait(Duration.ofMinutes(config().isDevPR() ? 4 : 5));
        nowPlayingPage
                .tapOnPlayButton()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                .validateNowPlayingTitleIsEqualTo(content.getStreamName());
        customWait(Duration.ofMinutes(minutesTimeout));
        nowPlayingPage.validateStreamStartPlaying();
        if (isRewindFastForwardButtonsDisplayed) {
            nowPlayingPage
                    .tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD)
                    .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(content.getStreamName());
            customWait(Duration.ofMinutes(minutesTimeout));
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(content.getStreamName())
                    .tapNowPlayingRewindOrFastForwardButton(REWIND)
                    .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(content.getStreamName());
            customWait(Duration.ofMinutes(minutesTimeout));
            nowPlayingPage.validateStreamStartPlaying();
        }
    }

    @Test(
            description = "Regular listening",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamsForStabilityTesting",
            groups = {STABILITY_TEST}
    )
    public void testRegularListening(Contents stream) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        deepLinksUtil.openTuneThroughDeeplink(stream);
        customWait(Duration.ofSeconds(config().timeStabilityTestInSeconds()));
        nowPlayingPage
                .validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()))
                .validateNowPlayingTitleIsEqualTo(stream.getStreamName());
    }

    @Test(
            description = "Weak Network Condition",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamsForStabilityTesting",
            groups = {STABILITY_TEST}
    )
    public void testWeakNetworkCondition(Contents stream) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        int timeout = config().fiveMinutesInSeconds();
        if (config().isDevPR()) {
            timeout = config().twoMinuteInSeconds();
        }
        deepLinksUtil.openTuneThroughDeeplink(stream);
        customWait(Duration.ofSeconds(config().isDevPR() ? config().fourMinutesInSeconds() : config().fiveMinutesInSeconds()));
        nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()));

        HeadspinUtil.NetworkConfigurationProfileTypes[] networkTypes = {NETWORK_PROFILE_3G, NETWORK_PROFILE_WIFI, NETWORK_PROFILE_LTE};
        for (int i = 0; i < 2; i++) {
            for (HeadspinUtil.NetworkConfigurationProfileTypes type : networkTypes) {
                HeadspinUtil.setDeviceNetworkConfiguration(type);
                customWait(Duration.ofSeconds(timeout));
                nowPlayingPage.validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()));
            }
        }
    }

    @Test(
            description = "Check listening in background",
            dataProviderClass = ContentProvider.class,
            dataProvider = "oneStationAndPodcastDataProviderStabilityTest",
            groups = {STABILITY_TEST}
    )
    public void testListeningInBackground(Contents content) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        int timeout = 7;
        if (config().isDevPR()) {
            timeout = 2;
        }
        deepLinksUtil.openTuneThroughDeeplink(content);
        for (int i = 0; i < 5; i++) {
            runAppInBackground(Duration.ofSeconds(120));
            miniPlayerPage.extendMiniPlayerIfDisplayed(content);
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(content.getStreamName());
            customWait(Duration.ofMinutes(timeout));
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(content.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(content.getStreamName());
        }
    }

    @Test(
            description = "Test Locked screen",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamsForStabilityTesting",
            groups = {STABILITY_TEST}
    )
    public void testLockedScreen(Contents stream) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        deepLinksUtil.openTuneThroughDeeplink(stream);
        for (int i = 0; i < 5; i++) {
            customWait(Duration.ofMinutes(1));
            deviceNativeActions.lockDevice();
            customWait(Duration.ofMinutes(config().isDevPR() ? 2 : 5));
            deviceNativeActions.unlockDevice();
            switchToolTipDialog.clickOnNotNowButtonIfDisplayed();
            customWait(Duration.ofMinutes(config().isDevPR() ? 1 : 2));
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(stream.getStreamName());
        }
    }

    @Test(
            description = "Interrupt playback",
            dataProviderClass = ContentProvider.class,
            dataProvider = "streamsForStabilityTesting",
            groups = {STABILITY_TEST}
    )
    public void testInterruptPlayback(Contents stream) {
        ReporterUtil.log("Headspin session: " + getHeadSpinSessionUrlToReportPortal());
        SelenideAppium.terminateApp("com.google.android.youtube");
        deepLinksUtil.openTuneThroughDeeplink(stream);

        for (int i = 0; i < 3; i++) {
            customWait(Duration.ofSeconds(config().isDevPR() ? 300 : 600));
            nowPlayingPage
                    .validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()))
                    .validateNowPlayingTitleIsEqualTo(stream.getStreamName());

            ReporterUtil.log("Open youtube video");
            SelenideAppium.openAndroidDeepLink("vnd.youtube://RzVvThhjAKw", "com.google.android.youtube");
            clickOnPlayButtonIfDisplayed();
            customWait(Duration.ofSeconds(config().isDevPR() ? 100 : 270));
            takeScreenshot();
            activateApp();
            takeScreenshot();
            miniPlayerPage.extendMiniPlayerIfDisplayed(stream);
            if (isElementDisplayed(nowPlayingPage.nowPlayingPlayButton)) nowPlayingPage.tapOnPlayButton();
            nowPlayingPage
                    .waitUntilPageReadyWithKnownContent(stream)
                    .validateStreamStartPlaying(getContentTypeValue(stream.getStreamType()));
        }
    }

}
