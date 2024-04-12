package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.StreamBitrate;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.StreamFormat;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public abstract class NowPlayingChooseStreamDialog extends BasePage {

    protected SelenideElement chooseStreamTitle = $(android(androidUIAutomator("text(\"Choose stream\")"))
            .ios(iOSNsPredicateString("name == 'Choose stream' AND type == 'XCUIElementTypeStaticText'"))).as("Title");

    protected SelenideElement chooseStreamCancelButton = $(android(androidUIAutomator("text(\"CANCEL\")"))
            .ios(iOSNsPredicateString("label == 'Cancel'"))).as("Cancel button");

    public ElementsCollection streamTypes = $$(android(androidUIAutomator("resourceIdMatches(\"^.*row_.*$\").className(android.widget.TextView)"))
            .ios(iOSNsPredicateString("//XCUIElementTypeOther/XCUIElementTypeButton[@visible='true' and @name != 'Cancel']"))).as("Stream types");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public NowPlayingChooseStreamDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(chooseStreamTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    public abstract NowPlayingPage chooseRequiredStreamFormat(StreamFormat streamFormat);

    public abstract NowPlayingPage chooseRequiredStreamFormat(StreamFormat streamFormat, StreamBitrate streamBitrate);

    @Step
    public NowPlayingPage tapCancelButton() {
        clickOnElement(chooseStreamCancelButton);
        return nowPlayingPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    public abstract List<StreamFormat> getStreamFormatList();
}
