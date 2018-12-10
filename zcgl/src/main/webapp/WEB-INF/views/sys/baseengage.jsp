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
    <title>设置专业组</title>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/Method.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
</head>
<body>
<from>
    <div class="layui-form" style="padding: 10px 0 0 10px"  lay-filter="pwh">
    <div class="layui-inline"style="width:300px;"  >
        <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会"lay-search="" >
        </select>
    </div>
    <div class="layui-inline"style="width:300px;" >
        <input type="text" name="SPECIALITY_NAME" id="SPECIALITY_NAME"   placeholder="请输入专家姓名" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-inline"style="width:300px;"  >
        <input type="text" name="NOWUNIT" id="NOWUNIT"   placeholder="请输入单位名称" autocomplete="off" class="layui-input">
    </div>
    <button class="layui-btn" lay-submit lay-filter="tabl">搜索</button>
    </div>
</from>
<table class="layui-hide" id="LAY_table_user" lay-filter="test"></table>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-group">
        <% if(MenuBtns.indexOf("Manual")>-1) { %>
        <button class="layui-btn layui-btn-sm" lay-event="add" title="手工添加"><i class="layui-icon">&#xe654;</i></button><% } %>
        <% if(MenuBtns.indexOf("Random")>-1) { %>
        <button class="layui-btn layui-btn-sm"lay-event="Random" title="随机分配"><i class="layui-icon">&#xe674;</i></button><% } %>
        <% if(MenuBtns.indexOf("Delete")>-1) { %>
        <button class="layui-btn layui-btn-sm"lay-event="delete" title="删除"><i class="layui-icon"></i></button><% } %>
      <%--  <% if(MenuBtns.indexOf("Insert")>-1) { %>
        <button class="layui-btn layui-btn-sm"lay-event="delete" title="确认提交"><i class="layui-icon">&#x1005;</i></button><% } %>--%>
    </div>
</script>
<script type="text/html" id="barDemo">
    {{#  if(d.JURY_DUTY!=1){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="director({{d.JUDGING_ID}},{{d.SPECIALITY_ID}},1)"  style="background-color: #5FB878;color:#fff" lay-event="detail">设为主任</a>
    {{#  } }}
    {{#  if(d.JURY_DUTY!=2){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs"  onclick="director({{d.JUDGING_ID}},{{d.SPECIALITY_ID}},2)" style="background-color: #5FB878;color:#fff" lay-event="edit">设为副主任</a>
    {{#  } }}
    {{#  if(d.JURY_DUTY==2){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="director({{d.JUDGING_ID}},{{d.SPECIALITY_ID}},3)"  style="background-color: #5FB878;color:#fff" lay-event="detail">取消副主任</a>
    {{#  } }}
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
            ,url: '<%=basePath%>/BaseEngage/selectEngagelistPage.do'
            ,toolbar: '#toolbarDemo'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'},
                {field:'SPECIALITY_NAME', title: '专家姓名' ,width:'90', fixed: 'left'}
                ,{field:'NOWUNIT', title: '单位名称',width:'200', fixed: 'left'}
                ,{field:'JURY_DUTY', title: '评委会职务' ,width:'100',templet:function(d){
                        if(d.JURY_DUTY==1) return "主任委员";else if(d.JURY_DUTY==2) return "副主任委员";else return "委员";
                    }}
              /*  ,{field:'BACK1', title: '是否确认',width:'100',templet:function(d){
                    if(d.BACK1==0) return "已确认";else if(d.BACK1==1) return "未确认"; }}*/
                ,{field:'SEX', title: '性别',width:'80',templet:function(d){
                        if(d.SEX==1) return "男";else if(d.SEX==2) return "女";else return "未说明性别";  }}
                ,{field:'MOBILEPHONE', title: '手机号码',width:'120'}
                ,{field:'TEL', title: '办公电话',width:'100'}
                ,{field:'PROFESSIAL_DUTY_NAME', title: '专业技术职务等级',width:'120'}
                ,{field:'PROFESSIAL_LEVEL_NAME', title: '专业技术职务',width:'120'}
                ,{field:'YN', title: '是否组员',width:'100',templet:function(d){ if(d.YN>0) return "是";else return "否"; }}
                ,{field:'TYPE', title: '是否出席',width:'100', fixed: 'right',templet:function(d){
                    var chek="";  if(d.TYPE==0){  chek="checked"; }
                    return '<input type="checkbox" name="sex" value="'+d.ID+'" lay-skin="switch" lay-text="出席|不出席" lay-filter="sexDemo" '+chek+'>';
                    }}
                ,{field:'REASON', title: '不出席理由',width:'120', edit: 'text', fixed: 'right'}
                ,{title: '管理', align:'center', toolbar: '#barDemo',width:'180', fixed: 'right'}
            ]],
            height: 'full-68',
            text: {none: '暂无相关数据'}
            ,id: 'testReload'
            ,page: true
        });
        //头工具栏事件
        table.on('toolbar(test)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            var JUDGING_CODE=$("#JUDGING_CODE").val();
            if(JUDGING_CODE==""){
                layer.msg("请选择评委会！");   return  }
            switch(obj.event){
                case 'add':
                    $("#JUDGING_CODE1").val(JUDGING_CODE);
                    table.reload('test1', {
                        url: '<%=basePath%>/Judging/getJudgingGroupList.do',
                        where: {NOTJUDGINGID:JUDGING_CODE}
                    });
                    layer.open({
                        type: 1,
                        title: "手动选择专家",
                        area: ['80%', '100%'], //宽高
                        content: $("#myadd"),
                    });
                    break;
                case 'Random': <%--随机--%>
                    $.ajax({
                        url: '<%=basePath%>/BaseEngage/selectProfessial.do',
                        data:{JUDGING_CODE:JUDGING_CODE},
                        dataType:'json',
                        success: function (returnData) {
                            var html='<option value="">请选择专业</option> ';
                            for(var i=0;i<returnData.length;i++){
                                html+='<option value="'+returnData[i].PROFESSIAL_CODE+'">'+returnData[i].PROFESSIAL_NAME+'</option>'
                            }
                            $("#zhuanye").html(html);
                            form.render('select');
                        }
                    });
                    var returnData=ajax('<%=basePath%>/BaseEngage/selectNoGroupSp.do',{JUDGING_CODE:$("#JUDGING_CODE").val(),zhuanye:$("#zhuanye").val()});
                    $('#allSpecialityCount').val(returnData.message);
                    layer.open({
                        type: 1,
                        title: "随机抽取专家",
                        area: ['460px','100%'], //宽高
                        btn: ['确定', '取消'],
                        content: $("#myRom"),
                        yes: function (index, layero) {
                            var JUDGING_CODE=$("#JUDGING_CODE").val();
                            var allSpecialityCount=$("#allSpecialityCount").val();
                            var specialityCount=$("#specialityCount").val();
                            var zhuanye=$("#zhuanye").val();
                            if(parseInt(specialityCount)>parseInt(allSpecialityCount)){
                                layer.msg("不能超过可供分配专家数");
                                return ;
                            }
                            if(specialityCount!=0){
                                returnData= ajax('<%=basePath%>/BaseEngage/addEngageRandom.do',{JUDGING_CODE:JUDGING_CODE,specialityCount:specialityCount,zhuanye:zhuanye})
                                layer.alert(returnData.message);
                                table.reload('testReload');
                                layer.close(index);
                            }
                        }
                    });
                    break;
                case 'delete':
                    var ids = [];
                    var names = [];
                    var data = checkStatus.data;
                    if(data.length==0){
                        layer_dele("请选择要删除的数据！");
                        return ;
                    }
                    for (var i = 0; i < data.length; i++) {
                        ids.push(data[i].ID);
                        names.push(data[i].SPECIALITY_NAME);
                    }
                    Delete(ids.join(','), names.join(','));
                    break;
            };
        });
        form.on('submit(tabl)', function(data){
            console.log(data.field);
                var JUDGING_CODE = $('#JUDGING_CODE').val();
                var SPECIALITY_NAME=$('#SPECIALITY_NAME').val();
                var NOWUNIT=$('#NOWUNIT').val();
                table.reload('testReload', {  page: {   curr: 1  }  ,where:data.field   });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        form.on('submit(tabladd)', function(data){
            console.log(data.field);
            table.reload('test1', {
                page: {   curr: 1  }
                ,where: data.field
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        form.on('select(zhuanye)', function(data){
            var returnData=ajax('<%=basePath%>/BaseEngage/selectNoGroupSp.do',{JUDGING_CODE:$("#JUDGING_CODE").val(),zhuanye:data.value});
            $('#allSpecialityCount').val(returnData.message);
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
        <%--监听是否出席--%>
        form.on('switch(sexDemo)', function(obj){
            var type;
            if(obj.elem.checked){  type=0; }else{  type=1; }
           var returndata= ajax("<%=basePath%>/BaseEngage/updateEngage.do",{id:this.value,type:type});
            if(returndata.isOk!=1){layer.msg(returndata.message);}
        });
        <%--监听不出席理由编辑--%>
        table.on('edit(test)', function(obj){
            var value = obj.value //得到修改后的值
                ,data = obj.data; //得到所在行所有键
            var returndata= ajax("<%=basePath%>/BaseEngage/updateEngage.do",{id:data.ID,reason:value});
            if(returndata.isOk!=1){layer.msg(returndata.message);}
        });
    });
    <%--修改主任副主任--%>
    function director(JUDGING_ID,SPECIALITY_ID,num){
        var returnData=ajax('<%=basePath%>/Judging/UpdateDirector.do',
            {JUDGING_ID:JUDGING_ID,SPECIALITY_ID:SPECIALITY_ID,JURY_DUTY:num});
        layer.alert(returnData.message);
        table.reload('testReload');
    }
    <%--删除信息--%>
    function Delete(ids, names) {
        layer.confirm('确认要删除选择项？【' + names + '】', {
            btn: ['确定','取消'] //按钮
        }, function(){
            var returnData=ajax('<%=basePath%>/BaseEngage/DeleteEngage.do',{id: ids});
            layer_dele(returnData.message,'');
            table.reload('testReload');
        });
    }
    <%--手工添加专家--%>
    function saveEn(id){
        var returnData=ajax('<%=basePath%>/BaseEngage/addEngage.do',{SPECIALITY_ID:id,JUDGING_CODE:$("#JUDGING_CODE").val()})
        layer.alert(returnData.message);
        table.reload('testReload');
        table.reload('test1');
    }

</script>

<form class="layui-form layui-form-pane" action="" style="display:none" id="myRom">
    <div class="layui-form-item" style="padding-top: 10px;" lay-filter="zy">
        <label class="layui-form-label" style="width:220px;text-align: right">请选择专业：</label>
        <div class="layui-input-inline">
            <select name="zhuanye" id="zhuanye" lay-filter="zhuanye"></select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"style="width:220px;text-align: right">当前可供分配专家数：</label>
        <div class="layui-input-inline">
            <input name="allSpecialityCount" id="allSpecialityCount"class="layui-input" disabled  type="text" autocomplete="off"/>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"style="width:220px;text-align: right">请输入随机抽取的专家数量：</label>
        <div class="layui-input-inline">
            <input name="specialityCount" id="specialityCount"  class="layui-input"  type="text" autocomplete="off"/>
        </div>
    </div>
    </form>

<form class="layui-form layui-form-pane" action="" title="专业组维护窗口"  style="display:none" id="myadd">
    <div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="Manual">
        <div class="layui-inline"style="width:200px;" >
            <input type="text" name="SPECIALITY_NAME" id="specialityName1"   placeholder="请选择专家姓名" class="layui-input">
        </div>
        <div class="layui-inline"style="width:200px;" >
            <input type="hidden" name="NOTJUDGINGID" id="JUDGING_CODE1"   placeholder="请输入单位名称" class="layui-input">
            <input type="text" name="NOWUNIT" id="NOWUNIT1"   placeholder="请输入单位名称" class="layui-input">
        </div>
        <div class="layui-inline"style="width:200px;"  >
            <input type="text" name="RECOMMEND_MAJOR" id="RECOMMEND_MAJOR"   placeholder="请输入专业" class="layui-input">
        </div>
       <button class="layui-btn layui-btn-sm" lay-submit lay-filter="tabladd">搜索</button>
    </div>
    <table class="layui-table" lay-data="{ height: 'full-20',page:true, id:'test1'}" lay-filter="test1">
        <thead>
        <tr>
            <th lay-data="{field:'LOGIN_NAME'}">专家登录号</th>
            <th lay-data="{field:'SPECIALITY_NAME'}">专家姓名</th>
            <th lay-data="{field:'PROFESSIAL_LEVEL_NAME', }">专业技术职务</th>
            <th lay-data="{field:'NOWUNIT'}">所在单位</th>
            <th lay-data="{field:'PROFESSIAL_NAME'}">专业</th>
            <th lay-data="{toolbar:'#titleTpl',align:'center',}">管理</th>

        </tr>
        </thead>
    </table>

</form>
<script type="text/html" id="titleTpl">
    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="saveEn({{d.ID}})"  style="background-color: #5FB878;color:#fff" lay-event="detail">添加</a>
</script>
</body>
</html>
