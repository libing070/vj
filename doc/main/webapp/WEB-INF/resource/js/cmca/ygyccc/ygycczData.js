// 审计结果-异常赠送金额排名
function load_result_amt_pm_chart(tooltip) {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量;
    $.ajax({
        url: "/cmca/ygyc/getYgycPresentAmount",
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
                    }
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + ' : </span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 百万元</b>';
                    },
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: tooltip,
                    data: data.amtList,
                    color: "#3095f2"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示
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

// 审计结果-异常操作工号数量排名
function load_result_num_pm_chart() {
    var postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        noDataX; //暂无数据文本显示的偏移量

    $.ajax({
        url: "/cmca/ygyc/getYgycEmployeeQty",
        dataType: 'json',
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
                    title: {
                        text: null
                    }
                },
                tooltip: {
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + ';">' + this.series.name + ' : </span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 人</b>';
                    },
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0,
                        pointWidth: $.pointW()
                    }
                },
                series: [{
                    name: '异常操作工号数量',
                    data: data.numList,
                    color: "#ff647f"
                }],
                lang: {
                    noData: "暂无数据" //无数据显示
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
            $("#contentShowWrap2").getNiceScroll(0).show();
            $("#contentShowWrap2").getNiceScroll(0).resize();
            $("#contentShowWrap2").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-异常赠送金额趋势
function load_result_amt_qs_chart(tooltip) {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };

    $.ajax({
        url: "/cmca/ygyc/getYgycPreAmountLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow3').css('minWidth', data.audtrmList.length * 10 + '%');
            $('#contentShow3').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
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
                            return this.value / 1;
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 2, '.', ',') + ' 百万元</b>';
                    },
                    useHTML: true
                },
                series: [{
                    name: tooltip,
                    data: data.amtList,
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

// 审计结果-异常工号数量趋势
function load_result_num_qs_chart() {
    var postData = {
        prvdId: $('#prvdId').val(),
        audTrm: $('#audTrm').val(),
        concern: $('#concern').val()
    };

    $.ajax({
        url: "/cmca/ygyc/getYgycEmployeeQtyLine",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $('#contentShow4').css('minWidth', data.audtrmList.length * 10 + '%');
            $('#contentShow4').highcharts({
                chart: {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title: {
                    text: null
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
                    }]
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><br/>',
                    pointFormatter: function () {
                        return '<span style="color:' + this.series.color + '">' + this.series.name + '：</span><b>' + Highcharts.numberFormat(this.y, 0, '.', ',') + ' 人</b>';
                    },
                    useHTML: true
                },
                series: [{
                    name: '异常工号数量趋势',
                    data: data.numList,
                    color: '#8885d5'
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
            $("#contentShowWrap4").getNiceScroll(0).show();
            $("#contentShowWrap4").getNiceScroll(0).resize();
            $("#contentShowWrap4").getNiceScroll(0).doScrollLeft(0);
        }
    });
}

// 审计结果-四个图形
function load_result_chart() {
    var concern = parseInt($('#concern').val()),
        tooltip = mapTooltip();
    load_result_amt_pm_chart(tooltip);
    load_result_amt_qs_chart(tooltip + '趋势');
    load_result_num_pm_chart();
    load_result_num_qs_chart();
}

// 统计报表-排名汇总
function load_fenxi_pmhz_table() {
    $('#rankingAllTable').bootstrapTable('destroy');
    $('#rankingAllTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            //暂不联动
            //prvdId: $('#prvdId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav1Con').height()) - 40,
        dataConcern = $("#concern").val(),
        title5, title6, title7;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title5 = '赠送话费金额（元）';
            title6 = '赠送账户数';
            title7 = '赠送次数';
            break;
            //异常高额退费
        case '5004':
            title5 = '退费金额（元）';
            title6 = '退费账户数';
            title7 = '退费次数';
            break;
            //异常高额积分赠送
        case '5001':
            title5 = '赠送积分价值金额（元）';
            title6 = '赠送账户数';
            title7 = '赠送次数';
            break;
            //异常积分
        case '5002':
            title5 = '转入积分价值金额（元）';
            title6 = '退费账户数';
            title7 = '转入次数';
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    // 千分位，保留两位小数
    function operateFormatter2(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 2, "table");
        } else {
            return '-';
        }
    }
    $.ajax({
        url: "/cmca/ygyc/getRankTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#rankingAllTable").bootstrapTable({
                    datatype: "local",
                    data: data.rankTable, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '序号',
                        valign: 'middle',
                        formatter: rn,
                        width: '8%'
                    }, {
                        field: 'prvdName',
                        // title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title:'公司',
                        valign: 'middle',
                        width: '10%'
                    }, {
                        field: 'prvdNum',
                        title: '违规地市数量',
                        sortable: true,
                        valign: 'middle'
                    }, {
                        field: 'employeeQty',
                        title: '操作工号数',
                        //sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter0
                    }, {
                        field: 'totalAmt',
                        title: title5,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'totalSubs',
                        title: title6,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'totalTimes',
                        title: title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }]
                });
                $('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
                // $('#rankingAllTable thead').remove();
                scroll('#rankingAllTableWrap', '#rankingAllTable');
            }
        }
    });
}

// 统计报表-历史统计
function load_fenxi_lstj_chart() {
    $('#historyTable').bootstrapTable('destroy');
    $('#historyTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav2Con').height()) - 40,
        time = $("#audTrm").val(),
        dataConcern = $("#concern").val(),
        title7,audTrms;
        if(time!=""){
            audTrms = monthSub(time, 6); //月份递减
        }else{
            audTrms =["","","","","","","",""];
        }
       
      
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title7 = '赠送次数';
            break;
            //异常高额退费
        case '5004':
            title7 = '退费次数';
            break;
            //异常高额积分赠送
        case '5001':
            title7 = '赠送次数';
            break;
            //异常积分
        case '5002':
            title7 = '转入次数';
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    $.ajax({
        url: "/cmca/ygyc/getHistoryTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                $("#historyTable").bootstrapTable({
                    datatype: "local",
                    data: data.historyTable, //加载数据
                    pagination: false, //是否显示分页
                    cache: false,
                    height: h,
                    columns: [{
                        field: 'rn',
                        title: '序号',
                        valign: 'middle',
                        formatter: rn,
                        width: '8%'
                    }, {
                        field: 'prvdName',
                        //title: ($('#prvdId').val() === '10000') ? '省公司' : '地市公司',
                        title: '公司',
                        valign: 'middle',
                        width: '10%'
                    }, {
                        field: 'month1',
                        title: audTrms[0] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'month2',
                        title: audTrms[1] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'month3',
                        title: audTrms[2] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'month4',
                        title: audTrms[3] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'month5',
                        title: audTrms[4] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'month6',
                        title: audTrms[5] + title7,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }]
                });
                $('#historyTable').parent('.fixed-table-body').attr('id', 'historyTableWrap');
                // $('#historyTable thead').remove();
                scroll('#historyTableWrap', '#historyTable');
            }
        }
    });
}

// 统计报表-重点关注工号
function load_fenxi_zdgz_Num_table() {
    // 表格重置
    $('#ycghfxTable').bootstrapTable('destroy');
    $('#ycghfxTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav3Con').height()) - 40,
        time = $("#audTrm").val(),
        dataConcern = $("#concern").val(),
        title5, title6,audTrms;
        if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title5 = '赠送话费金额（万元）';
            title6 = '赠送账户数';
            break;
            //异常高额退费
        case '5004':
            title5 = '退费金额（万元）';
            title6 = '退费账户数';
            break;
            //异常高额积分赠送
        case '5001':
            title5 = '赠送积分价值金额（万元）';
            title6 = '赠送账户数';
            break;
            //异常积分
        case '5002':
            title5 = '转入积分价值金额（万元）';
            title6 = '退费账户数';
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位，保留小数
    function operateFormatter2(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 2, "table");
        } else {
            return '-';
        }
    }
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    $.ajax({
        url: "/cmca/ygyc/getYgycFocusEmployee",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#ycghfxTable").bootstrapTable({
                datatype: "local",
                data: data.employeeData,
                pagination: false, //是否显示分页
                cache: false,
                height: h,
                columns: [
                    [{
                        field: 'rn',
                        title: '序号',
                        valign: 'middle',
                        rowspan: 2,
                        width: '8%',
                        formatter: rn
                    }, {
                        field: 'prvd_name',
                        title: '公司',
                        valign: 'middle',
                        rowspan: 2,
                    }, {
                        field: 'city_name',
                        title: '地市',
                        valign: 'middle',
                        rowspan: 2
                    }, {
                        field: 'staff_id',
                        title: '操作员工标识',
                        valign: 'middle',
                        rowspan: 2,
                        //sortable: true,
                        formatter: tagColumn
                    }, {
                        field: 'audTrm1',
                        title: audTrms[0],
                        valign: 'middle',
                        colspan: 2
                    }, {
                        field: 'audTrm2',
                        title: audTrms[1],
                        valign: 'middle',
                        colspan: 2
                    }, {
                        field: 'audTrm3',
                        title: audTrms[2],
                        valign: 'middle',
                        colspan: 2
                    }],
                    [{
                        field: 'total_Value_t1',
                        title: title5,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Subs_t1',
                        title: title6,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'total_Value_t2',
                        title: title5,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Subs_t2',
                        title: title6,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }, {
                        field: 'total_Value_t3',
                        title: title5,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Subs_t3',
                        title: title6,
                        valign: 'middle',
                        sortable: true,
                        formatter: operateFormatter0
                    }]
                ]
            });
            // 下钻列添加a标签
            function tagColumn(value, row, index) {
                return '<a href="javascript:;" value="' + row.staff_id + '">' + value + '</a>';
            }
            // 添加滚动条
            $('#ycghfxTable').parent('.fixed-table-body').attr('id', 'ycghfxTableWrap');
            // $('#ycghfxTable thead').remove();
            scroll('#ycghfxTableWrap', '#ycghfxTable');
        }
    });
}

// 统计报表-重点关注工号分析下钻
function load_fenxi_zdgz_NumInfo_table() {
    //隐藏上级滚动条
    $('#ycghfxTableTo').bootstrapTable('destroy');
    $('#ycghfxTableTo').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            staffId: $('#staffId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav3Con').height()) - 40,
        time = $("#audTrm").val(),
        dataConcern = $('#concern').val(),
        keepDec;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title = '赠送金额（元）';
            keepDec = 2;
            break;
            //异常高额退费
        case '5004':
            title = '退费金额（元）';
            keepDec = 2;
            break;
            //异常高额积分赠送
        case '5001':
            title = '赠送积分';
            keepDec = 0;
            break;
            //异常积分转移
        case '5002':
            title = '转移积分';
            keepDec = 0;
            break;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位，保留小数
    function operateFormatter2(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 2, "table");
        } else {
            return '-';
        }
    }
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    $.ajax({
        url: "/cmca/ygyc/getYgycFocusEmployeeTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#ycghfxTableTo").bootstrapTable({
                datatype: "local",
                data: data.detailsData,
                pagination: false, //是否显示分页
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '序号',
                    valign: 'middle',
                    formatter: rn
                }, {
                    field: 'prvd_name',
                    title: '公司',
                    valign: 'middle'
                }, {
                    field: 'city_name',
                    title: '地市',
                    valign: 'middle'
                }, {
                    field: 'staff_id',
                    title: '操作员工标识',
                    valign: 'middle',
                }, {
                    field: 'Aud_trm',
                    title: '月份',
                    valign: 'middle'
                }, {
                    field: 'trade_amt_tot',
                    title: title,
                    valign: 'middle',
                    sortable: true,
                    formatter: dataConcern == '5003' || dataConcern == '5004' ? operateFormatter2 : operateFormatter0
                }, {
                    field: 'subs_id',
                    title: '用户标识',
                    valign: 'middle',
                }]
            });
            // 添加滚动条
            $('#ycghfxTableTo').parent('.fixed-table-body').attr('id', 'ycghfxTableToWrap');
            // $('#ycghfxTableTo thead').remove();
            scroll('#ycghfxTableToWrap', '#ycghfxTableTo');
        }
    });
}

// 统计报表-重点关注用户 
function load_fenxi_zdgz_User_table() {
    $('#sjyhfxTable').bootstrapTable('destroy');
    $('#sjyhfxTable').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav4Con').height()) - 40,
        time = $("#audTrm").val(),
        dataConcern = $("#concern").val(),
        title5, title6,audTrms;
        if(time!=""){
            audTrms = monthSub(time, 3); //月份递减 
        }else{
            audTrms =["","","","","","","",""];
        }
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title5 = '异常高额话费赠送（万元）';
            break;
            //异常高额退费
        case '5004':
            title5 = '异常高额退费（万元）';
            break;
            //异常高额积分赠送
        case '5001':
            title5 = '异常高额积分赠送（万元）';
            break;
            //异常积分
        case '5002':
            title5 = '异常高额积分转移（万元）';
            break;
        default:
            return;
    }

    // 千分位，保留小数
    function operateFormatter2(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 2, "table");
        } else {
            return '-';
        }
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }

    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.subsId + '">' + value + '</a>';
    }
    $.ajax({
        url: "/cmca/ygyc/getFocusUserTable",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#sjyhfxTable").bootstrapTable({
                datatype: "local",
                data: data.FocusUserData,
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
                        width: '8%',
                        formatter: rn
                    }, {
                        field: 'prvdName',
                        title: '公司',
                        align: 'center',
                        valign: 'middle',
                        colspan: 1,
                        rowspan: 2
                    }, {
                        field: 'cityName',
                        title: '地市',
                        align: 'center',
                        colspan: 1,
                        rowspan: 2,
                        valign: 'middle'
                    }, {
                        field: 'subsId',
                        title: dataConcern == '5001' || dataConcern == '5002' ? '用户标识' : '账户标识',
                        align: 'center',
                        colspan: 1,
                        rowspan: 2,
                        valign: 'middle',
                        formatter: tagColumn
                    }, {
                        title: audTrms[0],
                        align: 'center',
                        valign: 'middle',
                        colspan: 2,
                        rowspan: 1
                    }, {
                        title: audTrms[1],
                        align: 'center',
                        valign: 'middle',
                        colspan: 2,
                        rowspan: 1
                    }, {
                        title: audTrms[2],
                        align: 'center',
                        colspan: 2,
                        rowspan: 1,
                        valign: 'middle'
                    }],
                    [{
                        field: 'total_Value_t1',
                        title: title5,
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Staff_t1',
                        title: '操作工号数',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                    }, {
                        field: 'total_Value_t2',
                        title: title5,
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Staff_t2',
                        title: '操作工号数',
                        align: 'center',
                        sortable: true,
                        valign: 'middle'
                    }, {
                        field: 'total_Value_t3',
                        title: title5,
                        align: 'center',
                        sortable: true,
                        valign: 'middle',
                        formatter: operateFormatter2
                    }, {
                        field: 'total_Staff_t3',
                        title: '操作工号数',
                        align: 'center',
                        sortable: true,
                        valign: 'middle'
                    }]
                ]
            });
            $('#sjyhfxTable').parent('.fixed-table-body').attr('id', 'sjyhfxTableWrap');
            // $('#sjyhfxTable thead').remove();
            scroll('#sjyhfxTableWrap', '#sjyhfxTable');
        }
    });

}

//统计报表-重点关注用户分析下钻
function load_fenxi_zdgz_UserInfo_table() {
    //隐藏上级滚动条
    $('#sjyhfxTableTo').bootstrapTable('destroy');
    $('#sjyhfxTableTo').bootstrapTable('resetView');
    var postData = {
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            acctId: $('#acctId').val()
        },
        h = parseInt($('#fenxiFourNav1FiveNav4Con').height()) - 40,
        dataConcern = $("#concern").val(),
        keepDec;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title5 = '异常高额话费赠送（元）';
            keepDec = 2;
            break;
            //异常高额退费
        case '5004':
            title5 = '异常高额退费（元）';
            keepDec = 2;
            break;
            //异常高额积分赠送
        case '5001':
            title5 = '异常高额积分赠送';
            keepDec = 0;
            break;
            //异常积分
        case '5002':
            title5 = '异常高额积分转移';
            keepDec = 0;
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    // 千分位，不保留小数
    function operateFormatter0(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 0, "table");
        } else {
            return '-';
        }
    }
    // 千分位，不保留小数
    function operateFormatter2(value, row, index) {
        if (value != "-") {
            return $.formatMoney(value, 2, "table");
        } else {
            return '-';
        }
    }
    $.ajax({
        url: "/cmca/ygyc/getFocusUserDetaile",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#sjyhfxTableTo").bootstrapTable({
                datatype: "local",
                data: data.DetaileData,
                pagination: false,
                cache: false,
                height: h,
                columns: [{
                    field: 'rn',
                    title: '序号',
                    valign: 'middle',
                    formatter: rn
                }, {
                    field: 'prvd_name',
                    title: '公司',
                    valign: 'middle'
                }, {
                    field: 'city_name',
                    title: '地市',
                    valign: 'middle'
                }, {
                    field: 'subs_id',
                    title: dataConcern == '5001' || dataConcern == '5002' ? '用户标识' : '账户标识',
                    valign: 'middle'
                }, {
                    field: 'staff_id',
                    title: '操作员工标识',
                    valign: 'middle',
                }, {
                    field: 'totalAmt',
                    title: title5,
                    valign: 'middle',
                    sortable: true,
                    formatter: dataConcern == '5003' || dataConcern == '5004' ? operateFormatter2 : operateFormatter0
                }, {
                    field: 'audTrm',
                    title: '月份',
                    valign: 'middle',
                }]

            });
            $('#sjyhfxTable').parent('.fixed-table-body').attr('id', 'sjyhfxTableWrap');
            // $('#sjyhfxTable thead').remove();
            scroll('#sjyhfxTableWrap', '#sjyhfxTable');
        }
    });

}

// 统计分析-审计报告
function load_fenxi_sjbg_table() {
    // 页面状态初始化
    $('#fenxiFourNav2Con').find('.audtrm_off,.audtrm_on').hide();
    // 定义所需变量
    var auditDates = auditDate(), //审计时间
        myData,
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            time: new Date().getTime()
        };
    // 填充表格上方文字说明数据
    $('#sjbgPrvdName').text($('#prvdId').val() == '10000' ? $('#prvdNameZH').val() : $('#prvdNameZH').val() + '公司'); //被审计公司
    $('#sjbgAudTrm').text(auditDates[2]); //审计时间
    $('#sjbgPeriod').text(auditDates[0] + ' -- ' + auditDates[1]); //审计期间

    // 请求数据
    $.ajax({
        url: "/cmca/ygyc/getReportInfo",
        dataType: 'html',
        data: postData,
        success: function (data) {
            if (data != '{"switchState":"audTrmColseForReport"}') {
                $('#fenxiFourNav2Con .audtrm_on').show();
                if (data != "") {
                    $('#baogaoTop5Con').html(data);
                    scroll('#baogaoTop5ConWrap', '#baogaoTop5Con');
                } else {
                    $("#baogaoTop5Con").html('');
                }
            } else {
                $('#fenxiFourNav2Con .audtrm_off').show();
                $('#fenxiFourNav2Con .audtrm_on').hide();
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


// 地图地市下钻-用户工号信息表格
function load_cty_staffIdInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#dialogQudaoTable').bootstrapTable('destroy');
    $('#dialogQudaoTable').bootstrapTable('resetView');
    var postData = {
            prvdId: $('#prvdId').val(),
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        },
        h = parseInt($('#dialogQudaoTableWrap').height()),
        dataConcern = $("#concern").val(),
        title5, title6, title7;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title5 = '赠送话费金额（元）';
            title6 = '赠送账户数';
            title7 = '赠送次数';
            break;
            //异常高额退费
        case '5004':
            title5 = '退费金额（元）';
            title6 = '退费账户数';
            title7 = '退费次数';
            break;
            //异常高额积分赠送
        case '5001':
            title5 = '赠送积分价值金额（元）';
            title6 = '赠送账户数';
            title7 = '赠送次数';
            break;
            //异常积分
        case '5002':
            title5 = '转入积分价值金额（元）';
            title6 = '退费账户数';
            title7 = '转入次数';
            break;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    /**
     * 定义点击可下钻列-渠道名称
     * formatter这个属性属于列参数，意思就是对当前列的数据进行格式化操作，它是一个函数，有三个参数，value，row，index
     * value:代表当前单元格中的值
     * row：代表当前行
     * index:代表当前行的下标,
     */
    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.staffId + '">' + value + '</a>';
    }
    $.ajax({
        url: "/cmca/ygyc/getYgycJobnumber",
        dataType: 'json',
        data: postData,
        showColumns: true,
        success: function (data) {
            $("#dialogQudaoTable").bootstrapTable({
                datatype: "local",
                data: data.jobinfo,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'totalAmt',
                sortOrder: 'desc',
                columns: [{
                    field: 'rn',
                    title: '序号',
                    align: 'center',
                    valign: 'middle',
                    width: '10%',
                    formatter: rn
                }, {
                    field: 'staffId',
                    title: '操作员工号',
                    align: 'center',
                    valign: 'middle',
                    width: '18%',
                    formatter: tagColumn
                }, {
                    field: 'totalAmt',
                    title: title5,
                    align: 'center',
                    valign: 'middle',
                    width: '20%',
                    sortable: true
                }, {
                    field: 'totalSubs',
                    title: title6,
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'totalTimes',
                    title: title7,
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'ctyName',
                    title: '地市公司',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'prvdName',
                    title: '省公司',
                    align: 'center',
                    valign: 'middle',
                }]
            });
            $('#dialogQudaoTable').parent('.fixed-table-body').attr('id', 'dialogQudaoTableWrapCtn');
            // $('#dialogQudaoTable thead').remove();
            scroll('#dialogQudaoTableWrapCtn', '#dialogQudaoTable');
        }
    });
}

//用户工号信息表格-员工_手机号信息表(第二个下钻表格)
function load_cty_accountIdInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#qudaoInfo').bootstrapTable('destroy');
    $('#qudaoInfo').bootstrapTable('resetView');
    $('#qudaoInfo').bootstrapTable('hideColumn', 'chnlId');
    var postData = {
            prvdId: $('#prvdId').val(),
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            staffId: $("#staffId").val()
        },
        h = parseInt($('#accountIdWrap').height()),
        dataConcern = $("#concern").val(),
        title3, title5, title7;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title3 = '账户标识';
            title5 = '赠送话费金额（元）';
            title7 = '赠送次数';
            break;
            //异常高额退费
        case '5004':
            title3 = '账户标识';
            title5 = '退费金额（元）';
            title7 = '退费次数';
            break;
            //异常高额积分赠送
        case '5001':
            title3 = '手机号';
            title5 = '赠送积分';
            title7 = '赠送次数';
            break;
            //异常积分
        case '5002':
            title3 = '手机号';
            title5 = '转入积分';
            title7 = '转入次数';
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    /**
     * 定义点击可下钻列-渠道名称
     * formatter这个属性属于列参数，意思就是对当前列的数据进行格式化操作，它是一个函数，有三个参数，value，row，index
     * value:代表当前单元格中的值
     * row：代表当前行
     * index:代表当前行的下标,
     */
    function tagColumn(value, row, index) {
        return '<a href="javascript:;" value="' + row.accountId + '">' + value + '</a>';
    }
    $.ajax({
        url: "/cmca/ygyc/getYgycPhoneTable",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#qudaoInfo").bootstrapTable({
                datatype: "local",
                data: data.phoneSum,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'totalAmt',
                sortOrder: 'desc',
                columns: [{
                    field: 'rn',
                    title: '序号',
                    align: 'center',
                    valign: 'middle',
                    width: '10%',
                    formatter: rn
                }, {
                    field: 'staffId',
                    title: '操作员工号',
                    align: 'center',
                    valign: 'middle',
                }, {
                    field: 'accountId',
                    title: title3,
                    align: 'center',
                    valign: 'middle',
                    width: '24%',
                    formatter: tagColumn
                }, {
                    field: 'totalTimes',
                    title: title7,
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'totalAmt',
                    title: title5,
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'ctyName',
                    title: '地市公司',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'prvdName',
                    title: '省公司',
                    align: 'center',
                    valign: 'middle'
                }]
            });
            $('#qudaoInfo').parent('.fixed-table-body').attr('id', 'qudaoInfoWrap');
            // $('#qudaoInfo thead').remove();
            scroll('#qudaoInfoWrap', '#qudaoInfo');
        }
    });
}

//员工_手机号信息表-操作员工号赠送明细(第三个下钻表格)
function load_cty_totalAmtInfo_table() {
    //先销毁表格,否则导致加载缓存数据
    $('#qudaoInfo1').bootstrapTable('destroy');
    $('#qudaoInfo1').bootstrapTable('resetView');
    $('#qudaoInfo1').bootstrapTable('hideColumn', 'chnlId');
    var postData = {
            prvdId: $('#prvdId').val(),
            ctyId: $('#ctyId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val(),
            acctId: $("#acctId").val(),
            staffId: $("#staffId").val()
        },
        h = parseInt($('#staffIdWrap').height()),
        dataConcern = $("#concern").val(),
        title3, title4, title5, title7;
    switch (dataConcern) {
        //异常高额话费赠送
        case '5003':
            title3 = '账户标识';
            title4 = '赠送时间';
            title5 = '赠送话费金额（元）';
            title7 = '赠送次数';
            break;
            //异常高额退费
        case '5004':
            title3 = '账户标识';
            title4 = '退费时间';
            title5 = '退费金额（元）';
            title7 = '退费次数';
            break;
            //异常高额积分赠送
        case '5001':
            title3 = '手机号';
            title4 = '赠送时间';
            title5 = '赠送积分';
            title7 = '赠送次数';
            break;
            //异常积分转移
        case '5002':
            title3 = '手机号';
            title4 = '转入时间';
            title5 = '转入积分';
            title7 = '转入次数';
            break;
        default:
            return;
    }
    // 全国显示序号为0时，显示-
    function rn(value, row, index) {
        return ((value === 0) ? '-' : value);
    }
    $.ajax({
        url: "/cmca/ygyc/getYgycOperatorByphone",
        dataType: 'json',
        data: postData,
        success: function (data) {
            $("#qudaoInfo1").bootstrapTable({
                datatype: "local",
                data: data.operatorInfo,
                pagination: false,
                cache: false,
                height: h,
                sortName: 'totalAmt',
                sortOrder: 'desc',
                columns: [{
                    field: 'rn',
                    title: '序号',
                    align: 'center',
                    valign: 'middle',
                    width: '8%',
                    formatter: rn
                }, {
                    field: 'staffId',
                    title: '操作员工号',
                    align: 'center',
                    valign: 'middle',
                    width: '14%',
                }, {
                    field: 'accountId',
                    title: title3,
                    align: 'center',
                    valign: 'middle',
                }, {
                    field: 'totalAmt',
                    title: title5,
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                }, {
                    field: 'operateTime',
                    title: title4,
                    align: 'center',
                    valign: 'middle',
                }, {
                    field: 'chnlName',
                    title: '员工归属渠道名称',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'chnlType',
                    title: '员工归属渠道类型',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'ctyName',
                    title: '地市公司',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'prvdName',
                    title: '省公司',
                    align: 'center',
                    valign: 'middle'
                }]
            });
            $('#qudaoInfo1').parent('.fixed-table-body').attr('id', 'qudaoInfo1Wrap');
            //$('#rankingAllTable thead').remove();
            scroll('#qudaoInfo1Wrap', '#qudaoInfo1');
        }
    });

    //第三个下钻表格删除按钮
    $('#TableToInfo1 .colse_btn').click(function () {
        $('#TableToInfo1').hide();
        $('#TableToInfo').show();
    });
}

// 地图下方卡片
function load_mapBtm_card_chart() {
    // 定义所需变量
    var prvdId = $('#prvdId').val(),
        prvdNum = 0, //涉及公司数量
        prvdNumMom = 0, //涉及公司数量增量
        totalAmt = 0, //涉及总金额
        totalAmountMom = 0, //涉及总金额增量
        employeeQty = 0, //涉及工号数量
        totalOprMom = 0, //涉及工号数量增量
        totalSubs = 0, //涉及用户数
        totalSubsMom = 0, //涉及用户数增量
        // 后台请求参数
        postData = {
            prvdId: $('#prvdId').val(),
            audTrm: $('#audTrm').val(),
            concern: $('#concern').val()
        };
    $.ajax({
        url: "/cmca/ygyc/getYgycMapBottomData",
        type: 'get',
        dataType: 'json',
        data: postData,
        success: function (data) {
            if (JSON.stringify(data) != "{}") {
                prvdNum = $.formatMoney(data[prvdId].prvdNum, 0);
                prvdNumMom = data[prvdId].prvdNumMom;
                totalAmt = $.formatMoney(data[prvdId].totalAmt, 2);
                totalAmountMom = data[prvdId].totalAmountMom;
                employeeQty = $.formatMoney(data[prvdId].employeeQty, 0);
                totalOprMom = data[prvdId].totalOprMom;
                totalSubs = $.formatMoney(data[prvdId].totalSubs, 0);
                totalSubsMom = data[prvdId].totalSubsMom;
            }
            // 涉及省公司数量
            if (prvdNumMom <= 0) {
                $('#card1').html(prvdNum + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(prvdNumMom), 0) + '</span></dt>');
            } else {
                $('#card1').html(prvdNum + '<span><i class="caret_up"></i>' + $.formatMoney(prvdNumMom, 0) + '</span></dt>');
            }
            // 涉及总金额(百万元)
            if (totalAmountMom <= 0) {
                $('#card2').html(totalAmt + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(totalAmountMom), 2) + '</span></dt>');
            } else {
                $('#card2').html(totalAmt + '<span><i class="caret_up"></i>' + $.formatMoney(totalAmountMom, 2) + '</span></dt>');
            }
            // 涉及工号数量
            if (totalOprMom <= 0) {
                $('#card3').html(employeeQty + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(totalOprMom), 0) + '</span></dt>');
            } else {
                $('#card3').html(employeeQty + '<span><i class="caret_up"></i>' + $.formatMoney(totalOprMom, 0) + '</span></dt>');
            }
            // 涉及用户数
            if (totalSubsMom <= 0) {
                $('#card4').html(totalSubs + '<span><i class="caret_down"></i>' + $.formatMoney(Math.abs(totalSubsMom), 0) + '</span></dt>');
            } else {
                $('#card4').html(totalSubs + '<span><i class="caret_up"></i>' + $.formatMoney(totalSubsMom, 0) + '</span></dt>');
            }
        }
    });
}

//主界面地图展示
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
 * concern：隐藏域关注点val
 * 调用方法drawChinaMap()，drawProvMap()
 */
// 绘制全国地图
function drawChinaMap() {
    // 样式
    $("#ProvBackBtn").hide(); // 返回按钮隐藏
    $('#nanhaiQundao').show(); // 显示南海诸岛
    // 定义所需变量
    var prvdNameZH,
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
            concern: $('#concern').val()
        },
        concern = $('#concern').val();
    // 请求全国各省数据
    $.ajax({
        url: "/cmca/ygyc/getYgycMapData",
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
                            map.properties.trueValue = [data[prvdCode].totalAmt, data[prvdCode].employeeQty];
                            if (order === 0) { //无数据
                                map.value = 8;
                            }
                            if (order <= 3 && order > 0) { //全国前三名
                                map.value = 2;
                            }
                            if (order >= 4 && order <= 50) { //全国4-10名
                                map.value = 2;
                            }
                            if (order <= 0) {
                                map.value = 0;
                            }
                        } else {
                            map.value = 8;
                        }
                    } else { //港澳台无数据
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
                                get_userBehavior_log('专题', '员工异常业务操作', '地图下钻', '查询');

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
                                            // 右侧排名汇总表格联动 暂不联动
                                            //load_fenxi_pmhz_table();
                                        }
                                        // 更新发送后台参数
                                        postData = {
                                            prvdId: $('#prvdId').val(),
                                            audTrm: $('#audTrm').val(),
                                            concern: $('#concern').val()
                                        };
                                        // 请求下钻省数据
                                        $.ajax({
                                            url: "/cmca/ygyc/getYgycMapData",
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
                                                            map.properties.trueValue = e.point.properties.trueValue;
                                                            if (ctyData[prvdId].rn === 0) {
                                                                map.value = 8;
                                                            } else {
                                                                map.value = 2;
                                                            }
                                                        } else { // 不是直辖市
                                                            //后台返回的数据中，可能会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                                            if (ctyData[ctyCode] !== undefined) {
                                                                order = ctyData[ctyCode].rn;
                                                                map.properties.trueValue = [ctyData[ctyCode].totalAmt, ctyData[ctyCode].employeeQty];
                                                                if (order === 0) { //无数据
                                                                    map.value = 8;
                                                                }
                                                                if (order <= 3 && order > 0) { //地市前三名
                                                                    map.value = 2;
                                                                }
                                                                if (order >= 4 && order <= 50) { //地市4-10名
                                                                    map.value = 2;
                                                                }
                                                                if (order <= 0) {
                                                                    map.value = 0;
                                                                }
                                                            } else { //地市无数据，灰色
                                                                map.value = 8;
                                                                map.properties.trueValue = [null, null];
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
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '市涉及用户对应的工号信息');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(prvdId);
                                                            } else {
                                                                $('#dialogQudaoTableTitle').text($('#prvdNameZH').val() + '省' + e.point.name + '市涉及用户对应的工号信息');
                                                                // 改变隐藏域
                                                                $('#prvdId').val(prvdId);
                                                                $('#ctyId').val(e.point.properties.code);
                                                            }
                                                            // 加载数据
                                                            load_mapBtm_card_chart();
                                                            load_cty_staffIdInfo_table();
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
                                // 右侧排名汇总表格联动 暂不联动
                                //load_fenxi_pmhz_table();
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
                tooltip: { //鼠标悬浮提示
                    formatter: function () {
                        cnm = this.point.properties.cnname;
                        nm = this.point.name;
                        dt = this.point.properties.trueValue;
                        prvdNameZH = $('#prvdNameZH').val();
                        if ($('#prvdId').val() != 10000) {
                            cnm = this.point.properties.name;
                        }
                        if (nm == "Taiwan" || nm == "HongKong" || nm == "Macau" || dt.indexOf(null) != -1 || dt == null || dt == -1) {
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/>没有数据';
                            } else {
                                return '<span>' + cnm + '</span><br/>没有数据';
                            }
                        } else {
                            // if ($('#concern').val() == '5003') {
                            //     if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                            //         return '<span>' + prvdNameZH + '</span><br/><span>异常赠送金额：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                            //     } else {
                            //         return '<span>' + cnm + '</span><br/><span>异常赠送金额：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                            //     }
                            // } else if ($('#concern').val() == '5001' || $('#concern').val() == '5002') {
                            //     if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                            //         return '<span>' + prvdNameZH + '</span><br/><span>异常积分金额：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                            //     } else {
                            //         return '<span>' + cnm + '</span><br/><span>异常积分金额：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                            //     }
                            // } else {
                            //     return '<span>' + cnm + '</span><br/>没有数据';
                            // }
                            if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                return '<span>' + prvdNameZH + '</span><br/><span>' + mapTooltip() + '：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                            } else {
                                return '<span>' + cnm + '</span><br/><span>' + mapTooltip() + '：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
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
    var prvdId = $('#prvdId').val(),
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
                url: "/cmca/ygyc/getYgycMapData",
                type: 'get',
                dataType: 'json',
                data: postData,
                success: function (ctyData) {
                    // 地图颜色
                    if (JSON.stringify(ctyData) != "{}") { //判断是否有数据，如果有数据，根据数据绘制地图颜色，如果无数据，则默认灰色
                        Highcharts.each(ctyMap, function (map) {
                            map.value = 8;
                            ctyCode = map.properties.code;
                            // 判断是否是直辖市，因为如果是直辖市，则只有直辖市的数据，下钻后所有区域应该都显示一个直辖市的数据
                            // 如果是直辖市，则整个直辖市区域所有区县都显示为红色
                            if (prvdId == 10100 || prvdId == 10200 || prvdId == 10300 || prvdId == 10400) {
                                map.properties.trueValue = [ctyData[prvdId].totalAmt, ctyData[prvdId].employeeQty];
                                if (ctyData[prvdId].rn === 0) {
                                    map.value = 8;
                                } else {
                                    map.value = 2;
                                }
                            } else { // 不是直辖市
                                //后台返回的数据中，可能map.properties.trueValue会出现某个地市没有数据的情况，导致数据中直接没有该地市数据对象，所以在此判断，如果没有数据，即值为undefined，则直接将该地市的值置为0，如果不做此判断，在循环地市数据的时候，没有某个地市的数据会导致报错，而无法下钻
                                if (ctyData[ctyCode] !== undefined) {
                                    order = ctyData[ctyCode].rn;
                                    map.properties.trueValue = [ctyData[ctyCode].totalAmt, ctyData[ctyCode].employeeQty];
                                    if (order === 0) { //无数据
                                        map.value = 8;
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
                                    map.value = 8;
                                    map.properties.trueValue = [null, null];
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
                                if (dt.indexOf(null) != -1 || dt == -1 || dt == null) {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return prvdNameZH + "<br/>没有数据";
                                    } else {
                                        return cnm + "<br/>没有数据";
                                    }
                                } else {
                                    if (prvdNameZH == '北京' || prvdNameZH == '上海' || prvdNameZH == '天津' || prvdNameZH == '重庆') {
                                        return '<span>' + prvdNameZH + '</span><br/><span>' + mapTooltip() + '：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
                                    } else {
                                        return '<span>' + cnm + '</span><br/><span>' + mapTooltip() + '：' + dt[0] + ' 百万元</span><br/><span>异常操作工号数量：' + dt[1] + ' 人</span>';
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
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '市涉及用户对应的工号信息');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(prvdId);
                                    } else {
                                        $('#dialogQudaoTableTitle').text(prvdNameZH + '省' + e.point.name + '市涉及用户对应的工号信息');
                                        // 改变隐藏域
                                        $('#prvdId').val(prvdId);
                                        $('#ctyId').val(e.point.properties.code);
                                    }
                                    // 加载数据
                                    load_mapBtm_card_chart();
                                    load_cty_staffIdInfo_table();
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