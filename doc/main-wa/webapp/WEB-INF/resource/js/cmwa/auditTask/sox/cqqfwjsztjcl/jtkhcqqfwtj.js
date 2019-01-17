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
	loadCqqfwtjzhsChart();
	
	
}

function initEvent(){
	
	//图形加载
	$("#cqqfwtjzhs_Chart_tj").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadCqqfwtjzhsChart();
    });
	//折线数据表1
	$("#cqqfwtjzhs_sf_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadCqqfwtjzhs_sf_TabDetailTable("cqqfwtjzhs_sf_Table","cqqfwtjzhs_sf_TablePageBar");
    });
	
	//折线数导出1
	$("#exportCqqfwtjzhs_sf_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cqqfwtjzhs_sf_Table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        window.location.href = $.fn.cmwaurl() + "/jtkhcqqfwtj/exportCqqfwtjzhs_sf_Detail?" + $.param(getHZ_QueryParam());
   
    });
	
	
	//折线数据表2
	$("#cqqfwtjzhs_zh_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadCqqfwtjzhs_zh_TabDetailTable("cqqfwtjzhs_zh_Table","cqqfwtjzhs_zh_TablePageBar");
    });
	
	//折线数导出2
	$("#exportCqqfwtjzhs_zh_Table").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cqqfwtjzhs_zh_Table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        window.location.href = $.fn.cmwaurl() + "/jtkhcqqfwtj/exportCqqfwtjzhs_zh_Detail?" + $.param(getHZ_QueryParam());
   
    });
	
	// 导出明细列表
    $("#exportMxDetailList").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#cityDetailGridData").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        window.location.href = $.fn.cmwaurl() + "/jtkhcqqfwtj/exportMxDetailList?" + $.param(getDetailQueryParam());
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
        
    }
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
        $("#currTab").val("hz");
    	//赠送有价卡集中充值地市分布
        loadCqqfwtjzhsChart();
        
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
        url : $.fn.cmwaurl() + "/jtkhcqqfwtj/initDefaultParams",
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
	
	loadCqqfwtjzhsChart();
	
	reloadCqqfwtjzhs_sf_DetailTable();
	
	reloadCqqfwtjzhs_zh_DetailTable();
}

/**
 * 刷新明细页面地市列表
 * 
 */
function reLoadCityDetailGridData() {
    var postData = getDetailQueryParam();
    $("#cityDetailGridData").jqGrid('clearGridData');
    var url1 = $.fn.cmwaurl() + "/jtkhcqqfwtj/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url1, postData: postData, page: 1}).trigger("reloadGrid");
}

/**
 * 加载地市字段列表
 * 
 */
function loadMxCityList() {
    var postData = getDetailQueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/jtkhcqqfwtj/getCityList",
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


// 数据表1
function reloadCqqfwtjzhs_sf_DetailTable() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/jtkhcqqfwtj/loadCqqfwtjzhs_sf_TabDetailTable";
    jQuery("#cqqfwtjzhs_sf_Table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//数据表2
function reloadCqqfwtjzhs_zh_DetailTable() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/jtkhcqqfwtj/loadCqqfwtjzhs_zh_TabDetailTable";
    jQuery("#cqqfwtjzhs_zh_Table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
	postData.mx_startMonth =$("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
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

function loadCqqfwtjzhsChart(){
	var postData = getHZ_QueryParam();
	var id = "cqqfwtjzhs_Chart";
    $.ajax({
        url : $.fn.cmwaurl() + "/jtkhcqqfwtj/getJtrkhcqqfwtjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            loadAuditConclusion("cqqfwtjzhs_ChartCon",data.maxMap,postData);
        }
    });
}

function drawRankLineChart(id,data,avgMap){
	var aud_trm = [];
    var sum_acct_cnt_num  = [];
    var avg_acct_cnt_num = [];
    if(data!=null){
    	 $.each(data, function(i, obj) {
    	    	aud_trm.push(obj.aud_trm);
    	    	sum_acct_cnt_num.push(obj.acct_cnt);
    	    	avg_acct_cnt_num.push(toDecimal(avgMap.avg_acc_cnt));
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
        yAxis: {
        	labels: {
                formatter: function (){
                	if(this.value>2000){
                		return Highcharts.numberFormat(this.value/1000,2)+'k';
                	}else{
                		return Highcharts.numberFormat(this.value);
                	}
                }
            },
            title: {
                text: '长期欠费帐户数量',
                style: {
                    color:Highcharts.getOptions().colors[1],
                    fontFamily:'微软雅黑',
                    fontSize:'16px'
                }
            }
            
        },
        tooltip: {
            shared: true
        },
        legend: {
            enabled: true
        },
        series: [{
            name: '各月长期欠费帐户数量',
            type: 'spline',
            color:'#65d3e3',
            data: sum_acct_cnt_num,
            tooltip: {
                valueSuffix: '',
                valueDecimals:0
            }
        },{
            name: '平均每月长期欠费帐户数量',
            type: 'spline',
            color:'#df5438',
            data: avg_acct_cnt_num,
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
	if(maxMap.aud_trm!=null){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "期间，长期欠费帐户数量每月波动趋势如下。" + $("#provinceName").val()+ timeToChinese(maxMap.aud_trm)
					+ "，长期欠费帐户数量达到" + formatCurrency(maxMap.max_acc_cnt,false)+"，高于平均值"+maxMap.highAvg+"。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "期间，长期欠费帐户数量每月波动趋势如下。" + $("#provinceName").val()+ timeToChinese(maxMap.aud_trm)
					+ "，长期欠费帐户数量达到" + formatCurrency(maxMap.max_acc_cnt,false)+"，高于平均值"+maxMap.highAvg+"。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#cqqfwtjzhs_ChartCon").html(text);
	$("#cqqfwtjzhs_sf_TableCon").html(text);
	$("#cqqfwtjzhs_zh_TableCon").html(text);
	
}


function loadCqqfwtjzhs_sf_TabDetailTable(tableId,pagerId){
	var postData = getHZ_QueryParam();
	
	var tableColNames = [ '审计区间','省名称','长期欠费帐户数量','欠费金额(元)'];
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
    tableMap.name = "count_acct_id";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_dbt_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/jtkhcqqfwtj/loadCqqfwtjzhs_sf_TabDetailTable",
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

function loadCqqfwtjzhs_zh_TabDetailTable(tableId,pagerId){
	var postData = getHZ_QueryParam();
	
	var tableColNames = [ '审计月','省名称','地市名称','帐户标识','客户标识','集团客户名称','欠费账期','欠费金额(元)'];
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
    tableMap.name = "acct_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cust_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "org_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    
    tableMap = new Object();
    tableMap.name = "acct_prd_cnt";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "dbt_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/jtkhcqqfwtj/loadCqqfwtjzhs_zh_TabDetailTable",
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
 * 明细
 */
function loadMxCityDetailGridData(){
	var postData = getDetailQueryParam();
	   var url= "/jtkhcqqfwtj/getCityDetailPagerList";
	   var tableColNames = [ '审计月','省名称','地市名称','帐户标识','客户标识','集团客户名称','集团客户状态',
	                         '集团客户等级','集团业务类型','欠费账期','欠费金额(元)'];
	   var tableColModel = [
	                        {name:'aud_trm',index:'aud_trm',sortable:false,width:100},
	                        {name:'short_name',index:'short_name',sortable:false,width:100},
	                        {name:'cmcc_prvd_nm_short',index:'cmcc_prvd_nm_short',sortable:false},
	                        {name:'acct_id',index:'acct_id',sortable:false,width:200},
	                        {name:'cust_id',index:'cust_id',sortable:false,width:200},
	                        {name:'org_nm',index:'org_nm',sortable:false,width:230},
	                        {name:'cust_stat_typ_nm',index:'cust_stat_typ_nm',sortable:false,width:100},
	                        {name:'org_cust_lvl',index:'org_cust_lvl',sortable:false,width:100},
	                        {name:'org_busn_nm',index:'org_busn_nm',sortable:false,width:200},
	                        {name:'acct_prd_ytm',index:'acct_prd_ytm',sortable:false,width:100},
	                        {name:'dbt_amt',index:'dbt_amt',formatter: "integer", formatoptions: {thousandsSeparator:",",decimalPlaces:2},sortable:false},
	                        ];
	   
	   loadMxTab(postData, tableColNames, tableColModel, "#cityDetailGridData", "#cityDetailGridDataPageBar", url);
}

function loadMxTab(postData,tableColNames,tableColModel,tabId,pageId,url){
	jQuery(tabId).jqGrid({
		url: $.fn.cmwaurl() + url,
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
		pager : pageId,
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
			$(tabId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$(tabId).setGridWidth($(".tab-nav").width()-40);
			$(pageId).css("width", $(pageId).width());
		}
	});
}


$(window).resize(function(){
	$("#cqqfwtjzhs_sf_Table").setGridWidth($("#cqqfwtjzhs_sf_DetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cqqfwtjzhs_zh_Table").setGridWidth($("#cqqfwtjzhs_zh_DetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});


function toDecimal(x) { 
    var f = parseFloat(x); 
    if (isNaN(f)) { 
      return; 
    } 
    f = Math.round(x*100)/100; 
    return f; 
  } 