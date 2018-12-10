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
    <title>评委会管理</title>
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
            datagrid2: null,
            datagrid3: null,
            edit_form: null,
            edit_group:null,
            edit_window: null,
            search_form: null,
            search_window: null,
            series_form: null,
            series_window: null,
            major_form: null,
            major_window: null,
            tree1: null,
            idFiled: 'id',
            jc: 'judgingCode',
            reviewSeries: 'reviewSeries',
            judgingName: 'judgingName',
            actionUrl: '<%=basePath%>/Judging/'
        };

        $(function () {
            pageInit();
            loadGrid();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.series_window = $('#series_window');
            me.series_form = me.series_window.find('#series_form');
            me.major_window = $('#major_window');
            me.major_form = me.major_window.find('#major_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.edit_group = $('#edit_group');
            me.show_group = me.edit_group.find('#show_group');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            me.datagrid2 = $('#datagrid2');
            me.datagrid3 = $('#datagrid3');
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_series_ok').linkbutton().click(function () {
                SaveData2();
            });
            $('#btn_major_ok').linkbutton().click(function () {
                SaveData3();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_series_cancel').linkbutton().click(function () {
                me.series_window.window('close');
            });
            $('#btn_major_cancel').linkbutton().click(function () {
                me.major_window.window('close');
            });
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.search_window.window('close');
            });
        }

        //加载列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getJudgingList.do',
                pagination: true,
                onClickRow: loadGrid2,
                frozenColumns: [[{
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
                },
                    {field: 'judgingCode', title: '评委会编号', width: 100, align: 'center'},
                    {field: 'judgingName', title: '评委会名称', width: 500, align: 'center'}]],
                columns: [[
                    {field: 'reviewJurisdiction', title: '评审权限', width: 100, align: 'center'},
                    {field: 'reviewScope', title: '评审范围', width: 150, align: 'center'},
                    {field: 'buildUnit', title: '承办部门', width: 150, align: 'center'},
                    {field: 'manageUnit', title: '监督部门', width: 150, align: 'center'},
                    {field: 'juryLevel', title: '评委会级别', width: 100, align: 'center'},
                    {field: 'back2', title: '主任委员', width: 100, align: 'center'},
                    {field: 'linkman', title: '联系人', width: 100, align: 'center'},
                    {field: 'phone', title: '联系电话', width: 200, align: 'center'},
                    {field: 'back3', title: '显示状态', hidden: 'true', width: 200, align: 'center'},
                ]],
                toolbar: [
                    <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            me.edit_window.find('div[region="south"]').css('display', 'block');
                            AddOrUpdate('add');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Update")>-1) { %>
                    {
                        text: '修改', iconCls: 'icon-edit', handler: function () {
                            me.edit_window.find('div[region="south"]').css('display', 'block');
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
                                    names.push(rows[i][me.judgingName]);
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
                 <!--  <% if(MenuBtns.indexOf("Group")>-1) { %>
                    {
                        text: '设置主任', iconCls: 'icon-tip', handler: function () {
                            AddOrUpdate('Group');
                        }
                    }
                    <% } %>  -->
                ],
                onBeforeLoad: function (param) {
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },
            });

        }

        //加载评审系列列表
        var loadGrid2 = function (rowIndex, rowData, value) {
            me.series_form.find('#judgingCode').textbox().textbox('setValue', rowData['judgingCode']);
            me.datagrid2.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getJudgingSerList.do?judgingCode=' + rowData['judgingCode'],
                pagination: false,
                onClickRow: loadGrid3,

            columns: [[
                    {field: 'back3', title: '评审系列', width: '100%', align: 'center'}
                ]],

                toolbar: [
                    <% if(MenuBtns.indexOf("Insert2")>-1) { %>

                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            var getDa = me.datagrid2.datagrid('getData').rows;
                          if(getDa.length==0){
                              me.series_window.find('div[region="south"]').css('display', 'block');
                              AddOrUpdate('add2');
                            }else{
                              showInfo('只能添加一条记录!');
                          }
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Delete2")>-1) { %>
                    {
                        text: '删除', iconCls: 'icon-remove', handler: function () {
                            var ids = [];
                            var jc = [];
                            var rows = me.datagrid2.datagrid('getSelections');
                            if (rows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            } else {
                                for (var i = 0; i < rows.length; i++) {
                                    ids.push(rows[i][me.reviewSeries]);
                                    jc.push(rows[i][me.jc]);
                                }
                                Delete2(ids.join(','), jc.join(','));
                            }
                        }
                    }
                    <% } %>
                ],
                onLoadSuccess: function(data) {
                  //HideButton();

                  getDeclaration();
                },
            })
           loadGrid4();
        }
        //加载评审专业列表
        var loadGrid3 = function (rowIndex, rowData, value) {
            me.major_form.find('#judgingCode1').textbox().textbox('setValue', rowData['judgingCode']);
            me.major_form.find('#reviewSeries1').textbox().textbox('setValue', rowData['reviewSeries']);
            me.datagrid3.datagrid({
                idField: me.idFiled,
                rownumbers: true,
                url: me.actionUrl + 'getJudgingMojorList.do?judgingCode=' + rowData['judgingCode'] + '&reviewSeries=' + rowData['reviewSeries'],
                pagination: false,
                columns: [[
                    {field: 'juryProfessionCode', title: '专业编号', width: '50%', align: 'center'},
                    {field: 'reviewProfessial', title: '评审专业', width: '50%', align: 'center'}
                ]],
                toolbar: [
                    <% if(MenuBtns.indexOf("Insert3")>-1) { %>

                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            me.major_window.find('div[region="south"]').css('display', 'block');
                            AddOrUpdate('add3');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Delete3")>-1) { %>
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

                                }
                                Delete3(ids.join(','), names.join(','));
                            }
                        }
                    }
                    <% } %>
                ],
            })

            $("#custab1").show();
        }

        //点击评委会数据清空评审专业数据
        function loadGrid4() {
            // $("#datagrid3").datagrid("loadData", { total: 0, rows: [] });

            $("#custab1").hide();
        }

        //添加修改初始化
        function AddOrUpdate(action) {
            getTree();
            getTree2();
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#id,#judgingCode,#judgingName,#reviewJurisdiction,#reviewScope ,#buildUnit,#manageUnit ,#juryLevel,#linkman,#phone').textbox().textbox('setValue', '');
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    $('#manageUnit').textbox('setValue',"<%=request.getAttribute("manname")%>");
                    break;
                case 'add2':
                    me.series_form.find('#id,#reviewSeries').textbox().textbox('setValue', '');
                    me.series_window.find('div[region="south"]').css('display', 'block');
                    me.series_window.window('open');
                    break;
                case 'add3':
                    me.major_form.find('#id,#reviewProfessial').textbox().textbox('setValue', '');
                    me.major_window.find('div[region="south"]').css('display', 'block');
                    me.major_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $("#id2").val(selectedRows[0][me.idFiled]);
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
               /* case 'Group':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        me.edit_group.window('open');
                        $("#JUDGING_ID").val(selectedRows[0].id);
                        group(selectedRows[0].id);
                    } else {
                        showError('请选择一条记录进行操作!');
                        return;
                    }
                    break;*/
            }
        }
      /*  function group(Code){
            var SPECIALITY_NAME=$("#SPECIALITY_NAME").val();
            var NOWUNIT=$("#NOWUNIT").val();
            var JUDGING_NAME=$("#JUDGING_NAME").val();
            $("#pwh").datagrid({
                url: me.actionUrl + 'getJudgingGroupList.do',
                queryParams:{JUDGING_NAME:JUDGING_NAME,NOWUNIT:NOWUNIT,SPECIALITY_NAME:SPECIALITY_NAME},
                frozenColumns: [[
                    {field: 'LOGIN_NAME', title: '专家登录号', width: 100, align: 'center' },
                    {field: 'SPECIALITY_NAME', title: '专家姓名', width: 100, align: 'center' },
                    {field: 'PROFESSIAL_LEVEL', title: '专业技术职务', width: 100, align: 'center'  },
                    { field: 'JURY_DUTY',  title: '评委会职务', width: 80,  align: 'center' ,formatter:function (value) {
                            if(value==1)return "主任委员"; else if(value==2) return "副主任委员";else return "委员";
                        }},
                ]],
                columns:[[
                    {field: 'NOWUNIT', title: '所在单位', width: 240, align: 'center', },
                    {field: 'BEGIN_DATE', title: '聘任时间', width: 100, align: 'center',formatter:function(value){
                            if(value==undefined) return "";
                            var unixTimestamp = new Date(value);
                            var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                            return s.substring(0,10);
                        } },
                    {field: 'END_DATE', title: '结束时间', width: 100, align: 'center',formatter:function(value){
                            if(value==undefined) return "";
                            var unixTimestamp = new Date(value);
                            var s = unixTimestamp.toLocaleString().replace(/\//g, "-");
                            return s.substring(0,10);
                        }  },
                    { field: 'JUDGING_NAME',  title: '所在评委会', width: 300,  align: 'center' },
                    {  field: 'opt', title: '管理', width: 170, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            console.log(rowData);
                            var strReturn = '<a href="javascript:void(0);" onclick="queren(\''+rowData['JUDGING_ID']+'\',\''+rowData['JUDGING_NAME']+'\',\''+rowData['ID']+'\',1)" title="设为主任" style="padding-left:20px;color:blue" >设为主任</a>';
                            if(rowData['JUDGING_ID']==Code){
                                if(rowData['JURY_DUTY']==2){
                                    strReturn+='<a href="javascript:void(0);" onclick="queren(\''+rowData['JUDGING_ID']+'\',\''+rowData['JUDGING_NAME']+'\',\''+rowData['ID']+'\',3)" title="取消副主任" style="padding-left:20px;color:blue" >取消副主任</a>';
                                }else{
                                    strReturn+='<a href="javascript:void(0);" onclick="queren(\''+rowData['JUDGING_ID']+'\',\''+rowData['JUDGING_NAME']+'\',\''+rowData['ID']+'\',2)" title="设为副主任" style="padding-left:20px;color:blue" >设为副主任</a>';
                                }
                            }else{
                                strReturn+='<a href="javascript:void(0);" onclick="queren(\''+rowData['JUDGING_ID']+'\',\''+rowData['JUDGING_NAME']+'\',\''+rowData['ID']+'\',2)" title="设为副主任" style="padding-left:20px;color:blue" >设为副主任</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
            });
        }*/
       /* function queren(code,name,ID,nums){
            var CODE=$("#JUDGING_ID").val();
            if(code!=CODE){
                if(name!=''&&name!='undefined'){
                    showInfo("请与【"+name+"】评委会协商");
                    return ;
                }
            }
            $.ajax({
                url: me.actionUrl + 'UpdateDirector.do',
                data:{JUDGING_ID:CODE,SPECIALITY_ID:ID,JURY_DUTY:nums},
                success: function (returnData) {
                    if (returnData) {
                        if (returnData.isOk == 1) {
                            showInfo(returnData.message);
                            $("#pwh").datagrid('reload');
                        } else {
                            showError(returnData.message);
                        }
                    }
                }
            });
        }*/
        function chaxun(){
            group($("#JUDGING_ID").val());
        }
        //添加修改评委会系列（保存）
        function SaveData() {
            if (me.edit_form.form('validate')) {
                $("");
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.datagrid1.datagrid('reload');
                                $("#edit_window").window("close");

                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }

        //添加评审系列（保存）
        function SaveData2() {
            if (me.series_form.form('validate')) {
                $("")
                $.ajax({
                    url: me.actionUrl + 'Save2.do',
                    data: me.series_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.datagrid2.datagrid('reload');
                                $("#series_window").window("close");


                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }
        //按钮的显示和隐藏
        function HideButton(){
            var getDa = me.datagrid2.datagrid('getData').rows;
            if(getDa.length==1){
                $('div.datagrid-toolbar a').eq(3).hide();

            }else if(getDa.length==0){
                $('div.datagrid-toolbar a').eq(3).show();

            }
        }
        //添加评审专业
        function SaveData3() {
             if(me.major_form.form('validate')){
                 $.ajax({
                     url: me.actionUrl + 'checkCode.do',
                     data: me.major_form.serialize(),
                     success: function (returnData) {
                         if (returnData) {
                             if (returnData.isOk == 0) {
                                 showError(returnData.message);
                            }
                             else {
                                 $.ajax({
                                     url: me.actionUrl + 'Save3.do',
                                     data: me.major_form.serialize(),
                                     success: function (returnData) {
                                         if (returnData) {
                                             if (returnData.isOk == 1) {
                                                 showInfo(returnData.message);
                                                 me.datagrid3.datagrid('reload');
                                                 $("#major_window").window("close");
                                             } else {
                                                 showError(returnData.message);
                                             }
                                         }
                                     }
                                 });
                             }
                         }
                     }
                 });
             }

        }

        //删除评委会
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
                                    me.datagrid2.datagrid('reload');
                                    me.datagrid3.datagrid('reload');
                                    me.tree1.tree('reload');
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            })
        }

        //删除评审系列
        function Delete2(ids, jc) {
            $.messager.confirm('提示信息', '确认要删除选择项？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete2.do',
                        data: {ids: ids, jc: jc},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);
                                    me.datagrid2.datagrid('reload');
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


        //删除评审专业
        function Delete3(ids, names) {
            $.messager.confirm('提示信息', '确认要删除选择项？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete3.do',
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

        //组建单位管理单位下拉列表树
        function getTree() {
            $("#buildUnit").combotree({
                url: '<%=basePath%>/BaseUnit/ybTree.do',
                valueField: 'id',
                textField: 'text',
                onBeforeExpand: function (node) {
                    $('#buildUnit').combotree('tree').tree('options').url = '<%=basePath%>/BaseUnit/ybTree.do?pid=' + node.id;

                }
            });
            <%--$("#manageUnit").combotree({--%>
                <%--url: '<%=basePath%>/BaseUnit/ybTree.do',--%>
                <%--valueField: 'id',--%>
                <%--textField: 'text',--%>
                <%--onLoadSuccess: function (node, data) {--%>
                    <%--$("#manageUnit").combotree('setValue', data[0].id);--%>
                <%--}--%>
            <%--});--%>
        }
        function defaultValue(cbtid, defVal, defText) {
            var combotree = $("#" + cbtid);
            var tree = combotree.combotree('tree');
            var defNode = tree.tree("find", defVal);
            if (!defNode) {
                tree.tree('append', {
                    data: [{
                        id: defVal,
                        text: defText
                    }]
                });
                defNode = tree.tree("find", defVal);
                combotree.combotree('setValue', defVal);
                tree.tree('select', defNode.target);
                defNode.target.style.display = 'none';
            } else {
                combotree.combotree('setValue', defVal);
            }
        }

        //根据评审系列获取申报系列
        function getDeclaration() {
            $('#accreditationSeries').combobox({
                onSelect: function (row) {
                    if (row != null) {
                        $('#reviewSeries').combobox({
                            url: '<%=basePath%>/BaseUnit/getDictListWhere.do?groupName=REVIEW_SERIES' + '&deptCode=' + row.dictCode,

                        });
                    }
                }
            });
        }

        function getTree2() {
            var reviewSeries = $("#reviewSeries1").val();
            $('#reviewProfessial').combobox({
                url: '<%=basePath%>/Professial/getListName.do?reviewSeries='+reviewSeries,
                valueField: 'id',
                textField:'professialName',
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

        }
        $.extend($.fn.validatebox.defaults.rules, {
            mobile: {// 验证手机号码
                validator: function (value) {
                    return /^(13|15|18)\d{9}$/i.test(value);
                },
                message: '手机号码格式不正确'
            },
            name: {// 验证姓名，中文
                validator: function (value) {
                    return /^[\Α-\￥]+$/i.test(value);
                },
                message: '请输入姓名'
            },
        })
    </script>
</head>
<body class="easyui-layout">
<div title="评委会数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
</div>
<div title="评委会维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 600px;height: 500px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="id2" name="id2"/>
                </div>
                <table cellpadding="5" align="center">
                    <tr>
                        <td>
                            评委会编号：
                        </td>
                        <td colspan="3">
                            <input id="judgingCode" name="judgingCode" class="easyui-textbox"
                                   required ="true"  invalidMessage = "该编号已存在,请重新输入!"
                                  validType= "remote['<%=basePath %>/Judging/checkCodeJuding.do?id='+id2.value,'judgingCode']"
                                   style="width: 395px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            评委会名称：
                        </td>
                        <td colspan="3">
                            <input id="judgingName" name="judgingName" class="easyui-textbox"
                                   required ="true"  invalidMessage = "该名称已存在,请重新输入!"
                                   validType= "remote['<%=basePath %>/Judging/checkCodeJudingName.do?id='+id2.value,'judgingName']"
                                   style="width: 395px" maxlength="30"/>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            评审权限：
                        </td>
                        <td>
                            <input id="reviewJurisdiction" class="easyui-combobox" name="reviewJurisdiction" value=""
                                   style="width: 150px" maxlength="30" required="required"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_JURISDICTION'"/>
                        </td>

                        <td>
                            评审范围：
                        </td>
                        <td>
                            <input id="reviewScope" class="easyui-combobox" name="reviewScope" value=""
                                   style="width: 150px" maxlength="30" required="required"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SCOPE'"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            承办部门：
                        </td>
                        <td colspan="3">
                            <input id="buildUnit" name="buildUnit" data-options="required:true"
                                   style="width:395px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            监督部门：
                        </td>
                        <td colspan="3">
                            <input id="manageUnit" name="manageUnit" readonly="readonly"
                                   style="width:395px" maxlength="30" class="easyui-textbox"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            联系人：
                        </td>
                        <td>
                            <input id="linkman" name="linkman" class="easyui-textbox"
                                   style="width: 150px" maxlength="30" validType="name"/>
                        </td>
                        <td>
                            联系人电话：
                        </td>
                        <td>
                            <input id="phone" name="phone" class="easyui-textbox"
                                   style="width: 150px" maxlength="30" validType="mobile"/>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            评委会级别：
                        </td>
                        <td>
                            <input id="juryLevel" class="easyui-combobox" name="juryLevel" value="" style="width: 150px"
                                   maxlength="30" required="required"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=JURY_LEVEL'"/>
                        </td>
                        <td>
                            状态： <input type="checkbox" id="state" name="state" value="1" checked="checked"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="backup1" name="backup1" style="width:395px;height:60px" class="easyui-textbox"
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
<div title="评审系列维护窗口" id="series_window" class="easyui-window" closed="true"
     style="width: 280px;height:200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="series_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" name="judgingCode" id="judgingCode"/>
                    <input type="hidden" name="back3" id="back3"/>
                </div>
                <table>
                    <tr>
                        <td>
                            评审系列:
                        </td>

                        <td>
                            <input id="accreditationSeries" class="easyui-combobox" name="accreditationSeries" required="true"
                                   style="width: 150px " maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ACCREDITATION_SERIES'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            申报系列:
                        </td>

                        <td>
                            <input id="reviewSeries" class="easyui-combobox" name="reviewSeries" required="true"
                                   style="width: 150px " maxlength="30"
                                   data-options="method:'get',valueField:'dictCode',textField:'dictName'"
                                  />
                        </td>

                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_series_ok" icon="icon-ok">确定</a>
            <a href="javascript:void(0)" id="btn_series_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>
<div title="评审专业维护窗口" id="major_window" class="easyui-window" closed="true"
     style="width: 380px;height:180px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="major_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" name="judgingCode" id="judgingCode1"/>
                    <input type="hidden" id="reviewSeries1" name="reviewSeries"/>
                </div>

                <table>
                    <tr>
                        <%--<td>--%>
                            <%--评审专业:--%>
                        <%--</td>--%>
                        <%--<td>--%>
                            <%--<input id="professialCode" name="reviewProfessial"  class= "easyui-textbox" required ="true"--%>
                                   <%--validType= "remote['<%=basePath %>/Judging/checkCode.do?reviewSeries1='+reviewSeries1.value+ '&judgingCode1='+judgingCode1.value,'reviewProfessial']"--%>
                                   <%--invalidMessage= "该专业已存在"--%>
                                   <%--style="width:250px" maxlength="30"/>--%>
                        <%--</td>--%>
                        <td>
                            评审专业:
                        </td>
                        <td>
                                <input id="reviewProfessial" name="reviewProfessial"  class= "easyui-combobox" required ="true"
                                       <%--validType= "remote['<%=basePath %>/Judging/checkCode.do?reviewSeries1='+reviewSeries1.value+ '&judgingCode1='+judgingCode1.value,'reviewProfessial']"--%>
                                       <%--&lt;%&ndash;invalidMessage= "该专业已存在"&ndash;%&gt;--%>
                                       style="width:250px" maxlength="30"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_major_ok" icon="icon-ok">确定</a>
            <a href="javascript:void(0)" id="btn_major_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>

<div data-options="region:'south',split:true" style="height:40%;">
    <div class="easyui-layout" data-options="fit:true">
        <div title="评审系列" region="center" border="false">
            <table id="datagrid2"></table>
        </div>
        <div data-options="region:'east',split:true" style="width:50%">
            <div class="easyui-layout" data-options="fit:true">
                <div title="评审专业" region="center" border="false">
                    <div id="custab1" style="height:200px; width:100%">
                    <table id="datagrid3"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div title="查询窗口" id="search_window" class="easyui-window" closed="true"
     style="width: 300px;height: 170px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            评委会名称：
                        </td>
                        <td>
                            <input name="judgingName" id="judgingName" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px;height:30px;">
            <a href="javascript:void(0)" id="btn_search_ok" icon="icon-ok">查找</a>
            <a href="javascript:void(0)" id="btn_search_cancel" icon="icon-cancel">关闭</a>
        </div>
    </div>
</div>
<div title="设置主任窗口" id="edit_group" class="easyui-window" closed="true"  style="width: 85%;height: 80%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="north" border="false" style="padding: 10px; background: #fff;margin-top: 10px;min-height:10px;">
            <form id="show_group" method="post">
                <table>
                    <tr>
                        <td>
                            <input type="hidden" id="JUDGING_ID">
                            专家姓名： <input name="SPECIALITY_NAME" id="SPECIALITY_NAME" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                        <td>
                            单位名称： <input name="NOWUNIT" id="NOWUNIT" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                        <td>
                            评委会： <input name="JUDGING_NAME" id="JUDGING_NAME" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                        <td>
                            <a href="javascript:void(0)"class="easyui-linkbutton" onclick="chaxun()" icon="icon-search">查询</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <div title="专家列表" region="center" border="true" style="text-align: center;margin-top: 0px;height:650px;">
            <table id="pwh">
            </table>
        </div>
    </div>
</div>
</body>
</html>
