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
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
    <script src="<%=basePath%>/static/js/Excel.js" charset="utf-8"></script>
    <style>
        .layui-table-tool-temp{
            padding-right:10px
        }
        .layui-table-cell{
            overflow:visible;
          /*  line-height:20px;*/
            white-space:normal;
            height: auto;
}
</style>
</head>
<body >
<div class="layui-form" >

    <table id="demo" style="display: none" border="1">
        <thead></thead>
        <tbody></tbody>
    </table>


    <table class="layui-table"  lay-data="{id: 'LAY_table_user',toolbar: '#toolbarDemo',text: {none: '暂无相关数据'},title:'评审情况统计表',height: '480px',defaultToolbar:['']}" lay-filter="test" style="height:100%;">
        <thead>
        <tr>
            <th lay-data="{field:'PROFESSIAL_NAME',align:'center', width:100}" rowspan="3">学科</th>
            <th lay-data="{field:'ZS',align:'center', width:40}" rowspan="3">申报数</th>
            <th lay-data="{field:'TGS', align:'center',width:40}" rowspan="3">通过数</th>
            <th lay-data="{field:'TGL',align:'center', width:40 ,templet: function(d){
            return isNaN(parseInt(d.TGS/d.ZS))? 0:parseInt(d.TGS/d.ZS);
            }}" rowspan="3">通过率</th>
            <th lay-data="{align:'center'}" colspan="9">正高</th>
            <th lay-data="{align:'center'}" colspan="9">副高</th>
            <th lay-data="{align:'center'}" colspan="9">中级</th>
        </tr>
        <tr>
            <th lay-data="{field:'AUM',align:'center', width:40}" rowspan="2">申报数</th>
            <th lay-data="{field:'TAUM',align:'center', width:40}"  rowspan="2">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TAUM/d.AUM))? 0:parseInt(d.TAUM/d.AUM);
            }}"  rowspan="2">通过率</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">正常</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">破格</th>
            <th lay-data="{field:'BUM', width:40}" rowspan="2">申报数</th>
            <th lay-data="{field:'TBUM',align:'center', width:40}"  rowspan="2">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TBUM/d.BUM))? 0:parseInt(d.TBUM/d.BUM);
            }}"  rowspan="2">通过率</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">正常</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">破格</th>
            <th lay-data="{field:'CUM',align:'center', width:40}" rowspan="2">申报数</th>
            <th lay-data="{field:'TCUM',align:'center', width:40}"  rowspan="2">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TCUM/d.CUM))? 0:parseInt(d.TCUM/d.CUM);
            }}"  rowspan="2">通过率</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">正常</th>
            <th lay-data="{field:'',align:'center', width:80}" colspan="3">破格</th>
        </tr>
        <tr>
            <th lay-data="{field:'AZ',align:'center', width:40}">申报数</th>
            <th lay-data="{field:'TAZ',align:'center', width:40}">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TAZ/d.AZ))? 0:parseInt(d.TAZ/d.AZ);
            }}">通过率</th>
            <th lay-data="{field:'AT',align:'center', width:40}">申报数</th>
            <th lay-data="{field:'TAT', align:'center',width:40}">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TAT/d.AT))? 0:parseInt(d.TAT/d.AT);
            }}">通过率</th>
            <th lay-data="{field:'BZ',align:'center', width:40}">申报数</th>
            <th lay-data="{field:'TBZ',align:'center', width:40}">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TBZ/d.BZ))? 0:parseInt(d.TBZ/d.BZ);
            }}">通过率</th>
            <th lay-data="{field:'BT',align:'center', width:40}">申报数</th>
            <th lay-data="{field:'TBT', align:'center',width:40}">通过数</th>
            <th lay-data="{field:'', align:'center',width:40,templet: function(d){
            return isNaN(parseInt(d.TBT/d.BT))? 0:parseInt(d.TBT/d.BT);
            }}">通过率</th>
            <th lay-data="{field:'CZ', align:'center',width:40}">申报数</th>
            <th lay-data="{field:'TCZ',align:'center', width:40}">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TCZ/d.CZ))? 0:parseInt(d.TCZ/d.CZ);
            }}">通过率</th>
            <th lay-data="{field:'CT',align:'center', width:40}">申报数</th>
            <th lay-data="{field:'TCT', align:'center',width:40}">通过数</th>
            <th lay-data="{field:'',align:'center', width:40,templet: function(d){
            return isNaN(parseInt(d.TCT/d.CT))? 0:parseInt(d.TCT/d.CT);
            }}">通过率</th>
        </tr>
        </thead>
    </table>
</div>
<script type="text/html" id="toolbarDemo" class="demoTable" style="padding-right:20px">
    <div class="layui-input-inline" style="">
        <div class="layui-inline" style="width:400px!important;float:left">
            <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会" lay-filter="JUDGING_CODE" lay-verify="required" lay-search="">
            </select>
        </div>
        <div class="layui-input-inline">
            <input type="text" class="layui-input" placeholder="请输入年份" name="YEAR_NO" id="YEAR_NO" lay-verify="required">
        </div>
        <button class="layui-btn" lay-filter="sss"  lay-submit >搜索</button>
    </div>
    <div class="layui-input-inline" style="float:right">
        <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="JttableToExcel('评审情况统计表','demo')" ><i class="layui-icon layui-icon-export"></i></button>
        <button  onclick="preview(11)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon layui-icon-print"></i></button>
    </div>
</script>
<script>
    var table;
    var form;
    $(function () {
        layui.use(['form','table','laydate'], function(){
             form =layui.form;
             table = layui.table;
            var laydate = layui.laydate;
            var $ = layui.$;
            laydate.render({ elem: '#YEAR_NO' ,type: 'year'  });
            form.on('submit(sss)', function(data){
                table.reload('LAY_table_user', {
                    url:'<%=basePath%>/Reprot/assessmentStatisticsList.do',
                    where: data.field,
                    defaultToolbar:[],
                    done:function (dd) {
                        $("#demo thead").html('');
                        $("#demo tbody").html('');
                        var thead=$(".layui-table-header .layui-table thead").html();
                        var tbody=$(".layui-table-body .layui-table tbody").html();
                        $("#demo thead").prepend(thead);
                        $("#demo tbody").prepend(tbody);
                        $('#YEAR_NO').val(data.field.YEAR_NO);
                        tree(data.field.JUDGING_CODE);
                        form.render('select');
                    }
                });
                laydate.render({ elem: '#YEAR_NO' ,type: 'year'  });
            });
            tree('');
        });
    });
    function tree(JUDGING_CODE){
        $.ajax({
            url: '<%=basePath%>/Speciality/getJudgingTree.do',
            dataType:'json',
            success: function (returnData) {
                var html='<option value=""></option> ';
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
