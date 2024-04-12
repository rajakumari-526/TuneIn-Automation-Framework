package com.tunein.mobile.testdata.dataprovider;

import com.google.gson.*;
import com.tunein.mobile.testdata.models.Users;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.tunein.mobile.pages.BasePage.isAndroid;
import static com.tunein.mobile.utils.DataUtil.*;
import static com.tunein.mobile.utils.DataUtil.getRandomString;

public class UserProvider {

    private static final String FILE_WITH_USERS = "users/Users.json";

    public static final Users USER_GENERAL = getUser(UserType.USER_GENERAL);

    public static final Users USER_FOR_LOGIN_TEST = getUser(UserType.USER_FOR_LOGIN_TEST);

    public static final Users USER_FOR_RECENTS_TEST = getUser(UserType.USER_FOR_RECENTS_TEST);

    public static final Users USER_FOR_FAVORITE_TEST = getUser(UserType.USER_FOR_FAVORITE_TEST);

    public static final Users USER_CARMODE = getUser(UserType.USER_CARMODE);

    public static final Users USER_PREMIUM = getUser(UserType.USER_PREMIUM);

    public static final Users USER_GOOGLE = getUser(UserType.USER_GOOGLE);

    public static final Users USER_FACEBOOK = getUser(UserType.USER_FACEBOOK);

    public static final Users USER_SMARTLOCK = getUser(UserType.USER_SMARTLOCK);

    public static final Users USER_WITHOUT_FAVORITES = getUser(UserType.USER_WITHOUT_FAVORITES);

    public static final Users USER_WITH_FAVORITES = getUser(UserType.USER_WITH_FAVORITES);

    public static final Users USER_WITH_TEAMS_FAVORITED = getUser(UserType.USER_WITH_TEAMS_FAVORITED);

    public static final Users USER_WITHOUT_RECENTS = getUser(UserType.USER_WITHOUT_RECENTS);

    public static final Users USER_WITH_MANY_FAVORITES = getUser(UserType.USER_WITH_MANY_FAVORITES);

    public static final Users USER_STATION_WITH_RECENTS = getUser(UserType.USER_STATION_WITH_RECENTS);

    public static final Users USER_WITH_PREMIUM_RECENTS = getUser(UserType.USER_WITH_PREMIUM_RECENTS);

    public static final Users USER_WITH_FAVORITES_FOR_LOGIN_TEST = getUser(UserType.USER_WITH_FAVORITES_FOR_LOGIN_TEST);

    public static final String[] INVALID_PASSWORDS = {
            getRandomString(8),
            getRandomString(33) + 1,
            "aaa222",
            Long.toString(genShortUniqueNumber())
    };

    public static Users getUser(UserType type) {
        Users user = new Users();
        Gson gson = new GsonBuilder().create();
        try {
            File jsonFile = new File(UserProvider.class.getClassLoader().getResource("users/Users.json").getFile());
            FileReader reader = new FileReader(jsonFile.getAbsolutePath());
            String toString = IOUtils.toString(reader);
            JsonArray jsonArray = JsonParser.parseString(toString).getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                if (object.has(type.getValue())) {
                    user = gson.fromJson(object.get(type.getValue()), Users.class);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static Users generateRandomUser() {
        Users user = new Users();
        String[] genders = {"Male", "Female", "Non-Binary", "Prefer Not to Say"};
        user.setUsername("tunein" + genShortUniqueNumber());
        user.setEmail(user.getUsername() + "@gmail.com");
        user.setPassword("Tunein" + genShortUniqueNumber());
        user.setProfileName(isAndroid() ? "Autotest" + genShortUniqueNumber() : user.getUsername());
        user.setUserBirthYear(getRandomNumberInRange(1970, 1995));
        user.setUserGender(genders[new Random().nextInt(genders.length)]);
        return user;
    }

    public enum UserType {
        USER_GENERAL("userGeneral"),
        USER_FOR_LOGIN_TEST("userLoginTest"),
        USER_FOR_RECENTS_TEST("userRecents"),
        USER_FOR_FAVORITE_TEST("userFavoriteTest"),
        USER_CARMODE("userCarmode"),
        USER_PREMIUM("userPremium"),
        USER_FACEBOOK("userFacebook"),
        USER_GOOGLE("userGoogle"),
        USER_SMARTLOCK("userSmartLock"),
        USER_WITHOUT_FAVORITES("userWithoutFavorites"),
        USER_WITH_FAVORITES("userWithFavorites"),
        USER_WITH_TEAMS_FAVORITED("userWithTeamsFavorited"),
        USER_WITHOUT_RECENTS("userWithoutRecents"),
        USER_STATION_WITH_RECENTS("userStationWithRecents"),
        USER_WITH_PREMIUM_RECENTS("userWithPremiumRecents"),
        USER_WITH_MANY_FAVORITES("userWithManyFavorites"),
        USER_WITH_FAVORITES_FOR_LOGIN_TEST("loginUserTest");

        private String value;

        private UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum UserGenderType {
        MALE("Male"),
        FEMALE("Female"),
        NON_BINARY("Non-Binary"),
        PREFER_NOT_TO_SAY("Prefer Not to Say");

        private String value;

        private UserGenderType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserGenderType getUserGenderEnumType(final String value) {
            List<UserGenderType> genderList = Arrays.asList(UserGenderType.values());
            return genderList.stream().filter(eachContent -> eachContent.toString().equals(value))
                    .findAny()
                    .orElse(null);
        }

        @Override
        public String toString() {
            return value;
        }

    }
}
