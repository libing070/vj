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
    
    $("#qdhyPrvdCharts").css({width: $("#qdhyPrvdCharts").parent().parent().parent().width() - 20, height: 315});
    
}

function initEvent() {
	
	$("#offerTab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadSumOfferTable('sumOfferTable','sumOfferTablePageBar');
		loadQdhyPrvdTableCon("sumOfferTableCon");
    });
	
	
	
	$("#exprotSumOfferTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");
		 var totalNum = $("#sumOfferTable").getGridParam("records");

		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		exportTab("exprotSumOfferTable");
    });

		
	$("#exportDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

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
        $("#currTab").val("hz");
    });
    
    // 明细数据Tab监听事件
    $("#mx_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
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
    
    $.ajax({
        url : $.fn.cmwaurl() + "/gprs/1501/initDefaultParams",
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
    postData.startTime = $("#startTime").val();
    postData.endTime = $("#endTime").val();
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	loadQdhyPrvd();
	loadOnlineMon();
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	$("#offerCharts").click();
	loadQdhyPrvd();
	loadOnlineMon();
}
/**
 * 构建离网率图表
 */
function loadQdhyPrvd(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/getQdhyPrvd",
		dataType : 'json',
		data : postData,
		success : function(data) {
			drawQdhyCharts('qdhyPrvdCharts',data);
			loadQdhyPrvdCon('qdhyPrvdCon',postData);
		}
	});
	
}
/**
 * 构建离网率审计结论
 * @param id
 * @param postData
 */
function loadQdhyPrvdCon(id,postData){
    text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"终端销售量前20的营销案对应的用户合约内离网率统计。";
	$('#'+id).html(text);
}
/**
 * 构建离网率数据表审计结论
 * @param id
 * @param postData
 */
function loadQdhyPrvdTableCon(id){
	var postData = getSumQueryParam();
    text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"合约终端营销案对应的合约内离网用户数率统计。";
	$('#'+id).html(text);
}
/**
 * 合约内离网用户的在网时长分
 */
function loadOnlineMon(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/getOnlineMon",
		dataType : 'json',
		data : postData,
		success : function(data) {
			loadOnlineMonTalbe("onlineMonTalbe",data);
			pieHighChart("#onlineMonPie",data);
			loadLineMonCon("onlineMonPieCon",postData);
		}
	});
}
function loadLineMonCon(id,postData){
    text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"合约内离网用户的在网时长分布及其数据统计结果。";
	$('#'+id).html(text);
}
//饼状图
function pieHighChart(Id,data){
	var pieData = [];
	
	for(var i=0;i<data.length;i++){
		var eachArray = [];
		eachArray.push(data[i].ONLINE_MON_ARR);
		eachArray.push(data[i].SUM_SUBS_NUM);
		pieData.push(eachArray);
	}
	
	$(Id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
        	formatter: function() {
                return '<b>'+ this.point.name +'用户数</b>: '+ 
                             Highcharts.numberFormat(this.y, 0) +',占比（'+Highcharts.numberFormat(this.percentage, 2)+"%)";
             }
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
            name: "合约内离网用户的在网时长分布",
            innerSize:'50%',
            data: pieData
        }]
    });
}
function loadOnlineMonTalbe(tableId,data){
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>入网时间</th>" +
			"<th>用户数</th>" +
			"<th>占比</th></tr>";
	var sum = 0 ;
	var mon1 = 0;
	var mon6 = 0;
	var mon12 = 0;
	$.each(data, function(i, obj) {
		if(obj.ONLINE_MON_ARR.indexOf("1-6")>-1){
			mon1 = obj.SUM_SUBS_NUM;
		}
		if(obj.ONLINE_MON_ARR.indexOf("7-12")>-1){
			mon6 = obj.SUM_SUBS_NUM;
		}
		if(obj.ONLINE_MON_ARR.indexOf("12月以上")>-1){
			mon12 = obj.SUM_SUBS_NUM;
		}
		sum = sum +obj.SUM_SUBS_NUM;
	});
	htmlstr = htmlstr+"<tr>" +
			"<td>1-6月</td>" +
			"<td>"+formatCurrency(mon1,false)+"</td>" +
			"<td>"+(mon1/sum*100).toFixed(2)+"%</td>" +
			"</tr>";
	htmlstr =htmlstr+ "<tr>" +
		"<td>7-12月</td>" +
		"<td>"+formatCurrency(mon6+888888,false)+"</td>" +
		"<td>"+(mon6/sum*100).toFixed(2)+"%</td>" +
		"</tr>";
	htmlstr =htmlstr+ "<tr>" +
		"<td>12月以上</td>" +
		"<td>"+formatCurrency(mon12,false)+"</td>" +
		"<td>"+(mon12/sum*100).toFixed(2)+"%</td>" +
		"</tr>";
	htmlstr = htmlstr+"<tr><th>合计</th>" +
	"<th>"+sum+"</th>" +
			"<th>"+sum/sum*100+"%</th>" +
			"</tr>";
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
}
/**
 * 营销案汇总数据表
 */
function loadSumOfferTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    var tableColNames = [ '省代码','省名称','营销案编码','营销案名称','销售月','合约内离网用户数'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "CMCC_PROV_PRVD_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SHORT_NAME";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        var hz_startMonth = $("#hz_startMonth").val();
        var hz_endMonth = $("#hz_endMonth").val();
        
        return "{0} - {1}".format(hz_startMonth, hz_endMonth);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUM_SUBS_NUM";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/getSumOffer",
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
 * 绘制详情列表信息
 * @param tableId
 * @param pagerId
 */
function loadDetailTable(tableId,pagerId){
	var postData = getSumQueryParam();
 
    var tableColNames = [ '审计月','省名称','地市名称','渠道名称','用户标识','销售时间','终端IMEI','营销案名称','捆绑周期','用户离网日期','用户入网日期','用户在网时长'];
    
    var tableColModel = [];
    tableMap = new Object();
    tableMap.name = "AUD_TRM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SHORT_NAME";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "CMCC_PRVD_NM_SHORT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "CHNL_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUBS_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SELL_DAT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "TRMNL_IMEI";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "BND_PRD";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "EFF_DT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "ENT_DT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "ONLINE_MON";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/getDetailListPager",
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
        	  $("#"+tableId).setGridWidth($(".tab-map-box").width() - 2);
              $("#"+pagerId).css("width", $("#"+pagerId).width() - 2);
              $("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
              $("#"+tableId).setGridWidth($(".shuju_table").width() - 2);
        }
    });
}
/**
 * 刷新明细地市列表
 * 
 */
function reLoadCityDetailGridData(tableId) {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/getDetailListPager";
    
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
/**
 * 绘制图表
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawQdhyCharts(id, data) {
		var name = [];
	    var IMEI_NUM  = [];
	    var LWL = [];
	    $.each(data, function(i, obj) {
	    	name.push(obj.OFFER_NM);
	    	IMEI_NUM.push(obj.SUM_IMEI_NUM);
	    	LWL.push(Number((obj.LWL*100).toFixed(2)));
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
	        	categories: name,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: "终端销售数量",
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	           
	        },{
	        	labels : {
					format : '{value}%',
				//enabled: false 
				},
	            title: {
	                text: "离网率",
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
	            name: "终端销售数量",
	            type: 'column',
	            color:'#65d3e3',
	            yAxis:0,
	            data: IMEI_NUM,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "离网率",
	            type: 'spline',
	            color:'#B32BD5',
	            yAxis:1,
	            data: LWL,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
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
function exportTab(functionName){
    var hz_rankType = parseInt($("#hz_rankType").val());
    var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/" +functionName+
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
 * 导出
 */
function exportDetailTab(functionName){
    var hz_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
    var mx_cityCode = $("#mx_cityCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1701/" +functionName+
	"?mx_cityCode="+mx_cityCode +
	"&mx_startMonth="+hz_startMonth +
	"&mx_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}
