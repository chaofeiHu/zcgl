package com.hz.demo.model.shenbao;

import java.util.List;

public class Paper {
    private String id;

    private String userId;

    private String papername;

    private String publishtime;

    private String wordnumber;

    private String publishinghouse;

    private Long appraisal;

    private String paiming;

    private String extrainfo;

    private String state;

    private String publicationName;

    private String isbn;

    private String paperLevel;

    private  List<Paperfile> paperfile;

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

    public String getPapername() {
        return papername;
    }

    public void setPapername(String papername) {
        this.papername = papername == null ? null : papername.trim();
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getWordnumber() {
        return wordnumber;
    }

    public void setWordnumber(String wordnumber) {
        this.wordnumber = wordnumber == null ? null : wordnumber.trim();
    }

    public String getPublishinghouse() {
        return publishinghouse;
    }

    public void setPublishinghouse(String publishinghouse) {
        this.publishinghouse = publishinghouse == null ? null : publishinghouse.trim();
    }

    public Long getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Long appraisal) {
        this.appraisal = appraisal;
    }

    public String getPaiming() {
        return paiming;
    }

    public void setPaiming(String paiming) {
        this.paiming = paiming == null ? null : paiming.trim();
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo == null ? null : extrainfo.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName == null ? null : publicationName.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? null : isbn.trim();
    }

    public String getPaperLevel() {
        return paperLevel;
    }

    public void setPaperLevel(String paperLevel) {
        this.paperLevel = paperLevel == null ? null : paperLevel.trim();
    }

    public List<Paperfile> getPaperfile() {
        return paperfile;
    }

    public void setPaperfile(List<Paperfile> paperfile) {
        this.paperfile = paperfile;
    }
}