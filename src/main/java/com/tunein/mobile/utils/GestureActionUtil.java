package com.tunein.mobile.utils;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.google.common.collect.ImmutableList;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.*;
import static com.tunein.mobile.utils.ElementHelper.*;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDistance.*;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.LaunchArgumentsTypes.BANNER_ADS;
import static com.tunein.mobile.utils.LaunchArgumentsUtil.isLaunchArgumentKeysSet;
import static com.tunein.mobile.utils.ScreenshotUtil.takeScreenshot;
import static com.tunein.mobile.utils.WaitersUtil.waitVisibilityOfElement;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.interactions.PointerInput.Kind.TOUCH;

public class GestureActionUtil {

    private static final Duration SCROLL_DUR = Duration.ofMillis(config().defaultScrollDuration());

    private static final int ANDROID_SCROLL_DIVISOR = 3;

    private static Dimension windowSize;

    /* --- Scrolling and Swiping Methods --- */

    private static void swipe(Point start, Point end, Duration duration) {
        PointerInput input = new PointerInput(TOUCH, "finger");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(input.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        if (isAndroid()) {
            duration = duration.dividedBy(ANDROID_SCROLL_DIVISOR);
        } else if (isIos()) {
            swipe.addAction(new Pause(input, duration));
            duration = Duration.ZERO;
        }
        swipe.addAction(input.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(input.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        getAppiumDriver().perform(ImmutableList.of(swipe));
    }

    public static void swipe(Point start, Point end) {
       swipe(start, end, SCROLL_DUR);
    }

    public static void swipeElement(SwipeDirection dir, double distance, SelenideElement element) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension elementSize = element.getSize();
        Point elementLocation = element.getLocation();
        Point midPoint = new Point((elementLocation.x + elementSize.width / 2), (elementLocation.y + elementSize.height / 2));
        int top = midPoint.y - (int) ((elementSize.height * distance) * 0.6);
        int bottom = midPoint.y + (int) ((elementSize.height * distance) * 0.6);
        int left = midPoint.x - (int) ((elementSize.width * distance) * 0.6);
        int right = midPoint.x + (int) ((elementSize.width * distance) * 0.6);
        if (dir == SwipeDirection.UP) {
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (dir == SwipeDirection.DOWN) {
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (dir == SwipeDirection.RIGHT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else if (dir == SwipeDirection.LEFT) {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    public static SelenideElement swipeToElement(By elementBy, int numberOfSwipes, SwipeDirection direction, SelenideElement parent, ScrollDistance... distance) {
        float distanceValue = distance.length > 0 ? (float) distance[0].getValue() : config().defaultSwipeDistance();
        for (int i = 0; i <= numberOfSwipes; i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementFullyVisible(elementBy)) {
                return $(elementBy);
            }
            if (i != numberOfSwipes) {
                swipeElement(direction, distanceValue, parent);
            }
        }
        throw new Error("Cannot find element " + elementBy + " after swipe " + direction);
    }

    public static SelenideElement swipeToElement(SelenideElement element, int numberOfSwipes, SwipeDirection direction, SelenideElement parent, ScrollDistance... distance) {
        float distanceValue = distance.length > 0 ? (float) distance[0].getValue() : config().defaultSwipeDistance();
        for (int i = 0; i <= numberOfSwipes; i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementFullyVisible(element)) return element;
            if (i != numberOfSwipes) swipeElement(direction, distanceValue, parent);
        }
        throw new Error("Cannot find element after swipe " + direction);
    }

    private static void scroll(ScrollDirection dir, double distance) {
        if (distance < 0 || distance > 1) {
            throw new Error("Scroll distance must be between 0 and 1");
        }
        Dimension size = getWindowSize();
        int heightValue = size.height - getAddBannerHeight() - getTabBarHeight();
        int widthValue = size.width;
        Point midPoint = new Point((int) (widthValue * 0.5), (int) (heightValue * 0.5));
        int top = midPoint.y - (int) ((heightValue * distance) * 0.5);
        int bottom = midPoint.y + (int) ((heightValue * distance) * 0.5);
        int left = midPoint.x - (int) ((widthValue * distance) * 0.5);
        int right = midPoint.x + (int) ((widthValue * distance) * 0.5);
       SelenideElement displayedButton = getDisplayedButtonInRange(top, bottom, dir);
        int buttonTopY = 0;
        int buttonBottomY = 0;
        if (displayedButton != null) {
            buttonTopY = displayedButton.getLocation().y;
            buttonBottomY = buttonTopY + displayedButton.getSize().height;
        }
        if (dir == ScrollDirection.UP) {
            if (displayedButton != null) {
                if (buttonTopY <= top && top <= buttonBottomY) {
                    top = buttonBottomY + 10;
                    bottom = bottom + buttonBottomY - top + 10;
                }
            }
            swipe(new Point(midPoint.x, top), new Point(midPoint.x, bottom), SCROLL_DUR);
        } else if (dir == DOWN) {
            if (displayedButton != null) {
                if (buttonTopY <= bottom && bottom <= buttonBottomY) {
                    top = top + bottom - buttonTopY - 10;
                    bottom = buttonTopY - 10;
                }
            }
            swipe(new Point(midPoint.x, bottom), new Point(midPoint.x, top), SCROLL_DUR);
        } else if (dir == ScrollDirection.LEFT) {
            swipe(new Point(left, midPoint.y), new Point(right, midPoint.y), SCROLL_DUR);
        } else {
            swipe(new Point(right, midPoint.y), new Point(left, midPoint.y), SCROLL_DUR);
        }
    }

    @Step("Scroll {dir}")
    public static void scroll(ScrollDirection dir, ScrollDistance... scrollDistance) {
        if (scrollDistance.length > 0) {
            ScrollDistance ratioType = scrollDistance[0];
            scroll(dir, ratioType.getValue());
        } else {
            scroll(dir, SHORT.getValue());
        }
    }

    @Step("Scroll to refresh page contents")
    public static void scrollToRefresh() {
        scroll(ScrollDirection.UP, isAndroid() ? MEDIUM : LONG);
    }

    @Step("Scroll to refresh page contents with distance")
    public static void scrollToRefresh(ScrollDistance distance, Object... currentPage) {
        scroll(GestureActionUtil.ScrollDirection.UP, distance);
        if (currentPage.length > 0) closePermissionPopupsIfDisplayed();
    }

    @Step("Scroll to {elementBy}")
    public static SelenideElement scrollTo(By elementBy, ScrollDirection direction, int... numberOfScrolls) {
        if (isElementFullyVisible(elementBy)) return $(elementBy);
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            scroll(direction);
            if (isElementFullyVisible(elementBy)) return $(elementBy);
        }
        throw new Error("Cannot find element after scroll " + direction);
    }

    @Step("Scroll to {elementBy}")
    public static SelenideElement scrollTo(By elementBy, ScrollDirection direction, ScrollDistance scrollDistance, int... numberOfScrolls) {
        if (isElementFullyVisible(elementBy)) return $(elementBy);
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            scroll(direction, scrollDistance);
            if (isElementFullyVisible(elementBy)) return $(elementBy);
        }
        throw new Error("Cannot find element after scroll " + direction);
    }

    @Step("Scroll to {element}")
    public static SelenideElement scrollTo(SelenideElement element, ScrollDirection direction, int... numberOfScrolls) {
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementFullyVisible(element)) return element;
            if (i != getScrollNumber(numberOfScrolls)) scroll(direction);
        }
        throw new Error("Cannot find element after scroll " + direction);
    }

    @Step("Scroll {direction} {numberOfScrolls} times")
    public static void scroll(ScrollDirection direction, int numberOfScrolls, ScrollDistance... scrollDistance) {
        for (int i = 0; i < numberOfScrolls; i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            scroll(direction, scrollDistance);
        }
    }

    @Step("Scroll to {element}")
    public static SelenideElement scrollTo(SelenideElement element, ScrollDirection direction, ScrollDistance scrollDistance, int... numberOfScrolls) {
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementFullyVisible(element)) return element;
            if (i != getScrollNumber(numberOfScrolls)) scroll(direction, scrollDistance);
        }
        throw new Error("Cannot find element after scroll " + direction);
    }

    @Step("Scroll to {direction} {numberOfScrolls} time(s) until {element} is fully visible after {timeout} seconds")
    public static SelenideElement scrollToElementWithoutError(SelenideElement element,
                                                         ScrollDirection direction,
                                                         ScrollDistance scrollDistance,
                                                         int numberOfScrolls,
                                                         int timeout
    ) {
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementFullyVisible(element, timeout)) return element;
            if (i != getScrollNumber(numberOfScrolls)) scroll(direction, scrollDistance);
        }
        ReporterUtil.log("\"Cannot find element after scrolling \"" + direction);
        return null;
    }

    @Step("Scroll until element disappear {elementBy}")
    public static SelenideElement scrollUntilElementDisappear(By elementBy, ScrollDirection direction, int... numberOfScrolls) {
        for (int i = 0; i <= getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementNotDisplayed(elementBy, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) return $(elementBy);
            if (i != getScrollNumber(numberOfScrolls)) scroll(direction);
        }
        throw new Error("Found element after scroll " + direction);
    }

    @Step("Scroll until element disappear {element}")
    public static int scrollUntilElementDisappear(SelenideElement element, ScrollDirection direction, int... numberOfScrolls) {
        for (int i = 0; i < getScrollNumber(numberOfScrolls); i++) {
            if (i <= 2) {
                takeScreenshot();
                ReporterUtil.log(getAppiumDriver().getPageSource());
            }
            if (isElementNotDisplayed(element, Duration.ofSeconds(config().waitShortTimeoutSeconds()))) return i;
            if (i != getScrollNumber(numberOfScrolls)) scroll(direction);
        }
        throw new Error("Found element after scroll " + direction);
    }

    @Step
    public static void scrollIfAdBannerIsDisplayed() {
        if (isAndroid() && $(By.id("ad_container_banner")).exists()) {
            scroll(DOWN, SHORT);
        }
    }

    private static int getAddBannerHeight() {
        if (isAndroid()) {
            if (isLaunchArgumentKeysSet(BANNER_ADS, "true")) {
                SelenideElement element = $(By.id("ad_container_banner"));
                if (element.exists()) return element.getSize().height;
            }
        }
        return 0;
    }

    private static int getMiniPlayerHeight() {
        if (isAndroid()) {
            SelenideElement element = $(By.id("mini_player_container"));
            if (element.exists()) return element.getSize().height;
        } else if (isIos()) {
            SelenideElement element = $(accessibilityId("miniPlayerView"));
            if (element.exists()) return element.getSize().height;
        }
        return 0;
    }

    private static SelenideElement getDisplayedButtonInRange(int topY, int bottomY, ScrollDirection dir) {
        if (isIos()) {
            ElementsCollection elements = $$(iOSNsPredicateString("name IN {'profilePlayButton', 'pauseButton', 'playbackSpeed', 'playButton', 'START EXPLORING', 'Start Listening Now'} AND visible == true AND type == 'XCUIElementTypeButton'"));
            if (elements.size() > 0) {
                int pointY = 0;
                switch (dir) {
                   case DOWN -> {
                       pointY = bottomY;
                   }
                   case UP -> {
                       pointY = topY;
                   }
                    default -> {
                       return null;
                   }
                }
                for (SelenideElement button: elements) {
                    int buttonTopY = button.getCoordinates().onPage().y;
                    int buttonBottomY = buttonTopY + button.getSize().height;
                    if (buttonTopY <= pointY && pointY <= buttonBottomY) {
                        return button;
                    }
                }
            }
        }
        return null;
    }

    private static int getTabBarHeight() {
        if (isAndroid() && isElementDisplayed($(By.id("bottom_navigation")))) {
            return $(By.id("bottom_navigation")).getSize().height;
        } else if (isIos() && isElementDisplayed($(iOSNsPredicateString("name == 'TabBarViewControllerId'")))) {
            return $(iOSNsPredicateString("name == 'TabBarViewControllerId'")).getSize().height;
        }
        return 0;
    }

    public static void swipeDownScreen() {
        Dimension dimensions;
        int height, width, startX = 0, startY = 0, endX = 0, endY = 0;
        dimensions = getAppiumDriver().manage().window().getSize();
        height = dimensions.getHeight();
        width = dimensions.getWidth();
        startX = width / 2;
        startY = (height / 2) - (height / 4);
        endX = width / 2;
        endY = height;
        swipe(new Point(startX, startY), new Point(endX, endY));
    }

    @Step("Drag and drop an element from one position to another position")
    public static void dragAndDropAction(SelenideElement startElement, SelenideElement endElement, PointerInput.MouseButton button) {
        int offset = button == PointerInput.MouseButton.LEFT ? endElement.getSize().width : endElement.getSize().width * -1;

        PointerInput finger = new PointerInput(TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), endElement.getLocation().x + offset, endElement.getLocation().y + endElement.getSize().height / 2));
        longPress.addAction(finger.createPointerDown(button.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(5000), PointerInput.Origin.viewport(), startElement.getLocation().x, startElement.getLocation().y - endElement.getSize().height / 3));
        longPress.addAction(finger.createPointerUp(button.asArg()));
        getAppiumDriver().perform(Collections.singletonList(longPress));
    }

    private static int getScrollNumber(int... numberOfScrolls) {
        if (numberOfScrolls == null) {
            return config().defaultScrollsNumber();
        } else {
            return (numberOfScrolls.length > 0) ? numberOfScrolls[0] : config().defaultScrollsNumber();
        }
    }

    @Step("Checking if element fully visible")
    public static boolean isElementFullyVisible(By elementBy) {
        int screenHeight = getWindowSize().height;
        int screenWidth = getWindowSize().width;
        try {
            if (waitVisibilityOfElement(elementBy, Duration.ofSeconds(config().waitVeryShortTimeoutSeconds())) != null) {
                SelenideElement element = $(elementBy);
                // Return `true` when get serial id from device is enabled/used
                if (config().isAppiumStreamTestEnabled() && config().appiumStreamGetSerialIdFromDevice()) return true;
                int elementTopPoint = element.getLocation().y;
                int elementBottomPoint = elementTopPoint + element.getSize().height;
                int elementLeftPoint = element.getLocation().x;
                int elementRightPoint = elementLeftPoint + element.getSize().width;
                if ((elementTopPoint >= 0) && (elementBottomPoint <= screenHeight - getAddBannerHeight() - getTabBarHeight() - getMiniPlayerHeight())) {
                    return (elementLeftPoint >= 0) && (elementRightPoint <= screenWidth);
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    @Step("Checking if element fully visible {element}")
    public static boolean isElementFullyVisible(SelenideElement element, int... timeout) {
        int screenHeight = getWindowSize().height;
        int screenWidth = getWindowSize().width;
        int waitTimeout = (timeout.length > 0) ? timeout[0] : config().waitVeryShortTimeoutSeconds();
        try {
            if (isElementDisplayed(element, Duration.ofSeconds(waitTimeout))) {
                // Return `true` when get serial id from device is enabled/used
                if (config().isAppiumStreamTestEnabled() && config().appiumStreamGetSerialIdFromDevice()) return true;
                int elementTopPoint = element.getLocation().y;
                int elementBottomPoint = elementTopPoint + element.getSize().height;
                int elementLeftPoint = element.getLocation().x;
                int elementRightPoint = elementLeftPoint + element.getSize().width;
                if ((elementTopPoint >= 0) && (elementBottomPoint <= screenHeight - getAddBannerHeight() - getTabBarHeight() - getMiniPlayerHeight())) {
                    return (elementLeftPoint >= 0) && (elementRightPoint <= screenWidth);
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    @Step("Checking if element fully visible")
    public static boolean isElementFullyVisibleWithoutWaitTimeout(SelenideElement element) {
        int screenHeight = getWindowSize().height;
        int screenWidth = getWindowSize().width;

        try {
            if (element.isDisplayed()) {
                int elementTopPoint = element.getLocation().y;
                int elementBottomPoint = elementTopPoint + element.getSize().height;
                int elementLeftPoint = element.getLocation().x;
                int elementRightPoint = elementLeftPoint + element.getSize().width;
                int defaultScreenHeightValue = screenHeight - getAddBannerHeight() - getTabBarHeight() - getMiniPlayerHeight();

                /*
                 * For the get serial id method use
                 *
                 * 1. ios -> `screenHeight`
                 * 2. android -> `screenHeight - getAddBannerHeight()`
                 * 3. default -> `screenHeight - getAddBannerHeight() - getTabBarHeight() - getMiniPlayerHeight()`
                */
                int getHeight = (config().isAppiumStreamTestEnabled() && config().appiumStreamGetSerialIdFromDevice())
                        ? (isIos()) ? screenHeight : screenHeight - getAddBannerHeight()
                        : defaultScreenHeightValue;

                if ((elementTopPoint >= 0) && (elementBottomPoint <= getHeight)) {
                    return (elementLeftPoint >= 0) && (elementRightPoint <= screenWidth);
                }
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static Dimension getWindowSize() {
        if (windowSize == null) {
            windowSize = getAppiumDriver().manage().window().getSize();
        }
        return windowSize;
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public enum SwipeDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public enum ScrollDistance {
        MEDIUM(isAndroid() ? 0.35 : 0.5),
        SHORT(0.2),
        LONG(0.7);
        private final double ratio;

        private ScrollDistance(double ratio) {
            this.ratio = ratio;
        }

        public double getValue() {
            return ratio;
        }
    }

}
