package com.tunein.mobile.tests.android.userprofile;

import com.tunein.mobile.pages.common.userprofile.AboutPage.AboutPageItems;
import com.tunein.mobile.tests.common.userprofile.UserProfileTest;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.ABOUT;
import static com.tunein.mobile.utils.ApplicationUtil.activateApp;

public class AndroidUserProfileTest extends UserProfileTest {

    @Override
    public void testSettingsInAboutTuneInPage() {
        navigationAction.navigateTo(ABOUT);
        aboutPage.validateAboutPageIsOpened();
        for (AboutPageItems aboutPageItem : AboutPageItems.values()) {
            aboutPage.navigateToAboutPageItem(aboutPageItem);
            aboutPage.validateCorrespondingAboutPageIsOpened(aboutPageItem);
            activateApp();
            aboutPage.validateAboutPageIsOpened();
        }
    }
}
