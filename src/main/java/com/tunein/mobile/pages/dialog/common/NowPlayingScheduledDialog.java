package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public abstract class NowPlayingScheduledDialog extends BasePage {

    protected SelenideElement scheduleCardShareButton = $(android(androidUIAutomator("text(\"Share\")"))
            .ios(accessibilityId("Share"))).as("Share button");

    protected SelenideElement scheduleCardMoreButton = $(android(androidUIAutomator("text(\"More\")"))
            .ios(accessibilityId("More"))).as("More button");

    protected SelenideElement scheduleCardFavoriteButton = $(android(androidUIAutomator("textContains(\"avorite\")"))
            .ios(iOSNsPredicateString("label == 'Favorite'"))).as("Favorite button");

    /* --- Loadable Component Method --- */

    @Step("Wait until nowplaying scheduled dialog is loaded")
    @Override
    public NowPlayingScheduledDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(scheduleCardMoreButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click on favorite button")
    public NowPlayingScheduledDialog clickOnFavoriteButton() {
        clickOnElement(scheduleCardFavoriteButton);
        return this;
    }

    @Step("Click on more button")
    public ContentProfilePage clickOnScheduleCardMoreButton() {
        clickOnElement(scheduleCardMoreButton);
        return contentProfilePage.waitUntilPageReady();
    }

    @Step("Click on share button")
    public ShareDialog clickOnShareButton() {
        clickOnElement(scheduleCardShareButton);
        return shareDialog.waitUntilPageReady();
    }

    /* --- Helper Methods --- */

    public abstract String getStreamName();

    public abstract HashMap<String, SelenideElement> schedulePageElements();

}
