package com.tunein.mobile.pages.android.userprofile;

import com.codeborne.selenide.ElementsCollection;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.AboutPage;
import com.tunein.mobile.pages.common.userprofile.HelpCenterWebPage;
import com.tunein.mobile.pages.common.userprofile.LegalNoticesPage;

import static com.codeborne.selenide.Selenide.$$x;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;

public class AndroidAboutPage extends AboutPage {

    public ElementsCollection legalNoticesList = $$x("//*[@resource-id=\"tunein.player:id/license_list\"]/android.view.ViewGroup").as("Legal Notice list");

    @Override
    @Step("Get \"Device Serial\" from the \"Settings\" screen")
    public String getDeviceServiceIdentifier() {
        throw new UnsupportedOperationException("This functionality is absent for the Android platform");
    }

    @Override
    public LegalNoticesPage tapOnLegalNoticesButton() {
        clickOnElement(aboutPageLegalNoticesButton);
        clickOnElement(legalNoticesList.get(0).as("FIrst element from legal notice list"));
        return legalNoticesPage.waitUntilPageReady();
    }

    @Step
    @Override
    public HelpCenterWebPage tapOnHelpCenterButton() {
        clickOnElement(scrollTo(aboutPageHelpCenterButton, DOWN).as("Help Center button"));
        return helpCenterWebPage.waitUntilPageReady();
    }
}
