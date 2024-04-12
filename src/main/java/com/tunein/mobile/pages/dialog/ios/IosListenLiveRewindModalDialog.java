package com.tunein.mobile.pages.dialog.ios;

import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.dialog.common.ListenLiveRewindModalDialog;

public class IosListenLiveRewindModalDialog extends ListenLiveRewindModalDialog {

    @Override
    public ListenLiveRewindModalDialog waitUntilPageReady() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public NowPlayingPage tapRewindButton() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public NowPlayingPage tapListenLiveButton() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public NowPlayingPage closeInformationPopUpIfPresent() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }

    @Override
    public void validateListenLiveRewindModalDisplayed() {
        throw new UnsupportedOperationException("Functionality is absent for iOS Platform");
    }
}
