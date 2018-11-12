package com.createarttechnology.config.util;

/**
 * Created by lixuhui on 2018/11/12.
 */
public abstract class InnerUtil {

    private static final String SPLITTER = "/";
    private static final String ROOT = "/config";

    public static String convert(byte[] data) {
        return data != null ? new String(data) : null;
    }

    public static byte[] convert(String data) {
        return data != null ? data.getBytes() : null;
    }

    public static String getRoot() {
        return ROOT;
    }

    public static String getConfigRoot(String profile) {
        return ROOT + SPLITTER + profile;
    }

    public static String getConfigNamePath(String profile, String configName) {
        return ROOT + SPLITTER + profile + SPLITTER + configName;
    }

    public static String getConfigFilePath(String profile, String configName) {
        return ROOT + SPLITTER + profile + SPLITTER + configName + SPLITTER + configName;
    }

    public static String getConfigTagFilePath(String profile, String configName, String configTagname) {
        return ROOT + SPLITTER + profile + SPLITTER + configName + SPLITTER + configTagname;
    }

    public static boolean isValidProfile(String profile) {
        return "dev".equals(profile) || "online".equals(profile);
    }

}
