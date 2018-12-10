package com.hz.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class base_judging_process {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.ID
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private BigDecimal id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.JUDGING_CODE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private String judgingCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.PROCESS_GROUP
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private Long processGroup;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.PROCESS_TYPE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private Long processType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.PROCESS_STATE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private Long processState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_JUDGING_PROCESS.CREATE_TIME
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    private Date createTime;
    private String processPattern;

    private String yearNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_PROCESS.ID
     *
     * @return the value of BASE_JUDGING_PROCESS.ID
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_PROCESS.ID
     *
     * @param id the value for BASE_JUDGING_PROCESS.ID
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_PROCESS.JUDGING_CODE
     *
     * @return the value of BASE_JUDGING_PROCESS.JUDGING_CODE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public String getJudgingCode() {
        return judgingCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_PROCESS.JUDGING_CODE
     *
     * @param judgingCode the value for BASE_JUDGING_PROCESS.JUDGING_CODE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public void setJudgingCode(String judgingCode) {
        this.judgingCode = judgingCode == null ? null : judgingCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_PROCESS.PROCESS_GROUP
     *
     * @return the value of BASE_JUDGING_PROCESS.PROCESS_GROUP
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public Long getProcessGroup() {
        return processGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_PROCESS.PROCESS_GROUP
     *
     * @param processGroup the value for BASE_JUDGING_PROCESS.PROCESS_GROUP
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public void setProcessGroup(Long processGroup) {
        this.processGroup = processGroup;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_PROCESS.PROCESS_TYPE
     *
     * @return the value of BASE_JUDGING_PROCESS.PROCESS_TYPE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public Long getProcessType() {
        return processType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_PROCESS.PROCESS_TYPE
     *
     * @param processType the value for BASE_JUDGING_PROCESS.PROCESS_TYPE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public void setProcessType(Long processType) {
        this.processType = processType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_JUDGING_PROCESS.PROCESS_STATE
     *
     * @return the value of BASE_JUDGING_PROCESS.PROCESS_STATE
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public Long getProcessState() {
        return processState;
    }


    public void setProcessState(Long processState) {
        this.processState = processState;
    }

    public String getYearNo() {
        return yearNo;
    }

    public void setYearNo(String yearNo) {
        this.yearNo = yearNo;
    }
    public void setProcessPattern(String processPattern) {
        this.processPattern = processPattern;
    }

    public String getProcessPattern() {
        return processPattern;
    }


    public void setProcessState(String processPattern) {
        this.processPattern = processPattern;
    }

    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_JUDGING_PROCESS.CREATE_TIME
     *
     * @param createTime the value for BASE_JUDGING_PROCESS.CREATE_TIME
     *
     * @mbg.generated Wed Oct 10 14:57:23 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}