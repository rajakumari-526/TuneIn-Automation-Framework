package com.tunein.mobile.pages.ios.contentprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.dialog.common.EpisodeModalDialog;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.SkipException;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IosContentProfilePage extends ContentProfilePage {

    private final static String DATE_FORMAT = "MMM d yyyy";

    private static final String TYPE_TEXT = getLocator(ElementLocatorTypes.TYPE_TEXT);

    private static final String TYPE_OTHER = getLocator(ElementLocatorTypes.TYPE_OTHER);

    private static final String TYPE_IMAGE = getLocator(ElementLocatorTypes.TYPE_IMAGE);

    private static final String TYPE_BUTTON = getLocator(ElementLocatorTypes.TYPE_BUTTON);

    private static final String TYPE_CELL = getLocator(ElementLocatorTypes.TYPE_CELL);

    private static final String EPISODE_CELL_TITLE_LOCATOR = "/" + getLocator(ElementLocatorTypes.TYPE_TEXT, 2);

    private static final String EPISODE_CELL_DESCRIPTION_LOCATOR = "/" + getLocator(ElementLocatorTypes.TYPE_OTHER) + "/" + getLocator(ElementLocatorTypes.TYPE_TEXT, 1);

    private static final String EPISODE_CELL_SUBTITLE_LOCATOR = "/" + getLocator(ElementLocatorTypes.TYPE_TEXT, 1);

    private static final String EPISODE_CELL_DURATION_LOCATOR = "//" + getLocator(ElementLocatorTypes.TYPE_TEXT) + "[contains(@name, \"Duration\")]";

    private final static String EPISODE_SPEAKER_LOCATOR = "//" + getLocator(ElementLocatorTypes.TYPE_IMAGE, 1);

    private final static String CONTENT_PROFILE_CATEGORY_WITH_NAME_LOCATOR = "name CONTAINS \"%s\" AND type == '" + TYPE_TEXT + "' AND visible == true";

    private final static String ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR = "//" + TYPE_OTHER + "[contains(@name, \"%s\")]/following-sibling::" + TYPE_CELL + "[%s]";

    private final static String STATION_SPEAKER_ICON_LOCATOR = "//" + TYPE_IMAGE + "[@name='phone_speaker_high' and @visible='true']";

    private static String getLocator(ElementLocatorTypes type, int... index) {
        if (index.length > 0) {
            List<int[]> invalidValue = Arrays.asList(index);
            if (invalidValue.contains(0)) {
                throw new Error("Unsupported index " + Arrays.toString(index) + ", use value > 0");
            }
            // return example `XCUIElementTypeCell[4]`
            return type.getValue() + Arrays.toString(index);
        }
        return type.getValue() + "";
    }

    /* --- UI Elements --- */

    protected SelenideElement seeAllButton = $(iOSNsPredicateString("label == 'See All' AND type == 'XCUIElementTypeButton'")).as("See all button");

    /* --- Helper Methods --- */

    @Step
    @Override
    public boolean isContentProfilePageFavorited() {
        ReporterUtil.log("Checking if content profile page is favorited");
        return getElementName(contentProfileFavoriteButton).contains("_On");
    }

    @Override
    protected String getDateFormat() {
        return DATE_FORMAT;
    }

    /* --- Validation Methods --- */

    @Step("Validate that favourite button clicked state is {state}")
    @Override
    public ContentProfilePage validateFavoriteButton(ContentProfileButtons state) {
        String errorMessage = "Favorite button did not change to " + state;
        switch (state) {
            case FAVORITE -> assertThat(contentProfilePage.isContentProfilePageFavorited()).as(errorMessage).isTrue();
            case UNFAVORITE ->
                    assertThat(contentProfilePage.isContentProfilePageFavorited()).as(errorMessage).isFalse();
            default -> throw new Error("Unsupported favorite button state " + state);
        }
        return this;
    }

    @Step
    @Override
    public ContentProfilePage validateUIOfContentProfileCells(CategoryType category, int numberOfCellsToCheck) {
        switch (category) {
            case CATEGORY_TYPE_EPISODES, CATEGORY_TYPE_PREMIUM_EPISODES -> {
                for (int i = 1; i <= numberOfCellsToCheck; i++) {
                    getSoftAssertion()
                            .assertThat(getContentItemDataByIndex(category, i, EPISODE_NAME, false))
                            .as("Episode name does not exist for " + category)
                            .isNotEmpty();
                    getSoftAssertion()
                            .assertThat(getContentItemDataByIndex(category, i, EPISODE_RELEASE_DATE, false))
                            .as("Episode release date does not exist for " + category)
                            .isNotEmpty();
                    tapContentItemMoreLessButtonByIndex(category, ContentProfileLabels.MORE, i);
                    getSoftAssertion()
                            .assertThat(getContentItemDataByIndex(category, i, EPISODE_DESCRIPTION, false))
                            .as("Episode description does not exist for " + category)
                            .isNotEmpty();
                }
            }
            default -> {
                for (int i = 1; i <= numberOfCellsToCheck; i++) {
                    getSoftAssertion()
                            .assertThat(getContentItemDataByIndex(category, i, STATION_TITLE, false))
                            .as("Content title does not exist for " + category)
                            .isNotEmpty();
                    getSoftAssertion()
                            .assertThat(getContentItemDataByIndex(category, i, STATION_SUBTITLE, false))
                            .as("Content subtitle date does not exist for " + category)
                            .isNotEmpty();
                }
            }
        }
        getSoftAssertion().assertAll();
        return this;
    }

    /* --- Action Methods --- */

    @Step("Open content in Content Profile under {category} with index {index}")
    @Override
    public void openContentProfileCategoryTypeByIndex(CategoryType category, int index, boolean... skipWaitUntilReady) {
        openContentUnderCategoryWithHeader(category, LIST, SHORT, index, false);
        if (skipWaitUntilReady.length > 0 && skipWaitUntilReady[0]) {
            return;
        }
        waitForProperPage(category);
    }

    @Step("Open category {category}")
    @Override
    public ContentsListPage tapOnRequiredCategory(CategoryType category) {
        By requiredCellContent = AppiumBy.iOSNsPredicateString(String.format(CONTENT_PROFILE_CATEGORY_WITH_NAME_LOCATOR, category.getCategoryTypeValue()));
        clickOnElement(scrollTo(requiredCellContent, DOWN));
        return contentsListPage.waitUntilPageReady();
    }

    @Step
    @Override
    public ContentProfilePage tapContentItemMoreButtonByIndex(CategoryType category, int index) {
        String episodeMoreButtonByIndex = ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + "/" + TYPE_BUTTON + "[@name=\"" + ContentProfileLabels.MORE.getValue() + "\"]";
        String format = String.format(episodeMoreButtonByIndex, category.getCategoryTypeValue(), index);
        clickOnElement(scrollTo(By.xpath(format), DOWN, 5));
        return this;
    }

    @Step("Tap on {contentItemButton}")
    @Override
    public ContentProfilePage tapContentItemMoreLessButtonByIndex(CategoryType category, ContentProfileLabels contentItemButton, int index) {
        String episodeMoreButtonByIndex = ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + "/" + TYPE_BUTTON + "[@name=\"" + contentItemButton.getValue() + "\"]";
        String format = String.format(episodeMoreButtonByIndex, category.getCategoryTypeValue(), index);
        clickOnElement(scrollTo(By.xpath(format), DOWN, 5));
        return this;
    }

    @Step("Tap on content's three-dots button with index {index} under category {category}")
    @Override
    public EpisodeModalDialog tapEpisodeThreeDotsByIndex(CategoryType category, int index) {
        String episodeThreeDotsButtonByIndex = ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + "//XCUIElementTypeButton[@name='more options']";
        String format = String.format(episodeThreeDotsButtonByIndex, category.getCategoryTypeValue(), index);
        clickOnElement(scrollTo(By.xpath(format), DOWN, 5).as("Three-dots button"), Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
        return episodeModalDialog.waitUntilPageReady();
    }

    @Step
    @Override
    public ContentProfilePage tapOnMoreButtonForContentCell(SelenideElement cellContent) {
        clickOnElementIfDisplayed(cellContent.$(iOSNsPredicateString("name == \"" + ContentProfileLabels.MORE.getValue() + "\"")));
        return this;
    }

    /* --- Helper Methods --- */

    @Step("Get {details} for content in category {category}")
    @Override
    public String getContentItemDataByIndex(CategoryType category, int index, ContentDescriptionArea details, boolean skipTestIfCategoryNotFound, int... numberOfScrolls) {
        if (index == 0) {
            throw new Error("Index cannot be \"" + index + "\"");
        }

        String value;
        int scrolls = (numberOfScrolls.length > 0) ? numberOfScrolls[0] : config().scrollFewTimes();

        switch (category) {
            case CATEGORY_TYPE_EPISODES, CATEGORY_TYPE_PREMIUM_EPISODES -> {
                switch (details) {
                    case EPISODE_NAME -> {
                        value = EPISODE_CELL_TITLE_LOCATOR;
                    }
                    case EPISODE_RELEASE_DATE -> {
                        value = EPISODE_CELL_SUBTITLE_LOCATOR;
                    }
                    case EPISODE_DESCRIPTION -> {
                        value = EPISODE_CELL_DESCRIPTION_LOCATOR;
                    }
                    case EPISODE_DURATION -> {
                        value = EPISODE_CELL_DURATION_LOCATOR;
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
            default -> {
                // before split: "MSNBC. MSNBC Live with Alex Witt"
                String format = String.format(ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + "/" + TYPE_OTHER, category.getCategoryTypeValue(), index);
                String subtitle = getElementNameOrLabel(scrollTo(By.xpath(format), DOWN, scrolls));
                String[] dataArray = subtitle.split("\\. ", 0);

                switch (details) {
                    case STATION_TITLE -> {
                        return dataArray[0]; // after split: "MSNBC"
                    }
                    case STATION_SUBTITLE -> {
                        return dataArray[1]; // after split: "MSNBC Live with Alex Witt"
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
        }


        String episodeDetailsByIndex = ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + value;
        String format = String.format(episodeDetailsByIndex, category.getCategoryTypeValue(), index);
        try {
            return getElementNameOrLabel(scrollTo(By.xpath(format), DOWN, scrolls));
        } catch (Throwable e) {
            if (skipTestIfCategoryNotFound) {
                throw new SkipException("Skipping this test: " + e);
            } else {
                throw e;
            }
        }
    }

    private boolean isEpisodeCellExpanded(SelenideElement episodeCell) {
        try {
            return isElementDisplayed(episodeCell.$x("//" + TYPE_BUTTON + "[@name='" + ContentProfileLabels.LESS.getValue() + "']"));
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    @Override
    public String getContentItemData(SelenideElement contentCellItem, CategoryType category, ContentDescriptionArea details, int... numberOfScrolls) {
        String value;
        switch (category) {
            case CATEGORY_TYPE_EPISODES, CATEGORY_TYPE_PREMIUM_EPISODES -> {
                switch (details) {
                    case EPISODE_NAME -> {
                        value = EPISODE_CELL_TITLE_LOCATOR;
                    }
                    case EPISODE_RELEASE_DATE -> {
                        value = EPISODE_CELL_SUBTITLE_LOCATOR;
                    }
                    case EPISODE_DESCRIPTION -> {
                        value = EPISODE_CELL_DESCRIPTION_LOCATOR;
                    }
                    case EPISODE_DURATION -> {
                        value = EPISODE_CELL_DURATION_LOCATOR;
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
            default -> {
                String cellTitle = getElementName(scrollTo(contentCellItem.$x("/" + TYPE_OTHER + ""), DOWN, 3));
                String[] dataArray = cellTitle.split("\\. ", 0);

                switch (details) {
                    case STATION_TITLE -> {
                        return dataArray[0]; // after split: "MSNBC"
                    }
                    case STATION_SUBTITLE -> {
                        return dataArray[1]; // after split: "MSNBC Live with Alex Witt"
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
        }
        int scrolls = (numberOfScrolls.length > 0) ? numberOfScrolls[0] : config().scrollFewTimes();
        return getElementName(scrollTo(contentCellItem.$(By.id(value)), DOWN, scrolls));
    }

    @Step("Validate that live speaker icon is displayed={isIconExpected}, under category {category} for item with index {index}")
    @Override
    public ContentProfilePage validateLiveStreamSpeakerIconForContentUnder(CategoryType category, int index, boolean isIconExpected) {
        String message = "Speaker icon for \"" + category.getCategoryTypeValue() + "\" at index \"" + index + "\" is not expected";
        String stringLocator = switch (category) {
            case CATEGORY_TYPE_EPISODES ->
                    String.format(ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + EPISODE_SPEAKER_LOCATOR, category.getCategoryTypeValue(), index);
            case CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION, CATEGORY_TYPE_STATIONS ->
                    String.format(ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + STATION_SPEAKER_ICON_LOCATOR, category.getCategoryTypeValue(), index);
            default -> throw new Error("Elements under this category \"" + category + "\" do not have speaker icon");
        };
        if (isIconExpected) {
            SelenideElement requiredContent = scrollTo(By.xpath(stringLocator), DOWN, 5);
            assertThat(isElementDisplayed(requiredContent)).as(message).isEqualTo(isIconExpected);
        } else {
            assertThatThrownBy(() -> {
                scrollTo(By.xpath(stringLocator), DOWN, 5);
            })
                    .as(message).isInstanceOf(Error.class)
                    .hasMessageContaining("Cannot find element after scroll");
        }
        return this;
    }

    @Step
    @Override
    public ContentProfilePage validateVisibilityOfGreenCircleForNewEpisode(boolean isDisplayed) {
        By greenIconBy = By.xpath(String.format(ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR, CATEGORY_TYPE_EPISODES.getCategoryTypeValue(), 1) + EPISODE_SPEAKER_LOCATOR + "[@visible='true']");
        SelenideElement requiredContent = scrollTo(greenIconBy, DOWN, 2);
        assertThat(isElementDisplayed(requiredContent))
                .as("Progress icon is not displayed for " + CATEGORY_TYPE_EPISODES.getCategoryTypeValue() + " index " + 1)
                .isTrue();
        return this;
    }

    @Step
    public ContentProfilePage validateEpisodeLessMoreButtonIsDisplayed(CategoryType category, ContentProfileButtons contentItemButton, int index) {
        String button = (contentItemButton == ContentProfileButtons.MORE) ? "More" : "Less";
        String episodeLessMoreButtonByIndex = ITEM_WITH_INDEX_UNDER_CATEGORY_LOCATOR + "/XCUIElementTypeButton[@name=\"" + button + "\"]";
        String format = String.format(episodeLessMoreButtonByIndex, category.getCategoryTypeValue(), index);
        SelenideElement lessMoreButton = scrollTo(By.xpath(format), DOWN, 5);
        assertThat(isElementDisplayed(lessMoreButton)).as("\"" + button + "\" Button is not displayed").isTrue();
        return this;
    }

    @Step
    public ContentProfilePage validateEpisodeCellAdditionalInfoIsPresent(CategoryType category, int index) {
        getSoftAssertion()
                .assertThat(getContentItemDataByIndex(category, index, EPISODE_DESCRIPTION, false))
                .as("Episode description does not exist for " + category)
                .isNotEmpty();
        getSoftAssertion()
                .assertThat(getContentItemDataByIndex(category, index, EPISODE_DURATION, false))
                .as("Episode description does not exist for " + category)
                .isNotEmpty();
        getSoftAssertion().assertAll();
        return this;
    }

    public enum ElementLocatorTypes {
        TYPE_TEXT("XCUIElementTypeStaticText"),
        TYPE_IMAGE("XCUIElementTypeImage"),
        TYPE_BUTTON("XCUIElementTypeButton"),
        TYPE_CELL("XCUIElementTypeCell"),
        TYPE_OTHER("XCUIElementTypeOther");

        private final String value;

        private ElementLocatorTypes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
