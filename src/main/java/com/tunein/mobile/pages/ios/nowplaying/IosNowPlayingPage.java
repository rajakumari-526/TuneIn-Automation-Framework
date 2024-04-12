package com.tunein.mobile.pages.ios.nowplaying;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.alert.IosAlert;
import com.tunein.mobile.pages.common.nowplaying.MiniPlayerPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.AppiumBy;
import org.assertj.core.data.Offset;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.SelenideAppium.$x;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.alert.Alert.CLOSE_BUTTON_TEXT;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStatus.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStreamTimeType.PASSED;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.SET_ALARM;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.CLOSE;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.OK;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class IosNowPlayingPage extends NowPlayingPage {

    public static final String NOW_PLAYING_STREAM_ERROR_POPUP = "An error occurred with this stream";

    private static final String NOW_PLAYING_LOW_BATTERY = "Low Battery";

    protected SelenideElement nowPlayingEpisodeCardTimeLabel = $x("//XCUIElementTypeOther[./*[(@value='See More') or (@value='See Less')]]/preceding-sibling::XCUIElementTypeStaticText[contains(@label,':')]").as("Time label in Episode card");

    protected SelenideElement nowPlayingEpisodeCardDateLabel = $x("//XCUIElementTypeOther[./*[(@value='See More') or (@value='See Less')]]/preceding-sibling::XCUIElementTypeStaticText[contains(@label,'/')]").as("Date label in episode card");

    protected SelenideElement episodeNotesDescription = $x("(//*[@name=\"EPISODE NOTES:\"]/following-sibling:: XCUIElementTypeStaticText[@label !=''])[2]").as("Episode notes description");
    
    protected SelenideElement episodeNotesEpisodeName = $x("(//*[@name=\"EPISODE NOTES:\"]/following-sibling:: XCUIElementTypeStaticText[@label !=''])[1]").as("Episode notes name");

    protected SelenideElement nowPlayingCommercialFreeLabel = $(AppiumBy.iOSNsPredicateString("label == 'Try a commercial free version'")).as("Commercial free label");
    
    private String contentWithTextLocator = "label == '%s'";

    private String contentInCardLocator = ".//*[@name='tileCellViewIdentifier']";

    private String cardTitleSuffix = "/preceding-sibling::XCUIElementTypeOther[1]";

    private String profileCardTitleSuffix = ".//XCUIElementTypeStaticText[@name='%s']";

    private String contentByNumberLocator = ".//XCUIElementTypeCell[@index=%s]";

    @Step("Wait until NowPlaying page is opened")
    @Override
    public NowPlayingPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        hidePlaybackSpeedTooltipPopUpIfPresent();
        if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
        setFirstLaunchValue(getThreadId(), false);
        waitForTheStreamPlayback();
        return this;
    }

    @Step("Wait until NowPlaying page for {contentType} is opened ")
    @Override
    public NowPlayingPage waitUntilPageReadyWithKnownContentType(ContentType contentType) {
        closePermissionPopupsIfDisplayed();
        switch (contentType) {
            case LIVE_STATION -> {
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PODCAST -> {
                hidePlaybackSpeedTooltipPopUpIfPresent();
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> hidePlaybackSpeedTooltipPopUpIfPresent();
            case PREMIUM_LIVE_STATION, OWNED_AND_OPERATED, CUSTOM_URL -> {
            }
            default -> throw new Error("Invalid content type");
        }
        setFirstLaunchValue(getThreadId(), false);
        waitForTheStreamPlayback(contentType);
        return this;
    }

    @Step("Wait until page ready with known content {content}")
    @Override
    public NowPlayingPage waitUntilPageReadyWithKnownContent(Contents content) {
        closePermissionPopupsIfDisplayed();
        ContentType contentType = getContentType(content);
        switch (contentType) {
            case LIVE_STATION, SHOW -> {
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PODCAST -> {
                hidePlaybackSpeedTooltipPopUpIfPresent();
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> hidePlaybackSpeedTooltipPopUpIfPresent();
            case PREMIUM_LIVE_STATION, OWNED_AND_OPERATED, CUSTOM_URL -> {
            }
            default -> throw new Error("Invalid content type");
        }
        setFirstLaunchValue(getThreadId(), false);
        waitForTheStreamPlayback(content);
        return this;
    }

    @Step
    @Override
    public NowPlayingPage waitUntilMinimiseButtonIsClickable() {
        closePermissionPopupsIfDisplayed();
        waitUntilStreamStopOpeningAndBuffering();
        closePopUpIfDisplayed(Prompts.STREAM_ERROR_POPUP);
        clickOnElement(nowPlayingMinimiseButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Wait until content will be partially buffered")
    @Override
    public NowPlayingPage waitUntilContentPartiallyBuffered() {
        long start = System.currentTimeMillis();
        while (getContentBufferedValue() <= 0) {
            if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                throw new Error("Content is not buffered" + config().waitCustomTimeoutMilliseconds() + " ms ");
            }
        }
        return this;
    }

    @Step("Waiting for Now Playing page to load without waiting for preroll to finish")
    @Override
    public NowPlayingPage waitUntilPageReadyLiteVersion() {
        closePermissionPopupsIfDisplayed();
        boolean isStreamTestEnabled = config().isAppiumStreamTestEnabled();
        SelenideElement element = isStreamTestEnabled ? nowPlayingMinimiseButton : nowPlayingSeekBar;
        waitTillVisibilityOfElement(element, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Tap on favorite icon. Set favorite state: {addToFavorite}")
    @Override
    public NowPlayingPage tapOnFavoriteIcon(boolean addToFavorite, NowPlayingFavoriteDialog.FavoriteContentType... typeOfFavoriteContent) {
        if (typeOfFavoriteContent != null && typeOfFavoriteContent.length > 0) {
            clickOnElement(nowPlayingFavoriteIcon);
            nowPlayingFavoriteDialog
                    .waitUntilPageReady()
                    .tapOnFavoriteContent(addToFavorite, typeOfFavoriteContent[0]);
        } else {
            if (getElementNameOrLabel(nowPlayingFavoriteIcon).contains("un") != addToFavorite) {
                clickOnElement(nowPlayingFavoriteIcon);
            }
        }
        return this;
    }

    @Step("Closing {prompt} prompt if displayed")
    @Override
    public NowPlayingPage closePopUpIfDisplayed(Prompts prompt) {
        switch (prompt) {
            case LOW_BATTERY_POPUP -> {
                ReporterUtil.log("Closing Low Battery prompt if displayed");
                alert.handleAlertIfDisplayed(NOW_PLAYING_LOW_BATTERY, CLOSE_BUTTON_TEXT);
                if (config().isAppiumStreamTestEnabled()) {
                    IosAlert.handleSimpleAlertIfPresent(CLOSE);
                }
                return this;
            }
            case STREAM_ERROR_POPUP -> {
                ReporterUtil.log("Closing Low Battery prompt if displayed");
                alert.handleAlertIfDisplayed(NOW_PLAYING_STREAM_ERROR_POPUP, OK_BUTTON_TEXT);
                // Sometimes Stream Error prompt is not dismissed with handleAlertIfDisplayed() method
                // adding IosAlert.handleSimpleAlertIfPresent(OK) as an additional guard
                if (config().isAppiumStreamTestEnabled()) {
                    IosAlert.handleSimpleAlertIfPresent(OK);
                }
                return this;
            }
            default -> throw new Error("Invalid system prompt type input " + prompt);
        }
    }

    @Step("Minimize now playing screen")
    @Override
    public MiniPlayerPage minimizeNowPlayingScreen() {
        clickOnElement(nowPlayingMinimiseButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return miniPlayerPage.waitUntilPageReady();
    }

    @Step("Hide playback speed tooltip if present")
    @Override
    public NowPlayingPage hidePlaybackSpeedTooltipPopUpIfPresent() {
        if (isElementDisplayed(nowPlayingPlaybackSpeedTooltipPopUp, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            try {
                clickOnElement(nowPlayingPlaybackSpeedTooltipPopUp);
            } catch (Throwable e) {
                ReporterUtil.log("Playback speed tooltip disappeared automatically");
            }
        }
        return this;
    }

    @Step("Move scrubber to position {scrubberPosition}")
    @Override
    public NowPlayingPage moveScrubberSliderToSpecificPosition(ScrubberPosition scrubberPosition, int topicDuration) {
        int requiredTimeInSeconds = 0;
        switch (scrubberPosition) {
            case BEGINNING -> {
                requiredTimeInSeconds = BEGINNING.getScrubberPositionValue();
            }
            case END -> {
                requiredTimeInSeconds = topicDuration - END.getScrubberPositionValue();
            }
            case DEFAULT -> {
                requiredTimeInSeconds = topicDuration - DEFAULT.getScrubberPositionValue();
            }
            default -> throw new Error("Invalid scrubber position");
        }
        int sliderWidth = nowPlayingSeekBar.getSize().getWidth();
        int ySliderPosition = nowPlayingSeekBar.getLocation().getY() + nowPlayingSeekBar.getSize().height / 2;
        int spaceBeforeSlider = ((getAppiumDriver().manage().window().getSize().getWidth() - sliderWidth) / 2);
        double currentSeconds = getStreamTimeInSeconds(PASSED);
        double secondsInPixel = (double) topicDuration / (sliderWidth);
        int xStartPosition = (int) (currentSeconds / secondsInPixel) + spaceBeforeSlider;
        int xEndPosition = (int) (requiredTimeInSeconds / secondsInPixel) + spaceBeforeSlider;
        ReporterUtil.log("Move scrubber to " + scrubberPosition + " position");
        swipe(new Point(xStartPosition, ySliderPosition), new Point(xEndPosition, ySliderPosition));
        waitUntilStreamStopOpeningAndBuffering();
        return this;
    }

    //TODO Update when Page Object model for More Options dialog will be available
    @Step
    @Override
    public void openEditSleepTimerDialog() {
        clickOnElement(nowPlayingSleepTimerIcon);
        // nowPlayingOptionsDialog.tapEditSleepTimerButton();
    }

    @Step("Close 'Got it' prompt")
    @Override
    public NowPlayingPage closeGotItPrompt() {
        closePermissionPopupsIfDisplayed();
        clickOnElement(gotItPromptClose);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step
    @Override
    public NowPlayingPage setAlarmTimeFlow(int minutesAhead) {
        tapOnMoreOptionsButton();
        nowPlayingMoreOptionsDialog.tapOnNowPlayingOptionsButton(SET_ALARM);
        nowPlayingSetAlarmDialog
                .turnOnAlarm()
                .setAlarmInCoupleMinutes(minutesAhead)
                .tapOnSaveButton();
        return this;
    }

    @Step("Click on {direction} station in Switch container")
    @Override
    public NowPlayingPage tapOnSwitchContainerStation(SwipeDirection direction) {
        closePermissionPopupsIfDisplayed();
        clickOnElement(listOfSwitchContainerStations.get(1));
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate that Nowplaying Page metadata is not empty for {contentType}")
    @Override
    public NowPlayingPage validateNowPlayingMetadataIsNotEmpty(ContentType contentType) {
        getSoftAssertion().assertThat(getTextValueOfNowPlayingTitle()).as("Now Playing title is empty").isNotEmpty();
        getSoftAssertion().assertThat(getTextValueOfNowPlayingSubtitle()).as("Now Playing subtitle is empty").isNotEmpty();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateNowPlayingSeekBarChangedPosition(ScrubberPosition scrubberPosition, int streamDuration) {
        int shouldContainValue = 0;
        switch (scrubberPosition) {
            case DEFAULT -> {
                shouldContainValue = streamDuration - DEFAULT.getScrubberPositionValue();
            }
            case BEGINNING -> {
                shouldContainValue = BEGINNING.getScrubberPositionValue();
            }
            default -> throw new Error("Invalid scrubber position");

        }
        int passedTimeCounter = getStreamTimeInSeconds(PASSED);
        getSoftAssertion().assertThat(passedTimeCounter).as("Passed time is incorrect ").isCloseTo(shouldContainValue, Offset.offset(30));
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate new episode is opened")
    @Override
    public NowPlayingPage validateNowPlayingOpenNewEpisode(String previousEpisode) {
        int passedTimeCounter = getCurrentStreamTimeInSeconds();
        String currentNameOfEpisode = getTextValueOfNowPlayingSubtitle();
        getSoftAssertion().assertThat(passedTimeCounter).as("Passed time is incorrect").isCloseTo(0, Offset.offset(50));
        getSoftAssertion().assertThat(previousEpisode).as("New Episode " + currentNameOfEpisode + " is not opened").isNotEqualTo(currentNameOfEpisode);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateStreamErrorIsDisplayed() {
        assertThat(getElementText(nowPlayingTimeCounterPassed))
                .as("Stream error message failed to display")
                .contains(ERROR.getNowPlayingStatusValue());
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateStreamIsStopped() {
        waitUntilStreamStopOpeningAndBuffering();
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingPlayButton)).as("Play button is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingPauseStopButton)).as("Pause/Stop button is present").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    protected void listOfValidationsForNowPlayingCardsElements(ScrollDirection scrollDirection, ContentType contentType, String... streamName) {
        int numberOfScrolls = 0;
        List<NowPlayingScrollableCards> listOfCards = new ArrayList<>();
        while (numberOfScrolls < 6) {
            if (!listOfCards.contains(RECENTS_CARD) && isElementDisplayed(nowPlayingRecentsCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(RECENTS_CARD);
                int count = nowPlayingRecentsCard.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(count > 0).as("Recent card is empty").isTrue();
            } else if (!listOfCards.contains(SIMILAR_TO_CARD) && isElementDisplayed(nowPlayingSimilarToCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(SIMILAR_TO_CARD);
                int count = nowPlayingSimilarToCard.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(isElementDisplayed(similarToCardTitle)).as("Similar to card title is not displayed").isTrue();
                if (streamName.length > 0) {
                    getSoftAssertion().assertThat(getElementName(similarToCardTitle)).as("Similar to card title has wrong value").isEqualTo("Similar to " + streamName[0]);
                }
                getSoftAssertion().assertThat(count > 0).as("Similar to card is empty").isTrue();
            } else if (!listOfCards.contains(LOCAL_RADIO_CARD) && isElementDisplayed(nowPlayingLocalRadioToCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(LOCAL_RADIO_CARD);
                int count = nowPlayingLocalRadioToCard.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(isElementDisplayed(localRadioCardTitle)).as("Local Radio card title is not displayed").isTrue();
                getSoftAssertion().assertThat(getElementName(localRadioCardTitle)).as("Local Radio card title has wrong value").isEqualTo("Local Radio");
                getSoftAssertion().assertThat(count > 0).as("Local Radio card is empty").isTrue();
            } else if (!listOfCards.contains(PROFILE_CARD) && isElementDisplayed(nowPlayingProfileCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(PROFILE_CARD);
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Go to profile button is not displayed").isTrue();
                if (streamName.length > 0) {
                    getSoftAssertion().assertThat(isElementDisplayed(nowPlayingProfileCard.$(By.xpath(String.format(profileCardTitleSuffix, streamName[0]))))).as("Go to profile title is not displayed").isTrue();
                }
            } else if ((contentType == PODCAST || contentType == PREMIUM_PODCAST || contentType == GAME_REPLAY || contentType == AUDIOBOOK) && !listOfCards.contains(EPISODE_CARD) && isElementDisplayed(nowPlayingEpisodeCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(EPISODE_CARD);
                getSoftAssertion().assertThat(isElementDisplayed(episodeNotesTitle)).as("Episodes card is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingSeeMoreButton)).as("See more button is not displayed").isTrue();
                break;
            } else if ((contentType != PODCAST && contentType != PREMIUM_PODCAST && contentType != GAME_REPLAY && contentType != AUDIOBOOK) && !listOfCards.contains(SCHEDULE_CARD) && isElementDisplayed(nowPlayingScheduleCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                listOfCards.add(SCHEDULE_CARD);
                getSoftAssertion().assertThat(isElementDisplayed(scheduleCardTitle)).as("Schedule card title is not displayed").isTrue();
                break;
            }
            numberOfScrolls++;
            scroll(scrollDirection);
        }
        getSoftAssertion().assertAll();
    }

    @Step
    @Override
    public NowPlayingPage validateNowPlayingCardElementsFor(NowPlayingScrollableCards card, Contents... content) {
       SelenideElement cardElement = scrollToRequiredCard(card);
        switch (card) {
            case SIMILAR_TO_CARD -> {
                int count = cardElement.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(isElementDisplayed(similarToCardTitle)).as("Similar to card title is not displayed").isTrue();
                if (content.length > 0) {
                    getSoftAssertion().assertThat(getElementName(similarToCardTitle)).as("Similar to card title has wrong value").isEqualTo("Similar to " + content[0].getStreamName());
                }
                getSoftAssertion().assertThat(count > 0).as("Similar to card is empty").isTrue();
            }
            case RECENTS_CARD -> {
                int count = cardElement.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(count > 0).as("Recent card is empty").isTrue();
            }
            case LOCAL_RADIO_CARD -> {
                int count = cardElement.$$(By.xpath(contentInCardLocator)).size();
                getSoftAssertion().assertThat(isElementDisplayed(localRadioCardTitle)).as("Local Radio card title is not displayed").isTrue();
                getSoftAssertion().assertThat(getElementName(localRadioCardTitle)).as("Local Radio card title has wrong value").isEqualTo("Local Radio");
                getSoftAssertion().assertThat(count > 0).as("Local Radio card is empty").isTrue();
            }
            case EPISODE_CARD -> {
                getSoftAssertion().assertThat(isElementDisplayed(episodeNotesTitle)).as("Episodes card is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingSeeMoreButton) || isElementDisplayed(nowPlayingSeeLessButton)).as("See more button is not displayed").isTrue();
            }
            case SCHEDULE_CARD -> {
                getSoftAssertion().assertThat(isElementDisplayed(scheduleCardTitle)).as("Schedule card title is not displayed").isTrue();
            }
            case PROFILE_CARD -> {
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Go to profile button is not displayed").isTrue();
                if (content.length > 0) {
                    getSoftAssertion().assertThat(isElementDisplayed(nowPlayingProfileCard.$(By.xpath(String.format(profileCardTitleSuffix, content[0].getStreamName()))))).as("Go to profile title is not displayed").isTrue();
                }
            }
            default -> throw new Error("Invalid scrollable card");
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateNumberOfContentInRecentCard(int expectedNumber) {
        SelenideElement recentCard = scrollToRequiredCard(RECENTS_CARD);
        int count = recentCard.$$(By.xpath(contentInCardLocator)).size();
        assertThat(count).as("Contnet number in Recent card is not equal to " + expectedNumber).isEqualTo(expectedNumber);
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateDateAndTimeDisplayedInEpisodeCard() {
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCardTimeLabel)).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCardDateLabel)).as("Episode card date is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Override
    public NowPlayingPage validateAudioVolumeLessThanMaximum(int currentVolume) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Override
    public NowPlayingPage validateAudioVolumeIsMaximum(int currentVolume) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Step("Depending on content type and play state, validates live button and timers below seek bar")
    @Override
    public NowPlayingPage validateSeekBarLooksAsExpected(Contents... content) {
        ContentType contentType = getContentType(content);
        return validateSeekBarLooksAsExpected(contentType);
    }

    @Step("Depending on content type and play state, validates live button and timers")
    @Override
    public NowPlayingPage validateSeekBarLooksAsExpected(ContentType contentType) {
        NowPlayingStatus nowPlayingStatus = getNowPlayingStreamStatus();
        switch (contentType) {
            case PODCAST, PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> {
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingTimeCounterLeft))
                        .as("Counter time left is not displayed in " + nowPlayingStatus.name() + " state")
                        .isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingTimeCounterPassed))
                        .as("Counter time passed is not displayed in " + nowPlayingStatus.name() + " state")
                        .isTrue();
            }
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL, OWNED_AND_OPERATED -> {
                switch (nowPlayingStatus) {
                    case PLAYING -> {
                        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingLiveLabel))
                                .as("Live label is not displayed in " + nowPlayingStatus.name() + " state")
                                .isTrue();
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingTimeCounterPassed))
                                .as("Counter time passed is displayed in " + nowPlayingStatus.name() + " state")
                                .isTrue();
                    }
                    case PAUSED, STOPPED -> {
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingLiveLabel))
                                .as("Live label is displayed in " + nowPlayingStatus.name() + " state")
                                .isTrue();
                        getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingTimeCounterPassed))
                                .as("Counter time passed is displayed in " + nowPlayingStatus.name() + " state")
                                .isTrue();
                    }
                    default -> throw new Error("Invalid NowPlaying status");
                }
            }
            case LIVE_EVENT -> {
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingLiveLabel))
                        .as("Live label is not displayed in " + nowPlayingStatus.name() + " state")
                        .isTrue();
                getSoftAssertion().assertThat(isElementNotDisplayed(nowPlayingTimeCounterPassed))
                        .as("Counter time passed is displayed in " + nowPlayingStatus.name() + " state")
                        .isTrue();
            }
            default -> throw new Error("Invalid content type");
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Verify display of sleep timer is set as {expectedSleepTimerState}")
    @Override
    public NowPlayingPage validateStateOfSleepTimer(boolean isSleepTimerSet) {
        if (!isSleepTimerSet) {
            assertThat(isElementNotDisplayed(nowPlayingEditSleepTimerIcon)).as("Sleep timer state is not in the 'unset' state").isTrue();
        } else {
            assertThat(isElementDisplayed(nowPlayingEditSleepTimerIcon)).as("Sleep timer state is not in the 'set' state").isTrue();
        }
        return this;
    }

    @Override
    public boolean isFavoriteIconFilled() {
        return getElementNameOrLabel(nowPlayingFavoriteIcon).contains("Un");
    }

    @Override
    public int getCurrentStreamTimeInSeconds() {
        waitUntilStreamStopOpeningAndBuffering();
        double value = Integer.parseInt(getElementText(nowPlayingSeekBar).replace(".0", "").replace("%", ""));
        int rawValue = (int) Math.round(getFullStreamDurationInSeconds() * (value / 100));
        return rawValue;
    }

    @Override
    public String getEpisodeCardDescriptionText() {
        return getElementText(episodeNotesDescription);
    }

    @Step("Open content with name {contentName} in card {card}")
    @Override
    public NowPlayingPage openContentWithNameInCard(String contentName, NowPlayingScrollableCards card) {
        switch (card) {
            case SIMILAR_TO_CARD, LOCAL_RADIO_CARD -> {
                SelenideElement requiredCard = scrollToRequiredCard(card);
                By locator = AppiumBy.iOSNsPredicateString(String.format(contentWithTextLocator, contentName));
                clickOnElement(swipeToElement(locator, 5, LEFT, requiredCard));
            }
            default -> throw new Error("Invalid card type - " + card.toString());
        }
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Open content by index {contentNumber} in card {card}")
    @Override
    public NowPlayingPage openContentByNumberInCard(int contentNumber, NowPlayingScrollableCards card) {
        switch (card) {
            case RECENTS_CARD, SIMILAR_TO_CARD -> {
                SelenideElement requiredCard = scrollToRequiredCard(card);
                SelenideElement requiredContent = requiredCard.$(By.xpath(String.format(contentByNumberLocator, contentNumber - 1)));
                clickOnElement(swipeToElement(requiredContent, 5, LEFT, requiredCard));
            }
            case LOCAL_RADIO_CARD -> {
                if (contentNumber > 0 && contentNumber <= 3) {
                    SelenideElement requiredCard = scrollToRequiredCard(card);
                    SelenideElement requiredContent = requiredCard.$(By.xpath(String.format(contentByNumberLocator, contentNumber - 1)));
                    clickOnElement(requiredContent);
                } else {
                    throw new Error("In iOS Local radio card has only 3 content options");
                }
            }
            default -> throw new Error("Invalid card type - " + card.toString());
        }
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Swipe switch container {direction}")
    @Override
    public NowPlayingPage swipeStationInSwitchContainer(SwipeDirection direction) {
        closePermissionPopupsIfDisplayed();
        SelenideElement station = listOfSwitchContainerStations.get(1);
        swipeElement(direction, config().nowPlayingSwipeDistance(), station);
        return this;
    }

    @Step("Waiting for the \"Opening...\", \"Connecting...\" and \"Buffering\" to disappear in the Now Playing screen")
    @Override
    public NowPlayingPage waitUntilStreamStopOpeningAndBuffering(Duration... timeout) {
        Duration requiredTimeout = timeout.length > 0 ? timeout[0] : Duration.ofSeconds(config().waitShortTimeoutSeconds());
        if (isElementDisplayed(nowPlayingTimeCounterPassed, requiredTimeout)) {
            waitTillAllTextsOfElementDisappear(
                    nowPlayingTimeCounterPassed,
                    new String[]{CONNECTING.getNowPlayingStatusValue(), OPENING.getNowPlayingStatusValue(), BUFFERING.getNowPlayingStatusValue()},
                    Duration.ofSeconds(config().waitLongTimeoutSeconds())
            );
        }
        return this;
    }

    @Step("Waiting for the \"Opening...\", \"Connecting...\" and \"Buffering\" to appear in the Now Playing screen")
    @Override
    public NowPlayingPage waitUntilStreamStartOpeningOrBuffering(Duration... timeout) {
        Duration requiredTimeout = timeout.length > 0 ? timeout[0] : Duration.ofSeconds(config().waitShortTimeoutSeconds());
        if (isElementDisplayed(nowPlayingTimeCounterPassed, requiredTimeout)) {
            waitTillOneOfTextsOfElementAppear(
                    nowPlayingTimeCounterPassed,
                    new String[]{CONNECTING.getNowPlayingStatusValue(), OPENING.getNowPlayingStatusValue(), BUFFERING.getNowPlayingStatusValue()},
                    Duration.ofSeconds(config().waitShortTimeoutSeconds())
            );
        }
        return this;
    }

    @Override
    public String getAudioInfo() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Override
    public int getMaxVolumeFromAudioInfo(String audioInfo) {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    @Override
    public int getCurrentVolumeFromAudioInfo() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }

    public int getContentBufferedValue() {
        return Integer.parseInt(nowPlayingSeekBarContainer.getAttribute("value").replace("%", ""));
    }

    @Step("Wait until alarm goes off")
    @Override
    public void waitUntilAlarmGoesOff(Duration timeout) {
        waitTillVisibilityOfElement(nowPlayingAlarm, Duration.ofSeconds(timeout.toSeconds()));
        waitTillElementDisappear(nowPlayingAlarm);
    }

}
