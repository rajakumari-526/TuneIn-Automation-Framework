package com.tunein.mobile.pages.android.navigation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.TeamsPage;

import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class AndroidTeamsPage extends TeamsPage {

    public ElementsCollection selectedItems = $$(xpath("//*[@resource-id=\"tunein.player:id/selectedOverlay\"]")).as("Selected items");

    @Step
    @Override
    public TeamsPage unselectItemsFromTeams(int noOfTeams) {
        int noOfTeamsSize = selectedItems.size();
        assertThat(noOfTeamsSize).as("Expected team size and selected teams size do not match").isEqualTo(noOfTeams);
        for (SelenideElement element : selectedItems) {
            clickOnElement(element);
        }
        return this;
    }
}
