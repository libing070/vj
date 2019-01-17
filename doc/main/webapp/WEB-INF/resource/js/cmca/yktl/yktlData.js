// 审计结果-养卡号码占比排名
function load_result_hmzb_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yktl/getCardPercentPm",
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
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '%';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2) + '%</b>';
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
                series: [{
                    name: '养卡号码数量占比',
                    data: data.perList,
                    color: "#3095f2"
                }],
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

// 审计结果-养卡号码数量排名
function load_result_hmsl_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yktl/getCardNumbersPm",
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
                    labels: {
                        formatter: function () {
                            return this.value / 1;
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万个</b>';
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
                lang: {
                    noData: "暂无数据" //无数据显示
                },
                noData: {
                    style: {
                        color: '#c2c2c2'
                    }
                },
                series: [{
                    name: '养卡号码个数',
                    data: data.numberList,
                    color: "#3095f2"
                }],
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

// 审结结果-养卡渠道占比排名
function load_result_qdzd_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yktl/getChnlPercentPm",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow2').css('minWidth', data.prvdList.length * 10 + '%');
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
                    name: '养卡渠道占比',
                    data: data.percentList,
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

// 审结结果-养卡渠道数量排名
function load_result_qdsl_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/yktl/getChnlNumbersPm",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow2').css('minWidth', data.prvdList.length * 10 + '%');
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
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 个</b>';
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
                    name: '养卡渠道数量',
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

// 审计结果-养卡号码占比趋势
function load_result_hmzb_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/yktl/getCardPercentPmLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.audTrmList.length * 5 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrmList,
                    gridLineWidth: 1,
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
                legend: {
                    enabled: false
                },
                series: [{
                    name: '养卡号码数量占比',
                    data: data.percentList,
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

// 审计结果-养卡号码数量趋势
function load_result_hmsl_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/yktl/getCardNumPmLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.audTrmList.length * 5 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrmList,
                    gridLineWidth: 1,
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
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万个</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '养卡号码数量',
                    data: data.numList,
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

// 审计结果-养卡渠道占比趋势
function load_result_qdzb_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/yktl/getChanlPercentPmLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audTrmList.length * 5 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrmList,
                    gridLineWidth: 1,
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
                legend: {
                    enabled: false
                },
                series: [{
                    name: '养卡号码涉及渠道占比',
                    data: data.percentList,
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

// 审计结果-养卡渠道数量趋势
function load_result_qdsl_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };
    $.ajax({
        url: "/cmca/yktl/getChnlNumsPmLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audTrmList.length * 5 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audTrmList,
                    gridLineWidth: 1,
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
                series: [{
                    name: '养卡渠道数量',
                    data: data.numList,
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
    /**
     * 因为页面首次加载的时候养卡号码占比排名和养卡渠道占比排名左右两个图形全部加载
     * 所以绘制审计结果下四个图形时判断左右两个图形哪个处于显示状态，以便在下拉列表区域选择/时间选择变化的时候加载对应的图形
     */
    if ($('#numTarget').val() == 'numPercentOrder') { //养卡号码数量占比排名
        load_result_hmzb_chart();
    } else if ($('#numTarget').val() == 'numAmtOrder') { //养卡号码数量排名
        load_result_hmsl_chart();
    }
    if ($('#chnlTarget').val() == 'chnlPercentOrder') { //养卡渠道占比排名
        load_result_qdzd_chart();
    } else if ($('#chnlTarget').val() == 'chnlAmtOrder') { //养卡渠道数量排名
        load_result_qdsl_chart();
    }
    if ($('#numTrendTarget').val() == 'numPercentTrend') { //养卡号码占比趋势
        load_result_hmzb_qs_chart();
    } else if ($('#numTrendTarget').val() == 'numAmtTrend') { //养卡号码数量趋势
        load_result_hmsl_qs_chart();
    }
    if ($('#chnlTrendTarget').val() == 'chnlPercentTrend') { //养卡渠道占比趋势
        load_result_qdzb_qs_chart();
    } else if ($('#chnlTrendTarget').val() == 'chnlAmtTrend') { //养卡渠道数量趋势
        load_result_qdsl_qs_chart();
    }
}

// 统计分析-统计报表-排名汇总表
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
    // 千分位格式化，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    //全国显示省公司为10000时，显示全国
    function judgePrvdName(value, row, index) {
        return ((value === '10000') ? '全国' : value);
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQdykDataPm",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#rankingAllTable").bootstrapTable({
                    datatype: "local",
                    data: data.pmdata, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '序号',
                        width: '6%',
                        valign: 'middle',
                        formatter: rn
                    }, {
                        field: 'prvdName',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        valign: 'middle',
                        width: '10%',
                        formatter: judgePrvdName
                    }, {
                        field: 'cntArea',
                        title: '养卡涉及地市公司数量（个）',
                        valign: 'middle',
                        sortable: true,
                        width: '12%'
                    }, {
                        field: 'errQty',
                        title: '养卡数量（个）',
                        sortable: true,
                        valign: 'middle',
                        width: '12%',
                        formatter: operateFormatter0
                    }, {
                        field: 'qtyPercent',
                        title: '养卡号码数量占比',
                        sortable: true,
                        valign: 'middle',
                        width: '12%',
                        formatter: operateFormatterB
                    }, {
                        field: 'fbQtyPerMonth',
                        title: '养卡占比增幅',
                        valign: 'middle',
                        width: '11%',
                        sortable: true,
                        formatter: operateFormatterB
                    }, {
                        field: 'chnlQty',
                        title: '涉及渠道数量（个）',
                        valign: 'middle',
                        width: '12%',
                        sortable: true
                    }, {
                        field: 'chnlPercent',
                        title: '涉及渠道占比',
                        valign: 'middle',
                        width: '12%',
                        sortable: true,
                        formatter: operateFormatterB
                    }, {
                        field: 'fbChnlPerMonth',
                        title: '渠道占比增幅',
                        valign: 'middle',
                        width: '11%',
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
            audTrm: $('#audTrm').val()
        },
        lastDataName,
        lastDataY,
        newLastData,
        dataZlfx,
        waterfallH = $('#pubuChart').closest('.pubu_show').height() - 40;
    $.ajax({
        url: "/cmca/yktl/getIncrementalData",
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
                    text: '养卡增量分析（个）'
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
                        return '<span>' + '</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 个</b>';
                    }
                },
                plotOptions: {
                    series: {
                        pointWidth: $.pubuPointW()
                    }
                },
                series: [{
                    //name: "",
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
    var myData,
        postData = {
            audTrm: $('#audTrm').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav3Con').height()),
        time = $("#audTrm").val(),
        audTrms; //月份递减
        if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
    // 千分位格式化，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }

    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.chnl_id + '" data="' + row.regionId + '">' + value + '</a>';
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQDYKConcernChnl",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#tuchuQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.chnlTable,
                pagination: false,
                cache: false,
                height: h,
                columns: [
                    [{
                        field: 'chnl_no',
                        title: '序号',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2
                    }, {
                        field: 'chnlName',
                        title: '渠道名称',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2,
                        formatter: tagColumn
                    }, {
                        field: 'prvd_name',
                        title: '公司',
                        align: 'center',
                        colspan: 1,
                        rowspan: 2,
                        valign: 'middle'
                    }, {
                        title: audTrms[0],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1
                    }, {
                        title: audTrms[1],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1
                    }, {
                        title: audTrms[2],
                        align: 'center',
                        colspan: 3,
                        rowspan: 1,
                        valign: 'middle'
                    }],
                    [{
                        field: 'tol',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'tol1',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err1',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb1',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'tol2',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err2',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb2',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
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
    // 定义所需变量
    var postData = {
            audTrm: $('#audTrm').val(),
            chnlId: $('#chnlId').val(),
            prvdId: $('#regionId').val()
        },
        qudaoBaseInfoTable,
        h = $('#qudaoQSChart').closest('.info_item').height() - 20;
    // 渠道养卡基本信息表格
    $.ajax({
        url: "/cmca/yktl/getQdykChnlBaseInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                qudaoBaseInfoTable = '<tr>' +
                    '<td>渠道标识</td><td>' + data.chnlInfo.cor_chnl_id + '</td>' +
                    '<td>开业时间</td><td>' + data.chnlInfo.busn_bgn_tm + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道名称</td><td>' + data.chnlInfo.chnl_nm + '</td>' +
                    '<td>渠道状态</td><td>' + data.chnlInfo.chnl_stat + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道类型</td><td>' + data.chnlInfo.cor_chnl_typ + '</td>' +
                    '<td>渠道基础类型</td><td>' + data.chnlInfo.chnl_basic_typ + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道星级</td><td>' + data.chnlInfo.chnl_level + '</td>' +
                    '<td>区域形态</td><td>' + data.chnlInfo.rgn_form + '</td>' +
                    '</tr><tr>' +
                    '<td>归属地市</td><td>' + data.chnlInfo.CMCC_prvd_nm_short + '</td>' +
                    '<td>所属区县</td><td>' + data.chnlInfo.cnty_nm + '</td>' +
                    '</tr><tr>' +
                    '<td>渠道地址</td><td colspan="3">' + data.chnlInfo.chnl_addr + '</td>' +
                    '</tr>';
                $('#qudaoInfoTable').empty().append(qudaoBaseInfoTable);
            }
        }
    });
    // 渠道养卡趋势图
    $.ajax({
        url: "/cmca/yktl/getQdykFocusChnlTrend",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#qudaoQSChart').highcharts({
                chart: {
                    zoomType: 'xy',
                    backgroundColor: 'rgba(0,0,0,0)',
                    height: h
                },
                title: {
                    text: null
                },
                xAxis: [{
                    categories: data.audtrmList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
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
                    name: '养卡号码数量',
                    type: 'column',
                    yAxis: 1,
                    data: data.numList,
                    tooltip: {
                        valueSuffix: ' 个'
                    }
                }, {
                    name: '养卡号码数量占比',
                    type: 'spline',
                    data: data.perList,
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

// 统计分析-统计报表-重点关注营销案 
function load_fenxi_zdgz_case_table() {
    $('#tuchuCaseTable').bootstrapTable('destroy');
    $('#tuchuCaseTable').bootstrapTable('resetView');
    var myData, postData = {
            audTrm: $('#audTrm').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav4Con').height()),
        time = $("#audTrm").val(),
        audTrms ; //月份递减
        if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
    // 千分位格式化，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }

    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.offer_cd + '" data="' + row.regionId + '">' + value + '</a>';
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQDYKConcernOffer",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#tuchuCaseTable").bootstrapTable({
                datatype: "local",
                data: data.offerTable,
                pagination: false,
                cache: false,
                height: h,
                columns: [
                    [{
                        field: 'offer_no',
                        title: '序号',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2
                    }, {
                        field: 'offerName',
                        title: '营销案名称',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2,
                        formatter: tagColumn

                    }, {
                        field: 'prvd_name',
                        title: '公司',
                        align: 'center',
                        colspan: 1,
                        rowspan: 2,
                        valign: 'middle'
                    }, {
                        title: audTrms[0],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1
                    }, {
                        title: audTrms[1],
                        align: 'center',
                        valign: 'middle',
                        colspan: 3,
                        rowspan: 1
                    }, {
                        title: audTrms[2],
                        align: 'center',
                        colspan: 3,
                        rowspan: 1,
                        valign: 'middle'
                    }],
                    [{
                        field: 'tol',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'tol1',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err1',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb1',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'tol2',
                        title: '入网数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'err2',
                        title: '养卡数量（个）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'zb2',
                        title: '养卡号码数量占比',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatterB
                    }]
                ]
            });
            $('#tuchuCaseTable').parent('.fixed-table-body').attr('id', 'tuchuCaseTableWrap');
            // $('#tuchuCaseTable thead').remove();
            scroll('#tuchuCaseTableWrap', '#tuchuCaseTable');
        }
    });
}

// 统计分析-统计报表-重点关注营销案-表格下钻图表
function load_fenxi_zdgz_caseInfo_chart() {
    //隐藏上级滚动条
    $('#tuchuCaseTable').parent('.fixed-table-body').getNiceScroll().hide();
    // 定义所需变量
    var postData = {
            audTrm: $('#audTrm').val(),
            chnlId: $('#chnlId').val(),
            prvdId: $('#regionId').val()
        },
        h1 = $('#caseYangkaNum').height(),
        h2 = $('#caseYangkaRatio').height();
    // 养卡数量折线图
    $.ajax({
        url: "/cmca/yktl/getQdykFocusOfferTrend",
        dataType: 'json',
        data: postData,
        h: h1,
        success: function (data) {
            $('#caseNumChart').highcharts({
                chart: {
                    type: 'line',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '养卡数量趋势'
                },
                xAxis: {
                    categories: data.audtrmList,
                    crosshair: true,
                    labels: {
                        // rotation: 0//调节倾斜角度偏移
                        style: {
                            fontSize: $.xNumFontSize()
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: ''
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1 + '';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + ' : </span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 个</b>';
                    },
                    shared: true,
                    useHTML: true,
                    valueDecimals: 2,
                    shadow: false
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: [{
                    name: '养卡号码数量趋势',
                    data: data.numList,
                    color: "#3095f2"
                }],
                credits: {
                    enabled: false
                },
                exporting: {
                    enabled: false
                }
            });
        }
    });
    // 养卡占比折线图
    $.ajax({
        url: "/cmca/yktl/getQdykFocusOfferTrend",
        dataType: 'json',
        data: postData,
        h: h2,
        success: function (data) {

            $('#casePerChart').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                subtitle: {
                    text: '养卡占比趋势'
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
                credits: {
                    enabled: false
                },
                tooltip: {
                    valueSuffix: '%',
                    shadow: false
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: '养卡号码数量占比',
                    data: data.perList,
                    color: '#00c58e'
                }],
                exporting: {
                    enabled: false
                }
            });
        }
    })
}

// 统计分析-统计报表-养卡渠道类型分布
function load_fenxi_ykqd_type_chart() {
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
        url: "/cmca/yktl/getQDYKqdlxData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataPie = [{
                        name: '社会渠道',
                        y: data.shqudao,
                        color: '#7CB5EC'
                    },
                    {
                        name: '自有渠道',
                        y: data.zyqudao,
                        color: '#90ED7D'
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
                        return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + '：</span>' + this.point.percentage.toFixed(2) + '%（' + Highcharts.numberFormat(this.point.y, 2, '.', ',') + ' 万个）';
                    },
                    useHTML: true
                },
                plotOptions: {
                    pie: {
                        dataLabels: {
                            enabled: true,
                            useHTML: true,
                            formatter: function () {
                                return '<span style="color:#fff;font-weight:lighter;white-space:pre-line;" >' + this.point.name + '：' + this.point.percentage.toFixed(2) + '%</span>';
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
                        allowPointSelect: false,
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

    // 2016年4月-所选审计月时间期间的数据
    $.ajax({
        url: "/cmca/yktl/getQDYKqdlxDuiData",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataArea = [{
                        name: '社会渠道',
                        data: data.shqudao,
                        color: '#7CB5EC'
                    },
                    {
                        name: '自有渠道',
                        data: data.zyqudao,
                        color: '#90ED7D'
                    }
                ];
                $('#zhanbiQushiChart').css('minWidth', data.audtrm.length * 18 + '%');
            }
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
                    categories: data.audtrm,
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
                            s += '<br/>' + '<span style="fontWeight:400;color:' + this.series.color + '">' + this.series.name + '</span>：' + Highcharts.numberFormat(this.percentage, 2, '.', ',') + '% （' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 万个）';
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
    $("#wenzeTableTitle").html('养卡套利整改问责表');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            concern: 2000
        },
        h = parseInt($('#fenxiFourNav2FiveNav1Con').height()) - 30;
    $.ajax({
        url: "/cmca/yktl/getQDYKZgwzData",
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
                columns: [{
                    field: 'order',
                    title: '编号',
                    align: 'center',
                    valign: 'middle',
                    width: '8%'
                }, {
                    field: 'prvdName',
                    title: '公司',
                    align: 'center',
                    valign: 'middle',
                    width: '10%'
                }, {
                    field: 'wtSummary',
                    title: '问题概要',
                    align: 'center',
                    valign: 'middle'
                    // width: '15%'
                }, {
                    field: 'wtDetails',
                    title: '问题详细描述',
                    align: 'center',
                    valign: 'middle'
                    // width: '18%'
                }, {
                    field: 'zgOpinion',
                    title: '整改建议',
                    align: 'center',
                    valign: 'middle'
                    //width: '18%'
                }, {
                    field: 'wzRequire',
                    title: '问责要求',
                    align: 'center',
                    valign: 'middle'
                    //width: '8%'
                }, {
                    field: 'zgsj',
                    title: '整改时间',
                    align: 'center',
                    valign: 'middle'
                    // width: '8%'
                }, {
                    field: 'zgqx',
                    title: '整改期限',
                    align: 'center',
                    valign: 'middle'
                    // width: '10%'
                }]
            });
            $('#wenzeTable').parent('.fixed-table-body').attr('id', 'wenzeTableWrap');
            // $('#wenzeTable thead').remove();
            scroll('#wenzeTableWrap', '#wenzeTable');
        }
    });
}

//六个月内达到整改标准次数排名（单位：次）
function load_fenxi_zgwz_zgbzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
        focusCd: $('#focusCd').val()
    };
    // 六个月内达到整改标准次数排名（单位：次）
    $.ajax({
        url: "/cmca/yktl/getQDYKZgwzTjSixMonth",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#stageAccOrder').css('minWidth', data.prvdname.length * 10 + '%');
            $('#stageAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.prvdname,
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
                    data: data.zgnum,
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
        audTrm: $('#audTrm').val(),
        focusCd: $('#focusCd').val()
    };
    // 六个月内达到问责标准次数排名（单位：次）
    $.ajax({
        url: "/cmca/yktl/getQDYKZgwzTjSixMonth",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#stageAccOrder').css('minWidth', data.prvdname.length * 10 + '%');
            $('#stageAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.prvdname,
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
                    data: data.zgnum,
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
        focusCd: $('#focusCd').val()
    };
    // 累计达到整改次数排名（单位：次）
    $.ajax({
        url: "/cmca/yktl/getQDYKZgwzTj",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#addUpAccOrder').css('minWidth', data.prvdname.length * 10 + '%');
            $('#addUpAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },

                xAxis: {
                    categories: data.prvdname,
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
                    data: data.zgnum,
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
        focusCd: $('#focusCd').val()
    };
    // 累计达到整改次数排名（单位：次）
    $.ajax({
        url: "/cmca/yktl/getQDYKZgwzTj",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#addUpAccOrder').css('minWidth', data.prvdname.length * 10 + '%');
            $('#addUpAccOrder').highcharts({
                chart: {
                    type: 'column',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },

                xAxis: {
                    categories: data.prvdname,
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
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0) + ' 个</b>';
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
                    data: data.zgnum,
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
        focusCd: $('#focusCd').val()
    };
    // 达到整改标准省公司数量趋势（单位：个）
    $.ajax({
        url: "/cmca/yktl/getQDYKSjzgTjLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#accTrend').css('minWidth', data.audtrm.length * 5 + '%');
            $('#accTrend').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audtrm,
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
                    data: data.zgnum,
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
        focusCd: $('#focusCd').val()
    };
    // 达到问责标准省公司数量趋势（单位：个）
    $.ajax({
        url: "/cmca/yktl/getQDYKSjzgTjLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#accTrend').css('minWidth', data.audtrm.length * 5 + '%');
            $('#accTrend').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                xAxis: {
                    categories: data.audtrm,
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
                    data: data.zgnum,
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

// 统计分析-审计整改问责-整改问责统计
function load_fenxi_zgwz_statis_chart() {
    // 整改问责统计样式初始化
    $('#zgbzcsBtn').addClass('active_btn').siblings().removeClass('active_btn');
    $('#zgcsPmBtn').addClass('active_btn').siblings().removeClass('active_btn');
    $('#zgbzBtn').addClass('active_btn').siblings().removeClass('active_btn');
    $('#zgbzcsBtn').parent('div').prev('p').text('六个月内达到整改标准次数排名（次）');
    $('#zgcsPmBtn').parent('div').prev('p').text('累计达到整改次数排名（次）');
    $('#zgbzBtn').parent('div').prev('p').text('达到整改标准省公司数量趋势（个）');

    //六个月内达到整改标准次数排名（单位：次）
    load_fenxi_zgwz_zgbzcs_chart();

    // 累计达到整改次数排名（单位：次）
    load_fenxi_zgwz_zgcs_chart();

    // 达到整改标准省公司数量趋势（单位：个）
    load_fenxi_zgwz_zgbz_chart();
}

// 统计分析-审计报告-审计结果摘要/养卡top5
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav3Con').find('.audtrm_off,.audtrm_on').hide();
    // 定义所需变量
    var auditDates = auditDate(), //审计时间
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            time: new Date().getTime()
        };
    // 先销毁表格，清除缓存
    $('#baogaoTop5').bootstrapTable('destroy');
    $('#baogaoTop5').bootstrapTable('resetView');
    // 填充表格上方文字说明
    $('#sjbgPrvdName').text($('#prvdNameZH').val()); //被审计公司
    $('#sjbgAudTrm').text(auditDates[2]); //审计时间
    $('#sjbgPeriod').text(auditDates[0] + ' -- ' + auditDates[1]); //审计期间

    // 千分位格式化，保留两位小数
    function operateFormatter(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }

    // 千分位格式化，不保留小数
    function operateFormatterhu(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQDYKReportText",
        dataType: 'json',
        data: postData,
        // 填充表格上方文字说明数据
        success: function (data) {
            if (data.switchState != 'audTrmColseForReport') {
                $('#fenxiFourNav3Con .audtrm_on').show();
                if (JSON.stringify(data) != "{}") {
                    // 返回数据
                    $('#baogaoInfo').html('新入网的' + data.reportText.totalQty0 + '个用户中,' +
                        '疑似养卡用户' + data.reportText.errQty0 + '个，' +
                        '占新增用户的' + data.reportText.qtyPercent0 + ';' +
                        '自由渠道新入网的' + data.reportText.totalQty1 + '个用户新中,' +
                        '疑似养卡用户' + data.reportText.errQty1 + '个,' +
                        '占新增用户的' + data.reportText.qtyPercent1 + ';' +
                        '社会渠道新入网的' + data.reportText.totalQty2 + '个用户中,' +
                        '疑似养卡用户' + data.reportText.errQty2 + '个,' +
                        '占新增用户的' + data.reportText.qtyPercent2 + ';' +
                        '养卡涉及渠道' + data.reportText.errChnlQty0 + '个,' +
                        '占渠道总量的' + data.reportText.qtyChnlPercent0 + ';' +
                        '涉及自有渠道' + data.reportText.errChnlQty1 + '个,' +
                        '占渠道总量的' + data.reportText.qtyChnlPercent1 + ';' +
                        '涉及社会渠道' + data.reportText.errChnlQty2 + '个,' +
                        '占渠道总量的' + data.reportText.qtyChnlPercent2 + '。'
                    );
                    //绘制表格
                    $.ajax({
                        url: "/cmca/yktl/getQDYKReportTable",
                        dataType: 'json',
                        data: postData,
                        success: function (data) {
                            // 绘制表格
                            $("#baogaoTop5").bootstrapTable({
                                datatype: "local",
                                data: data.reportTable,
                                cache: false,
                                columns: [{
                                    field: 'RN',
                                    title: '排名',
                                    align: 'center',
                                    valign: 'middle'
                                }, {
                                    field: 'regionName',
                                    title: '公司',
                                    align: 'center',
                                    valign: 'middle',
                                }, {
                                    field: 'errQty',
                                    title: '数量(个)',
                                    align: 'center',
                                    valign: 'middle'
                                }, {
                                    field: 'qtyPercent',
                                    title: '占比(%)',
                                    align: 'center',
                                    valign: 'middle',
                                    formatter: operateFormatterB
                                }]
                            });
                        }
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


// 地图下钻表格-地市养卡套利信息
function load_cty_yktlInfo_table() {
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
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQdykChnlTable",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.qdykChnl,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'errQty',
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
                    field: 'chnlClassNm',
                    title: '渠道类型',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'errQty',
                    title: '养卡数量（个）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'qtyPercent',
                    title: '养卡号码数量占比',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatterB

                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
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

// 地图下钻表格-搜索-地市养卡套利信息
function load_cty_searchInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $('#dialogQudaoTable').bootstrapTable('resetView');
    var postData = {
            prvdId: $('#prvdId').val(),
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            chnlName: $('#chnlName').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height());
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    $.ajax({
        url: "/cmca/yktl/getQdykChnlTable",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.qdykChnl,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'errQty',
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
                    field: 'chnlClassNm',
                    title: '渠道类型',
                    align: 'center',
                    valign: 'middle',
                    width: '28%'
                }, {
                    field: 'errQty',
                    title: '养卡数量（个）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    width: '15%'
                }, {
                    field: 'qtyPercent',
                    title: '养卡号码数量占比',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    width: '15%',
                    formatter: operateFormatterB
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
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

// 养卡套利信息下钻-渠道基本信息/渠道养卡趋势图
function load_chnlInfo_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
        chnlId: $('#chnlId').val(),
        prvdId: $('#prvdId').val()
    };
    // 渠道养卡基本信息表格
    $.ajax({
        url: "/cmca/yktl/getQdykChnlBaseInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            var ChnlBaseInfoTable = '<tr>' +
                '<td>渠道标识</td><td>' + data.chnlInfo.cor_chnl_id + '</td>' +
                '<td>开业时间</td><td>' + data.chnlInfo.busn_bgn_tm + '</td>' +
                '</tr><tr>' +
                '<td>渠道名称</td><td>' + data.chnlInfo.chnl_nm + '</td>' +
                '<td>渠道状态</td><td>' + data.chnlInfo.chnl_stat + '</td>' +
                '</tr><tr>' +
                '<td>渠道类型</td><td>' + data.chnlInfo.cor_chnl_typ + '</td>' +
                '<td>渠道基础类型</td><td>' + data.chnlInfo.chnl_basic_typ + '</td>' +
                '</tr><tr>' +
                '<td>渠道星级</td><td>' + data.chnlInfo.chnl_level + '</td>' +
                '<td>区域形态</td><td>' + data.chnlInfo.rgn_form + '</td>' +
                '</tr><tr>' +
                '<td>归属地市</td><td>' + data.chnlInfo.CMCC_prvd_nm_short + '</td>' +
                '<td>所属区县</td><td>' + data.chnlInfo.cnty_nm + '</td>' +
                '</tr><tr>' +
                '<td>渠道地址</td><td colspan="3">' + data.chnlInfo.chnl_addr + '</td>' +
                '</tr>';
            $('#ChnlBaseInfoTable').empty().append(ChnlBaseInfoTable);
        }
    });
    // 渠道养卡趋势
    $.ajax({
        url: "/cmca/yktl/getQdykChnlTrend",
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
                    categories: data.audtrmList,
                    crosshair: true,
                    labels: {
                        style: {
                            fontSize: $.xNumFontSize()
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
                    name: '养卡号码数量',
                    type: 'column',
                    yAxis: 1,
                    data: data.numList,
                    tooltip: {
                        valueSuffix: ' 个'
                    }
                }, {
                    name: '养卡号码数量占比',
                    type: 'spline',
                    data: data.perList,
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
        numPercent = 0, //养卡号码占比
        numPercentMom = 0, //养卡号码占比增量
        numAmt = 0, //养卡号码数量
        numAmtMom = 0, //养卡号码数量增量
        chnlPercent = 0, //养卡渠道占比
        chnlPercentMom = 0, //养卡渠道占比增量
        chnlAmt = 0, //养卡渠道数量
        chnlAmtMom = 0, //养卡渠道数量增量
        // 后台请求参数
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    $.ajax({
        url: "/cmca/yktl/getQdykMapBottom",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                numPercent = $.formatMoney(data[prvdId].qtyPercent, 2);
                numPercentMom = data[prvdId].fbQtyPerMonth;
                numAmt = $.formatMoney(data[prvdId].errQtyQdyk, 2);
                numAmtMom = data[prvdId].fbQtyMonth;
                chnlPercent = $.formatMoney(data[prvdId].chnlPercent, 2);
                chnlPercentMom = data[prvdId].fbChnlPerMonth;
                chnlAmt = $.formatMoney(data[prvdId].chnlQty, 0);
                chnlAmtMom = data[prvdId].fbChnlMonth;
            }
            // 养卡号码占比
            if (numPercentMom <= 0 || numPercentMom === null) {
                $('#card1').html(numPercent + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(numPercentMom), 2) + '%</span>');
            } else {
                $('#card1').html(numPercent + '%<span><i class="caret_up"></i>' + $.formatMoney(numPercentMom, 2) + '%</span>');
            }
            // 养卡号码数量
            if (numAmtMom <= 0 || numAmtMom === null) {
                $('#card2').html(numAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(numAmtMom), 2) + '</span>');
            } else {
                $('#card2').html(numAmt + '<span><i class="caret_up"></i>' + $.formatMoney(numAmtMom, 2) + '</span>');
            }
            // 养卡渠道占比
            if (chnlPercentMom <= 0 || chnlPercentMom === null) {
                $('#card3').html(chnlPercent + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(chnlPercentMom), 2) + '%</span>');
            } else {
                $('#card3').html(chnlPercent + '%<span><i class="caret_up"></i>' + $.formatMoney(chnlPercentMom, 2) + '%</span>');
            }
            // 养卡渠道数量
            if (chnlAmtMom <= 0 || chnlAmtMom === null) {
                $('#card4').html(chnlAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(chnlAmtMom), 0) + '</span>');
            } else {
                $('#card4').html(chnlAmt + '<span><i class="caret_up"></i>' + $.formatMoney(chnlAmtMom, 0) + '</span>');
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
 * targetTxt:指标标题
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
        targetTxt = $('#targetTxt').val(),
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
            concern: $('#concern').val()
        };
    // 请求全国各省数据
    $.ajax({
        url: "/cmca/yktl/getQdykMap",
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
                                case 'numPercentOrder': //指标是养卡号码占比排名（根据占比值渲染）
                                    var QtyPercent = data[prvdCode].qtyPercent;
                                    map.properties.trueValue = QtyPercent;
                                    if (QtyPercent > 0 && QtyPercent <= 1) {
                                        map.value = 8;
                                    } else if (QtyPercent > 1 && QtyPercent <= 3) {
                                        map.value = 5;
                                    } else if (QtyPercent > 3) {
                                        map.value = 2;
                                    } else {
                                        map.value = 0;
                                    }
                                    break;
                                case 'numAmtOrder': //指标是养卡号码数量排名
                                    order = data[prvdCode].rnErrQty;
                                    map.properties.trueValue = data[prvdCode].errQtyQdyk;
                                    break;
                                case 'chnlPercentOrder': //指标是养卡渠道占比排名
                                    order = data[prvdCode].rnChnlPercent;
                                    map.properties.trueValue = data[prvdCode].chnlPercent;
                                    break;
                                case 'chnlAmtOrder': //指标是养卡渠道数量排名
                                    order = data[prvdCode].rnChnlQty;
                                    map.properties.trueValue = data[prvdCode].chnlQty;
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
                                get_userBehavior_log('专题', '养卡套利', '地图下钻', '查询');

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
                                            concern: $('#concern').val()
                                        };
                                        // 请求下钻省数据
                                        $.ajax({
                                            url: "/cmca/yktl/getQdykMap",
                                            type: 'get',
                                            dataType: 'json',
                                            data: postData,
                                            success: function (ctyData) {
                                                // 地图颜色
                                                if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                                                    Highcharts.each(ctyMap, function (map) {
                                                        map.value = 8;
                                                        ctyCode = map.properties.code;
                                                        // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据,整个直辖市区域所有区县都显示为红色
                                                        if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                            map.value = 2;
                                                            map.properties.trueValue = e.point.properties.trueValue;
                                                        } else { // 不是直辖市
                                                            //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                                            if (ctyData[ctyCode] !== undefined) {
                                                                // 判断指标
                                                                switch (orderName) {
                                                                    case 'numPercentOrder': //当指标是养卡号码占比排名（根据占比值渲染）
                                                                        var QtyPercent = ctyData[ctyCode].qtyPercent;
                                                                        map.properties.trueValue = QtyPercent;
                                                                        if (QtyPercent > 0 && QtyPercent <= 1) {
                                                                            map.value = 8;
                                                                        } else if (QtyPercent > 1 && QtyPercent <= 3) {
                                                                            map.value = 5;
                                                                        } else if (QtyPercent > 3) {
                                                                            map.value = 2;
                                                                        } else {
                                                                            map.value = 0;
                                                                        }
                                                                        break;
                                                                    case 'numAmtOrder': //当指标是养卡号码数量排名
                                                                        order = ctyData[ctyCode].rnErrQty;
                                                                        map.properties.trueValue = ctyData[ctyCode].errQtyQdyk;
                                                                        break;
                                                                    case 'chnlPercentOrder': //当指标是养卡渠道占比排名
                                                                        order = ctyData[ctyCode].rnChnlPercent;
                                                                        map.properties.trueValue = ctyData[ctyCode].chnlPercent;
                                                                        break;
                                                                    case 'chnlAmtOrder': //当指标是养卡渠道数量排名
                                                                        order = ctyData[ctyCode].rnChnlQty;
                                                                        map.properties.trueValue = ctyData[ctyCode].chnlQty;
                                                                        break;
                                                                }
                                                                if (order <= 3 && order > 0) { //地市前三名，红色
                                                                    map.value = 2;
                                                                }
                                                                if (order >= 4 && order <= 10) { //地市4-10名，黄色
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
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '市养卡套利信息');
                                                                // 改变隐藏域
                                                                $('#ctyId').val(prvdId);
                                                            } else {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '省' + e.point.name + '市养卡套利信息');
                                                                // 改变隐藏域
                                                                $('#ctyId').val(e.point.properties.code);
                                                            }
                                                            // 加载数据
                                                            load_cty_yktlInfo_table();
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
                    text: null,
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
                tooltip: { //鼠标悬浮提示
                    formatter: function () {
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
                            // 判断指标
                            switch (orderName) {
                                case 'numPercentOrder': // 当指标是养卡号码占比排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>养卡号码数量占比：' + dt + '%</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>养卡号码数量占比：' + dt + '%</span>';
                                    }
                                    break;
                                case 'numAmtOrder': // 当指标是养卡号码数量排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>养卡号码个数：' + Highcharts.numberFormat(dt, 2, '.', ',') + ' 万个</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>养卡号码个数：' + Highcharts.numberFormat(dt, 2, '.', ',') + ' 万个</span>';
                                    }
                                    break;
                                case 'chnlPercentOrder': // 当指标是养卡渠道占比排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>养卡渠道占比：' + dt + '%</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>养卡渠道占比：' + dt + '%</span>';
                                    }
                                    break;
                                case 'chnlAmtOrder': // 当指标是养卡渠道数量排名
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>养卡渠道数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 个</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>养卡渠道数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 个</span>';
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
        targetTxt = $('#targetTxt').val(),
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
                url: "/cmca/yktl/getQdykMap",
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
                                    case 'numPercentOrder': //当指标是养卡号码占比排名
                                        map.properties.trueValue = ctyData[prvdId].qtyPercent;
                                        break;
                                    case 'numAmtOrder': //当指标是养卡号码数量排名
                                        mmap.properties.trueValue = ctyData[prvdId].errQtyQdyk;
                                        break;
                                    case 'chnlPercentOrder': //当指标是养卡渠道占比排名
                                        map.properties.trueValue = ctyData[prvdId].chnlPercent;
                                        break;
                                    case 'chnlAmtOrder': //当指标是养卡渠道数量排名
                                        map.properties.trueValue = ctyData[prvdId].chnlQty;
                                        break;
                                }
                            } else { // 不是直辖市
                                //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    switch (orderName) {
                                        case 'numPercentOrder': //当指标是养卡号码占比排名（根据占比值渲染）
                                            var QtyPercent = ctyData[ctyCode].qtyPercent;
                                            map.properties.trueValue = QtyPercent;
                                            if (QtyPercent > 0 && QtyPercent <= 1) {
                                                map.value = 8;
                                            } else if (QtyPercent > 1 && QtyPercent <= 3) {
                                                map.value = 5;
                                            } else if (QtyPercent > 3) {
                                                map.value = 2;
                                            } else {
                                                map.value = 0;
                                            }
                                            break;
                                        case 'numAmtOrder': //当指标是养卡号码数量排名
                                            order = ctyData[ctyCode].rnErrQty;
                                            map.properties.trueValue = ctyData[ctyCode].errQtyQdyk;
                                            break;
                                        case 'chnlPercentOrder': //当指标是养卡渠道占比排名
                                            order = ctyData[ctyCode].rnChnlPercent;
                                            map.properties.trueValue = ctyData[ctyCode].chnlPercent;
                                            break;
                                        case 'chnlAmtOrder': //当指标是养卡渠道数量排名
                                            order = ctyData[ctyCode].rnChnlQty;
                                            map.properties.trueValue = ctyData[ctyCode].chnlQty;
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
                            text: null,
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
                                        case 'numPercentOrder': //当指标是养卡号码占比排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>养卡号码数量占比：' + dt + '%</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>养卡号码数量占比：' + dt + '%</span>';
                                            }
                                            break;
                                        case 'numAmtOrder': //当指标是养卡号码数量排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>养卡号码数量：' + Highcharts.numberFormat(dt, 2, '.', ',') + ' 万个</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>养卡号码数量：' + Highcharts.numberFormat(dt, 2, '.', ',') + ' 万个</span>';
                                            }
                                            break;
                                        case 'chnlPercentOrder': //当指标是养卡渠道占比排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>养卡渠道占比：' + dt + '%</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>养卡渠道占比：' + dt + '%</span>';
                                            }
                                            break;
                                        case 'chnlAmtOrder': //当指标是养卡渠道数量排名
                                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                                return '<span>' + prvdNameZH + '</span><br/><span>养卡渠道数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 个</span>';
                                            } else {
                                                return '<span>' + cnm + '</span><br/><span>养卡渠道数量：' + Highcharts.numberFormat(dt, 0, '.', ',') + ' 个</span>';
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
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '市养卡套利信息');
                                        // 改变隐藏域
                                        $('#ctyId').val(prvdId);
                                    } else {
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '省' + e.point.name + '市养卡套利信息');
                                        // 改变隐藏域
                                        $('#ctyId').val(e.point.properties.code);
                                    }
                                    // 加载数据
                                    load_cty_yktlInfo_table();
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