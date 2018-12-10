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
            edit_form: null,
            idFiled: 'bid',
            displayName: 'unitName',
            actionUrl: '<%=basePath%>/BaseUnit/'
        };

        $(function () {
            pageInit();
            Update();
        });

        function pageInit() {
            me.edit_window = $('#edit_window');
            me.edit_form = me.edit_window.find('#edit_form');
            //select 只读状态
            $('#unitAttach.easyui-combobox').combobox({ disabled:'true' });
            $('#unitCategory.easyui-combobox').combobox({ disabled:'true' });
            $('#areaGrade1.easyui-combobox').combobox({ disabled:'true' });
            $('#areaGrade2.easyui-combobox').combobox({ disabled:'true' });
            $('#areaGrade3.easyui-combobox').combobox({ disabled:'true' });
            $('#unitName').textbox('textbox').attr('readonly',true);
            $('#unitName').textbox('disable');
            $('#btn_edit_ok').linkbutton().click(function () {
                SaveData();
            });
            $("#file").change(excelUpload);
            $("#file").trigger("click");//触发点击事件
        }
        //修改
        function Update() {
                        $.ajax({
                        url: me.actionUrl + 'Update.do',
                        success: function (data) {
                            //清空表单
                            me.edit_form.form('clear');
                            me.edit_form.form('load', data.baseUnit);
                            me.edit_form.find('#areaGrade1').combobox().combobox('setValue', '410000');
                            //行政区赋值
                            area1('410000');
                            area2(data.areaCode1);
                            area3(data.areaCode2);
                            me.edit_form.find('#unitName').attr('disabled', true);
                        }
                    });
            }

        //地区下拉框 省
        function area1(value) {
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

        }
        //地区下拉框 市
        function area2(value) {
            $("#areaGrade2").combobox({
                valueField:'areaCode',
                textField:'areaName',
                url:'<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=2',
                onSelect: function(data){
                    $('#areaGrade3').combobox('setValue', '==请选择==');
                    var url = '<%=basePath%>/BaseUnit/getAreaList.do?areaGrade=3&areaCode='+data.areaCode;
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
                onLoadSuccess: function () { //加载完成后,设置选中
                    $(this).combobox('select', value);
                }
            })
        }

        //添加修改（保存）
        function SaveData() {
            if (me.edit_form.form('validate')) {
                //var fileName = $("#file").val();
                $.ajax({
                    url: me.actionUrl + 'saveUnit.do',
                    data: me.edit_form.serialize(),
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
        }

        //上传公司证件图片
        function excelUpload() {
            $.messager.confirm("上传提示","请上传一张带有本单位统一社会信用代码的图片!", function(r){
                if(r){
                    var formData = new FormData($("#importForm")[0]);
                    var imgName = $("#file").val();
                    var  idx = imgName.lastIndexOf(".");
                    if (idx != -1){
                        var  ext = imgName.substr(idx+1).toUpperCase();
                             ext = ext.toLowerCase( );
                        if (ext != 'jpg' && ext != 'png' && ext != 'jpeg' && ext != 'gif'){
                            alert("只能上传.jpg  .png  .jpeg  .gif类型的文件!");
                            return;
                        }
                    }else {
                        alert("只能上传.jpg  .png  .jpeg  .gif类型的文件!");
                        return;
                    }
                        $.ajax({
                        url:me.actionUrl +'uploadImage.do',
                        type:'post',
                        data:formData,
                        dataType:'json',
                        processData: false,
                        contentType: false,
                        success:function(data){
                            if(data.isOk == 1){
                                $.messager.alert('上传','上传成功');
                                $("#creditCodePath").val(data.realPath);
                                $("#imageSpan").html("已上传");
                            }else{
                                $.messager.alert('上传',data.message);
                            }
                        },
                        error:function(){
                            $.messager.alert('上传',"上传失败!");
                        }
                    })
                }
            })
        }

    </script>
</head>
<body class="easyui-layout">

<div title="" id="edit_window" style="width: 100%;height: 100%; padding: 5px;">
    <div class="easyui-layout" fit="true" >
        <div region="west" border="false" style="padding: 1% 20%; background: #fff; border: 1px solid #ccc;">
            <form id="edit_form" name="edit_form" method="post">
                <div title="隐藏参数" style="display: none">
                    <input type="hidden" id="bid" name="bid" value="00000000-0000-0000-0000-000000000000"/>
                    <input type="hidden" id="creditCodePath" name="creditCodePath" />
                </div>
                <table cellpadding="5" >
                    <tr>
                        <td width="100px">
                            机构名称：
                        </td>
                        <td colspan="3">
                            <input id="unitName" name="unitName" class="easyui-textbox"
                                   style="width: 475px" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            机构类别：
                        </td>
                        <td colspan="3">
                            <input id="unitCategory" class="easyui-combobox" name="unitCategory" value="机构类别" style="width: 475px"
                                   data-options="valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_CATEGORY'"/>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            行政区划：
                        </td>
                        <td style="width:150px;word-break:break-all">
                            <input id="areaGrade1" class="easyui-combobox" value="==请选择==" style="width: 150px"
                                   data-options="required:true"/>

                        </td>
                        <td style="width:150px;word-break:break-all">
                            <input id="areaGrade2" class="easyui-combobox" value="==请选择==" style="width: 150px"
                                   data-options="required:true"/>
                        </td>
                        <td>
                            <input id="areaGrade3" class="easyui-combobox" name="areaNumber" value="==请选择=="
                                   style="width: 150px" maxlength="30"
                                   data-options="required:true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            所属行业：
                        </td>
                        <td colspan="3">
                            <input id="industryInvolved" class="easyui-combobox" name="industryInvolved" value="所属行业"
                                   style="width: 475px" maxlength="30"
                                   data-options="required:true,valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=INDUSTRY_INVOLVED'"/>

                        </td>
                    </tr>
                    <tr>
                        <td>
                            单位性质：
                        </td>
                        <td colspan="3">
                            <input id="unitNature" class="easyui-combobox" name="unitNature" value="单位性质"
                                   style="width: 475px" maxlength="30"
                                   data-options="required:true,valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=UNIT_NATURE'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            经济类型：
                        </td>
                        <td colspan="3">
                            <input id="economicType" class="easyui-combobox"  name="economicType" value="经济类型"
                                   style="width: 475px" maxlength="30"
                                   data-options="required:true,valueField:'dictCode',textField:'dictName',url:'<%=basePath%>/BaseUnit/getDictList.do?groupName=ECONOMIC_TYPE'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            组织机构代码：
                        </td>
                        <td colspan="3">
                            <input id="organizationCode" name="organizationCode" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            统一社会信用代码：
                        </td>
                        <td colspan="3">
                            <input id="creditCode" name="creditCode" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>

                    </tr>


                    <tr>
                        <td>
                            联系人：
                        </td>
                        <td colspan="3">
                            <input id="linkMan" name="linkMan" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            联系电话：
                        </td>
                        <td colspan="3">
                            <input id="phone" name="phone" class="easyui-textbox"
                                   style="width: 475px" maxlength="30" data-options="required:true,validType:'mobile'"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            通讯地址：
                        </td>
                        <td colspan="3">
                            <input id="address" name="address" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            邮政编码：
                        </td>
                        <td colspan="3">
                            <input id="postalCode" name="postalCode" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            传真：
                        </td>
                        <td colspan="3">
                            <input id="fax" name="fax" class="easyui-textbox"
                                   style="width: 475px" maxlength="30"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            电子邮箱：
                        </td>
                        <td colspan="3">
                            <input id="email" name="email" class="easyui-textbox"
                                   style="width: 475px" maxlength="30" data-options="validType:'email'"/>
                        </td>
                    </tr>
                </table>
                   <%-- <label>
                        统一社会信用代码图片：
                    </label>
                    <label>
                        <input id="image" name="image"  type="file" multiple=""
                               style="width: 475px" maxlength="30"/>
                    </label>--%>
            </form>

            <form id="importForm" enctype="multipart/form-data" method="post">
                <table cellpadding="5">
                    <tr>
                        单位证件照片上传(请上传一张带有本单位统一社会信用代码的证件图片)：
                    </tr>
                    <tr>
                        <input type="file" name="file" id="file" multiple="false" accept=".jpg,.jepg,.png,.gif" style="display:block;"/>
                        <span id = "imageSpan">未上传</span>
                    </tr>
                </table>
            </form>

            <div  border="false" style="padding: 1% 30%;">
                <a id="btn_edit_ok" icon="icon-save" href="javascript:void(0)">提交单位信息</a>
            </div>

        </div>
    </div>
</div>
</body>
</html>
