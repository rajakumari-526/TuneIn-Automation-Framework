package com.tunein.mobile.pages.common.userprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;

import java.time.Duration;
import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.waitTillVisibilityOfElement;
import static io.appium.java_client.AppiumBy.*;

public abstract class EditUserProfilePage extends BasePage {

    public static final String PASSWORD_SUCCESS_TEXT = "Your password was changed successfully.";

    protected SelenideElement editProfileTitle = $(android(id("tunein.player:id/generalTextLabel"))
            .ios(iOSNsPredicateString("name == 'General'"))).as("Edit profile title");

    protected SelenideElement profilePhoto = $(android(id("tunein.player:id/photoLabelTxt"))
            .ios(iOSNsPredicateString("label == 'Profile Photo'"))).as("Photo");

    protected SelenideElement profileName = $(android(id("tunein.player:id/displayNameLabelTxt"))
            .ios(iOSNsPredicateString("label == 'Name' AND name == 'editProfileChangePasswordId'"))).as("Name");

    protected SelenideElement editProfileName = $(android(id("tunein.player:id/displayNameEditText"))
            .ios(iOSNsPredicateString("name == 'editProfileNameFieldId'"))).as("Edit profile name");

    protected SelenideElement changePassword = $(android(id("tunein.player:id/passwordLabelTxt"))
            .ios(iOSNsPredicateString("label == 'Change Password' AND name == 'editProfileNameId'"))).as("Change password");

    protected SelenideElement currentPassword = $(android(id("tunein.player:id/currentPasswordEdt"))
            .ios(iOSNsPredicateString("name == 'currentPasswordTextFieldId'"))).as("Current password");

    protected SelenideElement newPassword = $(android(id("tunein.player:id/newPasswordEdt"))
            .ios(iOSNsPredicateString("name == 'newPasswordTextFieldId'"))).as("New password");

    protected SelenideElement confirmNewPassword = $(android(id("tunein.player:id/confirmPasswordEdt"))
            .ios(iOSNsPredicateString("name == 'confirmPasswordTextFieldId'"))).as("Confirm new password");

    protected SelenideElement saveButton = $(android(androidUIAutomator("text(\"SAVE\")"))
            .ios(iOSNsPredicateString("label == 'Save' OR label == 'SAVE'"))).as("Save button");

    @Override
    public EditUserProfilePage waitUntilPageReady() {
        closePermissionPopupsIfDisplayed();
        waitTillVisibilityOfElement(editProfileTitle, Duration.ofSeconds(config().pageReadyTimeoutSeconds()));
        return this;
    }

    @Step("Change user password ")
    public EditUserProfilePage changePassword(String oldPassword, String enterNewPassword) {
        clickOnElement(changePassword);
        waitTillVisibilityOfElement(currentPassword).setValue(oldPassword);
        waitTillVisibilityOfElement(newPassword).setValue(enterNewPassword);
        waitTillVisibilityOfElement(confirmNewPassword).setValue(enterNewPassword);
        clickOnElement(saveButton);
        closePasswordChangedSuccessfullyPopUp();
        return editUserProfilePage.waitUntilPageReady();
    }

    public abstract EditUserProfilePage changeName(String newName);

    public abstract HashMap<String, SelenideElement> editProfilePageElements();
    
    public abstract void closeEditUserProfilePage();

    public abstract void closePasswordChangedSuccessfullyPopUp();

}
