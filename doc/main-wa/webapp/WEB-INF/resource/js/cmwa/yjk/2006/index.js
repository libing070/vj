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
    $("#ranklineChartBig").css({width: "80%", height: "85%"});
    $("#cityMapChartBig").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
}

function initEvent() {
    
	 //设置数据源类型
    $("#bossType").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val('boss');
    	$("#typeName").val('总部有价卡赠送BOSS数据');
    	if (!hzFormValidator()) {
            return false;
        }
        
        reloadGlobalData();
    });
    //设置数据源类型
    $("#manuType").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#trendType").val('manu');
    	$("#typeName").val('有价卡赠送手工电子台账数据');
    	if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    //集中度分析列表
    $("#focusNumber").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadFocusTable('focusTable','focusTablePageBar');
    });
    //赠送员工分析列表
    $("#staffTab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadStaffTable('staffTable','staffTablePageBar');
    });
    //导出赠送员工分析列表
    $("#exportstaffTab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	var totalNum = $("#staffTable").getGridParam("records");
         
         if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
             return;
         }
         exportTab("exportStaff");
    });
    //导出赠送集中度分析数据表
    $("#exportFocusTab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	var totalNum = $("#focusTable").getGridParam("records");
         
         if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
             return;
         }
         exportTab("exportFocusTab");
    });
    
    
    
    //导出大明细数据
    $("#exportDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	if (!mxFormValidator()) {
            return false;
        }
    	var totalNum = $("#cityDetailGridData").getGridParam("records");
         
         if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
             return;
         }
         exportDetailTab("exportFzgFjtDetail");
    });
    
    /**
     * 导出
     */
    function exportTab(functionName){
        var hz_rankType = parseInt($("#hz_rankType").val());
        var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
        var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
        var provId = $("#provinceCode").val();
        var trendType=$("#trendType").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjk/2006/" +functionName+
		"?hz_rankType="+hz_rankType +
		"&hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&trendType="+trendType;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    /**
     * 导出
     */
    function exportDetailTab(functionName){
        var hz_rankType = parseInt($("#hz_rankType").val());
        var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
        var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
        var provId = $("#provinceCode").val();
        var trendType=$("#detailtrendType").val();
        var mx_cityCode = $("#mx_cityCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjk/2006/" +functionName+
		"?hz_rankType="+hz_rankType +
		"&hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&trendType="+trendType+
		"&mx_cityCode="+mx_cityCode;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    
    //赠送非中高端非集团客户有价卡列表
    $("#fzgfjtRanking").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadFzgfjtTable('fzgfjtTable','fzgfjtTablePageBar');
    }); 
    
    $("#exportFzgfjtTab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	 var totalNum = $("#fzgfjtTable").getGridParam("records");
         
         if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
             return;
         }
         exportTab("exportFzgfjtTab");
    });
    
    
    $("#exportPurposeTab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

   	 var totalNum = $("#purposeTable").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        exportTab("");
   });
    
    
    //设置数据源类型
    $("#detailbossType").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#detailtrendType").val('boss');
    	loadFzgFjtDetail("cityDetailGridData","cityDetailGridDataPageBar");
    });
    //设置数据源类型
    $("#detailmanuType").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	$("#detailtrendType").val('manu');
    	loadFzgFjtDetail("cityDetailGridData","cityDetailGridDataPageBar");
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
        loadFzgFjtDetail("cityDetailGridData","cityDetailGridDataPageBar");
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
        url : $.fn.cmwaurl() + "/yjk/2006/initDefaultParams",
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
    postData.hz_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_yxaFlag = $("#mx_yxaFlag").val();
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.trendType = $("#detailtrendType").val();
    
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	
	loadSumMoney();
	loadYJKReal();
	loadYJKFocus();
	loadFzgfjtCharts();
	loadSumPurpose();
	loadSumStaff();

	
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	
	loadSumMoney();
	loadYJKReal();
	loadYJKFocus();
	loadFzgfjtCharts();
	loadSumPurpose();
	loadSumStaff();

	
//	loadTop10CityPer();
}
/**
 * 加载有价卡真实性
 */
function loadYJKReal(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2006/getYJKRealInfo",
		dataType : 'json',
		data : postData,
		success : function(data) {
			var pieData = [];
			var sumDocument =0;
			var y = 0;
			var n = 0;
			$.each(data, function(i, obj) {
				var eachArray = [];
				eachArray.push(obj.exist_word);
				eachArray.push(obj.sum_yjk_amt);
				pieData.push(eachArray);
				sumDocument = sumDocument+Number(obj.sum_yjk_amt);
				if(obj.exist_word=="无公文号"){
					n =  obj.sum_yjk_amt;
				}
				if(obj.exist_word=="有公文号"){
					y =  obj.sum_yjk_amt;
				}
			});
			$("#nDocument").html(n.toFixed(2));
			$("#yDocument").html(y.toFixed(2));
			$("#sumDocument").html(sumDocument.toFixed(2));
			loadRealCon(n,postData);
			pieHighChart("#pieReal","赠送真实性分析",pieData);
		}
	});
}

/**
 * 有价卡赠送真实性结论
 */
function loadRealCon(x,postData){
	var text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"的"+$("#typeName").val()+"，发现约"+x.toFixed(2)+"元有价卡赠送未记录批复文件公文号，无法保证有价卡赠送的真实性。";
	$("#pieRealCon").html(text);
}
/**
 * 加载赠送集中度分析
 */
function loadYJKFocus(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2006/getYJKFocus",
		dataType : 'json',
		data : postData,
		success : function(data) {
			var map = data[0];
			var amtArray = [];
			var countArray = [];
			amtArray.push(map.sum_yjk_amt_10000);
			amtArray.push(map.sum_yjk_amt_20000);
			amtArray.push(map.sum_yjk_amt_50000);
			amtArray.push(map.sum_yjk_amt_100000);
			amtArray.push(map.sum_yjk_amt_other);
			countArray.push(map.count_yjk_amt_10000);
			countArray.push(map.count_yjk_amt_20000);
			countArray.push(map.count_yjk_amt_50000);
			countArray.push(map.count_yjk_amt_100000);
			countArray.push(map.count_yjk_amt_other);
			var name=['1万以下','1万-2万','2万-5万','5万-10万','10万以上'];
			drawDoubleHistogram("histogramFocus",name,amtArray,countArray);
			loadFocusCon('histogramFocusCon',postData,map.count_yjk_amt_other,map.sum_yjk_amt_other);
		}
	});
}
/**
 * 有价卡赠送真实性结论
 */
function loadFocusCon(id,postData,count,amt){
	var text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"的"+$("#typeName").val()+"，发现部分客户累计有价卡赠送金额较大。其中，赠送有价卡总金额超过10万元的客户有"+count+"个，涉及金额"+amt.toFixed(2)+"元。";
	$("#"+id).html(text);
}

/**
 * 赠送非中高端客户、非集团客户
 */
function loadFzgfjtCharts(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2006/getZsfzgfjtCustomer",
		dataType : 'json',
		data : postData,
		success : function(data) {
			loadFzgfjtCon("fzgfjtCon",data.yjkAmt,postData);
			drawHistogram("fzgfjtCharts",data);
		}
	});
};

/**
 * 赠送非中高端客户、非集团客户审计结论
 */
function loadFzgfjtCon(id,map,postData){
	var text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"的"+$("#typeName").val()+"，发现存在向非中高端、非集团客户赠送有价卡的情况，共涉及金额"+map.sum_yjk_num.toFixed(2)+"元。按照违规赠送有价卡金额排名前5的营销案如下图所示。";
	$("#"+id).html(text);
}

/**
 * 构建赠送用途分析
 */
function loadSumPurpose(){
    var postData = getSumQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2006/getSumPurpose",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var xdata = [];
        	var ydata = [];
        	var sumAmt = 0;
        	$.each(data, function(i, obj) {
        		xdata.push(obj.yjk_purpose);
        		ydata.push(obj.sum_yjk_amt);
        		sumAmt = sumAmt+obj.sum_yjk_amt;
        		
        	});
        	
        	loadSumPurposeCon("purposeCon",sumAmt,postData);
        	drawPurposeHistogram("purposeCharts",xdata,ydata);
        	loadPurposeTable("purposeTableAjax",data,postData);
        }
    });	
}

/**
 * 构建赠送用途审计结论
 * @param id
 * @param sumAmt
 * @param postData
 */
function loadSumPurposeCon(id,sumAmt,postData){
	var text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"的"+$("#typeName").val()+"，发现存在以有价卡赠送方式支付租金、渠道酬金、员工福利的情况，共涉及金额"+sumAmt.toFixed(2)+"元.";
	$("#"+id).html(text);
}


/**
 * 赠送用途分析数据表
 */
function loadPurposeTable(tableId,data,postData) {
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>审计月</th>" +
			"<th>有价卡用途</th>" +
			"<th>营销案编号</th>" +
			"<th>营销案名称</th>" +
			"<th>有价卡金额(元)</th></tr>";
	$.each(data, function(i, obj) {
		htmlstr = htmlstr+"<tr><td>"+postData.hz_startMonth+"-"+postData.hz_endMonth+"</td>";
		htmlstr = htmlstr+"<td>"+obj.yjk_purpose+"</td>";
		htmlstr = htmlstr+"<td>"+obj.yjk_offer_cd+"</td>";
		htmlstr = htmlstr+"<td>"+obj.yjk_offer_nm+"</td>";
		htmlstr = htmlstr+"<td>"+obj.sum_yjk_amt.toFixed(2)+"</td>";
		htmlstr = htmlstr+"</tr>";
	});
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
}

/**
 * 加载赠送员工分析
 */
function loadSumStaff(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2006/getSumStaff",
		dataType : 'json',
		data : postData,
		success : function(data) {
			var map = data;
			var amtArray = [];
			var countArray = [];
			amtArray.push(map.sum_yjk_amt_10000);
			amtArray.push(map.sum_yjk_amt_20000);
			amtArray.push(map.sum_yjk_amt_50000);
			amtArray.push(map.sum_yjk_amt_100000);
			amtArray.push(map.sum_yjk_amt_other);
			countArray.push(map.count_yjk_amt_10000);
			countArray.push(map.count_yjk_amt_20000);
			countArray.push(map.count_yjk_amt_50000);
			countArray.push(map.count_yjk_amt_100000);
			countArray.push(map.count_yjk_amt_other);
			var name=['1万以下','1万-2万','2万-5万','5万以-10万','10万以上'];
			drawDoubleHistogram("staffCharts",name,amtArray,countArray);
			loadStaffCon('staffCon',postData,map.sum_yjk_amt_10000,map.count_yjk_amt_10000);
		}
	});
}

/**
 * 有价卡赠送真实性结论
 */
function loadStaffCon(id,postData,amt,count){
	var text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"的"+$("#typeName").val()+"，关联员工记录，发现存在向员工赠送大额有价卡的情况。其中，赠送有价卡总金额超过1万元的员工人数有"+count+"人，涉及金额"+amt.toFixed(2)+"元。";
	$("#"+id).html(text);
}

/**
 * 构建赠送金额汇总
 */
function loadSumMoney(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjk/2006/getSumMoney",
		dataType : 'json',
		data : postData,
		success : function(data) {
			var odsSum = data.odsSum;
			var sum_yjk_amt = data.sum_yjk_amt;
			if(odsSum>sum_yjk_amt){
				var pieData = [];
				pieData.push(['总部数据与财务数据差异金额',odsSum-sum_yjk_amt]);
				pieData.push(['财务数据金额',sum_yjk_amt]);
				pieHighChart("#moneyCharts","赠送金额汇总分析",pieData);
				loadMoneyCon('moneyCon',postData,odsSum,sum_yjk_amt);
			}else{
				var pieData = [];
				pieHighChart("#moneyCharts","赠送金额汇总分析",pieData);
				loadMoneyCon('moneyCon',postData,odsSum,sum_yjk_amt);
			}
			loadMoneyTable('moneyTable',data.dataList,postData);
		}
	});
}
/**
 * 赠送金额数据表
 */
function loadMoneyTable(tableId,data,postData) {
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>审计月</th>" +
			"<th>科目</th>" +
			"<th>本月累计(元)</th>" +
			"<th>赠送有价卡面值(元)</th>";
	$.each(data, function(i, obj) {
		htmlstr = htmlstr+"<tr><td>"+postData.hz_startMonth+"-"+postData.hz_endMonth+"</td>";
		htmlstr = htmlstr+"<td>"+obj.name+"</td>";
		htmlstr = htmlstr+"<td>"+obj.yjk_amt_sum.toFixed(2)+"</td>";
		htmlstr = htmlstr+"<td>"+obj.odsSum.toFixed(2)+"</td>";
		htmlstr = htmlstr+"</tr>";
	});
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
}
/**
 * 构建赠送金额汇总审计结论
 * @param x
 * @param postData
 * @param amt
 * @param count
 */
function loadMoneyCon(id,postData,odsSum,sum_yjk_amt){
	if(odsSum>sum_yjk_amt){
	var chayi = ((odsSum-sum_yjk_amt)/odsSum*100).toFixed(2);
	var text = "审阅" + $("#provinceName").val() + "在"
			+ timeToChinese(postData.hz_startMonth) + "-"
			+ timeToChinese(postData.hz_endMonth) + "，"
			+ $("#provinceName").val() + "财务系统记录赠送有价卡"+odsSum.toFixed(2)+"元，" + $("#typeName").val()
			+ "记录赠送有价卡" + sum_yjk_amt.toFixed(2) + "元，差异金额" + (odsSum - sum_yjk_amt).toFixed(2)
			+ "元，占到财务赠送有价卡金额的"+chayi+"%。";
	if(chayi>30){
		text = text+"由于总部数据与财务数据差异大于30%，请上传有价卡赠送手工电子台账用于进一步分析。";
	}
		$("#"+id).html(text);
	}else{
		$("#"+id).html("暂无审计结论");
	}
}
/**
 * 加载大明细列表数据
 */
function loadFzgFjtDetail(tableId,pagerId){
	var postData = getDetailQueryParam();
	  
    var tableColNames = ['审计月','地市名称','有价卡序列号','有价卡赠送时间','有价卡类型','赠送有价卡金额(元)','获赠有价卡到期时间','获赠有价卡的用户标识','获赠有价卡的客户标识','获赠有价卡的手机号','平均月ARPU','有价卡赠送依据','营销案编号','营销案名称','有价卡赠送文号说明','赠送渠道标识','赠送渠道名称'];
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
    tableMap.name = "yjk_ser_no";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_pres_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_typ";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_amt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_end_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cust_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
   
    tableMap = new Object();
    tableMap.name = "msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "send_avg_arpu";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_dependency";
    tableMap.align = "center";
    tableColModel.push(tableMap);
  
    tableMap = new Object();
    tableMap.name = "yjk_offer_cd";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_offer_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "offer_word";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cor_chnl_typ";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cor_chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2006/getFzgFjtDetail",
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

function reLoadCityDetailGridData(tableId) {
	var postData = getDetailQueryParam();
   
    var url = $.fn.cmwaurl() + "/yjk/2006/getFzgFjtDetail";
    
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

//饼状图
function pieHighChart(Id,name,data){
	$(Id).highcharts({
        title: {
            text: ''
        },
        chart:{
        	height:300
        },
        tooltip: {
        	 formatter: function() {
                 return '<b>'+ this.point.name +'</b>: '+ 
                              Highcharts.numberFormat(this.y, 2) +'元';
              }
        },
        colors:['#FF0000','#0997F7'],
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
            name: name,
            innerSize:'50%',
            data: data
        }]
    });
}
/**
 * 双柱状图
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawDoubleHistogram(id, xdata,ydata1,ydata2) {
	    Highcharts.setOptions({
	        lang: {
	            numericSymbols: null
	        }
	    });
	    $('#'+id).highcharts({
			chart: {
	            zoomType: 'xy',
	            defaultSeriesType: 'line',
	            height:300
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
	        	labels : {
					format : '{value}元',
				//enabled: false 
				},
	            title: {
	                text: '赠送金额(元)',
	                style: {
	                    color:Highcharts.getOptions().colors[1],
	                    fontFamily:'微软雅黑',
	                    fontSize:'16px'
	                }
	            },
	           
	        },{
	        	labels : {
					format : '{value}',
				//enabled: false 
				},
	            title: {
	                text: '号码数量',
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
	            name: '赠送金额(元)',
	            type: 'column',
	            color:'#11EEEE',
	            yAxis:0,
	            data: ydata1,
	            tooltip: {
	                valueSuffix: '元'
	            }
	        },{
	            name: '号码数量',
	            type: 'column',
	            color:'#FF00FF',
	            yAxis:1,
	            data: ydata2,
	            tooltip: {
	                valueSuffix: ''
	            }
	        }]
	    });
}

/**
 * 有价卡集中赠送集中度分析
 */
function loadFocusTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月','赠送号','类型','集团客户名称','赠送金额(元)'];
    
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
    tableMap.name = "msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_type";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "org_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return cellvalue.toFixed(2);
    };
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2006/getYJKFocusInfo",
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
 * 柱状图
 * 
 * @param id 绘制id
 * @param data 数据
 */
function drawHistogram(id, data) {
		var xdata = [];
		var ydata = [];
		$.each(data.dataList, function(i, obj) {
			xdata.push(obj.yjk_offer_nm);
			ydata.push(obj.sum_yjk_amt);
			
		});
	    Highcharts.setOptions({
	        lang: {
	            numericSymbols: null
	        }
	    });
	    $('#'+id).highcharts({
			chart: {
	            zoomType: 'xy',
	            defaultSeriesType: 'line',
	            height:300
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
	        	labels : {
					format : '{value}元',
				//enabled: false 
				},
	            title: {
	                text: '违规赠送金额（元）',
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
	            name: '违规赠送金额（元）',
	            type: 'column',
	            color:'#11EEEE',
	            yAxis:0,
	            data: ydata,
	            tooltip: {
	                valueSuffix: '元'
	            }
	        }]
	    });
}

/**
 * 赠送非中高端客户、非集团客户
 */
function loadFzgfjtTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月','营销案编号','营销案名称','赠送非中高端客户、非集团客户金额(元)'];
    
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
    tableMap.name = "yjk_offer_cd";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_offer_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "sum_yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return cellvalue.toFixed(2);
    };
    tableColModel.push(tableMap);

    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2006/getZsfzgfjtCustomerInfo",
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
 * 构建赠送用途柱状图
 */
function drawPurposeHistogram(id, xdata,ydata) {

    Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
    $('#'+id).highcharts({
		chart: {
            zoomType: 'xy',
            defaultSeriesType: 'line',
            height:300
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
        	labels : {
				format : '{value}元',
			//enabled: false 
			},
            title: {
                text: '赠送金额（元）',
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
            name: '赠送金额（元）',
            type: 'column',
            color:'#11EEEE',
            yAxis:0,
            data: ydata,
            tooltip: {
                valueSuffix: '元'
            }
        }]
    });
}

/**
 * 赠送员工分析列表
 */
function loadStaffTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    
    var tableColNames = [ '审计月','赠送号码','员工姓名','部门','在职/离职','赠送总金额(元)'];
    
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
    tableMap.name = "msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "emp_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "emp_dept";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "emp_stat";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return cellvalue.toFixed(2);
    };
    tableColModel.push(tableMap);

    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjk/2006/getPurposeInfo",
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
