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
    <title>评审控制指标</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/js/jquery.cookie.js" charset="GBK"></script>
    <script type="text/javascript">
        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            tree1: null,
            actionUrl: '<%=basePath%>/ParamReview/'
        };

        $(function () {
            pageInit();
            loadTree();
        });

        function pageInit() {
            me.edit_form = $('#edit_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            $('#btn_edit_ok,#btn_edit_cancel,#btn_search_ok,#btn_search_cancel').linkbutton();
            $('#btn_edit_ok').click(function () {
                SaveData();
            });
            $('#btn_search_ok').click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
        }
        //加载树
        function loadTree() {
            me.tree1.tree({
                url: me.actionUrl + 'getTree.do',
                onClick: function (node) {
                    chakan(node.id);
                },
                onLoadSuccess: function (node, param) {
                    var selectedNode = $(this).tree('find', me.edit_form.find('#parentId').val());
                    if (selectedNode)
                        $(this).tree('select', selectedNode.target);
                    if ($("#judGingCode").val()==null||$("#judGingCode").val()==''){
                        chakan(param[0].id);
                    } else {
                        chakan($("#judGingCode").val());
                    }
                },
                onLoadError: function () {
                    showError('数据加载错误！');
                }
            });
        }
        function chakan(judGingCode) {
            $("#judGingCode").val(judGingCode);
            $.ajax({
                url: me.actionUrl + 'select.do?judGingCode=' + judGingCode,
                success: function (data) {
                    if (data==null){
                        $("#id").val('');
                        $("#fg").textbox("setValue", "");
                        $("#zg").textbox("setValue", "");
                        $("#zj").textbox("setValue", "");
                    }else {
                        $("#judGingCode").val(judGingCode);
                        me.edit_form.form('load', data);
                    }
                }
            });
        }

        function SaveData() {
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.datagrid1.datagrid('reload');
                                me.tree1.tree('reload');
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }
    </script>
</head>
<body class="easyui-layout">
<div region="west" hide="true" split="true" title="评委会" style="width: 400px;">
    <ul id="tree1">
    </ul>
</div>
<div title="（0和空表示不限制, -1表示禁止申报）" region="center" border="false">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="judGingCode" name="judGingCode"/>
                </div>
                <table>
                    <tr>
                        <td>
                            正高数量：
                        </td>
                        <td>
                            <input id="zg" name="zg" type="text" class="easyui-textbox" required="true"
                                   style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            副高数量：
                        </td>
                        <td>
                            <input id="fg" name="fg" type="text" class="easyui-textbox" required="true"
                                   style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            中级数量：
                        </td>
                        <td>
                            <input id="zj" name="zj" type="text" class="easyui-textbox" required="true"
                                   style="width: 250px;"/>
                        </td>
                    </tr>
                </table>
            </form>
            <div region="south" border="false" style="margin-top: 12px;margin-left: 170px; height:30px;">
                <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">保存</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
