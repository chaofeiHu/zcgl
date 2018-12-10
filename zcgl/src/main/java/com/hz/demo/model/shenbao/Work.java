package com.hz.demo.model.shenbao;

import java.util.List;

public class Work {
    private String id;

    private String userid;

    private String starttime;

    private String endtime;

    private String workcompanyname;

    private String job;

    private String positionaltitles;

    private String reference;

    private String extrainfo;

    private String state;
    private String backup1;
    private String backup2;
    private String backup3;

    private List<Workfile> workfile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getWorkcompanyname() {
        return workcompanyname;
    }

    public void setWorkcompanyname(String workcompanyname) {
        this.workcompanyname = workcompanyname == null ? null : workcompanyname.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getPositionaltitles() {
        return positionaltitles;
    }

    public void setPositionaltitles(String positionaltitles) {
        this.positionaltitles = positionaltitles == null ? null : positionaltitles.trim();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference == null ? null : reference.trim();
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
        this.state = state;
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1;
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2;
    }

    public String getBackup3() {
        return backup3;
    }

    public void setBackup3(String backup3) {
        this.backup3 = backup3;
    }

    public List<Workfile> getWorkfile() {
        return workfile;
    }

    public void setWorkfile(List<Workfile> workfile) {
        this.workfile = workfile;
    }
}