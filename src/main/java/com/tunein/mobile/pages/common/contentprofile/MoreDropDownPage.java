package com.tunein.mobile.pages.common.contentprofile;

import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.BasePage;
import com.tunein.mobile.pages.dialog.common.ShareDialog;
import com.tunein.mobile.testdata.dataprovider.ContentProvider;

import java.util.HashMap;

import static com.codeborne.selenide.appium.SelenideAppium.$;
import static com.codeborne.selenide.appium.selector.CombinedBy.android;
import static com.tunein.mobile.utils.GestureActionUtil.ScrollDirection.DOWN;
import static com.tunein.mobile.utils.GestureActionUtil.scrollTo;
import static io.appium.java_client.AppiumBy.iOSNsPredicateString;
import static org.openqa.selenium.By.xpath;

public abstract class MoreDropDownPage extends BasePage {

    protected SelenideElement moreDropDownMenu = $(android(xpath("//android.widget.LinearLayout[./*[@resource-id='tunein.player:id/info_contents_layout']]"))
            .ios(xpath("//XCUIElementTypeCell[./XCUIElementTypeStaticText[contains(@name,'profileDetails_')]]"))).as("Menu");

    protected SelenideElement moreDropDownLocation = $(android(xpath("//android.widget.TextView[preceding-sibling::android.widget.TextView[@text='Location:']]"))
            .ios(iOSNsPredicateString("name == 'profileDetails_location'"))).as("Location");

    protected SelenideElement moreDropDownGenres = $(android(xpath("//android.widget.TextView[preceding-sibling::android.widget.TextView[@text='Genres:']]"))
            .ios(iOSNsPredicateString("name == \"profileDetails_genres\""))).as("Genres");

    protected SelenideElement moreDropDownDescription = $(android(xpath("//*[@resource-id='tunein.player:id/info_contents_layout']/android.widget.LinearLayout[3]/android.widget.TextView"))
            .ios(iOSNsPredicateString("name == \"profileDetails_description\""))).as("Description");

    protected SelenideElement moreDropDownWebsiteButton = $(android(xpath("//android.widget.RelativeLayout[./android.widget.TextView[@text='Visit Website']]"))
            .ios(iOSNsPredicateString("name == \"profileDetails_link\""))).as("Website button");

    protected SelenideElement moreDropDownShareButton = $(android(xpath("//android.widget.RelativeLayout[./android.widget.TextView[@text='Share']]"))
            .ios(iOSNsPredicateString("name == \"profileDetails_share\""))).as("Share button");

    protected SelenideElement moreDropDownTwitterButton = $(android(xpath("//*[@resource-id='tunein.player:id/info_buttons_layout']/android.widget.RelativeLayout[./android.widget.TextView[@text!='Share' and @text!='Visit Website']]"))
            .ios(iOSNsPredicateString("name == \"profileDetails_tweet\""))).as("Twitter button");

    /* --- Loadable Component Method --- */

    public abstract MoreDropDownPage waitUntilPageReady();

    /* --- Validation Methods --- */

    public abstract MoreDropDownPage validateMoreDropdownIsExpanded();

    /* --- Helper Methods --- */

    public abstract HashMap<String, SelenideElement> moreDropDownPageElements(ContentProvider.ContentType contentType);

    @Step
    public ShareDialog clickShareButton() {
        clickOnElement(scrollTo(moreDropDownShareButton, DOWN, 2));
        return shareDialog.waitUntilPageReady();
    }

}
