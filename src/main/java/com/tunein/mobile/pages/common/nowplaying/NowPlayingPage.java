package com.tunein.mobile.pages.common.nowplaying;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog.FavoriteContentType;
import com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog;
import com.tunein.mobile.pages.dialog.common.NowPlayingScheduledDialog;
import com.tunein.mobile.pages.dialog.common.NowPlayingSleepTimerDialog;
import com.tunein.mobile.pages.dialog.common.ShareDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.FAST_FORWARD;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingMediaControlButtonsType.REWIND;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStatus.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStreamTimeType.PASSED;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.*;
import static com.tunein.mobile.pages.ios.nowplaying.IosNowPlayingPage.NOW_PLAYING_STREAM_ERROR_POPUP;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_WITHOUT_ADS;
import static com.tunein.mobile.utils.DataUtil.getDurationInSeconds;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.UP;
import static com.tunein.mobile.utils.ReportingUtil.allowAllHosts;
import static com.tunein.mobile.utils.ReportingUtil.refreshAllowHosts;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//TODO Maybe we need to create separate page for Scrollable
public abstract class NowPlayingPage extends BasePage {

    protected SelenideElement nowPlayingMinimiseButton = $(android(androidUIAutomator("descriptionMatches(\"^.*Back.*$|^.*Navigate up.*$\").className(\"android.widget.ImageButton\")"))
            .ios(id("closeButton"))).as("Close button");

    protected SelenideElement nowPlayingFavoriteIcon = $(android(androidUIAutomator("descriptionContains(\"avorited\")"))
            .ios(iOSNsPredicateString("name == 'Favorite' OR name == 'Unfavorite' OR name == 'favoriteButton'"))).as("Favorite button");

    protected SelenideElement nowPlayingSleepTimerIcon = $(android(androidUIAutomator("descriptionMatches(\"^.*sleep timer.*$\")"))
            .ios(iOSNsPredicateString("label IN {'Sleep timer', 'Edit Sleep Timer'}"))).as("Sleep timer icon");

    protected SelenideElement nowPlayingEditSleepTimerIcon = $(android(accessibilityId("Edit sleep timer"))
            .ios(iOSNsPredicateString("label IN {'Edit Sleep Timer'}"))).as("Edit sleep timer button");

    protected SelenideElement nowPlayingNoAdsButton = $(android(androidUIAutomator("text(\"No ads\")"))
            .ios(iOSNsPredicateString("name == 'goPremiumButton'"))).as("No Ads button");

    protected SelenideElement nowPlayingDontLikeAdsLabel = $(android(id("whyads_overlay"))
            .ios(iOSNsPredicateString("name='whyAdsBannerLabel'"))).as("Why Ads button");

    protected SelenideElement nowPlayingTitle = $(android(id("player_main_title"))
            .ios(iOSNsPredicateString("name == 'titleLabel'"))).as("Title label");

    public SelenideElement nowPlayingSubtitle = $(android(id("player_main_subtitle"))
            .ios(iOSNsPredicateString("name == 'subtitleLabel'"))).as("Subtitle label");

    protected SelenideElement nowPlayingStationLogo = $(android(id("player_logo_large"))
            .ios(id("artworkImage"))).as("Artwork image");

    public SelenideElement nowPlayingAdvertisementLabel = $(android(androidUIAutomator("text(\"Advertisement\").resourceId(\"tunein.player:id/player_main_title\")"))
            .ios(iOSNsPredicateString("label == 'Advertisement' AND name == 'titleLabel'"))).as("Advertisement label");

    protected SelenideElement nowPlayingSeekBarContainer = $(android(id("seekbar_layout"))
            .ios(id("bufferProgressBar"))).as("Seekbar container");

    protected SelenideElement nowPlayingSeekBar = $(android(id("player_progress"))
            .ios(id("progressSlider"))).as("Seek bar");

    public SelenideElement nowPlayingTimeCounterPassed = $(android(id("player_time_passed"))
            .ios(id("leftTime"))).as("Time counter passed time");

    public SelenideElement nowPlayingTimeCounterLeft = $(android(id("player_time_left"))
            .ios(iOSNsPredicateString("name == 'rightTime'"))).as("Time counter left time");

    public SelenideElement nowPlayingLiveLabel = $(android(androidUIAutomator("textContains(\"LIVE\")"))
            .ios(iOSNsPredicateString("name == 'liveIndicatorLabel' OR label IN {'LIVE', 'PLAY LIVE'}"))).as("LIVE label");

    protected SelenideElement nowPlayingPlayLiveLabel = $(android(androidUIAutomator("textContains(\"Play LIVE\")"))
            .ios(iOSNsPredicateString("not defined"))).as("Play Live label");

    public SelenideElement streamTesterNowPlayingTimeCounterPassed = $(android(id("player_time_passed"))
            .ios(id("leftTime"))).as("Time counter passed time");

    public SelenideElement streamTesterNowPlayingTimeCounterLeft = $(android(id("player_time_left"))
            .ios(id("rightTime"))).as("Time counter left time");

    protected SelenideElement nowPlayingStopButton = $(android(accessibilityId("Stop"))
            .ios(id("stopButton"))).as("Stop button");

    public SelenideElement nowPlayingPauseButton = $(android(androidUIAutomator("description(\"Pause Station\")"))
            .ios(id("pauseButton"))).as("Pause button");

    public SelenideElement nowPlayingPlayButton = $(android(androidUIAutomator("description(\"Play\")"))
            .ios(id("playButton"))).as("Play button");

    public SelenideElement nowPlayingPauseStopButton = $(android(androidUIAutomator("descriptionMatches(\"^.*Stop|Pause.*$\")"))
            .ios(iOSNsPredicateString("name IN { 'stopButton', 'pauseButton'}"))).as("Pause/Stop button");

    public SelenideElement nowPlayingMainPlayPauseStopButton = $(android(androidUIAutomator("descriptionMatches(\"^.*Stop|Pause.*$|^.*Play\")"))
            .ios(iOSNsPredicateString("name IN { 'stopButton', 'pauseButton', 'playButton'}"))).as("Stop/Pause/Play button");

    public SelenideElement nowPlayingBufferingAnimation = $(android(androidUIAutomator("Not Implemented"))
            .ios(id("bufferingView"))).as("Buffering animation");

    protected SelenideElement nowPlayingExternalDevicesButton = $(android(accessibilityId("Cast"))
            .ios(iOSNsPredicateString("label == 'External devices'"))).as("Chromecast button");

    protected SelenideElement nowPlayingRewindButton = $(android(accessibilityId("rewind"))
            .ios(id("skipBackButton"))).as("Rewind button");

    public SelenideElement nowPlayingFastForwardButton = $(android(accessibilityId("scan forward"))
            .ios(id("skipForwardButton"))).as("Fast-Forward button");

    protected SelenideElement nowPlayingMoreOptions = $(android(id("menu_more"))
            .ios(iOSNsPredicateString("label == 'More'"))).as("More button");

    protected SelenideElement nowPlayingSpeedButton = $(android(androidUIAutomator("description(\"Playback Speed\")"))
            .ios(id("playbackSpeed"))).as("Playback Speed button");

    protected SelenideElement nowPlayingPlaybackSpeedTooltipPopUp = $(android(xpath("//android.widget.LinearLayout[./*[contains(@text,'Customize audio playback from half speed to 3x ')]]/.."))
            .ios(accessibilityId("New: Change playback speed"))).as("Playback speed tooltip");

    protected SelenideElement nowPlayingRecentsCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[contains(@text,'RECENTS')]) and (.//*[contains(@content-desc,'guideItemImage')])]"))
            .ios(xpath("//XCUIElementTypeOther[@name='RECENTS']/following-sibling::XCUIElementTypeCell[1][.//*[@name='tileCellViewIdentifier']]"))).as("Recents card");

    protected SelenideElement nowPlayingSimilarToCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[contains(@text,'Similar to')]) and (.//*[contains(@content-desc,'guideItemTitle')])]"))
            .ios(xpath("//XCUIElementTypeOther[contains(@name,'Similar to')]/following-sibling::XCUIElementTypeCell[1][(.//*[@name='tileCellViewIdentifier'])]"))).as("Similar to card");

    protected SelenideElement nowPlayingLocalRadioToCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[contains(@text,'Local')]) and (.//*[contains(@content-desc,'guideItemTitle')])]"))
            .ios(xpath("//XCUIElementTypeOther[contains(@name,'Local Radio')]/following-sibling::XCUIElementTypeCell[1][(.//*[@name='tileCellViewIdentifier'])]"))).as("Local radio card");

    protected SelenideElement nowPlayingProfileCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[@resource-id='tunein.player:id/profileBtn'])]"))
            .ios(xpath("//XCUIElementTypeCell[(.//*[@name='Go to Profile'])]"))).as("Profile card");

    protected SelenideElement nowPlayingScheduleCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[contains(@content-desc,'_subtitle_')]) and (.//*[@text='UP NEXT:'])]"))
            .ios(xpath("//XCUIElementTypeCell[.//XCUIElementTypeTable//*[@name='UP NEXT:']]"))).as("Scheduled card");

    public ElementsCollection nowPlayingScheduleCardTriDotButtonList = $$(android(id("scheduleOptions"))
            .ios(iOSNsPredicateString("name == 'TridotRefresh'"))).as("Scheduled card Tri dot button list");

    protected SelenideElement nowPlayingEpisodeCard = $(android(xpath("//*[contains(@content-desc,'item') and (.//*[@resource-id='tunein.player:id/seeMoreBtn']) and (.//*[@text='EPISODE NOTES:'])]"))
            .ios(xpath("//XCUIElementTypeCell[(.//XCUIElementTypeStaticText[(@name='See More' or @name='See Less') and (@visible='true')]) and (.//XCUIElementTypeStaticText[@name='EPISODE NOTES:'])]"))).as("Episode card");

    protected SelenideElement nowPlayingGoToProfileButton = $(android(id("profileBtn"))
            .ios(iOSNsPredicateString("label == 'Go to Profile' AND type == 'XCUIElementTypeButton'"))).as("'Go to Profile' button");

    protected SelenideElement nowPlayingSeeLessButton = $(android(androidUIAutomator("text(\"See Less\")"))
            .ios(iOSNsPredicateString("label == 'See Less'"))).as("See less button");

    protected SelenideElement nowPlayingSeeMoreButton = $(android(androidUIAutomator("text(\"See More\")"))
            .ios(iOSNsPredicateString("label == 'See More'"))).as("See more button");

    protected SelenideElement localRadioCardTitle = $(android(androidUIAutomator("text(\"Local Radio\")"))
            .ios(iOSNsPredicateString("name == 'Local Radio' AND type == 'XCUIElementTypeOther'"))).as("Local radio card title");

    protected SelenideElement similarToCardTitle = $(android(xpath("//*[contains(@text,'Similar to')]"))
            .ios(xpath("//XCUIElementTypeOther[contains(@name,'Similar to')]"))).as("'Similar To' card title");

    protected SelenideElement profileCardTitle = $(android(xpath("(//*[contains(@resource-id,'titleTxt') and (//*[contains(@resource-id,\"profileImg\")])])[1]"))
            .ios(xpath("//XCUIElementTypeButton[@name='Go to Profile']/preceding-sibling::XCUIElementTypeStaticText[2]"))).as("Profile card title");

    protected SelenideElement scheduleCardTitle = $(android(xpath("(//*[contains(@text,'UP NEXT:') or contains(@text,'TOMORROW:')])[1]"))
            .ios(iOSNsPredicateString("label IN {'UP NEXT:', 'TOMORROW:'}"))).as("Scheduled card title");

    protected SelenideElement episodeNotesTitle = $(android(androidUIAutomator("text(\"EPISODE NOTES:\")"))
            .ios(iOSNsPredicateString("label == 'EPISODE NOTES:'"))).as("Episode notes title");

    protected SelenideElement connectionErrorLabel = $(android(androidUIAutomator("text(\"A stream connection error occurred\")"))
            .ios(iOSNsPredicateString("not defined"))).as("Connection error label");

    protected SelenideElement nowPlayingShareButton = $(android(androidUIAutomator("description(\"Share\")"))
            .ios(iOSNsPredicateString("label == 'Share'"))).as("Share button");

    protected SelenideElement nowPlayingAdBanner = $(android(androidUIAutomator("resourceId(\"tunein.player:id/player_ad_container_medium\")"))
            .ios(xpath("//*[./XCUIElementTypeOther/*[@name='subtitleLabel']]/preceding-sibling::*[1]/*/XCUIElementTypeOther[not(XCUIElementTypeImage[@name='artworkImage'])]"))).as("Ad banner");

    protected SelenideElement gotItPrompt = $(android(id("tunein.player:id/summary"))
            .ios(iOSNsPredicateString("label CONTAINS 'Got it.' and type == 'XCUIElementTypeStaticText'"))).as("Got It prompt");

    protected SelenideElement gotItPromptClose = $(android(id("tunein.player:id/close_button"))
            .ios(iOSNsPredicateString("label == \"Misc/Close\""))).as("'Close Got It Prompt' button");

    protected SelenideElement switchStationsContainer = $(android(id("tunein.player:id/switch_boost_selector_viewpager_container"))
            .ios(xpath("//XCUIElementTypeCollectionView[.//XCUIElementTypeImage]"))).as("Switch container");

    protected ElementsCollection listOfSwitchContainerStations = $$(android(xpath("//*[@resource-id='tunein.player:id/switch_boost_selector_viewpager_container']//android.widget.ImageView"))
            .ios(iOSClassChain("**/XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeImage"))).as("List of station in Switch container");

    protected SelenideElement nowPlayingAlarm = $(android(id("Alarm"))
            .ios(id("NotificationShortLookView"))).as("Now Playing Alarm View");

    private static Map<Long, Boolean> firstLaunchMap = new HashMap<>();

    private static final int REQUIRED_CARD_NUMBER_OF_SCROLLS = 8;

    public static final String SPEED_VALUE_0_5 = "0.5";

    public static final String SPEED_VALUE_1_0 = "1.0";

    public static final String SPEED_VALUE_1_5 = "1.5";

    public static final String SPEED_VALUE_2_0 = "2.0";

    public static final String SPEED_VALUE_2_5 = "2.5";

    public static final String SPEED_VALUE_3_0 = "3.0";

    public static final String[] SPEED_VALUES = {
            SPEED_VALUE_0_5,
            SPEED_VALUE_1_0,
            SPEED_VALUE_1_5,
            SPEED_VALUE_2_0,
            SPEED_VALUE_2_5,
            SPEED_VALUE_3_0
    };

    public static final String ADVERTISEMENT = "Advertisement";

    /* --- Loadable Component Method --- */

    public abstract NowPlayingPage waitUntilPageReady();

    public abstract NowPlayingPage waitUntilPageReadyWithKnownContent(Contents content);

    public abstract NowPlayingPage waitUntilPageReadyWithKnownContentType(ContentType contentType);

    public abstract NowPlayingPage waitUntilPageReadyLiteVersion();

    @Step("Wait for Live label")
    public NowPlayingPage waitTillVisibilityOfLive(Duration... timeoutDuration) {
        Duration duration = timeoutDuration.length > 0 ? timeoutDuration[0] : Duration.ofSeconds(config().pageReadyTimeoutSeconds());
        waitTillVisibilityOfElement(nowPlayingLiveLabel, duration);
        return this;
    }

    public abstract NowPlayingPage waitUntilMinimiseButtonIsClickable();

    public abstract NowPlayingPage waitUntilContentPartiallyBuffered();

    /* --- Action Methods --- */

    //TODO Add AirPlay Page Object Model
    @Step
    public void tapOnExternalDevicesButton() {
        clickOnElement(nowPlayingExternalDevicesButton);
    }

    @Step("Tap on ad banner in Now Playing page")
    public void tapOnNowPlayingAdBanner() {
        int clickAttempts = 8;
        while (isElementDisplayed(nowPlayingFavoriteIcon)) {
            if (clickAttempts == 0) {
                throw new Error("Too many click attempts");
            }
            clickOnElement(nowPlayingAdBanner, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            customWait(Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            clickAttempts = --clickAttempts;
        }
    }

    public abstract NowPlayingPage tapOnFavoriteIcon(boolean addToFavorite, FavoriteContentType... typeOfFavoriteContent);

    public abstract MiniPlayerPage minimizeNowPlayingScreen();

    @Step("Minimize Now Playing screen if displayed")
    public void minimizeIfNowPlayingDisplayed() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(nowPlayingMinimiseButton);
    }

    @Step("Click on Now Playing title")
    public void tapOnNowPlayingTitle() {
        clickOnElement(nowPlayingTitle);
        if (!isOnNowPlayingPage(Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
            contentProfilePage.waitUntilPageReady();
        }
    }

    @Step("Tap on 'No Ads' button")
    public UpsellPage tapOnNoAdsButton() {
        clickOnElement(nowPlayingNoAdsButton);
        return upsellPage.waitUntilPageReady();
    }

    @Step("Tap on 3 dots")
    public NowPlayingScheduledDialog tapOnThreeDotsButton() {
        clickOnElement(nowPlayingScheduleCardTriDotButtonList.get(0));
        return nowPlayingScheduledDialog.waitUntilPageReady();
    }

    @Step
    public NowPlayingSleepTimerDialog openSleepTimerDialogThroughIcon() {
        clickOnElement(nowPlayingSleepTimerIcon);
        return nowPlayingSleepTimerDialog.waitUntilPageReady();
    }

    public abstract NowPlayingPage hidePlaybackSpeedTooltipPopUpIfPresent();

    public abstract NowPlayingPage moveScrubberSliderToSpecificPosition(ScrubberPosition scrubberPosition, int topicDuration);

    @Step("Tap on now playing {mediaControlButtonsType} button")
    public NowPlayingPage tapNowPlayingRewindOrFastForwardButton(NowPlayingMediaControlButtonsType mediaControlButtonsType, int... numberOfTaps) {
        SelenideElement requiredButton;
        switch (mediaControlButtonsType) {
            case REWIND -> {
                requiredButton = nowPlayingRewindButton;
            }
            case FAST_FORWARD -> {
                requiredButton = nowPlayingFastForwardButton;
            }
            default -> throw new Error("Unexpected value: " + mediaControlButtonsType);
        }
        if (numberOfTaps.length > 0) {
            for (int i = 1; i <= numberOfTaps[0]; i++) {
                clickOnElement(requiredButton);
            }
        } else {
            clickOnElement(requiredButton);
        }
        return this;
    }

    @Step("Tap on More Options button")
    public NowPlayingMoreOptionsDialog tapOnMoreOptionsButton() {
        clickOnElement(nowPlayingMoreOptions);
        return nowPlayingMoreOptionsDialog.waitUntilPageReady();
    }

    @Step("Tap on play button")
    public NowPlayingPage tapOnPlayButton() {
        clickOnElement(nowPlayingPlayButton);
        waitUntilStreamStopOpeningAndBuffering();
        return this;
    }

    @Step("Tap on play live button")
    public NowPlayingPage tapOnPlayLiveButton() {
        clickOnElement(nowPlayingPlayLiveLabel);
        return this;
    }

    @Step("Tap on pause button")
    public NowPlayingPage tapOnPauseButton() {
        clickOnElement(nowPlayingPauseButton);
        return this;
    }

    @Step
    public NowPlayingPage tapOnStopButton() {
        clickOnElement(nowPlayingStopButton);
        return this;
    }

    @Step("Tap on \"Stop\" or \"Pause\" button to stop the stream playback")
    public NowPlayingPage stopStreamPlaying() {
        waitUntilStreamStopOpeningAndBuffering();
        clickOnElement(nowPlayingPauseStopButton);
        return this;
    }

    @Step
    public NowPlayingPage tapPauseButtonIfEnabled() {
        if (isElementEnabled(nowPlayingPauseButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            tapOnPauseButton();
        }
        return this;
    }

    //TODO Add Page Object model for Speed dialog
    @Step("Click on speed button")
    public void tapOnSpeedButton() {
        clickOnElement(nowPlayingSpeedButton);
        nowPlayingSpeedPlaybackDialog.waitUntilPageReady();
    }

    //TODO Add Page Object model for Sleeper time dialog
    public abstract void openEditSleepTimerDialog();

    @Step
    public NowPlayingPage playStreamForSecond() {
        waitUntilStreamStopOpeningAndBuffering();
        waitForSliderPosition(1, Duration.ofSeconds(config().waitShortTimeoutSeconds()), false);
        return this;
    }

    @Step("Play stream for more than specified time in seconds {requiredTimeInSeconds}")
    public NowPlayingPage playStreamMoreThanRequiredTime(int requiredTimeInSeconds) {
        waitUntilStreamStopOpeningAndBuffering();
        waitForSliderPosition(requiredTimeInSeconds, Duration.ofSeconds(requiredTimeInSeconds), false);
        return this;
    }

    @Step("Play stream until '{duration}' ")
    public NowPlayingPage playStreamForRequiredTime(Duration duration) {
        waitForSliderPosition((int) duration.getSeconds(), Duration.ofSeconds(duration.getSeconds()), false);
        return this;
    }

    @Step("Scroll to card {card}")
    public SelenideElement scrollToRequiredCard(NowPlayingScrollableCards card) {
        SelenideElement cardElement = getMapOfCards().get(card.getScrollabelCardNameValue()).as("NowPlaying Scrollable card: " + card.getScrollabelCardNameValue());
        if (isElementDisplayed(nowPlayingSeekBar, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
            return scrollTo(cardElement, DOWN, REQUIRED_CARD_NUMBER_OF_SCROLLS);
        }
        String firstDisplayedCardLabel = getLabelOfFirstDisplayedNowPlayingCard();
        int firstDisplayedCardIndex = getNowPlayingCardOrderNumber(getCardType(firstDisplayedCardLabel));
        int requiredCardIndex = getNowPlayingCardOrderNumber(card);
        if (firstDisplayedCardIndex <= requiredCardIndex) {
            return scrollTo(cardElement, DOWN, REQUIRED_CARD_NUMBER_OF_SCROLLS);
        } else {
            return scrollTo(cardElement, UP, REQUIRED_CARD_NUMBER_OF_SCROLLS);
        }
    }

    public abstract NowPlayingPage openContentWithNameInCard(String contentName, NowPlayingScrollableCards card);

    public abstract NowPlayingPage openContentByNumberInCard(int contentNumber, NowPlayingScrollableCards card);

    @Step
    public ContentProfilePage tapOnGoToProfileButtonInProfileCard() {
        clickOnElement(nowPlayingGoToProfileButton);
        return contentProfilePage.waitUntilPageReady();
    }

    @Step
    public NowPlayingPage tapOnSeeLessButton() {
        clickOnElement(nowPlayingSeeLessButton);
        return this;
    }

    @Step
    public NowPlayingPage tapOnSeeMoreButton() {
        clickOnElement(nowPlayingSeeMoreButton);
        return this;
    }

    @Step("Tap on share button")
    public ShareDialog tapOnShareButton() {
        clickOnElement(nowPlayingShareButton);
        return shareDialog.waitUntilPageReady();
    }

    public abstract NowPlayingPage swipeStationInSwitchContainer(SwipeDirection direction);

    public abstract NowPlayingPage closeGotItPrompt();

    public abstract NowPlayingPage setAlarmTimeFlow(int minutesAhead);

    /**
     * Change stream speed to specified value
     * @param requiredSpeedValue Speed value that setting to; it's recommended to use "SPEED_VALUE" constants from NowPlayingPage
     * @param applyToAll (iOS only) Set if the adjusted playback speed applies to all future streams with variable speed;
     *                   on Android, speed adjustments apply to all future streams by default
     * @return NowPlayingPage
     */
    @Step("Change stream speed: {requiredSpeedValue}")
    public NowPlayingPage adjustPlaybackSpeed(String requiredSpeedValue, boolean... applyToAll) {
        String currentValue = nowPlayingPage.getSpeedAdjustmentValue().replace("x", "").replace("X", "");
        if (!currentValue.equals(requiredSpeedValue)) {
            nowPlayingPage.tapOnSpeedButton();
            nowPlayingSpeedPlaybackDialog.setSpeedPickerPosition(currentValue, requiredSpeedValue, applyToAll);
        }
        return this;
    }

    public abstract NowPlayingPage tapOnSwitchContainerStation(SwipeDirection direction);

    /* --- Validation Methods --- */

    @Step("Validate Now Playing Screen is displayed")
    public NowPlayingPage validateThatNowPlayingIsOpened() {
        assertThat(isOnNowPlayingPage()).as("User is not redirected to Now Playing screen").isTrue();
        return this;
    }

    @Step("Verify the content follow status. Expected value followed = {expectedFollowedState}")
    public NowPlayingPage validateContentFollowState(boolean expectedFollowedState) {
        assertThat(isFavoriteIconFilled()).as("Station follow state is wrong")
                .isEqualTo(expectedFollowedState);
        return this;
    }

    @Step("Validate stream title is not equal to {expectedTitle}")
    public NowPlayingPage validateNowplayingStationTitlesNotEqual(String expectedTitle) {
        assertThat(expectedTitle).as("Station titles are equal").isNotEqualTo(getTextValueOfNowPlayingTitle());
        return this;
    }

    @Step
    public NowPlayingPage validateThatCloseButtonDisplayed() {
        assertThat(isElementDisplayed(nowPlayingMinimiseButton)).as("Minimise button isn't displayed").isTrue();
        return this;
    }

    @Step("Validating the stream speed is {expectedValue}")
    public NowPlayingPage validateSpeedAdjustmentValue(String expectedValue) {
        String actual = getSpeedAdjustmentValue();
        assertThat(actual).as("Now Playing steam speed : " + actual).isEqualToIgnoringCase(expectedValue + "x");
        return this;
    }

    public abstract NowPlayingPage validateNowPlayingMetadataIsNotEmpty(ContentType contentType);

    @Step("Validate that title in Now Playing page equals to {expectedTitle}")
    public NowPlayingPage validateNowPlayingTitleIsEqualTo(String expectedTitle) {
        assertThat(getTextValueOfNowPlayingTitle()).as("Now Playing title doesn't match expected title").isEqualTo(expectedTitle);
        return this;
    }

    @Step
    public NowPlayingPage validateNowPlayingSubtitleIsEqualTo(String expectedSubtitle) {
        assertThat(getTextValueOfNowPlayingSubtitle()).as("Now Playing subtitle doesn't match expected subtitle").isEqualTo(expectedSubtitle);
        return this;
    }

    @Step
    public NowPlayingPage validateEditSleepTimerIconDisplayed() {
        assertThat(isElementDisplayed(nowPlayingEditSleepTimerIcon)).as("Edit sleep timer icon is not displayed").isTrue();
        return this;
    }

    @Step("Validate rewind and fast forward buttons are not displayed")
    public NowPlayingPage validateRewindAndFastForwardButtonsAreNotDisplayed() {
        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingFastForwardButton)).as("Fast Forward Button is displayed").isTrue();
        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingRewindButton)).as("Rewind Button is displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    public abstract NowPlayingPage validateNowPlayingSeekBarChangedPosition(ScrubberPosition scrubberPosition, int streamDuration);

    public abstract NowPlayingPage validateNowPlayingOpenNewEpisode(String previousEpisode);

    public abstract NowPlayingPage validateStreamErrorIsDisplayed();

    @Step("Validate stream time after Fats Forward and Rewind")
    public NowPlayingPage validateStreamTimeAfterFastForwardAndRewind() {
        int beforeFastForward = getStreamTimeInSeconds(PASSED);
        tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD);
        int afterFastForward = getStreamTimeInSeconds(PASSED);
        tapNowPlayingRewindOrFastForwardButton(REWIND);
        int afterRewind = getStreamTimeInSeconds(PASSED);
        getSoftAssertion().assertThat(beforeFastForward).as("Stream time before fast forward should be less than after fast forward").isLessThanOrEqualTo(afterFastForward);
        getSoftAssertion().assertThat(afterRewind).as("Stream time after rewind should be less than before rewind").isLessThanOrEqualTo(afterFastForward);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate timer after rewind or fast forward")
    public NowPlayingPage validateTimerIsChangedAfterRewindOrForward(int secondsBeforeAction, NowPlayingStreamTimeType timeType, NowPlayingMediaControlButtonsType action, int numberOfClicks) {
        int secondsAfterAction = getStreamTimeInSeconds(timeType);
        if (action == REWIND) {
            if (secondsBeforeAction < 30 * numberOfClicks) {
                assertThat(secondsBeforeAction - secondsAfterAction).as("Difference between time before rewind and after should be equal 30").isCloseTo(0, Offset.offset(5));
            } else {
                assertThat(secondsBeforeAction - secondsAfterAction).as("Difference between time before rewind and after should be equal 0").isCloseTo(30 * numberOfClicks, Offset.offset(5));
            }
        } else {
            assertThat(secondsAfterAction - secondsBeforeAction).as("Stream time before fast forward should be less than after fast forward").isCloseTo(30 * numberOfClicks, Offset.offset(5));
        }
        return this;
    }

    @Step("Check that the stream is close to the start, within 10 seconds of 00:00")
    public NowPlayingPage validateStreamCloseToStart() {
        assertThat(getStreamTimeInSeconds(PASSED)).as("Stream is not close to the start").isLessThanOrEqualTo(15);
        return this;
    }

    @Step("Check that the stream passed time is not changed")
    public NowPlayingPage validateStreamPassedTimeIsNotChanged() {
        int passedTime = getStreamTimeInSeconds(PASSED);
        assertThat(passedTime).as("Stream is not close to the start").isLessThanOrEqualTo(passedTime + 10);
        return this;
    }

    @Step("Validate stream is rewound to {expectedRewindTime} seconds")
    public NowPlayingPage validateIsRewound(int expectedRewindTime) {
        long startBeforeRewind = System.currentTimeMillis();
        int beforeRewind = getStreamTimeInSeconds(PASSED);
        long endBeforeRewind = System.currentTimeMillis();

        tapNowPlayingRewindOrFastForwardButton(REWIND);

        long startAfterRewind = System.currentTimeMillis();
        int afterRewind = getStreamTimeInSeconds(PASSED);
        long endAfterRewind = System.currentTimeMillis();

        int diff = beforeRewind + ((int) ((endBeforeRewind - startBeforeRewind) / 1000)) - (afterRewind + ((int) ((endAfterRewind - startAfterRewind) / 1000)));
        assertThat(diff).as("Stream isn't rewound " + expectedRewindTime + " seconds").isCloseTo(expectedRewindTime, Offset.offset(20));
        return this;
    }

    @Step("Validate rewind and fast forward buttons are displayed")
    public NowPlayingPage validateRewindAndFastForwardButtonsAreDisplayed() {
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingFastForwardButton)).as("Fast Forward Button is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingRewindButton)).as("Rewind Button is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate that stream is fast forwarded on 30 seconds")
    public NowPlayingPage validateIsFastForwarded() {
        int beforeFastForward = getStreamTimeInSeconds(PASSED);
        tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD);
        int afterFastForward = getStreamTimeInSeconds(PASSED);
        int diff = afterFastForward - beforeFastForward;
        assertThat(diff).as("Stream isn't fast forwarded on 30 seconds").isBetween(30, 40);
        return this;
    }

    @Step("Validate that Preroll is absent")
    public NowPlayingPage validateThatPreRollIsAbsent() {
        assertThat(isElementDisplayed(nowPlayingAdvertisementLabel))
                .as("Pre-roll/midroll ad is playing for the non-dfp enabled station")
                .isFalse();
        return this;
    }

    @Step("Validate sleep timer icon is displayed")
    public NowPlayingPage validateSleepTimerIconDisplayed() {
        assertThat(isElementDisplayed(nowPlayingSleepTimerIcon)).as("Sleep timer icon is not displayed").isTrue();
        return this;
    }

    @Step("Validate that seekbar is displayed")
    public NowPlayingPage validateSeekBarDisplayed() {
        assertThat(isElementDisplayed(nowPlayingSeekBar)).as("Seekbar is not displayed").isTrue();
        return this;
    }

    @Step("Validate that external devices button is displayed")
    public NowPlayingPage validateThatExternalDevicesButtonDisplayed() {
        Assertions.assertThat(isElementDisplayed(nowPlayingExternalDevicesButton)).as("External Devices button is not displayed").isTrue();
        return this;
    }

    @Step("Validate that stop button is displayed")
    public NowPlayingPage validateStopButtonDisplayed() {
        assertThat(isElementEnabled(nowPlayingStopButton)).as("Stop button is not displayed").isTrue();
        return this;
    }

    @Step("Validate that play button is displayed")
    public NowPlayingPage validatePlayButtonDisplayed() {
        assertThat(isElementDisplayed(nowPlayingPlayButton)).as("Play button is not displayed").isTrue();
        return this;
    }

    @Step("Validate that station logo is displayedd")
    public NowPlayingPage validateStationLogoDisplayed() {
        assertThat(isElementDisplayed(nowPlayingStationLogo)).as("Station Logo is not displayed").isTrue();
        return this;
    }

    @Step("Validate that 'No Ads' button is displayed")
    public NowPlayingPage validateNoAdsButtonDisplayed() {
        assertThat(isElementDisplayed(nowPlayingNoAdsButton)).as("No ads button is not displayed").isTrue();
        return this;
    }

    @Step("Validate that 'No Ads' button is not displayed")
    public NowPlayingPage validateNoAdsButtonNotDisplayed() {
        if (isElementDisplayed(nowPlayingNoAdsButton)) {
            assertThat(getElementNameOrLabel(nowPlayingNoAdsButton))
                    .as("No ads button is displayed")
                    .doesNotContain("No Ads");
        } else {
            assertThat(isElementNotDisplayed(nowPlayingNoAdsButton))
                    .as("No ads button is displayed")
                    .isTrue();
        }
        return this;
    }

    @Step("Validate Live label")
    public NowPlayingPage validateLiveButtonIsNotClickable() {
        Assertions.assertThat(isElementClickable(nowPlayingLiveLabel)).as("Live button is clickable").isTrue();
        return this;
    }

    @Step
    public NowPlayingPage validateNoAdsButton(boolean isNoAdsExpected) {
        String message = isNoAdsExpected ? "\"No ads\" button is not displayed" : "\"No ads\" button is displayed";
        assertThat(isElementDisplayed(nowPlayingNoAdsButton)).as(message).isEqualTo(isNoAdsExpected);
        return this;
    }

    @Step("Validate got it prompt is displayed")
    public NowPlayingPage validateGotItPromptIsDisplayed() {
        closePermissionPopupsIfDisplayed();
        assertThat(isElementDisplayed(gotItPrompt)).as("Got it dialog is not displayed").isTrue();
        return this;
    }

    @Step("Validate stream has started playing")
    public NowPlayingPage validateStreamStartPlaying(ContentType... contentType) {
        ContentType requiredContentType = contentType.length > 0 ? contentType[0] : getContentType();

        waitUntilStreamStartOpeningOrBuffering();
        waitUntilStreamStopOpeningAndBuffering();

        // Pressing play on podcast profile can result in playing a live station that's currently playing it
        if (requiredContentType == PODCAST && getContentType() == LIVE_STATION) {
            requiredContentType = LIVE_STATION;
        }

        getSoftAssertion()
                .assertThat(isElementDisplayed(nowPlayingPauseStopButton))
                .as("Neither pause nor stop button is displayed")
                .isTrue();
        switch (requiredContentType) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                int streamTimeBeforeWait = getStreamTimeInSeconds(PASSED);
                waitForSliderPosition(3, Duration.ofSeconds(config().oneMinuteInSeconds()), false);

                int streamTimeAfterWait = getStreamTimeInSeconds(PASSED);
                getSoftAssertion()
                        .assertThat(streamTimeBeforeWait)
                        .as("Stream didn't start play")
                        .isNotEqualTo(streamTimeAfterWait);
            }
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL, OWNED_AND_OPERATED -> {
                getSoftAssertion()
                        .assertThat(isElementNotDisplayed(nowPlayingPlayButton))
                        .as("Play button is displayed")
                        .isTrue();
            }
            default -> throw new Error("Unhandled contentType " + contentType);
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    public NowPlayingPage validateStreamIsPaused() {
        waitUntilStreamStopOpeningAndBuffering();
        switch (getNowPlayingContentType()) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                int streamTimeBeforeWait = getStreamTimeInSeconds(PASSED);
                customWait(Duration.ofSeconds(config().waitShortTimeoutSeconds()));
                int streamTimeAfterWait = getStreamTimeInSeconds(PASSED);
                getSoftAssertion().assertThat(streamTimeBeforeWait == streamTimeAfterWait).as("Stream is not paused").isTrue();
            }
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL, OWNED_AND_OPERATED -> {
                getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPauseButton)).as("Pause button is displayed").isTrue();
            }
            default -> throw new Error("Unhandled contentType");
        }
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingPlayButton)).as("Play button is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate pause button is displayed")
    public NowPlayingPage validatePauseButtonDisplayed() {
        assertThat(isElementDisplayed(nowPlayingPauseButton)).as("Pause button is not displayed").isTrue();
        return this;
    }

    public abstract NowPlayingPage validateStreamIsStopped();

    public abstract NowPlayingPage validateSeekBarLooksAsExpected(Contents... content);

    public abstract NowPlayingPage validateSeekBarLooksAsExpected(ContentType contentType);

    @Step("Validate media contorls buttons looks as expected")
    public NowPlayingPage validateMediaControlsButtonsLooksAsExpected(Contents... content) {
        ContentType contentType = getContentType(content);
        NowPlayingStatus nowPlayingStatus = getNowPlayingStreamStatus();
        switch (contentType) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                switch (nowPlayingStatus) {
                    case PLAYING -> {
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingPauseButton)).as("Neither stop nor pause button is enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPlayButton)).as("Play button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingRewindButton)).as("Rewind button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingFastForwardButton)).as("Fast Forward button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    case PAUSED -> {
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPauseButton)).as("Pause button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingPlayButton)).as("Play button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingRewindButton)).as("Rewind button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingFastForwardButton)).as("Fast Forward button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    default -> throw new Error("Unhandled nowPlayingStatus " + nowPlayingStatus);
                }
            }
            case LIVE_STATION, CUSTOM_URL, PREMIUM_LIVE_STATION -> {
                switch (nowPlayingStatus) {
                    case PLAYING -> {
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingPauseStopButton)).as("Pause or Stop button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPlayButton)).as("Play button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    case PAUSED -> {
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPauseButton)).as("Pause button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingPlayButton)).as("Play button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    default -> throw new Error("Unhandled nowPlayingStatus " + nowPlayingStatus);
                }
            }
            case OWNED_AND_OPERATED -> {
                switch (nowPlayingStatus) {
                    case PLAYING -> {
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingStopButton)).as("Neither stop nor pause button is enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPlayButton)).as("Play button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    case PAUSED -> {
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingStopButton)).as("Pause button is displayed for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingPlayButton)).as("Play button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                        getSoftAssertion().assertThat(isElementEnabled(nowPlayingMoreOptions)).as("More options button is not enabled for state: " + nowPlayingStatus.name()).isTrue();
                    }
                    default -> throw new Error("Unhandled nowPlayingStatus " + nowPlayingStatus);
                }
            }
            default -> throw new Error("Unhandled contentType " + contentType);
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate required cards: {cardsList} are present ")
    public NowPlayingPage validateRequiredCardsArePresent(List<NowPlayingScrollableCards> cardsList) {
        cardsList.stream().forEach(card -> {
            getSoftAssertion().assertThat(isElementDisplayed(scrollToRequiredCard(card))).as(card.name() + " is not displayed").isTrue();
        });
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate nowplaying page cards elements")
    public NowPlayingPage validateNowPlayingCardsElements(Contents... content) {
        ContentType contentType = (content.length > 0) ? ContentType.getContentTypeValue(content[0].getStreamType()) : getNowPlayingContentType();
        ScrollDirection direction = isElementFullyVisible(nowPlayingSubtitle) ? DOWN : UP;
        if (content.length > 0) {
            listOfValidationsForNowPlayingCardsElements(direction, contentType, content[0].getStreamName());
        } else {
            listOfValidationsForNowPlayingCardsElements(direction, contentType);
        }
        return this;
    }

    @Step
    public NowPlayingPage validateNowPlayingCardsElements(ScrollDirection scrollDirection, Contents... content) {
        ContentType contentType = (content.length > 0) ? ContentType.getContentTypeValue(content[0].getStreamType()) : getNowPlayingContentType();
        if (content.length > 0) {
            listOfValidationsForNowPlayingCardsElements(scrollDirection, contentType, content[0].getStreamName());
        } else {
            listOfValidationsForNowPlayingCardsElements(scrollDirection, contentType);
        }
        return this;
    }

    @Step("Validate now playing is scrollable")
    public NowPlayingPage validateNowPlayingScreenIsScrollable() {
        scrollToRequiredCard(PROFILE_CARD);
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Now playing screen is not scrollable down").isTrue();
        scrollTo(nowPlayingTitle, UP, 10);
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingSeekBar)).as("Now playing screen is not scrollable up").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate now playing profile card is displayed")
    public void validateNowPlayingProfileCardIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Now playing screen is not scrollable down").isTrue();
    }

    protected abstract void listOfValidationsForNowPlayingCardsElements(ScrollDirection scrollDirection, ContentType contentType, String... streamName);

    public abstract NowPlayingPage validateNowPlayingCardElementsFor(NowPlayingScrollableCards card, Contents... content);

    public abstract NowPlayingPage validateNumberOfContentInRecentCard(int expectedNumber);

    @Step
    public NowPlayingPage validateSeeLessButtonInEpisodeCardIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingSeeLessButton)).as("See less button is not displayed").isTrue();
        return this;
    }

    @Step
    public NowPlayingPage validateSeeMoreButtonInEpisodeCardIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingSeeMoreButton)).as("See more button is not displayed").isTrue();
        return this;
    }

    @Step("Validating Live label is present or not")
    public NowPlayingPage validateLiveLableIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingLiveLabel)).as("Live label is not displayed").isTrue();
        return this;
    }

    @Step("Validating Play Live label is present or not")
    public NowPlayingPage validatePlayLiveLabelIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingPlayLiveLabel)).as("Play Live label is not displayed").isTrue();
        return this;
    }

    @Step("Validating Play Live label is present or not")
    public NowPlayingPage validateLiveLabelChangeToPlayLive() {
        stopStreamPlaying();
        assertThat(isElementDisplayed(nowPlayingPlayLiveLabel, Duration.ofSeconds(config().waitLongTimeoutSeconds()))).as("Play live is not visible").isTrue();
        return this;
    }

    public abstract NowPlayingPage validateDateAndTimeDisplayedInEpisodeCard();

    public abstract NowPlayingPage validateAudioVolumeLessThanMaximum(int currentVolume);

    public abstract NowPlayingPage validateAudioVolumeIsMaximum(int currentVolume);

    @Step("Validate stream duration is close to {expectedStreamDuration}")
    public NowPlayingPage validateStreamDurationIsCloseTo(int expectedStreamDuration) {
        int nowPlayingDuration = getFullStreamDurationInSeconds();
        assertThat(expectedStreamDuration).as("Episode durations aren't close").isCloseTo(nowPlayingDuration, Offset.offset(60));
        return this;
    }

    @Step("Verify that now playing speed button is displayed")
    public NowPlayingPage validateNowPlayingSpeedButtonIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingSpeedButton)).as("Now playing speed button is not displayed").isTrue();
        return this;
    }

    @Step("Verify that now playing speed button is not displayed")
    public NowPlayingPage validateNowPlayingSpeedButtonIsNotDisplayed() {
        assertThat(isElementNotDisplayed(nowPlayingSpeedButton)).as("Now playing speed button is displayed").isTrue();
        return this;
    }

    @Step("Validate episode is played from previous spot: {expectedStreamDuration} seconds")
    public NowPlayingPage validateEpisodePlaysFromPreviousSpot(int expectedStreamDuration) {
        long start = System.currentTimeMillis();
        int seekBarValue = getStreamTimeInSeconds(PASSED);
        long end = System.currentTimeMillis();
        assertThat(expectedStreamDuration)
                .as("Current stream duration " + seekBarValue + " does not match the previous stream duration " + expectedStreamDuration)
                .isCloseTo(seekBarValue - ((int) ((end - start) / 1000)), Offset.offset(30));
        return this;
    }

    @Step("Validate Switch stations container is displayed")
    public NowPlayingPage validateSwitchStationsContainerIsDisplayed() {
        assertThat(isElementDisplayed(switchStationsContainer)).as("Switch stations collection view is not displayed").isTrue();
        return this;
    }

    @Step("Validate that preroll is displayed")
    public NowPlayingPage validateThatPreRollIsDisplayed(Duration... timeout) {
        if (timeout.length > 0) {
            assertThat(isElementDisplayed(nowPlayingAdvertisementLabel, timeout[0]))
                    .as("Preroll ad is not playing for the station")
                    .isTrue();
        } else {
            assertThat(isElementDisplayed(nowPlayingAdvertisementLabel))
                    .as("Preroll ad is not playing for the station")
                    .isTrue();
        }
        return this;
    }

    @Step("Validate that Now Playing ad banner is displayed {expectIsDisplayed}")
    public NowPlayingPage validateNowPlayingAdBannerDisplayed(boolean expectIsDisplayed, Duration... timeout) {
        if (timeout.length > 0) {
            assertThat(isElementDisplayed(nowPlayingAdBanner, timeout[0]))
                    .as("Expected ad banner display to be " + expectIsDisplayed + " but was not")
                    .isEqualTo(expectIsDisplayed);
        } else {
            assertThat(isElementDisplayed(nowPlayingAdBanner))
                    .as("Expected ad banner display to be " + expectIsDisplayed + " but was not")
                    .isEqualTo(expectIsDisplayed);
        }
        return this;
    }

    @Step("Validate that one of the Max Ads is displayed")
    public NowPlayingPage validateOneOfTheMaxBannersIsDisplayed() {
        assertThat(isElementDisplayed(nowPlayingAdBanner) || isElementDisplayed($(By.id(AD_BANNER_LOCATOR))))
                .as("Expected ad banner is not displayed")
                .isTrue();
        return this;
    }

    /* --- Helper Methods --- */

    @Step("Open stream profile page from Now Playing page")
    public ContentProfilePage goToStreamProfile() {
        clickOnElement(nowPlayingTitle);
        return contentProfilePage.waitUntilPageReady();
    }

    public boolean isRewindButtonIsDisplayed() {
        return isElementDisplayed(nowPlayingRewindButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }

    public boolean isPlayButtonIsDisplayed() {
        return isElementDisplayed(nowPlayingPlayButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }

    public boolean isOnNowPlayingPage(Duration... timeout) {
        closePermissionPopupsIfDisplayed();
        return (timeout.length > 0) ? isElementDisplayed(nowPlayingSeekBar, Duration.ofSeconds((int) timeout[0].toSeconds())) : isElementDisplayed(nowPlayingSeekBar, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()));
    }

    public boolean isShareButtonIsDisplayed(Duration... timeout) {
        closePermissionPopupsIfDisplayed();
        return (timeout.length > 0) ? isElementDisplayed(nowPlayingShareButton, Duration.ofSeconds((int) timeout[0].toSeconds())) : isElementDisplayed(nowPlayingShareButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
    }

    public boolean isPlaybackSpeedTooltipPopUpDisplayed() {
        return isElementDisplayed(nowPlayingPlaybackSpeedTooltipPopUp, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
    }

    public abstract NowPlayingPage validateStateOfSleepTimer(boolean isSleepTimerSet);

    public abstract boolean isFavoriteIconFilled();

    public ContentType getNowPlayingContentType() {
        if (getElementText(nowPlayingTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds())).contains("Custom Stream") || getElementText(nowPlayingTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds())).contains("http")) {
            return CUSTOM_URL;
        } else if (isElementNotDisplayed(nowPlayingSpeedButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            return LIVE_STATION;
        } else if (isElementDisplayed(nowPlayingSpeedButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            return PODCAST;
        } else if (getElementText(nowPlayingTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds())).contains("Game Replays") && isElementDisplayed(nowPlayingSpeedButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            return GAME_REPLAY;
        } else if (isElementDisplayed(nowPlayingStopButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            return OWNED_AND_OPERATED;
        } else if (getElementText(nowPlayingTitle).contains(ERROR.getNowPlayingStatusValue())) {
            return STREAM_ERROR;
        }
        return null;
    }

    public NowPlayingStatus getNowPlayingStreamStatus() {
        waitUntilStreamStopOpeningAndBuffering();
        if (isElementDisplayed(nowPlayingPauseStopButton)) {
            return PLAYING;
        }
        if (isElementDisplayed(nowPlayingPlayButton)) {
            return PAUSED;
        }
        return null;
    }

    public int getStreamTimeInSeconds(NowPlayingStreamTimeType streamTimeType) {
        SelenideElement timeType = streamTimeType == PASSED ? nowPlayingTimeCounterPassed : nowPlayingTimeCounterLeft;
        String getStreamDuration = getElementNameOrLabel(timeType);
        ReporterUtil.log("Stream duration: " + getStreamDuration);
        return getDurationInSeconds(getStreamDuration);
    }

    public LocalTime getStreamTime(NowPlayingStreamTimeType streamTimeType) {
        switch (streamTimeType) {
            case LEFT -> {
                return getTimeFromElement(nowPlayingTimeCounterLeft);
            }
            case PASSED -> {
                return getTimeFromElement(nowPlayingTimeCounterPassed);
            }
            default -> throw new Error("Unexpected value: " + streamTimeType);
        }
    }

    public NowPlayingPage generateRecentsForNowPlayingPage(int recentNumber, List<Contents> contentsList, int... startIndex) {
        if (recentNumber < 1 || recentNumber > 8) {
            throw new UnsupportedOperationException("Invalid number value, should be in range 1-8");
        }
        int start = (startIndex.length > 0) ? startIndex[0] : 0;
        for (Contents content : contentsList.subList(start, start + recentNumber)) {
            deepLinksUtil.openTuneThroughDeeplink(content);
        }
        return this;
    }

    public NowPlayingPage generatePrerollForStream(Contents stream, boolean... allowAllHosts) {
        if (isAndroid()) {
            if (allowAllHosts.length > 0 && (allowAllHosts[0])) {
                allowAllHosts();
            } else {
                refreshAllowHosts();
            }
        }
        alert.handleAlertIfDisplayed(NOW_PLAYING_STREAM_ERROR_POPUP, OK_BUTTON_TEXT);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);
        nowPlayingPage.stopStreamPlaying();
        deepLinksUtil.openTuneThroughDeeplink(stream);
        if (isElementDisplayed(nowPlayingPlayButton)) {
            nowPlayingPage.tapOnPlayButton();
            nowPlayingPage.waitUntilPageReadyWithKnownContent(stream);
        }
        return nowPlayingPage;
    }

    public int getFullStreamDurationInSeconds() {
        waitUntilStreamStopOpeningAndBuffering();
        int hours = 0;
        String timePassedCounter = getElementNameOrLabel(nowPlayingTimeCounterPassed);
        String timeLeftCounter = getElementNameOrLabel(nowPlayingTimeCounterLeft);
        ReporterUtil.log("Left time counter : " + timeLeftCounter);
        ReporterUtil.log("Passed time counter : " + timePassedCounter);
        String[] timePassedCounterArray = timePassedCounter.replace(" ", "").replace("-", "").split(":");
        String[] timeLeftCounterArray = timeLeftCounter.replace(" ", "").replace("-", "").split(":");

        /* Hours/Minutes/Seconds */
        if (timeLeftCounterArray.length == 3) {
            hours = Integer.parseInt(timeLeftCounterArray[0]);
        }
        int minutes = Integer.parseInt(timeLeftCounterArray[timeLeftCounterArray.length - 2].replace(" ", "")) + Integer.parseInt(timePassedCounterArray[timePassedCounterArray.length - 2]);
        int seconds = Integer.parseInt(timeLeftCounterArray[timeLeftCounterArray.length - 1].replace(" ", "")) + Integer.parseInt(timePassedCounterArray[timePassedCounterArray.length - 1]);
        return hours * 3600 + minutes * 60 + seconds;
    }

    public abstract int getCurrentStreamTimeInSeconds();

    public String getTextValueOfNowPlayingTitle() {
        return getElementText(nowPlayingTitle);
    }

    public String getTextValueOfNowPlayingSubtitle() {
        return getElementText(nowPlayingSubtitle);
    }

    public abstract String getEpisodeCardDescriptionText();

    public String getSpeedAdjustmentValue() {
        return getElementText(nowPlayingSpeedButton, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
    }

    public boolean isRecentContentCardDisplayed() {
        return isElementDisplayed(nowPlayingRecentsCard);
    }

    public boolean isSimilarToCardDisplayed() {
        return isElementDisplayed(similarToCardTitle);
    }

    public HashMap<Integer, NowPlayingScrollableCards> getListOfCardsOnPage(Contents... content) {
        ContentType contentType = getContentType(content);
        HashMap<Integer, NowPlayingScrollableCards> mapOfCards = new HashMap<>();
        int numberOfScrolls = 0;
        Integer id = 0;
        while (numberOfScrolls < 6) {
            if (!mapOfCards.containsValue(RECENTS_CARD) && isElementDisplayed(nowPlayingRecentsCard, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, RECENTS_CARD);
                id++;
            } else if (!mapOfCards.containsValue(SIMILAR_TO_CARD) && isElementDisplayed(similarToCardTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, SIMILAR_TO_CARD);
                id++;
            } else if (!mapOfCards.containsValue(LOCAL_RADIO_CARD) && isElementDisplayed(localRadioCardTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, LOCAL_RADIO_CARD);
                id++;
            } else if (!mapOfCards.containsValue(PROFILE_CARD) && isElementDisplayed(profileCardTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, PROFILE_CARD);
                id++;
            } else if (!mapOfCards.containsValue(EPISODE_CARD)
                    && (contentType == PODCAST || contentType == PREMIUM_PODCAST || contentType == GAME_REPLAY || contentType == AUDIOBOOK)
                    && isElementDisplayed(episodeNotesTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, EPISODE_CARD);
                id++;
                break;
            } else if (!mapOfCards.containsValue(SCHEDULE_CARD)
                    && (contentType != PODCAST && contentType != PREMIUM_PODCAST && contentType != GAME_REPLAY && contentType != AUDIOBOOK)
                    && isElementDisplayed(scheduleCardTitle, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                mapOfCards.put(id, SCHEDULE_CARD);
                id++;
                break;
            }
            numberOfScrolls++;
            scroll(DOWN);
        }
        scrollTo(nowPlayingTitle, UP, numberOfScrolls);
        return mapOfCards;
    }

    public int getNowPlayingCardOrderNumber(NowPlayingScrollableCards card) {
        switch (card) {
            case RECENTS_CARD -> {
                return 0;
            }
            case SIMILAR_TO_CARD -> {
                return 1;
            }
            case LOCAL_RADIO_CARD -> {
                return 2;
            }
            case PROFILE_CARD -> {
                return 3;
            }
            case EPISODE_CARD -> {
                return 4;
            }
            case SCHEDULE_CARD -> {
                return 5;
            }
            default -> throw new Error("Invalid browsies tab type - " + card.toString());
        }
    }

    private LinkedHashMap<String, SelenideElement> getMapOfCards() {
        return new LinkedHashMap<String, SelenideElement>() {{
            put("RECENTS", nowPlayingRecentsCard);
            put("Similar to", nowPlayingSimilarToCard);
            put("Local Radio", nowPlayingLocalRadioToCard);
            put("Go to Profile", nowPlayingProfileCard);
            put("EPISODE NOTES", nowPlayingEpisodeCard);
            put("Up Next", nowPlayingScheduleCard);
        }};
    }

    public String getLabelOfFirstDisplayedNowPlayingCard() {
        return getMapOfCards()
                .entrySet()
                .stream()
                .filter(card -> isElementDisplayed(card.getValue()))
                .findFirst()
                .get()
                .getKey();
    }

    @Step("Wait until preroll ad disappear if it's displayed")
    public NowPlayingPage waitUntilPreRollAdDisappearIfDisplayed() {
        try {
            if (isElementDisplayed(nowPlayingAdvertisementLabel, Duration.ofSeconds(config().pageReadyTimeoutSeconds()))) {
                takeScreenshot();
                ReporterUtil.log("Advertisement label is displayed");
                waitTillElementDisappear(nowPlayingAdvertisementLabel, Duration.ofSeconds(65));
            } else if (isElementDisplayed(nowPlayingTimeCounterLeft, Duration.ofSeconds(config().waitShortTimeoutSeconds()))
                    && isElementNotDisplayed(nowPlayingSpeedButton, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
                takeScreenshot();
                ReporterUtil.log("Preroll is displayed");
                waitTillElementDisappear(nowPlayingTimeCounterLeft, Duration.ofSeconds(90));
            }
        } catch (Throwable exception) {
            ReporterUtil.log("Preroll could be still available");
        }
        return this;
    }

    public abstract NowPlayingPage waitUntilStreamStopOpeningAndBuffering(Duration... timeout);

    public abstract NowPlayingPage waitUntilStreamStartOpeningOrBuffering(Duration... timeout);

    @Step("Wait for episode to stop")
    public NowPlayingPage waitEpisodeStopped() {
        waitTillVisibilityOfElement(nowPlayingPlayButton);
        return this;
    }

    @Step
    public NowPlayingPage waitEpisodeChanged(String nameOfCurrentEpisode) {
        waitTextOfElementToDisappear(nowPlayingSubtitle, nameOfCurrentEpisode, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        return this;
    }

    @Step
    public NowPlayingPage waitStreamIsOpening(int... millisecondsTimeout) {
        if (millisecondsTimeout.length > 0) {
            waitTillElementWithTextLoaded(nowPlayingSubtitle, OPENING.getNowPlayingStatusValue(), Duration.ofMillis(millisecondsTimeout[0]));
        } else {
            waitTillElementWithTextLoaded(nowPlayingSubtitle, OPENING.getNowPlayingStatusValue());
        }
        return this;
    }

    @Step("Wait for stream playback")
    public NowPlayingPage waitForTheStreamPlayback(Contents... content) {
        ContentType requiredContentType = getContentType(content);
        switch (requiredContentType) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> waitVisibilityOfElement(nowPlayingTimeCounterLeft, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL, OWNED_AND_OPERATED -> waitVisibilityOfElement(nowPlayingPauseStopButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
            default -> throw new Error("Unhandled contentType " + requiredContentType);
        }
        return this;
    }

    @Step("Wait for stream playback")
    public NowPlayingPage waitForTheStreamPlayback(ContentType contentType) {
        switch (contentType) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> waitTillVisibilityOfElement(nowPlayingTimeCounterLeft, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL, OWNED_AND_OPERATED -> waitTillVisibilityOfElement(nowPlayingLiveLabel, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
            default -> throw new Error("Unhandled contentType " + contentType);
        }
        return this;
    }

    @Step
    public void waitForSliderPosition(ScrubberPosition scrubberPosition, long timeout, boolean tapFastForward, int topicDuration) {
        int requiredTimeInSeconds = 0;
        long start = System.currentTimeMillis();
        long end = start + timeout * 1000;
        switch (scrubberPosition) {
            case BEGINNING -> {
                requiredTimeInSeconds = BEGINNING.getScrubberPositionValue();
            }
            case NEAR_MIDDLE -> {
                requiredTimeInSeconds = (topicDuration / 2) - NEAR_MIDDLE.getScrubberPositionValue();
            }
            case END -> {
                requiredTimeInSeconds = topicDuration - END.getScrubberPositionValue();
            }
            case DEFAULT -> {
                requiredTimeInSeconds = topicDuration - DEFAULT.getScrubberPositionValue();
            }
            default -> throw new Error("Unhandled scrubber Position " + scrubberPosition);
        }
        while (getCurrentStreamTimeInSeconds() < requiredTimeInSeconds) {
            if (System.currentTimeMillis() > end) {
                ReporterUtil.log("Now playing slider did not move to expected position " + scrubberPosition.getScrubberPositionValue() + " after \"" + timeout + "\" seconds.");
            }
            /* Tap (30 >) button to skip 30 sec ahead to land on expected slider position */
            if (tapFastForward) {
                tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD);
            }
        }
        ReporterUtil.log("Now player slider position is " + getCurrentStreamTimeInSeconds());
    }

    @Step("Wait for slider position {requiredTimeInSeconds} during {timeout}")
    public NowPlayingPage waitForSliderPosition(int requiredTimeInSeconds, Duration timeout, boolean tapFastForward) {
        int currentScrubberPosition = getCurrentStreamTimeInSeconds();
        long start = System.currentTimeMillis();
        long end = start + timeout.toMillis();
        int requiredScrubberPosition = currentScrubberPosition + requiredTimeInSeconds;
        while (currentScrubberPosition < requiredScrubberPosition) {

            /* Tap (30 >) button to skip 30 sec ahead to land on expected slider position */
            if (tapFastForward) {
                tapNowPlayingRewindOrFastForwardButton(FAST_FORWARD);
            }

            if (System.currentTimeMillis() > end) {
                throw new RuntimeException("Now playing slider (" + currentScrubberPosition + ") did not move to expected position " + requiredTimeInSeconds + " after \"" + timeout.getSeconds() + "\" seconds.");
            }

            currentScrubberPosition = getCurrentStreamTimeInSeconds();
        }
        ReporterUtil.log("Now player slider position is " + currentScrubberPosition);
        return this;
    }

    protected ContentType getContentType(Contents... content) {
        if (content.length > 0 && !(content[0].getStreamType()).equals("")) {
            return getContentTypeValue(content[0].getStreamType());
        } else {
            return getNowPlayingContentType();
        }
    }

    public static synchronized void setFirstLaunchValue(Long threadId, Boolean firstLaunch) {
        firstLaunchMap.put(threadId, firstLaunch);
    }

    public static synchronized Boolean getFirstLaunchValue() {
        if (firstLaunchMap.get(Thread.currentThread().getId()) == null) {
           return true;
        }
        return firstLaunchMap.get(Thread.currentThread().getId());
    }

    public HashMap<String, SelenideElement> nowPlayingPageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        switch (getNowPlayingContentType()) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "8", nowPlayingFastForwardButton);
                elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "7", nowPlayingRewindButton);
                if (getNowPlayingStreamStatus() == PLAYING) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingPauseButton);
                } else {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingPlayButton);
                }
            }
            case OWNED_AND_OPERATED -> {
                if (getNowPlayingStreamStatus() == PLAYING) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingStopButton);
                } else {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingPlayButton);
                }
            }
            default -> {
                if (getNowPlayingStreamStatus() == PLAYING) {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingPauseButton);
                } else {
                    elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "4", nowPlayingPlayButton);
                }
            }
        }
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "1", nowPlayingTitle);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "2", nowPlayingSubtitle);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "3", nowPlayingStationLogo);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "5", nowPlayingFavoriteIcon);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "6", nowPlayingSeekBar);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "10", nowPlayingMoreOptions);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "11", nowPlayingMinimiseButton);
        return elementsMap;
    }

    public abstract String getAudioInfo();

    public abstract int getMaxVolumeFromAudioInfo(String audioInfo);

    public abstract int getCurrentVolumeFromAudioInfo();

    public abstract void waitUntilAlarmGoesOff(Duration timeout);

    public enum NowPlayingStatus {
        OPENING("Opening"),
        PLAYING(""),
        PAUSED(""),
        STOPPED(""),
        CONNECTING("Connecting"),
        BUFFERING("uffer"),
        ERROR("error occurred"),
        NO_CONNECTION("no connection"),
        CONTENT_START_SHORTLY("Your content will start shortly");

        private String nowPlayingStatusValue;

        private NowPlayingStatus(String nowPlayingStatusValue) {
            this.nowPlayingStatusValue = nowPlayingStatusValue;
        }

        public String getNowPlayingStatusValue() {
            return nowPlayingStatusValue;
        }

        public static ContentProvider.ContentType getNowPlayingStatus(final String nowPlayingStatusValue) {
            List<ContentProvider.ContentType> nowPlayingStatusList = Arrays.asList(ContentProvider.ContentType.values());
            return nowPlayingStatusList.stream().filter(eachContent -> eachContent.toString().equals(nowPlayingStatusValue))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return nowPlayingStatusValue;
        }

    }

    public enum NowPlayingStreamTimeType {
        PASSED,
        LEFT
    }

    public enum NowPlayingMediaControlButtonsType {
        REWIND,
        FAST_FORWARD
    }

    public enum NowPlayingPageButtons {
        FAVORITE, UNFAVORITE
    }

    public enum ScrubberPosition {
        BEGINNING(10),
        NEAR_MIDDLE(20),
        END(5),
        DEFAULT(100);

        private int scrubberPositionValue;

        private ScrubberPosition(int scrubberPositionValue) {
            this.scrubberPositionValue = scrubberPositionValue;
        }

        public int getScrubberPositionValue() {
            return scrubberPositionValue;
        }
    }

    public enum Prompts {
        LOW_BATTERY_POPUP,
        STREAM_ERROR_POPUP
    }

    public abstract NowPlayingPage closePopUpIfDisplayed(Prompts prompt);

    public enum NowPlayingScrollableCards {
        RECENTS_CARD("RECENTS"),
        SIMILAR_TO_CARD("Similar to"),
        LOCAL_RADIO_CARD("Local Radio"),
        PROFILE_CARD("Go to Profile"),
        EPISODE_CARD("EPISODE NOTES"),
        SCHEDULE_CARD("Up Next");

        private String scrollableCardName;

        private NowPlayingScrollableCards(String scrollableCardName) {
            this.scrollableCardName = scrollableCardName;
        }

        public static NowPlayingScrollableCards getCardType(final String cardLabel) {
            List<NowPlayingScrollableCards> cardList = Arrays.asList(NowPlayingScrollableCards.values());
            return cardList.stream().filter(eachContent -> eachContent.toString().equals(cardLabel))
                    .findAny()
                    .orElse(null);
        }

        public String getScrollabelCardNameValue() {
            return scrollableCardName;
        }

        @Override
        public String toString() {
            return scrollableCardName;
        }
    }

    public boolean isPrerollDisplayed() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(nowPlayingAdvertisementLabel, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    @Step("Verify that Now Playing Station Card title is equal to {actualTitle}")
    public void validateNowPlayingStationCardTitleIsEqualTo(String actualTitle) {
        assertThat(getElementNameOrLabel(profileCardTitle).equals(actualTitle)).as("Station card title is not equal to " + actualTitle).isTrue();
    }

}
