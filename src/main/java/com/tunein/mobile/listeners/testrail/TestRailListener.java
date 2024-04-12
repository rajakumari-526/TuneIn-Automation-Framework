package com.tunein.mobile.listeners.testrail;

import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import com.tunein.mobile.api.testrail.dto.*;
import com.tunein.mobile.api.testrail.util.AnnotationParser;
import com.tunein.mobile.api.testrail.util.ResultsSender;
import com.tunein.mobile.api.testrail.util.TestPlanCreator;
import com.tunein.mobile.api.testrail.util.TestRailStatus;
import com.tunein.mobile.utils.ReporterUtil;
import org.testng.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Screenshots.takeScreenShotAsFile;
import static com.tunein.mobile.api.testrail.TestRailService.TEST_RAIL_SERVICE;
import static com.tunein.mobile.api.testrail.util.ResultsSender.getTestId;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isIos;
import static com.tunein.mobile.utils.TestResultsUtil.getUrlForTestWithName;

public class TestRailListener implements ISuiteListener, ITestListener, IInvokedMethodListener {

    private final static String SCREENSHOT_FILE_PATH = "screenShotFilePath";

    @Override
    public void onStart(ISuite suite) {
        createTestPlanIfNeed(suite);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        sendResultToTestRail(result);
        if (config().postTestRailResult()) updateTestStatusIfOneOfResultsFailed(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (config().postTestRailResult()) {
            try {
                File screen = takeScreenShotAsFile();
                result.setAttribute(SCREENSHOT_FILE_PATH, screen.getAbsolutePath());
            } catch (Exception ex) {
                ReporterUtil.log("Cannot take screenshot");
            }
            sendResultToTestRail(result);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (config().postTestRailResult()) {
            try {
                File screen = takeScreenShotAsFile();
                result.setAttribute(SCREENSHOT_FILE_PATH, screen.getAbsolutePath());
            } catch (Exception ex) {
                ReporterUtil.log("Cannot take screenshot");
            }
            sendResultToTestRail(result);
        }
    }

    private void sendResultToTestRail(ITestResult iTestResult) {
        if (config().postTestRailResult()) {

            AnnotationParser annotationParser = new AnnotationParser();

            ITestNGMethod method = iTestResult.getMethod();
            String testDescription = iTestResult.getMethod().getDescription();

            String testCaseIdAnnotation = annotationParser.getTmsLinkNumber(method);
            TestRailStatus testRailStatus = TestRailStatus.findByTestNgStatus(iTestResult.getStatus());

            if (!method.isTest() || !method.getEnabled()) {
                return;
            }

            long runTime = iTestResult.getEndMillis() - iTestResult.getStartMillis();

            String[] namesArray = iTestResult.getInstanceName().split("\\.");
            String testMethodName = namesArray[namesArray.length - 1] + " -> " + method.getMethodName();

            ResultsSender resultsSender;

            if (iTestResult.getThrowable() == null) {
                resultsSender = new ResultsSender()
                        .setTestMethodName(testMethodName)
                        .setTestMethodDescription(testDescription)
                        .setTestCaseId(testCaseIdAnnotation)
                        .setTestResultStatus(testRailStatus)
                        .setIssues(annotationParser.getIssueNumber(method))
                        .setRunTime(runTime)
                        .setComments("")
                        .setPathToScreenShot(null);

            } else {
                resultsSender = new ResultsSender()
                        .setTestMethodName(testMethodName)
                        .setTestMethodDescription(testDescription)
                        .setTestCaseId(testCaseIdAnnotation)
                        .setTestResultStatus(testRailStatus)
                        .setIssues(annotationParser.getIssueNumber(method))
                        .setRunTime(runTime)
                        .setComments(createFailComment(method, iTestResult.getThrowable()) + "\n\n SCREENSHOT:")
                        .setPathToScreenShot(iTestResult.getAttribute(SCREENSHOT_FILE_PATH).toString());
            }
            resultsSender.sendResults();
        }
    }

    public void updateTestStatusIfOneOfResultsFailed(ITestResult result) {
        AnnotationParser annotationParser = new AnnotationParser();
        ITestNGMethod method = result.getMethod();
        String testCaseIdAnnotation = annotationParser.getTmsLinkNumber(method);
        Arrays.asList(testCaseIdAnnotation.split(",")).forEach(
                testCaseId -> {
                    String testId = getTestId(testCaseId);
                    if (testId != null) {
                        ListOfResults listOfResults = TEST_RAIL_SERVICE.getTestResults(testId);
                        if (listOfResults != null) {
                            List<Result> results = Arrays.asList(listOfResults.results);
                            Result failedResult = results
                                    .stream()
                                    .filter(resultInList -> resultInList.getStatusId() == 5 && resultInList.getElapsed() != null)
                                    .findAny()
                                    .orElse(null);
                            if (failedResult != null) {
                                TestRailStatus testRailStatus = TestRailStatus.FAILED;
                                String[] namesArray = result.getInstanceName().split("\\.");
                                String testMethodName = namesArray[namesArray.length - 1] + " -> " + method.getMethodName();

                                long runTime = result.getEndMillis() - result.getStartMillis();

                                ResultsSender resultsSender = new ResultsSender()
                                        .setTestCaseId(testCaseIdAnnotation)
                                        .setTestResultStatus(testRailStatus)
                                        .setRunTime(runTime)
                                        .setComments("Test is failed because one of the results are failed (" + testMethodName + ")");
                                resultsSender.sendResults();
                            }
                        }
                    }
                }
        );
    }

    private void createTestPlanIfNeed(ISuite suite) {
        if (config().postTestRailResult() && !config().useExistingTestPlan()) {
            try {
                List<String> testCasesIds = getTestCasesIds(suite);
                Map<String, String> filteredTestCaseIds = filterTestCases(config().testRailProjectId(), config().testRailSuiteIds(), testCasesIds);
                TestPlanCreator.getTestPlanCreator().createTestPlan(filteredTestCaseIds);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                ReporterUtil.log("Cannot create TestRail test plan");
            }
        }
    }

    private List<String> getTestCasesIds(ISuite suite) {
        List<String> testCasesIds = new ArrayList<>();
        AnnotationParser annotationParser = new AnnotationParser();

        List<ITestNGMethod> testNGMethods = suite.getAllMethods();

        testNGMethods.forEach(method -> {
            String tms = annotationParser.getTmsLinkNumber(method);
            if (tms.contains(",")) {
                String[] array = tms.split(",");
                testCasesIds.addAll(Arrays.asList(array));
            } else {
                testCasesIds.add(tms);
            }
        });
        return testCasesIds;
    }

    public static Map<String, String> filterTestCases(String projectId, String suiteIds, List<String> testcasesToFilter) {
        Map<String, String> allIds = new HashMap<>();
        Map<String, String> filteredIds = new HashMap<>();
        List<String> suiteIdsList = List.of(suiteIds.split(","));
        for (String suiteId: suiteIdsList) {
            boolean isNextPageEmpty = false;
            int offset = 0;
            while (!isNextPageEmpty) {
                ListOfCases testCases = TEST_RAIL_SERVICE.getTestCases(projectId, "suite_id=" + suiteId, "offset=" + offset);

                Arrays.stream(testCases.getCases())
                        .map(testCase -> testCase.id)
                        .collect(Collectors.toList())
                        .stream()
                        .forEach(key -> {
                            allIds.put(key.toString(), suiteId);
                        });

                if (testCases.links.next == null || testCases.links.next.equalsIgnoreCase("null")) {
                    isNextPageEmpty = true;
                }
                offset = offset + 250;
            }
        }
        for (String testcaseId: testcasesToFilter) {
            if (allIds.keySet().contains(testcaseId)) {
                filteredIds.put(testcaseId, allIds.get(testcaseId));
            }
        }
        return filteredIds;
    }

    private String createFailComment(ITestNGMethod method, Throwable throwable) {
        StringBuilder failComment = new StringBuilder();
        String failReason = throwable
                .getMessage()
                .replaceAll("\n", "")
                .replaceAll("(Screenshot: file).*(html)", "");
        failComment
                .append("FAIL REASON:\n" + failReason + "\n\n")
                .append("AUTOTEST NAME:\n" + method.getMethodName() + "\n\n")
                .append("CLASS: \n" + method.getRealClass() + "\n\n")
                .append("DESCRIPTION: \n" + method.getDescription() + "\n\n");
        if (config().isReportPortalEnabled()) {
            String testName = method.getMethodName();
            failComment.append("ReportPortal TEST URL: \n" + getUrlForTestWithName(testName));
        }
        return failComment.toString();
    }

    private String createTestData(ITestResult iTestResult) {
        StringBuilder testData = new StringBuilder();
        if (iTestResult.getParameters().length > 0) {
            testData.append("\n Test data: \n");
            Arrays.stream(iTestResult.getParameters()).forEach(item -> testData.append(item.toString() + "; "));
        }
        return testData.toString();
    }

    public static void skipTestWithoutTestCaseId(Method method) {
        int projectId = isIos() ? 10 : 13;
        int unifiedProjectId = 29;
        boolean skipTest = true;
        List<TestCaseId> testCaseIdsList = new ArrayList<>();
        TestCaseId testCaseIdFromAnnotation = method.getAnnotation(TestCaseId.class);
        if (testCaseIdFromAnnotation == null) {
            TestCaseIds testCaseIds = method.getAnnotation(TestCaseIds.class);
            testCaseIdsList.addAll(List.of(testCaseIds.value()));
        } else {
            testCaseIdsList.add(testCaseIdFromAnnotation);
        }
        for (TestCaseId testCaseId: testCaseIdsList) {
            SingleCase testCase = TEST_RAIL_SERVICE.getTestCase(testCaseId.value());
            Suite suite = TEST_RAIL_SERVICE.getTestSuite(String.valueOf(testCase.getSuiteId()));
            int testCaseProjectId = suite.projectId;
            if (testCaseProjectId == projectId || testCaseProjectId == unifiedProjectId) {
                skipTest = false;
                break;
            }
        }
        if (skipTest) throw new Error("Skipping " + method.getName() + " test without testcase id");
    }

}
