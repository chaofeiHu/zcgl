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
    <title>创建申报人账户</title>
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
            displayName: 'displayName',
            actionUrl: '<%=basePath%>/Proposer/'
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
                idField: me.idFiled,
                url: me.actionUrl + 'GetList.do',
                frozenColumns: [[
                    {
                        field: 'state',
                        title: '状态',
                        width: 60,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeState(\'' + rowData.id + '\',\'' + value + '\')"  style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeState(\'' + rowData.id + '\',\'' + value + '\')" style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    {field: 'displayName', title: '姓名', width: 150, align: 'center'}
                ]],
                columns: [[
                    {field: 'declareUnitcode', title: '申报单位', width: 180, align: 'center'},
                    {field: 'basicUnitcode', title: '基层单位', width: 180, align: 'center'},
                    {field: 'mobilephone', title: '手机号', width: 150, align: 'center'},
                    {field: 'email', title: '电子邮箱', width: 200, align: 'center'},
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
                    me.edit_form.find('#id,#displayName,#declareUnitcode,#basicUnitcode,#email,#backup1').textbox().textbox('setValue', '');
                    getTree();
                    $("#mobilephone").val('');
                    $("#idCardNo").val('');
                    $("#mobilephone").validatebox({ required: true });
                    $("#idCardNo").validatebox({ required: true });
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?proposerId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
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
                            url: me.actionUrl + 'AddOrUpdate.do?proposerId=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#loginName').attr('disabled', true);
                                me.edit_window.find('div[region="south"]').css('display', 'none');
                                me.edit_window.window('open');
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

        //更改状态操作
        function changeState(id, value) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {id: id, state: value},
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

        //展示下拉列表树
        function getTree() {
            //基层单位
            $("#basicUnitcode").combotree({
                url: '<%=basePath%>/BaseUnit/getListWhere.do',
                valueField:'id',
                textField:'text',
                onLoadSuccess: function (node, data) {
                    var basic = me.edit_form.find('#basicUnitcode').textbox().textbox('getValue');
                    if (basic.length == 0){
                        $("#basicUnitcode").combotree('setValue', data[0].id);
                    }
                }
            });
        }

        // 函数参数必须是字符串，因为二代身份证号码是十八位，而在javascript中，十八位的数值会超出计算范围，造成不精确的结果，导致最后两位和计算的值不一致，从而该函数出现错误。
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

        //后台验证号码是否已存在
        function checkCode(idcode) {
            var id = $("#id").val();
            var flag;
            $.ajax({
                url:'<%=basePath %>/Proposer/checkCode.do?id='+id + '&&idCardNo='+idcode ,
                async: false,
                success: function (data) {
                    flag = data;
                }
            })
            return flag;
        }

        //自定义身份证验证
        $.extend($.fn.validatebox.defaults.rules, {
            idcared: {
                validator: function(value,param){
                    var flag= checkIDCard(value);
                    if (flag){
                        flag = checkCode(value);
                    }
                    return flag==true?true:false;
                },
                message: '不是有效的身份证号码或号码已注册!'
            }
        });

    </script>
</head>
<body class="easyui-layout">
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div title="数据维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 600px;height: 350px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="backup2" name="backup2" value="0"/>
                </div>
                <table cellpadding="5" align="center">
                    <tr>
                        <td>
                            姓  名：
                        </td>
                        <td>
                            <input id="displayName" name="displayName" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true,validType:'chinese'"/>
                        </td>
                        <td>
                            手机号：
                        </td>
                        <td>
                            <input id="mobilephone" name="mobilephone" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   required ="true"
                                   <%--validType= "remote['<%=basePath %>/Proposer/checkCode.do','mobilephone'],'mobile'"
                                   invalidMessage= "电话号码已存在"--%>
                                   invalidMessage = "电话号码已注册或格式不正确"
                                   data-options="validType:['remote[\'<%=basePath %>/Proposer/checkCode.do?id=\'+id.value,\'mobilephone\',\'电话号码已被占用!\']','mobile']"
                            />
                        </td>

                    </tr>

                    <tr>
                        <td>
                            身份证号：
                        </td>
                        <td>
                            <input id="idCardNo" name="idCardNo" class="easyui-validatebox" style="width:150px;height: 30px;"
                                   required ="true"
                                   <%--invalidMessage = "身份证号码已注册或格式不正确"--%>
                                   data-options="validType:'idcared'"
                            />
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
                            基层单位：
                        </td>
                        <td colspan="3">
                            <input id="basicUnitcode" name="basicUnitcode" class = "easyui-combotree" data-options="required:true"
                                   style="width:400px;" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="backup1" name="backup1" style="width:400px;height:80px" class="easyui-textbox"
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>
</body>
</html>
