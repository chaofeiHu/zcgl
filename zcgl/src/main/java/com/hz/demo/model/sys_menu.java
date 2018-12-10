package com.hz.demo.model;

import java.util.Date;

public class sys_menu {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String menuId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String menuCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String menuName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_url
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String menuUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.parent_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Integer fsort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.has_child
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Integer hasChild;

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
     * This field corresponds to the database column sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String addUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Date addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    private String backup3;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_id
     *
     * @return the value of sys_menu.menu_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_id
     *
     * @param menuId the value for sys_menu.menu_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_code
     *
     * @return the value of sys_menu.menu_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_code
     *
     * @param menuCode the value for sys_menu.menu_code
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_name
     *
     * @return the value of sys_menu.menu_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_name
     *
     * @param menuName the value for sys_menu.menu_name
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_url
     *
     * @return the value of sys_menu.menu_url
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_url
     *
     * @param menuUrl the value for sys_menu.menu_url
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.parent_id
     *
     * @return the value of sys_menu.parent_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.parent_id
     *
     * @param parentId the value for sys_menu.parent_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.fsort
     *
     * @return the value of sys_menu.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Integer getFsort() {
        return fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.fsort
     *
     * @param fsort the value for sys_menu.fsort
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setFsort(Integer fsort) {
        this.fsort = fsort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.has_child
     *
     * @return the value of sys_menu.has_child
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Integer getHasChild() {
        return hasChild;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.has_child
     *
     * @param hasChild the value for sys_menu.has_child
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
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
     * This method returns the value of the database column sys_menu.add_user_id
     *
     * @return the value of sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getAddUserId() {
        return addUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.add_user_id
     *
     * @param addUserId the value for sys_menu.add_user_id
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId == null ? null : addUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.add_time
     *
     * @return the value of sys_menu.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.add_time
     *
     * @param addTime the value for sys_menu.add_time
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.state
     *
     * @return the value of sys_menu.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.state
     *
     * @param state the value for sys_menu.state
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.backup1
     *
     * @return the value of sys_menu.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup1() {
        return backup1;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.backup1
     *
     * @param backup1 the value for sys_menu.backup1
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.backup2
     *
     * @return the value of sys_menu.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup2() {
        return backup2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.backup2
     *
     * @param backup2 the value for sys_menu.backup2
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.backup3
     *
     * @return the value of sys_menu.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public String getBackup3() {
        return backup3;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.backup3
     *
     * @param backup3 the value for sys_menu.backup3
     *
     * @mbg.generated Tue May 08 09:26:48 CST 2018
     */
    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }
}