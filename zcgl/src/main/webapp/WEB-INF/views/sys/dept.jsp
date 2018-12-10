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
    <title>信息管理系统</title>
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
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'deptId',
            actionUrl: '<%=basePath%>/Dept/'
        };

        $(function () {
            pageInit();
            loadTree();
            loadGrid();
        });


        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
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

        //加载树
        function loadTree() {
            me.tree1.tree({
                url: me.actionUrl + 'getTree.do',
                onClick: function (node) {
                    me.edit_form.find('#parentId').val(node.id);
                    me.edit_form.find('#parentName').textbox().textbox('setValue', node.text);
                    me.edit_form.find('#levelPath').val(node.attributes);
                    me.edit_form.find('#state').attr('checked', true);
                    me.datagrid1.datagrid('reload');
                },
                onLoadSuccess: function (node, param) {
                    var selectedNode = $(this).tree('find', me.edit_form.find('#parentId').val());
                    if (selectedNode)
                        $(this).tree('select', selectedNode.target);
                    me.edit_form.find('#parentName').textbox().textbox('setValue', selectedNode.text);
                },
                onLoadError: function () {
                    showError('数据加载错误！');
                }
            });
        }

        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getList.do',
                frozenColumns: [[
                    {
                        field: 'state',
                        title: '状态',
                        width: 60,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok"  style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
                columns: [[
                    {field: 'deptName', title: '部门名称', width: 200, align: 'center'},
                    {field: 'deptCode', title: '部门编码', width: 100, align: 'center'},
                    {field: 'fsort', title: '排序', width: 60, align: 'center'},
                    {field: 'backup1', title: '备注', width: 200, align: 'left'}
                ]],
                toolbar: [
                    <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: function () {
                            AddOrUpdate('add');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Update")>-1) { %>
                    {
                        text: '修改',
                        iconCls: 'icon-edit',
                        handler: function () {
                            AddOrUpdate('update');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Delete")>-1) { %>
                    {
                        text: '删除', iconCls: 'icon-remove', handler: function () {
                            var ids = [];
                            var names = [];
                            var rows = me.datagrid1.datagrid('getSelections');
                            if (rows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            } else {
                                for (var i = 0; i < rows.length; i++) {
                                    ids.push(rows[i][me.idFiled]);
                                    names.push(rows[i]["deptName"]);
                                }
                                Delete(ids.join(','), names.join(','));
                            }
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("View")>-1) { %>
                    {
                        text: '查看', iconCls: 'icon-tip', handler: function () {
                            AddOrUpdate('view');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Select")>-1) { %>
                    {
                        text: '查找', iconCls: 'icon-search', handler: function () {
                            me.search_window.window('open');
                        }
                    }
                    <% } %>
                ],
                pageNumber: 1,
                pageSize: 10,
                onBeforeLoad: function (param) {
                    param.parentId = me.edit_form.find('#parentId').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });
        }

        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#deptId').val('');
                    me.edit_form.find('#deptCode,#deptName,#backup1').textbox().textbox('setValue', '');
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?deptId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                            }
                        });
                        me.edit_window.find('div[region="south"]').css('display', 'block');
                        me.edit_window.window('open');
                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;
                case 'view':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?deptId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                            }
                        });
                        me.edit_window.find('div[region="south"]').css('display', 'none');
                        me.edit_window.window('open');
                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;
            }
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

        function Delete(ids, names) {
            $.messager.confirm('提示信息', '确认要删除选择项？【' + names + '】', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete.do',
                        data: {ids: ids},
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
            })
        }

    </script>
</head>
<body class="easyui-layout">
<div region="west" hide="true" split="true" title="导航树" style="width: 200px;">
    <ul id="tree1">
    </ul>
</div>
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div id="edit_window" class="easyui-window" closed="true" title="数据维护窗口"
     style="width: 400px;height: 350px; padding: 5px;" closed="true">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数">
                    <input type="hidden" id="deptId" name="deptId"/>
                    <input type="hidden" id="levelPath" name="levelPath"/>
                </div>
                <table cellpadding="3">
                    <tr>
                        <td>
                            部门编码：
                        </td>
                        <td>
                            <input id="deptCode" name="deptCode" type="text" class="easyui-textbox" required="true"
                                   style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            部门名称：
                        </td>
                        <td>
                            <input id="deptName" name="deptName" type="text" class="easyui-textbox" required="true"
                                   style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>上级部门：</td>
                        <td>
                            <input id="parentName" name="parentName" class="easyui-textbox" style="width: 250px;"
                                   readonly="true"
                                   value="系统基础代码"/>
                            <input id="parentId" name="parentId" type="hidden"
                                   value="00000000-0000-0000-0000-000000000000"/>
                        </td>
                    </tr>
                    <tr>
                        <td>排序：</td>
                        <td>
                            <input id="fsort" name="fsort" class="easyui-numberbox" style="width: 50px;" reuired="true"
                                   maxlength="20" value="0"/>

                            部门状态：<input type="checkbox" id="state" name="state" value="1" checked="checked"/>
                        </td>
                    </tr>
                    <tr>
                        <td>备注：</td>
                        <td>
                            <input id="backup1" name="backup1" style="width:250px;height:50px" class="easyui-textbox"
                                   multiline="true">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">确定</a>
            <a id="btn_edit_cancel" icon="icon-cancel" href="javascript:void(0)">关闭</a>
        </div>
    </div>
</div>
<div id="search_window" class="easyui-window" closed="true" title="查询窗口" style="width: 350px;
		height: 200px; padding: 5px;" closed="true">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            部门名称：
                        </td>
                        <td>
                            <input name="deptName" id="deptName" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_search_ok" icon="icon-ok">确定</a>
            <a href="javascript:void(0)" id="btn_search_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>
</body>
</html>
