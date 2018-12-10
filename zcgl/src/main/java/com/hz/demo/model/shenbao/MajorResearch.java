package com.hz.demo.model.shenbao;

public class MajorResearch {
    private String id;

    private String userId;

    private String major;

    private String majorResearch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major == null ? null : major.trim();
    }

    public String getMajorResearch() {
        return majorResearch;
    }

    public void setMajorResearch(String majorResearch) {
        this.majorResearch = majorResearch == null ? null : majorResearch.trim();
    }
}