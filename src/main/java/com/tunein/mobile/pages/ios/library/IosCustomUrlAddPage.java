package com.tunein.mobile.pages.ios.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.library.CustomUrlAddPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.CUSTOM_URL;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosCustomUrlAddPage extends CustomUrlAddPage {

    protected SelenideElement customUrlInvalidUrlLabel = $(iOSNsPredicateString("label == 'Invalid URL'")).as("Invalid url label");

    protected SelenideElement customUrlValidUrlLabel = $(iOSNsPredicateString("name == 'CustomUrlTittle' AND value != 'Invalid URL'")).as("Valid url label");

    @Step
    @Override
    public NowPlayingPage saveCustomURL() {
        clickOnElement(customUrlValidUrlLabel);
        return nowPlayingPage.waitUntilPageReadyWithKnownContentType(CUSTOM_URL);

    }

}
