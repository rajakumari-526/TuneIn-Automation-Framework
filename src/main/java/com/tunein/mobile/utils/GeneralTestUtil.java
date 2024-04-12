package com.tunein.mobile.utils;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.opencsv.CSVWriter;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.ScreenFacade;
import com.tunein.mobile.pages.alert.IosAlert;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.testdata.models.HeadspinStreams;
import io.appium.java_client.appmanagement.ApplicationState;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.deviceactions.DeviceNativeActions.MemoryInfoType.TOTAL_PSS;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStatus.BUFFERING;
import static com.tunein.mobile.pages.common.search.SearchPage.RESULTS;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.*;
import static com.tunein.mobile.utils.ApplicationUtil.closePermissionPopupFor;
import static com.tunein.mobile.utils.CommandLineProgramUtil.executeCommandUntilSuccessful;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.*;
import static com.tunein.mobile.utils.GeneralTestUtil.Counter.*;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;
import static org.openqa.selenium.By.id;

public class GeneralTestUtil extends ScreenFacade {

    /* --- Constants --- */

    public static final String DATE_PATTERN = "MM-dd-yyyy";

    public static final String TIME_PATTERN = "hh:mm:ss a";

    public static final String DETAILED_DATE_PATTERN = "MM-dd-yyyy hh:mm:ss a";

    public static final String SECONDS = "sec";

    public static final String MINUTES = "min";

    public static final String HOURS = "hour";

    public static final String STREAM_ERROR_PROMPT = "//*[@name=\"Error\"]";

    public static final String STREAM_ERROR_MESSAGE = "Stream Error";

    public static final String UNKNOWN_STREAM_ERROR_OCCURRED_MESSAGE = "An unknown stream error occurred";

    public static final String A_STREAM_CONNECTION_ERROR_OCCURRED_MESSAGE = "A stream connection error occurred";

    public static final String UNABLE_TO_REACH_TUNEIN_MESSAGE = "Unable to reach TuneIn";

    public static final String STREAMS_TEST_CASE_NAME = "testStreams";

    public static final String STATE_OPENING = "Opening...";

    public static final String STATE_CONNECTING = "Connecting...";

    public static final String STATE_BUFFERING = "Buffering...";

    public static final String LIVE = "LIVE";

    public static final String SUCCESS = "Playing";

    public static final String PARTNER_ID = (isIos()) ? "777" : "500";

    /* --- Selenide Elements --- */

    public static SelenideElement elementLeft = $(android(id("player_time_passed")).ios(id("leftTime"))).as("Left Timer");

    public static SelenideElement elementRight = $(android(id("player_time_left")).ios(id("rightTime"))).as("Right Timer");

    public static SelenideElement title = $(android(id("player_main_title")).ios(iOSNsPredicateString("name == 'titleLabel'"))).as("Title");

    public static SelenideElement subtitle = $(android(id("player_main_subtitle")).ios(iOSNsPredicateString("name == 'subtitleLabel'"))).as("Subtitle");

    public static SelenideElement advertisement = $(android(androidUIAutomator("text(\"Advertisement\").resourceId(\"tunein.player:id/player_main_title\")"))
            .ios(iOSNsPredicateString("label == 'Advertisement' AND name == 'titleLabel'")))
            .as("Advertisement label");

    public static SelenideElement pauseButton = $(android(androidUIAutomator("description(\"Pause Station\")"))
            .ios(id("pauseButton")))
            .as("Pause button");

    public static SelenideElement playButton = $(android(androidUIAutomator("description(\"Play\")"))
            .ios(id("playButton")))
            .as("Play button");

    public static SelenideElement stopButton = $(android(androidUIAutomator("description(\"Stop\")"))
            .ios(id("stopButton")))
            .as("Stop button");

    public static SelenideElement nowPlayingPauseStopButton = $(android(androidUIAutomator("descriptionMatches(\"^.*Stop|Pause.*$\")"))
            .ios(iOSNsPredicateString("name IN { 'stopButton', 'pauseButton'}")))
            .as("Now playing pause stop button");

    public static SelenideElement liveLabel = $(android(androidUIAutomator("textContains(\"LIVE\")"))
            .ios(iOSNsPredicateString("name == 'liveIndicatorLabel' OR label IN {'LIVE', 'PLAY LIVE'}")))
            .as("LIVE label");

    public static SelenideElement fastForwardButton = $(android(androidUIAutomator("description(\"scan forward\")"))
            .ios(id("skipForwardButton")))
            .as("Scan Forward button");

    public static SelenideElement rewindButton = $(android(androidUIAutomator("description(\"rewind\")"))
            .ios(id("skipBackButton")))
            .as("Rewind button");

    /* --- Integer Map --- */

    private static Map<Long, Integer> prerollTotalCountMap = new HashMap<>();

    private static Map<Long, Integer> midrollTotalCountMap = new HashMap<>();

    private static Map<Long, Integer> midrollTotalSegmentCountMap = new HashMap<>();

    private static Map<Long, Integer> previousTimeCounterMap = new HashMap<>();

    /* --- Boolean Map --- */

    private static Map<Long, Boolean> prerollCompleteMap = new HashMap<>();

    private static Map<Long, Boolean> streamPlayedSuccessfullyAfterPrerollMap = new HashMap<>();

    private static Map<Long, Boolean> streamTestStatusMap = new HashMap<>();

    private static Map<Long, Boolean> isStreamErrorPromptHandledMap = new HashMap<>();

    private static Map<Long, Boolean> premiumUserTestMap = new HashMap<>();

    /* --- String Map --- */

    private static Map<Long, String> numMidrollAdsPerSegmentMap = new HashMap<>();

    private static Map<Long, String> midrollPerSegmentCountMap = new HashMap<>();

    static Map<Long, String> streamTestStatusMessageMap = new HashMap<>();

    private static Map<Long, String> streamTestCsvFile = new HashMap<>();

    private static Map<Long, String> sessionDateMap = new HashMap<>();

    private static Map<Long, String> onDemandStationNameMap = new HashMap<>();

    private static Map<Long, String> onDemandStationIdMap = new HashMap<>();

    private static Map<Long, String> ownerNameMap = new HashMap<>();

    /** Internet speed test - Download Speed */
    private static Map<Long, String> downloadSpeedMap = new HashMap<>();

    /** Internet speed test - Upload Speed */
    private static Map<Long, String> uploadSpeedMap = new HashMap<>();

    private static Map<Long, String> serialIdMap = new HashMap<>();

    /* --- Setters --- */

    @Step("Setting \"On demand station name\" value to {ownerName}")
    public static void setOnDemandStationName(Long threadId) {
        String[] names = config().appiumStreamTestStationName().split(",");
        for (String name: names) {
            if (!onDemandStationNameMap.containsValue(name)) {
                onDemandStationNameMap.put(threadId, name);
            }
        }
    }

    @Step("Setting \"On demand stationId\" value to {ownerName}")
    public static void setOnDemandStationId(Long threadId) {
        String[] ids = config().appiumStreamTestStationId().split(",");
        for (String id: ids) {
            if (!onDemandStationIdMap.containsValue(id)) {
                onDemandStationIdMap.put(threadId, id);
            }
        }
    }

    @Step("Setting \"Test Owner\" value to {ownerName}")
    public static void setOwnerName(String ownerName) {
        ownerNameMap.put(Thread.currentThread().getId(), ownerName);
    }

    @Step("Setting \"is Stream Error Prompt Handled\" value to {isStreamErrorPromptHandled}")
    public static void setIsStreamErrorPromptHandled(boolean isStreamErrorPromptHandled) {
        isStreamErrorPromptHandledMap.put(Thread.currentThread().getId(), isStreamErrorPromptHandled);
    }

    @Step("Setting \"Number of Midroll Ads per Segment\" value to {midrollCount}")
    public static void setNumMidrollAdsPerSegment(String midrollCount) {
        String value = (getPreviousMidrollCount().isEmpty()) ? midrollCount : getPreviousMidrollCount() + "," + midrollCount;
        numMidrollAdsPerSegmentMap.put(Thread.currentThread().getId(), value + ",");
        setPreviousMidrollCount(value);
    }

    @Step("Setting \"Stream Test Status\" value to {streamTestStatus}")
    public static void setStreamTestStatus(boolean streamTestStatus) {
        streamTestStatusMap.put(Thread.currentThread().getId(), streamTestStatus);
    }

    @Step("Setting \"Stream Test Status Message\" value to {streamTestStatusMessage}")
    public static void setStreamTestStatusMessage(String streamTestStatusMessage) {
        streamTestStatusMessageMap.put(Thread.currentThread().getId(), streamTestStatusMessage);
    }

    @Step("Setting \"Preroll Complete Status\" value to {prerollComplete}")
    public static void setPrerollCompleteStatus(boolean prerollComplete) {
        prerollCompleteMap.put(Thread.currentThread().getId(), prerollComplete);
    }

    @Step("Setting \"Did stream played successfully after Preroll\" to {streamSuccess}")
    public static void setDidStreamPlayedSuccessfullyAfterPreroll(boolean streamSuccess) {
        streamPlayedSuccessfullyAfterPrerollMap.put(Thread.currentThread().getId(), streamSuccess);
    }

    @Step("Setting \"Preroll Count\" to {prerollCount}")
    public static synchronized void setPrerollCount(Integer prerollCount) {
        prerollTotalCountMap.put(Thread.currentThread().getId(), prerollCount);
    }

    @Step("Setting \"Midroll Count\" to {midrollCount}")
    public static synchronized void setMidrollCount(Integer midrollCount) {
        midrollTotalCountMap.put(Thread.currentThread().getId(), midrollCount);
    }

    @Step("Setting \"Previous Midroll Count\" to {segmentCount}")
    public static synchronized void setPreviousMidrollCount(String segmentCount) {
        midrollPerSegmentCountMap.put(Thread.currentThread().getId(), segmentCount);
    }

    @Step("Setting \"Previous Time Counter\" to {counter}")
    public static synchronized void setPreviousTimeCounter(Integer counter) {
        previousTimeCounterMap.put(Thread.currentThread().getId(), counter);
    }

    @Step("Setting \"Midroll Segment Count\" to {midrollCount}")
    public static synchronized void setMidrollSegmentCount(Integer midrollCount) {
        midrollTotalSegmentCountMap.put(Thread.currentThread().getId(), midrollCount);
    }

    @Step("Setting \"Session Start Date\" to {sessionDate}")
    public static void setSessionStartDate(String sessionDate) {
        sessionDateMap.put(Thread.currentThread().getId(), sessionDate);
    }

    @Step("Setting \"Premium User Test Status\"")
    public static synchronized void setPremiumUserTestStatus(Long threadId) {
        int premiumUserNumber = config().appiumPremiumUserTest();
        if (!premiumUserTestMap.containsKey(threadId)) {
            int numberOfTrueValues = premiumUserTestMap.values().stream().filter(val -> val).collect(Collectors.toList()).size();
            premiumUserTestMap.put(threadId, premiumUserNumber > 0 && numberOfTrueValues < premiumUserNumber);
        }
    }

    @Step("Setting \"Download Speed\" to {downloadSpeedValue}")
    public static void setDownloadSpeedValue(String downloadSpeedValue) {
        downloadSpeedMap.put(Thread.currentThread().getId(), downloadSpeedValue);
    }

    @Step("Setting \"Upload Speed\" to {uploadSpeedValue}")
    public static void setUploadSpeedValue(String uploadSpeedValue) {
        uploadSpeedMap.put(Thread.currentThread().getId(), uploadSpeedValue);
    }

    @Step("Setting \"Serial Identifier\" to {serialIdentifier}")
    public static void setSerialIdentifier(String serialIdentifier) {
        serialIdMap.put(Thread.currentThread().getId(), serialIdentifier);
    }

    /* --- Getters --- */

    @Step("Getting \"On demand station name\"")
    public static synchronized String getOnDemandStationName() {
        if (onDemandStationNameMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return onDemandStationNameMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"On demand station id\"")
    public static synchronized String getOnDemandStationId() {
        if (onDemandStationIdMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return onDemandStationIdMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Number of Midroll Ads per segment\"")
    public static synchronized String getNumMidrollAdsPerSegment() {
        if (numMidrollAdsPerSegmentMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return numMidrollAdsPerSegmentMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Serial Identifier\"")
    public static synchronized String getSerialIdentifier() {
        if (serialIdMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return serialIdMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Preroll Count\"")
    public static synchronized int getPrerollCount() {
        if (prerollTotalCountMap.get(Thread.currentThread().getId()) == null) {
            return 0;
        }
        return prerollTotalCountMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Midroll Count\"")
    public static synchronized int getMidrollCount() {
        if (midrollTotalCountMap.get(Thread.currentThread().getId()) == null) {
            return 0;
        }
        return midrollTotalCountMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Midroll segment count\"")
    public static synchronized int getMidrollSegmentCount() {
        if (midrollTotalSegmentCountMap.get(Thread.currentThread().getId()) == null) {
            return 0;
        }
        return midrollTotalSegmentCountMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Previous Midroll count\"")
    public static synchronized String getPreviousMidrollCount() {
        if (midrollPerSegmentCountMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return midrollPerSegmentCountMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Preroll complete Status\"")
    public static boolean getPrerollCompleteStatus() {
        if (prerollCompleteMap.get(Thread.currentThread().getId()) == null) {
            return false;
        }
        return prerollCompleteMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Previous time counter\"")
    public static synchronized int getPreviousTimeCounter() {
        if (previousTimeCounterMap.get(Thread.currentThread().getId()) == null) {
            return 0;
        }
        return previousTimeCounterMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Stream played successfully after Preroll\"")
    public static boolean didStreamPlayedSuccessfullyAfterPreroll() {
        if (streamPlayedSuccessfullyAfterPrerollMap.get(Thread.currentThread().getId()) == null) {
            return false;
        }
        return streamPlayedSuccessfullyAfterPrerollMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Stream test status\"")
    public static boolean getStreamTestStatus() {
        if (streamTestStatusMap.get(Thread.currentThread().getId()) == null) {
            return false;
        }
        return streamTestStatusMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Stream test status message\"")
    public static String getStreamTestStatusMessage() {
        if (streamTestStatusMessageMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return streamTestStatusMessageMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Owner name\"")
    public static String getOwnerName() {
        if (ownerNameMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return ownerNameMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"is Stream Error Prompt Handled\" value")
    public static boolean isStreamErrorPromptHandled() {
        if (isStreamErrorPromptHandledMap.get(Thread.currentThread().getId()) == null) {
            return false;
        }
        return isStreamErrorPromptHandledMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Session Start Date\" value")
    public static synchronized String getSessionStartDate() {
        if (sessionDateMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return sessionDateMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Premium User Test Status\" value")
    public static synchronized Boolean getPremiumUserTestStatus() {
        return premiumUserTestMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Download Speed\" value")
    public static synchronized String getDownloadSpeedValue() {
        if (downloadSpeedMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return downloadSpeedMap.get(Thread.currentThread().getId());
    }

    @Step("Getting \"Upload Speed\" value")
    public static synchronized String getUploadSpeedValue() {
        if (uploadSpeedMap.get(Thread.currentThread().getId()) == null) {
            return "";
        }
        return uploadSpeedMap.get(Thread.currentThread().getId());
    }

    /* --- Helper Methods --- */

    @Step("Check if session {headspinSessionId} contains silence during {silenceThreshold}")
    public String isSilenceDetectedForHeadspinSession(String headspinSessionId, Duration silenceThreshold) {
        return executeCommandUntilSuccessful("python3 /Users/serviceaccount/dev/smart-speaker-testing/detect_silence_for_session.py " + headspinSessionId + " " + silenceThreshold.toSeconds() + " 240 10", Duration.ofMinutes(15));
    }

    public String getStatusOfStream() {
        if (applicationUtil.getAppState() != RUNNING_IN_FOREGROUND) {
            return "Error, app crashed or not running in the foreground";
        } else {
            takeScreenshot();
            String statusTitle = "";
            if (isElementDisplayed(nowPlayingPage.nowPlayingSubtitle)) {
                statusTitle = nowPlayingPage.getTextValueOfNowPlayingSubtitle();
            } else if (isElementDisplayed(miniPlayerPage.miniPlayerStationPodcastInfo)) {
                statusTitle = miniPlayerPage.getStationInfo();
            }
            switch (statusTitle) {
                case STATE_BUFFERING -> {
                    return "Infinite buffering";
                }
                case STATE_OPENING -> {
                    return "Infinite opening";
                }
                case STATE_CONNECTING -> {
                    return "Infinite connecting";
                }
                case A_STREAM_CONNECTION_ERROR_OCCURRED_MESSAGE -> {
                    return "A stream connection error occurred";
                }
                case UNKNOWN_STREAM_ERROR_OCCURRED_MESSAGE -> {
                    return "An unknown stream error occurred";
                }
                case UNABLE_TO_REACH_TUNEIN_MESSAGE -> {
                    return "Unable to reach TuneIn";
                }
                default -> {
                    if (nowPlayingPage.isPlayButtonIsDisplayed() || isElementDisplayed(miniPlayerPage.miniPlayerPlayButton)) {
                        return "Stream is stopped";
                    } else if (deviceNativeActions.getDeviceMemoryInfo(TOTAL_PSS) > config().criticalMemoryValueBytes()) {
                        return "Device's memory issue";
                    }
                    return "Unknown error";
                }
            }
        }
    }

    /**
     * Closes LOCATION and NOTIFICATIONS system prompts
     */
    public void closeSystemPromptsOnHeadspinDevice() {
        if (config().isAppiumStreamTestEnabled()) {
            customWait(Duration.ofSeconds(config().waitVeryShortTimeoutMilliseconds()));
            closePermissionPopupFor(LOCATION);
            closePermissionPopupFor(NOTIFICATIONS);
        }
    }

    /**
     * Plays stream from the Search action method
     *
     * @param content Streams from data provider `streamTesterDataProvider`
     * @param writer  write to a .csv file
     */
    public void playStreamFromSearch(HeadspinStreams content, CSVWriter writer) {
        try {
            navigationAction.navigateTo(SEARCH);
            String searchByGuideId = DeepLinksUtil.SEARCH_BY_GUIDE_ID_PREFIX + "s" + content.getStationId();
            searchPage.searchByGuideIdAndOpenFirstInCategoryForStreamTest(searchByGuideId, RESULTS);
        } catch (Error ignored) {
            checkForStreamError(content, writer, "from search field");
        }
    }

    /**
     * Check if `Advertisement` text is present in now playing screen
     *
     * @param content Streams from data provider `streamTesterDataProvider`
     * @param left    value of the stream duration locator
     * @param right   value of the stream passed locator
     * @param writer  write to a .csv file
     */
    public void checkForTheAdvertisementLabel(HeadspinStreams content, String left, String right, CSVWriter writer) {
        ReporterUtil.log("--- Stream Tester Info: Checking if title contains \"Advertisement\" text");

        if (isElementDisplayed(nowPlayingPage.nowPlayingAdvertisementLabel, Duration.ofMillis(config().timeoutHalfASecond()))) {
            if (config().appiumStreamWriteToFile()) {
                reportStreamData(content, writer, String.valueOf(getPrerollCount()));
            }
            // TODO: This logic breaks mid-roll testing, removing for now
            // waitTillElementDisappear(nowPlayingPage.nowPlayingAdPreRollLogo, config().waitLongTimeoutSeconds());
        } else {
            if (config().appiumStreamWriteToFile()) {
                String message = "on-demand or non-pre-roll content is playing: " + left + " > " + right;
                reportStreamData(content, writer, String.valueOf(getPrerollCount()));
            }
            ReporterUtil.log("--- Stream Tester Info: Pre-roll / mid-roll finished");
        }
    }

    private int convertTimeCounterToNumericValue(HeadspinStreams content, CSVWriter writer, String counterValue) {
        int value = 0;

        // Split time counter to numeric string (seconds)
        String[] timePassedCounterArray = counterValue.replace(" ", "").replace("-", "").split(":");

        // Check if provided argument value is not numeric (example "Buffering..." or "Connecting..." or "Downloading...")
        try {
            value = Integer.parseInt(timePassedCounterArray[timePassedCounterArray.length - 1].replace(" ", ""));
        } catch (Exception ignored) {
            // Report "Buffering...", "Connecting..." or "Downloading..."
            reportStreamData(content, writer, counterValue);
        }
        return value;
    }

    @Step("Checking for the time counter passed and time counter left")
    public Boolean isDurationAndTimePassedDisplayed() {
        try {
            if (isElementDisplayed(nowPlayingPage.nowPlayingTimeCounterPassed) && isElementDisplayed(nowPlayingPage.nowPlayingTimeCounterLeft)) {
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    @Step("Checking if playing content is a podcast")
    public Boolean isPodcastPlayingCheck() {
        ReporterUtil.log("--- Stream Tester Info: Checking if podcast is playing instead of the preroll/midroll ad");

        if (rewindButton.exists()) {
            ReporterUtil.log(rewindButton.getAlias() + " detected");
            return true;
        }

        // TODO use selenide
//        if (waitVisibilityOfElement(nowPlayingPage.nowPlayingRewindButton, config().timeoutOneSecond()) != null) {
//            ReporterUtil.log("--- Stream Tester Info: \"Rewind\" button detected");
//            return true;
//        }
        ReporterUtil.log("--- Stream Tester Info: Playing content is not a podcast");
        return false;
    }

    @Step("Checking for mid-roll ad")
    public void midRollAdCheck(HeadspinStreams content, CSVWriter writer, long endTime) {
        int secondsLeft;
        int secondsRight;
        int segment = 1;
        int midroll = 0;
        String counterRight;
        String counterLeft;

        // Get updated stream counter values
        counterRight = getCounterValue(RIGHT);
        counterLeft = getCounterValue(LEFT);

        // Entering Midroll reporting check midRollAdCheck method
        ReporterUtil.log("--- Stream Tester Info: Checking for the midroll ad");
        if (!counterRight.isEmpty() && !counterLeft.isEmpty()) {
            if (isPodcastPlayingCheck()) return;

            // Convert counter value to seconds
            secondsLeft = convertTimeCounterToNumericValue(content, writer, counterLeft);
            secondsRight = convertTimeCounterToNumericValue(content, writer, counterRight);

            int prerollWaitTime = secondsRight + secondsLeft;
            while (!counterLeft.isEmpty() && !counterRight.isEmpty()) {
                ReporterUtil.log("--- Stream Tester Info: Start reporting midroll ad duration");
                if (System.currentTimeMillis() - endTime > prerollWaitTime) break;
                segment = getMidrollSegmentCount() + 1;

                // Set `num_midroll_segments` counter
                if (getPreviousTimeCounter() < secondsRight) {
                    midroll = midroll + 1;
                    setMidrollCount(getMidrollCount() + 1);
                }

                setPreviousTimeCounter(secondsRight);
                reportStreamData(content, writer, counterRight, segment + " : " + midroll);

                counterLeft = getCounterValue(LEFT);
                counterRight = getCounterValue(RIGHT);
                secondsRight = convertTimeCounterToNumericValue(content, writer, counterRight);
            }
            // reportStreamData(content, writer, (!counterRight.isEmpty()) ? counterRight : counterLeft);

            // Set midroll segment and midroll data to be collected by Master csv
            setMidrollSegmentCount(getMidrollSegmentCount() + 1);
            setNumMidrollAdsPerSegment(String.valueOf(midroll));

            isPlayButtonDisplays(content, writer, "after midroll");
        }
//
//        // Exit method early if second pre-roll did not finish
//        if (!getPrerollCompleteStatus()) {
//            return;
//        }
//
//        // TODO investigate, how to improve this method so it would return value in less than 5 seconds
//        counterLeft = streamTestUtil.getCounterValue(LEFT);
//        counterRight = streamTestUtil.getCounterValue(RIGHT);
//
//        long start = System.currentTimeMillis();
//        while (!counterRight.isEmpty() && !counterLeft.isEmpty()) {
//            // Convert stream counter to seconds
//            secondsLeft = convertTimeCounterToNumericValue(stream, writer, counterLeft);
//            secondsRight = convertTimeCounterToNumericValue(stream, writer, counterRight);
//
//            if (System.currentTimeMillis() - start > config().fiveMinuteInMilliseconds()) {
//                ReporterUtil.log("midroll timeout" + config().fiveMinuteInMilliseconds() + " ms ");
//                return;
//            }
//
//            // Set midroll count and midroll segment count
//            if (getPreviousTimeCounter() < secondsRight) {
//                setMidrollCount(getMidrollCount() + 1);
//                setMidrollPerSegmentCount(midrollCountInSegment++);
//                array = new String[]{String.valueOf(getMidrollSegmentCount() + 1), String.valueOf(midrollCountInSegment)};
//            }
//
//            setPreviousTimeCounter(secondsRight);
//            reportStreamSuccess(stream, writer, secondsLeft + " : " + secondsRight, array);
//
//            counterRight = streamTestUtil.getCounterValue(RIGHT);
//            counterLeft = streamTestUtil.getCounterValue(LEFT);
//        }
//        setMidrollSegmentCount(getMidrollSegmentCount() + 1);
//
//        // Wait for the Connecting... Buffering... to stop display after each midroll segment
//        nowPlayingPage.waitUntilStreamStopOpeningAndBuffering();
    }

    @Step("Checking for the Advertisement label in the now playing screen")
    public void advertisementLabelCheck() {
        String advertisementLabel = advertisement.getAlias();
        ReporterUtil.log("Waiting for the to preroll ad to load with " + advertisementLabel);

        try {
            if (waitVisibilityOfElement(advertisement, Duration.ofSeconds(config().timeoutOneSecond())) == null) {
                ReporterUtil.log(advertisementLabel + " not found");
                if (isDurationAndTimePassedDisplayed()) {
                    nowPlayingPage
                            .waitUntilStreamStartOpeningOrBuffering()
                            .waitUntilStreamStopOpeningAndBuffering();
                }
            }
        } catch (Exception ignored) {
            ReporterUtil.log(advertisementLabel + " not found, continue with the preroll check");
        }
        ReporterUtil.log(advertisementLabel + " detected");
    }

    @Step("Checking for pre-roll ad")
    public void preRollAdCheck(HeadspinStreams content, CSVWriter writer, long endTime) {
        int secondsLeft;
        int secondsRight;
        String counterRight;
        String counterLeft;
        // TODO investigate how to return immediately
        boolean isPodcast;

        // Exit early
        if (didStreamPlayedSuccessfullyAfterPreroll() || getPrerollCompleteStatus()) {
            ReporterUtil.log("--- Stream Tester Info: Preroll Complete Status: " + getPrerollCompleteStatus());
            return;
        } else if (getMidrollCount() > 0 || getPrerollCount() >= 2) {
            setPrerollCompleteStatus(true);
            ReporterUtil.log("--- Stream Tester Info: Preroll Completed: " + getPrerollCompleteStatus());
            return;
        }

        // Get updated stream counter values
        counterRight = getCounterValue(RIGHT);
        counterLeft = getCounterValue(LEFT);

        // Entering pre-roll check
        if (!counterRight.isEmpty() && !counterLeft.isEmpty()) {
            // Convert stream counter to seconds
            secondsLeft = convertTimeCounterToNumericValue(content, writer, counterLeft);
            secondsRight = convertTimeCounterToNumericValue(content, writer, counterRight);

            if (!isLongPreRollDisplayed(counterRight)) {
                ReporterUtil.log("--- Stream Tester Info: Starting pre-roll check");

                int retryBeforeError = 0;
                int prerollWaitTime = secondsRight + secondsLeft;
                isPodcast = isPodcastPlayingCheck();

                while (!counterLeft.isEmpty() && !counterRight.isEmpty()) {
                    if (System.currentTimeMillis() - endTime > prerollWaitTime || isPodcast) {
                        break;
                    }

                    // Set `num_preroll_ads` counter
                    if (getPreviousTimeCounter() < secondsRight) {
                        setPrerollCount(getPrerollCount() + 1);
                    }

                    // Check for the infinite preroll spinner bug (-00:00)
                    if (getPreviousTimeCounter() == secondsRight) {
                        if (retryBeforeError < 2) {
                            // Check if the stream is still buffering before raising an error
                            // The stream may take longer to reconnect
                            nowPlayingPage
                                    .waitUntilStreamStartOpeningOrBuffering()
                                    .waitUntilStreamStopOpeningAndBuffering();
                        } else {
                            setStreamTestStatus(false);
                            setStreamTestStatusMessage("Infinite pre-roll spinner error");
                            break;
                        }
                        retryBeforeError++;
                    }

                    setPreviousTimeCounter(secondsRight);
                    reportStreamData(content, writer, counterRight);

                    counterLeft = getCounterValue(LEFT);
                    counterRight = getCounterValue(RIGHT);
                    secondsRight = convertTimeCounterToNumericValue(content, writer, counterRight);

                    // Exit early if playing content is either podcast or topic
                    isPodcast = isPodcastPlayingCheck();
                }
            } else {
                ReporterUtil.log("--- Stream Tester Info: Long pre-roll is displayed, clicking on \"Skip Ad\" button");
                setPreviousTimeCounter(secondsRight);
                reportStreamData(content, writer, counterRight);
            }
            reportStreamData(content, writer, (!counterRight.isEmpty()) ? counterRight : counterLeft);
        }
    }

    @Step("Checking for \"Buffering...\", \"Opening...\" and \"Connecting...\" states")
    public void bufferingCheck(HeadspinStreams content, CSVWriter writer, long endTime) {
        // iOS
        String rightValue;
        String leftValue;
        // Android
        String bufferingValue;

        // Check for the app crash
        checkAppState(content, writer, RUNNING_IN_FOREGROUND);

        // Exit early if getStreamTestStatus is false and contains error message
        if (!getStreamTestStatusMessage().isEmpty() && !getStreamTestStatus()) return;

        ReporterUtil.log("--- Stream Tester Info: Checking for \"Buffering...\", \"Opening...\" and \"Connecting...\" states");
        // TODO if element is not available it may take up to 5 seconds to return null
        if (isIos()) {
            leftValue = getCounterValue(LEFT);
            rightValue = getCounterValue(RIGHT);

            while (!leftValue.isEmpty() && rightValue.isEmpty()) {
                if (System.currentTimeMillis() - endTime > config().waitCustomTimeoutMilliseconds()) {
                    break;
                }
                reportStreamData(content, writer, leftValue);
                if (getStreamTestStatusMessage().contains(STREAM_ERROR_MESSAGE)) {
                    ReporterUtil.log("--- Stream Tester Info: \"" + STREAM_ERROR_MESSAGE + "\" detected");
                    break;
                }
                leftValue = getCounterValue(LEFT);
                rightValue = getCounterValue(RIGHT);
            }
        } else {
            // While Pause / Stop button not displayed, check for the Buffering state
            while (waitVisibilityOfElement(nowPlayingPage.nowPlayingPauseStopButton, Duration.ofSeconds(config().timeoutOneSecond())) == null) {
                if (System.currentTimeMillis() - endTime > config().waitCustomTimeoutMilliseconds()) {
                    break;
                }
                // Get Buffering value
                bufferingValue = getCounterValue(SUBTITLE);

                String[] states = {STATE_BUFFERING, STATE_CONNECTING, STATE_OPENING};
                for (String state : states) {
                    if (bufferingValue.contains(state)) {
                        // Report buffering event
                        reportStreamData(content, writer, state);
                        break;
                    }
                }
                if (getStreamTestStatusMessage().contains(STREAM_ERROR_MESSAGE)) {
                    ReporterUtil.log("--- Stream Tester Info: \"" + STREAM_ERROR_MESSAGE + "\" detected");
                    break;
                }
            }
        }
        isPlayButtonDisplays(content, writer, "after buffering");
    }

    @Step("Checking for the stream playback status")
    public void playbackCheck(HeadspinStreams content, CSVWriter writer, long endTime) {
        String iosDurationValue = "";
        SelenideElement pauseOrStopButton = nowPlayingPage.nowPlayingPauseStopButton;
        SelenideElement liveLabel = (isPodcastPlayingCheck()) ? nowPlayingPage.nowPlayingFastForwardButton : nowPlayingPage.nowPlayingLiveLabel;
        String playbackSuccess = (isPodcastPlayingCheck()) ? SUCCESS : LIVE;

        int timeout = config().timeoutOneSecond();

        /** is stream (Station) playing on iOS?
         * ELEMENT_ID           LOCATION_IOS           LOCATION_ANDROID
         * "LIVE or PLAY LIVE   middle                 subtitle
         * "Opening..."         left side              subtitle
         * "Buffering..."       left side              subtitle
         * "Connecting..."      left side              subtitle
         */

        ReporterUtil.log("--- Stream Tester Info: Checking for the playback success status");

        // Check if the PLAY button displayed during playback test
        isPlayButtonDisplays(content, writer, "after midroll");

        if (isIos()) {
            // TODO if element is not available it may take up to 5 seconds to return null
            iosDurationValue = getCounterValue(LEFT);

            // While PAUSE/STOP and stream duration elements are present report as playback success
            while (iosDurationValue.isEmpty() && waitVisibilityOfElement(liveLabel, Duration.ofSeconds(timeout)) != null) {
                if (System.currentTimeMillis() > endTime) {
                    break;
                }
                ReporterUtil.log("--- Stream Tester Info: \"" + LIVE + "\" label detected");
                reportStreamData(content, writer, playbackSuccess);
                iosDurationValue = getCounterValue(LEFT);
            }
        } else {
            // Check if the PLAY button displayed during playback test
            isPlayButtonDisplays(content, writer, "after midroll");

            // While PAUSE/STOP and LIVE elements are present report as playback success
            while (waitVisibilityOfElement(pauseOrStopButton, Duration.ofSeconds(timeout)) != null && waitVisibilityOfElement(liveLabel, Duration.ofSeconds(timeout)) != null) {
                if (System.currentTimeMillis() > endTime) {
                    break;
                }
                ReporterUtil.log("--- Stream Tester Info: \"" + LIVE + "\" label detected");
                reportStreamData(content, writer, playbackSuccess);
            }
        }
    }

    @Step("Checking if the Play button is visible {message}")
    private void isPlayButtonDisplays(HeadspinStreams content, CSVWriter writer, String message) {
        Duration timeout = Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds());
        SelenideElement buffering = (isIos()) ? nowPlayingPage.nowPlayingTimeCounterPassed : nowPlayingPage.nowPlayingSubtitle;
        SelenideElement playButton = nowPlayingPage.nowPlayingPlayButton;

        // Check if Play button is displayed after midroll/preroll to detect stream timeout
        try {
            if (isElementDisplayed(playButton)) {
                String info = "\"Play\" button was detected,";

                // Wait for the Buffering state to disappear
                try {
                    if (isElementDisplayed(buffering)) {
                        nowPlayingPage.waitUntilStreamStartOpeningOrBuffering(timeout);
                        waitTextOfElementToDisappear(buffering, BUFFERING.getNowPlayingStatusValue(), Duration.ofSeconds(config().waitLongTimeoutSeconds()));
                    }
                } catch (Exception ignored) {
                    // Ignore exceptions if Buffering state is not present
                }

                // If Play button still present, raise an error
                if (waitVisibilityOfElement(playButton, timeout) != null) {
                    ReporterUtil.log(info + " checking for the \"" + STREAM_ERROR_MESSAGE + "\"");
                    checkForStreamError(content, writer, message);
                    checkForTheStreamStatus(content, writer);
                }
                ReporterUtil.log("\"Play\" button disappeared after " + timeout + " sec");

            }
        } catch (Exception ignored) {
            ReporterUtil.log("\"Play\" button is not detected " + message);
        }
    }

    @Step("Checking for the stream timeout")
    public void checkForTheStreamSuccessAfterPreroll() {
        // Set locator for the playing content type (episode id vs station id)
        SelenideElement element = (config().appiumStreamTestStationId().chars().count() > 6)
                ? fastForwardButton
                : liveLabel;

        boolean isStreamPlaying = waitVisibilityOfElement(element, Duration.ofSeconds(config().elementVisibleTimeoutSeconds())) != null;

        // Checks if stream starts playing after first preroll, then sets appropriate value
        if (!didStreamPlayedSuccessfullyAfterPreroll() && isStreamPlaying) {
            setDidStreamPlayedSuccessfullyAfterPreroll(true);
            setPrerollCompleteStatus(true);
        }
    }

    private String getStreamConnectionErrorMessage() {
        if (waitVisibilityOfElement(nowPlayingPage.nowPlayingPauseButton, Duration.ofSeconds(config().timeoutOneSecond())) == null) {
            String attribute = isIos() ? "label" : "name";
            ReporterUtil.log("--- Stream Tester Info: \"Pause\" button not detected");

            String[] states = {STATE_OPENING, STATE_BUFFERING, STATE_CONNECTING};
            for (String state : states) {
                try {
                    if (isElementDisplayed(nowPlayingPage.nowPlayingSubtitle)) {
                        if (nowPlayingPage.nowPlayingSubtitle.getAttribute(attribute).contains(state)) {
                            setStreamTestStatus(false);
                            setStreamTestStatusMessage(state);
                            return state;
                        }
                    }
                } catch (Error ignored) {
                    ReporterUtil.log("--- Stream Tester Info: " + state + " not detected");
                }
            }
        }
        return "";
    }

    private boolean isLongPreRollDisplayed(String counter) {
        String[] timePassedCounterArray = counter.replace(" ", "").replace("-", "").split(":");
        int minutes = Integer.parseInt(timePassedCounterArray[timePassedCounterArray.length - 2].replace(" ", ""));
        String message = (minutes > 0) ? "minutes" : "seconds";
        ReporterUtil.log("--- Stream Test Info: Long Pre-roll ad is detected that is " + minutes + " " + message + " long");
        return minutes > 0;
    }

    @Step("Check for the mid-roll ad")
    private boolean isMidRollDisplayed() {
        try {
            if (!isElementDisplayed(fastForwardButton) && isElementDisplayed(elementRight)) {
                ReporterUtil.log("Mid-roll ad detected");
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    /**
     * This method checks for the `Unknown Stream Error`
     *
     * @param content stationGuideId from data provider `streamTesterDataProvider`
     * @param writer  write to a .csv file
     */
    public void checkForUnknownStreamError(HeadspinStreams content, CSVWriter writer) {
        String errorMessage = " \"" + UNKNOWN_STREAM_ERROR_OCCURRED_MESSAGE + "\" error message";
        ReporterUtil.log("--- Stream Tester Info: Checking for the \"" + errorMessage);

        if (isElementDisplayed(nowPlayingPage.nowPlayingTimeCounterPassed, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
            try {
                ElementHelper.AttributeType attribute = BasePage.isAndroid() ? NAME : LABEL;
                String unknownStreamErrorOccurred = getElementAttributeValue(nowPlayingPage.nowPlayingTimeCounterPassed, attribute);

                if (unknownStreamErrorOccurred.equals(UNKNOWN_STREAM_ERROR_OCCURRED_MESSAGE)) {
                    ReporterUtil.log("--- Stream Tester Info:" + errorMessage + " occurred");
                    if (config().appiumStreamWriteToFile()) {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        writer.writeNext(listOfStreamTestResults(content, timestamp, unknownStreamErrorOccurred));
                    }
                    throw new Error("failed to play the stream" + errorMessage);
                }
            } catch (Error ignored) {
                throw new Error("Failed to detect" + errorMessage);
            }
        }
    }

    public enum StreamErrorsType {
        UNABLE_TO_REACH_TUNEIN(UNABLE_TO_REACH_TUNEIN_MESSAGE),
        CONNECTION_ERROR_OCCURRED(A_STREAM_CONNECTION_ERROR_OCCURRED_MESSAGE),
        UNKNOWN_STREAM_ERROR_OCCURRED(UNKNOWN_STREAM_ERROR_OCCURRED_MESSAGE),
        STREAM_ERROR(STREAM_ERROR_MESSAGE);

        private String value;

        private StreamErrorsType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * This method checks for the error messages in the now playing screen
     *
     * @param errorType error text that needs to be checked for
     * @param message   where the stream error is being checked
     * @param content   known content profile type
     * @param writer    write to a .csv file
     * @param error     buffering value
     */
    public void reportStreamErrorTypeIfDisplayed(
            StreamErrorsType errorType,
            String message,
            HeadspinStreams content,
            CSVWriter writer,
            String... error
    ) {
        if (isIos() && !isStreamErrorPromptHandled() && getPrerollCompleteStatus()) {
            IosAlert.handleSimpleAlertIfPresent(OK);
            ReporterUtil.log("Closing \"" + STREAM_ERROR_MESSAGE + "\" prompt");
        }

        if (isIos() && isStreamErrorPromptHandled()) {
            String value = (errorType != null) ? errorType.getValue() : "";
            String errorMessage = STREAM_ERROR_MESSAGE + " " + value + " " + message;

            // Set stream status
            setStreamTestStatus(false);
            setStreamTestStatusMessage(errorMessage);

            // Write to master csv file
            if (config().appiumStreamWriteToFile()) {
                reportStreamData(content, writer, errorMessage);
            }
        }
    }

    /**
     * Checks for the second stream error occurrence after redirecting to a different station
     *
     * @param content stationGuideId from data provider `streamTesterDataProvider`
     * @param writer  write to a .csv file
     * @param message detailed report message
     */
    @Step("Checking for the stream error {message}")
    public void checkForStreamError(HeadspinStreams content, CSVWriter writer, String message) {
        ReporterUtil.log("--- Stream Tester Info: Checking for the \"" + STREAM_ERROR_MESSAGE + "\"");
        String streamErrorMessage = getCounterValue((isIos()) ? LEFT : SUBTITLE);

        // Handle Error Prompt on iOS if displayed
        if (isIos() && config().isAppiumStreamTestEnabled() && streamErrorMessage.isEmpty()) {
            IosAlert.handleSimpleAlertIfPresent(OK);
            streamErrorMessage = getCounterValue(LEFT);
        }

        // Get StreamErrorsType
        String finalStreamErrorMessage = streamErrorMessage;
        StreamErrorsType streamErrorType = Arrays.stream(StreamErrorsType.values())
                .filter(x -> x.getValue().equals(finalStreamErrorMessage))
                .findFirst()
                .orElse(null);

        // Report stream error
        reportStreamErrorTypeIfDisplayed(
                streamErrorType,
                message,
                content,
                writer,
                (streamErrorType != null) ? streamErrorType.getValue() : streamErrorMessage
        );
    }

    @Step("Report false playback test status")
    private void reportPlaybackTestFailStatus(HeadspinStreams content, CSVWriter writer, String errorMessage) {
        if (config().appiumStreamWriteToFile()) {
            generalTestUtil.reportStreamData(content, writer, errorMessage);
        }
        setDidStreamPlayedSuccessfullyAfterPreroll(false);
        setStreamTestStatus(false);
        setStreamTestStatusMessage(errorMessage);
    }

    @Step("Wait for the Play/Pause/Stop buttons to be displayed")
    private void waitForPlayPauseStopButtonsToDisplay(HeadspinStreams content, CSVWriter writer, String errorMessage, Duration timeout) {
        try {
            if (waitVisibilityOfElement(nowPlayingPage.nowPlayingMainPlayPauseStopButton, Duration.ofSeconds(config().elementVisibleTimeoutSeconds())) == null) {
                reportPlaybackTestFailStatus(content, writer, errorMessage);
            }
        } catch (Exception ignored) {
            // Check if NP screen is in Connecting... state
            if (isIos() && waitVisibilityOfElement(nowPlayingPage.nowPlayingBufferingAnimation, Duration.ofSeconds(config().elementVisibleTimeoutSeconds())) != null) {
                // Wait for the Connecting... state to disappear
                nowPlayingPage.waitUntilStreamStopOpeningAndBuffering(timeout);
            }
        }
    }

    @Step("Check if Connecting or Buffering states are still displayed after {timeout} seconds timeout")
    private void checkIfConnectingBufferingStillDisplayedAfterTimeout(HeadspinStreams content, CSVWriter writer, Duration timeout) {
        if (isIos()) {
            try {
                String errorMessage = "Still Connecting... after " + timeout + " sec.";
                if (waitVisibilityOfElement(nowPlayingPage.nowPlayingBufferingAnimation, Duration.ofSeconds(config().timeoutOneSecond())) != null) {
                    reportPlaybackTestFailStatus(content, writer, errorMessage);
                }
            } catch (Exception ignored) {
                takeScreenshot();
                reportPlaybackTestFailStatus(content, writer, " Failed to detect buffering animation element");
            }
        }
    }

    /**
     * Checks for the infinite buffering error
     *
     * @param content stationGuideId from data provider `streamTesterDataProvider`
     * @param writer write to a .csv file
     */
    @Step("Checking for the infinite buffering")
    public void infiniteBufferingCheck(HeadspinStreams content, CSVWriter writer) {
        // Exit this method early if midroll ad detected
        if (isMidRollDisplayed()) return;

        ReporterUtil.log("--- Checking for the infinite buffering error");

        // Check for the app state
        checkAppState(content, writer, RUNNING_IN_FOREGROUND);

        String errorMessage = (getStreamConnectionErrorMessage().isEmpty())
                ? "Infinite buffering error"
                : getStreamTestStatusMessage();

        Duration bufferingCheckTimeout = Duration.ofSeconds(config().oneMinuteInSeconds());
        if (!getPrerollCompleteStatus() && !didStreamPlayedSuccessfullyAfterPreroll()) {
            waitForPlayPauseStopButtonsToDisplay(content, writer, errorMessage, bufferingCheckTimeout);
            checkIfConnectingBufferingStillDisplayedAfterTimeout(content, writer, bufferingCheckTimeout);
        }
    }

    @Step("Checking for the stream status")
    public void checkForTheStreamStatus(HeadspinStreams content, CSVWriter writer) {
        try {
            nowPlayingPage.nowPlayingPlayButton.isDisplayed();
            // Write to master csv file
            if (config().appiumStreamWriteToFile()) reportStreamData(content, writer, "Stream stopped");
            // Set stream status
            setStreamTestStatus(false);
            setStreamTestStatusMessage("Stream stopped");
        } catch (Exception ignored) {
            ReporterUtil.log("--- Stream Tester Info: \"Play\" button was not detected");
            takeScreenshot();
        }
    }

    @Step("Report application not running in the foreground state")
    private void reportAppState(HeadspinStreams content, CSVWriter writer) {
        String message = "Error, app crashed or not running in the foreground";
        if (config().appiumStreamWriteToFile()) reportStreamData(content, writer, message);
        setStreamTestStatus(false);
        setStreamTestStatusMessage(message);
    }

    @Step("Checking if application state is {applicationState}")
    public void checkAppState(HeadspinStreams content, CSVWriter writer, ApplicationState applicationState) {
        try {
            if (applicationUtil.getAppState() != applicationState) reportAppState(content, writer);
        } catch (Exception ignore) {
            takeScreenshot();
            reportAppState(content, writer);
            ReporterUtil.log("--- Stream Tester Info: App is not in the \"RUNNING_IN_FOREGROUND\" state");
        }
    }

    public void checkForStreamRedirect(HeadspinStreams expectedStationName, String actualStationName, CSVWriter writer) {
        ReporterUtil.log("--- Stream Tester Info: Checking for the stream redirect");
        if (!actualStationName.equals(expectedStationName.getStationName())) {
            String message = "Station \"" + expectedStationName.getStationName() + "\" redirected to \"" + actualStationName + "\"";
            if (config().appiumStreamWriteToFile()) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                writer.writeNext(listOfStreamTestResults(expectedStationName, timestamp, message));
            }
            takeScreenshot();
            throw new Error(message);
        }
    }

    public enum Counter {
        LEFT, RIGHT, SUBTITLE, TITLE
    }

    private String getAttributeValue(SelenideElement element, AttributeType attributeType) {
        int timeout = config().timeoutOneSecond();
        try {
            return ElementHelper.getElementAttributeValue(element, attributeType, timeout);
        } catch (Exception ignored) {
            ReporterUtil.log("Attribute not found");
            return "";
        }
    }

    @Step("Getting element value from {counterPosition}")
    public String getCounterValue(Counter counterPosition) {
        switch (counterPosition) {
            case LEFT -> {
                //return getAttributeValue(elementLeft, isAndroid() ? NAME : LABEL);
                try {
                    SelenideElement elementLeft = nowPlayingPage.streamTesterNowPlayingTimeCounterPassed;
                    return ElementHelper.getElementNameOrLabel(elementLeft, Duration.ofSeconds(config().timeoutOneSecond()));
                } catch (Exception ignored) {
                    return "";
                }
            }
            case RIGHT -> {
                //return getAttributeValue(elementRight, isAndroid() ? NAME : LABEL);
                try {
                    SelenideElement elementRight = nowPlayingPage.streamTesterNowPlayingTimeCounterLeft;
                    return ElementHelper.getElementNameOrLabel(elementRight, Duration.ofSeconds(config().timeoutOneSecond()));
                } catch (Exception ignored) {
                    return "";
                }
            }
            case SUBTITLE -> {
                // TODO Revert
                return getAttributeValue(subtitle, isAndroid() ? TEXT : LABEL);
            }
            case TITLE -> {
                // TODO Revert
                return getAttributeValue(title, isAndroid() ? TEXT : LABEL);
            }
            default -> throw new IllegalStateException("Unexpected value: " + counterPosition);
        }
    }

    public void reportStreamData(HeadspinStreams content, CSVWriter writer, String value, String... midrollValue) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String midrollSegmentAndMidrollCountValues = (midrollValue.length > 0) ? Arrays.toString(midrollValue) : "no midroll";

        if (config().appiumStreamWriteToFile()) {
            writer.writeNext(listOfStreamTestResults(content, timestamp, value, midrollSegmentAndMidrollCountValues));
        }
    }

    public void reportToMasterCsvFile(HeadspinStreams content, String start) {
        try {
            String masterCsvFilePath = config().appiumMasterFilePath() + config().appiumMasterFileName();
            CSVWriter writer = new CSVWriter(new FileWriter(masterCsvFilePath, true));
            writer.writeNext(masterCsvListOfData(content, start));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* --- Writer Helper Methods --- */

    public String[] listOfStreamTestResults(HeadspinStreams content, Timestamp timestamp, String counterValue, String... midrollValue) {
        String headspinSession = String.valueOf(getAppiumDriver().getSessionId());
        String sessionIdUrl = "https://ui.headspin.io/sessions/" + headspinSession + "/waterfall";
        String midrollSegmentCount = (midrollValue.length > 0) ? String.valueOf(midrollValue[0]) : String.valueOf(getMidrollSegmentCount());

        // This values will override if `appium.stream.station.name` and `appium.stream.station.id` were set manually with workflow dispatch
        String stationName = (config().appiumStreamTestStationName().isEmpty()) ? content.getStationName() : config().appiumStreamTestStationName();
        String stationId = (config().appiumStreamTestStationId().isEmpty()) ? content.getStationId() : config().appiumStreamTestStationId();
        String internetSpeed = getDownloadSpeedValue() + " MBps" + "/" + getUploadSpeedValue() + " MBps";

        return new String[]{
                content.getStreamType(),            // 1 - media_type
                stationName,                        // 2 - station_name
                stationId,                          // 3 - station_id
                String.valueOf(timestamp),          // 4 - timestamp
                counterValue,                       // 5 - test_results
                internetSpeed,                      // 6 - internet_speed
                String.valueOf(getPrerollCount()),  // 7 - preroll_count
                String.valueOf(getMidrollCount()),  // 8 - midroll_count
                midrollSegmentCount,                // 9 - midroll_segment_count
                content.getDeviceName(),            // 10 - device_id
                sessionIdUrl                        // 11 - headspin_url
        };
    }

    public String[] masterCsvListOfData(HeadspinStreams content, String sessionStart) {
        String headspinSession = String.valueOf(getAppiumDriver().getSessionId());
        String sessionIdUrl = "https://ui.headspin.io/sessions/" + headspinSession + "/waterfall";
        String video = "https://" + config().appiumHeadspinToken() + "@api-dev.headspin.io/v0/sessions/" + headspinSession + ".mp4";
        String isPrerollDetected = (getPrerollCount() > 0) ? "yes" : "no";
        String deviceName = content.getDeviceName();
        String networkType = content.getNetworkType();

        // Make sure to pass the following commands
        // -Drp.enable="true"
        // -Drp.project="playback_testing"
        // -Drp.launch="ANDROID_PLAYBACK_TESTING" / "IOS_PLAYBACK_TESTING"
        String reportPortalUrl = TestResultsUtil.getReportPortalUrl();

        // Get midroll ads per segment data ~ example [4,4,4,3,]
        String rawMidrollAdsData = getNumMidrollAdsPerSegment();

        // Remove very last comma from the string ~ example [4,4,4,3]
        String trimmedMidrollAdsData = (rawMidrollAdsData.endsWith(","))
                ? rawMidrollAdsData.substring(0, rawMidrollAdsData.length() - 1)
                : getNumMidrollAdsPerSegment();
        String midrollAdsData = "[" + trimmedMidrollAdsData + "]";

        // get stream status (Error Prompt)
        String status = (getStreamTestStatusMessage().isEmpty()) ? "passed" : getStreamTestStatusMessage();

        // Update `owner` column if premium user
        String owner = getStreamsTestOwnerName();

        // Update `test_duration` column
        String testDuration = config().appiumStreamMonitorDuration() + " (" + getTimestampTimeDifference(sessionStart) + ")";

        // This values will override if `appium.stream.station.name` and `appium.stream.station.id` were set manually with workflow dispatch
        String stationName = (config().appiumStreamTestStationName().isEmpty()) ? content.getStationName() : config().appiumStreamTestStationName();
        String stationId = (config().appiumStreamTestStationId().isEmpty()) ? content.getStationId() : config().appiumStreamTestStationId();
        String internetSpeed = getDownloadSpeedValue() + " MBps" + "/" + getUploadSpeedValue() + " MBps";

        return new String[] {
                testDuration,                                           // 1 - test_duration
                getSessionStartDate(),                                  // 2 - session_date
                getSessionStartDate() + " " + sessionStart,             // 3 - start_time
                getDate(DETAILED_DATE_PATTERN),                         // 4 - end_time
                isPrerollDetected,                                      // 5 - preroll_detected
                String.valueOf(getPrerollCount()),                      // 6 - num_preroll_ads
                String.valueOf(getMidrollSegmentCount()),               // 7 - num_midroll_segments
                midrollAdsData,                                         // 8 - num_midroll_ads_per_segment
                getSerialIdentifier(),                                  // 9 - serial_id
                owner,                                                  // 10 - owner
                stationName,                                            // 11 - station_name
                stationId,                                              // 12 - station_id
                PARTNER_ID,                                             // 13 - partner_id
                deviceName,                                             // 14 - device_name
                status,                                                 // 15 - status
                networkType,                                            // 16 - network_type
                internetSpeed,                                          // 17 - internet_speed
                reportPortalUrl,                                        // 18 - rp_url
                video,                                                  // 19 - mp4_url
                sessionIdUrl                                            // 20 - headspin_session
        };
    }

    public enum DeviceSerial {
        IPHONE_12("iPhone 12", "F01B1507-3D5A-44B3-99C5-76E198313234"),
        IPHONE_12_SHARED("iPhone 12 Shared", "6467E19B-7CFC-4190-A992-1B7377C1245D"),
        IPHONE_13("iPhone 13", "84DF2D0B-F34B-4783-A2D6-057232DC9362"),
        IPHONE_13_PRO_MAX("iPhone 13 Pro Max", "ACF4ED40-BB01-4880-8896-3DA1BBFD523B"),
        IPHONE_14("iPhone 14", "13E06A05-FDC1-4529-AB83-6F5F6D5F471E"),
        IPHONE_14_PRO("iPhone 14 Pro", "EA6AE843-23C2-47C1-AC7E-F2803F0C728E"),
        PIXEL_6("Pixel 6", "f080dd54-decc-4801-913a-c522305140df"),
        PIXEL_6A("Pixel 6a", "27c44598-3328-4852-8d67-0572168c3b66"),
        PIXEL_7_PRO("Pixel 7 Pro", "b661a505-523f-46dc-8539-504e53db7669"),
        MOTO_G5("moto g 5G (2022)", "117f4833-f766-4edb-a7a1-bb457a016f67"),
        GALAXY_S22("SM-S901U1", "33dc9df0-b62f-41bc-98c1-05647b1a22b5"),
        GALAXY_S22_5G("SM-S901U1 Shared", "7eedeb1a-d388-4c40-8a00-bdb772dc3d7d");

        private final String key;

        private final String value;

        private DeviceSerial(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getSerialIdKey() {
            return key;
        }

        public String getSerialIdValue() {
            return value;
        }
    }

    private static final Map<String, String> DEVICE_SERIALS_IOS = Map.of(
            DeviceSerial.IPHONE_12.getSerialIdKey(), DeviceSerial.IPHONE_12.getSerialIdValue(),
            DeviceSerial.IPHONE_12_SHARED.getSerialIdKey(), DeviceSerial.IPHONE_12_SHARED.getSerialIdValue(),
            DeviceSerial.IPHONE_13.getSerialIdKey(), DeviceSerial.IPHONE_13.getSerialIdValue(),
            DeviceSerial.IPHONE_13_PRO_MAX.getSerialIdKey(), DeviceSerial.IPHONE_13_PRO_MAX.getSerialIdValue(),
            DeviceSerial.IPHONE_14.getSerialIdKey(), DeviceSerial.IPHONE_14.getSerialIdValue(),
            DeviceSerial.IPHONE_14_PRO.getSerialIdKey(), DeviceSerial.IPHONE_14_PRO.getSerialIdValue()
    );

    private static final Map<String, String> DEVICE_SERIALS_ANDROID = Map.of(
            // DeviceSerial.PIXEL_6.getSerialIdKey(), DeviceSerial.PIXEL_6.getSerialIdValue(),
            // DeviceSerial.PIXEL_6A.getSerialIdKey(), DeviceSerial.PIXEL_6A.getSerialIdValue(),
            // DeviceSerial.PIXEL_7_PRO.getSerialIdKey(), DeviceSerial.PIXEL_7_PRO.getSerialIdValue(),
            DeviceSerial.MOTO_G5.getSerialIdKey(), DeviceSerial.MOTO_G5.getSerialIdValue()
            // --- Removing these devices, since they share the same name and only one serial id returns for both
            // DeviceSerial.GALAXY_S22.getSerialIdKey(), DeviceSerial.GALAXY_S22.getSerialIdValue(),
            // DeviceSerial.GALAXY_S22_5G.getSerialIdKey(), DeviceSerial.GALAXY_S22_5G.getSerialIdValue()
    );

    @Step("Return TuneIn serial Id from headspin {deviceName} device")
    public String getDeviceSerialId(String deviceName) {
        Map<String, String> deviceSerials = isIos() ? DEVICE_SERIALS_IOS : DEVICE_SERIALS_ANDROID;
        return deviceSerials.getOrDefault(deviceName, "");
    }

    @Step("Get TuneIn serial id from {deviceName}")
    public String getDeviceSerialIdFromDevice(String deviceName) {
        try {
            nowPlayingPage.minimizeIfNowPlayingDisplayed();
            navigationAction.navigateTo((isIos()) ? ABOUT : SETTINGS);

            SelenideElement element = (isIos() ? aboutPage.getVersion() : settingsPage.development);
            if (isIos() && !isElementDisplayed(element)) {
                // Return an empty string when debug options are disabled
                return "";
            }
            return (isIos()) ? aboutPage.getDeviceServiceIdentifier() : settingsPage.getDeviceServiceIdentifier();
        } catch (UnsupportedOperationException e) {
            return "Unsupported device " + deviceName;
        }
    }

    /**
     * Grabbing serial id from device
     * @param stream device name
     */
    @Step("Start device serial id from {stream} device")
    public void getDeviceSerialIdFlow(HeadspinStreams stream) {
        String deviceName = stream.getDeviceName();
        String serial = (config().appiumStreamGetSerialIdFromDevice()) ? getDeviceSerialIdFromDevice(deviceName) : "";

        if (!serial.isEmpty()) {
            setSerialIdentifier(serial);
        } else {
            takeScreenshot();
            setSerialIdentifier("Unable to capture serial id");
        }
    }

    /**
     * Returns UTC date
     * @param pattern examples "MM-dd-yyyy", "HH:mm:ss"
     * @return date
     */
    public String getDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(utc.getTime());
    }

    /**
     * Calculate the time difference between two timestamps
     * @param sessionStart start of the session
     * @return time difference between sessions
     */
    private String getTimestampTimeDifference(String sessionStart) {
        // Example "9:29:58 PM";
        String start = String.valueOf(sessionStart);
        String end = String.valueOf(getDate(TIME_PATTERN));
        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTERN);

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(start);
            d2 = format.parse(end);

            // in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (Integer.parseInt(String.valueOf(diffHours)) > 0) {
                ReporterUtil.log(diffHours + " " + HOURS);
                return diffHours + " " + HOURS;
            } else if (Integer.parseInt(String.valueOf(diffMinutes)) > 0) {
                ReporterUtil.log(diffMinutes + " " + MINUTES);
                return diffMinutes + " " + MINUTES;
            } else if (Integer.parseInt(String.valueOf(diffSeconds)) > 0) {
                ReporterUtil.log(diffSeconds + " " + SECONDS);
                return diffSeconds + " " + SECONDS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public enum DeeplinkPrefixType {
        STATION(DeepLinksUtil.DEEPLINK_PREFIX_TUNE_STATION),
        PODCAST(DeepLinksUtil.DEEPLINK_PREFIX_TUNE_PODCAST);

        private String value;

        private DeeplinkPrefixType(String value) {
            this.value = value;
        }

        public String getPrefixValue() {
            return value;
        }
    }

    @Step("Opening now playing screen from a deeplink to a {stationOrContentId} - {prefix}")
    public NowPlayingPage deeplinkToNowPlayingScreen(DeeplinkPrefixType prefix, String stationOrContentId) {
        if (isIos() && config().isAppiumStreamTestEnabled()) {
            IosAlert.handleSimpleAlertIfPresent(LOCATION);
            IosAlert.handleSimpleAlertIfPresent(NOTIFICATIONS);
            deepLinksUtil.openDeeplinkInSafari(prefix.getPrefixValue() + stationOrContentId);
        } else {
            deepLinksUtil.openURL(prefix.getPrefixValue() + stationOrContentId);
        }
        return nowPlayingPage.waitUntilPageReadyLiteVersion();
    }

    /** -- On-Demand Methods -- */

    public enum OnDemandData {
        STATION_NAME(getOnDemandStationName()),
        STATION_ID(getOnDemandStationId());

        private String onDemandDataValue;

        private OnDemandData(String value) {
            this.onDemandDataValue = value;
        }

        public String getAttributeValue() {
            return onDemandDataValue;
        }
    }

    public void setOnDemandValues(OnDemandData onDemandDataValue) {
        switch (onDemandDataValue) {
            case STATION_NAME -> {
                if (!config().appiumStreamTestStationName().isEmpty()) {
                    setOnDemandStationName(Thread.currentThread().getId());
                }
            }
            case STATION_ID -> {
                if (!config().appiumStreamTestStationName().isEmpty()) {
                    setOnDemandStationId(Thread.currentThread().getId());
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + onDemandDataValue);
        }
    }

    /**
     * Adds a new line in the `Owner` column of the master csv file
     *
     * 1. On Demand Premium User
     * 2. On Demand User
     * 3. Premium User
     * @return Stream test owner name
     */
    public String getStreamsTestOwnerName() {
        String ownerName = getOwnerName();
        if (!ownerName.isEmpty()) {
            if (getPremiumUserTestStatus()) {
                return "Automation On Demand Premium by " + ownerName;
            } else {
                return "Automation On Demand by " + ownerName;
            }
        } else if (getPremiumUserTestStatus()) {
            return "Automation Premium User";
        }

        return "Automation";
    }
}
