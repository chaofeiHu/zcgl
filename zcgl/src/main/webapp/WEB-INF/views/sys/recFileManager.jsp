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
    <title>任职资格文件管理</title>
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
            displayName: 'fileTitle',
            actionUrl: '<%=basePath%>/RecFileManager/'
        };

        $(function () {
            pageInit();
            loadGrid();
            initYearNo();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            var date = new Date();
            var value = date.getFullYear();
            $("#yearNo1").combobox('setValue',value);
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_search_ok').linkbutton().click(function () {
                loadGrid();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {

                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                $("#fileTitle1").textbox().textbox('setValue','');
                $("#yearNo1").combobox('clear');
            });
        }

        //加载列表
        function loadGrid() {
            var fileTitle = $("#fileTitle1").textbox().textbox('getValue');
            var yearNo = $("#yearNo1").val();
            me.datagrid1.datagrid({
                idField: me.idFiled,
                queryParams: {
                    fileTitle: fileTitle,
                    yearNo:yearNo
                },
                url: me.actionUrl + 'getFileList.do',
                rownumbers: true,
                fit: true,
                checkOnSelect: true,
                singleSelect: false,
                selectOnCheck: true,
                pageNumber: 1,
                pagination: true,
                showHeader:true,
                showFooter:true,
                frozenColumns: [[
                    {field: 'id', checkbox:true, align: 'center',},
                    {field: 'yearNo', title: '年度', width: 200, align: 'center',},
                    {field: 'fileCode', title: '文件编号', width: 200, align: 'center',},
                    {field: 'fileTitle', title: '文件标题', width: 300, align: 'center',},
                    {field: 'addtime', title: '发文时间', width: 200, align: 'center',
                        /*formatter:function(value,row,index){
                            if (value != null && value.length != 0){
                                var date = new Date(value);
                                //console.log(row);
                                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() ;
                            }
                        }*/
                    },
                    {field: 'filePath', title: '操作', width: 300, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0)"   title="查看文件内容" onClick="viewFile(\'' + rowData['back2'] +'\');" style="padding-left:20px;" >查看文件内容</a>'
                            strReturn += '<a href="javascript:void(0)"   title="下载" onClick="downLoad(\'' + rowData['id'] +'\');" style="padding-left:20px;" >下载</a>'
                            return strReturn;
                        }
                    },
                    {field: 'back1', title: '备注', width: 200, align: 'center',},
                ]],

                toolbar: [
                   /* <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                        text: '新增', iconCls: 'icon-add', handler: function () {
                            AddOrUpdate('add');
                        }
                    }, '-',
                    <% } %>*/
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
                                    //console.log(rows[i]);
                                    ids.push(rows[i][me.idFiled]);
                                    names.push(rows[i].fileTitle);
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
                    }, '-',
                    <% } %>
                    {
                        text: '导出当前文件目录', iconCls: 'icon-excel', handler: function () {
                        var page=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageNumber;
                        var rows=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageSize;
                        var val='?yearNo='+$("#yearNo1").val()+'&fileTitle='+$("#fileTitle1").textbox().textbox('getValue')+
                            '&page='+page+'&rows='+rows;
                        window.location.href = me.actionUrl +'exportFileManager.do'+val;
                        }
                    }
                ],
                onBeforeLoad: function (param) {
                    param.id = me.edit_form.find('#id').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },

            });

        }

        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#id,#fileCode,#addtime,#filePath,#fileTitle,#back1').textbox().textbox('setValue', '');
                    $("#yearNo1").combobox('clear');
                    me.edit_form.find("#filePath").textbox('textbox').attr('readonly', false);
                    getDate();
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    $('#edit_form').form('clear');
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'addOrUpdate.do?id=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                //getDate(data.addtime);
                                me.edit_form.form('load', data);
                                me.edit_form.find("#filePath").textbox('textbox').attr('readonly', true);
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
                            url: me.actionUrl + 'addOrUpdate.do?id=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                me.edit_form.form('load', data);
                                me.edit_form.find('#specialityName').attr('disabled', true);
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
                                //清空表单
                            } else {
                                showError(returnData.message);
                            }
                        }
                        me.datagrid1.datagrid('reload');
                        $("#edit_window").window("close");
                    }
                });
            }
        }

        //删除文件
        function Delete(ids, names) {
            $.messager.confirm('提示信息', '确认要删除选择项？【' + names + '】', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'delete.do',
                        data: {ids: ids},
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

        function initYearNo(){

            $("#yearNo1").combobox({
                url:me.actionUrl + 'getYear.do',
                valueField:'yearCode',
                textField:'yearText',
                onLoadSuccess:function (data) {
                    console.log(data[0].yearCode);
                    $("#yearNo1").combobox("setValue", data[0].yearCode);
                }
            });
            $("#yearNo").combobox({
                url:me.actionUrl + 'getYear.do',
                valueField:'yearCode',
                textField:'yearText',
            });
        }

        //下载文件
        function downLoad(data) {
            window.location.href = me.actionUrl +'downLoadFile.do?id='+data;
            //window.open('<%=basePath%>/'+data,'_blank');
            /*$.ajax({
                url:me.actionUrl + 'downLoadFile.do?id='+data,
                beforeSend:ajaxLoading,
                success: function (returnData) {
                    ajaxLoadEnd();
                    if (returnData) {
                        if (returnData.isOk == 1) {
                            //console.log(returnData);
                           /!* $("#path").val(returnData.path);
                            filePath = returnData.path*!/
                            //viewPdf(returnData.path);
                            showInfo("下载成功!");
                        } else {
                            showError("下载失败!");
                        }
                    }
                    me.datagrid1.datagrid('reload');
                }
            });*/
        }

        //查看文件内容 后台生成的PDF
        function viewFile(data) {
            console.log(data);
            if (data == undefined || data == "undefined"){
                showError("查看失败!");
            }else {
                window.open('<%=basePath%>/'+data,'_blank');
            }
            me.datagrid1.datagrid('reload');
        }

        //采用jquery easyui loading css效果
        function ajaxLoading(){
            $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
            $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
        }

        function ajaxLoadEnd(){
            $(".datagrid-mask").remove();
            $(".datagrid-mask-msg").remove();
        }

        //时间插件
        function getDate() {
            $('#addtime').datetimebox("setValue",$('#addtime').datetimebox('options').currentText);
        }

    </script>
</head>
<body class="easyui-layout">

<div title="查找任职资格文件" data-options="region:'north'" style="height:80px">
    <table cellspacing="8px">
        <tr>

            <td>年度：</td>
            <td><input name="yearNo" id="yearNo1" class="easyui-combobox" data-options="editable:false" style="width:200px"/></td>
            <td>标题包含：</td>
            <td><input name="fileTitle" id="fileTitle1" class="easyui-textbox" style="width:200px"/></td>
            <td><a href="javascript:void(0)" id="btn_search_ok" icon="icon-search">查询</a></td>
            <td><a href="javascript:void(0)" id="btn_search_cancel" icon="icon-remove">清空条件</a></td>
        </tr>
    </table>

</div>
<div title="任职资格文件列表" region="center" border="false">
    <table id="datagrid1">
    </table>

</div>
<div title="任职资格文件维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 35%;height: 35%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="path" name="path"/>
                </div>
                <table cellpadding="4" align="center">
                    <tr>
                        <td style="text-align:left" >
                            文件编号：
                        </td>
                        <td>
                            <input id="fileCode" name="fileCode" class="easyui-textbox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                        <td>
                            年度：
                        </td>
                        <td>
                            <input id="yearNo" name="yearNo" class="easyui-numberbox" data-options="required:true,editable:false"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            文件标题：
                        </td>
                        <td>
                            <input id="fileTitle" name="fileTitle" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            发文时间：
                        </td>
                        <td>
                            <input id="addtime" name="addtime" class="easyui-datetimebox" style="width:150px"
                                   data-options=""/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            文件路径：
                        </td>
                        <td>
                            <input id="filePath" name="filePath" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            备注：
                        </td>
                        <td>
                            <input id="back1" name="back1" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
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
<%--<div title="查询窗口" id="search_window" class="easyui-window" closed="true"
     style="width: 350px;height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            文件标题包含：
                        </td>
                        <td>
                            <input name="fileTitle" id="fileTitle1" class="easyui-textbox" style="width: 150px;"/>
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
</div>--%>
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>

</body>
</html>
