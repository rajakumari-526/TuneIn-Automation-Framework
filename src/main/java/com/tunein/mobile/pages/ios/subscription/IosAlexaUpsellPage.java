package com.tunein.mobile.pages.ios.subscription;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class IosAlexaUpsellPage extends AlexaUpsellPage {

    @Step
    @Override
    public AlexaUpsellPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(alexaStartFreeTrial, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    /* --- Action Methods --- */

    @Step
    @Override
    public void tapNoThanksButton() {
        clickOnElement(alexaNoThanks);
    }

    /* --- Helper Methods --- */

    @Override
    public boolean isOnAlexaUpsellPage() {
        closePermissionPopupsIfDisplayed();
        return isElementDisplayed(alexaStartFreeTrial, Duration.ofSeconds(config().waitShortTimeoutSeconds()));
    }
}
