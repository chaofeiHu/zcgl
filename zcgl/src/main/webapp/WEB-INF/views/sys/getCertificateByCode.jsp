<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;Charset=utf-8;" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String certificatePath=(String) request.getAttribute("certificatePath");
%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>通过二维码查看证书</title>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script type="text/javascript">
        $(function () {
            if("<%=certificatePath%>"==""){
                $("#noCertificate").html("未找到证书。");
            }else {
                window.open("<%=basePath%>/<%=certificatePath%>","_top");
            }
        });

    </script>
</head>
<body>
<div id="noCertificate">


</div>

</body>
</html>