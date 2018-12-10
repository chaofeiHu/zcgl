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
    <title>设置学科组</title>
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
        .easyui-validatebox { appearance: none;-webkit-appearance: none;outline: none;  margin: 0 5px 0 0; -webkit-border-radius: 50%; -moz-border-radius: 50%; -o-border-radius: 50%;border-radius: 50%;border: 1px solid #ccc!important; padding:4px!important;top: 3px;position: relative;}
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
            displayName: 'groupName',
            actionUrl: '<%=basePath%>/SpecialityTickets/'
        };

        $(function () {
            pageInit();
            loadTree();
            $('#DIV_toolbar').appendTo('.datagrid-toolbar');
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
        function loadGrid(GroupId) {
            me.datagrid1.datagrid({
                url: me.actionUrl + 'selectTicketsComment.do',
                queryParams: {
                    GROUP_ID: GroupId,
                    TYPE:$("#type").val()
                },pagination:false,
                frozenColumns: [[
                    {field: 'DISPLAY_NAME', title: '姓名', width: 50, align: 'center'},
                    {field: 'UNIT_NAME', title: '单位名称', width: 140, align: 'center'},
                    {field: 'GROUP_RESULT', title: '是否通过', width: 150, align: 'center',formatter:function(value,row,index){
                        var html="<label style='margin:0 10px 0 0;text-align: center'><input type='checkbox' class='easyui-validatebox' name='GROUP_RESULT"+index+"' id='GROUP_RESULT"+index+"' value='1'";
                        var proportion=me.datagrid1.datagrid('getData').proportion;
                        if(proportion!=""){
                            var zong=me.datagrid1.datagrid('getData').nus;
                            var num=0;
                            num=Math.ceil(proportion*zong);
                            if(num==0) return html+=" />通过</label>";
                            if(row["TY"]>=num){
                                html+="checked />通过</label>";
                            }else{
                                html+=" />通过</label>";
                            }
                        }else{
                            html+=" />通过</label>";
                        }
                        return html;
                    }},
                    {field: 'REVIEW_RESULT', title: '是否通过', width: 150, align: 'center',formatter:function(value,row,index){
                            var html="<label style='margin:0 10px 0 0;text-align: center'><input type='checkbox' class='easyui-validatebox' name='REVIEW_RESULT"+index+"' id='REVIEW_RESULT"+index+"' value='1'";
                            var proportion=me.datagrid1.datagrid('getData').proportion;
                            if(proportion!=""){
                                var zong=me.datagrid1.datagrid('getData').nus;
                                var num=0;
                                num=Math.ceil(proportion*zong);
                                if(num==0) return html+=" />通过</label>";
                                if(row["TY"]>=num){
                                    html+="checked />通过</label>";
                                }else{
                                    html+=" />通过</label>";
                                }
                            }else{
                                html+=" />通过</label>";
                            }
                            return html;
                        }},
                    {field: 'TY', title: '同意票数', width: 100, align: 'center',},
                    {field: 'BTY', title: '反对票数', width: 100, align: 'center', },
                    {field: 'QQ', title: '弃权票数', width: 100, align: 'center'},
                    {field: 'GROUP_RESULT_OPINION', title: '专业组评议', width: 155, align: 'center',formatter:function(value,row,index){
                            if(value==undefined) value="";
                            return "<input  style='width: 150px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' value='"+value+"'  name='GROUP_RESULT_OPINION"+index+"' id='GROUP_RESULT_OPINION"+index+"'>";
                        }},
                    {field: 'REVIEW_RESULT_OPINION', title: '评委会评议', width: 155, align: 'center',formatter:function(value,row,index){
                            if(value==undefined) value="";
                            return "<input  style='width: 150px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' value='"+value+"'  name='REVIEW_RESULT_OPINION"+index+"' id='REVIEW_RESULT_OPINION"+index+"'>";
                        }},
                ]],
                toolbar:"#tb",
                onLoadSuccess:function(rowData){
                    $("#PROPORTION").combobox("setValue",rowData.proportion);
                    $("#NUS").val(rowData.nus);
                    var rosw=rowData.rows;
                    for(var i=0;i<rosw.length;i++){
                        if(rosw[i].GROUP_RESULT==1)
                            $("#GROUP_RESULT"+i).attr("checked",true);
                        else if(rosw[i].GROUP_RESULT==0)
                            $("#GROUP_RESULT"+i).attr("checked",false);
                        if(rosw[i].REVIEW_RESULT==1)
                            $("#REVIEW_RESULT"+i).attr("checked",true);
                        else if(rosw[i].REVIEW_RESULT==0)
                            $("#REVIEW_RESULT"+i).attr("checked",false);
                    }
                    var type=$("#type").val();
                    if(type!="1"){
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT');
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT_OPINION');
                    }else{
                        me.datagrid1.datagrid('hideColumn', 'REVIEW_RESULT');
                        me.datagrid1.datagrid('hideColumn', 'REVIEW_RESULT_OPINION');
                    }
                }
            });
        }
        function saveSubject() {
            var GroupId=$("#GroupId").val();
            var TYPE=$("#type").val();
            var PROPORTION=$("#PROPORTION").combobox("getValue");
            if(PROPORTION==""){
                showInfo("请定义通过票数比例");
                return ;
            }
            $.ajax({
                url: me.actionUrl + 'saveSubjectGroup.do',
                data: {GroupId:GroupId,PROPORTION:PROPORTION,TYPE:TYPE},
                dataType:'json',
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
        //添加修改（保存）
        function SaveData() {
            var arrPerson=new Array();
            var arr=me.datagrid1.datagrid('getData').rows;
            for(var i=0;i<arr.length;i++){
                var person=new Object();
                var type=$("#type").val();
                var TICKETS="";
                var COMMENT="";
                if(type==1){
                    TICKETS=$("input[name='GROUP_RESULT"+i+"']:checked").val();
                    COMMENT=$("#GROUP_RESULT_OPINION"+i).val();
                }else{
                    TICKETS=$("input[name='REVIEW_RESULT"+i+"']:checked").val();
                    COMMENT=$("#REVIEW_RESULT_OPINION"+i).val();
                }
                if(TICKETS==undefined){
                    TICKETS=0;
                }
                person.STATE=TICKETS;
                person.TY=arr[i].TY;
                person.BTY=arr[i].BTY;
                person.QQ=arr[i].QQ;
                person.COMMENT=COMMENT;
                person.PROPOSER_ID=arr[i].PROPOSER_ID;
                person.GROUP_ID=arr[i].GROUP_ID;
                person.TYPE=$("#type").val();
                arrPerson.push(person);
            }
            $.ajax({
                url: me.actionUrl + 'saveComment.do',
                data: {arrPerson:JSON.stringify(arrPerson)},
                dataType:'json',
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

        //加载学科组
        function loadTree() {
            me.tree1.tree({
                url: '<%=basePath%>/BaseUnit/getGroupTreeList.do?type=${type}',
                onClick: function (node) {
                    $("#GroupId").val(node.id);
                    loadGrid(node.id);
                },
                onLoadSuccess: function (node, param) {
                },
                onLoadError: function (data) {
                    showError('数据加载错误！');
                }
            });
        }
    </script>
</head>
<body class="easyui-layout">
<input value="${type}" type="hidden" name="type" id="type">
<input  type="hidden" name="GroupId" id="GroupId">
<div region="west" hide="true" split="true" title="学科组列表" style="width: 300px;">
    <ul id="tree1">
    </ul>
</div>
<div title="票数统计列表" region="center"  border="false" id="edit_notice" >
    <div class="easyui-layout" fit="true"style="height: 100%">
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
    <div id="tb" style="padding:3px">
        <span>实际参加评委人数: <input style='width: 100px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' type="text"  name="NUS" id="NUS" disabled></span>
        <span>定义通过票数比例:</span>
        <select name="PROPORTION" id="PROPORTION"  class="easyui-combobox" style="width: 100px">
            <option value="">请选择</option>
            <option value="0.5">1/2</option>
            <option value="0.33">1/3</option>
            <option value="0.67">2/3</option>
        </select>
        <span  style="padding-left: 10px;"></span>
        <a href="#" class="easyui-linkbutton" icon="icon-save" plain="true" onclick="saveSubject()">确定</a>
    </div>
</body>
</html>
