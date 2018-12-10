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
    <title>角色管理</title>
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
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            view_window: null,
            datagrid2: null,
            idFiled: 'roleId',
            actionUrl: '<%=basePath%>/Role/',
            menu_window: null,
            menu_window_tree: null
        };

        $(function () {
            pageInit();
            loadGrid();
        });

        //页面初始化事件
        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.view_window = me.search_window.find('#view_window');
            me.datagrid2 = $('#datagrid2');
            me.datagrid1 = $('#datagrid1');
            me.menu_window = $('#menu_window');
            me.menu_window_tree = me.menu_window.find('#menu_window_tree');

            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.search_window.window('close');
            });
            $('#menu_window_ok').linkbutton().click(function () {
                SaveMenuFun();
            });
            $('#menu_window_no').linkbutton().click(function () {
                me.menu_window.window('close');
            });
        }

        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'GetList.do',
                frozenColumns: [[
                    {field: 'fsort', title: '角色编码', width: '5%', align: 'center'},
                    {field: 'roleName', title: '角色名称', width: '20%', align: 'center'}
                ]],
                columns: [[
                    {field: 'backup1', title: '角色描述', width: '40%', align: 'center'},
                    {field: 'opt', title: '操作', width: '15%', align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            strReturn = '';
                            <% if(MenuBtns.indexOf("SelectRoleUser")>-1) { %>
                            strReturn += '<a href="javascript:void(0)"   title="点击查看角色下的用户" onClick="ViewUser(\'' + rowData[me.idFiled] + '\');" style="padding-left:20px;" >查看用户</a>';
                            <% } %>
                            <% if(MenuBtns.indexOf("SetRoleFu")>-1) { %>
                            strReturn += '<a href="javascript:void(0)"  title="点击设置角色的权限" onClick="SetMenuFun(\'' + rowData[me.idFiled] + '\');" style="padding-left:20px;" >设置权限</a>';
                            <% } %>
                            return strReturn;
                        }
                    },
                    {field: 'state', title: '状态', width: '20%', align: 'center',
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
                toolbar: [
                    <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            AddOrUpdate('add');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Update")>-1) { %>
                    {
                        text: '修改', iconCls: 'icon-edit', handler: function () {
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
                                    names.push(rows[i]["roleName"]);
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
                onBeforeLoad: function (param) {
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });

        }

        //对应操作事件
        function AddOrUpdate(action) {

            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#roleId,#roleName,#backup1,#fsort').textbox().textbox('setValue', '');

                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?roleId=' + selectedRows[0][me.idFiled],
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
                            url: me.actionUrl + 'AddOrUpdate.do?roleId=' + selectedRows[0][me.idFiled],
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

        //保存
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
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }

        //删除角色
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
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            })
        }

        //查看角色关联用户
        function ViewUser(RoleID) {
            $('#role_window_datagrid').datagrid({
                idField: "user_role_id",
                url: me.actionUrl + 'GetUserListByRole.do',
                queryParams: {roleId: RoleID},
                columns: [[
                    {field: 'DISPLAY_NAME', title: '用户名', width: 150, align: 'center'},
                    {field: 'LOGIN_NAME', title: '登录名', width: 150, align: 'center'},
                    {field: 'DEPT_NAME', title: '部门', width: 200, align: 'center'}
                ]]
            });
            $('#role_window').window({title: "角色下用户"}).window('open');

        }

        //获取角色权限
        function SetMenuFun(RoleID) {
            me.menu_window_tree.tree({
                url: me.actionUrl + 'GetRoleTree.do?roleId=' + RoleID,
                data: {roleId: RoleID},
                checkbox: true,
                onlyLeafCheck: false,
                checkboxFn: function (node) {
                    return true;
                },
                onSelect: function (node) {
                    if (node.checked) {
                        $(this).tree('uncheck', node.target);
                    } else {
                        $(this).tree('check', node.target);
                    }
                }
            });
            me.menu_window.data('roleId', RoleID);
            me.menu_window.window({title: "设置权限"}).window('open');
        }

        //保存角色权限
        function SaveMenuFun() {
            var selectedNodes = me.menu_window_tree.tree('getChecked');
            var selectedMenuFunID = [];
            $.each(selectedNodes, function (index, node) {
                if (node.attributes.split('|')[1] != '1') {
                    selectedMenuFunID.push(node.id.split('|')[2]);
                }
            });
            var roleid = me.menu_window.data('roleId');
            $.ajax({
                url: me.actionUrl + 'SaveRoleMenuFun.do',
                data: {roleid: roleid, menufunids: selectedMenuFunID.join(',')},
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
    </script>

</head>
<body class="easyui-layout">
<noscript>
    <div style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; text-align: center;">
        <img src="../images/noscript.gif" alt='抱歉，请开启脚本支持！'/>
    </div>
</noscript>
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div id="edit_window" class="easyui-window" closed="true" title="数据维护窗口"
     style="width: 400px; height: 300px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="deptId" name="deptId"/>
                    <input type="hidden" id="roleId" name="roleId"/>
                </div>
                <table cellpadding="3">
                    <tr>
                        <td>
                            角色编号：
                        </td>
                        <td>
                            <input id="fsort" name="fsort" type="text" class="easyui-textbox" required="true"
                                   style="width:250px"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            角色名称：
                        </td>
                        <td>
                            <input id="roleName" name="roleName" type="text" class="easyui-textbox" required="true"
                                   style="width:250px"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            角色描述：
                        </td>
                        <td>
                            <input id="backup1" name="backup1" style="width:250px;height:50px" class="easyui-textbox"
                                   multiline="true">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            角色状态：
                        </td>
                        <td align="left">
                            <input type="checkbox" id="state" name="state" value="1" checked="checked"/>
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
<div id="search_window" class="easyui-window" closed="true" title="查询窗口"
     style="width: 350px; height: 200px; padding: 5px">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            角色名：
                        </td>
                        <td>
                            <input name="roleName" id="roleName" class="easyui-textbox" style="width: 200px;"/>
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
    <div id="role_window" class="easyui-window" closed="true" title="查看用户窗口" style="width: 550px; height: 400px;">
        <table id="role_window_datagrid"></table>
    </div>
    <div id="menu_window" class="easyui-window" closed="true" title="设置权限窗口" style="width: 300px; height: 450px;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <ul id="menu_window_tree"></ul>
            </div>
            <div region="south" split="false" border="false" style="text-align: center;margin-top: 12px; height:30px;">
                <a href="javascript:void(0)" id="menu_window_ok" icon="icon-ok">确定</a>
                <a href="javascript:void(0)" id="menu_window_no" icon="icon-cancel">关闭</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>