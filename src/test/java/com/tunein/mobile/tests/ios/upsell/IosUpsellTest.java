package com.tunein.mobile.tests.ios.upsell;

import com.tunein.mobile.tests.common.upsell.UpsellTest;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.utils.ApplicationUtil;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.TestGroupName.UPSELL_TEST;
import static com.tunein.mobile.testdata.dataprovider.LocationProvider.DeviceLocation.LOC_GERMANY;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.utils.ApplicationUtil.setDeviceLocation;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.UPSELL_SCREEN_SHOW_ON_LAUNCH;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.updateLaunchArgumentFor;

public class IosUpsellTest extends UpsellTest {

    @Override
    public void testUpsellUI() {
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
        upsellPage
                .waitUntilPageReady()
                .validateUIElements(upsellPage.upsellPageElements());
        upsellPage
                .tapOnStartListeningFreeButton()
                .validateAppleSignInPrompt();
    }

    @Override
    public void testUpsellCloseButton() {
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
        upsellPage
                .waitUntilPageReady()
                .tapCloseButton();
        homePage.validateHomePageIsOpened();
    }

    @Override
    public void testAlexaUpsell() {
        navigationAction.navigateTo(SIGNIN_FORM);
        signInPage.signInWithCredentials(USER_GENERAL.getEmail(), USER_GENERAL.getPassword());
        userProfilePage.tapOnSettingsButton();
        settingsPage.tapOnEnableAlexaLiveSkill();
        alexaUpsellPage
                .validateIsOnAlexaUpsellPage()
                .tapNoThanksButton();
        settingsPage
                .validateIsOnSettingsPage()
                .tapBackButton();
    }

    @TestCaseIds({@TestCaseId("729761")})
    @Test(description = "Premium tab upsell", groups = {UPSELL_TEST})
    public void testPremiumTabUpsell() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @TestCaseIds({@TestCaseId("259858")})
    @Test(description = "Upsell on launch", groups = {UPSELL_TEST})
    public void testUpsellOnLaunch() {
        updateLaunchArgumentFor(UPSELL_SCREEN_SHOW_ON_LAUNCH, "true");
        upsellPage
                .waitUntilPageReady()
                .validateUIElements(upsellPage.upsellPageElements());
        upsellPage
                .tapOnStartListeningFreeButton()
                .validateAppleSignInPrompt();
        ApplicationUtil.restartApp();
        upsellPage.validateIsOnUpsellPage(false);
    }

    //Set location doesn't work in headless mode
    @Ignore
    @TestCaseIds({@TestCaseId("713661")})
    @Test(description = "Check price label has different prices per region in upsell", groups = {UPSELL_TEST})
    public void testUpsellPriceLabelRegionalDifferences() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        String initialPrice = upsellPage.getFullPriceString();
        upsellPage.closeUpsell();
        setDeviceLocation(LOC_GERMANY);
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        upsellPage.validateUpsellPriceIsNotEqualTo(initialPrice);
    }
}
