/**
 * 个性化本页面的特殊风格
 * 绑定本页面元素的响应时间
 * 获取默认首次加载本页面的初始化参数，并给隐藏form赋值
 * 触发页面默认加载函数
 */
$(document).ready(function() {
	initStyle();
	initEvent();
	initDefaultParams();
	initDefaultData();
});

//自己的风格
function initStyle(){
	// 初始化图形组件大小等于盒子大小（普通）
	/*$("#qfboqszhsId").css({width: $(".tab-map-info").width() - 20, height: 335});
    $("#qfboqsjeId").css({width: $(".tab-map-info").width() - 20, height: 335});
    $("#zlzhsId").css({width: $("#zlzhsId").parent().parent().parent().width() - 20, height: 315});
    $("#zljeId").css({width: $("#zljeId").parent().parent().parent().width() - 20, height: 315});
    $("#xkzhanglingzhsId").css({width: $("#xkzhanglingzhsId").parent().parent().parent().width() - 20, height: 315});*/
    $("#xkzhanglingjeId").css({width: $("#xkzhanglingjeId").parent().parent().parent().width() - 20, height: 315});
}

//响应事件
function initEvent(){
	//导出汇总数据表
	$("#xkzhanglingexport").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		var totalNum = $("#xkzhanglingGridData").getGridParam("records");
		
		if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
			return;
		}
		
		window.location.href = $.fn.cmwaurl() + "/grqfxkfx/exportxkZhangLingList?" + $.param(getSumQueryParam());
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
        	 $("#currDetBeginDate").val(beforeAcctMonth);
             $("#currDetEndDate").val(endAcctMonth);
        }
    	detMxTab();
    });
    $("#hz_tab").click(function() {
    	insertCodeFun("MAS_hp_cmwa_hzmx_top_tab_01"); 

    	insertCodeFun("MAS_hp_cmwa_hzmx_search_02");
    	$("#currTab").val("hz");
    });
	//导出清单列表数据
	$("#exportList").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_table_download_03");

		
		var totalNum = $("#cityDetailGridData").getGridParam("records");
        
        if (!confirm("有{0}条数据，是否要导出？".format(totalNum))) {
            return;
        }
        var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		if(detEndDate >= detBeginDate){
			$("#currDetBeginDate").val(detBeginDate);
			$("#currDetEndDate").val(detEndDate);
		}else{
			alert("开始时间不能大于结束时间");
			return false;
		}
		//地市名称
		var  cityType= $("#cityType li.active").attr("date");
		$("#currCityType").val(cityType);
		
		//超约定缴费期限后仍欠费月数（X个月 - X个月）
		var  minOutOfMon= $("#beginOutOfMon").val();
		var  maxOutOfMon= $("#endOutOfMon").val();
		$("#minOutOfMon").val(minOutOfMon);
		$("#maxOutOfMon").val(maxOutOfMon);
		if(minOutOfMon != "" && minOutOfMon != null && maxOutOfMon != "" && maxOutOfMon != null){
			if(minOutOfMon<=maxOutOfMon){
				$("#minOutOfMon").val(minOutOfMon);
				$("#maxOutOfMon").val(maxOutOfMon);
			}else{
				alert("最小超约定缴费期限后仍欠费月数不能大于最大超约定缴费期限后仍欠费月数");
				return false;
			}
		}else{
			$("#minOutOfMon").val(minOutOfMon);
			$("#maxOutOfMon").val(maxOutOfMon);
		}
		//欠费金额（X元 - X元）
		/*var  minDbtAmt= $("#beginDbtAmt").val();
		var  maxDbtAmt= $("#endDbtAmt").val();
		if(minDbtAmt != "" && minDbtAmt != null && maxDbtAmt != "" && maxDbtAmt != null){
			if(minDbtAmt<=maxDbtAmt){
				$("#minDbtAmt").val(minDbtAmt);
				$("#maxDbtAmt").val(maxDbtAmt);
			}else{
				alert("最小欠费金额不能大于最大欠费金额");
				return false;
			}
		}else{
			$("#minDbtAmt").val(minDbtAmt);
			$("#maxDbtAmt").val(maxDbtAmt);
		}*/
		reLoadGridData("#cityDetailGridData","/grqfxkfx/getDetList");
		window.location.href = $.fn.cmwaurl() + "/grqfxkfx/exprotDetList?" + $.param(getDetQueryParam());
	});
	
	//汇总查询
	$("#hzfxclick").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var sumBeginDate= $("#sumBeginDate").val().replaceAll("-", "");
		var sumEndDate = $("#sumEndDate").val().replaceAll("-", "");
		if(sumBeginDate>sumEndDate){
			alert("开始时间不能大于结束时间");
			return false;
		}
		$("#currSumBeginDate").val(sumBeginDate);
		$("#currSumEndDate").val(sumEndDate);
		sumClick();
	});

	//清单查询
	$("#queryButton").click(function() {
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		var  detBeginDate= $("#detBeginDate").val().replaceAll("-", "");
		var  detEndDate= $("#detEndDate").val().replaceAll("-", "");
		if(detEndDate >= detBeginDate){
			$("#currDetBeginDate").val(detBeginDate);
			$("#currDetEndDate").val(detEndDate);
		}else{
			alert("开始时间不能大于结束时间");
			return false;
		}
		//地市名称
		var  cityType= $("#cityType li.active").attr("date");
		$("#currCityType").val(cityType);
		
		//超约定缴费期限后仍欠费月数（X个月 - X个月）
		var  minOutOfMon= $("#beginOutOfMon").val();
		var  maxOutOfMon= $("#endOutOfMon").val();
		$("#minOutOfMon").val(minOutOfMon);
		$("#maxOutOfMon").val(maxOutOfMon);
		if(minOutOfMon != "" && minOutOfMon != null && maxOutOfMon != "" && maxOutOfMon != null){
			if(minOutOfMon<=maxOutOfMon){
				$("#minOutOfMon").val(minOutOfMon);
				$("#maxOutOfMon").val(maxOutOfMon);
			}else{
				alert("最小超约定缴费期限后仍欠费月数不能大于最大超约定缴费期限后仍欠费月数");
				return false;
			}
		}else{
			$("#minOutOfMon").val(minOutOfMon);
			$("#maxOutOfMon").val(maxOutOfMon);
		}

		reLoadGridData("#cityDetailGridData","/grqfxkfx/getDetList");
	});
	
	$(".tab-box .tab-info .tab-sub-nav ul li").unbind("click");
    
    $(".tab-sub-nav ul li a").click(function(event) {
    	insertCodeFun("MAS_hp_cmwa_hzmx_tab_01");

        event.preventDefault();
        var currTab = $("#currTab").val();
        window.location.href = $(this).attr("href") + "&tab=" + currTab;
    });
	
	$("#resetMxId").click(function(){
		var initBeginDate = $("#initBeginDate").val();
		var initEndDate = $("#initEndDate").val();
		$("#detBeginDate").val($.fn.timeStyle(initBeginDate));
		$("#detEndDate").val($.fn.timeStyle(initEndDate));
		
		$("#currCityType").val("");
		$("#beginOutOfMon").val("");
		$("#endOutOfMon").val("");
	});
	
	$("#xkzhanglingsjbId").click(function(){
		insertCodeFun("MAS_hp_cmwa_hzmx_search_02");

		xkzhanglingTab();
	});
}

//页面调用方法
function initDefaultData(){
	xktongjiTab();
	xkzhanglingpei("#xkzhanglingzhsId","账户数分布","/grqfxkfx/getxkzhanglingzhs");
	xkzhanglingpei("#xkzhanglingjeId","欠费金额分布","/grqfxkfx/getxkzhanglingje");
	xinkongjielun();
	xkzhanglingjielun();
}

//汇总查询调用
function sumClick(){
	reLoadSumGridData("#xinkongGridData","/grqfxkfx/getXinkongPagerList");
	xkzhanglingpei("#xkzhanglingzhsId","账户数分布","/grqfxkfx/getxkzhanglingzhs");
	xkzhanglingpei("#xkzhanglingjeId","欠费金额分布","/grqfxkfx/getxkzhanglingje");
	xinkongjielun();
	xkzhanglingjielun();
}


//信控等级 审计结论
function xinkongjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#xinkongsjbId").html("截止至"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有0个账户有欠费数据，其中已匹配信控等级的账户有0个，占全部欠费账户的0%，另有0个账户未能匹配到信控等级。");
	$.ajax({
		url :$.fn.cmwaurl() + "/grqfxkfx/getXinkongjielun",
		dataType : "json",
		data : postData,
		success : function(backdata) {
			var xinkongStr ="截止至"+ timeToChinese(currSumBeginDate)+"-"+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户共有"+backdata.orgAcctQfNum+"个账户有欠费数据，其中已匹配信控等级的账户有"+(backdata.orgAcctQfNum-backdata.noLvlNum)+"个，占全部欠费账户的"+changeTwoDecimal((backdata.orgAcctQfNum-backdata.noLvlNum)/backdata.orgAcctQfNum*100)+"%，另有"+backdata.noLvlNum+"个账户未能匹配到信控等级。";
			$("#xinkongsjbId").html(xinkongStr);
		}
	});
}
//欠费规模分布审计结论
function xkzhanglingjielun(){
	var postData = getSumQueryParam();
	var provinceCode = $('#provinceCode').val();
	var currSumBeginDate = $('#currSumBeginDate').val();
	var currSumEndDate = $('#currSumEndDate').val();
	$("#xkzhanglingzhsjielunId").html(timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账0元，涉及账户0个。其中，欠费超透支额度12个月后仍出账0元，涉及账户0个，占所有欠费个人账户的0%。");
	$("#xkzhanglingjejielunId").html(timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账0元，涉及账户0个。其中，欠费超透支额度12个月后仍出账0元，占全部个人欠费金额0%，涉及账户0个。");
	$("#xkzhanglingsjbjielunId").html(timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账0元，涉及账户0个。其中，欠费超透支额度12个月后仍出账0元，涉及账户0个。");
	var xkzhanglingzhs="";
	var xkzhanglingje="";
	var xkzhanglingsjb ="";
	$.ajax({
		url :$.fn.cmwaurl() + "/grqfxkfx/getxkzhanglingjielun",
		dataType : "json",
		async:false,
		data : postData,
		success : function(backdata) {
			xkzhanglingsjb =timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及账户"+backdata.acctNum+"个。其中，欠费超透支额度12个月后仍出账"+changeTwoDecimal(backdata.moreThanOneYearDbtAmt)+"元，涉及账户"+backdata.moreThanOneYearAcctNum+"个。";
			xkzhanglingzhs = timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及账户"+backdata.acctNum+"个。其中，欠费超透支额度12个月后仍出账"+changeTwoDecimal(backdata.moreThanOneYearDbtAmt)+"元，涉及账户"+backdata.moreThanOneYearAcctNum+"个，占所有欠费个人账户的"+changeTwoDecimal(backdata.moreThanOneYearAcctNum/backdata.acctNum*100)+"%。";
			if(backdata.dbtAmt==0){
				xkzhanglingje =timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及账户"+backdata.acctNum+"个。其中，欠费超透支额度12个月后仍出账"+changeTwoDecimal(backdata.moreThanOneYearDbtAmt)+"元，占全部个人欠费金额0.00%，涉及账户"+backdata.moreThanOneYearAcctNum+"个。";
			}else{
				xkzhanglingje =timeToChinese(currSumBeginDate)+" - "+timeToChinese(currSumEndDate)+"，"+provinceName(provinceCode)+"个人客户欠费超透支额度后仍出账"+changeTwoDecimal(backdata.dbtAmt)+"元，涉及账户"+backdata.acctNum+"个。其中，欠费超透支额度12个月后仍出账"+changeTwoDecimal(backdata.moreThanOneYearDbtAmt)+"元，占全部个人欠费金额"+changeTwoDecimal(backdata.moreThanOneYearDbtAmt/backdata.dbtAmt*100)+"%，涉及账户"+backdata.moreThanOneYearAcctNum+"个。";
			}
			$("#xkzhanglingzhsjielunId").html(xkzhanglingzhs);
			$("#xkzhanglingjejielunId").html(xkzhanglingje);
			$("#xkzhanglingsjbjielunId").html(xkzhanglingsjb);
			
		}
	});
}
//加载波动趋势图
function xkzhanglingpei(Id,name,url){
	var postData = getSumQueryParam();
	$.ajax({
		url :$.fn.cmwaurl() + url,
		dataType : "json",
		data : postData,
		success : function(backdata) {
            // 设置x,y轴的值
			if(backdata.numPer1 != null){
				var data =[["1至3个月",backdata.numPer1],["4至6个月",backdata.numPer2],["7至12个月",backdata.numPer3],
				           ["13至18个月",backdata.numPer4],["19至24个月",backdata.numPer5],["25个月以上",backdata.numPer6]];
			}else{
				var data =[["1至3个月",backdata.amtPer1],["4至6个月",backdata.amtPer2],["7至12个月",backdata.amtPer3],
				           ["13至18个月",backdata.amtPer4],["19至24个月",backdata.amtPer5],["25个月以上",backdata.amtPer6]];
				
			}
            peiHighChart(Id,name,data);
		}
	});
}

//信控等级汇总统计 数据表
function xktongjiTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = ['信控等级编码','信控等级名称','透支额度(元)','个人账户数','其中欠费个人账户数'];
    var tableColModel = [];
    
	tableMap = new Object();
    tableMap.name = "crLvlCd";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "crLvlNm";
    tableMap.align = "left";
    tableColModel.push(tableMap);

    tableMap = new Object();
    tableMap.name = "outOfDbtAmt";
    tableMap.align = "right";
    tableMap.formatter = function(cellvalue, options, rowObject) {
    	return formatCurrency(cellvalue, true);
    };
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "tolAcctNum";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    tableMap = new Object();
    tableMap.name = "orgAcctQfNum";
    tableMap.align = "center";
    tableColModel.push(tableMap);
    
    loadsjbTab(postData, tableColNames, tableColModel, "#xinkongGridData", "#xinkongGridDataPageBar", "/grqfxkfx/getXinkongPagerList");
}
//超透支额度后欠费账龄分布 数据表
function xkzhanglingTab(){
	var postData = this.getSumQueryParam();
	var tableColNames = ['超透支额度后仍欠费月数','欠费账户数','欠费超透支额度后在审计期间内仍出账金额（元）'];
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "outOfMon";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctNum";
	tableMap.align = "left";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);
	
	loadsjbTab(postData, tableColNames, tableColModel, "#xkzhanglingGridData", "#xkzhanglingGridDataPageBar", "/grqfxkfx/getxkzhanglingPagerList");
}


$(window).resize(function(){
	$("#xinkongGridData").setGridWidth($(".tab-map-info").width()-1);
});
$(window).resize(function(){
	$("#xkzhanglingGridData").setGridWidth($(".tab-map-info").width()-1);
});
$(window).resize(function(){
	$("#cityDetailGridData").setGridWidth($(".shuju_table").width());
});

//加载明细 数据表
function detMxTab(){
	var postData = this.getDetQueryParam();
	var tableColNames = [ '审计月', '地市', '欠费账期','最早欠费账期','超透支额度欠费月','超透支额度欠费月数','欠费用户标识','手机号码','欠费客户标识','欠费帐户标识','信控等级编码','信控等级名称','透支额度','用户状态','欠费金额','最后欠费月套餐'];
	
	var tableColModel = [];
	
	tableMap = new Object();
	tableMap.name = "audTrm";
	tableMap.align = "center";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "cmccPrvdNmShort";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "acctPrdYtm";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "minACCTPrdYtm";
	tableMap.align = "right";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "outOfCrDat";
	tableMap.align = "right";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "outOfMon";
	tableMap.align = "right";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "subsId";
	tableMap.align = "right";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "MSISDN";
	tableMap.align = "right";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "bltoCustId";
	tableMap.align = "right";
	tableColModel.push(tableMap);


	tableMap = new Object();
	tableMap.name = "acctId";
	tableMap.align = "right";
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "crLvlCd";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "crLvlNm";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "outOfDbtAmt";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "subsStatTypNm";
	tableMap.align = "right";
	tableColModel.push(tableMap);
	
	tableMap = new Object();
	tableMap.name = "dbtAmt";
	tableMap.align = "right";
	tableMap.formatter = function(cellvalue, options, rowObject) {
		return formatCurrency(cellvalue, true);
	};
	tableColModel.push(tableMap);

	tableMap = new Object();
	tableMap.name = "basicPackId";
	tableMap.align = "right";
	
	tableColModel.push(tableMap);
	
	loadMxTab(postData, tableColNames, tableColModel, "#cityDetailGridData", "#cityDetailGridDataPageBar", "/grqfxkfx/getDetList");
}

//饼状图
function peiHighChart(Id,name,data){
	$(Id).highcharts({
        title: {
            text: ''
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        colors: ['#666666','#000088', '#00ff00', '#0000ff', '#ffff00', '#ff0000'],
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
            name: name,
            innerSize:'50%',
            data: data
        }]
    });
}

//数据表
function loadsjbTab(postData,tableColNames,tableColModel,tabId,pageId,url){
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
        rowNum: 5,
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
            $(tabId).setGridWidth($(tabId).parent().parent().parent().width() - 2);
            $(pageId).css("width", $(pageId).width() - 2);
        }
    });
}
//数据表
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
			$(pageId).css("width", $(pageId).width() - 2);
			$(tabId).setGridWidth($(".tab-nav").width()-40);
		}
	});
}

function reLoadGridData(id,url) {
    var postData = getDetQueryParam();
    var url = $.fn.cmwaurl() + url;
    $(id).jqGrid('clearGridData');
    jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}
function reLoadSumGridData(id,url) {
	var postData = getSumQueryParam();
	var url = $.fn.cmwaurl() + url;
	$(id).jqGrid('clearGridData');
	jQuery(id).jqGrid('setGridParam', {url: url, postData: postData, page: 1}).trigger("reloadGrid");
}

//汇总数据查询条件
function getSumQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currSumBeginDate = $('#currSumBeginDate').val();
	postData.currSumEndDate = $('#currSumEndDate').val();
	return postData;
}

//清单数据查询条件
function getDetQueryParam(){
	var postData = {};
	postData.provinceCode = $('#provinceCode').val();
	postData.currDetBeginDate = $('#currDetBeginDate').val();
	postData.currDetEndDate = $('#currDetEndDate').val();
	postData.currCityType = $('#currCityType').val();
	//超透支额度后仍欠费月数（X个月 - X个月）
	postData.minOutOfMon =  $("#minOutOfMon").val();
	postData.maxOutOfMon =  $("#maxOutOfMon").val();
	return postData;
}

//初始化数据
function initDefaultParams() {
	var postData = {};
	var beforeAcctMonth = $.fn.GetQueryString("beforeAcctMonth");
    var endAcctMonth = $.fn.GetQueryString("endAcctMonth");
    var provinceCode = $.fn.GetQueryString("provinceCode");
    var auditId = $.fn.GetQueryString("auditId");
    var tab = $.fn.GetQueryString("tab");
    var urlParams = window.location.search.replaceAll("&tab=mx", "").replaceAll("&tab=hz", "");
    $(".tab-sub-nav ul li a").each(function() {
        var link = $(this).attr("href") + urlParams;
        $(this).attr("href", link);
    });

    if (tab == "mx") {
    	$("#currTab").val("mx");
        $("#mx_tab").click();
    }
	$.ajax({
		url : $.fn.cmwaurl() + "/grqfxkfx/initDefaultParams",
		async : false,
		dataType : 'json',
		data : postData,
		success : function(data) {
			
			/*国信数据*/
			$('#provinceCode').val(provinceCode);
			$('#beforeAcctMonth').val(beforeAcctMonth);    
			$('#endAcctMonth').val(endAcctMonth);
			$('#auditId').val(auditId);
			//$('#taskCode').val(data['taskCode']);
			/*汇总数据*/
			$('#currSumBeginDate').val(beforeAcctMonth);
			$('#currSumEndDate').val(endAcctMonth);
			//$('#provinceName').val(data['provinceName']);
			/*清单数据*/
			$('#currDetBeginDate').val(beforeAcctMonth);
			$('#currDetEndDate').val(endAcctMonth);
			/*$('#currCityType').val(data['currCityType']);*/
			initCityList("#cityType", data['currCityType']);
			
			/*汇总、清单页面 时间input 填充值*/
			$('#sumBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#sumEndDate').val($.fn.timeStyle(endAcctMonth));
			$('#detBeginDate').val($.fn.timeStyle(beforeAcctMonth));
			$('#detEndDate').val($.fn.timeStyle(endAcctMonth));

			/**/
			$('#initBeginDate').val(beforeAcctMonth);
			$('#initEndDate').val(endAcctMonth);
			
			$('.form_datetime').datetimepicker('setStartDate',beforeAcctMonth.substring(0, 4)+"-"+beforeAcctMonth.substring(4, 6));
			$('.form_datetime').datetimepicker('setEndDate',endAcctMonth.substring(0, 4)+"-"+endAcctMonth.substring(4, 6));
		}
	});
}

//初始化地市列表
function initCityList(idStr, data) {
	var len = data.length;
	var liStr = "";
	if (len != 0) {
		for ( var i = 0; i < len; i++) {
			liStr += "<li date='" + data[i].CMCC_prvd_cd + "'>"
					+ data[i].CMCC_prvd_nm_short + "</li>";
		}
	}
	if (len == 0) {
		liStr += "<li>暂无数据</li>";
	}
	$(idStr).html(liStr);
}