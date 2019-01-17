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
    
    $("#trendlineChartSingle").css({width: $("#trendlineChartSingle").parent().parent().parent().width() - 20, height: 315});
    $("#YJKCZYDetailSingle").css({width: $("#YJKCZYDetailSingle").parent().parent().parent().width() - 20, height: 315});
    $("#YJKCZYDetailSS").css({width: $("#YJKCZYDetailSS").parent().parent().parent().width() - 20, height: 315});
    $("#JzzsChartsSingle").css({width: $("#JzzsChartsSingle").parent().parent().parent().width() - 20, height: 315});
    $("#JzzsChartsSS").css({width: $("#JzzsChartsSS").parent().parent().parent().width() - 20, height: 315});
    
}

function initEvent() {
	
	//有价卡数量趋势(单时)
    $("#thrend_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	loadNumberTrendChart();
    });
    
    //有价卡数量趋势(单秒)
    $("#thrend_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	loadNumberTrendChart();
    });
    
	//有价卡数量前十地市(单时)
    $("#top10city_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	loadTop10CityNumber();
    });
    
    //有价卡数量前十地市(单秒)
    $("#top10city_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	loadTop10CityNumber();
    });
    
    
    //有价卡数量占比前十地市(单时)
    $("#top10cityper_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	loadTop10CityPer();
    });
    
    //有价卡数量占比前十地市(单秒)
    $("#top10cityper_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	loadTop10CityPer();
    });
    
    //有价卡操作员明细汇总(单时)
    $("#YJKCZYDetail_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadYJKCZYDetailSingle();
    });
    
    //有价卡操作员明细汇总(单秒)
    $("#YJKCZYDetail_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadYJKCZYDetailSS();
    });
    
    //有价卡操作员明细汇总(单时)
    $("#YJKCZYDetailTable_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	loadYJKCZYDetailTableCon("YJKCZYDetailTableConclusionSingle","单时",$("#czyCitySingleText").val());
    	loadYJKCZYDetailTable("YJKCZYDetailSingleTable","YJKCZYDetailSinglePageBar",$("#czyCitySingle").val());
    });
    
    //有价卡操作员明细汇总(单秒)
    $("#YJKCZYDetailTable_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	loadYJKCZYDetailTableCon("YJKCZYDetailTableConclusionSS","单秒",$("#czyCitySSText").val());
    	loadYJKCZYDetailTable("YJKCZYDetailSSTable","YJKCZYDetailSSPageBar",$("#czyCitySS").val());
    	
    });
    //详情列表数据（单时）
    $("#detailTypeSingle").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	reLoadCityDetailGridData();
    });
    //详情列表数据（单秒）
    $("#detailTypeSS").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	reLoadCityDetailGridData();
    });
    
    
    // 导出有价卡操作员汇总列表(单时)
    $("#exportYJKCZYSingle").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#YJKCZYDetailSingleTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        $("#trendType").val("single");
      //  var exportFileName = "{0}_有价卡合规性_集中激活分析_操作员汇总(单秒).csv".format($('#auditId').val());
        //exportYJKCZYDetail(exportFileName,$("#czyCitySingle").val());
        var postData = {};
        postData.cityId=$("#czyCitySingle").val();
        window.location.href = $.fn.cmwaurl() + "/yjk/2001/exportYJKCZYDetail?" + $.param(getSumQueryParam()) + "&" + $.param(postData);

    });
    
    // 导出有价卡操作员汇总列表(单秒)
    $("#exportYJKCZYSS").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#YJKCZYDetailSSTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        $("#trendType").val("ss");
        
       // var exportFileName = "{0}_有价卡合规性_集中激活分析_操作员汇总(单秒).csv".format($('#auditId').val());
        //exportYJKCZYDetail(exportFileName,$("#czyCitySS").val());
        var postData = {};
        postData.cityId=$("#czyCitySS").val();
        window.location.href = $.fn.cmwaurl() + "/yjk/2001/exportYJKCZYDetail?" + $.param(getSumQueryParam()) + "&" + $.param(postData);

    });
    /**
     * 导出有价卡操作员
     */
    function exportYJKCZYDetail(exportFileName,cityId){
        var hz_rankType = parseInt($("#hz_rankType").val());
        var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
        var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
        var provId = $("#provinceCode").val();
        var trendType=$("#trendType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjk/2001/exportYJKCZYDetail" +
		"?hz_rankType="+hz_rankType +
		"&hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&trendType="+trendType+
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    
    
    // 导出有价卡集中赠送列表(单时)
    $("#exportJzzsSingle").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#JzzsSingleTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        $("#trendType").val("single");
    /*    var exportFileName = "{0}_有价卡合规性_集中激活分析_操作员批量激活有价卡及集中赠送激活统计(单单时).csv".format($('#auditId').val());
        exportJzzs(exportFileName,$("#jzzsCitySingle").val());*/
        var postData = {};
        postData.cityId=$("#jzzsCitySingle").val();
        window.location.href = $.fn.cmwaurl() + "/yjk/2001/exportJzzs?" + $.param(getSumQueryParam()) + "&" + $.param(postData);

    });
    
    // 导出有价卡操作员汇总列表(单秒)
    $("#exportJzzsSS").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#JzzsSSTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        $("#trendType").val("ss");
        
       // var exportFileName = "{0}_有价卡合规性_集中激活分析_操作员批量激活有价卡及集中赠送激活统计(单秒).csv".format($('#auditId').val());
        //exportJzzs(exportFileName,$("#jzzsCitySS").val());
        var postData = {};
        postData.cityId=$("#jzzsCitySS").val();
        window.location.href = $.fn.cmwaurl() + "/yjk/2001/exportJzzs?" + $.param(getSumQueryParam()) + "&" + $.param(postData);

    });
    /**
     * 导出有价卡集中赠送
     */
    function exportJzzs(exportFileName,cityId){
        var hz_rankType = parseInt($("#hz_rankType").val());
        var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
        var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
        var provId = $("#provinceCode").val();
        var trendType=$("#trendType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjk/2001/exportJzzs" +
		"?hz_rankType="+hz_rankType +
		"&hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&trendType="+trendType+
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    
    //有价卡集中赠送图表(单时)
    $("#jzzs_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadJzzsSingle();
    });
    
    //有价卡集中赠送图表(单秒)
    $("#jzzs_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadJzzsSS();
    });
    
    //有价卡集中赠送汇总列表(单时)
    $("#jzzsTable_single").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("single");
    	loadJzzsTableCon("JzzsSingleTableConclusion","单时",$("#jzzsCitySingleText").val());
    	loadJzzsTable("JzzsSingleTable","JzzsSingleTablePageBar",$("#jzzsCitySingle").val());
    });
    
    //有价卡集中赠送汇总列表(单秒)
    $("#jzzsTable_ss").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val("ss");
    	loadJzzsTableCon("JzzsSSTableConclusion","单秒",$("#jzzsCitySSText").val());
    	loadJzzsTable("JzzsSSTable","JzzsSSTablePageBar",$("#jzzsCitySS").val());
    	
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
        $("#trendType").val("single");
        if( $("#provinceCode").val()==null||$("#provinceCode").val()==""){
       	 var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
       	 var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
       	 var provinceCode = $.fn.GetQueryString("provinceCode");
       	 $("#provinceCode").val(provinceCode);
       	 $("#mx_startMonth").val(beforeAcctMonth);
            $("#mx_endMonth").val(endAcctMonth);
        }
        loadCityDetailGridData();
    });
    
    
    
    // 明细查询按钮监听事件
    $("#mx_search_btn").click(function() {

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        if (!mxFormValidator()) {
            return false;
        }
        
        reLoadCityDetailGridData();
    });

    // 明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        clearHideFormInput();
    });

    
    // 导出明细列表
    $("#exportDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        
        var postData = {};
        
        postData.exportFileName = "{0}_有价卡合规性_批量激活分析_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjk/2001/exportDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
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
    $("#czyCitySSText").val($("#provinceName").val());
    $("#czyCitySingleText").val($("#provinceName").val());
    $("#jzzsCitySingleText").val($("#provinceName").val());
    $("#jzzsCitySSText").val($("#provinceName").val());
    
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2001/initDefaultParams",
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
    
    postData.trendType = $("#trendType").val();
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	
	loadNumberTrendChart();
	loadTop10CityNumber();
	loadTop10CityPer();
	
	loadYJKCZYDetailSingle();
	loadYJKCZYDetailSS();
	
	loadJzzsSingle();
	loadJzzsSS();
	
	loadCzyCityList();
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	
	loadNumberTrendChart();
	loadTop10CityNumber();
	loadTop10CityPer();
	
	loadYJKCZYDetailSingle();
	loadYJKCZYDetailSS();
	
	loadJzzsSingle();
	loadJzzsSS();
	
	$("#YJKCZYDetail_ss").click();
	$("#YJKCZYDetail_single").click();
	$("#jzzs_single").click();
	$("#jzzs_ss").click();
	loadTop10CityPer();
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
 * 加载排名图形组件
 * 
 */
function loadNumberTrendChart() {
    var postData = getSumQueryParam();
    var id = "";
    if(postData.trendType=="single"){
    	id="trendlineChartSingle";
    }else{
    	id="trendlineChartSS";
    }
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2001/getYJKNumberTrend",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            if(postData.trendType=="single"){
            	loadAuditConclusion("auditConclusionSingle",data.maxMap,postData,"单时",data.avgMap);
            }else{
            	loadAuditConclusion("auditConclusionSS",data.maxMap,postData,"单秒",data.avgMap);
            }
        }
    });
} 
/**
 * 计算激活有价卡数量趋势审计结论
 * @param data
 * @param postData
 */
function loadAuditConclusion(id,data,postData,type,avgNum){
	var text = "";
	 if(data!=null){
		 text = type+":" + timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+ "月均批量激活有价卡（"+type+"）的数量为"+formatCurrency(avgNum.avg_yjk_cnt,true)+"张，其中在"+timeToChinese(data.aud_trm) +"，批量激活有价卡（"+type+"）的数量达到" + formatCurrency(data.max_yjk_cnt,false)
			+ "张，高于平均值" + data.highAvg + "。";
	 }else{
		 text = type+":" + timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + provinceName(postData.provId) +"无数据。";
	 }
	$('#'+id).html(text);	
}

/**
 * 批量激活有价卡数量前十地市
 */
function loadTop10CityNumber(){
	var postData = getSumQueryParam();
	var id = "";
	if (postData.trendType == "single") {
		id = "top10CityChartSingle";
	} else {
		id = "top10CityChartSS";
	}

	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2001/getYJKTop10CityNumber",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			drawYJKTop10CityNumber(id, data.data);
			
			if (postData.trendType == "single") {
				loadTop10CityConclusion("top10CityConclusionSingle", data.data,postData, "单时");
			} else {
				loadTop10CityConclusion("top10CityConclusionSS", data.data,postData, "单秒");
			}
		}
	});
}
/**
 * 计算激活有价卡数量趋势审计结论
 * @param data
 * @param postData
 */
function loadTop10CityConclusion(id,data,postData,type){
	var provinceCode = postData.provId;
	var text = "";
	var top3city="";
	var prvdName = "";
	var yjkNumber="";
    $.each(data, function(i, obj) {
    	top3city = top3city+obj.CMCC_PRVD_NM_SHORT+""+formatCurrency(obj.SUM_YJK_CNT,false)+"张;";
    	prvdName = obj.SHORT_NAME;
    	yjkNumber = obj.SUM_YJK_CNT;
    	i++;
    	if(i==3){
    		return false;
    	}
    });
    if(data.length!=0){
    	
    	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300" ){
    		text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+prvdName+"批量激活有价卡（"+type+"）的数量为"+formatCurrency(yjkNumber,false)+"张。";
    	}else{
    		text = type+"："+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+prvdName+"各地市批量激活有价卡（"+type+"）的数量横向对比，其中批量激活数量排名前三的地市："+top3city;
    	}
    }else{
    	text = type+"："+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+provinceName(postData.provId)+"无数据。";
    }
	$('#'+id).html(text);	
}
/**
 * 批量激活有价卡数量占比前十地市
 */
function loadTop10CityPer(){
	var postData = getSumQueryParam();
	var id = "";
	if (postData.trendType == "single") {
		id = "top10CityPerChartSingle";
	} else {
		id = "top10CityPerChartSS";
	}

	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2001/getYJKTop10CityPer",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			drawYJKTop10CityPerNumber(id, data.data,data.avgMap);
			
			if (postData.trendType == "single") {
				loadTop10CityPerConclusion("top10CityPerConclusionSingle", data.data,postData, "单时",data.avgMap);
			} else {
				loadTop10CityPerConclusion("top10CityPerConclusionSS", data.data,postData, "单秒",data.avgMap);
			}
		}
	});
}
/**
 * 计算激活有价卡数量占比趋势审计结论
 * @param data
 * @param postData
 */
function loadTop10CityPerConclusion(id,data,postData,type,avgMap){
	var provinceCode = postData.provId;
	var text = "";
	var hgihcity="";
	var prvdName = "";
    $.each(data, function(i, obj) {
    	prvdName=obj.SHORT_NAME;
    	if(obj.AVG_PER_CNT>avgMap.AVG_PER_CNT){
    		hgihcity +=obj.CMCC_PRVD_NM_SHORT+";";
    	}
    });
    if(data.length!= 0){
    	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300" ){
    		text = type+"："+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+prvdName+"批量激活有价卡（"+type+"）的比例为"+formatCurrency(data[0].AVG_PER_CNT*100,true)+"%。";
    	}else{
    		text = type+"："+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+prvdName+"各地市批量激活有价卡（"+type+"）的比例与全省平均值对比，其中批量激活比例高于全省平均值的地市："+hgihcity;
    	}
    }else{
    	text = type+"："+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+provinceName(postData.provId)+"无数据。";
    }
    
	$('#'+id).html(text);	
}

/**
 * 有价卡操作员明细信息汇总(单时)
 */
function loadYJKCZYDetailSingle(){
	var postData = getSumQueryParam();
	postData.cityId = $("#czyCitySingle").val();
	
	var id = "YJKCZYDetailSingle";
	var conId = "YJKCZYDetailConclusionSingle";
	var type="单时";
	postData.trendType="single";
	postData.loadType="czyDetail";
	drawYJKCZYCharts(id,postData,conId,type);
}
/**
 * 有价卡操作员明细信息汇总(单秒)
 */
function loadYJKCZYDetailSS(){
	var postData = getSumQueryParam();
	postData.cityId = $("#czyCitySS").val();
	
	var id = "YJKCZYDetailSS";
	var conId = "YJKCZYDetailConclusionSS";
	var type="单秒";
	postData.trendType="ss";
	postData.loadType="czyDetail";
	drawYJKCZYCharts(id,postData,conId,type);
}

/**
 * 有价卡集中赠送信息汇总(单时)
 */
function loadJzzsSingle(){
	var postData = getSumQueryParam();
	postData.cityId = $("#jzzsCitySingle").val();
	var id = "JzzsChartsSingle";
	var conId = "JzzsConclusionSingle";
	var type="单时";
	postData.trendType="single";
	postData.loadType="jzzs";
	drawYJKCZYCharts(id,postData,conId,type);
}
/**
 * 有价卡集中赠送信息汇总(单秒)
 */
function loadJzzsSS(){
	var postData = getSumQueryParam();
	postData.cityId = $("#jzzsCitySS").val();
	var id = "JzzsChartsSS";
	var conId = "JzzsChartsConclusionSS";
	var type="单秒";
	postData.trendType="ss";
	postData.loadType="jzzs";
	drawYJKCZYCharts(id,postData,conId,type);
}


/**
 * 绘制图表
 * @param id
 * @param postData
 */
function drawYJKCZYCharts(id,postData,conId,type){
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2001/getYJKCZYTop10Detail",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			if(postData.loadType=="czyDetail"){
				drawYJKCZYDetail(id, data.data);
				loadYJKCZYDetailSingleConclusion(conId,data.data,postData,type);
				return;
			}
			
			if(postData.loadType=="jzzs"){
				drawJzzs(id, data.data);
				loadJzzsSingleConclusion(conId,data.data,postData,type);
			}
		}
	});
}

/**
 * 有价卡操作员明细审计结论
 * @param data
 * @param postData
 */
function loadYJKCZYDetailSingleConclusion(id,data,postData,type){
	var text = "";
	var top3czy="";
	var YJKCZYPrvdName = "";
	if(type=="单时"){
		YJKCZYPrvdName = $("#czyCitySingleText").val();
	}else{
		YJKCZYPrvdName = $("#czyCitySSText").val();
	}
	
    $.each(data, function(i, obj) {
    	top3czy = top3czy+"员工"+obj.NM+"、激活有价卡"+formatCurrency(obj.SUM_YJK_CNT,false)+"张，涉及营销案的有价卡"+formatCurrency(obj.SUM_ZS_YJK_CNT,false)+"张，营销活动向非中高端、非集团客户赠送有价卡"+formatCurrency(obj.SUM_DZKH_ZS_NUM,false)+"张;";
    	i++;
    	if(i==3){
    		return false;
    	}
    });
    if(data.length != 0){
    	
    	text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+YJKCZYPrvdName+"批量激活有价卡（"+type+"）的数量前十的操作员统计，其中批量激活数量排名前三的操作员："+top3czy;
    }else{
    	text =  type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+YJKCZYPrvdName+"无数据。";
    }
	$('#'+id).html(text);	
}

/**
 * 有价卡操作员明细审计结论(数据表)
 */
function loadYJKCZYDetailTableCon(id,type,YJKCZYPrvdName){
	var postData = getSumQueryParam();
	var text = "";
    text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+YJKCZYPrvdName+"批量激活有价卡（"+type+"）的数量、金额、赠送有价卡数量、营销活动向非中高端、非集团客户赠送有价卡的明细信息统计.";
    $('#'+id).html(text);	
}

/**
 * 有价卡集中赠送审计结论
 * @param data
 * @param postData
 */
function loadJzzsSingleConclusion(id,data,postData,type){
	var text = "";
	var top3czy="";
	var YJKCZYPrvdName = "";
	if(type=="单时"){
		YJKCZYPrvdName = $("#jzzsCitySingleText").val();
	}else{
		YJKCZYPrvdName = $("#jzzsCitySSText").val();
	}

	if(data.length!=0){
		
		$.each(data, function(i, obj) {
			var PRO_JZZS = ((obj.SUM_PLJH_ZS_NUM/obj.SUM_YJK_CNT)*100).toFixed(2);
			var PRO_JZCZ = ((obj.SUM_JZZS_CZ_NUM/obj.SUM_PLJH_ZS_NUM)*100).toFixed(2);
			if(PRO_JZCZ==""||PRO_JZCZ==0.00||PRO_JZCZ=="NaN"){
				PRO_JZCZ = 0.00;
			}
			top3czy = top3czy+"员工"+obj.NM+"、批量激活有价卡"+formatCurrency(obj.SUM_YJK_CNT,false)+"张，其中涉及集中赠送的有价卡"+formatCurrency(obj.SUM_PLJH_ZS_NUM,false)+"张，比例为"+PRO_JZZS+"%,集中赠送有价卡中涉及集中充值有价卡"+formatCurrency(obj.SUM_JZZS_CZ_NUM,false)+"张,比例为"+PRO_JZCZ+"%;";
			i++;
			if(i==3){
				return false;
			}
		});
		text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+YJKCZYPrvdName+"批量激活有价卡（"+type+"）的数量前十的操作员统计，其中批量激活数量排名前三的操作员："+top3czy;
	}else{
		text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+YJKCZYPrvdName+"无数据。";
		
	}
	$('#'+id).html(text);	
}

/**
 * 有价卡集中赠送审计结论(数据表)
 */
function loadJzzsTableCon(id,type,prvdName){
	var postData = getSumQueryParam();
	var text = "";
    text = type+":"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+prvdName+"批量激活有价卡（"+type+"）的数量、金额、批量激活的有价卡涉及集中赠送的有价卡数量、集中赠送的有价卡涉及集中激活的有价卡数量的明细信息统计。";
    $('#'+id).html(text);	
}



/**
 * 获取明细地市列表
 * 
 */
function loadCityDetailGridData() {
	 if ($("#cityDetailGridData").html() != "") {
	        return;
	    }
	    
	    var postData = getDetailQueryParam();
	    
	    var tableColNames = [ '审计月','地市名称', '激活日期', '操作员姓名', '有价卡序列号', '金额（元）', '获赠有价卡的手机号', '平均 月ARPU（元）', '营销案名称', '被充值手机号码' ];
	    var tableColModel = [];
	    
	    tableMap = new Object();
	    tableMap.name = "aud_trm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cmcc_prvd_nm_short";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "opr_dt";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "nm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "yjk_ser_no";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "yjk_amt";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue,true);
	    };
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "msisdn";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);

	    tableMap = new Object();
	    tableMap.name = "send_avg_arpu";
	    tableMap.align = "center";
	    tableMap.formatter = function(cellvalue, options, rowObject) {
	        return formatCurrency(cellvalue,true);
	    };
	    tableColModel.push(tableMap);
	    
	    
	    tableMap = new Object();
	    tableMap.name = "offer_nm";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    tableMap = new Object();
	    tableMap.name = "cz_msisdn";
	    tableMap.align = "center";
	    tableColModel.push(tableMap);
	    
	    jQuery("#cityDetailGridData").jqGrid({
	        url: $.fn.cmwaurl() + "/yjk/2001/getCityDetailPagerList",
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
	        pager : "#cityDetailGridDataPageBar",
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
	            $("#cityDetailGridData").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	            $("#cityDetailGridData").setGridWidth($(".tab-nav").width()-40);
	            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width() - 2);
	        }
	    });
}



/**
 * 刷新明细地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/yjk/2001/getCityDetailPagerList";
    
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 绘制数量趋势图
 * 
 * @param id 绘制id
 */
function drawRankLineChart(id, data,avgMap) {
	 	var aud_trm = [];
	    var yjk_cnt  = [];
	    var avg_yjk_cnt = [];
	    $.each(data, function(i, obj) {
	    	aud_trm.push(obj.aud_trm);
	    	yjk_cnt.push(obj.yjk_cnt);
	    	avg_yjk_cnt.push(Number(avgMap.avg_yjk_cnt.toFixed(2)));
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
	        	categories: aud_trm,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: '批量激活有价卡数量',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	            labels : {
					format : '',
				},
	        }],
	        tooltip: {
	            shared: true
	        },
	        legend: {
	            enabled: true
	        },
	        series: [{
	            name: '批量激活有价卡数量',
	            type: 'spline',
	            color:'#65d3e3',
	            data: yjk_cnt,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '批量激活有价卡月平均数量',
	            type: 'spline',
	            color:'#df5438',
	            data: avg_yjk_cnt,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
	    });
	    
}

/**
 * 
 * @param id 绘制ID
 * @param x 阈值x
 * @param y 阈值y
 * @param data 数据集
 */
function drawCityMapChart(id, x, y, data) {
    var postData = getSumQueryParam();
    var mapFileName;
    var provList = $.fn.provList();
    
    // 根据省份ID找到要加载的地图文件
    $.each(provList, function(i, obj) {
       if (obj.provId == postData.provId) {
           mapFileName = obj.fieldName.toLowerCase();
           
           return false;
       }
    });
    
    $.ajax({
        url:  $.fn.cmwaurl() + "/resource/js/highcharts/maps/" + mapFileName + ".geo.json",
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function(json) {
            var mapData = Highcharts.geojson(json);
            
            $.each(mapData, function(i, map) {
                var mapCode = map.properties.code;
                map.value = 0;
                
                $.each(data, function(j, obj) {
                    if (mapCode == obj.cmcc_prov_id) {
                        map.value = (obj.yjk_cnt / obj.cnt_tol * 100).toFixed(2);
                        map.tips1 = obj.yjk_cnt;
                        map.tips2 = obj.amt_sum;
                    }
                });
            });
            
            $('#' + id).highcharts('Map', {
                chart : {
                    backgroundColor: 'rgba(0,0,0,0)'
                },
                title : {
                    text : null
                },
                legend : {
                    enabled: true
                },
                tooltip : {
                    formatter : function() {
                        if (this.point.value == 0) {
                            return "没有数据";
                        }
                        
                        var tipsInfo = "<b>{0}</b><br/>批量激活有价卡卡数:{1}<br/>批量激活有价卡金额（元）:{2}<br/>批量激活有价卡数量占比:{3}%";
                        return tipsInfo.format(this.point.name, 
                                formatCurrency(this.point.tips1, false), 
                                formatCurrency(this.point.tips2, true), 
                                formatCurrency(this.point.value, true));
                    }
                },
                colorAxis : {
                    dataClasses : [
                        {color : "#DDDDDD", from : -100, to : 0.001, name : "没有数据"},
                        {color : "#F2CA68", from : 0.001, to : x, name : "批量激活有价卡数量占比 < X"},
                        {color : "#65D3E3", from : x, to : y + 0.001, name : "批量激活有价卡数量占比在X - Y之间"},
                        {color : "#FF7979", from : y + 0.001, name : "批量激活有价卡数量占比 > Y"}
                    ]
                },
                series : [{
                    animation: {
                        duration: 1000
                    },
                    data : mapData,
                    dataLabels: {
                        enabled: true,
                        color: '#000',
                        allowOverlap : true,
                        format: '{point.name}'
                    }
                }]
            });
        }
    });
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
 * 加载操作员地市字段列表
 * 
 */
function loadCzyCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
            $.each(data.body.ctyList, function(i, map) {
                li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
               
            });
            
            $("#czyCitySingleSelect").append(li);
            $("#czyCitySSSelect").append(li);
            $("#jzzsCitySingleSelect").append(li);
            $("#jzzsCitySSSelect").append(li);
            
            addCzySingleSelectEvent("czyCitySingle");
            addCzySSSelectEvent("czyCitySS");
            
            addJzzsSingleEvent("jzzsCitySingle");
            addJzzsSSEvent("jzzsCitySS");
        }
    });
}
/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addCzySingleSelectEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
	    $("#trendType").val("single");
	    loadYJKCZYDetailTableCon("YJKCZYDetailTableConclusionSingle","单时",$("#" + id + "Text").val());
	    reloadYJKCZYDetailTable("YJKCZYDetailSingleTable",$(this).attr("code"));
	    loadYJKCZYDetailSingle();
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}
/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addCzySSSelectEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
	    $("#trendType").val("ss");
	    loadYJKCZYDetailTableCon("YJKCZYDetailTableConclusionSS","秒",$("#" + id + "Text").val());
	    reloadYJKCZYDetailTable("YJKCZYDetailSSTable",$(this).attr("code"));
	    loadYJKCZYDetailSS();
        
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}
/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addJzzsSingleEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
	    $("#trendType").val("single");
    	loadJzzsTableCon("JzzsSingleTableConclusion","单时",$("#jzzsCitySingleText").val());
    	reloadJzzsTable("JzzsSingleTable",$(this).attr("code"));
	    loadJzzsSingle();
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

/**
 * 为指定ID添加下拉框监听事件
 * @param id
 */
function addJzzsSSEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
	    $("#trendType").val("ss");
	    loadJzzsTableCon("JzzsSSTableConclusion","单秒",$("#jzzsCitySSText").val());
    	reloadJzzsTable("JzzsSSTable",$(this).attr("code"));
	    loadJzzsSS();
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addSelectEvent(id, callback) {
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
 * 绘制有价卡激活数量前十10地市
 * 
 * @param id 绘制id
 * @param xValue x轴值
 * @param yValue y轴值
 */
function drawYJKTop10CityNumber(id, data) {
	 	var CMCC_PRVD_NM_SHORT = [];
	    var SUM_YJK_CNT  = [];
	    $.each(data, function(i, obj) {
	    	CMCC_PRVD_NM_SHORT.push(obj.CMCC_PRVD_NM_SHORT);
	    	SUM_YJK_CNT.push(obj.SUM_YJK_CNT);
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
	            title: {
	                text: '累计批量激活有价卡数量',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	        }],
	        tooltip: {
	            shared: true
	        },
	        legend: {
	            enabled: true
	        },
	        series: [{
	            name: '累计批量激活有价卡数量',
	            type: 'column',
	            color:'#65d3e3',
	            data: SUM_YJK_CNT,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
	    });
}

/**
 * 绘制有价卡激活数量占比前十10地市
 * 
 * @param id 绘制id
 * @param xValue x轴值
 * @param yValue y轴值
 */
function drawYJKTop10CityPerNumber(id, data,avgMap) {
	 	var CMCC_PRVD_NM_SHORT = [];
	    var PER_CNT  = [];
	    var AVG_PER_CNT = [];
	    $.each(data, function(i, obj) {
	    	CMCC_PRVD_NM_SHORT.push(obj.CMCC_PRVD_NM_SHORT);
	    	PER_CNT.push(Number((obj.AVG_PER_CNT*100).toFixed(2)));
	    	AVG_PER_CNT.push(Number((avgMap.AVG_PER_CNT*100).toFixed(2)));
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
	            title: {
	                text: '累计批量激活有价卡的比例',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	        }],
	        tooltip: {
	            shared: true
	        },
	        legend: {
	            enabled: true
	        },
	        series: [{
	            name: '批量激活有价卡数量占比',
	            type: 'spline',
	            color:'#65d3e3',
	            data: PER_CNT,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: '批量激活有价卡平均数量占比',
	            type: 'spline',
	            color:'#df5438',
	            data: AVG_PER_CNT,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
}


/**
 * 绘制有价卡操作员明细
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawYJKCZYDetail(id, data) {
	 	var NM = [];
	    var SUM_YJK_CNT  = [];
	    var SUM_ZS_YJK_CNT  = [];
	    var SUM_DZKH_ZS_NUM  = [];
	    
	    $.each(data, function(i, obj) {
	    	NM.push(obj.NM);
	    	SUM_YJK_CNT.push(obj.SUM_YJK_CNT);
	    	SUM_ZS_YJK_CNT.push(obj.SUM_ZS_YJK_CNT);
	    	SUM_DZKH_ZS_NUM.push(obj.SUM_DZKH_ZS_NUM);
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
	        	categories: NM,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: '累计批量激活有价卡数量',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	        }],
	        tooltip: {
	            shared: true
	        },
	        legend: {
	            enabled: true
	        },
	        series: [{
	            name: '累计批量激活有价卡数量',
	            type: 'column',
	            color:'#65d3e3',
	            data: SUM_YJK_CNT,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '赠送有价卡数量',
	            type: 'column',
	            color:'#df5438',
	            data: SUM_ZS_YJK_CNT,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '向非中高端、非集团客户赠送有价卡数量',
	            type: 'column',
	            color:'#76e56f',
	            data: SUM_DZKH_ZS_NUM,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
	    });
}
function toDecimal2(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}  
/**
 * 汇总有价卡操作员明细列表
 */
function loadYJKCZYDetailTable(tableId,pagerId,cityId) {
    var postData = getSumQueryParam();
    postData.cityId = cityId;
    var tableColNames = [ '审计月','地市','操作员工号','操作员姓名','操作日期','操作时间','批量激活有价卡数量','批量激活有价卡金额(元)','赠送有价卡数量','营销活动向非中高端、非集团客户赠送有价卡'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_tm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "yjk_cnt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    

    tableMap = new Object();
    tableMap.name = "amt_sum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "zs_yjk_cnt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dzkh_zs_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2001/getYJKCZY_tableDetail",
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
 * 刷新有价卡操作员明细列表
 */
function reloadYJKCZYDetailTable(tableId,cityId) {
	
	var postData = getSumQueryParam(); 
	postData.cityId = cityId;			  
    var url = $.fn.cmwaurl() + "/yjk/2001/getYJKCZY_tableDetail";
    
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function toDecimal2(x) {    
    var f = parseFloat(x);    
    if (isNaN(f)) {    
        return false;    
    }    
    var f = Math.round(x*100)/100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}  

/**
 * 绘制有价集中赠送汇总信息
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawJzzs(id, data) {
	 	var NM = [];
	    var SUM_YJK_CNT  = [];
	    var SUM_PLJH_ZS_NUM  = [];
	    var SUM_JZZS_CZ_NUM  = [];
	    var PRO_JZZS = [];
	    var PRO_JZCZ = [];
	    $.each(data, function(i, obj) {
	    	NM.push(obj.NM);
	    	SUM_YJK_CNT.push(obj.SUM_YJK_CNT);
	    	SUM_PLJH_ZS_NUM.push(obj.SUM_PLJH_ZS_NUM);
	    	SUM_JZZS_CZ_NUM.push(obj.SUM_JZZS_CZ_NUM);
	    	PRO_JZZS.push(Number(((obj.SUM_PLJH_ZS_NUM/obj.SUM_YJK_CNT)*100).toFixed(2)));
	    	PRO_JZCZ.push(Number(((obj.SUM_JZZS_CZ_NUM/obj.SUM_PLJH_ZS_NUM)*100).toFixed(2)));
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
	        	categories: NM,
	            crosshair: true
	        }],
	        yAxis: [{
	            title: {
	                text: '累计批量激活有价卡数量',
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
	                text: '集中赠送和集中充值有价卡比例',
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
	            name: '累计批量激活有价卡数量',
	            type: 'column',
	            color:'#65d3e3',
	            yAxis:0,
	            data: SUM_YJK_CNT,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '集中赠送的有价卡数量',
	            type: 'column',
	            color:'#df5438',
	            yAxis:0,
	            data: SUM_PLJH_ZS_NUM,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '集中充值的有价卡数量',
	            type: 'column',
	            color:'#76e56f',
	            yAxis:0,
	            data: SUM_JZZS_CZ_NUM,
	            tooltip: {
	                valueSuffix: ''
	            }
	        },{
	            name: '集中赠送的有价卡占比',
	            type: 'spline',
	            color:'#11EEEE',
	            yAxis:1,
	            data: PRO_JZZS,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        },{
	            name: '集中充值的有价卡占比',
	            type: 'spline',
	            color:'#B32BD5',
	            yAxis:1,
	            data: PRO_JZCZ,
	            tooltip: {
	                valueSuffix: '%'
	            }
	        }]
	    });
}

/**
 * 有价卡集中赠送汇总列表
 */
function loadJzzsTable(tableId,pagerId,cityId) {
    var postData = getSumQueryParam();
    postData.cityId =cityId;
    var tableColNames = [ '审计月','地市','操作员工号','操作员姓名','操作日期','操作时间','批量激活有价卡数量','批量激活有价卡金额(元)','批量激活的有价卡涉及集中赠送的有价卡数量',"集中赠送的有价卡涉及集中充值的有价卡数量"];
    
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.align = "center";
    tableMap.name = "aud_trm";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "opr_tm";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_cnt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    

    tableMap = new Object();
    tableMap.name = "amt_sum";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pljh_zs_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "jzzs_cz_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2001/getYJKCZY_tableDetail",
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
 * 刷新有价卡操集中赠送明细
 */
function reloadJzzsTable(tableId,cityId) {
	var postData = getSumQueryParam(); 
	postData.cityId = cityId;
    var url = $.fn.cmwaurl() + "/yjk/2001/getYJKCZY_tableDetail";
    
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}