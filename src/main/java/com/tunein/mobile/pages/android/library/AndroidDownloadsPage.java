package com.tunein.mobile.pages.android.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.library.DownloadsPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.PODCAST;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;

public class AndroidDownloadsPage extends DownloadsPage {

    protected SelenideElement downloadsEditButton = $(id("menu_edit")).as("Edit button");

    private static final String ANDROID_EPISODE_LOCATOR = "//android.view.ViewGroup[./*[@text='%s' and @resource-id='tunein.player:id/titleTxt']]";

    @Override
    public DownloadsPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(downloadsEditButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Tap on episode or audiobook with {name}")
    @Override
    public NowPlayingPage tapOnContentName(String name) {
        clickOnElement(By.xpath(String.format(ANDROID_EPISODE_LOCATOR, name)), Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        return nowPlayingPage.waitUntilPageReadyWithKnownContentType(PODCAST);
    }

    @Step("Get content with name {contentName} in downloads")
    @Override
    public SelenideElement getDownloadsContentWithName(String contentName) {
        String format = String.format(ANDROID_EPISODE_LOCATOR, contentName.replaceAll("\"", "\\\""));
        return scrollTo(By.xpath(format), DOWN, 3).as("Download content with name: " + contentName);
    }

    @Step("Get station name {stationName} from downloads")
    @Override
    public SelenideElement getDownloadsStationName(String stationName) {
        throw new UnsupportedOperationException("This functionality is absent for iOS Platform");
    }
}