package com.tunein.mobile.appium.driverprovider;

import com.epam.reportportal.annotations.Step;
import com.tunein.mobile.utils.ReporterUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tunein.mobile.conf.ConfigLoader.config;
import static com.tunein.mobile.utils.DataUtil.getRandomNumberInRange;

public class AppiumSession {
    static Map<Long, String> device = new HashMap<>();

    static Map<Long, String> streamTestCsvFile = new HashMap<>();

    static Map<Long, Integer> serverPortByThread = new HashMap<>();

    static Map<Long, Integer> mjpegServerPortByThread = new HashMap<>();

    static Map<Long, Integer> systemPortByThread = new HashMap<>();

    static Map<Long, String> deriveDataPath = new HashMap<>();

    static Map<Long, String> deviceNameByThread = new HashMap<>();

    public static synchronized String getUDID() {
        return device.get(Thread.currentThread().getId());
    }

    public static synchronized String getStreamTestCsvFile() {
        if (streamTestCsvFile.get(Thread.currentThread().getId()) == null) {
            streamTestCsvFile.put(Thread.currentThread().getId(), config().appiumStreamFileName());
        }
        return streamTestCsvFile.get(Thread.currentThread().getId());
    }

    public static synchronized Integer getSystemPort() {
        if (systemPortByThread.get(Thread.currentThread().getId()) == null) {
            if (config().testOnRealDevices()) {
                systemPortByThread.put(Thread.currentThread().getId(), getRandomNumberInRange(8400, 8499));
            } else {
                systemPortByThread.put(Thread.currentThread().getId(), getRandomNumberInRange(6200, 8399));
            }
        }
        return systemPortByThread.get(Thread.currentThread().getId());
    }

    public static synchronized int setNewSystemPort() {
        int newPort = 0;
        List<Integer> ports = systemPortByThread.values().stream().collect(Collectors.toList());
        while (newPort == 0 || ports.contains(newPort)) {
            if (config().testOnRealDevices()) {
                newPort = getRandomNumberInRange(8400, 8499);
            } else {
                newPort = getRandomNumberInRange(6200, 8399);
            }
        }
        systemPortByThread.put(Thread.currentThread().getId(), newPort);
        return newPort;
    }

    public static synchronized Integer getMjpegServerPort() {
        if (mjpegServerPortByThread.get(Thread.currentThread().getId()) == null) {
            mjpegServerPortByThread.put(Thread.currentThread().getId(), getRandomNumberInRange(5810, 6199));
        }
        return mjpegServerPortByThread.get(Thread.currentThread().getId());
    }

    public static synchronized void putAppiumPort(Integer port) {
        if (port == null) {
            port = config().appiumPort();
        }
        serverPortByThread.put(Thread.currentThread().getId(), port);
    }

    public static synchronized Integer getAppiumPort() {
        return serverPortByThread.get(Thread.currentThread().getId());
    }

    public static synchronized ArrayList<Integer> getAllAppiumPorts() {
        return new ArrayList<>(serverPortByThread.values());
    }

    public static synchronized String getDeviceName() {
        return deviceNameByThread.get(Thread.currentThread().getId());
    }

    public static synchronized String getDerivedDataPath() {
        if (deriveDataPath.get(Thread.currentThread().getId()) == null) {
            String path = Paths.get("").toAbsolutePath().normalize().toString() + "/deriveData/" + getUDID();
            deriveDataPath.put(Thread.currentThread().getId(), path);
        }
        return deriveDataPath.get(Thread.currentThread().getId());
    }

    public static synchronized String getThreadName() {
        return Thread.currentThread().getName();
    }

    public static synchronized long getThreadId() {
        return Thread.currentThread().getId();
    }

    public static synchronized void setDeviceName(Long threadId) {
        String[] deviceNames = config().deviceName().split(",");
        for (String deviceName: deviceNames) {
            if (!deviceNameByThread.containsValue(deviceName) && !deviceNameByThread.containsKey(threadId)) {
                deviceNameByThread.put(threadId, deviceName);
            }
        }
    }

    @Step("Setting device UDID in thread {threadId}")
    public static synchronized void setUDID(Long threadId) {
        String[] udids = config().udid().split(",");
        for (String udid: udids) {
            if (!device.containsValue(udid) && !device.containsKey(threadId)) {
                device.put(threadId, udid);
                ReporterUtil.log("Set device udid " + udid + " for current thread " + threadId);
            }
        }
    }

    public static synchronized void setStreamTestCsvFile(Long threadId) {
        String[] csvFiles = config().appiumStreamFileName().split(",");
        if (csvFiles.length == 1) {
            streamTestCsvFile.put(threadId, csvFiles[0]);
        } else {
            for (String csvFile : csvFiles) {
                if (!streamTestCsvFile.containsValue(csvFile) && !streamTestCsvFile.containsKey(threadId)) {
                    streamTestCsvFile.put(threadId, csvFile);
                }
            }
        }
    }
}
