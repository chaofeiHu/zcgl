package com.hz.demo.model;

import java.util.Date;

public class base_judging_series {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.ID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.JUDGING_CODE
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String judgingCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.REVIEW_SERIES
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String reviewSeries;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.REVIEW_PROFESSIAL
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String reviewProfessial;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.ADDTIME
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private Date addtime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.ADDUSERID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String adduserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.BACK1
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String back1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.BACK2
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String back2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_SERIES.BACK3
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    private String back3;
    private String juryProfessionCode;

    public String getJuryProfessionCode() {
        return juryProfessionCode;
    }

    public void setJuryProfessionCode(String juryProfessionCode) {
        this.juryProfessionCode = juryProfessionCode;
    }



    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.ID
     *
     * @return the value of BASE_JUDGING_SERIES.ID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.ID
     *
     * @param id the value for BASE_JUDGING_SERIES.ID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.JUDGING_CODE
     *
     * @return the value of BASE_JUDGING_SERIES.JUDGING_CODE
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getJudgingCode() {
        return judgingCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.JUDGING_CODE
     *
     * @param judgingCode the value for BASE_JUDGING_SERIES.JUDGING_CODE
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setJudgingCode(String judgingCode) {
        this.judgingCode = judgingCode == null ? null : judgingCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.REVIEW_SERIES
     *
     * @return the value of BASE_JUDGING_SERIES.REVIEW_SERIES
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getReviewSeries() {
        return reviewSeries;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.REVIEW_SERIES
     *
     * @param reviewSeries the value for BASE_JUDGING_SERIES.REVIEW_SERIES
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setReviewSeries(String reviewSeries) {
        this.reviewSeries = reviewSeries == null ? null : reviewSeries.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.REVIEW_PROFESSIAL
     *
     * @return the value of BASE_JUDGING_SERIES.REVIEW_PROFESSIAL
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getReviewProfessial() {
        return reviewProfessial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.REVIEW_PROFESSIAL
     *
     * @param reviewProfessial the value for BASE_JUDGING_SERIES.REVIEW_PROFESSIAL
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setReviewProfessial(String reviewProfessial) {
        this.reviewProfessial = reviewProfessial == null ? null : reviewProfessial.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.ADDTIME
     *
     * @return the value of BASE_JUDGING_SERIES.ADDTIME
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.ADDTIME
     *
     * @param addtime the value for BASE_JUDGING_SERIES.ADDTIME
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.ADDUSERID
     *
     * @return the value of BASE_JUDGING_SERIES.ADDUSERID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getAdduserid() {
        return adduserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.ADDUSERID
     *
     * @param adduserid the value for BASE_JUDGING_SERIES.ADDUSERID
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setAdduserid(String adduserid) {
        this.adduserid = adduserid == null ? null : adduserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.BACK1
     *
     * @return the value of BASE_JUDGING_SERIES.BACK1
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getBack1() {
        return back1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.BACK1
     *
     * @param back1 the value for BASE_JUDGING_SERIES.BACK1
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setBack1(String back1) {
        this.back1 = back1 == null ? null : back1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.BACK2
     *
     * @return the value of BASE_JUDGING_SERIES.BACK2
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getBack2() {
        return back2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.BACK2
     *
     * @param back2 the value for BASE_JUDGING_SERIES.BACK2
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setBack2(String back2) {
        this.back2 = back2 == null ? null : back2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_SERIES.BACK3
     *
     * @return the value of BASE_JUDGING_SERIES.BACK3
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public String getBack3() {
        return back3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_SERIES.BACK3
     *
     * @param back3 the value for BASE_JUDGING_SERIES.BACK3
     *
     * @mbg.generated Thu Sep 13 17:01:07 CST 2018
     */
    public void setBack3(String back3) {
        this.back3 = back3 == null ? null : back3.trim();
    }
}