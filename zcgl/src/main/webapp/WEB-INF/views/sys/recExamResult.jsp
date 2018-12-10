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
    <title>导入职称考试信息</title>
    <link type="text/css" href="<%=basePath%>/static/css/default.css" rel="stylesheet"/>
    <link id="easyuiTheme" type="text/css" href="<%=basePath%>/static/js/themes/<%=themeName%>/easyui.css"
          rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>/static/js/themes/icon.css" rel="stylesheet"/>
    <script src="<%=basePath%>/static/js/jquery.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery-migrate-1.4.1.min.js"></script>
    <script src="<%=basePath%>/static/js/jquery.easyui.min.js"></script>
    <script src="<%=basePath%>/static/js/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=basePath%>/static/js/common.js"></script>
    <script src="<%=basePath%>/static/js/jquery-form.js"></script>
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
            actionUrl: '<%=basePath%>/RecExamResult/'
        };
        var regIdCardNo = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        var regBirthday = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
        var regSex = /^['男'|'女']$/ ;

        $(function () {
            pageInit();
            initYearNo();
            $("#custab").hide();

        });

        function pageInit() {
            me.datagrid1 = $('#datagrid1');
            $('#btn_save_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#down').linkbutton().click(function () {
                Down();
            });
            $('#btn').linkbutton().click(function () {

            });
            $('#remove').linkbutton().click(function () {
                    clearData();
            });
        }

        function clearData(){
              $("#file").val("");
              $("#datagrid1").datagrid("loadData", { total: 0, rows: [] });
              $("#custab").hide();
        }
        function Down(){
           window.location.href=me.actionUrl+'downExcel.do'
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
            $('#examClass').combobox({
                onSelect: function (row) {
                    if (row != null) {
                        $('#examName').combobox({
                            url: '<%=basePath%>/BaseUnit/getDictListWhere.do?groupName=EXAM_NAME' + '&deptCode=' + row.dictCode,
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
                },
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
        $(document).ready(function(){
            $('#btn').click(function(){
                var yearNo = $('#yearNo').val();
                var gettime = $('#gettime').val();
                var examClass=$('#examClass').val();
                var examName1 =$('#examName').combobox('getText');
                if(checkData()){
                    $("#custab").show();
                    var fileDir = $("#file").val();
                    //var suffix1 = fileDir.substr(fileDir.lastIndexOf(".")-8,fileDir.lastIndexOf(".")+1);
                    // if(suffix1!="职称考试信息模板.xls"){
                    var reg = RegExp(/职称考试信息模板/);
                    if(!reg.exec(fileDir)){
                        $("#custab").hide();
                        alert("请下载右侧职称考试信息模板并填写信息")
                        return false;
                    }
                    $('#form1').ajaxSubmit({
                        type: 'post',
                        url:me.actionUrl+ 'LeadInUser.do',
                        dataType: 'text',
                        data:{yearNo:yearNo,gettime:gettime,examClass:examClass,examName1:examName1},
                        success:function(returnData){
                            var a =$.parseJSON(returnData)
                            if(a) {
                                if (a.isOk == 0) {
                                    showError(a.message);
                                    $("#custab").hide();
                                } else {
                                    loadDatagrid(returnData);
                                }
                            }
                        },

                    });

                }
            });
        })
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
            var flag;
            $.ajax({
                url:'<%=basePath %>/RecExamResult/checkCode.do?idCardNo='+idcode,
                async: false,
                success: function (data) {
                    flag = data;
                }
            })
            return flag;
        }
        function SaveData() {
            if (checkCell()) {
                var yearNo = $('#yearNo').val();
                var gettime = $('#gettime').val();
                var examClass = $('#examClass').val();
                var examName = $('#examName').val();
                var list1 = $('#datagrid1').datagrid("getData");
                var list2 = list1.rows;
                $.ajax({
                    url: me.actionUrl + 'save.do',
                    data: {
                        listdata: JSON.stringify(list2),
                        yearNo: yearNo,
                        gettime: gettime,
                        examClass: examClass,
                        examName: examName
                    },
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                $("#datagrid1").datagrid("loadData", { total: 0, rows: [] });
                                $("#custab").hide();
                                showInfo(returnData.message);
                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }
        function checkCell() {
            var yearNo1 = $('#yearNo').val();
            var gettime1 = $('#gettime').val();
            var examClass=$('#examClass').val();
            var examName1 =$('#examName').combobox('getText');
            var list1=$('#datagrid1').datagrid("getData");
            var list2 = list1.rows;
            if(list2.length==0){
                alert("请点击导入数据并数据不能为空")
                return false;
            }
            for(var i =0;i<list2.length;i++){
                var yearNo=list2[i].yearNo;
                if(yearNo==""){
                    alert("第"+(i+1)+"行年份不能为空")
                    return false;
                }
                if(yearNo!=yearNo1){
                    alert("第"+(i+1)+"行年份数据不匹配")
                    return false;
                }
                var areaCode=list2[i].areaCode;
                if(areaCode==""){
                    alert("第"+(i+1)+"行地市不能为空")
                    return false;
                }
                var ticketNumber=list2[i].ticketNumber;
                if(ticketNumber==""){
                    alert("第"+(i+1)+"行准考证号不能为空")
                    return false;
                }
                var userName=list2[i].userName;
                if(userName==""){
                    alert("第"+(i+1)+"行姓名不能为空")
                    return false;
                }
                var sex=list2[i].sex;
                if(sex==""){
                    alert("第"+(i+1)+"行性别不能为空")
                    return false;
                }
                if(!regSex.test(sex)){
                    alert("第"+(i+1)+"行性别错误")
                    return false;
                }
                var titleLevel=list2[i].titleLevel;
                if(titleLevel==""){
                    alert("第"+(i+1)+"行级别不能为空")
                    return false;
                }
                var professialCode=list2[i].professialCode;
                if(professialCode==""){
                    alert("第"+(i+1)+"行专业不能为空")
                    return false;
                }
                var idCardNo=list2[i].idCardNo;
                // !regIdCardNo.test(idCardNo)
                if(!checkIDCard(idCardNo)){
                    alert("第"+(i+1)+"行身份证编号格式有误或不能为空")
                    return false;
                }
                if(!checkCode(idCardNo)){
                    alert("第"+(i+1)+"行身份证编号已存在")
                    return false;
                }
                var gettime=list2[i].gettime;
                if(gettime==""){
                    alert("第"+(i+1)+"行资格取得时间不能为空")
                    return false;
                }
                if(gettime!=gettime1){
                    alert("第"+(i+1)+"行资格取得时间数据不匹配")
                    return false;
                }
                var unitName=list2[i].unitName;
                if(unitName==""){
                    alert("第"+(i+1)+"行单位名称不能为空")
                    return false;
                }
                var fileNumber=list2[i].fileNumber;
                if(fileNumber==""){
                    alert("第"+(i+1)+"行档案号不能为空")
                    return false;
                }
                var birthday=list2[i].birthday;
                if(!regBirthday.test(birthday)){
                    alert("第"+(i+1)+"行生日格式有误或不能为空")
                    return false;
                }
                var examName=list2[i].examName;
                if(examName==""){
                    alert("第"+(i+1)+"行考试名称不能为空")
                    return false;
                }
                if(examName!=examName1){
                    alert("第"+(i+1)+"行考试名称数据不匹配")
                    return false;
                }
                var certificateNumber=list2[i].certificateNumber;
                if(certificateNumber==""){
                    alert("第"+(i+1)+"行证书编号不能为空")
                    return false;
                }
                var managerNo=list2[i].managerNo;
                if(managerNo==""){
                    alert("第"+(i+1)+"行管理号不能为空")
                    return false;
                }
                var positionalTitles=list2[i].positionalTitles;
                if(positionalTitles==""){
                    alert("第"+(i+1)+"行职称不能为空")
                    return false;
                }
            }
            return true;
        }
      //数据验证
        function checkData(){
            var yearNo = $('#yearNo').val();
            var gettime = $('#gettime').val();
            var examClass=$('#examClass').val();
            var examName = $('#examName').val();
            var fileDir = $("#file").val();
            var suffix = fileDir.substr(fileDir.lastIndexOf("."));
            if("" == fileDir){
                alert("选择需要导入的Excel文件！");
                return false;
            }
            if(yearNo.length==0){
                alert("年度不能为空");
                return false;
            }
            if(gettime.length==0){
                alert("获取时间不能为空");
                return false;
            }
            if(examClass.length==0){
                alert("考试分类不能为空");
                return false;
            }
            if(examName.length==0){
                alert("考试名称不能为空");
                return false;
            }
            if(".xls" != suffix && ".xlsx" != suffix ){
                alert("选择Excel格式的文件导入！");
                return false;
            }

            return true;
        }

        function loadDatagrid(data) {
            var yearNo = $('#yearNo').val();
            var gettime = $('#gettime').val();
            var examClass=$('#examClass').val();
            var examName =$('#examName').combobox('getText');
            me.datagrid1.datagrid({
                idField: me.idFiled,
                checkOnSelect: true,
                singleSelect: false,
                selectOnCheck: true,
                data: $.parseJSON(data),
                frozenColumns: [[{ field: 'ck', checkbox: true },
                    {field: 'yearNo', title: '年度', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value == yearNo) {
                                strReturn =value;
                            } else {
                               strReturn = '<span style="color: red" >'+value+'</span>';
                            }
                            return strReturn;
                        }
                        },
                    {field: 'areaCode', title: '地市', width: 150, align: 'center',},
                    {field: 'ticketNumber', title: '准考证号', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'userName', title: '姓名', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'sex', title: '性别', width: 60, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if(regSex.test(value)) {
                                strReturn =value;
                            }else{
                                strReturn = '<span style="color: red" >'+value+'</span>';
                            }
                            return strReturn;
                        }},
                ]],
                columns: [[
                    {field: 'titleLevel', title: '级别', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'professialCode', title: '专业', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'idCardNo', title: '证件号', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if(checkIDCard(value)) {
                                strReturn =value;
                            }else{
                                strReturn = '<span style="color: red" >'+value+'</span>';
                            }
                            return strReturn;
                           }
                        },
                    {field: 'gettime', title: '资格取得时间', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            if (value.length==0) {
                                $('#null1').val(1);
                            }
                            var strReturn = '';
                            if (value ==gettime) {
                                strReturn =value;
                            } else {
                                strReturn = '<span style="color: red" >'+value+'</span>';
                                $('#stu').val(1);
                            }
                            return strReturn;
                        }},
                    {field: 'unitName', title: '单位名称', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'fileNumber', title: '档案号', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'birthday', title: '出生日期', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            if (value.length==0) {
                                $('#null1').val(1);
                            }
                        var strReturn = '';

                        if(regBirthday.test(value)) {
                             strReturn =value;
                            }else{
                            strReturn = '<span style="color: red" >'+value+'</span>';
                            $('#stu').val(1);
                            }
                            return strReturn;
                      }
                    },
                    {field: 'examName', title: '考试名称', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            if (value.length==0) {
                                $('#null1').val(1);
                            }
                            var strReturn = '';
                            if (value == examName) {
                                strReturn =value;
                            } else {
                                strReturn = '<span style="color: red" >'+value+'</span>';
                                $('#stu').val(1);
                            }
                            return strReturn;
                        }},
                    {field: 'certificateNumber', title: '证书编号', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'managerNo', title: '管理号', width: 120, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'positionalTitles', title: '职称', width: 150, align: 'center',
                        formatter: function (value, row, index) {
                            var strReturn = '';
                            if (value.length==0) {
                                $('#null1').val(1);
                            }else {
                                strReturn = value;
                            }
                            return strReturn;
                        }},
                    {field: 'back1', title: '备注', width: 150, align: 'center',
                    },
                    {
                        field: 'ops', title: '操作', width: 100, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var str = "";
                            if (rowIndex != null) {
                                str += '<span style="line-height: 28px"><a href="javascript:void(0)" style="color:blue;text-decoration:underline;" title="删除"  onclick="deleteRow(this)" >删除</a></span>';
                                return str;
                            }
                        }
                    },
                ]],
            });
        }
        function deleteRow(target) {
            var row = $('#datagrid1').datagrid('getChecked');
            row.forEach(function(element){
                var index = $('#datagrid1').datagrid('getRowIndex',element);
                $('#datagrid1').datagrid('deleteRow',index);
                me.datagrid1.datagrid('reload');
            });

        }
    </script>
</head>
<body class="easyui-layout">
<input type="hidden" id="stu" name="stu"/>
<input type="hidden" id="null1" name="null1"/>
<div data-options="region:'north'" style="height:90px">
    <form method="POST"  enctype="multipart/form-data" id="form1">
    <table>
        <tr>
            <td>考试年度：</td>
            <td><input name="yearNo" id="yearNo" class="easyui-combobox" style="width:150px"/></td>
            <td>取得时间：</td>
            <td><input name="gettime" id="gettime" class="easyui-datebox" style="width:250px"/></td>
        </tr>
        <tr>
            <td>考试分类:</td>
            <td>
                <input id="examClass" class="easyui-combobox" name="examClass"
                       style="width: 150px " maxlength="30"
                       data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=EXAM_CLASS'"/>
            </td>
            <td>
                考试名称:
            </td>

            <td>
                <input id="examName" class="easyui-combobox" name="examName"
                       style="width: 250px " maxlength="30"
                       data-options="method:'get',valueField:'dictCode',textField:'dictName'"
                />
            </td>
            <td> <input type="file" id="file" name="file" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel" value="浏览" /></td>
            <td><a href="javascript:void(0)" id="btn" icon="icon-excel">导入Excel</a></td>
            <td><a href="javascript:void(0)" id="btn_save_ok" icon="icon-save">保存</a></td>
            <td><a href="javascript:void(0)" id="remove" icon="icon-remove">清空</a></td>
            <td><a href="javascript:void(0)" id="down" icon="icon-download" >下载职称考试模版</a>
            </td>
        </tr>
    </table>
    </form>
</div>
<div title="数据列表" region="center" border="false" >
    <div id ="custab"style="height: 100%">
        <table id="datagrid1" class="easyui-datagrid">
        </table>
    </div>
</div>
</body>
</html>
