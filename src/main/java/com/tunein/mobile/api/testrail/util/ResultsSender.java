package com.tunein.mobile.api.testrail.util;

import com.tunein.mobile.api.testrail.dto.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tunein.mobile.api.testrail.TestRailService.TEST_RAIL_SERVICE;
import static com.tunein.mobile.api.testrail.util.TestPlanCreator.getTestPlanCreator;
import static com.tunein.mobile.conf.ConfigLoader.config;

@Data
@Accessors(chain = true)
public class ResultsSender {

    private static final String MINIMAL_RUN_TIME = "1s";

    private static final String ZERO_RUN_TIME = "0";

    private static final String ELAPSED_TIME_FORMAT = "H:m:s";

    private static final String COMMA_SPLIT = ",";

    private static final String COLON_SPLIT = ":";

    private final static Map<String, String> CASE_IDS_MAP = getCaseIdsMap();

    private String testMethodName;

    private String testMethodDescription;

    private String testCaseId;

    private TestRailStatus testResultStatus;

    private String pathToScreenShot;

    private String issues;

    private String comments;

    private Long runTime;

    private static Map<String, String> getCaseIdsMap() {
        Map<String, String> testCases = new ConcurrentHashMap<>();
        String planId = config().testRailPlanId();
        if (!config().useExistingTestPlan()) {
            planId = String.valueOf(getTestPlanCreator().getCreatedTestPlan().getId());
        }
        if (planId == null || planId.isEmpty()) {
            throw new RuntimeException("Test plan id is not set up");
        }
        List<Test.Tests> testList = new ArrayList<>();
        TestPlan testPlan = TEST_RAIL_SERVICE.getTestPlan(planId);
        List<Entry> entriesList = testPlan.getEntries();
        for (Entry entry: entriesList) {
          Test.Tests[] tests = TEST_RAIL_SERVICE.getTests(String.valueOf(entry.runs.get(0).id)).tests;
            testList.addAll(Arrays.asList(tests));
        }
        testList.forEach(test -> {
            String testId = String.valueOf(test.id);
            String testCaseId = String.valueOf(test.caseId);

            testCases.put(testCaseId, testId);
        });

        return testCases;
    }

    public void sendResults() {
        if (testCaseId.contains(COMMA_SPLIT)) {
            Arrays.asList(testCaseId.split(COMMA_SPLIT)).forEach(this::sendResult);
        } else {
            sendResult(testCaseId);
        }
    }

    private void sendResult(String testCaseId) {
        String testId = CASE_IDS_MAP.get(testCaseId);
        if (testId != null) {
            TestResults testResults = new TestResults();
            testResults.setStatusId(testResultStatus.getTestRailStatus());
            testResults.setElapsed(createRunTimeFormat(runTime));
            if (comments == null || comments.isEmpty()) {
                testResults.setComment("Auto test that has been executed: " + testMethodName + " (" + testMethodDescription + ")");
            } else {
                testResults.setComment(comments);
            }
            testResults.setDefects(issues);
            Result result = TEST_RAIL_SERVICE.sendTestResult(testId, testResults);
            if (pathToScreenShot != null) {
                TEST_RAIL_SERVICE.attachScreenshotToResult(String.valueOf(result.id), pathToScreenShot);
            }
        }
    }

    public static String getTestId(String testCaseId) {
        return CASE_IDS_MAP.get(testCaseId);
    }

    private String createRunTimeFormat(long durationMillis) {
        String format = DurationFormatUtils.formatDuration(durationMillis, ELAPSED_TIME_FORMAT, true);
        String[] formatSplit = format.split(COLON_SPLIT);
        if (!formatSplit[0].equalsIgnoreCase(ZERO_RUN_TIME)) {
            return String.format("%sh %sm %ss", formatSplit[0], formatSplit[1], formatSplit[2]);
        } else if (!formatSplit[1].equalsIgnoreCase(ZERO_RUN_TIME)) {
            return String.format("%sm %ss", formatSplit[1], formatSplit[2]);
        } else if (!formatSplit[2].equalsIgnoreCase(ZERO_RUN_TIME)) {
            return String.format("%ss", formatSplit[2]);
        } else {
            return MINIMAL_RUN_TIME;
        }
    }

}
