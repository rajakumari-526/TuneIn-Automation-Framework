package com.tunein.mobile.pages.common.navigation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.ScreenFacade;
import com.tunein.mobile.pages.common.authentication.*;
import com.tunein.mobile.pages.common.explorer.ExplorerPage;
import com.tunein.mobile.pages.common.homepage.CarModePage;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels;
import com.tunein.mobile.pages.common.library.DownloadsPage;
import com.tunein.mobile.pages.common.library.FavoritesPage;
import com.tunein.mobile.pages.common.library.LibraryPage;
import com.tunein.mobile.pages.common.premium.PremiumPage;
import com.tunein.mobile.pages.common.search.SearchPage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.pages.common.userprofile.AboutPage;
import com.tunein.mobile.pages.common.userprofile.SettingsPage;
import com.tunein.mobile.pages.common.userprofile.UserProfilePage;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.pages.common.homepage.HomePage.BrowsiesBarTabsLabels.*;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection;
import static com.tunein.mobile.utils.GestureActionUtil.scroll;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static io.appium.java_client.AppiumBy.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class NavigationAction extends ScreenFacade {

    protected SelenideElement navigationBar = $(android(id("toolbar"))
            .ios(iOSNsPredicateString("name == 'TuneIn_Radio.HomeTableView'"))).as("Navigation Bar");

    protected SelenideElement tabBar = $(android(id("bottom_navigation"))
            .ios(iOSNsPredicateString("name == 'TabBarViewControllerId'"))).as("Tab bar");

    protected SelenideElement homeButton = $(android(id("menu_navigation_home"))
            .ios(iOSNsPredicateString("name == 'HomeIdRootTabBarItem'"))).as("Home button");

    protected SelenideElement libraryButton = $(android(id("menu_navigation_library"))
            .ios(iOSNsPredicateString("name == 'LibraryIdRootTabBarItem'"))).as("Library button");

    protected SelenideElement explorerButton = $(android(id("menu_navigation_mapview"))
            .ios(xpath("(//XCUIElementTypeOther[@name=\"LibraryIdRootTabBarItem\"])[2]"))).as("Explorer button");

    protected SelenideElement premiumButton = $(android(id("menu_navigation_premium"))
            .ios(iOSNsPredicateString("name == 'PremiumIdRootTabBarItem'"))).as("Premium button");

    protected SelenideElement carModeButton = $(android(androidUIAutomator("description(\"Car mode\")"))
            .ios(iOSNsPredicateString("name == 'carModeIdNavBarButton'"))).as("Card mode button");

    protected SelenideElement profileButton = $(android(id("action_bar_account"))
            .ios(iOSNsPredicateString("name == 'profileIdNavBarButton'"))).as("Profile button");

    protected SelenideElement searchButton = $(android(id("menu_navigation_search"))
            .ios(iOSNsPredicateString("name == 'SearchIdRootTabBarItem'"))).as("Search button");

    protected SelenideElement navigationBackButton = $(android(accessibilityId("Back"))
            .ios(iOSNsPredicateString("label == 'Back'"))).as("Back button");

    public ElementsCollection contentCellContainer = $$(android(xpath("//*[@resource-id='tunein.player:id/gallery_recycler_view']"))
            .ios(xpath("//XCUIElementTypeTable/XCUIElementTypeCell"))).as("Content cell container");

    public boolean isTabBarDisplayed() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(tabBar);
    }

    @DataProvider(name = "deepLinkNavigations")
    public static Object[][] deepLinkNavigations() {
        return new Object[][]{{UPSELL}, {PREMIUM}, {REGWALL}, {CARMODE}};
    }

    @Step("Click on back button if displayed")
    public void tapBackButtonIfDisplayed() {
        clickOnElementIfDisplayed(navigationBackButton);
    }

    @Step("Navigate to {navigationActionItem}")
    public void navigateTo(NavigationActionItems navigationActionItem) {
        closePermissionPopupsIfDisplayed();
        switch (navigationActionItem) {
            case HOME -> openHomePage();
            case LIBRARY -> openLibraryPage();
            case EXPLORER -> openExplorerPage();
            case SEARCH -> openSearchPage();
            case PREMIUM -> openPremiumPage();
            case PROFILE -> openUserProfilePage();
            case SETTINGS -> openSettingsPage();
            case REGWALL -> openRegwallPage();
            case SIGNUP_FORM -> openSignUpPage();
            case SIGNIN_FORM -> openSignInPage();
            case FORGOT_PASSWORD_FORM -> openForgotPasswordPage();
            case FACEBOOK_SIGNUP_FORM -> openFacebookSignUpPage();
            case GOOGLE_SIGNUP_FORM -> openGoogleSignUpPage();
            case FAVORITES -> openFavoritesPage();
            case DOWNLOADS -> openDownloadsPage();
            case UPSELL -> openUpsellPage();
            case ABOUT -> openAboutTuneInPage();
            case CARMODE -> openCarModePage();
            default -> throw new IllegalStateException("Navigation item isn't available " + navigationActionItem);
        }
    }

    private HomePage openHomePage() {
        for (int i = 0; i <= 2; i++) {
            try {
                clickOnElement(homeButton);
            } catch (Throwable e) {
                upsellPage.closeUpsell();
                takeScreenshot();
                clickOnElementIfDisplayed(homeButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            }
            if (isElementNotDisplayed(navigationBackButton)) break;
        }
        return homePage.waitUntilPageReady();
    }

    private UserProfilePage openUserProfilePage() {
        if (isElementNotDisplayed(profileButton)) {
            openHomePage();
        }
        try {
            clickOnElement(profileButton);
        } catch (Throwable e) {
            upsellPage.closeUpsell();
            clickOnElementIfDisplayed(profileButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
        }
        return userProfilePage.waitUntilPageReady();
    }

    private SettingsPage openSettingsPage() {
        return openUserProfilePage().tapOnSettingsButton();
    }

    private LibraryPage openLibraryPage() {
        for (int i = 0; i <= 2; i++) {
            try {
                clickOnElement(libraryButton);
            } catch (Throwable e) {
                upsellPage.closeUpsell();
                userProfilePage.closeProfilePageIfDisplayed();
                clickOnElementIfDisplayed(libraryButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            }
            if (isElementNotDisplayed(navigationBackButton)) break;
        }
        return libraryPage.waitUntilPageReady();
    }

    private ExplorerPage openExplorerPage() {
        if (!explorerPage.isOnExplorerPage()) {
            try {
                clickOnElement(explorerButton);
            } catch (Throwable e) {
                upsellPage.closeUpsell();
                clickOnElementIfDisplayed(explorerButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            }
        }
        return explorerPage.waitUntilPageReady();
    }

    private SearchPage openSearchPage() {
        if (!isTabBarItemSelected(SEARCH)) {
            try {
                clickOnElement(searchButton);
            } catch (Throwable e) {
                upsellPage.closeUpsell();
                clickOnElementIfDisplayed(searchButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            }
        }
        return searchPage.waitUntilPageReady();
    }

    /**
     * Navigates to TuneIn Premium screen
     * @return UpsellPage
     */
    private PremiumPage openPremiumPage() {
        if (!isTabBarItemSelected(PREMIUM)) {
            try {
                clickOnElement(premiumButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            } catch (Throwable e) {
                upsellPage.closeUpsell();
                clickOnElementIfDisplayed(premiumButton, Duration.ofSeconds(config().waitMediumTimeoutSeconds()));
            }
        }
        return premiumPage.waitUntilPageReady();
    }

    /**
     * Navigates and opens registration wall screen
     * @return regWallPage
     */
    private RegWallPage openRegwallPage() {
        openUserProfilePage();
        return userProfilePage.tapOnLoginSignupButton();
    }

    /**
     * Navigates and opens Sign-In form screen
     * @return signInPage
     */
    private SignInPage openSignInPage() {
        openRegwallPage();
        return regWallPage.tapOnSignInButton();
    }

    /**
     * Navigates and opens Sign-Up form screen
     * @return signInPage
     */
    private SignUpPage openSignUpPage() {
        openRegwallPage();
        return regWallPage.tapSignUpWithEmailButton();
    }

    /**
     * Navigates and opens Facebook Sign-Up form screen
     * @return FacebookSignUpPage
     */
    private FacebookAuthenticationPage openFacebookSignUpPage() {
        openRegwallPage();
        return regWallPage.tapSignUpWithFacebookButton();
    }

    /**
     * Navigates and opens Google Sign-Up form screen
     * @return GoogleSignUpPage
     */
    private GoogleAuthenticationPage openGoogleSignUpPage() {
        openRegwallPage();
        return regWallPage.tapSignUpWithGoogleButton();
    }

    /**
     * Navigates and opens ForgotPasswordPage screen
     * @return ForgotPage
     */
    private ForgotPasswordPage openForgotPasswordPage() {
        openSignInPage();
        return signInPage.tapForgetPassword();
    }

    /**
     * Navigates to Favorites from Library screen
     * @return FavoritesPage
     */
    private FavoritesPage openFavoritesPage() {
        openLibraryPage();
        return libraryPage.tapOnFavoritesButton();
    }

    /**
     * Navigates to Upsell from Premium screen
     * @return UpsellPage
     */
    private UpsellPage openUpsellPage() {
        openPremiumPage();
        return premiumPage.tapFreeTrialButton();
    }

    /**
     * Navigates to About TuneIn screen
     * @return AboutPage
     */
    private AboutPage openAboutTuneInPage() {
        openUserProfilePage();
        return userProfilePage.tapOnAboutTuneInButton();
    }

    private DownloadsPage openDownloadsPage() {
        openLibraryPage();
        return libraryPage.tapOnDownloadsButton();
    }

    private CarModePage openCarModePage() {
        if (isElementNotDisplayed(carModeButton)) {
           openHomePage();
        }
        try {
            clickOnElement(carModeButton);
        } catch (Throwable e) {
            upsellPage.closeUpsell();
            clickOnElement(carModeButton);
        }
        return carModePage.waitUntilPageReady();
    }

    public abstract void closePremiumTabTooltipIfPresent();

    public abstract NavigationAction navigateToPageWithNavigationBar();

    public abstract boolean isTabBarItemSelected(NavigationActionItems tabBarItem);

    @Step
    public void validateNavBarIsDisplayed() {
        assertThat(isElementDisplayed(navigationBar)).as("Navigation Bar is not displayed after scrolling UP").isTrue();
    }

    @Step
    public void validateNavBarIsNotDisplayed() {
        assertThat(isElementNotDisplayed(navigationBar)).as("Navigation Bar is displayed after scrolling down").isTrue();
    }

    @Step("Validate navigation-bar after scrolling {direction}")
    public NavigationAction validateNavigationBarAfterScrolling(ScrollDirection direction) {
        scroll(direction, config().scrollTenTimes());
        switch (direction) {
            case UP -> validateNavBarIsDisplayed();
            case DOWN -> {
                ReporterUtil.log("Number of Categories displayed" + contentCellContainer.size());
                if (isIos() && contentCellContainer.size() <= 6) {
                    throw new SkipException("Skipping this test as enough categories are not present to scroll down");
                } else {
                    validateNavBarIsNotDisplayed();
                }
            }
            default -> throw new IllegalStateException("Unsupported scroll direction " + direction);
        }
        return this;
    }

    @Step("Verify corresponding navigation action item page is opened")
    public void validateCorrespondingPageIsOpened(NavigationAction.NavigationActionItems navigationActionItems) {
        switch (navigationActionItems) {
            case HOME -> homePage.validateHomePageIsOpened();
            case LIBRARY -> libraryPage.validateLibraryPageIsOpened();
            case FAVORITES -> favoritesPage.validateFavouritesPageIsOpened();
            case EXPLORER -> explorerPage.validateThatOnExplorerPage();
            case PREMIUM -> premiumPage.validatePremiumPageIsOpened();
            case DOWNLOADS -> downloadsPage.validateDownloadsPromptIsDisplayed();
            case PROFILE -> userProfilePage.validateThatOnProfilePage();
            case SEARCH -> searchPage.validateThatOnSearchPage();
            default -> throw new Error("Invalid navigation action items");
        }
    }

    @Step("Validate car mode button is displayed")
    public void validateCarModeButtonIsDisplayed() {
        assertThat(isElementDisplayed(carModeButton)).as("Car mode button is not displayed").isTrue();
    }

    @Step("Validate user profile button is displayed")
    public void validateUserProfileButtonIsDisplayed() {
        assertThat(isElementDisplayed(profileButton)).as("User profile is not displayed").isTrue();
    }

    public List<NavigationActionItems> mainNavigationBarElements() {
        return Arrays.asList(LIBRARY, SEARCH, PREMIUM, HOME);
    }

    public List<NavigationActionItems> differentNavigationBarElements() {
        return Arrays.asList(LIBRARY, SEARCH, PREMIUM, FAVORITES, HOME, DOWNLOADS);
    }

    public enum NavigationActionItems {
        HOME,
        LIBRARY,
        FAVORITES,
        EXPLORER,
        DOWNLOADS,
        PREMIUM,
        PROFILE,
        SEARCH,
        SETTINGS,
        CARMODE,
        REGWALL,
        SIGNUP_FORM,
        SIGNIN_FORM,
        FORGOT_PASSWORD_FORM,
        UPSELL,
        ABOUT,
        FACEBOOK_SIGNUP_FORM,
        GOOGLE_SIGNUP_FORM;
    }

    @Step("Navigate to {browsiesBarTabsLabelsAction}")
    public void navigateToBrowsies(BrowsiesBarTabsLabels browsiesBarTabsLabelsAction) {
        navigationAction.navigateTo(HOME);
        switch (browsiesBarTabsLabelsAction) {
            case FOR_YOU -> homePage.tapOnRequiredBrowsiesBarTab(FOR_YOU);
            case RADIO -> homePage.tapOnRequiredBrowsiesBarTab(RADIO);
            case SPORTS -> homePage.tapOnRequiredBrowsiesBarTab(SPORTS);
            case I_HEART_RADIO -> homePage.tapOnRequiredBrowsiesBarTab(I_HEART_RADIO);
            case NEWS_AND_TALK -> homePage.tapOnRequiredBrowsiesBarTab(NEWS_AND_TALK);
            case PODCASTS -> homePage.tapOnRequiredBrowsiesBarTab(PODCASTS);
            case MUSIC -> homePage.tapOnRequiredBrowsiesBarTab(MUSIC);
            case BY_LANGUAGE -> homePage.tapOnRequiredBrowsiesBarTab(BY_LANGUAGE);
            case BY_LOCATION -> homePage.tapOnRequiredBrowsiesBarTab(BY_LOCATION);
            default -> throw new IllegalStateException("Browsies navigation item isn't available " + browsiesBarTabsLabelsAction);
        }
    }

}
