package com.tunein.mobile.tests.android.upsell;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.tests.common.upsell.UpsellTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PREMIUM;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PROFILE;
import static com.tunein.mobile.testdata.TestGroupName.UPSELL_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static org.openqa.selenium.ScreenOrientation.LANDSCAPE;

public class AndroidUpsellTest extends UpsellTest {

    @Override
    public void testUpsellUI() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }

    @Override
    public void testUpsellCloseButton() {
        navigationAction.navigateTo(PREMIUM);
        premiumPage.tapFreeTrialButton();
        upsellPage.tapCloseButton();
        premiumPage.validatePremiumPageIsOpened();
    }

    @Override
    public void testAlexaUpsell() {
        signInPage.signInFlowForUser(USER_GENERAL);
        navigationAction.navigateTo(PROFILE);
        userProfilePage
                .tapOnTuneInPremiumButton()
                .tapLinkWithAlexaButton();
        alexaUpsellPage
                .validateIsOnAlexaUpsellPage()
                .tapNoThanksButton();
        tuneInPremiumPage.validateThatTuneInPremiumPageIsOpened();
    }

    @Ignore("Landscape mode is planned to be disabled on Android phones (not tablets)")
    @TestCaseId("730586")
    @Test(description = "Upsell landscape", groups = {UPSELL_TEST})
    public void testUpsellLandscape() {
        navigationAction.navigateTo(PREMIUM);
        deviceNativeActions.setOrientationMode(LANDSCAPE);
        premiumPage.tapFreeTrialButton();
        upsellPage.validatePortraitOrientation();
        upsellPage.validateUIElements(upsellPage.upsellPageElements());
    }
}
