package com.tunein.mobile.conf;

import org.aeonbits.owner.Config;

public interface TestRailConfig extends Config {

    @Key("suite")
    @DefaultValue("")
    String suite();

    @Key("testrail.url")
    @DefaultValue("https://tunein.testrail.io")
    String testRailUrl();

    @Key("testrail.user")
    @DefaultValue("")
    String testRailUser();

    @Key("testrail.password")
    @DefaultValue("")
    String testRailPassword();

    @Key("testrail.authorization.token")
    String testRailAuthorizationToken();

    @Key("testrail.project.id")
    String testRailProjectId();

    @Key("testrail.run.id")
    String testRailRunId();

    @Key("testrail.plan.id")
    String testRailPlanId();

    @Key("testrail.suite.ids")
    String testRailSuiteIds();

    @Key("post.testrail.result")
    @DefaultValue("false")
    boolean postTestRailResult();

    @Key("use.existing.run.id")
    @DefaultValue("true")
    boolean useExistingTestRun();

    @Key("use.existing.plan.id")
    @DefaultValue("true")
    boolean useExistingTestPlan();

    @Key("store.test.plan.url.in.file")
    @DefaultValue("false")
    boolean storeTestRailTestPlanUrlInFile();

}
