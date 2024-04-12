package com.tunein.mobile.tests.android.nowplaying;

import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingScrollableTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.tunein.mobile.pages.BasePage.CategoryType.CATEGORY_TYPE_TOP_LOCAL_STATIONS;
import static com.tunein.mobile.pages.BasePage.CategoryType.LOCAL_RADIO;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.SCHEDULE_CARD;
import static com.tunein.mobile.testdata.TestGroupName.IGNORE_TEST;
import static com.tunein.mobile.testdata.TestGroupName.NOW_PLAYING_TEST;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_CNN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;

public class AndroidNowPlayingScrollableTest extends NowPlayingScrollableTest {

    @Issues({@Issue("DROID-14589"), @Issue("IOS-16774")})
    @TestCaseId("730200")
    @Test(description = "Check scrollable NowPPlaying when Internet connection - lost and restored", groups = {NOW_PLAYING_TEST, IGNORE_TEST})
    public void testNowPlayingConnectionLostAndRestored() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_WITHOUT_ADS);

        // Check Scrollable NowPlaying after reconnection
        deviceNativeActions.disableWiFi();
        nowPlayingPage.waitUntilStreamStartOpeningOrBuffering();
        deviceNativeActions.enableWifi();
        nowPlayingPage.validateNowPlayingCardsElements(STREAM_STATION_WITHOUT_ADS);

        // Check Scrollable NowPlaying when connection is absent
        deviceNativeActions.disableWiFi();
        nowPlayingPage.validateNowPlayingCardsElements(STREAM_STATION_WITHOUT_ADS);

        // Check Scrollable NowPlaying after reconnection for similar station
        nowPlayingPage.openContentByNumberInCard(1, SIMILAR_TO_CARD);
        deviceNativeActions.enableWifi();
        nowPlayingPage.tapOnPlayButton();
        nowPlayingPage.validateNowPlayingCardsElements();
    }

    @Override
    public void testScrollableOpenedFrom() {
        settingsPage.enableScrollableNowPlayingFlow();
        Contents contentForSearch = STREAM_STATION_WITHOUT_ADS;
        // Open stream from Browsies
        navigationAction.navigateTo(HOME);
        homePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_TOP_LOCAL_STATIONS, TILE, SHORT, 1, false);
        nowPlayingPage
                .validateNowPlayingCardsElements()
                .minimizeNowPlayingScreen();
        // Open stream from Search and Profile
        navigationAction.navigateTo(SEARCH);
        searchPage.searchStreamAndPlayFirstResult(contentForSearch);
        nowPlayingPage
                .validateNowPlayingCardsElements(contentForSearch)
                .goToStreamProfile();
        contentProfilePage.tapProfilePlayButton(contentForSearch);
        nowPlayingPage.validateNowPlayingCardsElements(contentForSearch);
    }

    @Override
    public void testScrollingDuringPreRoll() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        scroll(DOWN, 3);
        nowPlayingPage
                .validateSeekBarDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingScreenIsScrollable();
    }

    @Override
    public void testScrollableLocalRadioView() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.tapOnCategoryHeader(LOCAL_RADIO, DOWN);
        nowPlayingPage
                .validateNowPlayingCardElementsFor(LOCAL_RADIO_CARD)
                .openContentUnderCategoryWithHeader(LOCAL_RADIO, TILE, SHORT, 4, true);
        contentListItemDialog
                .validateContentStreamDialogIsDisplayed()
                .tapOnContentItem(0);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @Override
    public void testNowPlayingScheduleCard() {
        settingsPage.enableScrollableNowPlayingFlow();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage
                .validateRequiredCardsArePresent(Arrays.asList(SCHEDULE_CARD))
                .tapOnThreeDotsButton();
        nowPlayingScheduledDialog.validateUIElements(nowPlayingScheduledDialog.schedulePageElements());
        nowPlayingScheduledDialog.clickOnFavoriteButton();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(LIBRARY);
        libraryPage.validateFavoritesCount(1);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage.scrollToRequiredCard(SCHEDULE_CARD);
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnFavoriteButton();
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnScheduleCardMoreButton();
        contentProfilePage.validateContentProfilePageIsOpened();

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage.scrollToRequiredCard(SCHEDULE_CARD);
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnShareButton();
        shareDialog.validateShareDialogDisplayed();
    }

}
