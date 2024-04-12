package com.tunein.mobile.pages.android.contentprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.dialog.common.EpisodeModalDialog;
import com.tunein.mobile.utils.ReporterUtil;
import org.openqa.selenium.By;

import org.testng.SkipException;

import java.time.Duration;
import java.util.HashMap;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.waitElementDisappear;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidContentProfilePage extends ContentProfilePage {

    private final static String DATE_FORMAT = "M/d/yyyy";

    private final static String ID = "//*[@resource-id='tunein.player:id/";

    private final static String EPISODE_CELL_TITLE_LOCATOR_BY_ID = "status_cell_title_id";

    private final static String EPISODE_CELL_DESCRIPTION_LOCATOR_BY_ID = "status_cell_description_id";

    private final static String EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID = "status_cell_subtitle_id";

    private final static String NON_EPISODE_CELL_TITLE_LOCATOR_BY_ID = "row_square_cell_title";

    private final static String NON_EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID = "row_square_cell_subtitle";

    private final static String STATUS_CELL_OPTIONS_IMAGE_ID = "status_cell_options_image_id";

    private final static String STATUS_CELL_OPTIONS_IMAGE_LOCATOR = ID + STATUS_CELL_OPTIONS_IMAGE_ID + "']";

    private final static String EPISODE_CELL_TITLE_LOCATOR = ID + EPISODE_CELL_TITLE_LOCATOR_BY_ID + "']";

    private final static String EPISODE_CELL_DESCRIPTION_LOCATOR = ID + EPISODE_CELL_DESCRIPTION_LOCATOR_BY_ID + "']";

    private final static String EPISODE_CELL_SUBTITLE_LOCATOR = ID + EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID + "']";

    private final static String NON_EPISODE_CELL_TITLE_LOCATOR = ID + NON_EPISODE_CELL_TITLE_LOCATOR_BY_ID + "']";

    private final static String NON_EPISODE_CELL_SUBTITLE_LOCATOR = ID + NON_EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID + "']";

    private final static String CONTENT_PROFILE_CATEGORY_ITEM_BY_INDEX = "//*[.//*[contains(@text, \"%s\")]]/following-sibling::android.view.ViewGroup[%s]";

    private final static String NEW_EPISODE_INDICATOR_IMAGE_LOCATOR = "//android.widget.ImageView[@content-desc='Status']";

    private final static String CONTENT_PROFILE_CATEGORY_LOCATOR = "//*[contains(@text, \"%s\")]";

    /* --- Helper Methods --- */

    @Step
    @Override
    public boolean isContentProfilePageFavorited() {
        ReporterUtil.log("Checking if content profile page is favorited");
        return getElementContent(contentProfileFavoriteButton).equals(ContentProfileLabels.FAVORITED_LABEL.getValue());
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
            case UNFAVORITE -> assertThat(contentProfilePage.isContentProfilePageFavorited()).as(errorMessage).isFalse();
            default -> throw new Error("Unsupported favorite button state " + state);
        }
        return this;
    }

    @Step
    @Override
    public BasePage validateTextOfUIElements(HashMap<String, SelenideElement> mapOfElements) {
        mapOfElements.keySet().stream()
                .filter(keyBeforeFilter -> !keyBeforeFilter.startsWith(SKIP_TEXT_VALIDATION_PREFIX))
                .forEach(key -> {
                    String message = key + " text is not displayed";
                    SelenideElement element = mapOfElements.get(key);
                    getSoftAssertion().assertThat(getElementText(element)).as(message).isEqualToIgnoringCase(key);
                });
        getSoftAssertion().assertAll();
        return this;
    }

    @Override
    public ContentProfilePage validateLiveStreamSpeakerIconForContentUnder(CategoryType category, int index, boolean isIconExpected) {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
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
        SelenideElement requiredContent = getContentUnderCategoryWithHeader(category, LIST, SHORT, index).as("Content item under category " + category.getCategoryTypeValue());
        takeScreenshot();
        clickOnElementIfDisplayed(requiredContent);
        takeScreenshot();
        waitElementDisappear(requiredContent, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
        if (skipWaitUntilReady.length > 0 && skipWaitUntilReady[0]) {
            return;
        }
        waitForProperPage(category);
    }

    @Step("Open category {category}")
    @Override
    public ContentsListPage tapOnRequiredCategory(CategoryType category) {
        clickOnElement(scrollTo(By.xpath(String.format(CONTENT_PROFILE_CATEGORY_LOCATOR, category.getCategoryTypeValue())), DOWN));
        return contentsListPage.waitUntilPageReady();
    }

    @Step
    @Override
    public ContentProfilePage tapContentItemMoreButtonByIndex(CategoryType category, int index) {
        String episodeMoreButtonByIndex = CONTENT_PROFILE_CATEGORY_ITEM_BY_INDEX + STATUS_CELL_OPTIONS_IMAGE_LOCATOR;
        String format = String.format(episodeMoreButtonByIndex, category.getCategoryTypeValue(), index);
        SelenideElement moreButton = scrollTo(By.xpath(format), DOWN, 5);
        clickOnElement(moreButton);
        return episodeModalDialog.waitUntilPageReady();
    }

    public ContentProfilePage tapContentItemMoreLessButtonByIndex(CategoryType category, ContentProfileLabels contentItemButton, int index) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

    @Step("Tap on content's three-dots button with index {index} under category {category}")
    @Override
    public EpisodeModalDialog tapEpisodeThreeDotsByIndex(CategoryType category, int index) {
        String episodeThreeDotsButtonByIndex = CONTENT_PROFILE_CATEGORY_ITEM_BY_INDEX + "//android.widget.ImageButton[@resource-id='tunein.player:id/status_cell_options_image_id']";
        String format = String.format(episodeThreeDotsButtonByIndex, category.getCategoryTypeValue(), index);
        clickOnElement(scrollTo(By.xpath(format), DOWN, 5).as("Three-dots button"));
        return episodeModalDialog.waitUntilPageReady();
    }

    @Override
    public ContentProfilePage tapOnMoreButtonForContentCell(SelenideElement cellContent) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

    /* --- Helper Methods --- */

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
                        throw new Error("\"EPISODE_DURATION\" is not supported on Android");
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
            default -> {
                switch (details) {
                    case STATION_SUBTITLE -> {
                        value = NON_EPISODE_CELL_SUBTITLE_LOCATOR;
                    }
                    case STATION_TITLE -> {
                        value = NON_EPISODE_CELL_TITLE_LOCATOR;
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
        }

        String episodeDetailsByIndex = CONTENT_PROFILE_CATEGORY_ITEM_BY_INDEX + value;
        String format = String.format(episodeDetailsByIndex, category.getCategoryTypeValue(), index);
        try {
            return getElementNameOrLabel(scrollTo(By.xpath(format), DOWN, scrolls));
        } catch (Error | Exception e) {
            if (skipTestIfCategoryNotFound) {
                throw new SkipException("Skipping this test: " + e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public String getContentItemData(
            SelenideElement contentCellItem,
            CategoryType category,
            ContentDescriptionArea details,
            int... numberOfScrolls) {
        String value;
        switch (category) {
            case CATEGORY_TYPE_EPISODES, CATEGORY_TYPE_PREMIUM_EPISODES -> {
                switch (details) {
                    case EPISODE_NAME -> {
                        value = EPISODE_CELL_TITLE_LOCATOR_BY_ID;
                    }
                    case EPISODE_RELEASE_DATE -> {
                        value = EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID;
                    }
                    case EPISODE_DESCRIPTION -> {
                        value = EPISODE_CELL_DESCRIPTION_LOCATOR_BY_ID;
                    }
                    case EPISODE_DURATION -> {
                        throw new Error("\"EPISODE_DURATION\" is not supported on Android");
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
            default -> {
                switch (details) {
                    case STATION_SUBTITLE -> {
                        value = NON_EPISODE_CELL_TITLE_LOCATOR_BY_ID;
                    }
                    case STATION_TITLE -> {
                        value = NON_EPISODE_CELL_SUBTITLE_LOCATOR_BY_ID;
                    }
                    default -> throw new Error("Unsupported details type \"" + details + "\"");
                }
            }
        }
        int scrolls = (numberOfScrolls.length > 0) ? numberOfScrolls[0] : config().scrollFewTimes();
        return getElementName(scrollTo(contentCellItem.$(By.id(value)), DOWN, scrolls));
    }

    @Step("Validating if New Episode image indicator is displayed")
    @Override
    public ContentProfilePage validateVisibilityOfGreenCircleForNewEpisode(boolean isDisplayed) {
        String locator = String.format(CONTENT_PROFILE_CATEGORY_ITEM_BY_INDEX, CATEGORY_TYPE_EPISODES.getCategoryTypeValue(), 1);
        By greenIconBy = By.xpath(locator + NEW_EPISODE_INDICATOR_IMAGE_LOCATOR);
        if (!isDisplayed) {
            assertThat(isElementNotDisplayed(greenIconBy, Duration.ofMillis(config().waitCustomTimeoutMilliseconds()))).as("Progress icon is displayed").isTrue();
        } else {
            SelenideElement requiredContent = scrollTo(greenIconBy, DOWN, 2);
            assertThat(isElementDisplayed(requiredContent)).as("Progress icon is not displayed").isTrue();
        }
        return this;
    }

    @Override
    public ContentProfilePage validateEpisodeCellAdditionalInfoIsPresent(CategoryType categoryTypeEpisodes, int index) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

    @Override
    public ContentProfilePage validateEpisodeLessMoreButtonIsDisplayed(CategoryType category, ContentProfileButtons contentItemButton, int index) {
        throw new UnsupportedOperationException("Functionality not supported in Android platform");
    }

}
