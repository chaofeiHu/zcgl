package com.hz.demo.model;

import java.math.BigDecimal;

public class speciality_tickets {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    private BigDecimal id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.GROUP_ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    private String groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.SPECIALITY_ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    private String specialityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.TICKETS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    private String tickets;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.REMARKS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    private String remarks;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.PROPOSER_ID
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    private String proposerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.TYPE
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BASE_SPECIALITY_TICKETS.TEXT
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    private String text;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.ID
     *
     * @return the value of BASE_SPECIALITY_TICKETS.ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.ID
     *
     * @param id the value for BASE_SPECIALITY_TICKETS.ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.GROUP_ID
     *
     * @return the value of BASE_SPECIALITY_TICKETS.GROUP_ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.GROUP_ID
     *
     * @param groupId the value for BASE_SPECIALITY_TICKETS.GROUP_ID
     *
     * @mbg.generated Thu Oct 25 14:50:14 CST 2018
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.SPECIALITY_ID
     *
     * @return the value of BASE_SPECIALITY_TICKETS.SPECIALITY_ID
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getSpecialityId() {
        return specialityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.SPECIALITY_ID
     *
     * @param specialityId the value for BASE_SPECIALITY_TICKETS.SPECIALITY_ID
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setSpecialityId(String specialityId) {
        this.specialityId = specialityId == null ? null : specialityId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.TICKETS
     *
     * @return the value of BASE_SPECIALITY_TICKETS.TICKETS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getTickets() {
        return tickets;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.TICKETS
     *
     * @param tickets the value for BASE_SPECIALITY_TICKETS.TICKETS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setTickets(String tickets) {
        this.tickets = tickets == null ? null : tickets.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.REMARKS
     *
     * @return the value of BASE_SPECIALITY_TICKETS.REMARKS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.REMARKS
     *
     * @param remarks the value for BASE_SPECIALITY_TICKETS.REMARKS
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.PROPOSER_ID
     *
     * @return the value of BASE_SPECIALITY_TICKETS.PROPOSER_ID
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getProposerId() {
        return proposerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.PROPOSER_ID
     *
     * @param proposerId the value for BASE_SPECIALITY_TICKETS.PROPOSER_ID
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setProposerId(String proposerId) {
        this.proposerId = proposerId == null ? null : proposerId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.TYPE
     *
     * @return the value of BASE_SPECIALITY_TICKETS.TYPE
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.TYPE
     *
     * @param type the value for BASE_SPECIALITY_TICKETS.TYPE
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BASE_SPECIALITY_TICKETS.TEXT
     *
     * @return the value of BASE_SPECIALITY_TICKETS.TEXT
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BASE_SPECIALITY_TICKETS.TEXT
     *
     * @param text the value for BASE_SPECIALITY_TICKETS.TEXT
     *
     * @mbg.generated Thu Oct 25 14:50:15 CST 2018
     */
    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }
}