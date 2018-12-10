package com.hz.demo.model.shenbao;

import com.hz.demo.model.base_proposer;

import java.util.List;

public class ProposerMsg {
    private String id;

    private String userId;

    private String proposeDate;

    private String dutyLevel;

    private String major;

    private String dutyTimeLimit;

    private String position;

    private String positionGetTime;

    private String positionLevel;

    private String proposePosition;

    private String proposeState;

    private String objectivePoint;

    private String totalPoint;

    private String areaNumber;

    private String backup1;

    private String backup2;

    private String backup3;

    private String userpicurl;

    private String usertype;

    private String frontidcardpicurl;

    private String behindidcardpicurl;

    private String extrainfo;

    private String createtime;

    private String statu;

    private String fristeducation;

    private String fristeducationurl;

    private String fristeducationtime;

    private String maxeducation;

    private String maxeducationtime;

    private String maxeducationurl;

    private String profess;

    private String professCertificate;

    private String startworktime;

    private String nowjob;

    private String nowjobTime;

    private String foreignName;

    private String foreignLevel;

    private String foreignTime;

    private String currentPost;

    private String currentPostSeries;

    private String currentPostLevel;

    private String currentPostGetTime;

    private String currentPostEngageTime;

    private String otherPost;

    private String otherPostSeries;

    private String otherPostLevel;

    private String otherPostEngageTime;

    private String administrativePost;

    private String administrativePostTime;

    private String socialPost;

    private String socialPostPhone;

    private String yearsOne;

    private String yearsOneInfo;

    private String yearsTwo;

    private String yearsTwoInfo;

    private String yearsThree;

    private String yearsThreeInfo;

    private String yearsFour;

    private String yearsFourInfo;

    private String yearsFive;

    private String yearsFiveInfo;

    private String pdfurl;

    private String progress;

    private String stat;

    private String shenbaomajorId;

    private String stat2;

    private String pingshentype;



    private String judgingStage;

    private String baseJudgingSeries;

    private String reviewSeries;

    private String sex;

    private String nation;

    private String politicalOutlook;

    private String eduEdu;

    private String eduDegree;

    private String eduMajor;

    private String reportDate;
    private String currentState;
    private String yearsOneInfoName;
    private String yearsTwoInfoName;
    private String yearsThreeInfoName;
    private String yearsFourInfoName;
    private String yearsFiveInfoName;

    private base_proposer baseProposer;//

    private List<Education> education;//
    private List<Work> work;//
    private List<Train> train;//


    private List<Award> award;//
    private List<Patent> patent;//
    private List<Project> project;//
    private List<Paper> paper;//
    private List<Book> book;//
    private List<Research> research;


    private String majorName;
    private String positionName;

    private Summary summary;//
    private List<JudLog> judLog;//
    private List<JudResult> judResult;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProposeDate() {
        return proposeDate;
    }

    public void setProposeDate(String proposeDate) {
        this.proposeDate = proposeDate;
    }

    public String getDutyLevel() {
        return dutyLevel;
    }

    public void setDutyLevel(String dutyLevel) {
        this.dutyLevel = dutyLevel == null ? null : dutyLevel.trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major == null ? null : major.trim();
    }

    public String getDutyTimeLimit() {
        return dutyTimeLimit;
    }

    public void setDutyTimeLimit(String dutyTimeLimit) {
        this.dutyTimeLimit = dutyTimeLimit == null ? null : dutyTimeLimit.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getPositionGetTime() {
        return positionGetTime;
    }

    public void setPositionGetTime(String positionGetTime) {
        this.positionGetTime = positionGetTime;
    }

    public String getPositionLevel() {
        return positionLevel;
    }

    public void setPositionLevel(String positionLevel) {
        this.positionLevel = positionLevel == null ? null : positionLevel.trim();
    }

    public String getProposePosition() {
        return proposePosition;
    }

    public void setProposePosition(String proposePosition) {
        this.proposePosition = proposePosition == null ? null : proposePosition.trim();
    }

    public String getProposeState() {
        return proposeState;
    }

    public void setProposeState(String proposeState) {
        this.proposeState = proposeState == null ? null : proposeState.trim();
    }

    public String getObjectivePoint() {
        return objectivePoint;
    }

    public void setObjectivePoint(String objectivePoint) {
        this.objectivePoint = objectivePoint == null ? null : objectivePoint.trim();
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint == null ? null : totalPoint.trim();
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber == null ? null : areaNumber.trim();
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    public String getBackup3() {
        return backup3;
    }

    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }

    public String getUserpicurl() {
        return userpicurl;
    }

    public void setUserpicurl(String userpicurl) {
        this.userpicurl = userpicurl == null ? null : userpicurl.trim();
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype == null ? null : usertype.trim();
    }

    public String getFrontidcardpicurl() {
        return frontidcardpicurl;
    }

    public void setFrontidcardpicurl(String frontidcardpicurl) {
        this.frontidcardpicurl = frontidcardpicurl == null ? null : frontidcardpicurl.trim();
    }

    public String getBehindidcardpicurl() {
        return behindidcardpicurl;
    }

    public void setBehindidcardpicurl(String behindidcardpicurl) {
        this.behindidcardpicurl = behindidcardpicurl == null ? null : behindidcardpicurl.trim();
    }

    public String getExtrainfo() {
        return extrainfo;
    }

    public void setExtrainfo(String extrainfo) {
        this.extrainfo = extrainfo == null ? null : extrainfo.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu == null ? null : statu.trim();
    }


    public String getFristeducation() {
        return fristeducation;
    }

    public void setFristeducation(String fristeducation) {
        this.fristeducation = fristeducation == null ? null : fristeducation.trim();
    }

    public String getFristeducationurl() {
        return fristeducationurl;
    }

    public void setFristeducationurl(String fristeducationurl) {
        this.fristeducationurl = fristeducationurl == null ? null : fristeducationurl.trim();
    }

    public String getFristeducationtime() {
        return fristeducationtime;
    }

    public void setFristeducationtime(String fristeducationtime) {
        this.fristeducationtime = fristeducationtime;
    }

    public String getMaxeducation() {
        return maxeducation;
    }

    public void setMaxeducation(String maxeducation) {
        this.maxeducation = maxeducation == null ? null : maxeducation.trim();
    }

    public String getMaxeducationtime() {
        return maxeducationtime;
    }

    public void setMaxeducationtime(String maxeducationtime) {
        this.maxeducationtime = maxeducationtime;
    }

    public String getMaxeducationurl() {
        return maxeducationurl;
    }

    public void setMaxeducationurl(String maxeducationurl) {
        this.maxeducationurl = maxeducationurl == null ? null : maxeducationurl.trim();
    }

    public String getProfess() {
        return profess;
    }

    public void setProfess(String profess) {
        this.profess = profess == null ? null : profess.trim();
    }

    public String getProfessCertificate() {
        return professCertificate;
    }

    public void setProfessCertificate(String professCertificate) {
        this.professCertificate = professCertificate == null ? null : professCertificate.trim();
    }

    public String getStartworktime() {
        return startworktime;
    }

    public void setStartworktime(String startworktime) {
        this.startworktime = startworktime;
    }

    public String getNowjob() {
        return nowjob;
    }

    public void setNowjob(String nowjob) {
        this.nowjob = nowjob == null ? null : nowjob.trim();
    }

    public String getNowjobTime() {
        return nowjobTime;
    }

    public void setNowjobTime(String nowjobTime) {
        this.nowjobTime = nowjobTime;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName == null ? null : foreignName.trim();
    }

    public String getForeignLevel() {
        return foreignLevel;
    }

    public void setForeignLevel(String foreignLevel) {
        this.foreignLevel = foreignLevel == null ? null : foreignLevel.trim();
    }

    public String getForeignTime() {
        return foreignTime;
    }

    public void setForeignTime(String foreignTime) {
        this.foreignTime = foreignTime;
    }

    public String getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(String currentPost) {
        this.currentPost = currentPost == null ? null : currentPost.trim();
    }

    public String getCurrentPostSeries() {
        return currentPostSeries;
    }

    public void setCurrentPostSeries(String currentPostSeries) {
        this.currentPostSeries = currentPostSeries == null ? null : currentPostSeries.trim();
    }

    public String getCurrentPostLevel() {
        return currentPostLevel;
    }

    public void setCurrentPostLevel(String currentPostLevel) {
        this.currentPostLevel = currentPostLevel == null ? null : currentPostLevel.trim();
    }

    public String getCurrentPostGetTime() {
        return currentPostGetTime;
    }

    public void setCurrentPostGetTime(String currentPostGetTime) {
        this.currentPostGetTime = currentPostGetTime;
    }

    public String getCurrentPostEngageTime() {
        return currentPostEngageTime;
    }

    public void setCurrentPostEngageTime(String currentPostEngageTime) {
        this.currentPostEngageTime = currentPostEngageTime;
    }

    public String getOtherPost() {
        return otherPost;
    }

    public void setOtherPost(String otherPost) {
        this.otherPost = otherPost == null ? null : otherPost.trim();
    }

    public String getOtherPostSeries() {
        return otherPostSeries;
    }

    public void setOtherPostSeries(String otherPostSeries) {
        this.otherPostSeries = otherPostSeries == null ? null : otherPostSeries.trim();
    }

    public String getOtherPostLevel() {
        return otherPostLevel;
    }

    public void setOtherPostLevel(String otherPostLevel) {
        this.otherPostLevel = otherPostLevel == null ? null : otherPostLevel.trim();
    }

    public String getOtherPostEngageTime() {
        return otherPostEngageTime;
    }

    public void setOtherPostEngageTime(String otherPostEngageTime) {
        this.otherPostEngageTime = otherPostEngageTime;
    }

    public String getAdministrativePost() {
        return administrativePost;
    }

    public void setAdministrativePost(String administrativePost) {
        this.administrativePost = administrativePost == null ? null : administrativePost.trim();
    }

    public String getAdministrativePostTime() {
        return administrativePostTime;
    }

    public void setAdministrativePostTime(String administrativePostTime) {
        this.administrativePostTime = administrativePostTime;
    }

    public String getSocialPost() {
        return socialPost;
    }

    public void setSocialPost(String socialPost) {
        this.socialPost = socialPost == null ? null : socialPost.trim();
    }

    public String getSocialPostPhone() {
        return socialPostPhone;
    }

    public void setSocialPostPhone(String socialPostPhone) {
        this.socialPostPhone = socialPostPhone == null ? null : socialPostPhone.trim();
    }

    public String getYearsOne() {
        return yearsOne;
    }

    public void setYearsOne(String yearsOne) {
        this.yearsOne = yearsOne == null ? null : yearsOne.trim();
    }

    public String getYearsOneInfo() {
        return yearsOneInfo;
    }

    public void setYearsOneInfo(String yearsOneInfo) {
        this.yearsOneInfo = yearsOneInfo == null ? null : yearsOneInfo.trim();
    }

    public String getYearsTwo() {
        return yearsTwo;
    }

    public void setYearsTwo(String yearsTwo) {
        this.yearsTwo = yearsTwo == null ? null : yearsTwo.trim();
    }

    public String getYearsTwoInfo() {
        return yearsTwoInfo;
    }

    public void setYearsTwoInfo(String yearsTwoInfo) {
        this.yearsTwoInfo = yearsTwoInfo == null ? null : yearsTwoInfo.trim();
    }

    public String getYearsThree() {
        return yearsThree;
    }

    public void setYearsThree(String yearsThree) {
        this.yearsThree = yearsThree == null ? null : yearsThree.trim();
    }

    public String getYearsThreeInfo() {
        return yearsThreeInfo;
    }

    public void setYearsThreeInfo(String yearsThreeInfo) {
        this.yearsThreeInfo = yearsThreeInfo == null ? null : yearsThreeInfo.trim();
    }

    public String getYearsFour() {
        return yearsFour;
    }

    public void setYearsFour(String yearsFour) {
        this.yearsFour = yearsFour == null ? null : yearsFour.trim();
    }

    public String getYearsFourInfo() {
        return yearsFourInfo;
    }

    public void setYearsFourInfo(String yearsFourInfo) {
        this.yearsFourInfo = yearsFourInfo == null ? null : yearsFourInfo.trim();
    }

    public String getYearsFive() {
        return yearsFive;
    }

    public void setYearsFive(String yearsFive) {
        this.yearsFive = yearsFive == null ? null : yearsFive.trim();
    }

    public String getYearsFiveInfo() {
        return yearsFiveInfo;
    }

    public void setYearsFiveInfo(String yearsFiveInfo) {
        this.yearsFiveInfo = yearsFiveInfo == null ? null : yearsFiveInfo.trim();
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl == null ? null : pdfurl.trim();
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress == null ? null : progress.trim();
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat == null ? null : stat.trim();
    }

    public String getShenbaomajorId() {
        return shenbaomajorId;
    }

    public void setShenbaomajorId(String shenbaomajorId) {
        this.shenbaomajorId = shenbaomajorId == null ? null : shenbaomajorId.trim();
    }

    public String getStat2() {
        return stat2;
    }

    public void setStat2(String stat2) {
        this.stat2 = stat2 == null ? null : stat2.trim();
    }

    public String getPingshentype() {
        return pingshentype;
    }

    public void setPingshentype(String pingshentype) {
        this.pingshentype = pingshentype == null ? null : pingshentype.trim();
    }



    public String getJudgingStage() {
        return judgingStage;
    }

    public void setJudgingStage(String judgingStage) {
        this.judgingStage = judgingStage == null ? null : judgingStage.trim();
    }


    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Work> getWork() {
        return work;
    }

    public void setWork(List<Work> work) {
        this.work = work;
    }



    public List<Paper> getPaper() {
        return paper;
    }

    public void setPaper(List<Paper> paper) {
        this.paper = paper;
    }

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }



    public base_proposer getBaseProposer() {
        return baseProposer;
    }

    public void setBaseProposer(base_proposer baseProposer) {
        this.baseProposer = baseProposer;
    }

    public List<Train> getTrain() {
        return train;
    }

    public void setTrain(List<Train> train) {
        this.train = train;
    }

    public List<Research> getResearch() {
        return research;
    }

    public void setResearch(List<Research> research) {
        this.research = research;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Patent> getPatent() {
        return patent;
    }

    public void setPatent(List<Patent> patent) {
        this.patent = patent;
    }


    public List<JudLog> getJudLog() {
        return judLog;
    }

    public void setJudLog(List<JudLog> judLog) {
        this.judLog = judLog;
    }

    public List<JudResult> getJudResult() {
        return judResult;
    }

    public void setJudResult(List<JudResult> judResult) {
        this.judResult = judResult;
    }

    public String getBaseJudgingSeries() {
        return baseJudgingSeries;
    }

    public void setBaseJudgingSeries(String baseJudgingSeries) {
        this.baseJudgingSeries = baseJudgingSeries;
    }

    public String getReviewSeries() {
        return reviewSeries;
    }

    public void setReviewSeries(String reviewSeries) {
        this.reviewSeries = reviewSeries;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPoliticalOutlook() {
        return politicalOutlook;
    }

    public void setPoliticalOutlook(String politicalOutlook) {
        this.politicalOutlook = politicalOutlook;
    }

    public String getEduEdu() {
        return eduEdu;
    }

    public void setEduEdu(String eduEdu) {
        this.eduEdu = eduEdu;
    }

    public String getEduDegree() {
        return eduDegree;
    }

    public void setEduDegree(String eduDegree) {
        this.eduDegree = eduDegree;
    }

    public String getEduMajor() {
        return eduMajor;
    }

    public void setEduMajor(String eduMajor) {
        this.eduMajor = eduMajor;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public List<Award> getAward() {
        return award;
    }

    public void setAward(List<Award> award) {
        this.award = award;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getYearsOneInfoName() {
        return yearsOneInfoName;
    }

    public void setYearsOneInfoName(String yearsOneInfoName) {
        this.yearsOneInfoName = yearsOneInfoName;
    }

    public String getYearsTwoInfoName() {
        return yearsTwoInfoName;
    }

    public void setYearsTwoInfoName(String yearsTwoInfoName) {
        this.yearsTwoInfoName = yearsTwoInfoName;
    }

    public String getYearsThreeInfoName() {
        return yearsThreeInfoName;
    }

    public void setYearsThreeInfoName(String yearsThreeInfoName) {
        this.yearsThreeInfoName = yearsThreeInfoName;
    }

    public String getYearsFourInfoName() {
        return yearsFourInfoName;
    }

    public void setYearsFourInfoName(String yearsFourInfoName) {
        this.yearsFourInfoName = yearsFourInfoName;
    }

    public String getYearsFiveInfoName() {
        return yearsFiveInfoName;
    }

    public void setYearsFiveInfoName(String yearsFiveInfoName) {
        this.yearsFiveInfoName = yearsFiveInfoName;
    }
}