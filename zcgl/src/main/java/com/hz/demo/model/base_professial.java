package com.hz.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class base_professial {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.ID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.PROFESSIAL_CODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String professialCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.PROFESSIAL_PARENTCODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String professialParentcode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.PROFESSIAL_NAME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String professialName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.PROFESSIAL_GRADE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String professialGrade;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.FSORT
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private BigDecimal fsort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.ADDTIME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private Date addtime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.ADDUSERID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String adduserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.STATE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private BigDecimal state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.BACK1
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String back1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.BACK2
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String back2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROFESSIAL.BACK3
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    private String back3;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.ID
     *
     * @return the value of BASE_PROFESSIAL.ID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.ID
     *
     * @param id the value for BASE_PROFESSIAL.ID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.PROFESSIAL_CODE
     *
     * @return the value of BASE_PROFESSIAL.PROFESSIAL_CODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getProfessialCode() {
        return professialCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.PROFESSIAL_CODE
     *
     * @param professialCode the value for BASE_PROFESSIAL.PROFESSIAL_CODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setProfessialCode(String professialCode) {
        this.professialCode = professialCode == null ? null : professialCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.PROFESSIAL_PARENTCODE
     *
     * @return the value of BASE_PROFESSIAL.PROFESSIAL_PARENTCODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getProfessialParentcode() {
        return professialParentcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.PROFESSIAL_PARENTCODE
     *
     * @param professialParentcode the value for BASE_PROFESSIAL.PROFESSIAL_PARENTCODE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setProfessialParentcode(String professialParentcode) {
        this.professialParentcode = professialParentcode == null ? null : professialParentcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.PROFESSIAL_NAME
     *
     * @return the value of BASE_PROFESSIAL.PROFESSIAL_NAME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getProfessialName() {
        return professialName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.PROFESSIAL_NAME
     *
     * @param professialName the value for BASE_PROFESSIAL.PROFESSIAL_NAME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setProfessialName(String professialName) {
        this.professialName = professialName == null ? null : professialName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.PROFESSIAL_GRADE
     *
     * @return the value of BASE_PROFESSIAL.PROFESSIAL_GRADE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getProfessialGrade() {
        return professialGrade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.PROFESSIAL_GRADE
     *
     * @param professialGrade the value for BASE_PROFESSIAL.PROFESSIAL_GRADE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setProfessialGrade(String professialGrade) {
        this.professialGrade = professialGrade == null ? null : professialGrade.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.FSORT
     *
     * @return the value of BASE_PROFESSIAL.FSORT
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public BigDecimal getFsort() {
        return fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.FSORT
     *
     * @param fsort the value for BASE_PROFESSIAL.FSORT
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setFsort(BigDecimal fsort) {
        this.fsort = fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.ADDTIME
     *
     * @return the value of BASE_PROFESSIAL.ADDTIME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.ADDTIME
     *
     * @param addtime the value for BASE_PROFESSIAL.ADDTIME
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.ADDUSERID
     *
     * @return the value of BASE_PROFESSIAL.ADDUSERID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getAdduserid() {
        return adduserid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.ADDUSERID
     *
     * @param adduserid the value for BASE_PROFESSIAL.ADDUSERID
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setAdduserid(String adduserid) {
        this.adduserid = adduserid == null ? null : adduserid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.STATE
     *
     * @return the value of BASE_PROFESSIAL.STATE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public BigDecimal getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.STATE
     *
     * @param state the value for BASE_PROFESSIAL.STATE
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setState(BigDecimal state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.BACK1
     *
     * @return the value of BASE_PROFESSIAL.BACK1
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getBack1() {
        return back1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.BACK1
     *
     * @param back1 the value for BASE_PROFESSIAL.BACK1
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setBack1(String back1) {
        this.back1 = back1 == null ? null : back1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.BACK2
     *
     * @return the value of BASE_PROFESSIAL.BACK2
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getBack2() {
        return back2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.BACK2
     *
     * @param back2 the value for BASE_PROFESSIAL.BACK2
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setBack2(String back2) {
        this.back2 = back2 == null ? null : back2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROFESSIAL.BACK3
     *
     * @return the value of BASE_PROFESSIAL.BACK3
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public String getBack3() {
        return back3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROFESSIAL.BACK3
     *
     * @param back3 the value for BASE_PROFESSIAL.BACK3
     *
     * @mbg.generated Wed Sep 12 11:13:25 CST 2018
     */
    public void setBack3(String back3) {
        this.back3 = back3 == null ? null : back3.trim();
    }
}