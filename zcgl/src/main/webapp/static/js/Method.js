function seleajax(url,op,value,text,id){
    $.ajax({
        url: url,
        dataType:'json',
        success: function (returnData) {
            console.log(returnData.length);
            var chil;
            if(returnData.length>1){
                chil=returnData;
            }else{
                chil =returnData[0].children;
            }
            var html='<option value="">'+op+'</option> ';
            for(var i=0;i<chil.length;i++){
                html+='<option value="'+chil[i][value]+'">'+chil[i][text]+'</option>'
            }
            $(id).html(html);
            form.render('select');
        }
    });
}


//日期转换
function formatdate(timestamp) {
    var date = new Date(timestamp);
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return Y + M + D + h + m + s;
}
//同步ajax，返回值
function ajax(url,data) {
    var returndata=new Object();
    $.ajax({
        async:false,  //同步
        url: url,
        data:data,
        dataType: 'json',
        success: function (ee) {
            returndata=ee;
        }
    })
    return returndata;
}

//打开弹出窗  并操作
function openlay(title,ID,url,data){
    layer.open({
        type: 1,
        title: title,
        maxmin: true,
        content: $("#"+ID),
        btn: ['确定', '取消'],
        yes: function (index, layero) {
            data=$("#"+ID).serialize()+data;
            var returnData=ajax(url,data);
            layer.alert(returnData.message);
            layer.close(index);
            table.reload('testReload');
        }
    });
}
function layer_dele(neirong,fengge){
    if(fengge==""||fengge==null){
        fengge='layer-ext-moon';
    }
    layer.alert(neirong, {
        title:'删除信息',
        icon: 2,
        skin: fengge
    })
}
function layer_upda(neirong,fengge){
    if(fengge==""||fengge==null){
        fengge='layer-ext-moon';
    }
    layer.alert(neirong, {
        title:'修改信息',
        icon: 3,
        skin: fengge
    })
}
function layer_add(neirong,fengge){
    if(fengge==""||fengge==null){
        fengge='layer-ext-moon';
    }
    layer.alert(neirong, {
        title:'添加信息',
        icon: 2,
        skin: fengge
    })
}