package com.tunein.stability.tests.android;

import com.opencsv.CSVWriter;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.utils.ReporterUtil;
import com.tunein.stability.tests.common.MonitoringTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.quiteAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.deviceactions.DeviceNativeActions.MemoryInfoType;
import static com.tunein.mobile.deviceactions.DeviceNativeActions.MemoryInfoType.TOTAL_PSS;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.utils.ApplicationUtil.activateApp;
import static com.tunein.mobile.utils.ApplicationUtil.closeApp;
import static com.tunein.mobile.utils.GeneralTestUtil.DETAILED_DATE_PATTERN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.TestResultsUtil.TestStatus.PASSED;
import static com.tunein.mobile.utils.TestResultsUtil.*;

public class AndroidMonitoringTest extends MonitoringTest {

    protected static CSVWriter writerMemoryValues = null;

    @BeforeMethod(alwaysRun = true)
    public void runBeforeMethod(ITestResult testResult, Method method) {
        try {
            String masterCsvFilePathForMemory = config().stabilityTestsMasterFilePath() + config().memoryMonitoringTestsMasterFileName();
            writerMemoryValues = new CSVWriter(new FileWriter(masterCsvFilePathForMemory, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void afterMethodInBaseTest(ITestResult testResult, Method method) {
        long testDurationInHours = Duration.ofMillis(testResult.getEndMillis() - testResult.getStartMillis()).toHours();
        ReporterUtil.log("dumpsys procstats --hours " + testDurationInHours + " - " + deviceNativeActions.getProcStatsDuringHours(getUDID(), (int) testDurationInHours));
        ReporterUtil.log("Start writing data to csv file");
        String stationName = STREAM_STATION_WITHOUT_ADS.getStreamName() + ", " + STREAM_PODCAST_MARKETPLACE.getStreamName() + ", " + STREAM_STATION_TODAYS_HITS.getStreamName();
        String stationId = STREAM_STATION_WITHOUT_ADS.getSearchQuery() + ", " + STREAM_PODCAST_MARKETPLACE.getSearchQuery() + ", " + STREAM_STATION_TODAYS_HITS.getSearchQuery();
        String mediaType = "ignore";
        String reportPortalUrl;
        String sessionIdUrl = getAppiumDriver().getSessionId().toString();

        String testName = method.getName();
        try {
            reportPortalUrl = getUrlForTestWithName(testName);
        } catch (Throwable throwable) {
            reportPortalUrl = getReportPortalUrl();
        }

        String testDescription = method.getAnnotation(Test.class).description();
        String status = testResult.isSuccess() ? "passed" : generalTestUtil.getStatusOfStream();
        if (testResult.isSuccess()) setStatusForTestWithName(PASSED, testName);

        ReporterUtil.log("SessionId: " + getAppiumDriver().getSessionId());
        quiteAppiumDriver();

        String silenceDetected = "ignore";
        String udid = getUDID();
        ReporterUtil.log("Device: " + udid + " with thread: " + Thread.currentThread().getId());

        String testType = "Daily";
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
                writerMemoryValues.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(description = "Monitor device memory")
    public void testMonitorDeviceMemory() {
        long start = System.currentTimeMillis();
        Duration timeoutForMonitoring = Duration.ofSeconds(config().fiveMinutesInSeconds());
        int criticalMemoryValue = config().criticalMemoryValueBytes();
        MemoryInfoType memoryInfoType = TOTAL_PSS;
        List<HomePage.BrowsiesBarTabsLabels> browsiesBarTabsLabels = Arrays.asList(FOR_YOU, SPORTS, MUSIC, AUDIOBOOKS, NEWS_AND_TALK, PODCASTS, BY_LANGUAGE, BY_LOCATION);

        continueListeningDialog.closeContinueListeningDialogIfDisplayed();
        if (nowPlayingPage.isOnNowPlayingPage()) {
            nowPlayingPage
                    .waitUntilPreRollAdDisappearIfDisplayed()
                    .minimizeNowPlayingScreen();
        }
        signInPage.signInFlowForUser(USER_GENERAL);
        while (System.currentTimeMillis() - start < Duration.ofHours(config().memoryMonitoringTimeoutInHours()).toMillis()) {
            navigationAction.navigateTo(SEARCH);
            searchPage
                    .searchStreamAndPlayFirstResult(STREAM_STATION_WITHOUT_ADS)
                    .validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            nowPlayingPage.goToStreamProfile();
            searchPage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            navigationAction.navigateTo(LIBRARY);
            searchPage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            deepLinksUtil.openURL(STREAM_PODCAST_MARKETPLACE.getStreamTuneDeepLink());
            nowPlayingPage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.navigateTo(HOME);
            browsiesBarTabsLabels.stream().forEach(browsiesBarTab -> {
                        homePage.tapOnRequiredBrowsiesBarTab(browsiesBarTab);
                    });
            homePage.tapOnRequiredBrowsiesBarTab(FOR_YOU);
            homePage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            navigationAction.navigateTo(SEARCH);
            searchPage
                    .searchStreamAndPlayFirstResult(STREAM_STATION_TODAYS_HITS)
                    .validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            if (System.currentTimeMillis() - start > Duration.ofHours(config().memoryMonitoringTimeoutInHours()).toMillis()) {
                break;
            }
            nowPlayingPage.minimizeNowPlayingScreen();
            navigationAction.navigateTo(HOME);
            scroll(DOWN, 7, MEDIUM);
            closeApp();
            homePage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            activateApp();
            navigationAction.navigateTo(EXPLORER);
            explorerPage.clickStationFilterByIndex(1);
            explorerPage.clickStationByIndex(3);
            explorerPage.validateDeviceMemoryIsLessThan(memoryInfoType, criticalMemoryValue, timeoutForMonitoring, writerMemoryValues);
            explorerPage.clickExplorerCloseButton();
        }
    }

}
