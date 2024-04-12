package com.tunein.mobile.api.testrail.util;

import com.tunein.mobile.annotations.Issue;
import com.tunein.mobile.annotations.Issues;
import com.tunein.mobile.annotations.TestCaseId;
import com.tunein.mobile.annotations.TestCaseIds;
import org.testng.ITestNGMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class AnnotationParser {

    public String getTmsLinkNumber(ITestNGMethod method) {
        TestCaseId tmsLink = getTmsLinkAnnotation(method);
        TestCaseIds tmsLinks = getTmsLinksAnnotation(method);
        StringBuilder sb = new StringBuilder();
        if (tmsLink != null) {
            return sb.append(tmsLink.value()).toString();
        } else if (tmsLinks != null) {
            TestCaseId[] tmsLinksArray = tmsLinks.value();
            for (int i = 0; i < tmsLinksArray.length; i++) {
                sb.append(tmsLinksArray[i].value().trim());
                if (i != tmsLinksArray.length - 1) sb.append(",");
            }
            return sb.toString();
        } else
            return sb.toString();
    }

    public String getIssueNumber(ITestNGMethod method) {
        Issue issue = getIssueAnnotation(method);
        Issues issues = getIssuesAnnotation(method);
        StringBuilder sb = new StringBuilder();
        if (issue != null) {
            return sb.append(issue.value()).toString();
        } else if (issues != null) {
            Issue[] issuesArr = issues.value();
            for (int i = 0; i < issuesArr.length; i++) {
                sb.append(issuesArr[i].value());
                if (i != issuesArr.length - 1) sb.append(", ");
            }
            return sb.toString();
        } else
            return sb.toString();
    }

    private TestCaseId getTmsLinkAnnotation(ITestNGMethod method) {
        Annotation[] declaredAnnotations = method.getConstructorOrMethod().getMethod().getDeclaredAnnotations();
        return (TestCaseId) Arrays.stream(declaredAnnotations).
                filter(a -> a.annotationType().equals(TestCaseId.class)).
                findFirst().
                orElse(null);
    }

    private TestCaseIds getTmsLinksAnnotation(ITestNGMethod method) {
        Annotation[] declaredAnnotations = method.getConstructorOrMethod().getMethod().getDeclaredAnnotations();
        return (TestCaseIds) Arrays.stream(declaredAnnotations).
                filter(a -> a.annotationType().equals(TestCaseIds.class)).
                findFirst().
                orElse(null);
    }

    private Issue getIssueAnnotation(ITestNGMethod method) {
        Annotation[] declaredAnnotations = method.getConstructorOrMethod().getMethod().getDeclaredAnnotations();
        return (Issue) Arrays.stream(declaredAnnotations).
                filter(a -> a.annotationType().equals(Issue.class)).
                findFirst().
                orElse(null);
    }

    private Issues getIssuesAnnotation(ITestNGMethod method) {
        Annotation[] declaredAnnotations = method.getConstructorOrMethod().getMethod().getDeclaredAnnotations();
        return (Issues) Arrays.stream(declaredAnnotations).
                filter(a -> a.annotationType().equals(Issues.class)).
                findFirst().
                orElse(null);
    }

}
