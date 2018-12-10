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
<div class="layui-form" style="padding: 10px 0 0 10px" lay-filter="pwh">
    <div class="layui-inline"style="width:300px;"  lay-filter="JUDGING_CODE" >
        <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会"lay-search="" >
        </select>
    </div>
    <button class="layui-btn" data-type="reload">搜索</button>
</div>
<table class="layui-hide" id="LAY_table_user" lay-filter="test"></table>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-group">
        <% if(MenuBtns.indexOf("Insert")>-1) { %>
        <button class="layui-btn layui-btn-sm" lay-event="add" title="新增"><i class="layui-icon"></i></button><% } %>
        <% if(MenuBtns.indexOf("Update")>-1) { %>
        <button class="layui-btn layui-btn-sm"  lay-event="update"title="修改"><i class="layui-icon"></i></button><% } %>
        <% if(MenuBtns.indexOf("Delete")>-1) { %>
        <button class="layui-btn layui-btn-sm"lay-event="delete" title="删除"><i class="layui-icon"></i></button><% } %>
        <%--<button class="layui-btn layui-btn-sm"><i class="layui-icon"></i></button>--%>
    </div>
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
            ,url: '<%=basePath%>/SubjectGroup/getList.do'
            ,toolbar: '#toolbarDemo'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'},
                {field:'groupNo', title: '组号', sort: true}
                ,{field:'groupName', title: '组名'}
                ,{field:'yearNo', title: '年度'}
                ,{field:'professialId', title: '下属专业'}
                ,{field:'back1', title: '备注'}
            ]],
            height: 'full-68',
            text: {none: '暂无相关数据'}
            ,id: 'testReload'
            ,page: true
        });

        //头工具栏事件
        table.on('toolbar(test)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            if($("#JUDGING_CODE").val()==""){
                layer.msg("请选择评委会！");
                return
            }
            switch(obj.event){
                case 'add':
                    $("#professial").html(null);
                    var professial = selectPlus.render({
                        el: '#professial',
                        data:returnHtml({JUDGING_CODE:$("#JUDGING_CODE").val()}),
                        valueName: "name",
                        valueSeparator: ","
                    })
                    var data='&judgingCode='+$("#JUDGING_CODE").val();
                    openlay('添加专业组','myadd','<%=basePath%>/SubjectGroup/save.do',data);
                    break;
                case 'update':
                    var data = checkStatus.data;
                    if(data.length>1||data.length==0){
                        layer_upda("请选择一条数据！");
                        return ;
                    }
                    $("#professial").html(null);
                    var cz={id:data[0].id};
                    var returndata=ajax('<%=basePath%>/SubjectGroup/addOrUpdate.do',cz);
                    var professial = selectPlus.render({
                        el: '#professial',
                        data:returnHtml({id:data[0].id,JUDGING_CODE:$("#JUDGING_CODE").val()}),
                        valueName: "name",
                        values:data[0].professialId.split(","),
                        valueSeparator: ","
                    })
                    //$('#myadd').form('load',returndata);
                    $("#groupName").val(returndata.groupName);
                    $("#yearNo").val(returndata.yearNo);
                    $("#groupNo").val(returndata.groupNo);
                    $("#back1").val(returndata.back1);
                    $("#professialId").val(returndata.professialId);
                    var cz='&judgingCode='+$("#JUDGING_CODE").val()+'&id='+data[0].id;
                    openlay('修改专业组','myadd','<%=basePath%>/SubjectGroup/save.do',cz);
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
                        ids.push(data[i].id);
                        names.push(data[i].groupName);
                    }
                    Delete(ids.join(','), names.join(','));
                    break;
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选': '未全选');
                    break;
            };
        });
        var $ = layui.$, active = {
            reload: function(){
                var JUDGING_CODE = $('#JUDGING_CODE').val();
                table.reload('testReload', {  page: {   curr: 1  }  ,where: { judgingCode: JUDGING_CODE }   });
            }
        };
        $('.layui-form .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
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
        selectPlus.on('selectPlus(professial)', function(obj){
            var oo='';
            $.each(obj.checkedData, function(){
                oo+=this.id+',';
            });
            $("#professialId").val(oo);
        })
    });
    function returnHtml(data){
        var html= new Array();
        var returnData=ajax( '<%=basePath%>/SubjectGroup/getProfessialByGroup.do',data);
        for(var i=0;i<returnData.length;i++){
            var obj =person=new Object();
            obj.name=returnData[i].PROFESSIAL_NAME;
            obj.vue=returnData[i].PROFESSIAL_CODE;
            obj.id=returnData[i].PROFESSIAL_CODE;
            html.push(obj);
        }
        return html;
    }
    function Delete(ids, names) {
        layer.confirm('确认要删除选择项？【' + names + '】', {
            btn: ['确定','取消'] //按钮
        }, function(){
                var returnData=ajax('<%=basePath%>/SubjectGroup/delete.do',{ids: ids});
                layer_dele(returnData.message,'');
                table.reload('testReload');
        });
    }

</script>

<form class="layui-form layui-form-pane" action="" title="专业组维护窗口"  style="display:none" id="myadd">
    <div class="layui-form-item">
        <label class="layui-form-label">组  名：</label>
        <div class="layui-input-inline">
            <input type="text" id="groupName" name="groupName" required  lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">组  号：</label>
        <div class="layui-input-inline">
            <input type="text" name="groupNo" id="groupNo" required  lay-verify="required" placeholder="请输入组号" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">年  度：</label>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" placeholder="请输入年份" name="yearNo" id="yearNo" lay-verify="required">
        </div>
    </div>
    <input type="hidden" id="professialId" name="professialId">
    <div class="layui-form-item">
        <label class="layui-form-label">评审专业：</label>
        <div class="layui-input-block" id="professial" lay-filter="professial">
        </div>
    </div>
       <div class="layui-form-item layui-form-text">
           <label class="layui-form-label">备  注：</label>
           <div class="layui-input-block">
               <textarea placeholder="请输入内容" id="back1" name="back1" class="layui-textarea"></textarea>
           </div>
       </div>
</form>
</body>
</html>
