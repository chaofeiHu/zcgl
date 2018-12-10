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
    <title>学科组/评委会投票页面</title>
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
        .easyui-validatebox { appearance: none;-webkit-appearance: none;outline: none;  margin: 0 5px 0 0; -webkit-border-radius: 50%; -moz-border-radius: 50%; -o-border-radius: 50%;border-radius: 50%;border: 1px solid #ccc; padding:4px;top: 3px;position: relative;}
        .easyui-validatebox:after { display: block;content: "";width: 8px;height: 8px;background: #fff;}
        .easyui-validatebox:checked:after { background: #00af6b;-webkit-border-radius: 50%; -moz-border-radius: 50%; -o-border-radius: 50%;border-radius: 50%;}
    </style>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'specialityName',
            actionUrl: '<%=basePath%>/SpecialityTickets/'
        };

        $(function () {
            pageInit();
            loadTree();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');


            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {

                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                $("#judgingName").combobox('clear');
                $("#specialityName1").textbox().textbox('setValue','');
                $("#administrativeDutyLevel1").combobox('clear');
                $("#professialLevel1").combobox('clear');
                $("#professialDutyLevel1").combobox('clear');
            });
        }

        //加载列表
        function loadGrid(GEOUPID) {
            var type = $("#type").val();
            me.datagrid1.datagrid({
                idField: me.idFiled,
                queryParams: {
                    type: type,
                    GEOUPID:GEOUPID
                },pagination:false,
                url: me.actionUrl + 'getList.do',
                frozenColumns: [[
                    {field: 'DISPLAY_NAME', title: '姓名', width: 100, align: 'center',formatter: function (value, row, index) {
                var strReturn = '';
                strReturn = '<span style="line-height: 28px">' +
                    '<a href="<%=basePath%>/SpecialityTickets/SpecialityProPage.do?PROPOSER_ID='+row['PROPOSER_ID']+'&Name='+value+'" target="_blank"   style="color:blue;text-decoration:underline;" title="详细信息"  ' + '>' + value + '</a></span>';
                return strReturn;
            }},
                    {field: 'UNIT_NAME', title: '推荐单位', width: 100, align: 'center',},
                    {field: 'XL', title: '申报系列', width: 100, align: 'center'},
                ]],columns:[[
                    {field: 'SQZC', title: '申报专业', width: 140, align: 'center'},
                    {field: 'PSLX', title: '评审类型', width: 140, align: 'center'},
                    {field: 'GROUP_NAME', title: '组名', width: 140, align: 'center'},
                    {field: 'TICKETS', title: '投票', width: 240, align: 'center',formatter:function(value,row,index){

                                return "<label style='margin:0 10px 0 0;text-align: center'><input type='radio' class='easyui-validatebox' name='TICKETS"+index+"' id='TICKETS"+index+"' value='1'  />通过</label>"+
                                    "<label style='margin:0 10px 0 0;text-align: center'><input type='radio' class='easyui-validatebox'name='TICKETS"+index+"' id='TICKETS"+index+"' value='2' />不通过</label>"+
                                    "<label style='margin:0 10px 0 0;text-align: center'><input type='radio' class='easyui-validatebox'name='TICKETS"+index+"' id='TICKETS"+index+"' value='3' />弃权</label>";

                        }},
                    {field: 'REMARKS', title: '备注', width: 155, align: 'center',formatter:function(value,row,index){
                            if(value==undefined) value="";
                            return "<input  style='width: 150px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' value='"+value+"'  name='REMARKS"+index+"' id='REMARKS"+index+"'>";
                        }},
                    {field: 'opt', title: '学科组意见', width: 140, align: 'center',formatter:function (value,rowData,index) {
                            return '<a href="javascript:void(0);" onclick="queren('+rowData['GROUP_ID']+','+rowData['PROPOSER_ID']+')" title="查看学科组意见" style="padding-left:20px;color:blue" >查看</a>';
                    }},
                ]],
               <%-- toolbar: [
                    {
                        text: '导出专家库', iconCls: 'icon-excel', handler: function () {
                        var page=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageNumber;
                        var rows=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageSize;
                        var val='?judgingId='+$("#judgingId").textbox().textbox("getValue")+'&specialityName1='+$('#specialityName1').val()+
                            '&page='+page+'&rows='+rows;
                        window.location.href = me.actionUrl +'exportExcelSpeciality.do'+val;
                        }
                    }
                ],--%>
               onLoadSuccess:function (data) {
                    var rosw=data.rows;
                    for(var i=0;i<rosw.length;i++){
                        if(rosw[i].TICKETS==undefined) {
                            $("input[name='TICKETS" + i + "'][value=1]").attr("checked", true);
                        }else {
                            $("input[name='TICKETS" + i + "'][value=" + rosw[i].TICKETS + "]").attr("checked", true);
                        }
                        if(rosw[i].PROCESS_GROUP=="2"){
                            me.datagrid1.datagrid('hideColumn', 'opt');
                            me.datagrid1.datagrid('hideColumn', 'GROUP_NAME');
                        }
                    }
                    var type=$("#type").val();
                    if(type=="1"){
                        me.datagrid1.datagrid('hideColumn', 'opt');
                    }

                }
            });
        }
        function queren(GROUP_ID,PROPOSER_ID){
            $("#edit_notice").window('open');
            var type = $("#type").val();
            $('#notice').datagrid({
                url:me.actionUrl + 'selectTicketsRemarks.do',
                queryParams: {
                    TYPE:type,
                    GROUP_ID:GROUP_ID,
                    PROPOSER_ID:PROPOSER_ID
                },pagination:false,
                frozenColumns: [[
                    {field: 'DISPLAY_NAME', title: '申报人姓名', width: 100, align: 'center'
                    },
                    {field: 'SPECIALITY_NAME', title: '专家姓名', width:100, align: 'center'},
                    {field: 'GROUP_LEADER', title: '学科组职务', width:100, align: 'center',formatter:function(value,row,index){
                        if(value==1){
                            return "组员";
                        }else return "组长";
                        }},
                    {field: 'TICKETS', title: '是否通过', width: 140, align: 'center',formatter:function(value,row,index){
                        if(value==1) return "通过";
                        else if(value==2) return "不通过";
                        else return "弃权";
                        }},
                    {field: 'REMARKS', title: '意见', width: 140, align: 'center'},
                ]]
            });
        }
        //加载学科组
        function loadTree() {
            me.tree1.tree({
                url: '<%=basePath%>/BaseUnit/getGroupTreeList.do?type=${type}',
                onClick: function (node) {
                    //$("#GroupId").val(node.id);
                    loadGrid(node.id);
                },
                onLoadSuccess: function (node, param) {
                },
                onLoadError: function (data) {
                    showError('数据加载错误！');
                }
            });
        }
        //添加修改（保存）
        function SaveData() {
            var arrPerson=new Array();
            var arr=me.datagrid1.datagrid('getData').rows;
            var type=$("#type").val();
            for(var i=0;i<arr.length;i++){
                var TICKETS=$("input[name='TICKETS"+i+"']:checked").val();
                var REMARKS=$("#REMARKS"+i).val();
                if(TICKETS!=arr[i].TICKETS||REMARKS!=arr[i].REMARKS){
                    var person=new Object();
                    person.TICKETS=TICKETS;
                    person.REMARKS=REMARKS
                    person.PROPOSER_ID=arr[i].PROPOSER_ID;
                    if(type==1){
                        person.GROUP_ID=arr[i].GROUP_ID;
                    }else{
                        person.GROUP_ID=arr[i].JUDGING_CODE;
                    }
                    person.SPECIALITY_ID=arr[i].SPECIALITY_ID;
                    person.TYPE=type;
                    arrPerson.push(person);
                }
            }
            if(arrPerson.length>0) {
                $.ajax({
                    url: me.actionUrl + 'save.do',
                    data: {arrPerson: JSON.stringify(arrPerson)},
                    dataType: 'json',
                    success: function (returnData) {
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
        }
    </script>
</head>
<body class="easyui-layout">

    <div class="easyui-layout" fit="true"style="height: 100%">
    <input value="${type}" type="hidden" name="type" id="type">
        <div region="west" hide="true" split="true" title="学科组列表" style="width: 300px;">
            <ul id="tree1">
            </ul>
        </div>
    <div region="center" border="false" style="background: #fff; border: 1px solid #ccc;height:100%">
        <form id="edit_form" name="edit_form" method="post" style="height: 90%">
            <table id="datagrid1" style="height: 100%">
            </table>
        </form>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:8%">
            <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">确定提交</a>
        </div>
    </div>

    </div>

<div title="学科组意见列表" id="edit_notice" class="easyui-window" closed="true"  style="width: 85%;height: 90%; padding: 5px;">
    <table id="notice"></table>
</div>
</body>
</html>
