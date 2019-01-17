/**
 * 柱状图
 * @param {String} urls 请求数据的url地址
 * @param {String} chartCtn 绘制图形的容器
 * @param {String} seriesName 鼠标悬浮提示文本
 * @param {String} seriesColor 柱状图颜色,目前有两种颜色的柱状图，分别是蓝色:#3095f2,红色:#FF647F
 * @param {String} formatterUnit 格式化数据单位
 * @param {Number} formatterDecimals 鼠标悬浮提示保留小数位数
 */
function load_column_chart(urls, chartCtn, seriesName, seriesColor, formatterUnit, formatterDecimals, parameterTypeName) {
    var chartCtnWrap = $('#' + chartCtn + '').parent(),
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            parameterType: parameterTypeName
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: urls,
        dataType: 'json',
        data: postData,
        cache: false,
        success: function (data) {
            if (data.prvdName != undefined) {
                $('#' + chartCtn + '').css('minWidth', data.prvdName.length * 10 + '%');
            }
            noDataX = -parseInt($('#' + chartCtn + '').width()) / 3;
            $('#' + chartCtn + '').highcharts({
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
                            return this.value / 1 + formatterUnit == '%' ? '%' : '';
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, formatterDecimals, ".", ",") + (formatterUnit === '%' ? formatterUnit : (' ' + formatterUnit)) + '</b>';

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
                    name: seriesName,
                    data: data.dataList === null ? data.dataList = 0 : data.dataList,
                    color: seriesColor
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
            chartCtnWrap.getNiceScroll(0).show();
            chartCtnWrap.getNiceScroll(0).resize();
            chartCtnWrap.getNiceScroll(0).doScrollLeft(0);
        }
    });
}

/**
 * 折线图
 * @param {String} urls 请求数据的url地址
 * @param {String} chartCtn 绘制图形的容器
 * @param {String} seriesName 鼠标悬浮提示文本
 * @param {String} seriesColor 柱状图颜色,目前有两种颜色的柱状图，分别是蓝色:#00c58e,红色:#8885d5
 * @param {String} formatterUnit 格式化数据单位
 * @param {Number} formatterDecimals 鼠标悬浮提示保留小数位数
 */
function load_line_chart(urls, chartCtn, seriesName, seriesColor, formatterUnit, formatterDecimals, parameterTypeName) {
    var chartCtnWrap = $('#' + chartCtn + '').parent(),
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            parameterType: parameterTypeName
        };
    $.ajax({
        url: urls,
        dataType: 'json',
        data: postData,
        cache: false,
        success: function (data) {
            if (data.audTrm != undefined) {
                $('#' + chartCtn + '').css('minWidth', data.audTrm.length * 10 + '%');
            }
            noDataX = -parseInt($('#' + chartCtn + '').width()) / 3;
            $('#' + chartCtn + '').highcharts({
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
                        color: seriesColor
                    }],
                    labels: {
                        formatter: function () {
                            return this.value / 1 + formatterUnit == '%' ? '%' : '';
                        }
                    }
                },
                credits: {
                    enabled: false
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, formatterDecimals, ".", ",") + (formatterUnit === '%' ? formatterUnit : (' ' + formatterUnit)) + '</b>';
                    },
                    useHTML: true
                },
                legend: {
                    enabled: false
                },
                series: [{
                    name: seriesName,
                    data: data.dataList,
                    color: seriesColor
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
            chartCtnWrap.getNiceScroll(0).show();
            chartCtnWrap.getNiceScroll(0).resize();
            chartCtnWrap.getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 基本的审计结果图形
function load_result_base_chart() {
    var concern = $('#concern').val(),
        targetVal = $('#target').val(),
        resultTarget1Val = $('#resultTarget1').val(),
        resultTarget2Val = $('#resultTarget2').val(),
        resultTarget3Val = $('#resultTarget3').val(),
        resultTarget4Val = $('#resultTarget4').val(),
        seriesName, formatterUnit, formatterDecimals;
    switch (resultTarget1Val) {
        case 'errTrafficNumber':
            seriesName = '异常赠送流量';
            formatterUnit = 'G';
            formatterDecimals = 2;
            parameterTypeName = "trafficNumColumn";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'trafficNumColumn':
            seriesName = '疑似违规转售流量';
            formatterUnit = 'G';
            formatterDecimals = 2;
            parameterTypeName = "trafficNumColumn";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'illegalGroupNumber':
            seriesName = '疑似违规转售集团客户数';
            formatterUnit = '个';
            formatterDecimals = 0;
            parameterTypeName = "illegalGroupNumColumn";
            $('#target').attr("data", parameterTypeName);
            break;
    }
    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow1', seriesName, '#3095f2', formatterUnit, formatterDecimals, parameterTypeName);
    switch (resultTarget2Val) {
        case 'errTrafficPercent':
            seriesName = '异常赠送流量占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "trafficPercentColumn";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'trafficPercentColumn':
            seriesName = '疑似违规转售流量占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "trafficPercentColumn";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'illegalGroupPercentColumn':
            seriesName = '疑似违规转售集团客户数占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "illegalGroupPercentColumn";
            $('#target').attr("data", parameterTypeName);
            break;
    }
    load_column_chart('/cmca/llglwg/getColumnData', 'contentShow2', seriesName, '#FF647F', formatterUnit, formatterDecimals, parameterTypeName);
    switch (resultTarget3Val) {
        case 'errTrafficNumber':
            seriesName = '异常赠送流量';
            formatterUnit = 'G';
            formatterDecimals = 2;
            parameterTypeName = "trafficLine";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'trafficLine':
            seriesName = '疑似违规转售流量';
            formatterUnit = 'G';
            formatterDecimals = 2;
            parameterTypeName = "trafficLine";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'illegalOrgLine':
            seriesName = '疑似违规转售集团客户数';
            formatterUnit = '个';
            formatterDecimals = 0;
            parameterTypeName = "illegalOrgLine";
            $('#target').attr("data", parameterTypeName);
            break;
    }
    load_line_chart('/cmca/llglwg/getLineData', 'contentShow3', seriesName, '#00c58e', formatterUnit, formatterDecimals, parameterTypeName);
    switch (resultTarget4Val) {
        case 'errTrafficPercent':
            seriesName = '异常赠送流量占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "trafficPercentLine";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'trafficPercentLine':
            seriesName = '疑似违规转售流量占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "trafficPercentLine";
            $('#target').attr("data", parameterTypeName);
            break;
        case 'illegalPercentOrgLine':
            seriesName = '疑似违规转售集团客户数占比';
            formatterUnit = '%';
            formatterDecimals = 2;
            parameterTypeName = "illegalPercentOrgLine";
            $('#target').attr("data", parameterTypeName);
            break;
    }
    load_line_chart('/cmca/llglwg/getLineData', 'contentShow4', seriesName, '#8885d5', formatterUnit, formatterDecimals, parameterTypeName);
}

// 审计结果
function load_result_chart() {
    var concern = $('#concern').val(),
        prvdId = $('#prvdId').val();
    /*  switch (concern) {
         case '7003':
         $('#resultTabCon .show_item .btn-group').show();
         load_result_base_chart();
             break;
         default:
             if (prvdId != '10000') {
                 $('#resultTabCon .show_item .btn-group').hide();
                 // 加载审计结果图形---重点关注集团客户
                 // $('#mapUserChooseConcernWrap1').hide();
                 // $('#dialogQudaoTableTitle').hide();
                 // load_cty_zdgz_bloc_table();
             } else {
                 $('#resultTabCon .show_item .btn-group').show();
                 // 加载基本审计结果图形
                 load_result_base_chart();
             }
             break;
     } */
    $('#resultTabCon .show_item .btn-group').show();
    load_result_base_chart();
}

// 统计分析-统计报表-排名汇总
function load_fenxi_pmhz_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#rankingAllTable').bootstrapTable('destroy');
    $('#rankingAllTable').bootstrapTable('resetView');
    var concern = $('#concern').val(),
        prvdId = $('#prvdId').val(),
        postData = {
            prvdId: prvdId,
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav1Con').height()) - 60,
        columns = [],
        urls,
        names;
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 根据不同的关注点，发送不同的请求，渲染不同的表格
    switch (concern) {
        case '7003':
            $('#rankingAllTableTitle').text('流量异常赠送值排名汇总');
            columns = [{
                field: 'rn',
                title: '排名',
                valign: 'middle',
                formatter: rn
            }, {
                field: 'prvdName',
                // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                title:'公司',
                valign: 'middle'
            }, {
                field: 'trafficValueTotle',
                title: '总赠送流量（G）',
                valign: 'middle',
                formatter: operateFormatter2
            }, {
                field: 'trafficTotle',
                title: '总赠送流量次数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'subsTotle',
                title: '总赠送用户数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'errTrafficNumber',
                title: '异常赠送流量（G）',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter2
            }, {
                field: 'errTrafficTotle',
                title: '异常赠送次数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'errSubsTotle',
                title: '异常赠送涉及用户数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'errTrafficPercent',
                title: '异常赠送流量占比',
                valign: 'middle',
                formatter: operateFormatterB
            }];
            break;
        default:
            $('#rankingAllTableTitle').text('疑似违规流量转售排名汇总');
            columns = [{
                field: 'rn',
                title: '排名',
                valign: 'middle',
                formatter: rn
            }, {
                field: 'prvdName',
                // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                title:'公司',
                valign: 'middle'
            }, {
                field: 'paySystemSubsOrg',
                title: '统付涉及集团客户数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'paySystemValueOrg',
                title: '统付涉及统付流量总值（G）',
                valign: 'middle',
                formatter: operateFormatter2
            }, {
                field: 'paySystemNumber',
                title: '统付流量订购次数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'paySystemPhoneTotle',
                title: '统付涉及号码总数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'illegalGroupNumber',
                title: '疑似违规流量转售集团客户数',
                valign: 'middle',
                sortable: true,
                formatter: operateFormatter0
            }, {
                field: 'errTrafficNumber',
                title: '疑似违规流量转售流量总值（G）',
                valign: 'middle',
                formatter: operateFormatter2
            }, {
                field: 'errTrafficTotle',
                title: '疑似违规流量转售总次数',
                valign: 'middle'
            }, {
                field: 'errPaySystemPhoneTotle',
                title: '疑似违规流量转售涉及号码总数',
                valign: 'middle',
                formatter: operateFormatter0
            }, {
                field: 'errPaySystemPhonePer',
                title: '疑似违规转售流量占总统付流量比',
                valign: 'middle',
                formatter: operateFormatterB
            }];
            break;
    }
    // 请求数据
    $.ajax({
        url: '/cmca/llglwg/getRankTable',
        dataType: 'json',
        data: postData,
        showColumns: true,
        cache: false,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#rankingAllTable").bootstrapTable({
                    datatype: "local",
                    data: data.rankTable, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: columns
                });
                $('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
                scroll('#rankingAllTableWrap', '#rankingAllTable');
            }
        }
    });
}

// 统计分析-统计报表-增量分析
function load_fenxi_zlfx_chart() {
    var concern = $('#concern').val(),
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: concern
        },
        waterfallH = $('#pubuChart').closest('.pubu_show').height() - 100,
        lastDataName, lastDataY, newLastData, dataZlfx, urls, sbuTitle;
    $("#fenxiFourNav1FiveNav2Con .table_title").text(($('#concern').val() == '7003') ? '流量异常赠送增量分析' : '疑似违规流量转售增量分析');
    // 请求数据
    $.ajax({
        url: '/cmca/llglwg/getIncrementalData',
        dataType: 'json',
        data: postData,
        cache: false,
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
                        text: null
                    },
                    labels: {
                        formatter: function () {
                            return this.value / 1 + "G";
                        }
                    }
                },
                legend: {
                    enabled: false
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span>' + '</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' G</b>';
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
            // 滚动条设置
            $("#fenxiFourNav1FiveNav2Con").getNiceScroll(0).show();
            $("#fenxiFourNav1FiveNav2Con").getNiceScroll(0).resize();
            $('#fenxiFourNav1FiveNav2Con').getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 统计分析-统计报表-重点关注地市
function load_fenxi_zdgz_city_table() {
    // 销毁表格，清除缓存
    $('#focusCityTable').bootstrapTable('destroy');
    $('#focusCityTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav3Con').height()) - 10;
    // 定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusCityTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#focusCityTable").bootstrapTable({
                    datatype: "local",
                    data: data.cityTop50, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rank_china',
                        title: '排名',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'prvd_nm',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'cty_nm',
                        title: '地市',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'giv_value_tol',
                        title: '总赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'giv_cnt_tol',
                        title: '总赠送流量次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'giv_subs_tol',
                        title: '总赠送用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value',
                        title: '异常赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'infrac_cnt',
                        title: '异常赠送次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_subs_cnt',
                        title: '异常赠送涉及用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value_pre',
                        title: '异常赠送流量占比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    }]
                });
                $('#focusCityTable').parent('.fixed-table-body').attr('id', 'focusCityTableWrap');
                scroll('#focusCityTableWrap', '#focusCityTable');
            }
        }
    });
}

// 统计分析-统计报表-重点关注营销案
function load_fenxi_zdgz_case_table() {
    // 销毁表格，清除缓存
    $('#focusCaseTable').bootstrapTable('destroy');
    $('#focusCaseTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav4Con').height()) - 10;
    // 定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusOfferTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#focusCaseTable").bootstrapTable({
                    datatype: "local",
                    data: data.offer, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '排名',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'offer_nm',
                        title: '营销案名称',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'offer_cd',
                        title: '营销案编号',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'prvd_nm',
                        title: '营销案所在省份',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'cty_nm',
                        title: '营销案所在地市',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'giv_value_tol',
                        title: '总赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'giv_cnt_tol',
                        title: '总赠送流量次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'giv_subs_tol',
                        title: '总赠送用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value',
                        title: '异常赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'infrac_cnt',
                        title: '异常赠送次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_subs_cnt',
                        title: '异常赠送涉及用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value_pre',
                        title: '异常赠送流量占比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    }]
                });
                $('#focusCaseTable').parent('.fixed-table-body').attr('id', 'focusCaseTableWrap');
                scroll('#focusCaseTableWrap', '#focusCaseTable');
            }
        }
    });
}

// 统计分析-统计报表-重点关注渠道 
function load_fenxi_zdgz_chnl_table() {
    // 销毁表格，清除缓存
    $('#focusChannelTable').bootstrapTable('destroy');
    $('#focusChannelTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav5Con').height()) - 10;
    // 定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusChnlTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#focusChannelTable").bootstrapTable({
                    datatype: "local",
                    data: data.chnl, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '排名',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'chnl_nm',
                        title: '渠道名称',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'chnl_id',
                        title: '渠道标识',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'prvd_nm',
                        title: '渠道所在省份',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'cty_nm',
                        title: '渠道所在地市',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'giv_value_tol',
                        title: '总赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'giv_cnt_tol',
                        title: '总赠送流量次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'giv_subs_tol',
                        title: '总赠送用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value',
                        title: '异常赠送流量（G）',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'infrac_cnt',
                        title: '异常赠送次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_subs_cnt',
                        title: '异常赠送涉及用户数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'infrac_value_pre',
                        title: '异常赠送流量占比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    }]
                });
                $('#focusChannelTable').parent('.fixed-table-body').attr('id', 'focusChannelTableWrap');
                scroll('#focusChannelTableWrap', '#focusChannelTable');
            }
        }
    });
}

// 统计分析-统计报表-重点关注用户
function load_fenxi_zdgz_user_table() {
    // 销毁表格，清除缓存
    $('#focusUsersTable').bootstrapTable('destroy');
    $('#focusUsersTable').bootstrapTable('resetView');
    var parameterType = $('#flagId').val(),
        postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            parameterType: parameterType,
            flagId: ""
        },
        h = parseInt($('#dialogQudaoTableWrap').height()) - 40;
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusUserTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            $("#focusUsersTable").bootstrapTable({
                datatype: "local",
                data: data.orgCustomer,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rank_infrac_typ',
                    title: '排名',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'subs_id',
                    title: '用户标识',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infrac_value',
                    title: '赠送流量值（G）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter2
                }, {
                    field: 'infrac_cnt',
                    title: '赠送流量次数',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'yk_flag',
                    title: '“养卡套利”模型结果',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'hf_flag',
                    title: "高频赠送",
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'hq_flag',
                    title: "高额赠送",
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'abn_flag',
                    title: "向非正常用户赠送",
                    align: 'center',
                    valign: 'middle'
                }]
            });
            $('#focusUsersTable').parent('.fixed-table-body').attr('id', 'focusUsersTableWrapCtn');
            scroll('#focusUsersTableWrapCtn', '#focusUsersTable');
        }
    });
}

// 统计分析-统计报表-疑似转售集团客户
function load_fenxi_zdgz_zsgroupUser_table() {
    // 销毁表格，清除缓存
    $('#zsfocusGroupUsersTable').bootstrapTable('destroy');
    $('#zsfocusGroupUsersTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav7Con').height()) - 10;
    // 定义数据格式
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusOrgCustomerTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#zsfocusGroupUsersTable").bootstrapTable({
                    datatype: "local",
                    data: data.orgCustomer, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '排名',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'CMCC_prov_prvd_nm',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'jt_cmcc_prvd_nm_short',
                        title: '地市名称',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'busn_mode_nm',
                        title: '统付方式',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'org_cust_id',
                        title: '集团客户标识',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'org_nm',
                        title: '集团客户名称',
                        align: 'center',
                        valign: 'middle'
                    }, {
                        field: 'sum_strm_cap',
                        title: '疑似转售流量总值（G）',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'strm_cap_per',
                        title: '疑似转售流量占该省统付总流量占比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    }, {
                        field: 'sum_cnt',
                        title: '统付总次数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'sum_msisdn',
                        title: '转入号码数',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'avg_strm_cap',
                        title: '每用户平均统付流量值（G）',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatter2
                    }]
                });
                $('#zsfocusGroupUsersTable').parent('.fixed-table-body').attr('id', 'zsfocusGroupUsersTableWrap');
                scroll('#zsfocusGroupUsersTableWrap', '#zsfocusGroupUsersTable');
            }
        }
    });
}
// 统计分析-统计报表-重点关注集团客户
function load_fenxi_zdgz_groupUser_table() {
     // 销毁表格，清除缓存
     $('#focusGroupUsersTable').bootstrapTable('destroy');
     $('#focusGroupUsersTable').bootstrapTable('resetView');
     var postData = {
             audTrm: $('#audTrm').val(),
             prvdId: $('#prvdId').val()
         },
         h = parseInt($('#fenxiFourNav1FiveNav3Con').height()),
         time = $("#audTrm").val(),
         audTrms ; //月份递减
         if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
 
     $.ajax({
         url: "/cmca/llglwg/getFocusOrgCustomerTableForZgWz",
         dataType: 'json',
         data: postData,
         showColumns: true,
         success: function (data) {
             //定义数据格式
             // 千分位，不保留小数
             function operateFormatter2(value, row, index) {
                 if (value != "-") {
                     return $.formatMoney(value, 2, "table");
                 } else {
                     return '-';
                 }
             }
             // 百分比格式化
             function operateFormatterB(value, row, index) {
                 if (value != "-") {
                     return $.formatMoney(value, 2, "table") + "%";
                 } else {
                    return '-';
                }
             }
 
             function tagColumn(value, row, index) {
                 return '<a href="javascript:;" value="' + row.orgCustId + '">' + value + '</a>';
             }
             $("#focusGroupUsersTable").bootstrapTable({
                 datatype: "local",
                 data: data,
                 pagination: false,
                 cache: false,
                 height: h,
                 columns: [
                     [{
                         field: 'companyName',
                         title: '集团客户名称',
                         align: 'center',
                         valign: 'middle',
                         colspan: 1,
                         rowspan: 2,
                         formatter: tagColumn,
                     }, {
                         field: 'prvdName',
                         title: '公司',
                         align: 'center',
                         valign: 'middle',
                         colspan: 1,
                         rowspan: 2,
                     }, {
                         title: audTrms[0],
                         align: 'center',
                         valign: 'middle',
                         colspan: 4,
                         rowspan: 1,
                     }, {
                         title: audTrms[1],
                         align: 'center',
                         valign: 'middle',
                         colspan: 4,
                         rowspan: 1,
                     }, {
                         title: audTrms[2],
                         align: 'center',
                         colspan: 4,
                         rowspan: 1,
                         valign: 'middle',
                     }],
                     [{
                         field: 'isNotWg_3',
                         title: '当月是否违规',
                         align: 'center',
                         valign: 'middle',
                     },{
                        field: 'tfSum_3',
                        title: '统付流量值（G）',
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter2,
                     },{
                         field: 'tfSumStrmPrvdPer_3',
                         title: '统付流量占该省统付总流量比',
                         align: 'center',
                         valign: 'middle',
                         formatter: operateFormatterB,
                     }, {
                         field: 'tfSumStrmAllerrPer_3',
                         title: '统付流量占当月全集团违规转售总流量比',
                         align: 'center',
                         valign: 'middle',
                         formatter: operateFormatterB
                     }, {
                        field: 'isNotWg_2',
                        title: '当月是否违规',
                        align: 'center',
                        valign: 'middle',
                    },{
                       field: 'tfSum_2',
                       title: '统付流量值（G）',
                       sortable: true,
                       align: 'center',
                       valign: 'middle',
                       formatter: operateFormatter2,
                    },{
                        field: 'tfSumStrmPrvdPer_2',
                        title: '统付流量占该省统付总流量比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB,
                    }, {
                        field: 'tfSumStrmAllerrPer_2',
                        title: '统付流量占当月全集团违规转售总流量比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    },{
                        field: 'isNotWg_1',
                        title: '当月是否违规',
                        align: 'center',
                        valign: 'middle',
                    },{
                       field: 'tfSum_1',
                       title: '统付流量值（G）',
                       sortable: true,
                       align: 'center',
                       valign: 'middle',
                       formatter: operateFormatter2,
                    },{
                        field: 'tfSumStrmPrvdPer_1',
                        title: '统付流量占该省统付总流量比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB,
                    }, {
                        field: 'tfSumStrmAllerrPer_1',
                        title: '统付流量占当月全集团违规转售总流量比',
                        align: 'center',
                        valign: 'middle',
                        formatter: operateFormatterB
                    }]
                 ]
             });
             $('#focusGroupUsersTable').parent('.fixed-table-body').attr('id', 'focusGroupUsersTableWrap');
             // $('#tuchuQudaoTable thead').remove();
             scroll('#focusGroupUsersTableWrap', '#focusGroupUsersTable');
         }
     });
}

//统计分析-统计报表-重点关注集团客户-集团客户名称
function load_fenxi_zdgz_chnlInfo_chart(){
    //隐藏上级滚动条
    $('#focusGroupUsersTable').parent('.fixed-table-body').getNiceScroll().hide();
    var postData = {
            audTrm: $('#audTrm').val(),
            orgCustId: $('#chnlId').val(),
            //concern: $('#concern').val(),
            prvdId: $('#prvdId').val()
        },
        qudaoChnlBaseInfoTable;
    h = $('#zdgz_qudaoChart').closest('.info_item').height() - 20;
    // 集团客户基本信息表格
    $.ajax({
        url: "/cmca/llglwg/getGroupInfo",
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                qudaoChnlBaseInfoTable = '<tr>' +
                    '<td>集团客户名称</td><td>' + data.groupInfo.companyName + '</td>' +
                    '<td>业务形态</td><td>' + data.groupInfo.formName + '</td>' +
                    '</tr><tr>' +
                    '<td>集团客户标识</td><td>' + data.groupInfo.orgCustId + '</td>' +
                    '<td>统付方式</td><td>' + data.groupInfo.modeName + '</td>' +
                    '</tr><tr>' +
                    '<td>公司</td><td>' + data.groupInfo.prvdName + '</td>' +
                    '<td>地市</td><td>' + data.groupInfo.city + '</td>' +
                    '</tr><tr>' +
                    '<td>违规审计月</td><td colspan="3">' + data.groupInfo.accountAudTrm + '</td>' +
                    '</tr>';
                $('#zdgz_ChnlBaseInfoTable').empty().append(qudaoChnlBaseInfoTable);
            }
        }
    });
    // 近6个月 集团客户趋势
    $.ajax({
        url: "/cmca/llglwg/getYszsllAudTrmData",
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
                        format: '{value} G',
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
                    name: '疑似转售流量总值',
                    type: 'column',
                    yAxis: 1,
                    data: data.columnData,
                    tooltip: {
                        valueSuffix: '（G）'
                    }
                }, {
                    name: '疑似转售流量占该省统付总流量占比',
                    type: 'spline',
                    data: data.lineData,
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
    $('#piePrvdName').text($('#chooseProvince').val());
    // 占比饼图
    $.ajax({
        url: "/cmca/llglwg/getTypeDistributePie",
        dataType: 'json',
        data: postData,
        cache: false,
        success: function (data) {
            if (JSON.stringify(data) != '{}' && data.unpackingNum !== 0) {
                dataPie = [{
                    name: '高频赠送流量占比',
                    y: data.errHighFrequency,
                    color: '#7CB5EC'
                }, {
                    name: '高额赠送流量占比',
                    y: data.errHighLimit,
                    color: '#90ED7D'
                }, {
                    name: '向非正常用户赠送流量值占比',
                    y: data.abnormalSubs,
                    color: '#FF9933'
                }];
            }
            $('#abnormalPieChart').highcharts({
                chart: {
                    type: 'pie',
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
                },
                tooltip: {
                    formatter: function () {
                        return '<span style="color:' + this.point.color + ';fontWeight:400;">' + this.point.name + '：</span><span>' + +this.point.percentage.toFixed(2) + '%（' + Highcharts.numberFormat(this.point.y, 2, '.', ',') + ' G）</span>';
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

    // 占比趋势图标题
    $('#areaPrvdName').text($('#chooseProvince').val());
    var h = $('#abnormalAreaChartWrap').height() - 40;
    // 异常销售数量趋势图
    $.ajax({
        url: "/cmca/llglwg/getTypeDistributeStack",
        dataType: 'json',
        data: postData,
        cache: false,
        success: function (data) {
            if (JSON.stringify(data) != '{}') {
                dataArea = [{
                    name: '高频赠送流量值',
                    data: data.errHighFrequency,
                    color: '#7CB5EC'
                }, {
                    name: '向非正常用户赠送流量值',
                    data: data.abnormalSubs,
                    color: '#90ED7D'
                }, {
                    name: '高额赠送流量值',
                    data: data.errHighLimit,
                    color: '#FF9933'
                }];
            }
            $('#abnormalAreaChart').css('minWidth', data.audTrm.length * 18 + '%');
            $('#abnormalAreaChart').highcharts({
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
                            s += '<br/>' + '<span style="color:' + this.series.color + '">' + this.series.name + '</span>：' + Highcharts.numberFormat(this.percentage, 2, '.', ',') + '% ，' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' G';
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
            $("#abnormalAreaChartWrap").getNiceScroll(0).show();
            $("#abnormalAreaChartWrap").getNiceScroll(0).resize();
            $('#abnormalAreaChartWrap').getNiceScroll(0).doScrollLeft(0);
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
            //concern: $('#concern').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav2FiveNav1Con').height()) - 30;
    $.ajax({
        url: "/cmca/llglwg/getChangeAccountInfoTable",
        dataType: 'json',
        data: postData,
        cache: false,
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
                    field: 'rn',
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
                    field: 'zgTime',
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
        url: "/cmca/llglwg/getRectifiyForSixColumn",
        dataType: 'json',
        data: postData,
        cache: false,
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

//六个月内达到问责标准次数排名（单位：次）
function load_fenxi_zgwz_wzbzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val()
    };
    // 六个月内达到问责标准次数排名（单位：次）
    $.ajax({
        url: "/cmca/llglwg/getAccountForSixColumn",
        dataType: 'json',
        data: postData,
        cache: false,
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
                    name: '问责次数',
                    data: data.accountNum,
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
        url: "/cmca/llglwg/getRectifiyColumn",
        dataType: 'json',
        data: postData,
        cache: false,
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

// 累计达到问责次数排名（单位：次）
function load_fenxi_zgwz_wzcs_chart() {
    var postData = {
        audTrm: $('#audTrm').val(),
    };
    // 累计达到整改次数排名（单位：次）
    $.ajax({
        url: "/cmca/llglwg/getAccountabilityColumn",
        dataType: 'json',
        data: postData,
        cache: false,
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
                    name: '问责次数',
                    data: data.accountNum,
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
        url: "/cmca/llglwg/getRectifiyLine",
        dataType: 'json',
        data: postData,
        cache: false,
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
        url: "/cmca/llglwg/getAccountabilityLine",
        dataType: 'json',
        data: postData,
        cache: false,
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
                    data: data.accountNum,
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

// 统计分析-审计报告
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav3Con').find('.audtrm_off,.audtrm_on').hide();
    // 定义所需变量
    var myData, NumMom3000, AmtMom3000, momText1, momText2, //文字说明所需数据
        h = parseInt($('#baogaoTop5Con').height()), //表格容器高度
        auditDates = auditDate(), //审计时间
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val()
        };
    // 填充表格上方文字说明数据
    $('#sjbgPrvdName').text($('#prvdId').val() == '10000' ? $('#prvdNameZH').val() : $('#prvdNameZH').val() + '公司'); //被审计公司
    $('#sjbgAudTrm').text(auditDates[2]); //审计时间
    $('#sjbgPeriod').text(auditDates[0] + ' -- ' + auditDates[1]); //审计期间

    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getReportInfo",
        dataType: 'html',
        cache: false,
        data: postData,
        success: function (data) {
            if (data.switchState != 'audTrmColseForReport') {
                $('#fenxiFourNav3Con .audtrm_on').show();
                if (data != "") {
                    $('#baogaoTop5Con').html(data);
                    scroll('#baogaoTop5ConWrap', '#baogaoTop5Con');
                } else {
                    $("#baogaoTop5Con").html('');
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
//         audTrm: $('#audTrm').val(),
//         prvdId: $('#prvdId').val(),
//         subjectId: $('#subjectId').val(),
//         fileType: "audDetail"
//     };
//     $.ajax({
//         url: '/cmca/bgxz/selCsvBySubjectId',
//         dataType: 'json',
//         type: 'POST',
//         data: postData,
//         cache: false,
//         success: function (data) {
//             var prvdId_val = $('#prvdId').val(),
//                 audTrm_val = $('#audTrm').val(),
//                 subjectId_val = $('#subjectId').val(),
//                 focusCd_val = $('#focusCd').val(),
//                 userName_val = encodeURI($('#userName').val()),
//                 fileType_val = $('#fileType').attr("data");
//             if (data.filenames.length != 0) {
//                 $.each(data.filepaths, function (n, value) {
//                     window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm_val + '&subjectId=' + subjectId_val + '&prvdId=' + prvdId_val + '&focusCd=' + focusCd_val + '&fileType=' + fileType_val + /* '&userName=' + userName + */ '&download_url=' + value);
//                 });
//             } else {
//                 alert("没有文件生成");
//             }
//         }
//     });
//     /*  var prvdId = $('#prvdId').val(),
//          audTrm = $('#audTrm').val(),
//          subjectId = $('#subjectId').val();
//      window.open('/cmca/pmhz/downFilePage?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&fileType=audDetail'); */
// }

// 地图下钻-地市重点关注营销案-表格
function load_cty_zdgz_case_table() {
    $('#dialogQudaoTableTitle').hide();
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $("#dialogQudaoTable").bootstrapTable('resetView');
    var postData = {
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height()) - 10;
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusOfferOfCityTable",
        dataType: 'json',
        data: postData,
        cache: false,
        showColumns: true,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.offer,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '排名',
                    align: 'center',
                    valign: 'middle',
                    width: '16%'
                }, {
                    field: 'offer_cd',
                    title: '营销案编号',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'giv_value_tol',
                    title: '总赠送流量（G）',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter2
                }, {
                    field: 'giv_cnt_tol',
                    title: '总赠送流量次数',
                    align: 'center',
                    valign: 'middle',
                }, {
                    field: 'giv_subs_tol',
                    title: '总赠送用户数',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infrac_value',
                    title: '异常赠送流量（G）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter2
                }, {
                    field: 'infrac_cnt',
                    title: '异常赠送次数',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infrac_subs_cnt',
                    title: '异常赠送涉及用户数',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infrac_value_pre',
                    title: '异常赠送流量占比',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatterB
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
        }
    });
}
// 地图下钻-地市重点关注集团客户-表格
function load_cty_zdgz_bloc_table() {
    $('#dialogQudaoTableTitle').show();
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $("#dialogQudaoTable").bootstrapTable('resetView');
    var postData = {
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            flagId: "city"
        },
        h = parseInt($('#dialogQudaoTableWrap').height()) - 10;
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        return $.formatMoney(value, 0, "table");
    }
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 百分比格式化
    function operateFormatterB(value, row, index) {
        if (value != null) {
            return $.formatMoney(value, 2, "table") + "%";
        }
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusOrgCustomerTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        cache: false,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.orgCustomer,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '排名',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'org_nm',
                    title: '集团客户名称',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'org_cust_id',
                    title: '集团客户标识',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'busn_mode_nm',
                    title: '统付方式',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'sum_strm_cap',
                    title: '疑似转售流量总值（G）',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter2,
                    sortable: true
                }, {
                    field: 'strm_cap_per',
                    title: '疑似转售流量占该省统付总流量占比',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatterB
                }, {
                    field: 'sum_cnt',
                    title: '统付总次数',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter0
                }, {
                    field: 'sum_msisdn',
                    title: '转入号码数',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter0
                }, {
                    field: 'avg_strm_cap',
                    title: '每用户平均统付流量值（G）',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter2
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
        }
    });
}
// 地图下钻-地市重点关注用户-表格
function load_cty_zdgz_user_table() {
    $('#dialogQudaoTableTitle').hide();
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $("#dialogQudaoTable").bootstrapTable('resetView');
    var parameterType = $('#flagId').val(),
        postData = {
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            parameterType: parameterType,
            flagId: "city"
        },
        h = parseInt($('#dialogQudaoTableWrap').height()) - 10;
    // 千分位格式化，保留2位小数
    function operateFormatter2(value, row, index) {
        return $.formatMoney(value, 2, "table");
    }
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getFocusUserTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        cache: false,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.orgCustomer,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rank_infrac_typ',
                    title: '排名',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'subs_id',
                    title: '用户标识',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'infrac_value',
                    title: '赠送流量值（G）',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: operateFormatter2
                }, {
                    field: 'infrac_cnt',
                    title: '赠送流量次数',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'yk_flag',
                    title: '“养卡套利”模型结果',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'hf_flag',
                    title: "高频赠送",
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'hq_flag',
                    title: "高额赠送",
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'abn_flag',
                    title: "向非正常用户赠送",
                    align: 'center',
                    valign: 'middle'
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
        }
    });
}

// 地图下方卡片
function load_mapBtm_card_chart() {
    // 定义所需变量
    var prvdId = $('#prvdId').val(),
        cmccErrAmts = "-", //异常赠送流量
        cmccErrAmtsZf = 0, //异常赠送流量增量
        cmccErrAmtPer = "-", //异常赠送流量占比
        cmccErrAmtPerZf = 0, //异常赠送流量占比增量
        cmccErrNum = "-", //异常赠送次数
        cmccErrNumZf = 0, //异常赠送次数增量
        cmccErrNumPer = "-", //异常赠送涉及用户数
        cmccErrNumPerZf = 0, //异常赠送涉及用户数增量
        baseErrAmts = "-", //疑似违规流量转售流量总值
        baseErrAmtsZf = 0, //疑似违规流量转售流量总值增量
        baseErrAmtPer = "-", //疑似违规转售流量占比
        baseErrAmtPerZf = 0, //疑似违规转售流量占比增量
        baseSumAmt = "-", //统付涉及集团客户数
        baseSumAmtZf = 0, //统付涉及集团客户数增量
        cmccSumAmt = "-", //疑似违规流量转售集团客户数
        cmccSumAmtZf = 0, //疑似违规流量转售集团客户数增量
        concern = $('#concern').val(), //关注点
        targetVal = $('#target').val(), //类别
        focusCd, postData, cardsTxt = [];
    /* if (targetVal == 'prvdNoAmount' || targetVal == 'prvdNoAmountPercent') {
        concern = '600102'; //省无基地有
    } else if (targetVal == 'prvdHaveAmount' || targetVal == 'prvdHaveAmountPercent') {
        concern = '600101'; //省有基地无
    } */
    // 后台请求参数
    postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val(),
    };
    // 请求数据
    $.ajax({
        url: "/cmca/llglwg/getMapBottomData",
        type: 'get',
        dataType: 'json',
        data: postData,
        cache: false,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                cmccErrAmts = $.formatMoney(data[prvdId].errTrafficNumber, 2);
                cmccErrAmtsZf = data[prvdId].errTrafficNumberMom;
                cmccErrAmtPer = $.formatMoney(data[prvdId].errTrafficPercent, 2);
                cmccErrAmtPerZf = data[prvdId].errTrafficPercentMom;
                cmccErrNum = $.formatMoney(data[prvdId].errTrafficTotle, 0);
                cmccErrNumZf = data[prvdId].errTrafficTotleMom;
                cmccErrNumPer = $.formatMoney(data[prvdId].errSubsTotle, 0);
                cmccErrNumPerZf = data[prvdId].errSubsTotleMom;
                baseErrAmts = $.formatMoney(data[prvdId].errTrafficNumber, 2);
                baseErrAmtsZf = data[prvdId].errTrafficNumberMom;
                baseErrAmtPer = $.formatMoney(data[prvdId].errTrafficPercent, 2);
                baseErrAmtPerZf = data[prvdId].errTrafficNumberMom;
                baseSumAmt = $.formatMoney(data[prvdId].paySystemGroupNumber, 0);
                baseSumAmtZf = data[prvdId].paySystemGroupNumberMom;
                cmccSumAmt = $.formatMoney(data[prvdId].illegalGroupNumber, 0);
                cmccSumAmtZf = data[prvdId].illegalGroupNumberMom;
            }
            // 所有增量如果是降低的，可能是负值，做判断改变示意图标，显示绝对值
            // 判断关注点，不同的关注点下，不同的指标渲染不同的数据
            switch (concern) {
                case '7003': //流量异常赠送
                    // 异常赠送流量
                    if (cmccErrAmtsZf <= 0 || cmccErrAmtsZf === null) {
                        $('#card1').html(cmccErrAmts + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrAmtsZf), 2) + '</span>');
                    } else {
                        $('#card1').html(cmccErrAmts + '<span><i class="caret_up"></i>' + $.formatMoney(cmccErrAmtsZf, 2) + '</span>');
                    }
                    // 异常赠送流量占比
                    if (cmccErrAmtPerZf <= 0 || cmccErrAmtPerZf === null) {
                        $('#card2').html(cmccErrAmtPer + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrAmtPerZf), 2) + '%</span>');
                    } else {
                        $('#card2').html(cmccErrAmtPer + '%<span><i class="caret_up"></i>' + $.formatMoney(cmccErrAmtPerZf, 2) + '%</span>');
                    }
                    // 异常赠送次数
                    if (cmccErrNumZf <= 0 || cmccErrNumZf === null) {
                        $('#card3').html(cmccErrNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrNumZf), 0) + '</span>');
                    } else {
                        $('#card3').html(cmccErrNum + '<span><i class="caret_up"></i>' + $.formatMoney(cmccErrNumZf, 0) + '</span>');
                    }
                    // 异常赠送涉及用户数
                    if (cmccErrNumPerZf <= 0 || cmccErrNumPerZf === null) {
                        $('#card4').html(cmccErrNumPer + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrNumPerZf), 0) + '</span>');
                    } else {
                        $('#card4').html(cmccErrNumPer + '<span><i class="caret_up"></i>' + $.formatMoney(cmccErrNumPerZf, 0) + '</span>');
                    }
                    cardsTxt = ['异常赠送流量（G）', '异常赠送流量占比', '异常赠送次数（次）', '异常赠送涉及用户数（个）'];
                    break;
                default: //疑似违规流量转售
                    switch (concern) {
                        case '7001': //疑似违规流量转售
                            if (cmccErrAmtsZf <= 0 || cmccErrAmtsZf === null) {
                                $('#card1').html(cmccErrAmts + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrAmtsZf), 2) + '</span>');
                            } else {
                                $('#card1').html(cmccErrAmts + '<span><i class="caret_up"></i>' + $.formatMoney(cmccErrAmtsZf, 2) + '</span>');
                            }
                            if (cmccErrAmtPerZf <= 0 || cmccErrAmtPerZf === null) {
                                $('#card2').html(cmccErrAmtPer + '%<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccErrAmtPerZf), 2) + '%</span>');
                            } else {
                                $('#card2').html(cmccErrAmtPer + '%<span><i class="caret_up"></i>' + $.formatMoney(cmccErrAmtPerZf, 2) + '%</span>');
                            }
                            cardsTxt = ['疑似违规流量转售流量总值（G）', '疑似违规转售流量占比', '统付涉及集团客户数（个）', ' 疑似违规流量转售集团客户数（个）'];
                            break;
                    }
                    // 
                    if (baseSumAmtZf <= 0 || baseSumAmtZf === null) {
                        $('#card3').html(baseSumAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(baseSumAmtZf), 0) + '</span>');
                    } else {
                        $('#card3').html(baseSumAmt + '<span><i class="caret_up"></i>' + $.formatMoney(baseSumAmtZf, 0) + '</span>');
                    }
                    // 
                    if (cmccSumAmtZf <= 0 || cmccSumAmtZf === null) {
                        $('#card4').html(cmccSumAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(cmccSumAmtZf), 0) + '</span>');
                    } else {
                        $('#card4').html(cmccSumAmt + '<span><i class="caret_up"></i>' + $.formatMoney(cmccSumAmtZf, 0) + '</span>');
                    }
                    break;
            }
            // 改变卡片文字
            $.each(cardsTxt, function (idx) {
                $('#cardContainer dl:eq(' + idx + ') dd').text(this);
            });
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
        url: "/cmca/llglwg/getMapData",
        type: 'get',
        dataType: 'json',
        data: postData,
        cache: false,
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
                                case 'errTrafficNumber': //异常赠送流量
                                    map.properties.trueValue = data[prvdCode].errTrafficNumber;
                                    break;
                                case 'trafficNumColumn': //疑似违规转售流量排名
                                    map.properties.trueValue = data[prvdCode].errTrafficNumber;
                                    break;
                                case 'trafficPercentColumn': //疑似违规转售流量占比排名
                                    map.properties.trueValue = data[prvdCode].errTrafficPercent;
                                    break;
                                case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                    map.properties.trueValue = data[prvdCode].illegalGroupNumber;
                                    break;
                                case 'illegalGroupPercentColumn': //疑似违规转售集团客户数占比排名
                                    map.properties.trueValue = data[prvdCode].illegalGroupPercent;
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
                            // 如果是异常发放电子券关注点下，则下钻到省地图

                            prvdId = e.point.properties.id;
                            // 异步下钻
                            if (e.point.drilldown && unDrilldown.indexOf(e.point.drilldown) === -1) {
                                // 插入一经事件码-查询
                                dcs.addEventCode('MAS_HP_CMCA_child_query_02');
                                // 日志记录
                                get_userBehavior_log('专题', '流量管理违规', '地图下钻', '查询');

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
                                        $('#nanhaiQundao').hide();
                                        // 改变隐藏域val
                                        $('#prvdId').val(prvdId);
                                        $('#prvdName').val(e.point.drilldown);
                                        $('#prvdNameZH').val(e.point.properties.cnname);
                                        // 改变标题
                                        mapTitle();
                                        // 加载地图底部卡片数据
                                        load_mapBtm_card_chart();
                                        if ($('#resultTabCon').is(':visible')) {
                                            // 右侧审计结果图形联动
                                            load_result_chart();
                                        } else if ($('#fenxiFourNav1FiveNav1Con').is(':visible')) {
                                            // 右侧排名汇总表格联动
                                            load_fenxi_pmhz_table();
                                        } else if ($('#fenxiFourNav1FiveNav4Con').is(':visible')) {
                                            //右侧重点关注营销案
                                            load_fenxi_zdgz_case_table();
                                        } else if ($('#fenxiFourNav1FiveNav5Con').is(':visible')) {
                                            load_fenxi_zdgz_chnl_table(); //重点关注营渠道
                                        } else if ($('#fenxiFourNav1FiveNav6Con').is(':visible')) {
                                            load_fenxi_zdgz_user_table(); //重点关注营用户
                                        } else if ($('#fenxiFourNav1FiveNav7Con').is(':visible')) {
                                            load_fenxi_zdgz_zsgroupUser_table(); //疑似转售集团客户
                                        } else if ($('#fenxiFourNav1FiveNav8Con').is(':visible')) {
                                            load_fenxi_wglxfb_chart(); //违规类型分布
                                        } else if ($('#fenxiFourNav1FiveNav9Con').is(':visible')) {
                                            load_fenxi_zdgz_groupUser_table(); //重点关注集团用户
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
                                            url: "/cmca/llglwg/getMapData",
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
                                                                    case 'errTrafficNumber': //异常赠送流量
                                                                        map.properties.trueValue = ctyData[ctyCode].errTrafficNumber;
                                                                        break;
                                                                    case 'trafficNumColumn': //疑似违规转售流量排名
                                                                        map.properties.trueValue = ctyData[ctyCode].errTrafficNumber;
                                                                        break;
                                                                    case 'trafficPercentColumn': //疑似违规转售流量占比排名
                                                                        map.properties.trueValue = ctyData[ctyCode].errTrafficPercent;
                                                                        break;
                                                                    case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                                                        map.properties.trueValue = ctyData[ctyCode].illegalGroupNumber;
                                                                        break;
                                                                    case 'illegalGroupPercentColumn': //疑似违规转售集团客户数占比
                                                                        map.properties.trueValue = ctyData[ctyCode].illegalGroupPercent;
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
                                                            return this.point.name;
                                                        }
                                                    },
                                                    events: {
                                                        click: function (e) {
                                                            //地市地图下钻表格弹层
                                                            $('#mapTableDialog').show();
                                                            $('#chooseWeiDu').val('重点关注营销案');
                                                            //$('#mapUserChooseConcernWrap1').show();
                                                            $('#dialogQudaoTableTitle').show();
                                                            //$('#mapUserChooseConcernWrap').hide();
                                                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '疑似违规流量转售集团客户');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(prvdId);
                                                            } else {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '省' + e.point.name + '疑似违规流量转售集团客户');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(e.point.properties.code);
                                                            }
                                                            // 加载数据
                                                            if ($("#concern").val() == "7003") {
                                                                $('#mapUserChooseConcernWrap1').show();
                                                                $('#mapUserChooseConcernWrap').hide();
                                                                load_cty_zdgz_case_table();
                                                            } else {
                                                                $('#mapUserChooseConcernWrap1').hide();
                                                                $('#mapUserChooseConcernWrap').hide();
                                                                $('#dialogQudaoTableTitle').hide();
                                                                load_cty_zdgz_bloc_table();
                                                            }
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
                            } else if ($('#fenxiFourNav1FiveNav4Con').is(':visible')) {
                                //右侧重点关注营销案
                                load_fenxi_zdgz_case_table();
                            } else if ($('#fenxiFourNav1FiveNav5Con').is(':visible')) {
                                load_fenxi_zdgz_chnl_table(); //重点关注营渠道
                            } else if ($('#fenxiFourNav1FiveNav6Con').is(':visible')) {
                                load_fenxi_zdgz_user_table(); //重点关注营用户
                            } else if ($('#fenxiFourNav1FiveNav7Con').is(':visible')) {
                                load_fenxi_zdgz_zsgroupUser_table(); //疑似转售集团客户
                            } else if ($('#fenxiFourNav1FiveNav8Con').is(':visible')) {
                                load_fenxi_wglxfb_chart(); //违规类型分布
                            }else if ($('#fenxiFourNav1FiveNav9Con').is(':visible')) {
                                load_fenxi_zdgz_groupUser_table(); //重点关注集团用户
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
                        var tipTxt, unit;
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
                                case 'errTrafficNumber': //异常赠送流量
                                    tipTxt = '异常赠送流量';
                                    unit = 'G';
                                    break;
                                case 'trafficNumColumn': //疑似违规转售流量值
                                    tipTxt = '疑似违规转售流量值';
                                    unit = 'G';
                                    break;
                                case 'trafficPercentColumn': //疑似违规转售流量占比
                                    tipTxt = '疑似违规转售流量占比';
                                    unit = '%';
                                    break;
                                case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                    tipTxt = '疑似违规转售集团客户数';
                                    unit = '个';
                                    break;
                                case 'illegalGroupPercentColumn': //疑似违规转售集团客户数占比
                                    tipTxt = '疑似违规转售集团客户数占比';
                                    unit = '%';
                                    break;
                            }
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/><span>' + tipTxt + '：' + Highcharts.numberFormat(dt, 2, ".", ",") + (unit === '%' ? unit : (' ' + unit)) + '</span>';
                            } else {
                                return '<span>' + cnm + '</span><br/><span>' + tipTxt + '：' + Highcharts.numberFormat(dt, 2, ".", ",") + (unit === '%' ? unit : (' ' + unit)) + '</span>';
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
        cache: false,
        success: function (data) {
            // 解析地市地图数据
            ctyMap = Highcharts.geojson(data);
            // 请求省数据
            $.ajax({
                url: "/cmca/llglwg/getMapData",
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
                                    case 'errTrafficNumber': //异常赠送流量
                                        map.properties.trueValue = ctyData[prvdId].errTrafficNumber;
                                        break;
                                    case 'trafficNumColumn': //疑似违规转售流量
                                        map.properties.trueValue = ctyData[prvdId].errTrafficNumber;
                                        break;
                                    case 'trafficPercentColumn': //疑似违规转售流量占比
                                        map.properties.trueValue = ctyData[prvdId].errTrafficPercent;
                                        break;
                                    case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                        map.properties.trueValue = ctyData[prvdId].illegalGroupNumber;
                                        break;
                                    case 'illegalGroupPercentColumn': //疑似违规转售集团客户数占比
                                        map.properties.trueValue = ctyData[prvdId].illegalGroupPercent;
                                        break;
                                }
                            } else { // 不是直辖市
                                //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    order = ctyData[ctyCode].rn;
                                    switch (orderName) {
                                        case 'errTrafficNumber': //异常赠送流量
                                            map.properties.trueValue = ctyData[ctyCode].errTrafficNumber;
                                            break;
                                        case 'trafficNumColumn': //疑似违规转售流量值
                                            map.properties.trueValue = ctyData[ctyCode].errTrafficNumber;
                                            break;
                                        case 'trafficPercentColumn': //疑似违规转售流量值占比
                                            map.properties.trueValue = ctyData[ctyCode].errTrafficPercent;
                                            break;
                                        case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                            map.properties.trueValue = ctyData[ctyCode].illegalGroupNumber;
                                            break;
                                        case 'illegalGroupPercentColumn': //疑似违规转售集团客户数占比
                                            map.properties.trueValue = ctyData[ctyCode].illegalGroupPercent;
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
                            text: null
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
                                var tipTxt, unit;
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
                                        case 'errTrafficNumber': //异常赠送流量
                                            tipTxt = '异常赠送流量';
                                            unit = 'G';
                                            break;
                                        case 'trafficNumColumn': //疑似违规转售流量值
                                            tipTxt = '疑似违规转售流量值';
                                            unit = 'G';
                                            break;
                                        case 'trafficPercentColumn': //疑似违规转售流量占比排名
                                            tipTxt = '疑似违规转售流量占比排名';
                                            unit = '%';
                                            break;
                                        case 'illegalGroupNumColumn': //疑似违规转售集团客户数
                                            tipTxt = '疑似违规转售集团客户数';
                                            unit = '个';
                                            break;
                                        case 'illegalGroupPercentColumn': //疑似违规转售流量占比排名
                                            tipTxt = '疑似违规转售集团客户数占比';
                                            unit = '%';
                                            break;
                                    }
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>' + tipTxt + '：' + Highcharts.numberFormat(dt, 2, ".", ",") + (unit === '%' ? unit : (' ' + unit)) + '</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>' + tipTxt + '：' + Highcharts.numberFormat(dt, 2, ".", ",") + (unit === '%' ? unit : (' ' + unit)) + '</span>';
                                    }
                                }
                            }
                        },
                        colorAxis: {
                            dataClasses: $.mapColor()
                        },
                        series: [{
                            events: {
                                click: function (e) { //----------------------------------------------------------待修改
                                    //地市地图下钻表格弹层
                                    $('#mapTableDialog').show();
                                    // 判断是否是直辖市
                                    if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '疑似违规流量转售集团客户');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(prvdId);
                                    } else {
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '省' + e.point.name + '疑似违规流量转售集团客户');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(e.point.properties.code);
                                    }
                                    // 加载数据
                                    if ($("#concern").val() == "7003") {
                                        $('#mapUserChooseConcernWrap1').show();
                                        $('#mapUserChooseConcernWrap').hide();
                                        load_cty_zdgz_case_table();
                                    } else {
                                        $('#mapUserChooseConcernWrap1').hide();
                                        $('#dialogQudaoTableTitle').hide();
                                        $('#mapUserChooseConcernWrap').hide();
                                        load_cty_zdgz_bloc_table();
                                    }
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
    var concern = $('#concern').val();
    // 地图容器初始化
    $('#chinaMap').empty();
    // 标题重绘
    mapTitle();
    // 判断全国/省，绘制全国/省地图
    if ($('#prvdId').val() != 10000) {
        // 判断异常发放电子券/电子券平台间数据不一致，绘制省地图/饼图
        /*  if (concern == 7001) {
             load_cty_zdgz_bloc_table();
         } else {
             drawProvMap();
         } */
        drawProvMap();
    } else {
        $('#chooseProvince').val('全公司');
        drawChinaMap();
    }
    load_mapBtm_card_chart();
}