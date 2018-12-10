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
            actionUrl: '<%=basePath%>/BaseBlackList/'
        };

        $(function () {
            pageInit();
            loadGrid();
            getTree();

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
                url: me.actionUrl + 'getList.do',
                columns: [[
                    {field: 'PROPOSER_NAME', title: '姓名', width: '100px', align: 'center'},
                    {field: 'UNIT_NAME', title: '单位名称', width: '150px', align: 'center'},
                    {field: 'ID_CARD_NO', title: '身份证号', width: '180px', align: 'center'},
                    {field: 'MOBILEPHONE', title: '手机号', width: '120px', align: 'center'},
                    {
                        field: 'RELIEVE_DATE',
                        title: '解禁时间',
                        width: '15%',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var unixTimestamp = new Date(value);
                            var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                            return s.substring(0, 10);
                        }
                    },
                    {field: 'REASON', title: '理由', width: '200px', align: 'center'},
                    {field: 'DISPLAY_NAME', title: '添加人', width: '10%', align: 'center'},
                    {
                        field: 'ADDTIME',
                        title: '添加时间',
                        width: '15%',
                        align: 'center',
                        formatter: function (value, row, index) {
                            var unixTimestamp = new Date(value);
                            var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                            return s.substring(0,10);
                        }
                    },
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
                                    names.push(rows[i].PROPOSER_NAME);
                                }
                                Delete(ids.join(','), names.join(','));
                            }
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
                    me.edit_form.form('clear');
                    //me.edit_form.find('#title,achievementtext,type').textbox().textbox('setValue', '');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        me.edit_window.find('div[region="south"]').css('display', 'block');
                        me.edit_window.window('open');
                        $.ajax({
                            url: me.actionUrl + 'selectBlackList.do?ID=' + selectedRows[0].ID,
                            success: function (data) {
                                var unixTimestamp = new Date(data.relieveDate);
                                var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                                me.edit_form.form('load', {
                                    proposerName:data.proposerName,
                                    idCardNo:data.idCardNo,
                                    mobilephone:data.mobilephone,
                                    reason:data.reason,
                                    unitName:data.unitName,
                                    id:data.id
                                });
                                $("#relieveDate").datebox('setValue',  s.substring(0, 10));
                            }
                        });

                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;
            }
        }

        //添加修改（保存）
        function SaveData() {
            var unitName = $("#unitName").combobox('getText');
            console.log(unitName);
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'AddOrUpdate.do?unitName='+unitName,
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                            showInfo(returnData);
                            me.datagrid1.datagrid('reload');
                            $("#edit_window").window("close");
                    },error:function(data){
                        showError(returnData);
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
                        data: {ID: ids},
                        success: function (returnData) {
                            if (returnData) {
                                    showInfo(returnData);
                                    me.datagrid1.datagrid('reload');
                            }
                        }
                    });
                }
            })
        }

        //自定义身份证验证
        $.extend($.fn.validatebox.defaults.rules, {
            idcared: {
                validator: function(value,param){
                    var flag= checkIDCard(value);
                    return flag==true?true:false;
                },
                message: '不是有效的身份证号码!'
            }
        });

        //身份证号验证
        function checkIDCard(idcode){
            // 加权因子
            var weight_factor = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
            // 校验码
            var check_code = ['1', '0', 'X' , '9', '8', '7', '6', '5', '4', '3', '2'];
            var code = idcode + "";
            var last = idcode[17];//最后一个
            var seventeen = code.substring(0,17);
            // ISO 7064:1983.MOD 11-2
            // 判断最后一位校验码是否正确
            var arr = seventeen.split("");
            var len = arr.length;
            var num = 0;
            for(var i = 0; i < len; i++){
                num = num + arr[i] * weight_factor[i];
            }
            // 获取余数
            var resisue = num%11;
            var last_no = check_code[resisue];
            // 格式的正则
            // 正则思路
            /*
             第一位不可能是0
             第二位到第六位可以是0-9
             第七位到第十位是年份，所以七八位为19或者20
             十一位和十二位是月份，这两位是01-12之间的数值
             十三位和十四位是日期，是从01-31之间的数值
             十五，十六，十七都是数字0-9
             十八位可能是数字0-9，也可能是X
             */
            var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;
            // 判断格式是否正确
            var format = idcard_patter.test(idcode);
            // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
            return last === last_no && format ? true : false;
        }

        //下拉列表树
        function getTree() {
            //基层单位
            $('#unitName').combotree({
                url: '<%=basePath%>/BaseUnit/ybTree.do',
                onBeforeExpand: function (node) {
                    $('#unitName').combotree('tree').tree('options').url = '<%=basePath%>/BaseUnit/ybTree.do?pid=' + node.id;
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
<div title="新增/修改" id="edit_window" class="easyui-window" closed="true"
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
                            姓名： </td>
                        <td>
                            <input id="id" name="id" type="hidden">
                            <input id="proposerName" name="proposerName" class="easyui-textbox" data-options="required:true"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>单位名称： </td>
                        <td>
                            <input id="unitName" name="unitName" class="easyui-textbox" data-options="required:true"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>解禁时间： </td>
                        <td>
                            <input id="relieveDate" name="relieveDate"  class="easyui-datebox"required="required"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>身份证号： </td>
                        <td>
                            <input id="idCardNo" name="idCardNo" class="easyui-textbox" data-options="required:true,prompt:'请输入正确的身份证号码。',validType:'idcared'"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>手机号： </td>
                        <td>
                            <input id="mobilephone" name="mobilephone" class="easyui-textbox" data-options="prompt:'请输入正确的手机号码。',validType:'mobile'"
                                   style="width: 700px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            理由：
                        </td>
                        <td colspan="3">
                            <input class="easyui-textbox"name="reason" id="reason" data-options="required:true,multiline:true" style="width:700px;height:150px">

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
     style="width: 450px;height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            姓名：
                        </td>
                        <td>
                            <input name="PROPOSER_NAME" id="PROPOSER_NAME" class="easyui-textbox" style="width: 300px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位名称：
                        </td>
                        <td>
                            <input name="UNIT_NAME" id="UNIT_NAME" class="easyui-textbox" style="width: 300px;"/>
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
