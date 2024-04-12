package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.AboutPage;
import com.tunein.mobile.pages.common.userprofile.HelpCenterWebPage;
import com.tunein.mobile.pages.common.userprofile.LegalNoticesPage;
import com.tunein.mobile.utils.ReporterUtil;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.AttributeType.VALUE;
import static com.tunein.mobile.utils.ElementHelper.getElementAttributeValue;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static io.appium.java_client.AppiumBy.iOSClassChain;

public class IosAboutPage extends AboutPage {

    protected SelenideElement deviceServiceIdentifier = $(iOSClassChain("**/XCUIElementTypeCell[`name == 'DevelopmentSectionSerialId'`]/XCUIElementTypeStaticText[`name == 'Subtitle'`]")).as("Device service identifier");

    @Override
    @Step("Get \"Device Service Identifier\" from the \"About TuneIn\" page")
    public String getDeviceServiceIdentifier() {
        ReporterUtil.log("Get \"Device Service Identifier\" from the \"About TuneIn\" page");
        try {
            return getElementAttributeValue(scrollTo(deviceServiceIdentifier, DOWN, config().scrollFewTimes()), VALUE);
        } catch (Exception ignored) {
            ReporterUtil.log("Unable to find \"Device Serial\" from the \"About TuneIn\" screen");
            takeScreenshot();
            return "";
        }
    }

    @Override
    public LegalNoticesPage tapOnLegalNoticesButton() {
        clickOnElement(scrollTo(aboutPageLegalNoticesButton, DOWN));
        return legalNoticesPage.waitUntilPageReady();
    }

    @Step
    @Override
    public HelpCenterWebPage tapOnHelpCenterButton() {
        clickOnElement(scrollTo(aboutPageHelpCenterButton, DOWN).as("Help Center button"));
        return helpCenterWebPage.waitUntilPageReady();
    }
}
