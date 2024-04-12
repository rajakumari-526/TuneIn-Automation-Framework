package com.tunein.mobile.pages.common.nowplaying;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.testdata.models.Contents;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillElementDisappear;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MiniPlayerPage extends BasePage {

    protected SelenideElement miniPlayer = $(android(id("mini_player_container"))
            .ios(iOSNsPredicateString("name == 'miniPlayerView'"))).as("Mini Player window");

    protected SelenideElement miniPlayerStopButton = $(android(androidUIAutomator("descriptionMatches(\"^.*Stop|Pause.*$\").className(android.widget.ImageButton)"))
            .ios(id("miniPlayerPauseButton"))).as("Pause button");

    public SelenideElement miniPlayerPlayButton = $(android(androidUIAutomator("description(\"Play\").className(android.widget.ImageButton)"))
            .ios(id("miniPlayerPlayButton"))).as("Play button");

    protected SelenideElement miniPlayerStationLogo = $(android(id("mini_player_logo"))
            .ios(id("miniPlayerImage"))).as("Station logo");

    protected SelenideElement miniPlayerStationPodcastName = $(android(id("mini_player_song_title"))
            .ios(id("miniPlayerSecondary"))).as("Station/Podcast name");

    public SelenideElement miniPlayerStationPodcastInfo = $(android(id("mini_player_station_title"))
            .ios(id("miniPlayerPrimaryTitle"))).as("Station/Podcast info");

    protected SelenideElement miniPlayerIsLiveImage = $(android(id("live_indicator"))
            .ios(id("miniPlayerIsLiveImage"))).as("Live image");

    /* --- Loadable Component Method --- */

    @Step
    @Override
    public MiniPlayerPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(miniPlayer, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step
    public void waitUntilMiniPlayerDisappear(Duration timeout) {
        waitTillElementDisappear(miniPlayer, timeout);
    }

    /* --- Action Methods --- */

    @Step("Extend mini player")
    public NowPlayingPage extendMiniPlayer(Contents... content) {
        clickOnElement(miniPlayer);
        return (content.length > 0) ? nowPlayingPage.waitUntilPageReadyWithKnownContent(content[0]) : nowPlayingPage.waitUntilPageReady();
    }

    @Step ("Tap on mini player")
    public void tapOnMiniPlayer() {
        clickOnElement(miniPlayer);
    }

    @Step
    public NowPlayingPage extendMiniPlayerIfDisplayed(Contents... content) {
        if (isMiniPlayerDisplayed()) {
            extendMiniPlayer(content);
        }
        return (content.length > 0) ? nowPlayingPage.waitUntilPageReadyWithKnownContent(content[0]) : nowPlayingPage.waitUntilPageReady();
    }

    @Step
    public MiniPlayerPage tapStopButton() {
        clickOnElement(miniPlayerStopButton);
        return this;
    }

    @Step
    public MiniPlayerPage tapPlayButton() {
        clickOnElement(miniPlayerPlayButton);
        return this;
    }

    /* --- Validation Methods --- */

    @Step
    public MiniPlayerPage validateMiniPlayerStopButtonIsDisplayed() {
        assertThat(isElementDisplayed(miniPlayerStopButton)).as("Stop button in miniplayer is not displayed").isTrue();
        return this;
    }

    @Step
    public MiniPlayerPage validateMiniPlayerIsDisplayed() {
        assertThat(isElementDisplayed(miniPlayer)).as("Mini-player is not displayed").isTrue();
        return this;
    }

    @Step
    public MiniPlayerPage validateMiniPlayerIsNotDisplayed() {
        closePermissionPopupsIfDisplayed();
        assertThat(isElementDisplayed(miniPlayer)).as("Mini-player is displayed").isFalse();
        return this;
    }

    @Step
    public MiniPlayerPage validateMiniPlayerLogoIsDisplayed() {
        assertThat(isElementDisplayed(miniPlayerStationLogo))
                .as("Station logo or album art isn't displayed in mini player")
                .isTrue();
        return this;
    }

    @Step
    public MiniPlayerPage validateMiniPlayerPlayButtonIsDisplayed() {
        assertThat(isElementDisplayed(miniPlayerPlayButton))
                .as("Play button isn't displayed in MiniPlayer")
                .isTrue();
        return this;
    }

    /**
     * This method compares mini-player's metadata with category item metadata
     * @param contentDescriptionArea STATION_TITLE or STATION_SUBTITLE, else will throw an error
     * @param expectedText expected metadata value
     */
    @Step
    public void validateCategoryItemWithMiniPlayerMetadata(
            ContentProfilePage.ContentDescriptionArea contentDescriptionArea,
            String expectedText
    ) {
        String message = "Unexpected value: \"" + contentDescriptionArea + "\" only `STATION_TITLE` and `STATION_SUBTITLE` are supported";
        String actual;
        switch (contentDescriptionArea) {
            case STATION_TITLE -> {
                actual = getStationName();
            }
            case STATION_SUBTITLE -> {
                actual = getStationInfo();
            }
            default -> throw new IllegalStateException(message);
        }
        assertThat(actual).as("Mini-player Metadata does not match").isEqualTo(expectedText);
    }

    @Step("Validate live icon is not displayed")
    public MiniPlayerPage validateLiveIconIsNotDisplayed() {
        assertThat(isElementNotDisplayed(miniPlayerIsLiveImage)).as("Live image is displayed when it shouldn't be").isTrue();
        return this;
    }

    @Step("Validate live icon is displayed")
    public MiniPlayerPage validateLiveIconIsDisplayed() {
        assertThat(isElementDisplayed(miniPlayerIsLiveImage)).as("Live image is not displayed when it should be").isTrue();
        return this;
    }

    @Step("Validate stream is playing")
    public MiniPlayerPage validateStreamStartsPlaying() {
        getSoftAssertion().assertThat(isElementDisplayed(miniPlayerStopButton)).as("Stop button in miniplayer is not displayed").isTrue();
        getSoftAssertion().assertThat(isElementNotDisplayed(miniPlayerPlayButton)).as("Stop button in miniplayer is not displayed").isTrue();
        getSoftAssertion().assertAll();
        return this;
    }

    /* --- Helper Methods --- */

    public String getStationName() {
        return getElementText(miniPlayerStationPodcastName);
    }

    public String getStationInfo() {
        return getElementText(miniPlayerStationPodcastInfo);
    }

    public boolean isMiniPlayerDisplayed() {
        return isElementDisplayed(miniPlayerStationLogo);
    }

    public boolean isMiniPlayerPlaying() {
        return isElementDisplayed(miniPlayerStopButton);
    }

    public HashMap<String, SelenideElement> miniPlayerPageElements(boolean isLive) {
        HashMap<String, SelenideElement> mapElements = new HashMap<>();
        mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "1", miniPlayerStationLogo);
        mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "2", miniPlayerStationPodcastName);
        mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "3", miniPlayerStationPodcastInfo);
        if (isMiniPlayerPlaying()) {
            mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "4", miniPlayerStopButton);
            if (isLive) {
                mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "5", miniPlayerIsLiveImage);
            }
        } else {
            mapElements.put(SKIP_TEXT_VALIDATION_PREFIX + "4", miniPlayerPlayButton);
        }
        return mapElements;
    }

}
