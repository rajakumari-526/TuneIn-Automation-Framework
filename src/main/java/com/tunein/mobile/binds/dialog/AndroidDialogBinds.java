package com.tunein.mobile.binds.dialog;

import com.google.inject.AbstractModule;
import com.tunein.mobile.pages.dialog.android.*;
import com.tunein.mobile.pages.dialog.common.*;

public class AndroidDialogBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(NowPlayingFavoriteDialog.class).to(AndroidNowPlayingFavoriteDialog.class).asEagerSingleton();
        bind(NowPlayingMoreOptionsDialog.class).to(AndroidNowPlayingMoreOptionsDialog.class).asEagerSingleton();
        bind(NowPlayingChooseStreamDialog.class).to(AndroidNowPlayingChooseStreamDialog.class).asEagerSingleton();
        bind(NowPlayingSleepTimerDialog.class).to(AndroidNowPlayingSleepTimerDialog.class).asEagerSingleton();
        bind(NowPlayingSetAlarmDialog.class).to(AndroidNowPlayingSetAlarmDialog.class).asEagerSingleton();
        bind(NowPlayingAlarmClockDialog.class).to(AndroidNowPlayingAlarmClockDialog.class).asEagerSingleton();
        bind(AlarmTimeDialog.class).to(AndroidAlarmTimeDialog.class).asEagerSingleton();
        bind(ContinueListeningDialog.class).to(AndroidContinueListeningDialog.class).asEagerSingleton();
        bind(EpisodeModalDialog.class).to(AndroidEpisodeModalDialog.class).asEagerSingleton();
        bind(ShareDialog.class).to(AndroidShareDialog.class).asEagerSingleton();
        bind(ContentListItemDialog.class).to(AndroidContentListItemDialog.class).asEagerSingleton();
        bind(NowPlayingSpeedPlaybackDialog.class).to(AndroidNowPlayingSpeedPlaybackDialog.class).asEagerSingleton();
        bind(NowPlayingScheduledDialog.class).to(AndroidNowPlayingScheduleCardDialog.class).asEagerSingleton();
        bind(SwitchToolTipDialog.class).to(AndroidSwitchToolTipDialog.class).asEagerSingleton();
        bind(WelcomeAdDialog.class).to(AndroidWelcomeAdDialog.class).asEagerSingleton();
        bind(TermsOfServiceDialog.class).to(AndroidTermsOfServiceDialog.class).asEagerSingleton();
    }

}
