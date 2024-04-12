package com.tunein.mobile.pages.dialog.android;

import com.codeborne.selenide.ElementsCollection;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;
import static com.tunein.mobile.pages.dialog.common.NowPlayingMoreOptionsDialog.MoreOptionsButtons.*;
import static com.tunein.mobile.testdata.dataprovider.ContentProvider.ContentType.LIVE_STATION;
import static com.tunein.mobile.utils.ElementHelper.getElementText;
import static com.tunein.mobile.utils.ElementHelper.isElementNotDisplayed;
import static org.assertj.core.api.Assertions.assertThat;

public class AndroidNowPlayingMoreOptionsDialog extends NowPlayingMoreOptionsDialog {

    @Step
    @Override
    public void closeMoreOptionsDialog() {
       deviceNativeActions.clickBackButton();
    }

    @Step
    @Override
    public void tapOnNowPlayingOptionsButton(MoreOptionsButtons moreOptionsButton) {
        switch (moreOptionsButton) {
            case SHARE -> { }
            case CAR_MODE -> { }
            case SLEEP_TIMER -> tapSleepTimerButton();
            case SET_ALARM -> tapSetAlarmButton();
            case CHOOSE_STREAM -> tapChooseStreamButton();
            case GO_TO_PROFILE -> tapGoToProfileButton();
            case NEED_HELP, CANCEL, CLOCK_DISPLAY -> throw new UnsupportedOperationException("Functionality is absent for Android Platform");
            default -> throw new Error("Wrong more options button");

        }
    }

    @Step
    @Override
    public NowPlayingMoreOptionsDialog validateEditAlarmButtonsIsDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for Android Platform");
    }

    @Step
    @Override
    public NowPlayingMoreOptionsDialog validateGoToProfileButtonIsNotDisplayed() {
        assertThat(isElementNotDisplayed(moreOptionsGoToProfileButton)).as("Go To Profile button is displayed").isTrue();
        return this;
    }

    @Override
    public List<MoreOptionsButtons> getMoreOptionsButtons() {
        ElementsCollection listOfOptionsButtons = $$(By.id("title"));
        return listOfOptionsButtons.asFixedIterable().stream()
                .map(moreOptionsButton -> getMoreOptionsType(getElementText(moreOptionsButton)))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoreOptionsButtons> getExpectedMoreOptionItems(ContentType contentType) {
        List<MoreOptionsButtons> expectedList = new ArrayList<>();
        expectedList.add(SHARE);
        expectedList.add(SET_ALARM);
        expectedList.add(SLEEP_TIMER);
        expectedList.add(CAR_MODE);
        expectedList.add(GO_TO_PROFILE);
        if (contentType == LIVE_STATION) {
            expectedList.add(CHOOSE_STREAM);
        }
        return expectedList;

    }

}
