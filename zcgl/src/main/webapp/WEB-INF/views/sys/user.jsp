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
    <title>本单位用户管理</title>
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
            idFiled: 'userId',
            displayName: 'displayName',
            actionUrl: '<%=basePath%>/User/'
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

            $('#mobilephone').validatebox({
                required: true,
                validType: 'mobile'
            });

            $('#mobilephone').bind('blur',function(){
                var mobilephone = $('#mobilephone').val();
                var displayName = $("#displayName").textbox().textbox("getValue");
                if (mobilephone.length !== 0 && displayName !== "系统管理员"){
                    $('#loginName').val(mobilephone);
                }
            });

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
                    {
                        field: 'state',
                        title: '状态',
                        width: 60,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeState(\'' + rowData.userId + '\',\'' + value + '\')"  style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeState(\'' + rowData.userId + '\',\'' + value + '\')" style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    <% if(MenuBtns.indexOf("SetUserRole")>-1) { %>
                    {
                        field: 'opt',
                        title: '操作',
                        width: 150,
                        align: 'center',
                        formatter: function (val, rec, index) {

                            var strReturn = '';
                            strReturn = '<div class="l-btn-text icon-search" > &nbsp;&nbsp;&nbsp;&nbsp;</div><span style="line-height: 28px"><a href="javascript:void(0)" title="用户设置角色"  onclick="SetUserRole(\'' + rec[me.idFiled] + '\',\'' + rec[me.displayName] + '\')" >设置角色</a></span>';
                            return strReturn;
                        }
                    },
                    <% } %>

                    {field: 'displayName', title: '姓名', width: 100, align: 'center'}
                ]],
                columns: [[

                    {field: 'loginName', title: '登录名', width: 200, align: 'center'},
                    {
                        field: 'sex',
                        title: '性别',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {

                            if (row.sex == "1") return '男'; else return '女';
                        }
                    },
                    {field: 'mobilephone', title: '手机号', width: 150, align: 'center'},
                    {field: 'email', title: '电子邮箱', width: 150, align: 'center'},
                    {field: 'idCardNo', title: '身份证号', width: 180, align: 'center'}
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
                                    names.push(rows[i]["displayName"]);
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
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#sex').combobox().combobox('setValue', '1');
                    me.edit_form.find('#userId,#displayName,#idCardNo,#email,#backup1').textbox().textbox('setValue', '');
                    me.edit_form.find('#loginName').attr('disabled', true);
                    me.edit_form.find('#displayName').textbox({disabled:false});
                    $("#mobilephone").val('');
                    $("#loginName").val('');
                    $("#mobilephone").validatebox({ required: true });
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?userId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                if(data.displayName =="系统管理员"){
                                    me.edit_form.find('#displayName').textbox({disabled:true});
                                    $("#mobilephone").validatebox({ required: false });
                                }else {
                                    $("#mobilephone").validatebox({ required: true });
                                }
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
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
                            url: me.actionUrl + 'AddOrUpdate.do?userId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
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
                var loginName =  $('#loginName').val();
                //console.log(loginName);
                $.ajax({
                    url: me.actionUrl + 'Save.do?loginName='+loginName,
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

        //删除用户
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
        function SetUserRole(userid, displayName) {
            $('#role_window_datagrid').datagrid({
                //pagination: false, 显示分页
                idField: "UserRoleID",
                url: "<%=basePath%>/Role/GetRoleListByUser.do?state=1",
                queryParams: {userId: userid},
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
                            var UserRoleID = rowData['USERROLEID'] == undefined ? '' : rowData['USERROLEID'];
                            var RoleID = rowData['ROLE_ID'];
                            if (UserRoleID) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击撤销角色"  onClick="BindUserRole(\'' + UserRoleID + '\',\'' + RoleID + '\',\'' + userid + '\')" style="padding-left:20px;"  align="center">&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击归属角色" onClick="BindUserRole(\'' + UserRoleID + '\',\'' + RoleID + '\',\'' + userid + '\')" style="padding-left:20px;" >&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]]
            });
            $('#role_window').window({title: "设置所属角色：" + displayName}).window('open');
        }

        //提交绑定操作
        function BindUserRole(UserRoleID, RoleID, UserID) {
            $.ajax({
                url: "<%=basePath%>/Role/SaveUserRole.do",
                data: {UserRoleID: UserRoleID, UserID: UserID, RoleID: RoleID},
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

        //更改状态操作
        function changeState(id, value) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {userId: id, state: value},
                success: function (returnData) {
                    if (returnData) {
                        if (returnData.isOk == 1) {
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
                    <input type="hidden" id="userId" name="userId"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>
                            姓名：
                        </td>
                        <td>
                            <input id="displayName" name="displayName" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true,validType:'chinese'"/>
                        </td>
                        <td>
                            性别：
                        </td>
                        <td>
                            <input id="sex" class="easyui-combobox" name="sex" value="性别" style="width: 150px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=SEX'" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            手机号：
                        </td>
                        <td>
                            <input id="mobilephone" name="mobilephone" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   data-options=""/>
                        </td>

                        <td>
                            登录名：
                        </td>
                        <td>
                            <input id="loginName" name="loginName" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   data-options=""/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            身份证号：
                        </td>
                        <td>
                            <input id="idCardNo" name="idCardNo" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'idcard'"/>
                        </td>
                        <td>
                            电子邮箱：
                        </td>
                        <td>
                            <input id="email" name="email" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'email'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="backup1" name="backup1" style="width:380px;height:60px" class="easyui-textbox"
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
<div title="查询窗口" id="search_window" class="easyui-window" closed="true"
     style="width: 350px;height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            姓名：
                        </td>
                        <td>
                            <input name="displayName" id="displayName1" class="easyui-textbox" style="width: 150px;"/>
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 550px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>
</body>
</html>

