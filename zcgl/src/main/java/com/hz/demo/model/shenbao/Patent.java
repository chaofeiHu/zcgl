package com.hz.demo.model.shenbao;


import java.util.List;

public class Patent {
    private String id;

    private String userid;

    private String patentname;

    private Integer patenttype;

    private String paiming;

    private Integer ascriptiontype;

    private String patentcompletetime;

    private Integer patentnumber;


    private String state;

    private List<Patentfile> patentfile;

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

    public String getPatentname() {
        return patentname;
    }

    public void setPatentname(String patentname) {
        this.patentname = patentname == null ? null : patentname.trim();
    }

    public Integer getPatenttype() {
        return patenttype;
    }

    public void setPatenttype(Integer patenttype) {
        this.patenttype = patenttype;
    }

    public String getPaiming() {
        return paiming;
    }

    public void setPaiming(String paiming) {
        this.paiming = paiming == null ? null : paiming.trim();
    }

    public Integer getAscriptiontype() {
        return ascriptiontype;
    }

    public void setAscriptiontype(Integer ascriptiontype) {
        this.ascriptiontype = ascriptiontype;
    }

    public String getPatentcompletetime() {
        return patentcompletetime;
    }

    public void setPatentcompletetime(String patentcompletetime) {
        this.patentcompletetime = patentcompletetime;
    }

    public Integer getPatentnumber() {
        return patentnumber;
    }

    public void setPatentnumber(Integer patentnumber) {
        this.patentnumber = patentnumber;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public List<Patentfile> getPatentfile() {
        return patentfile;
    }

    public void setPatentfile(List<Patentfile> patentfile) {
        this.patentfile = patentfile;
    }
}