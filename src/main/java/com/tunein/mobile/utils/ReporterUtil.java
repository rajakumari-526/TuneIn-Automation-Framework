package com.tunein.mobile.utils;

import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.ReportPortal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class ReporterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporterUtil.class.getName());

    private static final Logger LOGGER_FILE = LoggerFactory.getLogger("binary_data_logger");

    public static void log(String message) {
        ReportPortal.emitLog(message, LogLevel.DEBUG.name(), new Date());
        LOGGER.info(message);
    }

    public static void log(String message, String[] array) {
        log(message);
        Arrays.asList(array).forEach(arrayMessage -> {
            log(arrayMessage);
        });
    }

    public static void log(JsonObject jsonObject) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(jsonObject);
        log(jsonOutput);
    }

    public static void log(String message, JsonObject jsonObject) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonOutput = gson.toJson(jsonObject);
        log(message);
        log(jsonOutput);
    }

    public static void log(String message, int value) {
        log(String.format(message, value));
    }

    public static void log(File file, String message) {
        ReportPortal.emitLog(message, LogLevel.DEBUG.name(), new Date(), file);
    }

}
