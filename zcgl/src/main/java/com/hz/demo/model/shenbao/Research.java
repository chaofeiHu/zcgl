package com.hz.demo.model.shenbao;

import java.util.List;

public class Research {
    private String id;

    private String userId;

    private String getDate;

    private String researchName;

    private String researchInfo;

    private String researchLevel;

    private String researchLevelName;

    private String backup1;

    private String backup2;

    private String backup3;
    private String paiming;

    private List<ResearchFile> researchFile;

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

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getResearchName() {
        return researchName;
    }

    public void setResearchName(String researchName) {
        this.researchName = researchName == null ? null : researchName.trim();
    }

    public String getResearchInfo() {
        return researchInfo;
    }

    public void setResearchInfo(String researchInfo) {
        this.researchInfo = researchInfo == null ? null : researchInfo.trim();
    }

    public String getResearchLevel() {
        return researchLevel;
    }

    public void setResearchLevel(String researchLevel) {
        this.researchLevel = researchLevel == null ? null : researchLevel.trim();
    }

    public String getResearchLevelName() {
        return researchLevelName;
    }

    public void setResearchLevelName(String researchLevelName) {
        this.researchLevelName = researchLevelName;
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    public String getBackup3() {
        return backup3;
    }

    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }

    public String getPaiming() {
        return paiming;
    }

    public void setPaiming(String paiming) {
        this.paiming = paiming;
    }

    public List<ResearchFile> getResearchFile() {
        return researchFile;
    }

    public void setResearchFile(List<ResearchFile> researchFile) {
        this.researchFile = researchFile;
    }
}