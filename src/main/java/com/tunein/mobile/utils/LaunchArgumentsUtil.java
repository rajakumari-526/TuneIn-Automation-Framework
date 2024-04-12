package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import io.appium.java_client.ios.options.wda.ProcessArguments;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationEnvironment.PROD;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationEnvironment.STAGE;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.NOTIFICATIONS;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.*;

public class LaunchArgumentsUtil {

    static Map<Long, String> androidLaunchArgumentsMap = new HashMap<>();

    public static synchronized String getAndroidLaunchArguments() {
        return androidLaunchArgumentsMap.get(Thread.currentThread().getId());
    }

    public static synchronized void setAndroidLaunchArguments(String launchArguments) {
        androidLaunchArgumentsMap.put(Thread.currentThread().getId(), launchArguments);
    }

    public static ProcessArguments getDefaultLaunchArguments() {
        List<String> launchArgumentsList = new ArrayList<>(Arrays.asList(
                UI_AUTOMATION.getLaunchArgumentValue(),
                UPSELL_SCREEN_SHOW_ON_LAUNCH.getLaunchArgumentValue(), "false",
                BOOST_TOOLTIP_ENABLED.getLaunchArgumentValue(), "false",
                VIDEO_PREROLL_ENABLED.getLaunchArgumentValue(), "true",
                BOOST_ENABLED.getLaunchArgumentValue(), "false",
                UPSELL_SCREEN_DONE_PLUS_FLOW.getLaunchArgumentValue(), "false",
                REGWALL_MODULE.getLaunchArgumentValue(), "true",
                CATERPILLAR_REGWALL.getLaunchArgumentValue(), "false",
                PLAYER_AUTO_RESTART_PLAYER_DEFAULT.getLaunchArgumentValue(), "false",
                NOWPLAYING_CHRYSALIS_PLAYER_BUTTONS.getLaunchArgumentValue(), "false",
                RATINGS_PROMPT.getLaunchArgumentValue(), "false",
                SUBSCRIPTION_PERIODIC_RESTORE_PURCHASE.getLaunchArgumentValue(), "false",
                APP_ENVIRONMENT.getLaunchArgumentValue(), (config().isProdEnvironment()) ? PROD.getEnvironment() : STAGE.getEnvironment(),
                PREMIUM_TAB_TOOLTIP.getLaunchArgumentValue(), "false",
                ADS_GDPR_CONSENT_FORM.getLaunchArgumentValue(), "false",
                ADS_WELCOME.getLaunchArgumentValue(), "false",
                BANNER_ADS.getLaunchArgumentValue(), config().isReportingTesting() ? (isIos() ? "True" : "true") : "false",
                REGWALL_FAVORITES.getLaunchArgumentValue(), "false",
                SHOW_REGWALL_ONLAUNCH.getLaunchArgumentValue(), isIos() ? "" : "false",
                PLAYER_SHAREPLAY_ENABLED.getLaunchArgumentValue(), "false",
                PLAYER_OFFLINE_SHAREPLAY_ENABLED.getLaunchArgumentValue(), "false",
                BRAZE_INAPP_MESSAGE_DISCARD.getLaunchArgumentValue(), isIos() ? "true" : "false",
                UNIFIED_EVENTS_INTERVAL.getLaunchArgumentValue(), "2",
                SPEED_CONTROL_TOOLTIP_ENABLED.getLaunchArgumentValue(), isIos() ? "" : "false",
                API_OPML_STARTUP_FLOW.getLaunchArgumentValue(), "LS,H",
                API_OPML_SUBSEQUENT_STARTUP_FLOW.getLaunchArgumentValue(), "LS,H",
                UNIFIED_EVENTS_ENABLED.getLaunchArgumentValue(), "true",
                SEARCH_AUTOCOMOPLETE_SUGGESTIONS_ENABLED.getLaunchArgumentValue(), isIos() ? "" : "false",
                ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS.getLaunchArgumentValue(), config().isReportingTesting() ? String.valueOf(config().timeBetweenRollsInSeconds()) : "1800",
                PLAYER_AUTOPLAY_ENABLED.getLaunchArgumentValue(), (config().isReportingTesting() && isIos()) ? "false" : "",
                PLAYER_AUTOPLAY_DEFAULT_VALUE.getLaunchArgumentValue(), (config().isReportingTesting() && isIos()) ? "false" : "",
                DATA_OPT_OUT.getLaunchArgumentValue(), (config().isReportingTesting() && isIos()) ? "false" : "",
                ADS_PREROLL_TYPE.getLaunchArgumentValue(), (config().isReportingTesting() && isIos()) ? "disabled" : ""
        ));
        return new ProcessArguments(launchArgumentsList.stream().filter(item -> !item.equals("")).collect(Collectors.toList()));
    }

    @Step("Update launch argument for {launchArgumentsType} with value {value}")
    public static void updateLaunchArgumentFor(LaunchArgumentsTypes launchArgumentsType, String value) {
        List<String> listOfLaunchArguments = updateDefaultLaunchArgumentList(launchArgumentsType, value, getLaunchArgumentsList());
        if (isIos()) {
            Map<String, String> environmentMap = new HashMap<>();
            environmentMap.put("braze.inAppMessages.discard", "true");
            HashMap<String, Object> arguments = new HashMap<>();
            arguments.put("bundleId", config().bundleIdIos());
            arguments.put("arguments", listOfLaunchArguments);
            arguments.put("environment", environmentMap);
            closePermissionPopupFor(NOTIFICATIONS);
            uninstallApp();
            installApp();
            setRequiredAppPermissions();
            getAppiumDriver().executeScript("mobile: launchApp", arguments);
            closePermissionPopupFor(NOTIFICATIONS);
        } else if (isAndroid()) {
            String launchArgumentString = String.join(" ", listOfLaunchArguments);
            terminateApp();
            adbGrantAllPermissions();
            adbStartDeviceWithLaunchArguments(launchArgumentString);
            setAndroidLaunchArguments(launchArgumentString);
        }
    }

    @Step("Update launch arguments from the list {launchArgumentsMap}")
    public static void updateLaunchArguments(Map<LaunchArgumentsTypes, String> launchArgumentsMap) {
        ArrayList<String> listOfLaunchArguments = getLaunchArgumentsList();
        for (Map.Entry<LaunchArgumentsTypes, String> entry : launchArgumentsMap.entrySet()) {
            listOfLaunchArguments = updateDefaultLaunchArgumentList(entry.getKey(), entry.getValue(), listOfLaunchArguments);
        }
        if (isIos()) {
            Map<String, String> environmentMap = new HashMap<>();
            environmentMap.put("braze.inAppMessages.discard", "true");
            HashMap<String, Object> arguments = new HashMap<>();
            arguments.put("bundleId", config().bundleIdIos());
            arguments.put("arguments", listOfLaunchArguments);
            arguments.put("environment", environmentMap);
            closePermissionPopupFor(NOTIFICATIONS);
            uninstallApp();
            installApp();
            setRequiredAppPermissions();
            getAppiumDriver().executeScript("mobile: launchApp", arguments);
        } else if (isAndroid()) {
            String launchArgumentString = String.join(" ", listOfLaunchArguments);
            terminateApp();
            adbGrantAllPermissions();
            adbStartDeviceWithLaunchArguments(launchArgumentString);
            setAndroidLaunchArguments(launchArgumentString);
        }
    }

    /**
     * If a launch arguments list is provided, then updates that list to set the specified launch argument type to the specified value and returns the list
     * If no list is provided, then updates the default launch arguments list and returns that list
     * @param launchArgumentsType Launch argument to be updated
     * @param value Value to set that launch argument to
     * @param optionalLaunchArgumentsList Launch argument list to update; if none provided, will use default launch arguments list
     * @return Updated launch arguments list
     */
    @Step("Update launch argument {launchArgumentsType} with value {value}")
    public static ArrayList<String> updateDefaultLaunchArgumentList(LaunchArgumentsTypes launchArgumentsType, String value, ArrayList<String>... optionalLaunchArgumentsList) {
        ArrayList<String> launchArgumentsList = optionalLaunchArgumentsList.length > 0 ? optionalLaunchArgumentsList[0] : getDefaultLaunchArgumentsList();
        String launchArgumentsTypeValue = launchArgumentsType.getLaunchArgumentValue();
        if (launchArgumentsTypeValue.isEmpty()) {
            ReporterUtil.log("Launch Argument " + launchArgumentsType + " is not available for current platform.");
            return launchArgumentsList;
        }
        launchArgumentsTypeValue = launchArgumentsTypeValue
                .replaceAll("--ez ", "")
                .replaceAll("--es ", "")
                .replaceAll("--ei ", "");
        String finalLaunchArgumentsTypeValue = launchArgumentsTypeValue;

        int keyPosition = IntStream
                .range(0, launchArgumentsList.size())
                .filter(i -> launchArgumentsList.get(i).contains(finalLaunchArgumentsTypeValue))
                .findFirst().orElse(-1);
        int valuePosition = keyPosition + 1;
        launchArgumentsList.set(valuePosition, value);
        return launchArgumentsList;
    }

    public static ArrayList<String> getDefaultLaunchArgumentsList() {
        ArrayList<String> defaultLaunchArgumentsList = (ArrayList<String>) getDefaultLaunchArguments().toMap().get("args");
        if (isAndroid()) {
            ArrayList<String> listOfLaunchArguments = new ArrayList<>();
            for (int i = 0; i < defaultLaunchArgumentsList.size(); i++) {
                if (defaultLaunchArgumentsList.get(i).contains("--")) {
                    listOfLaunchArguments.add(defaultLaunchArgumentsList.get(i));
                    listOfLaunchArguments.add(defaultLaunchArgumentsList.get(i + 1));
                }
            }
            return listOfLaunchArguments;
        }
        return defaultLaunchArgumentsList;
    }

    private static ArrayList<String> getLaunchArgumentsList() {
        if (isIos()) {
            Map<String, Object> mapOfCapabilities = getAppiumDriver().getCapabilities().asMap();
            return ((Map<String, ArrayList<String>>) mapOfCapabilities.get("appium:processArguments")).get("args");
        } else if (isAndroid()) {
            return new ArrayList<>(Arrays.asList(getAndroidLaunchArguments().split(" ")));
        }
        return null;
    }

    public static boolean isLaunchArgumentKeysSet(LaunchArgumentsTypes launchArgumentsType, String expectedValue) {
        String launchArgumentsTypeValue = launchArgumentsType.getLaunchArgumentValue();
        if (launchArgumentsType != null) {
            ArrayList<String> listOfLaunchArguments = getLaunchArgumentsList();
            int keyPosition = listOfLaunchArguments.indexOf(launchArgumentsTypeValue.replace("--ez ", ""));
            int valuePosition = keyPosition + 1;
            boolean expected = listOfLaunchArguments.get(valuePosition).equals(expectedValue.toLowerCase());
            if (!config().isAppiumStreamTestEnabled()) {
                ReporterUtil.log("Config key \"" + launchArgumentsTypeValue + "\" was set to \"" + expected + "\"");
            }
            return expected;
        }
        return false;
    }

    public enum LaunchArgumentsTypes {
        UI_AUTOMATION(isIos() ? "-UIAutomation" : ""),
        UPSELL_SCREEN_SHOW_ON_LAUNCH(isIos() ? "-upsellScreen.showOnLaunch" : "--ez \"isFirstLaunchUpsellEnabled\""),
        UPSELL_SCREEN_DONE_PLUS_FLOW(isIos() ? "-upsellScreen.donePlusFlow" : ""),
        REGWALL_MODULE(isIos() ? "-regwall.module.enabled" : ""),
        CATERPILLAR_REGWALL(isIos() ? "-caterpillar.regwall.enabled" : ""),
        PLAYER_AUTO_RESTART_PLAYER_DEFAULT(isIos() ? "-player.autoRestartPlayerDefault" : ""),
        NOWPLAYING_CHRYSALIS_PLAYER_BUTTONS(isIos() ? "-nowplaying.chrysalisPlayerButtons.enabled" : ""),
        RATINGS_PROMPT(isIos() ? "-ratings.prompt.enabled" : ""),
        SUBSCRIPTION_PERIODIC_RESTORE_PURCHASE(isIos() ? "-subscription.periodic.restorePurchases" : ""),
        PREMIUM_TAB_TOOLTIP(isIos() ? "-premium.tab.tooltip.enabled" : ""),
        ADS_GDPR_CONSENT_FORM(isIos() ? "-ads.gdpr.consent.form.enabled" : ""),
        ADS_WELCOME(isIos() ? "-ads.welcome.enabled" : "--ez \"isAdsWelcomeEnabled\""),
        REGWALL_FAVORITES(isIos() ? "-regwall.favorites.enabled" : "--ez \"isFavoriteRegWallEnabled\""),
        BANNER_ADS(isIos() ? "-bannerads.enabled" : "--ez \"isAdBannerEnabled\""),
        SHOW_REGWALL_ONLAUNCH(isIos() ? "" : "--ez \"isFirstLaunchRegWallEnabled\""),
        APP_ENVIRONMENT(isIos() ? "-app_settings_environment" : ""),
        PLAYER_SHAREPLAY_ENABLED(isIos() ? "-player.shareplay.enabled" : ""),
        PLAYER_OFFLINE_SHAREPLAY_ENABLED(isIos() ? "-player.offlineShareplay.enabled" : ""),
        BRAZE_INAPP_MESSAGE_DISCARD(isIos() ? "-braze.inAppMessages.discard" : "--ez \"areInAppNotificationsEnabled\""),
        SPEED_CONTROL_TOOLTIP_ENABLED(isIos() ? "" : "--ez \"isSpeedControlTooltipEnabled\""),
        API_OPML_STARTUP_FLOW(isIos() ? "-account.startupFlow" : ""),
        API_OPML_SUBSEQUENT_STARTUP_FLOW(isIos() ? "-account.subsequentStartupFlow" : ""),
        UNIFIED_EVENTS_INTERVAL(isIos() ? "-unified.events.interval" : ""),
        UNIFIED_EVENTS_ENABLED(isIos() ? "-unified.events.enabled" : "--ez \"enableInstantUnifiedEventsReport\""),
        SEARCH_AUTOCOMOPLETE_SUGGESTIONS_ENABLED(isIos() ? "" : "--ez \"areSearchAutocompleteSuggestionsEnabled\""),
        ADS_ACC_TIME_BETWEEN_ROLLS_IN_SECONDS(isIos() ? "-ads.acc.timeBetweenRollsInSeconds" : "--ei \"setTimeBetweenRollsInSeconds\""),
        PLAYER_AUTOPLAY_ENABLED((config().isReportingTesting() && isIos()) ? "-player.autoPlay.enabled" : ""),
        PLAYER_AUTOPLAY_DEFAULT_VALUE((config().isReportingTesting() && isIos()) ? "-player.autoPlay.defaultValue" : ""),
        BOOST_TOOLTIP_ENABLED(isIos() ? "-boost.tooltip.enabled" : "--ez \"isBoostTooltipEnabled\""),
        VIDEO_PREROLL_ENABLED(isIos() ? "-video.preroll.enabled" : "--ez \"enableVideAds\""),
        BOOST_ENABLED(isIos() ? "-boost.enabled" : "--ez \"isBoostEnabled\""),
        ADS_PREROLL_TYPE(isIos() ? "-ads.preroll.simplification.behavior.type" : ""),
        DATA_OPT_OUT(isIos() ? "-ads.dataOptOut.enabled" : "");

        private String launchArgument;

        LaunchArgumentsTypes(String launchArgument) {
            this.launchArgument = launchArgument;
        }

        public String getLaunchArgumentValue() {
            return launchArgument;
        }

        public static LaunchArgumentsTypes getLaunchArgumentType(final String launchArgument) {
            List<LaunchArgumentsTypes> launchArgumentList = Arrays.asList(LaunchArgumentsTypes.values());
            return launchArgumentList.stream().filter(eachMoreOptionItem -> eachMoreOptionItem.toString().equals(launchArgument))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return launchArgument;
        }
    }
}
