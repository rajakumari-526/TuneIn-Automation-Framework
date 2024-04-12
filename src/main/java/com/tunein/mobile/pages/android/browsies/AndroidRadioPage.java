package com.tunein.mobile.pages.android.browsies;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.browsies.RadioPage;
import com.tunein.mobile.utils.ReporterUtil;

import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.ElementHelper.isElementDisplayed;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidRadioPage extends RadioPage {

    @Step
    @Override
    public RadioPage waitUntilPageReady() {
        assertThat(isElementDisplayed(explorerLogo, Duration.ofSeconds(config().pageReadyTimeoutSeconds()))).isTrue();
        return this;
    }

    @Override
    public void closeRadioPage() {
        ReporterUtil.log("Functionality is not implemented for Android yet");
    }

    @Override
    public boolean isRadioPageOpened() {
        ReporterUtil.log("Functionality is not implemented for Android yet");
        return false;
    }
}
