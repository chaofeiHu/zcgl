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
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
    <script src="<%=basePath%>/static/js/Excel.js" charset="utf-8"></script>
    <style>
        .layui-table-tool-temp{
            padding-right:10px
        }
    </style>
</head>
<body >
<div class="layui-form" >
    <table id="demo" style="display: none" border="1">
        <thead></thead>
        <tbody></tbody>
    </table>
    <div class="gg" style="">
        <label class="layui-form-label" style="width:120px;text-align: left">请选择评委会：</label>
        <div class="layui-inline" style="width:400px!important;">
           <select id="GROUP_ID" name="GROUP_ID"  lay-filter="group_id" lay-verify="required" lay-search="">
           </select>
        </div>

    </div>
    <table class="layui-table"  lay-data="{url:'<%=basePath%>/SpecialityTickets/getCommentList.do',id: 'LAY_table_user',toolbar: '#toolbarDemo',defaultToolbar:[''],text: {none: '暂无相关数据'},title:'评委会评议情况汇总表',height: '455px'}" lay-filter="test" style="height:100%;">
        <thead>
        <tr>
            <th lay-data="{field:'UNIT_NAME', width:100}" rowspan="2">单位名称</th>
            <th lay-data="{field:'DISPLAY_NAME', width:100}" rowspan="2">姓名</th>
            <th lay-data="{align:'center'}" colspan="5">专业组评议情况</th>
            <th lay-data="{align:'center'}" colspan="5">大评委评议情况</th>
        </tr>
        <tr>
            <th lay-data="{field:'GROUP_RESULT', width:100,templet:function(d){if(d.GROUP_RESULT==0){return '未通过';}else{return '通过'}}}">是否通过</th>
            <th lay-data="{field:'GROUP_RESULT_YES', width:100}">同意票数</th>
            <th lay-data="{field:'GROUP_RESULT_NO', width:100}">反对票数</th>
            <th lay-data="{field:'GROUP_RESULT_WAIVE', width:100}">弃权票数</th>
            <th lay-data="{field:'GROUP_RESULT_OPINION', width:100}">专业组评议</th>
            <th lay-data="{field:'REVIEW_RESULT', width:100,templet:function(d){ if(d.REVIEW_RESULT==0){return '未通过';}else{return '通过'}}}">是否通过</th>
            <th lay-data="{field:'REVIEW_RESULT_YES', width:100}">同意票数</th>
            <th lay-data="{field:'REVIEW_RESULT_NO', width:100}">反对票数</th>
            <th lay-data="{field:'REVIEW_RESULT_WAIVE', width:100}">弃权票数</th>
            <th lay-data="{field:'REVIEW_RESULT_OPINION', width:100}">评委会评议</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/html" id="toolbarDemo" class="demoTable">
    <label class="layui-form-label" style="width:120px;text-align: left">大评委结果:</label>
    <div class="layui-input-inline">
        <select name="STATES" id="STATES" >
            <option value="">全部</option>
            <option value="1">通过</option>
            <option value="0">未通过</option>
        </select>
    </div>
    <button class="layui-btn" data-type="reload" onclick="load()">搜索</button>
    <div class="layui-input-inline" style="float:right">
        <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="JttableToExcel('评审情况统计表','demo')" ><i class="layui-icon layui-icon-export"></i></button>
        <button  onclick="preview(11)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon layui-icon-print"></i></button>
    </div>
</script>
<script>
    var table;
    var form;
    $(function () {

        layui.use(['form','table'], function(){
             form=layui.form;
             table = layui.table;
            var $ = layui.$;
            $.ajax({
                url: '<%=basePath%>/BaseUnit/getGroupTreeList.do?type=${type}',
                dataType:'json',
                success: function (returnData) {
                    var html='<option value=""></option> ';
                    for(var i=0;i<returnData.length;i++){
                        html+='<option value="'+returnData[i].id+'">'+returnData[i].text+'</option>'
                    }
                    $("#GROUP_ID").html(html);
                    form.render('select');
                }
            });
            form.on('select(group_id)', function(data){
                load(table);
            });
        });
    });
    function load(){
        var STATES = $('#STATES').val();
        var GROUP_ID=$("#GROUP_ID").val();
        table.reload('LAY_table_user', {
            where: {STATES:STATES,GROUP_ID:GROUP_ID},
            done:function (data) {
                $("#demo thead").html('');
                $("#demo tbody").html('');
                var thead=$(".layui-table-header .layui-table thead").html();
                var tbody=$(".layui-table-body .layui-table tbody").html();
                $("#demo thead").prepend(thead);
                $("#demo tbody").prepend(tbody);
                $('#STATES').val(STATES);
                form.render('select');
            }
        });
    }
</script>
</body>
</html>
