package com.hz.demo.model.shenbao;

import java.util.List;

public class Education {
    private String id;

    private String userid;

    private String starttime;

    private String endtime;

    private String schoolname;

    private String job;

    private String reference;
    /**
     * 0否  1是
     */
    private Integer fulltimetype;

    private String extrainfo;

    private String state;


    private String major;
    private String education;
    private String degree;
    private String backup1;
    private String backup2;
    private String backup3;
    private String dictName;
    private String dictName1;

    private List<Educationfile> educationfile;
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public String getDictName1() {
        return dictName1;
    }

    public void setDictName1(String dictName1) {
        this.dictName1 = dictName1 == null ? null : dictName1.trim();
    }


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

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname == null ? null : schoolname.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference == null ? null : reference.trim();
    }

    public Integer getFulltimetype() {
        return fulltimetype;
    }

    public void setFulltimetype(Integer fulltimetype) {
        this.fulltimetype = fulltimetype;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public List<Educationfile> getEducationfile() {
        return educationfile;
    }

    public void setEducationfile(List<Educationfile> educationfile) {
        this.educationfile = educationfile;
    }
}