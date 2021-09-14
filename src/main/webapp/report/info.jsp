<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表</div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-12">
                    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
                    <div id="monthChart" style="height:500px; width: 100%"></div>

                    <!-- 百度地图的容器 -->
                    <h3 align="center">用户地区分布图</h3>
                    <div id="baiduMap" style="height: 600px; width: 100%">

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 引入 ECharts 文件 -->
<script src="../statics/echarts/echarts.js"></script>

<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=odMf9zrQoudBkMiYG7c73lZwwIpoEgeu"></script>

<script type="text/javascript">
    /**
     * 通过月份查询云记数量
     */
    $.ajax({
       type:"get",
       url:"report",
       data:{
           actionName:"month"
       },
        success:function (result) {
            console.log(result);

            if(result.code == 1 ){

                var monthArray = result.result.monthArray;
                var dataArray = result.result.dataArray;
                //加载柱状图
                loadMonthChart(monthArray,dataArray);
            }
        }
    });


    function loadMonthChart(monthArray,dataArray) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('monthChart'));

        var dataAxis = monthArray;
        var data = dataArray;
        var yMax = 20;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }

        var option = {
            title: {
                text: '月份统计',
                subtext: '通过月份查询对应的云记数量',
                left:'center'
            },
            tooltip: {},
            //X轴
            xAxis: {
                data: dataAxis,
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
            },
            //Y轴
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            //系列
            series: [
                {
                    type: 'bar',
                    showBackground: true,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                {offset: 0, color: '#83bff6'},
                                {offset: 0.5, color: '#188df0'},
                                {offset: 1, color: '#188df0'}
                            ]
                        )
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                    data: data
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    $.ajax({
       type:"get",
       url:"report",
       data:{
           actionName:"location"
       },
        success:function (result) {
            console.log(result);
            if(result.code == 1){
               //
                loadBaiduMap(result.result);
            }
        }
    });

    function loadBaiduMap(markers) {

        //加载地图实例
        var map = new BMap.Map("baiduMap");
        //设置地图中心点
        var point = new BMap.Point(115.883254,28.746904);
        //地图初始化，同时设置地图展示级别
        map.centerAndZoom(point, 15);


        //添加地图类型控件
        map.addControl(new BMap.MapTypeControl({
            mapTypes:[
                BMAP_NORMAL_MAP,
                BMAP_HYBRID_MAP
            ]}));
        map.setCurrentCity("南昌");          // 设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

        //判断坐标
        if(markers != null && markers.length >0){
            map.centerAndZoom(new BMap.Point(markers[0].lon,markers[0].lat),10);

            for (var i = 1; i < markers.length; i++) {
                // 创建点标记
                var marker = new BMap.Marker(new BMap.Point(markers[i].lon,markers[i].lat));
                // 在地图上添加点标记
                map.addOverlay(marker);
            }
        }
    }
</script>