package com.tunein.mobile.utils;

import com.google.gson.JsonObject;

import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

import static com.google.gson.JsonParser.parseString;

public class DataUtil {

    public static int getRandomNumberInRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(String.format("Min %d cannot be bigger than max %d", min, max));
        } else {
            Random rnd = new Random();
            return min + rnd.nextInt(max - min + 1);
        }
    }

    public static int[] genIntArrayWithValue(int startWithNumber, int size) {
        startWithNumber = startWithNumber < 0 ? 0 : startWithNumber;
        return IntStream.rangeClosed(startWithNumber, startWithNumber + size).toArray();
    }

    public static synchronized long genShortUniqueNumber() {
        return (getRandomNumberInRange(1, 9) * 10_000_000) + (System.currentTimeMillis() % 10_000_000);
    }

    public static String getRandomString(int length) {
        int decimalA = 97;
        int decimalB = 122;
        char[] symbols = new char[length];

        for (int i = 0; i < symbols.length; ++i) {
            symbols[i] = (char) getRandomNumberInRange(decimalA, decimalB);
        }
        return new String(symbols);
    }

    public static String generateStringOfPasswordDots(int numberOfDots) {
        return "•".repeat(numberOfDots);
    }

    public static int getBirthYear(int age) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear - age;
    }

    public static int getDurationInSeconds(String duration) {
        int hours = 0;
        String[] arr = duration
                .replace("hr ", ":")
                .replace("hr", ":00:00")
                .replace("min", ":00")
                .replace("-", "")
                .strip()
                .split(":");

        /* Hours/Minutes/Seconds */
        if (arr.length == 3) {
            hours = Integer.parseInt(arr[0]);
        }
        int minutes = Integer.parseInt(arr[arr.length - 2]);
        int seconds = Integer.parseInt(arr[arr.length - 1]);
        return hours * 3600 + minutes * 60 + seconds;
    }

    public static JsonObject parseJson(String text) {
        return (JsonObject) parseString(text);
    }

    public static String decodeUrl(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

    public static String getIpAddressFromDomain(String domain) {
        try {
            InetAddress ipAddress = InetAddress.getByName(domain);
            return ipAddress.getHostAddress();
        } catch (UnknownHostException e) {
            ReporterUtil.log("Can NOT get IP address from '" + domain + "' domain");
        }
        return "unknown";
    }

    public static String getAllIpAddressFromDomain(String domain) {
        String ipAdresses = "";
        try {
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(domain));
            for (InetAddress ip : inetAddresses) {
                if (!ip.getHostAddress().contains(":")) {
                    ipAdresses = ipAdresses + ip.getHostAddress() + "|";
                }
            }
            return ipAdresses.substring(0, ipAdresses.length() - 1);
        } catch (UnknownHostException e) {
            ReporterUtil.log("Can NOT get IP address from '" + domain + "' domain");
        }
        return "unknown";
    }

    public static String convertSpecialCharactersToHexInString(String stringValue) {
        return stringValue.replaceAll(":", "%3A").replaceAll("\\|", "%7C");
    }

    public static String getSongNameFromString(String artistSongText) {
        return (artistSongText.split(" - ")[1]).trim();
    }

    public static String getArtistNameFromString(String artistSongText) {
        return artistSongText.split(" - ")[0];
    }

    public static boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public static boolean isLong(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }

}
