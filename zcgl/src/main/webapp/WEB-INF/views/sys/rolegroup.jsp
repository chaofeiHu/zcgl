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
    <title>角色组管理</title>
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

    </script>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'roleGroupname',
            actionUrl: '<%=basePath%>/RoleGroup/'
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
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');

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
        }

        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getList.do',
                frozenColumns: [[
                    <% if(MenuBtns.indexOf("SetGroupRole")>-1) { %>
                    {
                        field: 'opt',
                        title: '操作',
                        width: 150,
                        align: 'center',
                        formatter: function (val, rec, index) {

                            var strReturn = '';
                            strReturn = '<div class="l-btn-text icon-search" > &nbsp;&nbsp;&nbsp;&nbsp;</div><span style="line-height: 28px"><a href="javascript:void(0)" title="角色组设置角色"  onclick="SetUserRole(\'' + rec[me.idFiled] + '\',\'' + rec[me.displayName] + '\')" >设置角色</a></span>';
                            return strReturn;
                        }
                    },
                    <% } %>

                ]],
                columns: [[

                    {field: 'roleGroupcode', title: '角色组编号', width: 200, align: 'center'},
                    {field: 'roleGroupname', title: '角色组名称', width: 200, align: 'center'},
                    {field: 'back1', title: '备注', width: 200, align: 'center'}

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
                                    names.push(rows[i]["roleGroupname"]);
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
                    param.deptId = me.edit_form.find('#deptId').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });

        }

        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#id,#roleGroupcode,#roleGroupname,#back1').textbox().textbox('setValue', '');
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?id=' + selectedRows[0][me.idFiled],
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
                            url: me.actionUrl + 'AddOrUpdate.do?id=' + selectedRows[0][me.idFiled],
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

        //添加修改（保存）
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
                                $("#edit_window").window("close");
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }
        //删除角色组
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

        //=================================设置角色开始
        //设置角色
        function SetUserRole(id, displayName) {
            $('#role_window_datagrid').datagrid({
                // pagination: false, 显示分页
                idField: "id",
                url:  me.actionUrl+"GetRoleListByGroup.do",
                queryParams: {id: id},
                columns: [[
                    {field: 'ROLE_ID', title: '角色ID', width: 60, align: 'center', hidden: true},
                    {field: 'ROLE_NAME', title: '角色名', width: 400, align: 'center'},
                    {
                        field: 'OPT',
                        title: '设置角色',
                        width: 100,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            var GroupRoleID = rowData['GROUPROLEID'] == undefined ? '' : rowData['GROUPROLEID'];
                            var RoleID = rowData['ROLE_ID'];
                            if (GroupRoleID) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击撤销角色"  onClick="BindUserRole(\'' + GroupRoleID + '\',\'' + RoleID + '\',\'' + id + '\')" style="padding-left:20px;"  align="center">&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击归属角色" onClick="BindUserRole(\'' + GroupRoleID + '\',\'' + RoleID + '\',\'' + id + '\')" style="padding-left:20px;" >&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]]
            });
            $('#role_window').window({title: "设置所属角色：" + displayName}).window('open');
        }

        //提交绑定操作
        function BindUserRole(GroupRoleID, RoleID, id) {
            $.ajax({
                <%--url: "<%=basePath%>/Role/SaveUserRole.do",--%>
                url:  me.actionUrl+"SaveGroupRole.do",
                data: {GroupRoleID: GroupRoleID, id: id, RoleID: RoleID},
                success: function (returnData) {
                    if (returnData) {
                        if (returnData.isOk == 1) {
                            $('#role_window_datagrid').datagrid('reload');
                        } else {
                            showError(returnData.message);
                        }
                    }
                }
            });
        }

        //=================================设置角色结束

    </script>
</head>
<body class="easyui-layout">
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div title="数据维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 550px;height: 350px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>
                            角色组编号：
                        </td>
                        <td>
                            <input id="roleGroupcode" name="roleGroupcode" class="easyui-numberbox" data-options="required:true"
                                   style="width: 250px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            角色组名称：
                        </td>
                        <td>
                            <input id="roleGroupname" name="roleGroupname" class="easyui-textbox" style="width:250px;"
                                   data-options="required:true"/>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="back1" name="back1" style="width:380px;height:60px" class="easyui-textbox"
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 550px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>
</body>
</html>
