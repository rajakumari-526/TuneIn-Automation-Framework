package com.tunein.mobile.pages.ios.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.userprofile.EditUserProfilePage;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.tunein.mobile.pages.alert.Alert.OK_BUTTON_TEXT;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;

public class IosEditUserProfilePage extends EditUserProfilePage {

    protected SelenideElement cancelButton = $((iOSNsPredicateString("label == 'CANCEL' AND type =='XCUIElementTypeButton'"))).as("Cancel button");

    protected SelenideElement editEmailId = $((iOSNsPredicateString("label == 'Email' AND name == 'editProfileEmailId'"))).as("Email");

    protected SelenideElement privateProfile = $((iOSNsPredicateString("label == 'Private Profile' AND name == 'editProfilePrivateProfileLabelId'"))).as("Private profile");

    protected SelenideElement privateProfileToggle = $((iOSNsPredicateString("label == 'Private Profile' AND name == 'editProfilePrivateProfileSwitchId'"))).as("Private profile toggle");

    @Override
    public HashMap<String, SelenideElement> editProfilePageElements() {
        HashMap<String, SelenideElement> elementsMap = new HashMap<>();
        elementsMap.put(SKIP_TEXT_VALIDATION_PREFIX + "0", privateProfileToggle);
        elementsMap.put("Profile Photo", profilePhoto);
        elementsMap.put("Name", profileName);
        elementsMap.put("Email", editEmailId);
        elementsMap.put("Change Password", changePassword);
        elementsMap.put("Private Profile", privateProfile);
        return elementsMap;
    }

    @Step("Close edit user profile page")
    @Override
    public void closeEditUserProfilePage() {
        clickOnElement(cancelButton);
    }

    @Step("Change user name on {newName}")
    @Override
    public EditUserProfilePage changeName(String newName) {
        deviceNativeActions.typeText(editProfileName, newName);
        clickOnElement(privateProfileToggle);
        clickOnElement(saveButton);
        return this;
    }

    @Step("Close popup (password changed successfully)")
    @Override
    public void closePasswordChangedSuccessfullyPopUp() {
        alert.handleAlertIfDisplayed(PASSWORD_SUCCESS_TEXT, OK_BUTTON_TEXT);
    }

}
