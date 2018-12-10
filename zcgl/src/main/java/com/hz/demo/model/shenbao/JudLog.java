package com.hz.demo.model.shenbao;


public class JudLog {
    private String id;

    private String proId;

    private String proResult;

    private String proView;

    private String judId;

    private String judDate;

    private String judStage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId == null ? null : proId.trim();
    }

    public String getProResult() {
        return proResult;
    }

    public void setProResult(String proResult) {
        this.proResult = proResult == null ? null : proResult.trim();
    }

    public String getProView() {
        return proView;
    }

    public void setProView(String proView) {
        this.proView = proView == null ? null : proView.trim();
    }

    public String getJudId() {
        return judId;
    }

    public void setJudId(String judId) {
        this.judId = judId == null ? null : judId.trim();
    }

    public String getJudDate() {
        return judDate;
    }

    public void setJudDate(String judDate) {
        this.judDate = judDate;
    }

    public String getJudStage() {
        return judStage;
    }

    public void setJudStage(String judStage) {
        this.judStage = judStage == null ? null : judStage.trim();
    }
}