package com.tunein.mobile.binds.page;

import com.google.inject.AbstractModule;
import com.tunein.mobile.pages.android.authentication.*;
import com.tunein.mobile.pages.android.browsies.*;
import com.tunein.mobile.pages.android.contentprofile.AndroidAffiliatesPage;
import com.tunein.mobile.pages.android.contentprofile.AndroidContentProfilePage;
import com.tunein.mobile.pages.android.contentprofile.AndroidMoreDropDownPage;
import com.tunein.mobile.pages.android.explorer.AndroidExplorerMiniPlayerPage;
import com.tunein.mobile.pages.android.explorer.AndroidExplorerPage;
import com.tunein.mobile.pages.android.homepage.AndroidCarModePage;
import com.tunein.mobile.pages.android.homepage.AndroidHomePage;
import com.tunein.mobile.pages.android.library.*;
import com.tunein.mobile.pages.android.navigation.AndroidContentsListPage;
import com.tunein.mobile.pages.android.navigation.AndroidTeamsPage;
import com.tunein.mobile.pages.android.nowplaying.AndroidMiniPlayerPage;
import com.tunein.mobile.pages.android.nowplaying.AndroidNowPlayingPage;
import com.tunein.mobile.pages.android.premium.AndroidPremiumPage;
import com.tunein.mobile.pages.android.search.AndroidSearchPage;
import com.tunein.mobile.pages.android.subscription.AndroidAlexaUpsellPage;
import com.tunein.mobile.pages.android.subscription.AndroidUpsellPage;
import com.tunein.mobile.pages.android.userprofile.*;
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
import com.tunein.mobile.pages.common.navigation.TeamsPage;
import com.tunein.mobile.pages.common.nowplaying.MiniPlayerPage;
import com.tunein.mobile.pages.common.nowplaying.NowPlayingPage;
import com.tunein.mobile.pages.common.premium.PremiumPage;
import com.tunein.mobile.pages.common.search.SearchPage;
import com.tunein.mobile.pages.common.subscription.AlexaUpsellPage;
import com.tunein.mobile.pages.common.subscription.UpsellPage;
import com.tunein.mobile.pages.common.userprofile.*;
import com.tunein.mobile.pages.dialog.android.AndroidCustomUrlFavoriteDialog;
import com.tunein.mobile.pages.dialog.android.AndroidListenLiveRewindModalDialog;
import com.tunein.mobile.pages.dialog.common.CustomUrlFavoriteDialog;
import com.tunein.mobile.pages.dialog.common.ListenLiveRewindModalDialog;

public class AndroidPageBinds extends AbstractModule {

    protected void configure() {
        super.configure();
        bind(HomePage.class).to(AndroidHomePage.class).asEagerSingleton();
        bind(UserProfilePage.class).to(AndroidUserProfilePage.class).asEagerSingleton();
        bind(RegWallPage.class).to(AndroidRegWallPage.class).asEagerSingleton();
        bind(SignInPage.class).to(AndroidSignInPage.class).asEagerSingleton();
        bind(UpsellPage.class).to(AndroidUpsellPage.class).asEagerSingleton();
        bind(AlexaUpsellPage.class).to(AndroidAlexaUpsellPage.class).asEagerSingleton();
        bind(SearchPage.class).to(AndroidSearchPage.class).asEagerSingleton();
        bind(NowPlayingPage.class).to(AndroidNowPlayingPage.class).asEagerSingleton();
        bind(ContentProfilePage.class).to(AndroidContentProfilePage.class).asEagerSingleton();
        bind(AffiliatesPage.class).to(AndroidAffiliatesPage.class).asEagerSingleton();
        bind(MiniPlayerPage.class).to(AndroidMiniPlayerPage.class).asEagerSingleton();
        bind(SignUpPage.class).to(AndroidSignUpPage.class).asEagerSingleton();
        bind(FacebookAuthenticationPage.class).to(AndroidFacebookAuthenticationPage.class).asEagerSingleton();
        bind(GoogleAuthenticationPage.class).to(AndroidGoogleAuthenticationPage.class).asEagerSingleton();
        bind(EditUserProfilePage.class).to(AndroidEditUserProfilePage.class).asEagerSingleton();
        bind(SettingsPage.class).to(AndroidSettingsPage.class).asEagerSingleton();
        bind(TuneInPremiumPage.class).to(AndroidTuneInPremiumPage.class).asEagerSingleton();
        bind(AboutPage.class).to(AndroidAboutPage.class).asEagerSingleton();
        bind(HelpCenterWebPage.class).to(AndroidHelpCenterWebPage.class).asEagerSingleton();
        bind(LibraryPage.class).to(AndroidLibraryPage.class).asEagerSingleton();
        bind(PremiumPage.class).to(AndroidPremiumPage.class).asEagerSingleton();
        bind(CustomUrlAddPage.class).to(AndroidCustomUrlAddPage.class).asEagerSingleton();
        bind(CustomUrlFavoriteDialog.class).to(AndroidCustomUrlFavoriteDialog.class).asEagerSingleton();
        bind(LocalRadioPage.class).to(AndroidLocalRadioPage.class).asEagerSingleton();
        bind(SportsPage.class).to(AndroidSportsPage.class).asEagerSingleton();
        bind(IheartRadioPage.class).to(AndroidIheartRadioPage.class).asEagerSingleton();
        bind(NewsAndTalkPage.class).to(AndroidNewsAndTalkPage.class).asEagerSingleton();
        bind(PodcastsPage.class).to(AndroidPodcastsPage.class).asEagerSingleton();
        bind(MusicPage.class).to(AndroidMusicPage.class).asEagerSingleton();
        bind(TrendingPage.class).to(AndroidTrendingPage.class).asEagerSingleton();
        bind(ByLocationPage.class).to(AndroidByLocationPage.class).asEagerSingleton();
        bind(ByLanguagePage.class).to(AndroidByLanguagePage.class).asEagerSingleton();
        bind(PrivacyPolicyPage.class).to(AndroidPrivacyPolicyPage.class).asEagerSingleton();
        bind(TermsOfServicePage.class).to(AndroidTermsOfServicePage.class).asEagerSingleton();
        bind(FavoritesPage.class).to(AndroidFavoritesPage.class).asEagerSingleton();
        bind(ForgotPasswordPage.class).to(AndroidForgotPasswordPage.class).asEagerSingleton();
        bind(MoreDropDownPage.class).to(AndroidMoreDropDownPage.class).asEagerSingleton();
        bind(ContentsListPage.class).to(AndroidContentsListPage.class).asEagerSingleton();
        bind(TeamsPage.class).to(AndroidTeamsPage.class).asEagerSingleton();
        bind(CarModePage.class).to(AndroidCarModePage.class).asEagerSingleton();
        bind(RecentsPage.class).to(AndroidRecentsPage.class).asEagerSingleton();
        bind(RadioPage.class).to(AndroidRadioPage.class).asEagerSingleton();
        bind(LegalNoticesPage.class).to(AndroidLegalNoticesPage.class).asEagerSingleton();
        bind(DownloadsPage.class).to(AndroidDownloadsPage.class).asEagerSingleton();
        bind(ListenLiveRewindModalDialog.class).to(AndroidListenLiveRewindModalDialog.class).asEagerSingleton();
        bind(ExplorerPage.class).to(AndroidExplorerPage.class).asEagerSingleton();
        bind(ExplorerMiniPlayerPage.class).to(AndroidExplorerMiniPlayerPage.class).asEagerSingleton();
    }
}