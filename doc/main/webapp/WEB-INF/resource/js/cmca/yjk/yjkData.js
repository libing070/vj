// 审计结果-违规金额占比排名
function load_result_jezb_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        conShow = $("#contentShow1"),
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yjk/getColumnAmtData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            conShow.css('minWidth', data.nameList.length * 10 + '%');
            noDataX = -parseInt($('#contentShow1').width()) / 3;
            conShow.highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.nameList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                tooltip: {
                    useHTML: true,
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.20,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '违规金额占比排名：',
                    data: data.dataList,
                    color: "#3095f2"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    },
                    position: {
                        x: noDataX
                    }
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#contentShowWrap1").getNiceScroll(0).show();
            $("#contentShowWrap1").getNiceScroll(0).resize();
            $("#contentShowWrap1").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-违规数量占比排名
function load_result_slzb_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        conShow = $("#contentShow2"),
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yjk/getColumnNumData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            conShow.css('minWidth', data.nameList.length * 10 + '%');
            noDataX = -parseInt($('#contentShow1').width()) / 3;
            conShow.highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },

                xAxis: {
                    categories: data.nameList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.20,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '违规数量占比排名：',
                    data: data.dataList,
                    color: "#ff647f"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    },
                    position: {
                        x: noDataX
                    }
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#contentShowWrap2").getNiceScroll(0).show();
            $("#contentShowWrap2").getNiceScroll(0).resize();
            $("#contentShowWrap2").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-违规金额占比趋势
function load_result_jezb_qs_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        conShow = $("#contentShow3");
    $.ajax({
        url: "/cmca/yjk/getLineAmtData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            conShow.css('minWidth', data.trmList.length * 5 + '%');
            conShow.highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.trmList,
                    gridLineWidth: 1,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    },
                    useHTML: true
                },
                series: [{
                    name: '违规金额占比趋势',
                    data: data.dataList,
                    color: '#00c58e'
                }],
                lang: {
                    noData: "暂无数据" //无数据显示
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#contentShowWrap3").getNiceScroll(0).show();
            $("#contentShowWrap3").getNiceScroll(0).resize();
            $("#contentShowWrap3").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-违规数量占比趋势
function load_result_slzb_qs_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        conShow = $("#contentShow4");
    $.ajax({
        url: "/cmca/yjk/getLineNumData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            conShow.css('minWidth', data.trmList.length * 5 + '%');
            conShow.highcharts({
                chart: {
                    type: 'line',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.trmList,
                    gridLineWidth: 1, // 线条,
                    crosshair: true, // 十字线
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    plotLines: [{ // 线的的样式
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                tooltip: {
                    useHTML: true,
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    }
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.20,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '违规数量占比趋势',
                    data: data.dataList,
                    color: "#8885d5"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#contentShowWrap4").getNiceScroll(0).show();
            $("#contentShowWrap4").getNiceScroll(0).resize();
            $("#contentShowWrap4").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-图形
function load_result_chart() {
    load_result_jezb_pm_chart();
    load_result_slzb_pm_chart();
    load_result_jezb_qs_chart();
    load_result_slzb_qs_chart();
}

// 统计分析-统计报表-排名汇总 
function load_fenxi_pmhz_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#rankingAllTable').bootstrapTable('destroy');
    $('#rankingAllTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav1Con').height()) - 40,
        concern = $('#concern').val(),
        columnsData,
        columnsDatas = [{
            field: 'rn',
            title: '序号',
            valign: 'middle',
            sortable: true,
            formatter: rn
        }, {
            field: 'prvdName',
            // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
            title:'公司',
            sortable: true,
            valign: 'middle'
        }, {
            field: 'totalNum',
            title: 'CRM有价卡已使用状态总数量（张）',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatter0
        }, {
            field: 'infractionNum',
            title: '有价卡违规数量（张）',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatter0
        }, {
            field: 'numPercent',
            title: '有价卡违规数量占比',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatterB
        }, {
            field: 'tolAmt',
            title: 'CRM有价卡已使用状态总金额（元）',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatter2
        }, {
            field: 'infractionAmt',
            title: '有价卡违规金额（元）',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatterB
        }, {
            field: 'amtPercent',
            title: '有价卡违规金额占比',
            valign: 'middle',
            sortable: true,
            formatter: operateFormatterB
        }];
    // 表头跟随关注点的变化变化
    switch (concern) {
        case '1000': //汇总
            columnsData = [{
                field: 'rn',
                title: '序号',
                valign: 'middle',
                formatter: rn
            }, {
                field: 'prvdName',
                // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                title:'公司',
                valign: 'middle'
            }, {
                field: 'totalNum',
                title: '有价卡总数量（张）',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter0
            }, {
                field: 'infractionNum',
                title: '有价卡违规量（张）',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter0
            }, {
                field: 'numPercent',
                title: '有价卡违规数量占比',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatterB
            }, {
                field: 'errQtyLrr',
                title: '有价卡违规数量环比增幅',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatterB
            }, {
                field: 'tolAmt',
                title: '有价卡总金（元）',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter2
            }, {
                field: 'infractionAmt',
                title: '有价卡违规金额（元）',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter2
            }, {
                field: 'amtPercent',
                title: '有价卡违规金额占比',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatterB
            }, {
                field: 'errAmtLrr',
                title: '有价卡违规金额环比增幅',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatterB
            }];
            break;
        case '1001': //未按规定在系统间同步加载
            columnsData = columnsDatas;
            break;
        case '1002': //违规激活
            columnsData = columnsDatas;
            break;
        case '1003': //违规销售
            columnsData = columnsDatas;
            break;
        case '1004': //退换后的坏卡或报废卡未封锁
            columnsData = columnsDatas;
            break;
        case '1005': //违规重复使用
            columnsData = columnsDatas;
            break;
    }
    // 全国显示序号为0时，显示-
    function rn(value) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位格式化，保留两位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 千分位格式化，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/yjk/getYjkPmhz",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#rankingAllTable").bootstrapTable({
                    datatype: "local",
                    data: data, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: columnsData
                });
                $('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
                // $('#rankingAllTable thead').remove();
                scroll('#rankingAllTableWrap', '#rankingAllTable');
            }
        }
    });
}

// 统计分析-统计报表-增量分析
function load_fenxi_zlfx_chart() {
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        lastDataName,
        lastDataY,
        newLastData,
        dataZlfx,
        waterfallH = $('#pubuChart').closest('.pubu_show').height() - 40;
    $.ajax({
        url: "/cmca/yjk/getIncrementalData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 操作返回数据
            $('#pubuChart').css('minWidth', data.length * 10 + '%');
            dataZlfx = data;
            if (data.length > 0 && data[0].y !== null) {
                // 最后一列全网环比增量，需要设置颜色属性，故修改返回数据
                lastDataName = data[data.length - 1].name;
                lastDataY = data[data.length - 1].y;
                newLastData = {
                    name: lastDataName,
                    y: lastDataY,
                    color: "#66CD00"
                };
                dataZlfx.pop();
                dataZlfx.unshift(newLastData);
            } else if (data.length > 0 && data[0].y === null) {
                dataZlfx = [];
            }
            // 绘制图形
            $('#pubuChart').highcharts({
                chart: {
                    type: 'waterfall',
                    backgroundColor: 'rgba(0,0,0,0)',
                    height: waterfallH
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '有价卡违规金额增量分析'
                },
                xAxis: {
                    type: 'category',
                    labels: {
                        style: {
                            fontSize: $.xFontSize(),
                            color: "#ccc"
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    }
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span>' + '</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 元</b>';
                    }
                },
                plotOptions: {
                    series: {
                        pointWidth: $.pubuPointW()
                    }
                },
                series: [{
                    //name: "￥",
                    upColor: "#ff647f",
                    color: "#3095f2",
                    data: dataZlfx,
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return Highcharts.numberFormat(this.y, 0, '.', ',');
                        },
                        style: {
                            color: '#FFFFFF',
                            fontWeight: 'bold',
                            fontSize: "13px",
                            textshadow: 'none'
                        }
                    },
                    pointPadding: 0
                }],
                legend: {
                    enabled: false
                },
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: { //对无数据标签的CSS样式。 默认值：[object Object].
                        color: '#c2c2c2'
                    }
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
            // “操作”这一列用来生成按钮的 动态生成的按钮 ，下面是动态生成按钮代码
            //获取表格的某一行的属性
            function operateFormatter(value, row, index) {
                return $.formatMoney(value, 2);
            }
        }
    });
}

// 统计分析-统计报表-违规类型分布
function load_fenxi_wglxfb_chart() {
    // 样式
    $('#pieAudTrm').text($('#chooseTime').val());
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val()
        },
        areaH = $('#zhanbiQushiChartWrap').height() - 30,
        dataPie = [], //新建数组，接受饼图数据
        dataArea = []; //新建数据，接受堆积图数据
    // 占比饼图
    $.ajax({
        url: "/cmca/yjk/getAmountPie",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataPie = [{
                        name: '单边加载',
                        y: data['1001'],
                        color: '#7CB5EC'
                    },
                    {
                        name: '违规激活',
                        y: data['1002'],
                        color: '#90ED7D'
                    },
                    {
                        name: '违规销售',
                        y: data['1003'],
                        color: '#FF9933'
                    },
                    {
                        name: '未封锁',
                        y: data['1004'],
                        color: '#8085E9'
                    },
                    {
                        name: '重复使用',
                        y: data['1005'],
                        color: '#FF99CC'
                    }
                ];
            }
            $('#zhanbiBingtuChart').highcharts({
                chart: {
                    type: 'pie',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                tooltip: {
                    formatter: function () {
                        return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + '：</span><span>' + this.point.percentage.toFixed(2) + '%（' + Highcharts.numberFormat(this.point.y, 2, '.', ',') + ' 万张）</span>';
                    },
                    useHTML: true
                },
                plotOptions: {
                    pie: {
                        dataLabels: {
                            enabled: true,
                            useHTML: true,
                            formatter: function () {
                                return '<span style="color: #fff;font-weight:lighter;white-space:pre-line;" >' + this.point.name + '：' + this.point.percentage.toFixed(2) + '%</span>';
                            },
                            style: {
                                // width: '20px',
                                fontFamily: 'Microsoft YaHei',
                                color: "#fff",
                                fontSize: "10px",
                                textOutline: '0px 0px #fff'
                            },
                            distance: 5
                        },
                        slicedOffset: 5,
                        allowPointSelect: true,
                        borderColor: null,
                        showInLegend: true
                    }
                },
                legend: {
                    itemStyle: {
                        color: '#fff',
                        fontWeight: 'normal'
                    }
                },
                series: [{
                    data: dataPie
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
        }
    });

    // 占比趋势图
    $.ajax({
        url: "/cmca/yjk/getPerTrend",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataArea = [{
                        name: '单边加载',
                        data: data.list1001,
                        color: '#7CB5EC'
                    },
                    {
                        name: '违规激活',
                        data: data.list1002,
                        color: '#90ED7D'
                    },
                    {
                        name: '违规销售',
                        data: data.list1003,
                        color: '#FF9933'
                    },
                    {
                        name: '未封锁',
                        data: data.list1004,
                        color: '#8085E9'
                    },
                    {
                        name: '重复使用',
                        data: data.list1005,
                        color: '#FF99CC'
                    }
                ];
            }
            $('#zhanbiQushiChart').css('minWidth', data.listAudTrm.length * 18 + '%');
            $('#zhanbiQushiChart').highcharts({
                chart: {
                    type: 'area',
                    backgroundColor: 'rgba(0,0,0,0)',
                    height: areaH
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.listAudTrm,
                    tickmarkPlacement: 'on',
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        },
                    }
                },
                yAxis: {
                    title: null,
                    labels: {
                        formatter: function () {
                            return this.value / 1 + "%";
                        }
                    }
                },
                tooltip: {
                    formatter: function () {
                        var s = '<span>' + this.x + '</span>';
                        $.each(this.points, function () {
                            s += '<br/>' + '<span style="fontWeight:400;color:' + this.series.color + '">' + this.series.name + '</span>：' + Highcharts.numberFormat(this.percentage, 2, '.', ',') + '% （' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万张）';
                        });
                        return s;
                    },
                    shared: true
                },
                plotOptions: {
                    area: {
                        stacking: 'percent',
                        lineColor: '#fff',
                        lineWidth: 1,
                        marker: {
                            lineWidth: 1,
                            lineColor: '#fff'
                        }
                    }
                },
                legend: {
                    itemStyle: {
                        color: '#fff',
                        fontWeight: 'normal'
                    }
                },
                series: dataArea,
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
            $("#zhanbiQushiChartWrap").getNiceScroll(0).show();
            $("#zhanbiQushiChartWrap").getNiceScroll(0).resize();
            $("#zhanbiQushiChartWrap").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 统计分析-审计整改问责-整改问责要求
function load_fenxi_zgwz_require_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#wenzeTable').bootstrapTable('destroy');
    $('#wenzeTable').bootstrapTable('resetView');
    $("#wenzeTableTitle").html('有价卡管理违规整改问责表');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: 1000,
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav2FiveNav1Con').height()) - 30;
    $.ajax({
        url: "/cmca/yjk/getYJKZgwzData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 由于后台并未返回“编号”列数据，在此自定义添加该列数据
            $.each(data, function (idx, dataObj) {
                dataObj['order'] = idx + 1;
            });
            // 绘制表格
            $("#wenzeTable").bootstrapTable({
                datatype: "local",
                data: data,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'errQty',
                columns: [{
                    field: 'order',
                    title: '编号',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'prvdName',
                    title: '公司',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'wtSummary',
                    title: '问题概要',
                    align: 'center',
                    valign: 'middle',
                    width: '16%'
                }, {
                    field: 'wtDetails',
                    title: '问题详细描述',
                    align: 'center',
                    valign: 'middle',
                    width: '16%'
                }, {
                    field: 'zgOpinion',
                    title: '整改建议',
                    align: 'center',
                    valign: 'middle',
                    width: '16%'
                }, {
                    field: 'wzRequire',
                    title: '问责要求',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'zgsj',
                    title: '整改时间',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'zgqx',
                    title: '整改期限',
                    align: 'center',
                    valign: 'middle'
                }]
            });
            $('#wenzeTable').parent('.fixed-table-body').attr('id', 'wenzeTableWrap');
            // $('#wenzeTable thead').remove();
            scroll('#wenzeTableWrap', '#wenzeTable');
        }
    });
}

// 统计分析-审计整改问责-整改问责统计
function load_fenxi_zgwz_statis_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 六个月整改达标排名
    $.ajax({
        url: "/cmca/yjk/getZGWZColumn1",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#stageAccOrder').css('minWidth', data.nameList.length * 10 + '%');
            $('#stageAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.nameList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0) + ' 次</b>';
                    },
                    useHTML: true
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    },
                    allowDecimals: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.20,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '整改次数',
                    data: data.dataList,
                    color: "#3095f2"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#stageAccOrderWrap").getNiceScroll(0).show();
            $("#stageAccOrderWrap").getNiceScroll(0).resize();
            $("#stageAccOrderWrap").getNiceScroll(0).doScrollLeft(0);
        }
    });

    // 累计达到整改次数排名
    $.ajax({
        url: "/cmca/yjk/getZGWZColumn2",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#addUpAccOrder').css('minWidth', data.nameList.length * 10 + '%');
            $('#addUpAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },

                xAxis: {
                    categories: data.nameList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    },
                    allowDecimals: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0) + ' 次</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.20,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '整改次数',
                    data: data.dataList,
                    color: "#ff647f"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#addUpAccOrderWrap").getNiceScroll(0).show();
            $("#addUpAccOrderWrap").getNiceScroll(0).resize();
            $("#addUpAccOrderWrap").getNiceScroll(0).doScrollLeft(0);
        }
    });

    // 达到整改标准省公司数量趋势
    $.ajax({
        url: "/cmca/yjk/getZGWZLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#accTrend').css('minWidth', data.trmList.length * 5 + '%');
            $('#accTrend').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.trmList,
                    gridLineWidth: 1,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    },
                    allowDecimals: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0) + ' 个</b>';
                    },
                    useHTML: true
                },
                series: [{
                    name: '整改公司数量',
                    data: data.dataList,
                    color: '#00c58e'
                }],
                lang: {
                    noData: "暂无数据" //无数据显示
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
            $("#accTrendWrap").getNiceScroll(0).show();
            $("#accTrendWrap").getNiceScroll(0).resize();
            $("#accTrendWrap").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 统计分析-审计报告-审计结果摘要 
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav3Con').find('.audtrm_off,.audtrm_on').hide();
    $('#baogaoTop5').bootstrapTable('destroy');
    $('#baogaoTop5').bootstrapTable('resetView');
    // 定义所需变量
    var myData,
        concernName,
        auditDates = auditDate(), //审计时间
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            time: new Date().getTime()
        },
        h = parseInt($('#baogaoTop5Con').height()),
        NumMom3000, AmtMom3000, momText1, momText2, //文字说明所需数据
        tableData = [], //定义数组接收表格数据
        time = $("#audTrm").val(),
        auditMon = monthSub(time, 0, 1),
        auditStartMon = monthSub(time, 0, -1),
        year = parseInt(time.substring(0, 4)),
        month = parseInt(time.substring(4)),
        new_date = new Date(year, month, 1),
        day = (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate(); //获取当前月的天数
    // 填充表格上方文字说明数据
    $('#sjbgPrvdName').text($('#prvdId').val() == '10000' ? $('#prvdNameZH').val() : $('#prvdNameZH').val() + '公司'); //被审计公司
    $('#sjbgAudTrm').text(auditDates[2]); //审计时间
    $('#sjbgPeriod').text(auditDates[0] + ' -- ' + auditDates[1]); //审计期间

    // 数据列千分位格式化，保留两位小数
    function operateFormatter(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }

    // 数据列千分位格式化，不保留小数
    function operateFormatterhu(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }

    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/yjk/getAuditReport",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (data.switchState != 'audTrmColseForReport') {
                $('#fenxiFourNav3Con .audtrm_on').show();
                if (JSON.stringify(data) != "{}" && data["1000"] != undefined) {
                    // 返回数据
                    myData = [
                        data["1001"],
                        data["1002"],
                        data["1003"],
                        data["1004"],
                        data["1005"],
                        data["1000"]
                    ];

                    // 判断增加的值为正还是为负，如果为正，文字显示内容为正，如果为负，显示为减少
                    NumMom3000 = parseInt(myData[5].numPercentMom);
                    AmtMom3000 = parseInt(myData[5].amtPercentMom);
                    momText1 = (NumMom3000 > 0) ? "上升" : "下降";
                    momText2 = (AmtMom3000 > 0) ? "上升" : "下降";

                    // 审计发现文字说明
                    $('#baogaoInfo').html('全网' + $('#chooseTime').val() +
                        '有价卡各环节状态发生变更的累计' + $.formatMoney(myData[5].totalNumTmp, 2) + '万张，' +
                        '金额' + $.formatMoney(myData[5].tolAmt, 2) + '万元，' +
                        '审计识别出违规数量' + $.formatMoney(myData[5].infractionNumTmp, 2) + '万张，' +
                        '占比' + $.formatMoney(myData[5].numPercent, 2) + '%,' +
                        '比上月' + momText1 + $.formatMoney(Math.abs(NumMom3000), 2) + '个百分点；' +
                        '违规金额' + $.formatMoney(myData[5].infractionAmt, 2) + '万元；' +
                        '占比' + $.formatMoney(myData[5].amtPercent, 2) + '%，' +
                        '比上月' + momText2 + $.formatMoney(Math.abs(AmtMom3000), 2) + '个百分点。'
                    );

                    // 审计报告表格部分
                    // 判断关注点下是否有数据
                    $.each(myData, function (idx) {
                        if (myData[idx] !== undefined) {
                            tableData.push(myData[idx]);
                        }
                        return tableData;
                    });

                    // 绘制表格
                    $("#baogaoTop5").bootstrapTable({
                        datatype: "local",
                        data: tableData,
                        cache: false,
                        pagination: false, //是否显示分页
                        height: h,
                        columns: [{
                            field: 'concernName',
                            title: '关注点',
                            align: 'center',
                            valign: 'middle',
                        }, {
                            field: 'infractionNumTmp',
                            title: '有价卡违规数量（万张）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter
                        }, {
                            field: 'numPercent',
                            title: '违规占比',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatterB
                        }, {
                            field: 'infractionAmt',
                            title: '违规金额（万元）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter
                        }, {
                            field: 'amtPercent',
                            title: '违规金额占比',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatterB
                        }]
                    });
                    // 为表格设置滚动条
                    $('#baogaoTop5').parent('.fixed-table-body').attr('id', 'baogaoTop5Wrap');
                    // $('#baogaoTop5 thead').remove();
                    scroll('#baogaoTop5Wrap', '#baogaoTop5');
                } else {
                    $('#baogaoTop5Title').html('');
                    $('#baogaoInfo').html('');
                    $("#baogaoTop5").html('');
                }
            } else {
                $('#fenxiFourNav3Con .audtrm_off').show();
                $('#fenxiFourNav3Con .audtrm_on').hide();
            }
        }
    });
}

// 审计报告下载 
function down_sjbg_file() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        subjectId: $('#subjectId').val(),
        fileType: "audReport"
    };
    $.ajax({
        url: '/cmca/pmhz/downFilePage',
        dataType: "text",
        type: 'GET',
        data: postData,
        async: false,
        cache: false,
        success: function (data) {
            if (data == "empty") {
                alert('没有生成文件');
            } else if (data == "error") {
                alert('下载文件报错');
            } else {
                window.open('/cmca/pmhz/downFilePage?audTrm=' + $('#audTrm').val() + '&subjectId=' + $('#subjectId').val() + '&prvdId=' + $('#prvdId').val() + '&fileType=audReport', "_blank");
            }
        }
    });
    /*   var prvdId = $('#prvdId').val(),
          audTrm = $('#audTrm').val(),
          subjectId = $('#subjectId').val();
      window.open('/cmca/pmhz/downFilePage?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&fileType=audReport'); */
}

// 审计清单下载
// function down_sjqd_file() {
//     var postData = {
//         prvdId: $('#prvdId').val(),
//         audTrm: $('#audTrm').val(),
//         subjectId: $('#subjectId').val(),
//         fileType: "audDetail"
//     };
//     $.ajax({
//         url: '/cmca/pmhz/downFilePage',
//         dataType: "text",
//         type: 'GET',
//         data: postData,
//         async: false,
//         cache: false,
//         success: function (data) {
//             if (data == "empty") {
//                 alert('没有生成文件');
//             } else if (data == "error") {
//                 alert('下载文件报错');
//             } else {
//                 window.open('/cmca/pmhz/downFilePage?audTrm=' + $('#audTrm').val() + '&subjectId=' + $('#subjectId').val() + '&prvdId=' + $('#prvdId').val() + '&fileType=audDetail', "_blank");
//             }
//         }
//     });
//     /*  var prvdId = $('#prvdId').val(),
//          audTrm = $('#audTrm').val(),
//          subjectId = $('#subjectId').val();
//      window.open('/cmca/pmhz/downFilePage?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&fileType=audDetail'); */
// }

// VC和CRM系统间数据比对图
function load_lifeCycle_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    // 左下生命周期条形图
    $.ajax({
        url: "/cmca/yjk/getCrmVsVcData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#lifeChart').highcharts({
                chart: {
                    type: 'bar',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.yjk_stat,
                    labels: {
                        rotation: 0 //调节倾斜角度偏移
                    },
                    style: {
                        textOverflow: 'none'
                    }
                },
                yAxis: {
                    min: 0,
                    max: 100,
                    title: {
                        text: null
                    },
                    labels: {
                        format: '{value}%',
                    }
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.percentage, 0) + '%</b>';
                    },
                    useHTML: true
                },
                plotOptions: {
                    series: {
                        stacking: 'percent',
                    }
                },
                series: [{
                    name: 'CRM存在但VC不存在',
                    data: data.crm_num
                }, {
                    name: 'CRM和VC均存在',
                    data: data.all_num
                }, {
                    name: 'VC存在但CRM不存在',
                    data: data.vc_num
                }],
                lang: {
                    noData: "暂无数据" //无数据显示的文本
                },
                legend: {
                    enabled: false,
                },
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
        }
    });

}

// 有价卡生命周期图左下方和下钻地图下方卡片数据展示
function load_leftBtm_card_chart() {
    // 定义所需变量
    var prvdId = $('#prvdId').val(),
        infractionAmt = 0, // 违规金额（）
        infractionAmtMom = 0, // 违规金额增减
        amtPercent = 0, // 违规金额占比（%）
        amtPercentMom = 0, // 违规金额占比增减
        infractionNum = 0, // 违规数量（张）
        infractionNumMom = 0, // 违规数量增减
        numPercent = 0, // 违规数量占比（%）
        numPercentMom = 0, // 违规数量占比增减
        // 后台请求参数
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    $.ajax({
        url: "/cmca/yjk/getMapBottomData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 变量赋值
            if (JSON.stringify(data) != "{}") {
                infractionAmt = $.formatMoney(data[prvdId].infractionAmt, 2);
                infractionAmtMom = data[prvdId].infractionAmtMom;
                amtPercent = $.formatMoney(data[prvdId].amtPercent, 2);
                amtPercentMom = data[prvdId].amtPercentMom;
                infractionNum = $.formatMoney(data[prvdId].infractionNum, 0);
                infractionNumMom = data[prvdId].infractionNumMom;
                numPercent = $.formatMoney(data[prvdId].numPercent, 2);
                numPercentMom = data[prvdId].numPercentMom;
            }
            // 数据渲染
            $('#infractionAmt').text(infractionAmt); // 违规金额
            $('#amtPercent').text(amtPercent + '%'); // 金额占比
            $('#infractionNum').text(infractionNum); // 违规数量
            $('#numPercent').text(numPercent + '%'); // 违规数量占比
            // 违规金额增减
            if (infractionAmtMom <= 0 || infractionAmtMom === null) {
                // c00c58e 蓝色下降 glyphicon-triangle-bottom 下降小图标
                // cbd4343 红色上升 glyphicon-triangle-top 上升小图标
                // 生命周期左下方数据展示
                $("#infractionAmtMom").html('<b class="fl c00c58e">' + $.formatMoney(Math.abs(infractionAmtMom), 2) + '</b>' + '<span class="glyphicon glyphicon-triangle-bottom text-right c00c58e fr"></span>');
                // 地图下方卡片数据展示
                $('#card1').html(infractionAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionAmtMom), 2) + '</span></dt>');
            } else {
                // 生命周期左下方数据展示
                $("#infractionAmtMom").html('<b class="fl cbd4343">' + $.formatMoney(infractionAmtMom, 2) + '</b>' + '<span class="glyphicon glyphicon-triangle-top text-right cbd4343 fr"></span>');
                // 地图下方卡片数据展示
                $('#card1').html(infractionAmt + '<span><i class="caret_up"></i>' + $.formatMoney(infractionAmtMom, 2) + '</span></dt>');
            }
            // 违规金额占比增减
            if (amtPercentMom <= 0 || amtPercentMom === null) {
                $("#amtPercentMom").html('<b class="fl c00c58e">' + $.formatMoney(Math.abs(amtPercentMom), 2) + '%' + '</b>' + '<span class="glyphicon glyphicon-triangle-bottom text-right c00c58e fr"></span>');
                $('#card2').html(amtPercent + '%' + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(amtPercentMom), 2) + '' + '%</span></dt>');
            } else {
                $("#amtPercentMom").html('<b class="fl cbd4343">' + $.formatMoney(amtPercentMom, 2) + '%' + '</b>' + '<span class="glyphicon glyphicon-triangle-top text-right cbd4343 fr"></span>');
                $('#card2').html(amtPercent + '%' + '<span><i class="caret_up"></i>' + $.formatMoney(amtPercentMom, 2) + '' + '%</span></dt>');
            }
            // 违规数量增减
            if (infractionNumMom <= 0 || infractionNumMom === null) {
                $("#infractionNumMom").html('<b class="fl c00c58e">' + $.formatMoney(Math.abs(infractionNumMom), 0) + '</b>' + '<span class="glyphicon glyphicon-triangle-bottom text-right c00c58e fr"></span>');
                $('#card3').html(infractionNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionNumMom), 0) + '</span></dt>');
            } else {
                $("#infractionNumMom").html('<b class="fl cbd4343">' + $.formatMoney(Math.abs(infractionNumMom), 0) + '</b>' + '<span class="glyphicon glyphicon-triangle-top text-right cbd4343 fr"></span>');
                $('#card3').html(infractionNum + '<span><i class="caret_up"></i>' + $.formatMoney(Math.abs(infractionNumMom), 0) + '</span></dt>');
            }
            // 违规数量占比增减
            if (numPercentMom <= 0 || numPercentMom === null) {
                $("#numPercentMom").html('<b class="fl c00c58e">' + $.formatMoney(Math.abs(numPercentMom), 2) + '%' + '</b>' + '<span class="glyphicon glyphicon-triangle-bottom text-right c00c58e fr"></span>');
                $('#card4').html(numPercent + '%' + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(numPercentMom), 2) + '' + '%</span></dt>');
            } else {
                $("#numPercentMom").html('<b class="fl cbd4343">' + $.formatMoney(numPercentMom, 2) + '%' + '</b>' + '<span class="glyphicon glyphicon-triangle-top text-right cbd4343 fr"></span>');
                $('#card4').html(numPercent + '%' + '<span><i class="caret_up"></i>' + $.formatMoney(numPercentMom, 2) + '' + '%</span></dt>');
            }
        }
    });
}

/**
 * 绘制地图
 * 地图数据$.chinaMapData(),位于cmca-common.js中
 * 说明：根据order值来判断排序名次，进而修改地图数据中的value参数值，来渲染省市排名的地图颜色
 * 注：在绘制地图前，通过修改隐藏域中target的val值，来修改相应的指标，绘制不同指标下的地图，此值不用向后台传递
 * 参数说明：
 * prvdNameZH：隐藏域省公司名称汉字，用来判断是否是直辖市，如果是直辖市，则省下钻后悬浮显示统一为直辖市名，而不是直辖市下面的区域名
 * orderName：指标val值（根据哪个指标来渲染排名）
 * mapData：省地图数据，值为$.chinaMapData(),位于cmca-common.js中，可以查看参数
 * map：绘制地图对象
 * unDrilldown：不能下钻的区域
 * prvdCode:省代码（存在地图数据中）
 * order：排序序号，根据此参数渲染颜色，前3名红色，4-10名黄色，10名以后绿色
 * prvdId：下钻省ID
 * ctyCode：地市代码（存在地图数据中）
 * ctyMap：省各地市地图数据
 * nm：鼠标悬浮判断是否是港澳台
 * cnm：鼠标悬浮显示的省/地市名称
 * dt：鼠标悬浮显示的数据
 * postData：向后台发送的请求
 * 调用方法drawChinaMap()，drawProvMap()
 */

// 绘制全国地图
function drawChinaMap() {
    // 样式
    $("#ProvBackBtn").hide(); // 返回按钮隐藏
    $('#nanhaiQundao').show(); // 显示南海诸岛
    // 定义所需变量
    var prvdNameZH,
        orderName = $('#target').val(),
        mapData = $.chinaMapData(),
        map = null,
        unDrilldown = ['taiwan', 'xianggang', 'aomen'],
        prvdCode,
        prvdId,
        order,
        ctyCode,
        ctyMap,
        cnm,
        nm,
        dt,
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    // 请求全国各省数据
    $.ajax({
        url: "/cmca/yjk/getMapData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 地图颜色
            if (JSON.stringify(data) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                $.each(mapData, function (idex, map) { //each $.chinaMapData()地图数据,map即为数据对象
                    if (map.name != 'Macau' && map.name != 'HongKong' && map.name != 'Taiwan') { //香港，澳门，台湾三省无数据
                        // map.value：2是红色，5是黄色，大于等于8是绿色，0是灰色。
                        map.value = 8;
                        prvdCode = map.properties.id;
                        //后台返回的数据中，可能会出现某个省没有数据的情况，导致数据中直接没有该省数据对象，所以在此判断，如果没有数据，即data[prvdCode]值为undefined，则直接将该省的value值置为0，没有数据，如果不做此判断，在循环省数据的时候，没有某个省的数据会导致报错，而无法下钻
                        if (data[prvdCode] !== undefined) {
                            order = data[prvdCode].rn; // data.rn，排名
                            map.properties.trueValue = data[prvdCode].amtPercent;
                            if (order <= 3 && order > 0) { //全国前三名
                                map.value = 2;
                            }
                            if (order >= 4 && order <= 10) { //全国4-10名
                                map.value = 5;
                            }
                            if (order <= 0) {
                                map.value = 0;
                            }
                        } else {
                            map.value = 0;
                        }
                    } else { //无数据
                        map.value = 0;
                    }
                });
            }
            // 绘制地图
            map = new Highcharts.Map('chinaMap', {
                lang: {
                    drillUpText: '<span style="color:#f8a900;">返回</span>'
                },
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)',
                    events: {
                        // 下钻到地市
                        drilldown: function (e) {
                            prvdId = e.point.properties.id; // 通过点击对象e取到需要的属性
                            // 异步下钻
                            if (e.point.drilldown && unDrilldown.indexOf(e.point.drilldown) === -1) {
                                // 插入一经事件码-查询
                                dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                                // 日志记录
                                get_userBehavior_log('专题', '有价卡管理违规', '地图下钻', '查询');

                                // 请求省地图数据
                                $.ajax({
                                    url: "/cmca/resource/plugins/highcharts/maps/" + prvdId + ".geo.json",
                                    type: "GET",
                                    dataType: "json",
                                    success: function (data) {
                                        // 解析地市地图数据
                                        ctyMap = Highcharts.geojson(data);
                                        // 样式
                                        $('#chooseProvince').val(e.point.properties.cnname);
                                        mapTitle();
                                        $('#nanhaiQundao').hide();
                                        // 改变隐藏域val
                                        $('#prvdId').val(prvdId); // 改变省ID
                                        $('#prvdName').val(e.point.drilldown); // 改变省拼音名
                                        $('#prvdNameZH').val(e.point.properties.cnname); // 改变省中文名
                                        // 加载底部卡片数据
                                        load_leftBtm_card_chart();
                                        // 右侧审计结果图形联动
                                        if ($('#resultTabCon').is(':visible')) {
                                            // 右侧审计结果图形联动
                                            load_result_chart();
                                        } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
                                            // 右侧排名汇总表格联动
                                            load_fenxi_pmhz_table();
                                        }
                                        // 更新发送后台参数
                                        postData = {
                                            prvdId: $('#prvdId').val(),
                                            audTrm: $('#audTrm').val(),
                                            concern: $('#concern').val()
                                        };
                                        // 请求下钻省数据
                                        $.ajax({
                                            url: "/cmca/yjk/getMapData",
                                            type: 'get',
                                            dataType: 'json',
                                            data: postData,
                                            success: function (ctyData) {
                                                // 地图颜色
                                                if (JSON.stringify(ctyData) != "{}") { // 判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                                                    Highcharts.each(ctyMap, function (map) {
                                                        map.value = 8; // 显示正常色
                                                        ctyCode = map.properties.code;
                                                        // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据
                                                        // 如果是直辖市，则整个直辖市区域所有区县都显示为红色
                                                        if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                            map.value = 2;
                                                            map.properties.trueValue = e.point.properties.trueValue;
                                                        } else { // 不是直辖市
                                                            //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                                            if (ctyData[ctyCode] !== undefined) {
                                                                order = ctyData[ctyCode].rn; // 通过rn查看排名情况
                                                                map.properties.trueValue = ctyData[ctyCode].amtPercent;
                                                                if (order <= 3 && order > 0) { //地市前三名
                                                                    map.value = 2;
                                                                }
                                                                if (order >= 4 && order <= 10) { //地市4-10名
                                                                    map.value = 5;
                                                                }
                                                                if (order <= 0) {
                                                                    map.value = 0;
                                                                }
                                                            } else {
                                                                map.value = 0;
                                                                map.properties.trueValue = 0;
                                                            }
                                                        }
                                                    });
                                                }
                                                map.addSeriesAsDrilldown(e.point, {
                                                    name: e.point.name,
                                                    data: ctyMap,
                                                    dataLabels: {
                                                        enabled: true,
                                                        allowOverlap: true,
                                                        color: '#3c3e3f',
                                                        style: {
                                                            "fontSize": $.xFontSize(),
                                                            "fontWeight": "lighter",
                                                            "textOutline": "0px 0px #3c3e3f"
                                                        },
                                                        formatter: function () {
                                                            // 地图文字重叠显示特殊处理
                                                            var ctyName = this.point.name;
                                                            // switch (ctyName) {
                                                            //     case "西城区":
                                                            //         this.point.plotY -= 10;
                                                            //         break;
                                                            //     case "东城区":
                                                            //         this.point.plotY += 10;
                                                            //         break;
                                                            //     case "安阳":
                                                            //         this.point.plotY -= 20;
                                                            //         break;
                                                            //     case "塔城":
                                                            //         this.point.plotY -= 25;
                                                            //         break;
                                                            //     case "益阳":
                                                            //         this.point.plotY += 35;
                                                            //         break;
                                                            //     case "鹤岗":
                                                            //         this.point.plotY += 12;
                                                            //         break;
                                                            //     case "无锡":
                                                            //         this.point.plotY += 27;
                                                            //         break;
                                                            //     case "上饶":
                                                            //         this.point.plotX += 35;
                                                            //         this.point.plotY += 12;
                                                            //         break;
                                                            //     case "泸州":
                                                            //         this.point.plotX -= 23;
                                                            //         this.point.plotY -= 12;
                                                            //         break;
                                                            //     case "百色":
                                                            //         this.point.plotX += 35;
                                                            //         this.point.plotY += 12;
                                                            //         break;
                                                            //     case "辽阳":
                                                            //         this.point.plotX += 27;
                                                            //         this.point.plotY += 28;
                                                            //         break;
                                                            // }
                                                            return this.point.name;
                                                        }
                                                    },
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        drillup: function () { // 返回
                            // 插入一经事件码-查询
                            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                            // 样式
                            $('#nanhaiQundao').show();
                            $('#chooseProvince').val('全公司');
                            mapTitle();
                            // 返回按钮隐藏
                            $('#ProvBackBtn').hide();
                            // 改变隐藏域val
                            $('#prvdId').val(10000);
                            $('#prvdName').val('');
                            $('#prvdNameZH').val('全公司');
                            // 地图下方卡片联动
                            load_leftBtm_card_chart();
                            if ($('#resultTabCon').is(':visible')) {
                                // 右侧审计结果图形联动
                                load_result_chart();
                            } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
                                // 右侧排名汇总表格联动
                                load_fenxi_pmhz_table();
                            }
                        }
                    }
                },
                title: {
                    text: null
                },
                mapNavigation: {
                    enabled: true,
                    buttonOptions: {
                        verticalAlign: 'bottom',
                        theme: {
                            fill: 'rgba(248,169,0,.2)',
                            'stroke-width': 0,
                            stroke: 'none',
                            borderColor: 'rgba(248,169,0,.2)',
                            r: 6,
                            states: {
                                hover: {
                                    fill: 'rgba(248,169,0,.2)'
                                },
                                select: {
                                    stroke: '#039',
                                    fill: 'rgba(248,169,0,.2)'
                                }
                            }
                        },
                        style: {
                            color: '#f8a900'
                        }
                    }
                },
                legend: {
                    align: 'center',
                    verticalAlign: 'bottom',
                    itemStyle: {
                        "fontSize": $.xFontSize()
                    }
                },
                tooltip: {
                    animation: false,
                    formatter: function () {
                        // 鼠标悬浮地图上方显示数据参数
                        cnm = this.point.properties.cnname; // 全国地图下，未下钻到省的时候，该值为省公司的名称
                        nm = this.point.name; // 省公司拼音
                        dt = this.point.properties.trueValue; // 数据
                        prvdNameZH = $('#prvdNameZH').val(); // 存在在隐藏域里面的省中文名
                        if ($('#prvdId').val() != 10000) { // 如果下钻到省后
                            cnm = this.point.properties.name; // 省公司地图下，下钻到省的时候，该值为地市公司的名称
                        }
                        if (nm == "Taiwan" || nm == "HongKong" || nm == "Macau" || dt == -1 || dt == null) {
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/>没有数据';
                            } else {
                                return '<span>' + cnm + '</span><br/>没有数据';
                            }
                        } else {
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/><span>违规金额占比：' + Highcharts.numberFormat(dt, 2, '.', ',') + '%</span>';
                            } else {
                                return '<span>' + cnm + '</span><br/><span>违规金额占比：' + Highcharts.numberFormat(dt, 2, '.', ',') + '%</span>';
                            }
                        }
                    }
                },
                colorAxis: {
                    dataClasses: $.mapColor()
                },
                series: [{
                    type: 'map',
                    animation: {
                        duration: 1000
                    },
                    data: mapData,
                    dataLabels: {
                        enabled: true,
                        allowOverlap: true,
                        color: '#3c3e3f',
                        style: {
                            "fontSize": $.xFontSize(),
                            "fontWeight": "lighter",
                            "textOutline": "0px 0px rgba( 0, 0, 0, 0)"
                        },
                        formatter: function () {
                            if (this.point.properties.cnname == "香港") {
                                this.point.plotY = this.point.plotY - 5;
                            }
                            return this.point.properties.cnname;
                        }
                    }
                }],
                drilldown: {
                    activeDataLabelStyle: {
                        textDecoration: 'none',
                        color: '#3c3e3f',
                        style: {
                            "fontSize": $.xFontSize(),
                            "fontWeight": "lighter",
                            "textOutline": "0px 0px rgba(60, 62, 63, 0)"
                        }
                    },
                    drillUpButton: {
                        relativeTo: 'spacingBox',
                        position: {
                            y: 0,
                            x: 0
                        },
                        color: 'white',
                        theme: {
                            fill: 'rgba(248,169,0,.2)',
                            'stroke-width': 0,
                            stroke: 'none',
                            borderColor: 'rgba(248,169,0,.2)',
                            r: 6,
                            states: {
                                hover: {
                                    fill: 'rgba(248,169,0,.2)'
                                },
                                select: {
                                    stroke: '#039',
                                    fill: 'rgba(248,169,0,.2)'
                                }
                            }
                        }
                    }
                },
                exporting: {
                    enabled: false
                },
                credits: {
                    enabled: false
                }
            });
        }
    });
}

// 绘制省地图-单独绘制省地图
function drawProvMap() {
    // 样式
    $('#nanhaiQundao').hide(); //隐藏南海诸岛
    $("#ProvBackBtn").show(); //显示返回按钮显示
    // 定义所需变量
    var orderName = $('#target').val(),
        prvdId = $('#prvdId').val(),
        prvdNameZH = $('#prvdNameZH').val(),
        map = null,
        order,
        ctyCode,
        ctyMap,
        cnm,
        dt,
        postData = { //向后台发送的请求数据
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    // 请求地市地图数据
    $.ajax({
        url: "/cmca/resource/plugins/highcharts/maps/" + prvdId + ".geo.json",
        type: "GET",
        dataType: "json",
        success: function (data) {
            // 解析地市地图数据
            ctyMap = Highcharts.geojson(data);
            // 请求后台数据
            $.ajax({
                url: "/cmca/yjk/getMapData",
                type: 'get',
                dataType: 'json',
                data: postData,
                success: function (ctyData) {
                    // 渲染地市地图颜色
                    if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                        Highcharts.each(ctyMap, function (map) {
                            map.value = 8;
                            ctyCode = map.properties.code; // 在本地地图数据里面拿到code
                            // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据
                            // 如果是直辖市，则整个直辖市区域所有区县都显示为红色
                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                map.value = 2;
                                map.properties.trueValue = ctyData[prvdId].amtPercent;
                            } else { // 不是直辖市
                                //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    order = ctyData[ctyCode].rn; // 通过rn查看排名情况
                                    map.properties.trueValue = ctyData[ctyCode].amtPercent;
                                    if (order <= 3 && order > 0) { //地市前三名
                                        map.value = 2;
                                    }
                                    if (order >= 4 && order <= 10) { //地市4-10名
                                        map.value = 5;
                                    }
                                    if (order <= 0) {
                                        map.value = 0;
                                    }
                                } else {
                                    map.value = 0;
                                    map.properties.trueValue = 0;
                                }
                            }
                        });
                    } else {
                        Highcharts.each(ctyMap, function (map) { //如果没有数据，地图为灰色
                            map.value = 0;
                        });
                    }
                    // 绘制地图
                    map = new Highcharts.Map('chinaMap', {
                        chart: {
                            backgroundColor: 'rgba(0,0,0,0)'
                        },
                        title: {
                            text: ''
                        },
                        legend: {
                            align: 'center',
                            verticalAlign: 'bottom',
                            itemStyle: {
                                "fontSize": $.xFontSize()
                            }
                        },
                        tooltip: {
                            formatter: function () {
                                cnm = this.point.properties.name;
                                dt = this.point.properties.trueValue;
                                if (dt == -1 || dt == null) {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return prvdNameZH + "<br/>没有数据";
                                    } else {
                                        return cnm + "<br/>没有数据";
                                    }
                                } else {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>违规金额占比：' + Highcharts.numberFormat(dt, 2, '.', ',') + '%</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>违规金额占比：' + Highcharts.numberFormat(dt, 2, '.', ',') + '%</span>';
                                    }
                                }
                            }
                        },
                        colorAxis: {
                            dataClasses: $.mapColor()
                        },
                        series: [{
                            data: ctyMap,
                            dataLabels: {
                                enabled: true,
                                allowOverlap: true,
                                color: '#3c3e3f',
                                formatter: function () {
                                    // 地图文字重叠显示特殊处理
                                    var ctyName = this.point.name;
                                    // switch (ctyName) {
                                    //     case "西城区":
                                    //         this.point.plotY -= 10;
                                    //         break;
                                    //     case "东城区":
                                    //         this.point.plotY += 10;
                                    //         break;
                                    //     case "安阳":
                                    //         this.point.plotY -= 20;
                                    //         break;
                                    //     case "塔城":
                                    //         this.point.plotY -= 25;
                                    //         break;
                                    //     case "益阳":
                                    //         this.point.plotY += 35;
                                    //         break;
                                    //     case "鹤岗":
                                    //         this.point.plotY += 12;
                                    //         break;
                                    //     case "无锡":
                                    //         this.point.plotY += 27;
                                    //         break;
                                    //     case "上饶":
                                    //         this.point.plotX += 35;
                                    //         this.point.plotY += 12;
                                    //         break;
                                    //     case "泸州":
                                    //         this.point.plotX -= 23;
                                    //         this.point.plotY -= 12;
                                    //         break;
                                    //     case "百色":
                                    //         this.point.plotX += 35;
                                    //         this.point.plotY += 12;
                                    //         break;
                                    //     case "辽阳":
                                    //         this.point.plotX += 27;
                                    //         this.point.plotY += 28;
                                    //         break;
                                    // }
                                    return this.point.name;
                                },
                                style: {
                                    "fontSize": $.xFontSize(),
                                    "fontWeight": "lighter",
                                    "textOutline": "0px 0px rgba(60, 62, 63, 0)"
                                }
                            }
                        }],
                        mapNavigation: {
                            enabled: true,
                            buttonOptions: {
                                verticalAlign: 'bottom',
                                theme: {
                                    fill: 'rgba(248,169,0,.2)',
                                    'stroke-width': 0,
                                    stroke: 'none',
                                    borderColor: 'rgba(248,169,0,.2)',
                                    r: 6,
                                    states: {
                                        hover: {
                                            fill: 'rgba(248,169,0,.2)'
                                        },
                                        select: {
                                            stroke: '#039',
                                            fill: 'rgba(248,169,0,.2)'
                                        }
                                    }
                                },
                                style: {
                                    color: '#f8a900'
                                }
                            }
                        },
                        exporting: {
                            enabled: false
                        },
                        credits: {
                            enabled: false
                        }
                    });
                }
            });
        }
    });
}

// 绘制地图
function drawMap() {
    if ($('#prvdId').val() != 10000) {
        $('#chinaMap').empty();
        mapTitle();
        drawProvMap();
    } else {
        $('#chooseProvince').val('全公司');
        mapTitle();
        drawChinaMap();
    }
}