package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.userprofile.TuneInPremiumPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;

public class IosTuneInPremiumPage extends TuneInPremiumPage {

    protected SelenideElement goPremiumToday = $(iOSNsPredicateString("label == \"Go Premium Today\"")).as("Go Premium Today button");

    protected SelenideElement tuneInPremiumTitle = $(iOSNsPredicateString("label == \"TuneIn Premium\"")).as("TuneIn Premium title");

    protected SelenideElement signIntoPremium = $(iOSNsPredicateString("label == 'Sign into your TuneIn Account'")).as("'Sign into your account' button");

    @Step
    @Override
    public RegWallPage tapSignIntoTuneInAccount() {
        clickOnElement(signIntoPremium);
        return regWallPage.waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public TuneInPremiumPage validateThatPremiumPageTitleIsDisplayed() {
        assertThat(isElementDisplayed(tuneInPremiumTitle)).as("Premium Title is not displayed").isTrue();
        return this;
    }

    @Step
    public TuneInPremiumPage validateThatGoPremiumTodayIsDisplayed() {
        assertThat(isElementDisplayed(goPremiumToday)).as("Go Premium Today is not displayed").isTrue();
        return this;
    }
}
