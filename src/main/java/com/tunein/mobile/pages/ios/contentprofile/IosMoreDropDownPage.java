package com.tunein.mobile.pages.ios.contentprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.contentprofile.MoreDropDownPage;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;

import java.time.Duration;
import java.util.HashMap;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.AUDIOBOOK;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;

public class IosMoreDropDownPage extends MoreDropDownPage {

    @Step
    @Override
    public MoreDropDownPage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(moreDropDownMenu, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Override
    public HashMap<String, SelenideElement> moreDropDownPageElements(ContentProvider.ContentType contentType) {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        int increment = 0;
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownGenres);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownLocation);
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownShareButton);
        if (contentType != AUDIOBOOK) {
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownTwitterButton);
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownWebsiteButton);
        }
        if (contentType == LIVE_STATION) {
            elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + ++increment, moreDropDownDescription);
        }
        return elementsMap;
    }

    @Step
    @Override
    public MoreDropDownPage validateMoreDropdownIsExpanded() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform in Appium");
    }
}
