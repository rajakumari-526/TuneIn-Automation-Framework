package com.tunein.mobile.pages.ios.navigation;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.NavigationAction;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.BasePage.clickOnElementIfDisplayed;
import static com.tunein.mobile.pages.BasePage.closePermissionPopupsIfDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.swipeDownScreen;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.By.xpath;

public class IosNavigationAction extends NavigationAction {

    protected SelenideElement navigationPremiumTabTooltip = $(iOSNsPredicateString("label == \"Tap here to explore TuneIn Premium\"")).as("Premium tab tooltip");

    private String tabBarItemImageSuffixLocator = ".//XCUIElementTypeImage";

    @Override
    public void closePremiumTabTooltipIfPresent() {
        closePermissionPopupsIfDisplayed();
        clickOnElementIfDisplayed(navigationPremiumTabTooltip);
    }

    @Step
    @Override
    public NavigationAction navigateToPageWithNavigationBar() {
        int counter = 0;
        while (!navigationAction.isTabBarDisplayed() && counter < 3) {
            swipeDownScreen();
            counter++;
        }
        return this;
    }

    @Override
    public boolean isTabBarItemSelected(NavigationActionItems tabBarItem) {
        SelenideElement requiredTabBarImage;
        switch (tabBarItem) {
            case HOME -> {
                requiredTabBarImage = homeButton.$(xpath(tabBarItemImageSuffixLocator));
            }
            case LIBRARY -> {
                requiredTabBarImage = libraryButton.$(xpath(tabBarItemImageSuffixLocator));
            }
            case SEARCH -> {
                requiredTabBarImage = searchButton.$(xpath(tabBarItemImageSuffixLocator));
            }
            case PREMIUM -> {
                requiredTabBarImage = premiumButton.$(xpath(tabBarItemImageSuffixLocator));
            }
            default -> throw new Error(tabBarItem + " item is not an element of tab bar");
        }
        return requiredTabBarImage.getAttribute("name").contains(".active");
    }

}
