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
    <title>专家库</title>
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
            displayName: 'specialityName',
            actionUrl: '<%=basePath%>/Speciality/'
        };

        $(function () {
            pageInit();
            loadTree();
            setEducation();

        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');

            //$('#birthdate').val("");
            $('#idCardNo').bind('blur',function(){
                var idCardNo = $('#idCardNo').val();
                if(idCardNo!==undefined && idCardNo.length>0){
                    var birthdate = idCardNo.substring(6,10)+"-"+idCardNo.substring(10,12)+"-"+idCardNo.substring(12,14);
                    //console.log(birthdate);
                    $('#birthdate').val(birthdate);
                }
            });

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
                $("#specialityName1").textbox().textbox('setValue','');
                $('#professialDutyLevel1.easyui-combobox').combobox({ disabled:false });
                $("#administrativeDutyLevel1").combobox('clear');
                $("#professialLevel1").combobox('clear');
                $("#professialDutyLevel1").combobox('clear');
            });
            loadGrid();
        }

        //加载列表
        function loadGrid() {
            //var judgingCode = $("#judgingName").textbox().textbox('getValue');
            var judgingCode = $("#judgingName").val();
            var specialityName = $("#specialityName1").textbox().textbox('getValue');
            var administrativeDutyLevel = $("#administrativeDutyLevel1").val();
            var professialLevel = $("#professialLevel1").val();
            var professialDutyLevel = $("#professialDutyLevel1").val();
            me.datagrid1.datagrid({
                idField: me.idFiled,
                queryParams: {
                    judgingCode: judgingCode,
                    specialityName: specialityName,
                    administrativeDutyLevel: administrativeDutyLevel,
                    professialLevel: professialLevel,
                    professialDutyLevel: professialDutyLevel,
                },
                url: me.actionUrl + 'getList.do',
                loadMsg:"正在加载,请稍候",
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
                    {field: 'specialityName', title: '专家姓名', width: 120, align: 'center',
                       },
                    {field: 'administrativeDutyLevel', title: '行政职务级别', width: 120, align: 'center',
                      },
                    {field: 'professialLevel', title: '专业技术职务', width: 150, align: 'center',
                        },
                    {field: 'professialDutyLevel', title: '专业技术职务等级', width: 140, align: 'center',
                        },
                    {field: 'juryDuty', title: '评委会职务', width: 100, align: 'center',
                        formatter:function(value,row,index){
                        //console.log(row);
                            return row.baseEngage.juryDuty;
                        }
                    },
                    {field: 'beginDate', title: '聘任开始时间', width: 120, align: 'center',
                        formatter:function(value,row,index){
                        if (row.baseEngage.beginDate != null && row.baseEngage.beginDate.length != 0){
                                var date = new Date(row.baseEngage.beginDate);
                                //console.log(date);
                                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                            }
                        }
                    },
                    {field: 'endDate', title: '聘任结束时间', width: 120, align: 'center',
                        formatter:function(value,row,index){
                            if (row.baseEngage.endDate != null && row.baseEngage.endDate.length != 0){
                                var date = new Date(row.baseEngage.endDate);
                                //console.log(row);
                                return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                            }
                        }
                    },
                ]],
                columns: [[

                    {field: 'idCardNo', title: '身份证号', width: 200, align: 'center',
                       },
                    {
                        field: 'sex',
                        title: '性别',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            //console.log(row);
                            if (value == "1") return '男';
                            else if (value == "2") return '女';
                            else return '未说明性别';
                        }
                    },
                    {field: 'mobilephone', title: '手机号', width: 120, align: 'center',
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
                                    //console.log(rows[i]);
                                    ids.push(rows[i][me.idFiled]);
                                    names.push(rows[i].specialityName);
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
                    /*{
                        text: 'excel模板下载', iconCls: 'icon-excel', handler: function () {
                        var val = '?judgingId=';
                        window.location.href = me.actionUrl + 'exportExcelSpeciality.do' + val;
                        }
                    }, '-',*/
                    /*{
                        text: 'excel导入', iconCls: 'icon-excel', handler: function () {
                        $("#file").trigger("click");//触发点击事件
                    }
                    }, '-',*/
                    {
                        text: '导出专家库', iconCls: 'icon-excel', handler: function () {
                        var page=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageNumber;
                        var rows=me.datagrid1.datagrid("getPager" ).data("pagination" ).options.pageSize;
                        var val='?judgingId='+$("#judgingId").textbox().textbox("getValue")+'&specialityName1='+$('#specialityName1').val()+
                            '&page='+page+'&rows='+rows;
                        window.location.href = me.actionUrl +'exportExcelSpeciality.do'+val;
                        }
                    }
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

      /*  //上传EXCEL
        function excelUpload() {
            $.messager.confirm("导入提示","确定要导入数据吗？", function(r){
                if(r){
                    var formData = new FormData($("#importForm")[0]);
                    var judgingId = $("#judgingId").textbox().textbox("getValue");
                    $.ajax({
                        url:me.actionUrl +'importExcelSpeciality.do?judgingId='+judgingId,
                        type:'post',
                        data:formData,
                        dataType:'json',
                        processData: false,
                        contentType: false,
                        success:function(data){
                            console.log(data);
                            if(data.isOk == 1){
                                $.messager.alert('导入','导入成功');
                                $("#file").val("");
                                me.datagrid1.datagrid('reload');
                            }else{
                                $("#file").val("");
                                $.messager.alert('导入',data.message);
                            }
                        },
                        error:function(){
                            $.messager.alert('导入',"导入失败!请检查表中数据是否合法!");
                        }
                    })
                }
            })
        }*/

        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    var jid = $("#judgingId").textbox().textbox("getValue");
                    me.edit_form.find('#state').attr('checked', true);
                    me.edit_form.find('#id,#specialityId,#administrativeDutyLevel,#professialDutyLevel,#professialLevel,#professial,#jobYear,#performance,#education' +
                        ',#degree,#nowunit,#areacode,#specialityName,#sex,#graduateSchool,#graduateDate' +
                        ',#tel,#postalAddress,#postalCode,#email,#presentation,#juryDuty,#beginDate,#endDate,#recommendSeries,#recommendMajor').textbox().textbox('setValue', '');
                    me.edit_form.find('#specialityName').attr('disabled', false);
                    $("#mobilephone").val('');
                    $("#idCardNo").val('');
                    $('#birthdate').val('');
                    //获取当前用户 unitCode 及 行政区划代码
                    $.ajax({
                        url:'<%=basePath%>/BaseUnit/AddOrUpdate.do',
                        success: function (data) {
                            //me.edit_form.form('load', data.baseUnit);
                            area2(data.areaCode1);
                            area3(data.areaCode2);
                        }
                    });
                    $("#mobilephone").validatebox({ required: true });
                    $("#idCardNo").validatebox({ required: true });
                    $("#specialityName").validatebox({ required: true });
                    $('#professialDutyLevel.easyui-combobox').combobox({ disabled:false });
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    //$('#edit_form').form('clear');
                    me.edit_form.find('#id,#specialityId,#administrativeDutyLevel,#professialDutyLevel,#professialLevel,#professial,#jobYear,#performance,#education' +
                        ',#degree,#nowunit,#areacode,#specialityName,#sex,#graduateSchool,#graduateDate' +
                        ',#tel,#postalAddress,#postalCode,#email,#presentation,#juryDuty,#beginDate,#endDate,#recommendSeries,#recommendMajor').textbox().textbox('setValue', '');
                        $("#mobilephone").val('');
                        $("#idCardNo").val('');
                        $('#birthdate').val('');
                        setDegree('');
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'addOrUpdate.do?id=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                area2(data.areaCode1);
                                me.edit_form.form('load', data.speciality);
                                $("#degree").combobox('setValue',data.speciality.degree);
                                area3(data.areaCode2);
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
                    me.edit_form.find('#id,#specialityId,#administrativeDutyLevel,#professialDutyLevel,#professialLevel,#professial,#jobYear,#performance,#education' +
                        ',#degree,#nowunit,#areacode,#specialityName,#sex,#graduateSchool,#graduateDate' +
                        ',#tel,#postalAddress,#postalCode,#email,#presentation,#juryDuty,#beginDate,#endDate,#recommendSeries,#recommendMajor').textbox().textbox('setValue', '');
                    $("#mobilephone").val('');
                    $("#idCardNo").val('');
                    $('#birthdate').val('');
                    setDegree('');
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'addOrUpdate.do?id=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                area2(data.areaCode1);
                                me.edit_form.form('load', data.speciality);
                                me.edit_form.find('#specialityName').attr('disabled', true);
                               // $("#degree").combobox('setValue',data.speciality.degree);
                                area3(data.areaCode2);
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

        //地区下拉框 省
        /*function area1(value) {
            $("#areaGrade1").combobox({
                valueField:'areaCode',
                textField:'areaName',
                url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=1',

                onSelect: function(data){
                    $('#areaGrade2').combobox('setValue', '==请选择==');
                    var url = '<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2&areaCode='+data.areaCode;
                    $('#areaGrade2').combobox('reload', url);
                },
                onLoadSuccess: function () { //加载完成后,设置选中
                    $(this).combobox('select', value);
                }
            });

        }*/

        //地区下拉框市
        function area2(value) {
            $("#areaGrade2").combobox({
                valueField:'areaCode',
                textField:'areaName',
                url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2&areaCode='+value,
                formatter: function(row){
                    if(row.areaCode =='410000')
                        row.areaName = '省直'
                    var opts = $(this).combobox('options');
                    //console.log(row.areaName);
                    //console.log(row[opts.textField]);

                    return row[opts.textField];
                },
                onSelect: function(data){
                    var url = '<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=3&areaCode='+data.areaCode;
                    $('#areaGrade3').combobox('setValue', '==请选择==');
                    $('#areaGrade3').combobox('reload', url);
                },
                onLoadSuccess: function () { //加载完成后,设置选中
                    $(this).combobox('select', value);
                }
            });

        }

        //地区下拉框 区县
        function area3(value) {
            $("#areaGrade3").combobox({
                valueField:'areaCode',
                textField:'areaName',
                formatter: function(row){
                    if(row.areaCode =='410000')
                        row.areaName = '省直';
                    if(row.areaGrade =='2')
                        row.areaName = '市直';
                    var opts = $(this).combobox('options');
                    //console.log(row.areaName);
                    //console.log(row.areaGrade);
                    return row[opts.textField];
                },
                onLoadSuccess: function () { //加载完成后,设置选中
                    $(this).combobox('select', value);
                }
            })
        }

        //添加修改（保存）
        function SaveData() {
            var areaCode = $('#areaGrade3').combobox('getValue');
            //var professial = $('#professial').combobox('getValue');
            //var professialLevel = $('#professialLevel').combobox('getValue');
            //var professialLevel = $('#professialLevel').val();
            var professialDutyLevel = $('#professialDutyLevel').combobox('getValue');
            //console.log(professialDutyLevel);
            if (areaCode == "==请选择=="){
                $.messager.alert('提示','请选择行政区划代码!');
                return;
            }
           /* if (professial == ""){
                $.messager.alert('提示','请选择从事专业!');
                return;
            }*/
            if (me.edit_form.form('validate')) {
                $.ajax({
                    url: me.actionUrl + 'save.do?professialDutyLevel='+professialDutyLevel,
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
                        me.tree1.tree('reload');
                        $("#edit_window").window("close");
                    }
                });
            }
        }

        //删除用户
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

        //更改状态操作
        function changeState(id, value) {
            $.ajax({
                url: me.actionUrl + 'updateState.do',
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
        

        //学历下拉框
        function setEducation() {
            $("#education").combobox({
                valueField:'dictCode',
                textField:'dictName',
                url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=EDU_EDU',
                onSelect: function(data){
                    //console.log(data);
                    setDegree(data.dictCode);
                },
               /* onLoadSuccess: function () { //加载完成后,设置选中
                    $(this).combobox('select', value);
                }*/
            });

        }

        //学位下拉框
        function setDegree(value) {
            $("#degree").combobox({
                valueField:'dictCode',
                textField:'dictName',
                url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=DEGREE',
                loadFilter: function(row){
                    if(value !==''){
                        var data1 = [];
                        for(var i = 0;i < row.length;i++)
                        {
                            if(value ==1 || value > 4) {
                                 //console.log(row[i]);
                                if(row[i].dictName.indexOf("无") != -1){
                                    data1.push(row[i]);
                                }
                            }
                            else if(value == 4){
                                if(row[i].dictName.indexOf("学士") != -1) {
                                    data1.push(row[i]);
                                }
                            }
                            else if(value == 3){
                                if(row[i].dictName.indexOf("硕士") != -1) {
                                    data1.push(row[i]);
                                }
                            }
                            else if(value == 2){
                                //console.log(row[i]);
                                if(row[i].dictName.indexOf("博士") != -1) {
                                    data1.push(row[i]);
                                }
                            }
                        }
                        return data1;
                    }else{
                        return row;
                    }
                }

            });

        }

        //加载评委会树
        function loadTree() {

            $('#judgingName').combobox({//此处加载所有评委会
                url: me.actionUrl + 'getJudgingList.do?pwh=1',
                valueField:'id',
                textField:'judgingName'
            });
        }

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
                url:'<%=basePath %>/Speciality/checkCode.do?id='+id + '&&idCardNo='+idcode ,
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
                    //console.log(flag);
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
<div data-options="region:'north'" style="height:73px" >
    <table>
        <tr>

            <td>评委会名称：</td>
            <td><input name="judgingName" id="judgingName" class="easyui-combobox" style="width:200px"/></td>
            <td>专家姓名：</td>
            <td><input name="specialityName" id="specialityName1" class="easyui-textbox" style="width:200px"/></td>

            <%--<td>系列：</td>
            <td><select name="xl" id="xl" style="width:208px"></select></td>--%>
            <td>行政职务级别：</td>
            <td><input name="administrativeDutyLevel" id="administrativeDutyLevel1" class="easyui-combobox"
                       data-options="valueField:'dictCode',textField:'dictName',
                       url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ADMINISTRATIVE_DUTY_LEVEL'"
                       style="width:200px"/></td>

        </tr>
        <tr>

            <td>专业技术职务：</td>
            <td><input name="professialLevel" id="professialLevel1" class="easyui-combobox"
                       data-options="valueField:'dictCode',textField:'dictName',
                       url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=POSITIONAL_TITLES',
                       onSelect: function(data){
                           $('#professialDutyLevel1.easyui-combobox').combobox({ disabled:true });
                           $('#professialDutyLevel1').combobox('setValue',data.backup2);
                        }" style="width:200px"/></td>
            <td>专业技术职务等级：</td>
            <td><input name="professialDutyLevel" id="professialDutyLevel1" class="easyui-combobox"
                       data-options="valueField:'dictCode',textField:'dictName',
                       url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=TITLE_LEVEL'"
                       style="width: 200px;"/>
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
<div title="数据维护窗口" id="edit_window" class="easyui-window" closed="true"
     style="width: 780px;height: 90%; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="id" name="id"/>
                    <input type="hidden" id="judgingId" name="judgingId"/>
                    <%--<input type="hidden" id="specialityId" name="specialityId"/>--%>
                </div>
                <table cellpadding="4" align="center">
                    <tr>
                        <td style="text-align:left" >
                            专家姓名：
                        </td>
                        <td>
                            <input id="specialityName" name="specialityName" class="easyui-textbox" data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                        <td>
                            性别：
                        </td>
                        <td>
                            <input id="sex" class="easyui-combobox" name="sex" value="性别" style="width: 150px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=SEX'" />
                        </td>
                    </tr>



                    <%--<tr>
                        <td>
                            聘任开始时间：
                        </td>
                        <td>
                            <input id="beginDate" name="beginDate" type="text" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true"/>
                        </td>

                        <td>
                            聘任结束时间：
                        </td>
                        <td>
                            <input id="endDate" name="endDate" type="text" class="easyui-textbox" style="width:150px"
                                   data-options="required:true"/>
                        </td>
                    </tr>--%>

                    <tr>
                        <td>
                            行政职务级别：
                        </td>
                        <td>
                            <input id="administrativeDutyLevel" class="easyui-combobox" name="administrativeDutyLevel" value="行政职务级别" style="width: 150px" maxlength="30"
                                   data-options="required:true,valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ADMINISTRATIVE_DUTY_LEVEL'" />
                        </td>

                        <td>
                            个人业绩表现：
                        </td>
                        <td>
                            <input id="performance" name="performance" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>


                    </tr>
                    <tr>
                        <td>
                            专业技术职务：
                        </td>
                        <td>
                            <input id="professialLevel" name="professialLevel" class="easyui-combobox"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=POSITIONAL_TITLES',
                                   onSelect: function(data){
                                       $('#professialDutyLevel.easyui-combobox').combobox({ disabled:true });
                                       $('#professialDutyLevel').combobox('setValue',data.backup2);
                                    }" style="width:150px;" required = "true"
                            />
                        </td>
                        <td>
                            专业技术职务等级：
                        </td>
                        <td>
                            <input id="professialDutyLevel" name="professialDutyLevel" class="easyui-combobox"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                                url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=TITLE_LEVEL'"
                                   style="width:150px"
                                   />
                        </td>

                    </tr>
                    <tr>
                        <td>
                            推荐系列：
                        </td>
                        <td>
                            <input id="recommendSeries" name="recommendSeries" class="easyui-combobox"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=REVIEW_SERIES',
                                   onSelect: function(data){
                                       var url = '<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+data.dictCode;
                                       $('#recommendMajor').combobox('reload', url);
                                    }" style="width:150px;" required = "true"
                            />
                        </td>
                        <td>
                            推荐专业：
                        </td>
                        <td>
                            <input id="recommendMajor" name="recommendMajor" class="easyui-combobox"
                                   data-options="valueField:'professialCode',textField:'professialName',
                                                url:'<%=basePath%>/Professial/getProfessialListWhere.do?reviewSeries='+recommendSeries.value,
                                    "
                                   style="width:150px" required="true"
                            />
                        </td>

                    </tr>
                    <tr>
                        <td>
                            从事专业：
                        </td>
                        <td>
                            <input id="professial" name="professial" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            从事专业年限：
                        </td>
                        <td>
                            <input id="jobYear" name="jobYear" class="easyui-textbox" style="width:150px;"
                                   data-options=""/>
                        </td>


                    </tr>
                    <tr>
                        <td>
                            学历：
                        </td>
                        <td>
                            <input id="education" name="education" class="easyui-combobox" style="width:150px;"
                                   data-options="" />
                        </td>

                        <td>
                            学位：
                        </td>
                        <td>

                            <input id="degree" class="easyui-combobox" name="degree" value="" style="width: 150px" maxlength="30"
                                   data-options="" />
                        </td>
                    </tr>

                    <tr>

                        <td>
                            所在单位：
                        </td>
                        <td colspan="3">
                            <input id="nowunit" name="nowunit" class="easyui-textbox" style="width:470px;"
                                   data-options=""/>
                        </td>
                    </tr>
                    <tr>
                        <td >
                            行政区划代码：
                        </td>

                        <%--<td style="width:150px;word-break:break-all">
                            <input id="areaGrade1" class="easyui-textbox" value="==请选择==" style="width: 150px" maxlength="30"
                                   data-options="
                                   required:true
                                    " />

                        </td>--%>
                        <td style="word-break:break-all" colspan="2">
                            <input id="areaGrade2" class="easyui-combobox"  value="" style="width: 150px" maxlength="30"
                                   data-options="required:true" />
                        </td>
                        <td colspan="2">
                            <input id="areaGrade3" class="easyui-combobox" name="areacode" value="==请选择==" style="width: 150px" maxlength="30"
                                   data-options="required:true" />
                        </td>
                    </tr>
                    <tr>
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
                            出生日期：
                        </td>
                        <td>
                            <input id="birthdate" name="birthdate" readonly="true" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   data-options=""/>
                        </td>
                        <td>
                            毕业学校：
                        </td>
                        <td>
                            <input id="graduateSchool" name="graduateSchool" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            毕业时间：
                        </td>
                        <td>
                            <input id="graduateDate" name="graduateDate" class="easyui-datebox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            手机号码：
                        </td>
                        <td>
                            <input id="mobilephone" name="mobilephone" class="easyui-validatebox textbox" style="width:150px;height: 30px;"
                                   required ="true"
                                   invalidMessage = "电话号码已注册或格式不正确"
                                   data-options="validType:['remote[\'<%=basePath %>/Speciality/checkCode.do?id=\'+id.value,\'mobilephone\',\'电话号码已被占用!\']','mobile']"
                            />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            办公电话：
                        </td>
                        <td>
                            <input id="tel" name="tel" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            邮政编码：
                        </td>
                        <td>
                            <input id="postalCode" name="postalCode" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            通信地址：
                        </td>
                        <td colspan="3">
                            <input id="postalAddress" name="postalAddress" class="easyui-textbox" style="width:470px"
                                   data-options=""/>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            是否随机抽取：
                        </td>
                        <td colspan="">
                            <input id="isRandom" name="isRandom" type="checkbox" value="0" checked ="checked"
                                   data-options=""/>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            个人简介：
                        </td>
                        <td colspan="3">
                            <input id="presentation" name="presentation" style="width:470px;height:80px" class="easyui-textbox"
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
<%--<div title="查询窗口" id="search_window" class="easyui-window" closed="true"
     style="width: 350px;height: 200px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="search_form" method="post">
                <table>
                    <tr>
                        <td>
                            专家姓名：
                        </td>
                        <td>
                            <input name="specialityName" id="specialityName1" class="easyui-textbox" style="width: 150px;"/>
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
