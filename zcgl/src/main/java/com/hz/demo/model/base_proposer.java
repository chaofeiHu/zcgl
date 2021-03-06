package com.hz.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class base_proposer {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private BigDecimal id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.DECLARE_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String declareUnitcode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.BASIC_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String basicUnitcode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.DISPLAY_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String displayName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.LOGIN_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String loginName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.LOGIN_PASSWORD
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String loginPassword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.MOBILEPHONE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String mobilephone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.EMAIL
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.ID_CARD_NO
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String idCardNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.ADD_USER_ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String addUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.ADD_TIME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private Date addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.STATE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.BACKUP1
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String backup1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.BACKUP2
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String backup2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_PROPOSER.BACKUP3
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    private String backup3;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.ID
     *
     * @return the value of BASE_PROPOSER.ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.ID
     *
     * @param id the value for BASE_PROPOSER.ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.DECLARE_UNITCODE
     *
     * @return the value of BASE_PROPOSER.DECLARE_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getDeclareUnitcode() {
        return declareUnitcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.DECLARE_UNITCODE
     *
     * @param declareUnitcode the value for BASE_PROPOSER.DECLARE_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setDeclareUnitcode(String declareUnitcode) {
        this.declareUnitcode = declareUnitcode == null ? null : declareUnitcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.BASIC_UNITCODE
     *
     * @return the value of BASE_PROPOSER.BASIC_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getBasicUnitcode() {
        return basicUnitcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.BASIC_UNITCODE
     *
     * @param basicUnitcode the value for BASE_PROPOSER.BASIC_UNITCODE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setBasicUnitcode(String basicUnitcode) {
        this.basicUnitcode = basicUnitcode == null ? null : basicUnitcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.DISPLAY_NAME
     *
     * @return the value of BASE_PROPOSER.DISPLAY_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.DISPLAY_NAME
     *
     * @param displayName the value for BASE_PROPOSER.DISPLAY_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.LOGIN_NAME
     *
     * @return the value of BASE_PROPOSER.LOGIN_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.LOGIN_NAME
     *
     * @param loginName the value for BASE_PROPOSER.LOGIN_NAME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.LOGIN_PASSWORD
     *
     * @return the value of BASE_PROPOSER.LOGIN_PASSWORD
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.LOGIN_PASSWORD
     *
     * @param loginPassword the value for BASE_PROPOSER.LOGIN_PASSWORD
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.MOBILEPHONE
     *
     * @return the value of BASE_PROPOSER.MOBILEPHONE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.MOBILEPHONE
     *
     * @param mobilephone the value for BASE_PROPOSER.MOBILEPHONE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.EMAIL
     *
     * @return the value of BASE_PROPOSER.EMAIL
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.EMAIL
     *
     * @param email the value for BASE_PROPOSER.EMAIL
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.ID_CARD_NO
     *
     * @return the value of BASE_PROPOSER.ID_CARD_NO
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.ID_CARD_NO
     *
     * @param idCardNo the value for BASE_PROPOSER.ID_CARD_NO
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.ADD_USER_ID
     *
     * @return the value of BASE_PROPOSER.ADD_USER_ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getAddUserId() {
        return addUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.ADD_USER_ID
     *
     * @param addUserId the value for BASE_PROPOSER.ADD_USER_ID
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId == null ? null : addUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.ADD_TIME
     *
     * @return the value of BASE_PROPOSER.ADD_TIME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.ADD_TIME
     *
     * @param addTime the value for BASE_PROPOSER.ADD_TIME
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.STATE
     *
     * @return the value of BASE_PROPOSER.STATE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.STATE
     *
     * @param state the value for BASE_PROPOSER.STATE
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.BACKUP1
     *
     * @return the value of BASE_PROPOSER.BACKUP1
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getBackup1() {
        return backup1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.BACKUP1
     *
     * @param backup1 the value for BASE_PROPOSER.BACKUP1
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.BACKUP2
     *
     * @return the value of BASE_PROPOSER.BACKUP2
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getBackup2() {
        return backup2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.BACKUP2
     *
     * @param backup2 the value for BASE_PROPOSER.BACKUP2
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_PROPOSER.BACKUP3
     *
     * @return the value of BASE_PROPOSER.BACKUP3
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public String getBackup3() {
        return backup3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_PROPOSER.BACKUP3
     *
     * @param backup3 the value for BASE_PROPOSER.BACKUP3
     *
     * @mbg.generated Tue Sep 11 10:30:02 CST 2018
     */
    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }
}