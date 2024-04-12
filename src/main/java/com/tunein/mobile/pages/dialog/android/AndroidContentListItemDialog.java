package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.ContentListItemDialog;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidContentListItemDialog extends ContentListItemDialog {

    protected SelenideElement unfavoriteDialog = $x("//android.widget.FrameLayout[ (contains(@resource-id,'action_bar_root')) and .//*[contains(@text,'Unfavorite ')]]").as("Unfavorite dialog");

    protected SelenideElement unFavoriteButton = $(AppiumBy.androidUIAutomator("textContains(\"Unfavorite\").className(\"android.widget.TextView\")")).as("Unfavorite button");

    protected SelenideElement dialogTitle = $(By.id("dialog_title")).as("Dialog title");

    @Step
    @Override
    public void clickOnUnfollowButton() {
        clickOnElement(unFavoriteButton);
    }

    @Step
    @Override
    public ContentListItemDialog validateUnfavoriteDialogDisplayed() {
        assertThat(isElementDisplayed(unfavoriteDialog)).as("Unfavorite dialog is not displayed").isTrue();
        return this;
    }

    @Step("Validate dialog title is equal to station name {station}")
    @Override
    public void validateDialogTitleIsEqualToStationName(String station) {
        assertThat(getElementText(dialogTitle)).as("Dialog title is not equal to station").isEqualTo(station);
    }

    @Step("Validate content stream dialog is displayed")
    @Override
    public ContentListItemDialog validateContentStreamDialogIsDisplayed() {
        assertThat(isOnContentListItemDialog()).as("Content stream dialog Page is not opened").isTrue();
        return this;
    }

    @Step("Tap on remove recents")
    @Override
    public void clickOnRemoveRecentsDialog() {
        clickOnElement(removeRecentsDialog);
    }
}
