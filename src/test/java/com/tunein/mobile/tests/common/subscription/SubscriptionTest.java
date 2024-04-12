package com.tunein.mobile.tests.common.subscription;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.PREMIUM;
import static com.tunein.mobile.testdata.TestGroupName.SUBSCRIPTION_TEST;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;
import static com.tunein.mobile.utils.ApplicationUtil.reinstallApp;

public class SubscriptionTest extends BaseTest {

    @TestCaseId("749790")
    @Test(description = "Check premium state after app is deleted", groups = {SUBSCRIPTION_TEST})
    public void testCheckPremiumStateAfterAppIsDeleted() {
        signInPage.signInFlowForUser(USER_PREMIUM);
        reinstallApp();
        navigationAction.navigateTo(PREMIUM);
        premiumPage.validateFreeTrailButtonIsDisplayed();
    }

}
