package com.tunein.mobile.mitmproxy;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;
import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.reporting.AdsWizzEventConstants.*;
import static com.tunein.mobile.reporting.BannerEventConstants.MAX_BANNER_DOMAIN;
import static com.tunein.mobile.reporting.GAMEventConstants.INSTREAM_GAM_DOMAIN;
import static com.tunein.mobile.reporting.IMAEventConstants.*;
import static com.tunein.mobile.reporting.ReportingConstants.PROD_OMPL_REPORTS_TUNEIN_COM;
import static com.tunein.mobile.reporting.ReportingConstants.REPORTS_RADIOTIME_COM;
import static com.tunein.mobile.reporting.UnifiedEventConstants.UNIFIED_EVENT_REPORT_PROD_DOMAIN;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.CommandLineProgramUtil.*;
import static com.tunein.mobile.utils.DataUtil.getAllIpAddressFromDomain;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class MitmproxyDriverProvider {

    private static final String ANDROID = "android";

    private static final String IOS = "ios";

    private static final String MITMDUMP = "mitmdump --";

    private static final String INSIGHT_ADSRVR_ORG = "insight.adsrvr.org";

    private static List<String> listOfDomains = Arrays.asList(UNIFIED_EVENT_REPORT_PROD_DOMAIN, ADS_WIZZ_EVENT_REPORT_DOMAIN_IOS,
            ADS_WIZZ_EVENT_REPORT_DOMAIN_ANDROID, REPORTS_RADIOTIME_COM, IMA_EVENT_NOW_PLAYING_CALL, IMA_EVENT_REPORT_DOMAIN_FIRST, IMA_EVENT_REPORT_DOMAIN_THIRD, IMA_EVENT_REPORT_DOMAIN_SECOND, INSIGHT_ADSRVR_ORG,
            PROD_OMPL_REPORTS_TUNEIN_COM, INSTREAM_GAM_DOMAIN, MAX_BANNER_DOMAIN, MEDIA_ADMOB_COM, GOOGLE_SYNDICATION, GOOGLE_AD_SERVICES, ADS_WIZZ_EVENT_REPORT_DOMAIN);

    private static final String BYPASSYING_DOMAINS = " *.infinityhdstream.* *.shoutca.* *.rosetta.* *.stream.* *.iheart.*"
            + " *.hostpleng.* *.revma.* *.securenetsystems.*"
            + " *.infinityhdstream.* *.akamaized.* *.streamguys1.* *.amperwave.* *.hls.* *.somafm.*,*.xrad.*"
            + " *wkxn-fm *.sslstream.* chunks.* streaming05.* *.streamcontrol.* rfcm.* *adulthitssk*";

    private static MitmproxyJava mitmproxyJava;

    static Map<Long, Integer> proxyPortByThread = new HashMap<>();

    public static boolean clearMessages = false;

    public static List<InterceptedMessage> messagesByThread = new ArrayList<>();

    private static final String HTTP_PROXY_TURN_ON = "networksetup -setwebproxy \"" + config().networkType() + "\" %s %s";

    private static final String HTTPS_PROXY_TURN_ON = "networksetup -setsecurewebproxy \"" + config().networkType() + "\" %s %s";

    private static final String HTTP_PROXY_TURN_OFF = "networksetup -setwebproxystate \"" + config().networkType() + "\" off";

    private static final String HTTPS_PROXY_TURN_OFF = "networksetup -setsecurewebproxystate \"" + config().networkType() + "\" off";

    private static final String SET_PROXY_BYPASS_DOMAIN = "networksetup -setproxybypassdomains " + config().networkType();

    private static final String GET_CURRENT_PROXY_BYPASS_DOMAINS = "networksetup -getproxybypassdomains " + config().networkType();

    public static synchronized void putProxyPort(Long threadId, Integer proxyPort) {
        if (proxyPort == null) {
            proxyPort = config().proxyPort();
        }
        proxyPortByThread.put(threadId, proxyPort);
    }

    public static synchronized Integer getProxyPort() {
        return proxyPortByThread.get(Thread.currentThread().getId());
    }

    @Step("Install \"Mitmproxy Certificate\" on device")
    public static void installMitmproxyCertificateOnDevice() {
        Path certificatePath = Paths.get(System.getProperty("user.home"), ".mitmproxy", "mitmproxy-ca-cert.pem");
        switch (config().mobileOS()) {
            case IOS -> {
                Map<String, Object> args = new HashMap<>();
                byte[] byteContent = new byte[0];
                try {
                    byteContent = Files.readAllBytes(certificatePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                args.put("content", Base64.getEncoder().encodeToString(byteContent));
                ((JavascriptExecutor) getAppiumDriver()).executeScript("mobile: installCertificate", args);
            }
            case ANDROID -> {
                adbRootDevice();
                ReporterUtil.log("Output: " + getOutputOfExecution("adb -s " + getUDID() + " shell avbctl disable-verification"));
                adbRebootDevice();
                adbRootDevice();
                adbRemountDevice();
                adbRebootDevice();
                adbRootDevice();
                adbRemountDevice();
                ReporterUtil.log("Push mitmproxy certificate to device");
                ReporterUtil.log("Output: " + executeCommandUntilSuccessful("adb -s " + getUDID() + " push " + certificatePath + " /system/etc/security/cacerts/c8750f0d.0"));
                runCommandLine("adb -s " + getUDID() + " shell chmod 664 /system/etc/security/cacerts/mitmproxy-ca-cert");
                adbRebootDevice();
                customWait(Duration.ofSeconds(config().waitLongTimeoutSeconds())); // Wait until device will be connected to internet after certificate install
            }
            default -> throw new Error("Unsupported platform");
        }
    }

    @Step
    public static void enableProxyOnMacOSMachine(Integer proxyPort) {
        if (isIos()) {
            if (proxyPort == null) {
                proxyPort = config().proxyPort();
            }
            runCommandLine(String.format(HTTP_PROXY_TURN_ON, config().proxyHost(), proxyPort));
            runCommandLine(String.format(HTTPS_PROXY_TURN_ON, config().proxyHost(), proxyPort));
        }
    }

    @Step("Disable Mitmproxy on Mac OS")
    public static void disableProxyOnMacOSMachine() {
        if (isIos()) {
            runCommandLine(HTTP_PROXY_TURN_OFF);
            runCommandLine(HTTPS_PROXY_TURN_OFF);
        }
    }

    @Step("Add new bypassing domains ")
    public static void setNewBypassingDomains() {
        String currentDomains = getListOfBypassingDomains();
        runCommandLine(SET_PROXY_BYPASS_DOMAIN + " " + currentDomains + BYPASSYING_DOMAINS);
    }

    @Step("Set default bypassing domains ")
    public static void setDefaultBypassingDomains() {
        runCommandLine(SET_PROXY_BYPASS_DOMAIN + " " + getListOfBypassingDomains().replace(BYPASSYING_DOMAINS, ""));
    }

    @Step("Get list of bypassing domains ")
    public static String getListOfBypassingDomains() {
        return getOutputOfExecution(GET_CURRENT_PROXY_BYPASS_DOMAINS);
    }

    public static String generateListOfAllowedHosts() {
        String listOfHosts = "";
        for (String domain: listOfDomains) {
            listOfHosts = listOfHosts + domain + "|" + getAllIpAddressFromDomain(domain) + "|";
        }
        return listOfHosts.substring(0, listOfHosts.length() - 1);
    }

    @Step("Start Mitmproxy on port {portNumber}")
    public static void startMitmproxyOnPort(Integer portNumber) {
        stopServiceWithKeyword(MITMDUMP);
        try {
            URL blockImaScriptURL = MitmproxyDriverProvider.class.getResource("/mitmproxy/addons/block-ima.py");
            String blockImaScriptPath = Paths.get(blockImaScriptURL.toURI()).toFile().getAbsolutePath();

            URL shutdownMitmproxyScriptURL = MitmproxyDriverProvider.class.getResource("/mitmproxy/addons/shutdown.py");
            String shutdownMitmproxyScriptPath = Paths.get(shutdownMitmproxyScriptURL.toURI()).toFile().getAbsolutePath();

            URL refreshAllowHostsScriptURL = MitmproxyDriverProvider.class.getResource("/mitmproxy/addons/allow-hosts.py");
            String refreshAllowHostsScriptPath = Paths.get(refreshAllowHostsScriptURL.toURI()).toFile().getAbsolutePath();

            URL modifyQueryScriptURL = MitmproxyDriverProvider.class.getResource("/mitmproxy/addons/modify-query.py");
            String modifyQueryScriptPath = Paths.get(modifyQueryScriptURL.toURI()).toFile().getAbsolutePath();
            List<String> extraMitmproxyParams;
             if (config().isDecryptionByApplovin()) {
                 extraMitmproxyParams = Arrays.asList(
                         "--allow-hosts", generateListOfAllowedHosts() + "|18.238.192.37|18.238.192.112|18.238.192.73|18.238.192.119",
                         "--script", shutdownMitmproxyScriptPath,
                         "--script", blockImaScriptPath,
                         "--script", refreshAllowHostsScriptPath,
                         "--script", modifyQueryScriptPath
                 );
             } else {
                 extraMitmproxyParams = Arrays.asList(
                         "--allow-hosts", generateListOfAllowedHosts() + "|18.238.192.37|18.238.192.112|18.238.192.73|18.238.192.119",
                         "--script", shutdownMitmproxyScriptPath,
                         "--script", blockImaScriptPath,
                         "--script", refreshAllowHostsScriptPath
                 );
             }

            ReporterUtil.log("Mitmproxy parameters: ", extraMitmproxyParams.toArray(String[]::new));

            mitmproxyJava = new MitmproxyJava(config().mitmdumpPath(), (InterceptedMessage m) -> {
                if (clearMessages) {
                    messagesByThread.clear();
                    clearMessages = false;
                }
                messagesByThread.add(m);
                return m;
            }, portNumber, extraMitmproxyParams);

            try {
                ReporterUtil.log("Start Mitmproxy");
                mitmproxyJava.start();
                ReporterUtil.log("Mitmproxy is started");
            } catch (Exception e) {
                e.printStackTrace();
                ReporterUtil.log("Cannot start mitmproxy");
                throw new RuntimeException(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ReporterUtil.log("Mitmproxy addons files are absent");
            throw new RuntimeException(ex);
        }
    }

    @Step("Stop Mitmproxy")
    public static void stopMitmproxy() {
        try {
            ReporterUtil.log(getOutputOfExecution("env http_proxy=http://localhost:" + config().proxyPort() + " curl -I http://shutdown.com/path"));
            mitmproxyJava.stop();
            ReporterUtil.log("Mitmproxy is stopped");
        } catch (InterruptedException e) {
            e.printStackTrace();
            ReporterUtil.log("Cannot stop mitmproxy");
        }
    }

}
