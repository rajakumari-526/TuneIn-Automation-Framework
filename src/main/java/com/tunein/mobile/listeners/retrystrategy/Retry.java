package com.tunein.mobile.listeners.retrystrategy;

import com.tunein.mobile.utils.ReporterUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static com.tunein.mobile.conf.ConfigLoader.config;

public class Retry implements IRetryAnalyzer {
    private int count = 0;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {                      //Check if test not succeed
            if (count < config().appiumTestRetryNumber()) {  //Check if maxtry count is reached
                count++;                                     //Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE);  //Mark test as failed
                ReporterUtil.log("Retry to run " + iTestResult.getMethod());
                return true;                                 //Tells TestNG to re-run the test
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);  //If maxCount reached,test marked as failed
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return false;
    }
}
