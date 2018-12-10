<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;Charset=utf-8;" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<%!
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
%>
<html>
<head>
    <meta charset="utf-8">
    <%--<meta http-equiv="X-UA-Compatible" content="IE=edge">--%>
    <meta name="renderer" content="webkit">
    <title>信息管理系统</title>
    <link type="text/css" href="<%=basePath%>/static/css/style.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/ui-cupertino/easyui.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <%--  <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>--%>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/js/cloud.js" type="text/javascript"></script>
    <style>
        .yanzhengma{
            width: 95px;
            float: right;
            height: 40px;
        }
        .yanzhengma:hover{
            cursor: pointer;
        }
        .putong{
            float: left;
            margin-right: 8px;
            width: 73px;
            margin-top: 20px;
        }
        .zhuanjia{
            margin-top: 20px;
        }
        button.btn-register{
            width: 120px;
            float: right;
        }
        .login_reme{
            padding-bottom: 0;
        }
    </style>
    <script language="javascript">


        //登陆操作
        function loginSys() {
            $.ajax({
                url: "<%=basePath%>/User/login.do",
                data: {
                    username: $('#txtUserName').val(),
                    password: $('#txtPassWord').val(),
                    code: $('#code').val(),
                    achiresult:$("input[name='achiresult']:checked").val()
                },
                beforeSend: function (xhr) {
                    return $('#form1').form('validate');
                },
                success: function (json) {
                    switch (json.isOk) {
                        case "1":
                            document.location.href = '<%=basePath%>/User/index.do';
                            break;
                        case "0":
                            showError(json.message);
                            break;
                    }
                },
                error: function () {
                    showError('获取账号信息失败...请联系管理员!');
                }
            });
        }

        document.onkeydown = function (event) {
            var e = event || window.event;
            if (e && e.keyCode == 13) { //回车键的键值为13
                loginSys(); //调用登录按钮的登录事件
            }
        };
        function trim(x,id) {
            document.getElementById(id).value = x.trim();
        }



    </script>

</head>
<body style=" background-repeat:no-repeat; background-position:center top; overflow:hidden;">
<!-- 从session中获取生成的验证码 -->
<%--<input id="getSession" type="hidden" value="<%=session.getAttribute("kaptcha") %>"/>--%>
<div id="mainBody" style="background-image:url(<%=basePath%>/static/images/cbd3.png);background-size: cover; overflow:hidden;">
    <div id="cloud1" class="cloud"></div>
    <div id="cloud2" class="cloud"></div>
</div>

<%--<div class="logintop">--%>
    <%--<span>欢迎登录信息管理系统</span>--%>
    <%--<ul>--%>
        <%--<li><a href="#">回首页</a></li>--%>
        <%--<li><a href="#">帮助</a></li>--%>
        <%--<li><a href="#">关于</a></li>--%>
    <%--</ul>--%>
<%--</div>--%>

<div class="loginbody">
    <span class="systemlogo"></span>
    <%--<div class="loginbox">--%>
        <%--<form id="form1">--%>
            <%--<ul>--%>
                <%--<li><input id="txtUserName" name="txtUserName" class="easyui-validatebox" placeholder="用户名" value="410000"--%>
                           <%--required="true"--%>
                           <%--validtype="length[1,20]"--%>
                           <%--style="width:343px; height:48px; background:url(<%=basePath%>/static/images/loginuser.png) no-repeat; border:none; line-height:48px; padding-left:44px; font-size:14px; color:#90a2bc;"/>--%>
                <%--</li>--%>
                <%--<li><input id="txtPassWord" name="txtPassWord" type="password" class="easyui-validatebox"--%>
                           <%--placeholder="密码" required="true"--%>
                           <%--validtype="length[1,20]" value="000000"--%>
                           <%--style="width:343px; height:48px; background:url(<%=basePath%>/static/images/loginpassword.png) no-repeat; border:none;line-height:48px; padding-left:44px; font-size:14px; color:#90a2bc;"/>--%>
                <%--</li>--%>
                <%--<li><input id="btnLogin" name="btnLogin" type="button" class="loginbtn" value="登录"--%>
                           <%--onclick="loginSys()"/>--%>
                    <%--<label><input id="checkboxId" name="checkboxId" type="checkbox" checked="checked"/>记住账户</label>--%>
                    <%--<label><a href="#">忘记密码？</a></label>--%>
                <%--</li>--%>
            <%--</ul>--%>
        <%--</form>--%>
    <%--</div>--%>
    <div class="login-topStyle3" id="loginStyle" style="background-color: #ffffff">
        <h3>用户登录</h3>
        <div class="ui-form-item loginUsername">
            <input type="username" placeholder="请输入用户名" id="txtUserName" value="410000" onchange="trim(this.value,'txtUserName')">
        </div>
        <div class="ui-form-item loginPassword">
            <input type="password" placeholder="请输入密码" id="txtPassWord" value="000000" onchange="trim(this.value,'txtPassWord')">
        </div>
        <div class="ui-form-item loginUsername">
            <input type="username" style="width: 180px" placeholder="请输入验证码" id="code" >
            <img class="yanzhengma"
                 src="<%=basePath%>/KaptchaController/getKaptcha.do" id="imgVcode" onclick="document.getElementById('imgVcode').src='<%=basePath%>/KaptchaController/getKaptcha.do?time='+(new Date()).getTime();"
                 title="点击更换图片"/>
        </div>

        <div class="ui-form-item loginUsername">


        </div>
        <div class="login_reme">
            <input type="checkbox" id="checkboxId">
            <a class="reme1">记住账号</a></div>
        <span class="error_xinxi" style="display:none;">您输入的密码不正确，请重新输入</span>
        <button type="button " class="btn-register" id="submitImg" onclick="loginSys()"> 立即登录</button>
        <div class="putong">
            <input type="radio" name="achiresult" value="0" checked="checked">
            <span style="display: initial;">管理用户</span>
        </div>
        <div class="zhuanjia">
            <input type="radio" name="achiresult" value="1" >
            <span style="display: initial;">专家登录</span>
        </div>
    </div>

</div>
<div class="loginbm">技术支持：郑州恒正电子科技有限公司</div>
</body>
</body>
</html>