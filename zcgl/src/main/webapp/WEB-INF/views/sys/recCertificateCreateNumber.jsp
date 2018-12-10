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
    <title>生成证书编号管理</title>
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
            displayName: 'userName',
            actionUrl: '<%=basePath%>/RecCertificate/'
        };

        $(function () {
            pageInit();
            loadGrid();
            loadTree();
            //getDate();
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
                loadGrid();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {

                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                $("#judgingName").combobox('clear');
                $("#yearNo").textbox().textbox('setValue', '');
                $("#userName").textbox().textbox('setValue', '');
                $("#hasCertificate").textbox().textbox('setValue', '');
                $("#reviewSeries").combobox('clear');
                $("#professialCode").combobox('clear');
                $('#professialCode').combobox({ disabled: true });
            });
        }

        //加载列表
        function loadGrid() {
            //var judgingCode = $("#judgingName").textbox().textbox('getValue');
            var judgingCode = $("#judgingName").val();
            var yearNo = $("#yearNo").textbox().textbox('getValue');
            var userName = $("#userName").textbox().textbox('getValue');
            var hasCertificate = $("#hasCertificate").val();
            var reviewSeries = $("#reviewSeries").val();
            var professialCode = $("#professialCode").val();
            me.datagrid1.datagrid({
                idField: me.idFiled,
                queryParams: {
                    judgingCode: judgingCode,
                    yearNo: yearNo,
                    userName: userName,
                    professialCode: professialCode,
                    reviewSeries: reviewSeries,
                    hasCertificate: hasCertificate,
                },
                checkOnSelect: true,
                singleSelect: false,
                selectOnCheck: true,
                url: me.actionUrl + 'getCreateList.do',
                frozenColumns: [[
                    {field: 'id', checkbox: true, align: 'center',},
                    {field: 'yearNo', title: '年度', width: 100, align: 'center',},
                    {
                        field: 'areaCode', title: '地市', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if (value == "河南省") return "省直";
                            else if (rowData.back2 == 2 || value == "2") return rowData.areaCode;
                            else if (rowData.back2 == 3 || value == "3") return rowData.back3;
                            else return null;
                        }
                    },
                    {field: 'unitCode', title: '主管单位名称', width: 120, align: 'center',},
                    {field: 'userName', title: '姓名', width: 100, align: 'center',},
                    {
                        field: 'certificateNumber', title: '证书编号', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if (rowData.recCertificate == null) {
                                return null;
                            } else {
                                return rowData['recCertificate']['certificateNumber'];
                            }
                        }
                    },
                    {field: 'unitCode', title: '发证单位', width: 120, align: 'center',},
                ]],
                columns: [[
                    {
                        field: 'sex', title: '性别', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            if (value == 1 || value == "1") return "男";
                            else if (value == 2 || value == "2") return "女";
                            else return "未说明性别";
                        }
                    },
                    {field: 'idCardNo', title: '身份证号', width: 150, align: 'center',},
                    {field: 'judgingCode', title: '评委会名称', width: 200, align: 'center',},
                    {field: 'reviewSeries', title: '申报系列', width: 100, align: 'center',},
                    {field: 'titleLevel', title: '申报级别', width: 100, align: 'center',},
                    {field: 'positionalTitles', title: '申报职称', width: 100, align: 'center',},
                    {field: 'professialCode', title: '申报专业', width: 100, align: 'center',},
                    {field: 'reviewType', title: '评审类型', width: 100, align: 'center',},
                    {field: 'fileCode', title: '文件编号', width: 100, align: 'center',},
                    {field: 'gettime', title: '取得资格时间', width: 100, align: 'center',},
                    {field: 'getway', title: '取得资格方式', width: 100, align: 'center',},
                    {field: 'back1', title: '备注', width: 100, align: 'center',},
                ]],
                toolbar: [
                    {
                        <% if(MenuBtns.indexOf("CreateCertificateNumber")>-1) { %>
                        text: '生成证书编号', iconCls: 'icon-save', handler: function () {
                            var selectedRows = me.datagrid1.datagrid('getSelections');
                            var ids = [];
                            if (selectedRows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            } else {
                                for (var i = 0; i < selectedRows.length; i++) {
                                    if (selectedRows[i]['recCertificate'] == null || selectedRows[i]['recCertificate']['certificateNumber'] == '') {
                                        console.log(selectedRows[i]);
                                        ids.push(selectedRows[i][me.idFiled]);
                                    }
                                }
                                if (ids.length == 0) {
                                    showError('请选择至少一条无证书编码的纪录进行操作!');
                                } else {
                                    confirmResult(ids.join(','));
                                }
                            }
                        }
                        <% } %>
                    },
                ],
                onBeforeLoad: function (param) {
                    param.judgingId = me.edit_form.find('#judgingId').val();
                    param.id = me.edit_form.find('#id').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },

            });

        }

        //确认审核
        function confirmResult(ids) {
            $.messager.confirm('提示信息', '是否确认生成？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'confirmResult.do',
                        data: {ids: ids},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo("生成成功");

                                } else {
                                    showError("生成失败");
                                }
                            }
                            me.datagrid1.datagrid('reload');
                        }
                    });
                }
            })
        }


        //加载评委会下拉列表
        function loadTree() {
            $('#judgingName').combobox({
                url: '<%=basePath%>/Speciality/getJudgingList.do',
                valueField: 'id',
                textField: 'judgingName',
                onSelect: function (data) {
                    $.ajax({
                        url: '<%=basePath%>/Judging/getReviewSeries.do',
                        data: {judgingCode: data.judgingCode},
                        success: function (returnData) {
                            $('#reviewSeries').combobox('setValue', returnData.reviewSeries);
                            console.log(returnData);
                            var url = '<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SERIES';
                            $('#reviewSeries').combobox('reload', url);
                        }
                    })
                }
            });
        }


    </script>
</head>
<body class="easyui-layout">
<div data-options="region:'north'" style="height:73px">
    <table>
        <tr>
            <td>评委会名称：</td>
            <td><input name="judgingName" id="judgingName" class="easyui-combobox" style="width:200px"/></td>
            <td>姓名：</td>
            <td><input name="userName" id="userName" class="easyui-textbox" style="width:200px"/></td>
            <td>年度：</td>
            <td><input name="yearNo" id="yearNo" class="easyui-textbox" style="width:200px"/></td>
        </tr>
        <tr>
            <td>申报系列：</td>
            <td>
                <input id="reviewSeries" name="reviewSeries" class="easyui-combobox"
                       data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SERIES',
                                   onSelect: function(data){
                                        $('#professialCode').combobox('clear');
                                       console.log(data);
                                       var url = '<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+data.dictCode;
                                       $('#professialCode').combobox('reload', url);
                                       $('#professialCode').combobox({ disabled: false });
                                    }" style="width:200px;"
                />
            </td>
            <td>申报专业：</td>
            <td>
                <input id="professialCode" name="professialCode" class="easyui-combobox" disabled="true"
                       data-options="valueField:'professialCode',textField:'professialName',
                                                url:'<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+reviewSeries.value,

                                    "
                       style="width:200px"
                />
            </td>
            <td>是否已生成证书编号：</td>
            <td>
                <input id="hasCertificate" name="hasCertificate" class="easyui-combobox" data-options="
                valueField:'id',textField:'text',
		data: [{
			id: '1',
			text: '是'
		},{
			id: '0',
			text: '否'
		}]"/>

            </td>
            <td><a href="javascript:void(0)" id="btn_search_ok" icon="icon-search">查询</a></td>
            <td><a href="javascript:void(0)" id="btn_search_cancel" icon="icon-remove">清空条件</a></td>
        </tr>
    </table>

</div>
<div title="数据列表" region="center" border="false">
    <table id="datagrid1">
    </table>
    <form id="importForm" enctype="multipart/form-data" method="post">
        <input type="file" name="file" id="file" multiple="false" style="display:none;"
               accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
    </form>

</div>


</body>
</html>
