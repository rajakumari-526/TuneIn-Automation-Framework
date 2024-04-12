package com.tunein.mobile.tests.android.ads;

import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.tests.common.ads.AdsUITest;

import java.util.Arrays;
import java.util.List;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_BBC_RADIO_5;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BANNER_ADS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class AndroidAdsUITest extends AdsUITest {

    public void testStopPlayingAdsFreeStation() {
        updateLaunchArgumentFor(BANNER_ADS, "true");
        deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_BBC_RADIO_5);
        nowPlayingPage.validateAdBannerDisplayed(false);
        nowPlayingPage
                .validateNowPlayingAdBannerDisplayed(false)
                .stopStreamPlaying()
                .validateNowPlayingAdBannerDisplayed(false)
                .validateAdBannerDisplayed(false);
        nowPlayingPage.minimizeNowPlayingScreen();
        List<NavigationAction.NavigationActionItems> listOfPages = Arrays.asList(HOME, LIBRARY, SEARCH);
        for (NavigationAction.NavigationActionItems page : listOfPages) {
            navigationAction.navigateTo(page);
            nowPlayingPage.validateAdBannerDisplayed(false);
        }
    }
}
