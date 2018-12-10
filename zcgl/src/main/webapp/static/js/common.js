$.extend($.fn.layout.methods, {
    /**
     * 面板是否存在和可见
     * @param {Object} jq
     * @param {Object} params
     */
    isVisible: function(jq, params) {
        var panels = $.data(jq[0], 'layout').panels;
        var pp = panels[params];
        if(!pp) {
            return false;
        }
        if(pp.length) {
            return pp.panel('panel').is(':visible');
        } else {
            return false;
        }
    },
    /**
     * 隐藏除某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    hidden: function(jq, params) {
        return jq.each(function() {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            if(!opts.regionState){
                opts.regionState = {};
            }
            var region = params;
            function hide(dom,region,doResize){
                var first = region.substring(0,1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if(panels[expand]) {
                    if($(dom).layout('isVisible', expand)) {
                        opts.regionState[region] = 1;
                        panels[expand].panel('close');
                    } else if($(dom).layout('isVisible', region)) {
                        opts.regionState[region] = 0;
                        panels[region].panel('close');
                    }
                } else {
                    panels[region].panel('close');
                }
                if(doResize){
                    $(dom).layout('resize');
                }
            };
            if(region.toLowerCase() == 'all'){
                hide(this,'east',false);
                hide(this,'north',false);
                hide(this,'west',false);
                hide(this,'south',true);
            }else{
                hide(this,region,true);
            }
        });
    },
    /**
     * 显示某个region，center除外。
     * @param {Object} jq
     * @param {Object} params
     */
    show: function(jq, params) {
        return jq.each(function() {
            var opts = $.data(this, 'layout').options;
            var panels = $.data(this, 'layout').panels;
            var region = params;

            function show(dom,region,doResize){
                var first = region.substring(0,1);
                var others = region.substring(1);
                var expand = 'expand' + first.toUpperCase() + others;
                if(panels[expand]) {
                    if(!$(dom).layout('isVisible', expand)) {
                        if(!$(dom).layout('isVisible', region)) {
                            if(opts.regionState[region] == 1) {
                                panels[expand].panel('open');
                            } else {
                                panels[region].panel('open');
                            }
                        }
                    }
                } else {
                    panels[region].panel('open');
                }
                if(doResize){
                    $(dom).layout('resize');
                }
            };
            if(region.toLowerCase() == 'all'){
                show(this,'east',false);
                show(this,'north',false);
                show(this,'west',false);
                show(this,'south',true);
            }else{
                show(this,region,true);
            }
        });
    }
});

$.extend($.fn.datagrid.defaults, {
    fit: true,
    nowrap: false,
    striped: true,
    rownumbers: true,
    singleSelect: true,
    pagination: true,
    pageSize: 10,
    pageNumber:1,
    sortOrder: 'desc',
    border: false,
    pageList: [10, 20, 30, 50, 100, 2147483647],
    onLoadSuccess: function (data) {
         $(this).datagrid('clearSelections');
        // $(this).datagrid('selectRow', 0);
    },
    onLoadError: function () {
        //showError('加载数据错误！');
        //showError('由于您长时间未操作，为了确保安全，请从新登录');
        document.location.reload();
    }
});

$.extend($.fn.window.defaults, {
    modal: true,
    //closed: true,
    shadow: false,
    collapsible: false,
    minimizable: false,
    maximizable: false
});


/*===================EasyUI通用Tree扩展开始===================*/

$.extend($.fn.tree.defaults, {
    onLoadError: function (XMLHttpRequest, textStatus, errorThrown) {
        if (errorThrown) {
           /* showError('加载数据错误！' + errorThrown);*/
            document.location.reload();
        } else {
           /* showError('加载数据错误！');*/
            document.location.reload();
        }
    }
});


$.extend($.fn.tree.methods,{
    getLeafChildren:function(jq, params){
        var nodes = [];
        $(params).next().children().children("div.tree-node").each(function(){
            nodes.push($(jq[0]).tree('getNode',this));
        });
        return nodes;
    }
});

/**
 *
 * @requires jQuery,EasyUI
 *
 * 扩展tree，使其支持平滑数据格式
 */
$.fn.tree.defaults.loadFilter = function(data, parent) {
    var opt = $(this).data().tree.options;
    var idFiled, textFiled, parentField;
    if (opt.parentField) {
        idFiled = opt.idFiled || 'id';
        textFiled = opt.textFiled || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [], tmpMap = [];
        for (i = 0, l = data.length; i < l; i++) {
            tmpMap[data[i][idFiled]] = data[i];
        }
        for (i = 0, l = data.length; i < l; i++) {
            if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children'])
                    tmpMap[data[i][parentField]]['children'] = [];
                data[i]['text'] = data[i][textFiled];
                tmpMap[data[i][parentField]]['children'].push(data[i]);
            } else {
                data[i]['text'] = data[i][textFiled];
                treeData.push(data[i]);
            }
        }
        return treeData;
    }
    return data;
};

/**
 *
 * @requires jQuery,EasyUI
 *
 * 扩展treegrid，使其支持平滑数据格式
 */
$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
    var opt = $(this).data().treegrid.options;
    var idFiled, textFiled, parentField;
    if (opt.parentField) {
        idFiled = opt.idFiled || 'id';
        textFiled = opt.textFiled || 'text';
        parentField = opt.parentField;
        var i, l, treeData = [], tmpMap = [];
        for (i = 0, l = data.length; i < l; i++) {
            tmpMap[data[i][idFiled]] = data[i];
        }
        for (i = 0, l = data.length; i < l; i++) {
            if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children'])
                    tmpMap[data[i][parentField]]['children'] = [];
                data[i]['text'] = data[i][textFiled];
                tmpMap[data[i][parentField]]['children'].push(data[i]);
            } else {
                data[i]['text'] = data[i][textFiled];
                treeData.push(data[i]);
            }
        }
        return treeData;
    }
    return data;
};

/**
 * @author wfire
 *
 * @requires jQuery,EasyUI
 *
 * 扩展combotree，使其支持平滑数据格式
 */
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;

/**
 *
 * @requires jQuery,EasyUI
 *
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
    var l = left;
    var t = top;
    if (l < 1) {
        l = 1;
    }
    if (t < 1) {
        t = 1;
    }
    var width = parseInt($(this).parent().css('width')) + 14;
    var height = parseInt($(this).parent().css('height')) + 14;
    var right = l + width;
    var buttom = t + height;
    var browserWidth = $(window).width();
    var browserHeight = $(window).height();
    if (right > browserWidth) {
        l = browserWidth - width;
    }
    if (buttom > browserHeight) {
        t = browserHeight - height;
    }
    $(this).parent().css({/* 修正面板位置 */
        left : l,
        top : t
    });
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/*===================EasyUI通用Tree扩展结束===================*/

/**
 *
 * @requires jQuery,EasyUI,jQuery cookie plugin
 *
 * 更换EasyUI主题的方法
 *
 * @param themeName
 *            主题名称
 */

changeTheme = function(themeName) {
    var $easyuiTheme = $('#easyuiTheme');
    var url = $easyuiTheme.attr('href');
    var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
    $easyuiTheme.attr('href', href);

    var $iframe = $('iframe');
    if ($iframe.length > 0) {
        for ( var i = 0; i < $iframe.length; i++) {
            var ifr = $iframe[i];
            $(ifr).contents().find('#easyuiTheme').attr('href', href);
        }
    }

    $.cookie('easyuiThemeName', themeName, {expires : 7, path: '/', secure: false});
};



serializeObject = function(form) {
    var o = {};
    $.each(form.serializeArray(), function(index) {
        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + "," + this['value'];
        } else {
            o[this['name']] = this['value'];
        }
    });
    return o;
};



$.ajaxSetup({
    global: false,
    type: "POST",
    dataType: 'json',
/*    error: function (XMLHttpRequest, textStatus, errorThrown) {
        if (errorThrown) {
            showError('加载数据错误！' + errorThrown);
        } else {
            showError('加载数据错误！');
        }*/
    error: function (XMLHttpRequest, textStatus, errorThrown) {
        if (errorThrown) {
            //showError('由于您长时间未操作，为了确保安全，请从新登录');
            //alert('由于您长时间未操作，为了确保安全，请从新登录');
            document.location.reload();
        } else {
            showError('加载数据错误！');
        }
    }
});

function showError(message) {
    $.messager.alert('错误信息', message, 'error');
}

function showInfo(message) {
    $.messager.alert("提示信息", message, "info");
}

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g,
        function (m, i) {
            return args[i];
        });
}

String.format = function () {
    if (arguments.length == 0)
        return null;

    var str = arguments[0];
    for (var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
}
/*===============================================================*/

/*===================EasyUI通用验证扩展开始===================*/

$.extend($.fn.validatebox.defaults.rules, {
    eqPwd : {
        validator : function(value, param) {
            return value == $(param[0]).val();
        },
        message : '密码不一致！'
    },
    idcard : {// 验证身份证
        validator : function(value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message : '身份证号码格式不正确'
    },
    minLength: {
        validator: function(value, param){
            return value.length >= param[0];
        },
        message: '请输入至少（2）个字符.'
    },
    length:{validator:function(value,param){
            var len=$.trim(value).length;
            return len>=param[0]&&len<=param[1];
        },
        message:"输入内容长度必须介于{0}和{1}之间."
    },
    phone : {// 验证电话号码
        validator : function(value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message : '格式不正确,请使用下面格式:010-88888888'
    },
    mobile : {// 验证手机号码
        validator : function(value) {
            return /^(13|15|18|17|19)\d{9}$/i.test(value);
        },
        message : '手机号码格式不正确'
    },
    intOrFloat : {// 验证整数或小数
        validator : function(value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message : '请输入数字，并确保格式正确'
    },
    currency : {// 验证货币
        validator : function(value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message : '货币格式不正确'
    },
    qq : {// 验证QQ,从10000开始
        validator : function(value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message : 'QQ号码格式不正确'
    },
    integer : {// 验证整数
        validator : function(value) {
            return /^[+]?[1-9]+\d*$/i.test(value);
        },
        message : '请输入整数'
    },
    age : {// 验证年龄
        validator : function(value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message : '年龄必须是0到120之间的整数'
    },
    chinese : {// 验证中文
        validator : function(value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message : '请输入中文'
    },
    english : {// 验证英语
        validator : function(value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message : '请输入英文'
    },
    unnormal : {// 验证是否包含空格和非法字符
        validator : function(value) {
            return /.+/i.test(value);
        },
        message : '输入值不能为空和包含其他非法字符'
    },
    username : {// 验证用户名
        validator : function(value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message : '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    faxno : {// 验证传真
        validator : function(value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message : '传真号码不正确'
    },
    zip : {// 验证邮政编码
        validator : function(value) {
            return /^[0-9]\d{5}$/i.test(value);
        },
        message : '邮政编码格式不正确'
    },
    ip : {// 验证IP地址
        validator : function(value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message : 'IP地址格式不正确'
    },
    name : {// 验证姓名，可以是中文或英文
        validator : function(value) {
            return /^[\Α-\￥]+$/i.test(value)|/^\w+[\w\s]+\w+$/i.test(value);
        },
        message : '请输入姓名'
    },
    msn:{
        validator : function(value){
            return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message : '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    }
});

/*===================EasyUI通用验证扩展结束===================*/


// 判断闰年

Date.prototype.isLeapYear = function () {

    return (0 == this.getYear() % 4 && ((this.getYear() % 100 != 0) || (this.getYear() % 400 == 0)));

}


// 日期格式化

// 格式 YYYY/yyyy/YY/yy 表示年份

// MM/M 月份

// W/w 星期

// dd/DD/d/D 日期

// hh/HH/h/H 时间

// mm/m 分钟

// ss/SS/s/S 秒
Date.prototype.Format = function (formatStr) {

    var str = formatStr;

    var Week = ['日', '一', '二', '三', '四', '五', '六'];

    str = str.replace(/yyyy|YYYY/, this.getFullYear());

    str = str.replace(/yy|YY/, (this.getYear() % 100) > 9 ? (this.getYear() % 100).toString() : '0' + (this.getYear() % 100));

    str = str.replace(/MM/, this.getMonth() > 9 ? this.getMonth().toString() : '0' + this.getMonth());

    str = str.replace(/M/g, this.getMonth());

    str = str.replace(/w|W/g, Week[this.getDay()]);

    str = str.replace(/dd|DD/, this.getDate() > 9 ? this.getDate().toString() : '0' + this.getDate());

    str = str.replace(/d|D/g, this.getDate());

    str = str.replace(/hh|HH/, this.getHours() > 9 ? this.getHours().toString() : '0' + this.getHours());

    str = str.replace(/h|H/g, this.getHours());

    str = str.replace(/mm/, this.getMinutes() > 9 ? this.getMinutes().toString() : '0' + this.getMinutes());

    str = str.replace(/m/g, this.getMinutes());

    str = str.replace(/ss|SS/, this.getSeconds() > 9 ? this.getSeconds().toString() : '0' + this.getSeconds());

    str = str.replace(/s|S/g, this.getSeconds());

    return str;

}

//+---------------------------------------------------

//| 求两个时间的天数差 日期格式为 YYYY-MM-dd

//+---------------------------------------------------

function daysBetween(DateOne, DateTwo) {

    var OneMonth = DateOne.substring(5, DateOne.lastIndexOf('-'));

    var OneDay = DateOne.substring(DateOne.length, DateOne.lastIndexOf('-') + 1);

    var OneYear = DateOne.substring(0, DateOne.indexOf('-'));

    var TwoMonth = DateTwo.substring(5, DateTwo.lastIndexOf('-'));

    var TwoDay = DateTwo.substring(DateTwo.length, DateTwo.lastIndexOf('-') + 1);

    var TwoYear = DateTwo.substring(0, DateTwo.indexOf('-'));

    var cha = ((Date.parse(OneMonth + '/' + OneDay + '/' + OneYear) - Date.parse(TwoMonth + '/' + TwoDay + '/' + TwoYear)) / 86400000);

    return Math.abs(cha);

}

//+---------------------------------------------------

//| 日期计算

//+---------------------------------------------------

Date.prototype.DateAdd = function (strInterval, Number) {

    var dtTmp = this;

    switch (strInterval) {

        case 's':
            return new Date(Date.parse(dtTmp) + (1000 * Number));

        case 'n':
            return new Date(Date.parse(dtTmp) + (60000 * Number));

        case 'h':
            return new Date(Date.parse(dtTmp) + (3600000 * Number));

        case 'd':
            return new Date(Date.parse(dtTmp) + (86400000 * Number));

        case 'w':
            return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));

        case 'q':
            return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());

        case 'm':
            return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());

        case 'y':
            return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());

    }

}

//+---------------------------------------------------

//| 比较日期差 dtEnd 格式为日期型或者 有效日期格式字符串

//+---------------------------------------------------

Date.prototype.DateDiff = function (strInterval, dtEnd) {

    var dtStart = this;

    if (typeof dtEnd == 'string')//如果是字符串转换为日期型
    {

        dtEnd = StringToDate(dtEnd);

    }

    switch (strInterval) {

        case 's':
            return parseInt((dtEnd - dtStart) / 1000);

        case 'n':
            return parseInt((dtEnd - dtStart) / 60000);

        case 'h':
            return parseInt((dtEnd - dtStart) / 3600000);

        case 'd':
            return parseInt((dtEnd - dtStart) / 86400000);

        case 'w':
            return parseInt((dtEnd - dtStart) / (86400000 * 7));

        case 'm':
            return (dtEnd.getMonth() + 1) + ((dtEnd.getFullYear() - dtStart.getFullYear()) * 12) - (dtStart.getMonth() + 1);

        case 'y':
            return dtEnd.getFullYear() - dtStart.getFullYear();

    }

}

//+---------------------------------------------------

//| 日期输出字符串，重载了系统的toString方法

//+---------------------------------------------------

Date.prototype.toString = function (showWeek) {

    var myDate = this;

    var str = myDate.toLocaleDateString();

    if (showWeek) {

        var Week = ['日', '一', '二', '三', '四', '五', '六'];

        str += ' 星期' + Week[myDate.getDay()];

    }

    //+---------------------------------------------------

    //| 字符串转成日期类型

    //| 格式 MM/dd/YYYY MM-dd-YYYY YYYY/MM/dd YYYY-MM-dd

    //+---------------------------------------------------

    function StringToDate(DateStr) {

        var converted = Date.parse(DateStr);

        var myDate = new Date(converted);

        if (isNaN(myDate)) {

            //var delimCahar = DateStr.indexOf('/')!=-1?'/':'-';

            var arys = DateStr.split('-');

            myDate = new Date(arys[0], --arys[1], arys[2]);

        }

        return myDate;
    }
}


/*//获取菜单页面的按钮权限
InitMenuFun = function(basePath,MenuId) {
    $.ajax({
        url: basePath+"/Menu/GetMenuFun.do?MenuId="+MenuId,
        dataType: 'text',
        success: function (returnData) {
           // _menuBtnFuns =  returnData;
        }
    });
};*/






