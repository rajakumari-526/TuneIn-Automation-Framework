package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.userprofile.TermsOfServicePage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosTermsOfServicePage extends TermsOfServicePage {

    protected SelenideElement termsOfServiceBackButton = $(iOSNsPredicateString("label == 'Back'")).as("Back button");

    @Step
    @Override
    public RegWallPage tapBackButton() {
        clickOnElement(termsOfServiceBackButton);
        return regWallPage.waitUntilPageReady();
    }

}
