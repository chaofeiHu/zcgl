package com.hz.demo.model.shenbao;

import java.math.BigDecimal;
import java.util.List;

public class Project {
    private String id;

    private String userid;

    private String projectname;

    private String projectcompany;

    private String projectstarttime;

    private Integer projectrole;

    private BigDecimal projectmoney;

    private String projectacceptancetime;

    private String acceptancetype;

    private String conclusiontype;

    private String paiming;

    private String headman;


    private String state;

    private List<Projectfile> projectfile;

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

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname == null ? null : projectname.trim();
    }

    public String getProjectcompany() {
        return projectcompany;
    }

    public void setProjectcompany(String projectcompany) {
        this.projectcompany = projectcompany == null ? null : projectcompany.trim();
    }

    public String getProjectstarttime() {
        return projectstarttime;
    }

    public void setProjectstarttime(String projectstarttime) {
        this.projectstarttime = projectstarttime;
    }

    public Integer getProjectrole() {
        return projectrole;
    }

    public void setProjectrole(Integer projectrole) {
        this.projectrole = projectrole;
    }

    public BigDecimal getProjectmoney() {
        return projectmoney;
    }

    public void setProjectmoney(BigDecimal projectmoney) {
        this.projectmoney = projectmoney;
    }

    public String getProjectacceptancetime() {
        return projectacceptancetime;
    }

    public void setProjectacceptancetime(String projectacceptancetime) {
        this.projectacceptancetime = projectacceptancetime;
    }

    public String getAcceptancetype() {
        return acceptancetype;
    }

    public void setAcceptancetype(String acceptancetype) {
        this.acceptancetype = acceptancetype;
    }

    public String getConclusiontype() {
        return conclusiontype;
    }

    public void setConclusiontype(String conclusiontype) {
        this.conclusiontype = conclusiontype;
    }

    public String getPaiming() {
        return paiming;
    }

    public void setPaiming(String paiming) {
        this.paiming = paiming == null ? null : paiming.trim();
    }

    public String getHeadman() {
        return headman;
    }

    public void setHeadman(String headman) {
        this.headman = headman == null ? null : headman.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public List<Projectfile> getProjectfile() {
        return projectfile;
    }

    public void setProjectfile(List<Projectfile> projectfile) {
        this.projectfile = projectfile;
    }
}