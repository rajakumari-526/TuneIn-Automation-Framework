package com.tunein.mobile.testdata.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Users {

        String email;

        String password;

        String profileName;

        String username;

        String userGender;

        int userBirthYear;

        int numberOfFavorites;

        int numberOfRecents;

        boolean isPremium;

}
