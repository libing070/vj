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
    
}
/**
 * 首次加载页面触发函数调用
 * 数据图形加载
 */
function initDefaultData(){
	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

	//获取用户获赠地市列表
	loadYhhzyjkCityList();
	//渠道赠送地市列表
	loadQdzsyjkCityList();
	//营销案赠送地市列表
	loadYxazsjkCityList();
	//各赠送区间地市列表
	loadGzsqjyhCityList();
	//明细
	loadMxCityList();
	
	//加载折现图 (有价卡赠送波动趋势)
	loadNumberZhexianChart();
	//加载饼图
	loadGzsqjChart();
	//用户获赠有价卡金额统计(top10)
	loadYhhzyjkTop10Chart();
	//渠道赠送有价卡金额统计
	loadQdzsyjkTop10ChartsChart();
	//营销案赠送有价卡金额统计
	loadYxazsyjkTop10Chart();
	
	
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
 * 绑定本页面元素的响应,
 * 比如onclick,onchange,hover等
 */
function initEvent(){
	
	//点击重新加载图形
	$("#gzsqjyhChart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		 reloadGzsqjyhChartsAndCon($("#gzsqjyhCity").val());
		 
    });
	
	//点击重新加载图形
	$("#yhhzTop10Tel_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		reloadYhhzChartsAndCon($("#yhhzCity").val());
    });
	
	//点击重新加载图形
	$("#qdzsyjkTop10_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		reloadQdzsyjkChartsAndCon($("#qdzsCity").val());
    });
	
	//点击重新加载图形
	$("#yxazsyjkTop10_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		reloadYxazsyjkChartsAndCon($("#yxazsCity").val());
    });
	
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_有价卡赠送相关集中度异常_有价卡赠送集中度异常_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	
	//区间有价卡金额明细汇总
	$("#gzsqjyjkDetail_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadGzsqjyhDetailTable("gzsqjyjkDetailTable","gzsqjyjkDetailTablePageBar",$("#gzsqjyhCity").val());
    });
	//用户手机号码获赠有价卡金额明细汇总
	$("#yhhzTel_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYhhzTelDetailTable("yhhzTelTable","yhhzTelTablePageBar",$("#yhhzCity").val());
    });
	
	//导出用户手机号码获赠有价卡金额明细汇总
    $("#exportYhhzTelTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#yhhzTelTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
       // var exportFileName = "{0}_有价卡赠送相关集中度异常_有价卡赠送集中度异常_用户获赠有价卡金额统计2_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYhhzTelDetailList?" + $.param(getHz_QueryParam());

       // exportYhhzTelDetail(exportFileName,$("#yhhzCity").val());
    });
    function exportYhhzTelDetail(exportFileName,cityId){
    	var dc_beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var dc_endAcctMonth="";
		var year = dc_beforeAcctMonth.substring(0,4);
		var mouth = dc_beforeAcctMonth.substring(4,6);
		mouth = Number(mouth)+11;
		if(mouth>12){
			year = Number(year)+1;
			mouth = Number(mouth)-12;
		}
		if(mouth<10){
			dc_endAcctMonth = year+"0"+mouth;
		}
		if(mouth>=10){
			dc_endAcctMonth = year+""+mouth;
		}
        var hz_startMonth = dc_beforeAcctMonth;
        var hz_endMonth = dc_endAcctMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYhhzTelDetailList" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
	
	//用户获赠有价卡金额明细汇总
	$("#yhhzTop10Tel_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYhhzyjkDetailTable("yhhzTop10TelTable","yhhzTop10TelTablePageBar",$("#yhhzCity").val());
    });
	//导出用户获赠有价卡金额明细汇总
    $("#exportYhhzTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

    	
        var totalNum = $("#yhhzTop10TelTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_有价卡赠送相关集中度异常_有价卡赠送集中度异常_用户赠送有价卡金额统计_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYhhzDetail?" + $.param(getHz_QueryParam());

       // exportYhhzDetail(exportFileName,$("#yhhzCity").val());
    });
  //导出用户获赠有价卡金额明细汇总(导出实现)
    function exportYhhzDetail(exportFileName,cityId){
    	var dc_beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var dc_endAcctMonth="";
		var year = dc_beforeAcctMonth.substring(0,4);
		var mouth = dc_beforeAcctMonth.substring(4,6);
		mouth = Number(mouth)+11;
		if(mouth>12){
			year = Number(year)+1;
			mouth = Number(mouth)-12;
		}
		if(mouth<10){
			dc_endAcctMonth = year+"0"+mouth;
		}
		if(mouth>=10){
			dc_endAcctMonth = year+""+mouth;
		}
        var hz_startMonth = dc_beforeAcctMonth;
        var hz_endMonth = dc_endAcctMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYhhzDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
	
	//渠道赠送有价卡金额统计明细汇总
    $("#qdzsyjkTop10_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadQdzsyjkDetailTable("qdzsyjkDelTable_tab","qdzsyjkDelTablePageBar",$("#qdzsCity").val());
    });
    //导出渠道赠送明细列表
    $("#exportQdzsTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#qdzsyjkDelTable_tab").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_有价卡赠送相关集中度异常_有价卡赠送集中度异常_渠道赠送有价卡金额统计_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportQdzsDetail?" + $.param(getHz_QueryParam());

       // exportQdzsDetail(exportFileName,$("#qdzsCity").val());
    });
  //导出有价卡操作员汇总列表(导出实现)
    function exportQdzsDetail(exportFileName,cityId){
    	var dc_beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var dc_endAcctMonth="";
		var year = dc_beforeAcctMonth.substring(0,4);
		var mouth = dc_beforeAcctMonth.substring(4,6);
		mouth = Number(mouth)+11;
		if(mouth>12){
			year = Number(year)+1;
			mouth = Number(mouth)-12;
		}
		if(mouth<10){
			dc_endAcctMonth = year+"0"+mouth;
		}
		if(mouth>=10){
			dc_endAcctMonth = year+""+mouth;
		}
        var hz_startMonth =dc_beforeAcctMonth;
        var hz_endMonth = dc_endAcctMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportQdzsDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
  //营销案赠送有价卡金额统计明细汇总
    $("#yxazsyjkTop10_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	loadYxazsyjkDetailTable("yxazsyjkDelTable","yxazsyjkTablePageBar",$("#yxazsCity").val());
    });
  //导出营销案赠送明细列表 
    $("#exportYxazsTable_csv").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#yxazsyjkDelTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_有价卡赠送相关集中度异常_有价卡赠送集中度异常_营销案赠送有价卡金额统计_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYxazsDetail_csv?" + $.param(getHz_QueryParam());

        //exportYxazsDetail(exportFileName,$("#yxazsCity").val());
    });
    
  //导出营销案赠送列表(导出实现)
    function exportYxazsDetail(exportFileName,cityId){
    	var dc_beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
		var dc_endAcctMonth="";
		var year = dc_beforeAcctMonth.substring(0,4);
		var mouth = dc_beforeAcctMonth.substring(4,6);
		mouth = Number(mouth)+11;
		if(mouth>12){
			year = Number(year)+1;
			mouth = Number(mouth)-12;
		}
		if(mouth<10){
			dc_endAcctMonth = year+"0"+mouth;
		}
		if(mouth>=10){
			dc_endAcctMonth = year+""+mouth;
		}
        var hz_startMonth =dc_beforeAcctMonth;
        var hz_endMonth =dc_endAcctMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/exportYxazsDetail_csv" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&cityId="+cityId+
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        $("#currTab").val("hz");
       //加载折现图 (有价卡赠送波动趋势)
    	loadNumberZhexianChart();
    	//加载饼图
    	loadGzsqjChart();
    	//用户获赠有价卡金额统计(top10)
    	loadYhhzyjkTop10Chart();
    	//渠道赠送有价卡金额统计
    	loadQdzsyjkTop10ChartsChart();
    	//营销案赠送有价卡金额统计
    	loadYxazsyjkTop10Chart();
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
        loadMxCityDetailGridData();
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
    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");
        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
}

/**
 * 清空隐藏表单值
 * 
 */
function clearHideFormInput() {
    $("#mx_cityCode").val("");
}


/**
 * 获取默认首次加载本页面的初始化参数，并给隐藏form赋值
 */
function initDefaultParams(){
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
    //获取默认的地市名称
    $("#yhhzCityText").val($("#provinceName").val());
    $("#qdzsCityText").val($("#provinceName").val());
    $("#yxazsCityText").val($("#provinceName").val());
    $("#gzsqjyhCityText").val($("#provinceName").val());
   
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
        $("#currTab").val("hz");
    });
    
    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/initDefaultParams",
        async : false,
        dataType : 'json',
        data : postData,    
        success : function(data) {        
            
            $('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
            $('.form_datetime').datetimepicker('setStartDate',new Date(beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6)));
            $('.form_datetime').datetimepicker('setEndDate',new Date(endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6)));
            
            /**
             * 明细时间不可选
             */
            var mxbeforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    		var mxendAcctMonth="";
    		var year = beforeAcctMonth.substring(0,4);
    		var mouth = beforeAcctMonth.substring(4,6);
    		mouth = Number(mouth)+11;
    		if(mouth>12){
    			year = Number(year)+1;
    			mouth = Number(mouth)-12;
    		}
    		if(mouth<10){
    			mxendAcctMonth = year+"0"+mouth;
    		}
    		if(mouth>=10){
    			mxendAcctMonth = year+""+mouth;
    		}
           
            $('#mx_startMonth').val(mxbeforeAcctMonth.substring(0, 4)+"-"+mxbeforeAcctMonth.substring(4, 6));
            $('#mx_endMonth').val(mxendAcctMonth.substring(0, 4)+"-"+mxendAcctMonth.substring(4, 6));
            
            
            
            $('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
            
        }
    });
} 


/**
 * 获取汇总查询条件
 * 
 */
function getHz_QueryParam() {
    var postData = {};
    var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth="";
	var year = beforeAcctMonth.substring(0,4);
	var mouth = beforeAcctMonth.substring(4,6);
	mouth = Number(mouth)+11;
	if(mouth>12){
		year = Number(year)+1;
		mouth = Number(mouth)-12;
	}
	if(mouth<10){
		endAcctMonth = year+"0"+mouth;
	}
	if(mouth>=10){
		endAcctMonth = year+""+mouth;
	}
    postData.hz_startMonth =beforeAcctMonth;
    postData.hz_endMonth = endAcctMonth;
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.cityId = $("#cityId").val();
    postData.trendType = $("#trendType").val();
    return postData;
}

/**
 * 获取汇总查询条件
 * 
 */
function getHZ_ZheXianQueryParam() {
    var postData = {};
    postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.cityId = $("#cityId").val();
    return postData;
}
/**
 * 重新加载全局触发函数调用
 * 查询时重新加载
 */
function reloadGlobalData() {
	//查询时重新加载折线图
	loadNumberZhexianChart();
}



/**
 * 有价卡赠送波动趋势(主函数)
 */
function loadNumberZhexianChart() {
    var postData = getHZ_ZheXianQueryParam();
    var id = "zhexianChartSingle";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYJKYC3001Trend",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            loadAuditConclusion("auditConclusionSingle",data.maxMap,postData);
        }
    });
} 


/**
 * 计算激活有价卡数量趋势审计结论
 * 有价卡赠送波动趋势
 * @param data
 * @param postData
 */
function loadAuditConclusion(id,data,postData){
	 var text = "";
	 var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	 if(data.maxYjk_pres_dt!=null){
		 if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+timeToChinese(data.maxYjk_pres_dt)+",赠送有价卡的金额达到" + formatCurrency(data.maxCount_zsyjkje,true)+"元"
				+ "，高于平均值" + data.highAvg + "。";
		 }else{
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+timeToChinese(data.maxYjk_pres_dt)+",赠送有价卡的金额达到" + formatCurrency(data.maxCount_zsyjkje,true)+"元"
				+ "，高于平均值" + data.highAvg + "。";
		 }
		 
	 }else{
		 text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	 }
	$('#'+id).html(text);
	
}
/**
 * 有价卡赠送波动趋势图形
 * @param id
 * @param data
 * @param avgMap
 */
function drawRankLineChart(id, data,avgMap) {
 	var yjk_pres_dt = [];
    var count_zsyjkje  = [];
    var avg_count_zsyjkje = [];
    $.each(data, function(i, obj) {
    	yjk_pres_dt.push(obj.yjk_pres_dt);
    	count_zsyjkje.push(obj.count_zsyjkje);
    	avg_count_zsyjkje.push(Number(avgMap.avg_count_zsyjkje.toFixed(2)));
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
        	categories: yjk_pres_dt,
            crosshair: true
        }],
        yAxis: [{
            title: {
                text: '赠送有价卡金额(元)',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
            shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '各月赠送有价卡金额',
            type: 'spline',
            color:'#65d3e3',
            data: count_zsyjkje,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均赠送有价卡金额',
            type: 'spline',
            color:'#df5438',
            data: avg_count_zsyjkje,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
    
}
/**
 * 各赠送区间(环图)
 */
function loadGzsqjChart(){
	var postData = getHz_QueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getGzsqjChart",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			if(data.sum_count_zsyjkje != null){
				var dataChar =[["2000-5000",data.sum_je2000_5000_zsyhsl],["5000-1万",data.sum_je5000_10000_zsyhsl],
				       ["1万-2万",data.sum_je10000_20000_zsyhsl], ["2万-5万",data.sum_je20000_50000_zsyhsl],
				       ["5万-10万",data.sum_je50000_100000_zsyhsl],["≥10万",data.sum_gt100000_zsyhsl]];
			}
			loadGzsqjPieHighChart("#gzsqjPieCharts", dataChar);
			loadGzsqjPieCon("gzsqjPieCon", data,postData);
			
			var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
			
		}
	});
	
}

//图
function loadGzsqjPieHighChart(Id,dataChar){
	$(Id).highcharts({
		  chart:{
//	        	height:350
	        },
        title: {
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
        },
        colors:['#0000FF','#1AE6E6','#FFFF00','#FFC977','#E9B28E','red'],
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
            name: "各赠送区间的用户数量统计",
            innerSize:'50%',
            data: dataChar
        }]
    });
}
/**
 * 各赠送区间统计结论
 * @param id
 * @param data
 * @param postData
 */
function loadGzsqjPieCon(id,data,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	var gt2k_zb;
	if(data!=null){
		var all_yhsl=parseInt(data.sum_je50000_100000_zsyhsl)+parseInt(data.sum_je20000_50000_zsyhsl)
					+parseInt(data.sum_je10000_20000_zsyhsl)+parseInt(data.sum_je5000_10000_zsyhsl)
					+parseInt(data.sum_je2000_5000_zsyhsl)+parseInt(data.sum_gt100000_zsyhsl)
					+parseInt(data.sum_je1_2000_zsyhsl);
			gt2k = parseInt(data.sum_je50000_100000_zsyhsl)+parseInt(data.sum_je20000_50000_zsyhsl)
					+parseInt(data.sum_je10000_20000_zsyhsl)+parseInt(data.sum_je5000_10000_zsyhsl)
					+parseInt(data.sum_je2000_5000_zsyhsl)+parseInt(data.sum_gt100000_zsyhsl);
		if(all_yhsl==0||all_yhsl==""||gt2k==0||gt2k==""){
			gt2k_zb=0.00;
		}else{
			gt2k_zb=formatCurrency((gt2k/all_yhsl)*100,true);
		}
		if(all_yhsl==0){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据。";
		}else{
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
				"，"+$("#provinceName").val()+"赠送有价卡金额大于2000元的用户数量"+formatCurrency(gt2k,false)+"个,占所有赠送有价卡用户数量的"+gt2k_zb+"%。" +
					"其中，获赠金额10万元以上的用户有"+formatCurrency(data.sum_gt100000_zsyhsl,false)+"个("+formatCurrency(data.sum_gt100000_zsyjkje,true)+"元),"+
					"获赠金额在5万~10万之间的用户有"+formatCurrency(data.sum_je5000_10000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt50000_100000_zsyjkje,true)+"元)," +
					"获赠金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt20000_50000_zsyjkje,true)+"元)。";
			}else{
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
				"，"+$("#provinceName").val()+"赠送有价卡金额大于2000元的用户数量"+formatCurrency(gt2k,false)+"个,占所有赠送有价卡用户数量的"+gt2k_zb+"%。" +
				"其中，获赠金额10万元以上的用户有"+formatCurrency(data.sum_gt100000_zsyhsl,false)+"个("+formatCurrency(data.sum_gt100000_zsyjkje,true)+"元),"+
				"获赠金额在5万~10万之间的用户有"+formatCurrency(data.sum_je5000_10000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt50000_100000_zsyjkje,true)+"元)," +
				"获赠金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt20000_50000_zsyjkje,true)+"元)。";
			}
		}
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#gzsqjyjkDetailTable_Con").html(text);
	
}


function loadGzsqjyhDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','单用户赠送金额区间',
	                      '累计赠送用户数量','累计赠送有价卡数量','累计赠送有价卡金额(元)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm_begin";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aud_trm_end";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "qj_zsje";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_count_zsyhsl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_count_zsyjksl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_count_zsyjkje";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getGzsqjyhDetailTable",
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width() - 2);
			$("#"+pagerId).css("width", $("#"+pagerId).width() - 2);
			
			var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
			
        }
    });
}


function loadGzsqjyhCityList(){
	var postData = getHz_QueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	$("#gzsqjyhCitySelect").append(li);
            addGzsqjyhSelectEvent("gzsqjyhCity");
        }
    });
}
function addGzsqjyhSelectEvent(id,callback){
	 $("#" + id + "Select").find("li").click(function() {
	        $(this).addClass('active').siblings().removeClass('active');
	        $("#" + id + "Text").val($(this).text());
	        $("#" + id).val($(this).attr("code"));
	        $(this).parent().hide();
	        var gzsqjyhCityId = $("#gzsqjyhCity").val();
	        reloadGzsqjyhChartsAndCon(gzsqjyhCityId);
	        reloadGzsqjyhDetil("gzsqjyjkDetailTable",$(this).attr("code"));
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
}

//各赠送区间下拉城市列表时的明细表
function reloadGzsqjyhDetil(tableId,gzsqjyhCityId) {
	var postData = getHz_QueryParam(); 
	postData.cityId = gzsqjyhCityId;
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getGzsqjyhDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function reloadGzsqjyhChartsAndCon(cityId){
	var postData = getHz_QueryParam();
    var id = "gzsqjyhChart";
    postData.cityId = cityId;
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getGzsqjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
			var	dataChar =[["2000-5000",data.sum_je2000_5000_zsyhsl],["5000-1万",data.sum_je5000_10000_zsyhsl],
				       ["1万-2万",data.sum_je10000_20000_zsyhsl], ["2万-5万",data.sum_je20000_50000_zsyhsl],
				       ["5万-10万",data.sum_je50000_100000_zsyhsl],["≥10万",data.sum_gt100000_zsyhsl]];
			loadGzsqjPieHighChart("#gzsqjPieCharts", dataChar);
			reloadGzsqjPieCon("gzsqjPieCon", data,postData);
			
			var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
			
        }
    });
}
function reloadGzsqjPieCon(id, data,postData){
	var text = "";
	var gt2k;
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data!=null){
		var all_yhsl=parseInt(data.sum_je50000_100000_zsyhsl)+parseInt(data.sum_je20000_50000_zsyhsl)
					+parseInt(data.sum_je10000_20000_zsyhsl)+parseInt(data.sum_je5000_10000_zsyhsl)
					+parseInt(data.sum_je2000_5000_zsyhsl)+parseInt(data.sum_gt100000_zsyhsl)
					+parseInt(data.sum_je1_2000_zsyhsl);
		gt2k = parseInt(data.sum_je50000_100000_zsyhsl)+parseInt(data.sum_je20000_50000_zsyhsl)
				+parseInt(data.sum_je10000_20000_zsyhsl)+parseInt(data.sum_je5000_10000_zsyhsl)
				+parseInt(data.sum_je2000_5000_zsyhsl)+parseInt(data.sum_gt100000_zsyhsl);
		var gt2k_zb;
		if(all_yhsl==0||gt2k==""||gt2k==0){
			gt2k_zb=0;
		}else{
			gt2k_zb=formatCurrency((gt2k/all_yhsl)*100,true);
		}
		if(all_yhsl==0 && postData.cityId==""){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据";
		}
		
		if(all_yhsl==0 && postData.cityId!=""){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#gzsqjyhCityText").val()+"无数据";
		}
		if(all_yhsl!=0){
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#gzsqjyhCityText").val()+"赠送有价卡金额大于2000元的用户数量"+gt2k+"个,占所有赠送有价卡用户数量的"+gt2k_zb+"%。" +
					"其中,获赠金额10万元以上的用户有"+formatCurrency(data.sum_gt100000_zsyhsl,false)+"个("+formatCurrency(data.sum_gt100000_zsyjkje,true)+"元),"+
					"获赠金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt50000_100000_zsyjkje,true)+"元)," +
					"获赠金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt20000_50000_zsyjkje,true)+"元)。";
			}else if(postData.cityId!=""){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#gzsqjyhCityText").val()+"赠送有价卡金额大于2000元的用户数量"+gt2k+"个,占所有赠送有价卡用户数量的"+gt2k_zb+"%;" +
					"其中,获赠金额10万元以上的用户有"+formatCurrency(data.sum_gt100000_zsyhsl,false)+"个("+formatCurrency(data.sum_gt100000_zsyjkje,true)+"元),"+
					"获赠金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt50000_100000_zsyjkje,true)+"元)," +
					"获赠金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt20000_50000_zsyjkje,true)+"元)。";
			}else{
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#provinceName").val()+"赠送有价卡金额大于2000元的用户数量"+gt2k+"个,占所有赠送有价卡用户数量的"+gt2k_zb+"%;" +
					"其中,获赠金额10万元以上的用户有"+formatCurrency(data.sum_gt100000_zsyhsl,false)+"个("+formatCurrency(data.sum_gt100000_zsyjkje,true)+"元),"+
					"获赠金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt50000_100000_zsyjkje,true)+"元)," +
					"获赠金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_zsyhsl,false)+"个("+formatCurrency(data.sum_bt20000_50000_zsyjkje,true)+"元)。";
			}
		}
		
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#gzsqjyhCityText").val()+"无数据";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	$('#'+id).html(text);
	$("#gzsqjyjkDetailTable_Con").html(text);
}

/**
 * 用户获赠有价卡金额统计(top10)主函数
 */
function loadYhhzyjkTop10Chart(){
	var postData = getHz_QueryParam();
    var id = "yhhzTop10TelCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzTop10TelCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYhhzyjkTop10Chart(id,data.data);
            loadAuditYhhzyjkTop10Conclusion("yhhzTop10TelChart_Con",data.data,postData);
            
            var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
            
        }
    });
}
/**
 * 用户获赠有价卡金额统计(top10)柱状图
 * @param id
 * @param data
 */
function drawRankLineYhhzyjkTop10Chart(id,data){
	var trade_msisdn = [];//手机号码
    var sum_yjk_trade_amt  = [];//sum_累计充值有价卡金额
    $.each(data, function(i, obj) {
    	trade_msisdn.push(obj.trade_msisdn);
    	sum_yjk_trade_amt.push(obj.sum_yjk_trade_amt);
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
        	labels: {
        		rotation: -20
        	},
        	categories: trade_msisdn,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '用户获赠有价卡金额（元）',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
            shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '用户获赠有价卡金额（元）',
            type: 'column',
            color:'#65d3e3',
            data: sum_yjk_trade_amt,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

/**
 * 用户获赠有价卡金额统计(top10)柱状图结论
 */
function loadAuditYhhzyjkTop10Conclusion(id,data,postData){
	var text = "";
	var top3Tel="";
	var top3Num = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj){
				top3Tel = top3Tel+obj.trade_msisdn+"，";
				top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Tel = top3Tel+obj.trade_msisdn+"，";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,false)+"元)";
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,false)+"元)、";
				}
				i++;
			});
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Tel = top3Tel+obj.trade_msisdn+"，";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,false)+"元)";
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,false)+"元)、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yhhzCityText").val()+"获赠金额异常排名前三的手机号码是" + top3Tel + "分别获赠有价卡" + top3Num + "。";
		}else if(postData.cityId!=""){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yhhzCityText").val()+"获赠金额异常排名前三的手机号码是" + top3Tel
				+ "分别获赠有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"获赠金额异常排名前三的手机号码是" + top3Tel
			+ "分别获赠有价卡" + top3Num + "。";
		}
			
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
}
//用户获赠有价卡金额统计  数据表2 结论
function loadYhhzTelListCon(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	$.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzTelDetailTable",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var text = "";
        	if(data.totalPages!=0){
        		if(cityId!=null||cityId!=""){
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
						+ "，" + $("#yhhzCityText").val()+"手机号码获赠有价卡汇总数据如下：";
        		}else{
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
						+ "，" + $("#provinceName").val()+"手机号码获赠有价卡汇总数据如下：";
        		}
        		
        	}else{
        		if(cityId!=null||cityId!=""){
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
    					+ "，" + $("#yhhzCityText").val()+"无数据。";
        		}else{
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
    					+ "，" + $("#provinceName").val()+"无数据。";
        		}
        	}
        	
        	$("#yhhzTelTable_Con").html(text);
        }
		
    });
}

//用户获赠有价卡金额统计  数据表2 结论
function loadYhhzTelDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	loadYhhzTelListCon(tableId,pagerId,cityId);
	var tableColNames = [ '审计月','省(市)','获赠手机号','用户类型','集团客户名称','赠送有价卡数量','赠送金额（元）'];
	
	var tableColModel = [];
	
	 tableMap = new Object();
     tableMap.name = "aud_trm";
     tableMap.align = "center";
     tableColModel.push(tableMap);
     
     tableMap = new Object();
     tableMap.name = "short_name";
     tableMap.align = "center";
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
     tableMap.name = "yjk_num";
     tableMap.align = "center";
     tableMap.formatter = function(cellvalue, options, rowObject) {
 		return formatCurrency(cellvalue, false);
 	};
     tableColModel.push(tableMap);
     
     tableMap = new Object();
     tableMap.name = "yjk_amt";
     tableMap.align = "center";
     tableMap.formatter = function(cellvalue, options, rowObject) {
 		return formatCurrency(cellvalue, true);
 	};
     tableColModel.push(tableMap);
     
     jQuery("#"+tableId).jqGrid({
     	url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzTelDetailTable",
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
         	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
 			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width());
 			$("#"+pagerId).css("width", $("#"+pagerId).width());
 			
 			var gzsqjDivId_height = $("#gzsqjDivId").height();
 			var yhhzDivId_height = $("#yhhzDivId").height();
 			if(gzsqjDivId_height>=yhhzDivId_height){
 				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
 			}else{
 				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
 			}
 			
         }
     });
	
}

function loadYhhzyjkDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','手机号码',
	                      '累计赠送有价卡数量','累计赠送有价卡金额(元)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm_begin";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aud_trm_end";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "trade_msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_trade_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_trade_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzyjkDetailTable",
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width());
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
			var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
			
        }
    });
	
}

/**
 * 加载用户获赠地市字段列表
 */
function loadYhhzyjkCityList() {
    var postData = getHz_QueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	 $("#yhhzCitySelect").append(li);
            addYhhzSelectEvent("yhhzCity");
        }
    });
}
/**
 * 加载用户获赠地市字段列表(选择后隐藏列表)
 */
function addYhhzSelectEvent(id, callback){
	 $("#" + id + "Select").find("li").click(function() {
	        $(this).addClass('active').siblings().removeClass('active');
	        $("#" + id + "Text").val($(this).text());
	        $("#" + id).val($(this).attr("code"));
	        $(this).parent().hide();
	        var yhhzYjkCityId = $("#yhhzCity").val();
	        reloadYhhzChartsAndCon(yhhzYjkCityId);
	        reloadYhhzDetil("yhhzTop10TelTable",$(this).attr("code"));
	        reloadYhhzTelDetailTable("yhhzTelTable",yhhzYjkCityId);
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
}
//用户获赠有价卡金额统计  数据表2 结论
function reloadYhhzTelDetailTable(tableId,yhhzYjkCityId){
	var postData = getHz_QueryParam(); 
	postData.cityId = yhhzYjkCityId;
	loadYhhzTelListCon("yhhzTelTable","yhhzTelTablePageBar",yhhzYjkCityId);
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzTelDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//用户获赠下拉城市列表时的明细表
function reloadYhhzDetil(tableId,yhhzYjkCityId) {
	var postData = getHz_QueryParam(); 
	postData.cityId = yhhzYjkCityId;
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzyjkDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//用户获赠赠送地市统计图(chart)
function reloadYhhzChartsAndCon(yhhzYjkCityId){
	var postData = getHz_QueryParam();
    var id = "yhhzTop10TelCharts";
    postData.cityId = yhhzYjkCityId;
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYhhzTop10TelCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYhhzyjkTop10Chart(id,data.data);
            reloadYhhzyjkTop10Conclusion("yhhzTop10TelChart_Con",data.data,postData);
            
            var gzsqjDivId_height = $("#gzsqjDivId").height();
			var yhhzDivId_height = $("#yhhzDivId").height();
			if(gzsqjDivId_height>=yhhzDivId_height){
				$("#yhhzDivId").css("height",$("#gzsqjDivId").css("height"));
			}else{
				$("#gzsqjDivId").css("height",$("#yhhzDivId").css("height"));
			}
            
        }
    });
}
//用户获赠地市统计结论
function reloadYhhzyjkTop10Conclusion(id,data,postData){
	var text = "";
	var top3Tel="";
	var top3Num = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj){
				top3Tel = top3Tel+obj.trade_msisdn+"，";
				top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Tel = top3Tel+obj.trade_msisdn+"，";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)、";
				}
				i++;
			});
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Tel = top3Tel+obj.trade_msisdn+"，";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yhhzCityText").val()+"赠送金额异常排名前三的手机号码是" + top3Tel + "分别赠送有价卡" + top3Num + "。";
		}else if(postData.cityId!=""){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yhhzCityText").val()+"赠送金额异常排名前三的手机号码是" + top3Tel
				+ "分别赠送有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"赠送金额异常排名前三的手机号码是" + top3Tel
			+ "分别赠送有价卡" + top3Num + "。";
		}
			
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
}

/**
 * 渠道赠送有价卡金额统计(图形)主方法
 */
function loadQdzsyjkTop10ChartsChart(){
	var postData = getHz_QueryParam();
    var id = "qdzsyjkTop10Charts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getQdzsyjkTop10Charts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineQdzsyjkTop10Chart(id,data.data);
            loadAuditQdzsyjkTop10Conclusion("qdzsyjkTop10Chart_Con",data.data,postData);
            
            var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
			
			
        }
    });
}
/**
 * 渠道赠送有价卡金额统计(图形)
 * @param id
 * @param data
 */
function drawRankLineQdzsyjkTop10Chart(id,data){
	var cor_chnl_nm = [];//手机号码
    var sum_count_zsyjkje  = [];//sum_累计充值有价卡金额
    $.each(data, function(i, obj) {
    	cor_chnl_nm.push(obj.cor_chnl_nm);
    	sum_count_zsyjkje.push(obj.sum_count_zsyjkje);
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
        	labels: {
        		rotation: -20
        	},
        	categories: cor_chnl_nm,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '渠道赠送有价卡金额(元)',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
            shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '渠道赠送有价卡金额(元)',
            type: 'column',
            color:'#65d3e3',
            data: sum_count_zsyjkje,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

/**
 * 渠道赠送有价卡金额统计(图形结论)
 * @param id
 * @param data
 * @param postData
 */
function loadAuditQdzsyjkTop10Conclusion(id,data,postData){
	var text = "";
	var top3qd="";
	var top3Num = "";//赠送多少张
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj) {
				top3qd = top3qd+obj.cor_chnl_nm;
				top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
			});
			
		}else if(data.length==2){
			$.each(data, function(i, obj) {
				if(i==1){
					top3qd = top3qd+obj.cor_chnl_nm;
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				}else{
					top3qd = top3qd+obj.cor_chnl_nm+"、";
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
				}
				i++;
			});
			
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3qd = top3qd+obj.cor_chnl_nm;
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				}else{
					top3qd = top3qd+obj.cor_chnl_nm+"、";
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		
		
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的渠道有" + top3qd
			+ "，分别赠送有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的渠道有" + top3qd
			+ "，分别赠送有价卡" + top3Num + "。";
		}
		 
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#qdzsyjkDelTable_Con").html(text);
	
}

//渠道赠送有价卡金额统计（明细）
function loadQdzsyjkDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','赠送渠道标识','赠送渠道名称',
	                      '累计赠送用户数量','累计赠送有价卡数量','累计赠送有价卡金额(元)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm_begin";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aud_trm_end";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
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
    
    tableMap = new Object();
    tableMap.name = "count_zsyhsl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "count_zsyjksl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "count_zsyjkje";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getQdzsyjkDetailTable",
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width());
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
			var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
			
        }
    });
	
}

/**
 * 渠道获赠下拉城市列表(选择后隐藏列表)
 */
function loadQdzsyjkCityList(){
	var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	$("#qdzsCitySelect").append(li);
            addQdzsyjkSelectEvent("qdzsCity");
        }
    });
}

/**
 * 渠道获赠下拉城市列表,重新加载数据
 * @param id
 */
function addQdzsyjkSelectEvent(id, callback) {
    $("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
        var qdzsYjkCityId = $("#qdzsCity").val();
        reloadQdzsyjkChartsAndCon(qdzsYjkCityId);
        reloadDetil_tab("qdzsyjkDelTable_tab",$(this).attr("code"));
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}
//渠道获赠下拉城市列表时的明细表
function reloadDetil_tab(tableId,qdzsYjkCityId) {
	var postData = getHz_QueryParam(); 
	postData.cityId = qdzsYjkCityId;
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getQdzsyjkDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//获取渠道赠送地市统计图(chart)
function reloadQdzsyjkChartsAndCon(qdzsYjkCityId){
	var postData = getHz_QueryParam();
	postData.cityId = qdzsYjkCityId;
    var id = "qdzsyjkTop10Charts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getQdzsyjkTop10Charts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineQdzsyjkTop10Chart(id,data.data);
            //获取渠道赠送地市统计结论
            reloadQdzsyjkCon("qdzsyjkTop10Chart_Con",data.data,postData);
            
            var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
            
        }
    });
}
//获取渠道赠送地市统计结论
function reloadQdzsyjkCon(id,data,postData){
	var text = "";
	var top3qd="";
	var top3Num = "";//赠送多少张
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj) {
				top3qd = top3qd+obj.cor_chnl_nm;
				top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
			});
			
		}else if(data.length==2){
			$.each(data, function(i, obj) {
				if(i==1){
					top3qd = top3qd+obj.cor_chnl_nm;
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				}else{
					top3qd = top3qd+obj.cor_chnl_nm+"、";
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
				}
				i++;
			});
			
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3qd = top3qd+obj.cor_chnl_nm;
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				}else{
					top3qd = top3qd+obj.cor_chnl_nm+"、";
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的渠道有" + top3qd
			+ "，分别获赠有价卡" + top3Num + "。";
		}else if(postData.cityId!=""){
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#qdzsCityText").val()+"赠送有价卡金额排名前三的渠道有" + top3qd
				+ "，分别获赠有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的渠道有" + top3qd
				+ "，分别获赠有价卡" + top3Num + "。";
		}
		 
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#qdzsCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#qdzsyjkDelTable_Con").html(text);
}

/**
 *  营销案赠送有价卡金额统计(主方法)
 */
function loadYxazsyjkTop10Chart(){
	var postData = getHz_QueryParam();
    var id = "yxazsyjkTop10Charts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYxazsyjkTop10Chart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYxazsyjkTop10Chart(id,data.data);
            loadAuditYxazsyjkTop10ChartConclusion("yxazsyjkTop10Chart_Con",data.data,postData);
            
            var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
            
        }
    });
}
/**
 * 营销案赠送有价卡金额统计(图形)
 * @param id
 * @param data
 */
function drawRankLineYxazsyjkTop10Chart(id,data){
	var yjk_offer_nm = [];
    var sum_count_zsyjkje  = [];//sum_累计充值有价卡金额
    $.each(data, function(i, obj) {
    	yjk_offer_nm.push(obj.yjk_offer_nm);
    	sum_count_zsyjkje.push(obj.sum_count_zsyjkje);
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
        	labels: {
        		rotation: -20
        	},
        	categories: yjk_offer_nm,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '营销案赠送有价卡金额(元)',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
            shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '营销案赠送有价卡金额(元)',
            type: 'column',
            color:'#65d3e3',
            data: sum_count_zsyjkje,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}
//营销案赠送结论
function loadAuditYxazsyjkTop10ChartConclusion(id,data,postData){
	var text = "";
	var top3offer="";
	var top3Num = "";//赠送多少张
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj){
				top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				top3offer = top3offer+obj.yjk_offer_nm+",";
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
					top3offer = top3offer+obj.yjk_offer_nm+",";
				}else{
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
					top3offer = top3offer+obj.yjk_offer_nm+"、";
				}
				i++;
			});
			
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
					top3offer = top3offer+obj.yjk_offer_nm+",";
				}else{
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
					top3offer = top3offer+obj.yjk_offer_nm+"、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yxazsCityText").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
				+ "分别获赠有价卡" + top3Num + "。";
		}else if(postData.cityId!=""){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yxazsCityText").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
				+ "分别获赠有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
			+ "分别获赠有价卡" + top3Num + "。";
		}
			
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yxazsCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#yxazsyjkDelTable_Con").html(text);
}

function loadYxazsyjkDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','有价卡赠送涉及的营销案编号',
	                      '营销案名称','累计赠送用户数量','累计赠送有价卡数量','累计赠送有价卡金额(元)'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm_begin";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aud_trm_end";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
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
    tableMap.name = "sum_count_zsyhsl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_count_zsyjksl";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_count_zsyjkje";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYxazsyjkDetailTable",
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
        	$("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width());
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
			var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
			
        }
    });
}

function loadYxazsjkCityList(){
	var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "<li code=''>{0}</li>".format($("#provinceName").val());
        	$.each(data.body.ctyList, function(i, map) {
             	 if(map.CMCC_prvd_cd == "10100" || map.CMCC_prvd_cd == "10400" || map.CMCC_prvd_cd == "10200" || map.CMCC_prvd_cd == "10300" ){
              		li ="<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}else{
              		li = li+"<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
              	}
             });
        	$("#yxazsCitySelect").append(li);
            addYxazsSelectEvent("yxazsCity");
        }
    });
}

function addYxazsSelectEvent(id,callback){
	$("#" + id + "Select").find("li").click(function() {
        $(this).addClass('active').siblings().removeClass('active');
        $("#" + id + "Text").val($(this).text());
        $("#" + id).val($(this).attr("code"));
        $(this).parent().hide();
        var yxazsYjkCityId = $("#yxazsCity").val();
        reloadYxazsyjkChartsAndCon(yxazsYjkCityId);
        reloadYxazsDetil("yxazsyjkDelTable",$(this).attr("code"));
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

function reloadYxazsDetil(tableId,yxazsYjkCityId){
	var postData = getHz_QueryParam(); 
	postData.cityId = yxazsYjkCityId;
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYxazsyjkDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reloadYxazsyjkChartsAndCon(yxazsYjkCityId){
	var postData = getHz_QueryParam();
	postData.cityId = yxazsYjkCityId;
    var id = "yxazsyjkTop10Charts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getYxazsyjkTop10Chart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYxazsyjkTop10Chart(id,data.data);
            reloadYxazsyjkCon("yxazsyjkTop10Chart_Con",data.data,postData);
            
            var qdzsDivId_height = $("#qdzsDivId").height();
			var yxazsDivId_height = $("#yxazsDivId").height();
			if(qdzsDivId_height>=yxazsDivId_height){
				$("#yxazsDivId").css("height",$("#qdzsDivId").css("height"));
			}else{
				$("#qdzsDivId").css("height",$("#yxazsDivId").css("height"));
			}
            
        }
    });
}

function reloadYxazsyjkCon(id,data,postData){
	var text = "";
	var top3offer="";
	var top3Num = "";//赠送多少张
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj){
				top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
				top3offer = top3offer+obj.yjk_offer_nm+",";
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
					top3offer = top3offer+obj.yjk_offer_nm+",";
				}else{
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
					top3offer = top3offer+obj.yjk_offer_nm+"、";
				}
				i++;
			});
			
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)";
					top3offer = top3offer+obj.yjk_offer_nm+",";
				}else{
					top3Num = top3Num+formatCurrency(obj.sum_count_zsyjksl,false)+"张("+formatCurrency(obj.sum_count_zsyjkje,true)+"元)、";
					top3offer = top3offer+obj.yjk_offer_nm+"、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yxazsCityText").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
				+ "分别获赠有价卡" + top3Num + "。";
		}else if(postData.cityId!=""){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#yxazsCityText").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
				+ "分别获赠有价卡" + top3Num + "。";
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡金额排名前三的营销案有" + top3offer
			+ "分别获赠有价卡" + top3Num + "。";
		}
			
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yxazsCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#yxazsyjkDelTable_Con").html(text);
}

function mxFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#mx_startMonth").val() >  $("#mx_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    return true;
}

/**
 * 刷新明细页面地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 明细页面列表
 */
function loadMxCityDetailGridData(){
	if ($("#cityDetailGridData").html() != "") {
        return;
    }
	var postData = getDetailQueryParam();
    var tableColNames = [ '审计起始月','审计结束月','省名称', '地市名称', 
                          '有价卡序列号', '金额(元)', 
                          '获赠有价卡的手机号',  '有价卡赠送涉及的营销案编号', 
                          '营销案名称', '赠送渠道标识','赠送渠道名称' ];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "aud_trm_begin";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "aud_trm_end";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
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
    tableMap.name = "yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pres_msisdn";
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
    tableMap.name = "cor_chnl_typ";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cor_chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityDetailPagerList",
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
            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width());
        }
    });
	
}

/**
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc/3001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            $.each(data.body.ctyList, function(i, map) {
                var li = "<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
                $("#mx_cityCodeSelect").append(li);
            });
            addMxSelectEvent("mx_cityCode");
        }
    });
}

/**
 * 为指定ID添加下拉框监听事件
 * @param id
 */
function addMxSelectEvent(id, callback) {
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
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
    var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth="";
	var year = beforeAcctMonth.substring(0,4);
	var mouth = beforeAcctMonth.substring(4,6);
	mouth = Number(mouth)+11;
	if(mouth>12){
		year = Number(year)+1;
		mouth = Number(mouth)-12;
	}
	if(mouth<10){
		endAcctMonth = year+"0"+mouth;
	}
	if(mouth>=10){
		endAcctMonth = year+""+mouth;
	}
    postData.mx_startMonth =beforeAcctMonth;
    postData.mx_endMonth = endAcctMonth;
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.cityId = $("#cityId").val();
    postData.trendType = $("#trendType").val();
    return postData;
}

/**
 * 金额保留两位小数
 * @param x
 * @returns
 */
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
$(window).resize(function(){
	$("#gzsqjyjkDetailTable").setGridWidth($("gzsqjyjkDetailTableDiv_class").width());
});
$(window).resize(function(){
	$("#yhhzTop10TelTable").setGridWidth($("yhhzTop10TelTableDiv_class").width());
});
$(window).resize(function(){
	$("#qdzsyjkDelTable_tab").setGridWidth($("qdzsyjkDelTableDiv_class").width());
});
$(window).resize(function(){
	$("#yxazsyjkDelTable").setGridWidth($("yxazsyjkDelTableDiv_class").width());
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});
