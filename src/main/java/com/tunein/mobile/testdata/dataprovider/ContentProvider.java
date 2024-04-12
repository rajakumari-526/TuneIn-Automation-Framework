package com.tunein.mobile.testdata.dataprovider;

import com.google.gson.*;
import com.tunein.mobile.appium.driverprovider.AppiumSession;
import com.tunein.mobile.testdata.models.Contents;
import com.tunein.mobile.testdata.models.Streams;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static com.tunein.mobile.pages.common.navigation.NavigationAction.NavigationActionItems.*;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_GENERAL;
import static com.tunein.mobile.testdata.dataprovider.UserProvider.USER_PREMIUM;

public class ContentProvider {

    private static final String FILE_WITH_CONTENTS = "contents/Contents.json";

    private static final String FILE_WITH_STREAMS = "streams/%s.csv";

    public final static String CUSTOM_URL = "http://listen.radionomy.com";

    public final static Contents STREAM_STATION_WITHOUT_ADS = getContent(ContentTags.STREAM_STATION_WITHOUT_ADS);

    public final static Contents STREAM_STATION_UNICC = getContent(ContentTags.STREAM_STATION_UNICC);

    public final static Contents STREAM_STATION_KQED = getContent(ContentTags.STREAM_STATION_KQED);

    public final static Contents STREAM_STATION_BBSFM = getContent(ContentTags.STREAM_STATION_BBSFM);

    public final static Contents STREAM_PODCAST_MARKETPLACE = getContent(ContentTags.STREAM_PODCAST_MARKETPLACE);

    public final static Contents STREAM_STATION_TODAYS_HITS = getContent(ContentTags.STREAM_STATION_TODAYS_HITS);

    public final static Contents STREAM_STATION_O_AND_0_WITH_ADS = getContent(ContentTags.STREAM_STATION_O_AND_0_WITH_ADS);

    public final static Contents STREAM_STATION_CLASSIC_ROCK_HITS = getContent(ContentTags.STREAM_STATION_CLASSIC_ROCK_HITS);

    public final static Contents STREAM_STATION_CHRISTIAN_HITS = getContent(ContentTags.STREAM_STATION_CHRISTIAN_HITS);

    public final static Contents STREAM_PODCAST_PROGRESSIVE_VOICES = getContent(ContentTags.STREAM_PODCAST_PROGRESSIVE_VOICES);

    public final static Contents STREAM_PREMIUM_PODCAST_NURSE_TALK = getContent(ContentTags.STREAM_PREMIUM_PODCAST_NURSE_TALK);

    public final static Contents STREAM_STATION_CNN = getContent(ContentTags.STREAM_STATION_CNN);

    public final static Contents STREAM_SUGGESTED_ARTIST = getContent(ContentTags.STREAM_SUGGESTED_ARTIST);

    public final static Contents STREAM_STATION_LA_JEFA = getContent(ContentTags.STREAM_STATION_LA_JEFA);

    public final static Contents STREAM_STATION_WITH_MP3_FORMAT = getContent(ContentTags.STREAM_STATION_WITH_MP3_FORMAT);

    public final static Contents STREAM_STATION_CBS_MUSIC_FM = getContent(ContentTags.STREAM_STATION_CBS_MUSIC_FM);

    public final static Contents STREAM_STATION_FM_CORDOBA = getContent(ContentTags.STREAM_STATION_FM_CORDOBA);

    public final static Contents STREAM_STATION_RADIO_X = getContent(ContentTags.STREAM_STATION_RADIO_X);

    public final static Contents STREAM_STATION_RADIO_538 = getContent(ContentTags.STREAM_STATION_RADIO_538);

    public final static Contents STREAM_STATION_AAC_FORMAT = getContent(ContentTags.STREAM_STATION_AAC_FORMAT);

    public final static Contents STREAM_STATION_BBC_RADIO_5 = getContent(ContentTags.STREAM_STATION_BBC_RADIO_5);

    public final static Contents STREAM_STATION_WITH_MUSIC_AAC = getContent(ContentTags.STREAM_STATION_WITH_MUSIC_AAC);

    public final static Contents STREAM_STATION_WITH_ACTIVE_SHOW = getContent(ContentTags.STREAM_STATION_WITH_ACTIVE_SHOW);

    public final static Contents STREAM_PROGRAM_WITH_ACTIVE_SHOW = getContent(ContentTags.STREAM_PROGRAM_WITH_ACTIVE_SHOW);

    public final static Contents STREAM_NEWS_PREMIUM_FOX_NEWS = getContent(ContentTags.STREAM_NEWS_PREMIUM_FOX_NEWS);

    public final static Contents STREAM_GAME_REPLAY = getContent(ContentTags.STREAM_GAME_REPLAY);

    public final static Contents STREAM_STATION_RESTRICTED = getContent(ContentTags.STREAM_STATION_RESTRICTED);

    public final static Contents STREAM_FREE_PODCAST_WITH_LONG_EPISODE = getContent(ContentTags.STREAM_FREE_PODCAST_WITH_LONG_EPISODE);

    public final static Contents STREAM_FREE_SHORT_PODCAST = getContent(ContentTags.STREAM_FREE_SHORT_PODCAST);

    public final static Contents STREAM_SECOND_PREMIUM_OWNED_AND_OPERATED = getContent(ContentTags.STREAM_SECOND_PREMIUM_OWNED_AND_OPERATED);

    public final static Contents STREAM_FREE_STREAM_FROM_PREMIUM_OWNED_AND_OPERATED = getContent(ContentTags.STREAM_FREE_STREAM_FROM_PREMIUM_OWNED_AND_OPERATED);

    public final static Contents STREAM_ARTIST_MADONNA = getContent(ContentTags.STREAM_ARTIST_MADONNA);

    public final static Contents STREAM_ARTIST_DRAKE = getContent(ContentTags.STREAM_ARTIST_DRAKE);

    public final static Contents STREAM_ALBUM_BEDTIME_STORIES_MADONNA = getContent(ContentTags.STREAM_ALBUM_BEDTIME_STORIES_MADONNA);

    public final static Contents STREAM_SHOW = getContent(ContentTags.STREAM_SHOW);

    public final static Contents STREAM_AL_JAZEERA = getContent(ContentTags.STREAM_AL_JAZEERA);

    public final static Contents STREAM_PREMIUM_NEWS_MSNBC = getContent(ContentTags.STREAM_PREMIUM_NEWS_MSNBC);

    public final static Contents STREAM_STAR_TALK_RADIO_PODCAST = getContent(ContentTags.STREAM_STAR_TALK_RADIO_PODCAST);

    public final static Contents STREAM_STAR_TALK_RADIO_LIVE_STATION = getContent(ContentTags.STREAM_STAR_TALK_RADIO_LIVE_STATION);

    public final static Contents STREAM_PODCAST_THE_DAILY = getContent(ContentTags.STREAM_PODCAST_THE_DAILY);

    public final static Contents TEAM_CATEGORY_BALTIMORE_ORIOLES = getContent(ContentTags.TEAM_CATEGORY_BALTIMORE_ORIOLES);

    public final static Contents STREAM_STATION_ESPN = getContent(ContentTags.STREAM_STATION_ESPN);

    public final static Contents STREAM_PREMIUM_PODCAST_JSA = getContent(ContentTags.STREAM_PREMIUM_PODCAST_JSA);

    public final static Contents STREAM_PODCAST_TED_TALK_DAILY = getContent(ContentTags.STREAM_PODCAST_TED_TALK_DAILY);

    public final static Contents STREAM_PODCAST_UNRESOLVED = getContent(ContentTags.STREAM_PODCAST_UNRESOLVED);

    public final static Contents STREAM_AUDIOBOOK_GATSBY = getContent(ContentTags.STREAM_AUDIOBOOK_GATSBY);

    public final static Contents STREAM_AUDIOBOOK_GATSBY_LAST_CHAPTER = getContent(ContentTags.STREAM_AUDIOBOOK_GATSBY_LAST_CHAPTER);

    public final static Contents STREAM_AUDIOBOOK_LIAR = getContent(ContentTags.STREAM_AUDIOBOOK_LIAR);

    public final static Contents STREAM_UNAVAILABLE_STATION = getContent(ContentTags.STREAM_UNAVAILABLE_STATION);

    public final static Contents STREAM_WITH_LISTEN_LIVE_OR_REWIND_BUTTON = getContent(ContentTags.STREAM_WITH_LISTEN_LIVE_OR_REWIND_BUTTON);

    public final static Contents STREAM_KBMR = getContent(ContentTags.STREAM_KBMR);

    public final static Contents STREAM_DELETED = getContent(ContentTags.STREAM_DELETED);

    public final static Contents STREAM_STATION_WILD_94 = getContent(ContentTags.STREAM_STATION_WILD_94);

    public final static Contents STREAM_STATION_WITH_PREROLL = getContent(ContentTags.STREAM_STATION_WITH_PREROLL);

    public final static Contents STREAM_STATION_IMA_VIDEO = getContent(ContentTags.STREAM_STATION_IMA_VIDEO);

    public final static Contents STREAM_STATION_IMA_VIDEO_SECOND = getContent(ContentTags.STREAM_STATION_IMA_VIDEO_SECOND);

    public final static Contents STREAM_STATION_IMA_VIDEO_THIRD = getContent(ContentTags.STREAM_STATION_IMA_VIDEO_THIRD);

    public final static Contents STREAM_STATION_ADSWIZZ_INSTREAM_AD = getContent(ContentTags.STREAM_STATION_ADSWIZZ_INSTREAM_AD);

    public final static Contents STREAM_STATION_INSTREAM_GAM_AD = getContent(ContentTags.STREAM_STATION_INSTREAM_GAM_AD);

    public final static Contents STREAM_STATION_ADSWIZZ_AD = getContent(ContentTags.STREAM_STATION_ADSWIZZ_AD);

    public final static Contents STREAM_STATION_WITH_MIDROLL = getContent(ContentTags.STREAM_STATION_WITH_MIDROLL);

    public final static Contents STREAM_STATION_WITH_SWITCH = getContent(ContentTags.STREAM_STATION_WITH_SWITCH);

    public final static Contents STREAM_STATION_WITH_SWITCH_ADDITIONAL = getContent(ContentTags.STREAM_STATION_WITH_SWITCH_ADDITIONAL);

    public final static Contents STREAM_STATION_IMA_VIDEO_AD = getContent(ContentTags.STREAM_STATION_IMA_VIDEO_AD);

    public final static Contents STREAM_STATION_IMA_AUDIO_AD = getContent(ContentTags.STREAM_STATION_IMA_AUDIO_AD);

    public final static Contents STREAM_PODCAST_ADS_EXPLICITLY_ENABLED = getContent(ContentTags.STREAM_PODCAST_ADS_EXPLICITLY_ENABLED);

    public final static Contents STREAM_PODCAST_ADS_EXPLICITLY_DISABLED = getContent(ContentTags.STREAM_PODCAST_ADS_EXPLICITLY_DISABLED);

    public final static Contents STREAM_STATION_NON_PUBLIC = getContent(ContentTags.STREAM_STATION_NON_PUBLIC);

    public final static Contents STREAM_SWITCH_STATION_WITH_STOP_BUTTON = getContent(ContentTags.STREAM_SWITCH_STATION_WITH_STOP_BUTTON);

    public final static Contents STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON = getContent(ContentTags.STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON);

    public final static Contents STREAM_STATION_WITH_SWITCH_AND_MIDROLL = getContent(ContentTags.STREAM_STATION_WITH_SWITCH_AND_MIDROLL);

    public final static Contents STREAM_STATION_WITH_SWITCH_AND_MIDROLL_SECOND = getContent(ContentTags.STREAM_STATION_WITH_SWITCH_AND_MIDROLL_SECOND);

    public final static Contents STREAM_STATION_SWITCH_MIX = getContent(ContentTags.STREAM_STATION_SWITCH_MIX);

    public final static Contents PREROLL_DISABLED_STATION = getContent(ContentTags.PREROLL_DISABLED_STATION);

    public final static Contents STREAM_STATION_KFJC = getContent(ContentTags.STREAM_STATION_KFJC);

    public final static Contents STREAM_STATION_WITH_DISABLED_PREROLLS = getContent(ContentTags.STREAM_STATION_WITH_DISABLED_PREROLLS);

    public final static Contents STREAM_STATION_IMA_VIDEO_AND_AUDIO_AD = getContent(ContentTags.STREAM_STATION_IMA_VIDEO_AND_AUDIO_AD);

    public final static List<Contents> GENERATE_RECENT_LIST = new ArrayList<>() {{
        add(STREAM_STATION_UNICC);
        add(STREAM_STATION_WITHOUT_ADS);
        add(STREAM_STATION_BBC_RADIO_5);
        add(STREAM_STATION_WITH_MUSIC_AAC);
        add(STREAM_GAME_REPLAY);
        add(STREAM_STATION_LA_JEFA);
        add(STREAM_PODCAST_MARKETPLACE);
        add(STREAM_STATION_AAC_FORMAT);
    }};

    public final static List<Contents> STATIONS_WITH_IMA_VIDEO = new ArrayList<>() {{
        add(STREAM_STATION_IMA_VIDEO_AND_AUDIO_AD);
        add(STREAM_STATION_IMA_VIDEO_AD);
        add(STREAM_STATION_IMA_VIDEO_SECOND);
    }};

    public final static List<Contents> STATIONS_WITH_ADSWIZZ = new ArrayList<>() {{
        add(STREAM_STATION_UNICC);
        add(STREAM_STATION_ADSWIZZ_AD);
        add(STREAM_STATION_WITH_PREROLL);
    }};

    public final static List<Contents> STATIONS_WITH_SWITCH_AND_MIDROLL = new ArrayList<>() {{
        add(STREAM_STATION_WITH_SWITCH_AND_MIDROLL);
        add(STREAM_STATION_WITH_SWITCH_AND_MIDROLL_SECOND);
        add(STREAM_SWITCH_STATION_WITH_STOP_BUTTON);
        add(STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON);
        add(STREAM_STATION_SWITCH_MIX);
    }};

    public static Contents getContent(ContentTags contentTag) {
        Contents content = new Contents();
        Gson gson = new GsonBuilder().create();
        try {
            File jsonFile = new File(UserProvider.class.getClassLoader().getResource(FILE_WITH_CONTENTS).getFile());
            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            String toString = IOUtils.toString(reader);
            JsonArray jsonArray = JsonParser.parseString(toString).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                if (object.has(contentTag.getContentTag())) {
                    content = gson.fromJson(object.get(contentTag.getContentTag()), Contents.class);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static Contents getStationWithStreamFormat(StreamFormat streamFormat, boolean isPremium) {
        Contents content = new Contents();
        Gson gson = new GsonBuilder().create();
        try {
            File jsonFile = new File(UserProvider.class.getClassLoader().getResource(FILE_WITH_CONTENTS).getFile());
            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            String toString = IOUtils.toString(reader);
            JsonArray jsonArray = JsonParser.parseString(toString).getAsJsonArray();
            ArrayList<JsonObject> listOfJsonElements = new ArrayList<JsonObject>();
            for (int i = 0; i < jsonArray.size(); i++) {
                listOfJsonElements.add(jsonArray.get(i).getAsJsonObject());
            }
            Collections.shuffle(listOfJsonElements);
            for (JsonObject object : listOfJsonElements) {
                String objectString = object.toString();
                if (objectString.contains(streamFormat.name()) && objectString.contains("premiumLiveStation") == isPremium) {
                    String contentTag = (objectString.split("\":"))[0].replace("{\"", "");
                    content = gson.fromJson(object.get(contentTag), Contents.class);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static List<Streams> getStreamsFromTheCSVFile(String fileName) {
        List<Streams> params = new ArrayList<>();

        String pathToFile = UserProvider.class.getClassLoader().getResource(String.format(FILE_WITH_STREAMS, fileName)).getFile();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pathToFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            br.readLine(); // Read first line and skip due to header in csv file
            String line;
            while ((line = br.readLine()) != null) {
                String[] stream = line.split(",");
                Streams streamObject = new Streams(stream[1], stream[2], stream[3]);
                params.add(streamObject);
            }
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return params;
    }

    @DataProvider(name = "streamFormats")
    public static Object[][] streamFormats() {
        StreamFormat[] streamFormatArray = StreamFormat.values();
        Object[][] dataProviderValues = new Object[streamFormatArray.length][1];
        for (int i = 0; i < streamFormatArray.length; i++) {
            dataProviderValues[i][0] = streamFormatArray[i];
        }
        return dataProviderValues;
    }

    @DataProvider(name = "stationsAndStreamFormatDataProvider")
    public static Object[][] stationsAndStreamFormatDataProvider() {
        StreamFormat[] formatsArray = StreamFormat.values();
        Object[][] hashMapObj = new Object[formatsArray.length][1];
        for (int i = 0; i < formatsArray.length; i++) {
            HashMap<StreamFormat, Contents> hashMapItem = new HashMap<StreamFormat, Contents>();
            hashMapItem.put(formatsArray[i], getStationWithStreamFormat(formatsArray[i], false));
            hashMapObj[i][0] = hashMapItem;
        }
        return hashMapObj;
    }

    @DataProvider(name = "streamTypesShortDataProviders")
    public static Object[][] streamTypesShortDataProviders() {
        return new Object[][]{{STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_RADIO_538}, {STREAM_STATION_TODAYS_HITS}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "differentStreamTypesDataProviders")
    public static Object[][] differentStreamTypesDataProviders() {
        return new Object[][]{{STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_AAC_FORMAT}, {STREAM_STATION_TODAYS_HITS}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "fullListOfStreamTypesDataProviders")
    public static Object[][] fullListOfStreamTypesDataProviders() {
        return new Object[][]{{STREAM_STATION_WITH_MUSIC_AAC}, {STREAM_PROGRAM_WITH_ACTIVE_SHOW}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_FREE_STREAM_FROM_PREMIUM_OWNED_AND_OPERATED}, {STREAM_STATION_TODAYS_HITS}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "stationsWithOneStreamFormatDataProvider")
    public static Object[][] stationsWithOneStreamFormatDataProvider() {
        return new Object[][]{{STREAM_STATION_WITH_MP3_FORMAT}, {STREAM_STATION_WITH_MUSIC_AAC}, {STREAM_STATION_BBC_RADIO_5}};
    }

    @DataProvider(name = "oneStationAndPodcastDataProvider")
    public static Object[][] oneStationAndPodcastDataProvider() {
        return new Object[][]{{STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_WITHOUT_ADS}};
    }

    @DataProvider(name = "oneStationAndPodcastDataProviderStabilityTest")
    public static Object[][] oneStationAndPodcastDataProviderStabilityTest() {
        return new Object[][]{{STREAM_FREE_PODCAST_WITH_LONG_EPISODE}, {STREAM_STATION_WITHOUT_ADS}};
    }

    @DataProvider(name = "stationPodcastAudiobookDataProvider")
    public static Object[][] stationPodcastAudiobookDataProvider() {
        return new Object[][]{{STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "playBackContentProfiles")
    public static Object[][] playBackContentProfiles() {
        return new Object[][]{{STREAM_STATION_WITHOUT_ADS}, {STREAM_ALBUM_BEDTIME_STORIES_MADONNA}, {STREAM_ARTIST_DRAKE}};
    }

    @DataProvider(name = "differentContentProfiles")
    public static Object[][] differentContentProfiles() {
        return new Object[][]{{STREAM_SHOW}, {STREAM_NEWS_PREMIUM_FOX_NEWS}, {STREAM_ALBUM_BEDTIME_STORIES_MADONNA}, {STREAM_ARTIST_MADONNA}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_STATION_TODAYS_HITS}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_RESTRICTED}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "playableContentProfiles")
    public static Object[][] playableContentProfiles() {
        return new Object[][]{{STREAM_PROGRAM_WITH_ACTIVE_SHOW}, {STREAM_ALBUM_BEDTIME_STORIES_MADONNA}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_STATION_TODAYS_HITS}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_PREMIUM_NEWS_MSNBC}};
    }

    @DataProvider(name = "nextContentProfiles")
    public static Object[][] nextContentProfiles() {
        return new Object[][]{{STREAM_SHOW}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_ARTIST_MADONNA}, {STREAM_STATION_TODAYS_HITS}};
    }

    @DataProvider(name = "searchContentProfiles")
    public static Object[][] searchContentProfiles() {
        return new Object[][]{{TEAM_CATEGORY_BALTIMORE_ORIOLES}, {STREAM_SUGGESTED_ARTIST}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_PROGRAM_WITH_ACTIVE_SHOW}, {STREAM_NEWS_PREMIUM_FOX_NEWS}};
    }

    @DataProvider(name = "playableThroughDeepLinkForContentProfiles")
    public static Object[][] playableThroughDeepLinkForContentProfiles() {
        return new Object[][]{{STREAM_STATION_WITHOUT_ADS}, {STREAM_STATION_TODAYS_HITS}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_PREMIUM_NEWS_MSNBC}, {STREAM_AUDIOBOOK_GATSBY}};
    }

    @DataProvider(name = "favoriteUnfavoriteContentProfiles")
    public static Object[][] favoriteUnfavoriteContentProfiles() {
        return new Object[][]{{STREAM_SHOW}, {STREAM_NEWS_PREMIUM_FOX_NEWS}, {STREAM_ARTIST_MADONNA}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_STATION_TODAYS_HITS}, {STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_PODCAST_MARKETPLACE}, {STREAM_STATION_RESTRICTED}, {STREAM_GAME_REPLAY}};
    }

    //TODO- add genre dataprovider
    @DataProvider(name = "contentProfilesForOfflineMode")
    public static Object[][] contentProfilesForOfflineMode() {
        return new Object[][]{{STREAM_PROGRAM_WITH_ACTIVE_SHOW}, {STREAM_ARTIST_DRAKE}, {STREAM_STATION_WITHOUT_ADS}, {STREAM_PODCAST_MARKETPLACE}};
    }

    @DataProvider(name = "categoryProvider")
    public static Object[][] categoryProvider() {
        return new Object[][]{{STREAM_ARTIST_DRAKE}, {STREAM_ALBUM_BEDTIME_STORIES_MADONNA}};
    }

    @DataProvider(name = "usersProviders")
    public static Object[][] usersProviders() {
        return new Object[][]{{USER_PREMIUM}, {USER_GENERAL}};
    }

    @DataProvider(name = "userWithFavorites")
    public static Object[][] userWithFavorites() {
        return new Object[][]{{STREAM_PODCAST_MARKETPLACE}, {STREAM_ARTIST_DRAKE}, {STREAM_SHOW}};
    }

    @DataProvider(name = "streamPremiumDataProviders")
    public static Object[][] streamPremiumProviders() {
        return new Object[][]{{STREAM_PREMIUM_PODCAST_NURSE_TALK}, {STREAM_STATION_TODAYS_HITS}, {STREAM_STATION_CNN}};
    }

    @DataProvider(name = "deepLinkPagesDataProviders")
    public static Object[][] deepLinkPagesDataProviders() {
        return new Object[][]{{REGWALL}, {SETTINGS}, {PREMIUM}, {ABOUT}, {CARMODE}};
    }

    @DataProvider(name = "favStationShowFromNPScreen")
    public static Object[][] favStationShowFromNPScreen() {
        return new Object[][]{{STREAM_STATION_WITHOUT_ADS}, {STREAM_STATION_TODAYS_HITS}, {STREAM_STATION_WITH_ACTIVE_SHOW}};
    }
  
    @DataProvider(name = "streamTypeDataProviders")
    public static Object[][] streamTypeDataProviders() {
        return new Object[][]{{STREAM_STATION_UNICC}, {STREAM_STATION_CBS_MUSIC_FM}};
    }

    @DataProvider(name = "streamsForStabilityTesting")
    public static Object[][] streamsForStabilityTesting() {
        return new Object[][]{{STREAM_STAR_TALK_RADIO_LIVE_STATION}, {STREAM_STATION_AAC_FORMAT}, {STREAM_STATION_BBC_RADIO_5}, {STREAM_FREE_PODCAST_WITH_LONG_EPISODE}};
    }

    @DataProvider(name = "streamTesterDataProvider")
    public static Object[][] streamTesterDataProvider() {
        List<Streams> streams;
        Object[][] dataProviderValues = null;

        try {
            // Getting stream CSV file name
            streams = getStreamsFromTheCSVFile(AppiumSession.getStreamTestCsvFile());
            dataProviderValues = new Object[streams.size()][1];

            Streams[] resultArray = new Streams[streams.size()];
            resultArray = streams.toArray(resultArray);

            for (int i = 0; i < resultArray.length; i++) {
                dataProviderValues[i][0] = resultArray[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataProviderValues;
    }

    public enum ContentType {
        LIVE_STATION("liveStation"),
        PREMIUM_LIVE_STATION("premiumLiveStation"),
        OWNED_AND_OPERATED("ownedAndOperated"),
        CUSTOM_URL("customUrl"),
        PODCAST("podcast"),
        PREMIUM_PODCAST("premiumPodcast"),
        AUDIOBOOK("audiobook"),
        GAME_REPLAY("gameReplay"),
        LIVE_EVENT("liveEvent"),
        STREAM_ERROR("streamError"),
        SHOW("show");
        private String contentName;

        private ContentType(String contentName) {
            this.contentName = contentName;
        }

        public String getContentName() {
            return contentName;
        }

        public static ContentType getContentTypeValue(final String contentNameValue) {
            List<ContentType> contentTypeList = Arrays.asList(ContentType.values());
            return contentTypeList.stream().filter(eachContent -> eachContent.toString().equals(contentNameValue))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return contentName;
        }
    }

    public enum ContentTags {
        STREAM_STATION_WITHOUT_ADS("streamStationWithoutAds"),
        STREAM_STATION_KQED("streamStationKQED"),
        STREAM_STATION_UNICC("streamStationUNiCC"),
        STREAM_STATION_BBSFM("streamStationBBSFM"),
        STREAM_PODCAST_MARKETPLACE("streamPodcastMarketplace"),
        STREAM_STATION_TODAYS_HITS("streamStationTodaysHits"),
        STREAM_STATION_O_AND_0_WITH_ADS("streamStationTodaysHitsWithAds"),
        STREAM_STATION_CLASSIC_ROCK_HITS("streamStationClassicRockHits"),
        STREAM_STATION_CHRISTIAN_HITS("streamStationChristianHits"),
        STREAM_PODCAST_PROGRESSIVE_VOICES("streamPodcastProgressiveVoices"),
        STREAM_PREMIUM_PODCAST_NURSE_TALK("streamPodcastNurseTalk"),
        STREAM_STATION_CNN("streamStationCNN"),
        STREAM_STATION_LA_JEFA("streamStationLaJefa"),
        STREAM_STATION_WITH_MP3_FORMAT("streamStationWithMP3Format"),
        STREAM_STATION_CBS_MUSIC_FM("streamStationCBSMusicFM"),
        STREAM_STATION_FM_CORDOBA("streamStationFMCordoba"),
        STREAM_STATION_RADIO_X("streamStationRadioX"),
        STREAM_STATION_RADIO_538("streamStationRadio538"),
        STREAM_STATION_AAC_FORMAT("streamStationAAC"),
        STREAM_STATION_BBC_RADIO_5("streamStationBBCRadio5"),
        STREAM_GAME_REPLAY("streamGameReplay"),
        STREAM_STATION_WITH_MUSIC_AAC("streamStationWithMusicAAC"),
        STREAM_PROGRAM_WITH_ACTIVE_SHOW("streamStationChristmasMusic"),
        STREAM_STATION_WITH_ACTIVE_SHOW("streamStationWithActiveShow"),
        STREAM_NEWS_PREMIUM_FOX_NEWS("streamNewsPremiumFoxNews"),
        STREAM_STATION_RESTRICTED("streamStationRestricted"),
        STREAM_FREE_SHORT_PODCAST("streamFreeShortShortPodcast"),
        STREAM_SECOND_PREMIUM_OWNED_AND_OPERATED("streamSecondPremiumOwnAndOperated"),
        STREAM_FREE_STREAM_FROM_PREMIUM_OWNED_AND_OPERATED("streamFreeStreamFromPremiumOwnAndOperated"),
        STREAM_FREE_PODCAST_WITH_LONG_EPISODE("streamFreePodcastWithLongEpisode"),
        STREAM_ARTIST_MADONNA("streamArtistProfileMadonna"),
        STREAM_ARTIST_DRAKE("streamArtistProfileDrake"),
        STREAM_SUGGESTED_ARTIST("streamSuggestedArtistProfile"),
        STREAM_ALBUM_BEDTIME_STORIES_MADONNA("streamAlbumBedtimeStoriesByMadonna"),
        STREAM_SHOW("streamShowKQEDNewsroom"),
        STREAM_AL_JAZEERA("streamNewsPremiumAlJazeera"),
        STREAM_PREMIUM_NEWS_MSNBC("streamNewsPremiumMSNBC"),
        STREAM_STAR_TALK_RADIO_PODCAST("streamStarTalkRadioPodcast"),
        STREAM_STAR_TALK_RADIO_LIVE_STATION("streamStarTalkRadioLiveStation"),
        STREAM_PODCAST_THE_DAILY("streamPodcastTheDaily"),
        TEAM_CATEGORY_BALTIMORE_ORIOLES("teamCategoryBaltimoreOrioles"),
        STREAM_STATION_ESPN("streamEspnRadio"),
        STREAM_PREMIUM_PODCAST_JSA("streamPremiumPodcast"),
        STREAM_PODCAST_TED_TALK_DAILY("streamPodcastTEDTalksDaily"),
        STREAM_PODCAST_UNRESOLVED("streamPodcastUnresolved"),
        STREAM_UNAVAILABLE_STATION("streamUnAvailableStation"),
        STREAM_AUDIOBOOK_GATSBY("streamAudiobookGatsby"),
        STREAM_WITH_LISTEN_LIVE_OR_REWIND_BUTTON("streamWithListenLiveOrRewindScreen"),
        STREAM_KBMR("streamKBMRStation"),
        STREAM_DELETED("streamDeletedStation"),
        STREAM_AUDIOBOOK_GATSBY_LAST_CHAPTER("streamAudiobookGatsbyLastChapter"),
        STREAM_AUDIOBOOK_LIAR("streamAudiobookLiar"),
        STREAM_STATION_WILD_94("streamStationWild94"),
        STREAM_STATION_WITH_PREROLL("streamStationWithPreroll"),
        STREAM_STATION_IMA_VIDEO("streamStationIMAVideo"),
        STREAM_STATION_ADSWIZZ_INSTREAM_AD("streamStationAdswizzInstreamAd"),
        STREAM_STATION_ADSWIZZ_AD("streamStationAdswizzAd"),
        STREAM_STATION_WITH_MIDROLL("streamStationWithMidroll"),
        STREAM_STATION_WITH_SWITCH("streamStationWithSwitch"),
        STREAM_STATION_WITH_SWITCH_ADDITIONAL("streamStationWithSwitchAdditional"),
        STREAM_STATION_IMA_VIDEO_SECOND("streamStationIMAVideoSecond"),
        STREAM_STATION_IMA_VIDEO_THIRD("streamStationIMAVideoThird"),
        STREAM_STATION_INSTREAM_GAM_AD("streamStationInstreamGamAd"),
        STREAM_STATION_IMA_VIDEO_AD("streamStationWithTwoImaVideoPrerolls"),
        STREAM_STATION_IMA_VIDEO_AND_AUDIO_AD("streamStationWithImaVideoAndAudioPrerolls"),
        STREAM_STATION_IMA_AUDIO_AD("streamStationWithTwoImaAudioPrerolls"),
        STREAM_PODCAST_ADS_EXPLICITLY_ENABLED("streamPodcastAdsExplicitlyEnabled"),
        STREAM_PODCAST_ADS_EXPLICITLY_DISABLED("streamPodcastWithAdsExplicitlyDisabled"),
        STREAM_STATION_NON_PUBLIC("streamStationNonPublic"),
        STREAM_SWITCH_STATION_WITH_STOP_BUTTON("streamSwitchStationWithStopButton"),
        STREAM_SWITCH_STATION_WITH_PAUSE_BUTTON("streamSwitchStationWithPauseButton"),
        STREAM_STATION_WITH_SWITCH_AND_MIDROLL("streamStationWithSwitchAndMidroll"),
        STREAM_STATION_WITH_SWITCH_AND_MIDROLL_SECOND("streamStationWithSwitchAndMidrollSecond"),
        STREAM_STATION_SWITCH_MIX("streamStationSwitchMix"),
        PREROLL_DISABLED_STATION("prerollDisabledStation"),
        STREAM_STATION_WITH_DISABLED_PREROLLS("streamStationWithDisabledPrerolls"),
        STREAM_STATION_KFJC("streamStationKFJC");

        private String contentTag;

        private ContentTags(String contentTag) {
            this.contentTag = contentTag;
        }

        public String getContentTag() {
            return contentTag;
        }

    }

    public enum StreamFormat {
        STREAM_TYPE_MP3("MP3"),
        STREAM_TYPE_AAC("AAC"),
        STREAM_TYPE_HLS("HLS"),
        STREAM_TYPE_QT("QT"),
        STREAM_TYPE_OGG("OGG"),
        STREAM_TYPE_FLASH("FLASH");

        private String streamFormat;

        private StreamFormat(String streamFormat) {
            this.streamFormat = streamFormat;
        }

        public String getStreamFormat() {
            return streamFormat;
        }

        public static StreamFormat getStreamFormat(final String streamFormatValue) {
            List<ContentProvider.StreamFormat> streamFormatList = Arrays.asList(ContentProvider.StreamFormat.values());
            return streamFormatList.stream().filter(eachContent -> eachContent.toString().equals(streamFormatValue))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return streamFormat;
        }
    }

    public enum StreamBitrate {
        BIT8("8kbps"),
        BIT16("16kbps"),
        BIT32("32kbps"),
        BIT64("64kbps"),
        BIT96("96kbps"),
        BIT128("128kbps"),
        BIT160("160kbps"),
        BIT192("192kbps"),
        BIT256("256kbps"),
        BIT320("320kbps");

        private String bitrateValue;

        private StreamBitrate(String bitrateValue) {
            this.bitrateValue = bitrateValue;
        }

        public String getBitrateValue() {
            return bitrateValue;
        }
    }

}
