package com.tunein.mobile.pages.dialog.ios;

import com.tunein.mobile.pages.dialog.common.TermsOfServiceDialog;

public class IosTermsOfServiceDialog extends TermsOfServiceDialog {

    @Override
    public TermsOfServiceDialog waitUntilPageReady() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public void closeTermsOfServiceDialogIfPresent() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public boolean isTermsOfServiceDialog() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }
}
