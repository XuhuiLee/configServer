package com.createarttechnology.config.bean;

/**
 * Created by lixuhui on 2018/11/12.
 */
public class EditConfigReq {
    private String profiles;
    private String name;
    private String content;

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EditConfigReq{" +
                "profiles='" + profiles + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
