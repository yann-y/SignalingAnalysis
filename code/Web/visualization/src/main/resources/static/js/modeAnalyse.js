var methods = new Array();
console.log(averData);
map()
function map() {
    for (var i = 0; i < averData.length; i++) {
        methods[i] = averData[i][0].method;
    }
    var methodsCount = new Array();
    for (var i = 0; i < averData.length; i++) {
        var value =
            methodsCount[i] = {"value": averData[i].length, "name": averData[i][0].method};
    }
    var averDistance = new Array();
    for (var i = 0; i < averData.length; i++) {
        var temp = averData[i];
        var distance = 0;
        for (var j = 0; j < temp.length; j++) {
            distance += parseFloat(temp[j].totalDistance);
        }
        averDistance[i] = Math.floor((distance / temp.length)*100)/100;
    }
    var averTime = new Array();

    for (var i = 0; i < averData.length; i++) {
        var temp = averData[i];
        var time = 0;
        for (var j = 0; j < temp.length; j++) {
            time += parseFloat(temp[j].totalTime);
        }
        averTime[i] = Math.floor((time / temp.length)*100)/100;
    }

    var averSpeed = new Array();

    for (var i = 0; i < averData.length; i++) {
        var temp = averData[i];
        var speed = 0;
        for (var j = 0; j < temp.length; j++) {
            speed += parseFloat(temp[j].averSpeed);
        }
        averSpeed[i] = Math.floor((speed / temp.length)*100)/100;
    }
    var PieMap = echarts.init(document.getElementById('pieMap'));
    var PieOption = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: methods
        },
        series: [
            {
                name: '访问来源',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                data: methodsCount,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    PieMap.setOption(PieOption);
    window.addEventListener("resize", function () {
        PieMap.resize();
    });
    var BarMap = echarts.init(document.getElementById('BarMap'));
    var BarOption = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['2020年']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'value',
            name: 'km',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: methods,
        },
        series: [
            {
                name: "2018-10-03",
                type: 'bar',
                data: averDistance
            }
        ]
    };
    BarMap.setOption(BarOption);
    window.addEventListener("resize", function () {
        BarMap.resize();
    });


    var speedMap = echarts.init(document.getElementById('speedMap'));
    var speedOption = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                name: '方式',
                type: 'category',
                data: methods,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                name: 'km/h',
                type: 'value'
            }
        ],
        series: [
            {
                name: '平均速度',
                type: 'bar',
                barWidth: '60%',
                data: averSpeed
            }
        ]
    };
    speedMap.setOption(speedOption);
    window.addEventListener("resize", function () {
        speedMap.resize();
    });
    var timeMap = echarts.init(document.getElementById('timeMap'));
    var timeOption = {
        backgroundColor: '#fff',
        grid: {
            top: '9%',
            right: '8%',
            left: '8%',
            bottom: '8%'
        },
        xAxis: [{
            name: '方式',
            type: 'category',
            color: '#59588D',
            data: methods,
            axisLabel: {
                margin: 20,
                color: '#999',
                textStyle: {
                    fontSize: 18
                },
            },
            axisLine: {
                lineStyle: {
                    color: '#d2d2d2',
                }
            },
            axisTick: {
                show: false
            },
        }],
        yAxis: [{
            name: 'min',
            min: 0,
            max: 70,
            axisLine: {
                lineStyle: {
                    color: 'rgba(107,107,107,0.37)',
                }
            },
            axisTick: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: 'rgba(131,101,101,0.2)',
                    type: 'dashed',
                }
            }
        }],
        series: [{
            type: 'bar',
            data: averTime,
            barWidth: '50px',
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: '#FF9A22' // 0% 处的颜色
                    }, {
                        offset: 1,
                        color: '#FFD56E' // 100% 处的颜色
                    }], false),
                    barBorderRadius: [30, 30, 0, 0],
                }
            },
            label: {
                show: true,
                fontSize: 18,
                fontWeight: 'bold',
                position: 'top',
                color: 'blue',
                rich: {//使用富文本编辑字体样式
                    a: {
                        color: 'red'
                    }
                }
            }
        }]
    };
    timeMap.setOption(timeOption);
    window.addEventListener("resize", function () {
        timeMap.resize();
    });
}