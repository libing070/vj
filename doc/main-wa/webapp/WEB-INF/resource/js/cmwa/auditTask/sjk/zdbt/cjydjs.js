$(function () {
    //step 1：个性化本页面的特殊风格
    initStyle();
    //step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
    initEvent();
    //step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
    initDefaultParams();
    //step 4：触发页面默认加载函数
    initDefaultData();
});

function initStyle() {
    scroll("#conShowWrap1", "#conShow1");
}

function initEvent() {

    showToggleFun("#contentTipBtn", "#hintText", "#noteText");
    showToggleFun("#mapTipBtn", "#noteText", "#hintText");
    clickBdHideFun("#hintText");
    clickBdHideFun("#noteText");

    // 显示表格按钮
    $("#showTbBtn").on("click", function () {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        $("#chartArea").hide();
        $("#tbArea").show();
        loadTable();
    });

    // 关闭表格按钮
    $("#tbOffBtn").on("click", function () {
        $("#tbArea").hide();
        $("#chartArea").show();
        $("#conShowWrap1").getNiceScroll(0).show();
        $("#conShowWrap1").getNiceScroll(0).resize();
        /*drowColumnChart();
        drowLineChart();*/
    });

    // 显示图形
    $("#showFullBtn").on("click", function () {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        $("#fullChartArea").show();
        $("#conShow3").css("minWidth","100%");
        columnChart("#conShow3",columnChartData[0],columnChartData[1]);
        lineChart("#conShow4",lineChartData[0],lineChartData[1]);
    });

    $("#fullOffBtn").on("click", function () {
        $("#fullChartArea").hide();
    });

   /* $("#allDataBtn").on("click", function () {
        $("#conShow1").css("minWidth","180%");
        getChinaMap();
        columnChart("#conShow1");
        lineChart("#conShow2");
    });
    $("#partDataBtn").on("click", function () {
        $("#conShow1").css("minWidth","100%");
        getChinaMap();
        chart1s("#conShow1");
        chart2s("#conShow2");
    });*/
    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-box .tab-info .tab-sub-nav ul li a").on("click",function(){
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

    	$("#fullOffBtn").click();
    	$("#tbOffBtn").click();
    });
    
    $("#table_export").on("click",function(){//tab页--导出
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	var totalNum = $("#tableId").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
	    var postData = getSumQueryParam();
		window.location.href = $.fn.cmwaurl() + "/cjydjssjk/exportTable"+"?" + $.param(postData);
	});
}


function initDefaultParams() {
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    
    var urlParams = window.location.search;
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    $('#auditId').val(auditId);
    $('#provinceCode').val(provinceCode);
    $('#beforeAcctMonth').val(beforeAcctMonth);
    $('#endAcctMonth').val(endAcctMonth);
    $('#currSumBeginDate').val(beforeAcctMonth);
    $('#currSumEndDate').val(endAcctMonth);
}

// 为元素设置滚动条，封装方法，方便调用
function scroll(wrap, item) {
    $(wrap).niceScroll(item, {
        'cursorcolor': '#BCBCBC',
        'cursorborderradius': '0',
        'background': '#F1F1F1',
        'cursorborder': 'none',
        'autohidemode': false
    });
}

function initDefaultData() {
    // 页面初始化加载
    $("#conShow1").css("minWidth","180%");
    drowColumnChart();
    drowLineChart();
    drowChinaMap();
}
//汇总数据查询条件
function getSumQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currSumBeginDate = $('#currSumBeginDate').val();
	postData.currSumEndDate = $('#currSumEndDate').val();
	postData.czyCitySingle = $('#czyCitySingle').val();
	postData.cityName = $('#cityName').val();
	return postData;
}

var columnChartData = [];
var lineChartData=[];

function drowColumnChart(){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + "/cjydjssjk/load_column_chart",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xData = [];
			var yData = [];
			var allProv = $.fn.provList();
			for(var i=0;i<backdata.length;i++){
				xData.push(backdata[i].short_name);
				yData.push(backdata[i].yc_imei_num);
			}
			for(var i=0;i<allProv.length;i++){
				if(!xData.contains(allProv[i].provName)&&allProv[i].provId!="10000"){
					xData.push(allProv[i].provName);
					yData.push(0);
				}
			}
			columnChartData.length=0;
			columnChartData.push(xData);
			columnChartData.push(yData);
			columnChart("#conShow1",xData,yData);
		}
	});
}
function drowLineChart(){
	var postData = getSumQueryParam();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$.ajax({
		url :$.fn.cmwaurl() + "/cjydjssjk/load_line_chart",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xData = [];
			var yData = [];
			var monArray = [];
			var index = 0;
			var year = Number(currSumBeginDate.substr(0,4));
			var mouth = Number(currSumBeginDate.substr(4,6));
			var endyear = Number(currSumEndDate.substr(0,4));
			var endmouth = Number(currSumEndDate.substr(4,6));
			var dateLength = (endyear-year)*12+endmouth - mouth;
			xData.push(year+""+currSumBeginDate.substr(4,6));
			for(var i = 0; i<dateLength;i++){
				mouth +=1;
				if(mouth>12){
					year = year + 1;
					mouth = mouth -12;
					if(mouth >=10){
						xData.push(year+""+mouth);
					}else{
						xData.push(year+"0"+mouth);
					}
				}else{
					if(mouth >=10){
						xData.push(year+""+mouth);
					}else{
						xData.push(year+"0"+mouth);
					}
				}
			}
			
			for(var i=0;i<backdata.length;i++){
				monArray.push(backdata[i].aud_trm);
			}
			for(var i=0;i<xData.length;i++){
				if(!monArray.contains(xData[i])){
					yData.push(0);
				}else{
					yData.push(backdata[index].yc_imei_num);
					index += 1;
				}
			}
			lineChartData=[];
			lineChartData.push(xData);
			lineChartData.push(yData);
			lineChart("#conShow2",xData,yData);
		}
	});
}

//地图
function drowChinaMap(){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + "/cjydjssjk/load_map_chart",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			getChinaMap(backdata);
		}
	});
}

// 显示/隐藏触发函数
function showToggleFun(bangBtn, showBox, hideBox) {
    $(bangBtn).on("click", function (even) {
        even.stopPropagation();
        if ($(showBox).is(':visible')) {
            $(showBox).hide();
        } else {
            $(showBox).show();
            $(hideBox).hide();
        }
    });
}

// 点击body隐藏Fun
function clickBdHideFun(hideWindow) {
    $("body").click(function () {
        $(hideWindow).hide();
    })
}


// 右侧柱图形容器宽度/字体随着窗口大小变化变化
function chartWith() {
    var pointW,
        xfontSize,
        xNumFontSize,
        pubuPointW,
        windowW = parseInt($(window).width());
    if (windowW <= 1024) {
        pubuPointW = 30;
        pointW = 14;
        xfontSize = '9px';
        xNumFontSize = '10px';
    } else if (windowW >= 1366) {
        pubuPointW = 40;
        pointW = 18;
        xfontSize = '12px';
        xNumFontSize = '12px';
    }
    return [pubuPointW, xfontSize, pointW, xNumFontSize];
}

// chart1
function columnChart(conShow,xData,yData) {
    // $(conShow).css('minWidth', xData.length * 6 + '%');
    $(conShow).highcharts({
        chart: {
            type: 'column',
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            categories: xData,
            crosshair: true,
            labels: {
                // rotation: 0//调节倾斜角度偏移
                style: {fontSize: chartWith()[1]}
            }
        },
        yAxis: {
            // min: 0,
            // max: 5,
            title: {
                text: ''
            },
            labels: {
                /*formatter: function () {
                    return this.value / 1 ;
                }*/
            }
        },
        tooltip: {
            shared: true,
            valueSuffix: '',
            shadow: false
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0,
                pointWidth: chartWith()[2]
            }
        },
        series: [{
            name: '异常终端IMEI数',
            data: yData,
            color: "#3095f2"
        }],
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        }
    });
    $("#conShowWrap1").getNiceScroll(0).show();
    $("#conShowWrap1").getNiceScroll(0).resize();
    $('#ascrail2000').remove();
}

// chart2
function lineChart(conShow,xData,yData) {
    // $(conShow).css('minWidth', xData.length * 8 + '%');
    $(conShow).highcharts({
        chart: {
            backgroundColor: 'rgba(0,0,0,0)'
        },
        title: {
            text: null
        },
        xAxis: {
            categories: xData,
            gridLineWidth: 1,
            labels: {
                // rotation: 0//调节倾斜角度偏移
                style: {fontSize: chartWith()[3]}
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
                /*formatter: function () {
                    return this.value / 1 ;
                }*/
            }
        },
        credits: {
            enabled: false
        },
        tooltip: {
            valueSuffix: '',
            shadow: false
        },
        legend: {
            enabled: false
        },
        series: [{
            name: '异常终端IMEI数',
            data: yData,
            color: '#00c58e'
        }],
        exporting: {enabled: false}
    });
}

// 主界面地图展示
function getChinaMap(backdata) {
    var mapData = $.fn.chinaMapData();
    var map = null;

    $.each(mapData, function (i, map) {
        var mapCode = map.properties.id;
        map.value = 37;
        $.each(backdata, function (i, obj) {
        	if(obj.short_name == map.properties.cnname){
        		if(Number(obj.yc_imei_num) != 0){
        			if(i<=4){
        				map.value = 4;
        			}else if(i>4){
        				map.value = 16;
        			}
        		}else{
        			map.value = 27;
        		}
        	}
        });
       
        if (map.properties.cnname == '台湾'||map.properties.cnname == '香港'||map.properties.cnname == '澳门') {
            map.value = 37;
        }
    });
    map = new Highcharts.Map('chinaMap', {
        lang: {
            drillUpText: '<span style="color:#f8a900;">返回</span>'
        },
        chart: {},
        title: {
            text: null
        },
        legend: {
            enabled: true,
            itemStyle: {
                fontSize: '12px'
            },
            width: 375
        },
        tooltip: {
            enabled: false
        },
        colorAxis: {
            dataClasses: [
                {color: "#FF485D", from: 1, to: 10, name: "高危预警"},
                {color: "#FFCF89", from: 11, to: 20, name: "危险预警"},
                {color: "#7BE198", from: 21, to: 30, name: "情况正常"},
                {color: "#CCCCCC", from: 31, name: "没有数据"}
            ]
        },
        series: [{
            animation: {
                duration: 1000
            },
            data: mapData,
            dataLabels: {
                enabled: true,
                allowOverlap: true,
                format: '{point.properties.cnname}',
                style: {
                    fontSize: '12px',
                    textOutline: "0px 0px #3c3e3f"
                }
            }
        }],
        drilldown: {
            activeDataLabelStyle: {
                textDecoration: 'none'
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        }
    });
    $('#chinaMap').find('.highcharts-data-label text').attr('style', 'font-size:9px;font-weight:lighter;font-family:Microsoft YaHei;color:#3c3e3f;fill:#3c3e3f;text-decoration:none;');
}

//加载数据表
function loadTable(){
    var postData = getSumQueryParam();
	var tableColNames = ['审计月','省名称','用户数','异常终端IMEI数','异常占比(%)','违规终端涉及酬金(元)'];
	
    var tableColModel = [

                    		{name:'Aud_trm',index:'Aud_trm',sortable:false},
                    		{name:'short_name',index:'short_name',sortable:false},
                    		{name:'subs_num',index:'subs_num',formatter: "currency",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
                    		{name:'yc_imei_num',index:'yc_imei_num',formatter: "currency",formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:0},sortable:false},
                    		{name:'per_imei',index:'per_imei',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                    		{name:'yc_amt',index:'yc_amt',formatter: "currency", formatoptions: {thousandsSeparator:",",decimalSeparator:".",decimalPlaces:2},sortable:false},
                         ];
    
    loadsjbTab(postData, tableColNames, tableColModel, "#tableId", "#table_pageBar", "/cjydjssjk/load_table");
}

//数据表
function loadsjbTab(postData,tableColNames,tableColModel,tabId,pageId,url){
	jQuery(tabId).jqGrid({
        url: $.fn.cmwaurl() + url,
        datatype: "json",
        postData: postData,
        colNames: tableColNames,
        colModel: tableColModel,
        autowidth: true,
        shrinkToFit: true,
        viewrecords: true,
        hoverrows: false,
        altRows : true,
        viewsortcols: true,
        height: "auto",
        altclass : "myAltRowClass",
        rowNum: 8,
        pager : pageId,
        jsonReader : {
            repeatitems : false,
            root : "dataRows",
            page : "curPage",
            total : "totalPages",
            records : "totalRecords"
        },
        prmNames : {
            rows : "pageSize",
            page : "curPage",
            sort : "orderBy",
            order : "order"
        },
        cmTemplate : { sortable : false, resizable : false},
        loadComplete : function() {
            // 因本工程样式和jQGrid自身样式有冲突，则对表格特殊处理
        	$(tabId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            $(tabId).setGridWidth($(tabId).parent().parent().parent().width());
            $(pageId).css("width", $(pageId).width());
        }
    });
}

$(window).resize(function(){
	   $("#tableId").setGridWidth($(".sjk_tb_ctn").width());
});