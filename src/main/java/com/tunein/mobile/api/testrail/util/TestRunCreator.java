package com.tunein.mobile.api.testrail.util;

import com.tunein.mobile.api.testrail.dto.Run;

import java.util.Calendar;
import java.util.List;

import static com.tunein.mobile.api.testrail.TestRailService.TEST_RAIL_SERVICE;
import static com.tunein.mobile.conf.ConfigLoader.config;

public class TestRunCreator {

    private static volatile TestRunCreator testRunInstance;

    private Run createdTestRun;

    private final static String RUN_NAME_PREFIX = config().suite().toUpperCase()
            + " AUTOTESTS "
            + config().mobileOS().toUpperCase();

    //ToDo check on parallel run if Singleton implementation needed
    public static TestRunCreator getTestRunCreator() {
        TestRunCreator localInstance = testRunInstance;
        if (localInstance == null) {
            synchronized (TestRunCreator.class) {
                localInstance = testRunInstance;
                if (localInstance == null) {
                    localInstance = new TestRunCreator();
                    testRunInstance = localInstance;
                }
            }
        }
        return localInstance;
    }

    public Run getCreatedTestRun() {
        return createdTestRun;
    }

    private String getDescription() {
        StringBuilder description = new StringBuilder();
        description
                .append("**Run on platform:** ")
                .append(config().mobileOS())
                .append(System.lineSeparator())
                .append("**Device:** ")
                .append(config().deviceName())
                .append(System.lineSeparator())
                .append("**Environment:** ")
                .append(config().testRailUrl())
                .append("\n");
        return description.toString();
    }

    public void createTestRun(List<String> caseIds) {
        if (createdTestRun == null) {
            Run run = new Run();
            run.setName(RUN_NAME_PREFIX + " " + Calendar.getInstance().getTime());
            run.setDescription(getDescription());
            run.setSuiteId(Integer.valueOf(config().testRailProjectId()));
            run.setIncludeAll(false);
            run.setCaseIds(caseIds);
            createdTestRun = TEST_RAIL_SERVICE.addRun(config().testRailProjectId(), run);
        }
    }
}
