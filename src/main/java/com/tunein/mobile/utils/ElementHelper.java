package com.tunein.mobile.utils;

import com.codeborne.selenide.*;
import com.epam.reportportal.annotations.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.AppiumClickOptions.longPressFor;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.utils.WaitersUtil.*;

public class ElementHelper {

    public static boolean isElementDisplayed(SelenideElement element) {
        boolean isDisplayed = true;
        try {
            element.shouldBe(visible, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable ex) {
            isDisplayed = false;
        }
        return isDisplayed;
    }

    public static boolean isElementDisplayed(SelenideElement element, Duration timeout) {
        boolean isDisplayed = true;
        try {
            element.shouldBe(visible, timeout);
        } catch (Throwable ex) {
            isDisplayed = false;
        }
        return isDisplayed;
    }

    public static boolean isElementDisplayed(By elementBy, Duration timeout) {
        boolean isDisplayed = true;
        try {
            $(elementBy).shouldBe(visible, timeout);
        } catch (Throwable ex) {
            isDisplayed = false;
        }
        return isDisplayed;
    }

    public static boolean isElementNotDisplayed(SelenideElement element) {
        boolean isNotDisplayed = true;
        try {
            element.shouldNotBe(visible, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable ex) {
            isNotDisplayed = false;
        }
        return isNotDisplayed;
    }

    public static boolean isElementNotDisplayed(SelenideElement element, Duration timeout) {
        boolean isNotDisplayed = true;
        try {
            element.shouldNotBe(visible, timeout);
        } catch (Throwable ex) {
            isNotDisplayed = false;
        }
        return isNotDisplayed;
    }

    public static boolean isElementNotDisplayed(By elementBy, Duration timeout) {
        boolean isNotDisplayed = true;
        try {
            $(elementBy).shouldNotBe(visible, timeout);
        } catch (Throwable ex) {
            isNotDisplayed = false;
        }
        return isNotDisplayed;
    }

    public static boolean isElementEnabled(SelenideElement element) {
        return waitVisibilityOfElement(element) != null && element.isEnabled();
    }

    public static boolean isElementEnabled(SelenideElement element, Duration timeout) {
        return waitVisibilityOfElement(element, timeout) != null && element.isEnabled();
    }

    public static boolean isElementClickable(SelenideElement element) {
        return waitVisibilityOfElement(element) != null && element.getAttribute("clickable").equalsIgnoreCase("false");
    }

    public static boolean isElementsMoreThanZero(ElementsCollection elements) {
        return elements.filterBy(visible).size() > 0;
    }

    public static boolean isElementsMoreThanZero(ElementsCollection elements, int timeoutSeconds) {
        return waitVisibilityOfElements(elements, timeoutSeconds) != null && elements.size() > 0;
    }

    public enum AttributeType {
        NAME("name"),
        LABEL("label"),
        CONTENT_DESC("content-desc"),
        TEXT("text"),
        CHECKED("checked"),
        VALUE("value");
        private String attributeValue;

        private AttributeType(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        public String getAttributeValue() {
            return attributeValue;
        }
    }

    @Step("Getting element label with {attributeType} attribute type")
    public static String getElementAttributeValue(SelenideElement element, AttributeType attributeType) {
        return waitVisibilityOfElement(element).getAttribute(attributeType.getAttributeValue());
    }

    @Step("Getting element label with {attributeType} attribute type")
    public static String getElementAttribute(SelenideElement element, String... customAttribute) {
        String attribute = (customAttribute.length > 0) ? customAttribute[0] : (isAndroid()) ? "name" : "label";
        return waitVisibilityOfElement(element).getAttribute(attribute);
    }

    @Step("Getting element label with {attributeType} attribute type")
    public static String getElementAttributeValue(SelenideElement element, AttributeType attributeType, int... timeout) {
        if (timeout.length > 0) {
            return waitVisibilityOfElement(element, Duration.ofSeconds(timeout[0])).getAttribute(attributeType.getAttributeValue());
        } else {
            return waitVisibilityOfElement(element).getAttribute(attributeType.getAttributeValue());
        }
    }

    public static String getElementNameOrLabel(SelenideElement element, Duration... timeout) {
        String attribute = isAndroid() ? "name" : "label";
        if (timeout.length > 0) {
            return waitVisibilityOfElement(element, timeout[0]).getAttribute(attribute);
        } else {
            return waitVisibilityOfElement(element).getAttribute(attribute);
        }
    }

    public static String getElementNameOrLabel(SelenideElement element, Duration timeout) {
        String attribute = isAndroid() ? "name" : "label";
        return waitVisibilityOfElement(element, timeout).getAttribute(attribute);
    }

    public static String getElementContentDescOrLabel(SelenideElement element) {
        String attribute = isAndroid() ? "content-desc" : "label";
        return waitVisibilityOfElement(element).getAttribute(attribute);
    }

    public static String getElementNameOrValue(SelenideElement element) {
        String attribute = isAndroid() ? "name" : "value";
        return waitVisibilityOfElement(element).getAttribute(attribute);
    }

    public static String getElementTextOrLabel(SelenideElement element) {
        String attribute = isAndroid() ? "text" : "label";
        return waitVisibilityOfElement(element).getAttribute(attribute);
    }

    public static String getElementTextOrLabel(SelenideElement element, Duration timeout) {
        String attribute = isAndroid() ? "text" : "label";
        return waitVisibilityOfElement(element, timeout).getAttribute(attribute);
    }

    public static String getElementContent(SelenideElement element) {
        return waitVisibilityOfElement(element).getAttribute("content-desc");
    }

    public static String getElementText(SelenideElement element) {
        return waitVisibilityOfElement(element).getText().trim();
    }

    public static String getElementName(SelenideElement element) {
        return waitVisibilityOfElement(element).getAttribute("name");
    }

    public static String getElementText(SelenideElement element, Duration timeout) {
        return waitVisibilityOfElement(element, timeout).getText();
    }

    public static boolean isElementChecked(SelenideElement element) {
        return waitVisibilityOfElement(element).getAttribute("checked").equals("true");
    }

    public static String getElementValue(SelenideElement element) {
        return waitVisibilityOfElement(element).getAttribute("value");
    }

    public static SelenideElement getElementFromParent(SelenideElement parent, By elementBy) {
        try {
            return parent.$(elementBy);
        } catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    public static LocalTime getTimeFromElement(SelenideElement element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(getElementText(element), formatter);
        ReporterUtil.log("Get time : " + time);
        return time;
    }

    public static Point getElementCoordinates(SelenideElement element, ElementSide elementSide) {
        int elementHeight = element.getSize().height;
        int elementWidth = element.getSize().width;
        int elementX = element.getLocation().x;
        int elementY = element.getLocation().y;
        switch (elementSide) {
            case LEFT -> {
                return new Point(elementX, elementY + elementHeight / 2);
            }
            case RIGHT -> {
                return new Point(elementX + elementWidth, elementY + elementHeight / 2);
            }
            default -> {
                return new Point(elementX + elementWidth / 2, elementY + elementHeight / 2);
            }
        }
    }

    public static void longPressOnElement(SelenideElement element) {
        element.click(longPressFor(Duration.ofSeconds(2)));
    }

    public enum ElementSide {
        LEFT, RIGHT, CENTER
    }

}
