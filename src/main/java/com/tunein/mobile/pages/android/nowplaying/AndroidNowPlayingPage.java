package com.tunein.mobile.pages.android.nowplaying;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.nowplaying.MiniPlayerPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.NowPlayingFavoriteDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.AppiumBy;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.data.Offset;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getThreadId;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStatus.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingStreamTimeType.PASSED;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.ScrubberPosition.*;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.SET_ALARM;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.*;
import static com.tunein.mobile.utils.CommandLineProgramUtil.getOutputOfExecution;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GeneralTestUtil.Counter.TITLE;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.WaitersUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidNowPlayingPage extends NowPlayingPage {

    private String contentInCardLocator = ".//*[contains(@content-desc,'item_%s_guideItemImage_')]";

    private String contentWithTextLocator = "//*[contains(@text,'%s')]/..";

    private String contentByNumberLocator = "(//*[@resource-id='tunein.player:id/row_tile_image'])[%s]";

    private String cardTitleLocator = "item_%s_title";

    private String cardSubTitleLocator = "item_%s_subtitle";

    private String cardImageLocator = "item_%s_profileImage";

    private String cardDescriptionLocator = "item_%s_description";

    private String cardSeeMoreTextLocator = "item_%s_seeMoreText";

    private String cardDateLocator = "item_%s_date";

    private String cardTimeLocator = "item_%s_duration";

    private int getIndexOfCard(NowPlayingScrollableCards card) {
        switch (card) {
            case RECENTS_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingRecentsCard).replace("item_", ""));
            }
            case SIMILAR_TO_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingSimilarToCard).replace("item_", ""));
            }
            case LOCAL_RADIO_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingLocalRadioToCard).replace("item_", ""));
            }
            case PROFILE_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingProfileCard).replace("item_", ""));
            }
            case EPISODE_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingEpisodeCard).replace("item_", ""));
            }
            case SCHEDULE_CARD -> {
                return Integer.parseInt(getElementContent(nowPlayingScheduleCard).replace("item_", ""));
            }
            default -> throw new Error("Invalid card type - " + card.toString());
        }
    }

    @Step("Wait until NowPlaying page is opened")
    @Override
    public NowPlayingPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitUntilContentPartiallyBuffered();
        hidePlaybackSpeedTooltipPopUpIfPresent();
        if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
        setFirstLaunchValue(getThreadId(), false);
      //  tapListenLiveButtonIfPresent(); We don't have this popup on prod. Rollback if it will be displayed
        waitForTheStreamPlayback();
        return this;
    }

    @Step("Wait until page ready with known content {content}")
    @Override
    public NowPlayingPage waitUntilPageReadyWithKnownContent(Contents content) {
        closePermissionPopupsIfDisplayed();
        ContentType contentType = getContentType(content);
        switch (contentType) {
            case LIVE_STATION, SHOW -> {
              //  closeInformationPopUpIfPresent();  We don't have this popup on prod. Rollback if it will be displayed
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PODCAST -> {
                hidePlaybackSpeedTooltipPopUpIfPresent();
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> hidePlaybackSpeedTooltipPopUpIfPresent();
            case PREMIUM_LIVE_STATION, OWNED_AND_OPERATED -> listenLiveRewindModalDialog.closeInformationPopUpIfPresent();
            case CUSTOM_URL -> { }
            default -> throw new Error("Invalid content type");
        }
        setFirstLaunchValue(getThreadId(), false);
        waitForTheStreamPlayback(content);
        return this;
    }

    @Step("Wait until NowPlaying page for {contentType} is opened ")
    @Override
    public NowPlayingPage waitUntilPageReadyWithKnownContentType(ContentType contentType) {
        closePermissionPopupsIfDisplayed();
        switch (contentType) {
            case LIVE_STATION -> {
                //  closeInformationPopUpIfPresent();  We don't have this popup on prod. Rollback if it will be displayed
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PODCAST -> {
                hidePlaybackSpeedTooltipPopUpIfPresent();
                if (!getFirstLaunchValue()) waitUntilPreRollAdDisappearIfDisplayed();
            }
            case PREMIUM_PODCAST, GAME_REPLAY, AUDIOBOOK -> hidePlaybackSpeedTooltipPopUpIfPresent();
            case PREMIUM_LIVE_STATION, OWNED_AND_OPERATED -> listenLiveRewindModalDialog.closeInformationPopUpIfPresent();
            case CUSTOM_URL -> { }
            default -> throw new Error("Invalid content type");
        }
        setFirstLaunchValue(getThreadId(), false);
        waitForTheStreamPlayback(contentType);
        return this;
    }

    @Step("Waiting for Now Playing page to load without waiting for preroll to finish")
    @Override
    public NowPlayingPage waitUntilPageReadyLiteVersion() {
        closePermissionPopupsIfDisplayed();
        boolean isStreamTestEnabled = config().isAppiumStreamTestEnabled();
        SelenideElement element = isStreamTestEnabled ? nowPlayingMoreOptions : nowPlayingSeekBarContainer;
        waitTillVisibilityOfElement(element, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    @Override
    public NowPlayingPage waitUntilMinimiseButtonIsClickable() {
        closePermissionPopupsIfDisplayed();
        clickOnElement(nowPlayingMinimiseButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Wait until content will be partially buffered")
    @Override
    public NowPlayingPage waitUntilContentPartiallyBuffered() {
        customWait(Duration.ofMillis(config().waitShortTimeoutMilliseconds()));
        return this;
    }

    @Step("Tap on favorite icon. Set favorite state: {addToFavorite}")
    @Override
    public NowPlayingPage tapOnFavoriteIcon(boolean addToFavorite, NowPlayingFavoriteDialog.FavoriteContentType... typeOfFavoriteContent) {
        if (typeOfFavoriteContent != null && typeOfFavoriteContent.length > 0) {
            clickOnElement(nowPlayingFavoriteIcon);
            if (nowPlayingFavoriteDialog.isFavoritePopUpDisplayed()) {
                nowPlayingFavoriteDialog.tapOnFavoriteContent(addToFavorite, typeOfFavoriteContent[0]);
            }
        } else {
            //TODO Add possibility to identify that favored icon is selected
            clickOnElement(nowPlayingFavoriteIcon);
        }
        return this;
    }

    @Step("Closing {prompt} prompt if displayed")
    @Override
    public NowPlayingPage closePopUpIfDisplayed(Prompts prompt) {
        ReporterUtil.log("Functionality is absent for Android Platform");
        return this;
    }

    @Step("Minimize now playing screen")
    @Override
    public MiniPlayerPage minimizeNowPlayingScreen() {
        clickOnElement(nowPlayingMinimiseButton, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        if (isElementDisplayed(nowPlayingSeekBarContainer, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            clickOnElement(nowPlayingMinimiseButton);
        }
        return miniPlayerPage.waitUntilPageReady();
    }

    @Step("Hide playback speed tooltip if present")
    @Override
    public NowPlayingPage hidePlaybackSpeedTooltipPopUpIfPresent() {
        ReporterUtil.log("Speed tool tip is disabled for Android by isSpeedControlTooltipEnabled config key");
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
            default -> {
                requiredTimeInSeconds = topicDuration - DEFAULT.getScrubberPositionValue();
            }
        }
        int sliderWidth = nowPlayingSeekBarContainer.getSize().getWidth();
        int ySliderPosition = nowPlayingSeekBar.getLocation().getY() + nowPlayingSeekBar.getSize().height / 2;
        int spaceBeforeSlider = ((getAppiumDriver().manage().window().getSize().getWidth() - sliderWidth) / 2);
        double currentSeconds = getStreamTimeInSeconds(PASSED);
        double secondsInPixel = (double) topicDuration / (sliderWidth);
        int xStartPosition = (int) (currentSeconds / secondsInPixel) + spaceBeforeSlider;
        int xEndPosition = (int) (requiredTimeInSeconds / secondsInPixel) + spaceBeforeSlider;
        ReporterUtil.log("Move scrubber to " + scrubberPosition + " position");
        swipe(new Point(xStartPosition, ySliderPosition), new Point(xEndPosition, ySliderPosition));
        waitUntilStreamStartOpeningOrBuffering();
        waitUntilStreamStopOpeningAndBuffering();
        return this;
    }

    @Step
    @Override
    public void openEditSleepTimerDialog() {
        clickOnElement(nowPlayingSleepTimerIcon);
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
        int seekBarValue = getCurrentStreamTimeInSeconds();
        getSoftAssertion().assertThat(seekBarValue).as("Scrubber is not at required position").isCloseTo(shouldContainValue, Offset.offset(30));
        getSoftAssertion().assertThat(passedTimeCounter).as("Stream passed time is incorrect").isCloseTo(shouldContainValue, Offset.offset(30));
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate new episode is opened")
    @Override
    public NowPlayingPage validateNowPlayingOpenNewEpisode(String previousEpisode) {
        int passedTimeCounter = getCurrentStreamTimeInSeconds();
        String currentNameOfEpisode = getTextValueOfNowPlayingSubtitle();
        int seekBarValue = getStreamTimeInSeconds(PASSED);
        getSoftAssertion().assertThat(seekBarValue).as("Scrubber is not at the beginning of the stream").isCloseTo(0, Offset.offset(50));
        getSoftAssertion().assertThat(passedTimeCounter).as("Stream passed time is incorrect").isCloseTo(0, Offset.offset(50));
        getSoftAssertion().assertThat(previousEpisode).as("New Episode " + currentNameOfEpisode + " is not opened").isNotEqualTo(currentNameOfEpisode);
        getSoftAssertion().assertAll();
        return this.waitUntilPageReady();
    }

    @Step
    @Override
    public NowPlayingPage validateStreamErrorIsDisplayed() {
        assertThat(getTextValueOfNowPlayingSubtitle()).as("Stream error message failed to display").contains(ERROR.getNowPlayingStatusValue());
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
        List<NowPlayingScrollableCards> listOfCards = new ArrayList<>();
        int numberOfScrolls = 0;
        while (numberOfScrolls < 6) {
            if (!listOfCards.contains(RECENTS_CARD) && isElementDisplayed(nowPlayingRecentsCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("RECENTS CARD is founded");
                listOfCards.add(RECENTS_CARD);
                int id = getIndexOfCard(RECENTS_CARD);
                int count = nowPlayingRecentsCard.$$x(String.format(contentInCardLocator, id)).size();
                getSoftAssertion().assertThat(count).as("Recent card is empty").isGreaterThan(0);
            } else if (!listOfCards.contains(SIMILAR_TO_CARD) && isElementDisplayed(nowPlayingSimilarToCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("SIMILAR TO CARD is founded");
                listOfCards.add(SIMILAR_TO_CARD);
                int id = getIndexOfCard(SIMILAR_TO_CARD);
                int count = nowPlayingSimilarToCard.$$x(String.format(contentInCardLocator, id)).size();
                getSoftAssertion().assertThat(isElementDisplayed(similarToCardTitle)).as("Similar to card title is not displayed").isTrue();
                if (streamName.length > 0) {
                    getSoftAssertion().assertThat(getElementText(similarToCardTitle)).as("Similar to card title has wrong value").isEqualTo("Similar to " + streamName[0]);
                }
                getSoftAssertion().assertThat(count > 0).as("Similar to card is empty").isTrue();
            } else if (!listOfCards.contains(LOCAL_RADIO_CARD) && isElementDisplayed(nowPlayingLocalRadioToCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("LOCAL RADIO CARD is founded");
                listOfCards.add(LOCAL_RADIO_CARD);
                int id = getIndexOfCard(LOCAL_RADIO_CARD);
                int count = nowPlayingLocalRadioToCard.$$(By.xpath(String.format(contentInCardLocator, id))).size();
                getSoftAssertion().assertThat(isElementDisplayed(localRadioCardTitle)).as("Local Radio card title is not displayed").isTrue();
                getSoftAssertion().assertThat(getElementText(localRadioCardTitle)).as("Local Radio card title has wrong value").isEqualTo("Local Radio");
                getSoftAssertion().assertThat(count > 0).as("Local Radio card is empty").isTrue();
            } else if (!listOfCards.contains(PROFILE_CARD) && isElementDisplayed(nowPlayingProfileCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("PROFILE CARD is founded");
                listOfCards.add(PROFILE_CARD);
                int id = getIndexOfCard(PROFILE_CARD);
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Go to profile button is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingProfileCard.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Go to profile title is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingProfileCard.$(AppiumBy.accessibilityId(String.format(cardImageLocator, id))))).as("Go to profile image is not displayed").isTrue();
                if (streamName.length > 0) {
                    getSoftAssertion().assertThat(getElementText(nowPlayingProfileCard.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Profile title has wrong value").isEqualTo(streamName[0]);
                }
            } else if (!listOfCards.contains(EPISODE_CARD) && (contentType == PODCAST || contentType == PREMIUM_PODCAST || contentType == GAME_REPLAY || contentType == AUDIOBOOK) && isElementDisplayed(nowPlayingEpisodeCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("EPISODE CARD is founded");
                listOfCards.add(EPISODE_CARD);
                int id = getIndexOfCard(EPISODE_CARD);
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Episode card title is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardDescriptionLocator, id))))).as("Episode card description is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardSubTitleLocator, id))))).as("Episode card subtitle is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardSeeMoreTextLocator, id))))).as("Episode card see more button is not displayed").isTrue();
                break;
            } else if (!listOfCards.contains(SCHEDULE_CARD) && (contentType != PODCAST && contentType != PREMIUM_PODCAST && contentType != GAME_REPLAY && contentType != AUDIOBOOK) && isElementDisplayed(nowPlayingScheduleCard, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))) {
                ReporterUtil.log("SCHEDULE CARD is founded");
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
        int id = getIndexOfCard(card);
        switch (card) {
            case SIMILAR_TO_CARD -> {
                int count = cardElement.$$(By.xpath(String.format(contentInCardLocator, id))).size();
                getSoftAssertion().assertThat(isElementDisplayed(similarToCardTitle)).as("Similar to card title is not displayed").isTrue();
                if (content.length > 0) {
                    getSoftAssertion().assertThat(getElementText(similarToCardTitle)).as("Similar to card title has wrong value").isEqualTo("Similar to " + content[0].getStreamName());
                }
                getSoftAssertion().assertThat(count > 0).as("Similar to card is empty").isTrue();
            }
            case RECENTS_CARD -> {
                int count = cardElement.$$(By.xpath(String.format(contentInCardLocator, id))).size();
                getSoftAssertion().assertThat(count > 0).as("Recent card is empty").isTrue();
            }
            case LOCAL_RADIO_CARD -> {
                int count = cardElement.$$(By.xpath(String.format(contentInCardLocator, id))).size();
                getSoftAssertion().assertThat(isElementDisplayed(localRadioCardTitle)).as("Local Radio card title is not displayed").isTrue();
                getSoftAssertion().assertThat(getElementText(localRadioCardTitle)).as("Local Radio card title has wrong value").isEqualTo("Local Radio");
                getSoftAssertion().assertThat(count > 0).as("Local Radio card is empty").isTrue();
            }
            case EPISODE_CARD -> {
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Episode card title is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardDescriptionLocator, id))))).as("Episode card description is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardSubTitleLocator, id))))).as("Episode card subtitle is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardSeeMoreTextLocator, id))))).as("Episode card see more button is not displayed").isTrue();
            }
            case SCHEDULE_CARD -> getSoftAssertion().assertThat(isElementDisplayed(scheduleCardTitle)).as("Schedule card title is not displayed").isTrue();
            case PROFILE_CARD -> {
                getSoftAssertion().assertThat(isElementDisplayed(nowPlayingGoToProfileButton)).as("Go to profile button is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Go to profile title is not displayed").isTrue();
                getSoftAssertion().assertThat(isElementDisplayed(cardElement.$(AppiumBy.accessibilityId(String.format(cardImageLocator, id))))).as("Go to profile image is not displayed").isTrue();
                if (content.length > 0) {
                    getSoftAssertion().assertThat(getElementText(cardElement.$(AppiumBy.accessibilityId(String.format(cardTitleLocator, id))))).as("Profile title has wrong value").isEqualTo(content[0].getStreamName());
                }
            }
            default -> throw new IllegalStateException("Unexpected value");
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateNumberOfContentInRecentCard(int expectedNumber) {
        SelenideElement recentCard = scrollToRequiredCard(RECENTS_CARD);
        int count = recentCard.$$(By.xpath(String.format(contentInCardLocator, getIndexOfCard(RECENTS_CARD)))).size();
        getSoftAssertion().assertThat(count).as("Contnet number in Recent card is not equal to " + expectedNumber).isEqualTo(expectedNumber);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateDateAndTimeDisplayedInEpisodeCard() {
        int cardId = getIndexOfCard(EPISODE_CARD);
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardDateLocator, cardId))))).as("Episode card date is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardTimeLocator, cardId))))).as("Episode card time is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateAudioVolumeLessThanMaximum(int currentVolume) {
        String audioDumpBeforeAlarm = nowPlayingPage.getAudioInfo();
        int maxVolume = nowPlayingPage.getMaxVolumeFromAudioInfo(audioDumpBeforeAlarm);
        assertThat(currentVolume)
                .as("Volume before alarm is " + currentVolume + " and not less than max volume " + maxVolume)
                .isLessThan(maxVolume);
        return this;
    }

    @Step
    @Override
    public NowPlayingPage validateAudioVolumeIsMaximum(int currentVolume) {
        String audioDumpBeforeAlarm = nowPlayingPage.getAudioInfo();
        int maxVolume = nowPlayingPage.getMaxVolumeFromAudioInfo(audioDumpBeforeAlarm);
        assertThat(currentVolume).as("Volume is not equal to max volume of " + maxVolume + ", it is " + currentVolume).isEqualTo(maxVolume);
        return this;
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
            case LIVE_STATION, PREMIUM_LIVE_STATION, CUSTOM_URL -> {
                switch (nowPlayingStatus) {
                    case PLAYING -> {
                        getSoftAssertion().assertThat(isElementDisplayed(nowPlayingLiveLabel))
                                .as("Live label is not displayed in " + nowPlayingStatus.name() + " state")
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
            case OWNED_AND_OPERATED -> {
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
                    getSoftAssertion().assertThat(isElementDisplayed(nowPlayingTimeCounterPassed))
                            .as("Counter time passed is not displayed in " + nowPlayingStatus.name() + " state")
                            .isTrue();
            }
            default -> throw new Error("Invalid content type");
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Get state of sleep timer icon")
    public String getSleepTimerState() {
        return getElementContentDescOrLabel(nowPlayingSleepTimerIcon);
    }

    @Step("Verify display of sleep timer is set as {expectedSleepTimerState}")
    @Override
    public NowPlayingPage validateStateOfSleepTimer(boolean isSleepTimerSet) {
        String state = getSleepTimerState();
        if (!isSleepTimerSet) {
            AssertionsForClassTypes.assertThat(state.contains("Set")).as("Sleep timer state is not in the 'unset' state").isTrue();
        } else {
            AssertionsForClassTypes.assertThat(state.contains("Edit")).as("Sleep timer state is not in the 'set' state").isTrue();
        }
        return this;
    }

    @Override
    public boolean isFavoriteIconFilled() {
        return getElementContent(nowPlayingFavoriteIcon).contains("Not");
    }

    @Override
    public int getCurrentStreamTimeInSeconds() {
        waitUntilStreamStopOpeningAndBuffering();
        double value = Integer.parseInt(getElementText(nowPlayingSeekBar).replace(".0", "").replace("%", ""));
        return (int) value;
    }

    @Override
    public String getEpisodeCardDescriptionText() {
        int id = getIndexOfCard(EPISODE_CARD);
        SelenideElement episodeDescription;
        if (isElementDisplayed(nowPlayingEpisodeCard, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) {
            episodeDescription = nowPlayingEpisodeCard.$(AppiumBy.accessibilityId(String.format(cardDescriptionLocator, id)));
        } else {
            SelenideElement episodeNotesCard = scrollToRequiredCard(EPISODE_CARD);
            episodeDescription = episodeNotesCard.$(AppiumBy.accessibilityId(String.format(cardDescriptionLocator, id)));
        }
        return getElementText(episodeDescription);
    }

    @Step("Open content with name {contentName} in card {card}")
    @Override
    public NowPlayingPage openContentWithNameInCard(String contentName, NowPlayingScrollableCards card) {
        switch (card) {
            case SIMILAR_TO_CARD, LOCAL_RADIO_CARD -> {
                SelenideElement requiredCard = scrollToRequiredCard(card);
                By locator = By.xpath(String.format(contentWithTextLocator, contentName));
                SelenideElement requiredContent = swipeToElement(locator, 5, LEFT, requiredCard);
                clickOnElement(requiredContent);
            }
            default -> throw new Error("Invalid card type - " + card.toString());
        }
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Open content by index {contentNumber} in card {card}")
    @Override
    public NowPlayingPage openContentByNumberInCard(int contentNumber, NowPlayingScrollableCards card) {
        switch (card) {
            case RECENTS_CARD, LOCAL_RADIO_CARD, SIMILAR_TO_CARD -> {
                int numberContentInCard = (card == RECENTS_CARD) ? 5 : 4;
                SelenideElement requiredCard = scrollToRequiredCard(card);
                if (contentNumber <= numberContentInCard) {
                    SelenideElement recentContentBy = requiredCard.$(By.xpath(String.format(contentByNumberLocator, contentNumber)));
                    clickOnElement(swipeToElement(recentContentBy, 5, LEFT, requiredCard));
                } else {
                    swipeElement(LEFT, config().nowPlayingSwipeDistance(), requiredCard);
                    openContentByNumberInCard(contentNumber - numberContentInCard, card);
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
        SelenideElement station;
        switch (direction) {
            case LEFT -> {
                station = listOfSwitchContainerStations.get(0);
            }
            case RIGHT -> {
                station = listOfSwitchContainerStations.get(1);
            }
            default -> throw new Error("Unsupported direction for Switch container");
        }
        swipeElement(direction, config().nowPlayingSwipeDistance(), station);
        return this;
    }

    @Step("Close 'Got it' prompt")
    @Override
    public NowPlayingPage closeGotItPrompt() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(gotItPrompt);
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
                .setAlarmPlayTimeDuration(minutesAhead)
                .tapOnSaveButton();
        return this;
    }

    @Step("Click on {direction} station in Switch container")
    @Override
    public NowPlayingPage tapOnSwitchContainerStation(SwipeDirection direction) {
        closePermissionPopupsIfDisplayed();
        int switchStationsContainerWidth = switchStationsContainer.getSize().width;
        int switchStationsContainerX = switchStationsContainer.getLocation().x;
        int smallIconYCoordinate;
        int smallIconHeight;
        int smallIconWidth;
        int xCoordinate;
        int yCoordinate;

        switch (direction) {
            case LEFT -> {
                smallIconYCoordinate = listOfSwitchContainerStations.get(0).getLocation().y;
                smallIconHeight = listOfSwitchContainerStations.get(0).getSize().height;
                smallIconWidth = listOfSwitchContainerStations.get(0).getSize().width;
                xCoordinate = switchStationsContainerX + (switchStationsContainerWidth / 4) - smallIconWidth;
                yCoordinate = smallIconYCoordinate + (smallIconHeight / 2);
            }
            case RIGHT -> {
                smallIconYCoordinate = listOfSwitchContainerStations.get(1).getLocation().y;
                smallIconHeight = listOfSwitchContainerStations.get(1).getSize().height;
                smallIconWidth = listOfSwitchContainerStations.get(1).getSize().width;
                xCoordinate = switchStationsContainerX + ((switchStationsContainerWidth / 4) * 3) + smallIconWidth;
                yCoordinate = smallIconYCoordinate + (smallIconHeight / 2);
            }
            default -> throw new Error("Unsupported direction for Switch container");
        }
        tapOnCoordinates(xCoordinate, yCoordinate);
        return this;
    }

    @Step("Validate that Nowplaying Page metadata is not empty for {contentType}")
    @Override
    public NowPlayingPage validateNowPlayingMetadataIsNotEmpty(ContentType contentType) {
        NowPlayingStatus nowPlayingStatus = getNowPlayingStreamStatus();
        if (nowPlayingStatus == PLAYING) {
            getSoftAssertion().assertThat(getTextValueOfNowPlayingSubtitle()).as("Now Playing subtitle is empty").isNotEmpty();
        }
        getSoftAssertion().assertThat(getTextValueOfNowPlayingTitle()).as("Now Playing title is empty").isNotEmpty();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Waiting for the \"Opening...\", \"Connecting...\" and \"Buffering\" to disappear in the Now Playing screen")
    @Override
    public NowPlayingPage waitUntilStreamStopOpeningAndBuffering(Duration... timeout) {
        Duration requiredTimeout = timeout.length > 0 ? timeout[0] : Duration.ofSeconds(config().waitShortTimeoutSeconds());
        if (isElementEnabled(nowPlayingSubtitle, requiredTimeout)) {
            // Exit early without checking for the all states when preroll is already playing
            if (config().isAppiumStreamTestEnabled() && generalTestUtil.getCounterValue(TITLE).contains(ADVERTISEMENT)) {
                return this;
            }
            waitTillAllTextsOfElementDisappear(
                    nowPlayingSubtitle,
                    new String[]{CONNECTING.getNowPlayingStatusValue(), OPENING.getNowPlayingStatusValue(), BUFFERING.getNowPlayingStatusValue()},
                    Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        }
        return this;
    }

    @Step("Waiting for the \"Opening...\", \"Connecting...\" and \"Buffering\" to appear in the Now Playing screen")
    @Override
    public NowPlayingPage waitUntilStreamStartOpeningOrBuffering(Duration... timeout) {
        Duration requiredTimeout = timeout.length > 0 ? timeout[0] : Duration.ofSeconds(config().waitShortTimeoutSeconds());
        if (isElementEnabled(nowPlayingSubtitle, requiredTimeout)) {
            // Exit early without checking for the all states when preroll is already playing
            if (config().isAppiumStreamTestEnabled() && generalTestUtil.getCounterValue(TITLE).contains(ADVERTISEMENT)) {
                return this;
            }
            waitTillOneOfTextsOfElementAppear(
                    nowPlayingSubtitle,
                    new String[]{CONNECTING.getNowPlayingStatusValue(), OPENING.getNowPlayingStatusValue(), BUFFERING.getNowPlayingStatusValue()},
                    Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds())
            );
        }
        return this;
    }

    @Override
    public String getAudioInfo() {
        return getOutputOfExecution("adb  -s " + getUDID() + " shell dumpsys audio").split(" STREAM_MUSIC:")[1].split("Current")[0];
    }

    @Override
    public int getMaxVolumeFromAudioInfo(String audioInfo) {
        return Integer.parseInt(audioInfo.split("Max:")[1].split("stream")[0].strip());
    }

    @Override
    public int getCurrentVolumeFromAudioInfo() {
        return Integer.parseInt(nowPlayingPage.getAudioInfo().split("streamVolume:")[1].split("Current")[0].strip());
    }

    @Step("Wait until alarm goes off")
    @Override
    public void waitUntilAlarmGoesOff(Duration timeout) {
        customWait(Duration.ofSeconds(timeout.toSeconds()));
        nowPlayingAlarmClockDialog.validateAlarmClockDialogIsOpened();
    }
}
