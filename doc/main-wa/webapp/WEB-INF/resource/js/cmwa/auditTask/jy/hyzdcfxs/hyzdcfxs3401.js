$(document).ready(function() {
	
	//step 1：个性化本页面的特殊风格
	//	initStyle();
	//step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
		initEvent();
//	step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
		initDefaultParams();
	//step 4：触发页面默认加载函数
			initDefaultData();
});

/**
 * step 4：触发页面默认加载函数
 */
function initDefaultData(){
	loadShuangZhuCharts();
	//补贴金额 -合约终端重复销售地市分布
	loadbtjeChart();
	//销售数量 -合约终端重复销售地市分布
	//loadxsslChart();
	//明细地市列表
	loadMxCityList();
}

function initEvent(){
	//点击重新加载图形
	$("#btje3401Charts").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//折线图  渠道养卡用户数量波动趋势
		loadbtjeChart();
    });
	
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_合约终端销售异常_合约终端重复销售_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/hyzdcfxs/3401/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	//合约终端重复销售次数分布统计-数据表
	$("#hyzdcfxsNum3401Detail_Table").on('click',function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		getHyzdcfxsNum3401Detail_Table("hz_hyzdcfxs_table","hz_hyzdcfxs_pageBar");
	 });
	
	//导出合约终端重复销售次数分布统计-数据表
    $("#exporthyzdcfxsTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#hz_hyzdcfxs_table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_合约终端销售异常_合约终端重复销售_汇总_重复销售终端统计.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/hyzdcfxs/3401/exportHyzdcfxsDetail?" + $.param(getHz_QueryParam());

       // exportHyzdcfxsDetail(exportFileName);
    });
    
    function exportHyzdcfxsDetail(exportFileName){
    	var postData = getHz_QueryParam();
    	var sj_month = postData.sj_month;
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/hyzdcfxs/3401/exportHyzdcfxsDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&sj_month="+sj_month +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    //销售数量数据表
    $("#hyzdcfxsdsfb3401Table").on('click',function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		getxssl3401Detail_Table("hz_xssl_table","hz_xssl_pageBar");
	 });
    
    $("#xssl3401Charts").on('click',function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

    	//销售数量 -合约终端重复销售地市分布
    	loadxsslChart();
	 });
    
  //导出合约终端重复销售次数分布统计-数据表
    $("#exportXsslTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#hz_xssl_table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var exportFileName = "{0}_合约终端销售异常_合约终端重复销售_汇总_地市.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/hyzdcfxs/3401/exportXssl3401Detail?" + $.param(getHz_QueryParam());

        //exportXssl3401Detail(exportFileName);
    });
    
    function exportXssl3401Detail(exportFileName){
    	var postData = getHz_QueryParam();
    	var sj_month = postData.sj_month;
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/hyzdcfxs/3401/exportXssl3401Detail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&sj_month="+sj_month +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    }
    
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
        loadMxCityDetailGridData();
    });
    
    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");

        $("#currTab").val("hz");
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
        clearHideFormInput();
    });
    
    // 明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {
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
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
  	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	$("#mx_startMonth").val($.fn.timeStyle(beforeAcctMonth));
    $("#mx_endMonth").val($.fn.timeStyle(endAcctMonth));
    $("#mx_cityCode").val("");
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

function reloadGlobalData() {
	//查询时重新加载折线图
	loadShuangZhuCharts();
	//补贴金额 -合约终端重复销售地市分布
	loadbtjeChart();
	//销售数量 -合约终端重复销售地市分布
	loadxsslChart();
	
	//合约终端重复销售次数分布统计 数据表
	reloadgetHyzdcfxsNum3401Detail_Table();
	
	reloadgetxssl3401Detail_Table();
	
}

function reloadgetHyzdcfxsNum3401Detail_Table(){
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/hyzdcfxs/3401/getHyzdcfxsNum3401Detail_Table";
    jQuery("#hz_hyzdcfxs_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function reloadgetxssl3401Detail_Table(){
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/hyzdcfxs/3401/getxssl3401Detail_Table";
    jQuery("#hz_xssl_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
/**
 *	step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
 */
function initDefaultParams() {
	var postData = {};
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	var provinceCode = $.fn.GetQueryString("provinceCode");
	var auditId = $.fn.GetQueryString("auditId");
	var tab = $.fn.GetQueryString("tab");
	var provList = $.fn.provList();
	var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
	$(".tab-sub-nav ul li a").each(function() {
		var link = $(this).attr("href") + urlParams;
		$(this).attr("href", link);
	});
    
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
		$("#currTab").val("mx");
		$("#mx_tab").click();
	}
	$.ajax({
		url : $.fn.cmwaurl() + "/hyzdcfxs/3401/initDefaultParams",
		async : false,
		dataType : 'json',
		data : postData,
		success : function(data) {
			$('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
			$('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
			$('.form_datetime').datetimepicker('setStartDate',new Date(beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6)));
            $('.form_datetime').datetimepicker('setEndDate',new Date(endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6)));
			
			$('#mx_startMonth').val($.fn.timeStyle(beforeAcctMonth));
			$('#mx_endMonth').val($.fn.timeStyle(endAcctMonth));
			
			$('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
		}
	});
}

//销售数量 -合约终端重复销售地市分布
function loadxsslChart(){
	var postData = getHz_QueryParam();
	var id = "xshl3401_Charts";
	$.ajax({
        url : $.fn.cmwaurl() + "/hyzdcfxs/3401/loadxsslChart",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var dataList = data.data;
        	var dataCon = data.dataCon;
            drawXsllDuijiChart(id,dataList);
            loadXsllDuijiChartConclusion("xshl3401_Con",dataCon,postData);
        }
    });
}
function loadXsllDuijiChartConclusion(id,dataCon,postData){
	var text = "";
	var count_trmnl_imei = 0;
	var sum_sell_num = 0;
	var sum_bt_je =0;
	var sum_sell_num_je = 0;
	var top3_dishi="";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(dataCon.length!=0){
		$.each(dataCon, function(i, obj) {
			count_trmnl_imei = count_trmnl_imei+obj.count_trmnl_imei;
			sum_sell_num = sum_sell_num+obj.sum_sell_num;
			sum_bt_je = sum_bt_je+obj.sum_bt_je;
		});
		$.each(dataCon, function(i, obj) {
				top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"、";
	    	i++;
	    	if(i==3){
	    		return false;
	    	}
	    });
		top3_dishi = top3_dishi.substring(0, top3_dishi.length-1)+"。";
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+"有" + formatCurrency(count_trmnl_imei,false)
					+ "台合约终端有重复销售情况，累计销售" + formatCurrency(sum_sell_num,false) + "次，累计补贴"+formatCurrency(sum_bt_je,true)+"元";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"有" + formatCurrency(count_trmnl_imei,false)
				+ "台合约终端有重复销售情况，累计销售" + formatCurrency(sum_sell_num,false) + "次，累计补贴"+formatCurrency(sum_bt_je,true)+"元。按照重复销售的累计次数排名前三的地市:"+top3_dishi;
		}
	}else{
		text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据";
	}
	
	$('#'+id).html(text);
}
function drawXsllDuijiChart(id,dataList){
	var dishi = [];
	var sell_num_2 = []; var sell_num_3 = [];
	var sell_num_4 = []; var sell_num_5 = [];
	var sell_num_gt5 = [];
	for(var i=0;i<dataList.length;i++){
		dishi.push(dataList[i].CMCC_PRVD_NM_SHORT);
		if(dataList[i].sum_sell_num2 || dataList[i].sum_sell_num2==0){
			if(dataList[i].sum_sell_num2==0){
				sell_num_2.push("0");
			}else{
				sell_num_2.push(dataList[i].sum_sell_num2);
			}
		}
		if(dataList[i].sum_sell_num3||dataList[i].sum_sell_num3==0){
			if(dataList[i].sum_sell_num3==0){
				sell_num_3.push("0");
			}else{
				sell_num_3.push(dataList[i].sum_sell_num3);
			}
		}
		if(dataList[i].sum_sell_num4 || dataList[i].sum_sell_num4==0){
			if(dataList[i].sum_sell_num4==0){
				sell_num_4.push("0");
			}else{
				sell_num_4.push(dataList[i].sum_sell_num4);
			}
		}
		if(dataList[i].sum_sell_num5 || dataList[i].sum_sell_num5==0){
			if(dataList[i].sum_sell_num5==0){
				sell_num_5.push("0");
			}else{
				sell_num_5.push(dataList[i].sum_sell_num5);
			}
			
		}
		if(dataList[i].sum_sell_num_gt5 || dataList[i].sum_sell_num_gt5==0){
			if(dataList[i].sum_sell_num_gt5==0){
				sell_num_gt5.push("0");
			}else{
				sell_num_gt5.push(dataList[i].sum_sell_num_gt5);
			}
			
		}
	}
	$('#'+id).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: dishi
        },
        yAxis: {
            min: 0,
            title: {
                text: '累计销售数量'
            }
        },
        legend: {
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },
        tooltip: {
        	headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.0f}(次) </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: false,
                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                    style: {
                        textShadow: '0 0 3px black'
                    }
                }
            }
        },
        series: [{
            name: '销售2次终端',
            data: sell_num_2
        }, {
            name: '销售3次终端',
            data: sell_num_3
        }, {
            name: '销售4次终端',
            data: sell_num_4
        },{
        	name: '销售5次终端',
            data: sell_num_5
        },{
            name: '销售5次以上终端',
            data: sell_num_gt5
        },]
    });
}

//补贴金额 -合约终端重复销售地市分布
function loadbtjeChart(){
	var postData = getHz_QueryParam();
	var id = "btje3401_Charts";
	$.ajax({
        url : $.fn.cmwaurl() + "/hyzdcfxs/3401/loadbtjeChart",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var dataList = data.data;
        	var dataCon = data.dataCon;
        	var tableConData = data.tableConData;
            drawDuijiChart(id,dataList);
            loadDuijiChartConclusion("btje3401_Con",dataCon,tableConData,postData);
        }
    });
}
//补贴金额 -合约终端重复销售地市分布  结论
function loadDuijiChartConclusion(id,dataCon,tableConData,postData){
	var text1 = "";
	var text2 = "";
	var count_trmnl_imei = 0;
	var sum_bt_je = 0;
	var top3_dishi="";
	var tbcount_trmnl_imei;var tbsum_sell_num; var tbsum_bt_je;
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	$.each(tableConData, function(i, obj){
		tbsum_sell_num = obj.tbsum_sell_num;
		tbcount_trmnl_imei = obj.tbcount_trmnl_imei;
		tbsum_bt_je = obj.tbsum_bt_je;
	});
	if(dataCon.length!=0){
		$.each(dataCon, function(i, obj) {
			count_trmnl_imei = count_trmnl_imei+obj.count_trmnl_imei;
			sum_bt_je = sum_bt_je+obj.sum_bt_je;
				top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"、";
	    	i++;
	    	if(i==3){
	    		return false;
	    	}
	    });
		top3_dishi = top3_dishi.substring(0, top3_dishi.length-1)+"。";
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text1 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+"有" + formatCurrency(count_trmnl_imei,false)
					+ "台合约终端有重复销售情况，累计销售"+ formatCurrency(tbsum_sell_num,false) + "次,累计补贴"+formatCurrency(tbsum_bt_je,true)+"元。";
			text2 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+"有" + tbcount_trmnl_imei
					+ "台合约终端有重复销售情况，累计销售" + formatCurrency(tbsum_sell_num,false) + "次,累计补贴"+formatCurrency(tbsum_bt_je,true)+"元。";
		}else{	
			text1 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+"有" + tbcount_trmnl_imei
					+ "台合约终端有重复销售情况，累计销售" + formatCurrency(tbsum_sell_num,false) + "次,累计补贴" + formatCurrency(tbsum_bt_je,true) + "元。按照重复销售的补贴金额排名前三的地市:"+top3_dishi;
			text2 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+"有" +tbcount_trmnl_imei
					+ "台合约终端有重复销售情况，累计销售" + formatCurrency(tbsum_sell_num,false) + "次,累计补贴"+formatCurrency(tbsum_bt_je,true)+"元。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text1 = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据";
		text2 = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据";
	}else{
		text1 = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据";
		text2 = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"无数据";
	}
	
	$('#'+id).html(text1);
	$("#hezdcfxs_jielun").html(text2);
}
//补贴金额 -合约终端重复销售地市分布 图形
function drawDuijiChart(id,dataList){
	var dishi = [];
	var je_2 = []; var je_3 = [];
	var je_4 = []; var je_5 = [];
	var je_gt5 = [];var je=[];
	for(var i=0;i<dataList.length;i++){
		dishi.push(dataList[i].CMCC_PRVD_NM_SHORT);
		if(dataList[i].sum_trmnl_fee_cost2||dataList[i].sum_trmnl_fee_cost2=="0.00"){
			if(dataList[i].sum_trmnl_fee_cost2=="0.00"){
				je_2.push("0.00");
			}else{
				je_2.push(dataList[i].sum_trmnl_fee_cost2);
			}
		}
		if(dataList[i].sum_trmnl_fee_cost3||dataList[i].sum_trmnl_fee_cost3=="0.00"){
			if(dataList[i].sum_trmnl_fee_cost3=="0.00"){
				je_3.push("0.00");
			}else{
				je_3.push(dataList[i].sum_trmnl_fee_cost3);
			}
		}
		if(dataList[i].sum_sell_num4||dataList[i].sum_sell_num4=="0.00"){
			if(dataList[i].sum_sell_num4=="0.00"){
				je_4.push("0.00");
			}else{
				je_4.push(dataList[i].sum_trmnl_fee_cost4);
			}
		}
		if(dataList[i].sum_sell_num5||dataList[i].sum_sell_num5=="0.00"){
			if(dataList[i].sum_sell_num5=="0.00"){
				je_5.push("0.00");
			}else{
				je_5.push(dataList[i].sum_trmnl_fee_cost5);
			}
		}
		if(dataList[i].sum_sell_num_gt5 || dataList[i].sum_sell_num_gt5=="0.00"){
			if(dataList[i].sum_sell_num_gt5=="0.00"){
				je_gt5.push("0.00");
			}else{
				je_gt5.push(dataList[i].sum_trmnl_fee_cost_gt5);
			}
		}
	}
	$('#'+id).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: dishi
        },
        yAxis: {
            min: 0,
            title: {
                text: '累计补贴金额(元)'
            },
            labels : {
				format : '',
			}
        },
        legend: {
            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },
        tooltip: {
        	footerFormat:function(){
        		if(point.y==0.00){
        			$("#tr").hide(); 
        		}
        	},
        	headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat:  '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            			  '<td style="padding:0"><b>{point.y:.2f} '+'(元)</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: false,
                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
                    style: {
                        textShadow: '0 0 3px black'
                    }
                }
            }
        },
        series: [{
            name: '销售2次终端',
            data: je_2
        }, {
            name: '销售3次终端',
            data: je_3
        }, {
            name: '销售4次终端',
            data: je_4
        },{
        	name: '销售5次终端',
            data: je_5
        },{
            name: '销售5次以上终端',
            data: je_gt5
        },]
    });
}


/**
 * 加载双柱图
 */
function loadShuangZhuCharts(){
	 var postData = getHz_QueryParam();
	 var id = "hyzdcfxsNum3401_Chart";
	 $.ajax({
	        url : $.fn.cmwaurl() + "/hyzdcfxs/3401/hyzdcfxsNum",
	        dataType : 'json',
	        data : postData,    
	        success : function(data) {
	        	var dataList = data.data;
	        	var sum_map = data.sum_map;
	            drawShuangZhuChart(id,dataList);
	            loadChartConclusion("hyzdcfxsNum3401Chart_conclusion",sum_map,postData);
	            loadChartTable("hyzdcfxsNum3401Chart_Table", dataList);
	        }
	    });
}

function loadChartConclusion(id,sum_map,postData){
	var text="";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(sum_map!=null){
		var sum_bt_je=0.00;var sum_sell_num=0;var count_trmnl_imei=0;
		if(sum_map.count_trmnl_imei==""||sum_map.count_trmnl_imei==undefined||sum_map.count_trmnl_imei==NaN){
			count_trmnl_imei=0;
		}else{
			count_trmnl_imei=sum_map.count_trmnl_imei;
		}
		
		if(sum_map.sum_bt_je==""||sum_map.sum_bt_je==undefined||sum_map.sum_bt_je==NaN){
			sum_bt_je=0.00;
		}else{
			sum_bt_je = sum_map.sum_bt_je;
		}
		
		if(sum_map.sum_sell_num==""||sum_map.sum_sell_num==undefined||sum_map.sum_sell_num==NaN){
			sum_sell_num=0;
		}else{
			sum_sell_num=sum_map.sum_sell_num;
		}
		
		if(sum_bt_je==0.00 && sum_sell_num==0 && count_trmnl_imei==0){
			text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据";
		}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text=timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+"，"+$("#provinceName").val()+"有"+formatCurrency(count_trmnl_imei,false)+"台合约终端重复销售情况,累计销售"+formatCurrency(sum_sell_num,false)
			+"次,累计补贴"+formatCurrency(sum_bt_je,true)+"元。";
		}else{
			text=timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+"，"+$("#provinceName").val()+"有"+formatCurrency(count_trmnl_imei,false)+"台合约终端重复销售情况,累计销售"+formatCurrency(sum_sell_num,false)
			+"次,累计补贴"+formatCurrency(sum_bt_je,true)+"元。";
		}
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据";
	}
	
	 
	$("#"+id).html(text);
	$("#shujubiao_jielun").html(text);
	
}

function loadChartTable(tableId,dataList){
	var sell_num = [];
    var sum_trmnl_fee_cost = [];
    var count_trmnl_imei = [];
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>同一终端在审计期间销售次数</th>" +
					"<th>终端数量</th>" +
					"<th>累计销售次数</th>" +
					"<th>累计补贴金额（元）</th>" +
					"<th>平均单次补贴金额（元）</th></tr>";
	var sum = 0 ;var sum1 = 0 ;
	var gt5_num=0; var gt5_amt=0; var gt5_imei_num=0;
    var eq5_num=0; var eq5_amt=0; var eq5_imei_num=0;
    var eq4_num=0; var eq4_amt=0; var eq4_imei_num=0;
    var eq3_num=0; var eq3_amt=0; var eq3_imei_num=0;
    var eq2_num=0; var eq2_amt=0; var eq2_imei_num=0;
    for(var i=0;i<dataList.length;i++){
    	if(dataList[i].SELL_NUM>5){
    		gt5_num = gt5_num+dataList[i].count_sell_num;
    		gt5_amt = gt5_amt+dataList[i].sum_trmnl_fee_cost;
    		gt5_imei_num = gt5_imei_num + dataList[i].count_trmnl_imei;
    	}
    	if(dataList[i].SELL_NUM==5){
    		eq5_num = eq5_num+dataList[i].count_sell_num;
    		eq5_amt = eq5_amt+dataList[i].sum_trmnl_fee_cost;
    		eq5_imei_num = eq5_imei_num + dataList[i].count_trmnl_imei;
    	}
    	if(dataList[i].SELL_NUM==4){
    		eq4_num = eq4_num+dataList[i].count_sell_num;
    		eq4_amt = eq4_amt+dataList[i].sum_trmnl_fee_cost;
    		eq4_imei_num = eq4_imei_num + dataList[i].count_trmnl_imei;
    	}
    	if(dataList[i].SELL_NUM==3){
    		eq3_num = eq3_num+dataList[i].count_sell_num;
    		eq3_amt = eq3_amt+dataList[i].sum_trmnl_fee_cost;
    		eq3_imei_num = eq3_imei_num + dataList[i].count_trmnl_imei;
    	}
    	if(dataList[i].SELL_NUM==2){
    		eq2_num = eq2_num+dataList[i].count_sell_num;
    		eq2_amt = eq2_amt+dataList[i].sum_trmnl_fee_cost;
    		eq2_imei_num = eq2_imei_num+dataList[i].count_trmnl_imei;
    	}
    	
    }
    sell_num.push(gt5_num,eq5_num,eq4_num,eq3_num,eq2_num);
    sum_trmnl_fee_cost.push(gt5_amt,eq5_amt,eq4_amt,eq3_amt,eq2_amt);
    count_trmnl_imei.push(gt5_imei_num,eq5_imei_num,eq4_imei_num,eq3_imei_num,eq2_imei_num);
    //累计销售台数
    var sum_count_trmnl_imei=count_trmnl_imei[4]+count_trmnl_imei[3]+count_trmnl_imei[2]+count_trmnl_imei[1]+count_trmnl_imei[0];
    //累计补贴次数
    var sum_sell_num=sell_num[4]+sell_num[3]+sell_num[2]+sell_num[1]+sell_num[0];
    //累计补贴金额
    var sum_sum_trmnl_fee_cost=sum_trmnl_fee_cost[4]+sum_trmnl_fee_cost[3]+sum_trmnl_fee_cost[2]+sum_trmnl_fee_cost[1]+sum_trmnl_fee_cost[0];
    
    var avg_2;var avg_3; var avg_4="0";var avg_5;var avg_gt5;
    if(sell_num[4]==0||sum_trmnl_fee_cost[4]==0||sum_trmnl_fee_cost[4]==""||sell_num[4]==""){
    	avg_4=0.00.toFixed(2);
    }else{
    	avg_4=(sum_trmnl_fee_cost[4]/sell_num[4]).toFixed(2);
    }
    if(sell_num[3]==0||sum_trmnl_fee_cost[3]==0||sell_num[3]==""||sum_trmnl_fee_cost[3]==""){
    	avg_3=0.00.toFixed(2);
    }else{
    	avg_3 = (sum_trmnl_fee_cost[3]/sell_num[3]).toFixed(2);
    }
    if(sell_num[2]==0||sum_trmnl_fee_cost[2]==0||sell_num[2]==""||sum_trmnl_fee_cost[2]==""){
    	avg_2=0.00.toFixed(2);
    }else{
    	avg_2 = (sum_trmnl_fee_cost[2]/sell_num[2]).toFixed(2);
    }
    if(sell_num[1]==0||sum_trmnl_fee_cost[1]==0||sum_trmnl_fee_cost[1]==""||sell_num[1]==""){
    	avg_5 =0.00.toFixed(2);
    }else{
    	avg_5 = (sum_trmnl_fee_cost[1]/sell_num[1]).toFixed(2);
    }
    if(sell_num[0]==0||sell_num[0]==""||sum_trmnl_fee_cost[0]==""||sum_trmnl_fee_cost[0]==0){
    	avg_gt5=0.00.toFixed(2);
    }else{
    	avg_gt5 = (sum_trmnl_fee_cost[0]/sell_num[0]).toFixed(2);
    }
    var avg_danciBTje=0.00;
    if(sum_sell_num==0||sum_sum_trmnl_fee_cost==0){
    	avg_danciBTje=0.00;
    }else{
    	avg_danciBTje= sum_sum_trmnl_fee_cost.toFixed(2)/sum_sell_num;
    }
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+"2次"+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(count_trmnl_imei[4],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sell_num[4],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sum_trmnl_fee_cost[4],true)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(avg_4,true)+"</td>";
		htmlstr = htmlstr+"</tr>";
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+"3次"+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(count_trmnl_imei[3],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sell_num[3],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sum_trmnl_fee_cost[3],true)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(avg_3,true)+"</td>";
		htmlstr = htmlstr+"</tr>";
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+"4次"+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(count_trmnl_imei[2],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sell_num[2],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sum_trmnl_fee_cost[2],true)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(avg_2,true)+"</td>";
		htmlstr = htmlstr+"</tr>";
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+"5次"+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(count_trmnl_imei[1],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sell_num[1],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sum_trmnl_fee_cost[1],true)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(avg_5,true)+"</td>";
		htmlstr = htmlstr+"</tr>";
		
		htmlstr = htmlstr+"<tr>";
		htmlstr = htmlstr+"<td>"+"5次以上"+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(count_trmnl_imei[0],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sell_num[0],false)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(sum_trmnl_fee_cost[0],true)+"</td>";
		htmlstr = htmlstr+"<td>"+formatCurrency(avg_gt5,true)+"</td>";
		htmlstr = htmlstr+"</tr>";
	htmlstr = htmlstr+"<tr><th>合计</th>" +
	"<th>"+formatCurrency(sum_count_trmnl_imei,false)+"</th>"+
	"<th>"+formatCurrency(sum_sell_num,false)+"</th>"+
	"<th>"+formatCurrency(sum_sum_trmnl_fee_cost,true)+"</th>"+
	"<th>"+formatCurrency(avg_danciBTje,true)+"</th>";
	htmlstr = htmlstr+"</table>";
	$("#"+tableId).html(htmlstr);
}


function drawShuangZhuChart(id,dataList){
    var count_sell_num = [];
    var sell_num_x = ['销售5次以上','销售5次','销售4次','销售3次','销售2次'];
    var sell_num = [];
    var sum_trmnl_fee_cost = [];
    var gt5_num=0; var gt5_amt=0;
    var eq5_num=0; var eq5_amt=0;
    var eq4_num=0; var eq4_amt=0;
    var eq3_num=0; var eq3_amt=0;
    var eq2_num=0; var eq2_amt=0;
    for(var i=0;i<dataList.length;i++){
    	if(dataList[i].SELL_NUM>5){
    		gt5_num = gt5_num+dataList[i].count_sell_num;
    		gt5_amt = gt5_amt+dataList[i].sum_trmnl_fee_cost;
    	}
    	if(dataList[i].SELL_NUM==5){
    		eq5_num = eq5_num+dataList[i].count_sell_num;
    		eq5_amt = eq5_amt+dataList[i].sum_trmnl_fee_cost;
    	}
    	if(dataList[i].SELL_NUM==4){
    		eq4_num = eq4_num+dataList[i].count_sell_num;
    		eq4_amt = eq4_amt+dataList[i].sum_trmnl_fee_cost;
    	}
    	if(dataList[i].SELL_NUM==3){
    		eq3_num = eq3_num+dataList[i].count_sell_num;
    		eq3_amt = eq3_amt+dataList[i].sum_trmnl_fee_cost;
    	}
    	if(dataList[i].SELL_NUM==2){
    		eq2_num = eq2_num+dataList[i].count_sell_num;
    		eq2_amt = eq2_amt+dataList[i].sum_trmnl_fee_cost;
    		
    	}
    	
    }
    sell_num.push(gt5_num,eq5_num,eq4_num,eq3_num,eq2_num);
    sum_trmnl_fee_cost.push(gt5_amt,eq5_amt,eq4_amt,eq3_amt,eq2_amt);
    
    Highcharts.setOptions({
        lang: {
            numericSymbols: null
        }
    });
	
	$('#'+id).highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        subtitle: {
            text: ''
        },
        xAxis: {
        	categories: sell_num_x,
            crosshair: true
        },
        yAxis : [ {
			labels : {
				format : '',
			},
			title : {
				text : '累计销售次数',
			},
		}, {
			labels : {
				format : '',
			},
			title : {
				text : '累计补贴金额（元）',
			},
			opposite: true
		} ],
        tooltip: {
            shared: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [
                 {
     				name : '累计销售次数',
     				color : '#f2ca68',
     				yAxis:0,
     				tooltip : {
     	            	valueSuffix : '',
     	            	valueDecimals: 0
     				},
     				data : sell_num,
     			}, {
     				name : '累计补贴金额(元)',
     				color : '#65d3e3',
     				tooltip : {
     	            	valueSuffix : '',
     	            	valueDecimals: 2
     				},
     				yAxis:1,
     				data : sum_trmnl_fee_cost,
     			} 
        ]
    });
}

function getxssl3401Detail_Table(tableId,pagerId){
	var postData = getHz_QueryParam();
	var tableColNames = [ '审计月','地市','终端IMEI','累计销售次数','累计补贴金额（元）','话费补贴成本(元)',
	                      '终端补贴成本（元）'];
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
    tableMap.name = "trmnl_imei";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_sell_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_bt_je";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_fee_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_trmnl_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);

    jQuery("#"+tableId).jqGrid({
        url: $.fn.cmwaurl() + "/hyzdcfxs/3401/getxssl3401Detail_Table",
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
        }
    });
}

function getHyzdcfxsNum3401Detail_Table(tableId,pagerId){
	var postData = getHz_QueryParam();
	var tableColNames = [ '审计月','地市名称','终端IMEI','累计销售次数',
	                      '累计补贴金额（元）','话费补贴成本(元)',
	                      '终端补贴成本（元）','平均单次补贴金额（元）'];
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
    tableMap.name = "trmnl_imei";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_sell_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_bt_je";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_fee_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_trmnl_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "avg_bt_je";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
        url: $.fn.cmwaurl() + "/hyzdcfxs/3401/getHyzdcfxsNum3401Detail_Table",
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
	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	//获取当前时间(年月)
	var date=new Date;  
	var year=date.getFullYear();   
	var month=date.getMonth()+1;  
	month =(month<10 ? "0"+month:month);   
	var mydateAcctMonth = (year.toString()+month.toString());
	//获取当前月份-2的日期
	var mydata_year = mydateAcctMonth.substring(0, 4);
	var mydata_mouth = mydateAcctMonth.substring(4, 6);
		mydata_mouth = Number(mydata_mouth);
	if(mydata_mouth==2){
		mydata_year = mydata_year - 1;
		mydata_mouth = 12;
		mydateAcctMonth = mydata_year+""+mydata_mouth;
	}else if(mydata_mouth==1){
		mydata_year = mydata_year - 1;
		mydata_mouth = 11;
		mydateAcctMonth = mydata_year+""+mydata_mouth;
	}else if(mydata_mouth==12){
		mydata_year = mydata_year;
		mydata_mouth = mydata_mouth-2;
		mydateAcctMonth = mydata_year+""+mydata_mouth;
	}else{
		mydata_year = mydata_year;
		mydata_mouth = mydata_mouth-2;
		mydateAcctMonth = mydata_year+"0"+mydata_mouth;
	}
	
	postData.sj_month = mydateAcctMonth;
//	postData.sj_month = '201703';
	postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
	postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
	postData.provId = $("#provinceCode").val();
	postData.prvdId = $("#provinceCode").val();
	postData.trendType = $("#trendType").val();
	return postData;
}

/**
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/hyzdcfxs/3401/getCityList",
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
        	$("#mx_cityCodeSelect").append(li);
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
 * 刷新明细页面地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/hyzdcfxs/3401/getCityDetailPagerList";
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
	//审计月、省名称、地市名称、终端IMEI、销售渠道标识、销售渠道名称、
	//员工标识、员工名称、用户标识、销售时间、营销案名称、终端补贴成本、话费补贴成本
	
	var tableColNames = [ '审计月','省名称', '地市名称', 
                          '终端IMEI', '销售渠道标识', 
                          '销售渠道名称',  '员工标识','员工名称', 
                          '用户标识', '销售时间','营销案名称',
                          '终端补贴成本（元）','话费补贴成本（元）' ];
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
    tableMap.name = "cmcc_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "trmnl_imei";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "emp_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "emp_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "subs_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sell_dat";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "offer_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "trmnl_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "fee_allow_cost";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/hyzdcfxs/3401/getCityDetailPagerList",
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
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
	
	//获取当前时间(年月)
 	var date=new Date;  
 	var year=date.getFullYear();   
 	var month=date.getMonth()+1;  
 	month =(month<10 ? "0"+month:month);   
 	var mydateAcctMonth = (year.toString()+month.toString());
 	//获取当前月份-2的日期
 	var mydata_year = mydateAcctMonth.substring(0, 4);
 	var mydata_mouth = mydateAcctMonth.substring(4, 6);
 		mydata_mouth = Number(mydata_mouth);
 	if(mydata_mouth==2){
 		mydata_year = mydata_year - 1;
 		mydata_mouth = 12;
 		mydateAcctMonth = mydata_year+""+mydata_mouth;
 	}else if(mydata_mouth==1){
 		mydata_year = mydata_year - 1;
 		mydata_mouth = 11;
 		mydateAcctMonth = mydata_year+""+mydata_mouth;
 	}else if(mydata_mouth==12){
 		mydata_year = mydata_year;
 		mydata_mouth = mydata_mouth-2;
 		mydateAcctMonth = mydata_year+""+mydata_mouth;
 	}else{
 		mydata_year = mydata_year;
 		mydata_mouth = mydata_mouth-2;
 		mydateAcctMonth = mydata_year+"0"+mydata_mouth;
 	}
 	
 	postData.sj_month = mydateAcctMonth;
    postData.mx_startMonth =$("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
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
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});
$(window).resize(function(){
	$("#hz_hyzdcfxs_table").setGridWidth($("#hz_hyzdcfxsDivId_class").width()-1);
});
$(window).resize(function(){
	$("#hz_xssl_table").setGridWidth($("#jtqfstyleone").width()-1);
});

