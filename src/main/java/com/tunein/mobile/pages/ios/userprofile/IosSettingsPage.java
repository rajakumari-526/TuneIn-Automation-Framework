package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.DeleteYourAccountPage;
import com.tunein.mobile.pages.common.userprofile.SettingsPage;
import com.tunein.mobile.testdata.models.Users;
import com.tunein.mobile.utils.ReporterUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.DATA_OPT_OUT;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.VIDEO_PREROLL_ENABLED;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public class IosSettingsPage extends SettingsPage {

    protected SelenideElement deleteYourAccount = $(accessibilityId("Delete Your Account")).as("Delete your account");

    protected SelenideElement backUpAlarmSettings = $(iOSNsPredicateString("label=\"Backup Alarm Sound\"")).as("Backup alarm settings");

    protected SelenideElement preferredStreamQuality = $(iOSNsPredicateString("label=\"Preferred Stream Quality\"")).as("Preferred Stream Quality");

    protected SelenideElement cellularDataInStreamPlayer = $(iOSNsPredicateString("label=\"Cellular Data\"")).as("Cellar data in stream player");

    @Step("Tap on \"Enable Scrollable NowPlaying\"")
    @Override
    public SettingsPage tapOnEnableScrollableNowPlayingTitle() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
        return this;
    }

    @Step("Enable \"Instant Unified Events\" reporting option")
    @Override
    public SettingsPage enableInstantUnifiedEventsReportingFlow() {
        ReporterUtil.log("Functionality is absent for iOS Platform");
        return this;
    }

    @Step("Set value {toEnable} for \"enable video ads\" option in Settings page")
    @Override
    public SettingsPage enableVideoAds(boolean toEnable) {
        updateLaunchArgumentFor(VIDEO_PREROLL_ENABLED, String.valueOf(toEnable));
        return this;
    }

    @Step("Set value {toEnable} for \"Ad Data Opt Out\" option in Settings page")
    @Override
    public SettingsPage enableOptOut(boolean toEnable) {
        updateLaunchArgumentFor(DATA_OPT_OUT, String.valueOf(toEnable));
        upsellPage.closeUpsell();
        return this;
    }

    @Override
    public void enableScrollableNowPlayingFlow() {
        ReporterUtil.log("Scrollable Now Playing is enabled");
    }

    @Step("Tap on \"Delete Your Account\"")
    @Override
    public DeleteYourAccountPage tapOnDeleteYourAccount() {
        clickOnElement(scrollTo(deleteYourAccount, DOWN, 5));
        return deleteYourAccountPage.waitUntilPageReady();
    }

    @Step("Tap on \"Exit\" button")
    @Override
    public void tapOnExitButton() {
        throw new UnsupportedOperationException("This functionality is absent for iOS Platform");
    }

    @Step("Tap on force to request auto download")
    @Override
    public void updateAutoDownloadConfig(boolean enableAutoDownload) {
        throw new UnsupportedOperationException("This functionality is absent for iOS Platform");
    }

    @Step
    @Override
    public SettingsPage validateDeleteAccountButtonNotDisplayed() {
        assertThat(isElementNotDisplayed(deleteYourAccount)).as("Delete Your Account is displayed").isTrue();
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> settingsPageElements(Users user) {
        return new LinkedHashMap<>() {{
            put("Display", displaySettings);
            put("Autoplay", autoPlaySettings);
            put("Backup Alarm Sound", backUpAlarmSettings);
            put("Waze Navigation", wazeNavigationSettings);
            if (user.isPremium()) {
                put(SKIP_TEXT_VALIDATION_PREFIX, enableAlexaLiveSkill);
            }
            put("Autodownload", autoDownloadSettings);
            put("Recently Listened to Podcasts", recentlyListenedPodcasts);
            put("Use Cellular Data", useCellularData);
            put("Preferred Stream Quality", preferredStreamQuality);
            put("Cellular Data", cellularDataInStreamPlayer);
            put("Delete Your Account", deleteYourAccount);
        }};
    }

    @Override
    @Step("Get \"Device Serial\" from the \"About TuneIn\" screen")
    public String getDeviceServiceIdentifier() {
        throw new UnsupportedOperationException("This functionality is absent for the iOS platform");
    }

    @Override
    public void validateSerialNumberIsNotEqualTo(String beforeSerialId) {
        throw new UnsupportedOperationException("This functionality is absent for the iOS platform");
    }

    @Override
    public void validateSerialNumberIsEqualTo(String beforeSerialId) {
        throw new UnsupportedOperationException("This functionality is absent for the iOS platform");
    }
}
