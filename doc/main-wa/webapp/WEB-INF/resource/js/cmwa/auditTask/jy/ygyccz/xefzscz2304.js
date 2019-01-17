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
	
	$("#xefzscz_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//折线图
		loadNumberZhexianChart();
    });
	$("#dsxefzcz_Chart").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		//柱状图
		loadzhuxingChart();
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
        window.location.href = $.fn.cmwaurl() + "/xefzscz/2304/exportMxDetailList?" + $.param(getDetailQueryParam()) + "&" + $.param(postData);
    });
	//折线数据表
	$("#xefzscz_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadXefzscz_TabDetailTable("xefzscz_Table","xefzscz_TablePageBar");
    });
	//折线数导出
	$("#exportXefzsczTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#xefzscz_Table").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        window.location.href = $.fn.cmwaurl() + "/xefzscz/2304/exportXefzsczDetail?" + $.param(getHz_QueryParam());
    });
	//柱形图数据表
	$("#dsxefzcz_Tab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadDsxefzcz_TabDetailTable("dsxefzczTable","dsxefzczTablePageBar");
    });
	
	//柱形图数据表导出
	$("#exportDsxefzczTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

        var totalNum = $("#dsxefzczTable").getGridParam("records");
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var postData = {};
        postData.exportFileName = "{0}_员工异常操作_小额非整数充值_地市小额非整数充值统计_汇总.csv".format($('#auditId').val());
        window.location.href = $.fn.cmwaurl() + "/xefzscz/2304/exportDsxefzczDetail?" + $.param(getHz_QueryParam()) + "&" + $.param(postData);
    });
	
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
        $("#mx_czTypeSelect").val($("#mx_czTypeSelect li.active").attr("date"));
        var postData = getDetailQueryParam();
        postData.mx_czTypeSelect=$("#mx_czTypeSelect").val();
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
        $("#mx_czTypeSelect").val("");
        
    }
 // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
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
        url : $.fn.cmwaurl() + "/xefzscz/2304/initDefaultParams",
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

//初始化界面加载函数
function initDefaultData(){
	//折线图
	loadNumberZhexianChart();
	//柱状图
	loadzhuxingChart();
	
	 //明细地市列表
	loadMxCityList();
	
}

//查询按钮重新加载
function reloadGlobalData(){
	//折线图
	loadNumberZhexianChart();
	//柱状图
	loadzhuxingChart();
	
	//小额非整数充值波动趋势 数据表
	reloadXefzscz_TabDetailTable();
	//地市小额非整数充值统计 数据表
	reloadDsxefzcz_TabDetailTable();
}

//小额非整数充值波动趋势
function reloadDsxefzcz_TabDetailTable() {
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/xefzscz/2304/loadXefzscz_TabDetailTable";
    jQuery("#xefzscz_Table").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//地市小额非整数充值统计
function reloadXefzscz_TabDetailTable() {
	var postData = getHz_QueryParam(); 
    var url = $.fn.cmwaurl() + "/xefzscz/2304/loadDsxefzcz_TabDetailTable";;
    jQuery("#dsxefzczTable").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
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

//柱形图
function loadzhuxingChart(){
	var postData = getHz_QueryParam();
    var id = "dsxefzczCharts";
    $.ajax({
        url : $.fn.cmwaurl() + "/xefzscz/2304/loadzhuxingChart",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            draw_dsxefzcz_Chart(id,data.data);
            load_dsxefzcz_Conclusion("dsxefzcz_Con",data.top3data,postData);
        }
    });
}

function load_dsxefzcz_Conclusion(id,data,postData){
	var text = "";
	var top3dishi="";
	var top3_sum_pay_num = "";
	var dishi_pay_num="";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(data.length>0){
		if(data.length==1){
			$.each(data, function(i, obj){
				top3_sum_pay_num = top3_sum_pay_num+formatCurrency(obj.sum_pay_num,false)+"笔。";
				top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"，";
				dishi_pay_num = obj.sum_pay_num;
			});
		}else if(data.length==2){
			$.each(data, function(i, obj){
				if(i==1){
					top3_sum_pay_num = top3_sum_pay_num+formatCurrency(obj.sum_pay_num,false)+"笔。";
					top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"，";
				}else{
					top3_sum_pay_num = top3_sum_pay_num+formatCurrency(obj.sum_pay_num,false)+"笔、";
					top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"、";
				}
				i++;
			});
		}else{
			$.each(data, function(i, obj) {
				if(i==2){
					top3_sum_pay_num = top3_sum_pay_num+formatCurrency(obj.sum_pay_num,false)+"笔。";
					top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"，";
				}else{
					top3_sum_pay_num = top3_sum_pay_num+formatCurrency(obj.sum_pay_num,false)+"笔、";
					top3dishi = top3dishi+obj.cmcc_prvd_nm_short+"、";
				}
		    	i++;
		    	if(i==3){
		    		return false;
		    	}
		    });
		}
		
		
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"办理小额非整数充值"+dishi_pay_num+"笔。" ;
		}else{
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
				+ "，" + $("#provinceName").val()+"小额非整数充值业务数量排名前三的地市为" + top3dishi + "分别办理小额非整数充值"+top3_sum_pay_num;
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

function draw_dsxefzcz_Chart(id,data){
	var all_dishi = [];
    var sum_pay_num  = [];
    $.each(data, function(i, obj) {
    	all_dishi.push(obj.cmcc_prvd_nm_short);
    	sum_pay_num.push(obj.sum_pay_num);
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
        	categories: all_dishi,
            crosshair: true
        }],
        yAxis: [{
        	title: {
                text: '小额非整数充值数量',
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
        legend: {
            enabled: true
        },
        series: [{
            name: '小额非整数充值数量',
            type: 'column',
            color:'#65d3e3',
            data: sum_pay_num,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

/**
 * 折线(主函数)
 */
function loadNumberZhexianChart() {
    var postData = getHz_QueryParam();
    var id = "xefzsczChart";
    $.ajax({
        url : $.fn.cmwaurl() + "/xefzscz/2304/getXefzscz2304Trend",
        dataType : 'json',
        async : false,
        data : postData,    
        success : function(data) {
            drawRankLineChart(id,data.data,data.avgMap);
            loadAuditConclusion("xefzsczChart_Con",data.maxMap,postData);
        }
    });
} 
//折线结论
function loadAuditConclusion(id,maxMap,postData){
	var text = "";
	var provinceCode = $('#provinceCode').val().replaceAll("-", "");
	if(maxMap.max_pay_num!=0){
		if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
					+ "，" + $("#provinceName").val()+ timeToChinese(maxMap.maxAud_trm)
					+ "小额非整数充值数量达到" + formatCurrency(maxMap.max_pay_num,false)+",高于平均值"+maxMap.highAvg+"。";
		}else{	
			text = timeToChinese(postData.hz_startMonth)+"-"+timeToChinese(postData.hz_endMonth)
			+ "，" + maxMap.maxShort_name+ timeToChinese(maxMap.maxAud_trm)
			+ "小额非整数充值数量达到" + formatCurrency(maxMap.max_pay_num,false) + ",高于平均值"+maxMap.highAvg;
		}
	}else if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300"){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+ "，" + $("#provinceName").val()+"无数据。";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+ "，" + $("#provinceName").val()+"无数据。";
	}
	
	$('#'+id).html(text);
	$("#yhhzTop10TelTable_Con").html(text);
}
//折线
function drawRankLineChart(id, data,avgMap){
	var aud_trm = [];
    var pay_num  = [];
    var avg_pay_num = [];
    var avg_pay_num_toFixed =(avgMap.avg_pay_num).toFixed(2);
    $.each(data, function(i, obj) {
    	aud_trm.push(obj.aud_trm);
    	pay_num.push(obj.pay_num);
    	avg_pay_num.push(Number(avgMap.avg_pay_num.toFixed(2)));
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
                text: '小额非整数充值数量',
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
            name: '各月小额非整数充值数量',
            type: 'spline',
            color:'#65d3e3',
            data: pay_num,
            tooltip: {
                valueSuffix: ''
            }
        },{
            name: '平均小额非整数充值数量',
            type: 'spline',
            color:'#df5438',
            data: avg_pay_num,
            tooltip: {
                valueSuffix: ''
            }
        }]
    });
}

//柱形图数据表
function loadDsxefzcz_TabDetailTable(tableId,pagerId){
	var postData = getHz_QueryParam();
	
	var tableColNames = [ '审计区间','省名称','地市名称','缴费用户数',
	                      '缴费笔数','缴费金额(元)'];
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
    tableMap.name = "pay_msisdn_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    

    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/xefzscz/2304/loadDsxefzcz_TabDetailTable",
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
function loadXefzscz_TabDetailTable(tableId,pagerId){
	var postData = getHz_QueryParam();
	
	var tableColNames = [ '审计月','省名称','缴费用户数','缴费笔数','缴费金额(元)'];
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
    tableMap.name = "pay_msisdn_num";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    
    tableMap = new Object();
    tableMap.name = "pay_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/xefzscz/2304/loadXefzscz_TabDetailTable",
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
 * 获取明细页面查询条件
 */
function getDetailQueryParam() {
	var postData = {};
	postData.mx_czTypeSelect=$("#mx_czTypeSelect").val();
	postData.mx_startMonth =$("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();
    postData.trendType = $("#trendType").val();
    return postData;
}
function loadMxCityDetailGridData(){
	if ($("#cityDetailGridData").html() != "") {
        return;
    }
	var postData = getDetailQueryParam();
	var tableColNames = ['审计月','省名称','地市名称','被充值手机号码用户标识','缴费类型编码','缴费类型名称','充值日期','充值金额','渠道名称' ];
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
    tableMap.name = "CMCC_prvd_nm_short";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "user_id";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_type";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_type_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_date";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "pay_amt";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue,true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "chnl_nm";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    jQuery("#cityDetailGridData").jqGrid({
        url: $.fn.cmwaurl() + "/xefzscz/2304/getCityDetailPagerList",
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
        url : $.fn.cmwaurl() + "/xefzscz/2304/getCityList",
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
    var url = $.fn.cmwaurl() + "/xefzscz/2304/getCityDetailPagerList";
    jQuery("#cityDetailGridData").jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

$(window).resize(function(){
	$("#xefzscz_Table").setGridWidth($("#xefzsczDetailTableDiv_class").width()-1);
});

$(window).resize(function(){
	$("#dsxefzczTable").setGridWidth($("#dsxefzsczDetailTableDiv_class").width()-1);
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