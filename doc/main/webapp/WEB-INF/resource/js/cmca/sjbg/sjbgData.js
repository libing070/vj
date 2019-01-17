function monthSubYM(startAudTrm, cycle) {
    var audTrmStr = startAudTrm.toString(),
        year = parseInt(audTrmStr.substring(0, 4)),
        month = parseInt(audTrmStr.substring(4)),
        audTrmArry = [startAudTrm.substring(0, 4) + '年' + startAudTrm.substring(4) + '月']; //取当前审计时间为数组第一个值，作为开始的审计时间
    // 如果递减的周期参数存在且不为0
    if (cycle) {
        for (var idx = 1; idx < cycle; idx++) {
            month--;
            if (month == 0) { //没有0的月份，故当月份为0时，修改月份值为12月，同时年份递减，如：201700 应该为 201612
                month = 12;
                year--;
            }
            if (month < 10) {
                month = '0' + month;
            }
            audTrmArry.push(year + '' + month + '');
        }
    }
    return audTrmArry;
}
var sjbg = (function () {
    /* 获取用户名 */
    var getUserName = function () {
        // 请求数据
        $.ajax({
            url: '/cmca/bgxz/index',
            dataType: 'json',
            type: 'POST',
            cache: false,
            success: function (data) {
                $("#userName").val(data.userName);
                if (data.isupload == "true") { //上传
                    $(".plusdown_list").append('<li id="up" class="up">' +
                        '<a href="#" class="upFile">' +
                        '<span class="glyphicon glyphicon-open-file"></span>文件上传</a>' +
                        '</li>');
                    // $("#approve").css("display", "none");
                    $("#up").css("display", "block");
                    $(".iconGly").css("display", "inline-block");
                }
                if (data.isreview == "true") { //审批
                    $(".plusdown_list").append('<li id="approve" class="approve">' +
                        '<a href="#" class="approveFile">' +
                        '<span class="glyphicon glyphicon-open-file"></span>文件审批</a>' +
                        '</li>');
                    $("#approve").css("display", "block");
                    // $("#up").css("display", "none");
                    $(".iconGly").css("display", "inline-block");
                }
            }
        });
    };
    /* 省公司 审计月 */
    var load_column_list_prvdIdName = function () {
        var postData = {
            prvdId: $('#prvdId').val(),
            subjectId: $('#subjectId').val(),
            roleId: $('#roleId').val()
        };
        $.ajax({
            url: '/cmca/base/getAudTrmDataTrmConf',
            dataType: 'json',
            type: 'POST',
            data: postData,
            async: false,
            cache: false,
            success: function (data) {
                // 下拉列表添加数据
                $("#prvdId").val('');
                $("#chooseProvices").val('');
                $("#provicesNameList li").remove();
                var prvdInfo = data.prvdInfo;
                if (JSON.stringify(prvdInfo) != "[]") {
                    $.each(prvdInfo, function (idx, listObj) {
                        $('#provicesNameList').append('<li><a data="' + listObj.prvdId + '">' + listObj.prvdName + '</a></li>');
                    });
                    $("#prvdId").val(prvdInfo[0].prvdId);
                    $("#chooseProvices").val(prvdInfo[0].prvdName);
                } else {
                    $("#prvdId").val('');
                    $("#chooseProvices").val('');
                    $("#provicesNameList li").remove();
                }
            }
        });
    };
    var load_column_list_audTrm = function () {
        var postData = {
            prvdId: $('#prvdId').val(),
            subjectId: $('#subjectId').val(),
            roleId: $('#roleId').val()
        };
        $.ajax({
            url: '/cmca/base/getAudTrmDataTrmConf',
            dataType: 'json',
            data: postData,
            type: 'POST',
            async: false,
            cache: false,
            success: function (data) {
                // 下拉列表添加数据
                $("#audTrm").val('');
                $("#chooseTime").val('');
                $("#timeList li").remove();
                var audTrmInfo = data.audTrmInfo;
                if (JSON.stringify(audTrmInfo) != "[]") {
                    $.each(audTrmInfo, function (idx, listObj) {
                        var audTrmYear = listObj.audTrm.substring(0, 4); //审计年
                        var audTrmMon = parseInt(listObj.audTrm.substring(4)); //审计月
                        $('#timeList').append('<li><a data="' + listObj.audTrm + '">' + audTrmYear + '年' + audTrmMon + '月' + '</a></li>');
                    });
                    $("#audTrm").val(audTrmInfo[0].audTrm);
                    $("#chooseTime").val($("#timeList li").eq(0).text());
                } else {
                    $("#audTrm").val('');
                    $("#chooseTime").val('');
                    $("#timeList li").remove();
                }
            }
        });
    };
    /* 专题名称 */
    var load_column_list_specialName = function () {
        /*   */
        $.ajax({
            url: '/cmca/csgl/selectModelSubject',
            dataType: 'json',
            type: 'POST',
            async: false,
            cache: false,
            success: function (data) {
                // 下拉列表添加数据
                if (JSON.stringify(data) != "{}") {
                    $.each(data, function (idx, listObj) {
                        /* if ($("#prvdId").val() == "10000") {
                            $('#specialNameList').append('<li><a data="' + listObj.id + '">' + listObj.name + '</li>');
                        } else {
                            if (listObj.id != 9) {
                                $('#specialNameList').append('<li><a data="' + listObj.id + '">' + listObj.name + '</li>');
                            }
                        } */
                        if (listObj.id != 9) {
                            $('#specialNameList').append('<li><a data="' + listObj.id + '">' + listObj.name + '</li>');
                        } else {
                            $('#specialNameList').append('<li class="gzt"><a data="' + listObj.id + '">' + listObj.name + '</li>');
                        }
                    });
                    $("#chooseSpecial").val(data[1].name);
                    $("#subjectId").val(data[1].id);
                } else {
                    $("#chooseSpecial").val('');
                    $("#subjectId").val('');
                }

            }
        });
    };

    ////////////////////////分专题下载次数排名 start///////////////////////
    var loadSjbgCharts1 = function () {
        var myChart1 = echarts.init(document.getElementById('contentShow1'));
        var postData = {
            audTrm: $('#audTrm').val(),
            prvdId: $('#prvdId').val(),
            fileType: $('#fileType').val()
            /* fileType: $('#fileType').attr("data") */
        };
        $.ajax({
            url: "/cmca/bgxz/downNumsBySubject",
            async: true,
            cache: false,
            type: 'POST',
            data: postData,
            dataType: 'json',
            success: function (data) {
                if (data != null) {
                    var yAxisData = [];
                    var seriesData = [];
                    for (var i = 0; i < data.length; i++) {
                        yAxisData.push(data[i].name);
                        seriesData.push(data[i].nums);
                    }
                    option1 = {
                        color: ['#3398DB'],
                        title: {},
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow'
                            },
                            formatter: function (params, ticket, callback) {
                                var e = "<span class='small_size'>" + params[0].axisValue + "<br/>" + $("#audTrm").val() + "<br/>" + "下载：" + params[0].value + "次</span>";
                                return e;
                            },
                            position: [125, 15]
                        },
                        legend: {

                        },
                        grid: {
                            top: '0',
                            left: '0%',
                            right: '4%',
                            bottom: '0',
                            containLabel: true
                        },
                        xAxis: {
                            type: 'value',
                            boundaryGap: [0, 0.01]
                        },
                        yAxis: {
                            type: 'category',
                            data: yAxisData,
                        },
                        series: [{
                            name: '',
                            type: 'bar',
                            itemStyle: {
                                normal: { //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                                    color: function (params) {
                                        // build a color map as your need.
                                        var colorList = [
                                            '#9973BE',
                                            '#66AD83',
                                            '#F9D02A',
                                            '#F97372',
                                            '#1FA7F1',
                                            '#61D5BE',
                                            '#F4A628',
                                            '#0192d5'
                                        ];
                                        return colorList[params.dataIndex];
                                    }
                                }
                            },
                            data: seriesData
                        }]
                    };
                    myChart1.clear();
                    myChart1.setOption(option1);
                }
            }
        });
        return {
            "myChart1": myChart1
        };
    };
    ///////////////////////////////////分专题下载次数排名end//////////////////////////////////////////

    ///////////////////////////////////分省下载次数排名start////////////////////////////////////////////
    var loadSjbgCharts2 = function () {
        var myChart2 = echarts.init(document.getElementById('contentShow2'));
        var postData = {
            audTrm: $('#audTrm').val(),
            fileType: $('#fileType').val(),
            subjectId: $('#subjectId').val()
        };
        $.ajax({
            url: "/cmca/bgxz/downNumsByPrvd",
            async: true,
            cache: false,
            data: postData,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                if (data != null) {
                    var xAxisData = [];
                    var seriesData = [];
                    for (var i = 0; i < data.length; i++) {
                        xAxisData.push(data[i].CMCC_prov_prvd_nm);
                        if (data[i].down_count == undefined) {
                            seriesData.push(0);
                        } else {
                            seriesData.push(data[i].down_count);
                        }
                    }
                    option2 = {
                        color: ['#3398DB'],
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (params, ticket, callback) {
                                var e = "<span class='small_size'>" + params[0].axisValue + $("#audTrm").val() + "下载：" + params[0].value + "次</span>";
                                return e;
                            }
                        },
                        grid: {
                            top: '3%',
                            left: '0',
                            right: '0',
                            bottom: '4%',
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: xAxisData,
                            axisLabel: {
                                interval: 0,
                                rotate: 0
                            }
                            /* axisTick: {
                                alignWithLabel: true
                            } */
                        }],
                        yAxis: [{
                            type: 'value'
                        }],
                        series: [{
                            name: '',
                            type: 'bar',
                            //barWidth: 1, //柱图宽度
                            //barMaxWidth: '10',
                            itemStyle: {
                                normal: {
                                    color: function (params) {
                                        // build a color map as your need.
                                        var colorList = [
                                            '#3BBB80', '#00BCD5',
                                            '#0192D5', '#F4A628',
                                            '#E055A6', '#91588F',
                                            '#7474EF', '#F05C6E',
                                            '#67AC82', '#A64DF5',
                                            '#9A72BD', '#00D5D2',
                                            '#497BD7', '#D2A45A',
                                            '#F4D028'
                                        ];
                                        // return colorList[params.dataIndex]
                                        return colorList[Math.floor(Math.random() * colorList.length)]; //颜色随机取值
                                    }
                                }
                            },
                            data: seriesData
                        }]
                    };

                    myChart2.clear();
                    // 使用刚指定的配置项和数据显示图表。
                    myChart2.setOption(option2);

                }
            }
        });

        return {
            "myChart2": myChart2
        };
    };
    ///////////////////////////////////分省下载次数排名end////////////////////////////////////////////

    ///////////////////////////////////分省生成时间分布start////////////////////////////////////////////
    var loadSjbgCharts3 = function () {
        var d = new Date(),
            currMon = parseInt(d.getMonth() + 1),
            time = d.getFullYear() + "" + (currMon < 10 ? "0" + currMon : currMon),
            audTrmArr = monthSub(time, 6),
            audTrms = [],
            postData = {
                fileType: $('#fileType').val(),
                prvdId: $('#prvdId').val(),
                subjectId: $('#subjectId').val()
            },
            Tbday = [], //审计通报
            Pmday = [], //排名汇总
            Reportday = [], //审计报告
            Detailday = []; //审计清单
        // 改变日期格式
        $.each(audTrmArr, function (idx, val) {
            var audTrm = val.replace(/[\u4e00-\u9fa5]/g, '');
            audTrms.push(audTrm);
        });

        $.ajax({
            url: "/cmca/bgxz/createDayBySubjectMonth",
            dataType: 'json',
            data: postData,
            type: 'POST',
            cache: false,
            success: function (data) {
                if (data != null) {
                    var filetType = $("#fileType").val();
                    switch (filetType) {
                        case '': //全选
                            $.each(data.tbday, function (idx, listObj) {
                                //审计通报
                                if (idx < 6 && $("#prvdId").val() == 10000) {
                                    Tbday.push(listObj);
                                }
                            });
                            $.each(data.pmday, function (idx, listObj) {
                                //排名汇总
                                if (idx < 6 && $("#prvdId").val() == 10000) {
                                    Pmday.push(listObj);
                                }
                            });
                            $.each(data.reportday, function (idx, listObj) {
                                //审计报告
                                if (idx < 6) {
                                    Reportday.push(listObj);
                                }
                            });
                            $.each(data.detailday, function (idx, listObj) {
                                //审计清单
                                if (idx < 6) {
                                    Detailday.push(listObj);
                                }
                            });
                            break;
                        case 'auditTB': //审计通报
                            $.each(data.tbday, function (idx, listObj) {
                                //审计通报
                                if (idx < 6 && $("#prvdId").val() == 10000) {
                                    Tbday.push(listObj);
                                }
                            });
                            break;
                        case 'auditPm': //排名汇总
                            $.each(data.pmday, function (idx, listObj) {
                                //排名汇总
                                if (idx < 6 && $("#prvdId").val() == 10000) {
                                    Pmday.push(listObj);
                                }
                            });
                            break;
                        case 'audReport': //审计报告
                            $.each(data.reportday, function (idx, listObj) {
                                //审计报告
                                if (idx < 6) {
                                    Reportday.push(listObj);
                                }
                            });
                            break;
                        case 'audDetail': //审计清单
                            $.each(data.detailday, function (idx, listObj) {
                                //审计清单
                                if (idx < 6) {
                                    Detailday.push(listObj);
                                }
                            });
                            break;
                    }
                }
                if ($("#subjectId").val() != 5 && $("#subjectId").val() != 6 && $("#subjectId").val() != 7 && $("#subjectId").val() != 8 && $("#subjectId").val() != 9 && $("#subjectId").val() != 11 && $("#subjectId").val() != 12&& $("#subjectId").val() != 13) {
                    $('#contentShowWrap3').highcharts({
                        title: {
                            text: null
                        },
                        xAxis: {
                            categories: [audTrms[5], audTrms[4], audTrms[3], audTrms[2], audTrms[1], audTrms[0]],
                            /*  tickPixelInterval: '10', */
                            title: {
                                text: null
                            }
                        },
                        tooltip: { /*  this.category */
                            /* backgroundColor: 'rgba(50,50,50,0.7)',
                            borderColor: 'rgba(50,50,50,0.7)',
                            style: {
                                color: '#fff'
                            }, */
                            /*   shared: true, */
                            formatter: function () {
                                /*  var  Xyear = parseInt((this.x).substring(0, 4)),
                                     Xmon = parseInt((this.x).substring(5)),
                                     Xaudtrm = Xyear + "" + Xmon,
                                     XtooltipAudtrm = monthSub(Xaudtrm, times);  */ // XtooltipAudtrm[times - 1]
                                var s = '<span>' + $("#chooseSpecial").val() + '</span><span style="color:' + this.point.series.color + '">' + this.series.name;

                                if (this.point.y != 0) {
                                    h = '<br/>生成时间：<br/>' + (this.x).substr(0, 4) + "年" + (this.x).substr(4, 6) + "月" + ((this.point.y) > 9 ? this.point.y : "0" + this.point.y) + '日</span><br/>';
                                } else {
                                    h = "<br/>未生成";
                                }
                                return s + h;

                            }
                        },
                        plotOptions: {
                            series: {
                                marker: {
                                    lineWidth: 2,
                                    lineColor: null // inherit from series
                                },
                            }
                        },
                        yAxis: {
                            tickInterval: 8, // 刻度值
                            title: {
                                text: null
                            },
                            labels: {
                                formatter: function () {
                                    return this.value / 1 + '日';
                                }
                            }
                        },
                        legend: {
                            // verticalAlign: 'top',
                            padding: 0,
                            margin: 0,
                            itemWidth: 92,
                            fontSize: '9px',
                            enabled: true
                        },
                        series: [{
                                name: '审计报告',
                                data: Reportday,
                                color: '#00D5D2',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#fileType").val() != "audReport" && $("#fileType").val() != "" ? false : true
                            },
                            {
                                name: '审计清单',
                                data: Detailday,
                                color: '#E055A6',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#fileType").val() != "audDetail" && $("#fileType").val() != "" ? false : true
                            },
                            {
                                name: '排名汇总',
                                data: Pmday,
                                color: '#67AC82',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#prvdId").val() != 10000 || $("#fileType").val() != "auditPm" && $("#fileType").val() != "" ? false : true
                            },
                            {
                                name: '审计通报',
                                data: Tbday,
                                color: '#F4D028',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#prvdId").val() != 10000 || $("#fileType").val() != "auditTB" && $("#fileType").val() != "" ? false : true
                            }
                        ],
                        credits: {
                            enabled: false
                        },
                        exporting: {
                            enabled: false
                        }
                    });
                } else if ($("#subjectId").val() == 9) {
                    $('#contentShowWrap3').highcharts({
                        title: {
                            text: null
                        },
                        xAxis: {
                            categories: [audTrms[5], audTrms[4], audTrms[3], audTrms[2], audTrms[1], audTrms[0]],
                            /*  tickPixelInterval: '10', */
                            title: {
                                text: null
                            }
                        },
                        tooltip: { /*  this.category */
                            /* backgroundColor: 'rgba(50,50,50,0.7)',
                            borderColor: 'rgba(50,50,50,0.7)',
                            style: {
                                color: '#fff'
                            }, */
                            /*   shared: true, */
                            formatter: function () {
                                /*  var  Xyear = parseInt((this.x).substring(0, 4)),
                                     Xmon = parseInt((this.x).substring(5)),
                                     Xaudtrm = Xyear + "" + Xmon,
                                     XtooltipAudtrm = monthSub(Xaudtrm, times);  */ // XtooltipAudtrm[times - 1]
                                var s = '<span>' + $("#chooseSpecial").val() + '</span><span style="color:' + this.point.series.color + '">' + this.series.name;

                                if (this.point.y != 0) {
                                    h = '<br/>生成时间：<br/>' + (this.x).substr(0, 4) + "年" + (this.x).substr(4, 6) + "月" + ((this.point.y) > 9 ? this.point.y : "0" + this.point.y) + '日</span><br/>';
                                } else {
                                    h = "<br/>未生成";
                                }
                                return s + h;

                            }
                        },
                        plotOptions: {
                            series: {
                                marker: {
                                    lineWidth: 2,
                                    lineColor: null // inherit from series
                                },
                            }
                        },
                        yAxis: {
                            tickInterval: 8, // 刻度值
                            title: {
                                text: null
                            },
                            labels: {
                                formatter: function () {
                                    return this.value / 1 + '日';
                                }
                            }
                        },
                        legend: {
                            // verticalAlign: 'top',
                            padding: 0,
                            margin: 0,
                            itemWidth: 92,
                            fontSize: '9px',
                            enabled: true
                        },
                        series: [{
                            name: '排名汇总',
                            data: Pmday,
                            color: '#67AC82',
                            lineWidth: 1,
                            marker: {
                                radius: 2
                            },
                            showInLegend: $("#prvdId").val() != 10000 || $("#fileType").val() != "auditPm" && $("#fileType").val() != "" ? false : true
                        }],
                        credits: {
                            enabled: false
                        },
                        exporting: {
                            enabled: false
                        }
                    });
                } else {
                    $('#contentShowWrap3').highcharts({
                        title: {
                            text: null
                        },
                        xAxis: {
                            categories: [audTrms[5], audTrms[4], audTrms[3], audTrms[2], audTrms[1], audTrms[0]],
                            /*  tickPixelInterval: '10', */
                            title: {
                                text: null
                            }
                        },
                        tooltip: { /*  this.category */
                            /* backgroundColor: 'rgba(50,50,50,0.7)',
                            borderColor: 'rgba(50,50,50,0.7)',
                            style: {
                                color: '#fff'
                            }, */
                            /*   shared: true, */
                            formatter: function () {
                                /*  var  Xyear = parseInt((this.x).substring(0, 4)),
                                     Xmon = parseInt((this.x).substring(5)),
                                     Xaudtrm = Xyear + "" + Xmon,
                                     XtooltipAudtrm = monthSub(Xaudtrm, times);  */ // XtooltipAudtrm[times - 1]
                                var s = '<span>' + $("#chooseSpecial").val() + '</span><span style="color:' + this.point.series.color + '">' + this.series.name;

                                if (this.point.y != 0) {
                                    h = '<br/>生成时间：<br/>' + (this.x).substr(0, 4) + "年" + (this.x).substr(4, 6) + "月" + ((this.point.y) > 9 ? this.point.y : "0" + this.point.y) + '日</span><br/>';
                                } else {
                                    h = "<br/>未生成";
                                }
                                return s + h;

                            }
                        },
                        plotOptions: {
                            series: {
                                marker: {
                                    lineWidth: 2,
                                    lineColor: null // inherit from series
                                },
                            }
                        },
                        yAxis: {
                            tickInterval: 8, // 刻度值
                            title: {
                                text: null
                            },
                            labels: {
                                formatter: function () {
                                    return this.value / 1 + '日';
                                }
                            }
                        },
                        legend: {
                            //verticalAlign: 'top',
                            padding: 0,
                            margin: 0,
                            itemWidth: 92,
                            fontSize: '9px',
                            enabled: true
                        },
                        series: [{
                                name: '审计报告',
                                data: Reportday,
                                color: '#00D5D2',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#fileType").val() != "audReport" && $("#fileType").val() != "" ? false : true
                            },
                            {
                                name: '审计清单',
                                data: Detailday,
                                color: '#E055A6',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#fileType").val() != "audDetail" && $("#fileType").val() != "" ? false : true
                            },
                            {
                                name: '排名汇总',
                                data: Pmday,
                                color: '#67AC82',
                                lineWidth: 1,
                                marker: {
                                    radius: 2
                                },
                                showInLegend: $("#prvdId").val() != 10000 || $("#fileType").val() != "auditPm" && $("#fileType").val() != "" ? false : true
                            }
                        ],
                        credits: {
                            enabled: false
                        },
                        exporting: {
                            enabled: false
                        }
                    });
                }

                $("#contentShowWrap3").getNiceScroll(0).show();
                $("#contentShowWrap3").getNiceScroll(0).resize();
                $("#contentShowWrap3").getNiceScroll(0).doScrollLeft(0);
            }
        });

    };
    ///////////////////////////////////分省生成时间分布end////////////////////////////////////////////

    ///////////////////////////////////table  start////////////////////////////////////////////
    var rankingAllTable = function () {
        var h = parseInt($('#fenxiFourNav1FiveNav1Con').height()),
            postData = {
                prvdId: $('#prvdId').val(),
                audTrm: $('#audTrm').val(),
                subjectId: $('#subjectId').val(),
                fileType: $('#fileType').val(),
            },
            fileName;
        $('#rankingAllTable').bootstrapTable('destroy');
        $('#rankingAllTable').bootstrapTable('resetView');

        function tagColumn(value, row, index) {
            var concernId = "";
            switch (row.name) {
                case '积分异常转移':
                    concernId = '5002';
                    break;
                case '异常高额退费':
                    concernId = '5000';
                    break;
                case '话费异常赠送':
                    concernId = '5003';
                    break;
                case '积分异常赠送':
                    concernId = '5001';
                    break;
            }
            var e = '<a href="#" class="btn Tabledownload" flag="down" concern="' + concernId + '" filetypeCn="' + row.file_type + '" filetype="' + row.file_typeTMP + '" data="' + row.download_url + '">下载</a>';
            if ($("#roleId").val() != "1") {
                h = "";
            } else {
                /*  if (row.file_type != "排名汇总") {
                     h = '<a href="#" style="margin-left:2px" filetype="' + row.file_type + '" class="btn generation" concern="' + concernId + '">手动生成</a>';
                 } else {
                     h = "";
                 } */
                h = '<a href="#" style="margin-left:2px" filetype="' + row.file_type + '" class="btn generation" concern="' + concernId + '">手动生成</a>';
            }
            return e + h;
        }
        $.ajax({
            url: "/cmca/bgxz/downRecordsTable",
            dataType: 'json',
            async: false,
            // type: 'POST',
            data: postData,
            cache: false,
            /*  showColumns: true, */
            success: function (data) {
                if ($("#prvdId").val() == 10000) {
                    /* 表格数据 */
                    $('#rankingAllTable').bootstrapTable({
                        datatype: "local",
                        data: data, //加载数据
                        pagination: false, //是否显示分页
                        cache: false,
                        height: h,
                        align: 'center',
                        columns: [{
                                title: '序号',
                                width: '4%',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            {
                                title: '专题名称',
                                field: 'name',
                            },
                            {
                                title: '文件类型',
                                field: 'file_type',
                                //formatter: filetypeName
                            },
                            {
                                title: '审计月',
                                field: 'audit_monthly',
                            },
                            {
                                title: '操作人',
                                field: 'create_person',
                            },
                            {
                                title: '审计期间',
                                field: 'sjqj',
                                width: "200px"
                            },
                            {
                                title: '文件最新生成时间',
                                field: 'create_time',
                                width: "140px"
                            },
                            {
                                title: '生成类型',
                                field: 'create_type'
                            },
                            {
                                title: '最新下载时间',
                                field: 'last_down_time',
                                width: "140px"
                            },
                            {
                                title: '下载次数',
                                field: 'down_count',
                                width: "66px"
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'download_url',
                                formatter: tagColumn,
                                width: "120px"
                            },
                            {
                                field: 'file_typeTMP',
                                visible: false
                            }
                        ]
                    });
                } else {
                    var dataTable = [];
                    $.each(data, function (n, value) {
                        if (value.file_type == "审计清单") {
                            dataTable.push(value);
                        } else if (value.file_type == "审计报告") {
                            dataTable.push(value);
                        }
                    });
                    $('#rankingAllTable').bootstrapTable({
                        datatype: "local",
                        data: dataTable, //加载数据
                        pagination: false, //是否显示分页
                        cache: false,
                        height: h,
                        align: 'center',
                        columns: [{
                                title: '序号',
                                width: '4%',
                                formatter: function (value, row, index) {
                                    return index + 1;
                                }
                            },
                            {
                                title: '专题名称',
                                field: 'name',
                            },
                            {
                                title: '文件类型',
                                field: 'file_type',
                                //formatter: filetypeName
                            },
                            {
                                title: '审计月',
                                field: 'audit_monthly',
                            },
                            {
                                title: '操作人',
                                field: 'create_person',
                            },
                            {
                                title: '审计期间',
                                field: 'sjqj',
                                width: "200px"
                            },
                            {
                                title: '文件最新生成时间',
                                field: 'create_time',
                                width: "140px"
                            },
                            {
                                title: '生成类型',
                                field: 'create_type'
                            },
                            {
                                title: '最新下载时间',
                                field: 'last_down_time',
                                width: "140px"
                            },
                            {
                                title: '下载次数',
                                field: 'down_count',
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'download_url',
                                formatter: tagColumn,
                                width: "120px"
                            },
                            {
                                field: 'file_typeTMP',
                                visible: false
                            }
                        ]
                    });
                }

            }

        });
        $('#rankingAllTable').parent('.fixed-table-body').attr('id', 'rankingAllTableWrap');
        scroll('#rankingAllTableWrap', '#rankingAllTable');

    };

    ///////////////////////////////////table  end////////////////////////////////////////////
    return {
        "getUserName": getUserName,
        "loadSjbgCharts1": loadSjbgCharts1,
        "loadSjbgCharts2": loadSjbgCharts2,
        "loadSjbgCharts3": loadSjbgCharts3,
        "rankingAllTable": rankingAllTable,
        "load_column_list_prvdIdName": load_column_list_prvdIdName,
        "load_column_list_specialName": load_column_list_specialName,
        "load_column_list_audTrm": load_column_list_audTrm
    };
})();
//审计清单--下载
function sjqdDown() {
    postData = {
        audTrm: $('#audTrm').val(),
        prvdId: $('#prvdId').val(),
        subjectId: $('#subjectId').val(),
        fileType: $('#fileType').attr("data")
    };
    $.ajax({
        url: '/cmca/bgxz/selCsvBySubjectId',
        dataType: 'json',
        type: 'POST',
        data: postData,
        cache: false,
        success: function (data) {
            var prvdId_val = $('#prvdId').val(),
                audTrm_val = $('#audTrm').val(),
                subjectId_val = $('#subjectId').val(),
                focusCd_val = $('#focusCd').val(),
                userName_val = encodeURI($('#userName').val()),
                fileType_val = $('#fileType').attr("data");
            if (data.filenames.length != 0) {
                $.each(data.filepaths, function (n, value) {
                    window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm_val + '&subjectId=' + subjectId_val + '&prvdId=' + prvdId_val + '&focusCd=' + focusCd_val + '&fileType=' + fileType_val + /* '&userName=' + userName + */ '&download_url=' + value);
                });
            } else {
                alert("没有文件生成");
            }
            sjbg.rankingAllTable();
        }
    });
}

//排名汇总--手动生成
function pmhz() {
    postData = {
        focusNum: $('#focusNum').val(),
        focusCd: $('#focusCd').val(),
        audTrm: $('#audTrm').val(),
        isAuto: $('#isAuto').val(),
        concern: $('#concern').attr("concern"),
        /*  userName: $('#userName').val(), */
        subjectId: $('#subjectId').val(),
        fileType: $('#fileType').attr("data")
    };
    $.ajax({
        url: '/cmca/pmhz/genByHand',
        dataType: 'json',
        type: 'POST',
        data: postData,
        cache: false,
        beforeSend: function () {
            $('.wrapper1').css("display", "block");
        },
        success: function (data) {
            $('.wrapper1').css("display", "none");
            if (data.status == "DONE") {
                alert("手动生成成功");
                sjbg.rankingAllTable();
            } else if (data.status == "FAILED") {
                alert("生成失败");
            } else if (data.status == "HAVEDONE") {
                alert("已经生成过");
            }
            sjbg.rankingAllTable();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $('.wrapper1').css("display", "none");
            alert("生成失败");
        }
    });
}

//审计通报--手动生成
function sjtb() {
    postData = {
        focusCd: $('#focusCd').val(),
        audTrm: $('#audTrm').val()
    };
    $.ajax({
        url: '/cmca/common/sjtb',
        dataType: 'json',
        type: 'POST',
        data: postData,
        cache: false,
        beforeSend: function () {
            $('.wrapper1').css("display", "block");
        },
        success: function (data) {
            $('.wrapper1').css("display", "none");
            if (data.code == "200") {
                alert("手动生成成功");
                sjbg.rankingAllTable();
            } else if (data.code == "500") {
                alert("手动生成失败");
            }
            sjbg.rankingAllTable();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $('.wrapper1').css("display", "none");
            alert("生成失败");
        }
    });
}

//审计报告 审计清单--手动生成

function sjqd() {
    postData = {
        focus: $('#focusCd').val(),
        audTrm: $('#audTrm').val(),
        prvdId: $('#prvdId').val()
    };
    $.ajax({
        url: '/cmca/common/runJob',
        dataType: 'json',
        data: postData,
        type: 'POST',
        cache: false,
        beforeSend: function () {
            $('.wrapper1').css("display", "block");
        },
        success: function (data) {
            $('.wrapper1').css("display", "none");
            if (data.code == "200") {
                alert("手动生成成功");
                sjbg.rankingAllTable();
            } else if (data.code == "300") {
                alert("本次job未发现文件生成请求，已结束！");
            } else if (data.code == "500") {
                alert("手动生成失败");
            }
            sjbg.rankingAllTable();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $('.wrapper1').css("display", "none");
            alert("生成失败");
        }
    });
}
//获取上传次数
function showUploadNum() {
    $.ajax({
        url: '/cmca/bgxz/countReportUpload',
        dataType: 'html',
        type: 'POST',
        cache: false,
        success: function (data) {
            $("#item1").html(data);
        }
    });
}
//获取审批次数
function showApporNum() {
    postData = {
        status: $('#concern').attr("data")
    };
    $.ajax({
        url: '/cmca/bgxz/countReviewFile',
        dataType: 'json',
        data: postData,
        type: 'POST',
        cache: false,
        success: function (data) {
            $("#item2").html(data.dshCount);
            $("#item3").html(data.yshCount);
        }
    });
}
//获取已上传文件列表
function showAleadyUploadList() {
    $(".upload_title").siblings(".yscLi,.yshLi,.waitLi,.upfileLi").remove();
    $.ajax({
        url: '/cmca/bgxz/selReportUpload?status=' + $("#concern").attr("data"),
        dataType: 'json',
        type: 'POST',
        cache: false,
        success: function (data) {
            if (data.length > 0) {
                $('.upload_title .upload_cell:first').removeClass("cellFirst").removeClass("filecellFirst").addClass("filecellUp");
            } else {
                $('.upload_title .upload_cell:first').addClass("cellFirst").removeClass("filecellFirst").removeClass("filecellUp");
            }
            var fileInputhtml, fileName, filetypeName, filetypeHtml, visibleProvinceHtml, day, statusCharacter, fileDlare, approverTypes, approvalOpinion, operation, UploadHtml;
            $.each(data, function (n, value) {
                if (value.file_type == "audDetail") {
                    filetypeName = "审计清单";
                } else {
                    filetypeName = "审计报告";
                }
                if (value.review_opinion != undefined) {
                    approvalOpinionName = value.review_opinion;
                } else {
                    approvalOpinionName = "";
                }
                if (value.file_comment != undefined) {
                    approverTypesName = value.file_comment;
                } else {
                    approverTypesName = "";
                }
                switch (value.status) {
                    case 'no':
                        statusCharacter = "已驳回";
                        break;
                    case 'yes':
                        statusCharacter = "审批通过";
                        break;
                    case 'wait':
                        statusCharacter = "已提交";
                        break;
                }
                fileInputhtml = "<span class='fileInput' data='" + value.status + "'><input type='checkbox' checked></span>";
                fileName = "<span class='upload_cell filecellFirst'>" +
                    value.file_path + "</span>";
                filetypeHtml = "<span class='upload_cell'>" +
                    "<select class='form-control' disabled >" +
                    "<option value='" + value.file_type + "'>" + filetypeName + "</option>" +
                    "</select></span >";
                visibleProvinceHtml = "<span class='upload_cell'>" +
                    " <select class='form-control' disabled>" +
                    "<option value='" + value.prvd_id + "'>" + value.CMCC_prov_prvd_nm + "</option>" +
                    "</select></span>";
                day = "<span class='upload_cell'>" + value.fileupload_date + "</span>";
                fileDlare = "<span class='upload_cell'>" + "<textarea maxlength='50' readonly='readonly' class='form-control filespec'style='resize:none;' title='" + approverTypesName + "'>" + approverTypesName + "</textarea></span>";
                approverTypes = "<span class='upload_cell approvertypes'>" + "<select class='form-control' disabled>" +
                    "<option value='" + value.status + "'>" + statusCharacter + "</option>" +
                    "</select></span>";
                approvalOpinion = "<span class='upload_cell approvalOpinion'>" + "<textarea readonly='readonly' maxlength='50' class='form-control 'style='resize:none;' title='" + approvalOpinionName + "'>" + approvalOpinionName + "</textarea></span > ";
                operation = "<span class='upload_cell upload_cellLast'>" + ((value.status == "no") ? "<span class='glyphicon glyphicon-remove-sign'></span>" : "") + "</span>";
                UploadHtml = "<li id='" + value.id + "' class='yscLi'>" + fileInputhtml + fileName + filetypeHtml + visibleProvinceHtml + day + fileDlare + approverTypes + approvalOpinion + operation + "</li>";
                $("#uploadUl").append(UploadHtml);
                $(".fileInput").css("display", "inline-block");
                $('.upload_cell').css({
                    "paddingLeft": "8px",
                    "paddingRight": "8px"
                });
            });
        }
    });
    scroll('#uploadUlWap', '#uploadUl');
}
//待审批文件列表
function showapproveFileList() {
    $.ajax({
        url: '/cmca/bgxz/selReportUpload?status=dsh',
        dataType: 'json',
        type: 'POST',
        cache: false,
        success: function (data) {
            $("#item2").html(data.length);
            if (data.length > 0) {
                $('.upload_title .upload_cell').css("padding", "5px 8px");
                $('.upload_title .upload_cell:first').removeClass("cellFirst").removeClass("filecellFirst").addClass("filecellUp");
            } else {
                $('.upload_title .upload_cell:first').addClass("cellFirst").removeClass("filecellFirst").removeClass("filecellUp");
            }
            var fileInputhtml, fileName, filetypeName, filetypeHtml, visibleProvinceHtml, oper_person, day, statusCharacter, fileDlare, approverTypes, approvalOpinion, operation, UploadHtml;
            $.each(data, function (n, value) {
                if (value.file_type == "audDetail") {
                    filetypeName = "审计清单";
                } else {
                    filetypeName = "审计报告";
                }
                if (value.review_opinion != undefined) {
                    approvalOpinionName = value.review_opinion;
                } else {
                    approvalOpinionName = "";
                }
                if (value.file_comment != undefined) {
                    approverTypesName = value.file_comment;
                } else {
                    approverTypesName = "";
                }
                switch (value.status) {
                    case 'no':
                        statusCharacter = "已驳回";
                        break;
                    case 'yes':
                        statusCharacter = "审批通过";
                        break;
                    case 'wait':
                        statusCharacter = "已提交";
                        break;
                }
                fileInputhtml = "<span class='fileInput'><input type='checkbox' checked></span>";
                fileName = "<span class='upload_cell filecellWait'>" +
                    value.file_path + "</span>";
                filetypeHtml = "<span class='upload_cell'>" +
                    "<select class='form-control' disabled >" +
                    "<option value='" + value.file_type + "'>" + filetypeName + "</option>" +
                    "</select></span >";
                visibleProvinceHtml = "<span class='upload_cell'>" +
                    " <select class='form-control' disabled>" +
                    "<option value='" + value.prvd_id + "'>" + value.CMCC_prov_prvd_nm + "</option>" +
                    "</select></span>";
                oper_person = "<span class='upload_cell'>" + value.oper_person + "</span>";
                day = "<span class='upload_cell'>" + value.fileupload_date + "</span>";
                fileDlare = "<span class='upload_cell'>" + "<textarea maxlength='50' readonly='readonly' class='form-control filespec'style='resize:none;' title='" + approverTypesName + "'>" + approverTypesName + "</textarea></span>";
                approverTypes = "<span class='upload_cell approvertypes'>" + "<select class='form-control' disabled>" +
                    "<option value='" + value.status + "'>" + statusCharacter + "</option>" +
                    "</select></span>";
                approvalOpinion = "<span class='upload_cell approvalOpinion'><textarea maxlength='50' readonly='readonly' class='form-control filespec'style='resize:none;'></textarea></span > ";
                operation = "<span class='upload_cell upload_cellLast operation'>" + ((value.status == "no") ? "<span class='glyphicon glyphicon-remove-sign'></span>" : "") + "</span>";
                UploadHtml = "<li id='" + value.id + "' class='waitLi'>" + fileInputhtml + fileName + filetypeHtml + visibleProvinceHtml + oper_person + day + fileDlare + approverTypes + approvalOpinion + operation + "</li>";
                $("#uploadUl").append(UploadHtml);
                $(".fileInput").css("display", "inline-block");
                $('.upload_cell').css({
                    "paddingLeft": "2px",
                    "paddingRight": "2px"
                });
            });
        }
    });
    scroll('#uploadUlWap', '#uploadUl');
}
//已审批列表
function showPassList() {
    showApporNum();
    $.ajax({
        url: '/cmca/bgxz/selReportUpload?status=ysh',
        dataType: 'json',
        type: 'POST',
        cache: false,
        success: function (data) {
            $("#item3").html(data.length);
            if (data.length > 0) {
                $('.upload_title .upload_cell').css("padding", "5px 8px");
                $('.upload_title .upload_cell:first').removeClass("cellFirst").removeClass("filecellFirst").addClass("filecellUp");
            } else {
                $('.upload_title .upload_cell:first').addClass("cellFirst").removeClass("filecellFirst").removeClass("filecellUp");
            }
            var fileInputhtml, fileName, filetypeName, filetypeHtml, visibleProvinceHtml, oper_person, day, statusCharacter, fileDlare, approverTypes, approvalOpinion, operation, UploadHtml;
            $.each(data, function (n, value) {
                if (value.file_type == "audDetail") {
                    filetypeName = "审计清单";
                } else {
                    filetypeName = "审计报告";
                }
                if (value.review_opinion != undefined) {
                    approvalOpinionName = value.review_opinion;
                } else {
                    approvalOpinionName = "";
                }
                if (value.file_comment != undefined) {
                    approverTypesName = value.file_comment;
                } else {
                    approverTypesName = "";
                }
                switch (value.status) {
                    case 'no':
                        statusCharacter = "已驳回";
                        break;
                    case 'yes':
                        statusCharacter = "审批通过";
                        break;
                    case 'wait':
                        statusCharacter = "已提交";
                        break;
                }
                fileInputhtml = "<span class='fileInput'><input type='checkbox' checked></span>";
                fileName = "<span class='upload_cell filecellWait'>" +
                    value.file_path + "</span>";
                filetypeHtml = "<span class='upload_cell'>" +
                    "<select class='form-control' disabled >" +
                    "<option value='" + value.file_type + "'>" + filetypeName + "</option>" +
                    "</select></span >";
                visibleProvinceHtml = "<span class='upload_cell'>" +
                    " <select class='form-control' disabled>" +
                    "<option value='" + value.prvd_id + "'>" + value.CMCC_prov_prvd_nm + "</option>" +
                    "</select></span>";
                oper_person = "<span class='upload_cell'>" + value.oper_person + "</span>";
                day = "<span class='upload_cell'>" + value.fileupload_date + "</span>";
                fileDlare = "<span class='upload_cell'>" + "<textarea maxlength='50' readonly='readonly' class='form-control filespec'style='resize:none;' title='" + approverTypesName + "'>" + approverTypesName + "</textarea></span>";
                approverTypes = "<span class='upload_cell approvertypes'>" + "<select class='form-control' disabled>" +
                    "<option value='" + value.status + "'>" + statusCharacter + "</option>" +
                    "</select></span>";
                approvalOpinion = "<span class='upload_cell approvalOpinion'>" + "<textarea readonly='readonly' maxlength='50' class='form-control 'style='resize:none;' title='" + approvalOpinionName + "'>" + approvalOpinionName + "</textarea></span > ";
                operation = "<span class='upload_cell upload_cellLast'>" + ((value.status == "no") ? "<span class='glyphicon glyphicon-remove-sign'></span>" : "") + "</span>";
                UploadHtml = "<li id='" + value.id + "' class='yshLi'>" + fileInputhtml + fileName + filetypeHtml + visibleProvinceHtml + oper_person + day + fileDlare + approverTypes + approvalOpinion + operation + "</li>";
                $("#uploadUl").append(UploadHtml);
                $(".fileInput").css("display", "inline-block");
                $('.upload_cell').css({
                    "paddingLeft": "2px",
                    "paddingRight": "2px"
                });
            });
        }
    });
    scroll('#uploadUlWap', '#uploadUl');
}
var Ids = [],
    downStatus = [],
    approvalOpinionNames = "",
    statusArry = [];
/* 已上传的 下载  按钮*/
$("#downloadBtn").on("click", function () {
    Ids = [];
    downStatus = [];
    statusArry = [];
    var flag = 0;
    $("#uploadUl .yscLi :checkbox").each(function () {
        if ($(this).is(':checked')) {
            flag++;
            if (Ids.indexOf($(this).parent().parent().attr("id")) == -1) {
                Ids.push($(this).parent().parent().attr("id"));
                downStatus.push($(this).siblings(".approvertypes").children().find('option').val());
                statusArry.push($(this).parent().attr("data"));
            }
        }
    });
    if (flag != 0) {
        $('#concern').attr("data", "ysc");
        // 插入一经事件码-下载
        dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
        // 日志记录
        get_userBehavior_log('业务管理', '报告下载', '已上传文件下载', '下载');
        downloadId();
        flag = 0;
    } else {
        alert("请选择需要下载的文件");
    }
});
$("#downBtn").on("click", function () {
    Ids = [];
    downStatus = [];
    statusArry = [];
    if ($("#uploadUl .yshLi").length > 0) {
        var flag = 0;
        $("#uploadUl .yshLi :checkbox").each(function () {
            if ($(this).is(':checked')) {
                flag++;
                if (Ids.indexOf($(this).parent().parent().attr("id")) == -1) {
                    Ids.push($(this).parent().parent().attr("id"));
                    downStatus.push($(this).siblings(".approvertypes").children().find('option').val());
                    statusArry.push($(this).parent().attr("data"));
                }
            }
        });
        if (flag != 0) {
            // 插入一经事件码-下载
            dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
            // 日志记录
            get_userBehavior_log('业务管理', '报告下载', '已审批文件下载', '下载');
            downloadId();
            flag = 0;
        } else {
            alert("请选择需要下载的文件");
        }
    } else {
        var flag = 0;
        $("#uploadUl .waitLi :checkbox").each(function () {
            if ($(this).is(':checked')) {
                flag++;
                if (Ids.indexOf($(this).parent().parent().attr("id")) == -1) {
                    Ids.push($(this).parent().parent().attr("id"));
                    downStatus.push($(this).siblings(".approvertypes").children().find('option').val());
                    statusArry.push($(this).parent().attr("data"));
                }
            }
        });
        if (flag != 0) {
            // 插入一经事件码-下载
            dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
            // 日志记录
            get_userBehavior_log('业务管理', '报告下载', '待审批文件下载', '下载');
            downloadId();
            flag = 0;
        } else {
            alert("请选择需要下载的文件");
        }
    }
});

//已上传 的下载id
function downloadIdS() {
    window.open('/cmca/bgxz/downFile?ids=' + Ids + '&status=' + $("#approSubmit").attr("data"));
}

function downloadId() {
    window.open('/cmca/bgxz/downFile?ids=' + Ids);
}


//审批提交
function submitIds() {
    postData = {
        "status": $("#approSubmit").attr("data"),
        "reviewOpinion": approvalOpinionNames
    };
    $.ajax({
        url: '/cmca/bgxz/reviewReportUpload?ids=' + Ids,
        dataType: 'html',
        data: postData,
        type: 'POST',
        cache: false,
        success: function (data) {
            if (data == "success") {
                alert("审批成功");
                $("#uploadUl .waitLi :checkbox").each(function () {
                    if ($(this).is(':checked')) {
                        $(this).siblings(".operation").html("<span class='glyphicon glyphicon-ok-sign'></span>");
                    }
                });
                $('.approveWarp').css("display", "none");
                $("#uploadUl .waitLi").remove();
                showApporNum();
                showapproveFileList();
            } else {
                alert("审批失败");
            }
        }
    });
}
/* 点击提交按钮 */
$("#approSubmit").on("click", function () {
    // 插入一经事件码-修改
    dcs.addEventCode('MAS_HP_CMCA_child_edit_data_09');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '审批文件提交', '修改');
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '已审批列表', '查询');
    Ids = [], downStatus = [], approvalOpinionNames = "";
    if ($("#uploadUl .waitLi :checkbox").length > 0 && $("#approSubmit").attr("data") != undefined) {
        $("#uploadUl .waitLi :checkbox").each(function () {
            if ($(this).is(':checked')) {
                $(this).parent().siblings(".approvalOpinion").children("textarea").attr("readonly", "readonly");
                if (Ids.indexOf($(this).parent().parent().attr("id")) == -1) {
                    Ids.push($(this).parent().parent().attr("id"));
                    downStatus.push($(this).parent().siblings(".approvertypes").children().find('option').val());
                    approvalOpinionNames = $(this).parent().siblings(".approvalOpinion").children("textarea").val();
                }
            }
        });
        submitIds();
    } else {
        alert("请审批");
    }
});
/* 文件说明获得焦点 */
$("#uploadUl").on("keyup", ".approvalOpinion", function () {
    if ($(this).val().length > 50) {
        alert("最多只能输入50字符");
        var value = value.substring(0, 50);
        $(this).attr("value", value);
        $(this).css("disabled", "disabled");
    } else {
        $(this).attr("title", $(this).val());
    }
});
$(".approveIdea").on("keyup", " textarea", function () {
    if ($(this).val().length > 50) {
        alert("最多只能输入50字符");
        var value = value.substring(0, 50);
        $(this).attr("value", value);
        $(this).css("disabled", "disabled");
    } else {
        $(this).attr("title", $(this).val());
        var approv = $(this).val();
        $("#uploadUl .waitLi :checkbox").each(function () {
            if ($(this).is(':checked')) {
                $(this).parent().siblings(".approvalOpinion").children("textarea").val(approv);
                $(this).parent().siblings(".approvalOpinion").children("textarea").attr("title", approv);
            }
        });
    }
});
/*  待审核和已审核 和已上传列表中的删除*/
$("#uploadUl").on("click", ".yshLi span.glyphicon-remove-sign,.yscLi span.glyphicon-remove-sign", function () {
    // 插入一经事件码-删除
    dcs.addEventCode('MAS_HP_CMCA_child_delete_data_08');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '待审批已审批已上传列表中文件删除', '删除');
    // 插入一经事件码-查询
    dcs.addEventCode('MAS_HP_CMCA_child_query_02');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '待审批已审批已上传列表中文件删除', '查询');
    Ids = [];
    Ids.push($(this).parent().parent().attr("id"));
    liName = $(this).parent().parent().attr("class");
    $.ajax({
        url: '/cmca/bgxz/delUploadFile?ids=' + Ids,
        dataType: 'html',
        cache: false,
        success: function (data) {
            if (data == "success") {
                alert("删除成功");
                $("#uploadUl .waitLi,#uploadUl .yshLi,#uploadUl .yscLi").remove();
                if (liName.indexOf("yshLi") > -1) {
                    showPassList();
                } else if (liName.indexOf("yscLi") > -1) {
                    showAleadyUploadList();
                }
            } else {
                alert("删除失败");
            }
            showUploadNum();
        }
    });
});
/* 审计清单 下载 */
$(".wrapper2").on("click", ".btn", function () {
    // 插入一经事件码-下载
    dcs.addEventCode('MAS_HP_CMCA_child_down_file_06');
    // 日志记录
    get_userBehavior_log('业务管理', '报告下载', '审计清单下载', '导出');
    var prvdId = $('#prvdId').val(),
        audTrm = $('#audTrm').val(),
        subjectId = $('#subjectId').val(),
        focusCd = $('#focusCd').val(),
        userName = encodeURI($('#userName').val()),
        fileType = $('#fileType').attr("data"),
        downUrl = $(this).attr("data");
    window.open('/cmca/pmhz/downPMHZ?audTrm=' + audTrm + '&subjectId=' + subjectId + '&prvdId=' + prvdId + '&focusCd=' + focusCd + '&fileType=' + fileType + /* '&userName=' + userName + */ '&download_url=' + downUrl);
    sjbg.rankingAllTable();
    $(".wrapper2").css("display", "none");
});

/* 判断是否登录 */
function loginFlag() {
    $.ajax({
        url: '/cmca/bgxz/checkLogin',
        dataType: 'json',
        cache: false,
        success: function (data) {
            if (data == "1") {
                //登录中
            } else {
                //登录失效
                alert("请重新登录");
                window.open('/cmca/home/index');
            }
        }
    });
}