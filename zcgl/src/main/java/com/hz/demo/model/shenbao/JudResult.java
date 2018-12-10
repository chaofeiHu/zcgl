package com.hz.demo.model.shenbao;

public class JudResult {
    private String id;

    private String proId;

    private String judResult;

    private String judView;

    private String judStage;

    private String judContent;

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

    public String getJudResult() {
        return judResult;
    }

    public void setJudResult(String judResult) {
        this.judResult = judResult == null ? null : judResult.trim();
    }

    public String getJudView() {
        return judView;
    }

    public void setJudView(String judView) {
        this.judView = judView == null ? null : judView.trim();
    }

    public String getJudStage() {
        return judStage;
    }

    public void setJudStage(String judStage) {
        this.judStage = judStage == null ? null : judStage.trim();
    }

    public String getJudContent() {
        return judContent;
    }

    public void setJudContent(String judContent) {
        this.judContent = judContent == null ? null : judContent.trim();
    }
}