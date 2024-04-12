package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tunein.mobile.testdata.models.HeadspinStreams;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.setFirstLaunchValue;
import static com.tunein.mobile.testdata.dataprovider.HeadspinProvider.getContent;
import static com.tunein.mobile.utils.CommandLineProgramUtil.*;
import static com.tunein.mobile.utils.HeadspinUtil.NetworkConfigurationProfiles.*;

public class HeadspinUtil {

    public static Map<String, String> headspinDeviceHosts = new HashMap<>() {{
        put("R52T707D1YX", "dev-us-sny-3.headspin.io:7009");
        put("R5CT90RF03W", "dev-us-sny-3.headspin.io:7008");
        put("R5CT90REYXM", "dev-us-sny-3.headspin.io:7008");
        put("2A221JEGR10220", "dev-us-sny-3.headspin.io:7008");
        put("27261FDH3000GY", "dev-us-sny-3.headspin.io:7008");
        put("ZY22FDCPST", "dev-us-sny-3.headspin.io:7008");
    }};

    private static String headspinUploadAppId = "";

    /* --- Setters --- */

    public static void setHeadspinUploadAppId(String headspinUploadAppId) {
        HeadspinUtil.headspinUploadAppId = headspinUploadAppId;
    }

    /* --- Getters --- */

    public static String getHeadspinAppId() {
        return headspinUploadAppId;
    }

    /**
     * Retrieving `Headspin` API token from the Gradle config parameter `appium.stream.headspin.token`
     */
    private static final String HEADSPIN_TOKEN = config().appiumHeadspinToken();

    public static final String HEADSPIN_NETWORK_CONFIG_URL = "https://%s@api-dev.headspin.io/v1/network/%s@%s/config";

    public static final String HEADSPIN_ADB_SHELL_URL = "https://%s@api-dev.headspin.io/v0/adb/%s/shell";

    private static final String DEVICE_HOST_NAME = (isiOS()) ? config().headspinIosDeviceHost() : config().headspinAndroidDeviceHost();

    public static final String HEADSPIN_CURL_SESSION_ID = "curl GET https://%s@api-dev.headspin.io/v0/sessions?include_all=false&num_sessions=1 | jq '.sessions[0].session_id'";

    public static final String HEADSPIN_HAR_CURL = "curl https://%s@api-dev.headspin.io/v0/sessions/%s.har?enhanced=True -o '%s'";

    private static final String HEADSPIN_INSTALL_CURL = "curl -X POST https://%s@api-dev.headspin.io/v1/app/%s/install/%s";

    private static final String HEADSPIN_UPLOAD_CURL = "curl -X POST https://%s@api-dev.headspin.io/v1/app/upload";

    private static final String HEADSPIN_POPUP_CURL = "curl -X POST https://%s@api-dev.headspin.io/v0/idevice/%s/poptap";

    private static final String HEADSPIN_ADB_SHELL_CURL = "curl -X POST " + HEADSPIN_ADB_SHELL_URL + " -d ";

    private static final String HEADSPIN_UPLOAD_TAG = "'{" + '"' + "tags" + '"' + ": " + '"' + "Automation stream test" + '"' + "}'";  //"'{"tags": "Automation stream test"}'";

    private static final String HEADSPIN_SUCCESS = "ok";

    private static final String HEADSPIN_UPLOAD_SUCCESS = "app_id";

    private static final String GET_METHOD = "GET";

    private static final String POST_METHOD = "POST";

    private static final String DELETE_METHOD = "DELETE";

    private static final String NETWORK_PROFILE_WIFI = "WiFi";

    private static final String NETWORK_PROFILE_LTE = "LTE";

    private static final String NETWORK_PROFILE_3G = "3G";

    private static final String NETWORK_PROFILE_EDGE = "EDGE";

    private static final String VERY_BAD_NETWORK = "VERY BAD NETWORK";

    /* --- Helper Methods --- */

    /**
     * This method installs pre-uploaded TuneIn app
     *
     * @param appId headspin app_id (example: "dd76b46d-e9e7-4c3b-93cf-d7bac0c2430f")
     */
    public static void installPreUploadedBuild(String appId) {
        // Command line execution
        ReporterUtil.log("Installing TuneIn Radio app on Headspin \"" + getUDID() + "\" device");
        String command = String.format(
                HEADSPIN_INSTALL_CURL,
                HEADSPIN_TOKEN,
                appId,
                getUDID()
        );
        runHeadspinCommand(command);

        ReporterUtil.log("Setting first launch value to true");
        setFirstLaunchValue(getThreadId(), true);
    }

    /**
     * Execute headspin command line
     *
     * @param command expected headspin command line
     * @param timeout to break while loop
     */
    private static String runHeadspinCommand(String command, Duration... timeout) {
        ReporterUtil.log("Executing command line \"" + command + "\"");
        String output;
        int loopTimeout = (timeout.length > 0) ? (int) timeout[0].toMillis() : config().oneMinuteInMilliseconds();
        String successCriteria = (command.contains("upload")) ? HEADSPIN_UPLOAD_SUCCESS : HEADSPIN_SUCCESS;

        long start = System.currentTimeMillis();
        output = getOutputOfExecution(command);

        // Wait until response message will contain either ok or app_id string
        while (!output.contains(successCriteria)) {
            if (output.contains("\"status_code\": 400")) {
                throw new Error(output);
            }

            if (System.currentTimeMillis() - start > loopTimeout) {
                throw new Error("Command \"" + command + "\" didn't return expected result " + " \"" + output + "\"");
            }
            output = getOutputOfExecution(command);
        }
        ReporterUtil.log("output: " + output);
        return output;
    }

    private static boolean isiOS() {
        return config().mobileOS().equalsIgnoreCase("ios");
    }

    public static String getHeadspinSessionId() {
        // Get headspin API response from command line
        String command = String.format(HEADSPIN_CURL_SESSION_ID, config().appiumHeadspinToken());
        String output = getOutputOfExecution(command);

        // Pretty-Print JSON response
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(output);
        String prettyJsonString = gson.toJson(jsonElement);

        // Get session_id key from JSON response object
        String sessionId = jsonElement.getAsJsonObject().get("sessions").getAsJsonArray().get(0).getAsJsonObject().get("session_id").toString();

        // Remove double quotes from beginning and ending
        return sessionId.substring(1, sessionId.length() - 1);
    }

    /**
     * This method uploads TuneIn build to headspin and then stores app_id on success
     */
    @Step("Uploading TuneIn app to Headspin")
    public static String uploadTuneInApp(Duration... timeout) {
        if (config().appiumHeadspinAppId() != null) {
            setHeadspinUploadAppId(config().appiumHeadspinAppId());
            return config().appiumHeadspinAppId();
        } else if (HeadspinUtil.getHeadspinAppId().isEmpty()) {
            if (!config().headspinAppPath().contains(".ipa") && !config().headspinAppPath().contains(".apk")) {
                throw new UnsupportedOperationException("Unsupported headspin file format");
            }
            // TODO Fix tag metadata quotes escape error
            // -F metadata=<json-object>
            //String tag = "-F metadata='{\"tags\": \"Automation stream test\"}'";
            // String tag = "-F metadata=" + HEADSPIN_UPLOAD_TAG;

            // -F app=@<path-to-app>
            String appPath = "-F app=@" + config().headspinAppPath();

            String output = runHeadspinCommand(
                    String.format(HEADSPIN_UPLOAD_CURL, HEADSPIN_TOKEN) + " " + appPath,
                    (timeout.length > 0) ? timeout[0] : Duration.ofMillis(config().oneMinuteInMilliseconds())
            );
            // Set app_id
            // TODO improve with replaceAll
            output = output.replace("{\"app_id\": ", "")
                    .replace("\"", "")
                    .replace("} ", "");
            setHeadspinUploadAppId(output);
            return output;
        } else {
            return config().appiumHeadspinAppId();
        }
    }

    /**
     * This method uploads and then installs uploaded build
     */
    @Step("Upload and install previously uploaded TuneIn Radio build")
    public static void uploadAndInstallTuneInAppFlow() {
        if (config().appiumInstallAppBeforeTest()) {
            String headspinAppId = uploadTuneInApp(Duration.ofMillis(config().oneMinuteInMilliseconds()));
            HeadspinUtil.installPreUploadedBuild(headspinAppId);
        }
    }

    /**
     * This method run adb shell command via Headspin App Management API
     */
    @Step("Run adb shell command {command}")
    public static void runAdbShellCommandInHeadspin(String command, boolean... throwError) {
        runCommandLine(String.format(HEADSPIN_ADB_SHELL_CURL, config().appiumHeadspinToken(), getUDID()) + "'" + command + "'", throwError);
    }

    @Step("Get display density for headspin device {deviceUDID}")
    public static String getDensityInfoForHeadspinDevice(String deviceUDID) {
        String[] curlCommand = new String[]{"curl", "-X", POST_METHOD, String.format(HEADSPIN_ADB_SHELL_URL, config().appiumHeadspinToken(), deviceUDID), "-d", "wm density"};
        return getOutputOfExecution(curlCommand);
    }

    @Step("Set display density {density} for headspin device {deviceUDID}")
    public static void setDensityValueForHeadspinDevice(String deviceUDID, int density) {
        getOutputOfExecution(new String[]{"curl", "-X", POST_METHOD, String.format(HEADSPIN_ADB_SHELL_URL, config().appiumHeadspinToken(), deviceUDID), "-d", "wm density " + density});
    }

    @Step("Get ProcStats for headspin device {deviceUDID} during {hours} hours")
    public static String getProcStatsForDeviceDuringHours(String deviceUDID, int hours) {
        return getOutputOfExecution(new String[]{"curl", "-X", POST_METHOD, String.format(HEADSPIN_ADB_SHELL_URL, config().appiumHeadspinToken(), deviceUDID), "-d", "dumpsys procstats --hours " + hours});
    }

    /**
     * Dismisses system prompts on the device. If there are no system prompts on device, it does nothing.
     *
     * @see <a href="https://ui.headspin.io/docs/idevice-api#dismiss-pop-ups-on-a-device">https://ui.headspin.io/docs</a>
     */
    @Step("Dismiss system prompts on the device using Headspin API")
    public static void dismissSystemPromptWithHeadspinAPI() {
        executeCommandUntilSuccessful(String.format(HEADSPIN_POPUP_CURL, config().appiumHeadspinToken(), config().udid()));
    }

    /* --- Device Network Configuration  --- */

    public enum NetworkConfigurationProfiles {
        MOBILE_EDGE("{\"shaping\": {\"down\": 0.24, \"up\": 0.20, \"rtt\": 0.40}, \"traffic_endpoints\": [{\"key\": \"default\"}]}"),
        MOBILE_3G("{\"shaping\": {\"down\": 0.78, \"up\": 0.33, \"rtt\": 0.10}, \"traffic_endpoints\": [{\"key\": \"default\"}]}"),
        MOBILE_LTE("{\"shaping\": {\"down\": 50.0, \"up\": 10.0, \"rtt\": 0.05}, \"traffic_endpoints\": [{\"key\": \"default\"}]}"),
        MOBILE_VERY_BAD_NETWORK("{\"shaping\": {\"down\": 1.0, \"up\": 1.0, \"rtt\": 0.50, \"loss\": 10.0}, \"traffic_endpoints\": [{\"key\": \"default\"}]}"),
        MOBILE_WIFI("{\"shaping\": {\"down\": 70.0, \"up\": 70.0, \"rtt\": 0.02}, \"traffic_endpoints\": [{\"key\": \"default\"}]}");

        private String networkTypeValue;

        private NetworkConfigurationProfiles(String bitrateValue) {
            this.networkTypeValue = bitrateValue;
        }

        public String getHeadspinNetworkTypeValue() {
            return networkTypeValue;
        }
    }

    public enum NetworkConfigurationProfileTypes {
        NETWORK_PROFILE_WIFI("WiFi"),
        NETWORK_PROFILE_LTE("LTE"),
        NETWORK_PROFILE_3G("3G"),
        NETWORK_PROFILE_EDGE("EDGE"),
        NEWTWORK_PROFILE_BAD_NETWORK("VERY BAD NETWORK");

        private String networkTypeValue;

        private NetworkConfigurationProfileTypes(String networkTypeValue) {
            this.networkTypeValue = networkTypeValue;
        }

        public String getNetworkTypeValue() {
            return networkTypeValue;
        }

        public static NetworkConfigurationProfileTypes getNetworkType(final String networkTypeTitle) {
            List<NetworkConfigurationProfileTypes> networkConfigurationProfiles1List = Arrays.asList(NetworkConfigurationProfileTypes.values());
            return networkConfigurationProfiles1List.stream().filter(eachContent -> eachContent.toString().equals(networkTypeTitle))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return networkTypeValue;
        }
    }

    @Step("Run \"Device Network Configuration\" profile to \"{networkType}\" for device {deviceUdid}")
    private static void updateDeviceNetwork(String networkType, String networkValue, String deviceUdid) {
        ReporterUtil.log("Set \"Device Network Configuration\" profile with type " + networkType);
        String deviceHost = getUDID().equals("R52T707D1YX") ? "dev-us-sny-3-proxy-14-lin.headspin.io" : DEVICE_HOST_NAME;
        String[] curlCommand = new String[]{"curl", "-X", POST_METHOD, String.format(HEADSPIN_NETWORK_CONFIG_URL, config().appiumHeadspinToken(), deviceUdid, deviceHost), "-d", networkValue};
        executeCommandUntilSuccessful(curlCommand);
    }

    /**
     * Network Config API allows devices simulate network conditions such as 3G, LTE and WI-FI
     *
     * @param networkType set Device Network Configuration profile that is set in the Streams.json
     * @see <a href="https://ui.headspin.io/docs/network-config-api#set-device-network-configuration">network-config-api</a>
     */
    @Step("Set \"Device Network Configuration\" profile {networkType}")
    public static void setDeviceNetworkConfiguration(HeadspinStreams networkType) {
        switch (getContent(getUDID()).getNetworkType()) {
            case NETWORK_PROFILE_WIFI -> {
                deleteDeviceNetworkConfigurationProfile(getUDID());
            }
            case NETWORK_PROFILE_LTE -> {
                updateDeviceNetwork(NETWORK_PROFILE_LTE, MOBILE_LTE.getHeadspinNetworkTypeValue(), getUDID());
            }
            case NETWORK_PROFILE_3G -> {
                updateDeviceNetwork(NETWORK_PROFILE_3G, MOBILE_3G.getHeadspinNetworkTypeValue(), getUDID());
            }
            case NETWORK_PROFILE_EDGE -> {
                updateDeviceNetwork(NETWORK_PROFILE_EDGE, MOBILE_EDGE.getHeadspinNetworkTypeValue(), getUDID());
            }
            case VERY_BAD_NETWORK -> {
                updateDeviceNetwork(VERY_BAD_NETWORK, MOBILE_VERY_BAD_NETWORK.getHeadspinNetworkTypeValue(), getUDID());
            }
            default -> throw new IllegalStateException("Unexpected value: " + networkType);
        }
    }

    @Step("Set \"Device Network Configuration\" profile {networkType}")
    public static void setDeviceNetworkConfiguration(NetworkConfigurationProfileTypes networkType) {
        switch (networkType) {
            case NETWORK_PROFILE_WIFI -> {
                deleteDeviceNetworkConfigurationProfile(getUDID());
            }
            case NETWORK_PROFILE_LTE -> {
                updateDeviceNetwork(NETWORK_PROFILE_LTE, MOBILE_LTE.getHeadspinNetworkTypeValue(), getUDID());
            }
            case NETWORK_PROFILE_3G -> {
                updateDeviceNetwork(NETWORK_PROFILE_3G, MOBILE_3G.getHeadspinNetworkTypeValue(), getUDID());
            }
            case NETWORK_PROFILE_EDGE -> {
                updateDeviceNetwork(NETWORK_PROFILE_EDGE, MOBILE_EDGE.getHeadspinNetworkTypeValue(), getUDID());
            }
            case NEWTWORK_PROFILE_BAD_NETWORK -> {
                updateDeviceNetwork(VERY_BAD_NETWORK, MOBILE_VERY_BAD_NETWORK.getHeadspinNetworkTypeValue(), getUDID());
            }
            default -> throw new IllegalStateException("Unexpected value: " + networkType);
        }
    }

    /**
     * Clear "Device Network Configuration" profile from device, if previously installed or set
     *
     * @see <a href="https://ui.headspin.io/docs/network-config-api#clear-device-network-configuration">clear-device-network-configuration-api</a>
     */
    @Step("Delete \"Device Network Configuration\" profile from device {deviceUdid}")
    public static void deleteDeviceNetworkConfigurationProfile(String deviceUdid) {
        String deviceHost = getUDID().equals("R52T707D1YX") ? "dev-us-sny-3-proxy-14-lin.headspin.io" : DEVICE_HOST_NAME;
        executeCommandUntilSuccessful(new String[]{"curl", "-X", DELETE_METHOD, String.format(HEADSPIN_NETWORK_CONFIG_URL, config().appiumHeadspinToken(), deviceUdid, deviceHost)});
    }

    /**
     * Gets "Device Network Configuration" profile from device
     *
     * @see <a href="https://ui.headspin.io/docs/network-config-api#get-device-network-configuration">get-device-network-configuration-api</a>
     */
    @Step("Get \"Device Network Configuration\" profile from device {deviceUdid}")
    public static void getDeviceNetworkConfigurationProfile(String deviceUdid) {
        String deviceHost = getUDID().equals("R52T707D1YX") ? "dev-us-sny-3-proxy-14-lin.headspin.io" : DEVICE_HOST_NAME;
        executeCommandUntilSuccessful(new String[]{"curl", "-X", GET_METHOD, String.format(HEADSPIN_NETWORK_CONFIG_URL, config().appiumHeadspinToken(), deviceUdid, deviceHost)});
    }

    public static String getHeadSpinSessionUrlToReportPortal() {
        String headspinSession = String.valueOf(getAppiumDriver().getSessionId());
        String sessionIdUrl = "https://ui.headspin.io/sessions/" + headspinSession + "/waterfall";
        return sessionIdUrl;
    }

    @Step("Download har file from headspin for session {sessionId}")
    public static void downloadHarFileForSession(String sessionId) {
        String filePath = config().headSpinHarFolder() + sessionId + ".har";
        runCommandLine(String.format(HEADSPIN_HAR_CURL, config().appiumHeadspinToken(), sessionId, filePath));

        long startTime = System.currentTimeMillis();
        while (!new File(filePath).exists()) {
            if (System.currentTimeMillis() - startTime > Duration.ofSeconds(config().tenMinutesInSeconds()).toMillis()) {
                ReporterUtil.log("Timeout: HAR file not downloaded within the specified time.");
                return;
            }
        }
        ReporterUtil.log("HAR file is downloaded to: " + filePath);
    }

}
