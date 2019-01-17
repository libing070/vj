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
	
	//获取用户获赠地市列表
	loadYhhzyjkCityList();
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
	
	//用户充值有价卡金额统计 数据表2
	$("#yhczJe_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYhczTelDetailTable("yhczTable","yhczTablePageBar",$("#yhhzCity").val());
    });
	
	
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_有价卡赠送相关集中度异常_赠送有价卡充值集中度异常_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	
	//区间有价卡金额明细汇总
	$("#gzsqjyjkDetail_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadGzsqjyhDetailTable("gzsqjyjkDetailTable","gzsqjyjkDetailTablePageBar",$("#gzsqjyhCity").val());
		
    });
	
	//用户获赠有价卡金额明细汇总
	$("#yhhzTop10Tel_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYhhzyjkDetailTable("yhhzTop10TelTable","yhhzTop10TelTablePageBar",$("#yhhzCity").val());
    });
	
	//导出用户获赠有价卡金额明细汇总
    $("#exportYhczTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#yhczTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}有价卡赠送相关集中度异常_赠送有价卡充值集中度异常_汇总2.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/exportYhczDetail?" + $.param(getHz_QueryParam());

        //exportYhczDetail(exportFileName,$("#yhhzCity").val());
    });
    
    function exportYhczDetail(exportFileName,cityId){
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
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/exportYhczDetail" +
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
    
	//导出用户获赠有价卡金额明细汇总
    $("#exportYhhzTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");
        var totalNum = $("#yhhzTop10TelTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_有价卡赠送相关集中度异常_赠送有价卡充值集中度异常_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/exportYhhzDetail?" + $.param(getHz_QueryParam());

        //exportYhhzDetail(exportFileName,$("#yhhzCity").val());
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
		var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/exportYhhzDetail" +
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
    	loadGzsqjChart();
    	//用户获赠有价卡金额统计(top10)
    	loadYhhzyjkTop10Chart();
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
    
    $(window).resize(function(){
    	$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
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
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/initDefaultParams",
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
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYJKYC3002Trend",
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
	 if(data.maxTrade_mon!=null){
		 if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+timeToChinese(data.maxTrade_mon)+"充值有价卡的金额达到"
					+ formatCurrency(data.maxYjk_trade_amt,true)+"元"
					+ "，高于平均值" + data.highAvg + "。";
		 }else{
			 text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+timeToChinese(data.maxTrade_mon)+"充值有价卡的金额达到"
				+ formatCurrency(data.maxYjk_trade_amt,true)+"元"
				+ "，高于平均值" + data.highAvg + "。";
		 }
		
	 }else{
		 text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据";
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
 	var trade_mon = [];
    var yjk_trade_amt  = [];
    var avg_yjk_trade_amt = [];
    $.each(data, function(i, obj) {
    	trade_mon.push(obj.trade_mon);
    	yjk_trade_amt.push(obj.yjk_trade_amt);
    	avg_yjk_trade_amt.push(Number(avgMap.avg_yjk_trade_amt.toFixed(2)));
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
        	categories: trade_mon,
            crosshair: true
        }],
        yAxis: [{
            title: {
                text: '充值有价卡金额(元)',
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
            name: '各月充值有价卡金额',
            type: 'spline',
            color:'#65d3e3',
            data: yjk_trade_amt,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均充值有价卡金额',
            type: 'spline',
            color:'#df5438',
            data: avg_yjk_trade_amt,
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
		url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getGzsqjChart",
		dataType : 'json',
		async : false,
		data : postData,
		success : function(data) {
			if(data.sum_yjk_trade_user_num != null){
				var dataChar =[
				       ["2000-5000",data.sum_je2000_5000_userNum],["5000-1万",data.sum_je5000_10000_userNum],
				       ["1万-2万",data.sum_je10000_20000_userNum], ["2万-5万",data.sum_je20000_50000_userNum],
				       ["5万-10万",data.sum_je50000_100000_userNum],["≥10万",data.sum_je100000_userNum]
				];
			}
			loadGzsqjPieHighChart("#gzsqjPieCharts", dataChar);
			loadGzsqjPieCon("gzsqjPieCon", data,postData);
			
			var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			}
			
		}
	});
	
}

//图
function loadGzsqjPieHighChart(Id,dataChar){
	$(Id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.0f}%</b>'
        },
        colors:['#0000FF','#1AE6E6','#FFFF00','#FFC977','#E9B28E','#FF8877'],
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
            innerSize:'60%',
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
	if(data!=null){
		var all_yhsl=parseInt(data.sum_je50000_100000_userNum)+parseInt(data.sum_je20000_50000_userNum)
					+parseInt(data.sum_je10000_20000_userNum)+parseInt(data.sum_je5000_10000_userNum)
					+parseInt(data.sum_je2000_5000_userNum)+parseInt(data.sum_je100000_userNum)
					+parseInt(data.sum_je1_2000_userNum);
		var gt2k =  parseInt(data.sum_je50000_100000_userNum) +parseInt(data.sum_je20000_50000_userNum)
				+parseInt(data.sum_je10000_20000_userNum)+parseInt(data.sum_je5000_10000_userNum)
				+parseInt(data.sum_je2000_5000_userNum)+parseInt(data.sum_je100000_userNum);
		var gt2k_zb="";
		
		if(all_yhsl==0||gt2k==""||gt2k==0){
			gt2k_zb=0;
		}else{
			gt2k_zb=formatCurrency((gt2k/all_yhsl)*100,true);
		}
		if(all_yhsl==0){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
		}else{
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
				"，"+$("#provinceName").val()+"充值有价卡金额大于2000元的用户数量"+formatCurrency(gt2k,false)+"个,占所有充值有价卡用户数量的"+gt2k_zb+"%。" +
					"其中，充值金额10万元以上的用户有"+formatCurrency(data.sum_je100000_userNum,false)+"个("+formatCurrency(data.sum_gt100000_tradeAmt,true)+"元)，"+
					"充值金额在5万~10万之间的用户有"+formatCurrency(data.sum_je5000_10000_userNum,false)+"个("+formatCurrency(data.sum_bt50000_100000_tradeAmt,true)+"元)，" +
					"充值金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_userNum,false)+"个("+formatCurrency(data.sum_bt20000_50000_tradeAmt,true)+"元)。";
			}else{
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
				"，"+$("#provinceName").val()+"充值有价卡金额大于2000元的用户数量"+formatCurrency(gt2k,false)+"个,占所有充值有价卡用户数量的"+gt2k_zb+"%。" +
				"其中，充值金额10万元以上的用户有"+formatCurrency(data.sum_je100000_userNum,false)+"个("+formatCurrency(data.sum_gt100000_tradeAmt,true)+"元)，"+
				"充值金额在5万~10万之间的用户有"+formatCurrency(data.sum_je5000_10000_userNum,false)+"个("+formatCurrency(data.sum_bt50000_100000_tradeAmt,true)+"元)，" +
				"充值金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_userNum,false)+"个("+formatCurrency(data.sum_bt20000_50000_tradeAmt,true)+"元)。";
			}
		}
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据";
	}
	
	$('#'+id).html(text);
	$("#gzsqjyjkDetailTable_Con").html(text);
}


function loadGzsqjyhDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','赠送有价卡单用户充值金额区间',
	                      '累计充值用户数量','累计充值有价卡数量','累计充值有价卡金额(元)'];
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
    tableMap.name = "yjk_amt_range";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_trade_user_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_trade_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_trade_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getGzsqjyhDetailTable",
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
//			$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			}
        }
    });
}


function loadGzsqjyhCityList(){
	var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getCityList",
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
	        //重新加载图形及结论
	        reloadGzsqjyhChartsAndCon(gzsqjyhCityId);
	        //重新加载数据表
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
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getGzsqjyhDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function reloadGzsqjyhChartsAndCon(cityId){
	var postData = getHz_QueryParam();
    var id = "gzsqjyhChart";
    postData.cityId = cityId;
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getGzsqjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	if(data.sum_yjk_trade_user_num != null){
				var dataChar =[
				       ["2000-5000",data.sum_je2000_5000_userNum],["5000-10000",data.sum_je5000_10000_userNum],
				       ["1万-2万",data.sum_je10000_20000_userNum], ["2万-5万",data.sum_je20000_50000_userNum],
				       ["5万-10万",data.sum_je50000_100000_userNum],["≥10万",data.sum_je100000_userNum]
				   ];
			}
			loadGzsqjPieHighChart("#gzsqjPieCharts", dataChar);
			reloadGzsqjPieCon("gzsqjPieCon", data,postData);
			
			var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			}
			
        }
    
    });
    $("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
}
function reloadGzsqjPieCon(id, data,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data!=null){
		
		var all_yhsl=parseInt(data.sum_je50000_100000_userNum)+parseInt(data.sum_je20000_50000_userNum)
					+parseInt(data.sum_je10000_20000_userNum)+parseInt(data.sum_je5000_10000_userNum)
					+parseInt(data.sum_je2000_5000_userNum)+parseInt(data.sum_je100000_userNum)
					+parseInt(data.sum_je1_2000_userNum);
		var gt2k =  parseInt(data.sum_je50000_100000_userNum) +parseInt(data.sum_je20000_50000_userNum)
					+parseInt(data.sum_je10000_20000_userNum)+parseInt(data.sum_je5000_10000_userNum)
					+parseInt(data.sum_je2000_5000_userNum)+parseInt(data.sum_je100000_userNum);
		var gt2k_zb;
		
		if(all_yhsl==0||gt2k==0){
			gt2k_zb=0;
		}else{
			gt2k_zb=formatCurrency((gt2k/all_yhsl)*100,true);
		}
		if(all_yhsl==0 && postData.cityId==""){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据。";
		}
		if(all_yhsl==0 && postData.cityId!=""){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#gzsqjyhCityText").val()+"无数据";
		}
		if(all_yhsl!=0){
			if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#provinceName").val()+"充值有价卡金额大于2000元的用户数量"+gt2k+"个,占所有充值有价卡用户数量的"+gt2k_zb+"%。" +
					"其中，充值金额10万元以上的用户有"+formatCurrency(data.sum_je100000_userNum,false)+"个("+formatCurrency(data.sum_gt100000_tradeAmt,true)+"元)，"+
					"充值金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_userNum,false)+"个("+formatCurrency(data.sum_bt50000_100000_tradeAmt,true)+"元)，" +
					"充值金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_userNum,false)+"个("+formatCurrency(data.sum_bt20000_50000_tradeAmt,true)+"元)。";
			}else if(postData.cityId!=""){
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#gzsqjyhCityText").val()+"充值有价卡金额大于2000元的用户数量"+gt2k+"个,占所有充值有价卡用户数量的"+gt2k_zb+"%。" +
					"其中，充值金额10万元以上的用户有"+formatCurrency(data.sum_je100000_userNum,false)+"个("+formatCurrency(data.sum_gt100000_tradeAmt,true)+"元)，"+
					"充值金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_userNum,false)+"个("+formatCurrency(data.sum_bt50000_100000_tradeAmt,true)+"元)，" +
					"充值金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_userNum,false)+"个("+formatCurrency(data.sum_bt20000_50000_tradeAmt,true)+"元)。";
			}else{
				text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+
					"，"+$("#provinceName").val()+"充值有价卡金额大于2000元的用户数量"+gt2k+"个,占所有充值有价卡用户数量的"+gt2k_zb+"%。" +
					"其中，充值金额10万元以上的用户有"+formatCurrency(data.sum_je100000_userNum,false)+"个("+formatCurrency(data.sum_gt100000_tradeAmt,true)+"元)，"+
					"充值金额在5万~10万之间的用户有"+formatCurrency(data.sum_je50000_100000_userNum,false)+"个("+formatCurrency(data.sum_bt50000_100000_tradeAmt,true)+"元)，" +
					"充值金额在2万~5万之间的用户有"+formatCurrency(data.sum_je20000_50000_userNum,false)+"个("+formatCurrency(data.sum_bt20000_50000_tradeAmt,true)+"元)。";
			}
		}
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#gzsqjyhCityText").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	$('#'+id).html(text);
	$("#gzsqjyjkDetailTable_Con").html(text);
}

/**
 * 用户充值有价卡金额统计(top10)主函数
 */
function loadYhhzyjkTop10Chart(){
	var postData = getHz_QueryParam();
    var id = "yhhzTop10TelCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhhzTop10TelCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYhhzyjkTop10Chart(id,data.data);
            loadAuditYhhzyjkTop10Conclusion("yhhzTop10TelChart_Con",data.data,postData);
            
            var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			}
            
        }
    });
}
/**
 * 用户充值有价卡金额统计(top10)柱状图
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
                text: '用户充值有价卡金额（元）',
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
            name: '用户充值有价卡金额',
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
				top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
				top3Tel = top3Tel+obj.trade_msisdn;
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
					top3Tel = top3Tel+obj.trade_msisdn;
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)、";
				}
				i++;
			});
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
					top3Tel = top3Tel+obj.trade_msisdn;
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
			+ "，" + $("#provinceName").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ "，分别获赠有价卡" + top3Num ;
		}else if(postData.cityId!=""){
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ "，分别获赠有价卡" + top3Num ;
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ ";分别获赠有价卡" + top3Num;
		}
		 
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#yhhzCityText").val()+"无数据";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据";
	}
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
}

function loadYhhzyjkDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	var tableColNames = [ '审计起始月','审计结束月','省(市)','手机号码',
	                      '累计充值有价卡数量','累计充值有价卡金额(元)'];
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
    tableMap.name = "yjk_trade_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_trade_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    
   // $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhhzyjkDetailTable",
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
			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width() );
			$("#"+pagerId).css("width", $("#"+pagerId).width());
			
			var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
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
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getCityList",
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
	        //重新加载图形及结论
	        reloadYhhzChartsAndCon(yhhzYjkCityId);
	        //重新加载数据表
	        reloadYhhzDetil("yhhzTop10TelTable",$(this).attr("code"));
	        reloadYhczTelDetailTable("yhczTable",yhhzYjkCityId);
	        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
	            callback();
	        }
	    });
}
function reloadYhczTelDetailTable(tableId,yhhzYjkCityId){
	var postData = getHz_QueryParam(); 
	postData.cityId = yhhzYjkCityId;
	loadYhczListCon("yhczTable","yhczTablePageBar",yhhzYjkCityId);
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhczDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
//用户获赠下拉城市列表时的明细表
function reloadYhhzDetil(tableId,yhhzYjkCityId) {
	var postData = getHz_QueryParam(); 
	postData.cityId = yhhzYjkCityId;
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhhzyjkDetailTable";
    jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//用户获赠赠送地市统计图(chart)
function reloadYhhzChartsAndCon(yhhzYjkCityId){
	var postData = getHz_QueryParam();
    var id = "yhhzTop10TelCharts";
    postData.cityId = yhhzYjkCityId;
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhhzTop10TelCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineYhhzyjkTop10Chart(id,data.data);
            reloadYhhzyjkTop10Conclusion("yhhzTop10TelChart_Con",data.data,postData);
            
            var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
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
				top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
				top3Tel = top3Tel+obj.trade_msisdn;
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
					top3Tel = top3Tel+obj.trade_msisdn;
				}else{
					top3Tel = top3Tel+obj.trade_msisdn+"、";
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)、";
				}
				i++;
			});
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3Num = top3Num+formatCurrency(obj.sum_yjk_trade_num,false)+"张("+formatCurrency(obj.sum_yjk_trade_amt,true)+"元)";
					top3Tel = top3Tel+obj.trade_msisdn;
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
			+ "，" + $("#provinceName").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ "，分别获赠有价卡" + top3Num ;
		}else if(postData.cityId!=""){
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#yhhzCityText").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ "，分别获赠有价卡" + top3Num ;
		}else{
			text =  timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"充值金额异常排名前三的手机号码是" + top3Tel
			+ "，分别获赠有价卡" + top3Num;
		}
		 
	}else if(postData.cityId!=""){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#yhhzCityText").val()+"无数据";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据";
	}
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
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
    var url = $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getCityDetailPagerList";
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
                          '充值手机号',  '充值日期', 
                          '有价卡赠送涉及的营销案编号', '营销案名称' ];
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
    tableMap.name = "countatal";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "trade_msisdn";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tradedate";
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
    
   
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getCityDetailPagerList",
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
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getCityList",
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
function loadYhczListCon(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	$.ajax({
        url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhczDetailTable",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var text = "";
        	if(data.totalPages!=0){
        		if(cityId!=null||cityId!=""){
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
						+ "，" + $("#yhhzCityText").val()+"同一个月内向同一个用户充值的有价卡数量大于10张或者金额大于1000元的数据如下：";
        		}else{
        			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
						+ "，" + $("#provinceName").val()+"同一个月内向同一个用户充值的有价卡数量大于10张或者金额大于1000元的数据如下：";
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
        	
        	$("#yhczTable_Con").html(text);
        }
		
    });
}
function loadYhczTelDetailTable(tableId,pagerId,cityId){
	var postData = getHz_QueryParam();
	postData.cityId = cityId;
	loadYhczListCon(tableId,pagerId,cityId);
	var tableColNames = [ '审计月','省(市)','充值月份','被充值手机号码用户标识','累计充值有价卡数量','累计充值金额（元）'];
	
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
     tableMap.name = "trade_mon";
     tableMap.align = "center";
     tableColModel.push(tableMap);
     
     tableMap = new Object();
     tableMap.name = "charge_msisdn";
     tableMap.align = "center";
     tableColModel.push(tableMap);
     
     tableMap = new Object();
     tableMap.name = "yjk_num";
     tableMap.align = "center";
     tableColModel.push(tableMap);
     
     
     tableMap = new Object();
     tableMap.name = "yjk_amt";
     tableMap.align = "center";
     tableMap.formatter = function(cellvalue, options, rowObject) {
         return toDecimal2(cellvalue);
     };
     tableColModel.push(tableMap);
     
     jQuery("#"+tableId).jqGrid({
     	url : $.fn.cmwaurl() + "/yjkzsjzdyc3002/3002/getYhczDetailTable",
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
 			$("#"+tableId).setGridWidth($("#"+tableId).parent().parent().parent().width() );
 			$("#"+pagerId).css("width", $("#"+pagerId).width() );
 			
 			var yhhzTjdiv_height = $("#yhhzTjdiv").height();
			var gzsqjyhTjdiv_height = $("#gzsqjyhTjdiv").height();
			if(yhhzTjdiv_height>=gzsqjyhTjdiv_height){
				$("#gzsqjyhTjdiv").css("height",$("#yhhzTjdiv").css("height"));
			}else{
				$("#yhhzTjdiv").css("height",$("#gzsqjyhTjdiv").css("height"));
			}
 			
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
	$("#yhhzTop10TelTable").setGridWidth($("#yhhzTop10TelTableDiv_class").width()-1);
});
$(window).resize(function(){
	$("#gzsqjyjkDetailTable").setGridWidth($("#gzsqjyjkDetailTableDiv_class").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});