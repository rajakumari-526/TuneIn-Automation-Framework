package com.tunein.mobile.pages.ios.navigation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.ios.contentprofile.IosContentProfilePage.ElementLocatorTypes.TYPE_TEXT;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.MEDIUM;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class IosContentsListPage extends ContentsListPage {

    public static final String CONTENT_SPEAKER_STRING_LOCATOR = ".//XCUIElementTypeImage[@visible='true'][1]";

    private final static String CONTENT_PROFILE_CATEGORY_WITH_NAME_LOCATOR = "name CONTAINS \"%s\" AND type == '" + TYPE_TEXT.getValue() + "' AND visible == true";

    public ElementsCollection contentCellContainer = $$(xpath("//XCUIElementTypeTable/XCUIElementTypeCell")).as("Cell container");

    protected SelenideElement seeAllButton = $(iOSNsPredicateString("label == \"See All\" AND type == \"XCUIElementTypeButton\"")).as("See all button");

    @Step("Click on See All button from {category} category page")
    @Override
    public ContentsListPage tapOnSeeAllButtonUnderCategoryTitle(CategoryType category) {
        closePermissionPopupsIfDisplayed();
        By categoryTitle = iOSNsPredicateString(String.format(CONTENT_PROFILE_CATEGORY_WITH_NAME_LOCATOR, category.getCategoryTypeValue()));
        scrollTo(categoryTitle, DOWN, MEDIUM);
        clickOnElement(scrollTo(seeAllButton, DOWN));
        return contentsListPage.waitUntilPageReady();
    }

    @Override
    public SelenideElement getCellFromContainer(int index) {
        return contentCellContainer.get(index);
    }

    @Step
    @Override
    public ContentsListPage validateEpisodeSpeakerOrGreenIconDisplayed(int index) {
        SelenideElement contentCell = contentsList.get(index);
        assertThat(isElementDisplayed(contentCell.$(By.xpath(CONTENT_SPEAKER_STRING_LOCATOR))))
                .as("Speaker icon is not displayed for " + index)
                .isTrue();
        return this;
    }

    @Override
    public ContentsListPage validateCaretButtonDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Step
    @Override
    public String getContentCellTitleTextByIndex(int index) {
        SelenideElement contentCell = contentsList.get(index);
        if (!isEpisodeCell(index)) {
            String contentCellTitle = getElementText(contentCell.$x("XCUIElementTypeOther"));
            return (contentCellTitle.split("\\. ", 0)[0]).replaceAll("[.]$", "");
        }
        return getElementText(contentCell.$x("XCUIElementTypeStaticText[2]"))
                .replaceAll("[.]$", "");
    }

    private boolean isEpisodeCell(int index) {
        SelenideElement contentCell = contentsList.get(index);
        try {
            contentCell.$(iOSNsPredicateString("name == 'more options'")).shouldBe(visible);
        } catch (Throwable ex) {
            return false;
        }
        return true;
    }

}
