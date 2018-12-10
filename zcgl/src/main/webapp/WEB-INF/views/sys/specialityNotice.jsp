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
    <title>设置学科组</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <style>
        .easyui-validatebox { appearance: none;-webkit-appearance: none;outline: none;  margin: 0 5px 0 0; -webkit-border-radius: 50%; -moz-border-radius: 50%; -o-border-radius: 50%;border-radius: 50%;border: 1px solid #ccc; padding:4px;top: 3px;position: relative;}
        .easyui-validatebox:after { display: block;content: "";width: 8px;height: 8px;background: #fff;}
        .easyui-validatebox:checked:after { background: #00af6b;-webkit-border-radius: 50%; -moz-border-radius: 50%; -o-border-radius: 50%;border-radius: 50%;}
    </style>
    <script type="text/javascript">

        var me = {
            datagrid1: null,
            edit_form: null,
            edit_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'id',
            displayName: 'groupName',
            actionUrl: '<%=basePath%>/SpecialityNotice/'
        };

        $(function () {
            pageInit();
            //loadGrid();
            loadTree();
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
            $('#btn_search_ok1').linkbutton().click(function () {
                loadTree();
            });
        }

        //加载列表
        function loadGrid(shu) {
            me.datagrid1.datagrid({
                idField: me.idFiled,
                url: me.actionUrl + 'getList.do?JUDGING_CODE='+shu,
                frozenColumns: [[
                    {field: 'GROUP_NO', title: '组号', width: 50, align: 'center'},
                    {field: 'GROUP_NAME', title: '组名', width: 140, align: 'center'},
                    {field: 'YEAR_NO', title: '年度', width: 50, align: 'center'},
                    {field: 'gjsl', title: '申报正高人数', width: 100, align: 'center',},
                    {field: 'fgsl', title: '申报副高人数', width: 100, align: 'center', },
                    {field: 'zgsl', title: '申报中级人数', width: 100, align: 'center'},
                    {field: 'NU', title: '职称办抽调专家数', width: 120, align: 'center'},
                    {field: 'NUS', title: '确定出席专家数', width: 120, align: 'center'},
                    {
                        field: 'opt', title: '管理', width: 170, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0);" onclick="queren('+rowData['ID']+',0)" title="专家确认管理" style="padding-left:20px;color:blue" >专家确认管理</a>'
                            return strReturn;
                        }
                    }
                ]],

                toolbar: [
                   /* <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                        text: '设置主任', iconCls: 'icon-add', handler: function () {
                            AddOrUpdate('add');
                        }
                    }, '-',
                    <% } %>*/
                    <% if(MenuBtns.indexOf("Insert")>-1) { %>
                    {
                       text: '导出专家信息', iconCls: 'icon-excel', handler: function () {
                            window.location.href = me.actionUrl +'exportExcelProposer.do?JUDGING_CODE='+$("#JUDGING_CODE").val();
                        }
                    }, '-',
                    <% } %>

                ],
                onBeforeLoad: function (param) {
                    param.judgingCode = me.edit_form.find('#judgingCode').val();
                    param.id = me.edit_form.find('#id').val();
                    me.search_form.find('input').each(function (index) {
                        param[this.name] = $(this).val();
                    });
                },onLoadSuccess:function(rowData){
                    var gj=0; var fg=0;  var zj=0;
                    var dict_name=rowData.rows[0].DICT_NAME;
                    for(var i=0;i<rowData.rows.length;i++){
                        if(rowData.rows[i].zgsl!=undefined){
                            zj+=1;
                        }
                        if(rowData.rows[i].fgsl!=undefined){
                            fg+=1;
                        }
                        if(rowData.rows[i].gjsl!=undefined){
                            gj+=1;
                        }
                    }
                    if(zj==0&&dict_name!="中级"){
                        me.datagrid1.datagrid('hideColumn', 'zgsl'); }
                    if(fg==0) me.datagrid1.datagrid('hideColumn', 'fgsl');
                    if(gj==0&&dict_name!="高级") {
                       if(fg!=0||gj==0){
                           me.datagrid1.datagrid('hideColumn', 'gjsl');
                       }
                    }else if(gj==0&&fg!=0){
                        me.datagrid1.datagrid('hideColumn', 'gjsl');
                    }
                }
            });
        }

        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                         if($("#JUDGING_CODE").val()=='null'||$("#JUDGING_CODE").val()==''){
                             showError('请选择评委会!');
                              return ;
                         }
                        me.edit_window.window('open');
                        pwh();
                        chongyong();
                        break;
            }
        }
        function YesOn(nums,index) {
            if(nums==1){
                $("#reason"+index).css("display","block");
            }else{
                $("#reason"+index).val("");
                $("#reason"+index).css("display","none");
            }
        }
        function chaxun2(){
            var xm=$('#xm').val();
            var dw=$('#dw').val();
            var sf=$('#sf').combobox("getValue");
            var code=$('#GROUP_ID').val();
            var url= me.actionUrl+'findSpecialityNotice.do?JUDGING_CODE='+$("#JUDGING_CODE").val()+'&GROUP_ID='+code;
            if(undefined!=xm){
                url+='&SPECIALITY_NAME='+xm;
            }
            if(undefined!=dw){
                url+='&NOWUNIT='+dw;
            }
            if(undefined!=sf){
                url+='&sf='+sf;
            }
            queren(code,url);
            $('#sf').combobox({
                onLoadSuccess:function () {
                    $('#sf').combobox('select',sf);
                }
            });
        }

        function queren(code,url2){
            var actiUrl=me.actionUrl+'findSpecialityNotice.do?JUDGING_CODE='+$("#JUDGING_CODE").val()+'&GROUP_ID='+code;
            if(url2!="0"){
                actiUrl=url2;
            }
            $("#GROUP_ID").val(code);
            $("#edit_notice").window('open');

            $('#notice').datagrid({
                url:actiUrl,
                frozenColumns: [[
                    {field: 'NOWUNIT', title: '单位名称', width: 100, align: 'center'},
                    {field: 'LOGIN_NAME', title: '专家登录号', width:100, align: 'center'},
                    {field: 'PROFESSIAL', title: '从事专业', width:100, align: 'center'},
                    {field: 'SPECIALITY_NAME', title: '姓名', width: 140, align: 'center'},
                ]],
                columns:[[
                    {field: 'DIRECTOR', title: '专家角色', width:100, align: 'center', formatter:function(value,row,index){
                        if(value=="0"){
                            return "主任专家";
                        }else{
                            return "普通专家";
                        }
                        }},
                    {field: 'ID_CARD_NO', title: '身份证', width:100, align: 'center'},
                    {field: 'MOBILEPHONE', title: '手机', width:100, align: 'center'},
                    {field: 'TEL', title: '办公电话', width:100, align: 'center'},
                    {field: 'TYPE', title: '通知结果', width: 140, align: 'center',color:'red',
                        formatter:function(value,row,index){
                            if(value=="1"){
                                return "<label style='margin:0 10px 0 0;text-align: center'><input type='radio' class='easyui-validatebox'name='type"+index+"' id='type"+index+"'onclick='YesOn(0,"+index+")' value='0' /> 出席</label>" +
                                    "<label style='margin:0 10px 0 0;text-align: center'><input type='radio' class='easyui-validatebox'name='type"+index+"' id='type"+index+"' onclick='YesOn(1,"+index+")' value='1' checked/>不出席</label>";
                            }else{
                                return "<label style='margin:0 10px 0 0;text-align: center' ><input type='radio' class='easyui-validatebox'name='type"+index+"' id='type"+index+"' onclick='YesOn(0,"+index+")' value='0' checked/> 出席</label>" +
                                    "<label style='margin:0 10px 0 0;text-align: center' ><input type='radio' class='easyui-validatebox'name='type"+index+"' id='type"+index+"' onclick='YesOn(1,"+index+")' value='1' />不出席</label>";
                            }
                        }
                    },
                    {field: 'REASON', title: '不出席理由', width:160, align: 'center',formatter:function(value,row,index){
                        if(value==undefined) value="";
                        if(row.TYPE==1){
                            return "<input  style='width: 150px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' value='"+value+"'  name='reason"+index+"' id='reason"+index+"'>";
                        }else{
                            return "<input  style='display:none;width: 150px;padding:6px;border:1px solid #87b9e8; background:#fff; -webkit-border-radius:4px; -moz-border-radius:4px; -o-border-radius:4px;border-radius:4px;' value='"+value+"'  name='reason"+index+"' id='reason"+index+"'>";
                        }
                    }},
                    { field: 'opt', title: '操作', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0)" onclick="initNoticePassword(\''+rowData['PERSONAL_NUMBER']+'\',\''+rowData['ID_CARD_NO']+'\')" title="初始化密码"  style="padding-left:20px;color:blue" >初始化密码</a>'
                            return strReturn;
                    } } ]],

            });

        }
        function initNoticePassword(userId,idCard){
            $.ajax({
                url: '<%=basePath%>/SpecialityNotice/initNoticePassword?PERSONAL_NUMBER='+userId+'&idCard='+idCard,
                dataType: 'json',
                success: function (jsonstr) {
                   if(jsonstr.ok=="1"){
                       showInfo(jsonstr.message);
                   }else{
                       showError(jsonstr.message);
                   }
                }
            })
        }

        //加载列表
        function pwh(SPECIALITY_NAME,NOWUNIT,xkz) {
            var url=me.actionUrl + 'findSpeciality.do?JUDGING_CODE='+$("#JUDGING_CODE").val();
           if(undefined!=SPECIALITY_NAME){
                url+='&SPECIALITY_NAME='+SPECIALITY_NAME;
           }
           if(undefined!=NOWUNIT){
               url+='&NOWUNIT='+NOWUNIT;
           }
            if(undefined!=xkz){
                url+='&xkz='+xkz;
           }
            $('#pwh').datagrid({
                url: url,
                frozenColumns: [[
                    {field: 'LOGIN_NAME', title: '专家登录号', width:100, align: 'center'},
                    {field: 'SPECIALITY_NAME', title: '姓名', width: 140, align: 'center'},
                    {field: 'GROUP_NAME', title: '学科组', width: 100, align: 'center'},
                    {field: 'NOWUNIT', title: '单位名称', width: 100, align: 'center'},
                    {field: 'DIRECTOR', title: '专家角色', width: 100, align: 'center',formatter:function(value,rowData,rowIndex){
                            if(value=="0"){
                                return "主任专家";
                            }else{
                                return "普通专家";
                            }
                        }},
                    {field: 'PROFESSIAL_LEVEL', title: '专业等级', width: 120, align: 'center'},
                    {field: 'DICT_NAME', title: '职务等级', width: 120, align: 'center'},
                    {
                        field: 'opt', title: '管理', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0)" title="设为主任" onClick="updateNotice(\'' + rowData['GROUP_ID'] + '\',\''+rowData['PERSONAL_NUMBER']+'\');" style="padding-left:20px;color:blue" >设为主任</a>'
                            return strReturn;
                        }
                    }
                ]],
            });
        }


        function updateNotice(GROUP_ID,PERSONAL_NUMBER){
            $.ajax({
                url: '<%=basePath%>/SpecialityNotice/AddOrUpdate?PERSONAL_NUMBER='+PERSONAL_NUMBER+'&GROUP_ID='+GROUP_ID+'&DIRECTOR=0',
                dataType: 'json',
                success: function (jsonstr) {
                    showInfo("设置成功!")
                }
            })
            var SPECIALITY_NAME=$('#SPECIALITY_NAME').val();
            var NOWUNIT=$('#NOWUNIT').val();
            var xkz=$('#xkz').combobox("getValue");
            pwh(SPECIALITY_NAME,NOWUNIT,xkz);
            chongyong();
        }

        function chaxun(){
            var SPECIALITY_NAME=$('#SPECIALITY_NAME').val();
            var NOWUNIT=$('#NOWUNIT').val();
            var xkz=$('#xkz').combobox("getValue");
            pwh(SPECIALITY_NAME,NOWUNIT,xkz);
            chongyong();
            $('#xkz').combobox({
                onLoadSuccess:function () {
                    $('#xkz').combobox('select',xkz);
                }
            });
        }


        function chongyong() {
            $.ajax({
                url: '<%=basePath%>/BaseUnit/selectSubject?JUDGING_CODE='+$("#JUDGING_CODE").val(),
                dataType: 'json',
                success: function (jsonstr) {
                    jsonstr.push({'ID': '', 'GROUP_NAME': '请选择..'});
                    $("#xkz").combobox({  //为下拉框赋值
                        data: jsonstr,
                        valueField: "ID",
                        textField: "GROUP_NAME"
                    });
                }
            })
        }
        //加载评委会树
        function loadTree() {
            var judgingName = $("#judgingName").val();
            me.tree1.tree({
                url: '<%=basePath%>/Speciality/getJudgingTree.do?judgingName='+judgingName,
                onClick: function (node) {
                    me.edit_form.find('#judgingCode').textbox().textbox('setValue',node.attributes);
                    var judgingCode = node.attributes;
                    loadGrid(judgingCode);
                    $("#JUDGING_CODE").val(judgingCode);
                },
                onLoadSuccess: function (node, param) {

                },
                onLoadError: function (data) {
                    showError('数据加载错误！');
                }
            });
        }
function subTable(){
            $.parser.parse($('edit_notice').parent());
            var rows= $('#notice').datagrid("getRows");
            var item=new Array();
            for(var i=0;i<rows.length;i++){  //循环获取出席信息,与未出席理由
                var person=new Object();
                var type= $("input[name='type"+i+"']:checked").val();//$("input[name='type"+i+"'][checked]").val();
                var reason=$("#reason"+i).val();
                if(type=="1"){
                    if(reason==""){
                        showError("请填写不出席理由!");
                        return;
                    }
                }
                person.TYPE=type;
                person.REASON=reason;
                person.GROUP_ID=rows[i].GROUP_ID;
                person.PERSONAL_NUMBER=rows[i].PERSONAL_NUMBER;
                item.push(person)
            }
            $.messager.confirm('提示信息', '确认要保存本页信息吗？', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'UpdateSpecialityNotice.do',
                        data: {rows: JSON.stringify(item)},
                        success: function (returnData) {
                            showInfo(returnData);
                            $('#datagrid1').datagrid('reload');
                            $("#edit_notice").window('close');
                        },error:function(returnData){
                            showInfo(returnData);
                            $('#datagrid1').datagrid('reload');
                            $("#edit_notice").window('close');
                        }
                    });
                }
            })
}
    </script>
</head>
<body class="easyui-layout">
<div region="west" hide="true" split="true" title="评委会列表" style="width: 300px;">
    <label>评委会名称包含：</label>
    <input name="judgingName" id="judgingName" class="easyui-textbox" style="width:200px"/>
    <label><a href="javascript:void(0)" id="btn_search_ok1" icon="icon-search">搜索</a></label>
    <ul id="tree1">
    </ul>
</div>
<div title="学科组列表" region="center" border="false">
    <input type="hidden" id="JUDGING_CODE">
    <table id="datagrid1">
    </table>
</div>
<%--<div title="设置主任窗口" id="edit_window" class="easyui-window" closed="true"  style="width: 85%;height: 80%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="north" border="false" style="padding: 10px; background: #fff;margin-top: 10px;min-height:10px;">
            <form id="show_form" method="post">
                <table>
                    <tr>
                        <td>
                            姓名： <input name="SPECIALITY_NAME" id="SPECIALITY_NAME" class="easyui-textbox" style="width: 150px;"/>
                        </td>
                        <td>
                            单位： <input name="NOWUNIT" id="NOWUNIT" class="easyui-textbox" style="width: 150px;"/>

                        </td>
                        <td>
                            学科组：<input id="xkz" name="xkz"  style="width:200px" maxlength="30"/>
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
</div>--%>

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
                            <input name="groupName" id="groupName1" class="easyui-textbox" style="width: 150px;"/>
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
<div title="专家确认管理" id="edit_notice" class="easyui-window" closed="true"  style="width: 85%;height: 90%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="north" border="false" style="padding: 10px; background: #fff;min-height:10px;">
            <form id="show_notice" method="post">
                <table>
                    <tr>
                        <td>
                            姓名： <input name="xm" id="xm" class="easyui-textbox" style="width: 150px;"/>
                            <input name="GROUP_ID" id="GROUP_ID" type="hidden">
                        </td>
                        <td>
                            单位： <input name="dw" id="dw" class="easyui-textbox" style="width: 150px;"/>

                        </td>
                        <td>
                            是否出席：<select id="sf" class="easyui-combobox"style="width: 150px;">
                            <option value="">请选择</option>
                            <option value="0">出席</option>
                            <option value="1">不出席</option>
                        </select>
                        </td>

                        <td>
                            <a href="javascript:void(0)"class="easyui-linkbutton" onclick="chaxun2()" icon="icon-search">查询</a>
                            <a href="javascript:void(0)" class="easyui-linkbutton" icon="icon-save" onclick="subTable()" icon="icon-search">提交确认信息</a>

                        </td>
                    </tr>
                </table>
            </form>
        </div>

        <div title="专家列表" region="center" border="true" style="text-align: center;margin-top: 0px;height:650px;">
            <table id="notice">
            </table>
        </div>
    </div>
</div>
</body>
</html>
