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
    <title>职称考试管理</title>
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
            idFiled: 'id',
            actionUrl: '<%=basePath%>/RecExamResult/'
        };

        $(function () {
            pageInit();
            loadTree();
            initYearNo();
            loadGrid();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_edit_cancel').linkbutton().click(function () {

                me.edit_window.window('close');
            });
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#birthday').datebox('setValue', "");
            $('#idCardNo').bind('blur',function(){
                var idCardNo = $('#idCardNo').val();
                       if (idCardNo !== undefined && idCardNo.length > 0) {
                           var birthday = idCardNo.substring(6, 10) + "-" + idCardNo.substring(10, 12) + "-" + idCardNo.substring(12, 14);
                           console.log(birthday);
                           $('#birthday').datebox('setValue', birthday);
                }
            });
        }
        function initYearNo(){
            $("#yearNo").combobox({
                url: '<%=basePath%>/RecFileManager/getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onLoadSuccess:function (data) {
                    $("#yearNo").combobox("setValue", data[0].yearCode);
                }
            });
            $("#yearNo1").combobox({
                url: '<%=basePath%>/RecFileManager/getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onHidePanel: function() {
                    var valueField = $(this).combobox("options").valueField;
                    var val = $(this).combobox("getValue");  //当前combobox的值
                    var allData = $(this).combobox("getData");   //获取combobox所有数据
                    var result = true;      //为true说明输入的值在下拉框数据中不存在
                    for (var i = 0; i < allData.length; i++) {
                        if (val == allData[i][valueField]) {
                            result = false;
                        }
                    }
                    if (result) {
                        $(this).combobox("clear");
                    }
                }
            });
            $('#examClass').combobox({
                onSelect: function (row) {
                    if (row != null) {
                        $('#examName').combobox({
                            url: '<%=basePath%>/BaseUnit/getDictListWhere.do?groupName=EXAM_NAME' + '&deptCode=' + row.dictCode,

                        });
                    }
                }
            });

        }
        function SaveData(){
        var examName =  $('#parentId').val();
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'recProSave.do?examName='+examName,
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);

                            } else {
                                showError(returnData.message);
                            }
                        }
                        me.edit_window.window('close');
                        me.datagrid1.datagrid('reload');
                    }
                });
            }
        }

        //加载树
        function loadTree() {
            me.tree1.tree({
                url: me.actionUrl + 'recProDictTree.do',
                onClick: function (node) {
                     $('#parentId').val(node.id);
                    // $("#yearNo").combobox('clear');
                    // $('#userName').textbox('clear');
                    me.datagrid1.datagrid('reload');
                },
                onLoadSuccess:function(){$(this).tree('collapseAll');$(this).tree('expand',$(this).tree('getRoot').target);}

            });
        }
        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getRecExamList.do',
                pagination: true,
                frozenColumns: [[
                    {field: 'yearNo', title: '年度', width: 120, align: 'center',
                    },
                    {field: 'areaCode', title: '地市', width: 150, align: 'center',
                    },
                    {field: 'ticketNumber', title: '准考证号', width: 120, align: 'center',
                    },
                    {field: 'userName', title: '姓名', width: 150, align: 'center',
                    },
                    {field: 'sex', title: '性别', width: 60, align: 'center',
                    },
                ]],
                columns: [[
                    {field: 'titleLevel', title: '级别', width: 150, align: 'center',
                    },
                    {field: 'professialCode', title: '专业', width: 120, align: 'center',
                    },
                    {field: 'idCardNo', title: '证件号', width: 150, align: 'center',
                    },
                    {field: 'gettime', title: '资格取得时间', width: 120, align: 'center',
                    },
                    {field: 'unitName', title: '单位名称', width: 150, align: 'center',
                    },
                    {field: 'fileNumber', title: '档案号', width: 120, align: 'center',
                    },
                    {field: 'birthday', title: '出生日期', width: 150, align: 'center',
                    },
                    {field: 'examName', title: '考试名称', width: 120, align: 'center',
                    },
                    {field: 'certificateNumber', title: '证书编号', width: 150, align: 'center',
                    },
                    {field: 'managerNo', title: '管理号', width: 120, align: 'center',
                    },
                    {field: 'positionalTitles', title: '职称', width: 150, align: 'center',
                    },
                    {field: 'back1', title: '备注', width: 150, align: 'center',
                    },
                    // {
                    //     field: 'opt',
                    //     title: '操作',
                    //     width: 170,
                    //     align: 'center',
                    //     formatter:nameFormatter,
                    // },
                ]],
                toolbar: [
                    <% if(MenuBtns.indexOf("Insert")>-1) { %>

                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            var examName =  $('#parentId').val();
                            $.ajax({
                                url: me.actionUrl + 'check.do',
                                data: {examName: examName},
                                success: function (returnData) {
                                    if (returnData) {
                                        if (returnData.isOk == 1) {
                                            AddOrUpdate('add');

                                        } else {
                                            showInfo(returnData.message);

                                        }
                                    }

                                }
                            });
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Edit")>-1) { %>
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
                                    names.push(rows[i].userName);
                                }
                                delRow(ids.join(','), names.join(','));
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
                ],
                onBeforeLoad: function (param) {
                    param.examName =  $('#parentId').val();
                    param.userName= $('#userName').val();
                    param.yearNo = $('#yearNo').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },
            });
        }
        function delRow(id,userName){
            $.messager.confirm('提示信息', '确认要删除姓名为【' + userName + '】的选择项？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'delete.do',
                        data: {id: id},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);

                                } else {
                                    showError(returnData.message);
                                }
                            }
                            me.datagrid1.datagrid('reload');
                        }
                    });
                }
            })
        }
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#id,#yearNo1,#areaCode,#ticketNumber,#userName ,#sex,#titleLevel ,#professialCode,#gettime,#unitName,#fileNumber,#certificateNumber,#managerNo,#positionalTitles,#back1').textbox().textbox('setValue', '');
                    $("#idCardNo").val('');
                    $('#birthday').combo('setText','')
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    // $("#idCardNo").val('');
                    // $('#birthdate').val('');
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'Update.do?id=' + selectedRows[0][me.idFiled],
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
        function checkCode(idcode) {
            var id = $('#id').val();
            var flag;
            $.ajax({
                url:'<%=basePath %>/RecExamResult/checkCode.do?idCardNo='+idcode+ '&&id='+id,
                async: false,
                success: function (data) {
                    flag = data;
                }
            })
            return flag;
        }
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
<input id="parentId" name="parentId" type="hidden">
<div region="west" hide="true" split="true" title="导航树" style="width: 285px">
        <ul id="tree1">
        </ul>
</div>

<div  region="center" border="false">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north'" style="height:40px;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>年度：</td>
                        <td><input name="yearNo" id="yearNo" class="easyui-combobox" style="width:150px"/></td>
                        <td>姓名：</td>
                        <td><input name="userName" id="userName" class="easyui-textbox" style="width:200px"/></td>
                        <td><a href="javascript:void(0)" id="btn_search_ok" icon="icon-search">查询</a></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center',title:'数据列表'">
            <table id="datagrid1" >
            </table>
        </div>
    </div>

</div>
<div title="数据维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width:600px;height: 90%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                </div>
                <table cellpadding="4" align="center">

                    <tr>
                        <td style="text-align:left" >
                            年度：
                        </td>
                        <td>
                            <input id="yearNo1" name="yearNo" class="easyui-combobox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                        <td style="text-align:left" >
                            地市：
                        </td>
                        <td>
                            <input id="areaCode" name="areaCode" class="easyui-textbox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>

                    </tr>
                    <tr>

                        <td>
                            准考证号：
                        </td>
                        <td>
                            <input id="ticketNumber" name="ticketNumber" class="easyui-textbox" style="width:150px"
                                   data-options="required:true"/>
                        </td>
                        <td>
                            姓名：
                        </td>
                        <td>
                            <input id="userName" name="userName" class="easyui-textbox" style="width:150px"
                                   data-options="required:true"/>
                        </td>

                    </tr>
                    <%--<tr>--%>
                        <%--<td>考试分类:</td>--%>
                        <%--<td>--%>
                            <%--<input id="examClass" class="easyui-combobox" name="examClass" required ="true"--%>
                                   <%--style="width: 150px " maxlength="30"--%>
                                   <%--data-options="valueField:'dictCode',textField:'dictName',--%>
                                   <%--url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=EXAM_CLASS'"/>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--考试名称:--%>
                        <%--</td>--%>

                        <%--<td>--%>
                            <%--<input id="examName" class="easyui-combobox" name="examName" required ="true"--%>
                                   <%--style="width: 150px " maxlength="30"--%>
                                   <%--data-options="method:'get',valueField:'dictCode',textField:'dictName'"--%>
                            <%--/>--%>
                        <%--</td>--%>

                    <%--</tr>--%>
                    <tr>
                        <td>
                            性别：
                        </td>
                        <td>
                            <input id="sex" class="easyui-combobox" name="sex" value="性别" style="width: 150px" maxlength="30" required ="true"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=SEX'" />
                        </td>
                        <td>
                            级别：
                        </td>
                        <td>
                            <input id="titleLevel" name="titleLevel" class="easyui-textbox" style="width:150px"
                                   data-options="required:true"/>
                        </td>

                    </tr>


                    <tr>
                        <td>
                            专业：
                        </td>
                        <td>
                            <input id="professialCode" name="professialCode" class="easyui-textbox" style="width:150px"
                                   data-options="required:true"/>
                        </td>
                        <td>
                            身份证号：
                        </td>
                        <td>
                            <input id="idCardNo" name="idCardNo" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   required ="true"
                            <%--invalidMessage = "身份证号码已注册或格式不正确"--%>
                                   data-options="validType:'idcared'"
                            <%--data-options="validType:['remote[\'<%=basePath %>/Speciality/checkCode.do?id=\'+id.value,\'idCardNo\']','idcard']"--%>
                            />
                        </td>

                    </tr>
                    <tr>

                            <td>
                                资格取得时间：
                            </td>
                            <td>
                                <input id="gettime" name="gettime" class="easyui-datebox" style="width:150px"
                                       data-options="required:true"/>
                            </td>
                            <%--<td>--%>
                                <%--出生日期：--%>
                            <%--</td>--%>
                            <%--<td>--%>
                                <%--<input id="birthday" name="birthday"  class="easyui-datebox" style="width:150px"--%>
                                       <%--data-options="required:true"/>--%>
                            <%--</td>--%>
                        <td>
                            出生日期：
                        </td>
                        <td>
                            <input id="birthday" name="birthday"  type="text" class="easyui-datebox" style="width:150px"
                                   readonly="readonly"   data-options=""/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位名称：
                        </td>
                        <td colspan="3">
                            <input id="unitName" name="unitName" class="easyui-textbox" style="width:390px;"
                                   data-options="required:true"/>
                        </td>
                    </tr>

                    <tr>

                        <td>
                            档案号：
                        </td>
                        <td>
                            <input id="fileNumber" name="fileNumber" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true" />
                        </td>

                        <td>
                            证书编号：
                        </td>
                        <td>

                            <input id="certificateNumber" class="easyui-textbox" name="certificateNumber" value="" style="width: 150px" maxlength="30"
                                   data-options="required:true" />
                        </td>
                    </tr>

                    <tr>
                        <td>
                            管理号：
                        </td>
                        <td>
                         <input id="managerNo" name="managerNo" class="easyui-textbox" style="width:150px"
                        data-options="required:true"/>
                        </td>
                        <td>
                            职称：
                        </td>
                        <td>
                        <input id="positionalTitles" name="positionalTitles" class="easyui-textbox" style="width:150px;"
                        data-options="required:true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="back1" name="back1" style="width:395px;height:60px" class="easyui-textbox"
                                   multiline="true" />
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