package com.tunein.mobile.api.testrail.util;

import com.tunein.mobile.api.testrail.dto.Entry;
import com.tunein.mobile.api.testrail.dto.TestPlan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.tunein.mobile.api.testrail.TestRailService.TEST_RAIL_SERVICE;
import static com.tunein.mobile.conf.ConfigLoader.config;

public class TestPlanCreator {
    private static volatile TestPlanCreator testPlanInstance;

    private TestPlan createdTestPlan;

    private final static String PLAN_NAME_PREFIX = config().buildName().toLowerCase();

    //ToDo check on parallel run if Singleton implementation needed
    public static TestPlanCreator getTestPlanCreator() {
        TestPlanCreator localInstance = testPlanInstance;
        if (localInstance == null) {
            synchronized (TestPlanCreator.class) {
                localInstance = testPlanInstance;
                if (localInstance == null) {
                    localInstance = new TestPlanCreator();
                    testPlanInstance = localInstance;
                }
            }
        }
        return localInstance;
    }

    public TestPlan getCreatedTestPlan() {
        return createdTestPlan;
    }

    private String getDescription() {
        StringBuilder description = new StringBuilder();
        description
                .append("**Test Plan on platform:** ")
                .append(config().mobileOS())
                .append(System.lineSeparator())
                .append("\n");
        return description.toString();
    }

    public void createTestPlan(Map<String, String> caseIds) {
        if (createdTestPlan == null) {
            TestPlan plan = new TestPlan();
            plan.setName(Calendar.getInstance().getTime() + " " + PLAN_NAME_PREFIX);
            plan.setDescription(getDescription());
            List<String> suiteIdsList = List.of(config().testRailSuiteIds().split(","));
            List<Entry> entriesList = new ArrayList<>();
            for (String suiteId: suiteIdsList) {
                Entry entry = new Entry();
                entry.setSuiteId(Integer.valueOf(suiteId));
                entry.setIncludeAll(false);
                List<String> suitIdOfTestCase = new ArrayList<>();
                caseIds.forEach((key, val) -> {
                    if (val.equals(suiteId)) {
                        suitIdOfTestCase.add(key);
                    }
                });
                if (!suitIdOfTestCase.isEmpty()) {
                    entry.setCaseIds(suitIdOfTestCase);
                    entriesList.add(entry);
                }
            }
            plan.setEntries(entriesList);
            createdTestPlan = TEST_RAIL_SERVICE.addTestPlan(config().testRailProjectId(), plan);
        }
    }
}

