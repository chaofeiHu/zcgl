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
<input type="hidden" name="GROUP_ID" id="GROUP_ID"   placeholder="请输入单位名称" class="layui-input">
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

    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="manual({{d.ID}},'{{d.GROUP_NAME}}')"  style="background-color: #5FB878;color:#fff" lay-event="detail">专家手工分组</a>

    <a class="layui-btn layui-btn-primary layui-btn-xs"  onclick="Random('{{d.PROFESSIAL_ID}}',{{d.ID}},'{{d.GROUP_NAME}}')" style="background-color: #5FB878;color:#fff" lay-event="edit">随机抽取专家</a>

    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="renyuan({{d.ID}},'{{d.GROUP_NAME}}')"  style="background-color: #5FB878;color:#fff" lay-event="detail">人员展示</a>

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
            ,url: '<%=basePath%>/SpecialityGroup/getList.do'
            ,toolbar: '#toolbarDemo'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'},
                {field:'GROUP_NAME', title: '组名' , fixed: 'left'}
                ,{field:'YEAR_NO', title: '年度', fixed: 'left'}
                ,{field:'PROFESSIAL_NAME', title: '下属专业' }
                ,{field:'BACK2', title: '分组人数'}
                ,{field:'BACK1', title: '评委会总人数'}
                ,{title: '管理', align:'center',width:300, toolbar: '#barDemo', fixed: 'right'}
            ]],
            height: 'full-68',
            text: {none: '暂无相关数据'}
            ,id: 'testReload'
            ,page: true
        });
        <%--父页面搜索--%>
        form.on('submit(tabl)', function(data){
            console.log(data.field);
            var JUDGING_CODE = $('#JUDGING_CODE').val();
            var SPECIALITY_NAME=$('#SPECIALITY_NAME').val();
            var NOWUNIT=$('#NOWUNIT').val();
            table.reload('testReload', {  page: {   curr: 1  }  ,where:data.field   });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--手工专家搜索--%>
        form.on('submit(tabladd)', function(data){
            console.log(data.field);
            table.reload('test1', {
                page: {   curr: 1  }
                ,where: data.field
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--人员展示搜索--%>
        form.on('submit(select)', function(data){
            console.log(data.field);
            table.reload('split', {
                page: {   curr: 1  }
                ,where: data.field
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        form.on('select(zhuanye)', function(data){
            var pro=data.value;
            if(data.value==""){
                pro=$("#professialId").val();
            }
         var returnData=ajax('<%=basePath%>/SpecialityGroup/selectSubjectSpecialityCount.do',{groupId:$("#GROUP_ID").val(),professialId:pro});
            $('#allSpecialityCount').val(returnData);
        });
        <%--监听是否出席--%>
        form.on('switch(sexDemo)', function(obj){
            var type;
            if(obj.elem.checked){  type=0; }else{  type=1; }
            var returndata= ajax("<%=basePath%>/SpecialityGroup/updateSpecialityGroup.do",{ID:this.value,TYPE:type});
            if(returndata.isOk!=1){layer.msg(returndata.message);}
            table.reload('split');
        });
        <%--监听不出席理由编辑--%>
        table.on('edit(split)', function(obj){
            console.log(obj);
            var value = obj.value //得到修改后的值
                ,data = obj.data; //得到所在行所有键
            var returndata= ajax("<%=basePath%>/SpecialityGroup/updateSpecialityGroup.do",{ID:data.BSPID,REASON:value});
            if(returndata.isOk!=1){layer.msg(returndata.message);}
            table.reload('split');
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

    <%--删除信息--%>
    function Delete() {
        var checkStatus = table.checkStatus('split');
        console.log(checkStatus);
        var ids = [];
        var names = [];
        for (var i = 0; i < checkStatus.data.length; i++) {
            ids.push(checkStatus.data[i].BSPID);
            names.push(checkStatus.data[i].SPECIALITY_NAME);
        }
        console.log(ids);
       layer.confirm('确认要删除选择项？【' + names + '】', {
            btn: ['确定','取消'] //按钮
        }, function(){
            var returnData=ajax('<%=basePath%>/SpecialityGroup/delete.do',{id: ids.join(',')});
            layer_dele(returnData.message,'');
           table.reload('split');
           table.reload('testReload');
        });
    }
    <%--打开 专家手工分组 窗口--%>
    function manual(GROUP_ID,title){
        $("#GROUP_ID").val(GROUP_ID);
        table.reload('test1', {
            url: '<%=basePath%>/SpecialityGroup/selectManualGroup.do',
            where: {GROUP_ID:GROUP_ID}
        });
        layer.open({
            type: 1,
            title: title,
            area: ['80%', '100%'], //宽高
            content: $("#myadd"),
        });
    }
    <%--手工添加专家--%>
    function saveEn(id){
        var returnData=ajax('<%=basePath%>/SpecialityGroup/save.do',{SPECIALITY_ID:id,JUDGING_CODE:$("#JUDGING_CODE").val(),GROUP_ID:$("#GROUP_ID").val()})
        layer.alert(returnData.message);
        table.reload('testReload');
        table.reload('test1');
    }
    <%--打开 人员展示 窗口--%>
    function renyuan(GROUP_ID,title){
        $("#GROUP_ID").val(GROUP_ID);
        table.reload('split', {
            url: '<%=basePath%>/SpecialityGroup/getSpecialityList.do',
            height: 'full-120',
            where: {GROUP_ID:GROUP_ID,sta:2}
        });
        layer.open({
            type: 1,
            title: title,
            maxmin: true, //开启最大化最小化按钮
            area: ['80%', '400px'], //宽高
            content: $("#renyuan"),
        });
    }
    <%--修改专业组职务--%>
    function updaSpli(id,GROUP_ID){
        ajax("<%=basePath%>/SpecialityGroup/updateGroup.do",{id:id,GROUP_ID:GROUP_ID});
        table.reload('split');
    }
    <%--打开 随机抽取专家 窗口--%>
    function Random(PROFESSIAL_ID,GROUP_ID,title){
        $("#GROUP_ID").val(GROUP_ID);
        $("#professialId").val(PROFESSIAL_ID);
        $.ajax({
            url: '<%=basePath%>/SpecialityGroup/selectProfessial.do',
            data:{professialId:PROFESSIAL_ID},
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
        var returnData=ajax('<%=basePath%>/SpecialityGroup/selectSubjectSpecialityCount.do',{groupId:GROUP_ID,professialId:PROFESSIAL_ID});
        $('#allSpecialityCount').val(returnData);
        layer.open({
            type: 1,
            title: title,
            area: ['460px','100%'], //宽高
            content: $("#myRom"),
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                var JUDGING_CODE=$("#JUDGING_CODE").val();
                var allSpecialityCount=$("#allSpecialityCount").val();
                var specialityCount=$("#specialityCount").val();
                var zhuanye=$("#zhuanye").val();
                if(parseInt(specialityCount)>parseInt(allSpecialityCount)){
                    layer.msg("不能超过可供分配专家数");
                    return ;
                }
                var pro=zhuanye;
                if(zhuanye==""){
                    pro=PROFESSIAL_ID;
                }
                if(specialityCount!=0){
                    returnData= ajax('<%=basePath%>/SpecialityGroup/getSpecialityByRandom.do',{JUDGING_CODE:JUDGING_CODE,specialityCount:specialityCount,GROUP_ID:GROUP_ID,professialId:pro})
                    layer.alert(returnData.message);
                    table.reload('testReload');
                    layer.close(index);
                }
            }
        });
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

<form class="layui-form layui-form-pane" action="" title="手工添加专家窗口"  style="display:none" id="myadd">
    <div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="Manual">
        <div class="layui-inline"style="width:200px;" >
            <input type="text" name="SPECIALITY_NAME" id="specialityName1"   placeholder="请选择专家姓名" class="layui-input">
        </div>
        <div class="layui-inline"style="width:200px;" >
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
          <%--  <th lay-data="{field:'LOGIN_NAME'}">专家登录号</th>--%>
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

<form class="layui-form layui-form-pane" action="" title="人员展示窗口"  style="display:none" id="renyuan">
    <div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="Manual">
        <div class="layui-inline"style="width:200px;" >
            <input type="text" name="SPECIALITY_NAME" id="SPECIALITY_NAME"   placeholder="请选择专家姓名" class="layui-input">
        </div>
        <button class="layui-btn layui-btn-sm" lay-submit lay-filter="select">搜索</button>
        <button class="layui-btn layui-btn-sm" type="button" onclick="Delete()" title="删除"><i class="layui-icon"></i></button>
    </div>
    <table class="layui-table" lay-data="{ height: 'full-20',page:true, id:'split'}" lay-filter="split">
        <thead>
        <tr>
            <th lay-data="{checkbox:true,fixed: 'left'}"></th>
            <th lay-data="{field:'SPECIALITY_NAME',width:120,fixed: 'left'}">专家姓名</th>
            <th lay-data="{field:'MOBILEPHONE',width:120,fixed: 'left'}">手机号码</th>
            <th lay-data="{field:'TEL',width:120}">办公电话</th>
            <th lay-data="{field:'PROFESSIAL_LEVEL_NAME',width:120 }">专业技术职务</th>
            <th lay-data="{field:'PROFESSIAL_DUTY_NAME',width:120}">专业技术职务等级</th>
            <th lay-data="{field:'IDCARDNO',width:120}">身份证号</th>
            <th lay-data="{field:'SEX',width:120,templet:'#SEX'}">性别</th>
            <th lay-data="{field:'GROUP_LEADER',width:120,templet:'#GROUP_LEADER'}">职务</th>
            <th lay-data="{field:'TYPE',width:120,templet:'#TYPE',fixed: 'right'}">是否出席</th>
            <th lay-data="{field:'REASON',width:120,edit: 'text',fixed: 'right'}">不出席理由</th>
            <th lay-data="{toolbar:'#titleTpl1',align:'center',width:120,fixed: 'right'}">管理</th>
        </tr>
        </thead>
    </table>

</form>
<script type="text/html" id="titleTpl1">
    {{#  if(d.GROUP_LEADER!=0&&d.TYPE!=1){ }}
    <a class="layui-btn layui-btn-primary layui-btn-xs" onclick="updaSpli({{d.ID}},{{d.GROUP_ID}})"  style="background-color: #5FB878;color:#fff" lay-event="detail">设为组长</a>
    {{#  } }}
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
<script type="text/html" id="TYPE">
    <input type="checkbox" name="TYPE" value="{{d.BSPID}}" lay-skin="switch" lay-text="出席|不出席"  {{ d.TYPE==0 ? 'checked':''}}  lay-filter="sexDemo" >
</script>
<script type="text/html" id="GROUP_LEADER">
    {{#  if(d.GROUP_LEADER==0){ }}
        组长
    {{#  }else{ }}
        组员
    {{#  } }}
</script>

</body>
</html>
