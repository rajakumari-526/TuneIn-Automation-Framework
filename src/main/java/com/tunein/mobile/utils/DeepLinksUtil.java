package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.ScreenFacade;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.testdata.models.Contents;
import org.openqa.selenium.WebDriverException;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.STATION_TYPE_SHOW;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.DEEPLINK;
import static com.tunein.mobile.utils.ApplicationUtil.*;

public class DeepLinksUtil extends ScreenFacade {

    /* --- Constants --- */

    public static final String DEEPLINK_PREFIX = "tunein:/"; // tunein://tune/s1231231

    public static final String DEEPLINK_PREFIX_ACTION_PROFILE = "/profile/";

    public static final String DEEPLINK_EXPLORER = DEEPLINK_PREFIX + "/explorer";

    public static final String DEEPLINK_PREFIX_ACTION_TUNE = "/tune/";

    public static final String DEEPLINK_PREFIX_CONTENT_STATION = "s";

    public static final String DEEPLINK_PREFIX_CONTENT_TOPIC = "t";

    /** "/tune/s" */
    public static final String DEEPLINK_PREFIX_STATION = DEEPLINK_PREFIX_ACTION_TUNE + DEEPLINK_PREFIX_CONTENT_STATION;

    /** "/tune/t" */
    public static final String DEEPLINK_PREFIX_EPISODE_TOPIC = DEEPLINK_PREFIX_ACTION_TUNE + DEEPLINK_PREFIX_CONTENT_TOPIC;

    /** "tunein://tune/s" */
    public static final String DEEPLINK_PREFIX_TUNE_STATION = DEEPLINK_PREFIX + DEEPLINK_PREFIX_STATION;

    /** "tunein://tune/t" */
    public static final String DEEPLINK_PREFIX_TUNE_PODCAST = DEEPLINK_PREFIX + DEEPLINK_PREFIX_EPISODE_TOPIC;

    public static final String SEARCH_BY_GUIDE_ID_PREFIX = "aa6-";

    /* --- Helper Methods --- */

    @Step("Open deeplink {deeplinkUrl}")
    public void openURL(String deeplinkUrl) {
        try {
            getAppiumDriver().get(deeplinkUrl);
        } catch (WebDriverException exception) {
            exception.printStackTrace();
            ReporterUtil.log("Cannot open " + deeplinkUrl);
        }
        if (isDeeplinkPermissionValueIsNull()) {
            setDeeplinkPermissionValue(getThreadId(), false);
        }
    }

    @Step("Open stream: {content}")
    public NowPlayingPage openTuneThroughDeeplink(Contents content, boolean... autoFollow) {
        if (content.getContentProfileType().equals(STATION_TYPE_SHOW.getContentProfileType())) {
            openContentProfileThroughDeeplink(content);
            contentProfilePage.tapProfilePlayButtonLightVersion();
        } else {
            String suffix = (autoFollow.length > 0 && autoFollow[0]) ? "?autoFollow=true" : "";
            openURL(content.getStreamTuneDeepLink() + suffix);
        }
        return nowPlayingPage.waitUntilPageReadyWithKnownContent(content);
    }

    @Step("Open stream: {content} with autoPlay = {autoPlay}")
    public NowPlayingPage openTuneThroughDeeplink(boolean autoPlay, Contents content) {
        if (content.getContentProfileType().equals(STATION_TYPE_SHOW.getContentProfileType())) {
            openContentProfileThroughDeeplink(content);
            contentProfilePage.tapProfilePlayButtonLightVersion();
        } else {
            String suffix = autoPlay ? "?autoPlay=true" : "?autoPlay=false";
            openURL(content.getStreamTuneDeepLink() + suffix);
        }
        return nowPlayingPage.waitUntilPageReadyWithKnownContent(content);
    }

    @Step("Open deeplink in Safari: {deeplinkUrl}")
    public void openDeeplinkInSafari(String deeplinkUrl) {
        if (!isIos()) {
            throw new UnsupportedOperationException("Not supported on Android");
        }
        BrowserUtil.launchSafariBrowser();
        BrowserUtil.openDeeplinkUrlInSafari(deeplinkUrl);
    }

    @Step("Open content profile page for {content}")
    public ContentProfilePage openContentProfileThroughDeeplink(Contents content) {
        openURL(content.getStreamProfileDeepLink());
        return contentProfilePage.waitUntilPageReady();
    }

    @Step("Open contents list page for {genresAndCategoriesList}")
    public ContentsListPage openContentsListThroughDeeplink(GenresAndCategoriesList genresAndCategoriesList) {
        openURL(genresAndCategoriesList.getUrl());
        return contentsListPage.waitUntilPageReady();
    }

    @Step
    public void createNewDeviceServiceThroughDeeplink() {
        if (isIos()) {
            navigationAction.navigateTo(HOME);
            openURL("tunein://AppSettingsOverride/?discardSerial=true");
            GestureActionUtil.scrollToRefresh();
        }
    }

    @Step
    public void openContentProfileThroughDeeplinkLightVersion(Contents content) {
        openURL(content.getStreamProfileDeepLink());
    }

    @Step("Pre-setup deep-links")
    public void deeplinkPreSetup() {
        if (isIos()) {
            openURL("tunein://");
            closePermissionPopupFor(DEEPLINK);
        }
    }

    @Step
    public RegWallPage openRegWallThroughDeeplink() {
        openURL("tunein://signup?landing=c100000195");
        return regWallPage.waitUntilPageReady();
    }

    @Step
    public DeepLinksUtil openDeeplinkWithGuideId(Contents content) {
        String id = content.getSearchQuery().split("-")[1];
        openURL("tunein://?guideid=" + id + "&autoFollow=true");
        return this;
    }

    public enum GenresAndCategoriesList {
        DEEPLINK_MUSIC_CATEGORY_60S("tunein://browse/g407"),
        DEEPLINK_BASEBALL_CATEGORY("tunein://browse/c16230079"),
        DEEPLINK_PREMIER_LEAGUE_CATEGORY("tunein://browse/c100006105"),
        DEEPLINK_BUSINESS_AND_FINANCE_CATEGORY("tunein://browse/c100001521");
        private String url;

        private GenresAndCategoriesList(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }
}
