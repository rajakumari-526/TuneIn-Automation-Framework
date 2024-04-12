package com.tunein.mobile.pages.common.contentprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.pages.dialog.common.EpisodeModalDialog;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.SkipException;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_EPISODES;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentDescriptionArea.EPISODE_RELEASE_DATE;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileLabels.*;
import static com.tunein.mobile.pages.common.contentprofile.ContentProfilePage.ContentProfileType.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.LONG;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class ContentProfilePage extends BasePage {

    /* --- UI Elements --- */

    /**
     * Content Profile Category `Podcast` section
     */

    protected SelenideElement contentProfileArtwork = $(android(id("tunein.player:id/profile_logo_id"))
            .ios(iOSNsPredicateString("name IN {'profileArtwork_loaded', 'profileArtwork_missing'}"))).as("Content Profile logo");
    /**
     * Name of the content profile page (e.g. CNN)
     */

    protected SelenideElement contentProfileTitle = $(android(id("tunein.player:id/profile_title"))
            .ios(iOSNsPredicateString("name IN {'ProfileHeader', 'profileHeaderId'}"))).as("Content Profile title");

    /**
     * Shows up the in station profile
     */

    protected SelenideElement contentProfileSubtitle = $(android(id("tunein.player:id/profile_subtitle"))
            .ios(iOSNsPredicateString("name IN {'ProfileSubtitle', 'profileSubtitleId'}"))).as("Content Profile subtitle");

    /**
     * Displays in the station profile
     */
    protected SelenideElement contentProfileStationSubtitle = $(android(xpath("//*[@resource-id='tunein.player:id/profile_subtitle'][translate(@text, '0123456789.K', '') = ''] | //*[@text='TuneIn Exclusive' and @resource-id='tunein.player:id/profile_subtitle']"))
            .ios(iOSNsPredicateString("name == 'ProfileSubtitle'"))).as("Station Profile subtitle");

    /**
     * Displays in podcast profile
     */

    protected SelenideElement contentProfileAffiliateLink = $(android(xpath("//*[(contains(@resource-id,'profile_subtitle') and contains(@text,'>'))]"))
            .ios(iOSNsPredicateString("name == 'ProfileSubtitle'"))).as("Affiliate link");

    /* --- UI Elements (Profile Navigation Buttons) --- */

    /**
     * Profile `Favorite` button
     */

    protected SelenideElement contentProfileFavoriteButton = $(android(androidUIAutomator("text(\"FAVORITE\")"))
            .ios(iOSNsPredicateString("name IN {'profileLeftButton_Off', 'profileLeftButton_On'}"))).as("Favorite button");

    /**
     * Profile disabled `Favorite` button
     */

    protected SelenideElement contentProfileFavoriteDisabledButton = $(android(xpath("//*[(contains(@resource-id,'profile_secondary_button')) and (@enabled='false')]"))
            .ios(iOSNsPredicateString("name == 'profileLeftButton_Off' AND visible == false"))).as("Disabled favorite button");

    /**
     * Profile `Play` button with all states (disabled, enabled))
     */

    protected SelenideElement contentProfileGenericPlayButton = $(android(id("tunein.player:id/profile_primary_button"))
            .ios(iOSNsPredicateString("name == 'profilePlayButton' AND label IN {'Play', 'Subscribe', 'playDarkPBS'}"))).as("Profile generic play button");

    /**
     * Profile `Play` button
     */

    protected SelenideElement contentProfilePlayButton = $(android(id("tunein.player:id/profile_primary_button"))
            .ios(iOSNsPredicateString("name == 'profilePlayButton' AND label IN {'Play', 'Subscribe'}"))).as("Profile play button");

    // todo: Add id for android disabled play button
    /**
     * Profile disabled `Play` button
     */

    protected SelenideElement contentProfilePlayDisabledButton = $(android(id("tunein.player:id/profile_primary_button"))
            .ios(iOSNsPredicateString("name == 'profilePlayButton' AND label == 'playDarkPBS'"))).as("Profile disabled play button");

    /**
     * Profile `More` button
     */

    protected SelenideElement contentProfileMoreButton = $(android(androidUIAutomator("text(\"MORE\")"))
            .ios(id("profileRightButton_More"))).as("Profile more button");

    /**
     * Profile `More` disabled button
     */

    protected SelenideElement contentProfileMoreDisabledButton = $(android(xpath("//*[(contains(@resource-id,'profile_tertiary_button')) and (@enabled='false')]"))
            .ios(iOSNsPredicateString("name == 'profileRightButton_More' AND visible == false"))).as("Profile disabled more button");

    /**
     * Profile `Less` button
     */

    protected SelenideElement contentProfileLessButton = $(android(androidUIAutomator("text(\"LESS\")"))
            .ios(id("profileRightButton_Less"))).as("Profile less button");

    public SelenideElement downloadSpinnerImage = $(iOSNsPredicateString("label == \"In progress\"")).as("Download spinner image");

    /* --- UI Elements (Profile Description) --- */

    // TODO update `contentProfileDescriptionText` when `https://tunein.atlassian.net/browse/IOS-15732` is done

    /**
     * Content profile description text
     */

    protected SelenideElement contentProfileDescriptionText = $(android(id("tunein.player:id/expandable_text"))
            .ios(iOSNsPredicateString("name CONTAINS 'contentProfileDescription'"))).as("Description text");

    /* --- UI Elements (Compact Prompt) --- */

    protected SelenideElement contentProfilePremiumPromptTitle = $(android(androidUIAutomator("text(\"Go Premium!\")"))
            .ios(iOSNsPredicateString("name == 'compactPromptTitleId'"))).as("Premium prompt title");

    protected SelenideElement contentProfilePremiumPromptSubtitle = $(android(id("tunein.player:id/subtitle"))
            .ios(iOSNsPredicateString("name == 'compactPromptSubtitleId'"))).as("Premium prompt subtitle");

    /**
     * Content profile super prompt button
     */

    protected SelenideElement contentProfilePremiumPromptButton = $(android(id("tunein.player:id/primary_button"))
            .ios(iOSNsPredicateString("name == 'not defined'"))).as("Premium prompt button");

    /* --- UI Elements (Compact Prompt - Restrictions) --- */

    /**
     * "Regional Restrictions ... "
     */

    protected SelenideElement contentProfileRestrictedPromptTitleText = $(android(androidUIAutomator("text(\"Regional Restrictions\")"))
            .ios(iOSNsPredicateString("name == 'compactPromptTitleId' AND label CONTAINS \"Regional Restrictions\""))).as("Restricted prompt title");

    /**
     * "This station is not available in your region."
     */

    protected SelenideElement contentProfileRestrictedPromptSubtitleText = $(android(androidUIAutomator("text(\"This station is not available in your region.\")"))
            .ios(iOSNsPredicateString("name == 'compactPromptSubtitleId' AND label == \"This station is not available in your region.\""))).as("Restricted prompt subtitle");

    /**
     * "Albums"
     */

    protected SelenideElement contentProfileAlbumsText = $(android(androidUIAutomator("text(\"Albums\")"))
            .ios(iOSNsPredicateString("label == \"Albums\""))).as("Albums title");

    // todo: Check if wrong `compactPromptDescription` Id is used on iOS
    protected SelenideElement contentProfileGetBestCoverageButton = $(android(id("tunein.player:id/profile_primary_button"))
            .ios(iOSNsPredicateString("name == 'compactPromptDecriptionId'"))).as("Get best coverage button");

    protected SelenideElement contentProfileGenresLabel = $(android(xpath("//*[(@resource-id='tunein.player:id/profile_subtitle') and (not (contains(@text,'>')))][translate(@text, '0123456789.K', '') != '']"))
            .ios(xpath("//*[(@name='ProfileSubtitle') and (not (contains(@label,'>'))) and (not( preceding-sibling::*[@name='ProfileHeartIconId'] ))]"))).as("Genres label");

    /* --- UI Elements (Compact Prompt - Premium Free Trial) --- */

    protected SelenideElement contentProfileFreeTrialButton = $(android(id("tunein.player:id/prompt_button1_id"))
            .ios(iOSNsPredicateString("label == \"Start Your Free Trial\" AND type == \"XCUIElementTypeButton\""))).as("Free trial button");

    /* --- UI Elements --- (Live Events) */

    protected SelenideElement contentProfileLiveEventLeftTeamArtwork = $(android(id("profile_left_logo_id"))
            .ios(iOSNsPredicateString("name == \"profileTeamArtworkLeft\""))).as("Left team logo");

    protected SelenideElement contentProfileLiveEventRightTeamArtwork = $(android(id("profile_right_logo_id"))
            .ios(iOSNsPredicateString("name == \"profileTeamArtworkRight\""))).as("Right team logo");

    protected SelenideElement contentProfileSeparator = $(android(id("profile_logo_separator_text"))
            .ios(iOSNsPredicateString("name == \"profileSeparator\""))).as("Logo separator");

    protected SelenideElement contentProfileLiveSportEventLabel = $(android(xpath("//*[@text='Live' and @resource-id='tunein.player:id/profile_subtitle']"))
            .ios(iOSNsPredicateString("name == 'profileSubtitleId' AND label == 'Live'"))).as("Live sport event label");

    /**
     * Example text `This show will be available on Fri, Oct 14 at 1:30AM.`
     */

    protected SelenideElement contentProfileScheduledSportEventPromptTitle = $(android(id("prompt_text_id"))
            .ios(id("contentProfilePromptTitle"))).as("Scheduled Sport event prompt title");

    /**
     * `Notify Me` super prompt button
     */

    protected SelenideElement contentProfileNotifyMeButton = $(android(id("prompt_button1_id"))
            .ios(id("contentProfilePromptTopButton"))).as("Notify me button");

    /* --- Other UI Elements --- */

    /**
     * Premium Podcast - Progressive Voices (p864998)
     */
    protected SelenideElement contentProfilePremiumEpisodesLabel = $(android(androidUIAutomator("text(\"Premium Episodes\")"))
            .ios(iOSNsPredicateString("label == 'Premium Episodes' AND type == 'XCUIElementTypeStaticText' AND visible == true"))).as("Premium episode label");

    /**
     * Commercial-Free News
     */

    protected SelenideElement contentProfileCommercialFreeNewsLabel = $(android(androidUIAutomator("text(\"Commercial-Free News\")"))
            .ios(iOSNsPredicateString("label == 'Commercial-Free News' AND visible == true"))).as("Commercial free news label");

    /**
     * Commercial-Free Music
     */

    protected SelenideElement contentProfileCommercialFreeLabel = $(android(androidUIAutomator("text(\"Commercial-Free\")"))
            .ios(iOSNsPredicateString("label == 'Commercial-Free' AND visible == true"))).as("Commercial free label");

    /**
     * Restricted
     */

    protected SelenideElement contentProfileRestrictedLabel = $(android(androidUIAutomator("text(\"This station is not currently available.\")"))
            .ios(iOSNsPredicateString("label == \"This station is not currently available.\""))).as("Restricted label");

    /**
     * Stations tha play (Artist name)
     */

    protected SelenideElement contentProfileArtistAndAlbumLabel = $(android(androidUIAutomator("textContains(\"Stations that play\")"))
            .ios(iOSNsPredicateString("label CONTAINS \"Stations that play\""))).as("Artist and Album label");

    /**
     * Listen on a live station (Used in live show)
     */

    protected SelenideElement contentProfileLiveShowLabel = $(android(androidUIAutomator("text(\"Listen on a live station\")"))
            .ios(iOSNsPredicateString("label == \"Listen on a live station\""))).as("Live show label");

    /**
     * `This show will be available ...` text
     * or `This program will be available ...` text
     * or `We didn't find any playable streams.` text
     * (Used in scheduled program/show)
     */

    protected SelenideElement contentProfileScheduledShowLabel = $(android(xpath("(//*[contains(@text, 'This program will be available') or contains(@text, 'This show will be available') or contains(@text, \"We didn't find any playable streams\")])"))
            .ios(iOSNsPredicateString("label CONTAINS \"This program will be available\" OR label CONTAINS \"This show will be available\" OR label == \"We didn't find any playable streams.\""))).as("Scheduled show label");

    /**
     * Donate button (Donate Today)
     */ // tunein.player:id/prompt_button1_id

    protected SelenideElement donateButton = $(android(xpath("//*[contains(@text,'DONATE TODAY')] | //*[@resource-id='tunein.player:id/prompt_button1_id']"))
            .ios(iOSNsPredicateString("label == \"Donate Today\" AND name == \"contentProfilePromptTopButton\""))).as("Donate button");

    protected SelenideElement donatePage = $(android(xpath("//*[contains(@text,'kqed')]"))
            .ios(iOSNsPredicateString("name='TabBarItemTitle' AND value CONTAINS 'kqed'"))).as("Donate page");


    /* --- Loadable Component Method --- */

    @Step("Wait until Content Profile page is ready")
    @Override
    public ContentProfilePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(contentProfileGenericPlayButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public ContentProfilePage validateContentProfilePageIsOpened() {
        assertThat(isOnContentProfilePage()).as("Content `Profile page is not opened").isTrue();
        return this;
    }

    public abstract ContentProfilePage validateEpisodeLessMoreButtonIsDisplayed(CategoryType category, ContentProfileButtons contentItemButton, int index);

    public abstract ContentProfilePage validateEpisodeCellAdditionalInfoIsPresent(CategoryType category, int index);

    @Step
    public ContentProfilePage validateContentProfileTitleIsEqualTo(String expectedTitle) {
        String message = "Content Profile title \"" + getContentProfileTitleText() + "\" doesn't match expected title \"" + expectedTitle + "\"";
        assertThat(getContentProfileTitleText()).as(message).isEqualTo(expectedTitle);
        return this;
    }

    @Step
    public void validateThatDonatePageIsOpened() {
        assertThat(isElementDisplayed(donatePage)).as("KQED donate page failed to load").isTrue();
    }

    @Step
    public abstract ContentProfilePage validateFavoriteButton(ContentProfileButtons state);

    @Step
    public abstract ContentProfilePage validateUIOfContentProfileCells(CategoryType category, int numberOfCellsToCheck);

    /**
     * Validates that speaker icon is displays for the currently playing content item in content profile page
     *
     * @param category       Category title when cell is presented
     * @param index          item position
     * @param isIconExpected is speaker icon expected
     * @return ContentProfilePage
     */
    public abstract ContentProfilePage validateLiveStreamSpeakerIconForContentUnder(CategoryType category, int index, boolean isIconExpected);

    @Step
    public ContentProfilePage validateMoreButtonIsDisplayed() {
        assertThat(isElementDisplayed(contentProfileMoreButton)).as("More Button is not displayed").isTrue();
        return this;
    }

    @Step
    public ContentProfilePage validateLessButtonIsDisplayed() {
        assertThat(isElementDisplayed(contentProfileLessButton)).as("Less Button is not displayed").isTrue();
        return this;
    }

    @Step
    public abstract ContentProfilePage validateVisibilityOfGreenCircleForNewEpisode(boolean isDisplayed);

    @Step
    public void validateEpisodePublishedWithInPastFiveDays() {
        String episodeDate = getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_RELEASE_DATE, false).replace(",", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getDateFormat(), Locale.ENGLISH);
        LocalDate episodeReleaseDate = LocalDate.parse(episodeDate, formatter);
        SimpleDateFormat format = new SimpleDateFormat(getDateFormat(), Locale.getDefault());
        String currentDate = format.format(new Date());
        LocalDate today = LocalDate.parse(currentDate, formatter);
        int noOfDays = Period.between(episodeReleaseDate, today).getDays();
        assertThat(noOfDays)
                .as("The most recent episode is not published with in the past five days")
                .isLessThanOrEqualTo(5);
    }

    @Step("Validate Description is present in expanded more menu")
    public ContentProfilePage validateDescriptionAfterExpandMoreMenu() {
        String shortDescription = getElementText(contentProfileDescriptionText);
        expandMoreSection();
        contentProfileDescriptionText.scrollTo();
        String longDescription = getElementText(contentProfileDescriptionText);
        assertThat(shortDescription).as("Short Description and long description under more dropdown are not same").isEqualTo(longDescription);
        return this;
    }

    @Step
    public ContentProfilePage validateFreeTrialButtonNotDisplayed() {
        assertThat(isElementNotDisplayed(contentProfileFreeTrialButton)).as("Free trial button is still displayed").isTrue();
        return this;
    }

    @Step("Skip test if non playable content")
    public ContentProfilePage skipTestIfNonPlayableContent() {
        if (isOnShowProfilePageWithNonPlayableContent(Duration.ofSeconds(config().elementVisibleTimeoutSeconds()))) {
            throw new SkipException("Content is not currently available");
        }
        return this;
    }

    /* --- Action Methods --- */

    @Step("Tap play button in profile")
    public void tapProfilePlayButton(Contents... content) {
        clickOnElement(contentProfilePlayButton);
        handlePageAfterPlayButton(content);
    }

    @Step("Tap favorite button in profile")
    public ContentProfilePage tapOnFavoriteButton() {
        ReporterUtil.log("Tap on \"Favorite\" button");
        clickOnElement(contentProfileFavoriteButton);
        return this;
    }

    @Step
    public void tapProfilePlayButtonLightVersion() {
        clickOnNonClickableElement(contentProfileGenericPlayButton);
        closePermissionPopupsIfDisplayed();
    }

    @Step
    public ContentProfilePage expandMoreSection() {
        clickOnElement(contentProfileMoreButton);
        scroll(DOWN, config().scrollOneTime(), SHORT);
        moreDropDownPage.waitUntilPageReady();
        return this;
    }

    @Step
    public ContentProfilePage collapseLessSection() {
        clickOnElement(contentProfileLessButton);
        return this;
    }

    public abstract void openContentProfileCategoryTypeByIndex(CategoryType category, int index, boolean... skipWaitUntilReady);

    @Step
    public UpsellPage tapPremiumFreeTrialButton() {
        clickOnElement(contentProfileFreeTrialButton);
        return upsellPage.waitUntilPageReady();
    }

    public abstract ContentsListPage tapOnRequiredCategory(CategoryType category);

    public abstract ContentProfilePage tapContentItemMoreButtonByIndex(CategoryType category, int index);

    /**
     * This method scrolls to `Donate` button and then taps on it
     */
    @Step
    public void tapOnDonateButton() {
        ReporterUtil.log("Scroll and tap on \"Donate Today\" button");
        clickOnElement(scrollTo(donateButton, DOWN, LONG, config().scrollReallyLongTime()));
    }

    @Step("Tap on Affiliates button")
    public AffiliatesPage tapOnAffiliatesButton() {
        ReporterUtil.log("Tap on \"Affiliates\" button");
        clickOnElement(contentProfileAffiliateLink);
        return affiliatesPage.waitUntilPageReady();
    }

    @Step
    public void tapOnProfilePlayButtonIfDisplayed() {
        clickOnElementIfDisplayed(contentProfilePlayDisabledButton);
    }

    public abstract ContentProfilePage tapContentItemMoreLessButtonByIndex(CategoryType category, ContentProfileLabels contentItemButton, int index);

    public abstract EpisodeModalDialog tapEpisodeThreeDotsByIndex(CategoryType category, int index);

    public abstract ContentProfilePage tapOnMoreButtonForContentCell(SelenideElement cellContent);

    /* --- Helper Methods --- */

    public boolean isOnContentProfilePage(Duration... timeout) {
        closePermissionPopupsIfDisplayed();
        return (timeout.length > 0) ? isElementDisplayed(contentProfileGenericPlayButton, timeout[0]) : isElementDisplayed(contentProfileGenericPlayButton, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));

    }

    public boolean isOnDonatePageKQED() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(donatePage, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
    }

    public String getContentProfileTitleText() {
        return getElementText(contentProfileTitle);
    }

    public String getContentProfileSubtitleText() {
        return getElementText(contentProfileAffiliateLink);
    }

    public ContentProfileType getContentProfilePageType() {
        Duration elementTimeout = Duration.ofSeconds(config().waitVeryShortTimeoutSeconds());

        if (isOnAlbumProfilePage(elementTimeout)) {
            return ALBUM_TYPE;
        } else if (isOnArtistProfilePage(elementTimeout)) {
            return ARTIST_TYPE;
        } else if (isOnStationProfilePage(elementTimeout)) {
            // TODO detect non-premium news, for now NEWS_TYPE is the same as station STATION_TYPE

            if (isOnPremiumNewsProfilePage(elementTimeout)) {
                return NEWS_TYPE_PREMIUM;
            } else if (isOnPremiumMusicProfilePage(elementTimeout)) {
                return STATION_TYPE_PREMIUM;
            } else if (isOnRestrictedStationProfilePage(elementTimeout)) {
                return STATION_TYPE_RESTRICTED;
            } else if (isOnShowProfilePage(elementTimeout)) {
                return STATION_TYPE_SHOW;
            }
            return STATION_TYPE;
        } else if (isOnPodcastProfilePage(elementTimeout)) {
            if (isOnPremiumPodcastProfilePage(elementTimeout)) {
                return PODCAST_TYPE_PREMIUM;
            }
            return PODCAST_TYPE;
        } else if (isOnLiveSportEventProfilePage(elementTimeout)) {
            return LIVE_SPORT_EVENT_TYPE;
        }
        throw new Error("Failed to get \"Content Profile Type\"");
    }

    public abstract boolean isContentProfilePageFavorited();

    protected abstract String getDateFormat();

    private boolean isOnStationProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"STATION_TYPE\"");
        return isElementDisplayed(contentProfileGenresLabel, timeout);
    }

    /**
     * Checks for the `Commercial-Free News` text
     */
    private boolean isOnPremiumNewsProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"NEWS_TYPE_PREMIUM\"");
        return isElementDisplayed(contentProfileCommercialFreeNewsLabel, timeout);
    }

    /**
     * Checks for the `Commercial-Free Music` text
     */
    private boolean isOnPremiumMusicProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"STATION_TYPE_PREMIUM\"");
        return isElementDisplayed(contentProfileCommercialFreeLabel, timeout);
    }

    /**
     * Checks for the affiliate url
     */
    private boolean isOnPodcastProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"PODCAST_TYPE\"");
        return isElementDisplayed(contentProfileAffiliateLink, timeout);
    }

    /**
     * Checks for the `Premium Episodes` text
     */
    private boolean isOnPremiumPodcastProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"PODCAST_TYPE_PREMIUM\"");
        return isElementDisplayed(contentProfilePremiumEpisodesLabel, timeout);
    }

    /**
     * Checks for the `Stations that play ARTIST_NAME` text and
     */
    private boolean isOnArtistProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"ARTIST_TYPE\"");
        return !isElementEnabled(contentProfileMoreDisabledButton, timeout) && isElementDisplayed(contentProfileArtistAndAlbumLabel, timeout);
    }

    /**
     * Checks for the `Stations that play ARTIST_NAME` text as well as `More` and `Favorites` buttons that are disabled
     */
    private boolean isOnAlbumProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"ALBUM_TYPE\"");
        return !isElementEnabled(contentProfileMoreDisabledButton, timeout)
                && !isElementEnabled(contentProfileFavoriteDisabledButton, timeout)
                && isElementDisplayed(contentProfileArtistAndAlbumLabel, timeout);
    }

    /**
     * Checks for the `We didn't find any playable streams.` text
     */
    private boolean isOnRestrictedStationProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"STATION_TYPE_RESTRICTED\"");
        return isElementDisplayed(contentProfileRestrictedPromptSubtitleText, timeout);
    }

    /**
     * Checks if on program/show page
     */
    private boolean isOnShowProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"STREAM_SHOW\"");
        return isElementDisplayed(contentProfileScheduledShowLabel, timeout) || isElementDisplayed(contentProfileLiveShowLabel, timeout);
    }

    /**
     * Checks if program or show page has playable content
     */
    private boolean isOnShowProfilePageWithPlayableContent(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"STREAM_SHOW\" with playable content");
        return isElementDisplayed(contentProfileLiveShowLabel, timeout) && isElementDisplayed(contentProfilePlayButton, timeout);
    }

    /**
     * Checks if program or show page has scheduled prompt and disabled content
     */
    private boolean isOnShowProfilePageWithNonPlayableContent(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"SHOW_TYPE_SCHEDULED\" with playable content");
        return isElementDisplayed(contentProfileScheduledShowLabel, timeout) && isElementDisplayed(contentProfilePlayDisabledButton, timeout);
    }

    private boolean isOnLiveSportEventProfilePage(Duration timeout) {
        ReporterUtil.log("Checking if ContentProfileType is \"SPORT_TEAM_TYPE\"");
        return isElementDisplayed(contentProfileLiveEventLeftTeamArtwork, timeout);
    }

    public enum ContentProfileType {
        STATION_TYPE("station"),
        STATION_TYPE_PREMIUM("premiumStation"),
        PODCAST_TYPE("podcast"),
        PODCAST_TYPE_PREMIUM("premiumPodcast"),
        NEWS_TYPE("news"),
        NEWS_TYPE_PREMIUM("premiumNews"),
        STATION_TYPE_SHOW("show"),
        SHOW_TYPE_SCHEDULED("showScheduled"),
        EVENT_TYPE("event"),
        LIVE_SPORT_EVENT_TYPE("sport"),
        SCHEDULED_SPORT_EVENT_TYPE("sportScheduled"),
        ARTIST_TYPE("artist"),
        ALBUM_TYPE("album"),
        STATION_TYPE_RESTRICTED("restricted"),
        AUDIOBOOK_TYPE("audiobook");
        private String contentProfileType;

        private ContentProfileType(String contentProfileType) {
            this.contentProfileType = contentProfileType;
        }

        public String getContentProfileType() {
            return contentProfileType;
        }

        public static ContentProfileType getContentProfileType(final String contentProfileTypeValue) {
            List<ContentProfileType> contentProfileTypesList = Arrays.asList(ContentProfileType.values());
            return contentProfileTypesList.stream().filter(eachContent -> eachContent.toString().equals(contentProfileTypeValue))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return contentProfileType;
        }
    }

    /**
     * Returns proper Favorite button element (enabled/disabled) based on the ContentProfileType
     */
    protected SelenideElement getCorrectProfileFavoriteButton(ContentProfileType expectedContentProfileType) {
        return (expectedContentProfileType == ALBUM_TYPE) ? contentProfileFavoriteDisabledButton : contentProfileFavoriteButton;
    }

    /**
     * Returns proper More button element (enabled/disabled) based on the ContentProfileType
     */
    protected SelenideElement getCorrectProfileMoreButton(ContentProfileType expectedContentProfileType) {
        switch (expectedContentProfileType) {
            case ARTIST_TYPE, ALBUM_TYPE -> {
                return contentProfileMoreDisabledButton;
            }
            default -> {
                return contentProfileMoreButton;
            }
        }
    }

    /**
     * Returns proper Play button element (enabled/disabled) based on the ContentProfileType
     */
    protected SelenideElement getCorrectProfilePlayButton(ContentProfileType expectedContentProfileType) {
        switch (expectedContentProfileType) {
            case STATION_TYPE_SHOW -> {
                if (isOnShowProfilePageWithPlayableContent(Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                    return contentProfilePlayButton;
                }
                return contentProfilePlayDisabledButton;
            }
            case STATION_TYPE_RESTRICTED, SHOW_TYPE_SCHEDULED -> {
                return contentProfilePlayDisabledButton;
            }
            default -> {
                return contentProfilePlayButton;
            }
        }
    }

    /**
     * Returns proper profile subtitle element (numeric, link or text) based on the ContentProfileType
     */
    protected SelenideElement getCorrectProfileSubtitleLabel(ContentProfileType expectedContentProfileType) {
        switch (expectedContentProfileType) {
            case ARTIST_TYPE, ALBUM_TYPE -> {
                return contentProfileGenresLabel;
            }
            case LIVE_SPORT_EVENT_TYPE -> {
                return contentProfileLiveSportEventLabel;
            }
            case AUDIOBOOK_TYPE -> {
                return contentProfileSubtitle;
            }
            default -> {
                return contentProfileStationSubtitle;
            }
        }
    }

    /**
     * Get content profile type
     *
     * @param content known content profile type
     * @return content profile type
     */
    protected ContentProfileType getContentType(Contents... content) {
        return (content.length > 0) ? getContentProfileType(content[0].getContentProfileType()) : getContentProfilePageType();
    }

    public HashMap<String, SelenideElement> contentProfileElements(ContentProfileType... contentType) {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;

        ContentProfileType contentProfilePageType = (contentType.length > 0) ? contentType[0] : getContentProfilePageType();
        ReporterUtil.log("Content profile page type: " + contentProfilePageType);

        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileTitle);
        if (contentProfilePageType != LIVE_SPORT_EVENT_TYPE && contentProfilePageType != SCHEDULED_SPORT_EVENT_TYPE) {
            // todo: Update artwork identifier when iOS ticket is done - https://tunein.atlassian.net/browse/IOS-15733
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileArtwork);
        }

        // For the `ALBUM_TYPE` type the profile `Favorites` and `More` buttons should be disabled
        elementsMap.put(FAVORITE_LABEL.getValue(), getCorrectProfileFavoriteButton(contentProfilePageType));
        elementsMap.put(MORE_BUTTON_LABEL.getValue(), getCorrectProfileMoreButton(contentProfilePageType));

        // TODO Clean up this method
        switch (contentProfilePageType) {
            case STATION_TYPE_SHOW,
                    ALBUM_TYPE,
                    ARTIST_TYPE,
                    STATION_TYPE,
                    STATION_TYPE_PREMIUM,
                    NEWS_TYPE,
                    NEWS_TYPE_PREMIUM,
                    STATION_TYPE_RESTRICTED,
                    SHOW_TYPE_SCHEDULED,
                    EVENT_TYPE,
                    AUDIOBOOK_TYPE -> {
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, getCorrectProfileSubtitleLabel(contentProfilePageType));
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, getCorrectProfilePlayButton(contentProfilePageType));

                if (contentProfilePageType == NEWS_TYPE_PREMIUM || contentProfilePageType == STATION_TYPE_PREMIUM) {
                    elementsMap.put(GO_PREMIUM.getValue(), contentProfilePremiumPromptTitle);
                } else if (contentProfilePageType == STATION_TYPE_RESTRICTED) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileRestrictedPromptTitleText);
                    elementsMap.put(STATION_RESTRICTION_MESSAGE.getValue(), contentProfileRestrictedPromptSubtitleText);
                } else if (contentProfilePageType == SHOW_TYPE_SCHEDULED) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileScheduledShowLabel);
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfilePlayDisabledButton);
                } else if (contentProfilePageType == STATION_TYPE_SHOW) {
                    if (isOnShowProfilePageWithPlayableContent(Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileLiveShowLabel);
                    } else {
                        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileScheduledShowLabel);
                    }
                }
            }
            case PODCAST_TYPE, PODCAST_TYPE_PREMIUM -> {
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileAffiliateLink);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfilePlayButton);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileDescriptionText);
                if (contentProfilePageType == PODCAST_TYPE_PREMIUM) {
                    elementsMap.put(PREMIUM_EPISODES.getValue(), contentProfilePremiumEpisodesLabel);
                }
            }
            case LIVE_SPORT_EVENT_TYPE, SCHEDULED_SPORT_EVENT_TYPE -> {
                // `LIVE_SPORT_EVENT_TYPE` has 2 (Team-A & Team-B) artworks
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileLiveEventLeftTeamArtwork);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileLiveEventRightTeamArtwork);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileSeparator);
                if (contentProfilePageType == SCHEDULED_SPORT_EVENT_TYPE && isElementDisplayed(contentProfileScheduledShowLabel)) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, contentProfileScheduledSportEventPromptTitle);
                    elementsMap.put(NOTIFY_ME.getValue(), contentProfileNotifyMeButton);
                }
            }
            default ->
                    throw new Error("Invalid or unsupported content profile type: \"" + contentProfilePageType + "\"");
        }
        return elementsMap;
    }

    @Step("Wait for proper page for content {categoryType}")
    public void waitForProperPage(CategoryType categoryType) {
        switch (categoryType) {
            case CATEGORY_TYPE_STATIONS_THAT_PLAY -> {
            }
            case CATEGORY_TYPE_SHOWS,
                    CATEGORY_TYPE_PODCASTS,
                    CATEGORY_TYPE_SCHEDULED,
                    CATEGORY_TYPE_ARTISTS,
                    CATEGORY_TYPE_ALBUMS,
                    CATEGORY_TYPE_UPCOMING_EVENTS,
                    CATEGORY_TYPE_LIVE_EVENTS,
                    CATEGORY_TYPE_EVENTS,
                    CATEGORY_TYPE_TEAMS,
                    CATEGORY_TYPE_FEATURED,
                    CATEGORY_TYPE_RELATED_STATIONS,
                    CATEGORY_TYPE_CATCH_UP_QUICK,
                    CATEGORY_TYPE_JOIN_THE_CONVERSATION,
                    CATEGORY_TYPE_TOP_STATIONS,
                    CATEGORY_TYPE_TOP_LOCAL_STATIONS,
                    CATEGORY_TYPE_TOP_40,
                    CATEGORY_TYPE_TOP_PODCASTS,
                    CATEGORY_TYPE_POPULAR_PODCASTS_IN_YOUR_AREA,
                    CATEGORY_TYPE_TEAM_PODCASTS,
                    CATEGORY_TYPE_FOOTBALL_AFC_EAST,
                    CATEGORY_TYPE_GLOBAL_MUSIC_RADIO,
                    CATEGORY_TYPE_TOP_10_PODCASTS,
                    CATEGORY_TYPE_COMMERCIAL_FREE_NEWS,
                    CATEGORY_TYPE_NEW_COMMERCIAL_FREE_STATIONS,
                    CATEGORY_TYPE_SUGGESTED_ARTIST,
                    CATEGORY_TYPE_TOP_PODCASTS_IN_YOUR_COUNTRY,
                    CATEGORY_TYPE_TOP_PODCASTS_GLOBALLY,
                    CATEGORY_TYPE_TOP_LOCAL_NEWS,
                    CATEGORY_TYPE_COMMERCIAL_FREE_CALMING_MUSIC,
                    NATIONAL_AND_WORLD_NEWS, RECENTS, RESULTS_CATEGORY -> contentProfilePage.waitUntilPageReady();
            case CATEGORY_TYPE_STATIONS,
                    CATEGORY_TYPE_EPISODES,
                    CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION -> {
                if (contentProfilePage.isOnContentProfilePage()) {
                    contentProfilePage.tapProfilePlayButton();
                } else {
                    nowPlayingPage.waitUntilPageReady();
                }
            }
            case CATEGORY_TYPE_PREMIUM_EPISODES,
                    CATEGORY_TYPE_COMMERCIAL_FREE_MUSIC,
                    CATEGORY_TYPE_COMMERCIAL_FREE -> {
                if (upsellPage.isOnUpsellPage()) {
                    upsellPage.waitUntilPageReady();
                } else {
                    nowPlayingPage.waitUntilPageReady();
                }
            }
            default -> throw new Error("Unsupported CategoryType: " + categoryType);
        }
    }

    public enum ContentDescriptionArea {
        EPISODE_RELEASE_DATE,
        EPISODE_DESCRIPTION,
        EPISODE_DURATION,
        EPISODE_NAME,
        STATION_TITLE,
        STATION_SUBTITLE;
    }

    @Step("Get {details} for content in category {category}")
    public abstract String getContentItemDataByIndex(CategoryType category, int index, ContentDescriptionArea details, boolean skipTestIfCategoryNotFound, int... numberOfScrolls);

    public abstract String getContentItemData(SelenideElement contentCellItem, CategoryType category, ContentDescriptionArea details, int... numberOfScrolls);

    public enum FavoriteAction {
        FAVORITE, UNFAVORITE
    }

    @Step
    public void verifyEpisodePublishedWithInPastFiveDays() {
        String episodeDate = getContentItemDataByIndex(CATEGORY_TYPE_EPISODES, 1, EPISODE_RELEASE_DATE, false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getDateFormat(), Locale.ENGLISH);
        LocalDate episodeReleaseDate = LocalDate.parse(episodeDate, formatter);
        SimpleDateFormat format = new SimpleDateFormat(getDateFormat(), Locale.getDefault());
        String currentDate = format.format(new Date());
        LocalDate today = LocalDate.parse(currentDate, formatter);
        int noOfDays = Period.between(episodeReleaseDate, today).getDays();
        assertThat(noOfDays)
                .as("The most recent episode is not published with in the past five days")
                .isLessThanOrEqualTo(5);
    }

    public enum ContentProfileButtons {
        MORE, LESS, FAVORITE, UNFAVORITE
    }

    public enum ContentProfileLabels {
        MORE("More"),
        LESS("Less"),
        SEE_ALL("See All"),
        NOTIFY_ME(isAndroid() ? "NOTIFY ME" : "Notify Me"),
        MORE_BUTTON_LABEL(isAndroid() ? "MORE" : "More"),
        FAVORITE_LABEL(isAndroid() ? "FAVORITE" : "Favorite"),
        FAVORITED_LABEL("Favorited"),
        PREMIUM_EPISODES("Premium Episodes"),
        COMMERCIAL_FREE_NEWS("Commercial-Free News"),
        COMMERCIAL_FREE_MUSIC("Commercial-Free Music"),
        COMMERCIAL_FREE("Commercial-Free"),
        GO_PREMIUM("Go Premium!"),
        STATION_RESTRICTION_MESSAGE("This station is not available in your region.");

        private final String value;

        private ContentProfileLabels(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
