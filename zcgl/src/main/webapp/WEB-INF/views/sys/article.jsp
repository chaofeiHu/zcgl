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
    <title>注意事项</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <%--百度编辑器--%>
    <script type="text/javascript" charset="utf-8"
            src="<%=basePath%>/static/js/plugin/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=basePath%>/static/js/plugin/ueditor/ueditor.all.js"></script>
    <style>
        #edui_fixedlayer {
            z-index: 10000 !important;
        }

        .window-mask {
            z-index: 1000 !important;
        }
    </style>
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
            actionUrl: '<%=basePath%>/Article/'
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
                url: me.actionUrl + 'getArticleList.do',

                columns: [[
                    {field: 'TITLE', title: '标题', width: '60%', align: 'center'},
                    {field: 'DISPLAY_NAME', title: '创建人', width: '10%', align: 'center'},
                    {
                        field: 'CREATE_TIME',
                        title: '创建时间',
                        width: '15%',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var unixTimestamp = new Date(value);
                            var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                            return s.substring(0, 9);
                        }
                    },
                    {
                        field: 'TYPE',
                        title: '类型',
                        width: '15%',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '申报指南';
                            } else if (value == '2') {
                                strReturn = '相关下载';
                            } else if (value == '3') {
                                strReturn = '政策指南';
                            } else if (value == '4') {
                                strReturn = '相关问题';
                            } else if (value == '5') {
                                strReturn = '填表须知';
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
                                    ids.push(rows[i].ID);
                                    names.push(rows[i].TITLE);
                                }
                                Delete(ids.join(','), names.join(','));
                            }
                        }
                    }, '-',
                    <% } %>
                    /*  <% if(MenuBtns.indexOf("View")>-1) { %>
                    {
                        text: '查看', iconCls: 'icon-tip', handler: function () {
                            AddOrUpdate('view');
                        }
                    }, '-',
                    <% } %>*/
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
                    me.edit_form.find('#title,achievementtext,type').textbox().textbox('setValue', '');
                    me.edit_window.window('open');
                    $("#ID").val("");
                    ue.setContent('');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        me.edit_window.find('div[region="south"]').css('display', 'block');
                        me.edit_window.window('open');
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?tid=' + selectedRows[0].ID,
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#TYPE').attr('disabled', true);
                                ue.ready(function () {//编辑器初始化完成再赋值
                                    ue.setContent(data.txt);  //赋值给UEditor
                                });
                            }
                        });

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
                var TYPE = $("#TYPE").combobox("getValue");
                if (TYPE == "") {
                    showError("请选择类型!")
                    return;
                }
                $.ajax({
                    url: me.actionUrl + 'AddOrUpdate.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData.isOk == 1) {
                            showInfo(returnData.message);
                            me.datagrid1.datagrid('reload');
                            me.tree1.tree('reload');
                            $("#edit_window").window("close");
                        } else {
                            showError(returnData.message);
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
     style="width: 880px;height: 500px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="userId" name="userId"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>
                            标题：
                        </td>
                        <td>
                            <input id="ID" name="ID" type="hidden">
                            <input id="TEXT" name="TEXT" type="hidden">
                            <input id="TITLE" name="TITLE" class="easyui-textbox" data-options="required:true"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            类型：
                        </td>
                        <td>
                            <select name="TYPE" id="TYPE" class="easyui-combobox" style="width:700px">
                                <option value="">请选择..</option>
                                <option value="1">申报指南</option>
                                <option value="2">相关下载</option>
                                <option value="3">政策指南</option>
                                <option value="4">相关问题</option>
                                <option value="5">填表须知</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            内容：
                        </td>
                        <td colspan="3">
                            <textarea name="achievementtext" id="achievementtext"
                                      style="height:300px;width:700px"></textarea>
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
                            标题：
                        </td>
                        <td>
                            <input name="title1" id="title1" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            类型：
                        </td>
                        <td>
                            <select name="type1" class="easyui-combobox" style="width:200px">
                                <option value="">请选择..</option>
                                <option value="1">申报指南</option>
                                <option value="2">相关下载</option>
                                <option value="3">政策指南</option>
                                <option value="4">相关问题</option>
                                <option value="5">填表须知</option>
                            </select>
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
<script type="text/javascript">

    // var editor = new UE.ui.Editor();

    //editor.render("achievementtext");

    //1.2.4以后可以使用一下代码实例化编辑器

    var ue = UE.getEditor('achievementtext');

</script>
</body>
</html>
