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
    <title>年度申报开关管理</title>
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
            datagrid2:null,
            edit_form: null,
            edit_window: null,
            series_form: null,
            series_window: null,
            major_form: null,
            tree1: null,
            idFiled: 'id',
            startState:'startState',
            actionUrl: '<%=basePath%>/ParamYeardeclare/'
        };

        $(function () {
            pageInit();
            loadGrid();
            getDate();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.series_window = $('#series_window');
            me.series_form = me.series_window.find('#series_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#btn_edit_cancel').linkbutton().click(function () {
                me.edit_window.window('close');
            });
            $('#btn_search_cancel').linkbutton().click(function () {
                me.series_window.window('close');
            });
        }

        //加载年度申报开关列表
        function loadGrid() {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getParamYeardeclareList.do',
                pagination:false,
                columns: [[
                    {field: 'reviewSeries', title: '评审系列', width: 200, align: 'center'},
                    {field: 'begintime', title: '开始时间', width: 200, align: 'center'},
                    {field: 'endtime', title: '结束时间', width: 200, align: 'center'},
                    {
                        field: 'opt',
                        title: '操作',
                        width: 170,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            strReturn = '';
                            <% if(MenuBtns.indexOf("Edit")>-1) { %>
                            strReturn += '<a href="javascript:void(0)"   title="编辑" onClick="EditorRow(\'' + rowData[me.idFiled] + '\');" style="padding-left:2px;" >编辑</a>';
                            <% } %>
                            return strReturn;
                        }
                    },
                    {
                        field: 'startState',
                        title: '状态',
                        width: 100,
                        align: 'center',
                        //hidden:'true',

                        formatter: function (value, row, index) {
                            $("#startState").val(row.startState);
                            fnDate();
                            var strReturn = '';
                            if (value == '1'||value == 1) {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeReview(\'' + row.id + '\',\'' + value + '\',\'' + row.reviewSeries +'\')"   style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeReview(\'' + row.id + '\',\'' + value + '\',\'' + row.reviewSeries +'\')"  style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    }
                ]],
                toolbar: [

                    <% if(MenuBtns.indexOf("Update")>-1) { %>
                    {
                        text: '修改', iconCls: 'icon-edit', handler: function () {
                            me.edit_window.find('div[region="south"]').css('display', 'block');
                            AddOrUpdate('update');
                        }
                    }, '-',
                    <% } %>
                    <% if(MenuBtns.indexOf("Sync")>-1) { %>
                    {
                        text: '同步数据',iconCls: 'icon-reload', handler: function () {
                           Sync();
                        }
                    }, '-',
                    <% } %>
                    {
                        text: '<div><span id="timeYear"></span>年度申报<button onclick="changeState()" id="clab" ><span id="Declaration"></span></button></div>'
                    },

                ],

            });

        }
        //修改时间事件
        function EditorRow(id) {
            var selectedRows = me.datagrid1.datagrid('getSelections');
            $("#id").val(id);
            if (selectedRows.length > 0) {
                $.ajax({
                    url: me.actionUrl + 'Update.do?id=' + id,
                    success: function (data) {
                        me.edit_form.form('load', data);
                    }
                });
                me.edit_window.find('div[region="south"]').css('display', 'block');
                me.edit_window.window('open');
            }
        }
        //保存已修改的时间
        function SaveData() {
            var id=$('#id').val();
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'Save.do',
                    data: me.edit_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                me.edit_window.window('close');
                                me.datagrid1.datagrid('reload');
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }
        //为时间的input框添加easyui的datebox样式
        function getDate() {
            $('#begintime').datebox({

            });
            $('#endtime').datebox({

            });
        }
        //同步评审系列的数据操作
        function Sync() {
            $.ajax({
                url: me.actionUrl + 'Sync.do',
                success: function (returnData) {
                    if (returnData) {
                        if (returnData.isOk == 1) {
                            showInfo(returnData.message);
                            me.datagrid1.datagrid('reload');
                        } else {
                            showInfo(returnData.message);
                        }
                    }
                }
            });
        }
        //更改申报总状态
        function changeState() {
           var obj = $('#startState').val()
            $.ajax({
                url: me.actionUrl + 'UpdateAllState.do',
                data:{startState:obj},
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
        //更改申报单个状态
        function changeReview(bid, value,name) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {id: bid, state: value,seriesNmae:name},
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
        //显示当前年份和按照申报状态加载显示的字
        function fnDate(){
            var oDiv=document.getElementById("timeYear");
            var date=new Date();
            var year=date.getFullYear();//当前年份
            var time=year;
            oDiv.innerHTML=time;
            var oDiv1=document.getElementById("Declaration");
            var a= $('#startState').val()
            if(a==1){
                $("#clab").css("background","#CDC9C9")
                oDiv1.innerHTML="结束申报";
            }else{
                $("#clab").css("background","#89bdff")
                oDiv1.innerHTML="开始申报";
            }

        }

    </script>
</head>
<body class="easyui-layout">
<div region="center" border="false">

    <table id="datagrid1">

    </table>
    <input type="hidden" id="startState" name="startState">
</div>
<div id="edit_window" class="easyui-window" closed="true" title="年度开关维护窗口"
     style="width: 280px; height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                </div>
                <table>
                    <tr>
                        <td>
                           开始时间
                        </td>
                        <td>
                            <input id="begintime" name="begintime" type="text" class="easyui-textbox" required="required" style="width: 150px"
                                   />
                        </td>
                    </tr>
                    <tr>
                        <td>
                          结束时间
                        </td>
                        <td>
                            <input id="endtime" name="endtime" type="text" class="easyui-textbox" required="required" style="width: 150px"
                                   />
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
