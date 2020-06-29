function heatmap(data) {
    console.log(data);
    var coords = new Array();
    for (var i = 0; i < data.length; i++) {
        coords[i] = [data[i].longitude, data[i].latitude, data[i].elevation];
    }
    var myChart = echarts.init(document.getElementById('mapChart'));
    myChart.setOption(option = {
        animation: false,
        bmap: {
            center: [123.43, 41.80],
            zoom: 13,
            roam: true
        },
        visualMap: {
            show: false,
            top: 'top',
            min: 0,
            max: 100,
            seriesIndex: 0,
            calculable: true,
            inRange: {
                color: ['blue', 'blue', 'green', 'yellow', 'red']
            }
        },
        series: [{
            type: 'heatmap',
            coordinateSystem: 'bmap',
            data: coords,
            pointSize: 8,
            blurSize: 10
        }]
    });
    // 添加百度地图插件
    var bmap = myChart.getModel().getComponent('bmap').getBMap();
    bmap.addControl(new BMap.MapTypeControl());
}