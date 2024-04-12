package com.tunein.mobile.pages.dialog.common;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static org.openqa.selenium.By.xpath;

public abstract class ContentListItemDialog extends BasePage {

    protected SelenideElement contentItemDialog = $(android(xpath("//*[contains(@resource-id,'dialog_title') or contains(@resource-id,'listViewItems')]"))).as("Item dialog");

    public ElementsCollection contentItemsList = $$(android(xpath("//*[@resource-id='tunein.player:id/listViewItems']//android.widget.TextView"))).as("Items list");

    protected SelenideElement removeRecentsDialog = $(android(androidUIAutomator("className(\"android.widget.TextView\").textContains(\"Remove From Recents\")"))).as("Remove recents dialog");

    /* --- Loadable Component Method --- */

    @Step
    public ContentListItemDialog waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(contentItemDialog, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Actions Methods --- */
    @Step
    public NowPlayingPage tapOnContentItem(int index) {
        clickOnElement(contentItemsList.get(index));
        return nowPlayingPage.waitUntilPageReady();
    }

    public abstract void clickOnUnfollowButton();

    public abstract void clickOnRemoveRecentsDialog();

    /* --- Validation Methods --- */

    public abstract ContentListItemDialog validateContentStreamDialogIsDisplayed();

    public abstract ContentListItemDialog validateUnfavoriteDialogDisplayed();

    public abstract void validateDialogTitleIsEqualToStationName(String station);

    /* --- Helper Methods --- */
    public boolean isOnContentListItemDialog() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(contentItemDialog, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
    }

}
