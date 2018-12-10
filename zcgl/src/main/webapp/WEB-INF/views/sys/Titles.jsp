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

    </script>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'bid',
            displayName: 'unitName',
            actionUrl: '<%=basePath%>/Titles/'
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
                pagination:true,
                columns: [[
                    {field: 'qualificationCode', title: '资格代码', width: 100, align: 'center'},
                    {field: 'qualificationName', title: '资格名称', width: 100, align: 'center'},
                    {
                        field: 'isReview',
                        title: '是否允许评审',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + rowData.id + '\',\'' + value + '\')"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + rowData.id + '\',\'' + value + '\')"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }, {
                        field: 'isFirmlyBelieve',
                        title: '是否允许认定',
                        width: 160,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeState(\'' + rowData.id + '\',\'' + value + '\')"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeState(\'' + rowData.id + '\',\'' + value + '\')"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
                checkOnSelect:true,
                singleSelect:true,
                selectOnCheck:true,
                pageNumber:1,
                toolbar: [
                    <% if(MenuBtns.indexOf("View")>-1) { %>
                    {
                        text: '查找', iconCls: 'icon-search', handler: function () {
                            me.search_window.window('open');
                        }
                    }
                    <% } %>,
                    <% if(MenuBtns.indexOf("insert")>-1) { %>
                    {
                        text: '同步', iconCls: 'icon-edit', handler: function () {
                           // me.search_window.window('open');
                            //AddOrUpdate("add");
                            tongbu();
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
        function tongbu() {

                $("")
                $.ajax({
                    url: me.actionUrl + 'InsertTitles.do',
                    //data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == "1") {
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
        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#areaGrade1,#areaGrade2,#areaGrade3').combobox().combobox('setValue', '   ==请选择==');
                    me.edit_form.find('#unitCode,#bid,#organizationCode,#creditCode,#unitName ,#unitAttach,#unitCategory ,#industryInvolved ,#unitNature  ,#economicType ,#areaNumber  ,#linkMan ,#phone ,#address ,#fax ,#email ,#postalCode ,#parentUnitCode ').textbox().textbox('setValue', '');
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?bid=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data.baseUnit);
                                me.edit_form.find('#areaGrade1').combobox().combobox('setValue', '410000');
                                $("#areaGrade2").combobox('setValue',data.areaCode1);
                                $("#areaGrade3").combobox('setValue',data.areaCode2);
                                $("#areaGrade2").combobox('setText',data.areaName1);
                                $("#areaGrade3").combobox('setText',data.areaName2);
                                me.edit_form.find('#unitName').attr('disabled', true);
                                me.edit_window.find('div[region="south"]').css('display', 'block');
                                me.edit_window.window('open');
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
                                me.edit_form.find('#unitName').attr('disabled', true);
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
                $("")
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


        //=================================设置角色结束*/
        //更改状态操作
        function changeState(bid, value) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {id: bid, state: value,type:2},
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
        function changeReview(bid, value) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {id: bid, state: value,type:1},
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
     style="width: 800px;height: 450px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="bid" name="bid" value="00000000-0000-0000-0000-000000000000"/>
                    <%--<input type="hidden" id="unitCode" name="unitCode"/>--%>
                </div>
                <table cellpadding="5"  align="center">
                   <%-- <tr>
                        <td>
                            单位编号：
                        </td>
                        <td>
                            <input id="unitCode" name="unitCode" class="easyui-textbox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                    </tr>--%>
                    <%--<tr>
                        <td>
                            统一社会信用代码：
                        </td>
                        <td>
                            <input id="creditCode" name="creditCode" class="easyui-textbox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                    </tr>--%>
                    <%--<tr>
                       <td>
                           组织机构代码：
                       </td>
                       <td>
                           <input id="organizationCode" name="organizationCode" class="easyui-textbox" data-options="required:true"
                                  style="width: 150px" maxlength="30"/>
                       </td>
                    </tr>--%>
                    <tr>
                        <td >
                        机构名称：
                        </td>
                        <td colspan="3">
                            <input id="unitName" name="unitName" class="easyui-textbox" data-options="required:true"
                                   style="width: 450px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            机构隶属：
                        </td>

                        <td colspan="3">
                            <input id="unitAttach" class="easyui-combobox" name="unitAttach" value="机构隶属" style="width: 450px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_ATTACH'" />

                        </td>
                    </tr>
                    <tr>
                        <td>
                            机构类别：
                        </td>
                        <td colspan="3">
                            <input id="unitCategory" class="easyui-combobox" name="unitCategory" value="机构类别" style="width: 450px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_CATEGORY'" />

                        </td>
                    </tr>
                    <tr>
                        <td>
                            所属行业：
                        </td>
                        <td colspan="3">
                            <input id="industryInvolved" class="easyui-combobox" name="industryInvolved" value="所属行业" style="width: 450px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=INDUSTRY_INVOLVED'" />

                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位性质：
                        </td>
                        <td colspan="3">
                            <input id="unitNature" class="easyui-combobox" name="unitNature" value="单位性质" style="width: 450px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_NATURE'" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            经济类型：
                        </td>
                        <td colspan="3">
                            <input id="economicType" class="easyui-combobox" name="economicType" value="经济类型" style="width: 450px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ECONOMIC_TYPE'" />
                        </td>
                    </tr>
                    <tr>
                        <td >
                            行政区划代码：
                        </td>

                        <td style="width:150px;word-break:break-all">
                            <input id="areaGrade1" class="easyui-combobox" value="==请选择==" style="width: 150px" maxlength="30"
                                   data-options="
                                   valueField:'areaCode',
                                   textField:'areaName',
                                   url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=1',

                                    onSelect: function(data){
                                            var url = '<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2&areaCode='+data.areaCode;
                                            $('#areaGrade2').combobox('reload', url);
                                        }
                                    "
                            />

                        </td>
                        <td style="width:150px;word-break:break-all">
                            <input id="areaGrade2" class="easyui-combobox"  value="==请选择==" style="width: 150px" maxlength="30"
                                   data-options="
                                   valueField:'areaCode',
                                   textField:'areaName',
                                   url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2',
                                   <%--onLoadSuccess: function (data) {
                                     $('#areaGrade2').combobox('select',data.areaCode);
                                    },--%>
                                    onSelect: function(data){
                                            var url = '<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=3&areaCode='+data.areaCode;
                                            $('#areaGrade3').combobox('reload', url);
                                        }
                                    " />
                        </td>
                        <td>
                            <input id="areaGrade3" class="easyui-combobox" name="areaNumber" value="==请选择==" style="width: 150px" maxlength="30"
                                   data-options="
                                   valueField:'areaCode',textField:'areaName'" />
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
                            显示名：
                        </td>
                        <td>
                            <input name="qualificationName" id="qualificationName" class="easyui-textbox" style="width: 150px;"/>
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>
</body>
</html>
