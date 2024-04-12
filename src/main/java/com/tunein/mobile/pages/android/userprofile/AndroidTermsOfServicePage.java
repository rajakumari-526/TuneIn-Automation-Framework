package com.tunein.mobile.pages.android.userprofile;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.authentication.RegWallPage;
import com.tunein.mobile.pages.common.userprofile.TermsOfServicePage;

public class AndroidTermsOfServicePage extends TermsOfServicePage {

    @Step
    @Override
    public RegWallPage tapBackButton() {
        deviceNativeActions.clickBackButton();
        return regWallPage.waitUntilPageReady();
    }

}
