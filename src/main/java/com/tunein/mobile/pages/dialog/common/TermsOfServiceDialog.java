package com.tunein.mobile.pages.dialog.common;

import com.tunein.mobile.pages.BasePage;

public abstract class TermsOfServiceDialog extends BasePage {

    public abstract TermsOfServiceDialog waitUntilPageReady();

    public abstract void closeTermsOfServiceDialogIfPresent();

    public abstract boolean isTermsOfServiceDialog();

}
