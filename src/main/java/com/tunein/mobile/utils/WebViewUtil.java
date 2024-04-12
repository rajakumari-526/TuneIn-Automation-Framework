package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.WebDriverException;

import java.util.ArrayList;

import static com.tunein.mobile.appium.driverprovider.AppiumDriverProvider.getAppiumDriver;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.NATIVE_APP;
import static com.tunein.mobile.utils.WebViewUtil.AppContext.WEBVIEW;

public class WebViewUtil {

    @Step
    public static void switchToWebViewWithUrl(String urlString) {
        switchToAppContext(WEBVIEW);
        long start = System.currentTimeMillis();
        ArrayList<String> contextsList = new ArrayList(getAppiumDriver().getWindowHandles());
        if (isAndroid()) {
            while (contextsList.size() == 1 && !getAppiumDriver().getCurrentUrl().contains(urlString)) {
                contextsList = new ArrayList(getAppiumDriver().getWindowHandles());
                if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                    throw new Error("Url cannot be found " + config().waitCustomTimeoutMilliseconds() + " ms ");
                }
            }
            for (String context : contextsList) {
                getAppiumDriver().switchTo().window(context);
                if (getAppiumDriver().getCurrentUrl().contains(urlString)) {
                    break;
                }
            }
        }
    }

    public static int getNumberOfWindowHandles() {
        return getAppiumDriver().getWindowHandles().size();
    }

    @Step("Switch to App Context {contextType}")
    public static void switchToAppContext(AppContext contextType) {
        SupportsContextSwitching driverContextSwitching = (SupportsContextSwitching) getAppiumDriver();
        if (!contextType.equals(NATIVE_APP)) {
            long start = System.currentTimeMillis();
            while (new ArrayList((driverContextSwitching).getContextHandles()).size() <= 1) {
                if (System.currentTimeMillis() - start > config().waitCustomTimeoutMilliseconds()) {
                    ReporterUtil.log("Context type " + contextType + " is not available within %s ms ", config().waitCustomTimeoutMilliseconds());
                    return;
                }
            }
        }

        try {
            ArrayList<String> contexts = new ArrayList((driverContextSwitching).getContextHandles());
                for (String context : contexts) {
                    if (context.contains(contextType.getValue())) {
                        try {
                            driverContextSwitching.context(context);
                        } catch (Exception e) {
                            ReporterUtil.log("Error " + e.getMessage());
                        }
                    }
                }
        } catch (WebDriverException ex) {
            ex.printStackTrace();
            ReporterUtil.log("Unable to get context handles");
        }
    }

    public enum AppContext {
        NATIVE_APP("NATIVE_APP"),
        WEBVIEW("WEBVIEW_tunein.player");
        private String value;

        private AppContext(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
