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
    
    // 初始化图形组件大小等于盒子大小（普通）
    $("#ranklineChart").css({width: $(".zhexian-map").width() - 20, height: 328});
    $("#cityMapChart").css({width: $(".map-img").width() - 20, height: 328});
    
    // 初始图形组件大小等于浮层大小（图形放大）
    $("#ranklineChartBig").css({width: $(".modal-box").width() - 162, height: $(".zhexian-bigmap").height()});
    $("#cityMapChartBig").css({width: $(".bigmap").width() - 20, height: $(".bigmap").height()});
    
    $("#repeatCharts").css({width: $("#repeatCharts").parent().parent().parent().width() - 20, height: 315});
    
}

function initEvent() {
	
	$("#sumTab").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		loadSumTable('sumCityTable','sumCityTablePageBar');
    });
	
	$("#exportCityTable").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#sumCityTable").getGridParam("records");

		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		exportTab("exprotSumTable");
	});
	
	
	$("#exportDetailList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#cityDetailGridData").getGridParam("records");

		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		exportDetailTab("exportDetailTable");
	});
	
    // 明细查询按钮监听事件
    $("#mx_search_btn").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

   	 if (!mxFormValidator()) {
         return false;
     }
    	reLoadCityDetailGridData("cityDetailGridData");
    });
    
    // 按地市排名下拉框事件监听
    addSelectEvent("hz_rankType", function() {

    });
    
    // 汇总统计开始时间监听事件
    addMonthCalendarEvent("hz_startMonth");
    
    // 汇总统计结束时间监听事件
    addMonthCalendarEvent("hz_endMonth");

    // 明细统计开始时间监听事件
    addMonthCalendarEvent("mx_startMonth");
    
    // 明细统计结束时间监听事件
    addMonthCalendarEvent("mx_endMonth");

    // 汇总查询按钮监听事件
    $("#hz_search_btn").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

        if (!hzFormValidator()) {
            return false;
        }
        reloadGlobalData();
    });
    
    // 是否营销案下拉框事件监听
    addSelectEvent("mx_yxaFlag");
    
    // 地市名称下拉框事件监听
    addSelectEvent("mx_cityCode");
    
    // 汇总数据Tab监听事件
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01");

        $("#currTab").val("hz");
    });
    
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
        loadDetailTable("cityDetailGridData","cityDetailGridDataPageBar");
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
    
    // 日历弹出事件
    $(".add-on").click(function(event) {
        var id = $(this).prev().attr("id");
        $('#' + id).datetimepicker('show');
    });
}

/**
 * 首次加载默认参数初始化
 * 
 */
function initDefaultParams() {
  
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
    
    $.ajax({
        url : $.fn.cmwaurl() + "/gprs/1501/initDefaultParams",
        async : false,
        dataType : 'json',
        data : postData,    
        success : function(data) {        
            $('#hz_rankType').val(data['hz_rankType']);
            $('#hz_map_double').val(data['hz_map_double']);
            
            $('#hz_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#hz_endMonth').val($.fn.timeStyle(endAcctMonth));
            $('#mx_startMonth').val($.fn.timeStyle(beforeAcctMonth));
            $('#mx_endMonth').val($.fn.timeStyle(endAcctMonth));
            
            $('#auditId').val(auditId);
            $('#provinceCode').val(provinceCode);
            $('#beforeAcctMonth').val(beforeAcctMonth);
            $('#endAcctMonth').val(endAcctMonth);
            
            $('#hz_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#hz_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#hz_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
            
            $('#mx_startMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setStartDate", new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_endMonth').datetimepicker("setEndDate", new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1, 1));
            $('#mx_startMonth').datetimepicker('setDate', new Date(beforeAcctMonth.substring(0, 4), beforeAcctMonth.substring(4, 6) - 1));
            $('#mx_endMonth').datetimepicker('setDate', new Date(endAcctMonth.substring(0, 4), endAcctMonth.substring(4, 6) - 1));
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

/**
 * 获取汇总查询条件
 * 
 */
function getSumQueryParam() {
    var postData = {};
    postData.hz_rankType = parseInt($("#hz_rankType").val());
    postData.hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    postData.hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    postData.provId = $("#provinceCode").val();
    postData.trendType=$("#trendType").val();
    return postData;
}

/**
 * 获取明细查询条件
 * 
 */
function getDetailQueryParam() {
    var postData = {};
    postData.mx_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    postData.mx_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    postData.mx_yxaFlag = $("#mx_yxaFlag").val();
    postData.mx_cityCode = $("#mx_cityCode").val();
    postData.provId = $("#provinceCode").val();
    postData.prvdId = $("#provinceCode").val();

    
    return postData;
}

/**
 * 首次加载页面触发函数调用
 * 
 */
function initDefaultData(){
	getSumRepeat();
	getSumPieCharts();
    loadCityList();
}

/**
 * 重新加载全局触发函数调用
 * 
 */
function reloadGlobalData() {
	$("#sumCharts").click();
	getSumRepeat();
	getSumPieCharts();
}
/**
 * 获取重复办理统计
 */
function getSumRepeat(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/getSumRepeat",
		dataType : 'json',
		data : postData,
		success : function(data) {
			loadSumRepeat("repeatCharts",data.dataList);
			loadSumRepeatCon(postData,data);
		}
	});
}
/**
 * 获取饼图数据
 */
function getSumPieCharts(){
	var postData = getSumQueryParam();
	$.ajax({
		url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/getSumPieCharts",
		dataType : 'json',
		data : postData,
		success : function(data) {
			loadPieCharts('#pieCharts',data.dataMap);
			loadPieCon(postData,data.conMap);
			loadPieTable("pieTable",data.dataMap);
		}
	});	
}
function loadPieTable(tableId,data){
	var htmlstr="<table>";
	htmlstr = htmlstr+"<tr><th>同一用户重复办理次数</th>" +
			"<th>用户数</th></tr>";
	htmlstr = htmlstr+"<tr>" +
	"<td>五次及以上</td>" +
	"<td>"+data.count_5+"</td></tr>";
	
	htmlstr = htmlstr+"<tr>" +
	"<td>四次</td>" +
	"<td>"+data.count_4+"</td></tr>";
	
	htmlstr = htmlstr+"<tr>" +
	"<td>三次</td>" +
	"<td>"+data.count_3+"</td></tr>";
	
	htmlstr = htmlstr+"<tr>" +
	"<td>二次</td>" +
	"<td>"+data.count_2+"</td></tr>";
	
	htmlstr = htmlstr+"<tr>" +
	"<td>一次</td>" +
	"<td>"+data.count_1+"</td></tr>";
	
	var sum=data.count_5+data.count_4+data.count_3+data.count_2+data.count_1;
	htmlstr = htmlstr+"<tr>" +
	"<td>合计</td>" +
	"<td>"+sum+"</td></tr>";
	
	htmlstr = htmlstr+"</table>";
	
	$("#"+tableId).html(htmlstr);
}
function loadPieCon(postData,data){
	var text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
		+"，"+$("#provinceName").val()+"办理终端类合约计划"+formatCurrency(data.SUM_TOL_IMEI,false)+"笔，其中重复办理"+formatCurrency(data.SUM_IMEI_NUM,false)+"笔。";
	$('#pieCon').html(text);
	
}
function loadPieCharts(Id,data){
	var pieData = [];
	pieData.push(['重复办理≥5次',data.count_5]);
	pieData.push(['重复4次',data.count_4]);
	pieData.push(['重复3次',data.count_3]);
	pieData.push(['重复2次',data.count_2]);
	pieData.push(['重复1次',data.count_1]);
	
	$(Id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
        	 formatter: function() {
                 return '<b>'+ this.point.name +'</b>: '+ 
                              Highcharts.numberFormat(this.y, 0) +'人';
              }
        },
        colors:['#0000FF','#1AE6E6','#FFFF00','#FFC977','#E9B28E'],
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
            name: "重复办理用户分布及数据表",
            innerSize:'50%',
            data: pieData
        }]
    });
}
/**
 * 构建审计结论
 * @param postData
 * @param data
 */
function loadSumRepeatCon(postData,data){
	var top3city = "";
	var top3sum  = 0;
	var provinceCode = $("#provinceCode").val();
	$.each(data.top3list, function(i, obj) {
		top3city = top3city+ obj.CMCC_PRVD_NM_SHORT+",";
		top3sum = top3sum+obj.IMEI_NUM;
	});
	var text = "";
	if(provinceCode == "10100" || provinceCode == "10400" || provinceCode == "10200" || provinceCode == "10300" ){
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)+"，"+$("#provinceName").val()
			+formatCurrency(data.sumMap.SUM_SUB_NUM,false)+"个用户重复办理终端类合约计划，涉及终端"+formatCurrency(data.sumMap.SUM_IMEI_NUM,false)+"台.";
	}else{
		text = timeToChinese(postData.hz_startMonth) + "-" + timeToChinese(postData.hz_endMonth)
			+"，"+$("#provinceName").val()+formatCurrency(data.sumMap.SUM_SUB_NUM,false)+"个用户重复办理终端类合约计划，涉及终端"
			+formatCurrency(data.sumMap.SUM_IMEI_NUM,false)+"台。重复办理合约计划终端数量排名前三的地市："+top3city+"涉及终端"+formatCurrency(top3sum,false)+"台";
	}
	
	$('#repeatCon').html(text);
	$('#sumTableCon').html(text);
}
/**
 * 画柱状图
 * @param id
 * @param data
 */
function loadSumRepeat(id,data){
	var city = [];
	var count1 = [];
	var count2 = [];
	var count3 = [];
	var count4 = [];
	var count5 = [];
	$.each(data, function(i, obj) {
		city.push(obj.CMCC_PRVD_NM_SHORT);
		count1.push(obj.count_1);
		count2.push(obj.count_2);
		count3.push(obj.count_3);
		count4.push(obj.count_4);
		count5.push(obj.count_5);
	});
	$('#'+id).highcharts({
		 chart: {
	            type: 'column'
	        },
	        title: {
	            text: ''
	        },
	        xAxis: {
	            categories: city
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '重复办理合约计划用户数'
	            }
	        },
	        legend: {
	            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
	            borderColor: '#CCC',
	            borderWidth: 1,
	            shadow: false
	        },
	        tooltip: {
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
            name: '重复办理1次',
            data: count1
        },{
            name: '重复办理2次',
            data: count2
        }, {
            name: '重复办理3次',
            data: count3
        }, {
            name: '重复办理4次',
            data: count4
        },{
        	name: '重复办理≥5次',
            data: count5
        }]
    });
}
/**
 * 构建数据表
 * @param tableId
 * @param pagerId
 */
function loadSumTable(tableId,pagerId) {
    var postData = getSumQueryParam();
    var tableColNames = [ '地市名称','同一用户重复办理次数','用户数'];
    var tableColModel = [];
    
    tableMap = new Object();
    tableMap.name = "CMCC_PRVD_NM_SHORT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "flag";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "usr_num";
    tableMap.align = "center";
    tableMap.formatter = function(cellvalue, options, rowObject) {
        return formatCurrency(cellvalue, false);
    };
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/getSumRepeatTable",
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
            $("#"+tableId).setGridWidth($(".tab-map-box").width() - 2);
            $("#"+pagerId).css("width", $("#"+pagerId).width() - 2);
        }
    });
}

/**
 * 绘制详情列表信息
 * @param tableId
 * @param pagerId
 */
function loadDetailTable(tableId,pagerId){
	var postData = getSumQueryParam();
 
    var tableColNames = [ '审计月','省名称','地市名称','营销案编码','营销案名称','员工标识','用户标识','重复办理营销案编码','重复办理营销案名称','重复办理时间','重复办理渠道标识','重复办理渠道名称','重复办理员工标识','产品类型名称'];
   
    var tableColModel = [];
    tableMap = new Object();
    tableMap.name = "AUD_TRM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SHORT_NAME";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "CMCC_PRVD_NM_SHORT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_ID2";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_NM2";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "EMP_ID2";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SUBS_ID2";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "OFFER_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SELL_DAT";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "SELL_CHNL_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "CHNL_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "EMP_ID";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "PROD_TYP_NM";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    $("#"+tableId+"Ajax").html("<table id='"+tableId+"'></table><div id='"+pagerId+"'></div>");
    
    jQuery("#"+tableId).jqGrid({
    	url : $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/getDetailListPager",
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
        	  $("#"+tableId).setGridWidth($(".tab-map-box").width() - 2);
              $("#"+pagerId).css("width", $("#"+pagerId).width() - 2);
              $("#"+tableId).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
              $("#"+tableId).setGridWidth($(".shuju_table").width() - 2);
        }
    });
}

function reLoadCityDetailGridData(tableId){
	  var postData = getDetailQueryParam();
	  var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/getDetailListPager";
	  jQuery("#"+tableId).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
/**
 * 清空隐藏表单值
 * 
 */
function clearHideFormInput() {
    $("#mx_yxaFlag").val("");
    $("#mx_cityCode").val("");
    $("#mx_startMonth").val($.fn.timeStyle($("#beforeAcctMonth").val()));
    $("#mx_endMonth").val($.fn.timeStyle($("#endAcctMonth").val()));
}

/**
 * 加载地市字段列表
 * 
 */
function loadCityList() {
    var postData = getDetailQueryParam();
    
    $.ajax({
        url : $.fn.cmwaurl() + "/yjk/2001/getCityList",
        dataType : 'json',
        data : postData,    
        success : function(data) {
            $.each(data.body.ctyList, function(i, map) {
                var li = "<li code='{0}'>{1}</li>".format(map.CMCC_prvd_cd, map.CMCC_prvd_nm_short);
                $("#mx_cityCodeSelect").append(li);
            });
            addSelectEvent("mx_cityCode");
        }
    });
}


/**
 * 为指定ID添加下拉框监听事件
 * 
 * @param id
 */
function addSelectEvent(id, callback) {
	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
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
 * 为指定ID添加日历(月)组件监听事件
 * 
 * @param id
 */
function addMonthCalendarEvent(id, callback) {
    $('#' + id).datetimepicker({
        format: 'yyyy-mm',
        weekStart: 1,
        autoclose: true,
        startView: 3,
        minView: 3,
        forceParse: false,
        language: 'zh'
    }).on('changeDate', function(ev) {
        if ("undefined" != typeof(callback) && typeof(callback) == "function") {
            callback();
        }
    });
}

/**
 * 导出
 */
function exportTab(functionName){
    var hz_rankType = parseInt($("#hz_rankType").val());
    var hz_startMonth = $("#hz_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#hz_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/" +functionName+
	"?hz_rankType="+hz_rankType +
	"&hz_startMonth="+hz_startMonth +
	"&hz_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}

/**
 * 导出
 */
function exportDetailTab(functionName){
    var hz_startMonth = $("#mx_startMonth").val().replaceAll("-", "");
    var hz_endMonth = $("#mx_endMonth").val().replaceAll("-", "");
    var provId = $("#provinceCode").val();
    var mx_cityCode = $("#mx_cityCode").val();
	var form = $("<form id='filedownload' style='display:none' target='' method='post' />");
	var url = $.fn.cmwaurl() + "/hyzdbthgx_jy/1702/" +functionName+
	"?mx_cityCode="+mx_cityCode +
	"&mx_startMonth="+hz_startMonth +
	"&mx_endMonth="+hz_endMonth +
	"&provId="+provId ;
	form.attr('action', url);
	$('body').append(form);
	form.submit();
	form.remove();
}
