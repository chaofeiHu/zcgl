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
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>参评人员分组</title>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/Method.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
</head>
<body>
<input type="hidden" name="professialId" id="professialId"   class="layui-input">
<from>
    <div class="layui-form" style="padding: 10px 0 0 10px"  lay-filter="pwh">
        <div class="layui-inline"style="width:300px;"  >
            <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会"lay-search="" >
            </select>
        </div>
        <button class="layui-btn" lay-submit lay-filter="tabl">搜索</button>
    </div>
</from>
<table class="layui-hide" id="LAY_table_user" lay-filter="test"></table>

<script type="text/html" id="barDemo">

    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="manual({{d.ID}},'{{d.GROUP_NAME}}','未分组参评人')"  style="background-color: #5FB878;color:#fff" lay-event="detail">未分组参评人</a>

    <a class="layui-btn layui-btn-primary layui-btn-xs"  onclick="Random({{d.ID}},'{{d.GROUP_NAME}}','当前分组参评人')" style="background-color: #5FB878;color:#fff" lay-event="edit">当前分组参评人</a>

</script>
<script>
    var form;
    var table
    layui.use(['selectPlus','form','table','laydate'], function(){
        form =layui.form;
        table = layui.table;
        var laydate = layui.laydate;
        var selectPlus = layui.selectPlus;
        var $=layui.$;
        laydate.render({ elem: '#yearNo' ,type: 'year'});
        //方法级渲染
        table.render({
            elem: '#LAY_table_user'
            ,url: '<%=basePath%>/ProposerGroup/getList.do'
            ,toolbar: '#toolbarDemo'
            ,cols: [[
                {field:'GROUP_NAME', title: '组名' }
                ,{field:'YEAR_NO', title: '年度'}
                ,{field:'PROFESSIAL_NAME', title: '下属专业' }
                ,{field:'BACK1', title: '参评人数量'}
                ,{title: '管理', align:'center',width:300, toolbar: '#barDemo', fixed: 'right'}
            ]],
            height: 'full-68',
            text: {none: '暂无相关数据'}
            ,id: 'testReload'
            ,page: true
        });
        <%--父页面搜索--%>
        form.on('submit(tabl)', function(data){
            table.reload('testReload', {  page: {   curr: 1  }  ,where:data.field});
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--手未分组参评人搜索--%>
        form.on('submit(tabladd)', function(data){
            var DISPLAY_NAME=$("#DISPLAY_NAME").val();
            table.reload('test1', {
                page: {   curr: 1  }
                ,where:{DISPLAY_NAME:DISPLAY_NAME,sta:"1",JUDGING_CODE:$("#JUDGING_CODE").val()}
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--当前分组参评人搜索--%>
        form.on('submit(select)', function(data){
            var DISPLAY_NAME=$("#SPECIALITY_NAME").val();
            table.reload('split', {
                page: {   curr: 1  }
                ,where:{DISPLAY_NAME:DISPLAY_NAME,sta:"2",JUDGING_CODE:$("#JUDGING_CODE").val()}
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--加载评委会--%>
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
                form.render('select',"pwh");
            }
        });
    });

    <%--打开 未分组参评人窗口 窗口--%>
    function manual(ID,title,ch){
        $("#GROUP_ID").val(ID);
        table.reload('test1', {
            url: '<%=basePath%>/ProposerGroup/getProposerList.do',
            where: {JUDGING_CODE:$("#JUDGING_CODE").val(),sta:1}
        });
        layer.open({
            type: 1,
            title: title+"-"+ch,
            area: ['80%', '100%'], //宽高
            content: $("#myadd"),
        });
    }
    <%--手工添加参评人--%>
    function saveEn(id){
        var returnData=ajax('<%=basePath%>/ProposerGroup/save.do',{ids:id,GROUP_ID:$("#GROUP_ID").val()})
        layer.alert(returnData.message);
        table.reload('testReload');
        table.reload('test1');
    }


    <%--打开 当前分组参评人 窗口--%>
    function Random(ID,title,ch){
        table.reload('split', {
            url: '<%=basePath%>/ProposerGroup/getProposerList.do',
            where: {GROUP_ID:ID,JUDGING_CODE:$("#JUDGING_CODE").val(),sta:2}
        });
        layer.open({
            type: 1,
            title: title+"-"+ch,
            area: ['80%', '100%'], //宽高
            content: $("#renyuan"),
        });
    }
    <%--删除信息--%>
    function Delete() {
        var checkStatus = table.checkStatus('split');
        console.log(checkStatus);
        var ids = [];
        var names = [];
        for (var i = 0; i < checkStatus.data.length; i++) {
            ids.push(checkStatus.data[i].GID);
            names.push(checkStatus.data[i].DISPLAY_NAME);
        }
        console.log(ids);
        layer.confirm('确认要删除选择项？【' + names + '】', {
            btn: ['确定','取消'] //按钮
        }, function(){
            var returnData=ajax('<%=basePath%>/ProposerGroup/delete.do',{ids: ids.join(',')});
            layer_dele(returnData.message,'');
            table.reload('split');
            table.reload('testReload');
        });
    }

</script>

<form class="layui-form layui-form-pane" action="" title="未分组参评人窗口"  style="display:none" id="myadd">
    <div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="Manual">
        <div class="layui-inline"style="width:200px;" >
            <input type="hidden" id="GROUP_ID" name="GROUP_ID">
            <input type="text" name="DISPLAY_NAME" id="DISPLAY_NAME"   placeholder="请选择姓名" class="layui-input">
        </div>
        <button class="layui-btn layui-btn-sm" lay-submit lay-filter="tabladd">搜索</button>
    </div>
    <table class="layui-table" lay-data="{ height: 'full-20',page:true, id:'test1'}" lay-filter="test1">
        <thead>
        <tr>
            <th lay-data="{field:'DISPLAY_NAME'}">参评人姓名</th>
            <th lay-data="{field:'SEX',templet:'#SEX' }">性别</th>
            <th lay-data="{field:'REVIEW_SERIES_NAME'}">评审系列</th>
            <th lay-data="{field:'PROFESSIAL_NAME'}">申报专业</th>
            <th lay-data="{field:'UNIT_NAME'}">申报单位</th>
            <th lay-data="{field:'POSITION_NAME'}">申报职称</th>
            <th lay-data="{field:'REPORT_DATE',templet:'#REPORT_DATE'}">申报时间</th>
            <th lay-data="{toolbar:'#titleTpl',align:'center',width:120,fixed: 'right'}">管理</th>
        </tr>
        </thead>
    </table>
</form>
<script type="text/html" id="titleTpl">
    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="saveEn('{{d.PROPID}}')"  style="background-color: #5FB878;color:#fff" lay-event="detail">添加</a>
</script>
<script type="text/html" id="SEX">
    {{#  if(d.SEX==1){ }}
    男
    {{#  }else if(d.SEX==2){ }}
    女
    {{#  }else{ }}
    未说明性别
    {{#  } }}
</script>


<form class="layui-form layui-form-pane" action="" title="当前分组参评人窗口"  style="display:none" id="renyuan">
    <div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="Manual">
        <div class="layui-inline"style="width:200px;" >
            <input type="text" name="DISPLAY_NAME" id="SPECIALITY_NAME"   placeholder="请选择姓名" class="layui-input">
        </div>
        <button class="layui-btn layui-btn-sm" lay-submit lay-filter="select">搜索</button>
        <button class="layui-btn layui-btn-sm" type="button" onclick="Delete()" title="删除"><i class="layui-icon"></i></button>
    </div>
    <table class="layui-table" lay-data="{ height: 'full-20',page:true, id:'split'}" lay-filter="split">
        <thead>
        <tr>
            <th lay-data="{checkbox:true}"></th>
            <th lay-data="{field:'DISPLAY_NAME'}">参评人姓名</th>
            <th lay-data="{field:'SEX',templet:'#SEX1' }">性别</th>
            <th lay-data="{field:'REVIEW_SERIES_NAME'}">评审系列</th>
            <th lay-data="{field:'PROFESSIAL_NAME'}">申报专业</th>
            <th lay-data="{field:'UNIT_NAME'}">申报单位</th>
            <th lay-data="{field:'POSITION_NAME'}">申报职称</th>
            <th lay-data="{field:'REPORT_DATE',templet:'#REPORT_DATE'}">申报时间</th>
        </tr>
        </thead>
    </table>
</form>
<script type="text/html" id="SEX1">
    {{#  if(d.SEX==1){ }}
    男
    {{#  }else if(d.SEX==2){ }}
    女
    {{#  }else{ }}
    未说明性别
    {{#  } }}
</script>
<script type="text/html" id="REPORT_DATE">
    <div>{{ formatdate(d.REPORT_DATE) }}</div>
</script>
</body>
</html>
