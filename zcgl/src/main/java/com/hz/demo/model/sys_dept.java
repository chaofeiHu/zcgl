package com.hz.demo.model;

import java.util.Date;

public class sys_dept {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.dept_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String deptId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.paren_tid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.dept_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String deptCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.dept_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String deptName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String levelPath;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Integer fsort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.add_user_rid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String addUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Date addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_dept.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup3;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.dept_id
     *
     * @return the value of sys_dept.dept_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.dept_id
     *
     * @param deptId the value for sys_dept.dept_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.paren_tid
     *
     * @return the value of sys_dept.paren_tid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.paren_tid
     *
     * @param parentId the value for sys_dept.paren_tid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.dept_code
     *
     * @return the value of sys_dept.dept_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.dept_code
     *
     * @param deptCode the value for sys_dept.dept_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.dept_name
     *
     * @return the value of sys_dept.dept_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.dept_name
     *
     * @param deptName the value for sys_dept.dept_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.add_user_id
     *
     * @return the value of sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getLevelPath() {
        return levelPath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.add_user_id
     *
     * @param levelPath the value for sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setLevelPath(String levelPath) {
        this.levelPath = levelPath == null ? null : levelPath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.fsort
     *
     * @return the value of sys_dept.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Integer getFsort() {
        return fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.fsort
     *
     * @param fsort the value for sys_dept.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setFsort(Integer fsort) {
        this.fsort = fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.add_user_rid
     *
     * @return the value of sys_dept.add_user_rid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getAddUserId() {
        return addUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.add_user_rid
     *
     * @param addUserId the value for sys_dept.add_user_rid
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId == null ? null : addUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.add_time
     *
     * @return the value of sys_dept.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.add_time
     *
     * @param addTime the value for sys_dept.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.state
     *
     * @return the value of sys_dept.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.state
     *
     * @param state the value for sys_dept.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.backup1
     *
     * @return the value of sys_dept.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup1() {
        return backup1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.backup1
     *
     * @param backup1 the value for sys_dept.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.backup2
     *
     * @return the value of sys_dept.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup2() {
        return backup2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.backup2
     *
     * @param backup2 the value for sys_dept.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_dept.backup3
     *
     * @return the value of sys_dept.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup3() {
        return backup3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_dept.backup3
     *
     * @param backup3 the value for sys_dept.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }
}