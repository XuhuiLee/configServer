package com.createarttechnology.config.bean;

/**
 * Created by lixuhui on 2018/11/12.
 */
public class AppConfig {
    private String configName;
    private String configContent;
    private String profile;
    private int version;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "configName='" + configName + '\'' +
                ", configContent='" + configContent + '\'' +
                ", profile='" + profile + '\'' +
                ", version=" + version +
                '}';
    }
}
