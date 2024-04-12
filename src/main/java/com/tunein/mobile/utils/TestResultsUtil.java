package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.testng.TestNGService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tunein.mobile.api.testrail.util.TestPlanCreator.getTestPlanCreator;
import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.pages.BasePage.isIos;
import static java.util.Optional.ofNullable;

public class TestResultsUtil {

    @Step("Get test value for {attribute} in ReportPortal for tests {testName}")
    public static String getValueForTestWithName(String testName, String attribute) {
        try {
            String launchId = getLaunchId();
            ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "https://tunein.reportportal.io/api/v1/" + config().reportPortalProject() + "/item?filter.eq.launchId=" + launchId + "&filter.eq.name=" + testName + "&isLatest=true", "-H", "accept: */*", "-H", "Authorization: bearer " + config().reportPortalToken());
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            JsonElement element = JsonParser.parseReader(isr);
            JsonArray array = element.getAsJsonObject().get("content").getAsJsonArray();
            List<JsonElement> results = IntStream.range(0, array.size())
                    .mapToObj(array::get)
                    .collect(Collectors.toList());
            JsonElement el = results.stream().reduce((a, b) ->
                    Long.parseLong(a.getAsJsonObject().get("startTime").toString()) > Long.parseLong(b.getAsJsonObject().get("startTime").toString()) ? a : b).get();
             return el.getAsJsonObject().get(attribute).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Get ReportPortal launch id")
    public static String getLaunchId() {
        try {
            String launchUuid = TestNGService.ITEM_TREE.getLaunchId().blockingGet();
            ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", "https://tunein.reportportal.io/api/v1/" + config().reportPortalProject() + "/launch?filter.eq.uuid=" + launchUuid, "-H", "accept: */*", "-H", "Authorization: bearer " + config().reportPortalToken());
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            JsonElement content = JsonParser.parseReader(isr).getAsJsonObject().get("content");
            return content.getAsJsonArray().get(0).getAsJsonObject().get("id").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Get url for test with name {testName}")
    public static String getUrlForTestWithName(String testName) {
        String launchId = getLaunchId();
        String testPath = getValueForTestWithName(testName, "path").replaceAll("\\.", "/").replaceAll("\"", "");
        return "https://tunein.reportportal.io/ui/#" + config().reportPortalProject() + "/launches/all/" + launchId + "/" + testPath + "/log";
    }

    @Step("Set status {testStatus} for test with name {testName}")
    public static void setStatusForTestWithName(TestStatus testStatus, String testName) {
        try {
            String testId = getValueForTestWithName(testName, "id");
            ProcessBuilder pb = new ProcessBuilder("curl", "-X", "PUT", "https://tunein.reportportal.io/api/v1/" + config().reportPortalProject() + "/item/" + testId + "/update", "-H", "accept: */*", "-H", "Content-Type: application/json", "-H", "Authorization: bearer " + config().reportPortalToken(), "-d", "{  \"status\" : \"" + testStatus.getTestStatus() + "\" }");
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Generate ReportPortal URL")
    public static String getReportPortalUrl() {
        ListenerParameters parameters = ofNullable(Launch.currentLaunch())
                .map(Launch::getParameters)
                .orElseThrow(() -> new IllegalStateException("Launch not found"));
        String launchUuid = TestNGService.ITEM_TREE.getLaunchId().blockingGet();
        return parameters.getBaseUrl() + "/ui/#" + parameters.getProjectName() + "/launches/all/" + launchUuid;
    }

    @Step("Get and post ReportPortal Url")
    public static void getAndPostReportPortalLaunchId() {
        String reportPortalURL = getReportPortalUrl();

        if (!config().storeReportPortalUrlInFile()) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(reportPortalURL), null);
        } else {
            try {
                String fileName = isIos() ? "ios_report_portal_launch_id.txt" : "android_report_portal_launch_id.txt";
                File myObj = new File(fileName);

                if (myObj.createNewFile()) {
                    ReporterUtil.log("File created: " + myObj.getName());
                } else {
                    ReporterUtil.log("File already exists.");
                }

                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(reportPortalURL);
                myWriter.close();
            } catch (IOException e) {
                ReporterUtil.log("An error occurred.");
                e.printStackTrace();
            }
        }
        ReporterUtil.log("Launch URL: " + reportPortalURL);
    }

    @Step("Get and post TestRail TestPlan Url")
    public static void getAndPostTestRailTestPlanUrl() {
        String testRailTestPlanURL = getTestPlanCreator().getCreatedTestPlan().getUrl();
        if (!config().storeTestRailTestPlanUrlInFile()) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(testRailTestPlanURL), null);
        } else {
            try {
                String fileName = isIos() ? "ios_testrail_test_plan.txt" : "android_testrail_test_plan.txt";
                File myObj = new File(fileName);
                if (myObj.createNewFile()) {
                    ReporterUtil.log("File created: " + myObj.getName());
                } else {
                    ReporterUtil.log("File already exists.");
                }
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(testRailTestPlanURL);
                myWriter.close();
            } catch (IOException e) {
                ReporterUtil.log("An error occurred.");
                e.printStackTrace();
            }
        }
        ReporterUtil.log("TestRail Test Plan URL: " + testRailTestPlanURL);
    }

    public enum TestStatus {
        PASSED("PASSED"),
        FAILED("FAILED"),
        SKIPPED("SKIPPED");
        private String testStatus;

        private TestStatus(String testStatus) {
            this.testStatus = testStatus;
        }

        public String getTestStatus() {
            return testStatus;
        }

    }

}
