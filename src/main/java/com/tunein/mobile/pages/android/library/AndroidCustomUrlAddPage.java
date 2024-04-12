package com.tunein.mobile.pages.android.library;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.library.CustomUrlAddPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.CUSTOM_URL;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class AndroidCustomUrlAddPage extends CustomUrlAddPage {

    protected SelenideElement customUrlSaveButton = $(androidUIAutomator("text(\"SAVE\")")).as("Save button");

    @Step
    @Override
    public NowPlayingPage saveCustomURL() {
        clickOnElement(customUrlSaveButton);
        if (regWallPage.isOnRegWallPage()) {
            regWallPage.tapOnRegWallCloseButton();
        }
        return nowPlayingPage.waitUntilPageReadyWithKnownContentType(CUSTOM_URL);
    }
}
