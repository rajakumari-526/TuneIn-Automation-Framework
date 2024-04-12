package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.EpisodeModalDialog;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.WaitersUtil.waitTillElementDisappear;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.id;

public class AndroidEpisodeModalDialog extends EpisodeModalDialog {

    protected SelenideElement episodeTitle = $(id("tunein.player:id/episode_title_id")).as("Episode title");

    protected SelenideElement episodeDate = $(id("tunein.player:id/episode_date_id")).as("Episode date");

    protected SelenideElement episodeDescription = $(id("tunein.player:id/episode_description_id")).as("Episode description");

    protected SelenideElement playButtonUnderEpisodeDetails = $(id("tunein.player:id/episode_play_button")).as("Play buttonn");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public EpisodeModalDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(playButtonUnderEpisodeDetails, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Tap on episode play button")
    @Override
    public void tapEpisodePlayButton(Contents... content) {
        clickOnElement(playButtonUnderEpisodeDetails);
        handlePageAfterPlayButton(content);
    }

    @Step("Close Episode model dialog")
    @Override
    public void closeEpisodeModalDialog() {
        deviceNativeActions.clickBackButton();
    }

    @Step("Tap Episode Download Button")
    @Override
    public EpisodeModalDialog tapEpisodeDownloadButton() {
        clickOnElement(downloadEpisodeButton);
        waitTillElementDisappear(downloadSpinnerImage, Duration.ofSeconds(config().fourMinutesInSeconds()));
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> episodeModalDialogElements() {
        int increment = 0;
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, downloadEpisodeButton);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, episodeTitle);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, episodeDate);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, episodeDescription);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, shareEpisodeIcon);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, playButtonUnderEpisodeDetails);
        return elementsMap;
    }

    /* --- Helper Methods --- */

    @Step("Get episode duration")
    @Override
    public String getEpisodeDuration() {
        String dateDuration = getElementText(episodeDate);
        String duration = dateDuration.split("·")[1].strip();
        ReporterUtil.log("Episode duration: " + duration);
        return duration;
    }
}
