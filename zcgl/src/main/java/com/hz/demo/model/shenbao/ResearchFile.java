package com.hz.demo.model.shenbao;

public class ResearchFile {
    private String id;

    private String researchid;

    private String fileurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getResearchid() {
        return researchid;
    }

    public void setResearchid(String researchid) {
        this.researchid = researchid == null ? null : researchid.trim();
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl == null ? null : fileurl.trim();
    }
}