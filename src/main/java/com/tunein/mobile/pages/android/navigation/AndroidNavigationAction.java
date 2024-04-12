package com.tunein.mobile.pages.android.navigation;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.NavigationAction;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitVisibilityOfElement;

public class AndroidNavigationAction extends NavigationAction {

    @Override
    public void closePremiumTabTooltipIfPresent() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public NavigationAction navigateToPageWithNavigationBar() {
        int counter = 0;
        while (!navigationAction.isTabBarDisplayed() && counter < 3) {
            deviceNativeActions.clickBackButton();
            counter++;
        }
        return this;
    }

    @Override
    public boolean isTabBarItemSelected(NavigationActionItems tabBarItem) {
        switch (tabBarItem) {
            case HOME -> {
                return waitVisibilityOfElement(homeButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds())).isSelected();
            }
            case LIBRARY -> {
                return waitVisibilityOfElement(libraryButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds())).isSelected();
            }
            case SEARCH -> {
                return waitVisibilityOfElement(searchButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds())).isSelected();
            }
            case PREMIUM -> {
                return waitVisibilityOfElement(premiumButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds())).isSelected();
            }
            default -> throw new Error(tabBarItem + " item is not an element of tab bar");
        }
    }

}
