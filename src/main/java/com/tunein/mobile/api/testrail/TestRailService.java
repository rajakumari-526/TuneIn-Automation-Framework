package com.tunein.mobile.api.testrail;

import com.tunein.mobile.api.retrofit.exception.RequestException;
import com.tunein.mobile.api.testrail.dto.*;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;

import static com.tunein.mobile.api.retrofit.ServiceFactory.createService;
import static com.tunein.mobile.api.testrail.TestRailOkClient.getTestRailOkClient;
import static com.tunein.mobile.conf.ConfigLoader.config;

public enum TestRailService {

    TEST_RAIL_SERVICE;

    private TestRailApi testRailApi;

    TestRailService() {
        testRailApi = createService(TestRailApi.class, getTestRailOkClient(), config().testRailUrl());
    }

    public void attachScreenshotToResult(String resultId, String filePath) {
        try {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("attachment", file.getName(), requestBody);
            testRailApi.attacheScreenShotToResult(resultId, filePart).execute().body();
        } catch (IOException e) {
            throw new RequestException("Attach screen shot to test result", e);
        }
    }

    public Test getTests(String runId) {
        try {
            return testRailApi.getTests(runId).execute().body();
        } catch (IOException e) {
            throw new RequestException("Get tests by run id", e);
        }
    }

    public Result sendTestResult(String testId, TestResults testResults) {
        try {
            return testRailApi.sendTestResult(testId, testResults).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not send result to TestRail for test id: " + testId, ex);
        }
    }

    public Run addRun(String projectId, Run testRun) {
        try {
            return testRailApi.addRun(projectId, testRun).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not add test run for project id: " + projectId, ex);
        }
    }

    public TestPlan addTestPlan(String projectId, TestPlan testPlan) {
        try {
            return testRailApi.addPlan(projectId, testPlan).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not add test plan for project id: " + projectId, ex);
        }
    }

    public TestPlan getTestPlan(String testPlanId) {
        try {
            return testRailApi.getTestPlan(testPlanId).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not find test plan with id: " + testPlanId, ex);
        }
    }

    public ListOfCases getTestCases(String projectId, String suiteId, String offset) {
        try {
            return testRailApi.getCases(projectId, suiteId, offset).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not get test cases for project id: " + projectId, ex);
        }
    }

    public ListOfResults getTestResults(String testId) {
        try {
            return testRailApi.getTestResults(testId).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not get test results for test: " + testId, ex);
        }
    }

    public SingleCase getTestCase(String testCaseId) {
        try {
            return testRailApi.getCase(testCaseId).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not get test case with id: " + testCaseId, ex);
        }
    }

    public Suite getTestSuite(String suiteId) {
        try {
            return testRailApi.getSuite(suiteId).execute().body();
        } catch (IOException ex) {
            throw new RequestException("Could not get test suite with id: " + suiteId, ex);
        }
    }

}
