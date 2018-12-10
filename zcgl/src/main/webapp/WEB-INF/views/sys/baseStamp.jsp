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
    <title>印章管理</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>

    <script type="text/javascript">

        var me = {
            edit_form: null,
            idFiled: 'ID',
            actionUrl: '<%=basePath%>/BaseStamp/'
        };

        $(function () {
            pageInit();
            Update();
        });

        function pageInit() {
            //select 只读状态
            $('#unitName').textbox('textbox').attr('readonly', true);
            $('#unitName').textbox('disable');
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_edit_delete').linkbutton().click(function () {
                DeleteImg();
            });
        }

        //初始
        function Update() {
            $.ajax({
                url: me.actionUrl + 'getData.do',
                type: "post",
                dataType: 'json',
                success: function (data) {
                    if (data) {
                        $("#id").val(data["ID"]);
                        $("#name").textbox("setValue", data["NAME"]);
                        $("#unitName").textbox("setValue", data["UNIT_NAME_NAME"]);
                        if (data.FILEURL != null && data.FILEURL != "") {
                            $("#imageSpan").html('<img id="stampImg" name="stampImg"/>');
                            $('#stampImg').attr("src", '<%=basePath%>' + data.FILEURL);
                        } else {
                            $("#imageSpan").html("未上传");
                        }
                    }
                }
            });
        }

        //添加修改（保存）
        function SaveData() {
            if ($("#edit_form").form('validate')) {
                var formData = new FormData($("#edit_form")[0]);
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: formData,
                    dataType: 'json',
                    processData: false,
                    contentType: false,
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                Update();
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }

        function DeleteImg() {
            $.messager.confirm("删除提示", "确认删除？", function (r) {
                if (r) {
                    var formData = new FormData($("#edit_form")[0]);
                    $.ajax({
                        url: me.actionUrl + 'DeleteImg.do',
                        data: formData,
                        dataType: 'json',
                        processData: false,
                        contentType: false,
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);
                                    Update();
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            });
        }


        //印章图片校验
        function excelUpload(e) {
            var imgName = $("#file").val();
            var idx = imgName.lastIndexOf(".");
            if (idx != -1) {
                var ext = imgName.substr(idx + 1).toUpperCase();
                ext = ext.toLowerCase();
                if (ext != 'jpg' && ext != 'png' && ext != 'jpeg' && ext != 'gif') {
                    alert("只能上传.jpg  .png  .jpeg  .gif类型的文件!");
                } else {
                    $("#imageSpan").html('<img id="stampImg" name="stampImg"/>');
                    for (var i = 0; i < e.target.files.length; i++) {
                        var file = e.target.files.item(i);
                        if (!(/^image\/.*$/i.test(file.type))) {
                            continue; //不是图片 就跳出这一次循环
                        }
                        var freader = new FileReader();
                        freader.readAsDataURL(file);
                        freader.onload = function (e) {
                            $("#stampImg").attr("src", e.target.result);
                        };
                    }
                }
            } else {
                alert("只能上传.jpg  .png  .jpeg  .gif类型的文件!");
            }
        }

    </script>
</head>
<body class="easyui-layout">
<div title="" id="edit_window" style="width: 100%;height: 100%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="west" border="false" style="padding: 1% 20%; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post" enctype="multipart/form-data">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id" value=""/>
                </div>
                <table cellpadding="5">
                    <tr>
                        <td width="100px">
                            单位名称：
                        </td>
                        <td colspan="3">
                            <input id="unitName" name="unitName" class="easyui-textbox"
                                   style="width: 475px"/>
                        </td>
                    </tr>
                </table>

                <table cellpadding="5">
                    <tr>
                        <td width="100px">
                            印章名称：
                        </td>
                        <td colspan="3">
                            <input id="name" name="name" class="easyui-textbox"
                                   style="width: 475px"/>
                        </td>
                    </tr>
                    <tr>
                        印章上传(请上传一张本单位印章图片)： <a id="btn_edit_delete" icon="icon-cancel"
                                               href="javascript:void(0)">删除印章文件</a>
                    </tr>
                    <tr>
                        <input type="file" name="file" id="file" multiple="false" accept=".jpg,.jepg,.png,.gif"
                               style="display:block;" onchange="excelUpload(event)"/>
                        <span id="imageSpan">未上传</span>
                    </tr>
                </table>
            </form>

            <div border="false" style="padding: 1% 30%;">
                <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">提交印章信息</a>
            </div>

        </div>
    </div>
</div>
</body>
</html>
