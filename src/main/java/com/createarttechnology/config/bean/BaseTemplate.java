package com.createarttechnology.config.bean;

/**
 * Created by lixuhui on 2018/11/12.
 */
public class BaseTemplate {
    private String title;
    private boolean admin = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
