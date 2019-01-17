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
	//图形  违规新增非长市漫合一的套餐数量波动趋势
	loadWgzsChart();
	
	 //明细地市列表
	loadMxCityList();
	
}

function initEvent(){
	
	//图形加载
	$("#wgzs_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadWgzsChart();
    });
	//折线数据表
	$("#wgzs_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadWgzs_TabDetailTable("wgzsTable","wgzsTablePageBar");
    });
	
	//折线数导出
	$("#exportWgzsTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#wgzsTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
      //  var exportFileName = "{0}_3.12_违规新增非长市漫合一的套餐_违规新增非长市漫合一的套餐_汇总.csv".format($('#auditId').val());
       // exportWgzsDetail(exportFileName);
        window.location.href = $.fn.cmwaurl() + "/wgxz/exportWgzsDetail?" + $.param(getHZ_QueryParam());

    });
	
	function exportWgzsDetail(exportFileName){
		var postData = getHZ_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/wgxz/exportWgzsDetail" +
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
        postData.exportFileName = "{0}_3.12_违规新增非长市漫合一的套餐_违规新增非长市漫合一的套餐_明细.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/wgxz/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
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
        $("#mx_xsType").val($("#mx_xsTypeSelect li.active").attr("date"));
        var postData = getDetailQueryParam();
        postData.mx_xsTypeSelect=$("#mx_xsType").val();
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
    	//赠送有价卡集中充值地市分布
        loadWgzsChart();
        
    });
    
    $(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
}

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
        url : $.fn.cmwaurl() + "/wgxz/initDefaultParams",
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

//查询按钮重新加载
function reloadGlobalData(){
	//违规新增非长市漫合一的套餐数量波动趋势
	loadWgzsChart();
	
	//违规新增非长市漫合一的套餐数量波动趋势 数据表
	reloadWgzs_TabDetailTable();
}

/**
 * 刷新明细页面地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    $("#mx_xsType").val($("#mx_xsTypeSelect li.active").attr("date"));
    postData.mx_xsTypeSelect=$("#mx_xsType").val();
    $("#cityDetailGridData").jqGrid('clearGridData');
    var url1 = $.fn.cmwaurl() + "/wgxz/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url1, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/wgxz/getCityList",
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


//违规新增非长市漫合一的套餐数量波动趋势 数据表
function reloadWgzs_TabDetailTable() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/wgxz/loadWgzs_TabDetailTable";
    jQuery("#wgzsTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
	postData.mx_xsTypeSelect=$("#mx_xsType").val();
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

function loadWgzsChart(){
	var postData = getHZ_QueryParam();
	var id = "wgzsChart";
    $.ajax({
        url : $.fn.cmwaurl() + "/wgxz/getWgzsChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            loadAuditConclusion("wgzsChartCon",data.maxMap,postData);
        }
    });
}

function drawRankLineChart(id,data,avgMap){
	var aud_trm = [];
    var sum_infrac_pack_num  = [];
    var avg_infrac_pack_num = [];
    if(data!=null){
    	 $.each(data, function(i, obj) {
    	    	aud_trm.push(obj.aud_trm);
    	    	sum_infrac_pack_num.push(obj.sum_infrac_pack_num);
    	    	avg_infrac_pack_num.push(Number(avgMap.avg_infrac_pack_num.toFixed(2)));
    	    });
    }
    
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
                text: '新增非长市漫合一的套餐数量',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            },
        }
				
         ],
        tooltip: {
            shared: true
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '新增非长市漫合一的套餐数量',
            type: 'spline',
            color:'#65d3e3',
            data: sum_infrac_pack_num,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均单月新增非长市漫合一的套餐数量',
            type: 'spline',
            color:'#df5438',
            data: avg_infrac_pack_num,
            tooltip: {
                valueSuffix: '',
                valueDecimals:2
            }
        }]
    });
}

function loadAuditConclusion(id,maxMap,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(maxMap.max_aud_trm!=null){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.max_aud_trm)
					+ "，违规新增非长市漫合一的套餐数量达到" + formatCurrency(maxMap.max_infrac_pack_num,false)+",高于平均值"+maxMap.highAvg+"。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.max_aud_trm)
			+ "，违规新增非长市漫合一的套餐数量达到" + formatCurrency(maxMap.max_infrac_pack_num,false) + ",高于平均值"+maxMap.highAvg+"。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#wgzsTableCon").html(text);
}


function loadWgzs_TabDetailTable(tableId,pagerId){
var postData = getHZ_QueryParam();
	
	var tableColNames = [ '审计月','省代码','省名称','新增非长市漫合一的套餐数量'];
    var tableColModel = [];
  
    tableMap = new Object();
    tableMap.name = "aud_trm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cmcc_prov_prvd_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "short_name";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "infrac_pack_num";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/wgxz/loadWgzs_TabDetailTable",
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

function loadMxCityDetailGridData(){
	
	var postData = getDetailQueryParam();
	
	var tableColNames = [ '审计月', '省名称','资费套餐名称','资费套餐描述','资费套餐销售状态','资费套餐停售时间', 
                          '上线日期', '目标区域市场','目标客户群','国内通话','省内通话','本地通话','集团通话','长市漫一体化套餐标识'];
	
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
    tableMap.name = "fee_pack_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "fee_pack_desc";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "fee_pack_stat";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pack_end_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "onln_dt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tgt_rgn_mkt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tgt_grp";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dom_call";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "prov_call";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "loacl_call";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "org_call";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "long_pack_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/wgxz/getCityDetailPagerList",
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
            //ie 设置数据表宽度
            $("#cityDetailGridData").setGridWidth($(".tab-nav").width()-40);
            $("#cityDetailGridDataPageBar").css("width", $("#cityDetailGridDataPageBar").width());
        }
    });
}


$(window).resize(function(){
	$("#wgzsTable").setGridWidth($("#wgzsDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

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


