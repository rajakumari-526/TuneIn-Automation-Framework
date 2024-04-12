package com.tunein.mobile.pages.dialog.common;

import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;

public abstract class ListenLiveRewindModalDialog extends BasePage {

    /* --- Loadable Component Method --- */

    public abstract ListenLiveRewindModalDialog waitUntilPageReady();

    /* --- Action Methods --- */

    public abstract NowPlayingPage tapRewindButton();

    public abstract NowPlayingPage tapListenLiveButton();

    public abstract NowPlayingPage closeInformationPopUpIfPresent();

    /* --- Validation Methods --- */

    public abstract void validateListenLiveRewindModalDisplayed();
}
