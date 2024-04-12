package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public abstract class CustomUrlFavoriteDialog extends BasePage {

    protected SelenideElement customUrlFavoriteTitle = $(android(id("tunein.player:id/dialog_title_container"))
            .ios(iOSNsPredicateString("label == \"Custom Stream\" AND type == \"XCUIElementTypeAlert\""))).as("Favorite title");

    protected SelenideElement customUrlNameField = $(android(id("tunein.player:id/favorites_custom_name"))
            .ios(iOSNsPredicateString("type == \"XCUIElementTypeTextField\""))).as("name field");

    protected SelenideElement customUrlSubmitButton = $(android(id("android:id/button1"))
            .ios(accessibilityId("OK"))).as("Submit button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public CustomUrlFavoriteDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(customUrlFavoriteTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    public NowPlayingPage tapSubmitButton() {
        clickOnElement(customUrlSubmitButton);
        if (regWallPage.isOnRegWallPage()) {
            regWallPage.tapOnRegWallCloseButton();
        }
        return nowPlayingPage.waitUntilPageReady();
    }

}
