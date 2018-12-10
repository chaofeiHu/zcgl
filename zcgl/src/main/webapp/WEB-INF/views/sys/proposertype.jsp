<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;Charset=utf-8;" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String MenuBtns = request.getAttribute("MenuBtns").toString();


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
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>信息管理系统</title>
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
        .shenpi {
            text-align: center;
            margin-bottom: 10px;
            margin-top: 10px;
        }

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
        }
    </style>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            edit_window99: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'displayName',
            actionUrl: '<%=basePath%>/Proposer/',
            actionUrlJudging: '<%=basePath%>/JudgingProposer/'
        };

        $(function () {
            initYearNo();
            pageInit();
            loadGrid();
            getTree();

        });

        //下拉列表树
        function getTree() {
            //基层单位
            $('#basicUnitcode').combotree({
                url: '<%=basePath%>/BaseUnit/ybTree.do',
                onBeforeExpand: function (node) {
                    $('#basicUnitcode').combotree('tree').tree('options').url = '<%=basePath%>/BaseUnit/ybTree.do?pid=' + node.id;
                }
            });
        }

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_window99 = $('#edit_window99');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');

            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.search_window.window('close');
            });
        }


        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'selectProposerTypeList.do',
                columns: [[
                    {
                        field: 'DISPLAY_NAME', title: '姓名', width: 100, align: 'center',
                        formatter: function (value, row, index) {

                            var strReturn = '';

                            strReturn = '<span style="line-height: 28px">' +
                                '<a href="javascript:void(0)" style="color:blue;text-decoration:underline;" title="审批信息"  ' +
                                'onclick="JudgingMsg(\'' + row['ID'] + '\',\'' + 'judging' + '\')" >' + value + '</a></span>';


                            return strReturn;
                        }
                    }, {field: 'ID_CARD_NO', title: '身份证', width: 150, align: 'center'},
                    {field: 'UNIT_NAME', title: '申报单位名称', width: 100, align: 'center'},
                    {field: 'DICT_NAME', title: '申报职称', width: 80, align: 'center'},
                    {field: 'PROFESSIAL_NAME', title: '学科专业', width: 100, align: 'center'},
                    {field: 'JUDGING_NAME', title: '评委会', width: 350, align: 'center'},
                    {field: 'UNIT_NAME3', title: '待审机构', width: 150, align: 'center'},
                    {
                        field: 'CURRENT_STATE',
                        title: '审查状态',
                        width: 160,
                        align: 'center'
                    },
                ]],
                toolbar: [
                    {
                        text: '姓名：<input name="displayName1" id="displayName1" style="width: 130px;border:1px solid #8bb3e8; border-radius:4px; padding:5px 3px; height:28px;"/>'
                    }, {
                        text: '年度：<select name="nian" id="nian" class="easyui-combobox"  style="width: 130px;"></select>'
                    }, {
                        text: '所在机构：<input id="basicUnitcode" name="basicUnitcode"  style="width:300px" maxlength="30"/>'
                    },
                    <% if(MenuBtns.indexOf("View")>-1) { %>
                    {
                        text: '查找', iconCls: 'icon-search', handler: function () {
                            me.datagrid1.datagrid('load', {
                                displayName1: $('#displayName1').val(),
                                nian: $('#nian').combobox('getValue'),
                                basicUnitcode: $('#basicUnitcode').val()
                            });
                        }
                    },
                    <% } %>
                    <%--<% if(MenuBtns.indexOf("Export")>-1) { %>--%>
                    {
                        text: '导出Excel', iconCls: 'icon-excel', handler: function () {
                            var page = me.datagrid1.datagrid("getPager").data("pagination").options.pageNumber;
                            var rows = me.datagrid1.datagrid("getPager").data("pagination").options.pageSize;
                            var va = '?displayName1=' + $('#displayName1').val() + '&nian=' + $('#nian').combobox('getValue')
                                + '&basicUnitcode=' + $('#basicUnitcode').val() + '&page=' + page + '&rows=' + rows;
                            window.location.href = me.actionUrl + 'exportExcelProposerType.do' + va;
                        }
                    }
                    <%--<% } %>--%>
                ],
                onBeforeLoad: function (param) {
                    param.deptId = me.edit_form.find('#deptId').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });

        }

        //添加修改初始化
        function AddOrUpdate(action) {
            getTree();
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#id,#displayName,#declareUnitcode,#loginName,#mobilephone,#idCardNo,#email,#backup1').textbox().textbox('setValue', '');
                    me.edit_form.find('#loginName').attr('disabled', false);
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?proposerId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
                                me.edit_window.find('div[region="south"]').css('display', 'block');
                                me.edit_window.window('open');
                            }
                        });
                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;
                case 'view':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?id=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
                            }
                        });
                        me.edit_window.find('div[region="south"]').css('display', 'none');
                        me.edit_window.window('open');
                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;
            }
        }

        //添加修改（保存）
        function SaveData() {
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.datagrid1.datagrid('reload');
                                me.tree1.tree('reload');
                                $("#edit_window").window("close");
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }

        //删除用户
        function Delete(ids, names) {
            $.messager.confirm('提示信息', '确认要删除选择项？【' + names + '】', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete.do',
                        data: {ids: ids},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);
                                    me.datagrid1.datagrid('reload');
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            })
        }

        //初始化所有申报信息
        function JudgingMsg(id, action) {
            switch (action) {
                case 'judging':

                    $.ajax({
                        url: me.actionUrlJudging + 'getProposerAllMsg.do?userid=' + id,
                        success: function (data) {
                            me.edit_form.form('load', data);

                            initProposerMsg(data);
                            initTableEdu(data['education']);
                            initTableWork(data['work']);
                            initTableTra(data['train']);
                            initTableAwa(data['award']);
                            initTablePat(data['patent']);
                            initTablePro(data['project']);
                            initTablePap(data['paper']);
                            initTableBook(data['book']);
                            initTableRes(data['research']);
                            initSumm(data['summary']);
                            initTableJudLog(data['userId']);
                            $(".imgbig").hover(function () {
                                var _this = $(this);//将当前的pimg元素作为_this传入函数
                                imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
                            }, function () {
                                $('#outerdiv').fadeOut("fast");
                            });


                        }
                    });
                    me.edit_window99.find('div[region="south"]').css('display', 'none');
                    me.edit_window99.window('open');

                    break;
            }
        }

        //初始基本信息
        function initProposerMsg(data) {

            $.ajax({
                url: me.actionUrlJudging + 'getGuMsg.do',
                type: "POST",
                data: {'userid': data['userId']},
                async: false,
                success: function (data) {
                    if (data != null && data != 'null') {
                        var msg = data;
                        $('#displayName').html(msg['DISPLAY_NAME']);
                        $('#idCardNo').html(msg['ID_CARD_NO']);
                        $('#unitName').html(msg['UNIT_NAME']);
                        $('#pingshentype').html(msg['LEIXING']);
                        $('#reviewSeries').html(msg['XILIE']);
                        $('#proposePosition').html(msg['ZIGE']);
                        $('#shenbaomajorId').html(msg['PROFESSIAL_NAME']);
                        $('#judgingName').html(msg['JUDGING_NAME']);
                    }
                }
            });


            if (data != null && data != 'null') {
                var msg = data;
                $('#displayName').html(msg['baseProposer'].displayName);
                $('#idCardNo').html(msg['baseProposer'].idCardNo);
                $('#mobilephone').html(msg['baseProposer'].mobilephone);

                if (msg.frontidcardpicurl != null) {
                    $('#myImgfrontidcardpic').attr("src", '<%=basePath%>' + '/' + msg.frontidcardpicurl);
                }
                if (msg.behindidcardpicurl != null) {
                    $('#myImgbehindidcardpic').attr("src", '<%=basePath%>' + '/' + msg.behindidcardpicurl);
                }


                $('#eduMajor').html(msg.eduMajor);
                $('#major').html(msg.major);
                // $('#nowpositionaltitles').html(msg.position);

                // var t3 = msg.positionGetTime;
                // $('#nowpositionaltitlestime').html(t3);

                var t4 = msg.startworktime;
                $('#startworktime').html(t4);
                $('#nowjob').html(msg.nowjob);

                var t5 = msg.nowjobTime;
                $('#nowjobtime').html(t5);
                $('#foreignlanguages').html(msg.foreignName);
                $('#foreignlanguageslevel').html(msg.foreignLevel);

                var t6 = msg.foreignTime;
                $('#foreignlanguagestime').html(t6);
                $('#currentpost').html(msg.currentPost);
                $('#currentpostseries').html(msg.currentPostSeries);
                $('#currentpostlevel').html(msg.currentPostLevel);

                var t7 = msg.currentPostGetTime;
                $('#currentpostgettime').html(t7);
                var t8 = msg.currentPostEngageTime;
                $('#currentpostengagetime').html(t8);
                $('#otherpost').html(msg.otherPost);
                $('#otherpostseries').html(msg.otherPostSeries);
                $('#otherpostlevel').html(msg.otherPostLevel);

                var t9 = msg.otherPostEngageTime;
                $('#otherpostengagetime').html(t9);
                $('#administrativepost').html(msg.administrativePost);
                var t10 = msg.administrativePostTime;
                $('#administrativeposttime').html(t10);
                $('#socialpost').html(msg.socialPost);
                // $('#socialpostphone').html(msg.socialPostPhone);
                $('#yearsone').html(msg.yearsOne);
                $('#yearsoneinfo').html(msg.yearsOneInfoName);
                $('#yearstwo').html(msg.yearsTwo);
                $('#yearstwoinfo').html(msg.yearsTwoInfoName);
                $('#yearsthree').html(msg.yearsThree);
                $('#yearsthreeinfo').html(msg.yearsThreeInfoName);
                $('#yearsfour').html(msg.yearsFour);
                $('#yearsfourinfo').html(msg.yearsFourInfoName);
                $('#yearsfive').html(msg.yearsFive);
                $('#yearsfiveinfo').html(msg.yearsFiveInfoName);
                $.ajax({
                    url: me.actionUrlJudging + 'getDict.do',
                    type: "POST",
                    data: {"groupName": "SEX"},
                    async: false,
                    success: function (data1) {
                        var msg1 = data1;
                        $(msg1).each(function (index, item) {
                            var name = item['dictName'];
                            var code = item['dictCode'];
                            if (code == msg.sex) {
                                $('#sex').html(name);
                            } else {

                            }

                        })
                    }
                });
                //政治面貌
                $.ajax({
                    url: me.actionUrlJudging + 'getDict.do',
                    type: "POST",
                    data: {"groupName": "POLITICAL_STATUS"},
                    async: false,
                    success: function (data1) {
                        var msg1 = data1;
                        $(msg1).each(function (index, item) {
                            var name = item['dictName'];
                            var code = item['dictCode'];
                            if (code == msg.politicalOutlook) {
                                $('#politicalOutlook').html(name);
                            } else {
                            }

                        })
                    }
                });
                //民族
                $.ajax({
                    url: me.actionUrlJudging + 'getDict.do',
                    type: "POST",
                    data: {"groupName": "NATION"},
                    async: false,
                    success: function (data1) {
                        var msg1 = data1;
                        $(msg1).each(function (index, item) {
                            var name = item['dictName'];
                            var code = item['dictCode'];
                            if (code == msg.nation) {
                                $('#nation').html(name);
                            } else {

                            }

                        })
                    }
                });
                //学历
                $.ajax({
                    url: me.actionUrlJudging + 'getDict.do',
                    type: "POST",
                    data: {"groupName": "EDU_EDU"},
                    async: false,
                    success: function (data1) {
                        var msg1 = data1;
                        $(msg1).each(function (index, item) {
                            var name = item['dictName'];
                            var code = item['dictCode'];
                            if (code == msg.eduEdu) {
                                $('#eduEdu').html(name);
                            } else {

                            }

                        })
                    }
                });
                //学位
                $.ajax({
                    url: me.actionUrlJudging + 'getDict.do',
                    type: "POST",
                    data: {"groupName": "DEGREE"},
                    async: false,
                    success: function (data1) {
                        var msg1 = data1;
                        $(msg1).each(function (index, item) {
                            var name = item['dictName'];
                            var code = item['dictCode'];
                            if (code == msg.eduDegree) {
                                $('#eduDegree').html(name);
                            } else {

                            }

                        })
                    }
                });

                if (msg.userpicurl != null) {
                    $('#myImguserpic').attr("src", '<%=basePath%>' + '/' + msg.userpicurl);
                }
            }
        }

        //初始学历
        function initTableEdu(data) {
            $('#cusTableEdu').datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'starttime',
                        title: '起止时间',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var s = row['starttime'];
                            var e = row['endtime'];
                            strHtml = s + "——" + e;
                            return strHtml;
                        }
                    }, {
                        field: 'schoolname',
                        title: '学校'
                    }, {
                        field: 'job',
                        title: '职务'
                    }, {
                        field: 'reference',
                        title: '证明人'
                    },
                    {
                        field: 'major',
                        title: '所学专业'
                    },
                    {
                        field: 'dictName1',
                        title: '学历'
                    },
                    {
                        field: 'dictName',
                        title: '学位'
                    },

                    {
                        field: 'fulltimetype',
                        title: '是否全日制',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['fulltimetype'];
                            switch (f) {
                                case 0:
                                    strHtml = "非全日制";
                                    break;
                                case 1:
                                    strHtml = "全日制";
                                    break;

                                default:
                                    null;
                            }
                            return strHtml;
                        }

                    },
                    {
                        field: 'extrainfo',
                        title: '备注',
                        align: 'center',
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
                        field: 'backup1',
                        title: '学历证书编号'
                    }, {
                        field: 'backup2',
                        title: '学位证书编号'
                    }, {
                        field: 'educationfile',
                        title: '学历证书文件(可预览)',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var educationfiles = row["educationfile"];
                            educationfiles.forEach(function (val, ind, arr) {
                                if (val['filetype'] == "1") {
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
                                }
                            });
                            return strHtml;
                        }

                    }, {
                        field: 'educationfile2',
                        title: '学位证书文件(可预览)',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var educationfiles = row["educationfile"];
                            educationfiles.forEach(function (val, ind, arr) {
                                if (val['filetype'] == "2") {
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
                                }
                            });
                            return strHtml;
                        }

                    },]],
            });
        }

        //初始工作经历
        function initTableWork(data) {
            $('#cusTableWork').datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'starttime',
                        title: '起止时间',
                        width: '21%',
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

        //初始培训经历
        function initTableTra(data) {
            $('#cusTableTra').datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'starttime',
                        width: '21%',
                        title: '起止时间',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var s = row['starttime'];
                            var e = row['endtime'];
                            strHtml = s + "——" + e;
                            return strHtml;
                        }
                    }, {
                        field: 'place',
                        width: '14%',
                        title: '学习地点'
                    }, {
                        field: 'content',
                        width: '20%',
                        title: '培训名称'
                    }, {
                        field: 'reference',
                        width: '23%',
                        title: '证明人'
                    }, {
                        field: 'trainFile',
                        title: '上传文件(可预览)',
                        width: '23.9%',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var trainFiles = row["trainFile"];
                            trainFiles.forEach(function (val, ind, arr) {


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
        function initTableAwa(data) {
            $('#cusTableAwa').datagrid({
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

        //初始项目/课题
        function initTablePro(data) {
            $('#cusTablePro').datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'projectname',
                        width: '9%',
                        title: '项目名称'
                    }, {
                        field: 'projectcompany',
                        width: '9%',
                        title: '立项单位'
                    }, {
                        field: 'projectstarttime',
                        width: '8.5%',
                        title: '立项时间'
                    }, {
                        field: 'projectacceptancetime',
                        width: '9%',
                        title: '认证/验收时间'
                    }, {
                        field: 'projectrole',
                        width: '9%',
                        title: '担任角色',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['projectrole'];
                            switch (f) {
                                case 0:
                                    strHtml = "项目负责人";
                                    break;
                                case 1:
                                    strHtml = "子项负责人";
                                    break;
                                case 2:
                                    strHtml = "主参与";
                                    break;
                                case 3:
                                    strHtml = "一般参与";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    }, {
                        field: 'projectmoney',
                        width: '9%',
                        title: '经费/万元'
                    }, {
                        field: 'acceptancetype',
                        width: '9%',
                        title: '认证/验收级别',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['acceptancetype'];
                            switch (f) {
                                case '0':
                                    strHtml = "国家";
                                    break;
                                case '1':
                                    strHtml = "省部";
                                    break;
                                case '2':
                                    strHtml = "厅局";
                                    break;
                                case '3':
                                    strHtml = "本市";
                                    break;
                                case '4':
                                    strHtml = "其他";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }

                    }, {
                        field: 'conclusiontype',
                        width: '9%',
                        title: '认证/验收结论',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['conclusiontype'];
                            switch (f) {
                                case '0':
                                    strHtml = "国际领先";
                                    break;
                                case '1':
                                    strHtml = "国际先进";
                                    break;
                                case '2':
                                    strHtml = "国内领先";
                                    break;
                                case '3':
                                    strHtml = "国内先进";
                                    break;
                                case '4':
                                    strHtml = "验收通过";
                                    break;
                                case '5':
                                    strHtml = "其他";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }

                    }, {
                        field: 'paiming',
                        width: '9%',
                        title: '获奖情况(排名)'

                    }, {
                        field: 'headman',
                        width: '9%',
                        title: '项目负责人'

                    }, {
                        field: 'projectfile',
                        width: '11.6%',
                        title: '上传文件(可预览)',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var projectfiles = row["projectfile"];
                            projectfiles.forEach(function (val, ind, arr) {


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

        //初始专利
        function initTablePat(data) {
            $('#cusTablePat').datagrid({
                idField: me.idFiled,
                data: data,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'patentname',
                        width: '12%',
                        title: '专利名称'
                    }, {
                        field: 'patenttype',
                        width: '11%',
                        title: '专利类别',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['patenttype'];
                            switch (f) {
                                case 0:
                                    strHtml = "发明专利";
                                    break;
                                case 1:
                                    strHtml = "外观专利";
                                    break;
                                case 2:
                                    strHtml = "实用新型专利";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    },
                    {
                        field: 'patentnumber',
                        width: '13%',
                        title: '专利编号'
                    }, {
                        field: 'patentcompletetime',
                        width: '20%',
                        title: '专利授予日期'

                    }, {
                        field: 'ascriptiontype',
                        width: '11%',
                        title: '专利归属',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['ascriptiontype'];
                            switch (f) {
                                case 0:
                                    strHtml = "本人";
                                    break;
                                case 1:
                                    strHtml = "单位";
                                    break;
                                case 2:
                                    strHtml = "共有";
                                    break;
                                case 3:
                                    strHtml = "无";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    },
                    {
                        field: 'paiming',
                        width: '11%',
                        title: '排名'

                    }, {
                        field: 'patentfile',
                        width: '23%',
                        title: '上传文件(可预览)',
                        formatter: function (value, row, index) {

                            var strHtml = "";
                            var patentfiles = row["patentfile"];
                            patentfiles.forEach(function (val, ind, arr) {


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
        function initTablePap(data) {
            $('#cusTablePap').datagrid({
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
        function initTableBook(data) {
            $('#cusTableBook').datagrid({
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
        function initTableRes(data) {
            $('#cusTableRes').datagrid({
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

        //初始个人总结
        function initSumm(data) {
            if (data != null) {
                $('#summarytext').html(data['summary']);
            }
        }

        //初始审批纪录
        function initTableJudLog(id) {
            $('#cusTableJudLog').datagrid({
                idField: me.idFiled,
                url: me.actionUrlJudging + 'getProposerJudLog.do?userid=' + id,
                pagination: false,//取消分页
                fit: false,//高度自适应
                columns: [[
                    {
                        field: 'JUD_STAGE',
                        width: '20%',
                        title: '审批阶段',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['JUD_STAGE'];
                            switch (f) {
                                case '1':
                                    strHtml = "基层单位经办人";
                                    break;
                                case '2':
                                    strHtml = "基层单位领导";
                                    break;
                                case '3':
                                    strHtml = "下属单位经办人";
                                    break;
                                case '4':
                                    strHtml = "下属单位领导";
                                    break;
                                case '5':
                                    strHtml = "单位经办人";
                                    break;
                                case '6':
                                    strHtml = "单位领导";
                                    break;
                                case '7':
                                    strHtml = "主管部门经办人";
                                    break;
                                case '8':
                                    strHtml = "主管部门领导";
                                    break;
                                case '9':
                                    strHtml = "县区职改办经办人";
                                    break;
                                case '10':
                                    strHtml = "县区职改办领导";
                                    break;
                                case '11':
                                    strHtml = "市职改办经办人";
                                    break;
                                case '12':
                                    strHtml = "市职改办领导";
                                    break;
                                case '13':
                                    strHtml = "省职改办经办人";
                                    break;
                                case '14':
                                    strHtml = "省职改办领导";
                                    break;
                                case '15':
                                    strHtml = "参评人员审查";
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    }, {
                        field: 'DISPLAY_NAME',
                        width: '19%',
                        title: '审批人'
                    }, {
                        field: 'PRO_RESULT',
                        width: '20%',
                        title: '审批结果',
                        formatter: function (value, row, index) {
                            var strHtml = "";
                            var f = row['PRO_RESULT'];
                            switch (f) {
                                case '0':
                                    strHtml = "未通过";
                                    break;
                                case '1':
                                    strHtml = "通过";
                                    break;
                                case '2':
                                    strHtml = "退回修改";
                                    break;
                                case '4':
                                    strHtml='加入黑名单';
                                    break;
                                default:
                                    null;
                            }
                            return strHtml;
                        }
                    },
                    {
                        field: 'PRO_VIEW',
                        width: '20%',
                        title: '审批意见'
                    },
                    {
                        field: 'JUD_DATE',
                        width: '22%',
                        title: '审批日期'
                    },
                ]],
            });
        }

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

        //查看图片
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
        //初始化年度
        function initYearNo(){
            $.ajax({
                url: '<%=basePath%>/RecFileManager/getYear.do',
                dataType: 'json',
                success: function (jsonstr) {
                    jsonstr.push({'yearCode': '', 'yearText': '请选择..'});
                    $("#nian").combobox({  //为下拉框赋值
                        data: jsonstr,
                        valueField: 'yearCode',
                        textField: 'yearText',
                        onLoadSuccess:function (data) {
                            $('#nian').combobox("setValue",data[0].yearCode);
                        }
                    });
                }
            });

        }
    </script>
</head>
<body class="easyui-layout">
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div title="数据维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 650px;height: 400px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                </div>
                <table cellpadding="5" align="center">
                    <tr>
                        <td>
                            登录名：
                        </td>
                        <td colspan="3">
                            <input id="loginName" name="loginName" class="easyui-textbox" data-options="required:true"
                                   style="width:400px" maxlength="30"/>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            基层单位：
                        </td>
                        <td colspan="3">
                            <input data-options="required:true"
                                   style="width:400px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            显示名：
                        </td>
                        <td>
                            <input id="displayName2" name="displayName2" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true,validType:'chinese'"/>
                        </td>

                        <td>
                            手机号：
                        </td>
                        <td>
                            <input id="mobilephone2" name="mobilephone2" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'mobile'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            身份证号：
                        </td>
                        <td>
                            <input id="idCardNo2" name="idCardNo2" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'idcard'"/>
                        </td>
                        <td>
                            电子邮箱：
                        </td>
                        <td>
                            <input id="email2" name="email2" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'email'"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            账号状态：
                        </td>
                        <td align="left">
                            <input type="checkbox" id="state" name="state" value="1" checked="checked"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="backup1" name="backup1" style="width:380px;height:60px" class="easyui-textbox"
                                   multiline="true">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">确定</a>
            <a id="btn_edit_cancel" icon="icon-cancel" href="javascript:void(0)">关闭</a>
        </div>
    </div>
</div>
<div title="查询窗口" id="search_window" class="easyui-window" closed="true"
     style="width: 450px;height: 300px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            姓名：
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_search_ok" icon="icon-ok">确定</a>
            <a href="javascript:void(0)" id="btn_search_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>
<div title="审批信息窗口" id="edit_window99" class="easyui-window" closed="true"
     style="width: 98%;height: 95%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form99" name="edit_form99" method="post">
                <div id="basemsg">
                    <div class="col-lg-12">
                        <div class="title">个人信息</div>
                        <div id="defaultFormbasemsg" method="post" class="form-horizontal">
                            <table class="mytable">
                                <tbody>
                                <tr class="xinxi">
                                    <td>姓名:<span id="displayName"></span></td>
                                    <td class="nothasinput">性别:<span id="sex"></span></td>
                                    <td class="nothasinput">民族：<span id="nation"></span></td>
                                    </td>
                                    <td>身份证号:<span id="idCardNo"></span></td>
                                </tr>
                                <tr class="xinxi">
                                    <td>申报方式:<span id="pingshentype"></span></td>
                                    <td>申报系列:<span id="reviewSeries"></span></td>
                                    <td>申报资格:<span id="proposePosition"></span></td>
                                    <td>申报专业:<span id="shenbaomajorId"></span></td>


                                </tr>
                                <tr class="xinxi">
                                    <td>单位:<span id="unitName"></span></td>
                                    <td class="nothasinput">政治面貌:<span id="politicalOutlook"></span></td>

                                    <td class="nothasinput" style="height: 42px!important;">手机号:<span
                                            id="mobilephone"></span></td>
                                    <td class="weiyi nothasinput">预分配评委会:<span id="judgingName"></span></td>
                                </tr>


                                <img class="imgbig zhaopian" id="myImguserpic"/>


                                </tr>

                                <tr class="jiange2">

                                </tr>
                                <tr class="jiange">
                                    <td class="hasinput form-group">学历:</td>
                                    <td class="hasinput form-group" style="color: #808080;font-weight: normal"
                                        id="eduEdu" name="eduEdu">

                                    </td>
                                    <td class="hasinput form-group">学位：</td>
                                    <td class="hasinput form-group" style="color: #808080;font-weight: normal"
                                        id="eduDegree" name="eduDegree">

                                    </td>
                                    <td class="hasinput form-group">专业：</td>
                                    <td class="hasinput form-group" style="color: #808080;font-weight: normal"
                                        name="eduMajor"
                                        id="eduMajor"></td>

                                </tr>
                                <tr class="jiange2">

                                </tr>
                                <tr class="jiange">
                                    <td class="nothasinput">从事专业：</td>
                                    <td class="hasinput form-group"><span name="major"
                                                                          id="major"></span></td>
                                    <%--<td class="nothasinput">现有职称：</td>
                                    <td class="hasinput form-group"><span
                                            name="nowpositionaltitles"
                                            id="nowpositionaltitles"></span></td>
                                    <td class="nothasinput">现有职称取得时间：</td>
                                    <td class="hasinput form-group"><span
                                            name="nowpositionaltitlestime"
                                            id="nowpositionaltitlestime"></span>
                                    </td>--%>
                                </tr>
                                <tr class="jiange">
                                    <td class="nothasinput">开始工作时间：</td>
                                    <td class="hasinput form-group"><span
                                            name="startworktime" id="startworktime"
                                    ></span></td>
                                    <td class="nothasinput">现从事事业：</td>
                                    <td class="hasinput form-group">
                                        <span name="nowjob"
                                              id="nowjob"></span>
                                    </td>
                                    <td class="nothasinput">现从事事业时间：</td>
                                    <td class="hasinput form-group"><span
                                            name="nowjobtime" id="nowjobtime"
                                    ></span></td>
                                </tr>

                                <tr class="jiange">
                                    <td class="nothasinput">掌握外语：</td>
                                    <td class="hasinput form-group"><span
                                            name="foreignlanguages" id="foreignlanguages"
                                    ></span>
                                    </td>
                                    <td class="nothasinput">掌握外语等级：</td>
                                    <td class="hasinput form-group"><span
                                            name="foreignlanguageslevel"
                                            id="foreignlanguageslevel"></span>
                                    </td>
                                    <td class="nothasinput">外语证书取得时间：</td>
                                    <td class="hasinput form-group"><span
                                            name="foreignlanguagestime"
                                            id="foreignlanguagestime"></span>
                                    </td>
                                </tr>
                                <tr class="jiange2">

                                </tr>
                                <tr>
                                    <td rowspan="2" class="nothasinput jiange1">现任专业技术职务：</td>
                                    <td>职务：</td>
                                    <td>系列：</td>
                                    <td>级别：</td>
                                    <td>取得时间：</td>
                                    <td>聘任时间：</td>
                                </tr>
                                <tr>
                                    <td class="hasinput form-group"><span
                                            name="currentpost" id="currentpost"
                                    ></span></td>
                                    <td class="hasinput form-group"><span
                                            name="currentpostseries"
                                            id="currentpostseries"></span></td>
                                    <td class="hasinput form-group"><span
                                            name="currentpostlevel" id="currentpostlevel"
                                    ></span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="currentpostgettime"
                                            id="currentpostgettime"></span></td>
                                    <td class="hasinput form-group"><span
                                            name="currentpostengagetime"
                                            id="currentpostengagetime"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="2" class="nothasinput jiange1">其他专业技术职务：</td>
                                    <td>职务：</td>
                                    <td>系列：</td>
                                    <td>级别：</td>
                                    <td colspan="2">聘任时间：</td>
                                </tr>
                                <tr>
                                    <td class="hasinput form-group"><span name="otherpost"
                                                                          id="otherpost"></span></td>
                                    <td class="hasinput form-group"><span
                                            name="otherpostseries" id="otherpostseries"
                                    ></span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="otherpostlevel" id="otherpostlevel"
                                    ></span></td>
                                    <td class="hasinput form-group" colspan="2"><span
                                            name="otherpostengagetime"
                                            id="otherpostengagetime"
                                    ></span></td>
                                </tr>

                                <tr>
                                    <td rowspan="2" class="nothasinput jiange1">兼任行政职务：</td>
                                    <td>职务：</td>
                                    <td class="hasinput form-group"><span
                                            name="administrativepost"
                                            id="administrativepost"></span></td>

                                    <td rowspan="2" class="nothasinput">担任学术团队职务或社会兼职：</td>
                                    <td rowspan="2" >职务/兼职：</td>
                                    <td rowspan="2" class="hasinput form-group"><span
                                            name="socialpost" id="socialpost"
                                    ></span></td>


                                </tr>
                                <tr>
                                    <td>时间：</td>
                                    <td class="hasinput form-group"><span
                                            name="administrativeposttime"
                                            id="administrativeposttime"></span>
                                    </td>


                                    <%--<td>个人联系方式：</td>
                                    <td class="hasinput form-group"><span
                                            name="socialpostphone" id="socialpostphone"
                                    ></span>
                                    </td>--%>
                                </tr>
                                <tr class="jiange2">

                                </tr>
                                <tr class="jiange">
                                    <td colspan="6" style="text-align: center">任现职近5年来年度考核情况</td>
                                </tr>
                                <tr class="jiange">
                                    <td class="nothasinput">年份：</td>
                                    <td class="hasinput form-group"><span
                                            name="yearsone"
                                            id="yearsone"></span><span>年</span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="yearstwo"
                                            id="yearstwo"></span><span>年</span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="yearsthree" id="yearsthree"
                                    ></span><span>年</span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="yearsfour"
                                            id="yearsfour"></span><span>年</span>
                                    </td>
                                    <td class="hasinput form-group"><span
                                            name="yearsfive"
                                            id="yearsfive"></span><span>年</span>
                                    </td>
                                </tr>
                                <tr class="jiange">
                                    <td class="nothasinput">情况：</td>
                                    <td class="hasinput form-group"><span
                                            name="yearsoneinfo" id="yearsoneinfo"
                                    ></span></td>
                                    <td class="hasinput form-group"><span
                                            name="yearstwoinfo" id="yearstwoinfo"
                                    ></span></td>
                                    <td class="hasinput form-group"><span
                                            name="yearsthreeinfo" id="yearsthreeinfo"
                                    ></span></td>
                                    <td class="hasinput form-group"><span
                                            name="yearsfourinfo" id="yearsfourinfo"
                                    ></span></td>
                                    <td class="hasinput form-group"><span
                                            name="yearsfiveinfo" id="yearsfiveinfo"
                                    ></span></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div id="edu">
                    <div id="toolbarEdu" class="btn-group-sm">
                        <div class="title">学习经历</div>
                    </div>
                    <table class="biaoge" id="cusTableEdu"></table>
                </div>

                <div id="work" class="table-x-hidden">
                    <div id="toolbarWork" class="btn-group-sm">
                        <div class="title">工作经历</div>
                    </div>
                    <table id="cusTableWork" class="table"></table>
                </div>

                <div id="train" class="table-x-hidden">
                    <div id="toolbarTra" class="btn-group-sm">
                        <div class="title">培训经历</div>
                    </div>
                    <table id="cusTableTra" class="table">

                    </table>
                </div>
                <div id="award" class="table-x-hidden">
                    <div id="toolbarAwa" class="btn-group-sm">
                        <div class="title">奖励</div>
                    </div>
                    <table id="cusTableAwa" class="table">
                    </table>
                </div>

                <div id="patent" class="table-x-hidden">
                    <div id="toolbarPat" class="btn-group-sm">
                        <div class="title">专利</div>
                    </div>
                    <table id="cusTablePat" class="table">
                    </table>
                </div>

                <div id="project" class="table-x-hidden">
                    <div id="toolbarPro" class="btn-group-sm">
                        <div class="title">项目/课题</div>
                    </div>
                    <table id="cusTablePro" class="table">
                    </table>
                </div>


                <div id="paper" class="table-x-hidden">
                    <div id="toolbarPap" class="btn-group-sm">
                        <div class="title">论文</div>
                    </div>
                    <table id="cusTablePap" class="table">
                    </table>
                </div>
                <div id="book" class="table-x-hidden">
                    <div id="toolbarBook" class="btn-group-sm">
                        <div class="title">著作</div>
                    </div>
                    <table id="cusTableBook" class="table">
                    </table>
                </div>

                <div id="research" class="table-x-hidden">
                    <div id="toolbarRes" class="btn-group-sm">
                        <div class="title">其他成果</div>
                    </div>
                    <table id="cusTableRes" class="table">
                    </table>
                </div>

                <div id="summary">
                    <div class="title">个人总结内容</div>
                    <div name="summarytext" id="summarytext"
                         style="border: solid 1px #dddddd;padding-left: 10px;line-height: 37px"></div>
                </div>

                <div id="judLog">
                    <div id="toolbarJudLog" class="btn-group-sm">
                        <div class="title">审批纪录</div>
                    </div>
                    <div class="easyui-panel" title="审批纪录明细"
                         style="width: 100%;border: solid 1px #dddddd;padding-left: 10px;line-height: 37px"
                         collapsible="true" collapsed="true">
                        <table id="cusTableJudLog" class="table">
                        </table>
                    </div>
                </div>


            </form>
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>
</body>
</html>
