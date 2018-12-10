package com.hz.demo.model;

import java.util.Date;

public class rec_certificate {
    private Short id;

    private Short resultid;

    private String certificateNumber;

    private String certificatePath;

    private Date addtime;

    private String back1;

    private String back2;

    private String back3;

    private rec_result recResult;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getResultid() {
        return resultid;
    }

    public void setResultid(Short resultid) {
        this.resultid = resultid;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber == null ? null : certificateNumber.trim();
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath == null ? null : certificatePath.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getBack1() {
        return back1;
    }

    public void setBack1(String back1) {
        this.back1 = back1 == null ? null : back1.trim();
    }

    public String getBack2() {
        return back2;
    }

    public void setBack2(String back2) {
        this.back2 = back2 == null ? null : back2.trim();
    }

    public String getBack3() {
        return back3;
    }

    public void setBack3(String back3) {
        this.back3 = back3 == null ? null : back3.trim();
    }

    public rec_result getRecResult() {
        return recResult;
    }

    public void setRecResult(rec_result recResult) {
        this.recResult = recResult;
    }
}