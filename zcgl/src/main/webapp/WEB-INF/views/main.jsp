<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"
         contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="<%=basePath%>/static/js/plugin/layui/css/layui.css"  media="all">
<script src="<%=basePath%>/static/js/plugin/Highcharts-5.0.0/js/highcharts.js"></script>
<%-- <script src="<%=basePath%>/static/js/plugin/Highcharts-5.0.0/js/modules/exporting.js"></script>--%>
<style>
    .ulli{width:50%;float:left}
    .mainul{width: 100%;line-height: 30px}
</style>
<script src="<%=basePath%>/static/js/plugin/layui/layui.js" charset="utf-8"></script>
            <div style="width:100%;height:100%" >
                <div style="width: 100%;height: 49%">
                    <div class="layui-tab layui-tab-card" style="float:left;width: 49%;margin:0;height: 100%">
                        <ul class="layui-tab-title">
                            <li class="layui-this">待办事项</li>
                        </ul>
                        <div id="daiban" class="layui-tab-content" style="height: 100%;color:#333333">

                        </div>
                    </div>
                    <div id="tb1" class="layui-tab layui-tab-card" style="float:right;width: 49%;height: 100%;margin:0;">

                    </div>
                </div>
                 <div  id="container" class="layui-tab layui-tab-card"style="margin-top:1%;width: 100%;height: 49%;clear: left;">

                 </div>
            </div>
            <script type="text/javascript">
                $(function () {
                    loads();
                    selectTBJB();
                    selectAgency();
                });
                function selectAgency(){
                   /* <ul class="mainul">
                        <li class="ulli">123</li>
                        <li class="ulli" style="text-align: right" ><div style="padding-right:20px">阿文</div></li>
                    </ul>*/
                    $.ajax({
                        url: "<%=basePath%>/User/selectAgency.do",
                        dataType:'json',
                        success: function (returnData) {
                            if(returnData.length==0){
                                $("#daiban").html("<div style='text-align:center'>暂无数据</div>");
                            }else{
                                var num=4;
                                if(returnData.length<=num){
                                    num=returnData.length;
                                }
                                var html='';
                                for(var i=0;i<num;i++){
                                    html+='<ul class="mainul">';
                                    html+=' <li class="ulli">'+returnData[i].name+'</li>';
                                    html+='<li class="ulli" style="text-align: right;" ><div style="margin-right: 20px"onclick="addTab(\''+returnData[i].name+'\',\'<%=basePath%>'+returnData[i].url+'\',\'icon-nav\')"><a href="#" style="text-decoration:underline" title="查看信息">'+returnData[i].item+'人</a></div></li>';
                                    html+='</ul><hr/>';
                                }
                                $("#daiban").html(html);
                            }

                        }
                    })


                }

                function selectTBJB(){
                    $.ajax({
                        url: "<%=basePath%>/User/selectTBJB.do",
                        dataType:'json',
                        success: function (returnData) {
                            $('#tb1').highcharts({
                                chart: {
                                    plotBackgroundColor: null,
                                    plotBorderWidth: null,
                                    plotShadow: false,
                                    type: 'pie'
                                },
                                title: {
                                    text: '职称级别申报人数'
                                },
                                tooltip: {
                                    pointFormat: '{series.name}: <b>{point.y}人</b>'
                                },
                                plotOptions: {
                                    pie: {
                                        allowPointSelect: true,
                                        cursor: 'pointer',
                                        dataLabels: {
                                            enabled: true,
                                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                            style: {
                                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                            }
                                        }
                                    }
                                },
                                series: [{
                                    name: '申报人数',
                                    colorByPoint: true,
                                    data: returnData
                                }]
                            });
                        }
                    });
                }
                function loads(){
                    $.ajax({
                        url: "<%=basePath%>/User/selectTBXL.do",
                        dataType:'json',
                        success: function (returnData) {
                            $('#container').highcharts({
                                chart: {
                                    type: 'column'
                                },
                                title: {
                                    text: '系列申报人数'
                                },
                                xAxis: {
                                    categories:returnData.name ,
                                    crosshair: true
                                },
                                yAxis: {
                                    min: 0,
                                    title: {
                                        text: '申报人数'
                                    }
                                },
                                tooltip: {
                                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                    '<td style="padding:0"><b>{point.y} 人</b></td></tr>',
                                    footerFormat: '</table>',
                                    shared: true,
                                    useHTML: true
                                },
                                series: [{
                                    name: '系列',
                                    data: returnData.cou

                                }]
                            });
                        }
                    });
                }
                layui.use('element', function() {
                    var $ = layui.jquery
                        , element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
                })
            </script>
