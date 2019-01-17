$(document).ready(function() {
	//step 1：个性化本页面的特殊风格
//	initStyle();
	//step 2：绑定本页面元素的响应时间,比如onclick,onchange,hover等
	initEvent();
	//step 3：获取默认首次加载本页面的初始化参数，并给隐藏form赋值
	initDefaultParams();
	//step 4：触发页面默认加载函数
	initDefaultData();
});

function initEvent(){
	
	
	//点击重新加载图形
	$("#qdykyhsl_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//折线图  渠道养卡用户数量波动趋势
		loadQdykyhslChart();
    });
	$("#dsxefzcz_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//柱形图  地市小额非整数充值统计
		loadDsqdykyhtjCharts();
    });
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_员工异常操作_小额非整数充值_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/qdyk/2401/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	//渠道养卡用户数量波动趋势
	$("#qdykyhsl_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadQdykyhsl_TabDetailTable("qdykyhsl_Table","qdykyhsl_TablePageBar");
    });
	
	//折线数导出
	$("#exportQdykyhslTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#qdykyhsl_Table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_渠道养卡_渠道养卡_渠道养卡用户数量波动趋势_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/qdyk/2401/exportQdykyhslDetail?" + $.param(getHz_QueryParam()) + "&" + $.param(postData);
      //  exportQdykyhslDetail(exportFileName);
    });
	
	function exportQdykyhslDetail(exportFileName){
		var postData = getHz_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/qdyk/2401/exportQdykyhslDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	//地市渠道养卡用户数量统计
	$("#dsxefzcz_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadDsxefzcz_TabDetailTable("dsxefzczTable","dsxefzczTablePageBar");
    });
	
	//地市渠道养卡用户数量统计
	$("#exportDsxefzczTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#dsxefzczTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_渠道养卡_渠道养卡_地市渠道养卡用户数量统计_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/qdyk/2401/exportDsxefzczDetail?" + $.param(getHz_QueryParam()) + "&" + $.param(postData);
        // exportDsxefzczDetail(exportFileName);
    });
	
	function exportDsxefzczDetail(exportFileName){
		var postData = getHz_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/qdyk/2401/exportDsxefzczDetail" +
		"?hz_startMonth="+hz_startMonth +
		"&hz_endMonth="+hz_endMonth +
		"&provId="+provId +
		"&exportFileName="+exportFileName;
		form.attr('action', url);
		$('body').append(form);
		form.submit();
		form.remove();
	}
	
	// 明细数据Tab监听事件
    $("#mx_tab").click(function() {
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
    
    //明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!mxFormValidator()) {
            return false;
        }
        $("#mx_qdTypeSelect").val($("#mx_qdTypeSelect li.active").attr("date"));
        var postData = getDetailQueryParam();
        postData.mx_qdTypeSelect=$("#mx_qdTypeSelect").val();
        reLoadCityDetailGridData();
    });
    
    //明细重置按钮监听事件
    $("#mx_reset_btn").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        clearHideFormInput();
    });
    
     //清空明细隐藏表单值
    function clearHideFormInput() {
    	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
      	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    	$("#mx_startMonth").val($.fn.timeStyle(beforeAcctMonth));
        $("#mx_endMonth").val($.fn.timeStyle(endAcctMonth));
        $("#mx_cityCode").val("");
        $("#mx_qdTypeSelect").arr("");
    }
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
        loadDsqdykyhtjCharts();
    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/qdyk/2401/initDefaultParams",
        dataType : 'json',
        async : false,
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

//初始化界面加载函数
function initDefaultData(){
	//折线图  渠道养卡用户数量波动趋势
	loadQdykyhslChart();
	//柱形图  地市小额非整数充值统计
	loadDsqdykyhtjCharts();
	 //明细地市列表
	loadMxCityList();
}
/**
 * 获取汇总查询条件
 * 
 */
function getHz_QueryParam() {
	var postData = {};
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
	var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
	postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
	postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
	postData.provId = $("#provinceCode").val();
	postData.prvdId = $("#provinceCode").val();
	postData.trendType = $("#trendType").val();
	return postData;
}
//查询重新加载页面
function reloadGlobalData(){
	//折线图
	loadQdykyhslChart();
	//柱形图  地市小额非整数充值统计
	loadDsqdykyhtjCharts();
	
	
	//地市渠道养卡用户数量统计数据表
	reloadDsxefzcz_TabDetailTable();
	//渠道养卡用户数量波动趋势数据表
	reloadQdykyhsl_TabDetailTable();
	
	 //明细地市列表
	loadMxCityList();
	
}

//小额非整数充值波动趋势
function reloadDsxefzcz_TabDetailTable() {
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/qdyk/2401/loadDsxefzcz_TabDetailTable";
    jQuery("#dsxefzczTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//渠道养卡用户数量波动趋势数据表
function reloadQdykyhsl_TabDetailTable() {
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/qdyk/2401/loadQdykyhslTabDetailTable";
    jQuery("#qdykyhsl_Table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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

//明细表单验证
function mxFormValidator() {
    // 对开始和结束月份进行校验
    if ($("#mx_startMonth").val() >  $("#mx_endMonth").val()) {
        alert($.fn.startMonthThanEndMonth());
        return false;
    }
    return true;
}

function loadQdykyhslChart(){
	var postData = getHz_QueryParam();
    var id = "qdykyhslChart";
    $.ajax({
        url : $.fn.cmwaurl() + "/qdyk/2401/getQdykyhslChart",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            drawQdykyhslChart(id,data.data,data.avgMap);
            loadQdykyhslConclusion("qdykyhslChart_Con",data.maxMap,postData);
        }
    });
}

function loadQdykyhslConclusion(id,maxMap,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(maxMap.maxAud_trm!=0){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.maxAud_trm)
					+ "渠道养卡用户数量达到" + formatCurrency(maxMap.max_qdyk_subs_num,false)  + ",高于平均值"+maxMap.highAvg+"。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.maxAud_trm)
			+ "渠道养卡用户数量达到" + formatCurrency(maxMap.max_qdyk_subs_num,false) + ",高于平均值"+maxMap.highAvg+"。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
				+ "，" +$("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" +$("#provinceName").val()+"无数据。";
	}
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
}

function drawQdykyhslChart(id, data,avgMap){
	var aud_trm = [];
    var qdyk_subs_num  = [];
    var avg_qdyk_subs_num = [];
    $.each(data, function(i, obj) {
    	aud_trm.push(obj.aud_trm);
    	qdyk_subs_num.push(obj.sum_qdyk_subs_num);
    	avg_qdyk_subs_num.push(Number(avgMap.avg_qdyk_num.toFixed(2)));
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
                text: '渠道养卡用户数量',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }
				
         ],
        tooltip: {
            shared: true,
            valueDecimals:0
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '各月渠道养卡用户数量',
            type: 'spline',
            color:'#65d3e3',
            data: qdyk_subs_num,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均渠道养卡用户数量',
            type: 'spline',
            color:'#df5438',
            data: avg_qdyk_subs_num,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function loadQdykyhsl_TabDetailTable(tableId,pagerId){
	var postData = getHz_QueryParam();
//审计月、省名称、疑似养卡用户数、疑似养卡用户数排名、新入网用户数、疑似养卡用户数占新入网用户数的比例、疑似养卡用户数占新入网用户数的比例在全国范围的排名
	var tableColNames = [ '审计月','省名称','渠道类型编码','渠道类型名称','疑似养卡用户数','疑似养卡用户数排名',
	                      '新入网用户数','疑似养卡用户数占新入网用户数的比例(%)',
	                      '疑似养卡用户数占新入网用户数的比例在全国范围的排名'];
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
    tableMap.name = "chnl_class";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_class_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "qdyk_subs_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "qdyk_subs_num_rank";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "ent_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "qdyk_num_perc";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return (cellvalue).toFixed(2)+"%";
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "qdyk_num_perc_rank";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/qdyk/2401/loadQdykyhslTabDetailTable",
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

function loadDsxefzcz_TabDetailTable(tableId,pagerId){
	var postData = getHz_QueryParam();
	var tableColNames = [ '审计月','省名称','地市名称','渠道编码',
	                      '渠道名称','疑似养卡用户数','新入网用户数',
	                      '疑似养卡用户数占新入网用户数的比例(%)'];
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
    tableMap.name = "chnl_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "rase_crd_qty";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tol_users";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, false);
	};
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "ykzb";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/qdyk/2401/loadDsxefzcz_TabDetailTable",
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


//地市小额非整数充值统计(主方法)
function loadDsqdykyhtjCharts(){
	 var postData = getHz_QueryParam();
	 var id = "dsxefzczCharts";
	 var provinceCode = $('#provinceCode').val();
	    $.ajax({
	        url : $.fn.cmwaurl() + "/qdyk/2401/dsqdykyhtjCharts",
	        dataType : 'json',
	        data : postData,    
	        success : function(backdata) {
	        	drawRankLineDsqdykyhtjChart(id,backdata.cmccProvIdList,backdata.qdlist);
	            loadDsqdykyhtjConclusion("dsxefzcz_Con",backdata.conList,postData);
	        }
	    });
}
//地市小额非整数充值统计结论
function loadDsqdykyhtjConclusion(id,dataConList,postData){
	var text = "";
	var top3dishi="";
	var top3_sum_rase_crd_qty="";//用户数
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(dataConList!=null){
		$.each(dataConList, function(i, obj) {
			top3_sum_rase_crd_qty = top3_sum_rase_crd_qty+formatCurrency(obj.count_chnl_id,false)+"个渠道养卡"+formatCurrency(obj.sum_rase_crd_qty,false);
			if(i<2){
				top3_sum_rase_crd_qty = top3_sum_rase_crd_qty+"个、";
				top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"、";
			}else{
				top3_sum_rase_crd_qty = top3_sum_rase_crd_qty+"个";
				top3dishi = top3dishi+obj.cmcc_prvd_nm_short+",";
			}
	    	i++;
	    	if(i==3){
	    		return false;
	    	}
	    });
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"通过"+top3_sum_rase_crd_qty.substring(0, top3_sum_rase_crd_qty.length-1)+"。";
		}else{
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"渠道养卡用户数量排名前三的地市有" + top3dishi + "通过"+top3_sum_rase_crd_qty;
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#dsxefzczTable_Con").html(text);
}
//地市小额非整数充值统计图(实现)
function drawRankLineDsqdykyhtjChart(id,cmccProvIdList,qdlist){
	var data = cmccProvIdList;
	var data2 = qdlist;
	var data_child = [];//钻取数据
	var downData = [];//钻取数据
	var all_dishi = [];
	
	var downData = [];
	var dishi = [];//一级数据
    var sum_rase_crd_qty = [];//养卡用户数
    var seriesData = [];
    $.each(data, function(i, obj) {
    	all_dishi.push(obj.cmcc_prvd_nm_short);
    	sum_rase_crd_qty.push(obj.sum_rase_crd_qty);
    });
    
    for(var i=0;i<data.length;i++){
    	var times = 0;
    	var relaRat = [];
    	for(var j = data2.length-1; j>=0;j--){
			   if(data[i]["cmcc_prov_id"]==data2[j]["cmcc_prov_id"]){
				   if(times<10){
					relaRat.push([data2[j]["chnl_nm"],data2[j]["sum_rase_crd_qty"]]);
				   }
				   times++;
			   };
		   }
    	dishi.push([data[i]["cmcc_prvd_nm_short"],data[i]["sum_rase_crd_qty"]]);
    	data_child.push(relaRat);
    }
    
    var categories = [];
	   for(var i=0;i<dishi.length;i++){
		   categories.push(dishi[i][0]);
		   seriesData.push({
		            name: dishi[i][0],
		            y: dishi[i][1],
		            drilldown: dishi[i][0],
		        });
		   
		   downData.push({
	            name: dishi[i][0],
	            id: dishi[i][0],
	            data: data_child[i]
	        });
	   }
    
    
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
        credits: {
            enabled: false
        },
        xAxis: [{
        	type: 'category'
        }],
        yAxis: [{
        	title: {
                text: '渠道养卡用户数量',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }],
        tooltip: {
        	shared: true,
            valueDecimals:0
        },
        lang:{
            drillUpText:'返回'
        },
        legend: {
            enabled: false
        },
        plotOptions: {
	        series: {
	            borderWidth: 0,
	        }
	    },
        series: [{
	        name: '渠道养卡用户数量',
	        colorByPoint: true,
	        data: seriesData
	    }],
	    drilldown: {
	        series: downData
	    }
    });
}

/**
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
	postData.mx_qdTypeSelect=$("#mx_qdTypeSelect").val();
    postData.mx_startMonth =$("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.trendType = $("#trendType").val();
    return postData;
}

function loadMxCityDetailGridData(){
	if($("#cityDetailGridData").html() != "") {
        return;
    }
	var postData = getDetailQueryParam();
	var tableColNames = [ '审计月','省名称', '地市名称', '用户标识', 
	                      '手机号码', '渠道名称', '渠道类型名称',
	                      '渠道类别名称', '渠道状态' ];
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
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "rase_crd_no";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_class_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_type_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_stat";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/qdyk/2401/getCityDetailPagerList",
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

function reLoadCityDetailGridData(){
	if ($("#cityDetailGridData").html() != "") {
        return;
    }
	var postData = getDetailQueryParam();
	var tableColNames = [ '审计月','省名称', '地市名称', '用户标识', 
	                      '手机号码', '渠道名称', '渠道类型名称',
	                      '渠道类别名称', '渠道状态' ];
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
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "rase_crd_no";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_class_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_type_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_stat";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/qdyk/2401/getCityDetailPagerList",
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
            $("#cityDetailGridData").setGridWidth($(".shuju_table").width() - 2);
            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width() - 2);
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
        url : $.fn.cmwaurl() + "/qdyk/2401/getCityList",
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
    var url = $.fn.cmwaurl() + "/qdyk/2401/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
	$("#qdykyhsl_Table").setGridWidth($("qdykyhslDivId_class").width());
});
$(window).resize(function(){
	$("#dsxefzczTable").setGridWidth($("dsxefzczDivId_class").width());
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});
	