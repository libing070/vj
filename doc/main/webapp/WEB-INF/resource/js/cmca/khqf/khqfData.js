// 审计结果-欠费金额排名
function load_result_qfje_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;

    $.ajax({
        url: "/cmca/khqf/getAmountColumnData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow1').css('minWidth', data.prvdList.length * 10 + '%');
            noDataX = -parseInt($('#contentShow1').width()) / 3;
            $('#contentShow1').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.prvdList,
                    crosshair: true,
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
                    formatter: function () {
                        return this.value / 1;
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 百万元</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '欠费金额',
                    data: data.amountList,
                    color: "#3095f2"
                }],
                lang: {
                    noData: "暂无数据" //无数据时显示的文本
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
            $("#contentShowWrap1").getNiceScroll(0).show();
            $("#contentShowWrap1").getNiceScroll(0).resize();
            $("#contentShowWrap1").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-欠费客户数量排名
function load_result_qfsl_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;

    $.ajax({
        url: "/cmca/khqf/getJTNumberPm",
        dataType: 'json',
        cache: false,
        data: postData,
        success: function (data) {
            $('#contentShow2').css('minWidth', data.prvdList.length * 10 + '%');
            noDataX = -parseInt($('#contentShow2').width()) / 3;
            $('#contentShow2').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.prvdList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                },
                yAxis: {
                    min: 0,
                    max: null,
                    title: {
                        text: null
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 户</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '欠费数量',
                    data: data.numberList,
                    color: "#ff647f"
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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

// 审计结果-欠费金额趋势
function load_result_qfje_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/khqf/getAmountLineData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.monthList.length * 10 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.monthList,
                    gridLineWidth: 1,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 百万元</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '欠费金额',
                    data: data.amountList,
                    color: '#00c58e'
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
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

// 审计结果-欠费客户数量趋势
function load_result_qfsl_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/khqf/getJTNumberPmZheXian",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audtrmList.length * 10 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: ''
                },
                xAxis: {
                    categories: data.audtrmList,
                    gridLineWidth: 1,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#8885d5'
                    }]
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 户</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '欠费数量',
                    data: data.numberList,
                    color: '#8885d5'
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
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

// 审计结果-四个图形
function load_result_chart() {
    load_result_qfje_pm_chart();
    load_result_qfsl_pm_chart();
    load_result_qfje_qs_chart();
    load_result_qfsl_qs_chart();
}

// 统计分析-统计报表-排名汇总
function load_fenxi_pmhz_table() {
    $('#allErrorTable').bootstrapTable('destroy');
    $('#allErrorTable').bootstrapTable('resetView');
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        myData,
        h = parseInt($('#fenxiFourNav1FiveNav1Con').height()) - 40;
    $.ajax({
        url: "/cmca/khqf/getJTNumberDataPaiming",
        dataType: 'json',
        data: postData,
        success: function (data) {
            myData = data.paimingList;
            $("#allErrorTable").bootstrapTable({
                datatype: "local",
                data: myData, //加载数据
                pagination: false, //是否显示分页
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '欠费金额排名',
                    valign: 'middle',
                    formatter: rn
                }, {
                    field: 'prvdName',
                    // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                    title:'公司',
                    valign: 'middle'
                }, {
                    field: 'infractionAmt',
                    title: '欠费金额（元）',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter2
                }, {
                    field: 'infractionNum',
                    title: '欠费客户数量（户）',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter0
                }, {
                    field: 'newAmt',
                    title: '新增欠费客户欠费金额（元）',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter2
                }, {
                    field: 'newNum',
                    title: '新增欠费客户数量（户）',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter0
                }]
            });
            $('#allErrorTable').parent('.fixed-table-body').attr('id', 'allErrorTableWrap');
            // $('#allErrorTable thead').remove();
            scroll('#allErrorTableWrap', '#allErrorTable');
        }
    });
    // “操作”这一列用来生成按钮的 动态生成的按钮 ，下面是动态生成按钮代

    // 获取单户是户的列，改变值的属性为没有小数位
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }

    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }

    // 金额排名第一个为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
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
        dataZlfx;
    $.ajax({
        url: "/cmca/khqf/getIncrementalData",
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
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '欠费增量分析（元）'
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
                        return '<span>' + '</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 元</b>';
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
                            return Highcharts.numberFormat(this.y, 2, '.', ',');
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

            $("#fenxiFourNav1FiveNav2Con").getNiceScroll(0).show();
            $("#fenxiFourNav1FiveNav2Con").getNiceScroll(0).resize();
            $('#fenxiFourNav1FiveNav2Con').getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 统计分析-统计报表-欠费账龄/金额分析
function load_fenxi_qffx_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val()
    };
    // chart1
    $.ajax({
        url: "/cmca/khqf/getGRNumberDataQfAge",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataAmt = [],
                dataNum = [];
            $(data.amtList).each(function (index, item) {
                dataAmt.push(item.amt1);
                dataAmt.push(item.amt2);
                dataAmt.push(item.amt3);
            });
            $(data.numList).each(function (index, item) {
                dataNum.push(item.num1);
                dataNum.push(item.num2);
                dataNum.push(item.num3);
            });
            //item1
            $('#qianfeiItem1').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '个人客户欠费账龄分布'
                },
                xAxis: [{
                    categories: ['7-12月', '13-24月', '>24月'],
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, {
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（万元）',
                    type: 'column',
                    yAxis: 1,
                    data: dataAmt
                }, {
                    name: '数量（万户）',
                    type: 'spline',
                    data: dataNum
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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
    // chart2
    $.ajax({
        url: "/cmca/khqf/getGRNumberDataQfAmt",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataAmt = [],
                dataNum = [];
            $(data.amtList).each(function (index, item) {
                dataAmt.push(item.amt1);
                dataAmt.push(item.amt2);
                dataAmt.push(item.amt3);
            });
            $(data.numList).each(function (index, item) {
                dataNum.push(item.num1);
                dataNum.push(item.num2);
                dataNum.push(item.num3);
            });
            $('#qianfeiItem2').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '个人客户欠费金额分布'
                },
                xAxis: [{
                    categories: ['0.2-1千', '1-5千', '>5千'],
                    crosshair: true,
                    labels: {
                        // rotation: 0//调节倾斜角度偏移
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value} ',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（万元）',
                    type: 'column',
                    yAxis: 1,
                    // data: [37.78, 35.75, 26.41]
                    data: dataAmt
                }, {
                    name: '数量（万户）',
                    type: 'spline',
                    // data: [9.26, 1.79, 0.19]
                    data: dataNum

                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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
    // chart3
    $.ajax({
        url: "/cmca/khqf/getOrgOweAging",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataAmt = [],
                dataNum = [];
            $(data.amtList).each(function (index, item) {
                // dataAmt.push(item.amt1);
                dataAmt.push(item.amt2);
                dataAmt.push(item.amt3);
            });
            $(data.numList).each(function (index, item) {
                // dataNum.push(item.num1);
                dataNum.push(item.num2);
                dataNum.push(item.num3);
            });
            $('#qianfeiItem3').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '集团客户欠费账龄分布'
                },
                xAxis: [{
                    categories: ['19-24月', '>24月'],
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（百万元）',
                    type: 'column',
                    yAxis: 1,
                    data: dataAmt
                }, {
                    name: '数量（户）',
                    type: 'spline',
                    data: dataNum
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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
    // chart4
    $.ajax({
        url: "/cmca/khqf/getOrgAmount",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataAmt = [],
                dataNum = [];
            $(data.amtList).each(function (index, item) {
                dataAmt.push(item.amt1);
                dataAmt.push(item.amt2);
                dataAmt.push(item.amt3);
            });
            $(data.numList).each(function (index, item) {
                dataNum.push(item.num1);
                dataNum.push(item.num2);
                dataNum.push(item.num3);
            });
            $('#qianfeiItem4').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '集团客户欠费金额分布'
                },
                xAxis: [{
                    categories: ['1-5万', '5-10万', '>10万'],
                    crosshair: true,
                    labels: {
                        style: {
                            style: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（百万元）',
                    type: 'column',
                    yAxis: 1,
                    data: dataAmt
                }, {
                    name: '数量（户）',
                    type: 'spline',
                    data: dataNum
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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
}

// 统计分析-统计报表-新增原有分布饼图/趋势图
function load_fenxi_xzyyfb_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        target = $("#XZYYFBchooseType").val();
    switch (target) {
        case "集团客户欠费":
            target = "集团";
            break;
        case "个人客户欠费":
            target = "个人";
            break;
    }
    //饼图标题
    $('#pieTitle').text('新增原有' + target + '客户欠费金额占比分布');
    // 占比饼图
    $.ajax({
        url: "/cmca/khqf/getAmountPie",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataPie = [{
                    name: '原有' + target + '客户欠费',
                    y: data.oldAmt,
                    color: '#7CB5EC'
                },
                {
                    name: '新增' + target + '客户欠费',
                    y: data.newAmt,
                    color: '#90ED7D'
                },
            ];
            if (data.oldAmt === undefined) {
                dataPie = [];
            }
            $('#zhanbiBingtuChart').highcharts({
                chart: {
                    type: 'pie',
                    backgroundColor: 'rgba(0,0,0,0)',
                },
                title: {
                    text: null
                },
                tooltip: {
                    formatter: function () {
                        return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + '：</span><span>' + this.point.percentage.toFixed(2) + '%（' + Highcharts.numberFormat(this.point.y, 2, '.', ',') + ' 百万元）</span>';
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

    //趋势图标题
    $('#qsTitle').text(target + '客户欠费新增原有占比分布趋势');
    // 用户欠费金额趋势图
    var h = $('#zhanbiQushiChartWrap').height() - 30;
    $.ajax({
        url: "/cmca/khqf/getJTNumberDataQffenbu",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var dataOri = data.oriAmtList,
                dataNew = data.newAmtList;
            if (data.oriAmtList === undefined) {
                dataOri = [];
            }
            if (data.newAmtList === undefined) {
                dataNew = [];
            }
            $('#zhanbiQushiChart').css('minWidth', data.audtrmList.length * 18 + '%');
            $('#zhanbiQushiChart').highcharts({
                chart: {
                    type: 'area',
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    backgroundColor: 'rgba(0,0,0,0)',
                    height: h
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audtrmList,
                    tickmarkPlacement: 'on',
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
                        },
                        // rotation: -45
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
                            s += '<br/>' + '<span style="color:' + this.series.color + '">' + this.series.name + '</span>：' + Highcharts.numberFormat(this.percentage, 2, '.', ',') + '% （' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 百万元）';
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
                series: [{
                    name: target + '原有客户欠费金额',
                    data: dataOri,
                    color: '#7CB5EC'
                }, {
                    name: '新增' + target + '客户欠费金额',
                    data: dataNew,
                    color: '#90ED7D'
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
            $("#zhanbiQushiChartWrap").getNiceScroll(0).show();
            $("#zhanbiQushiChartWrap").getNiceScroll(0).resize();
            $('#zhanbiQushiChartWrap').getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 统计分析-统计报表-欠费回收
function load_fenxi_qfhs_chart() {
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        dataAmt = [],
        dataNum = [];
    // 集团欠费历史分布
    $.ajax({
        url: "/cmca/khqf/getJTQFHSData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data.amtList) != '[]') {
                dataAmt = [data.amtList[0].amt2, data.amtList[0].amt3];
            } else {
                dataAmt = [];
            }
            if (JSON.stringify(data.numList) != '[]') {
                dataNum = [data.numList[0].num2, data.numList[0].num3];
            } else {
                dataNum = [];
            }
            $('#groupQianfeiHistory').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '集团客户欠费回收情况'
                },
                xAxis: [{
                    categories: ['13-24月', '>24月'],
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, {
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（百万元）',
                    type: 'column',
                    yAxis: 1,
                    data: dataAmt
                }, {
                    name: '数量（户）',
                    type: 'spline',
                    data: dataNum
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


    // 个人欠费历史分布
    $.ajax({
        url: "/cmca/khqf/getGRQFHSData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data.amtList) != '[]') {
                dataAmt = [data.amtList[0].amt1, data.amtList[0].amt2, data.amtList[0].amt3];
            }
            if (JSON.stringify(data.numList) != '[]') {
                dataNum = [data.numList[0].num1, data.numList[0].num2, data.numList[0].num3];
            }
            /*$(data.amtList).each(function(index, item) {
                dataAmt.push(item.amt1);
                dataAmt.push(item.amt2);
                dataAmt.push(item.amt3);
            });
            $(data.numList).each(function(index, item) {
                dataNum.push(item.num1);
                dataNum.push(item.num2);
                dataNum.push(item.num3);
            });*/
            $('#personQianfeiHistory').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '个人客户欠费回收情况'
                },
                xAxis: [{
                    categories: ['7-12月', '13-24月', '>24月'],
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: null,
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    itemStyle: {
                        fontWeight: 'normal',
                        fontSize: '11px'
                    }
                },
                series: [{
                    name: '金额（百万元）',
                    type: 'column',
                    yAxis: 1,
                    data: dataAmt
                }, {
                    name: '数量（万户）',
                    type: 'spline',
                    data: dataNum
                }],
                lang: {
                    noData: "暂无数据" //真正显示的文本
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
}

// 统计分析-审计整改问责-整改问责要求 
function load_fenxi_zgwz_require_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#wenzeTable').bootstrapTable('destroy');
    $('#wenzeTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: 4000,
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav2FiveNav1Con').height()) - 30;
    $.ajax({
        url: "/cmca/khqf/getKHQFgwzData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 绘制表格
            $("#wenzeTable").bootstrapTable({
                datatype: "local",
                data: data,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'errQty',
                columns: [{
                    field: 'rn',
                    title: '编号',
                    align: 'center',
                    valign: 'middle',
                    width: '8%'
                }, {
                    field: 'prvdName',
                    // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                    title:'公司',
                    align: 'center',
                    valign: 'middle',
                    width: '10%'
                }, {
                    field: 'wtSummary',
                    title: '问题概要',
                    align: 'center',
                    valign: 'middle',
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
    //六个月内达到整改标准次数排名（单位：次）
    load_fenxi_zgwz_zgbzcs_chart();

    // 累计达到整改次数排名（单位：次）
    load_fenxi_zgwz_zgcs_chart();

    // 达到整改标准省公司数量趋势（单位：个）
    load_fenxi_zgwz_zgbz_chart();
}

//六个月内达到整改标准次数排名（单位：次）
function load_fenxi_zgwz_zgbzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val()
    };
    // 六个月内达到整改标准次数排名（单位：次）
    $.ajax({
        url: "/cmca/khqf/getRectifiyForSixColumn",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#stageAccOrder').css('minWidth', data.prvdName.length * 10 + '%');
            $('#stageAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.prvdName,
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
                        return '<span style="color:' + this.series.color + '">' + this.series.name + ' : </span><b>' + Highcharts.numberFormat(this.y, 0) + ' 次</b>';
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
                    data: data.rectifyNum,
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
            $('#stageAccOrderWrap').getNiceScroll(0).doScrollLeft(0);

        }
    });
}

// 累计达到整改次数排名（单位：次）
function load_fenxi_zgwz_zgcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 累计达到整改次数排名（单位：次）
    $.ajax({
        url: "/cmca/khqf/getRectifiyColumn",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#addUpAccOrder').css('minWidth', data.prvdName.length * 10 + '%');
            $('#addUpAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },

                xAxis: {
                    categories: data.prvdName,
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
                        return '<span style="color:' + this.series.color + '">' + this.series.name + ' : </span><b>' + Highcharts.numberFormat(this.y, 0) + ' 次</b>';
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
                    data: data.rectifyNum,
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
            $('#addUpAccOrderWrap').getNiceScroll(0).doScrollLeft(0);

        }
    });
}

//达到整改标准省公司数量趋势（单位：个）
function load_fenxi_zgwz_zgbz_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 达到整改标准省公司数量趋势（单位：个）
    $.ajax({
        url: "/cmca/khqf/getRectifiyLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#accTrend').css('minWidth', data.audTrm.length * 5 + '%');
            $('#accTrend').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrm,
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
                    data: data.rectifyNum,
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
            $('#accTrendWrap').getNiceScroll(0).doScrollLeft(0);

        }
    });
}

// 统计分析-审计报告-客户欠费情况汇总
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav3Con').find('.audtrm_off,.audtrm_on').hide();
    // 定义所需变量
    var myData,
        auditDates = auditDate(),
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            time: new Date().getTime()
        };
    // 先销毁表格，清除缓存
    $('#baogaoTop5').bootstrapTable('destroy');
    $('#baogaoTop5').bootstrapTable('resetView');
    // 填充表格上方文字说明数据
    $('#sjbgPrvdName').text($('#prvdId').val() == '10000' ? $('#prvdNameZH').val() : $('#prvdNameZH').val() + '公司'); //被审计公司
    $('#sjbgAudTrm').text(auditDates[2]); //审计时间
    $('#sjbgPeriod').text('2013年1月1日 -- ' + auditDates[1]); //审计期间

    // 千分位格式化，保留两位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 千分位格式化，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 请求数据
    $.ajax({
        url: "/cmca/khqf/getSjbgData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (data.switchState != 'audTrmColseForReport') {
                $('#fenxiFourNav3Con .audtrm_on').show();
                var myData;
                if (JSON.stringify(data) != "[{},{}]") {
                    if (data[0]["4003"] != undefined && data[1]["4001"] != undefined) {
                        // 返回数据
                        myData = [
                            data[0]["4003"],
                            data[1]["4001"]
                        ];
                        // 判断增加的值为正还是为负，如果为正，文字显示内容为正，如果为负，显示为减少
                        var NumMom4003 = parseInt(myData[0].infractionNumMom),
                            AmtMom4003 = parseFloat(myData[0].infractionAmtMom),
                            NumMom4001 = parseInt(myData[1].infractionNumMom),
                            AmtMom4001 = parseFloat(myData[1].infractionAmtMom),
                            momText1,
                            momText2;
                        momText1 = (NumMom4003 < 0) ? "减少" : "增加";
                        momText2 = (AmtMom4003 < 0) ? "减少" : "增加";
                        momText3 = (NumMom4001 < 0) ? "减少" : "增加";
                        momText4 = (AmtMom4001 < 0) ? "减少" : "增加";
                        $('#baogaoInfo').html('截止' + $('#chooseTime').val() +
                            '，个人客户欠费账期达6个月以上、累计欠费金额超过200元且正常在用的有' + $.formatMoney(myData[0].infractionNum, 0) + '户，' +
                            '欠费金额共计' + $.formatMoney(myData[0].infractionAmt, 2) + '万元，' +
                            '比上期' + momText1 + $.formatMoney(Math.abs(NumMom4003), 0) + '户，' +
                            '欠费金额' + momText2 + $.formatMoney(Math.abs(AmtMom4003), 2) + '万元，' +
                            '其中' + $.formatMoney(myData[0].newNum, 0) + '户为新增客户，' +
                            '欠费金额' + $.formatMoney(myData[0].newAmt, 2) + '万元。<br>' +
                            '本期长期高额欠费个人客户中红名单及协议免催免停客户' + $.formatMoney(myData[0].vipNum, 0) + '户，' +
                            '欠费金额' + $.formatMoney(myData[0].vipAmt, 2) + '万元。' +
                            '截止' + $('#chooseTime').val() + '，集团产品客户欠费账期达18个月以上、累计欠费金额超过10000元的有' + $.formatMoney(myData[1].infractionNum, 0) + '户，' +
                            '欠费金额' + $.formatMoney(myData[1].infractionAmt, 2) + '万元，' +
                            '比上期' + momText3 + $.formatMoney(Math.abs(NumMom4001), 0) + '户，' +
                            '欠费金额' + momText4 + $.formatMoney(Math.abs(AmtMom4001), 2) + '万元，' +
                            '其中' + $.formatMoney(myData[1].newNum, 0) + '户为新增客户，' +
                            '欠费金额' + $.formatMoney(myData[1].newAmt, 2) + '万元。'
                        );
                    } else if (data[1]["4001"] == undefined) {
                        // 返回数据
                        myData = [
                            data[0]["4003"]
                        ];
                        // 判断增加的值为正还是为负，如果为正，文字显示内容为正，如果为负，显示为减少
                        var hasNumMom4003 = parseInt(myData[0].infractionNumMom),
                            hasAmtMom4003 = parseFloat(myData[0].infractionAmtMom),
                            hasmomText1, hasmomText2;
                        hasmomText1 = (hasNumMom4003 < 0) ? "减少" : "增加";
                        hasmomText2 = (hasAmtMom4003 < 0) ? "减少" : "增加";
                        $('#baogaoInfo').html('截止' + $('#chooseTime').val() +
                            '，个人客户欠费账期达6个月以上、累计欠费金额超过200元且正常在用的有' + $.formatMoney(myData[0].infractionNum, 0) + '户，' +
                            '欠费金额共计' + $.formatMoney(myData[0].infractionAmt, 2) + '万元，' +
                            '比上期' + hasmomText1 + $.formatMoney(Math.abs(hasNumMom4003), 0) + '户，' +
                            '欠费金额' + hasmomText2 + $.formatMoney(Math.abs(hasAmtMom4003), 2) + '万元，' +
                            '其中' + $.formatMoney(myData[0].newNum, 0) + '户为新增客户，' +
                            '欠费金额' + $.formatMoney(myData[0].newAmt, 2) + '万元。<br>' +
                            '本期长期高额欠费个人客户中红名单及协议免催免停客户' + $.formatMoney(myData[0].vipNum, 0) + '户，' +
                            '欠费金额' + $.formatMoney(myData[0].vipAmt, 2) + '万元。'
                        );
                    } else if (data[0]["4003"] == undefined) {
                        // 返回数据
                        myData = [
                            data[1]["4001"]
                        ];
                        // 判断增加的值为正还是为负，如果为正，文字显示内容为正，如果为负，显示为减少
                        var hasNumMom4001 = parseInt(myData[0].infractionNumMom),
                            hasAmtMom4001 = parseFloat(myData[0].infractionAmtMom),
                            hasmomText3, hasmonText4;
                        hasmomText3 = (hasNumMom4001 < 0) ? "减少" : "增加";
                        hasmomText4 = (hasAmtMom4001 < 0) ? "减少" : "增加";
                        $('#baogaoInfo').html(
                            '截止' + $('#chooseTime').val() + '，集团产品客户欠费账期达18个月以上、累计欠费金额超过10000元的有' + $.formatMoney(myData[0].infractionNum, 0) + '户，' +
                            '欠费金额' + $.formatMoney(myData[0].infractionAmt, 2) + '万元，' +
                            '比上期' + hasmomText3 + $.formatMoney(Math.abs(hasNumMom4001), 0) + '户，' +
                            '欠费金额' + hasmomText4 + $.formatMoney(Math.abs(hasAmtMom4001), 2) + '万元，' +
                            '其中' + $.formatMoney(myData[0].newNum, 0) + '户为新增客户，' +
                            '欠费金额' + $.formatMoney(myData[0].newAmt, 2) + '万元。'
                        );
                    }

                    // 绘制表格
                    $("#baogaoTop5").bootstrapTable({
                        datatype: "local",
                        data: myData,
                        cache: false,
                        columns: [{
                            field: 'concern',
                            title: '关注点',
                            align: 'center',
                            valign: 'middle'
                        }, {
                            field: 'infractionNum',
                            title: '欠费<br>数量（户）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter0
                        }, {
                            field: 'infractionAmt',
                            title: '欠费<br>金额（万元）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter2
                        }, {
                            field: 'newNum',
                            title: '新增欠费客户数量（户）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter0
                        }, {
                            field: 'newAmt',
                            title: '新增客户欠费金额（万元）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter2
                        }, {
                            field: 'oriNum',
                            title: '原有欠费客户数量（户）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter0
                        }, {
                            field: 'oriAmt',
                            title: '原有欠费客户欠费金额（万元）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter2
                        }]
                    });
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

// 地图下钻-客户欠费明细
function load_qfInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $('#dialogQudaoTable').bootstrapTable('resetView');
    var postData = {
            prvdId: $('#prvdId').val(),
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height());

    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    $.ajax({
        url: "/cmca/khqf/getSjbgCtyDetData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '序号',
                    width: '12%',
                    valign: 'middle'
                }, {
                    field: 'custNm',
                    title: '客户名称',
                    width: '20%',
                    valign: 'middle'
                }, {
                    field: 'custTyp',
                    title: '客户类型',
                    width: '18%',
                    valign: 'middle'
                }, {
                    field: 'acctAge',
                    title: '欠费账龄',
                    width: '18%',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'infractionAmt',
                    title: '欠费金额',
                    sortable: true,
                    width: '25%',
                    valign: 'middle',
                    formatter: operateFormatter2
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
        }
    });
}

// 地图下方卡片
function load_mapBtm_card_chart() {
    // 定义所需变量
    var prvdId = $('#prvdId').val(),
        infractionNum = 0, //欠费客户数量
        infractionNumMom = 0, //欠费客户数量增量
        infractionAmt = 0, //欠费金额
        infractionAmtMom = 0, //欠费金额增量
        oriNum = 0, //原有欠费客户数量
        oriNumMom = 0, //原有欠费客户数量增量
        oriAmt = 0, //原有欠费金额
        oriAmtMom = 0, //原有欠费金额增量
        // 后台请求参数
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    $.ajax({
        url: "/cmca/khqf/getMapBottomData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                infractionNum = $.formatMoney(data[prvdId].infractionNum, 0);
                infractionNumMom = data[prvdId].infractionNumMom;
                infractionAmt = $.formatMoney(data[prvdId].infractionAmt, 2);
                infractionAmtMom = data[prvdId].infractionAmtMom;
                oriNum = $.formatMoney(data[prvdId].oriNum, 0);
                oriNumMom = data[prvdId].oriNumMom;
                oriAmt = $.formatMoney(data[prvdId].oriAmt, 2);
                oriAmtMom = data[prvdId].oriAmtMom;
            }
            // 欠费客户数量
            if (infractionNumMom <= 0 || infractionNumMom === null) {
                $('#card1').html(infractionNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionNumMom), 0) + '</span>');
            } else {
                $('#card1').html(infractionNum + '<span><i class="caret_up"></i>' + $.formatMoney(infractionNumMom, 0) + '</span>');
            }
            // 欠费金额
            if (infractionAmtMom <= 0 || infractionAmtMom === null) {
                $('#card2').html(infractionAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionAmtMom), 2) + '</span>');
            } else {
                $('#card2').html(infractionAmt + '<span><i class="caret_up"></i>' + $.formatMoney(infractionAmtMom, 2) + '</span>');
            }
            // 原有欠费客户数量
            if (oriNumMom <= 0 || oriNumMom === null) {
                $('#card3').html(oriNum + '<span><i class="caret_down"></i>' + Math.abs(oriNumMom) + '</span>');
            } else {
                $('#card3').html(oriNum + '<span><i class="caret_up"></i>' + oriNumMom + '</span>');
            }
            // 原有欠费客户金额
            if (oriAmtMom <= 0 || oriAmtMom === null) {
                $('#card4').html(oriAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(oriAmtMom), 2) + '</span>');
            } else {
                $('#card4').html(oriAmt + '<span><i class="caret_up"></i>' + $.formatMoney(oriAmtMom, 2) + '</span>');
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
 * 调用方法drawMap()
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
        url: "/cmca/khqf/getMapData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            // 地图颜色
            if (JSON.stringify(data) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                $.each(mapData, function (idex, map) {
                    if (map.name != 'Macau' && map.name != 'HongKong' && map.name != 'Taiwan') { //香港，澳门，台湾三省无数据
                        map.value = 8;
                        prvdCode = map.properties.id;
                        //后台返回的数据中，可能会出现某个省没有数据的情况，导致数据中直接没有该省数据对象，所以在此判断，如果没有数据，即data[prvdCode]值为undefined，则直接将该省的value值置为0，没有数据，如果不做此判断，在循环省数据的时候，没有某个省的数据会导致报错，而无法下钻
                        if (data[prvdCode] !== undefined) {
                            // 判断指标
                            switch (orderName) {
                                case 'amtOrder': //当指标是金额排名
                                    order = data[prvdCode].amtOrder;
                                    map.properties.trueValue = data[prvdCode].infractionAmt;
                                    break;
                                case 'numOrder': //当指标是数量排名
                                    order = data[prvdCode].numOrder;
                                    map.properties.trueValue = data[prvdCode].infractionNum;
                                    break;
                            }
                            if (order <= 3 && order > 0) { //全国前三名, 红色
                                map.value = 2;
                            }
                            if (order >= 4 && order <= 10) { //全国4-10名，黄色
                                map.value = 5;
                            }
                            if (order <= 0) {
                                map.value = 0;
                            }
                        } else {
                            map.value = 0;
                        }
                    } else { //无数据，灰色
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
                        // 下钻
                        drilldown: function (e) {
                            prvdId = e.point.properties.id;
                            // 判断下钻对象是否为空或港澳台，如果是则不能下钻
                            if (e.point.drilldown && unDrilldown.indexOf(e.point.drilldown) === -1) {
                                // 插入一经事件码-查询
                                dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                                // 日志记录
                                get_userBehavior_log('专题', '客户欠费', '地图下钻', '查询');

                                // 请求省地图数据
                                $.ajax({
                                    url: "/cmca/resource/plugins/highcharts/maps/" + prvdId + ".geo.json",
                                    type: "GET",
                                    dataType: "json",
                                    success: function (data) {
                                        // 解析省地图数据
                                        ctyMap = Highcharts.geojson(data);
                                        // 样式 
                                        $('#chooseProvince').val(e.point.properties.cnname);
                                        $('#nanhaiQundao').hide();
                                        // 改变隐藏域val
                                        $('#prvdId').val(prvdId);
                                        $('#prvdName').val(e.point.drilldown);
                                        $('#prvdNameZH').val(e.point.properties.cnname);
                                        // 加载地图底部卡片数据
                                        load_mapBtm_card_chart();
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
                                            url: "/cmca/khqf/getMapData",
                                            type: 'get',
                                            dataType: 'json',
                                            data: postData,
                                            success: function (ctyData) {
                                                // 地图颜色
                                                if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                                                    Highcharts.each(ctyMap, function (map) {
                                                        map.value = 8;
                                                        ctyCode = map.properties.code;
                                                        // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据，整个直辖市区域所有区县都显示为红色
                                                        if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                            map.value = 2;
                                                            map.properties.trueValue = e.point.properties.trueValue;
                                                        } else { // 不是直辖市
                                                            //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                                            if (ctyData[ctyCode] !== undefined) {
                                                                // 判断指标
                                                                switch (orderName) {
                                                                    case 'amtOrder': //当指标是金额排名
                                                                        order = ctyData[ctyCode].amtOrder;
                                                                        map.properties.trueValue = ctyData[ctyCode].infractionAmt;
                                                                        break;
                                                                    case 'numOrder': //当指标是数量排名
                                                                        order = ctyData[ctyCode].numOrder;
                                                                        map.properties.trueValue = ctyData[ctyCode].infractionNum;
                                                                        break;
                                                                }
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
                                                                map.value = 0; //地市无数据，灰色
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
                                                            //     case "红桥区":
                                                            //         this.point.plotY -= 5;
                                                            //         break;
                                                            //     case "和平区":
                                                            //         this.point.plotX += 15;
                                                            //         this.point.plotY += 20;
                                                            //         break;
                                                            //     case "河北区":
                                                            //         this.point.plotY -= 5;
                                                            //         break;
                                                            //     case "河西区":
                                                            //         this.point.plotY += 5;
                                                            //         break;
                                                            //     case "南开区":
                                                            //         this.point.plotX -= 10;
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
                                                    events: {
                                                        click: function (e) {
                                                            // 插入一经事件码-查询
                                                            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                                                            //地市地图下钻表格弹层
                                                            $('#mapTableDialog').show();
                                                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                                // 样式-下钻表格标题
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '市客户欠费信息');
                                                                // 改变隐藏域
                                                                $('#ctyId').val(prvdId);
                                                            } else {
                                                                // 样式-下钻表格标题
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '省' + e.point.name + '市客户欠费信息');
                                                                // 改变隐藏域
                                                                $('#ctyId').val(e.point.properties.code);
                                                            }
                                                            // 加载数据
                                                            load_qfInfo_table();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        // 下钻返回
                        drillup: function () {
                            // 插入一经事件码-查询
                            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                            // 样式
                            $('#nanhaiQundao').show();
                            $('#chooseProvince').val('全公司');
                            $('#ProvBackBtn').hide();
                            // 省地图返回全国地图时，下钻弹出的表格也要关闭
                            $('#mapTableDialog').hide();
                            // 改变隐藏域val
                            $('#prvdId').val(10000);
                            $('#prvdName').val('');
                            $('#prvdNameZH').val('全公司');
                            // 地图下方卡片联动
                            load_mapBtm_card_chart();
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
                    },
                },
                tooltip: {
                    formatter: function () { //鼠标悬浮提示
                        cnm = this.point.properties.cnname;
                        nm = this.point.name;
                        dt = this.point.properties.trueValue;
                        prvdNameZH = $('#prvdNameZH').val();
                        // 判断是否在全国地图下
                        if ($('#prvdId').val() != 10000) {
                            cnm = this.point.properties.name;
                        }
                        // 判断是否是港澳台或当前区域没数据(dt == -1)
                        if (nm == "Taiwan" || nm == "HongKong" || nm == "Macau" || dt == -1 || dt == null) {
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/>没有数据';
                            } else {
                                return '<span>' + cnm + '</span><br/>没有数据';
                            }
                        } else {
                            switch (orderName) {
                                case 'amtOrder': // 当指标是金额排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>欠费金额：' + Highcharts.numberFormat(dt, 2) + ' 百万元</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>欠费金额：' + Highcharts.numberFormat(dt, 2) + ' 百万元</span>';
                                    }
                                    break;
                                case 'numOrder': // 当指标是数量排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>欠费数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 户</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>欠费数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 户</span>';
                                    }
                                    break;
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

// 绘制省地图
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
        postData = {
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
            // 请求省数据
            $.ajax({
                url: "/cmca/khqf/getMapData",
                type: 'get',
                dataType: 'json',
                data: postData,
                success: function (ctyData) {
                    // 地图颜色
                    if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                        Highcharts.each(ctyMap, function (map) {
                            map.value = 8;
                            ctyCode = map.properties.code;
                            // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据，整个直辖市区域所有区县都显示为红色
                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                map.value = 2;
                                switch (orderName) {
                                    case 'amtOrder': //当指标是金额排名
                                        map.properties.trueValue = ctyData[prvdId].infractionAmt;
                                        break;
                                    case 'numOrder': //当指标是数量排名
                                        map.properties.trueValue = ctyData[prvdId].infractionNum;
                                        break;
                                }
                            } else { // 不是直辖市
                                //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    switch (orderName) {
                                        case 'amtOrder': //当指标是金额排名
                                            order = ctyData[ctyCode].amtOrder;
                                            map.properties.trueValue = ctyData[ctyCode].infractionAmt;
                                            break;
                                        case 'numOrder': //当指标是数量排名
                                            order = ctyData[ctyCode].numOrder;
                                            map.properties.trueValue = ctyData[ctyCode].infractionNum;
                                            break;
                                    }
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
                            },
                        },
                        tooltip: {
                            formatter: function () {
                                cnm = this.point.properties.name;
                                dt = this.point.properties.trueValue;
                                // 判断是否有数据
                                if (dt == -1) {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return prvdNameZH + "<br/>没有数据";
                                    } else {
                                        return cnm + "<br/>没有数据";
                                    }
                                } else {
                                    switch (orderName) {
                                        case 'amtOrder': //当指标是金额排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>欠费金额：' + Highcharts.numberFormat(dt, 2) + ' 百万元</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>欠费金额：' + Highcharts.numberFormat(dt, 2) + ' 百万元</span>';
                                            }
                                            break;
                                        case 'numOrder': //当指标是数量排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>欠费数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 户</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>欠费数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 户</span>';
                                            }
                                            break;
                                    }
                                }
                            }
                        },
                        colorAxis: {
                            dataClasses: $.mapColor()
                        },
                        series: [{
                            events: {
                                click: function (e) {
                                    // 插入一经事件码-查询
                                    dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                                    //地市地图下钻表格弹层
                                    $('#mapTableDialog').show();
                                    // 判断是否是直辖市
                                    if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                        // 样式
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '市客户欠费信息');
                                        // 改变隐藏域
                                        $('#ctyId').val(prvdId);
                                    } else {
                                        // 样式
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '省' + e.point.name + '市客户欠费信息');
                                        // 改变隐藏域
                                        $('#ctyId').val(e.point.properties.code);
                                    }
                                    // 加载数据
                                    load_qfInfo_table();
                                }
                            },
                            data: ctyMap,
                            dataLabels: {
                                enabled: true,
                                allowOverlap: true,
                                color: '#3c3e3f',
                                formatter: function () {
                                    // 地图文字重叠显示特殊处理
                                    var ctyName = this.point.name;
                                    switch (ctyName) {
                                        case "西城区":
                                            this.point.plotY -= 10;
                                            break;
                                        case "东城区":
                                            this.point.plotY += 10;
                                            break;
                                        case "红桥区":
                                            this.point.plotY -= 5;
                                            break;
                                        case "和平区":
                                            this.point.plotX += 15;
                                            this.point.plotY += 20;
                                            break;
                                        case "河北区":
                                            this.point.plotY -= 5;
                                            break;
                                        case "河西区":
                                            this.point.plotY += 5;
                                            break;
                                        case "南开区":
                                            this.point.plotX -= 10;
                                            break;
                                        case "安阳":
                                            this.point.plotY -= 20;
                                            break;
                                        case "塔城":
                                            this.point.plotY -= 25;
                                            break;
                                        case "益阳":
                                            this.point.plotY += 35;
                                            break;
                                        case "鹤岗":
                                            this.point.plotY += 12;
                                            break;
                                        case "无锡":
                                            this.point.plotY += 27;
                                            break;
                                        case "上饶":
                                            this.point.plotX += 35;
                                            this.point.plotY += 12;
                                            break;
                                        case "泸州":
                                            this.point.plotX -= 23;
                                            this.point.plotY -= 12;
                                            break;
                                        case "百色":
                                            this.point.plotX += 35;
                                            this.point.plotY += 12;
                                            break;
                                        case "辽阳":
                                            this.point.plotX += 27;
                                            this.point.plotY += 28;
                                            break;
                                    }
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
        drawProvMap();
    } else {
        $('#chooseProvince').val('全公司');
        drawChinaMap();
    }
}