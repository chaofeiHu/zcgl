package com.hz.demo.model;

import java.util.Date;

public class base_unit {
    private Integer bid;
    private String unitCode;
    private String organizationCode;
    private String creditCode;
    private String unitName;
    private String unitAttach;
    private String unitCategory;
    private String industryInvolved;
    private String unitNature;
    private String  economicType;
    private String areaNumber;
    private String linkMan;
    private String phone;
    private String address;
    private String fax;
    private String email;
    private String postalCode;
    private Date addTime;
    private String addUserId;
    private Integer state;
    private String parentUnitCode;
    private Integer isBuildjudging;
    private Integer isManageunit;
    private Integer isFirmlyorganization;
    private Integer isDelete;
    private String back1;
    private String back2;
    private String back3;

    @Override
    public String toString() {
        return "base_unit{" +
                "bid=" + bid +
                ", unitCode='" + unitCode + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", creditCode='" + creditCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitAttach='" + unitAttach + '\'' +
                ", unitCategory='" + unitCategory + '\'' +
                ", industryInvolved='" + industryInvolved + '\'' +
                ", unitNature='" + unitNature + '\'' +
                ", economicType='" + economicType + '\'' +
                ", areaNumber='" + areaNumber + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", addTime=" + addTime +
                ", addUserId='" + addUserId + '\'' +
                ", state=" + state +
                ", parentUnitCode='" + parentUnitCode + '\'' +
                ", isBuildjudging=" + isBuildjudging +
                ", isManageunit=" + isManageunit +
                ", isFirmlyorganization=" + isFirmlyorganization +
                ", isDelete=" + isDelete +
                ", back1='" + back1 + '\'' +
                ", back2='" + back2 + '\'' +
                ", back3='" + back3 + '\'' +
                '}';
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitAttach() {
        return unitAttach;
    }

    public void setUnitAttach(String unitAttach) {
        this.unitAttach = unitAttach;
    }

    public String getUnitCategory() {
        return unitCategory;
    }

    public void setUnitCategory(String unitCategory) {
        this.unitCategory = unitCategory;
    }

    public String getIndustryInvolved() {
        return industryInvolved;
    }

    public void setIndustryInvolved(String industryInvolved) {
        this.industryInvolved = industryInvolved;
    }

    public String getUnitNature() {
        return unitNature;
    }

    public void setUnitNature(String unitNature) {
        this.unitNature = unitNature;
    }

    public String getEconomicType() {
        return economicType;
    }

    public void setEconomicType(String economicType) {
        this.economicType = economicType;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getParentUnitCode() {
        return parentUnitCode;
    }

    public void setParentUnitCode(String parentUnitCode) {
        this.parentUnitCode = parentUnitCode;
    }

    public Integer getIsBuildjudging() {
        return isBuildjudging;
    }

    public void setIsBuildjudging(Integer isBuildjudging) {
        this.isBuildjudging = isBuildjudging;
    }

    public Integer getIsManageunit() {
        return isManageunit;
    }

    public void setIsManageunit(Integer isManageunit) {
        this.isManageunit = isManageunit;
    }

    public Integer getIsFirmlyorganization() {
        return isFirmlyorganization;
    }

    public void setIsFirmlyorganization(Integer isFirmlyorganization) {
        this.isFirmlyorganization = isFirmlyorganization;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getBack1() {
        return back1;
    }

    public void setBack1(String back1) {
        this.back1 = back1;
    }

    public String getBack2() {
        return back2;
    }

    public void setBack2(String back2) {
        this.back2 = back2;
    }

    public String getBack3() {
        return back3;
    }

    public void setBack3(String back3) {
        this.back3 = back3;
    }

    public base_unit(Integer bid, String unitCode, String organizationCode, String creditCode, String unitName, String unitAttach, String unitCategory, String industryInvolved, String unitNature, String economicType, String areaNumber, String linkMan, String phone, String address, String fax, String email, String postalCode, Date addTime, String addUserId, Integer state, String parentUnitCode, Integer isBuildjudging, Integer isManageunit, Integer isFirmlyorganization, Integer isDelete, String back1, String back2, String back3) {

        this.bid = bid;
        this.unitCode = unitCode;
        this.organizationCode = organizationCode;
        this.creditCode = creditCode;
        this.unitName = unitName;
        this.unitAttach = unitAttach;
        this.unitCategory = unitCategory;
        this.industryInvolved = industryInvolved;
        this.unitNature = unitNature;
        this.economicType = economicType;
        this.areaNumber = areaNumber;
        this.linkMan = linkMan;
        this.phone = phone;
        this.address = address;
        this.fax = fax;
        this.email = email;
        this.postalCode = postalCode;
        this.addTime = addTime;
        this.addUserId = addUserId;
        this.state = state;
        this.parentUnitCode = parentUnitCode;
        this.isBuildjudging = isBuildjudging;
        this.isManageunit = isManageunit;
        this.isFirmlyorganization = isFirmlyorganization;
        this.isDelete = isDelete;
        this.back1 = back1;
        this.back2 = back2;
        this.back3 = back3;
    }

    public base_unit() {

    }
}