package com.tunein.mobile.pages.android.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.EditUserProfilePage;
import com.tunein.mobile.utils.ReporterUtil;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static io.appium.java_client.AppiumBy.id;

public class AndroidEditUserProfilePage extends EditUserProfilePage {

    protected SelenideElement publicFavorites = $(id("tunein.player:id/publicFavoritesSwitch")).as("Public favorites");

    @Override
    public HashMap<String, SelenideElement> editProfilePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put("Profile Photo", profilePhoto);
        elementsMap.put("Name", profileName);
        elementsMap.put("Password", changePassword);
        elementsMap.put("Public Favorites", publicFavorites);
        return elementsMap;
    }

    @Step("Change user name on {newName}")
    @Override
    public EditUserProfilePage changeName(String newName) {
        deviceNativeActions.typeText(editProfileName, newName);
        clickOnElement(saveButton);
        return this;
    }

    @Step("Close edit user profile page")
    @Override
    public void closeEditUserProfilePage() {
        navigationAction.tapBackButtonIfDisplayed();
    }

    @Override
    public void closePasswordChangedSuccessfullyPopUp() {
        ReporterUtil.log("Functionality is absent for Android Platform");
    }
}
