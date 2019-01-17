$(document).ready(function() {
	//step 1：个性化本页面的特殊风格
	//initStyle();
	//step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
	initEvent();
	//step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
	initDefaultParams();
	//step 4：触发页面默认加载函数
	initDefaultData();
});

//初始化界面加载函数
function initDefaultData(){
	//图形   赠送有价卡集中充值地市分布
	loadZsyjkjzczChart();
	//图形  赠送有价卡集中充值金额排名前十手机号码
	loadZsyjkjzczJeChart();
	
	 //明细地市列表
	loadMxCityList();
	
}

function initEvent(){
	
	//赠送有价卡集中充值地市分布
	$("#zsyjkjzczds_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadZsyjkjzczChart();
    });
	
	//赠送有价卡集中充值地市分布 数据表
	$("#zsyjkjzczds_Table").on('click',function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		getZsyjkjzczdsDetail_Table("zsyjkjzczdsTable","zsyjkjzczdsTablePageBar");
	 });
	
	//赠送有价卡集中充值地市分布 数据表导出
	$("#exportZsyjkjzczdsTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#zsyjkjzczdsTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        
        var postData = {};
        postData.exportFileName = "赠送有价卡集中充值地市分布_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/zsyjkczjzd/exportZsyjkjzczdsDetail?" + $.param(getHZ_QueryParam()) + "&" + $.param(postData);

        
      //  exportZsyjkjzczdsDetail(exportFileName);
    });
	//赠送有价卡集中充值地市分布    数据表导出实现
	function exportZsyjkjzczdsDetail(exportFileName){
		var postData = getHZ_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/zsyjkczjzd/exportZsyjkjzczdsDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	//赠送有价卡集中充值地市分布
	$("#zsyjkjzczje_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadZsyjkjzczJeChart();
    });
	
	//赠送有价卡集中充值地市分布 数据表
	$("#zsyjkjzczje_Table").on('click',function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		getZsyjkjzczjeDetail_Table("zsyjkjzczjeTable","zsyjkjzczjeTablePageBar");
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
    
    //赠送有价卡集中充值金额排名前十手机号码 数据表导出
	$("#exportZsyjkjzczjeTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#zsyjkjzczjeTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        
        var postData = {};
        postData.exportFileName = "{0}赠送有价卡集中充值金额排名前十手机号码_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/zsyjkczjzd/exportZsyjkjzczjeDetail?" + $.param(getHZ_QueryParam()) + "&" + $.param(postData);

       // exportZsyjkjzczjeDetail(exportFileName);
    });
    function exportZsyjkjzczjeDetail(exportFileName){
    	var postData = getHZ_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/zsyjkczjzd/exportZsyjkjzczjeDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
    	
    }
    
    // 导出明细列表
    $("#exportMxDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}赠送有价卡充值集中度_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/zsyjkczjzd/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
    //明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
        if (!mxFormValidator()) {
            return false;
        }
        reLoadCityDetailGridData();
    });
    
    //明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {
        clearHideFormInput();
    });
    
     //清空明细隐藏表单值
    function clearHideFormInput() {
    	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
      	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    	$("#mx_startMonth").val($.fn.timeStyle(beforeAcctMonth));
        $("#mx_endMonth").val($.fn.timeStyle(endAcctMonth));
        $("#mx_cityCode").val("");
        
    }
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
    	//赠送有价卡集中充值地市分布
    	loadZsyjkjzczChart();
    	//赠送有价卡集中充值金额排名前十手机号码
    	loadZsyjkjzczJeChart();
    	
        
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
    
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
        $("#mx_tab").click();
    }
    
    
    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/zsyjkczjzd/initDefaultParams",
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

//查询按钮重新加载
function reloadGlobalData(){
	//赠送有价卡集中充值地市分布
	loadZsyjkjzczChart();
	//赠送有价卡集中充值金额排名前十手机号码
	loadZsyjkjzczJeChart();
	
	//赠送有价卡集中充值地市分布  数据表
	regetZsyjkjzczdsDetail_Table();
	//赠送有价卡集中充值金额排名前十手机号码 数据表
	reloadZsyjkjzczJeDetailTable();
}

//赠送有价卡集中充值地市分布
function regetZsyjkjzczdsDetail_Table() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzdCzdsDetailTable";;
    jQuery("#zsyjkjzczdsTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//赠送有价卡集中充值金额排名前十手机号码
function reloadZsyjkjzczJeDetailTable() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzczjeDetail";;
    jQuery("#zsyjkjzczjeTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}



//赠送有价卡集中充值地市分布
function loadZsyjkjzczChart(){
	var postData = getHZ_QueryParam();
	var id = "zsyjkjzczdsChart";
    $.ajax({
        url : $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzdCzdsChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.dataChar);
            loadAuditConclusion("zsyjkjzczdsChartCon",data,postData);
        }
    });
	
}
//赠送有价卡集中充值地市分布   图形
function drawRankLineChart(id,data){
	var dishi = [];
	var sum_yjk_amt = [];
	var sum_yjk_num = [];
	
	$.each(data, function(i, obj) {
		dishi.push(obj.cmcc_prvd_nm_short);
		sum_yjk_amt.push(obj.sum_yjk_amt);
		sum_yjk_num.push(obj.sum_yjk_num);
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
        	categories: dishi,
            crosshair: true
        }],
        yAxis : [ {
			labels : {
				format : '',
			},
			title : {
				text : '集中充值有价卡金额(元)',
			},
		}, {
			labels : {
				format : '',
			},
			title : {
				text : '集中充值有价卡数量',
			},
			opposite: true
		} ],
        tooltip: {
            shared: true
          
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '集中充值有价卡金额(元)',
            type: 'column',
            color:'#65d3e3',
            data: sum_yjk_amt,
            yAxis:0,
            tooltip: {
                valueSuffix: '',
                valueDecimals:2
            }
        },{
            name: '集中充值有价卡数量',
            type: 'column',
            color:'#df5438',
            yAxis:1,
            data: sum_yjk_num,
            tooltip: {
                valueSuffix: ''
                
            }
        }]
    });
}
//结论
function loadAuditConclusion(id,data,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	var top3_dishi = "";
	var sum_yjk_amt =0;
	var sum_yjk_num =0;
	var dataChar = data.dataChar;
	var dataCon = data.dataCon;
	if(dataCon.length>0){
		$.each(dataCon, function(i, obj){
			sum_yjk_amt = sum_yjk_amt+obj.sum_yjk_amt;
			sum_yjk_num = sum_yjk_num+obj.sum_yjk_num;
		});
	}
	if(dataChar.length>0){
		if(dataChar.length==1){
			$.each(dataChar, function(i, obj){
				top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"。";
			});
		}
		if(dataChar.length==2){
			$.each(dataChar, function(i, obj){
				if(i<1){
					top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"、";
				}else{
					top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"。";
				}
				
			});
		}
		if(dataChar.length>2){
			$.each(dataChar, function(i, obj){
				if(i<2){
					top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"、";
				}else{
					top3_dishi = top3_dishi+obj.cmcc_prvd_nm_short+"。";
				}
				i++;
				if(i==3){
		    		return false;
		    	}
			});
		}
		
	}
	if(top3_dishi!=""){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"赠送有价卡集中充值金额"+formatCurrency(sum_yjk_amt,true)+"元，有价卡"
				+formatCurrency(sum_yjk_num,true)+"张。</br>注：集中充值，是指同一个月内使用赠送有价卡向同一个用户充值金额大于1000元或者数量大于10张。";
		}else{
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"赠送有价卡集中充值金额"+formatCurrency((sum_yjk_amt).toFixed(2),true)+"元，有价卡"
				+formatCurrency(sum_yjk_num,true)+"张。"
				+"其中，赠送有价卡集中充值金额排名前三的地市："+top3_dishi+"</br>注：集中充值，是指同一个月内使用赠送有价卡向同一个用户充值金额大于1000元或者数量大于10张。";
		}
	}else{
		text =timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"无数据。";
	}
	$('#'+id).html(text);
	$('#zsyjkjzczdsTableCon').html(text);
}

//数据表
function getZsyjkjzczdsDetail_Table(tableId,pagerId){
	var postData = getHZ_QueryParam();
	
	var tableColNames = [ '统计区间','省名称','地市名称','集中充值有价卡金额（元）','集中充值有价卡数量','被充值手机号码数量'];
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
    tableMap.name = "sum_yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(toDecimal2(cellvalue),true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_charge_msisdn_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
        url: $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzdCzdsDetailTable",
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


function loadZsyjkjzczJeChart(){
	var postData = getHZ_QueryParam();
	var id = "zsyjkjzczjeCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzczjeCharts",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankJeLineChart(id,data.dataChar);
            loadAuditJeConclusion("zsyjkjzczjeCon",data,postData);
        }
    });
}

function drawRankJeLineChart(id,data){
	var charge_msisdn = [];
	var sum_yjk_amt = [];
	var sum_yjk_num = [];
	
	$.each(data, function(i, obj) {
		charge_msisdn.push(obj.charge_user);
		sum_yjk_amt.push(obj.sum_yjk_amt);
		sum_yjk_num.push(obj.sum_yjk_num);
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
        	categories: charge_msisdn,
            crosshair: true
        }],
        yAxis : [ {
			labels : {
				format : '',
			},
			title : {
				text : '集中充值有价卡金额(元)',
			},
		}, {
			labels : {
				format : '',
			},
			title : {
				text : '集中充值有价卡数量',
			},
			opposite: true
		} ],
        tooltip: {
            shared: true
          
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '集中充值有价卡金额(元)',
            type: 'column',
            color:'#65d3e3',
            data: sum_yjk_amt,
            yAxis:0,
            tooltip: {
                valueSuffix: '',
                valueDecimals:2
            }
        },{
            name: '集中充值有价卡数量',
            type: 'column',
            color:'#df5438',
            yAxis:1,
            data: sum_yjk_num,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function loadAuditJeConclusion(id,data,postData){
	var text1 = "";
	var text2 = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	var top3_dishi = "";
	var sum_yjk_amt =0.00;
	var sum_yjk_num =0;
	var dataCon = data.dataCon;
	
	if(dataCon.length>0){
		$.each(dataCon, function(i, obj){
			sum_yjk_amt = sum_yjk_amt+obj.sum_yjk_amt;
			sum_yjk_num = sum_yjk_num+obj.sum_yjk_num;
		});
	}
	if(sum_yjk_num==0||sum_yjk_amt==0.00||sum_yjk_amt==0){
		text1 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"无数据。";
		text2 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()+"无数据。";
	}else{
		text1 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡集中充值金额"+formatCurrency((sum_yjk_amt).toFixed(2),true)
			+"元，有价卡"+formatCurrency(sum_yjk_num,true)+"张。"
			+ "其中，赠送有价卡集中充值金额排名前十的用户见下图。</br>注：集中充值，是指同一个月内使用赠送有价卡向同一个用户充值金额大于1000元或者数量大于10张。";
		text2 = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"赠送有价卡集中充值金额"+formatCurrency((sum_yjk_amt).toFixed(2),true)
			+"元，有价卡"+formatCurrency(sum_yjk_num,true)
			+"张。</br>注：集中充值，是指同一个月内使用赠送有价卡向同一个用户充值金额大于1000元或者数量大于10张。";
	}
	$('#'+id).html(text1);
	$('#zsyjkjzczjeTableCon').html(text2);
}


function getZsyjkjzczjeDetail_Table(tableId,pagerId){
	var postData = getHZ_QueryParam();
	
	var tableColNames = [ '统计区间','省名称','地市名称','被充值手机号码用户标识','充值有价卡金额（元）','充值有价卡数量'];
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
    tableMap.name = "charge_user";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(toDecimal2(cellvalue),true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_yjk_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
        url: $.fn.cmwaurl() + "/zsyjkczjzd/getZsyjkjzczjeDetail",
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
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/zsyjkczjzd/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
        	var li = "";
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
    var url = $.fn.cmwaurl() + "/zsyjkczjzd/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function loadMxCityDetailGridData(){
	var postData = getDetailQueryParam();
	var tableColNames = [ '审计月','省名称','地市名称','充值月份','充值日期','充值时间','被充值手机号码用户标识','有价卡序列号','金额（元）','有价卡赠送时间','获赠有价卡用户标识'];
	
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
    tableMap.name = "trade_mon";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tradedate";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tradetime";
    tableMap.align = "center";
    tableColModel.push(tableMap); 
    
    tableMap = new Object();
    tableMap.name = "charge_user";
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
        return toDecimal2(cellvalue);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "yjk_pres_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap); 
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap); 
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/zsyjkczjzd/getCityDetailPagerList",
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
	postData.mx_startMonth =$("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.trendType = $("#trendType").val();
    return postData;
}



/**
 * 获取汇总查询条件
 * 
 */
function getHZ_QueryParam() {
    var postData = {};
    postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.cityId = $("#cityId").val();
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


//样式格式化
$(window).resize(function(){
	$("#zsyjkjzczjeTable").setGridWidth($("#zsyjkjzczjeDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#zsyjkjzczdsTable").setGridWidth($("#zsyjkjzczdsDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});