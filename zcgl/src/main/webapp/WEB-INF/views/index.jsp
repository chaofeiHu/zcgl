<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"
         contentType="text/html;charset=UTF-8" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
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
    <title>信息管理系统</title>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet">
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/index.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/js/jquery.cookie.js" charset="GBK"></script>
    <style type="text/css">

        /*menu*/
        .icon {
            background: url(<%=basePath%>/static/images/tabicons.png) no-repeat;
            width: 18px;
            line-height: 18px;
            display: inline-block;
        }

        .icon-sys {
            background-position:-240px -500px;
        }

        .icon-nav {
            background-position: -180px -280px;
        }

        .icon-san{
            background-position:-200px -280px;
        }

        .icon-clo{
            background: url(<%=basePath%>/static/images/next.gif) no-repeat;
            line-height: 14px;
            margin-left: 4px;
        }

        .icon-ope{
            background: url(<%=basePath%>/static/images/pre.gif) no-repeat;
            line-height: 14px;
            margin-left: 4px;
        }
        /*top*/
        .topleft {
            height: 88px;
            background: url(<%=basePath%>/static/images/topleft.jpg) no-repeat;
            float: left;
            width: 300px;
        }

        .topleft img {
            margin-top: 6px;
            margin-left: 10px;
        }

        .topright {
            height: 88px;
            background: url(<%=basePath%>/static/images/topright.jpg) no-repeat right;
            float: right;
        }

        .topright ul {
            padding-top: 5px;
            float: right;
            padding-right: 12px;
        }

        .topright ul li {
            float: left;
            padding-left: 9px;
            padding-right: 9px;
            background: url(<%=basePath%>/static/images/line.gif) no-repeat right;
            list-style-type: none;
        }

        .topright ul li:last-child {
            background: none;
        }

        .topright ul li a {
            font-size: 13px;
            color: #e9f2f7;
        }

        .topright ul li a:hover {
            color: #454545;
        }

        .topright ul li span {
            margin-top: -3px;
            float: left;
            padding-right: 2px;
        }

        .user {
            height: 30px;
            background: url(<%=basePath%>/static/images/ub1.png) repeat-x;
            clear: both;
            margin-top: 10px!important;
            float: right;
            margin-right:27px;
            border-radius: 30px;
            behavior: url(<%=basePath%>/static/js/pie.htc);
            white-space: nowrap;
            position: relative;
        }

        .user span {
            display: inline-block;
            padding-right: 10px;
            background: url(<%=basePath%>/static/images/user.png) no-repeat 15px 10px;
            line-height: 30px;
            font-size: 14px;
            color: #b8ceda;
            padding-left: 20px;
            padding-left: 35px;
        }

        .user b {
            display: inline-block;
            width: 20px;
            height: 18px;
            background: url(<%=basePath%>/static/images/msg.png);
            text-align: center;
            font-weight: normal;
            color: #fff;
            font-size: 14px;
            margin-right: 13px;
            margin-top: 7px;
            line-height: 18px;
        }

        .user i {
            display: inline-block;
            margin-right: 5px;
            font-style: normal;
            line-height: 30px;
            font-size: 14px;
            color: #b8ceda;
        }
        .panel-title{
            margin-left: 5px;
        }
        .easyui-accordion ul{
            padding:0 10px;
        }
        .helpimg{
            margin-top: 4px;
        }
        .m-btn-downarrow{
           top: 6px;
        }
    </style>
    <script type="text/javascript">
        var _menus = '';

        var me = {
            win_changepwd: null,
            win_changepwd_form: null
        };

        $(function () {
          
            getUserInfo();
            tabClose();
            tabCloseEven();
            pageInit();

        });


        function judgingTree(){
            $('#displayName').click(function(){
                $("#updatejudging").window('open');
            });
        }
        function pageInit() {
            me.win_changepwd = $('#win_changepwd');
            me.win_changepwd_form = me.win_changepwd.find('#win_changepwd_form');

            $('#editpass').click(function () {
                me.win_changepwd.window('open');
            });
            $('#btnEp').click(function () {
                changePwd();
            });
            $('#btnCancel').click(function () {
                me.win_changepwd.window('close');
            })
            $('#JUDGING_CODE').combobox({
                url: "<%=basePath%>/User/findJudging.do",
                valueField:'JUDGING_CODE',
                textField:'JUDGING_NAME',
                width:"350",
                onSelect:function(data){
                    if(data.JUDGING_CODE!=$('#JUDGING_CODE').combobox('getValue')){
                        $.ajax({
                            url: "<%=basePath%>/User/UpdateJudging.do",
                            data: {JUDGING_CODE:data.JUDGING_CODE,JUDGING_NAME:data.JUDGING_NAME},
                            success: function (returnData) {
                                if (returnData.isOk == 1)
                                    window.location.reload();
                                else
                                    showError("修改错误！");
                            }
                        });
                    }
                }
            });

            $('#loginOut').click(function () {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function (isOk) {
                    if (isOk) {
                        $.ajax({
                            url: "<%=basePath%>/User/loginOut.do",
                            data: {},
                            success: function (returnData) {
                                if (returnData.isOk == 1)
                                    document.location.href = "<%=basePath%>/User/baseLogin.do";
                                else
                                    document.location.href = "<%=basePath%>/User/baseLogin.do";
                            }
                        });
                    }
                });
            })
        }

        //修改密码
        function changePwd() {
            if (me.win_changepwd_form.form('validate')) {
                $.ajax({
                    url: "<%=basePath%>/User/ChangePassword.do",
                    data: me.win_changepwd_form.serialize(),
                    success: function (returnData) {
                        if (returnData.isOk == 1) {
                            $.messager.alert("提示信息", "恭喜，密码修改成功！<br>您的新密码为：" + me.win_changepwd_form.find("#NewPassword").val(), "info");
                            me.win_changepwd_form.form('clear');
                            me.win_changepwd.window('close');
                            window.location.reload();
                        } else {
                            $.messager.alert("错误信息", returnData.message, "error");
                        }
                    }
                });
            }
        }
        //获取用户信息
        function getUserInfo() {
            $.ajax({
                url: "<%=basePath%>/User/getUserInfo.do",
                //dataType:'json',
                success: function (returnData) {
                    changeTheme(returnData.userThemes);
                    if(returnData.type=="1"){
                        judgingTree();
                        $("#JUDGING_CODE").combobox("select",returnData.JUDGING_CODE);
                    }
                    $('#displayName').html(returnData.displayName);
                    if(returnData.isOk=="-1"){
                        $('.easyui-layout').layout('hidden','west');
                        load();
                    }else{
                        $.ajax({
                            url: "<%=basePath%>/Menu/GetMenuByUserID.do",
                            dataType: 'text',
                            success: function (returnData) {
                                _menus = eval('(' + returnData + ')');
                                InitLeftMenu();
                            }
                        });
                    }
                }
            });
        }


        function load() {
            $("<div class=\"datagrid-mask\"></div>").css({
                display: "block",
                width: "100%",
                height: $(window).height()
            }).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("请修改初始密码").appendTo("body").css({
                display: "block",
                left: ($(document.body).outerWidth(true) - 190) / 2,
                top: ($(window).height() - 45) / 2
            });
        }

        //更新用户选择的主题信息
        function UpdateUserThemes(themeName) {
            $.ajax({
                url: "<%=basePath%>/User/UpdateUserThemes.do?userThemes=" + themeName,
                success: function (returnData) {
                    changeTheme(themeName);
                }
            });
        }

    </script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<noscript>
    <div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px;
            width: 100%; background: white; text-align: center;">
        <img src="<%=basePath%>/static/images/noscript.gif" alt='抱歉，请开启脚本支持！'/>
    </div>
</noscript>
<div region="north" split="false" border="true" style="background:url(<%=basePath%>/static/images/topbg.gif) repeat-x;">
    <div class="topleft">
        <a href="#" target="_parent"><img src="<%=basePath%>/static/images/loginlogo1.png" title="系统首页"/></a>
    </div>
    <div class="topright">
        <ul>
            <li><span><img src="<%=basePath%>/static/images/help.png" title="帮助" class="helpimg"/></span><a
                    href="#">帮助</a></li>
            <li><a href="javascript:void(0)" id="editpass">修改密码</a></li>
            <li><a href="#" id="loginOut">安全退出</a></li>
            <li style="height: 20px;"><a href="javascript:void(0);" class="easyui-menubutton"
                                         data-options="menu:'#layout_north_pfMenu'">更换主题</a></li>

        </ul>
        <div class="user" style="">
            <span id="displayName"></span>
      <%--    <a href="#"><i>消息</i></a>
            <a href="#"><b id="NotMsgCount">5</b></a>--%>
        </div>
        <div id="layout_north_pfMenu" style=" display: none;">
            <div onclick="UpdateUserThemes('default');">default</div>
            <div onclick="UpdateUserThemes('bootstrap');">bootstrap</div>
            <div onclick="UpdateUserThemes('black');">black</div>
            <div onclick="UpdateUserThemes('gray');">gray</div>
            <div onclick="UpdateUserThemes('material');">material</div>
            <div onclick="UpdateUserThemes('material-teal');">material-teal</div>
            <div onclick="UpdateUserThemes('metro');">metro</div>
            <div onclick="UpdateUserThemes('metro-blue');">metro-blue</div>
            <div onclick="UpdateUserThemes('metro-gray');">metro-gray</div>
            <div onclick="UpdateUserThemes('metro-green');">metro-green</div>
            <div onclick="UpdateUserThemes('metro-red');">metro-red</div>
            <div onclick="UpdateUserThemes('metro-orange');">metro-orange</div>
            <div onclick="UpdateUserThemes('ui-cupertino');">ui-cupertino</div>
            <div onclick="UpdateUserThemes('ui-pepper-grinder');">ui-pepper-grinder</div>
            <div onclick="UpdateUserThemes('ui-sunny');">ui-sunny</div>
        </div>
    </div>
</div>

<div region="south" split="false" border="true" style="height: 28px; background: #efefef;">
    <div class="footer">
        河南省职称信息工作平台
    </div>
</div>
<div region="west" hide="true"  split="true" title="导航菜单" style="width: 220px;display:none" id="west">
    <div id="nav" class="easyui-accordion" fit="true" border="true" title="导航内容">
    </div>
</div>
<div id="mainPanle" region="center" style="background: #eee; overflow-y: hidden">
    <div id="tabs" class="easyui-tabs" fit="true" border="false">
        <div title="首页" style="padding: 20px; overflow: hidden; color: red; display: block;">
            <%@ include file="main.jsp" %>
        </div>

    </div>
</div>
<div id="win_changepwd" class="easyui-window" closed="true" title="修改密码" icon="icon-save"
     style="width: 350px; height: 250px; padding: 5px; background: #fafafa;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="win_changepwd_form" name="win_changepwd_form" method="post">
                <table cellpadding="3" align="center">
                    <tr>
                        <td>
                            旧密码：
                        </td>
                        <td>
                            <input id="OldPassword" name="OldPassword" type="Password" class="easyui-textbox"
                                   required="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            新密码：
                        </td>
                        <td>
                            <input id="NewPassword" name="NewPassword" type="Password" class="easyui-textbox"
                                   required="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            确认密码：
                        </td>
                        <td>
                            <input id="NewPasswordRe" name="NewPasswordRe" type="Password" class="easyui-textbox"
                                   required="true" validtype="eqPwd['#NewPassword']"/>

                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
            <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">
                关闭</a>
        </div>
    </div>
</div>

<div id="updatejudging" class="easyui-window" closed="true" title="当前所属评委会" icon="icon-save"
     style="width: 380px; height: 100px; padding: 10px 0 10px 5px; background: #fafafa;">
    <input id="JUDGING_CODE" width="340px" name="JUDGING_CODE" >
</div>


<div id="mm" class="easyui-menu" style="width: 170px;" title="多标签右键菜单">
    <div id="mm-tabupdate">
        刷新
    </div>
    <div class="menu-sep">
    </div>
    <div id="mm-tabclose">
        关闭
    </div>
    <div id="mm-tabcloseall">
        全部关闭
    </div>
    <div id="mm-tabcloseother">
        除此之外全部关闭
    </div>
    <div class="menu-sep">
    </div>
    <div id="mm-tabcloseright">
        当前页右侧全部关闭
    </div>
    <div id="mm-tabcloseleft">
        当前页左侧全部关闭
    </div>
    <div class="menu-sep">
    </div>
    <div id="mm-exit">
        退出
    </div>
</div>
</body>
</html>