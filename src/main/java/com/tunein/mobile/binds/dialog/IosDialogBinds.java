package com.tunein.mobile.binds.dialog;

import com.google.inject.AbstractModule;
import com.tunein.mobile.pages.dialog.common.*;
import com.tunein.mobile.pages.dialog.ios.*;

public class IosDialogBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(NowPlayingFavoriteDialog.class).to(IosNowPlayingFavoriteDialog.class).asEagerSingleton();
        bind(NowPlayingMoreOptionsDialog.class).to(IosNowPlayingMoreOptionsDialog.class).asEagerSingleton();
        bind(NowPlayingChooseStreamDialog.class).to(IosNowPlayingChooseStreamDialog.class).asEagerSingleton();
        bind(NowPlayingSleepTimerDialog.class).to(IosNowPlayingSleepTimerDialog.class).asEagerSingleton();
        bind(NowPlayingSetAlarmDialog.class).to(IosNowPlayingSetAlarmDialog.class).asEagerSingleton();
        bind(ContinueListeningDialog.class).to(IosContinueListeningDialog.class).asEagerSingleton();
        bind(EpisodeModalDialog.class).to(IosEpisodeModalDialog.class).asEagerSingleton();
        bind(ShareDialog.class).to(IosShareDialog.class).asEagerSingleton();
        bind(ContentListItemDialog.class).to(IosContentListItemDialog.class).asEagerSingleton();
        bind(NowPlayingSpeedPlaybackDialog.class).to(IosNowPlayingSpeedPlaybackDialog.class).asEagerSingleton();
        bind(NowPlayingScheduledDialog.class).to(IosNowPlayingScheduleCardDialog.class).asEagerSingleton();
        bind(SwitchToolTipDialog.class).to(IosSwitchToolTipDialog.class).asEagerSingleton();
        bind(WelcomeAdDialog.class).to(IosWelcomeAdDialog.class).asEagerSingleton();
        bind(TermsOfServiceDialog.class).to(IosTermsOfServiceDialog.class).asEagerSingleton();
    }

}
