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
    <title>评审流程控制</title>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <style>
        .ystep-container { font-family: "Helvetica Neue",Helvetica,"Hiragino Sans GB","Wenquanyi Micro Hei","Microsoft Yahei",Arial,sans-serif;
            padding-top: 50px;
        }
        .ystep-container ul:after {visibility:hidden;display:block;font-size:0;content:" ";clear:both;height:0}
        .ystep-container ul { zoom:1; }
        .ystep-container ul,.ystep-container li {list-style: none;}
        .ystep-container ul,.ystep-container li,.ystep-container p { margin: 0;padding: 0;}
        .ystep-container li { float: left; width:12.5%; text-align:center; font-size:12px;}
        .ystep-container li .txt{ margin:10px 0; color:#3e88dd; line-height:30px;border: none}
        .ystep-container li .txt span{ display:block; margin:0 auto; width:30px; height:30px; text-align:center; line-height:30px; color:#3e88dd; font-family:Arial, Helvetica, sans-serif; background:#eee; -webkit-border-radius:50%;-moz-border-radius:50%; -ms-border-radius:50%; -o-border-radius:50%; border-radius:50%; }
        .ystep-container .ystep-step-active .txt{ color:#009688;}
        .ystep-container .ystep-step-active .txt span{ background:#009688; color:#fff;}
        .ystep-container .ystep-step-done .txt{ color:#5FB878;}
        .ystep-container .ystep-step-done .txt span{ background:#5FB878; color:#fff;}
        .ystep-progress{ height:12px; background:#f5f5f5;}
        .ystep-progress .pro{ height:12px;background:#36c6d3;}
    </style>
</head>
<body>
<div class="layui-form" style="height:90%;position:absolute; width:100%;">
    <div class="layui-input-inline" style="padding-top:20px;text-align: center;height:20%;display:block">
        <div class="layui-form-label" style="width:120px;display:inline-block;float:none">请选择评委会：</div>
        <div class="layui-inline" style="text-align: left;width:400px;display:inline-block;">
            <select id="JUDGING_CODE" name="JUDGING_CODE" placeholder="请选择评委会" lay-filter="JUDGING_CODE" lay-verify="required" lay-search="">
            </select>
        </div>
    </div>
    <div style="padding:0 20px 20px 20px;height:80%;text-align: center;">
    <fieldset class="layui-elem-field" style="height:100%;text-align: center">
        <legend style="font-weight:bold">评委会</legend>
            <div class="layui-form layui-form-pane"  lay-filter="PROCESS_STATE" style="width:1000px;text-align: center; margin:0 auto;padding-top: 5%;height:20%">
                <div class="layui-form-item">
                    <input type="hidden" id="BID" name="BID">
                    <input type="hidden" id="PROCESS_TYPE" name="PROCESS_TYPE">
                    <label class="layui-form-label">评审方式:</label>
                    <div class="layui-input-inline" >
                        <select name="PROCESS_STATE" id="PROCESS_STATE"  lay-verify="required" >
                            <option value="">请选择评审方式</option>
                            <option value="1">网上评审</option>
                            <option value="2">app评审</option>
                            <option value="3">传统纸质材料评审</option>
                        </select>
                    </div>
                    <label class="layui-form-label">分组方式:</label>
                    <div class="layui-input-inline"    >
                        <select name="PROCESS_GROUP" id="PROCESS_GROUP" lay-verify="required" >
                            <option value="">请选择分组方式</option>
                            <option value="1">先分学科组评审,再评委会评审</option>
                            <option value="2">不分学科组评审,直接评委会评审</option>
                        </select>
                    </div>
                    <label class="layui-form-label">评审模式:</label>
                    <div class="layui-input-inline"    >
                        <select name="PROCESS_PATTERN" id="PROCESS_PATTERN" lay-verify="required" >
                            <option value="">请选择评审模式</option>
                            <option value="0">正常</option>
                            <option value="1">盲评</option>
                        </select>
                    </div>
                    <button class="layui-btn layui-btn-sm" style="padding:0 10px;height:36px" lay-submit="" lay-filter="demo1" >设置</button>
                </div>
            </div>
        <div class="ystep-container" id="shezhi" >
            <ul>
                <li class="ystep-step-undone" id="y1">
                    <div class="txt"><span>1</span><p>设置专业组</p></div>
                </li>
                <li class="ystep-step-undone" id="y2">
                    <div class="txt"><span>2</span><p>人员审查</p></div>
                </li>
                <li class="ystep-step-undone" id="y3">
                    <div class="txt"><span>3</span><p>人员分组</p></div>
                </li>
                <li class="ystep-step-undone" id="y4">
                    <div class="txt"><span>4</span><p>专业组投票</p></div>
                </li>
                <li class="ystep-step-undone" id="y5">
                    <div class="txt"><span>5</span><p>专业组评议</p></div>
                </li>
                <li class="ystep-step-undone" id="y6">
                    <div class="txt"><span>6</span><p>大评委投票</p></div>
                </li>
                <li class="ystep-step-undone" id="y7">
                    <div class="txt"><span>7</span><p>大评委评议</p></div>
                </li>
                <li class="ystep-step-undone" id="y8">
                    <div class="txt"><span>8</span><p>评审结束</p></div>
                </li>
            </ul>
        </div>
        <div class="ystep-container" id="shezhi2" style="display:none">
            <ul>
                <li class="ystep-step-undone" style="width: 25%;" id="y11">
                    <div class="txt"><span>1</span><p>人员审查</p></div>
                </li>
                <li class="ystep-step-undone"style="width: 25%;" id="y12">
                    <div class="txt"><span>2</span><p>大评委投票</p></div>
                </li>
                <li class="ystep-step-undone" style="width: 25%;"id="y13">
                    <div class="txt"><span>3</span><p>大评委评议</p></div>
                </li>
                <li class="ystep-step-undone"style="width: 25%;" id="y14">
                    <div class="txt"><span>4</span><p>评审结束</p></div>
                </li>
            </ul>
        </div>
        <div class="layui-progress layui-progress-big" lay-filter="jdt"  lay-showPercent="true">
            <div class="layui-progress-bar" lay-percent="0%"></div>
        </div>
    </fieldset>
    </div>
</div>
<script>
    var table;
    var form;
    var element;

    var layer;
    $(function () {
        layui.use(['form','table','element','layer'], function(){
            form =layui.form;
            table = layui.table;
            layer=layui.layer;
            var $ = layui.$;
            element= layui.element;
            tree();
            form.on('select(JUDGING_CODE)', function(obj){
                $(".layui-elem-field legend").html($(obj.elem).find("option:selected").text());
                editupdate();
            });
            <%--修改评审模式，分组方式--%>
            form.on('submit', function(data){
                $.ajax({
                    url: '<%=basePath%>/JudgingProcess/AddOrUpdate.do',
                    data: data.field,
                    dataType:"json",
                    success: function (returnData) {
                        layer.msg(returnData.message);
                        editupdate();
                    }
                });
            });
        });
        $(".txt").click(function(){
            var PROCESS_GROUP=$("#PROCESS_GROUP").val();
            var PROCESS_STATE=$("#PROCESS_STATE").val();
            var JUDGING_CODE=$("#JUDGING_CODE").val();
            var PROCESS_PATTERN=$("#PROCESS_PATTERN").val();
            if(JUDGING_CODE==""){
                layer.msg("请选择评委会"); return }
            if(PROCESS_STATE==""){
                layer.msg("请选择评审方式"); return }
            if(PROCESS_GROUP==""){
                layer.msg("请选择分组方式"); return;}
            if(PROCESS_PATTERN==""){
                layer.msg("请选择评审模式"); return;}
            <%--判断是否能够进入下一步--%>
            var zhi=$(this).find("span").html();
            var processType=$("#PROCESS_TYPE").val();
            if(processType=="8"){
                return ;
            }
            var hui=processType-1;
            if(PROCESS_GROUP=="2"){
                if(parseInt(zhi)>1){
                    zhi=parseInt(zhi)+4;
                }else{
                    hui-=4;
                }
                if(processType==1){
                    processType=parseInt(processType)+4;
                }
            }
            var bz=$(this).find("p").html();
            var nu=zhi-1;

            if(processType==nu||zhi==hui){
                layer.confirm('确认进入【'+bz+'】吗？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    $.ajax({
                        url: '<%=basePath%>/JudgingProcess/selectJudgingProcess.do',
                        data: {PROCESS_TYPE:processType,PROCESS_GROUP:PROCESS_GROUP,JUDGING_CODE:JUDGING_CODE,zhi:zhi},
                        dataType:'JSON',
                        success: function (returnData) {
                            if(returnData.msg!="yes"){
                                layer.msg(returnData.msg);
                                return ;
                            }else{
                                    edituu(zhi);
                            }
                        }
                    });
                })
            }


        })
    });
    <%--修改评审阶段--%>
    function edituu(zhi){
            var BID=$("#BID").val();
            var PROCESS_GROUP=$("#PROCESS_GROUP").val();
            $.ajax({
                url: '<%=basePath%>/JudgingProcess/AddOrUpdate.do',
                data: {BID:BID,PROCESS_TYPE:zhi,PROCESS_GROUP:PROCESS_GROUP,JUDGING_CODE:$("#JUDGING_CODE").val(),PROCESS_PATTERN:$("#PROCESS_PATTERN").val()},
                dataType:'JSON',
                success: function (returnData) {
                    layer.msg(returnData.message);
                    editupdate();
                }
            });
    }
    <%--读取评委会信息并展示--%>
    function editupdate(){
        $.ajax({
            url: '<%=basePath%>/JudgingProcess/selectProcess.do',
            data: {JUDGING_CODE:$("#JUDGING_CODE").val()},
            dataType:"json",
            success: function (data) {
                $("#BID").val(data["id"]);
                $("#PROCESS_TYPE").val(data["processType"]);
                $("#PROCESS_STATE").val(data["processState"]);
                $("#PROCESS_GROUP").val(data["processGroup"]);
                $("#PROCESS_PATTERN").val(data["processPattern"]);
                form.render('select','PROCESS_STATE');
                var nu=parseInt(data['processType']);
                if(nu==undefined||isNaN(nu)){
                    nu=0;
                }
                if(data["processGroup"]=="2"){
                    $("#shezhi2").css("display","block");
                    $("#shezhi").css("display","none");
                    if(nu>=6){
                        element.progress('jdt',((nu-4)/4)*100+'%');
                    }else{
                        element.progress('jdt', '25%');
                    }
                }else{
                    $("#shezhi").css("display","block");
                    $("#shezhi2").css("display","none");
                    element.progress('jdt', (nu/8)*100+'%');
                }
                for(var i=1;i<=8;i++){
                    var va="";
                    if(data["processGroup"]=="2"){
                        if(nu>=6){  nu=nu-4; }
                    }
                    data["processGroup"]=="2" ? va="#y1":va="#y";
                    if(i<=nu-1){
                        $(va+i).attr("class","ystep-step-done");
                    }
                    if(i==nu){
                        $(va+i).attr("class","ystep-step-active");
                    }
                    if(nu<i){
                        $(va+i).attr("class","ystep-step-undone");
                    }
                }
            }
        })
    }
    <%--加载评委会信息--%>
    function tree(){
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
                form.render('select');
            }
        });
    }
</script>
</body>
</html>