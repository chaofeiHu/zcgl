package com.hz.demo.model.shenbao;

import java.util.List;

public class Book {
    private String id;

    private String userId;

    private String bookname;

    private String publishtime;

    private String wordnumber;

    private String publishinghouse;

    private String paiming;

    private String extrainfo;

    private String bookLevel;

    private String bookNumber;

    private List<BookFile> bookFile;

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

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname == null ? null : bookname.trim();
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

    public String getBookLevel() {
        return bookLevel;
    }

    public void setBookLevel(String bookLevel) {
        this.bookLevel = bookLevel == null ? null : bookLevel.trim();
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber == null ? null : bookNumber.trim();
    }

    public List<BookFile> getBookFile() {
        return bookFile;
    }

    public void setBookFile(List<BookFile> bookFile) {
        this.bookFile = bookFile;
    }
}