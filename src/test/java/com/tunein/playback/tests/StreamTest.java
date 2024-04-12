package com.tunein.playback.tests;

import com.opencsv.CSVWriter;
import com.tunein.BaseTest;
import com.tunein.mobile.pages.alert.IosAlert;
import com.tunein.mobile.testdata.models.HeadspinStreams;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.Prompts.LOW_BATTERY_POPUP;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.getFirstLaunchValue;
import static com.tunein.mobile.testdata.dataprovider.HeadspinProvider.getContent;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.*;
import static com.tunein.mobile.utils.ApplicationUtil.closePermissionPopupFor;
import static com.tunein.mobile.utils.BrowserUtil.*;
import static com.tunein.mobile.utils.BrowserUtil.Browser.CHROME;
import static com.tunein.mobile.utils.BrowserUtil.Browser.SAFARI;
import static com.tunein.mobile.utils.HeadspinUtil.*;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.GeneralTestUtil.*;
import static com.tunein.mobile.utils.GeneralTestUtil.DeeplinkPrefixType.PODCAST;
import static com.tunein.mobile.utils.GeneralTestUtil.DeeplinkPrefixType.STATION;
import static com.tunein.mobile.utils.GeneralTestUtil.OnDemandData.*;

public class StreamTest extends BaseTest {

    protected File csvFile = null;

    protected FileWriter fileWriter = null;

    protected CSVWriter writer = null;

    private static final int TEST_DURATION = config().appiumStreamMonitorDuration() * 1000;

    /* --- Before Method --- */

    @BeforeMethod(alwaysRun = true)
    public void runBeforeMethod(ITestResult testResult, Method method) {

        HeadspinStreams streamsModule = new HeadspinStreams();
        setDeviceNetworkConfiguration(streamsModule);

        if (method.getName().equalsIgnoreCase(STREAMS_TEST_CASE_NAME) && config().appiumStreamWriteToFile()) {
            if (testResult.getMethod().getCurrentInvocationCount() == 0) {
                // Create a writer file and header line
                try {
                    String[] strArray = {
                            "media_type",
                            "station_name",
                            "station_id",
                            "timestamp",
                            "test_results",
                            "internet_speed",
                            "preroll_count",
                            "midroll_count",
                            "midroll_segment_count",
                            "device_id",
                            "headspin_url"
                    };

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String updatedTimestamp = String.valueOf(timestamp).replaceAll("[^\\dA-Za-z\\n]", "_");

                    String path = config().appiumReportFilePath();
                    String name = config().appiumReportFileName().replace(".csv", "");
                    String sessionId = String.valueOf(getAppiumDriver().getSessionId());
                    String file = path + name + "_" + sessionId + ".csv";

                    csvFile = new File(file);
                    fileWriter = new FileWriter(csvFile);
                    writer = new CSVWriter(fileWriter);
                    writer.writeNext(strArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        setIsStreamErrorPromptHandled(false);
        upsellPage.closeUpsellIfDisplayed();

        closePermissionPopupFor(CHOOSE_GOOGLE_ACCOUNT);

        // Sign in as a premium user, if `appium.premium.user.test` > 0
        if (getPremiumUserTestStatus()) {
            signInPage.signInFlowForUser(USER_PREMIUM);
        }

        // Set owner name
        if (!config().appiumStreamTestOwnerName().isEmpty()) {
            setOwnerName(config().appiumStreamTestOwnerName());
        }

        // Set Station name and id if different from `HeadspinProvider.getContent(getUDID())` method
        generalTestUtil.setOnDemandValues(STATION_ID);
        generalTestUtil.setOnDemandValues(STATION_NAME);

        // Extract serial id from device
        generalTestUtil.getDeviceSerialIdFlow(getContent(getUDID()));
        // Start internet speed test
        if (config().appiumStreamInternetSpeedTest()) {
            startInternetSpeedTest(isIos() ? SAFARI : CHROME);
        }
    }

    /* --- After Method --- */

    @AfterMethod(alwaysRun = true)
    public void runAfterMethod(ITestResult testResult, Method method) {
        // Close write session
        if (method.getName().equalsIgnoreCase(STREAMS_TEST_CASE_NAME) && config().appiumStreamWriteToFile()) {
            if (!testResult.getMethod().hasMoreInvocation()) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Delete Network Configuration Profile
        deleteDeviceNetworkConfigurationProfile(getUDID());

        // Stop the stream at the end of the test session
        if (!isStreamErrorPromptHandled() || getStreamTestStatus()) {
            nowPlayingPage.closePopUpIfDisplayed(LOW_BATTERY_POPUP);

            try {
                if (nowPlayingPauseStopButton.isDisplayed()) {
                    ReporterUtil.log("Attempting to tap on " + nowPlayingPauseStopButton.getAlias());
                    nowPlayingPage.stopStreamPlaying();
                }
            } catch (Throwable ignored) {
                takeScreenshot();
                String errorMessage = (getStreamTestStatusMessage().isEmpty())
                        ? "Unable to stop the stream, could be stream time-out or stream error"
                        : getStreamTestStatusMessage();
                throw new RuntimeException(errorMessage);
            }
        }

        // Sign out from the user account
        if (getPremiumUserTestStatus()) {
            nowPlayingPage.minimizeNowPlayingScreen();
            userProfilePage.signOutUserFlow();
        }
    }

    /* --- Test Case --- */

    @Test()
    public void testStreams() {
        HeadspinStreams stream = getContent(getUDID());

        // This values will override if `appium.stream.station.name` and `appium.stream.station.id` were set manually with workflow dispatch
        String stationName = (config().appiumStreamTestStationName().isEmpty()) ? stream.getStationName() : getOnDemandStationName();
        String stationId = (config().appiumStreamTestStationId().isEmpty()) ? stream.getStationId() : getOnDemandStationId();

        DeeplinkPrefixType contentType = (config().appiumStreamTestStationId().chars().count() > 6) ? PODCAST : STATION;
        generalTestUtil.deeplinkToNowPlayingScreen(contentType, stationId);

        /*
         * Leave this commented for some time
         *
         * After deeplinking into the stream, there are two expected use cases:
         * 1. Immediate `Stream Error` prompt
         * 2. Pre-roll ad followed by the Stream Error prompt
         *
         * Check for the Stream Error if the pre-roll ad is not present:
         *
         * if (waitVisibilityOfElement(advertisement, config().timeoutOneSecond()) == null) {
         *     streamTestUtil.checkForStreamError(stream, writer, "before preroll");
         * }
        */

        // An early Stream Error check.
        String sessionStart = (!getStreamTestStatus()) ? generalTestUtil.getDate(TIME_PATTERN) : "";

        // Check for the Stream Error if Now Playing elements not detected.
        // Possible places where stream error can be displayed before 'Connecting' / 'Buffering' event or right after
        try {
            // Setting setIsStreamErrorPromptHandled to false
            // because its set itself to true after deeplink-prompt
            setIsStreamErrorPromptHandled(false);
            generalTestUtil.advertisementLabelCheck();
        } catch (Throwable ignored) {
            ReporterUtil.log("--- Stream Tester Info: Checking for the \"Stream Error\" before preroll");
            generalTestUtil.checkForStreamError(stream, writer, "before preroll");
        }

        if (isIos() && config().isAppiumStreamTestEnabled() && getFirstLaunchValue()) {
            IosAlert.handleSimpleAlertIfPresent(USER_TRACKING);
        }

        // Start stream playback test
        sessionStart = generalTestUtil.getDate(TIME_PATTERN);
        setSessionStartDate(generalTestUtil.getDate(DATE_PATTERN));
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TEST_DURATION;

        ReporterUtil.log("--- Stream Tester Info: Playback test started");
        while (System.currentTimeMillis() < endTime && !getStreamTestStatusMessage().toLowerCase().contains("error")) {
            // Break early if stream error was reported early
            if (!getStreamTestStatus() && !getStreamTestStatusMessage().isEmpty()) break;

            generalTestUtil.preRollAdCheck(stream, writer, endTime);

            // Check for the stream error after buffering and after preroll
            String message = (!getStreamTestStatus()) ? "after buffering" : "after preroll";
            generalTestUtil.checkForStreamError(stream, writer, message);

            // Break early if stream error detected
            if (!getStreamTestStatus() && !getStreamTestStatusMessage().isEmpty()) break;

            generalTestUtil.checkForTheStreamSuccessAfterPreroll();
            generalTestUtil.infiniteBufferingCheck(stream, writer);

            // Break early if infinite buffering reported
            if (!getStreamTestStatus() && !getStreamTestStatusMessage().isEmpty()) break;

            generalTestUtil.midRollAdCheck(stream, writer, endTime);
            generalTestUtil.playbackCheck(stream, writer, endTime);
            generalTestUtil.bufferingCheck(stream, writer, endTime);

            if (playButton.isDisplayed()) break;
        }

        // Append data to master.csv file
        generalTestUtil.reportToMasterCsvFile(stream, sessionStart);

        // Stop the test if error prompt detected
        if (!getStreamTestStatus() && getStreamTestStatusMessage().toLowerCase().contains("error")) {
            String message = (!getStreamTestStatusMessage().isEmpty())
                    ? getStreamTestStatusMessage()
                    : "Encountered a stream error with the \"" + stationName + "\" stream";
            throw new RuntimeException(message);
        } else if (playButton.isDisplayed()) {
            throw new RuntimeException("Encountered a stream error with the \"" + stationName + "\" stream");
        }
    }

    @Test()
    public void testDifferentNetworkConditions() {
        HeadspinStreams stream = getContent(getUDID());

        // This values will override if `appium.stream.station.name` and `appium.stream.station.id` were set manually with workflow dispatch
        String stationName = (config().appiumStreamTestStationName().isEmpty()) ? stream.getStationName() : getOnDemandStationName();
        String stationId = (config().appiumStreamTestStationId().isEmpty()) ? stream.getStationId() : getOnDemandStationId();

        DeeplinkPrefixType contentType = (config().appiumStreamTestStationId().chars().count() > 6) ? PODCAST : STATION;
        generalTestUtil.deeplinkToNowPlayingScreen(contentType, stationId);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + TEST_DURATION;
        final long[] networkStartTime = {startTime};
        int i = 0;

        while (System.currentTimeMillis() < endTime) {
            List<NetworkConfigurationProfileTypes> networkConfigurationProfileTypes = Arrays.asList(NetworkConfigurationProfileTypes.values());
                if (System.currentTimeMillis() - networkStartTime[0] >= 10000) {
                    setDeviceNetworkConfiguration(networkConfigurationProfileTypes.get(i));
                    networkStartTime[0] = System.currentTimeMillis();
                    i = i + 1;
                }
        }

        // Stop the test if error prompt detected
        if (!getStreamTestStatus() && getStreamTestStatusMessage().toLowerCase().contains("error")) {
            String message = (!getStreamTestStatusMessage().isEmpty())
                    ? getStreamTestStatusMessage()
                    : "Encountered a stream error with the \"" + stationName + "\" stream";
            throw new RuntimeException(message);
        }
    }

}

    //TODO: Uncomment for the stream test with conditions
//    @Test(dataProviderClass = ContentProvider.class, dataProvider = "streamTesterDataProvider")
//    public void testStreamsWithStreamErrorChecks(Streams stream) {
//        setIsStreamErrorPromptHandled(false);
//        boolean checkForStreamRedirectBeforeStreamTestStarts = false;
//        ReporterUtil.log("--- Stream Tester Info: " + stream);
//
//        // --- Pre-Test Setup ---
//
//        ReporterUtil.log("--- Stream Tester Info: Closing Upsell if displayed");
//        upsellPage.closeUpsellIfDisplayed();
//
////        streamTestUtil.closeSystemPromptsOnHeadspinDevice(); // this takes for ever in Headspin!
//
//        ReporterUtil.log("--- Stream Tester Info: Opening deeplink" + DeepLinksUtil.DEEPLINK_PREFIX_TUNE_STATION);
//        deepLinksUtil.openTuneThroughDeeplink(DeepLinksUtil.DEEPLINK_PREFIX_TUNE_STATION + stream.getStationGuideId());
//
//        // --- Stream Test ---
//
//        // TODO: Add `config().appiumStreamTuneFromDeepLink()` check when open deeplink from safari is implemented
//        //streamTestUtil.playStreamFromSearch(stream, writer);
//
//        // --- Debug, remove when tests are ready to run on first launch
//        setFirstLaunchValue(getThreadId(), false);
//
//        // TODO: Move to a method
//        ReporterUtil.log("--- Stream Tester Info: Is first launch " + getFirstLaunchValue());
//
//        if (!getFirstLaunchValue()) {
//            ReporterUtil.log("Stream info: Wait for the pre-roll add to finish");
//            // Note: code below is the same as waitUntilPreRollAdDisappearIfDisplayed();
//            if (isElementDisplayed(nowPlayingPage.nowPlayingAdPreRollLogo, config().waitVeryShortTimeoutSeconds())) {
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                writer.writeNext(streamTestUtil.listOfStreamTestResults(stream, timestamp, "pre-roll ad"));
//                waitTillElementDisappear(nowPlayingPage.nowPlayingAdPreRollLogo, config().oneMinuteInSeconds());
//            }
//        }
//
//        ReporterUtil.log("--- Stream Tester Info: Wait until stream starts \"Opening...\" or \"Buffering...\"");
//        nowPlayingPage.waitUntilStreamStartOpeningOrBuffering();
//
//        if (config().isAppiumStreamTestEnabled()) {
//            IosAlert.handleSimpleAlertIfPresent(USER_TRACKING);
//        }
//
//        // Checking for the in-stream redirect message (in case when playing stream is not available in the region, or it's a premium content)
////        streamTestUtil.checkForStreamError(stream, config().timeoutForDeeplinkInMilliseconds(), writer);
//
//        String rightValue = "";
//        String leftValue = "";
//
//        for (int i = 0; i < config().appiumStreamMonitorDuration(); i++) {
//            String stationName = "";
//
////            // If stream error handled, do not run `waitUntilPageReadyWithKnownContentType` method
////            if (!isIsStreamErrorPromptHandled() && !checkForStreamRedirectBeforeStreamTestStarts) {
////                checkForStreamRedirectBeforeStreamTestStarts = true;
////                nowPlayingPage.waitUntilPageReadyWithKnownContentType(LIVE_STATION);
////                stationName = nowPlayingPage.nowPlayingTitle.getText();
////            }
//
//            // Report Stream Success, when LIVE label is detected
//            //rightValue = streamTestUtil.getNowPlayingLiveLabelValue(stream, writer);
//            rightValue = streamTestUtil.getNowPlayingCounterValue(stream, writer, RIGHT);
//
//            if (!rightValue.isEmpty()) {
//                if (rightValue.contains("-")) {
//                    if (config().appiumStreamWriteToFile()) {
//                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                        String message = "--- Stream Tester Info: pre-roll / mid-roll: " + leftValue + " > " + rightValue;
//                        writer.writeNext(streamTestUtil.listOfStreamTestResults(stream, timestamp, message));
//                    }
//                } else {
//                    streamTestUtil.reportStreamSuccess(rightValue, stream, writer);
//                }
//                continue;
//            }
//
//            if (isElementNotDisplayed(nowPlayingPage.nowPlayingLiveLabel, config().timeoutForDeeplinkInMilliseconds())) {
//                // Check if stream duration is present
//                try {
//                    ReporterUtil.log("--- Stream Tester Info: Getting stream duration text value");
//                    if (isElementDisplayed(nowPlayingPage.nowPlayingTimeCounterPassed, config().timeoutHalfASecond())) {
//                        leftValue = (getElementText(nowPlayingPage.nowPlayingTimeCounterPassed));
//                    }
//
//                    // Check if Advertisement text is present in now playing screen
//                    if (!leftValue.isEmpty() && !rightValue.isEmpty()) {
//                        ReporterUtil.log("--- Stream Tester Info: Checking if steam was redirected");
//                        if (leftValue.contains(":") && rightValue.contains("-") && config().appiumStreamWriteToFile()) {
//                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//                            String message = "--- Stream most-likely redirected to another station: " + leftValue + " > " + rightValue;
//                            writer.writeNext(streamTestUtil.listOfStreamTestResults(stream, timestamp, message));
//                            continue;
//                        }
//                        streamTestUtil.checkForTheAdvertisementLabel(stream, leftValue, rightValue, writer);
//                        continue;
//                    }
//
//                    // Check Stream Error prompt
//                    if (!leftValue.isEmpty()) {
//                        streamTestUtil.checkForUnknownStreamError(stream, writer);
//                        break;
//                    }
//                } catch (Error ignored) { }
//            }
//
////            streamTestUtil.checkForTheStreamTimeout(stream, writer);
////            streamTestUtil.checkForStreamError(stream, config().waitShortTimeoutSeconds(), writer);
//            //streamTestUtil.checkForStreamRedirect(stream, stationName, writer);
//        }
