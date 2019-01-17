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
	loadYckycyzxjChart();
	
	
}

function initEvent(){
	
	//图形加载
	$("#hz_yckyc_tj_chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYckycyzxjChart();
    });
	//折线数据表1
	$("#hz_yckyc_sjb").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadYckyc_TabDetailTable("hz_yckyc_sjb_table","hz_yckyc_sjb_pageBar");
    });
	
	//折线数导出1
	$("#export_hz_yckyc_sjb").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#hz_yckyc_sjb_table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        window.location.href = $.fn.cmwaurl() + "/yckycyzx/exportYckyc_Detail?" + $.param(getHZ_QueryParam()) + "&" + $.param(postData);
   
        // exportYckyc_Detail(exportFileName);
    });
	
	function exportYckyc_Detail(exportFileName){
		var postData = getHZ_QueryParam();
        var hz_startMonth = postData.hz_startMonth;
        var hz_endMonth = postData.hz_endMonth;
        var provId = $("#provinceCode").val();
		var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
		var url = $.fn.cmwaurl() + "/yckycyzx/exportYckyc_Detail" +
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
    });
    
    //明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
        if (!mxFormValidator()) {
            return false;
        }
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
        loadYckycyzxjChart();
        
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
        url : $.fn.cmwaurl() + "/yckycyzx/initDefaultParams",
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
	
	loadYckycyzxjChart();
	
	reloadYckyc_DetailTable();
	
}

// 数据表1
function reloadYckyc_DetailTable() {
	var postData = getHZ_QueryParam(); 
    var url = $.fn.cmwaurl() + "/yckycyzx/load_yckyc_TabDetailTable";
    jQuery("#hz_yckyc_sjb_table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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
    return postData;
}

function loadYckycyzxjChart(){
	var postData = getHZ_QueryParam();
    $.ajax({
        url : $.fn.cmwaurl() + "/yckycyzx/getYckycyzxjChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
        	var data_char = data.dataChar;
			var dataCon = data.dataCon;
			var x_value = [];
			var y_value1 = [];
			var y_value2 = [];
			for ( var i = 0; i < data_char.length; i++) {
				x_value.push(data_char[i].aud_trm);
				y_value1.push(data_char[i].sum_yjk_amt);
				y_value2.push(data_char[i].sum_qmye);
			}
            drawRankLineChart(x_value,y_value1,y_value2);
            loadAuditConclusion("hz_yckyc_conclusion",dataCon,postData);
        }
    });
}

function drawRankLineChart(x_value,y_value1,y_value2){
	$('#hz_yckyc_chart').highcharts({
		chart : {
			zoomType : 'xy'
		},
		title : {
			text : ''
		},
		xAxis : [ {
			categories : x_value,
			crosshair : true
		} ],
		yAxis : [ {
			labels : {
				 formatter: ''
			},
			title : {
				text : 'BOSS和财务系统预存款金额(元)',
				style: {
                	color : Highcharts.getOptions().colors[1],
					fontFamily : '微软雅黑',
					fontSize : '16px'
                }
			},
		} ],
		tooltip : {
			shared : true/*,
			valueDecimals: 2//小数位数  */			
			},
		series : [ {
			name : "BOSS系统预存款金额(元)",
			color : '#f2ca68',
			type:'column',
			valueDecimals: 2,
			data : y_value1,
			tooltip : {
				valueDecimals: 2
			}
		}, {
			name : "财务系统预存款金额(元)",
			color : '#65d3e3',
			type:'column',
			valueDecimals: 2,
			data : y_value2,
			tooltip : {
				valueSuffix : '',
				valueDecimals: 2
			}
		} ]
	});
}

function loadAuditConclusion(id,dataCon,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(dataCon.length>0){
		$.each(dataCon, function(i, obj) {
			sum_yjk_amt = dataCon[i].sum_yjk_amt;
			sum_qmye = dataCon[i].sum_qmye;
			cyje = dataCon[i].cyje;
		    });
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，"+$("#provinceName").val()+ "财务系统记录预存款金额"+formatCurrency(sum_qmye,true)
					+ "元，BOSS系统记录预存款金额" + formatCurrency(sum_yjk_amt,true)+"元，差异金额"+formatCurrency(cyje,true)+"元。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，"+ $("#provinceName").val()+ "省财务系统记录预存款金额"+formatCurrency(sum_qmye,true)
				+ "元，BOSS系统记录预存款金额" + formatCurrency(sum_yjk_amt,true)+"元，差异金额"+formatCurrency(cyje,true)+"元。";
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"省无数据。";
	}
	
	$('#'+id).html(text);
	$("#hz_yckyc_conclusion").html(text);
	$("#hz_yckyc_sjb_conclusion").html(text);
}


function loadYckyc_TabDetailTable(tableId,pagerId){
	var postData = getHZ_QueryParam();
	
	var tableColNames = [ '审计月','省名称','BOSS预存款总金额(元)','ERP预收账款-用户预存款-期末余额(元)','差异金额(元)'];
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
    tableMap.name = "sum_yjk_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "sum_qmye";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "cyje";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/yckycyzx/load_yckyc_TabDetailTable",
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



$(window).resize(function(){
	$("#hz_yckyc_sjb_table").setGridWidth($("#yckyc_DetailTableDiv_class").width()-1);
});




