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
    
    $("#overCharts").css({width: $("#overCharts").parent().parent().parent().width() - 20, height: 315});
    $("#subsCharts").css({width: $("#subsCharts").parent().parent().parent().width() - 20, height: 315});
    $("#ctyCharts").css({width: $("#ctyCharts").parent().parent().parent().width() - 20, height: 315});
    
}

function initEvent() {
	
	$("#gprsTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadGprsTable("subsTable","subsTablePageBar");
	});
	
	$("#ctyTab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadCtyTable("ctyTable","ctyTablePageBar");
	});
	 // 导出地市汇总数据
    $("#exportCtyTable").click(function() { 
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#ctyTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        exportTab("exportCtyTable");
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
        
        $("#user50G").click();
        $("#ctyChartsTab").click();
        
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

    
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	loadSumGprsPrvd();
	loadSumGprsCty();
	
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	loadSumGprsPrvd();
	loadSumGprsCty();
}
/**
 * 构建省汇总
 */
function loadSumGprsPrvd(){
	 var postData = getSumQueryParam();
	    $.ajax({
	        url : $.fn.cmwaurl() + "/gprs/1501/getSumGprsPrvd",
	        dataType : 'json',
	        async : false,
	        data : postData,    
	        success : function(data) {
	        	var maxPerSubs = 0.00;
	        	var maxPerSubsObject;
	        	var maxPerOver = 0.00;
	        	var maxPerOverObject;
	        	var audTrmArray = [];
	        	var perSubsArray = [];
	        	var perOverArray = [];
	        	var	numSubsArray = [];
	        	var numOverArray = [];
	        	var maxNum=0;
	        	var maxNumAudTrm="";
	        	var maxOVER_GPRS = 0;
	        	var over_gprs_audTrm="";
	            $.each(data, function(i, eachObj) {
	            	
	            	audTrmArray.push(eachObj.AUD_TRM);
	            	perSubsArray.push(Number(((eachObj.PER_SUBS_MON*100).toFixed(2))));
	            	numOverArray.push(Number(eachObj.OVER_GPRS.toFixed(2)));
	            	numSubsArray.push(eachObj.SUBS_NUM);
	            	
	            	if(maxOVER_GPRS<eachObj.OVER_GPRS){
	            		maxOVER_GPRS = eachObj.OVER_GPRS;
	            		over_gprs_audTrm = eachObj.AUD_TRM;
	            	}
	            	
	            	var perOver = Number(eachObj.OVER_GPRS)/Number(eachObj.TOL_GPRS);
	            	if(perOver>maxPerOver){
	            		maxPerOver = perOver;
	            		maxPerOverObject = eachObj;
	            		maxPerOverObject.maxPerOver = Number((maxPerOver*100).toFixed(2));
	            	}
	            	if(eachObj.PER_SUBS_MON>maxPerSubs){
	            		maxPerSubs = eachObj.PER_SUBS_MON;
	            		maxPerSubsObject = eachObj;
	            		maxPerSubsObject.maxPerSubs = Number((maxPerSubs*100).toFixed(2));
	            	}
	            	if(maxNum<eachObj.SUBS_NUM){
	            		maxNum = eachObj.SUBS_NUM;
	            		maxNumAudTrm = eachObj.AUD_TRM;
	            	}
	            	perOverArray.push(Number((perOver*100).toFixed(2)));
	            	
	            });
	            
	            drawCharts('subsCharts',audTrmArray,numSubsArray,perSubsArray,'月使用流量超50G用户数','环比');
	            loadSubsConclusion("subsCon",maxPerSubsObject,postData,maxNum,maxNumAudTrm);
	            
	            drawCharts('overCharts',audTrmArray,numOverArray,perOverArray,'超限流量（G）','超限流量占比');
	            loadOverConclusion("overCon",maxPerOverObject,postData,maxOVER_GPRS,over_gprs_audTrm);
	        }
	    });
}
/**
 * 构建超限流量审计结论
 * @param data
 * @param postData
 */
function loadOverConclusion(id,data,postData,maxOVER_GPRS,over_gprs_audTrm){
	var text = "";
	if(data==undefined){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+"，"+$("#provinceName").val()+"无数据。";
		$('#'+id).html(text);
	}else{
	    text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
	    	+"，"+$("#provinceName").val()+"的超限流量按月统计趋势如下。其中"+timeToChinese(over_gprs_audTrm)
	    	+"，超限流量最高，达到"+maxOVER_GPRS+"G，"+timeToChinese(data.AUD_TRM)
	    	+"超限流量占比最高，达到"+data.maxPerOver+"%。";
		$('#'+id).html(text);
	}
		
}
/**
 * 构建超50G用户数
 * @param data
 * @param postData
 */
function loadSubsConclusion(id,data,postData,maxNum,maxNumAudTrm){
	var text = "";
	if(data==undefined){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+"，"+$("#provinceName").val()+"无数据。";
		$('#'+id).html(text);
	}else{
	    text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
	    	+"，"+$("#provinceName").val()+"月使用流量超50G的违规用户按月统计趋势如下。" +"其中"+timeToChinese(maxNumAudTrm)
	    			+"违规用户数最多，达到"+maxNum+"个，"+timeToChinese(data.AUD_TRM)+"违规用户数环比最高，达到"+data.maxPerSubs+"%。";
		$('#'+id).html(text);
	}
		
}
/**
 * 绘制图表
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawCharts(id, xdata,ydata1,ydata2,ytext1,ytext2) {
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
	        	categories: xdata,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: ytext1,
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
	                text: ytext2,
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
	            name: ytext1,
	            type: 'column',
	            color:'#65d3e3',
	            yAxis:0,
	            data: ydata1,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: ytext2,
	            type: 'spline',
	            color:'#B32BD5',
	            yAxis:1,
	            data: ydata2,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
}
/**
 * 数据表
 */
function loadGprsTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    var tableColNames = [ '审计月','月流量超50G的用户数','用户数环比','超限流量（G）','超限流量占比','全省实际使用总流量（G）'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "AUD_TRM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUBS_NUM";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "PER_SUBS_MON";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return (cellvalue*100).toFixed(2)+"%";
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OVER_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "PER_OVER_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return (cellvalue*100).toFixed(2)+"%";
    };
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "TOL_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
   
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/gprs/1501/getSumGprsPrvdPager",
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
 * 构建地市汇总图表
 */
function loadSumGprsCty(){
	var postData = getSumQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/gprs/1501/getSumGprsCty",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	drawCtyCharts("ctyCharts",data);
        	loadCtyCon("ctyCon",data,postData);
        }
    });	
}
/**
 * 审计结论
 * @param id
 * @param data
 */
function loadCtyCon(id,data,postData){
	var text = "";
	var sum = 0;
	var maxPER_OVER_GPRS=0;
	var maxShortName="";
	var maxSUM_OVER_GPRS=0;
	var maxGPRSShortName="";
    $.each(data, function(i, obj) {
    	sum = sum + obj.SUM_OVER_GPRS;
    	if(i<1){
    		maxPER_OVER_GPRS = obj.PER_OVER_GPRS;
    		maxShortName=obj.CMCC_PRVD_NM_SHORT;
    	}
    	if(maxSUM_OVER_GPRS<obj.SUM_OVER_GPRS){
    		maxSUM_OVER_GPRS = obj.SUM_OVER_GPRS;
    		maxGPRSShortName = obj.CMCC_PRVD_NM_SHORT;
    	}
    });
    if(sum == 0){
    	text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"无数据";
    }else{
	    var provinceCode = postData.provId;
	    if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300" ){
	    	text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"
	    	+$("#provinceName").val()+"超限流量共有"+formatCurrency(sum,false)+"G，超限流量占比"+formatCurrency(maxPER_OVER_GPRS*100,true)+"%。";
	    }else{
	    	text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
	    	+"，"+$("#provinceName").val()+"超限流量共有"+formatCurrency(sum,false)+"G。其中"
	    	+maxShortName+"超限流量占比最高，达到"+formatCurrency(maxPER_OVER_GPRS*100,true)+"%，"
	    	+maxGPRSShortName+"超限流量最高，达到"+formatCurrency(maxSUM_OVER_GPRS,false)+"G。";
	    }
    }
	$('#'+id).html(text);
}
/**
 * 绘制图表
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawCtyCharts(id, data) {
		var shortName = [];
	    var SUM_OVER_GPRS  = [];
	    var PER_OVER_GPRS = [];
	    $.each(data, function(i, obj) {
	    	shortName.push(obj.CMCC_PRVD_NM_SHORT);
	    	SUM_OVER_GPRS.push(obj.SUM_OVER_GPRS);
	    	PER_OVER_GPRS.push(Number((obj.PER_OVER_GPRS*100).toFixed(2)));
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
	        	categories: shortName,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: "超限流量（G）",
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
	                text: "超限流量占比",
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
	            name: "超限流量（G）",
	            type: 'column',
	            color:'#65d3e3',
	            yAxis:0,
	            data: SUM_OVER_GPRS,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: "超限流量占比",
	            type: 'spline',
	            color:'#B32BD5',
	            yAxis:1,
	            data: PER_OVER_GPRS,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
}


/**
 * 数据表
 */
function loadCtyTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    var tableColNames = [ '审计月','地市名称','超限流量（G）','超限流量占比','地市实际使用总流量（G）'];
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
    tableMap.name = "SUM_OVER_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
   
    
    tableMap = new Object();
    tableMap.name = "PER_OVER_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return (cellvalue*100).toFixed(2)+"%";
    };
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "SUM_TOL_GPRS";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
   
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/gprs/1501/getSumGprsCtyPager",
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
 * 构建明细表
 */
function loadDetailTable(tableId,pagerId){
	  var postData = getDetailQueryParam();
	  
	    var tableColNames = [ '审计月','地市名称','用户标识','手机号','用户使用总流量(G)','用户状态','用户类型','出账收入(元)','业务受理类型','业务办理时间','基础套餐标识','基础套餐名称'];
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
	    tableMap.width = 100;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	   
	    
	    tableMap = new Object();
	    tableMap.name = "MSISDN";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "SUM_STRM_AMT";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "SUBS_STAT_TYP_CD";
	    tableMap.width = 100;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    
	    tableMap = new Object();
	    tableMap.name = "SUBS_BUSN_TYP_CD";
	    tableMap.width = 100;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    
	    tableMap = new Object();
	    tableMap.name = "MER_AMT";
	    tableMap.width = 100;
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue, true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "BUSI_ACCE_TYP";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "BUSI_OPR_TM";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "BASIC_PACK_ID";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "BASIC_PACK_NAME";
	    tableMap.width = 200;
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	   
	    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
	    
	    jQuery("#"+tableId).jqGrid({
	    	url : $.fn.cmwaurl() + "/gprs/1501/getGprsDetailPager",
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
    var url = $.fn.cmwaurl() + "/gprs/1501/getGprsDetailPager";
    
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
function exportTab(functionName){
    var hz_rankType = parseInt($("#hz_rankType").val());
    var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/gprs/1501/" +functionName+
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
	var url = $.fn.cmwaurl() + "/gprs/1501/" +functionName+
	"?mx_cityCode="+mx_cityCode +
	"&mx_startMonth="+hz_startMonth +
	"&mx_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}
