package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.testdata.models.Contents;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static io.appium.java_client.AppiumBy.*;
import static org.openqa.selenium.By.xpath;

public abstract class EpisodeModalDialog extends BasePage {

    /**Episode Details*/

    protected SelenideElement episodeDrawer = $(android(id("view_model_list"))
            .ios(xpath("//XCUIElementTypeScrollView[.//XCUIElementTypeButton[@name!='Cancel']]"))).as("Drawer");

    protected SelenideElement downloadEpisodeButton = $(android(id("prompt_button"))
            .ios(iOSNsPredicateString("label == \"Download\""))).as("Download episode button");

    protected SelenideElement deleteEpisodeButton = $(android(androidUIAutomator("text(\"DELETE\")"))
            .ios(id("Delete"))).as("Delete episode button");

    protected SelenideElement downloadSpinnerImage = $(android(id("tunein.player:id/in_progress_spinner"))).as("Download spinner image");

    protected SelenideElement shareEpisodeIcon = $(android(id("episode_share_id"))
            .ios(iOSNsPredicateString("label == \"Share\""))).as("Share episode icon");

    /* --- Loadable Component Method --- */

    /* --- Action Methods --- */

    public abstract void tapEpisodePlayButton(Contents... content);

    public abstract String getEpisodeDuration();

    public abstract void closeEpisodeModalDialog();

    public abstract EpisodeModalDialog tapEpisodeDownloadButton();

    @Step("Tap Episode Delete Button")
    public EpisodeModalDialog tapEpisodeDeleteButton() {
        clickOnElement(deleteEpisodeButton);
        return this;
    }

    /* --- Validation Methods --- */

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> episodeModalDialogElements();

}
