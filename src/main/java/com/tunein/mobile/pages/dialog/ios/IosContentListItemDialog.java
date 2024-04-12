package com.tunein.mobile.pages.dialog.ios;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.ContentListItemDialog;

public class IosContentListItemDialog extends ContentListItemDialog {

    @Step
    @Override
    public void clickOnUnfollowButton() {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public ContentListItemDialog validateUnfavoriteDialogDisplayed() {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public void validateDialogTitleIsEqualToStationName(String station) {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public ContentListItemDialog validateContentStreamDialogIsDisplayed() {
        throw new UnsupportedOperationException("Not supported in iOS");
    }

    @Step
    @Override
    public void clickOnRemoveRecentsDialog() {
        throw new UnsupportedOperationException("Not supported in iOS");
    }
}
