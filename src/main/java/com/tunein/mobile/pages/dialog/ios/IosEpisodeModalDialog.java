package com.tunein.mobile.pages.dialog.ios;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.EpisodeModalDialog;
import com.tunein.mobile.testdata.models.Contents;
import io.appium.java_client.AppiumBy;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillElementDisappear;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class IosEpisodeModalDialog extends EpisodeModalDialog {

    protected SelenideElement cancelButton = $(AppiumBy.iOSNsPredicateString("label == \"Cancel\"")).as("Cancel button");

    @Step
    @Override
    public EpisodeModalDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(episodeDrawer, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Override
    public void tapEpisodePlayButton(Contents... content) {
        throw new UnsupportedOperationException("Functionality not supported on iOS platform");
    }

    @Step("Close Episode model dialog")
    @Override
    public void closeEpisodeModalDialog() {
        clickOnElement(cancelButton);
    }

    @Step("Tap Episode Download Button")
    @Override
    public EpisodeModalDialog tapEpisodeDownloadButton() {
        clickOnElement(downloadEpisodeButton);
        waitTillElementDisappear(contentProfilePage.downloadSpinnerImage, Duration.ofSeconds(config().fourMinutesInSeconds()));
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> episodeModalDialogElements() {
        int increment = 0;
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, downloadEpisodeButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, shareEpisodeIcon);
        return elementsMap;
    }

    @Override
    public String getEpisodeDuration() {
        throw new UnsupportedOperationException("Functionality not supported on iOS platform");
    }
}
