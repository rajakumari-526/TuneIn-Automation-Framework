package com.tunein.mobile.utils;

import com.codeborne.selenide.*;
import com.epam.reportportal.annotations.Step;
import org.openqa.selenium.By;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.getElementText;

public class WaitersUtil {

    private static WebDriverWait getWebDriverWait(Duration timeout) {
        return new WebDriverWait(getAppiumDriver(), timeout);
    }

    public static ElementsCollection waitUntilNumberOfElementsMoreThanZero(ElementsCollection elementsCollection, Duration timeout) {
        try {
            return elementsCollection.shouldBe(CollectionCondition.sizeGreaterThan(0), timeout);
        } catch (Throwable e) {
            e.printStackTrace();
            ReporterUtil.log("List of elements is empty " + e);
            throw new RuntimeException(e);
        }
    }

    public static SelenideElement waitVisibilityOfElement(SelenideElement element) {
        try {
            return element.shouldBe(Condition.visible, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable e) {
            ReporterUtil.log("Element '" + element.getAlias() + "' is not visible");
        }
        return null;
    }

    public static SelenideElement waitVisibilityOfElement(SelenideElement element, Duration timeout) {
        try {
            return element.shouldBe(Condition.visible, timeout);
        } catch (Throwable e) {
            ReporterUtil.log("Element '" + element.getAlias() + "' is not visible, within '" + timeout + "' sec.");
        }
        return null;
    }

    public static SelenideElement waitVisibilityOfElement(By elementBy, Duration timeout) {
        return $(elementBy).shouldBe(Condition.visible, timeout);
    }

    public static void waitTillTextForElementUploaded(SelenideElement element) {
        try {
            long start = System.currentTimeMillis();
            while (getElementText(element).equals("")) {
                if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                    throw new TimeoutException(String.format("Condition not met within %s ms", config().waitCustomTimeoutMilliseconds()));
                }
            }
        } catch (Throwable e) {
            throw new Error("Text is not loaded for element: " + element.getAlias());
        }
    }

    public static void waitTillTextForElementUploaded(SelenideElement element, Duration timeout) {
        try {
            long start = System.currentTimeMillis();
            while (getElementText(element).equals("")) {
                if (System.currentTimeMillis() - start > timeout.toMillis()) {
                    throw new TimeoutException(String.format("Condition not met within %s ms", timeout.toSeconds()));
                }
            }
        } catch (Throwable e) {
            throw new Error("Text is not loaded for element: " + element.getAlias());
        }
    }

    public static void waitTillElementWithTextLoaded(SelenideElement element, String text) {
        try {
            long start = System.currentTimeMillis();
            while (!getElementText(element).contains(text)) {
                if (System.currentTimeMillis() - start > config().elementVisibleTimeoutSeconds()) {
                    throw new TimeoutException(String.format("Condition not met"));
                }
            }
        } catch (Throwable e) {
            throw new Error("Text is not loaded for element: " + element.getAlias());
        }
    }

    public static void waitTillElementWithTextLoaded(SelenideElement element, String text, Duration timeout) {
        try {
            long start = System.currentTimeMillis();
            while (!getElementText(element).contains(text)) {
                if (System.currentTimeMillis() - start > timeout.toMillis()) {
                    throw new TimeoutException(String.format("Condition not met"));
                }
            }
        } catch (Throwable e) {
            throw new Error("Text is not loaded for element: " + element.getAlias());
        }
    }

    public static void waitTillAllTextsOfElementDisappear(SelenideElement element, String[] arrayOfTextData, Duration timeout) {
        ExpectedCondition<?>[] arrayOfConditions = new ExpectedCondition[arrayOfTextData.length];
        for (int i = 0; i < arrayOfTextData.length; i++) {
            arrayOfConditions[i] = ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, arrayOfTextData[i]));
        }
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.and(arrayOfConditions));
        } catch (Throwable throwable) {
            if (throwable instanceof TimeoutException) {
                throw new Error("Text for element didn't disappear");
            }
            ReporterUtil.log("Text for Element " + element + " has already disappeared");
        }
    }

    public static void waitTillOneOfTextsOfElementAppear(SelenideElement element, String[] arrayOfTextData, Duration timeout) {
        ExpectedCondition<?>[] arrayOfConditions = new ExpectedCondition[arrayOfTextData.length];
        for (int i = 0; i < arrayOfTextData.length; i++) {
            arrayOfConditions[i] = ExpectedConditions.textToBePresentInElement(element, arrayOfTextData[i]);
        }
        try {
            getWebDriverWait(timeout).until(ExpectedConditions.or(arrayOfConditions));
        } catch (Throwable e) {
            ReporterUtil.log("Text for Element " + element.getAlias() + " has not appeared");
        }
    }

    public static void waitTextOfElementToDisappear(SelenideElement element, String text) {
        try {
            element.shouldNotBe(Condition.text(text), Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable e) {
            throw new Error("Element '" + element.getAlias() + "' contains wrong text");
        }
    }

    public static void waitTextOfElementToDisappear(SelenideElement element, String text, Duration timeout) {
        try {
            element.shouldNotBe(Condition.text(text), timeout);
        } catch (Throwable e) {
            throw new Error("Element '" + element.getAlias() + "' contains wrong text");
        }
    }

    @Step("Wait for the {element} element to be visible")
    public static SelenideElement waitTillVisibilityOfElement(SelenideElement element) {
        try {
            return element.shouldBe(Condition.visible, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable e) {
            e.printStackTrace();
            ReporterUtil.log("Element '" + element.getAlias() + "' is not visible");
            throw new RuntimeException(e);
        }
    }

    public static SelenideElement waitTillVisibilityOfElement(SelenideElement element, Duration timeoutDuration) {
        try {
           return element.shouldBe(Condition.visible, timeoutDuration);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new Error("Element '" + element.getAlias() + "' is not visible");
        }
    }

    public static SelenideElement waitTillVisibilityOfElement(By by, Duration timeout) {
        try {
            return $(by).shouldBe(Condition.visible, timeout);
        } catch (Throwable e) {
            throw new Error("Element '" + by + "' is not visible, within '" + timeout + "' sec.");
        }
    }

    public static void waitTillElementDisappear(SelenideElement element, Duration timeout) {
        try {
            element.should(disappear, timeout);
        } catch (Throwable e) {
            throw new Error("Element '" + element.getAlias() + "' is  visible, within '" + timeout + "' sec.");
        }
    }

    public static void waitTillElementDisappear(SelenideElement element) {
        try {
            element.should(disappear, Duration.ofSeconds(config().elementVisibleTimeoutSeconds()));
        } catch (Throwable e) {
            throw new RuntimeException("Element '" + element.getAlias() + "' is still visible");
        }
    }

    public static SelenideElement waitElementDisappear(SelenideElement element, Duration timeout) {
        try {
            return element.should(disappear, timeout);
        } catch (Throwable e) {
            ReporterUtil.log("Element '" + element.getAlias() + "' is still visible");
        }
        return null;
    }

    public static SelenideElement waitElementDisappear(SelenideElement element) {
        try {
            return element.shouldNotBe(Condition.visible, Duration.ofSeconds(config().elementNotVisibleTimeoutSeconds()));
        } catch (Throwable e) {
            ReporterUtil.log("Element '" + element.getAlias() + "' is still visible");
        }
        return null;
    }

    public static SelenideElement waitElementDisappear(By elementBy, Duration timeout) {
        try {
            return $(elementBy).shouldNotBe(Condition.visible, timeout);
        } catch (Throwable e) {
            ReporterUtil.log("Element with By: '" + elementBy + "'  is still visible");
        }
        return null;
    }

    public static ElementsCollection waitVisibilityOfElements(ElementsCollection elements) {
        try {
            elements.asFixedIterable().stream().forEach(element -> {
                 element.shouldBe(Condition.visible);
            });
            return elements;
        } catch (Throwable e) {
            ReporterUtil.log("Elements '" + elements + "' are not visible");
        }
        return null;
    }

    public static ElementsCollection waitVisibilityOfElements(ElementsCollection elements, int timeout) {
        try {
            return elements.shouldBe();
        } catch (Throwable e) {
            ReporterUtil.log("Elements '" + elements + "' are not visible, within '" + timeout + "' sec.");
        }
        return null;
    }

    @Step("Wait for {timeout}")
    public static void customWait(Duration timeout) {
        try {
            Thread.sleep(timeout.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
