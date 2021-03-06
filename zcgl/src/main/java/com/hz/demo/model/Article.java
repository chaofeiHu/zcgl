package com.hz.demo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Article {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private BigDecimal id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.title
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.text
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private String text;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.create_time
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.user_id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private BigDecimal userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TB_ARTICLE.type
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    private BigDecimal type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.id
     *
     * @return the value of TB_ARTICLE.id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.id
     *
     * @param id the value for TB_ARTICLE.id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.title
     *
     * @return the value of TB_ARTICLE.title
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.title
     *
     * @param title the value for TB_ARTICLE.title
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.text
     *
     * @return the value of TB_ARTICLE.text
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public String getText() {
        return text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.text
     *
     * @param text the value for TB_ARTICLE.text
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.create_time
     *
     * @return the value of TB_ARTICLE.create_time
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.create_time
     *
     * @param createTime the value for TB_ARTICLE.create_time
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.user_id
     *
     * @return the value of TB_ARTICLE.user_id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public BigDecimal getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.user_id
     *
     * @param userId the value for TB_ARTICLE.user_id
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TB_ARTICLE.type
     *
     * @return the value of TB_ARTICLE.type
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public BigDecimal getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TB_ARTICLE.type
     *
     * @param type the value for TB_ARTICLE.type
     *
     * @mbg.generated Sun Sep 30 09:36:19 CST 2018
     */
    public void setType(BigDecimal type) {
        this.type = type;
    }
}