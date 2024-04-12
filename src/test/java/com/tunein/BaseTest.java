package com.tunein;

import com.codeborne.selenide.testng.TextReport;
import com.tunein.mobile.appium.driverprovider.AppiumSession;
import com.tunein.mobile.binds.DriverModule;
import com.tunein.mobile.binds.PageModule;
import com.tunein.mobile.listeners.reportportal.ListenersTestNG;
import com.tunein.mobile.listeners.testrail.TestRailListener;
import com.tunein.mobile.mitmproxy.MitmproxyDriverProvider;
import com.tunein.mobile.pages.ScreenFacade;
import com.tunein.mobile.utils.GeneralTestUtil;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.*;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.listeners.testrail.TestRailListener.skipTestWithoutTestCaseId;
import static com.tunein.mobile.mitmproxy.MitmproxyDriverProvider.*;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.SETTINGS;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.setFirstLaunchValue;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.HeadspinUtil.dismissSystemPromptWithHeadspinAPI;
import static com.tunein.mobile.utils.HeadspinUtil.uploadAndInstallTuneInAppFlow;
import static com.tunein.mobile.utils.ReportingUtil.refreshAllowHosts;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.TestResultsUtil.getAndPostReportPortalLaunchId;
import static com.tunein.mobile.utils.TestResultsUtil.getAndPostTestRailTestPlanUrl;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.switchToAppContext;

@Listeners({ListenersTestNG.class, TestRailListener.class, TextReport.class})
@Guice(modules = {DriverModule.class, PageModule.class})
public class BaseTest extends ScreenFacade {

    @BeforeTest(alwaysRun = true)
    @Parameters(value = {"appiumPort", "proxyPort"})
    public synchronized void beforeTest(@Optional Integer appiumPort, @Optional Integer proxyPort) {
        AppiumSession.setUDID(Thread.currentThread().getId());
        Thread.currentThread().setName("Device_" + getUDID());
        if (config().testOnRealDevices()) uploadAndInstallTuneInAppFlow();
        AppiumSession.putAppiumPort(appiumPort);
        MitmproxyDriverProvider.putProxyPort(Thread.currentThread().getId(), proxyPort);

        if (config().isReportingTesting()) {
            startMitmproxyOnPort(getProxyPort());
            if (isAndroid()) {
                installMitmproxyCertificateOnDevice();
            }
        }
        if (config().isAppiumStreamTestEnabled()) {
            // Set setPremiumUserTest is stream test required to sign in with premium user before test
            GeneralTestUtil.setPremiumUserTestStatus(Thread.currentThread().getId());
        }
        if (!config().testOnRealDevices()) appiumService.startServer();
        if (!config().stabilityTests()) {
            getAppiumDriver();
            ReporterUtil.log("SessionId: " + getAppiumDriver().getSessionId());
        }
        if ((config().isReportingTesting()) && (isIos())) installMitmproxyCertificateOnDevice();
        if (!config().testOnRealDevices() && (isIos())) deepLinksUtil.deeplinkPreSetup();
        if (isAndroid()) homePage.updateScreenDensityIfNeeded();
        if (config().isReportingTesting()) {
            upsellPage.closeUpsell();
            if (isAndroid()) {
                restartApp();
                welcomeAdDialog.clickOnCloseAdButtonIfDisplayed();
                takeScreenshot();
                navigationAction.navigateTo(HOME);
                navigationAction.navigateTo(SETTINGS);
                settingsPage.tapOnAutoPlaySettings();
            }
        }
        deviceNativeActions.disableWelcomeChrome();
    }

    @AfterTest(alwaysRun = true, description = "Stop server after test")
    public synchronized void afterTest() {
        if (config().isReportPortalEnabled()) {
            getAndPostReportPortalLaunchId();
        }
        if (config().postTestRailResult()) {
            getAndPostTestRailTestPlanUrl();
        }
        if (config().isReportingTesting()) {
            stopMitmproxy();
            disableProxyOnMacOSMachine();
            if (isIos()) setDefaultBypassingDomains();
        }
        if (!config().stabilityTests()) quiteAppiumDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethodInBaseTest(Method method) {
        if (config().stabilityTests()) {
            if (method.getName().equals("testWeakNetworkCondition") || method.getName().equals("testMonitorDeviceMemory")) {
                System.setProperty("appium.stream.headspin.capture.network", "false");
            } else {
                System.setProperty("appium.stream.headspin.capture.network", "true");
            }
            createAppiumDriver();
        }
        ReporterUtil.log("Test name: " + method.getName());
        if (config().skipTestsWithoutTestCase()) skipTestWithoutTestCaseId(method);
        setSoftAssertionsInstance(getThreadId());
        setFirstLaunchValue(getThreadId(), !config().testOnRealDevices());
        if (isAndroid()) deviceNativeActions.enableWifi();
        // Dismiss pop-ups on the device before test
        if (isIos() && config().testOnRealDevices()) dismissSystemPromptWithHeadspinAPI();
        launchApp();
        if (!config().isAppiumStreamTestEnabled() && isIos()) {
            upsellPage.closeUpsell();
        }

        if (!config().testOnRealDevices() && isAndroid() && !config().isReportingTesting()) {
            navigationAction.navigateTo(HOME);
        }
        if (config().isReportingTesting()) refreshAllowHosts();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodInBaseTest(ITestResult testResult, Method method) {
        if (config().isReportingTesting()) clearMessages = true;
        if (config().testOnRealDevices() || config().isReportingTesting()) {
            if (config().appiumUninstallAppAfterTest()) uninstallApp();
        } else {
            if (isAndroid()) switchToAppContext(NATIVE_APP);
            uninstallApp();
        }
    }

}
