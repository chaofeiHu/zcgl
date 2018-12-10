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
    <title>评委会评议情况汇总表</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/js/print.js"></script>
    <script src="<%=basePath%>/static/js/jquery.jqprint.js"></script>
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
function pri(){
    $(".datagrid-view").jqprint();
}
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
                url: me.actionUrl + 'getCommentList.do',
                queryParams: {
                    GROUP_ID: GroupId
                },pagination:false,nowrap:true,
                frozenColumns: [[
                    {field: 'UNIT_NAME', title: '单位名称', width: 140, align: 'center'},
                    {field: 'DISPLAY_NAME', title: '姓名', width: 50, align: 'center'},
                    ]],
                columns: [
                    [{"title":"专业组评议情况","colspan":5},
                        {"title":"大评委评议情况","colspan":5}],
                    [
                        {field: 'GROUP_RESULT', title: '是否通过', width: 100, align: 'center',formatter:function(value,row,index){
                            if(value==1) return "通过";
                            else if(value==0) return "未通过";
                            }},
                        {field: 'GROUP_RESULT_YES', title: '同意票数', width: 100, align: 'center',},
                        {field: 'GROUP_RESULT_NO', title: '反对票数', width: 100, align: 'center', },
                        {field: 'GROUP_RESULT_WAIVE', title: '弃权票数', width: 100, align: 'center'},
                        {field: 'GROUP_RESULT_OPINION', title: '专业组评议', width: 155, align: 'center'},
                       {field: 'REVIEW_RESULT', title: '是否通过', width: 100, align: 'center',formatter:function(value,row,index){
                                    if(value==1) return "通过";
                                    else if(value==0) return "未通过";
                       }},
                       {field: 'REVIEW_RESULT_YES', title: '同意票数', width: 100, align: 'center',},
                       {field: 'REVIEW_RESULT_NO', title: '反对票数', width: 100, align: 'center', },
                       {field: 'REVIEW_RESULT_WAIVE', title: '弃权票数', width: 100, align: 'center'},
                       {field: 'REVIEW_RESULT_OPINION', title: '评委会评议',width: 155, align: 'center'}
                ]],
                toolbar:"#tb",
                onLoadSuccess:function(rowData){
                    console.log(rowData);
                    if(rowData.nus=="2"){
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT');
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT_YES');
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT_NO');
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT_WAIVE');
                        me.datagrid1.datagrid('hideColumn', 'GROUP_RESULT_OPINION');

                    }
                }
            });
        }
        function saveSubject(){
           var  STATES=$("#STATES").combobox("getValue");
            var  GroupId=$("#GroupId").val();
            me.datagrid1.datagrid('load',{
                STATES: STATES,
                GROUP_ID:GroupId
            });
        };

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
    </div>
</div>
    <div id="tb" style="padding:3px">
        <span>大评委结果:</span>
        <select name="STATES" id="STATES"  class="easyui-combobox" style="width: 100px">
            <option value="">全部</option>
            <option value="1">通过</option>
            <option value="0">未通过</option>
        </select>
        <span  style="padding-left: 10px;"></span>
        <a href="#" class="easyui-linkbutton" icon="icon-search"  onclick="saveSubject()">查询</a>
        <a href="#" class="easyui-linkbutton" icon="icon-search"  onclick="print()">打印</a>
    </div>
</body>
</html>
