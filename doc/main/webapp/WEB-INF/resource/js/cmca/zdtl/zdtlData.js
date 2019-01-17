//审计结果-异常销售终端占比-排名
function load_result_ycxsPercent_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/zdtl/getPercentColumnData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow1').css('minWidth', data.prvdName.length * 10 + '%');
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
                    categories: data.prvdName,
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
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';

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
                    name: '异常销售终端占比',
                    data: data.qtyPercent === null ? data.qtyPercent = 0 : data.qtyPercent,
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

//审计结果-异常销售终端数量-排名
function load_result_ycxsNum_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/zdtl/getNumberColumnData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow1').css('minWidth', data.prvdName.length * 10 + '%');
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
                    categories: data.prvdName,
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
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '万';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万台</b>';
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
                    name: '异常销售终端数量',
                    data: data.infractionNum,
                    color: "#3095f2"
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
            $("#contentShowWrap1").getNiceScroll(0).show();
            $("#contentShowWrap1").getNiceScroll(0).resize();
            $("#contentShowWrap1").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

//审计结果-异常销售渠道占比-排名
function load_result_ycxschnlPercent_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/zdtl/getPercentChnlColumnData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow2').css('minWidth', data.prvdName.length * 10 + '%');
            noDataX = -parseInt($('#contentShow1').width()) / 3;
            $('#contentShow2').highcharts({
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
                    min: 0,
                    max: null,
                    title: {
                        text: null
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
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b/>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
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
                    name: '异常销售渠道占比',
                    data: data.infractionChnlPercent,
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

//审计结果-异常销售渠道数量-排名
function load_result_ycxschnlNum_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/zdtl/getNumberChnlColumnData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow2').css('minWidth', data.prvdName.length * 10 + '%');
            noDataX = -parseInt($('#contentShow1').width()) / 3;
            $('#contentShow2').highcharts({
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
                    min: 0,
                    max: null,
                    title: {
                        text: null
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 个</b>';
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
                    name: '异常销售渠道数量',
                    data: data.infractionChnlNum,
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

//审计结果-异常销售终端占比-趋势
function load_result_ycxsPercent_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val(),
    };
    $.ajax({
        url: "/cmca/zdtl/getPercentLineData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.audTrm.length * 5 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrm,
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
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '异常销售终端占比',
                    data: data.qtyPercent,
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

//审计结果-异常销售终端数量-趋势
function load_result_ycxsNum_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val(),
    };
    $.ajax({
        url: "/cmca/zdtl/getPercentLineData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.audTrm.length * 5 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrm,
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
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万台</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '异常销售终端数量',
                    data: data.infractionNum,
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

//审计结果-异常销售渠道占比-趋势
function load_result_ycxschnlPercent_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val(),
    };
    $.ajax({
        url: "/cmca/zdtl/getPercentChnlLineData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audTrm.length * 5 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrm,
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
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '异常销售渠道占比',
                    data: data.infractionChnlPercent,
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
//审计结果-异常销售渠道数量趋势-趋势
function load_result_ycxschnlNum_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val(),
    };
    $.ajax({
        url: "/cmca/zdtl/getPercentChnlLineData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audTrm.length * 5 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrm,
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
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + ' 万台</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '异常销售渠道数量趋势',
                    data: data.infractionChnlNum,
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

// 审计结果-图形
function load_result_chart() {
    /**
     * 因为页面首次加载的时候异常销售终端占比排名和异常销售终端占比排名左右两个图形已经全部加载
     * 所以绘制审计结果下四个图形时判断左右两个图形哪个处于显示状态，以便在下拉列表区域选择/时间选择变化的时候加载对应的图形，而不是初始化图形
     * 因为关联到地图，所以如果初始化，还需要初始化地图
     */
    if ($('#errTarget').val() == 'ycxszb') { //异常销售终端占比排名
        load_result_ycxsPercent_pm_chart();
    } else if ($('#errTarget').val() == 'ycxs') { //异常销售终端数量排名
        load_result_ycxsNum_pm_chart();
    }
    if ($('#errChnlTarget').val() == 'ycxsqdzb') { //异常销售渠道占比排名
        load_result_ycxschnlPercent_pm_chart();
    } else if ($('#errChnlTarget').val() == 'ycxsqd') { //异常销售渠道数量排名
        load_result_ycxschnlNum_pm_chart();
    }
    if ($('#errQsTarget').val() == 'errPercentQs') { //异常销售占比趋势
        load_result_ycxsPercent_qs_chart();
    } else if ($('#errQsTarget').val() == 'errNumQs') { //异常销售数量趋势
        load_result_ycxsNum_qs_chart();
    }
    load_result_ycxschnlPercent_qs_chart();
}

// 统计分析-统计报表-排名汇总
function load_fenxi_pmhz_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#rankingAllTable').bootstrapTable('destroy');
    $('#rankingAllTable').bootstrapTable('resetView');
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav1Con').height()) - 40;
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    //定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/zdtl/getRankTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#rankingAllTable").bootstrapTable({
                    datatype: "local",
                    data: data.rankData, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '异常销售占比排名',
                        valign: 'middle',
                        formatter: rn
                    }, {
                        field: 'prvdName',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        valign: 'middle'
                    }, {
                        field: 'errCityNum',
                        title: '涉及地市数量',
                        valign: 'middle',
                        sortable: true
                    }, {
                        field: 'tolSellNum',
                        title: '终端销售数量（台）',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'infractionNum',
                        title: '异常销售数量（台）',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'qtyPercent',
                        title: '异常销售占比',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatterB
                    }, {
                        field: 'qtyPercentMom',
                        title: '异常销售占比增幅',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatterB
                    }, {
                        field: 'tolSellChnlNum',
                        title: '终端销售渠道',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'infractionChnlNum',
                        title: '异常销售涉及渠道',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'infractionChnlPercent',
                        title: '涉及渠道占比',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatterB
                    }, {
                        field: 'infractionChnlPercentMom',
                        title: '涉及渠道占比增幅',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatterB
                    }]
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
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        lastDataName,
        lastDataY,
        newLastData,
        dataZlfx,
        waterfallH = $('#pubuChart').closest('.pubu_show').height() - 40;
    // seriesName=parseFloat(this.y>0) ? "增加" : "减少";
    $.ajax({
        url: "/cmca/zdtl/getIncrementalData",
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
                    text: '全网异常销售终端占比（%）'
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
                            return this.value / 1 + "%";
                        }
                    }
                },
                legend: {
                    enabled: false
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span>' + '</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + '%</b>';
                    }
                },
                plotOptions: {
                    series: {
                        pointWidth: $.pubuPointW()
                    }
                },
                series: [{
                    //name: ,
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

// 统计分析-统计报表-重点关注渠道 
function load_fenxi_zdgz_chnl_table() {
    // 销毁表格，清除缓存
    $('#tuchuQudaoTable').bootstrapTable('destroy');
    $('#tuchuQudaoTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav3Con').height()),
        time = $("#audTrm").val(),
        audTrms; //月份递减
        if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
    $.ajax({
        url: "/cmca/zdtl/getFocusThingTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            //定义数据格式
            // 千分位，不保留小数
            function operateFormatter0(value, row, index) {
                if (value != "-") {
                    return $.formatMoney(value, 0, "table");
                } else {
                    return '-';
                }
            }
            // 百分比格式化
            function operateFormatterB(value, row, index) {
                if (value != null) {
                    return $.formatMoney(value, 2, "table") + "%";
                }
            }

            function tagColumn(value, row, index) {
                return '<a href="javascript:;" value="' + row.chnlId + '">' + value + '</a>';
            }
            $("#tuchuQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.focusData,
                pagination: false,
                cache: false,
                height: h,
                columns: [
                    [{
                        field: 'rn',
                        title: '序号',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2,
                        //width: '4%'
                    }, {
                        field: 'chnlName',
                        title: '渠道名称',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2,
                        formatter: tagColumn,
                    }, {
                        field: 'prvdName',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        align: 'center',
                        colspan: 1,
                        rowspan: 2,
                        valign: 'middle',
                    }, {
                        title: audTrms[0],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1,
                    }, {
                        title: audTrms[1],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1,
                    }, {
                        title: audTrms[2],
                        align: 'center',
                        colspan: 3,
                        rowspan: 1,
                        valign: 'middle',
                    }],
                    [{
                        field: 'tol_num',
                        title: '终端销量（台）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'infraction_num',
                        title: '异常销售终端数量（台）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'per_num',
                        title: '异常销售终端占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'm1_tol_num',
                        title: '终端销量（台）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'm1_infraction_num',
                        title: '异常销售终端数量（台）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'm1_per_num',
                        title: '异常销售终端占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'm2_tol_num',
                        title: '终端销量',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'm2_infraction_num',
                        title: '异常销售终端数量（台）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0,
                    }, {
                        field: 'm2_per_num',
                        title: '异常销售终端占比',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatterB
                    }]
                ]
            });
            $('#tuchuQudaoTable').parent('.fixed-table-body').attr('id', 'tuchuQudaoTableWrap');
            // $('#tuchuQudaoTable thead').remove();
            scroll('#tuchuQudaoTableWrap', '#tuchuQudaoTable');
        }
    });
}

// 统计分析-统计报表-重点关注渠道-表格下钻图表
function load_fenxi_zdgz_chnlInfo_chart() {
    //隐藏上级滚动条
    $('#tuchuQudaoTable').parent('.fixed-table-body').getNiceScroll().hide();
    var postData = {
            audTrm: $('#audTrm').val(),
            chnlId: $('#chnlId').val(),
            concern: $('#concern').val(),
            prvdId: $('#prvdId').val()
        },
        qudaoChnlBaseInfoTable;
    h = $('#zdgz_qudaoChart').closest('.info_item').height() - 20;
    // 渠道基本信息表格
    $.ajax({
        url: "/cmca/zdtl/getChnlBaseInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                qudaoChnlBaseInfoTable = '<tr>' +
                    '<td>渠道标识</td><td>' + data.chnlBase.cor_chnl_id + '</td>' +
                    '<td>开业时间</td><td>' + data.chnlBase.busn_bgn_tm + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道名称</td><td>' + data.chnlBase.chnl_nm + '</td>' +
                    '<td>渠道状态</td><td>' + data.chnlBase.chnl_stat + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道类型</td><td>' + data.chnlBase.cor_chnl_typ + '</td>' +
                    '<td>渠道基础类型</td><td>' + data.chnlBase.chnl_basic_typ + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道星级</td><td>' + data.chnlBase.chnl_level + '</td>' +
                    '<td>区域形态</td><td>' + data.chnlBase.rgn_form + '</td>' +
                    '</tr><tr>' +
                    '<td>归属地市</td><td>' + data.chnlBase.CMCC_prvd_nm_short + '</td>' +
                    '<td>所属区域</td><td>' + data.chnlBase.chnl_addr + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道地址</td><td colspan="3">' + data.chnlBase.chnl_addr + '</td>' +
                    '</tr>';
                $('#zdgz_ChnlBaseInfoTable').empty().append(qudaoChnlBaseInfoTable);
            }
        }
    });
    // 近6个月渠道趋势
    $.ajax({
        url: "/cmca/zdtl/getChnlTrend",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#zdgz_qudaoChart').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)',
                    height: h
                },
                title: {
                    text: null
                },
                xAxis: [{
                    categories: data.audTrm,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{
                    labels: {
                        format: '{value} %',
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
                        format: '{value} 个',
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
                    floating: false,
                    layout: 'horizontal',
                    verticalAlign: 'bottom',
                },
                series: [{
                    name: '异常销售数量',
                    type: 'column',
                    yAxis: 1,
                    data: data.infractionNum,
                    tooltip: {
                        valueSuffix: ' 个'
                    }
                }, {
                    name: '异常销售占比',
                    type: 'spline',
                    data: data.qtyPercent,
                    tooltip: {
                        valueSuffix: '%'
                    }
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

// 统计报表-违规类型分布 
function load_fenxi_wglxfb_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val()
        },
        dataPie = [], //新建数组，接受饼图数据
        dataArea = []; //新建数据，接受堆积图数据
    //饼图标题
    $('#pieAudTrm').text($('#chooseTime').val());
    // 占比饼图
    $.ajax({
        url: "/cmca/zdtl/getTypeDistributePie",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}' && data.unpackingNum !== 0) {
                dataPie = [{
                        name: '拆包',
                        y: data.unpackingNum,
                        color: '#7CB5EC'
                    },
                    {
                        name: '跨省串货',
                        y: data.transProvinceNum,
                        color: '#90ED7D'
                    },
                    {
                        name: '沉默套利',
                        y: data.silentNum,
                        color: '#FF9933'
                    },
                    {
                        name: '养机套利',
                        y: data.keepMachineNum,
                        color: '#8085E9'
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
                        return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + '：</span><span>' + this.point.percentage.toFixed(2) + '%（' + Highcharts.numberFormat(this.point.y, 0, '.', ',') + ' 台）</span>';
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
                                // width: "70px",
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

    var h = $('#zhanbiQushiChartWrap').height() - 30;
    // 异常销售数量趋势图
    $.ajax({
        url: "/cmca/zdtl/getTypeDistributeStack",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataArea = [{
                        name: '拆包',
                        data: data.unpackingNum,
                        color: '#7CB5EC'
                    }, //拆包
                    {
                        name: '跨省串货',
                        data: data.transProvinceNum,
                        color: '#90ED7D'
                    }, //跨省串货
                    {
                        name: '沉默套利',
                        data: data.silentNum,
                        color: '#FF9933'
                    }, //沉默套利
                    {
                        name: '养机套利',
                        data: data.keepMachineNum,
                        color: '#8085E9'
                    } //养机套利
                ];
            }
            $('#zhanbiQushiChart').css('minWidth', data.audTrm.length * 18 + '%');
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
                    categories: data.audTrm,
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
                            s += '<br/>' + '<span style="color:' + this.series.color + '">' + this.series.name + '</span>：' + Highcharts.numberFormat(this.percentage, 2, '.', ',') + '% （' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 台）';
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
            $('#zhanbiQushiChartWrap').getNiceScroll(0).doScrollLeft(0);
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
            concern: $('#concern').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav2FiveNav1Con').height()) - 30;
    $.ajax({
        url: "/cmca/zdtl/getZDTLZgwzData",
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
                    // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                    title:'公司',
                    align: 'center',
                    valign: 'middle'
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
        url: "/cmca/zdtl/getRectifiyForSixColumn",
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

//六个月内达到问责标准次数排名（单位：次）
function load_fenxi_zgwz_wzbzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val()
    };
    // 六个月内达到问责标准次数排名（单位：次）
    $.ajax({
        url: "/cmca/zdtl/getAccountForSixColumn",
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
                    name: '问责次数',
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
        url: "/cmca/zdtl/getRectifiyColumn",
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

// 累计达到问责次数排名（单位：次）
function load_fenxi_zgwz_wzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 累计达到整改次数排名（单位：次）
    $.ajax({
        url: "/cmca/zdtl/getAccountabilityColumn",
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
                    name: '问责次数',
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
        url: "/cmca/zdtl/getRectifiyLine",
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

//达到问责标准省公司数量趋势（单位：个）
function load_fenxi_zgwz_wzbz_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 达到问责标准省公司数量趋势（单位：个）
    $.ajax({
        url: "/cmca/zdtl/getAccountabilityLine",
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
                    name: '问责公司数量',
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

// 统计分析-审计报告 社会渠道终端套利异常销售、套利
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav3Con').find('.audtrm_off,.audtrm_on').hide();
    // 定义所需变量
    var myData, NumMom3000, AmtMom3000, momText1, momText2, //文字说明所需数据
        h = parseInt($('#baogaoTop5Con').height()), //表格容器高度
        tableData = [], //定义数组接收表格数据
        auditDates = auditDate(), //审计时间
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
    $('#sjbgPeriod').text(auditDates[0] + ' -- ' + auditDates[1]); //审计期间

    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }

    // 请求数据
    $.ajax({
        url: "/cmca/zdtl/getReportInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (data.switchState != 'audTrmColseForReport') {
                $('#fenxiFourNav3Con .audtrm_on').show();
                if (JSON.stringify(data) != "{}" && data['3000'] != undefined) {
                    // 返回数据
                    myData = [
                        data["3001"],
                        data["3002"],
                        data["3004"],
                        data["3005"],
                        data["3000"]
                    ];

                    // 判断增加的值为正还是为负，如果为正，文字显示内容为正，如果为负，显示为减少
                    NumMom3000 = parseInt(myData[4].infractionNumMOM);
                    AmtMom3000 = parseInt(myData[4].infractionChnlPercentMOM);
                    momText1 = (NumMom3000 > 0) ? "上升" : "下降";
                    momText2 = (AmtMom3000 > 0) ? "上升" : "下降";

                    $('#baogaoTop5Title').text('社会渠道终端套利分关注点汇总');
                    // 审计发现文字说明 
                    $('#baogaoInfo').html('全网' + $('#chooseTime').val() +
                        '月份销售终端数量共计' + $.formatMoney(myData[4].tolSellNum, 0) + '台，' +
                        '审计识别出异常销售的终端数量' + $.formatMoney(myData[4].infractionNum, 0) + '台，' +
                        '占比' + $.formatMoney(myData[4].qtyPercent, 2) + '%,' +
                        '比上期' + momText1 + $.formatMoney(Math.abs(NumMom3000), 0) + '个百分点;' +
                        '异常销售的终端中存在套利行为的终端为' + $.formatMoney(myData[4].infractionNumCJ, 0) + '台；' +
                        '异常销售终端涉及社会渠道' + $.formatMoney(myData[4].infractionChnlNum, 0) + '个，' +
                        '占比' + $.formatMoney(myData[4].infractionChnlPercent, 2) + '%，' +
                        '比上期' + momText2 + $.formatMoney(Math.abs(AmtMom3000), 0) + '个百分点。'
                    );

                    // 审计报告表格部分
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
                            field: 'infractionNum',
                            title: '异常销售数量（台）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter0
                        }, {
                            field: 'qtyPercent',
                            title: '异常销售数量占比（%）',
                            align: 'center',
                            valign: 'middle',
                        }, {
                            field: 'infractionNumCJ',
                            title: '套利数量（台）',
                            align: 'center',
                            valign: 'middle',
                            formatter: operateFormatter0
                        }, {
                            field: 'qtyPercentCJ',
                            title: '套利数量占比（%）',
                            align: 'center',
                            valign: 'middle',
                        }]
                    });
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

// 地图下钻-地市终端套利信息
function load_cty_qtyPercentInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $("#dialogQudaoTable").bootstrapTable('resetView');
    var postData = {
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height());
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/zdtl/getChnlTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.zdtlChnl,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '序号',
                    align: 'center',
                    valign: 'middle',
                    width: '8%',
                    formatter: rn
                }, {
                    field: 'chnlName',
                    title: '渠道名称',
                    align: 'center',
                    valign: 'middle',
                    width: '30%',
                    formatter: tagColumn
                }, {
                    field: 'chnlType',
                    title: '渠道类型',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infractionNum',
                    title: '异常销售数量（台）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'qtyPercent',
                    title: '异常销售占比',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatterB
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
            // $('#dialogQudaoTable').parent('.fixed-table-body').niceScroll({
            //     'cursorcolor': '#f8a900',
            //     'cursorborderradius': '0',
            //     'background': '#353741',
            //     'cursorborder': 'none',
            //     'autohidemode': false,
            //     'enablescrollonselection': true
            // });

            /**
             * 定义点击可下钻列-渠道名称
             * formatter这个属性属于列参数，意思就是对当前列的数据进行格式化操作，它是一个函数，有三个参数，value，row，index
             * value:代表当前单元格中的值
             * row：代表当前行
             * index:代表当前行的下标,
             */
            function tagColumn(value, row, index) {
                return '<a href="javascript:;" value="' + row.chnlId + '">' + value + '</a>';
            }
        }
    });
}

// 地图下钻-地市终端套利信息-搜索
function load_cty_searchInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $("#dialogQudaoTable").bootstrapTable('resetView');
    var postData = {
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            chnlName: $('#qudaoName').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height());
    /**
     * 定义点击可下钻列-渠道名称
     * formatter这个属性属于列参数，意思就是对当前列的数据进行格式化操作，它是一个函数，有三个参数，value，row，index
     * value:代表当前单元格中的值
     * row：代表当前行
     * index:代表当前行的下标,
     */
    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.chnlId + '">' + value + '</a>';
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/zdtl/getChnlByChnlName",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.zdtlChnl,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '序号',
                    align: 'center',
                    valign: 'middle',
                    width: '12%'
                }, {
                    field: 'chnlName',
                    title: '渠道名称',
                    align: 'center',
                    valign: 'middle',
                    width: '30%',
                    formatter: tagColumn
                }, {
                    field: 'chnlType',
                    title: '渠道类型',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infractionNum',
                    title: '异常销售数量（台）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'qtyPercent',
                    title: '异常销售占比',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatterB
                    //sortable: true
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
            // $('#dialogQudaoTable').parent('.fixed-table-body').niceScroll({
            //     'cursorcolor': '#f8a900',
            //     'cursorborderradius': '0',
            //     'background': '#353741',
            //     'cursorborder': 'none',
            //     'autohidemode': false,
            //     'enablescrollonselection': true
            // });
        }
    });
}

// 地市终端套利信息下钻-渠道基本信息
function load_chnlInfo_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
        chnlId: $('#chnlId').val(),
        concern: $('#concern').val(),
        prvdId: $('#prvdId').val()
    };
    // 渠道基本信息表格
    $.ajax({
        url: "/cmca/zdtl/getChnlBaseInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                var ChnlBaseInfoTable = '<tr>' +
                    '<td>渠道标识</td><td>' + data.chnlBase.cor_chnl_id + '</td>' +
                    '<td>开业时间</td><td>' + data.chnlBase.busn_bgn_tm + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道名称</td><td>' + data.chnlBase.chnl_nm + '</td>' +
                    '<td>渠道状态</td><td>' + data.chnlBase.chnl_stat + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道类型</td><td>' + data.chnlBase.cor_chnl_typ + '</td>' +
                    '<td>渠道基础类型</td><td>' + data.chnlBase.chnl_basic_typ + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道星级</td><td>' + data.chnlBase.chnl_level + '</td>' +
                    '<td>区域形态</td><td>' + data.chnlBase.rgn_form + '</td>' +
                    '</tr><tr>' +
                    '<td>归属地市</td><td>' + data.chnlBase.CMCC_prvd_nm_short + '</td>' +
                    '<td>所属区域</td><td>' + data.chnlBase.chnl_addr + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道地址</td><td colspan="3">' + data.chnlBase.chnl_addr + '</td>' +
                    '</tr>';
                $('#ChnlBaseInfoTable').empty().append(ChnlBaseInfoTable);
            }
        }
    });
    // 近6个月渠道趋势
    $.ajax({
        url: "/cmca/zdtl/getChnlTrend",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#qudaoChart').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: [{
                    categories: data.audTrm,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xFontSize()
                        }
                    }
                }],
                yAxis: [{
                    labels: {
                        format: '{value} %',
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
                        format: '{value} 台',
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
                    floating: false,
                    layout: 'horizontal',
                    verticalAlign: 'bottom',
                },
                series: [{
                    name: '异常销售数量',
                    type: 'column',
                    yAxis: 1,
                    data: data.infractionNum,
                    tooltip: {
                        valueSuffix: ' 台'
                    }
                }, {
                    name: '异常销售占比',
                    type: 'spline',
                    data: data.qtyPercent,
                    tooltip: {
                        valueSuffix: '%'
                    }
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

// 地图下方卡片
function load_mapBtm_card_chart() {
    // 定义所需变量
    var prvdId = $('#prvdId').val(),
        infractionNum = 0, //异常销售数量
        infractionNumMom = 0, //异常销售数量增量
        qtyPercent = 0, //异常销售占比
        qtyPercentMom = 0, //异常销售占比增量
        infractionChnlNum = 0, //异常销售渠道数量
        infractionChnlNumMom = 0, //异常销售渠道数量增量
        infractionChnlPercent = 0, //异常销售渠道数量占比
        infractionChnlPercentMom = 0, //异常销售渠道数量占比增量
        // 后台请求参数
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    $.ajax({
        url: "/cmca/zdtl/getMapBottomData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                infractionNum = $.formatMoney(data[prvdId].infractionNum, 2);
                infractionNumMom = data[prvdId].infractionNumMom;
                qtyPercent = $.formatMoney(data[prvdId].qtyPercent, 2);
                qtyPercentMom = data[prvdId].qtyPercentMom;
                infractionChnlNum = $.formatMoney(data[prvdId].infractionChnlNum, 0);
                infractionChnlNumMom = data[prvdId].infractionChnlNumMom;
                infractionChnlPercent = $.formatMoney(data[prvdId].infractionChnlPercent, 2);
                infractionChnlPercentMom = data[prvdId].infractionChnlPercentMom;
            }
            // 所有增量如果是降低的，可能是负值，做判断改变示意图标，显示绝对值
            // 异常销售数量
            if (infractionNumMom <= 0 || infractionNumMom === null) {
                $('#card1').html(infractionNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionNumMom), 2) + '</span>');
            } else {
                $('#card1').html(infractionNum + '<span><i class="caret_up"></i>' + $.formatMoney(infractionNumMom, 2) + '</span>');
            }
            // 异常销售占比
            if (qtyPercentMom <= 0 || qtyPercentMom === null) {
                $('#card2').html(qtyPercent + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(qtyPercentMom), 2) + '%</span>');
            } else {
                $('#card2').html(qtyPercent + '%<span><i class="caret_up"></i>' + $.formatMoney(qtyPercentMom, 2) + '%</span>');
            }
            // 异常销售渠道数量
            if (infractionChnlNumMom <= 0 || infractionChnlNumMom === null) {
                $('#card3').html(infractionChnlNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionChnlNumMom), 0) + '</span>');
            } else {
                $('#card3').html(infractionChnlNum + '<span><i class="caret_up"></i>' + $.formatMoney(infractionChnlNumMom, 0) + '</span>');
            }
            // 异常销售渠道数量占比
            if (infractionChnlPercentMom <= 0 || infractionChnlPercentMom === null) {
                $('#card4').html(infractionChnlPercent + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(infractionChnlPercentMom), 2) + '%</span>');
            } else {
                $('#card4').html(infractionChnlPercent + '%<span><i class="caret_up"></i>' + $.formatMoney(infractionChnlPercentMom, 2) + '%</span>');
            }
        }
    });
}

/**
 * 绘制地图
 * 地图数据$.chinaMapData(),位于cmca-common.js中；
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
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            concern: $('#concern').val(),
            parameterType: $('#target').val()
        };
    // 请求全国各省数据
    $.ajax({
        url: "/cmca/zdtl/getMapData",
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
                            order = data[prvdCode].rn;
                            switch (orderName) {
                                case 'ycxszb': //当指标是异常销售终端占比排名
                                    map.properties.trueValue = data[prvdCode].qtyPercent;
                                    break;
                                case 'ycxs': //当指标是异常销售终端数量排名
                                    map.properties.trueValue = data[prvdCode].infractionNum;
                                    break;
                                case 'ycxsqdzb': //当指标是异常销售渠道数量占比
                                    map.properties.trueValue = data[prvdCode].infractionChnlPercent;
                                    break;
                                case 'ycxsqd': //当指指标是异常销售渠道数量
                                    map.properties.trueValue = data[prvdCode].infractionChnlNum;
                                    break;
                            }
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
                        // 下钻
                        drilldown: function (e) {
                            prvdId = e.point.properties.id;
                            // 异步下钻
                            if (e.point.drilldown && unDrilldown.indexOf(e.point.drilldown) === -1) {
                                // 插入一经事件码-查询
                                dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                                // 日志记录
                                get_userBehavior_log('专题', '终端套利', '地图下钻', '查询');

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
                                            concern: $('#concern').val(),
                                            parameterType: $('#target').val()
                                        };
                                        // 请求下钻省数据
                                        $.ajax({
                                            url: "/cmca/zdtl/getMapData",
                                            type: 'get',
                                            dataType: 'json',
                                            data: postData,
                                            success: function (ctyData) {
                                                // 地图颜色
                                                if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                                                    Highcharts.each(ctyMap, function (map) {
                                                        map.value = 8;
                                                        ctyCode = map.properties.code;
                                                        // 后台返回的数据可能某个地市没有数据，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错
                                                        // 如果是直辖市，则整个直辖市区域所有区县都显示为红色
                                                        if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                            map.value = 2;
                                                            map.properties.trueValue = e.point.properties.trueValue;
                                                        } else { // 不是直辖市
                                                            //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                                            if (ctyData[ctyCode] !== undefined) {
                                                                order = ctyData[ctyCode].rn;
                                                                switch (orderName) {
                                                                    case 'ycxszb': //当指标是异常销售终端占比排名
                                                                        map.properties.trueValue = ctyData[ctyCode].qtyPercent;
                                                                        break;
                                                                    case 'ycxs': //当指标是异常销售终端数量排名
                                                                        map.properties.trueValue = ctyData[ctyCode].infractionNum;
                                                                        break;
                                                                    case 'ycxsqdzb': //当指标是异常销售渠道数量占比
                                                                        map.properties.trueValue = ctyData[ctyCode].infractionChnlPercent;
                                                                        break;
                                                                    case 'ycxsqd': //当指标是异常销售渠道数量
                                                                        map.properties.trueValue = ctyData[ctyCode].infractionChnlNum;
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
                                                    events: {
                                                        click: function (e) {
                                                            // 插入一经事件码-查询
                                                            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                                                            //地市地图下钻表格弹层
                                                            $('#mapTableDialog').show();
                                                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '市终端套利信息');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(prvdId);
                                                            } else {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '省' + e.point.name + '市终端套利信息');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(e.point.properties.code);
                                                            }
                                                            // 加载数据
                                                            load_cty_qtyPercentInfo_table();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        // 返回
                        drillup: function () {
                            // 插入一经事件码-查询
                            dcs.addEventCode('MAS_HP_CMCA_child_query_02');

                            // 样式
                            $('#nanhaiQundao').show();
                            $('#chooseProvince').val('全公司');
                            mapTitle();
                            // 顶部提示按钮显示返回按钮隐藏
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
                legend: {
                    align: 'center',
                    verticalAlign: 'bottom',
                    itemStyle: {
                        "fontSize": $.xFontSize()
                    },
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
                tooltip: { //鼠标悬浮提示
                    formatter: function () {
                        cnm = this.point.properties.cnname;
                        nm = this.point.name;
                        dt = this.point.properties.trueValue;
                        prvdNameZH = $('#prvdNameZH').val();
                        if ($('#prvdId').val() != 10000) {
                            cnm = this.point.properties.name;
                        }
                        if (nm == "Taiwan" || nm == "HongKong" || nm == "Macau" || dt == -1 || dt == null) {
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/>没有数据';
                            } else {
                                return '<span>' + cnm + '</span><br/>没有数据';
                            }
                        } else {
                            switch (orderName) {
                                case 'ycxszb': //异常销售占比
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>异常销售占比：' + dt + '%</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>异常销售占比：' + dt + '%</span>';
                                    }
                                    break;
                                case 'ycxs': //异常销售数量
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>异常销售数量：' + dt + ' 万台</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>异常销售数量：' + dt + ' 万台</span>';
                                    }
                                    break;
                                case 'ycxsqdzb': //异常销售渠道占比
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>异常销售渠道占比：' + dt + '%</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>异常销售渠道占比：' + dt + '%</span>';
                                    }
                                    break;
                                case 'ycxsqd': //异常销售渠道数量
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>异常销售渠道数量：' + dt + ' 个</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>异常销售渠道数量：' + dt + ' 个</span>';
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
            concern: $('#concern').val(),
            parameterType: $('#target').val()
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
                url: "/cmca/zdtl/getMapData",
                type: 'get',
                dataType: 'json',
                data: postData,
                success: function (ctyData) {
                    // 地图颜色
                    if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                        Highcharts.each(ctyMap, function (map) {
                            map.value = 8;
                            ctyCode = map.properties.code;
                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                map.value = 2;
                                switch (orderName) {
                                    case 'ycxszb': //当指标是异常销售渠道数量占比
                                        map.properties.trueValue = ctyData[prvdId].qtyPercent;
                                        break;
                                    case 'ycxs': //当指标是异常销售终端数量排名 
                                        map.properties.trueValue = ctyData[prvdId].infractionNum;
                                        break;
                                    case 'ycxsqdzb': //当指标是异常销售渠道数量占比
                                        map.properties.trueValue = ctyData[prvdId].infractionChnlPercent;
                                        break;
                                    case 'ycxsqd': //当指标是异常销售渠道数量
                                        map.properties.trueValue = ctyData[prvdId].infractionChnlNum;
                                        break;
                                }
                            } else { // 不是直辖市
                                //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    order = ctyData[ctyCode].rn;
                                    switch (orderName) {
                                        case 'ycxszb': //当指标是异常销售渠道数量占比
                                            map.properties.trueValue = ctyData[ctyCode].qtyPercent;
                                            break;
                                        case 'ycxs': //当指标是异常销售终端数量排名 
                                            map.properties.trueValue = ctyData[ctyCode].infractionNum;
                                            break;
                                        case 'ycxsqdzb': //当指标是异常销售渠道数量占比
                                            map.properties.trueValue = ctyData[ctyCode].infractionChnlPercent;
                                            break;
                                        case 'ycxsqd': //当指标是异常销售渠道数量
                                            map.properties.trueValue = ctyData[ctyCode].infractionChnlNum;
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
                                if (dt == -1 || dt == null) {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return prvdNameZH + "<br/>没有数据";
                                    } else {
                                        return cnm + "<br/>没有数据";
                                    }
                                } else {
                                    switch (orderName) {
                                        case 'ycxszb': //当指标是异常销售渠道数量占比
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>异常销售占比：' + dt + '%</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>异常销售占比：' + dt + '%</span>';
                                            }
                                            break;
                                        case 'ycxs': //当指标是异常销售终端数量排名 
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>异常销售数量：' + dt + ' 万台</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>异常销售数量：' + dt + ' 万台</span>';
                                            }
                                            break;
                                        case 'ycxsqdzb': //当指标是异常销售渠道数量占比
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>异常销售渠道占比：' + dt + '%</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>异常销售渠道占比：' + dt + '%</span>';
                                            }
                                            break;
                                        case 'ycxsqd': //当指标是异常销售渠道数量
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>异常销售渠道数量：' + dt + ' 个</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>异常销售渠道数量：' + dt + ' 个</span>';
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
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '市终端套利信息');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(prvdId);
                                    } else {
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '省' + e.point.name + '市终端套利信息');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(e.point.properties.code);
                                    }
                                    // 加载数据
                                    load_cty_qtyPercentInfo_table();
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