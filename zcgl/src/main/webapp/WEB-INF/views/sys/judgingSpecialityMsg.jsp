<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;Charset=utf-8;" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String MenuBtns = request.getAttribute("MenuBtns").toString();
    String currentJudgingStage = "13";


    //主题
    String themeName = "default";
    Cookie cookies[] = request.getCookies();
    if (cookies != null && cookies.length > 0) {
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("easyuiThemeName")) {
                themeName = cookies[i].getValue();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>审批推荐专家信息</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>

    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <style>
        .imgbig {
            cursor: pointer;
        }

        table {
            border: none;

        }

        .mytable td {
            border: 1px solid #dddddd;
            text-align: center;
            color: #000;
            font-weight: bold;
            height: 26px;
        }

        .mytable {
            width: 100%;
        }

        .mytable td span {
            color: #808080;
            text-align: center;
            font-weight: normal;
        }

        .jiange {
            line-height: 35px;
        }

        .jiange1 {
            line-height: 60px;
        }

        .jiange2 {
            height: 10px;
        }

        .tongguo, .butongguo, .yijian {
            margin-left: 15px;
        }

        textarea {
            margin-bottom: -11px;
            padding-left: 8px;
            padding-right: 0;
            padding-top: 8px;
            padding-bottom: 0;
            height: 24px;
            color: #13ba5e;
        }

        input[type="radio" ] {
            margin-left: 0px;
            margin-right: 0px;
        }

        input[type="radio" ]:hover {
            cursor: pointer;
        }

        .title {
            color: #0e90d2;
            font-size: 18px;
            font-weight: bolder;
            margin: 10px 5px;
        }

        /*tbody,tr{*/
        /*width: 100%;*/
        /*position: absolute;*/
        /*display: table;*/
        /*}*/
        .xinxi td {
            line-height: 18px;
            border: none !important;
            font-weight: bold;
            text-align: left;
        }

        .xinxi span {
            margin-left: 8px;
        }

        .zhaopian {
            height: 100px;
            margin-top: -132px;
            top: 112px;
            position: relative;
            float: right;
            /*margin-right: 78px;*/
            width: 66px;
        }

        @media screen and (max-width: 1366px) {
            .weiyi {
                width: 100px;
            }

            .weier {
                width: 200px;
            }
        }
    </style>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form_spe: null,
            edit_window_spe: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'displayName',
            actionUrl: '<%=basePath%>/CreateSpeciality/',
            actionUrlJudging: '<%=basePath%>/JudgingProposer/'
        };


        //下拉框
        function chongyong(url, ids, vaid, tid) {
            $.ajax({
                url: url,
                dataType: 'json',
                success: function (jsonstr) {
                    if (vaid == "dictCode") {
                        jsonstr.push({'dictCode': '', 'dictName': '请选择..'});
                    } else {
                        jsonstr.push({'ID': '', 'TEXT': '请选择..'});
                    }
                    $(ids).combobox({  //为下拉框赋值
                        data: jsonstr,
                        valueField: vaid,
                        textField: tid
                    });
                }
            })
        }

        $(function () {
            pageInit();
            loadGrid();
            chongyong('<%=basePath%>/BaseUnit/getDictList?groupName=REVIEW_SERIES', '#xl', 'dictCode', 'dictName');
            chongyong('<%=basePath%>/BaseUnit/getDictList?groupName=CURRENT_STATE', '#sczt', 'dictCode', 'dictName');
            initYearNo();
            $('#dqzt').combobox({  //为下拉框赋值
                url: '<%=basePath%>/BaseUnit/selectScxt',
                valueField: 'id',
                textField: 'text'
            });
            $('#dqzt').combobox('select', '-1');
        });

        function pageInit() {


            me.edit_window_spe = $('#edit_window_spe');
            me.edit_form_spe = me.edit_window_spe.find('#edit_form_spe');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');


            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.search_window.window('close');
            });
            $('#saveJudgingSpe').linkbutton().unbind();
            $('#saveJudgingSpe').linkbutton().click(function () {
                if ($('#judgingViewSpe')[0].value != "" && $('#judgingViewSpe')[0].value != null) {
                    saveJudgingSpe();
                } else {
                    alert("请填写意见");
                }

            });
            //查询
            $('#btn_search_ok').click(function () {
                me.datagrid1.datagrid('load', {
                    displayName2: $('#displayName2').val(),
                    nian: $('#nian').combobox('getValue'),
                    gljg: $('#gljg').val(),
                    xl: $('#xl').combobox('getValue'),
                    sczt: $('#sczt').combobox('getValue'),
                    dqzt: $("#dqzt").combobox('getValue')
                });
            });

            //导出
            $('#btn_search_cancel').click(function () {
                var page = me.datagrid1.datagrid("getPager").data("pagination").options.pageNumber;
                var rows = me.datagrid1.datagrid("getPager").data("pagination").options.pageSize;
                var va = '?displayName2=' + $('#displayName2').val()
                    + '&gljg=' + $('#gljg').val()
                    + '&nian=' + $('#nian').combobox('getValue')
                    + '&xl=' + $('#xl').combobox('getValue')
                    + '&sczt=' + $('#sczt').combobox('getValue')
                    + '&dqzt=' + $('#dqzt').combobox('getValue')
                    + '&judgingStage=' + <%=currentJudgingStage%>
                    + '&page=' + page + '&rows=' + rows;
                window.location.href = me.actionUrl + 'exportExcelSpecialityMsgType.do' + va;
            });
        }

        //显示图片
        function imgShow(outerdiv, innerdiv, bigimg, _this) {
            var src = _this.attr("src");//获取当前点击的pimg元素中的src属性
            $(bigimg).attr("src", src);//设置#bigimg元素的src属性

            /*获取当前点击图片的真实大小，并显示弹出层及大图*/
            $("<img/>").attr("src", src).load(function () {
                var windowW = $(window).width();//获取当前窗口宽度
                var windowH = $(window).height();//获取当前窗口高度
                var realWidth = this.width;//获取图片真实宽度
                var realHeight = this.height;//获取图片真实高度
                var imgWidth, imgHeight;
                var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

                if (realHeight > windowH * scale) {//判断图片高度
                    imgHeight = windowH * scale;//如大于窗口高度，图片高度进行缩放
                    imgWidth = imgHeight / realHeight * realWidth;//等比例缩放宽度
                    if (imgWidth > windowW * scale) {//如宽度扔大于窗口宽度
                        imgWidth = windowW * scale;//再对宽度进行缩放
                    }
                } else if (realWidth > windowW * scale) {//如图片高度合适，判断图片宽度
                    imgWidth = windowW * scale;//如大于窗口宽度，图片宽度进行缩放
                    imgHeight = imgWidth / realWidth * realHeight;//等比例缩放高度
                } else {//如果图片真实高度和宽度都符合要求，高宽不变
                    imgWidth = realWidth;
                    imgHeight = realHeight;
                }
                $(bigimg).css("width", imgWidth);//以最终的宽度对图片缩放

                var w = (windowW - imgWidth) / 2 - 100;//计算图片与窗口左边距
                var h = (windowH - imgHeight) / 2;//计算图片与窗口上边距
                $(innerdiv).css({"top": h, "left": w});//设置#innerdiv的top和left属性
                $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
            });

            $(outerdiv).click(function () {//再次点击淡出消失弹出层
                $(this).fadeOut("fast");
            });
        }

        //保存评审意见
        function saveJudgingSpe() {
            var formData = new FormData($("#edit_form_spe")[0]);
            $.ajax({
                url: '<%=basePath%>/JudgingProposer/saveJudgingSpe.do?judId=${sessionScope.User.userId}',
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,

                success: function (data) {
                    if (data.isOk == 1) {
                        showInfo(data.message);
                        me.edit_window_spe.window('close');
                        me.datagrid1.datagrid('reload');
                    } else {
                        showError(data.message);
                    }
                }
            });
        }


        //加载专家列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'GetJudgingSpecialityMsgList.do',
                frozenColumns: [[
                    {
                        field: 'DISPLAY_NAME', title: '姓名', width: 100, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            strReturn = '<span style="line-height: 28px">' +
                                '<a href="javascript:void(0)" style="color:blue;text-decoration:underline;" title="审批信息"  ' +
                                'onclick="JudgingMsg(\'' + row['ID'] + '\',\'' + row['BACKUP2'] + '\')" >' + value + '</a></span>';
                            return strReturn;
                        }
                    }
                ]],
                columns: [[
                    {field: 'UNIT_NAME', title: '推荐单位', width: 100, align: 'center'},
                    {field: 'UNIT_NAME', title: '单位', width: 100, align: 'center'},
                    {field: 'XILIE', title: '推荐系列', width: 100, align: 'center'},
                    {field: 'PROFESSIAL_NAME', title: '推荐专业', width: 150, align: 'center'},
                    {field: 'CURRENT_STATE', title: '当前状态', width: 100, align: 'center'},

                ]],
                onBeforeLoad: function (param) {
                    // param.deptId = me.edit_form.find('#deptId').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                    param.judgingStage =<%=currentJudgingStage%>;
                }
            });

        }

        //初始化审批信息
        function JudgingMsg(id, action) {
            switch (action) {
                case "1":
                    $.ajax({
                        url: me.actionUrlJudging + 'getCreateSpecialityAllMsg.do?userid=' + id,
                        success: function (data) {
                            me.edit_form_spe.form('load', data);
                            $('#userIdSpe').val(data['userId']);
                            $('input[name=finalResultSpe][value="1"]').attr("checked", true);
                            me.edit_window_spe.find('#weiTongDivSpe').css('display', 'none');
                            me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'none');
                            initProposerMsgSpe(data);
                            initTableWork('cusTableWorkSpe', data['work']);
                            initTableAchi('cusTableAchiSpe', data['achievement']);
                            initTableAwa('cusTableAwaSpe', data['award']);
                            initTablePap('cusTablePapSpe', data['paper']);
                            initTableBook('cusTableBookSpe', data['book']);
                            initTableRes('cusTableResSpe', data['research']);
                            initTableMaj('cusTableMajSpe', data['majorResearch']);
                            initJudgingSpe(id);
                            $(".imgbig").hover(function () {
                                var _this = $(this);//将当前的pimg元素作为_this传入函数
                                imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
                            }, function () {
                                $('#outerdiv').fadeOut("fast");
                            })
                        }
                    });
                    me.edit_window_spe.window('open');
                    break;
                default:
                    break;
            }
        }




        //初始化意见  绑定单选框点击事件(隐藏/显示意见)
        function initJudgingSpe(id) {
            $('input[name=finalResultSpe]').change(function () {
                if (this.value == '0') {//不通过
                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'block');
                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'none');
                    $('#saveJudgingSpe').linkbutton({text:"不推荐"});
                } else if (this.value == '1') {//通过
                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'none');
                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'none');
                    $('#saveJudgingSpe').linkbutton({text:"推荐"});
                } else if (this.value == '2' ) {//退回
                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'none');
                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'block');
                    $('#saveJudgingSpe').linkbutton({text:"退回修改"});
                }
            });
            //初始所有意见  按钮
            $.ajax({
                url: me.actionUrlJudging + 'getProposerJudResult.do?userid=' + id + '&&currentJudgingStage=<%=currentJudgingStage%>',
                success: function (data) {
                    $("#saveJudgingSpe").css('display', 'block');
                    $('#finalResultSpe0').css('display', 'inline');
                    $('#finalResultSpe1').css('display', 'inline');
                    $('#finalResultSpe2').css('display', 'inline');
                    if (data != null && data.length > 0) {
                        $(data).each(function (index, item) {
                            if (item.judContent == '99') {
                                if (item.judResult == '0') {//不通
                                    $('input[name=finalResultSpe][value="0"]').attr("checked", true);
                                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'block');
                                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'none');
                                    $('#saveJudgingSpe').linkbutton({text:"不推荐"});
                                    me.edit_window_spe.find('#judgingViewSpe').textbox().textbox('setValue', item.judView);
                                    $("#saveJudgingSpe").css('display', 'none');
                                    $('#finalResultSpe1').css('display', 'none');
                                    $('#finalResultSpe2').css('display', 'none');
                                } else if (item.judResult == '1' ) {//通
                                    $('input[name=finalResultSpe][value="1"]').attr("checked", true);
                                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'none');
                                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'none');
                                    $('#saveJudgingSpe').linkbutton({text:"推荐"});
                                    $("#saveJudgingSpe").css('display', 'none');
                                    $('#finalResultSpe0').css('display', 'none');
                                    $('#finalResultSpe2').css('display', 'none');
                                } else if (item.judResult == '2' ) {//退
                                    $('input[name=finalResultSpe][value="2"]').attr("checked", true);
                                    me.edit_window_spe.find('#weiTongDivSpe').css('display', 'none');
                                    me.edit_window_spe.find('#tuiHuiDivSpe').css('display', 'block');
                                    $('#saveJudgingSpe').linkbutton({text:"退回修改"});
                                    me.edit_window_spe.find('#TuiHuiViewSpe').textbox().textbox('setValue', item.judView);
                                    $("#saveJudgingSpe").css('display', 'none');
                                    $('#finalResultSpe1').css('display', 'none');
                                    $('#finalResultSpe0').css('display', 'none');
                                }else if(item.judResult == null){
                                    $('input[name=finalResultSpe][value="1"]').attr("checked", true);
                                    me.edit_window99.find('#weiTongDivSpe').css('display', 'none');
                                    me.edit_window99.find('#tuiHuiDivSpe').css('display', 'none');
                                    $('#saveJudgingSpe').linkbutton({text:"推荐"});
                                }
                            }
                        })
                    }
                }
            });
        }
        //初始基本信息(推荐专家)
        function initProposerMsgSpe(data) {
            $.ajax({
                url: me.actionUrlJudging + 'getCreateSpecialityGuMsg.do',
                type: "POST",
                data: {'userid': data['userId']},
                async: false,
                success: function (data) {
                    if (data != null && data != 'null') {
                        var msg = data;
                        $('#displayNameSpe').html(msg['DISPLAY_NAME']);
                        $('#idCardNoSpe').html(msg['ID_CARD_NO']);
                        $('#unitNameSpe').html(msg['UNIT_NAME']);
                        $('#reviewSeriesSpe').html(msg['XILIE']);
                        $('#shenbaomajorIdSpe').html(msg['PROFESSIAL_NAME']);
                    }
                }
            });

            if (data != null && data != 'null') {
                var msg = data;
                $('#displayNameSpe').html(msg['baseProposer'].displayName);
                $('#idCardNoSpe').html(msg['baseProposer'].idCardNo);
                $('#mobilephoneSpe').html(msg['baseProposer'].mobilephone);
                $('#birthdaySpe').html(msg.birthday);
                $('#startworktimeSpe').html(msg.startworktime);
                $('#nowJobTimeSpe').html(msg.nowJobTime);
                $('#jobYearSpe').html(msg.jobYear);
                $('#graduateSchoolSpe').html(msg.graduateSchool);
                $('#graduateDateSpe').html(msg.graduateDate);
                $('#attendingAcademicGroupsSpe').html(msg.attendingAcademicGroups);
                $('#honoraryTitleSpe').html(msg.honoraryTitle);
                $('#workSituationSpe').html(msg.workSituation);
                $('#addressSpe').html(msg.address);
                $('#performanceSpe').html(msg.performance);
                $('#emailSpe').html(msg.email);
                $('#telSpe').html(msg.tel);
                $('#professialSpe').html(msg.professial);
                $('#presentationSpe').html(msg.presentation);
                initSelect(me.actionUrlJudging + 'getDict.do', "SEX", 'sexSpe', msg.sex);
                initSelect(me.actionUrlJudging + 'getDict.do', "POLITICAL_STATUS", 'politicalOutlookSpe', msg.politicalOutlook);
                initSelect(me.actionUrlJudging + 'getDict.do', "NATION", 'nationSpe', msg.nation);
                initSelect(me.actionUrlJudging + 'getDict.do', "EDU_EDU", 'educationSpe', msg.education);
                initSelect(me.actionUrlJudging + 'getDict.do', "DEGREE", 'degreeSpe', msg.degree);
                initSelect(me.actionUrlJudging + 'getDict.do', "ADMINISTRATIVE_DUTY_LEVEL", 'administrativeDutyLevelSpe', msg.administrativeDutyLevel);
                initSelect(me.actionUrlJudging + 'getDict.do', "TITLE_LEVEL", 'professialDutyLevelSpe', msg.professialDutyLevel);
                initSelect(me.actionUrlJudging + 'getDict.do', "POSITIONAL_TITLES", 'professialLevelSpe', msg.professialLevel);
                var areaCode = msg.areacode;
                if (areaCode != null) {
                    areaCode = areaCode.substr(0, 4) + "00";
                }
                initArea("<%=basePath%>/BaseUnit/getAreaList.do", 2, "410000", "areaGradeSpe2", areaCode);
                initArea("<%=basePath%>/BaseUnit/getAreaList.do", 3, areaCode, "areaGradeSpe3", msg.areacode);
                if (msg.userpicurl != null) {
                    $('#myImguserpicSpe').attr("src", '<%=basePath%>' + '/' + msg.userpicurl);
                }
            }
        }

        //初始地域
        function initArea(url, areaGrade, areaCode, selectid, selectCode) {
            $.ajax({
                url: '<%=basePath%>/BaseUnit/getAreaList.do',
                type: "POST",
                data: {"areaGrade": areaGrade, "areaCode": areaCode},
                async: false,
                success: function (data1) {
                    var msg1 = data1;
                    $('#' + selectid).html('');
                    $(msg1).each(function (index, item) {
                        var name = item['areaName'];
                        var code = item['areaCode'];
                        if (code == "410000" && (areaGrade == "2" || areaGrade == "3")) {
                            name = "省直";
                        }
                        if (code == areaCode && areaGrade == "3" && code != "410000") {
                            name = "市直";
                        }
                        if (code == selectCode) {
                            $('#' + selectid).html(name);
                        } else {
                        }
                    })
                }
            });
        }

        //通过字典初始
        function initSelect(url, groupName, selectid, selectCode) {
            $.ajax({
                url: url,
                type: "POST",
                data: {"groupName": groupName},
                async: false,
                success: function (data1) {
                    $('#' + selectid).html('');
                    var msg1 = data1;
                    $(msg1).each(function (index, item) {
                        var name = item['dictName'];
                        var code = item['dictCode'];
                        if (code == selectCode) {
                            $('#' + selectid).html(name);
                        } else {
                        }
                    })
                }
            });
        }

        //初始工作经历
        function initTableWork(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'starttime',
                        title: '起止时间',
                        width: '20%',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var s = row['starttime'];
                            var e = row['endtime'];
                            strHtml = s + "——" + e;
                            return strHtml;
                        }
                    },
                    {
                        field: 'workcompanyname',
                        width: '10%',
                        title: '单位'
                    }, {
                        field: 'job',
                        width: '14%',
                        title: '职务'
                    }, {
                        field: 'reference',
                        width: '10%',
                        title: '证明人'
                    }, {
                        field: 'extrainfo',
                        title: '备注',
                        width: '23%',
                        formatter: function (value, row, index) {
                            if (value != null) {
                                if (value.length > 10) {
                                    return "<div  title='" + value + "'><a>" + "详情" + "</a></div>";
                                } else {
                                    return "<div >" + value + "</div>";
                                }
                            } else {
                                return "-"
                            }
                        }

                    }, {
                        field: 'workfile',
                        title: '上传文件(可预览)',
                        width: '24%',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var workfiles = row["workfile"];
                            workfiles.forEach(function (val, ind, arr) {


                                var f = val['fileurl'];
                                if (f != null) {
                                    var last = f.lastIndexOf('/');
                                    var sl = f.slice(last + 1 + 13);
                                    var hou = f.slice(f.lastIndexOf("."));
                                    if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                        strHtml = strHtml + "<img style='height: 20px' class='imgbig' src=<%=basePath%>" + val['fileurl'] + "></img>&nbsp;";
                                    } else if(hou=='.pdf'){
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewPdfFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }else {
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }
                                }

                            });
                            return strHtml;
                        }
                    },
                ]],
            });
        }

        //初始奖励
        function initTableAwa(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'name',
                        title: '奖励名称',
                        width: '10%',
                    }, {
                        field: 'awardDepartment',
                        width: '10%',
                        title: '颁奖部门'
                    }, {
                        field: 'awardDate',
                        width: '10%',
                        title: '获奖日期'
                    }, {
                        field: 'paiming',
                        width: '5%',
                        title: '排名'
                    }, {
                        field: 'awardLevel',
                        width: '5%',
                        title: '级别'
                    }, {
                        field: 'grade',
                        width: '5%',
                        title: '等级'
                    }, {
                        field: 'backup1',
                        width: '33%',
                        title: '备注'
                    }, {
                        field: 'awardfile',
                        width: '23.7%',
                        title: '相关文件(可预览)',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var awardfiles = row["awardFile"];
                            awardfiles.forEach(function (val, ind, arr) {
                                var f = val['fileurl'];
                                if (f != null) {
                                    var last = f.lastIndexOf('/');
                                    var sl = f.slice(last + 1 + 13);
                                    var hou = f.slice(f.lastIndexOf("."));
                                    if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                        strHtml = strHtml + "<img style='height: 20px' class='imgbig' src=<%=basePath%>" + val['fileurl'] + "></img>&nbsp;";
                                    } else if(hou=='.pdf'){
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewPdfFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }else {
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }
                                }

                            });
                            return strHtml;
                        }

                    },
                ]],
            });
        }

        //初始论文
        function initTablePap(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'papername',
                        width: '11.8%',
                        title: '论文题目'
                    }, {
                        field: 'publishinghouse',
                        width: '12%',
                        title: '出版社'
                    }, {
                        field: 'publishtime',
                        width: '10%',
                        title: '发表/出版日期'
                    }, {
                        field: 'wordnumber',
                        width: '5%',
                        title: '字数'
                    }, {
                        field: 'maindelivery',
                        width: '5%',
                        title: '是否鉴定论文',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['appraisal'];
                            switch (f) {
                                case 0:
                                    strHtml = "未鉴定";
                                    break;
                                case 1:
                                    strHtml = "已鉴定";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    }, {
                        field: 'paiming',
                        width: '5%',
                        title: '排名'
                    }, {
                        field: 'extrainfo',
                        width: '12%',
                        title: '备注',
                        formatter: function (value, row, index) {
                            if (value != null) {
                                if (value.length > 10) {
                                    return "<div  title='" + value + "'><a>" + "详情" + "</a></div>";
                                } else {
                                    return "<div >" + value + "</div>";
                                }
                            } else {
                                return "-";
                            }
                        }
                    }, {
                        field: 'publicationName',
                        width: '7%',
                        title: '刊物名称'
                    }, {
                        field: 'isbn',
                        width: '7%',
                        title: 'isbn'
                    }, {
                        field: 'paperLevel',
                        width: '5%',
                        title: '级别'
                    }, {
                        field: 'paperfile',
                        width: '23.2%',
                        title: '文件(封面、目录、内容 可预览)',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var paperfiles = row["paperfile"];
                            paperfiles.forEach(function (val, ind, arr) {
                                var f = val['fileurl'];
                                if (f != null) {
                                    var last = f.lastIndexOf('/');
                                    var sl = f.slice(last + 1 + 13);
                                    var hou = f.slice(f.lastIndexOf("."));
                                    if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                        strHtml = strHtml + "<img style='height: 20px' class='imgbig' src=<%=basePath%>" + val['fileurl'] + "></img>&nbsp;";
                                    } else if(hou=='.pdf'){
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewPdfFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }else {
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }
                                }
                            });
                            return strHtml;
                        }
                    },
                ]],
            });
        }

        //初始著作
        function initTableBook(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'bookname',
                        width: '11.8%',
                        title: '著作书名'
                    }, {
                        field: 'publishinghouse',
                        width: '12%',
                        title: '出版社'
                    }, {
                        field: 'publishtime',
                        width: '10%',
                        title: '出版日期'
                    }, {
                        field: 'wordnumber',
                        width: '11%',
                        title: '字数'
                    }, {
                        field: 'paiming',
                        width: '11%',
                        title: '排名'
                    }, {
                        field: 'extrainfo',
                        width: '12%',
                        title: '备注',
                        formatter: function (value, row, index) {
                            if (value != null) {
                                if (value.length > 10) {
                                    return "<div  title='" + value + "'><a>" + "详情" + "</a></div>";
                                } else {
                                    return "<div >" + value + "</div>";
                                }
                            } else {
                                return "-";
                            }
                        }
                    }, {
                        field: 'bookNumber',
                        width: '5%',
                        title: '书号'
                    }, {
                        field: 'bookLevel',
                        width: '5%',
                        title: '级别'
                    }, {
                        field: 'bookFile',
                        width: '23.2%',
                        title: '文件(封面 版权页 内容 可预览)',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var bookFiles = row["bookFile"];
                            bookFiles.forEach(function (val, ind, arr) {
                                var f = val['fileurl'];
                                if (f != null) {
                                    var last = f.lastIndexOf('/');
                                    var sl = f.slice(last + 1 + 13);
                                    var hou = f.slice(f.lastIndexOf("."));
                                    if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                        strHtml = strHtml + "<img style='height: 20px' class='imgbig' src=<%=basePath%>" + val['fileurl'] + "></img>&nbsp;";
                                    } else if(hou=='.pdf'){
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewPdfFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }else {
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }
                                }
                            });
                            return strHtml;
                        }
                    },
                ]],
            });
        }

        //初始其他成果
        function initTableRes(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'getDate',
                        width: '19%',
                        title: '获得时间'
                    }, {
                        field: 'researchName',
                        width: '20%',
                        title: '研究成果名'
                    }, {
                        field: 'researchInfo',
                        width: '20%',
                        title: '研究成果简介'
                    }, {
                        field: 'researchLevelName',
                        width: '20%',
                        title: '研究成果级别'
                    }, {
                        field: 'researchFile',
                        width: '22%',
                        title: '上传文件(可预览)',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var researchFiles = row["researchFile"];
                            researchFiles.forEach(function (val, ind, arr) {
                                var f = val['fileurl'];
                                if (f != null) {
                                    var last = f.lastIndexOf('/');
                                    var sl = f.slice(last + 1 + 13);
                                    var hou = f.slice(f.lastIndexOf("."));
                                    if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                        strHtml = strHtml + "<img style='height: 20px' class='imgbig' src=<%=basePath%>" + val['fileurl'] + "></img>&nbsp;";
                                    } else if(hou=='.pdf'){
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewPdfFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }else {
                                        strHtml = strHtml + "<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;";
                                    }
                                }
                            });
                            return strHtml;
                        }
                    },
                ]],
            });
        }

        //初始工作业绩
        function initTableAchi(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'achievementName',
                        title: '业绩名',
                        width: '20%',
                    },
                    {
                        field: 'getTime',
                        title: '业绩获得时间',
                        width: '20%',
                    }, {
                        field: 'content',
                        title: '工作内容',
                        width: '60%',
                    },
                ]],
            });
        }

        //初始研究方向
        function initTableMaj(tableid, data) {
            $('#' + tableid).datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'major',
                        title: '专业',
                        width: '20%',
                    },
                    {
                        field: 'majorResearch',
                        title: '专业研究方向',
                        width: '80%',
                    },
                ]],
            });
        }

        //预览文件
        function previewFile(fileurl) {
            $.ajax({
                type: "post",
                async: false,
                dataType: "text",
                url: me.actionUrlJudging + "previewFile.do?fileName=" + fileurl,
                success: function (data) {
                    var msg = eval("(" + data + ")");
                    var xmlhttp;
                    if (window.XMLHttpRequest) {
                        xmlhttp = new XMLHttpRequest();//其他浏览器
                    } else if (window.ActiveXObject) {
                        try {
                            xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");//旧版IE
                        } catch (e) {
                        }
                        try {
                            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");//新版IE
                        } catch (e) {
                        }
                        if (!xmlhttp) {
                            console.log("不能创建XMLHttpRequest对象");
                        }
                    }
                    xmlhttp.open("GET", "<%=basePath%>" + msg['previewFile'], false);
                    xmlhttp.send();
                    if (xmlhttp.readyState == 4) {
                        if (xmlhttp.status == 200) {
                            window.open("<%=basePath%>" + msg['previewFile'], '_blank');
                        } else {
                            showError("文件缺失");
                        }
                    }
                }

            });
        }

        //打开pdf文件
        function previewPdfFile(fileUrl) {
            var xmlhttp;
            if (window.XMLHttpRequest) {
                xmlhttp = new XMLHttpRequest();//其他浏览器
            } else if (window.ActiveXObject) {
                try {
                    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");//旧版IE
                } catch (e) {
                }
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");//新版IE
                } catch (e) {
                }
                if (!xmlhttp) {
                    console.log("不能创建XMLHttpRequest对象");
                }
            }
            xmlhttp.open("GET", '<%=basePath%>/' + fileUrl, false);
            xmlhttp.send();
            if (xmlhttp.readyState == 4) {
                if (xmlhttp.status == 200) {
                    window.open('<%=basePath%>/' + fileUrl, '_blank');
                } else {
                    showError("文件缺失");
                }
            }
        }

        //初始化年度
        function initYearNo(){
            $("#nian").combobox({
                url:'<%=basePath%>/RecFileManager/getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onLoadSuccess:function (data) {
                    $('#nian').combobox("setValue",data[0].yearCode);
                    //console.log(data[0]);
                }
            });

        }
    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'north'" style="height:73px">
    <table>
        <tr>


            <td>系列：</td>
            <td><select name="xl" id="xl" style="width:208px"></select></td>
            <td>当前状态：</td>
            <td><select name="sczt" id="sczt" style="width:200px"></select></td>
            <td>管理机构：</td>
            <td><input name="gljg" id="gljg"
                       style="width: 200px;border:1px solid #8bb3e8; border-radius:4px; padding:5px 3px; height:18px;"/>
            </td>
        </tr>
        <tr>
            <td>专家姓名：</td>
            <td><input name="displayName2" id="displayName2"
                       style="width: 200px;border:1px solid #8bb3e8; border-radius:4px; padding:5px 3px; height:18px;"/>
            </td>
            <td>审查状态：</td>
            <td><select name="dqzt" class="easyui-combobox" id="dqzt" style="width: 200px;"></select></td>
            <td>年度：</td>
            <td><input name="nian" id="nian" class="easyui-combobox"
                       style="width: 200px;"/>
            </td>
            <td><a href="javascript:void(0)" id="btn_search_ok" icon="icon-search">查询</a></td>
            <td><a href="javascript:void(0)" id="btn_search_cancel" icon="icon-excel">导出</a></td>
        </tr>
    </table>
</div>
<div region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div title="审批信息窗口" id="edit_window_spe" class="easyui-window" closed="true"
     style="width: 98%;height: 95%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form_spe" name="edit_form_spe" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="idSpe" name="id"/>
                    <input type="hidden" id="userIdSpe" name="userId"/>
                    <input type="hidden" id="currentJudgingStageSpe" name="currentJudgingStage"
                           value="<%=currentJudgingStage%>"/>
                </div>
                <div id="basemsgSpe">
                    <div class="col-lg-12">
                        <div class="title">个人信息</div>
                        <div id="defaultFormbasemsgSpe" method="post" class="form-horizontal">
                            <table class="mytable">
                                <tbody>
                                <tr class="xinxi">
                                    <td>姓名:<span id="displayNameSpe"></span></td>
                                    <td class="nothasinput">性别:<span id="sexSpe" name="sex"></span></td>
                                    <td class="nothasinput">民族：<span id="nationSpe" name="nation"></span></td>
                                    </td>
                                    <td style="text-align: left;">手机号:<span id="mobilephoneSpe"
                                                                            name="mobilephone"></span></td>
                                </tr>
                                <tr class="xinxi">
                                    <td>出生日期:<span id="birthdaySpe" name="birthday"></span></td>
                                    <td>推荐系列:<span id="reviewSeriesSpe" name="reviewSeries"></span></td>
                                    <td style="text-align: left;">推荐专业:<span id="shenbaomajorIdSpe"
                                                                             name="shenbaomajorId"></span></td>


                                </tr>
                                <tr class="xinxi">
                                    <td>单位:<span id="unitNameSpe" name="unitName"></span></td>
                                    <td class="nothasinput">政治面貌:<span id="politicalOutlookSpe"
                                                                       name="politicalOutlook"></span></td>
                                    <td>身份证号:<span id="idCardNoSpe" name="idCardNo"></span></td>
                                </tr>
                                <div>
                                    <img class="imgbig zhaopian" id="myImguserpicSpe" name="myImguserpic"/>
                                </div>
                                <tr class="jiange2">
                                </tr>
                                <tr class="jiange">
                                    <td>开始工作时间：<span id="startworktimeSpe" name="startworktime"/></td>
                                    <td>行政职务：<span id="administrativeDutyLevelSpe" name="administrativeDutyLevel"></td>
                                    <td>现任专业技术职务：<span id="professialLevelSpe" name="professialLevel"/></td>
                                    <td>专业技术职务等级：<span id="professialDutyLevelSpe" name="professialDutyLevel"/></td>
                                </tr>
                                <tr class="jiange2"></tr>
                                <tr class="jiange">
                                    <td>任职时间：<span id="nowJobTimeSpe" name="nowJobTime"/></td>
                                    <td>从事专业：<span id="professialSpe" name="professial"/></td>
                                    <td>从事专业年限：<span id="jobYearSpe" name="jobYear"/></td>
                                    <td>学位：<span id="degreeSpe" name="degree"/></td>
                                </tr>
                                <tr class="jiange">
                                    <td>学历：<span id="educationSpe" name="education"/></td>
                                    <td>毕业学校：<span id="graduateSchoolSpe" name="graduateSchool"/></td>
                                    <td>毕业时间：<span id="graduateDateSpe" name="graduateDate"/></td>
                                    <td>参加学术团队任职：<span id="attendingAcademicGroupsSpe" name="attendingAcademicGroups"/>
                                    </td>
                                </tr>

                                <tr class="jiange">
                                    <td>学术技术荣誉称号：<span id="honoraryTitleSpe" name="honoraryTitle"/></td>
                                    <td>参加评审工作情况：<span id="workSituationSpe" name="workSituation"/></td>
                                    <td>个人业绩表现：<span id="performanceSpe" name="performance"/></td>
                                    <td>通讯地址：<span id="addressSpe" name="address"/></td>
                                </tr>
                                <tr class="jiange">
                                    <td>EMAIL：<span id="emailSpe"/></td>
                                    <td>办公电话：<span id="telSpe"/></td>
                                    <td>行政区划代码：市：<span id="areaGradeSpe2"/></td>
                                    <td>县：<span id="areaGradeSpe3"/></td>
                                </tr>
                                <tr>
                                    <td>个人简介</td>
                                    <td colspan="3"><span id="presentationSpe"></span></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>


                <div id="workSpe" class="table-x-hidden">
                    <div id="toolbarWorkSpe" class="btn-group-sm">
                        <div class="title">工作经历</div>
                    </div>
                    <table id="cusTableWorkSpe" class="table"></table>
                </div>

                <div id="achievementSpe" class="table-x-hidden">
                    <div id="toolbarAchiSpe" class="btn-group-sm">
                        <div class="title">工作业绩</div>
                    </div>
                    <table id="cusTableAchiSpe" class="table"></table>
                </div>

                <div id="paperSpe" class="table-x-hidden">
                    <div id="toolbarPapSpe" class="btn-group-sm">
                        <div class="title">论文</div>
                    </div>
                    <table id="cusTablePapSpe" class="table">
                    </table>
                </div>

                <div id="bookSpe" class="table-x-hidden">
                    <div id="toolbarBookSpe" class="btn-group-sm">
                        <div class="title">著作</div>
                    </div>
                    <table id="cusTableBookSpe" class="table">
                    </table>
                </div>

                <div id="researchSpe" class="table-x-hidden">
                    <div id="toolbarResSpe" class="btn-group-sm">
                        <div class="title">其他成果</div>
                    </div>
                    <table id="cusTableResSpe" class="table">
                    </table>
                </div>

                <div id="awardSpe" class="table-x-hidden">
                    <div id="toolbarAwaSpe" class="btn-group-sm">
                        <div class="title">奖励</div>
                    </div>
                    <table id="cusTableAwaSpe" class="table">
                    </table>
                </div>

                <div id="majorResearchSpe" class="table-x-hidden">
                    <div id="toolbarMajSpe" class="btn-group-sm">
                        <div class="title">研究方向</div>
                    </div>
                    <table id="cusTableMajSpe" class="table">
                    </table>
                </div>

                <div id="judTuiHuiSpe">
                    <div id="toolbarTuiHuiSpe" class="btn-group-sm">
                        <div class="title">审查</div>
                    </div>
                    <div style="width: 100%;margin-bottom: 10px">
                        <table style="border: solid 1px #b7aaaa;width: 100%">
                            <tr>
                                <td colspan="1">审查意见</td>
                                <td colspan="9"><span id="finalResultSpe1"><input type="radio" name="finalResultSpe" value="1" checked="checked"/>通过</span>
                                    <span id="finalResultSpe0"><input type="radio" name="finalResultSpe" value="0"/>未通过</span>
                                    <span id="finalResultSpe2"><input type="radio" name="finalResultSpe" value="2"/>退回修改</span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <%--未通过--%>
                    <div style="width: 100%" id="weiTongDivSpe">
                        <table style="width: 100%">
                            <tr>
                                <td colspan="2">
                                    不通过意见：
                                </td>
                                <td colspan="8">
                                    <input id="judgingViewSpe" name="judgingViewSpe" class="easyui-textbox"
                                           style="width: 80%;"
                                           reuired="true"
                                           value="不通过"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <%--退回修改--%>
                    <div style="width: 100%" id="tuiHuiDivSpe">
                        <table style="width: 100%">
                            <tr>
                                <td colspan="2">
                                    退回修改意见：
                                </td>
                                <td colspan="8">
                                    <input id="TuiHuiViewSpe" name="TuiHuiViewSpe" class="easyui-textbox"
                                           style="width: 80%;"
                                           reuired="true"
                                           value="XXX需修改"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div style="width: 400px; margin: 0px auto;margin-top: 20px" class="hiddenEdit">
                    <a href="javascript:void(0)" id="saveJudgingSpe" value="提交审批结果"
                       style="  margin-left: 135px; margin-top: 20px;">提交审批结果</a>
                </div>
            </form>
        </div>
    </div>
</div>


<div title="工作内容窗口" id="achi_window" class="easyui-window" closed="true"
     style="width: 350px;height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <div class="row">
                <div>
                    <div>
                        <label>工作内容：</label>
                        <div name="achievementtext" id="achievementtext"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="outerdiv"
     style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:2000000;display:none;">
    <div id="innerdiv" style="position:absolute;">
        <%--弹出图片框--%>
        <img id="bigimg" style="border:5px solid #fff;" src=""/>
    </div>
</div>
</body>
</html>
