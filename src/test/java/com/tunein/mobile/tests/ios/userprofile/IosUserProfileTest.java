package com.tunein.mobile.tests.ios.userprofile;

import com.tunein.mobile.pages.common.userprofile.AboutPage;
import com.tunein.mobile.tests.common.userprofile.UserProfileTest;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.ABOUT;
import static com.tunein.mobile.utils.ApplicationUtil.activateApp;
import static com.tunein.mobile.utils.BrowserUtil.terminateSafariBrowser;

public class IosUserProfileTest extends UserProfileTest {

    @Override
    public void testSettingsInAboutTuneInPage() {
        navigationAction.navigateTo(ABOUT);
        aboutPage.validateAboutPageIsOpened();
        for (AboutPage.AboutPageItems aboutPageItem : AboutPage.AboutPageItems.values()) {
            aboutPage.navigateToAboutPageItem(aboutPageItem);
            aboutPage.validateCorrespondingAboutPageIsOpened(aboutPageItem);
            terminateSafariBrowser();
            activateApp();
            aboutPage.validateAboutPageIsOpened();
        }
    }
}
