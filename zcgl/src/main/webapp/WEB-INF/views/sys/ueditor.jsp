<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"
         contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<head>
    <title>系统参数管理</title>
    <meta charset="utf-8">
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/css/bootstrap.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/ueditor/ueditor.config.js"></script>
    <script src="<%=basePath%>/static/ueditor/ueditor.all.js"></script>
    <link rel="stylesheet" href="<%=basePath%>/static/ueditor/themes/default/css/ueditor.css">
    <script src="<%=basePath%>/static/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style>
        form > div.form-group > label.text-left {
            text-align: left;
        }
    </style>
</head>
<script type="text/javascript">
    var ue;
    var uu;
    var cs='<p><strong>111111111111111</strong></p>';
    jQuery(document).ready(function() {
        ue = UE.getEditor('myeditor',{
            toolbars: [
                ['source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'backcolor', 'fontsize', 'fontfamily', 'justifyleft', 'justifyright', 'justifycenter', 'justifyjustify', 'date', 'time', 'spechars', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
            ]
        });
        uu = UE.getEditor('myeditors',{
            toolbars: [
                ['source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'backcolor', 'fontsize', 'fontfamily', 'justifyleft', 'justifyright', 'justifycenter', 'justifyjustify', 'date', 'time', 'spechars', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
            ]
        });
        $("#myeditor").css("width","90%");
        $("#myeditor").css("margin-left","176px");
        $("#myeditor").css("margin-top","22px");
        $("#myeditors").css("width","90%");
        $("#myeditors").css("margin-left","176px");
        $("#myeditors").css("margin-top","22px");
        //ue.ready(function() {//编辑器初始化完成再赋值
        //ue.setContent(cs);  //赋值给UEditor
        //});
    });
</script>
<style>
    .style_body{
        background-size: cover;
        border:none;
    }
    .style_body1{
        background: url("<%=basePath%>/static/images/zhizhang2.png");
        background-size: cover;
    }
    .form-control {
        margin: 5px 0;
    }

    .font14_row {
        margin: 0 38px;
        background: none;
        border:none;
    }

    hr {
        margin-bottom: 0;
    }

    .text-center {
        margin-top: 10px;
    }
    .col-md-2{
        position: relative;
        min-height: 1px;
        padding-right: 10px;
        padding-left: 0;
        font-weight: 400;
        text-align: right;
        margin-top: 120px;
    }
    .col-md-3{
        position: relative;
        min-height: 1px;
        padding-right: 10px;
        padding-left: 0;
        font-weight: 400;
        text-align: right;
        margin-left: -75px;
    }
    .container{
        background: url("<%=basePath%>/static/images/zhizhang2.png");
        background-size: cover;
    }
    .checkbox-inline{
        padding-left: 20px;
        padding-bottom: 15px;
        padding-top: 0!important;
    }
    .form-horizontal .control-label{
        padding-top: 12px;
    }
    .shu{
        position: absolute;
        float: left;
        margin-top: 13px;
        margin-left: 5px;
    }
    .style_title{font-size: 30px;color:#337ab7;font-weight: bold;text-align:center;margin:0;}
</style>
<body class="style_body">
<div class="container">
    <div class="style_border" style="overflow:initial">
        <div  style="padding: 0 25px 25px;margin-top: 40px">
            <h2 class="text-center style_title">订单明细11</h2>
            <label class="control-label col-md-2">公司介绍:</label>
            <textarea id="myeditor"  style="height:140px;" name="newscontent"></textarea>
            <label class="control-label col-md-2">软件功能介绍:</label>
            <textarea id="myeditors"  style="height:140px;" name="newscontent"></textarea>
            <label class="control-label col-md-3">医生分成比例</label>
            <input style="width: 80%;margin-left: 175px;" class="form-control" id="doctorRate" name="doctorRate" type="text">
            <label class="control-label col-md-3">消息回复声明</label>
            <textarea style="width: 80%;margin-left: 175px;" class="form-control" id="messageStatement" name="messageStatement" type="text"></textarea>
            <label class="control-label col-md-3">订单回复声明</label>
            <textarea style="width: 80%;margin-left: 175px;" class="form-control" id="indentStatement" name="indentStatement" type="text"></textarea>
            <button style="margin-left: 475px;margin-top: 5px" type="button" class="btn btn-primary" id="submitBtn">提交</button>
        </div>
    </div>
</div>

<%--模态弹窗引用jquery-ui设置可拖动--%>
<script>
    $(document).ready(function () {
        $(".modal-content").draggable({cursor: "move"});//为模态对话框添加拖拽
    })
</script>
</body>