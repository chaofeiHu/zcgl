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
    <title>申报信息标准统计</title>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
    <script src="<%=basePath%>/static/js/Excel.js" charset="utf-8"></script>
    <style>
        .ranktable{ width:100%; }
        .ranktable th,.ranktable td{text-align: center }
        .ranktable th{ padding:0;font-size:14px; color:#333; line-height:50px; font-weight:normal;}
        .ranktable td{ padding:10px; font-size:13px; color:#666; line-height:20px; text-align:center;}
        .ranktable .column{ width:120px; position:relative; z-index:1; line-height:20px; height:50px; overflow:hidden;}
        .ranktable .column .t1{ position:absolute; top:5px; right:20px;}
        .ranktable .column .t2{ position:absolute; bottom:5px; left:20px;}
        .ranktable .column .line{ position:absolute; top:-50px; left:60px; width:1px; height:150px; background:#ddd;transform:rotate(68deg);-ms-transform:rotate(68deg);-moz-transform:rotate(68deg);-webkit-transform:rotate(-68deg);-o-transform:rotate(-68deg);}
    </style>
</head>
<body >
<div class="layui-form" >

    <table id="demo" style="display: none" border="1">
        <thead></thead>
        <tbody></tbody>
    </table>

    <div class="demoTable" style="margin-top: 10px;padding-left: 20px;text-align: center">
        <div class="layui-input-inline">
            <input type="text" class="layui-input" placeholder="请输入年份" id="YEAR_NO" lay-verify="required">
        </div>
        <button class="layui-btn" data-type="xilie">系列级别</button>
        <button class="layui-btn" data-type="xueli">学历级别</button>
        <button class="layui-btn" data-type="dw">单位级别</button>
        <button class="layui-btn" data-type="danwei">单位系列</button>
       <%-- <button class="layui-btn" data-type="year">年龄级别</button>--%>
    </div>
    <div class="layui-form" style="padding:10px 20px 20px 20px;text-align: center">
            <div  class="layui-elem-field" style="border-style:none;text-align: right;padding-right: 20px;display:none">
                <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="JttableToExcel('123','demo')" ><i class="layui-icon layui-icon-export"></i></button>
                <button  onclick="preview(1)" class="layui-btn layui-btn-primary layui-btn-sm"><i class="layui-icon layui-icon-print"></i></button>
            </div>
        <!--startprint1-->
            <table class="layui-table ranktable"id="xlsb">
            </table>
        <!--endprint1-->
    </div>
</div>
<script>
    var table;
    var form;
    $(function () {

        layui.use(['form','table','laydate'], function(){
             form =layui.form;
             table = layui.table;
            var laydate = layui.laydate;
            laydate.render({
                elem: '#YEAR_NO'
                ,type: 'year'
            });
            var $ = layui.$, active = {
                xilie: function(){
                    load(1,'系列','级别');

                },
                xueli:function(){
                    load(2,'学历','级别');
                },
                dw:function(){
                    load(3,'单位','级别');
                },
                danwei:function(){
                    load(4,'单位','系列');
                },
               /* year:function(){
                    load(5,'年龄','级别');
                },*/
            };

            $('.demoTable .layui-btn').on('click', function(){
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            });
        });

    });
    function load(num,t1,t2){
        if($("#YEAR_NO").val()==""){
            layer.msg("请选择年份！");
            return false;
        }
        //方法级渲染
        $("#demo thead").html('');
        $("#demo tbody").html('');
        tabAdd(num,t1,t2);
        $(".layui-elem-field").css("display","block");
    }
    function tabAdd(num,t1,t2){
        $.ajax({
            url: '<%=basePath%>/Reprot/declarationInformationList.do',
            data:{num:num,YEAR_NO:$("#YEAR_NO").val()},
            dataType:'json',
            success: function (data) {
               var _html='';
               var jb=data[0].jb;
               var xl=data[0].xl;
               var num=data[0].num;
                _html+='<thead>' +
                    '</tr><tr><th width="120"><div class="column"><div class="t1">'+t2+'</div>' +
                    '<div class="t2">'+t1+'</div><div class="line"></div></div></th>';

                for(var j=0;j<jb.length;j++){
                    _html+='<th >'+jb[j]+'</th>';
                }
                _html+='</tr></thead><tbody>';
                for(var k=0;k<xl.length;k++){
                    _html+='<tr><td>'+xl[k]+'</td>';

                    for(var j=0;j<jb.length;j++) {
                        var va=0;
                        for (var i = 0; i < num.length; i++) {
                            if (xl[k] == num[i].XL &&jb[j]==num[i].JB) {
                                va=1;
                                _html += '<td>' + num[i].COU + '</td>';
                            }
                        }
                        if(va==0){
                            _html += '<td>0</td>';
                        }
                    }
                    _html+='</tr>';
                }
                $("#xlsb").html(_html+'</tbody>');
                var thead=$(".layui-table thead").html();
                var tbody=$(".layui-table tbody").html();
                $("#demo thead").prepend(thead);
                $("#demo tbody").prepend(tbody);
            }
        });
    }
</script>
</body>
</html>
