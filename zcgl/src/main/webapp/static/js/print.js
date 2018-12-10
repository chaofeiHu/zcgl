    
	function print(){
    var tr =$('datagrid-view').find('table');
    console.log(tr.children);
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
    console.log(html);
   var printHtml = document.getElementById("picOutDiv").innerHTML;
var wind = window.open("",'newwindow', 'height=300, width=700, top=100, left=100, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no');
wind.document.body.innerHTML = html;
wind.document.getElementsByClassName("pic")[0].style.width = "100%";
wind.document.getElementsByClassName("pic")[0].style.height = "100%";
wind.print();

    }