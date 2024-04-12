package com.tunein.mobile.pages.ios.navigation;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.pages.common.navigation.TeamsPage;

public class IosTeamsPage extends TeamsPage {

    @Step
    @Override
    public TeamsPage unselectItemsFromTeams(int noOfTeams) {
        teamsPage.selectNumberOfTeams(noOfTeams);
        return this;
    }
}
