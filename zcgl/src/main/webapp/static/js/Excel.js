function base64 (s) { return window.btoa(unescape(encodeURIComponent(s))) }
    function tableToExcel(title,table){
    var tr =$('#'+table).find('tr');
    var uri = 'data:application/vnd.ms-excel;base64,';
    var html='<table border="1">';
    for(var i=0;i<tr.length;i++){
        html+='<tr>';
        for(var j=0;j<tr[i].cells.length;j++) {
            var rowspan=tr[i].cells[j].getAttribute('rowspan')==null ?0:tr[i].cells[j].getAttribute('rowspan');
            var colspan=tr[i].cells[j].getAttribute('colspan')==null?0:tr[i].cells[j].getAttribute('colspan');
            html+='<td align="center" rowspan='+rowspan+' colspan='+colspan+'>'+tr[i].cells[j].children[0].innerHTML+'</td>';
        }
        html+='</tr>';
    }
    html+='</table>';
      //下载的表格模板数据
      var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"xmlns="http://www.w3.org/TR/REC-html40"> ' +
          '<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>' +
          '<x:Name>'+title+'</x:Name>' +
          '<x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>' +
          '</x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->' +
          '</head><body>'+html+'</body></html>';

        document.getElementById("daochuExcel").download =title+".xls";
        document.getElementById("daochuExcel").href =uri+base64(template);
    }


function JttableToExcel(title,table) {  //静态table导出
    var tr =$('#'+table).find('tr');
    var uri = 'data:application/vnd.ms-excel;base64,';
    var html='<table border="1">';
    for(var i=0;i<tr.length;i++){
        html+='<tr>';
        for(var j=0;j<tr[i].cells.length;j++) {
            var rowspan=tr[i].cells[j].getAttribute('rowspan')==null ?0:tr[i].cells[j].getAttribute('rowspan');
            var colspan=tr[i].cells[j].getAttribute('colspan')==null?0:tr[i].cells[j].getAttribute('colspan');
            html+='<td align="center" rowspan='+rowspan+' colspan='+colspan+'>'+tr[i].cells[j].innerHTML+'</td>';
        }
        html+='</tr>';
    }
    html+='</table>';
    //下载的表格模板数据
    var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"xmlns="http://www.w3.org/TR/REC-html40"> ' +
        '<head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>' +
        '<x:Name>'+title+'</x:Name>' +
        '<x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>' +
        '</x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->' +
        '</head><body>'+html+'</body></html>';

    window.location.href = uri + base64(template);

}

    function preview(oper){

    if(oper < 10) {

        bdhtml =window.document.body.innerHTML;//获取当前页的html代码

        sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域

        eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域

        prnhtml =bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html

        prnhtml = prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html

        window.document.body.innerHTML= prnhtml;

        window.print();

        window.document.body.innerHTML =bdhtml;

    } else{
        bdhtml =window.document.body.innerHTML;//获取当前页的html代码
        var html='<table border="1"  class="layui-table" >';
        html+=$(".layui-table-header .layui-table thead").html();
        html+=$(".layui-table-body .layui-table tbody").html();
        html+='</table>';
        window.document.body.innerHTML= html;
        window.print();
      window.document.body.innerHTML =bdhtml;

    }

}
