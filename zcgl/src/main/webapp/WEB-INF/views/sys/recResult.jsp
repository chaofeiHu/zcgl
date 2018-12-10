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
    <title>评审结果管理</title>
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
            actionUrl: '<%=basePath%>/RecResult/'
        };

        $(function () {
            pageInit();
            loadTree();
            initYearNo();
            loadGrid();
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

                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                $("#judgingName").combobox('clear');
                $("#yearNo").combobox('clear');
                $("#userName").textbox().textbox('setValue','');
                $("#reviewSeries").combobox('clear');
                $("#professialCode").combobox('clear');
                $("#areaCode").combobox('clear');
                $('#professialCode').combobox({ disabled: true });
            });
        }

        //加载列表
        function loadGrid() {
            //var judgingCode = $("#judgingName").textbox().textbox('getValue');
            var judgingCode = $("#judgingName").val();
            var yearNo = $("#yearNo").val();
            var userName = $("#userName").textbox().textbox('getValue');
            var reviewSeries = $("#reviewSeries").val();
            var areaCode = $("#areaCode").val();
            var professialCode = $("#professialCode").val();
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
                checkOnSelect:true,
                singleSelect: false,
                selectOnCheck: true,
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
                        /*formatter: function (value, rowData, rowIndex) {
                            if(value == 1|| value == "1") return "男";
                            else if(value == 2|| value == "2") return "女";
                            else return "未说明性别";
                        }*/},
                    {field: 'idCardNo', title: '身份证号', width: 180, align: 'center',},
                    {field: 'judgingCode', title: '评委会名称', width: 200, align: 'center',},
                    {field: 'groupId', title: '专业组', width: 200, align: 'center',},
                    {field: 'reviewSeries', title: '申报系列', width: 100, align: 'center',},
                    {field: 'titleLevel', title: '申报级别', width: 100, align: 'center',},
                    {field: 'positionalTitles', title: '申报职称', width: 100, align: 'center',},
                    {field: 'professialCode', title: '申报专业', width: 100, align: 'center',},
                    {field: 'reviewType', title: '评审类型', width: 100, align: 'center',},
                    {field: 'gettime', title: '取得资格时间', width: 100, align: 'center',
                        formatter:function (value,row,index) {
                           if (value != null && value.length != 0){

                                var date = new Date(value);

                                return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
                           }
                        }
                    },
                    {field: 'getway', title: '取得资格方式', width: 100, align: 'center',},
                    {field: 'back1', title: '备注', width: 100, align: 'center',},
                ]],
                toolbar: [
                    {
                        text: '导出评审结果', iconCls: 'icon-excel', handler: function () {
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
                    var date = new Date();
                    var value = date.getFullYear();
                    //$("#yearNo").combobox('setValue',value);
                    param.judgingId = me.edit_form.find('#judgingId').val();
                    param.id = me.edit_form.find('#id').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },

            });

        }


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

        //初始化年度
        function initYearNo(){
            $("#yearNo").combobox({
                url:'<%=basePath%>/RecFileManager/getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onLoadSuccess:function (data) {
                    $('#yearNo').combobox("setValue",data[0].yearCode);
                    //console.log(data[0]);
                }
            });
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
<div data-options="region:'north'" style="height:73px">
    <table>
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
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
    <form id="importForm" enctype="multipart/form-data" method="post">
        <input type="file" name="file" id="file" multiple="false" style="display:none;"
               accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
    </form>

</div>

<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>

</body>
</html>
