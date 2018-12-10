<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;Charset=utf-8;" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>参评人员审查</title>
    <link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/Method.js"></script>
    <script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
    <style>
        .jiac{font-weight:bold;font-size: 12px;color:#000}
         #tabl td{text-align: center}
        .layui-layer-photos{top:100px!important;text-align: center}
        .layui-table td, .layui-table th{font-size:12px}
        .layui-table th{font-weight:bold;color:#000}
        #neirong{margin:10px}
        .layui-anim-upbit{top: auto!important; bottom: 42px!important;}
    </style>
</head>
<body>

<form class="layui-form layui-form-pane" action="" style="text-align: center;width:98%;">
    <div class="layui-row">
        <div class="layui-col-xs10" style="padding-left: 10px;">
            <table class="layui-table" style="height: 115px;">
                <tr>
                    <td><span class="jiac">姓名：</span><span id="displayName"></span></td>
                    <td><span class="jiac">性别：</span><span id="sex"></span></td>
                    <td><span class="jiac">民族：</span><span id="nation"></span></td>
                    <td><span class="jiac">身份证号：</span><span id="idCardNo"></span></td>
                </tr>
                <tr>
                    <td><span class="jiac">申报方式：</span><span id="pingshentype"></span></td>
                    <td><span class="jiac">申报系列：</span><span id="reviewSeries"></span></td>
                    <td><span class="jiac">申报资格：</span><span id="proposePosition"></span></td>
                    <td><span class="jiac">申报专业：</span><span id="shenbaomajorId"></span></td>
                </tr>
                <tr>
                    <td><span class="jiac">单位：</span><span id="unitName"></span></td>
                    <td><span class="jiac">政治面貌：</span><span id="politicalOutlook"></span></td>
                    <td><span class="jiac">手机号：</span><span id="mobilephone"></span></td>
                    <td><span class="jiac">预分配评委会：</span><span id="judgingName"></span></td>
                </tr>
            </table>
        </div>
        <div class="layui-col-xs2" style="padding:0px 0px 0px 10px">
            <fieldset class="layui-elem-field" style="margin-top: 10px;height: 115px">
                <div id="myImguserpic" class="layer-photos-demo">
                    <img style="width:100%;height:100%;padding: 0px;" layer-src="http://localhost:8088/upload/gg.jpg" src="" alt="图片名">
                </div>
            </fieldset>
        </div>
    </div>
    <table class="layui-table" id="tabl"  style="margin:0px 0px 10px 10px;display:block">
        <tr>
            <td class="jiac">学历：</td>
            <td><span id="eduEdu" name="eduEdu"></span></td>
            <td class="jiac">学位：</td>
            <td><span id="eduDegree" name="eduDegree"></span></td>
            <td class="jiac">专业：</td>
            <td><span id="eduMajor" name="eduMajor"></span></td>
        </tr>
        <tr>
            <td  class="jiac">从事专业：</td>
            <td><span id="major" name="major"></span></td>
            <td class="jiac">开始工作时间：</td>
            <td><span id="startworktime" name="startworktime"></span></td>
            <td class="jiac">现从事事业：</td>
            <td><span id="nowjob" name="nowjob"></span></td>
        </tr>
        <tr>
            <td class="jiac">掌握外语：</td>
            <td><span id="foreignlanguages" name="foreignlanguages"></span></td>
            <td class="jiac">掌握外语等级：</td>
            <td><span id="foreignlanguageslevel" name="foreignlanguageslevel"></span></td>
            <td class="jiac">外语证书取得时间：</td>
            <td><span id="foreignlanguagestime" name="foreignlanguagestime"></span></td>
        </tr>
        <tr>
            <td rowspan="2" class="jiac">现任专业技术职务：</td>
            <td class="jiac">职务:</td>
            <td class="jiac">系列：</td>
            <td class="jiac">级别:</td>
            <td class="jiac">取得时间：</td>
            <td class="jiac">聘任时间:</td>
        </tr>
        <tr>
            <td><span  name="currentpost" id="currentpost"></span></td>
            <td><span  name="currentpostseries" id="currentpostseries"></span></td>
            <td><span  name="currentpostlevel" id="currentpostlevel"></span></td>
            <td><span  name="currentpostgettime" id="currentpostgettime"></span></td>
            <td><span  name="currentpostengagetime" id="currentpostengagetime"></span></td>
        </tr>
        <tr>
            <td rowspan="2" class="jiac">其他专业技术职务：</td>
            <td class="jiac">职务：</td>
            <td class="jiac">系列：</td>
            <td class="jiac">级别：</td>
            <td colspan="2" class="jiac">聘任时间：</td>
        </tr>
        <tr>
            <td><span  name="otherpost" id="otherpost"></span></td>
            <td><span  name="otherpostseries" id="otherpostseries"></span></td>
            <td><span  name="otherpostlevel" id="otherpostlevel"></span></td>
            <td  colspan="2"><span  name="otherpostengagetime" id="otherpostengagetime"></span></td>
        </tr>
        <tr>
            <td rowspan="2" class="jiac">兼任行政职务：</td>
            <td class="jiac">职务：</td>
            <td><span  name="administrativepost"  id="administrativepost"></span></td>
            <td rowspan="2" class="jiac">担任学术团队职务或社会兼职：</td>
            <td rowspan="2" class="jiac">职务/兼职：</td>
            <td rowspan="2"><span  name="socialpost"  id="socialpost"></span></td>
        </tr>
        <tr>
            <td class="jiac">时间：</td>
            <td ><span name="administrativeposttime" id="administrativeposttime"></span> </td>
        </tr>
        <tr>
            <td colspan="6" class="jiac">任现职近5年来年度考核情况</td>
        </tr>
        <tr>
            <td class="jiac">年份：</td>
            <td><span  name="yearsone" id="yearsone"></span><span>年</span></td>
            <td><span  name="yearstwo" id="yearstwo"></span><span>年</span></td>
            <td><span  name="yearsthree" id="yearsthree"></span><span>年</span></td>
            <td><span  name="yearsfour" id="yearsfour"></span><span>年</span></td>
            <td><span  name="yearsfive" id="yearsfive"></span><span>年</span></td>
        </tr>
        <tr>
            <td class="jiac">情况：</td>
            <td><span  name="yearsoneinfo" id="yearsoneinfo"></span></td>
            <td><span  name="yearstwoinfo" id="yearstwoinfo"></span></td>
            <td><span  name="yearsthreeinfo" id="yearsthreeinfo"></span></td>
            <td><span  name="yearsfourinfo" id="yearsfourinfo"></span></td>
            <td><span  name="yearsfiveinfo" id="yearsfiveinfo"></span></td>
        </tr>
    </table>


</form>

<div class="layui-collapse" id="neirong">
    <div class="layui-colla-item" id="edu" style="display:block">
        <h2 class="layui-colla-title"  style="color: #0e90d2;font-size: 18px;font-weight: bolder">学习经历</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableEdu">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="work" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">工作经历</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableWork">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="train" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">培训经历</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableTra">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item"  id="award" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">奖励</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableAwa">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="patent" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">专利</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTablePat">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="project" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">项目/课题</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTablePro">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="paper" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">论文</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTablePap">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="book" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">著作</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableBook">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="research" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">其他成果</h2>
        <div class="layui-colla-content layui-show">
            <table class="layui-table" id="cusTableRes">
                <thead class="layui-table-header"></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <div class="layui-colla-item" id="summary" style="display:block">
        <h2 class="layui-colla-title" style="color: #0e90d2;font-size: 18px;font-weight: bolder">个人总结内容</h2>
        <div class="layui-colla-content layui-show">
            <div name="summarytext" id="summarytext"
                 style="border: solid 1px #dddddd;padding-left: 10px;line-height: 37px;height:100px"></div>
        </div>
    </div>
</div>
<form class="layui-form layui-form-pane" id="myshencha">
<fieldset class="layui-elem-field" style="margin: 10px">
    <legend style="color: #0e90d2;font-size: 18px;font-weight: bolder">审查</legend>
        <div class="layui-input-block" style="text-align: center;margin-left: 0px" >
            <input type="radio" name="finalResult" title="通过" checked="" value="1"lay-filter="encrypt">
            <input type="radio" name="finalResult" title="未通过" value="0"lay-filter="encrypt">
            <input type="radio" name="finalResult"  title="退回修改" value="2"lay-filter="encrypt">
            <input type="radio" name="finalResult" title="加入黑名单" value="4"lay-filter="encrypt">
        </div>
        <div class="layui-form-item layui-form-text" id="wei" style="display: none;padding-top: 20px;">
            <label class="layui-form-label">未通过意见：</label>
            <div class="layui-input-block">
                <textarea placeholder="请输入意见" id="judgingView" name="judgingView" class="layui-textarea"></textarea>
            </div>
        </div>

        <div class="layui-input-block" id="tuihui" style="display: none;margin-left: 0px;padding-top: 20px;">
            <table class="layui-table" id="LAY_table_user" lay-filter="test">
            </table>
            <table class="layui-table">
                <tr>
                    <td style="width:20%"> <select name="judContent" id="judContent" lay-filter="judContent"></select></td>
                    <td style="width:60%"> <input type="text" name="tuiHuiView" id="tuiHuiView" placeholder="请输入意见" autocomplete="off" class="layui-input"></td>
                    <td style="width:20%"><a href="javascript:void(0);" style="color:blue;text-decoration:underline;" onclick="addTuiHui()">新增</a></td>
                </tr>
            </table>
        </div>
        <div id="lahei" style="display: none;padding-top:20px;">
            <div class="layui-form-item" >
                <label class="layui-form-label">封禁结束时间：</label>
                <div class="layui-input-inline">
                    <input type="text" name="blackendtime" id="blackendtime" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-text">
                <label class="layui-form-label">黑名单意见：</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" id="blackView" name="blackView" class="layui-textarea"></textarea>
                </div>
            </div>
        </div>
        <div class="layui-form-item" style="text-align: center;padding-top: 10px;">
            <button class="layui-btn" lay-submit lay-filter="demo2">提交审批结果</button>
        </div>

</fieldset>
</form>
<script>
    var layer;
    var form;
    var table;
    layui.use(['element','layer','form','laydate','table'], function(){
        var element = layui.element;
         form=layui.form;
         table=layui.table;
         var laydate = layui.laydate;
        layer=layui.layer;
        laydate.render({
            elem: '#blackendtime'
        });
        seleajax('<%=basePath%>/JudgingProposer/getDictByRole.do','请选择审批内容','dictCode','dictName','#judContent');
        form.render('select');
        <%--监听评审信息--%>
        form.on('radio(encrypt)', function(data){
            if(data.value=="0"){
                $("#wei").css("display","block");
                $("#lahei").css("display","none");
                $("#tuihui").css("display","none");
            }else if(data.value=="2"){
                $("#tuihui").css("display","block");
                $("#lahei").css("display","none");
                $("#wei").css("display","none");
            }else if(data.value=="4"){
                $("#lahei").css("display","block");
                $("#tuihui").css("display","none");
                $("#wei").css("display","none");
            }else{
                $("#lahei").css("display","none");
                $("#tuihui").css("display","none");
                $("#wei").css("display","none");
            }
        });
        <%--监听评审提交信息--%>
        form.on('submit(demo2)', function(data){
            if(data.field.finalResult=="0"){  //未通过
                var judgingView=$("#judgingView").val();
                if(judgingView==""){
                    layer.tips('请输入意见', '#judgingView',{tips:1});
                    return false;
                }
            }/*else if(data.field.finalResult=="2"){  //退回修改
                var judgingView=$("#judgingView").val();
                if(judgingView==""){
                    layer.tips('请输入意见', '#judgingView');
                    return false;
                }
            }*/else if(data.field.finalResult=="4"){  //加入黑名单
                var blackendtime=$("#blackendtime").val();
                var blackView=$("#blackView").val();
                if(blackendtime==""){
                    layer.tips('封禁结束时间', '#blackendtime',{tips:1});
                    return false;
                }
                if(blackView==""){
                    layer.tips('请输入意见', '#blackView',{tips: 1});
                    return false;
                }
            }
            data.field.userId="${PROPOSER_ID}";
            data.field.currentJudgingStage="${CURRENT_STATE}";
           var ret=ajax('<%=basePath%>/JudgingProposer/saveJudging.do?judId=${PROPOSER_ID}',data.field);
            if(ret.isOk=="1"){
                parent.location.reload();
                layer.closeAll('iframe');
                layer.msg(ret.message);
            }else{
                layer.msg(ret.message);
            }
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        <%--加载权限信息--%>

        <%--加载基础信息--%>
        $.ajax({
            url: '<%=basePath%>/JudgingProposer/getProposerAllMsg.do?userid=${PROPOSER_ID}',
            dataType:'json',
            success: function (data) {
                if('${CURRENT_STATE}'>=15&&'${CURRENT_STATE}'<18){
                    $("#myshencha").css("display","block");
                }
             /*   $.ajax({
                    url: '<%=basePath%>/Role/getJudgingRoleByUserId.do',
                    type: "POST",
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    dataType:'json',
                    success: function (data) {
                        if (data != null && data.length != 0) {
                            $(data).each(function (index, item) {
                                switch (item.FSORT) {
                                    case '01':
                                        $('#tabl').css("display", 'block');
                                        break;
                                    case '02':
                                        $('#edu').css("display", 'block');
                                        break;
                                    case '03':
                                        $('#work').css("display", 'block');
                                        break;
                                    case '04':
                                        $('#train').css("display", 'block');
                                        break;
                                    case '05':
                                        $('#award').css("display", 'block');
                                        break;
                                    case '06':
                                        $('#patent').css("display", 'block');
                                        break;
                                    case '07':
                                        $('#project').css("display", 'block');
                                        break;
                                    case '08':
                                        $('#paper').css("display", 'block');
                                        break;
                                    case '09':
                                        $('#book').css("display", 'block');
                                        break;
                                    case '10':
                                        $('#research').css("display", 'block');
                                        break;
                                    case '11':
                                        $('#summary').css("display", 'block');
                                        break;
                                }
                            })

                        }
                    }
                });*/

                initProposerMsg(data);
                initTableEdu(data['education']);
                initTableWork(data['work']);
                initTableTra(data['train']);
                initTableAwa(data['award']);
                initTablePat(data['patent']);
                initTablePro(data['project']);
                initTablePap(data['paper']);
                initTableBook(data['book']);
                initTableRes(data['research']);
                initSumm(data['summary']);
                jiazai();
            }
        });

    });


    <%--新增退回修改意见--%>
    function addTuiHui() {
        if ($('#judContent').val() == '') {
            layer.tips('请选择审批内容', $('#judContent').parent(),{tips:1})
            return false;
        }
        if ($('#tuiHuiView').val() == '') {
            layer.tips('请输入意见', '#tuiHuiView',{tips:1});
            return false;
        }
        var retu=ajax('<%=basePath%>/JudgingProposer/addTuiHui.do',{judView:$('#tuiHuiView').val(),
            judContent:$('#judContent').val(),userId:${PROPOSER_ID},currentJudgingStage:${CURRENT_STATE}});
        if(retu.isOk==1){
            $("#tuiHuiView").val('');
            $("#judContent").val("");
            form.render('select');
            table.reload('testReload');
        }else{
            layer.msg(retu.message);
        }
    }
    <%--加载退回意见--%>
    function jiazai(){
        table.render({
            elem: '#LAY_table_user'
            ,url: '<%=basePath%>/JudgingProposer/getProposerJudResultTui.do?userid=${PROPOSER_ID}&currentJudgingStage=${CURRENT_STATE}'
            ,cols: [[
            {field:'JUD_CONTENT_NAME',width:'20%',title: '审批内容', sort: true}
            ,{field:'JUD_VIEW',width:'60%', title: '审批意见'}
            ,{field:'ID',width:'20%', title: '操作',templet:function(d){
                return '<a href="javascript:void(0);" onclick="deleteJudResult(\''+d.ID+'\')" style="color:blue;text-decoration:underline;">删除</a>'
                    }}
             ]],
            text: {none: '暂无相关数据'}
            ,id: 'testReload'
            ,page: false
        });
    }
    <%--删除退回意见--%>
    function deleteJudResult(id) {
       var returnData= ajax('<%=basePath%>/JudgingProposer/delteteJudResult.do',{judResultId:id});
        if (returnData.isOk == 1) {
            table.reload('testReload');
        } else {
           layer.msg(returnData.message);
        }
    }
    <%--初始个人总结--%>
    function initSumm(data) {
        if (data != null) {
            $('#summarytext').html(data['summary']);
        }
    }
    <%--初始其他成果--%>
    function initTableRes(data) {
        var thead='<th>获得时间</th> <th>研究成果名</th> <th>研究成果简介</th> <th>研究成果级别</th><th>上传文件(可预览)</th>';
        $("#cusTableRes thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var appraisal = "";
                if (appraisal == "0") {
                    appraisal = "未鉴定";
                } else if (appraisal == "1") {
                    appraisal = "已鉴定";
                }
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var tbody = '<tr><td>' + formatdate(row.getDate) + '</td><td>' + row.researchName + '</td><td>' + row.researchInfo + '</td><td>' + row.researchLevelName + '</td><td><div id="researchFile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTableRes tbody").append(tbody);
                ech(row.researchFile, '#researchFile'+i);
            }
        }else{
            $("#cusTableRes tbody").html("<td colspan='5' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始著作--%>
    function initTableBook(data) {
        var thead='<th>著作书名</th> <th>出版社</th> <th>出版日期</th> <th>字数</th><th>排名</th>' +
            ' <th>备注</th><th>书号</th> <th>级别</th><th>文件(封面 版权页 内容 点击可预览)</th>';
        $("#cusTableBook thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var tbody = '<tr><td>' + row.bookname + '</td><td>' + row.publishinghouse + '</td><td>' + formatdate(row.publishtime) + '</td><td>' + row.wordnumber + '</td><td>' + row.paiming + '</td><td>' + row.extrainfo + '</td><td>' + row.bookNumber + '</td><td>' + row.bookLevel + '</td><td><div id="bookFile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTableBook tbody").append(tbody);
                ech(row.bookFile, '#bookFile'+i);
            }
        }else{
            $("#cusTableBook tbody").html("<td colspan='9' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始论文--%>
    function initTablePap(data) {
        var thead='<th>论文题目</th> <th>出版社</th> <th>发表/出版日期</th> <th>字数</th><th>是否鉴定论文</th>' +
            ' <th>排名</th><th>备注</th> <th>刊物名称</th><th>isbn</th> <th>级别</th><th>文件(封面、目录、内容 点击可预览)</th>';
        $("#cusTablePap thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var appraisal = row.appraisal;
                if (appraisal == "0") {
                    appraisal = "未鉴定";
                } else if (appraisal == "1") {
                    appraisal = "已鉴定";
                }
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var tbody = '<tr><td>' + row.papername + '</td><td>' + row.publishinghouse + '</td><td>' + formatdate(row.publishtime) + '</td><td>' + row.wordnumber + '</td><td>' + appraisal + '</td><td>' + row.paiming + '</td><td>' + row.extrainfo + '</td><td>' + row.publicationName + '</td><td>' + row.isbn + '</td><td>' + row.paperLevel + '</td>' +
                    '<td><div id="paperfile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTablePap tbody").append(tbody);
                ech(row.paperfile, '#paperfile'+i);
            }
        }else{
            $("#cusTablePap tbody").html("<td colspan='11' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始专利--%>
    function initTablePat(data) {
        var thead = '<th>专利名称</th> <th>专利类别</th> <th>专利编号</th> <th>专利授予日期</th><th>专利归属</th> <th>排名</th> <th>上传文件(可预览)</th>';
        $("#cusTablePat thead").html(thead);
        if (data.length > 0) {
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var patenttype = "";
                if (row.patenttype == "0") {
                    patenttype = "发明专利"
                } else if (row.patenttype == "1") {
                    patenttype = "外观专利"
                } else if (row.patenttype == "2") {
                    patenttype = "实用新型专利"
                }

                var ascriptiontype = "";
                if (row.ascriptiontype == "0") {
                    ascriptiontype = "本人"
                } else if (row.ascriptiontype == "1") {
                    ascriptiontype = "单位"
                } else if (row.ascriptiontype == "2") {
                    ascriptiontype = "共有"
                } else if (row.ascriptiontype == "3") {
                    ascriptiontype = "无"
                }
                var tbody = '<tr><td>' + row.patentname + '</td><td>' + patenttype + '</td><td>' + row.patentnumber + '</td><td>' + formatdate(row.patentcompletetime) + '</td><td>' + ascriptiontype + '</td><td>' + row.paiming + '</td>' +
                    '<td><div id="patentfile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTablePat tbody").append(tbody);
                ech(row.patentfile, '#patentfile'+i);
            }
        }else{
            $("#cusTablePat tbody").html("<td colspan='7' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始项目经验--%>
    function initTablePro(data) {
        var thead = '<th>项目名称</th> <th>立项单位</th> <th>立项时间</th> <th>认证/验收时间</th><th>担任角色</th> <th>经费/万元</th> <th>认证/验收级别</th><th>认证/验收结论</th><th>获奖情况(排名)</th> <th>项目负责人</th><th>上传文件(可预览)</th>';
        $("#cusTablePro thead").html(thead);
        if (data.length > 0) {
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var strHtml = "";
                if (row.projectrole == "0") {
                    strHtml = "项目负责人"
                } else if (row.projectrole == "1") {
                    strHtml = "子项负责人"
                } else if (row.projectrole == "2") {
                    strHtml = "主参与"
                } else {
                    strHtml = "一般参与"
                }
                var va = "";
                if (row.acceptancetype == "0") {
                    va = "国家"
                } else if (row.acceptancetype == "1") {
                    va = "省部"
                } else if (row.acceptancetype == "2") {
                    va = "厅局"
                } else if (row.acceptancetype == "3") {
                    va = "本市"
                } else {
                    va = "其他"
                }
                var conclusiontype = "";
                if (row.conclusiontype == "0") {
                    conclusiontype = "国际领先"
                } else if (row.conclusiontype == "1") {
                    conclusiontype = "国际先进"
                } else if (row.conclusiontype == "2") {
                    conclusiontype = "国内领先"
                } else if (row.conclusiontype == "3") {
                    conclusiontype = "国内先进"
                } else if (row.conclusiontype == "4") {
                    conclusiontype = "验收通过"
                } else if (row.projectrole == "5") {
                    conclusiontype = "其他"
                }
                var tbody = '<tr><td>' + row.projectname + '</td><td>' + row.projectcompany + '</td><td>' + formatdate(row.projectstarttime) + '</td><td>' + formatdate(row.projectacceptancetime) + '</td><td>' + strHtml + '</td><td>' + row.projectmoney + '</td><td>' + va + '</td><td>' + conclusiontype + '</td><td>' + row.paiming + '</td><td>' + row.headman + '</td>' +
                    '<td><div id="projectfile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTablePro tbody").append(tbody);
                ech(row.projectfile, '#projectfile'+i);
            }
        }else{
            $("#cusTablePro tbody").html("<td colspan='11' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始奖励--%>
    function initTableAwa(data) {
        console.log(data);
        var thead='<th>奖励名称</th> <th>颁奖部门</th> <th>获奖日期</th> <th>排名</th><th>级别</th> <th>等级</th> <th>备注</th><th>相关文件(可预览)</th>';
        $("#cusTableAwa thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++){
                var row=notempty(data[i]);
                var tbody='<tr><td>'+row.name+'</td><td>'+row.awardDepartment+'</td><td>'+formatdate(row.awardDate)+'</td><td>'+row.paiming+'</td><td>'+row.awardLevel+'</td><td>'+row.grade+'</td>' +
                    '<td>'+row.backup1+'</td>'+
                    '<td><div id="awardFile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTableAwa tbody").append(tbody);
                ech(row.awardFile,'#awardFile'+i);
            }
        }else{
            $("#cusTableAwa tbody").html("<td colspan='8' style='text-align: center'>暂无数据</td>");
        }
    }
    <%--初始培训经历--%>
    function initTableTra(data) {
        var thead='<th>起止时间</th> <th>学习地点</th> <th>培训名称</th> <th>证明人</th><th>上传文件(可预览)</th>';
        $("#cusTableTra thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var tbody = '<tr><td>' + starttime + '</td><td>' + row.place + '</td><td>' + row.content + '</td><td>' + row.reference + '</td>' +
                    '<td><div id="trainFile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTableTra tbody").append(tbody);
                ech(row.trainFile, '#trainFile'+i);
            }
        }else{
            $("#cusTableTra tbody").html("<td colspan='5' style='text-align: center'>暂无数据</td>");
        }
    }
    <%-- //初始工作经历--%>
    function initTableWork(data) {
        var thead='<th>起止时间</th> <th>单位</th> <th>职务</th> <th>证明人</th> <th>备注</th> <th>上传文件(可预览)</th>';
        $("#cusTableWork thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var tbody = '<tr><td>' + starttime + '</td><td>' + row.workcompanyname + '</td><td>' + row.job + '</td><td>' + row.reference + '</td>' +
                    '<td>' + row.extrainfo + '</td>' +
                    '<td><div id="workfile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td></tr>';
                $("#cusTableWork tbody").append(tbody);
                ech(row.workfile, '#workfile'+i);
            }
        }else{
            $("#cusTableWork tbody").html("<td colspan='6' style='text-align: center'>暂无数据</td>");
        }
    }
    <%-- //初始学习经历--%>
    function initTableEdu(data) {
        var thead='<th style="190px">起止时间</th> <th>学校</th> <th>职务</th> <th>证明人</th> <th>所学专业</th> <th>学历</th> <th>学位</th> <th>是否全日制</th> <th>备注</th> <th>学历证书编号</th> <th>学位证书编号</th> <th>学历证书文件(可预览)</th> <th>学位证书文件(可预览)</th>';
        $("#cusTableEdu thead").html(thead);
        if(data.length>0){
            for(var i=0;i<data.length;i++) {
                var row = notempty(data[i]);
                var starttime = formatdate(row.starttime) + '-' + formatdate(row.endtime);
                var strHtml = '';
                if (row.fulltimetype == "0") {
                    strHtml = '非全日制';
                } else {
                    strHtml = '全日制';
                }
                var tbody = '<tr><td>' + starttime + '</td><td>' + row.schoolname + '</td><td>' + row.job + '</td><td>' + row.reference + '</td>' +
                    '<td>' + row.major + '</td><td>' + row.dictName1 + '</td><td>' + row.dictName + '</td>' +
                    '<td>' + strHtml + '</td><td>' + '' + '</td><td>' + row.backup1 + '</td><td>' + row.backup2 + '</td>' +
                    '<td><div id="educationfile'+i+'" class="layer-photos-demo"><img layer-src="" src="" /></div></td>' +
                    '<td><div id="educationfile1'+i+'" class="layer-photos-demo"><img layer-src="" src="" ></div></td></tr>';
                $("#cusTableEdu tbody").append(tbody);
                row.educationfile.forEach(function (val, ind, arr) {
                    if (val['filetype'] == "1") {
                        var f = val['fileurl'];
                        if (f != null) {
                            var last = f.lastIndexOf('/');
                            var sl = f.slice(last + 1 + 13);
                            var hou = f.slice(f.lastIndexOf("."));
                            if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                photo('#educationfile'+i, '<%=basePath%>' + val['fileurl']);
                            } else {
                                $("#educationfile"+i).html("<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;");
                            }
                        }
                    } else if (val['filetype'] == "2") {
                        var f = val['fileurl'];
                        if (f != null) {
                            var last = f.lastIndexOf('/');
                            var sl = f.slice(last + 1 + 13);
                            var hou = f.slice(f.lastIndexOf("."));
                            if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                                photo('#educationfile1'+i, '<%=basePath%>' + val['fileurl']);
                            } else {
                                $("#educationfile1"+i).html("<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;");
                            }
                        }
                    }
                })
            }
        }else{
            $("#cusTableEdu tbody").html("<td colspan='13' style='text-align: center'>暂无数据</td>");
        }
    }
    //初始基本信息
    function initProposerMsg(data) {
        $.ajax({
            url:'<%=basePath%>/JudgingProposer/getGuMsg.do',
            type: "POST",
            data: {'userid': ${PROPOSER_ID}},
            async: false,
            dataType:'json',
            success: function (retu) {
                if (retu != null && retu != 'null') {
                    var msg = retu;
                    if (${processPattern=="1"}) {//盲评
                        $('#displayName').html("***");
                        $('#idCardNo').html("***");
                        idCardNoQuan = msg['ID_CARD_NO'];
                        $('#unitName').html("***");
                        $('#mobilephone').html("***");
                    } else {
                        $('#displayName').html(msg['DISPLAY_NAME']);
                        $('#idCardNo').html(msg['ID_CARD_NO']);
                        $('#unitName').html(msg['UNIT_NAME']);
                        $('#mobilephone').html(msg['MOBILEPHONE']);
                    }
                    $('#pingshentype').html(msg['LEIXING']);
                    $('#reviewSeries').html(msg['XILIE']);
                    $('#proposePosition').html(msg['ZIGE']);
                    $('#shenbaomajorId').html(msg['PROFESSIAL_NAME']);
                    $('#judgingName').html(msg['JUDGING_NAME']);
                    $('#alreadyJudging').html(msg['JUDGING_NAME']);
                }
            }
        });
        if (data != null && data != 'null') {

            $('#eduMajor').html(data.eduMajor);
            $('#major').html(data.majorName);
            var t4 = data.startworktime;
            $('#startworktime').html(t4);
            $('#nowjob').html(data.nowjob);

            var t5 = data.nowjobTime;
            $('#nowjobtime').html(t5);
            $('#foreignlanguages').html(data.foreignName);
            $('#foreignlanguageslevel').html(data.foreignLevel);

            var t6 = data.foreignTime;
            $('#foreignlanguagestime').html(t6);
            $('#currentpost').html(data.currentPost);
            $('#currentpostseries').html(data.currentPostSeries);
            $('#currentpostlevel').html(data.currentPostLevel);

            var t7 = data.currentPostGetTime;
            $('#currentpostgettime').html(t7);
            var t8 = data.currentPostEngageTime;
            $('#currentpostengagetime').html(t8);
            $('#otherpost').html(data.otherPost);
            $('#otherpostseries').html(data.otherPostSeries);
            $('#otherpostlevel').html(data.otherPostLevel);

            var t9 = data.otherPostEngageTime;
            $('#otherpostengagetime').html(t9);
            $('#administrativepost').html(data.administrativePost);
            var t10 = data.administrativePostTime;
            $('#administrativeposttime').html(t10);
            $('#socialpost').html(data.socialPost);
            // $('#socialpostphone').html(msg.socialPostPhone);
            $('#yearsone').html(data.yearsOne);
            $('#yearsoneinfo').html(data.yearsOneInfoName);
            $('#yearstwo').html(data.yearsTwo);
            $('#yearstwoinfo').html(data.yearsTwoInfoName);
            $('#yearsthree').html(data.yearsThree);
            $('#yearsthreeinfo').html(data.yearsThreeInfoName);
            $('#yearsfour').html(data.yearsFour);
            $('#yearsfourinfo').html(data.yearsFourInfoName);
            $('#yearsfive').html(data.yearsFive);
            $('#yearsfiveinfo').html(data.yearsFiveInfoName);

            var returnD=ajax('<%=basePath%>/JudgingProposer/getDict.do',{"groupName": "SEX"});
            $(returnD).each(function (index, item) {
                var name = item['dictName'];
                var code = item['dictCode'];
                if (code == data.sex) {
                    $('#sex').html(name);
                }
            }) //政治面貌
            returnD=ajax('<%=basePath%>/JudgingProposer/getDict.do',{"groupName": "POLITICAL_STATUS"});
            $(returnD).each(function (index, item) {
                var name = item['dictName'];
                var code = item['dictCode'];
                if (code == data.politicalOutlook) {
                    $('#politicalOutlook').html(name);
                }
            })
            //民族
            returnD=ajax('<%=basePath%>/JudgingProposer/getDict.do',{"groupName": "NATION"});
            $(returnD).each(function (index, item) {
                var name = item['dictName'];
                var code = item['dictCode'];
                if (code == data.nation) {
                    $('#nation').html(name);
                }
            })
            //学历
            returnD=ajax('<%=basePath%>/JudgingProposer/getDict.do',{"groupName": "EDU_EDU"});
            $(returnD).each(function (index, item) {
                var name = item['dictName'];
                var code = item['dictCode'];
                if (code == data.eduEdu) {
                    $('#eduEdu').html(name);
                }
            })
            //学位
            returnD=ajax('<%=basePath%>/JudgingProposer/getDict.do',{"groupName": "DEGREE"});
            $(returnD).each(function (index, item) {
                var name = item['dictName'];
                var code = item['dictCode'];
                if (code == data.eduDegree) {
                    $('#eduDegree').html(name);
                }
            })
            if (data.userpicurl != null) {
                if (${processPattern=="1"}) {//盲评
                    photo('#myImguserpic','<%=basePath%>/static/images/zhizhang2.png')
                } else {
                    photo('#myImguserpic','<%=basePath%>'+ data.userpicurl)
                }
            }
        }
    }
    //循环读取
    function ech(data,id){
        data.forEach(function (val, ind, arr) {
            var f = val['fileurl'];
            if (f != null) {
                var last = f.lastIndexOf('/');
                var sl = f.slice(last + 1 + 13);
                var hou = f.slice(f.lastIndexOf("."));
                if (hou == ".jpg" || hou == ".jpeg" || hou == ".gif" || hou == ".png" || hou == ".bmp") {
                    photo(id,'<%=basePath%>'+val['fileurl']);
                } else {
                    $(id).html("<div style='display: inline;color: #00a6b2' " + "onclick=previewFile" + "(" + "'" + val['fileurl'] + "'" + ")" + ">" + sl + "</div>&nbsp;");
                }
            }
        });
    }
    //预览文件
    function previewFile(fileurl) {
        var url = "previewFile.do?fileName=" + fileurl;
        if (${processPattern=="1"}) {//盲评
            url = "previewFileZhe.do?fileName=" + fileurl + "&&idCardNo=" + idCardNoQuan;
        }
        $.ajax({
            type: "post",
            async: false,
            dataType: "text",
            url: '<%=basePath%>/JudgingProposer/' + url,
            success: function (data) {
                var msg = eval("(" + data + ")");
                var xmlhttp;
                if (window.XMLHttpRequest) {
                    xmlhttp = new XMLHttpRequest();//其他浏览器
                } else if (window.ActiveXObject) {
                    try {
                        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");//旧版IE
                    } catch (e) {
                    }
                    try {
                        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");//新版IE
                    } catch (e) {
                    }
                    if (!xmlhttp) {
                        console.log("不能创建XMLHttpRequest对象");
                    }
                }
                xmlhttp.open("GET", "<%=basePath%>" + msg['previewFile'], false);
                xmlhttp.send();
                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        window.open("<%=basePath%>" + msg['previewFile'], '_blank');
                    } else {
                        showError("文件缺失");
                    }
                }
            }

        });
    }
    function photo(ID,url){
        $(ID+' img').attr("src",url);
        $(ID+' img').attr("layer-src", url);
        layer.photos({
            photos: ID,
        });
    }

</script>
</body>
</html>
