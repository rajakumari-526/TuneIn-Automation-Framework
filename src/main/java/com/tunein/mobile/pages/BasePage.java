package com.tunein.mobile.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.appium.SelenideAppium;
import com.epam.reportportal.annotations.Step;
import com.opencsv.CSVWriter;
import com.tunein.mobile.deviceactions.DeviceNativeActions;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.utils.ReporterUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.testng.SkipException;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.quiteAppiumDriver;
import static com.tunein.mobile.appium.driverprovider.AppiumSession.getUDID;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.HOME;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.STREAM_STATION_KQED;
import static com.tunein.mobile.utils.ApplicationUtil.ApplicationPermission.*;
import static com.tunein.mobile.utils.ApplicationUtil.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.SwipeDirection.LEFT;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.customWait;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.id;
import static io.appium.java_client.appmanagement.ApplicationState.RUNNING_IN_FOREGROUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public abstract class BasePage extends ScreenFacade {

    protected SelenideElement offlineErrorMessageText = $(id("snackbar_text")).as("Offline error message text");

    protected SelenideElement offlineErrorRetryButton = $(id("snackbar_action")).as("Offline error retry button");

    private static Map<Long, SoftAssertions> softAssertionsMap = new HashMap<>();

    public static final String SKIP_TEXT_VALIDATION_PREFIX = "SKIP_TEXT_ELEMENT_VALIDATION_";

    private static final String ANDROID_CATEGORY_HEADER_LOCATOR = "//*[@resource-id='tunein.player:id/view_model_container_title' and @text='%s']";

    private static final String IOS_CATEGORY_HEADER_LOCATOR = "//XCUIElementTypeOther[@name='%s' and @visible='true']";

    private static final String ANDROID_CONTENT_LIST_UNDER_CATEGORY_LOCATOR = "//*[./*[contains(@text, '%s')]]/following-sibling::android.widget.ScrollView/*/android.view.ViewGroup";

    private static final String IOS_CONTENT_LIST_UNDER_CATEGORY_LOCATOR = "//*[@name='%s']/following-sibling::XCUIElementTypeCell[1]/*/XCUIElementTypeCell";

    public static final String AD_BANNER_LOCATOR = isAndroid() ? "ad_container_banner" : "bannerAdView";

    protected SelenideElement adsWebPage = SelenideAppium.$(android(id("com.android.chrome:id/location_bar"))
            .ios(accessibilityId("CapsuleNavigationBar?isSelected=true"))).as("Web page");

    public static final String IOS = "ios";

    public static final String ANDROID = "android";

    public static final String RELEASE = "release";

    public static final String UPPER_CASE = "\"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"";

    public static final String LOWER_CASE = "\"abcdefghijklmnopqrstuvwxyz\"";

    public static boolean isAndroid() {
        return config().mobileOS().equalsIgnoreCase(ANDROID);
    }

    public static boolean isIos() {
        return config().mobileOS().equalsIgnoreCase(IOS);
    }

    public static boolean isReleaseTestRun() {
        return config().suite().equalsIgnoreCase(RELEASE);
    }

    public static void closePermissionPopupsIfDisplayed() {
            if (config().testOnRealDevices()) {
                if (!config().isAppiumStreamTestEnabled()) {
                    closePermissionPopupFor(CHOOSE_GOOGLE_ACCOUNT);
                    closePermissionPopupFor(LOCATION);
                    closePermissionPopupFor(NOTIFICATIONS);
                }
                closePermissionPopupFor(USER_TRACKING);

            } else {
                closePermissionPopupFor(DEEPLINK);
                closePermissionPopupFor(NOTIFICATIONS);
            }
    }

    public static synchronized void setSoftAssertionsInstance(Long threadId) {
        softAssertionsMap.put(threadId, new SoftAssertions());
    }

    public static synchronized SoftAssertions getSoftAssertion() {
        return softAssertionsMap.get(Thread.currentThread().getId());
    }

    public abstract <T extends BasePage> T waitUntilPageReady();

    @Step("Tap on coordinates x {xAxis} y {yAxis}")
    public static void tapOnCoordinates(int xAxis, int yAxis) {
        if (isIos()) {
            Map<String, Object> args = new HashMap<>();
            args.put("x", xAxis);
            args.put("y", yAxis);
            getAppiumDriver().executeScript("mobile: tap", args);
        } else if (isAndroid()) {
            PointerInput finger = new PointerInput(org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
            org.openqa.selenium.interactions.Sequence clickPosition = new org.openqa.selenium.interactions.Sequence(finger, 1);
            clickPosition
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), xAxis, yAxis))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            getAppiumDriver().perform(Arrays.asList(clickPosition));
        }
    }

    public static void clickOnElement(SelenideElement element, ElementSide elementSide) {
        if (isIos()) {
            Point elementCoordinates = getElementCoordinates(element, elementSide);
            tapOnCoordinates(elementCoordinates.x, elementCoordinates.y);
        } else {
            clickOnElement(element);
        }
        ReporterUtil.log("Tap on \"" + element.getAlias() + "\"");
    }

    public static void clickOnElement(SelenideElement element) {
        element.shouldBe(visible.because("We need to click on " + element.getAlias())).click();
        ReporterUtil.log("Tap on \"" + element.getAlias() + "\"");
    }

    public static void clickOnElement(SelenideElement element, Duration timeoutDuration) {
        element.shouldBe(visible.because("We need to click on " + element.getAlias()), timeoutDuration).click();
        ReporterUtil.log("Tap on \"" + element.getAlias() + "\"");
    }

    public static void clickOnElement(By elementBy) {
        $(elementBy).shouldBe(visible).click();
        ReporterUtil.log("Tap on \"" + elementBy + "\"");
    }

    public static void clickOnElement(By elementBy, Duration timeoutDuration) {
        $(elementBy).shouldBe(visible, timeoutDuration).click();
        ReporterUtil.log("Tap on \"" + elementBy + "\"");
    }

    public static void clickAboveTheElement(SelenideElement element) {
        int screenHeight = getWindowSize().height;
        int screenWidth = getWindowSize().width;
        int elementHeight = element.getSize().height;
        tapOnCoordinates(screenWidth / 2, (screenHeight - elementHeight) / 2);
        ReporterUtil.log("Tap above the \"" + element.getAlias() + "\"");
    }

    public static void clickOnElementIfDisplayed(SelenideElement element) {
        try {
            element.click();
            ReporterUtil.log("Tap on \"" + element.getAlias() + "\" if displayed");
        } catch (Throwable e) {
            ReporterUtil.log("Impossible to click on element");
        }
    }

    public static void clickOnElementIfDisplayed(SelenideElement element, Duration timeout, boolean... showStackTrace) {
        try {
            element.shouldBe(visible.because("We need to click on " + element.getAlias()), timeout).click();
            ReporterUtil.log("Tap on \"" + element.getAlias() + "\"");
        } catch (Throwable e) {
            boolean stackTraceRequired = showStackTrace.length > 0 ? showStackTrace[0] : true;
            if (stackTraceRequired) {
                ReporterUtil.log("Impossible to click on element " + element.getAlias());
            }
        }
    }

    public static void clickOnNonClickableElement(SelenideElement element) {
        Actions action = new Actions(getAppiumDriver());
        action.click(waitTillVisibilityOfElement(element));
        action.perform();
    }

    @Step("Set value for picker wheel {value}")
    public static void setValueForPickerWheel(SelenideElement wheelPicker, String value) {
        if (isAndroid()) {
            wheelPicker.clear();
            clickOnElement(wheelPicker);
        }
        wheelPicker.setValue(value);
    }

    @Step("Update screen density if needed")
    public void updateScreenDensityIfNeeded() {
        if (isAndroid()) {
            String udid = getUDID();
            if (!config().appAutoLaunch()) launchApp();
            if (config().testOnRealDevices()) {
                continueListeningDialog.closeContinueListeningDialogIfDisplayed();
                if (nowPlayingPage.isOnNowPlayingPage()) {
                    nowPlayingPage
                            .waitUntilPreRollAdDisappearIfDisplayed()
                            .minimizeNowPlayingScreen();
                }
            }
            navigationAction.navigateTo(HOME);
            deepLinksUtil.openTuneThroughDeeplink(STREAM_STATION_KQED);
            if (nowPlayingPage.isShareButtonIsDisplayed()) {
                ReporterUtil.log("Display's density is ok");
                takeScreenshot();
            } else {
                ReporterUtil.log("Automation framework will update display's density");
                takeScreenshot();
                long startTime = System.currentTimeMillis();
                String displayDensityInfo = deviceNativeActions.getCurrentDensityValue(udid);
                int physicalDensity;
                String physicalDensityRaw = StringUtils.substringAfter(displayDensityInfo, "Physical density: ").replaceAll("\\s", "").replaceAll("n", "").replaceAll("\\\\", "").replace("\"}", "");
                if (displayDensityInfo.contains("Override")) {
                    physicalDensityRaw = StringUtils.substringBefore(displayDensityInfo, "Override density:").replace("Physical density: ", "").replaceAll("\\s", "").replaceAll("n", "").replaceAll("\\\\", "");
                }
                if (physicalDensityRaw.contains("stdout")) {
                    physicalDensity = Integer.parseInt(StringUtils.substringAfter(physicalDensityRaw, "stdout\":\""));
                } else {
                    physicalDensity = Integer.parseInt(physicalDensityRaw);
                }
                while (!nowPlayingPage.isShareButtonIsDisplayed(Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()))) {
                    physicalDensity = physicalDensity - 4;
                    deviceNativeActions.changeScreenDensity(udid, physicalDensity);
                    if (System.currentTimeMillis() - startTime > config().fiveMinuteInMilliseconds()) {
                        throw new Error("Display density was not updated");
                    }
                }
                ReporterUtil.log("New display's density: " + physicalDensity);
                takeScreenshot();
            }
            if (!config().testOnRealDevices()) {
                uninstallApp();
                if (config().appAutoLaunch()) {
                    launchApp();
                } else {
                    installApp();
                }
            } else {
                quiteAppiumDriver();
            }
        }
    }

    /* --- General Validation Methods --- */

    @Step("Validate that UI elements are displayed {mapOfElements}")
    public BasePage validateUIElements(HashMap<String, SelenideElement> mapOfElements) {
        for (SelenideElement element : mapOfElements.values()) {
            getSoftAssertion().assertThat(isElementDisplayed(element)).as(element.getAlias() + " is not displayed").isTrue();
        }
        getSoftAssertion().assertAll();
        return this;
    }

    @Step("Validate Text of elements {mapOfElements}")
    public BasePage validateTextOfUIElements(HashMap<String, SelenideElement> mapOfElements) {
        mapOfElements.keySet().stream()
                .filter(keyBeforeFilter -> !keyBeforeFilter.startsWith(SKIP_TEXT_VALIDATION_PREFIX))
                .forEach(key -> {
                    String message = key + " text is not displayed";
                    SelenideElement element = mapOfElements.get(key);
                    getSoftAssertion().assertThat(getElementNameOrLabel(element)).as(message).isEqualTo(key);
                });
        getSoftAssertion().assertAll();
        return this;
    }

    /**
     * Validates that an ad banner is displayed or not displayed; if expected to be displayed, waits for it to display
     * @param expectIsDisplayed If the ad banner is expected to be displayed
     * @return This page as a BasePage
     */
    @Step("Validate that ad banner isDisplayed = {expectIsDisplayed}")
    public BasePage validateAdBannerDisplayed(boolean expectIsDisplayed) {
        SelenideElement banner = $(By.id(AD_BANNER_LOCATOR)).as("Ad banner");
        if (expectIsDisplayed) {
            waitTillVisibilityOfElement(banner, Duration.ofSeconds(config().waitLongTimeoutSeconds()));
        }
        assertThat(banner.exists()).as("Expected ad banner display to be " + expectIsDisplayed + " but was not").isEqualTo(expectIsDisplayed);
        return this;
    }

    @Step("Validate Offline error message")
    public BasePage validateThatYouAreOfflineNowErrorDisplayed() {
        if (isAndroid()) {
            closePermissionPopupsIfDisplayed();
            assertThat(isElementDisplayed(offlineErrorMessageText)).as("You are offline now message not displayed").isTrue();
        }
        return this;
    }

    @Step("Validate Webpage is displayed")
    public BasePage validateWebPageIsDisplayed() {
        AssertionsForClassTypes.assertThat(isElementDisplayed(adsWebPage))
                .as("Expected webpage is not displayed")
                .isTrue();
        return this;
    }

    @Step("Clicking on AdBanner")
    public BasePage clickAdBanner() {
        clickOnElement(By.id(AD_BANNER_LOCATOR));
        return this;
    }

    @Step("Validating app state is {applicationState}")
    public void verifyAppState(ApplicationState applicationState) {
        if (isIos()) {
            assertThat(((IOSDriver) getAppiumDriver()).queryAppState(config().bundleIdIos())).as("The status of application " + applicationState).isEqualTo(applicationState);
        } else {
            assertThat(((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid())).as("The status of application " + applicationState).isEqualTo(applicationState);
        }
    }

    @Step("Validating app is not Running")
    public void verifyAppIsNotRunning() {
        if (isIos()) {
            assertThat(((IOSDriver) getAppiumDriver()).queryAppState(config().bundleIdIos())).as("Application is running").isNotEqualTo(RUNNING_IN_FOREGROUND);
        } else {
            assertThat(((AndroidDriver) getAppiumDriver()).queryAppState(config().appPackageAndroid())).as("Application is running").isNotEqualTo(RUNNING_IN_FOREGROUND);
        }
    }

    @Step("Validate that category header {categoryType} is displayed ")
    public void validateThatCategoryHeaderIsDisplayed(CategoryType categoryType, ScrollDirection direction) {
        SelenideElement element;
        try {
            element = getCategoryHeader(categoryType, direction, 15);
        } catch (Throwable thr) {
            element = null;
        }
        assertThat(element).as("Category header " + categoryType + " is absent").isNotNull();
    }

    @Step("Validate Station has a switch indicator")
    public BasePage verifySwitchIndicatorForStation(String station) {
        //Todo Attribute values are not set to switch indicator for IOS
                String ios = "//XCUIElementTypeOther[contains(@name,'%1$s')]/XCUIElementTypeImage[@name='boostBadge']";
                String android = "(//*[./android.widget.ImageView[@content-desc='%1$s']]//android.widget.ImageView[@content-desc='Switch Station'] | //*[@text='%1$s']/preceding-sibling::android.widget.ImageView[@resource-id='tunein.player:id/row_switch_badge'])";
                SelenideElement element = scrollTo(xpath(String.format((isIos()) ? ios : android, station)), DOWN);
                assertThat(isElementDisplayed(element, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds()))).as(station + "Content tile switch indicator is not displayed").isTrue();
                return this;
    }

    @Step("Validate device's memory value {memoryType} is less than critical value {criticalValue} during timeout {timeout}")
    public void validateDeviceMemoryIsLessThan(DeviceNativeActions.MemoryInfoType memoryType, int criticalValue, Duration timeout, CSVWriter... fileTowrite) {
        if (isAndroid()) {
            String sessionId = getAppiumDriver().getSessionId().toString();
            boolean moreThanCriticalMemory = false;
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < timeout.toMillis()) {
                customWait(Duration.ofSeconds(30));
                int retry = 0;
                int currentValue = deviceNativeActions.getDeviceMemoryInfo(memoryType);
                if (fileTowrite.length > 0) {
                    String[] strArray = {
                            sessionId,
                            String.valueOf(System.currentTimeMillis()),
                            String.valueOf(currentValue)
                    };
                    fileTowrite[0].writeNext(strArray);
                }
                if (currentValue > criticalValue) {
                    while (retry < 3) {
                        customWait(Duration.ofSeconds(5));
                        currentValue = deviceNativeActions.getDeviceMemoryInfo(memoryType);
                        if (fileTowrite.length > 0) {
                            String[] strArray = {
                                    sessionId,
                                    String.valueOf(System.currentTimeMillis()),
                                    String.valueOf(currentValue)
                            };
                            fileTowrite[0].writeNext(strArray);
                        }
                        moreThanCriticalMemory = currentValue > criticalValue;
                        retry += 1;
                    }
                }
                ReporterUtil.log("Current memory value: " + currentValue);
                assertThat(moreThanCriticalMemory)
                        .as("Device's Memory value " + memoryType.getMemoryInfoTypeName() + ":" + currentValue + " is bigger than critical value {criticalValue}")
                        .isFalse();
            }
        }
    }

    @Step("Tap on Offline Retry button")
    public BasePage tapOnOfflineRetryButtonIfDisplayed() {
        if (isAndroid()) {
            if (isElementDisplayed(offlineErrorRetryButton)) {
                clickOnElement(offlineErrorRetryButton);
            }
        }
        return this;
    }

    @Step("Tap on category header {categoryType}")
    public ContentsListPage tapOnCategoryHeader(CategoryType categoryType, ScrollDirection direction) {
        String locator = isAndroid() ? ANDROID_CATEGORY_HEADER_LOCATOR : IOS_CATEGORY_HEADER_LOCATOR;
        clickOnElement(scrollTo(xpath(String.format(locator, categoryType.getCategoryTypeValue())), direction));
        return contentsListPage.waitUntilPageReady();
    }

    @Step("Get category header {categoryType}")
    protected SelenideElement getCategoryHeader(CategoryType categoryType, ScrollDirection direction, int... numberOfScrolls) {
        String locator = isAndroid() ? ANDROID_CATEGORY_HEADER_LOCATOR : IOS_CATEGORY_HEADER_LOCATOR;
        SelenideElement element = scrollTo(xpath(String.format(locator, categoryType.getCategoryTypeValue())), direction, numberOfScrolls);
        return element;
    }

    @Step("Tap on content under category {categoryType} with index {index}")
    public void openContentUnderCategoryWithHeader(CategoryType categoryType, ViewModelType viewModelType, ScrollDistance scrollDistance, int index,
                                                   boolean isLongPress, Boolean... skipTestIfCategoryNotFound) {
        String ios;
        String android;
        switch (viewModelType) {
            case LIST -> {
                ios = "(//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%1$s')]/following-sibling::*/XCUIElementTypeOther[./*[@name='phone_speaker_high']] | //*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%1$s')]/following-sibling::*//XCUIElementTypeCell | //*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%1$s')]/following-sibling::XCUIElementTypeCell[./*[(@name='more options') or (@name='UPGRADE NOW') or (@name='Play') or (@name='Notify Me') or (@type='XCUIElementTypeImage') or (@name='CloseCell')]])[%2$s]";
                android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "),'%s') and (contains(@resource-id,'container_title') or contains(@resource-id,'text'))]]/following-sibling::*[.//*[contains(@resource-id,'cell_title') or contains(@resource-id,'cell_text') or contains(@resource-id,'first_team_name') or contains(@resource-id,'text1')]][%s]";
            }
            case TILE -> {
                ios = "((//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::*/XCUIElementTypeCollectionView)[1]/XCUIElementTypeCell)[%s]";
                android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "),'%s')]]/following-sibling::android.widget.ScrollView/android.widget.GridView/android.view.ViewGroup[%s]";
            }
            default -> throw new IllegalStateException("Unexpected value: " + viewModelType);
        }

        // Tap on the very first item under category type (example first item under `Events`)
        // TODO: Add logic to scroll horizontally through tiles; until then, only check the first few indices
        String format = String.format((isIos()) ? ios : android, categoryType.getCategoryTypeValue().toLowerCase(), index);
        try {
            if (isLongPress) {
                longPressOnElement(scrollTo(xpath(format), DOWN, scrollDistance, 35).as("Content with index " + index + " under category " + categoryType.getCategoryTypeValue()));
            } else {
                clickOnElement(scrollTo(xpath(format), DOWN, scrollDistance, 35).as("Content with index " + index + " under category " + categoryType.getCategoryTypeValue()));
            }
        } catch (Throwable e) {
            if (skipTestIfCategoryNotFound.length > 0 && skipTestIfCategoryNotFound[0]) {
                throw new SkipException("Skipping this test: " + e);
            } else {
                throw e;
            }
        }

        if (contentProfilePage.isOnContentProfilePage()) {
            contentProfilePage.waitForProperPage(categoryType);
        } else if (nowPlayingPage.isOnNowPlayingPage()) {
            nowPlayingPage.waitUntilPageReady();
        } else if (contentListItemDialog.isOnContentListItemDialog()) {
            contentListItemDialog.waitUntilPageReady();
        } else if (upsellPage.isOnUpsellPage()) {
            upsellPage.waitUntilPageReady();
        } else if (carModePage.isOnCarModePage()) {
            carModePage.waitUntilPageReady();
        } else {
            contentsListPage.waitUntilPageReady();
        }
    }

    @Step("Tap on content with label {contentLabel} under category {categoryType}")
    public void openContentWithLabelUnderCategoryWithHeader(CategoryType categoryType, ViewModelType viewModelType, ScrollDistance scrollDistance, String contentLabel, int... numberOfScrolls) {
        int numberScrolls = numberOfScrolls.length > 0 ? numberOfScrolls[0] : 14;
        switch (viewModelType) {
            case LIST -> {
                String ios = "//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::XCUIElementTypeCell[./*[substring-before(@name,'. ')='%s']]";
                String android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%s')]]/following-sibling::*[./*[@text='%s']]";
                String format = String.format((isIos()) ? ios : android, categoryType.getCategoryTypeValue().toLowerCase(), contentLabel);
                clickOnElement(scrollTo(xpath(format), DOWN, scrollDistance, numberScrolls));
            }
            case TILE -> {
                String iosCardLocator = "(//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::*/XCUIElementTypeCollectionView)[1]";
                String iosItemLocator = "/XCUIElementTypeCell[.//*[@label='%s']]";
                String androidCardLocator = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%1$s')]]/following-sibling::android.widget.ScrollView/android.widget.GridView[.//android.widget.TextView]";
                String androidItemLocator = "/android.view.ViewGroup[(./android.widget.TextView[@text='%2$s']) or (.//android.widget.ImageView[@content-desc='%2$s'])]";
                SelenideElement requiredCard = scrollTo(xpath(String.format(isIos() ? iosCardLocator : androidCardLocator, categoryType.getCategoryTypeValue().toLowerCase())), DOWN, scrollDistance, numberScrolls);
                clickOnElement(swipeToElement(xpath(String.format((isIos() ? iosCardLocator + iosItemLocator : androidCardLocator + androidItemLocator), categoryType.getCategoryTypeValue().toLowerCase(), contentLabel)), 8, LEFT, requiredCard));
            }
            default -> throw new IllegalStateException("Unexpected value: " + categoryType);
        }

        if (contentProfilePage.isOnContentProfilePage()) {
            contentProfilePage.waitForProperPage(categoryType);
        } else if (nowPlayingPage.isOnNowPlayingPage()) {
            nowPlayingPage.waitUntilPageReady();
        } else if (contentListItemDialog.isOnContentListItemDialog()) {
            contentListItemDialog.waitUntilPageReady();
        } else if (upsellPage.isOnUpsellPage()) {
            upsellPage.waitUntilPageReady();
        } else {
            contentsListPage.waitUntilPageReady();
        }
    }
    
    @Step("Get content under category {categoryType} with index {index}")
    public SelenideElement getContentUnderCategoryWithHeader(CategoryType categoryType, ViewModelType viewModelType, ScrollDistance scrollDistance, int index) {
        String ios;
        String android;
        switch (viewModelType) {
            case LIST -> {
                ios = "(//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::*/XCUIElementTypeOther)[%s]";
                android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%s')]]/following-sibling::*[./*[contains(@resource-id,'cell_title') or contains(@resource-id,'cell_text') or contains(@resource-id,'first_team_name')]][%s]";
            }
            case TILE -> {
                ios = "((//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::*/XCUIElementTypeCollectionView)[1]/XCUIElementTypeCell)[%s]";
                android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%s')]]/following-sibling::android.widget.ScrollView/android.widget.GridView/android.view.ViewGroup[%s]";
            }
            default -> throw new IllegalStateException("Unexpected value: " + categoryType);
        }

        String format = String.format((isIos()) ? ios : android, categoryType.getCategoryTypeValue().toLowerCase(), index);
        return scrollTo(xpath(format), DOWN, scrollDistance, 35);
    }

    @Step("Get content title under category {categoryType} with index {index}")
    public String getContentTitleUnderCategoryWithHeader(CategoryType categoryType, ViewModelType viewModelType, ScrollDistance scrollDistance, int index) {
        SelenideElement element = getContentUnderCategoryWithHeader(categoryType, viewModelType, scrollDistance, index);
        if (isAndroid()) {
            return getElementText(element.$x("*//android.widget.TextView[@resource-id='tunein.player:id/row_tile_title']"));
        } else {
            return getElementNameOrLabel(element.$x("*//XCUIElementTypeOther[@name='tileCellViewIdentifier']"));
        }
    }

    @Step("Get content with label {contentLabel} under category {categoryType}")
    public SelenideElement getContentWithLabelUnderCategoryWithHeader(CategoryType categoryType, ViewModelType viewModelType, ScrollDistance scrollDistance, String contentLabel, int... scrollNumbers) {
        int numberOfScrolls = scrollNumbers.length > 0 ? scrollNumbers[0] : 14;
        switch (viewModelType) {
            case LIST -> {
                String ios = "//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::XCUIElementTypeCell[./*[substring-before(@name,'. ')='%s']]";
                String android = "//*[.//*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%s')]]/following-sibling::*[./*[@text='%s']]";
                String format = String.format((isIos()) ? ios : android, categoryType.getCategoryTypeValue().toLowerCase(), contentLabel);
                return scrollTo(xpath(format), DOWN, scrollDistance, numberOfScrolls);
            }
            case TILE -> {
                String iosCardLocator = "(//*[contains(translate(@name," + UPPER_CASE + "," + LOWER_CASE + "),'%s')]/following-sibling::*/XCUIElementTypeCollectionView)[1]";
                String iosItemLocator = "/XCUIElementTypeCell[.//*[@label='%s']]";
                String androidCardLocator = "//*[./*[contains(translate(@text, " + UPPER_CASE + ", " + LOWER_CASE + "), '%1$s')]]/following-sibling::android.widget.ScrollView/android.widget.GridView[(.//android.widget.TextView) or (.//android.view.ViewGroup)]";
                String androidItemLocator = "/android.view.ViewGroup[(./android.widget.TextView[@text='%2$s']) or (.//android.widget.ImageView[@content-desc='%2$s'])]";
                SelenideElement requiredCard = scrollTo(xpath(String.format(isIos() ? iosCardLocator : androidCardLocator, categoryType.getCategoryTypeValue().toLowerCase())), DOWN, scrollDistance, numberOfScrolls);
                return swipeToElement(xpath(String.format((isIos() ? iosCardLocator + iosItemLocator : androidCardLocator + androidItemLocator), categoryType.getCategoryTypeValue().toLowerCase(), contentLabel)), 8, LEFT, requiredCard);
            }
            default -> throw new IllegalStateException("Unexpected value: " + categoryType);
        }
    }

    public static int getItemsNumberUnderCategory(CategoryType categoryType) {
        String locator = isAndroid() ? ANDROID_CONTENT_LIST_UNDER_CATEGORY_LOCATOR : IOS_CONTENT_LIST_UNDER_CATEGORY_LOCATOR;
        return $$(xpath(String.format(locator, categoryType.getCategoryTypeValue()))).size();
    }

    public static ElementsCollection getItemsUnderCategory(CategoryType categoryType) {
        String locator = isAndroid() ? ANDROID_CONTENT_LIST_UNDER_CATEGORY_LOCATOR : IOS_CONTENT_LIST_UNDER_CATEGORY_LOCATOR;
        return $$(xpath(String.format(locator, categoryType.getCategoryTypeValue())));
    }

    public String getNavigationTitleText() {
        switch (config().mobileOS()) {
            case IOS -> {
                return getElementText($(xpath("*//XCUIElementTypeNavigationBar//following-sibling::XCUIElementTypeStaticText[1]")));
            }
            case ANDROID -> {
                return getElementText($(id("toolbar_title")));
            }
            default -> throw new IllegalStateException("Unexpected value: " + config().mobileOS());
        }
    }

    protected void handlePageAfterPlayButton(Contents... content) {
        if (upsellPage.isOnUpsellPage()) {
            upsellPage.waitUntilPageReady();
        } else {
            if (content.length > 0 && !(content[0].getStreamType()).equals("")) {
                nowPlayingPage.waitUntilPageReadyWithKnownContent(content[0]);
            } else {
                nowPlayingPage.waitUntilPageReady();
            }
        }
    }

    public enum ViewModelType {
        LIST, TILE, CAR_MODE_LIST
    }

    public enum CategoryType {
        CATEGORY_TYPE_SHOWS("Shows"),
        CATEGORY_TYPE_PODCASTS("Podcasts"),
        CATEGORY_TYPE_FEATURED("Featured"),
        CATEGORY_TYPE_LIVE_EVENTS("Live Events"),
        CATEGORY_TYPE_EVENTS("Events"),
        CATEGORY_TYPE_SCHEDULED("Today's Schedule"),
        CATEGORY_TYPE_UPCOMING_EVENTS("Upcoming Events"),
        CATEGORY_TYPE_RELATED_STATIONS("Related Stations"),
        CATEGORY_TYPE_STATIONS("Stations"), // Now Playing
        CATEGORY_TYPE_LISTEN_ON_A_LIVE_STATION("Listen on a live station"),
        CATEGORY_TYPE_EPISODES("Episodes"),
        CATEGORY_TYPE_PREMIUM_EXCLUSIVE_SPORTS("Premium Exclusive Sports"),
        CATEGORY_TYPE_NEW_COMMERCIAL_FREE_STATIONS("New Commercial-Free Stations"),
        CATEGORY_TYPE_TOP_25_PODCASTS("This Week's Top 25 Podcasts"),
        CATEGORY_TYPE_HONORING_BLACK_HISTORY_MONTH("Honoring Black History Month"),
        CATEGORY_TYPE_TOP_10_PODCASTS("Top 10 Podcasts"),
        CATEGORY_TYPE_TOP_PODCASTS("Top Podcasts"),

        CATEGORY_TYPE_NEWS_PODCASTS("News Podcasts"),
        CATEGORY_TYPE_TOP_BOOK_SERIES("Top Book Series"),
        CATEGORY_TYPE_TOP_PODCASTS_IN_YOUR_COUNTRY("Top Podcasts in Your Country"),
        CATEGORY_TYPE_POPULAR_PODCASTS_IN_YOUR_AREA("Popular Podcasts in Your Area"),
        CATEGORY_TYPE_TOP_PODCASTS_GLOBALLY("Top Podcasts Globally"),

        CATEGORY_TYPE_POPULAR_STATIONS_IN_YOUR_AREA("Popular Stations in Your Area"),
        // ---Upsell or Now Playing ---
        CATEGORY_TYPE_COMMERCIAL_FREE_NEWS("Commercial-Free News"), // Upsell or Now Playing
        CATEGORY_TYPE_COMMERCIAL_FREE("Commercial-Free"), // Upsell or Now Playing
        CATEGORY_TYPE_LISTEN_COMMERCIAL_FREE("Listen Commercial-Free"),
        CATEGORY_TYPE_MOST_POPULAR_NEWS_STATIONS("Most Popular News Stations"),
        CATEGORY_TYPE_LOCAL_NEWS_RADIO("Local News Radio"),
        CATEGORY_TYPE_COMMERCIAL_FREE_MUSIC("Commercial-Free Music"), // Upsell or Now Playing
        CATEGORY_TYPE_COMMERCIAL_FREE_CALMING_MUSIC("Commercial-Free Calming Music"), // Upsell or Now Playing
        CATEGORY_TYPE_PREMIUM_EPISODES("Premium Episodes"),
        CATEGORY_TYPE_TODAY_TOP_HITS("Today's Top Hits"),
        CATEGORY_TYPE_ARTISTS("Artists"),
        CATEGORY_TYPE_ALBUMS("Albums"),
        CATEGORY_TYPE_SUGGESTED_ARTIST("Suggestions (Artist)"),
        CATEGORY_TYPE_Music("Music"),
        CATEGORY_TYPE_GENRE("Genre"),
        CATEGORY_TYPE_SONGS("Songs"),
        CATEGORY_TYPE_STATIONS_THAT_PLAY("Stations that play"),
        CATEGORY_TYPE_JOIN_THE_CONVERSATION("Join the Conversation"),
        CATEGORY_TYPE_CATCH_UP_QUICK("Catch Up Quick"), // Browsies: News & Talk
        CATEGORY_TYPE_TOP_40("Commercial-Free Music: Top 40"), // Premium page
        CATEGORY_TYPE_TOP_STATIONS("Top Stations"),
        CATEGORY_TYPE_TOP_LOCAL_STATIONS("Top Local Stations"),
        CATEGORY_TYPE_TOP_LOCAL_NEWS("Top Local News"),

        // ---Team Podcasts ---
        CATEGORY_TYPE_TEAM_PODCASTS("Team Podcasts"),
        CATEGORY_TYPE_FM("FM"),
        CATEGORY_TYPE_AM("AM"),
        BY_LOCATION_REGIONS_CATEGORY("Regions"),
        BY_LOCATION_EXPLORE_CATEGORY("Explore"),
        BY_LOCATION_STATIONS_CATEGORY("Stations"),
        CATEGORY_EXPLORE("Explore"),
        CATEGORY_LANGUAGES("Languages"),
        CATEGORY_CATEGORIES("Categories"),
        // --- Sports ---
        CATEGORY_TYPE_EXPLORE_BY_SPORT("Explore by Sport"),
        CATEGORY_TYPE_EXPLORE_BY_CATEGORY("Explore By Category"),
        CATEGORY_TYPE_FOOTBALL("Football"),
        CATEGORY_TYPE_PRO_FOOTBALL("Pro Football"),
        CATEGORY_TYPE_FOOTBALL_AFC_EAST("AFC East"),
        CATEGORY_TYPE_BASEBALL("Baseball"),
        CATEGORY_TYPE_BASEBALL_AL_EAST("AL East"),
        CATEGORY_TYPE_TEAMS("Teams"),
        // --- Music ---
        CATEGORY_TYPE_TOP_MUSIC_GENRES("Top Music Genres"),
        CATEGORY_TYPE_GLOBAL_MUSIC_RADIO("Global Music Radio"),
        CATEGORY_TYPE_EXPLORE("Explore"),
        TRUECRIME_PODCASTS("Top True Crime Podcasts"),
        IHEARTRADIO_STATIONS("iHeartRadio Stations"),
        RECENTS("RECENTS"),
        RECOMMENDED("Recommended"),
        FAVORITES_CATEGORY("Favorites"),
        TOP_IHEARTRADIO_STATION("Top iHeartRadio Stations"),
        WHAT_DO_YOU_WANT_TO_LISTEN_TO("What Do You Want To Listen To?"),
        JUST_FOR_YOU("Just For You"),
        FEATURED_NEWS_STATIONS("Featured News Stations"),
        POPULAR_IN_SPORTS("Popular In Sports"),
        THIS_WEEKS_TOP25_PODCASTS("This Weeks Top 25 Podcasts"),
        COMEDY_PODCASTS("Comedy Podcasts"),
        SCIENCE_PODCASTS("Science Podcasts"),
        LOCAL_RADIO("Local Radio"),
        LOCAL_PUBLIC_RADIO("Local Public Radio"),
        YOUR_GAMES("Your Games"),
        FEATURED_PODCASTS("Featured Podcasts"),
        THIS_WEEKS_FEATURED_PODCASTS("This Week's Featured Podcasts"),
        NATIONAL_AND_WORLD_NEWS("National & World News"),
        CONSERVATIVE_TALK("Conservative Talk"),
        RESULTS_CATEGORY("Results"),
        ADD_YOUR_FAVORITES("Add Your Favorites"),
        SPORTS_TALK_PODCASTS("Sports Talk Podcasts");;

        private String categoryTypeValue;

        private CategoryType(String categoryTypeValue) {
            this.categoryTypeValue = categoryTypeValue;
        }

        public String getCategoryTypeValue() {
            return categoryTypeValue;
        }

        public static CategoryType getCategoryType(final String categoryTypeTitle) {
            List<CategoryType> categoryTypesList = Arrays.asList(CategoryType.values());
            return categoryTypesList.stream().filter(eachContent -> eachContent.toString().equals(categoryTypeTitle))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return categoryTypeValue;
        }
    }

}
