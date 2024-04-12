package com.tunein.mobile.api.testrail.util;

import org.testng.ITestResult;

import java.util.Arrays;

public enum TestRailStatus {

    PASSED(1, ITestResult.SUCCESS),
    FAILED(5, ITestResult.FAILURE),
    FAILED_IF_SKIP(5, ITestResult.SKIP),
    FAILED_IF_SUCCESS_PERCENTAGE(5, ITestResult.SUCCESS_PERCENTAGE_FAILURE);

    int testRailStatus;

    int testNgStatus;

    TestRailStatus(int testRailStatus, int testNgStatus) {
        this.testRailStatus = testRailStatus;
        this.testNgStatus = testNgStatus;
    }

    public int getTestRailStatus() {
        return testRailStatus;
    }

    public int getTestNgStatus() {
        return testNgStatus;
    }

    public static TestRailStatus findByTestNgStatus(int testNgStatus) {
        return Arrays.stream(TestRailStatus.values()).
                filter(status -> status.getTestNgStatus() == testNgStatus).
                findFirst().
                orElse(FAILED);
    }
}
