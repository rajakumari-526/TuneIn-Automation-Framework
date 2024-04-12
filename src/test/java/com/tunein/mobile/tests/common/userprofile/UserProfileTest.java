package com.tunein.mobile.tests.common.userprofile;

import com.tunein.BaseTest;
import com.tunein.mobile.annotations.TestCaseId;
import org.testng.annotations.Test;

import static com.tunein.mobile.testdata.TestGroupName.ACCEPTANCE_TEST;

public abstract class UserProfileTest extends BaseTest {

    @TestCaseId("749131")
    @Test(description = "test the settings in about TuneIn page", groups = {ACCEPTANCE_TEST})
    public abstract void testSettingsInAboutTuneInPage();
}
