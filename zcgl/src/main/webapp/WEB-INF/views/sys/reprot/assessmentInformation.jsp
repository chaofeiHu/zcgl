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
    <title>评审情况统计表</title>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/Excel.js" charset="utf-8"></script>
</head>
<body >
<div class="layui-form" >
    <table id="demo" style="display: none" border="1">
        <thead></thead>
        <tbody></tbody>
    </table>

    <table class="layui-table"  lay-data="{height: 'full-20',id: 'LAY_table_user',url:'<%=basePath%>/Reprot/assessmentInformationList.do',toolbar: '#toolbarDemo',page:'true',text: {none: '暂无相关数据'},title:'评审情况统计表',}" lay-filter="test" style="height:100%;">
        <thead>
        <tr>
            <th lay-data="{field:'UNIT_NAME',align:'left', width:170,sort:'true'}">单位</th>
            <th lay-data="{field:'DISPLAY_NAME',align:'left', width:80,sort:'true'}">姓名</th>
            <th lay-data="{field:'ID_CARD_NO', align:'left',width:180}">身份证号</th>
            <th lay-data="{field:'JUDGING_NAME',align:'left', width:300}">评委会名称</th>
            <th lay-data="{field:'XL_NAME',align:'left', width:120,sort:'true'}">申报系列</th>
            <th lay-data="{field:'JB_NAME', align:'center',width:100}">职称级别</th>
            <th lay-data="{field:'ZC_NAME',align:'left', width:130}">职称</th>
            <th lay-data="{field:'PROFESSIAL_NAME',align:'left', width:120,sort:'true'}">申报专业</th>
            <th lay-data="{field:'PSLX_NAME', align:'left',width:100}">评审类型</th>
            <th lay-data="{field:'GROUP_NAME',align:'left', width:130}">专业组名称</th>
            <th lay-data="{field:'GROUP_RESULT', align:'center',width:100,templet:function(d){
           if(d.GROUP_RESULT==0){return '未通过';}else{return '通过'}}}">专业组通过</th>
            <th lay-data="{field:'GROUP_RESULT_YES', align:'center',width:120}">专业组同意票数</th>
            <th lay-data="{field:'GROUP_RESULT_NO', align:'center',width:120}">专业组反对票数</th>
            <th lay-data="{field:'GROUP_RESULT_WAIVE', align:'center',width:120}">专业组弃权票数</th>
            <th lay-data="{field:'REVIEW_RESULT', align:'center',width:100,templet:function(d){ if(d.REVIEW_RESULT==0){return '未通过';}else{return '通过'}}}">大评委通过</th>
            <th lay-data="{field:'REVIEW_RESULT_YES', align:'center',width:120}">大评委同意票数</th>
            <th lay-data="{field:'REVIEW_RESULT_NO',align:'center', width:120}">大评委反对票数</th>
            <th lay-data="{field:'REVIEW_RESULT_WAIVE', align:'center',width:120}">大评委弃权票数</th>
            <th lay-data="{field:'FRISTEDUCATION',align:'center', width:120,sort:'true'}">第一学历</th>
            <th lay-data="{field:'MAXEDUCATION', align:'center',width:120}">最高学历</th>
            <th lay-data="{field:'MAXEDUCATIONTIME',align:'center', width:120}">取得资格时间</th>
            <th lay-data="{field:'STARTWORKTIME',align:'center', width:120,sort:'true'}">工作时间</th>
            <th lay-data="{field:'NOWJOB', align:'center',width:120}">从事专业</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/html" id="toolbarDemo" class="demoTable">
    <div class="layui-form" style="">
        <div class="layui-inline">
            <input class="layui-input" name="UNIT_NAME"  placeholder="请输入单位" id="UNIT_NAME" autocomplete="off">
        </div>
        <div class="layui-inline">
            <input class="layui-input" name="DISPLAY_NAME"  placeholder="请输入名称" id="DISPLAY_NAME" autocomplete="off">
        </div>
        <div class="layui-inline"style="width:180px"  lay-filter="JUDGING_CODE" >
            <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会"lay-search="" >
            </select>
        </div>
        <div class="layui-inline"style="width:180px"  lay-filter="XL_NAME">
            <select id="XL_NAME" name="XL_NAME" placeholder="请选择系列" lay-search="" >
            </select>
        </div>
        <div class="layui-inline"style="width:180px" lay-filter="JB_NAME">
            <select id="JB_NAME" name="JB_NAME" placeholder="请输入职称级别"  lay-search="" >
            </select>
        </div>
        <button class="layui-btn" style="padding: 0 8px;" lay-submit="" lay-filter="sss">搜索</button>
    </div>
</script>
<script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
<script>
    var table;
    var form;

    $(function () {
        layui.use(['form','table','laydate'], function(){
            form =layui.form;
            table = layui.table;
            var $ = layui.$;

            tree('');
            xl('',"REVIEW_SERIES","XL_NAME");
            xl('',"TITLE_LEVEL","JB_NAME");
            form.on('submit(sss)', function(data){
                table.reload('LAY_table_user', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where:  data.field,
                    done:function (da) {
                        $("#UNIT_NAME").val(data.field.UNIT_NAME);
                        $("#DISPLAY_NAME").val(data.field.DISPLAY_NAME);
                        $("#JUDGING_CODE").val(data.field.JUDGING_CODE);
                        $("#XL_NAME").val(data.field.XL_NAME);
                        $("#JB_NAME").val(data.field.JB_NAME);
                        tree(data.field.JUDGING_CODE);
                        xl(data.field.XL_NAME,"REVIEW_SERIES","XL_NAME");
                        xl(data.field.JB_NAME,"TITLE_LEVEL","JB_NAME");
                    }
                });
            })
        });
    });
    function xl(GROUP_ID,groupName,ID){
        $.ajax({
            url: '<%=basePath%>/JudgingProposer/getDict.do',
            dataType:'json',
            type: "POST",
            data:{groupName:groupName},
            success: function (returnData) {
                var html='<option value="">请选择系列</option> ';
                var chil=returnData;
                for(var i=0;i<chil.length;i++){
                    html+='<option value="'+chil[i].dictCode+'">'+chil[i].dictName+'</option>'
                }
                $("#"+ID).html(html);
                $('#'+ID).val(GROUP_ID);
                form.render('select');
            }
        });
    }

    function tree(JUDGING_CODE){
        $.ajax({
            url: '<%=basePath%>/Speciality/getJudgingTree.do',
            dataType:'json',
            success: function (returnData) {
                var html='<option value="">请选择评委会</option> ';
                var chil=returnData[0].children;
                for(var i=0;i<chil.length;i++){
                    html+='<option value="'+chil[i].attributes+'">'+chil[i].text+'</option>'
                }
                $("#JUDGING_CODE").html(html);
                $('#JUDGING_CODE').val(JUDGING_CODE);
                form.render('select');
            }
        });
    }
</script>
</body>
</html>
