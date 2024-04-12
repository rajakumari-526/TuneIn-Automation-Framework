package com.tunein.mobile.pages.common.navigation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.SkipException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.library.RecentsPage.getItemsUnderRecentsCategory;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.waitUntilNumberOfElementsMoreThanZero;
import static io.appium.java_client.AppiumBy.androidUIAutomator;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class ContentsListPage extends BasePage {

    protected ElementsCollection contentsList = $$(android(xpath("//androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout | //androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[not(@resource-id='tunein.player:id/content_frame') and not(@resource-id='tunein.player:id/prompt_button_container') ] | //android.widget.LinearLayout[./*[@resource-id='tunein.player:id/follow_button']] | //android.widget.GridView/android.view.ViewGroup[.//*[@resource-id='tunein.player:id/row_tile_image']]"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeCell[@visible='true']"))).as("Contents list");

    protected SelenideElement pickTeamsButton = $(android(androidUIAutomator("text(\"PICK YOUR TEAMS\")"))
            .ios(iOSNsPredicateString("label == 'Pick Your Teams' AND type == 'XCUIElementTypeButton'"))).as("Pick teams button");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public ContentsListPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilNumberOfElementsMoreThanZero(contentsList, Duration.ofMillis(config().waitLongTimeoutMilliseconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click on content by index {index}")
    public void clickOnContentCellByIndex(int index, ContentProvider.ContentType... contentType) {
        closePermissionPopupsIfDisplayed();
        clickOnElement(contentsList.get(index));
        if (upsellPage.isOnUpsellPage()) {
            upsellPage.waitUntilPageReady();
        } else if (contentProfilePage.isOnContentProfilePage()) {
            contentProfilePage.waitUntilPageReady();
        } else {
            if (contentType.length > 0 && !contentType[0].getContentName().isEmpty()) {
                nowPlayingPage.waitUntilPageReadyWithKnownContentType(contentType[0]);
            } else {
                nowPlayingPage.waitUntilPageReady();
            }
        }
    }

    @Step
    public TeamsPage tapOnPickYourTeams() {
        clickOnElement(scrollTo(pickTeamsButton, DOWN));
        return teamsPage.waitUntilPageReady();
    }

    public abstract ContentsListPage tapOnSeeAllButtonUnderCategoryTitle(CategoryType category);

    @Step("Change order of the element {startElement} in the list ")
    public ContentsListPage reorderingTheElements(SelenideElement startElement, SelenideElement endElement) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), endElement.getLocation().x, endElement.getLocation().y + endElement.getSize().height / 2));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(10000), PointerInput.Origin.viewport(), startElement.getLocation().x, startElement.getLocation().y - endElement.getSize().height / 3));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        getAppiumDriver().perform(Collections.singletonList(longPress));
        return waitUntilPageReady();
    }

    /* --- Validation Methods --- */

    @Step
    public ContentsListPage validateContentListIsNotEmpty() {
        assertThat(contentsList).as("Contents list is empty").isNotEmpty();
        return this;
    }

    @Step
    public ContentsListPage validateContentListsOrder(List<Contents> expectedContenstList) {
        ElementsCollection elements = getItemsUnderRecentsCategory();
        List<String> expectedStreamNames = new ArrayList<>();
        List<String> actualStreamNames = new ArrayList<>();
        for (int i = expectedContenstList.size() - 1; i < expectedContenstList.size(); i--) {
            String stationName = expectedContenstList.get(i).getStreamName().toLowerCase();
            expectedStreamNames.add(stationName);
            if (i <= 0) {
                break;
            }
        }
        for (int j = 0; j < elements.size(); j++) {
            String actualName = getElementText(elements.get(j)).split("\\. ", 0)[0].replaceAll("[.]$", "").toLowerCase();
            actualStreamNames.add(actualName);
        }
        assertThat(actualStreamNames).as("Recent Order is not matching").containsExactlyElementsOf(expectedStreamNames);
        return this;
    }

    @Step
    public ContentsListPage validateContentListSizeIsGreaterThanOrEqualTo(int minimumSize) {
        assertThat(contentsList.size() >= minimumSize).as("Contents list size is " + contentsList.size() + " which is less than " + minimumSize).isEqualTo(true);
        return this;
    }

    public abstract ContentsListPage validateEpisodeSpeakerOrGreenIconDisplayed(int index);

    public abstract ContentsListPage validateCaretButtonDisplayed();

    @Step
    public ContentsListPage skipTestIfNotOnContentsListPage() {
        if (!isOnContentsListPage()) {
            throw new SkipException("Skipping test, not enough stations for contents list page to open");
        }
        return this;
    }

    @Step("Validate that Contents List page is displayed")
    public ContentsListPage validateContentsListPageIsDisplayed() {
        assertThat(isOnContentsListPage()).as("Contents List page is not displayed").isTrue();
        return this;
    }

    @Step("Validate category {categoryType} is displayed")
    public ContentsListPage validateThatCategoryIsDisplayed(CategoryType categoryType) {
        SelenideElement requiredElement = getCategoryHeader(categoryType, DOWN);
        assertThat(isElementDisplayed(requiredElement)).as("Contents List page is not displayed").isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnContentsListPage() {
        closePermissionPopupsIfDisplayed();
        return !contentsList.isEmpty() && isElementDisplayed(contentsList.get(1), Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    public abstract String getContentCellTitleTextByIndex(int index);

    @Step("Verify that content with name {contentNameBeforeReorder} reordered states is {isReordered}")
    public void validateOrderOfList(String contentNameBeforeReorder, String contentNameAfterReorder, boolean isReordered) {
        if (isReordered) {
            assertThat(contentNameBeforeReorder).as("Content is not reordered").isNotEqualTo(contentNameAfterReorder);
        } else {
            assertThat(contentNameBeforeReorder).as("Content is reordered").isEqualTo(contentNameAfterReorder);
        }
    }

    public abstract SelenideElement getCellFromContainer(int index);

    public SelenideElement getElementFromContentList(int index) {
        return contentsList.get(index);
    }

}
