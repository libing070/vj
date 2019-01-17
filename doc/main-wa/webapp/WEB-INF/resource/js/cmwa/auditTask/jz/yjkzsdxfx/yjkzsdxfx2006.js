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

//初始化界面加载函数
function initDefaultData(){
	//折线图
	loadNumberZhexianChart();
	//柱状图
	loadzhuxingChart();
	
	 //明细地市列表
	loadMxCityList();
	
}

function initEvent(){
	$("#zsfzdfjt_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//柱状图
		loadzhuxingChart();
    });
	
	$("#zsfzdfjt_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//数据表
		loadZsfzdfjtTabDetailTable("zsfzdfjtTable","zsfzdfjtTablePageBar");
    });
	
	//柱形图数据导出
	$("#exportZsfzdfjtTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#zsfzdfjtTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        window.location.href = $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/exportloadZsfzdfjt_TabDetail?" + $.param(getHz_QueryParam());

        //  exportZsfzdfjtTableDetail(exportFileName);
    });
	
	function exportZsfzdfjtTableDetail(exportFileName){
		var postData = getHz_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/exportloadZsfzdfjt_TabDetail" +
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
        postData.exportFileName = "{0}_3.10_有价卡赠送对象分析_赠送非中高端非集团客户_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
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
    
    //明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        if (!mxFormValidator()) {
            return false;
        }
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
        
    }
 // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 
    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
        loadNumberZhexianChart();
        loadzhuxingChart();
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
        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/initDefaultParams",
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

function reloadGlobalData(){
	//折线图
	loadNumberZhexianChart();
	
	loadzhuxingChart();
	
	reloadZsfzdfjt_TabDetailTable();
}

function reloadZsfzdfjt_TabDetailTable(){
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/loadZsfzdfjt_TabDetailTable";
    jQuery("#zsfzdfjtTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//柱形图住方法
function loadzhuxingChart(){
	var postData = getHz_QueryParam();
    var id = "zsfzdfjtCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/loadzhuxingChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            draw_zsfzdfjt_Chart(id,data.data);
            load_zsfzdfjt_Conclusion("zsfzdfjtCon",data.tolAmtdata,postData);
        }
    });
}

function draw_zsfzdfjt_Chart(id,data){
	var top10_name = [];
    var sum_yjk_amt  = [];
    $.each(data, function(i, obj) {
    	top10_name.push(obj.yjk_offer_nm);
    	sum_yjk_amt.push(obj.sum_yjk_amt);
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
        	categories: top10_name,
            crosshair: true
        }],
        yAxis: [{
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
        	shared: true,
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '违规赠送金额（元）',
            type: 'column',
            color:'#65d3e3',
            data: sum_yjk_amt,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function load_zsfzdfjt_Conclusion(id,tolAmtdata,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(tolAmtdata!=0){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "的总部有价卡赠送BOSS数据，发现存在向非中高端、非集团客户赠送有价卡的情况，共涉及金额"+formatCurrency(tolAmtdata,true)+"元。按照违规赠送有价卡金额排名前10的营销案如下图所示。";
			text2 = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "的总部有价卡赠送BOSS数据，发现存在向非中高端、非集团客户赠送有价卡的情况，共涉及金额"+formatCurrency(tolAmtdata,true)+"元。";
		}else{	
			text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "的总部有价卡赠送BOSS数据，发现存在向非中高端、非集团客户赠送有价卡的情况，共涉及金额"+formatCurrency(tolAmtdata,true)+"元。按照违规赠送有价卡金额排名前10的营销案如下图所示。" ;
			text2 = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "的总部有价卡赠送BOSS数据，发现存在向非中高端、非集团客户赠送有价卡的情况，共涉及金额"+formatCurrency(tolAmtdata,true)+"元。" ;
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+"的总部有价卡赠送BOSS数据，未发现存在向非中高端、非集团客户赠送有价卡的情况。";
		text2 = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+"的总部有价卡赠送BOSS数据，未发现存在向非中高端、非集团客户赠送有价卡的情况。";
	}else{
		text = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+"的总部有价卡赠送BOSS数据，未发现存在向非中高端、非集团客户赠送有价卡的情况。";
		text2 = "审阅"+$("#provinceName").val()+"在"+timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+"的总部有价卡赠送BOSS数据，未发现存在向非中高端、非集团客户赠送有价卡的情况。";
	}
	
	$('#'+id).html(text);
	$('#zsfzdfjtTable_Con').html(text2);
	
}

function loadZsfzdfjtTabDetailTable(tableId,pagerId){
	
	var postData = getHz_QueryParam();
	
	var tableColNames = [ '审计区间','省名称','营销案编码','营销案名称',
	                      '赠送非高端客户、非集团客户金额（元）'];
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
        return formatCurrency(toDecimal2(cellvalue),false);
    };
    tableColModel.push(tableMap);
    

    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/loadZsfzdfjt_TabDetailTable",
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
        }
    });
}

/**
 * 折线(主函数)
 */
function loadNumberZhexianChart() {
    var postData = getHz_QueryParam();
    var id = "zsfzgdfjtkhChart";
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/getYjkzsdxfx2006Trend",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            loadAuditConclusion("zsfzgdfjtkhChartCon",data.maxMap,postData);
        }
    });
} 

//折线实现
function drawRankLineChart(id, data,avgMap){
	var aud_trm = [];
    var yjk_amt  = [];
    var avg_yjk_amt = [];
    
    $.each(data, function(i, obj) {
    	aud_trm.push(obj.aud_trm);
    	yjk_amt.push(obj.yjk_amt);
    	avg_yjk_amt.push(Number(avgMap.avg_yjk_amt.toFixed(2)));
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
                text: '赠送非中高端非集团客户金额（元）',
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
            valueDecimals:2
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '各月违规赠送金额（元）',
            type: 'spline',
            color:'#65d3e3',
            data: yjk_amt,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均违规赠送金额（元）',
            type: 'spline',
            color:'#df5438',
            data: avg_yjk_amt,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

function loadAuditConclusion(id,maxMap,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(maxMap.max_yjk_amt!=0.00){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val() + timeToChinese(maxMap.maxAud_trm)
					+ "赠送非中高端非集团客户有价卡的金额达到" + formatCurrency(maxMap.max_yjk_amt,true)+"元,高于平均值"+maxMap.highAvg+"。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.maxAud_trm)
				+ "赠送非中高端非集团客户有价卡的金额达到" + formatCurrency(maxMap.max_yjk_amt,true) + "元,高于平均值"+maxMap.highAvg+"。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
}



function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    var url = $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

function loadMxCityDetailGridData(){
	var postData = getDetailQueryParam();
//	审计月，地市名称，有价卡序列号，有价卡赠送时间，有价卡类型，赠送有价卡金额，获赠有价卡到期时间，
//	获赠有价卡的用户标识，获赠有价卡的客户标识，获赠有价卡的手机号，平均月ARPU，有价卡赠送依据，营销案编码，
//	营销案名称，有价卡赠送文号说明，赠送渠道标识，赠送渠道名称
	var tableColNames = [ '审计月','地市名称', '有价卡序列号', '有价卡赠送时间', '有价卡类型', 
                          '赠送有价卡金额(元)','获赠有价卡到期时间','获赠有价卡的用户标识', 
                          '获赠有价卡的客户标识', '获赠有价卡的手机号','平均月ARPU',
                          '有价卡赠送依据','营销案编码','营销案名称','有价卡赠送文号说明',
                          '赠送渠道标识','赠送渠道名称' ];
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
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return toDecimal2(cellvalue);
    };
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
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return toDecimal2(cellvalue);
    };
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
   
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/getCityDetailPagerList",
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
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yjkzsdxfx2006/200604/getCityList",
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
	$("#zsfzdfjtTable").setGridWidth($("#zsfzdfjtDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});