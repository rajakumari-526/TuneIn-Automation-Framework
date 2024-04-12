package com.tunein.mobile.pages.android.navigation;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidContentsListPage extends ContentsListPage {

    private final static String CONTENT_CELL_TITLE_LOCATOR = ".//android.widget.TextView[contains(@resource-id,'status_cell_title_id') or contains(@resource-id,'row_square_cell_title')]";

    protected SelenideElement caretButton = $(id("tunein.player:id/view_model_container_right_arrow")).as("Caret button");

    @Override
    public ContentsListPage tapOnSeeAllButtonUnderCategoryTitle(CategoryType category) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

    @Step
    @Override
    public ContentsListPage validateEpisodeSpeakerOrGreenIconDisplayed(int index) {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public ContentsListPage validateCaretButtonDisplayed() {
        assertThat(isElementDisplayed(caretButton)).as("Stations list caret button is not displayed").isTrue();
        return this;
    }

    @Step
    @Override
    public String getContentCellTitleTextByIndex(int index) {
        SelenideElement cellTitle = contentsList.get(index).$(By.xpath(CONTENT_CELL_TITLE_LOCATOR));
        return getElementText(cellTitle);
    }

    @Override
    public SelenideElement getCellFromContainer(int index) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

}
