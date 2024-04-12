package com.tunein.mobile.utils;

import com.epam.reportportal.annotations.Step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.WaitersUtil.customWait;

public class CommandLineProgramUtil {

    private static final String FAIL = "fail";

    private static final String OFFLINE = "offline";

    private static final String ERROR = "error";

    /**
     * Getting output from executing a command line program
     * @param command - command line program
     */
    public static String getOutputOfExecution(String command) {
        StringBuffer output = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString().replaceAll("\n", " ");
    }

    public static String getOutputOfExecution(String[] command) {
        StringBuffer output = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString().replaceAll("\n", " ");
    }

    public static void runCommandLine(String command, boolean... throwError) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
            if (throwError.length > 0 && !throwError[0]) {
                ReporterUtil.log("Can not execute " + command + " command in command line!");
                return;
            }
            throw new Error("Can not execute " + command + " command in command line!");
        }
    }

    public static void stopServiceOnPort(Integer port) {
        try {
            Runtime.getRuntime().exec("kill -9 $(lsof -t -i:" + port + ")");
            ReporterUtil.log("Stop server on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServiceWithKeyword(String keyword) {
        String[] nodeArray = getOutputOfExecution("pgrep -f \"" + keyword + "\"").split(" ");
        for (String node : nodeArray) {
            if (!node.isEmpty()) {
                ReporterUtil.log("kill -9 " + node);
                runCommandLine("kill -9 " + node);
                ReporterUtil.log("Services with keyword: " + keyword + " and pid: " + node + " was killed");
            }
        }
    }

    @Step("Execute command {command}")
    public static String executeCommandUntilSuccessful(String command, Duration... duration) {
        long timeout = duration.length > 0 ? duration[0].toMillis() : config().waitCustomTimeoutMilliseconds();
        long start = System.currentTimeMillis();
        String result = getOutputOfExecution(command).toLowerCase();

        while (result.contains(FAIL) || result.contains(OFFLINE) || result.contains(ERROR) || result.equals("")) {
            if (System.currentTimeMillis() - start > timeout) {
                ReporterUtil.log("Condition is not met within " + timeout + " ms ");
                return result;
            }
            customWait(Duration.ofMillis(1000));
            result = getOutputOfExecution(command).toLowerCase();
        }
        return result;
    }

    @Step("Execute command {command}")
    public static String executeCommandUntilSuccessful(String[] command) {
        long start = System.currentTimeMillis();
        long timeout = config().waitExtraLongTimeoutMilliseconds();
        String result = getOutputOfExecution(command).toLowerCase();
        ReporterUtil.log(result);

        while (result.contains(FAIL) || result.contains(OFFLINE) || result.contains(ERROR) || result.equals("")) {
            if (System.currentTimeMillis() - start > timeout) {
                ReporterUtil.log("Condition not met within " + timeout + " ms ");
                return result;
            }
            customWait(Duration.ofMillis(1000));
            result = getOutputOfExecution(command);
        }
        return result;
    }

}
