package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import com.google.inject.Singleton;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;

@Singleton
public class DeleteYourAccountPage extends BasePage {

    protected SelenideElement areYouSureOnDeletePage = $(android(id("absent"))
            .ios(iOSNsPredicateString("type = 'XCUIElementTypeStaticText' AND name = 'Are you sure?' AND accessible = true"))).as("Are you sure?");

    protected SelenideElement deleteYourAccountButton = $(android(id("absent"))
            .ios(iOSNsPredicateString("name == 'Delete Your Account' AND type == 'XCUIElementTypeButton'"))).as("'Delete your account' button");

    protected SelenideElement requestReceivedCloseButton = $(android(id("absent"))
            .ios(iOSNsPredicateString("name == 'Close'"))).as("Close button");

    @Step
    public DeleteYourAccountPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(areYouSureOnDeletePage, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        return this;
    }

    @Step
    public void tapOnDeleteYourAccountButton() {
        clickOnElement(deleteYourAccountButton);
        clickOnElement(requestReceivedCloseButton);
    }
}
