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
    <title>组织机构管理</title>
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
            edit_form: null,
            edit_user_form: null,
            edit_window: null,
            edit_user_window: null,
            search_form: null,
            search_window: null,
            tree1: null,
            idFiled: 'bid',
            displayName: 'unitName',
            actionUrl: '<%=basePath%>/BaseUnit/'
        };

        $(function () {
            pageInit();
            loadGrid();

        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_user_window = $('#edit_user_window');
            me.edit_form = me.edit_window.find('#edit_form');
            me.edit_user_form = me.edit_user_window.find('#edit_user_form');
            me.search_window = $('#search_window');
            me.search_form = me.search_window.find('#search_form');
            me.tree1 = $('#tree1');
            me.datagrid1 = $('#datagrid1');
            me.datagrid2 = $('#datagrid2');
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $('#user_edit_ok').linkbutton().click(function () {
                SaveUserData();
            });
            $('#user_edit_cancel').linkbutton().click(function () {
                me.edit_user_window.window('close');
            });
            $('#btn_search_ok').linkbutton().click(function () {
                me.datagrid1.datagrid({pageNumber: 1});
                me.datagrid2.datagrid({pageNumber: 1});
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
                url: me.actionUrl + 'getList.do',
                pagination: true,
                columns: [[
                    {
                        field: 'state',
                        title: '状态',
                        width: 60,
                        align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            if (value == '1') {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-ok" title="点击改变状态" onClick="changeState(\'' + rowData.bid + '\',\'' + value + '\')"  style="padding-left:20px;height:20px;" >&nbsp;&nbsp;</a>';
                            } else {
                                strReturn = '<a href="javascript:void(0)" class="l-btn-text icon-no"  title="点击改变状态" onClick="changeState(\'' + rowData.bid + '\',\'' + value + '\')" style="padding-left:20px;height:20px;">&nbsp;&nbsp;</a>';
                            }
                            return strReturn;
                        }
                    },
                    {field: 'unitName', title: '机构名称', width: 200, align: 'center'},
                    {field: 'unitCode', title: '单位编号', width: 160, align: 'center'},
                    {field: 'unitAttach', title: '机构隶属', width: 100, align: 'center'},
                    {field: 'unitCategory', title: '机构类别', width: 180, align: 'center'},
                    {field: 'industryInvolved', title: '所属行业', width: 200, align: 'center'},
                    {field: 'unitNature', title: '单位性质', width: 100, align: 'center'},
                    {field: 'economicType', title: '经济类型', width: 100, align: 'center'},
                    {field: 'organizationCode', title: '组织机构代码', width: 120, align: 'center'},
                    {field: 'creditCode', title: '统一社会信用代码', width: 150, align: 'center'},
                    {field: 'back3', title: '机构证件预览', width: 100, align: 'center',
                        formatter: function (value, data, index) {
                            var strReturn = '';
                            strReturn = '<a href="javascript:void(0)" title="点击查看" id = "creditCodeImage'+index+'" onClick="checkImage(\'' + value + '\',\'' + index + '\')"  style="padding-center:20px;height:20px;" >点击查看</a>'
                            return strReturn;

                        }
                    },
                    /*{field: 'back2', title: '统一社会信用代码预览2', width: 200, align: 'center',
                        formatter: function (value, data, index) {
                            var strReturn = '';
                            strReturn = '<div> &nbsp;&nbsp;&nbsp;&nbsp;</div><span ><img src="/static/uploadImages/春联万家20181022140030.jpg"></span>';
                            return strReturn;
                        }
                    },*/
                ]],
                checkOnSelect: true,
                singleSelect: true,
                selectOnCheck: true,
                pageNumber: 1,
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
                        text: '注销', iconCls: 'icon-remove', handler: function () {
                            var ids = [];
                            var names = [];
                            var unitCodes = [];
                            var rows = me.datagrid1.datagrid('getSelections');
                            if (rows.length == 0) {
                                showError('请选择一条记录进行操作!');
                            }
                            else {
                                for (var i = 0; i < rows.length; i++) {
                                    if (rows[i]['unitCode'].length == 6){
                                        showError('职称办单位不能被注销!');
                                        return;
                                    }else{
                                        ids.push(rows[i][me.idFiled]);
                                        names.push(rows[i][me.displayName]);
                                        unitCodes.push(rows[i]['unitCode']);
                                    }
                                }
                                Delete(ids.join(','), names.join(','),unitCodes.join(","));
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
                },
                onSelect: function (rowIndex, rowData) {
                    loadGrid2(rowData.unitCode);
                },
                onLoadSuccess:function () {
                    $("a[name='creditCodeImage']").click(function () {
                        var _this = $(this);//将当前的pimg元素作为_this传入函数
                        imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
                    }/*, function () {
                        $('#outerdiv').fadeOut("fast");
                    }*/)
                }

            });

        }

        //点击查看图片
        function checkImage(val,index) {
            /*$("#creditCodeImage"+index).click(function () {*/
                if (val == null || val ==undefined ){
                    showError("图片未上传!");
                    return;
                }else {
                 var idx = val.lastIndexOf(".");
                    if (idx != -1){
                        var  ext = val.substr(idx+1).toUpperCase();
                        ext = ext.toLowerCase( );
                        if (ext != 'jpg' && ext != 'png' && ext != 'jpeg' && ext != 'gif'){
                            showError("图片格式不正确!");
                            return;
                        }
                    }else {
                        showError("图片未上传或格式不正确!");
                        return;
                    }
                }
                var _this = $(this);//将当前的pimg元素作为_this传入函数
                imgShow("#outerdiv", "#innerdiv", "#bigimg", _this, val);
           // }
                 /*function () {
                $('#outerdiv').fadeOut("fast");
            })*/
        }

        //显示图片
        function imgShow(outerdiv, innerdiv, bigimg, _this,val) {
            //console.log("哈哈");
           // var src = val;//获取当前点击的pimg元素中的src属性
            //$(bigimg).attr("src", val);//设置#bigimg元素的src属性

            /*获取当前点击图片的真实大小，并显示弹出层及大图*/
            $("#bigimg").attr("src", val).load(function () {
                var windowW = $(window).width();//获取当前窗口宽度
                var windowH = $(window).height();//获取当前窗口高度
                var realWidth = this.width;//获取图片真实宽度
                var realHeight = this.height;//获取图片真实高度
                var imgWidth, imgHeight;
                var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

                if (realHeight > windowH * scale) {//判断图片高度
                    imgHeight = windowH * scale;//如大于窗口高度，图片高度进行缩放
                    imgWidth = imgHeight / realHeight * realWidth;//等比例缩放宽度
                    if (imgWidth > windowW * scale) {//如宽度扔大于窗口宽度
                        imgWidth = windowW * scale;//再对宽度进行缩放
                    }
                } else if (realWidth > windowW * scale) {//如图片高度合适，判断图片宽度
                    imgWidth = windowW * scale;//如大于窗口宽度，图片宽度进行缩放
                    imgHeight = imgWidth / realWidth * realHeight;//等比例缩放高度
                } else {//如果图片真实高度和宽度都符合要求，高宽不变
                    imgWidth = realWidth;
                    imgHeight = realHeight;
                }
                $(bigimg).css("width", imgWidth);//以最终的宽度对图片缩放

                var w = (windowW - imgWidth) / 2 - 100;//计算图片与窗口左边距
                var h = (windowH - imgHeight) / 2;//计算图片与窗口上边距
                $(innerdiv).css({"top": h, "left": w});//设置#innerdiv的top和left属性
                $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
            });

            $(outerdiv).click(function () {//再次点击淡出消失弹出层
                $(this).fadeOut("fast");
            });
        }

        //添加修改初始化
        function AddOrUpdate(action) {
            switch (action) {
                case 'add':
                    me.edit_form.find('#state').attr('checked', true);
                    //me.edit_form.find('#areaGrade1,#areaGrade2,#areaGrade3').combobox().combobox('setValue', '   ==请选择==');
                    //清空表单
                    me.edit_form.form('clear');
                    me.edit_form.find('#unitName').textbox({disabled:false});
                    //获取当前用户 unitCode 及 行政区划代码
                    $.ajax({
                        url: me.actionUrl + 'AddOrUpdate.do',
                        success: function (data) {
                            me.edit_form.form('load', data.baseUnit);
                           // me.edit_form.find('#areaGrade1').combobox().combobox('setValue', '410000');
                            //行政区赋值
                            //area1('410000');
                            area2(data.areaCode1);
                            area3(data.areaCode2);
                        }
                    });
                   // me.edit_form.find('#unitCode,#bid,#organizationCode,#creditCode,#unitName ,#unitAttach,#unitCategory ,#industryInvolved ,#unitNature  ,#economicType ,#linkMan ,#phone ,#address ,#fax ,#email ,#postalCode ,#parentUnitCode ').textbox().textbox('setValue', '');
                    $('#unitCategory.easyui-combobox').combobox({ disabled:false });
                    $('#areaGrade2.easyui-combobox').combobox({ disabled:false });
                    $('#areaGrade3.easyui-combobox').combobox({ disabled:false });
                    me.edit_window.find('div[region="south"]').css('display', 'block');
                    me.edit_window.window('open');
                    break;
                case 'update':
                    var selectedRows = me.datagrid1.datagrid('getSelections');
                    if (selectedRows.length > 0) {
                        $.ajax({
                            url: me.actionUrl + 'AddOrUpdate.do?bid=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                //清空表单
                                me.edit_form.form('clear');
                                $('#unitCategory.easyui-combobox').combobox({ disabled:true });
                                $('#areaGrade2.easyui-combobox').combobox({ disabled:true });
                                $('#areaGrade3.easyui-combobox').combobox({ disabled:true });
                                me.edit_form.find('#unitName').textbox({disabled:true});
                                me.edit_form.find('#unitName').textbox({required:false});
                                me.edit_form.form('load', data.baseUnit);
                                me.edit_form.find('#areaGrade1').combobox().combobox('setValue', '410000');
                                //行政区赋值
                                //area1('410000');
                                area2(data.areaCode1);
                                area3(data.areaCode2);

                                console.log(data.areaCode1);
                                $('#unitName').textbox('textbox').attr('readonly',true);
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
                            url: me.actionUrl + 'AddOrUpdate.do?bid=' + selectedRows[0][me.idFiled],
                            success: function (data) {
                                //清空表单
                                me.edit_form.form('clear');
                                //加载数据
                                me.edit_form.find('#unitName').textbox({disabled:true});
                                me.edit_form.find('#unitName').textbox({required:false});
                                me.edit_form.form('load', data.baseUnit);
                                //行政区赋值
                                //area1('410000');
                                area2(data.areaCode1);
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

        //更改状态操作
        function changeState(id, value) {
            $.ajax({
                url: me.actionUrl + 'UpdateState.do',
                data: {bid: id, state: value ,type : 1},
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
            var areaCode = $("#areaGrade3").val();
            if (areaCode == null || areaCode =='==请选择==' || areaCode.length==0){
                showInfo('行政区划代码必填!');
                return;
            };
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

        //删除机构
        function Delete(ids, names,unitCodes) {
            $.messager.confirm('提示信息', '确认要注销【' + names + '】机构？注销后该机构所有用户将不能登录', function (isClickedOk) {
                if (isClickedOk) {
                    $.ajax({
                        url: me.actionUrl + 'Delete.do',
                        data: {ids: ids,unitCodes:unitCodes},
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

        //修改管理员用户
        function updateUser(userId,displayName) {
            $.ajax({
                url: '<%=basePath%>/User/AddOrUpdate.do?userId='+userId,
                success: function (data) {
                    if(displayName !== "系统管理员"){
                        $("#mobilephone").attr('data-options',"validType:'mobile'");
                    }
                    me.edit_user_form.form('load', data);
                    me.edit_user_window.find('div[region="south1"]').css('display', 'block');
                    me.edit_user_window.window('open');
                }
            });
        }

        //保存管理员用户数据
        function SaveUserData() {
            if (me.edit_user_form.form('validate')) {
                $.ajax({
                    url: '<%=basePath%>/User/Save.do',
                    data: me.edit_user_form.serialize(),
                    success: function (returnData) {
                        if (returnData) {
                            if (returnData.isOk == 1) {
                                showInfo(returnData.message);
                                me.datagrid2.datagrid('reload');
                                $("#edit_user_window").window("close");

                            } else {
                                showError(returnData.message);
                            }
                        }
                    }
                });
            }
        }

        //初始化管理员密码
        function initPassword(userId) {
            $.messager.confirm('确认', '您确认要初始化改管理员密码吗？', function (r) {
                if (r) {
                    $.ajax({
                        url: '<%=basePath%>/User/initPassword.do',
                        data: {userId: userId},
                        success: function (returnData) {
                            if (returnData) {
                                if (returnData.isOk == 1) {
                                    showInfo(returnData.message);
                                } else {
                                    showError(returnData.message);
                                }
                            }
                        }
                    });
                }
            });

        }

        //根据单位名称展示管理员用户
        function loadGrid2(unitCode) {
            me.datagrid2.datagrid({
                idField: 'userId',
                url: me.actionUrl + 'getUserWhere.do?unitCode=' + unitCode,
                columns: [[
                    {field: 'displayName', title: '姓名', width: '20%', align: 'center'},
                    {field: 'loginName', title: '登录名', width: 200, align: 'center'},
                    {field: 'mobilephone', title: '手机号', width: 100, align: 'center'},
                    {field: 'email', title: '电子邮箱', width: 150, align: 'center'},
                    {field: 'opt', title: '操作', width: 240, align: 'center',
                        formatter: function (value, rowData, rowIndex) {
                            var strReturn = '';
                            strReturn += '<a href="javascript:void(0)"   title="点击修改管理员信息" onClick="updateUser(\'' + rowData['userId'] + '\',\'' + rowData['displayName'] + '\');" style="padding-left:20px;" >编辑信息</a>';
                            strReturn += '&nbsp;&nbsp;<a href="javascript:void(0)"  title="点击初始化管理员密码" onClick="initPassword(\'' + rowData['userId'] + '\');" style="padding-left:20px;" >初始化密码</a>';
                            return strReturn;
                        }
                    }
                ]],
                checkOnSelect: true,
                singleSelect: true,
                selectOnCheck: true,
                pageNumber: 1,
                pagination: false
            })
        }

    </script>
</head>
<body class="easyui-layout">

<div title="数据列表" region="center" border="false">
    <table id="datagrid1"></table>
</div>

<div title="组织结构信息" id="edit_window" class="easyui-window" closed="true"
     style="width: 640px;height: 430px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="bid" name="bid" value="00000000-0000-0000-0000-000000000000"/>
                </div>
                <table cellpadding="5" align="center">
                    <tr>
                        <td>
                            机构名称：
                        </td>
                        <td colspan="3">
                            <input id="unitName" name="unitName" class="easyui-textbox"
                                   invalidMessage = "该机构已存在,请检查是否输入有误!"
                                   data-options="required:true,validType:['remote[\'<%=basePath %>/BaseUnit/checkCode.do\',\'unitName\']']"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            机构隶属：
                        </td>

                        <td colspan="3">
                            <input id="unitAttach" class="easyui-combobox" name="unitAttach" value="机构隶属"
                                   style="width: 475px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',
                                   url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_ATTACH'"/>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            机构类别：
                        </td>
                        <td colspan="3">
                            <input id="unitCategory" class="easyui-combobox" name="unitCategory" value="机构类别"
                                   style="width: 475px" maxlength="30"
                                   data-options="required:true,valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_CATEGORY'"/>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            所属行业：
                        </td>
                        <td colspan="3">
                            <input id="industryInvolved" class="easyui-combobox" name="industryInvolved" value="所属行业"
                                   style="width: 475px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=INDUSTRY_INVOLVED'"/>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位性质：
                        </td>
                        <td colspan="3">
                            <input id="unitNature" class="easyui-combobox" name="unitNature" value="单位性质"
                                   style="width: 475px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_NATURE'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            经济类型：
                        </td>
                        <td colspan="3">
                            <input id="economicType" class="easyui-combobox" name="economicType" value="经济类型"
                                   style="width: 475px" maxlength="30"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ECONOMIC_TYPE'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            行政区划代码：
                        </td>
                        <%--<td style="width:150px;word-break:break-all">
                            <input id="areaGrade1" class="easyui-combobox" value="==请选择==" style="width: 150px"
                                   data-options="required:true"/>

                        </td>--%>
                        <td style="word-break:break-all">
                            <input id="areaGrade2" class="easyui-combobox" value="==请选择==" style="width: 230px"
                                   data-options=""/>
                        </td>
                        <td>
                            <input id="areaGrade3" class="easyui-combobox" name="areaNumber" value="==请选择=="
                                   style="width: 230px" maxlength="30"
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

<div title="管理员信息" id="edit_user_window" class="easyui-window" closed="true"
     style="width: 550px;height: 300px; padding: 5px;">
    <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
            <form id="edit_user_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="userId" name="userId"/>
                </div>
                <table cellpadding="3" align="center">
                    <tr>
                        <td>
                            登录名：
                        </td>
                        <td>
                            <input id="loginName" name="loginName" readOnly="true" class="easyui-textbox"
                                   data-options="required:true"
                                   style="width: 150px" maxlength="30"/>
                        </td>
                        <td>
                            姓名：
                        </td>
                        <td>
                            <input id="displayName" name="displayName" class="easyui-textbox" style="width:150px;"
                                   data-options="required:true,validType:'chinese'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            联系人手机号：
                        </td>
                        <td>
                            <input id="mobilephone" name="mobilephone" class="easyui-textbox" style="width:150px"
                                   data-options=""/>
                        </td>
                        <td>
                            电子邮箱：
                        </td>
                        <td>
                            <input id="email" name="email" class="easyui-textbox" style="width:150px"
                                   data-options="validType:'email'"/>
                        </td>
                    </tr>

                    </tr>
                    <tr>
                        <td>
                            备注：
                        </td>
                        <td colspan="3">
                            <input id="backup1" name="backup1" style="width:380px;height:60px" class="easyui-textbox"
                                   multiline="true">
                        </td>
                    </tr>

                    <tr>
                        <td>
                            账号状态：
                        </td>
                        <td align="left">
                            <input type="checkbox" id="state" name="state" value="1" checked="checked"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div region="south" border="false" style="text-align: center;margin-top: 12px; height:30px;">
            <a id="user_edit_ok" icon="icon-save" href="javascript:void(0)">确定</a>
            <a id="user_edit_cancel" icon="icon-cancel" href="javascript:void(0)">关闭</a>
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
                            组织机构名称：
                        </td>
                        <td>
                            <input name="displayName" id="displayName1" class="easyui-textbox" style="width: 150px;"/>
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
<div title="设置角色" id="role_window" class="easyui-window" closed="true" style="width: 400px;height: 460px;">
    <table id="role_window_datagrid">
    </table>
</div>

<div data-options="region:'south',split:true" style="height:20%;">
    <div class="easyui-layout" data-options="fit:true">
        <div title="机构管理员信息" region="center" border="false">
            <table id="datagrid2"></table>
        </div>
    </div>
</div>

<div id="outerdiv"
     style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:2000000;display:none;">
    <div id="innerdiv" style="position:absolute;">
        <%--弹出图片框--%>
        <img id="bigimg" style="border:5px solid #fff;" src=""/>
    </div>
</div>
</body>
</html>
