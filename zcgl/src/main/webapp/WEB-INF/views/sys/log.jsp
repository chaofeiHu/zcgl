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
    <title>日志信息</title>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script type="text/javascript">
        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            view_window: null,
            idFiled: 'logId',
            actionUrl: '<%=basePath%>/Log/',
        };

        $(function () {
            pageInit();
            loadGrid();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.view_window = me.search_window.find('#view_window');
            me.datagrid2 = $('#datagrid2');
            me.datagrid1 = $('#datagrid1');

            $('#btn_edit_ok,#btn_edit_cancel,#btn_search_ok,#btn_search_cancel').linkbutton();
            $('#btn_edit_ok').click(function () {
                SaveData();
            });
            $('#btn_search_ok').click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_edit_cancel').click(function () {
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').click(function () {
                me.search_window.window('close');
            });
        }

        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                singleSelect: false,
                idField: me.idFiled,
                url: me.actionUrl + 'getList.do',
                pagination : true,//分页控件
                frozenColumns: [[
                    {field: 'ck', checkbox: true}
                ]],
                columns: [[
                    {field: 'logId', title: '日志编码', width:80, align: 'center'},
                    {field: 'ip', title: '客户端IP', width: 120, align: 'center'},
                    {field: 'page', title: '操作路径', width: 200, align: 'center'},
                    {field: 'backup1', title: '所属机构', width: 200, align: 'center'},
                    {field: 'addUserId', title: '机构操作人', width: 200, align: 'center'},
                    {field: 'addTime', title: '操作时间', width: 170, align: 'center'},
                    {field: 'message', title: '操作内容', width: 300, align: 'center'}
                ]],
                toolbar: [
                    <% if(MenuBtns.indexOf("Delete")>-1) { %>
                    {
                        text: '删除', iconCls: 'icon-remove', handler: function () {
                            var ids = [];
                            var rows = me.datagrid1.datagrid('getSelections');
                            if (rows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            } else {
                                for (var i = 0; i < rows.length; i++) {
                                    ids.push(rows[i][me.idFiled]);
                                }
                                Delete(ids.join(','));
                            }
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Select")>-1) { %>
                    {
                        text: '查找', iconCls: 'icon-search', handler: function () {
                            me.search_window.window('open');
                        }
                    }, '-',
                    <% } %>

                    <% if(MenuBtns.indexOf("Export")>-1) { %>
                    {
                        text: '导出Excel', iconCls: 'icon-excel', handler: function () {
                            window.location.href = me.actionUrl +'exportExcelLog.do'
                        }
                    }
                    <% } %>

                ],

                onBeforeLoad: function (param) {
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });

        }

        function Delete(ids) {
            $.messager.confirm('提示信息', '确认要删除选择项？【' + ids + '】', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete.do',
                        data: {ids: ids},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);
                                    me.datagrid1.datagrid('reload');
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            })
        }

    </script>
</head>
<body class="easyui-layout">
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div id="search_window" class="easyui-window" closed="true" title="查询窗口" style="width: 300px;
		height:190px; padding: 5px;" closed="true">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 15px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            关键字：
                        </td>
                        <td>
                            <input name="message" id="message" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a href="javascript:void(0)" id="btn_search_ok" icon="icon-ok">确定</a>
            <a href="javascript:void(0)" id="btn_search_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>
</body>
</html>
