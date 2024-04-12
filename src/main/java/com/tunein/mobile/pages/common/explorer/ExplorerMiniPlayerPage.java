package com.tunein.mobile.pages.common.explorer;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static io.appium.java_client.AppiumBy.id;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class ExplorerMiniPlayerPage extends BasePage {

    protected SelenideElement explorerMiniPlayerRecommendedTitle = $(android(id("recommendedTitle"))
            .ios(iOSNsPredicateString("name CONTAINS 'SIMILAR TO ' AND type == 'XCUIElementTypeStaticText'"))).as("Recommended section title");

    protected ElementsCollection explorerMiniPlayerRecommendedStationList = $$(android(xpath("//*[contains(@resource-id, 'recommended')]/android.widget.ImageView"))
            .ios(xpath("//XCUIElementTypeCollectionView/XCUIElementTypeCell"))).as("Recommended section station list");

    protected SelenideElement explorerMiniPlayer = $(android(id("miniplayer"))
            .ios(xpath("//XCUIElementTypeOther[./XCUIElementTypeButton[(contains(@name,'miniPlayerPlayButton')) or (contains(@name,'miniPlayerPauseButton'))]]"))).as("Explorer mini-player");

    // TODO: Button states not differentiated on Android
    protected SelenideElement explorerMiniPlayerPlayButton = $(android(id("playbackBtn"))
            .ios(id("miniPlayerPlayButton"))).as("Play button");

    protected SelenideElement explorerMiniPlayerStationLogo = $(android(id("artwork"))
            .ios(xpath("//XCUIElementTypeOther[./XCUIElementTypeButton[(contains(@name,'miniPlayerPlayButton')) or (contains(@name,'miniPlayerPauseButton'))]]/XCUIElementTypeImage"))).as("Station/Podcast logo");

    protected SelenideElement explorerMiniPlayerStationPodcastName = $(android(id("title"))
            .ios(xpath("//XCUIElementTypeOther[./XCUIElementTypeButton[(contains(@name,'miniPlayerPlayButton')) or (contains(@name,'miniPlayerPauseButton'))]]/*/XCUIElementTypeStaticText[1]"))).as("Station/Podcast name");

    protected SelenideElement explorerMiniPlayerStationPodcastInfo = $(android(id("subtitle"))
            .ios(xpath("//XCUIElementTypeOther[./XCUIElementTypeButton[(contains(@name,'miniPlayerPlayButton')) or (contains(@name,'miniPlayerPauseButton'))]]/*/XCUIElementTypeStaticText[2]"))).as("Station/Podcast info");

    protected SelenideElement explorerMiniPlayerStopButton = $(android(id("playbackBtn"))
            .ios(id("miniPlayerPauseButton"))).as("Stop/Pause button");

    // TODO: Button states not differentiated on Android
    protected SelenideElement explorerMiniPlayerFavoriteButton = $(android(id("followBtn"))
            .ios(xpath("//XCUIElementTypeButton[@name='map unfavorited' and not(@value)]"))).as("Favorite button");

    /* --- Loadable Component Method --- */

    @Override
    public ExplorerMiniPlayerPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(explorerMiniPlayer, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step("Click Explorer mini player")
    public NowPlayingPage clickExplorerMiniPlayer() {
        clickOnElement(explorerMiniPlayer);
        return nowPlayingPage.waitUntilPageReady();
    }

    @Step("Click Explorer mini player recommended station by index")
    public ExplorerMiniPlayerPage clickRecommendedStationByIndex(int index) {
        clickOnElement(explorerMiniPlayerRecommendedStationList.get(index));
        return this;
    }

    @Step("Click Explorer mini player favorite button")
    public ExplorerMiniPlayerPage clickExplorerMiniPlayerFavoriteButton() {
        clickOnElement(explorerMiniPlayerFavoriteButton);
        return this;
    }

    @Step("Click on stop button")
    public ExplorerMiniPlayerPage tapStopButton() {
        clickOnElement(explorerMiniPlayerStopButton);
        return validateMiniPlayerPlayButtonIsDisplayed();
    }

    @Step("Click on play button")
    public ExplorerMiniPlayerPage tapPlayButton() {
        clickOnElement(explorerMiniPlayerPlayButton);
        return this;
    }

    /* --- Validation Methods --- */

    @Step("Validate Explorer mini player page is displayed")
    public ExplorerMiniPlayerPage validateExplorerMiniPlayerIsDisplayed() {
        assertThat(isOnExplorerMiniPlayerPage()).as("Explorer page is not displayed").isTrue();
        return this;
    }

    @Step("Validate stream is playing")
    public ExplorerMiniPlayerPage validateThatStreamStartsPlaying() {
        assertThat(isElementDisplayed(explorerMiniPlayerPlayButton)).as("Pause button not displayed").isFalse();
        return this;
    }

    @Step("Validate Explorer mini player page is not displayed")
    public ExplorerMiniPlayerPage validateExplorerMiniPlayerIsNotDisplayed() {
        assertThat(isOnExplorerMiniPlayerPage()).as("Explorer miniplayer is displayed").isFalse();
        return this;
    }

    @Step("Validate recommended stations are displayed")
    public ExplorerMiniPlayerPage validateRecommendedStationsAreDisplayed() {
        assertThat((isElementDisplayed(explorerMiniPlayerRecommendedTitle))).as("Explorer miniplayer is displayed").isTrue();
        return this;
    }

    @Step("Click Explorer mini player unfavorite button")
    public abstract ExplorerMiniPlayerPage clickExplorerMiniPlayerUnFavouriteButton();

    @Step("Validate stream is playing")
    public ExplorerMiniPlayerPage validateStreamStartsPlaying() {
        assertThat(isElementDisplayed(explorerMiniPlayerStopButton)).as("Stop button in miniplayer is not displayed").isTrue();
        return this;
    }

    @Step("Validate that meta data is not empty")
    public ExplorerMiniPlayerPage validateThatMetaDataIsNotEmpty() {
        getSoftAssertion().assertThat(getElementText(explorerMiniPlayerStationPodcastName)).as("Cannot find the title of the station").isNotNull();
        getSoftAssertion().assertThat(getElementText(explorerMiniPlayerStationPodcastInfo)).as("Cannot find the sub title of the station").isNotNull();
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Verifying that title and subtitle are different")
    public ExplorerMiniPlayerPage validateThatMetaDataHasChanged(String title, String subTitle) {
        getSoftAssertion().assertThat(getElementText(explorerMiniPlayerStationPodcastName)).as("Titles of the stations are equal").isNotEqualTo(title);
        getSoftAssertion().assertThat(getElementText(explorerMiniPlayerStationPodcastInfo)).as("Sub titles of the stations are equal").isNotEqualTo(subTitle);
        getSoftAssertion().assertAll();
        return this;
    }

    @Step
    public ExplorerMiniPlayerPage validateMiniPlayerPlayButtonIsDisplayed() {
        assertThat(isElementDisplayed(explorerMiniPlayerPlayButton))
                .as("Play button isn't displayed in MiniPlayer")
                .isTrue();
        return this;
    }

    @Step
    public ExplorerMiniPlayerPage validateMiniPlayerPauseButtonIsDisplayed() {
        assertThat(isElementDisplayed(explorerMiniPlayerStopButton))
                .as("Pause button isn't displayed in MiniPlayer")
                .isTrue();
        return this;
    }

    @Step("Validate explorer miniplayer station title is not equal to {expectedTitle}")
    public ExplorerMiniPlayerPage validateExplorerMiniplayerStationTitlesNotEqual(String expectedTitle) {
        assertThat(expectedTitle).as("Station titles are equal").isNotEqualTo(getStationName());
        return this;
    }

    /* --- Helper Methods --- */

    public boolean isOnExplorerMiniPlayerPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(explorerMiniPlayer, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
    }

    public abstract HashMap<String, SelenideElement> explorerMiniPlayerPageElements();

    @Step("Get station name")
    public String getStationName() {
        return getElementText(explorerMiniPlayerStationPodcastName);
    }

    @Step("Get station subtitle")
    public String getStationSubTitle() {
        return getElementText(explorerMiniPlayerStationPodcastInfo);
    }
}
