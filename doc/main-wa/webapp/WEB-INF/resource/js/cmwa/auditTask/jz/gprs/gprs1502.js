$(function() {
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
    
    // 初始化图形组件大小等于盒子大小（普通）
    $("#ranklineChart").css({width: $(".zhexian-map").width() - 20, height: 328});
    $("#cityMapChart").css({width: $(".map-img").width() - 20, height: 328});
    
    // 初始图形组件大小等于浮层大小（图形放大）
    $("#ranklineChartBig").css({width: $(".modal-box").width() - 162, height: $(".zhexian-bigmap").height()});
    $("#cityMapChartBig").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
    
    $("#ctyCharts").css({width: $("#ctyCharts").parent().parent().parent().width() - 20, height: 315});
    
}

function initEvent() {
		
	 $("#ctyTab").click(function() {
		 insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		 loadCtyTable("sumCtyTable","sumCtyTablePageBar");
	 });
	 
	 $("#exportSumCtyTable").click(function() {
		 insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		 // 导出地市汇总数据
		var totalNum = $("#sumCtyTable").getGridParam("records");

		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		exportTab("exportCtyTable");
	 });
	 
	 $("#exportDetailList").click(function() {
		 insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		 // 导出地市汇总数据
		var totalNum = $("#cityDetailGridData").getGridParam("records");

		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		exportDetailTab("exportDetailTable");
	 });
	 
	 
    // 明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	 if (!mxFormValidator()) {
             return false;
         }
    	reLoadCityDetailGridData("cityDetailGridData");
    });
    
    // 按地市排名下拉框事件监听
    addSelectEvent("hz_rankType", function() {

    });
    
    // 汇总统计开始时间监听事件
    addMonthCalendarEvent("hz_startMonth");
    
    // 汇总统计结束时间监听事件
    addMonthCalendarEvent("hz_endMonth");

    // 明细统计开始时间监听事件
    addMonthCalendarEvent("mx_startMonth");
    
    // 明细统计结束时间监听事件
    addMonthCalendarEvent("mx_endMonth");

    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!hzFormValidator()) {
            return false;
        }
        
        reloadGlobalData();
    });
    
    // 是否营销案下拉框事件监听
    addSelectEvent("mx_yxaFlag");
    
    // 地市名称下拉框事件监听
    addSelectEvent("mx_cityCode");
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
    });
    
    // 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
       $("#currTab").val("mx");
       if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
	       	 var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	       	 var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	       	 var provinceCode = $.fn.GetQueryString("provinceCode");
	       	 $("#provinceCode").val(provinceCode);
	       	 $("#mx_startMonth").val(beforeAcctMonth);
	         $("#mx_endMonth").val(endAcctMonth);
       }
       loadDetailTable("cityDetailGridData","cityDetailGridDataPageBar");
    });
    
    
  

    // 明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        clearHideFormInput();
    });

    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
    
    // 日历弹出事件
    $(".add-on").click(function(event) {
        var id = $(this).prev().attr("id");
        $('#' + id).datetimepicker('show');
    });
}
/**
 * 导出
 */
function exportTab(functionName){
    var hz_rankType = parseInt($("#hz_rankType").val());
    var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/gprs_llcp/1502/" +functionName+
	"?hz_rankType="+hz_rankType +
	"&hz_startMonth="+hz_startMonth +
	"&hz_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}


/**
 * 首次加载默认参数初始化
 * 
 */
function initDefaultParams() {
  
    var postData = {};
    var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    var tab = $.fn.GetQueryString("tab");
    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
    var provList = $.fn.provList();
    // 获取当前省名称
    $.each(provList, function(i, obj) {
       if (obj.provId == provinceCode) {
    	   $("#provinceName").val(obj.provName);
           return false;
       }
    });
    
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    // 汇总数据Tab监听事件
//    $("#hz_tab").click(function() {
//        $("#currTab").val("hz");
//    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/gprs_llcp/1502/initDefaultParams",
        async : false,
        dataType : 'json',
        data : postData,    
        success : function(data) {        
            $('#hz_rankType').val(data['hz_rankType']);
            $('#hz_map_double').val(data['hz_map_double']);
            
            $('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
            $('#mx_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#mx_endMonth').val($.fn.timeStyle(endAcctMonth));
            
            $('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
            
            $('#hz_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#hz_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
            
            $('#mx_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#mx_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
        }
    });
}

/**
 * 汇总表单校验
 */
function hzFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#hz_startMonth").val() >  $("#hz_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    
    return true;
}

/**
 * 明细表单校验
 */
function mxFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#mx_startMonth").val() >  $("#mx_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    
    return true;
}

/**
 * 获取汇总查询条件
 * 
 */
function getSumQueryParam() {
    var postData = {};
    postData.hz_rankType = parseInt($("#hz_rankType").val());
    postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
    postData.trendType=$("#trendType").val();
    return postData;
}

/**
 * 获取明细查询条件
 * 
 */
function getDetailQueryParam() {
    var postData = {};
    postData.mx_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_yxaFlag = $("#mx_yxaFlag").val();
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();

    
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	loadSumPrice();
	loadSumCty();
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	loadSumPrice();
	loadSumCty();
	loadCtyTable("sumCtyTable","sumCtyTablePageBar");
}

/**
 * 构建饼图
 */
function loadSumPrice(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/gprs_llcp/1502/getSumPrice",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			loadPriceTable("priceTable", data);
			pieHighChart("#pricePieCharts", data);
			loadPriceCon("pricePieCon", data);
		}
	});
	
}
/**
 * 赠送金额数据表
 */
function loadPriceTable(tableId,data) {
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>流量单价</th>" +
			"<th>使用流量(G)</th></tr>";
	var sum = 0 ;
	if(data.length>=5){
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+data[2].PRICE_RANGE+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(data[2].SUM_STRM_AMT_G,false)+"</td>";
		htmlstr = htmlstr+"</tr>";
		sum = sum +data[2].SUM_STRM_AMT_G;
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+data[1].PRICE_RANGE+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(data[1].SUM_STRM_AMT_G,false)+"</td>";
		htmlstr = htmlstr+"</tr>";
		sum = sum +data[1].SUM_STRM_AMT_G;
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+data[0].PRICE_RANGE+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(data[0].SUM_STRM_AMT_G,false)+"</td>";
		htmlstr = htmlstr+"</tr>";
		sum = sum +data[0].SUM_STRM_AMT_G;
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+data[3].PRICE_RANGE+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(data[3].SUM_STRM_AMT_G,false)+"</td>";
		htmlstr = htmlstr+"</tr>";
		sum = sum +data[3].SUM_STRM_AMT_G;
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+data[4].PRICE_RANGE+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(data[4].SUM_STRM_AMT_G,false)+"</td>";
		htmlstr = htmlstr+"</tr>";
		sum = sum +data[4].SUM_STRM_AMT_G;
	}
	htmlstr = htmlstr+"<tr><th>合计</th>" +
	"<th>"+formatCurrency(sum,false)+"</th></tr>";
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
}
/**
 * 构建单价汇总审计结论
 */
function loadPriceCon(id,data){
	$("#"+id).html("");
	var postData = getSumQueryParam();
	var min5g = 0;
	var minflow = 0;
	$.each(data, function(i, obj) {
		if(obj.PRICE_RANGE.indexOf("小于5元")>-1){
			min5g = obj.SUM_STRM_AMT_G;
		}
	});
	$.ajax({
		url : $.fn.cmwaurl() + "/gprs_llcp/1502/getSumPrvd",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			if(data.length!=0){
				minflow = data[0].SUM_LOW_GPRS_TOL_G;
			}
		}
	});
	if(min5g==0&&minflow==0){
		var htmlstr=timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"无数据";
	}else{
		var htmlstr=timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"的低价流量共使用"+formatCurrency(minflow,false)+"G，其中单价小于5元/G的流量是"+formatCurrency(min5g,false)+"G。";
	}
	
	$("#"+id).html(htmlstr);
}

//饼状图
function pieHighChart(Id,data){
	var pieData = [];
	if(data.length>=5){
	var eachArray2 = [];
	eachArray2.push(data[2].PRICE_RANGE);
	eachArray2.push(data[2].SUM_STRM_AMT_G);
	pieData.push(eachArray2);
	
	var eachArray1 = [];
	eachArray1.push(data[1].PRICE_RANGE);
	eachArray1.push(data[1].SUM_STRM_AMT_G);
	pieData.push(eachArray1);
	
	var eachArray0 = [];
	eachArray0.push(data[0].PRICE_RANGE);
	eachArray0.push(data[0].SUM_STRM_AMT_G);
	pieData.push(eachArray0);
	
	var eachArray3 = [];
	eachArray3.push(data[3].PRICE_RANGE);
	eachArray3.push(data[3].SUM_STRM_AMT_G);
	pieData.push(eachArray3);
	
	var eachArray4 = [];
	eachArray4.push(data[4].PRICE_RANGE);
	eachArray4.push(data[4].SUM_STRM_AMT_G);
	pieData.push(eachArray4);
	}
	
	
	$(Id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
        },
        colors:['#0000FF','#1AE6E6','#FFFF00','#FFC977','#E9B28E'],
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: "低价流量单价分布",
            innerSize:'50%',
            data: pieData
        }]
    });
}
/**
 * 绘制地市汇总信息
 */
function loadSumCty(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/gprs_llcp/1502/getSumCty",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			var data1 = data.data;
			var sumGPRS = data.sumGPRS;
			var maxCity3 = data.maxCity3;
			var minCity3 = data.minCity3;
			drawSumCty("ctyCharts",data1);
			loadSumCtyCon("ctyCon",sumGPRS,maxCity3,minCity3,postData);
		}
	});
}
/**
 * 审计结论
 * @param id
 * @param data
 * @param postData
 */
function loadSumCtyCon(id,sumGPRS,maxCity3,minCity3,postData){
	var provinceCode = postData.provId;
	var sumGPRS1 = 0;
	var top3_max = "";
	var top3_min = "";
	var maxCity3_GPRS=0;
	var top = "";
	var maxtop="";
	var mintop="";
	sumGPRS1 = sumGPRS;
	$.each(maxCity3, function(i, obj) {
		if (i < 3) {
			top3_max = top3_max + obj.cmcc_prvd_nm_short + "、";
			maxCity3_GPRS = maxCity3_GPRS + obj.sum_gprs_G;
		}
	});
	
	$.each(minCity3, function(i, obj) {
		if (i < 3) {
			top3_min = top3_min + obj.cmcc_prvd_nm_short + "、";
		}
	});
	if(top3_max.length>0){
		maxtop = top3_max.substring(0, top3_max.length-1);
	}
	if(top3_min.length>0){
		mintop = top3_min.substring(0, top3_min.length-1);
	}
	if(sumGPRS1!=0){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			var htmlstr = timeToChinese(postData.hz_startMonth) + "-"+ timeToChinese(postData.hz_endMonth) + "，"
			+ $("#provinceName").val() + "低价流量共使用"+formatCurrency(sumGPRS,false)+"G。";
		}else{
			var htmlstr = timeToChinese(postData.hz_startMonth) + "-"+ timeToChinese(postData.hz_endMonth) + "，"
			+ $("#provinceName").val() + "低价流量共使用"+formatCurrency(sumGPRS,false)+"G，其中低价流量使用量排名前三的地市："+maxtop
			+"、低价流量共使用"+formatCurrency(maxCity3_GPRS,false)+"G；低价流量平均单价最低的前三地市："+mintop+"。";
		}
	}else{
		var htmlstr = timeToChinese(postData.hz_startMonth) + "-"
					+ timeToChinese(postData.hz_endMonth) + "，"+$("#provinceName").val() + "无数据";
	}
	
	$("#" + id).html(htmlstr);
}
/**
 * 绘制地市汇总信息
 * 
 * @param id
 * @param data
 */
function drawSumCty(id, data) {
 	var CMCC_PRVD_NM_SHORT = [];
    var SUM_LOW_GPRS_TOL_G  = [];
    var AVG_PER_GPRS_SUBS_G  = [];
    
    $.each(data, function(i, obj) {
    	CMCC_PRVD_NM_SHORT.push(obj.CMCC_PRVD_NM_SHORT);
    	SUM_LOW_GPRS_TOL_G.push(Number(obj.SUM_LOW_GPRS_TOL_G.toFixed(2)));
    	AVG_PER_GPRS_SUBS_G.push(Number(obj.AVG_PER_GPRS_SUBS_G.toFixed(2)));
    });
   Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
    $('#'+id).highcharts({
		chart: {
            zoomType: 'xy',
            defaultSeriesType: 'line'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        credits: {
            enabled: false
        },
        xAxis: [{
        	categories: CMCC_PRVD_NM_SHORT,
            crosshair: true
        }],
        yAxis: [{
        	labels : {
			},
            title: {
                text: '低价流量使用量(G)',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        },{
        	labels : {
			},
            title: {
                text: '低价流量平均单价（元/G）',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
            opposite: true
        }],
        tooltip: {
            shared: true
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '低价流量使用量(G)',
            type: 'column',
            color:'#65d3e3',
            yAxis:0,
            data: SUM_LOW_GPRS_TOL_G,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '低价流量平均单价（元/G）',
            type: 'spline',
            yAxis:1,
            color:'#df5438',
            data: AVG_PER_GPRS_SUBS_G,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}
/**
 * 构建地市汇总表
 * @param tableId
 * @param pagerId
 */
function loadCtyTable(tableId,pagerId){
		var postData = getSumQueryParam();
	 
	    var tableColNames = [ '审计区间','地市名称','低价流量使用量（G）','低价流量收入（元）','低价流量平均单价（元/M）','低价流量平均单价（元/G）'];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        var hz_startMonth = $("#hz_startMonth").val();
	        var hz_endMonth = $("#hz_endMonth").val();
	        
	        return "{0} - {1}".format(hz_startMonth, hz_endMonth);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "CMCC_PRVD_NM_SHORT";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUM_LOW_GPRS_TOL_G";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, false);
	    };
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "SUM_LOW_GPRS_FEE";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "AVG_PER_GPRS_SUBS_M";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return cellvalue.toFixed(4);
	    };
	    tableColModel.push(tableMap);
	    

	    tableMap = new Object();
	    tableMap.name = "AVG_PER_GPRS_SUBS_G";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	    	return cellvalue.toFixed(4);
	    };
	    tableColModel.push(tableMap);
	    
	    
	    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
	    
	    jQuery("#"+tableId).jqGrid({
	    	url : $.fn.cmwaurl() + "/gprs_llcp/1502/getSumCtyPager",
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
	        rowNum: 5,
	        pager : "#"+pagerId,
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
	            $("#"+tableId).setGridWidth($(".tab-map-box").width() - 2);
	            $("#"+pagerId).css("width", $("#"+pagerId).width() - 2);
	        }
	    });
}

/**
 * 导出明细数据
 * @param tableId
 * @param pagerId
 */
function loadDetailTable(tableId,pagerId){
	var postData = getSumQueryParam();
 
    var tableColNames = [ '审计月','地市名称','用户标识','用户总流量(M)','GPRS收入(元)','基本月租费(元)','GPRS相关收入(元)','实际使用流量单(元/M)',
                          '资费套餐统一编码','资费套餐名称','流量总量','语音总量','短信总量','彩信总量','WLAN总量时长','WLAN总量流量'];
    var tableColModel = [];
    tableMap = new Object();
    tableMap.name = "AUD_TRM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "CMCC_PRVD_NM_SHORT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUBS_ID";
    tableMap.width = 200;
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUM_STRM_AMT";
    tableMap.width = 200;
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "GPRS_AMT";
//    tableMap.width = 100;
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "BAS_MON_FR";
//    tableMap.width = 100;
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "FEE_SUM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
  
    tableMap = new Object();
    tableMap.name = "GPRS_PRICE";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "fee_pack_unit_cd";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "fee_pack_nm";
    tableMap.align = "center";
    tableMap.width = 100;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "strm_tot";
    tableMap.align = "center";
    tableMap.width = 100;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "voic_tot";
    tableMap.align = "center";
    tableMap.width = 100;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sms_tot";
    tableMap.align = "center";
    tableMap.width = 100;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "mms_tot";
    tableMap.align = "center";
    tableMap.width = 100;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "wlan_tot_dur";
    tableMap.align = "center";
    tableMap.width = 120;
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "wlan_tot_strm";
    tableMap.align = "center";
    tableMap.width = 120;
    tableColModel.push(tableMap);
    
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/gprs_llcp/1502/getDetailPager",
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
        rowNum: 10,
        pager : "#"+pagerId,
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
        	  $("#"+tableId).setGridWidth($(".tab-map-box").width() - 1);
              $("#"+pagerId).css("width", $("#"+pagerId).width() - 1);
              $("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
              $("#"+tableId).setGridWidth($(".tab-nav").width()-40);
        }
    });
}
/**
 * 刷新明细地市列表
 * 
 */
function reLoadCityDetailGridData(tableId) {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/gprs_llcp/1502/getDetailPager";
    
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
/**
 * 清空隐藏表单值
 * 
 */
function clearHideFormInput() {
    $("#mx_yxaFlag").val("");
    $("#mx_cityCode").val("");
    $("#mx_startMonth").val($.fn.timeStyle($("#beforeAcctMonth").val()));
    $("#mx_endMonth").val($.fn.timeStyle($("#endAcctMonth").val()));
}

/**
 * 加载地市字段列表
 * 
 */
function loadCityList() {
    var postData = getDetailQueryParam();
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            $.each(data.body.ctyList, function(i, map) {
                var li = "<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
                $("#mx_cityCodeSelect").append(li);
            });
            addSelectEvent("mx_cityCode");
        }
    });
}


/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addSelectEvent(id, callback) {
	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}


/**
 * 为指定ID添加日历(月)组件监听事件
 * 
 * @param id
 */
function addMonthCalendarEvent(id, callback) {
    $('#' + id).datetimepicker({
        format: 'yyyy-mm',
        weekStart: 1,
        autoclose: true,
        startView: 3,
        minView: 3,
        forceParse: false,
        language: 'zh'
    }).on('changeDate', function(ev) {
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}



/**
 * 导出
 */
function exportDetailTab(functionName){
    var hz_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
    var mx_cityCode = $("#mx_cityCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/gprs_llcp/1502/" +functionName+
	"?mx_cityCode="+mx_cityCode +
	"&mx_startMonth="+hz_startMonth +
	"&mx_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}
