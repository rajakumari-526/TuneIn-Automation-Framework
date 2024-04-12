package com.tunein.mobile.pages;

import com.google.inject.Inject;
import com.tunein.mobile.appium.service.AppiumService;
import com.tunein.mobile.deviceactions.DeviceNativeActions;
import com.tunein.mobile.pages.alert.Alert;
import com.tunein.mobile.pages.common.authentication.*;
import com.tunein.mobile.pages.common.browsies.*;
import com.tunein.mobile.pages.common.contentprofile.AffiliatesPage;
import com.tunein.mobile.pages.common.contentprofile.ContentProfilePage;
import com.tunein.mobile.pages.common.contentprofile.MoreDropDownPage;
import com.tunein.mobile.pages.common.explorer.ExplorerMiniPlayerPage;
import com.tunein.mobile.pages.common.explorer.ExplorerPage;
import com.tunein.mobile.pages.common.homepage.CarModePage;
import com.tunein.mobile.pages.common.homepage.HomePage;
import com.tunein.mobile.pages.common.library.*;
import com.tunein.mobile.pages.common.navigation.ContentsListPage;
import com.tunein.mobile.pages.common.navigation.NavigationAction;
import com.tunein.mobile.pages.common.navigation.OfflinePage;
import com.tunein.mobile.pages.common.navigation.TeamsPage;
import com.tunein.mobile.pages.common.nowplaying.MiniPlayerPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.common.premium.PremiumPage;
import com.tunein.mobile.pages.common.search.SearchPage;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.pages.common.userprofile.*;
import com.tunein.mobile.pages.dialog.common.*;
import com.tunein.mobile.reporting.asserts.common.ReportingAsserts;
import com.tunein.mobile.reporting.asserts.common.UnifiedEventsAsserts;
import com.tunein.mobile.utils.*;

public class ScreenFacade {

    @Inject
    protected ApplicationUtil applicationUtil;

    @Inject
    protected SignInPage signInPage;

    @Inject
    protected SignUpPage signUpPage;

    @Inject
    protected NowPlayingScheduledDialog nowPlayingScheduledDialog;

    @Inject
    protected ForgotPasswordPage forgotPasswordPage;

    @Inject
    protected MoreDropDownPage moreDropDownPage;

    @Inject
    protected FacebookAuthenticationPage facebookAuthenticationPage;

    @Inject
    protected GoogleAuthenticationPage googleAuthenticationPage;

    @Inject
    protected SearchPage searchPage;

    @Inject
    protected NowPlayingPage nowPlayingPage;

    @Inject
    protected NowPlayingMoreOptionsDialog nowPlayingMoreOptionsDialog;

    @Inject
    protected NowPlayingChooseStreamDialog nowPlayingChooseStreamDialog;

    @Inject
    protected MiniPlayerPage miniPlayerPage;

    @Inject
    protected ContentProfilePage contentProfilePage;

    @Inject
    protected AffiliatesPage affiliatesPage;

    @Inject
    protected UserProfilePage userProfilePage;

    @Inject
    protected EditUserProfilePage editUserProfilePage;

    @Inject
    protected SettingsPage settingsPage;

    @Inject
    protected TuneInPremiumPage tuneInPremiumPage;

    @Inject
    protected AboutPage aboutPage;

    @Inject
    protected HelpCenterWebPage helpCenterWebPage;

    @Inject
    protected RegWallPage regWallPage;

    @Inject
    protected UpsellPage upsellPage;

    @Inject
    protected AlexaUpsellPage alexaUpsellPage;

    @Inject
    protected SignInSignUpSuccessPage signInSignUpSuccessPage;

    @Inject
    protected Alert alert;

    @Inject
    protected DeviceNativeActions deviceNativeActions;

    @Inject
    protected NavigationAction navigationAction;

    @Inject
    protected HomePage homePage;

    @Inject
    protected AppiumService appiumService;

    @Inject
    protected NowPlayingFavoriteDialog nowPlayingFavoriteDialog;

    @Inject
    protected DeepLinksUtil deepLinksUtil;

    @Inject
    protected LibraryPage libraryPage;

    @Inject
    protected RecentsPage recentsPage;

    @Inject
    protected CarModePage carModePage;

    @Inject
    protected PremiumPage premiumPage;

    @Inject
    protected NowPlayingSleepTimerDialog nowPlayingSleepTimerDialog;

    @Inject
    protected NowPlayingSetAlarmDialog nowPlayingSetAlarmDialog;

    @Inject
    protected NowPlayingAlarmClockDialog nowPlayingAlarmClockDialog;

    @Inject
    protected AlarmTimeDialog alarmTimeDialog;

    @Inject
    protected CustomUrlAddPage customUrlAddPage;

    @Inject
    protected CustomUrlFavoriteDialog customUrlFavoriteDialog;

    @Inject
    protected ContinueListeningDialog continueListeningDialog;

    @Inject
    protected LocalRadioPage localRadioPage;

    @Inject
    protected SportsPage sportsPage;

    @Inject
    protected IheartRadioPage iheartRadioPage;

    @Inject
    protected NewsAndTalkPage newsAndTalkPage;

    @Inject
    protected PodcastsPage podcastsPage;

    @Inject
    protected MusicPage musicPage;

    @Inject
    protected TrendingPage trendingPage;

    @Inject
    protected ByLocationPage byLocationPage;

    @Inject
    protected ByLanguagePage byLanguagePage;

    @Inject
    protected PrivacyPolicyPage privacyPolicyPage;

    @Inject
    protected TermsOfServicePage termsOfServicePage;

    @Inject
    protected FavoritesPage favoritesPage;

    @Inject
    protected DeleteYourAccountPage deleteYourAccountPage;

    @Inject
    protected ContentsListPage contentsListPage;

    @Inject
    protected OfflinePage offlinePage;

    @Inject
    protected EpisodeModalDialog episodeModalDialog;

    @Inject
    protected ShareDialog shareDialog;

    @Inject
    protected ReportingUtil reportingUtil;

    @Inject
    protected ReportingAsserts reportingAsserts;

    @Inject
    protected UnifiedEventsAsserts unifiedEventsAsserts;

    @Inject
    protected GeneralTestUtil generalTestUtil;

    @Inject
    protected ContentListItemDialog contentListItemDialog;

    @Inject
    protected TeamsPage teamsPage;

    @Inject
    protected RadioPage radioPage;

    @Inject
    protected LegalNoticesPage legalNoticesPage;

    @Inject
    protected DownloadsPage downloadsPage;

    @Inject
    protected NowPlayingSpeedPlaybackDialog nowPlayingSpeedPlaybackDialog;

    @Inject
    protected ListenLiveRewindModalDialog listenLiveRewindModalDialog;

    @Inject
    protected ExplorerPage explorerPage;

    @Inject
    protected ExplorerMiniPlayerPage explorerMiniPlayerPage;

    @Inject
    protected SwitchToolTipDialog switchToolTipDialog;

    @Inject
    protected AlarmPlayTimeDialog alarmPlayTimeDialog;

    @Inject
    protected HarUtil harUtil;

    @Inject
    protected WelcomeAdDialog welcomeAdDialog;

    @Inject
    protected TermsOfServiceDialog termsOfServiceDialog;

}
