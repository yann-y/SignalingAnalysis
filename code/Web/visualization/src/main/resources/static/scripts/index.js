var methods = new Array();

//map();
//map();
function heatmap(data) {
    console.log(data);
    var coords = new Array();
    for (var i = 0; i < data.length; i++) {
        coords[i] = [data[i].longitude, data[i].latitude, data[i].elevation];
    }
    var mapChart = echarts.init(document.getElementById('mapChart'));
    mapChart.setOption({
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
    /*
    mapChart.on('click', function (params) {
        $("#el-dialog").removeClass('hide');
        $("#reportTitle").html(params.value[2]);
    });
*/
    var bmap = mapChart.getModel().getComponent('bmap').getBMap()
    bmap.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP]}));
    bmap.setMapStyle({style: 'midnight'})
}

function map() {
    console.log("yes")
    init();
    //init2();
    $("#el-dialog").addClass("hide");
    $(".close").click(function (event) {
        $("#el-dialog").addClass("hide");
    });
/*
    var date = new Date();
    var numble = date.getDate();
    var today = getFormatMonth(new Date());
    $("#date1").html(today);
    $("#date2").html(today);
    $("#date3").html(today);
    $("#date4").html(today);

*/
    lay('.demo-input').each(function () {
        laydate.render({
            type: 'month',
            elem: this,
            trigger: 'click',
            theme: '#95d7fb',
            calendar: true,
            showBottom: true,
            done: function () {
                console.log($("#startDate").val())
            }
        })
    });
}

function init() {
    console.log(averData);
    /*添加代码*/
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
        averDistance[i] = Math.floor((distance / temp.length) * 100) / 100;
    }
    var averTime = new Array();

    for (var i = 0; i < averData.length; i++) {
        var temp = averData[i];
        var time = 0;
        for (var j = 0; j < temp.length; j++) {
            time += parseFloat(temp[j].totalTime);
        }
        averTime[i] = Math.floor((time / temp.length) * 100) / 100;
    }

    var averSpeed = new Array();

    for (var i = 0; i < averData.length; i++) {
        var temp = averData[i];
        var speed = 0;
        for (var j = 0; j < temp.length; j++) {
            speed += parseFloat(temp[j].averSpeed);
        }
        averSpeed[i] = Math.floor((speed / temp.length) * 100) / 100;
    }
    //出行方式
    var pieChart1 = echarts.init(document.getElementById('pieChart1'));
    pieChart1.setOption({
        color: ["#87cefa", "#ff7f50", "#32cd32", "#da70d6", "#00008b",],
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            y: '250',
            x: 'center',
            textStyle: {
                color: '#ffffff'

            },
            data: methods
        },
        axisLine: {
            lineStyle: {
                color: '#fff'
            }
        },
        series: [
            {
                name: '访问来源',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                data: methodsCount,
                emphasis: {
                    label: {
                        show: false,
                        position: 'center',
                        textStyle: {
                            fontSize: '20',
                            fontWeight: 'bold'
                        }
                    }
                }
            }
        ]
    });
    //添加代码

    //出行方式平均速度
    var lineChart = echarts.init(document.getElementById('lineChart'));
    lineChart.setOption({
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
                axisLabel: {
                    show: true,
                    textStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    lineStyle: {
                        color: '#fff'
                    }
                }
            }
        ],
        yAxis: [
            {
                name: 'km/h',
                type: 'value',
                axisLabel: {
                    show: true,
                    textStyle: {
                        color: '#fff'
                    }
                },
                nameTextStyle: {
                    color: '#fff'
                },
                axisLine: {
                    lineStyle: {
                        color: '#fff'
                    }
                }
            }
        ],
        series: [
            {
                name: '平均速度',
                type: 'bar',
                //barWidth: '60%',
                data: averSpeed
            }
        ]
    });
    /*

    */
//出行方式平均距离
    var histogramChart = echarts.init(document.getElementById('histogramChart'));
    histogramChart.setOption({
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
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff'
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#fff'
                }
            },
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: methods,
            axisLabel: {
                show: true,
                textStyle: {
                    color: '#fff'
                }
            },
            axisLine: {
                lineStyle: {
                    color: '#fff'
                }
            }

        },
        series: [
            {
                name: "2018-10-03",
                type: 'bar',
                data: averDistance
            }
        ]
    })
    /*

*/
    var lineChart2 = echarts.init(document.getElementById('lineChart2'));
    lineChart2.setOption({
        //backgroundColor: '#fff',
        grid: {
            top: '10%',
            right: '8%',
            left: '8%',
            bottom: '10%'
        },
        xAxis: [{
            name: '',
            type: 'category',
            color: '#59588D',
            data: methods,
            axisLabel: {
                margin: 20,
                color: '#999',
                textStyle: {
                    fontSize: 12
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
            max: 40,
            axisLine: {
                lineStyle: {
                    color: '#fff'
                }
            },
            axisTick: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: '#fff',
                    type: 'dashed',
                }
            }
        }],
        series: [{
            type: 'bar',
            data: averTime,
            //barWidth: '50px',
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
                fontSize: 12,
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

    });
}