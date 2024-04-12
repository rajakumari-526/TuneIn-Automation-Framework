package com.tunein.mobile.tests.common.navigation;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import static com.tunein.mobile.testdata.TestGroupName.ACCEPTANCE_TEST;

public class NavigationTest extends BaseTest {

    @TestCaseId("749146")
    @Test(description = "verifying Nav bar, Tab bar, Status bar fom different pages", groups = {ACCEPTANCE_TEST})
    public void testNavBarTabBarStatusBar() {
        navigationAction
                .mainNavigationBarElements().forEach(barItem -> {
                    navigationAction.navigateTo(barItem);
                    navigationAction.validateCarModeButtonIsDisplayed();
                    navigationAction.validateUserProfileButtonIsDisplayed();
                    navigationAction.validateCorrespondingPageIsOpened(barItem);
                });
        navigationAction.validateNavBarIsDisplayed();
    }
}
