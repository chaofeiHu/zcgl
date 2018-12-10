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
    <title>评审系列与专业绑定</title>
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
            datagrid2:null,
            datagrid3:null,
            tree1: null,
            idFiled: 'id',
            revser:' reviewSeries',
            actionUrl: '<%=basePath%>/SeriesProfessial/',
            menu_window: null,
            menu_window_tree: null
        };

        $(function () {
            pageInit();
            loadGrid();
        });

        function pageInit() {
            me.tree1 = $('#tree1');
            me.datagrid2 = $('#datagrid2');
            me.datagrid3 = $('#datagrid3');
            me.menu_window = $('#menu_window');
            me.menu_window_tree =$('#menu_window_tree');
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.series_window.window('close');
            });
            $('#btn_major_cancel').linkbutton().click(function () {
                me.major_window.window('close');
            });
            $('#menu_window_ok').linkbutton().click(function () {
                SaveMenuFun();
            });
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
        }

        //加载列表
        function loadGrid() {
            me.datagrid2.datagrid({
                idField: me.idFiled,
                url: me.actionUrl +'getDictList',
                pagination:false,
                showHeader:false,
                onClickRow:loadGrid2,
                columns: [[
                    {field: 'dictName',width: '100%', align: 'center'}
                ]],
                toolbar: [
                ],
            });

        }
        //加载评审系列列表
        var loadGrid2 = function (rowIndex, rowData, value) {
            me.edit_form.find('#reviewSeries').textbox().textbox('setValue', rowData['dictCode']);
            me.datagrid3.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getJudgingSerList.do?reviewSeries=' + rowData['dictCode'],
                pagination: true,
                frozenColumns: [[
                    {
                        field: 'state',
                        title: '状态',
                        width: 60,
                        align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
                columns: [[
                    {field: 'professialCode', title: '编码', width: 100, align: 'center'},
                    {field: 'professialName', title: '名称', width: 200, align: 'center'}
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
                            var rows = me.datagrid3.datagrid('getSelections');
                            if (rows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            } else {
                                for (var i = 0; i < rows.length; i++) {
                                    ids.push(rows[i][me.idFiled]);
                                    names.push(rows[i]["professialName"]);
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

            })
        }
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#id,#professialCode,#professialName').textbox().textbox('setValue', '');
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid3.datagrid('getSelections');
                    $("#id2").val(selectedRows[0][me.idFiled])
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
                    var selectedRows = me.datagrid3.datagrid('getSelections');
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
        function SaveData() {
            // var reviewSeries = $('#reviewSeries').val();
            // alert(reviewSeries)
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.edit_window.window('close');
                                me.datagrid3.datagrid('reload');
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
                                    me.datagrid3.datagrid('reload');
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            })
        }

        //保存评审系列与专业绑定
        function SaveMenuFun() {
            var selectedNodes = me.menu_window_tree.tree('getChecked');
            var selectedMenuFunID = [];
            $.each(selectedNodes, function (index, node) {
                if (node.id != '000000') {
                    selectedMenuFunID.push(node.id);
                }
            });
            var reviewSeries = me.revser;
            $.ajax({
                url: me.actionUrl + 'saveReviewSeries.do',
                data: {reviewSeries:reviewSeries, menufunids: selectedMenuFunID.join(',')},
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
<div id="menu_window" class="easyui-window" closed="true" title="设置权限窗口" style="width: 300px; height: 450px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
        </div>
    </div>
</div>
<div data-options="region:'south',split:true" style="height:100%;">
    <div class="easyui-layout" data-options="fit:true">
        <div title="评审系列" region="center" border="false">
            <table id="datagrid2"></table>
        </div>

        <div data-options="region:'east',split:true" style="width:60%">
            <div class="easyui-layout" data-options="fit:true">
                <div title="相关专业" region="center" border="false">
                    <table id="datagrid3">
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="edit_window" class="easyui-window" closed="true" title="数据维护窗口"
     style="width: 300px;height: 240px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 5px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="id2" name="id2"/>
                    <input type="hidden" id="reviewSeries" name="reviewSeries"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>编码：</td>
                        <td>
                            <input id="professialCode" name="professialCode"  class= "easyui-textbox" required ="true"
                            validType= "remote['<%=basePath %>/SeriesProfessial/checkCode.do?reviewSeries='+reviewSeries.value+'&id='+id2.value,'professialCode']"
                            invalidMessage= "该编码已存在"
                                   style="width:160px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>名称：</td>
                        <td>
                            <input id="professialName" name="professialName" type="text" class="easyui-textbox"
                                   style="width:160px;"
                                   validType= "remote['<%=basePath %>/SeriesProfessial/checkCode.do?reviewSeries='+reviewSeries.value+'&id='+id2.value,'professialName']"
                                   invalidMessage= "该名称已存在"
                                   required="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>排序：</td>
                        <td>
                            <input id="fsort" name="fsort" class="easyui-numberbox" style="width: 50px;" reuired="true"
                                   maxlength="20" value="0"/>

                            状态：<input type="checkbox" id="state" name="state" value="1" checked="checked"/>
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
</body>
</html>

