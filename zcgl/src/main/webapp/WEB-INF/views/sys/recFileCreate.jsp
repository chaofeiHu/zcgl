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
    <title>生成任职资格文件</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script type="text/javascript">

    </script>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'userName',
            actionUrl: '<%=basePath%>/RecFileManager/'
        };

        $(function () {
            pageInit();
            loadGrid();
            loadTree();
            initYearNo();
            area();
            //getDate();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            var date = new Date();
            var value = date.getFullYear();
            $("#yearNo").combobox('setValue',value);
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_search_ok').linkbutton().click(function () {
                loadGrid();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                $("#addtime").val();
                $("#fileCode").textbox().textbox('setValue','');
                $("#fileTitle").textbox().textbox('setValue','');
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                $("#judgingName").combobox('clear');
                $("#yearNo").combobox('clear');
                $("#userName").textbox().textbox('setValue','');
                $("#reviewSeries").combobox('clear');
                $("#professialCode").combobox('clear');
                $('#professialCode').combobox({ disabled: true });
                $("#areaCode").combobox('clear');
            });
        }

        //加载列表
        function loadGrid() {
            //var judgingCode = $("#judgingName").textbox().textbox('getValue');
            var judgingCode = $("#judgingName").val();
            var yearNo = $("#yearNo").val();
            var userName = $("#userName").textbox().textbox('getValue');
            var reviewSeries = $("#reviewSeries").val();
            var professialCode = $("#professialCode").val();
            var areaCode = $("#areaCode").val();
            me.datagrid1.datagrid({
                idField: me.idFiled,
                queryParams: {
                    judgingCode: judgingCode,
                    yearNo: yearNo,
                    userName: userName,
                    professialCode: professialCode,
                    reviewSeries: reviewSeries,
                    areaCode: areaCode,
                },
                rownumbers: true,
                fit: true,
                checkOnSelect: true,
                singleSelect: false,
                selectOnCheck: true,
                pageNumber: 1,
                pagination: true,
                showHeader:true,
                showFooter:true,
                url: me.actionUrl + 'getList.do',
                frozenColumns: [[
                    {field: 'id', checkbox:true, align: 'center',},
                    {field: 'yearNo', title: '年度', width: 100, align: 'center',},
                    {field: 'areaCode', title: '地市', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if(value == "河南省") return "省直";
                            else if(rowData.back2 == 2|| rowData.back2 == "2") return rowData.areaCode;
                            else if(rowData.back2 == 3|| rowData.back2 == "3") return rowData.back3;
                            else return null;
                        }},
                    {field: 'unitCode', title: '主管单位名称', width: 120, align: 'center',},
                    {field: 'userName', title: '姓名', width: 100, align: 'center',},
                ]],
                columns: [[
                    {field: 'sex', title: '性别', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if(value == 1|| value == "1") return "男";
                            else if(value == 2|| value == "2") return "女";
                            else return "未说明性别";
                        }},
                    {field: 'idCardNo', title: '身份证号', width: 200, align: 'center',},
                    {field: 'judgingCode', title: '评委会名称', width: 200, align: 'center',},
                    {field: 'groupId', title: '专业组', width: 200, align: 'center',},
                    {field: 'reviewSeries', title: '申报系列', width: 100, align: 'center',},
                    {field: 'titleLevel', title: '申报级别', width: 100, align: 'center',},
                    {field: 'positionalTitles', title: '申报职称', width: 100, align: 'center',},
                    {field: 'professialCode', title: '申报专业', width: 100, align: 'center',},
                    {field: 'reviewType', title: '评审类型', width: 100, align: 'center',},
                    {field: 'gettime', title: '取得资格时间', width: 120, align: 'center',
                        formatter:function (value,row,index) {
                            if(value != null && value.length != 0) {
                                var date = new Date(value);
                                return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
                            }
                        }
                    },
                    {field: 'getway', title: '取得资格方式', width: 120, align: 'center',},
                    /*{field: 'opt', title: '操作', width: 300, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0)"   title="确认审核" onClick="confirmResult(\'' + rowData['id'] +'\');" style="padding-left:20px;" >审核确认</a>'
                            strReturn += '<a href="javascript:void(0)"   title="转异议人员库" onClick="toDissent(\'' + rowData['id'] +'\');" style="padding-left:20px;" >转异议人员库</a>'
                            return strReturn;
                        }
                    },*/
                    {field: 'back1', title: '备注', width: 100, align: 'center',},
                ]],
                toolbar: [
                    {
                        text: '录入发文信息', iconCls: 'icon-save', handler: function () {
                        var selectedRows = me.datagrid1.datagrid('getSelections');
                        var ids = [];
                        if (selectedRows.length == 0) {
                            showError('请选择至少一条数据进行操作!');
                        } else {
                            for (var i = 0; i < selectedRows.length; i++) {
                                console.log(selectedRows[i]);
                                ids.push(selectedRows[i][me.idFiled]);
                            }
                            //弹开窗口 初始化
                            $("#addtime").val();
                            $("#fileCode").textbox().textbox('setValue','');
                            $("#fileTitle").textbox().textbox('setValue','');
                            getDate();
                            $("#ids").val(ids.join(','));
                            me.edit_window.window('open');
                        }
                    }
                    }, '-',
                    /*{
                        text: '转异议人员库', iconCls: 'icon-remove', handler: function () {
                        var selectedRows = me.datagrid1.datagrid('getSelections');
                        var ids = [];
                        if (selectedRows.length == 0) {
                            showError('请选择一条记录进行操作!');
                        } else {
                            for (var i = 0; i < selectedRows.length; i++) {
                                console.log(selectedRows[i]);
                                ids.push(selectedRows[i][me.idFiled]);
                            }
                            toDissent(ids.join(','));
                        }
                    }
                    }, '-',*/
                    {
                        text: '导出当前待发文人员列表', iconCls: 'icon-excel', handler: function () {
                        var page=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageNumber;
                        var rows=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageSize;
                        var val='?judgingCode='+$("#judgingName").val()+'&yearNo='+$("#yearNo").val()+'&areaCode='+$("#areaCode").val()+
                            '&userName='+$("#userName").textbox().textbox('getValue')+'&reviewSeries='+$("#reviewSeries").val()
                            +'&professialCode='+$("#professialCode").val()+'&page='+page+'&rows='+rows;
                            window.location.href = me.actionUrl+'exportExcelReviewResult.do'+val;
                        }
                    }
                ],
                onBeforeLoad: function (param) {
                    param.judgingId = me.edit_form.find('#judgingId').val();
                    param.id = me.edit_form.find('#id').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },

            });

        }

        //确定发文
        function SaveData() {
            $.messager.confirm('提示信息', '是否确认发文？', function (isClickedOk) {
                if (me.edit_form.form('validate')) {
                    $("#edit_window").window("close");
                    $.ajax({
                        url: me.actionUrl + 'confirmSendFile.do',
                        data: me.edit_form.serialize(),
                        beforeSend:ajaxLoading,
                        success: function (returnData) {
                            ajaxLoadEnd();
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);

                                } else {
                                    showError(returnData.message);
                                }
                            }
                            me.datagrid1.datagrid('reload');

                        }
                    });
                }
            })
        }

       /*
        //转异议人员库
        function toDissent(ids) {
            $.messager.confirm('提示信息', '是否确认转入异议人员库？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'toDissent.do',
                        data: {ids: ids},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo("转入异议人员库成功");

                                } else {
                                    showError("转入异议人员库失败");
                                }
                            }
                            me.datagrid1.datagrid('reload');
                        }
                    });
                }
            })
        }*/

        //加载评委会下拉列表
        function loadTree() {
            $('#judgingName').combobox({
                url:'<%=basePath%>/Speciality/getJudgingList.do',
                valueField:'id',
                textField:'judgingName',
                onSelect:function(data){
                    $.ajax({
                        url:'<%=basePath%>/Judging/getReviewSeries.do',
                        data: {judgingCode: data.judgingCode},
                        success: function (returnData) {
                        $('#reviewSeries').combobox('setValue',returnData.reviewSeries);
                        console.log(returnData);
                        var url = '<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SERIES';
                        $('#reviewSeries').combobox('reload', url);
                        }
                    })
                }
            });
        }
        //时间插件
        function getDate() {
            $('#addtime').datetimebox("setValue",$('#addtime').datetimebox('options').currentText);
            /*$('#addtime').datebox({
                currentText:true,
            });*/
        }

        //初始化年度
        function initYearNo(){
            $("#yearNo").combobox({
                url:me.actionUrl + 'getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onLoadSuccess:function (data) {
                    $("#yearNo").combobox("setValue", data[0].yearCode);
                }
            });
        }

        //采用jquery easyui loading css效果
        function ajaxLoading(){
            $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
        }
        function ajaxLoadEnd(){
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }

        //地市下拉框
        function area() {
            $("#areaCode").combobox({
                valueField:'areaCode',
                textField:'areaName',
                url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2&areaCode=410000',
                formatter: function(row){
                    if(row.areaCode =='410000')
                        row.areaName = '省直'
                    var opts = $(this).combobox('options');
                    //console.log(row[opts.textField]);
                    return row[opts.textField];
                },
            });
        }
    </script>
</head>
<body class="easyui-layout">
<div title="选择待发文人员" data-options="region:'north'" style="height:120px">
    <table cellspacing="8px">
        <tr>
            <td>评委会名称：</td>
            <td><input name="judgingName" id="judgingName" class="easyui-combobox" style="width:200px"/></td>
            <td>姓名：</td>
            <td><input name="userName" id="userName" class="easyui-textbox" style="width:200px"/></td>
            <td>年度：</td>
            <td><input name="yearNo" id="yearNo" class="easyui-combobox" data-options="editable:false" style="width:200px"/></td>
        </tr>
        <tr>
            <td>地市：</td>
            <td><input name="areaCode" id="areaCode" class="easyui-combobox" style="width:200px"/></td>
            <td>申报系列：</td>
            <td>
                <input id="reviewSeries" name="reviewSeries" class="easyui-combobox"
                       data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SERIES',
                                   onSelect: function(data){
                                        $('#professialCode').combobox('clear');
                                       console.log(data);
                                       var url = '<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+data.dictCode;
                                       $('#professialCode').combobox('reload', url);
                                       $('#professialCode').combobox({ disabled: false });
                                    }" style="width:200px;"
                />
            </td>
            <td>申报专业：</td>
            <td>
                <input id="professialCode" name="professialCode" class="easyui-combobox" disabled="true"
                       data-options="valueField:'professialCode',textField:'professialName',
                                                url:'<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+reviewSeries.value,
                                    "
                       style="width:200px"
                />
            </td>
            <td><a href="javascript:void(0)" id="btn_search_ok" icon="icon-search">查询</a></td>
            <td><a href="javascript:void(0)" id="btn_search_cancel" icon="icon-remove">清空条件</a></td>
        </tr>
    </table>

</div>
<div title="待发文人员列表" region="center" border="false">
    <table id="datagrid1">
    </table>
    <form id="importForm" enctype="multipart/form-data" method="post">
        <input type="file" name="file" id="file" multiple="false" style="display:none;"
               accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
    </form>

</div>

<div title="录入发文信息窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 450px;height: 250px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="ids" name="ids"/>
                </div>
                <table cellspacing="5px">
                    <tr>
                        <td>
                            文件编号：
                        </td>
                        <td>
                            <input name="fileCode" id="fileCode" class="easyui-textbox" style="width: 300px;"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            文件标题：
                        </td>
                        <td>
                            <input name="fileTitle" id="fileTitle" class="easyui-textbox" style="width: 300px;"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            发文时间：
                        </td>
                        <td>
                            <input name="addtime" id="addtime" class="easyui-datetimebox" style="width: 300px;"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_edit_ok" icon="icon-ok">发文</a>
            <a href="javascript:void(0)" id="btn_edit_cancel" icon="icon-cancel">返回</a>
        </div>
    </div>
</div>
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>

</body>
</html>
