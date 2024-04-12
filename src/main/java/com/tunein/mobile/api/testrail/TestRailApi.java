package com.tunein.mobile.api.testrail;

import com.tunein.mobile.api.testrail.dto.*;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TestRailApi {

    //"%3F" is encoded symbol "?" It's a crutch for retrofit should be replaced with ? during okhttp client building
    String PATH = "/index.php%3F/api/v2/";

    @GET(PATH + "get_tests/{runId}")
    Call<Test> getTests(@Path("runId") String runId);

    @POST(PATH + "add_result/{testId}")
    Call<Result> sendTestResult(@Path("testId") String testId, @Body TestResults testResults);

    @GET(PATH + "get_results/{testId}")
    Call<ListOfResults> getTestResults(@Path("testId") String testId);

    @POST(PATH + "add_run/{projectId}")
    Call<Run> addRun(@Path("projectId") String projectId, @Body Run testRun);

    @GET(PATH + "get_cases/{projectId}&{suiteId}&{offset}")
    Call<ListOfCases> getCases(@Path("projectId") String projectId, @Path("suiteId") String suiteId, @Path("offset") String offset);

    @GET(PATH + "get_plan/{id}")
    Call<TestPlan> getTestPlan(@Path("id") String planId);

    @GET(PATH + "get_projects")
    Call<List<Project>> getProjects();

    @POST(PATH + "add_plan/{projectId}")
    Call<TestPlan> addPlan(@Path("projectId") String projectId, @Body TestPlan testPlan);

    @POST(PATH + "add_plan_entry/{planId}")
    Call<Entry> addPlanEntry(@Path("planId") String planId, @Body Entry entry);

    @POST(PATH + "update_plan_entry/{planId}/{entryId}")
    Call<Entry> updatePlanEntry(@Path("planId") String planId,
                                @Path("entryId") String entryId, @Body Entry entry);

    @GET(PATH + "get_suites/{projectId}")
    Call<ResponseBody> getSuites(@Path("projectId") String projectId);

    @GET(PATH + "get_suite/{suiteId}")
    Call<Suite> getSuite(@Path("suiteId") String suiteId);

    @GET(PATH + "get_runs/{projectId}")
    Call<List<Run>> getRuns(@Path("projectId") String projectId);

    @GET(PATH + "get_run/{runId}")
    Call<Run> getRun(@Path("runId") String runId);

    @GET(PATH + "get_sections/{projectId}&suite_id={suiteId}")
    Call<List<Section>> getSections(@Path("projectId") String projectId, @Path("suiteId") String suiteId);

    @GET(PATH + "get_section/{sectionId}")
    Call<Section> getSection(@Path("sectionId") String sectionId);

    @GET(PATH + "get_case/{caseId}")
    Call<SingleCase> getCase(@Path("caseId") String caseId);

    @Multipart
    @POST(PATH + "add_attachment_to_result/{resultId}")
    Call<ResponseBody> attacheScreenShotToResult(@Path("resultId") String resultId, @Part MultipartBody.Part filePart);
}
