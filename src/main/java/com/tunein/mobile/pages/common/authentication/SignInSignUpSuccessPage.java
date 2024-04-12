package com.tunein.mobile.pages.common.authentication;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public class SignInSignUpSuccessPage extends BasePage {

    public static final String SUCCESS_PREMIUM_TEXT = "Commercial-free news and music. Less ads on 100,000 radio stations.";

    protected SelenideElement signInSuccessFinishButton = $(iOSNsPredicateString("name CONTAINS 'regwallSuccessScreenFinishButtonId'")).as("Finish button");

    protected SelenideElement signInSuccessPageTitle = $(iOSNsPredicateString("name == 'Devices'")).as("Page title");

    protected SelenideElement signInSuccessImage = $(iOSNsPredicateString("name == 'regwallTitleId'")).as("Page image");

    protected SelenideElement signInSuccessPageSubtitle = $(iOSNsPredicateString("name == 'regwallTextId'")).as("Page subtitle");

    protected SelenideElement signInSuccessGoPremiumButton = $(iOSNsPredicateString("name == 'regwallSuccessScreenLinkAlexaButtonId'")).as("Go Premium button");

    protected SelenideElement signInSuccessPremiumImage = $(iOSNsPredicateString("name == 'regwallSuccessScreenBannerImageId'")).as("Premium image");

    protected SelenideElement signInSuccessPremiumText = $(iOSNsPredicateString("name == 'regwallSuccessScreenLinkAlexaTextId'")).as("Premium text");

    @Step
    @Override
    public SignInSignUpSuccessPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(signInSuccessFinishButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Tap on finish button")
    public void tapOnFinishButton() {
        clickOnElement(signInSuccessFinishButton);
    }

    @Step
    public SignInSignUpSuccessPage validateSignUpSuccessPremiumText() {
        assertThat(getElementText(signInSuccessPremiumText)).as("Premium text is not displayed on sign up success page").isEqualTo(SUCCESS_PREMIUM_TEXT);
        return this;
    }

    @Step
    public SignInSignUpSuccessPage validatePremiumTextDoesNotExist() {
        assertThat(getElementText(signInSuccessPremiumText)).as("Premium text is displayed on sign in success page").isNotEqualTo(SUCCESS_PREMIUM_TEXT);
        return this;
    }
}
