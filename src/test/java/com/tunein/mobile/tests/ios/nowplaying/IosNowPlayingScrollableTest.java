package com.tunein.mobile.tests.ios.nowplaying;

import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.tests.common.nowplaying.NowPlayingScrollableTest;

import java.util.Arrays;

import static com.tunein.mobile.pages.BasePage.CategoryType.*;
import static com.tunein.mobile.pages.BasePage.ViewModelType.LIST;
import static com.tunein.mobile.pages.BasePage.ViewModelType.TILE;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.pages.common.nowplaying.NowPlayingPage.NowPlayingScrollableCards.SCHEDULE_CARD;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.SHORT;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BANNER_ADS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class IosNowPlayingScrollableTest extends NowPlayingScrollableTest {

    @Override
    public void testScrollableOpenedFrom() {
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
                .tapOnGoToProfileButtonInProfileCard();
        contentProfilePage.tapProfilePlayButton(contentForSearch);
        nowPlayingPage.validateNowPlayingCardsElements(contentForSearch);
    }

    @Override
    public void testScrollingDuringPreRoll() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        navigationAction.navigateTo(HOME);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        deepLinksUtil.openURL(STREAM_STATION_WITH_PREROLL.getStreamTuneDeepLink());
        scroll(DOWN, 1);
        nowPlayingPage
                .validateSeekBarDisplayed()
                .waitUntilPreRollAdDisappearIfDisplayed()
                .validateNowPlayingScreenIsScrollable();
    }

    @Override
    public void testScrollableLocalRadioView() {
        deepLinksUtil.openTuneThroughDeeplink(STREAM_PODCAST_MARKETPLACE);
        nowPlayingPage.tapOnCategoryHeader(LOCAL_RADIO, DOWN);
        contentsListPage.validateContentsListPageIsDisplayed();
        homePage.openContentUnderCategoryWithHeader(CATEGORY_TYPE_FM, LIST, SHORT, 2, false);
        nowPlayingPage.validateStreamStartPlaying();
    }

    @Override
    public void testNowPlayingScheduleCard() {
        deepLinksUtil.createNewDeviceServiceThroughDeeplink();
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage
                .validateRequiredCardsArePresent(Arrays.asList(SCHEDULE_CARD))
                .tapOnThreeDotsButton();
        String stationName = nowPlayingScheduledDialog.getStreamName();
        nowPlayingScheduledDialog.validateUIElements(nowPlayingScheduledDialog.schedulePageElements());
        nowPlayingScheduledDialog.clickOnFavoriteButton();
        nowPlayingPage.minimizeIfNowPlayingDisplayed();
        navigationAction.navigateTo(FAVORITES);
        favoritesPage.validateThatContentAppearedInFavorites(stationName);
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage.scrollToRequiredCard(SCHEDULE_CARD);
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnFavoriteButton();
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnScheduleCardMoreButton();
        contentProfilePage.validateContentProfileTitleIsEqualTo(stationName);

        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_CNN);
        nowPlayingPage.scrollToRequiredCard(SCHEDULE_CARD);
        nowPlayingPage.tapOnThreeDotsButton();
        nowPlayingScheduledDialog.clickOnShareButton();
        shareDialog.validateShareDialogDisplayed();
    }

}
