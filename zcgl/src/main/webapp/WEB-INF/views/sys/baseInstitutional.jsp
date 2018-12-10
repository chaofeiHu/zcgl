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
    <script type="text/javascript">
        var me = {
            datagrid1: null,
            datagrid_btn: null,
            edit_form: null,
            edit_window: null,
            edit_form_btn: null,
            edit_window_btn: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'menuId',
            actionUrl: '<%=basePath%>/BaseUnit/'
        };

        $(function () {
            pageInit();
            loadTree();
            loadGrid();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.edit_window_btn = $('#edit_window_btn');
            me.edit_form_btn = me.edit_window_btn.find('#edit_form_btn');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            me.datagrid_btn = $('#menu_window_datagrid');

            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_edit_ok_btn').linkbutton().click(function () {
                SaveDataBtn();
            });
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_edit_cancel_btn').linkbutton().click(function () {
                me.edit_window_btn.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.search_window.window('close');
            });
        }

        //加载树
        function loadTree() {
            me.tree1.tree({
                url: me.actionUrl + 'getBaseUnitTree.do',
                onClick: function (node) {
                    me.edit_form.find('#parentId').val(node.id);
                    me.edit_form.find('#parentName').textbox().textbox('setValue', node.text);
                    me.edit_form.find('#levelPath').textbox().textbox('setValue', node.attributes);
                    me.edit_form.find('#state').attr('checked', true);
                    me.datagrid1.datagrid('reload');
                },
                onLoadSuccess: function (node, param) {
                    var selectedNode = $(this).tree('find', me.edit_form.find('#parentId').val());
                    if (selectedNode)
                        $(this).tree('select', selectedNode.target);
                    me.edit_form.find('#parentName').textbox().textbox('setValue', selectedNode.text);


                    //默认展开第一级节点
                    $(this).tree('collapseAll', selectedNode.target);
                    var childrenNodes= $(this).tree('getLeafChildren',selectedNode.target);
                    for ( var i = 0 ; i < childrenNodes.length ; i++ )
                    {
                        $(this).tree('expandTo',childrenNodes[i].target);
                    }
                }
            });
        }

        //加载列表
        function loadGrid() {

            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getList.do',
                pagination: true,//分页控件
                pageNumber: 1,
                pageSize: 10,//每页显示的记录条数，默认为10
                pageList: [10, 20, 40],//可以设置每页记录条数的列表
                frozenColumns: [[
                    <% if(MenuBtns.indexOf("BindFu")>-1) { %>
                    {
                        field: 'opt',
                        title: '操作',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {

                            var strReturn = '';
                            strReturn = '<div class="l-btn-text icon-more" > &nbsp;&nbsp;&nbsp;&nbsp;</div><span style="line-height: 28px"><a href="javascript:void(0)" title="绑定功能"  onclick="SetMenuFun(\'' + row[me.idFiled]+ '\',\'' + row['menuName']+'\')" >绑定功能</a></span>';
                            return strReturn;
                        }
                    },
                    <% } %>
                ]],
                columns: [[
                    {field: 'unitCode', title: '单位编号', width: 120, align: 'right'},
                    {field: 'unitName', title: '机构名称', width: 100, align: 'center'},
                    {
                        field: 'state',
                        title: '状态',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',1)"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',1)"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    {
                        field: 'isBuildjudging',
                        title: '是否允许可组建评委会',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',2)"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',2)"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    {
                        field: 'isManageunit',
                        title: '是否评委会管理单位',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',3)"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',3)"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    {
                        field: 'isFirmlyorganization',
                        title: '是否认定机构',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',4)"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + rowData.bid + '\',\'' + value + '\',4)"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
                onBeforeLoad: function (param) {
                    param.parentId = me.edit_form.find('#parentId').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                }
            });
        }


        //更改状态操作
        function changeReview(bid, value, type) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {bid: bid, state: value, type: type},
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



        //=================================设置功能开始
        function SetMenuFun(MenuID,menuName) {
            me.edit_form_btn.find('#menuId').val(MenuID);

            me.datagrid_btn.datagrid({
                idField: "menu_fun_id",
                pagination: false,
                url: me.actionUrl + 'GetListByMenuID.do',
                queryParams: {menuId: MenuID, state: 1},
                columns: [[

                    /* {field: 'dict_id', title: '功能ID', width: 60,  align: 'center'},*/
                    {field: 'BTN_NAME', title: '功能名', width: 100, align: 'center'},
                    {field: 'BTN_CODE', title: '功能编码', width: 100, align: 'center'},

                    {
                        field: 'STATE',
                        title: '绑定状态',
                        width: 100,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {

                            var strReturn = '';
                            var MenuFunID = (rowData['MENU_FUN_ID'] == 'undefined') ? 0 : rowData['MENU_FUN_ID'];
                            var state = (rowData['STATE'] == 'undefined') ? 0 : rowData['STATE'];
                            if (state) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击取消绑定功能"  onClick="UpdateBtnState(\'' + MenuFunID + '\',\'' + '0' + '\');" style="padding-left:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击绑定功能" onClick="UpdateBtnState(\'' + MenuFunID + '\',\'' + '1' + '\');" style="padding-left:20px;" >&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]]
            });
            $('#menu_window').window({title: "绑定菜单功能("+menuName+")"}).window('open');

        }


        //菜单绑定按钮操作
        function UpdateBtnState(MenuFunID, State) {
            $.ajax({
                url: me.actionUrl + 'menuBindBtn.do',
                data: {MenuFunID: MenuFunID, state: State},
                success: function (returnData) {
                    if (returnData) {
                        if (returnData.isOk == 1) {
                            $('#menu_window_datagrid').datagrid('reload');
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

<div region="west" hide="true" split="true" title="导航树" style="width: 180px;">
    <ul id="tree1">
    </ul>
</div>
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div id="edit_window" class="easyui-window" closed="true" title="数据维护窗口"
     style="width: 500px;height: 380px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="menuId" name="menuId"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>URL：</td>
                        <td>
                            <input id="menuUrl" name="menuUrl" class="easyui-textbox"
                                   data-options="required:true" style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>编码：</td>
                        <td>
                            <input id="menuCode" name="menuCode" class="easyui-textbox"
                                   required="true" style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>名称：</td>
                        <td>
                            <input id="menuName" name="menuName" class="easyui-textbox"
                                   data-options="required:true" style="width: 250px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>上级菜单：</td>
                        <td>
                            <input id="parentName" name="parentName" class="easyui-textbox" style="width: 250px;"
                                   readonly="true"
                                   value="菜单管理"/>
                            <input id="parentId" name="parentId" type="hidden"
                                   reuired="true" value="00000000-0000-0000-0000-000000000000"/>
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
                    <tr style="display: none">
                        <td>层级：</td>
                        <td>
                            <input id="levelPath" name="levelPath" class="easyui-textbox"
                                   style="width: 200px;"/>
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
<div id="search_window" class="easyui-window" closed="true" title="查询窗口"
     style="width: 300px;height: 170px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            名称：
                        </td>
                        <td>
                            <input name="menuName" id="menuName" class="easyui-textbox" style="width: 200px;"/>
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
<div id="menu_window" class="easyui-window" closed="true" title="设置功能" style="width: 350px;height: 500px;">
    <table id="menu_window_datagrid"></table>
</div>
<div id="edit_window_btn" class="easyui-window" closed="true" title="数据维护窗口_按钮"
     style="width: 300px;height: 250px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form_btn" name="edit_form_btn" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="menuId" name="menuId"/>
                    <input type="hidden" id="menuFunId" name="menuFunId"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>按钮编码：</td>
                        <td>
                            <input id="btnCode" name="btnCode" class="easyui-textbox"
                                   data-options="required:true" />
                        </td>
                    </tr>
                    <tr>
                        <td>按钮名：</td>
                        <td>
                            <input id="btnName" name="btnName" class="easyui-textbox"
                                   required="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>排序：</td>
                        <td>
                            <input id="fsort" name="fsort" class="easyui-numberbox" style="width: 50px;"
                                   reuired="true"
                                   maxlength="20" value="0"/>

                            状态：<input type="checkbox" id="state" name="state" value="1" checked="checked"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a id="btn_edit_ok_btn" icon="icon-save" href="javascript:void(0)">确定</a>
            <a id="btn_edit_cancel_btn" icon="icon-cancel" href="javascript:void(0)">关闭</a>
        </div>
    </div>
</div>
</body>
</html>